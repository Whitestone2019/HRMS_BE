package com.whitestone.hrms.controller;

import com.whitestone.entity.WsslCalendarMod;
import com.whitestone.hrms.repo.WsslCalendarModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CalendarController {

    @Autowired
    private WsslCalendarModRepository calendarRepository;

    // Create new calendar event - Accept Map instead of WsslCalendarMod
    @PostMapping("/calendar")
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody Map<String, Object> requestMap) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Extract fields
            String eventName = (String) requestMap.get("eventName");
            String eventDateStr = (String) requestMap.get("eventDate");
            String eventType = (String) requestMap.get("eventType");
            String description = (String) requestMap.get("description");
            String isPublic = (String) requestMap.get("isPublic");
            String createdBy = (String) requestMap.get("createdBy");
            
            // Validate required fields
            if (eventName == null || eventName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Event name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (eventDateStr == null || eventDateStr.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Event date is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Parse date from yyyy-MM-dd format (from HTML date input)
            Date eventDate;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date parsedDate = sdf.parse(eventDateStr);
                eventDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                response.put("success", false);
                response.put("message", "Invalid date format. Use yyyy-MM-dd format");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if event already exists for this date (SIMPLIFIED CHECK)
            List<WsslCalendarMod> existingEvents = calendarRepository.findByEventDate(eventDate);
            if (!existingEvents.isEmpty()) {
                // Get the first existing event to show details
                WsslCalendarMod existingEvent = existingEvents.get(0);
                response.put("success", false);
                response.put("message", "A holiday already exists for date " + eventDateStr + 
                    " (" + existingEvent.getEventName() + "). Please choose a different date.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create calendar event object
            WsslCalendarMod calendarEvent = new WsslCalendarMod();
            
            // Set all required fields from entity
            calendarEvent.setEventName(eventName);
            calendarEvent.setEventDate(eventDate);
            
            // EVENT_TYPE is NOT NULL in your entity, so set it
            if (eventType != null && !eventType.trim().isEmpty()) {
                calendarEvent.setEventType(eventType);
            } else {
                calendarEvent.setEventType("Holiday"); // Default value
            }
            
            // Set optional fields
            if (description != null && !description.trim().isEmpty()) {
                calendarEvent.setDescription(description);
            }
            
            // IS_PUBLIC - default to "Y" for holidays
            if (isPublic != null && !isPublic.trim().isEmpty()) {
                calendarEvent.setIsPublic(isPublic);
            } else {
                calendarEvent.setIsPublic("Y");
            }
            
            // CREATED_BY - required field
            if (createdBy != null && !createdBy.trim().isEmpty()) {
                calendarEvent.setCreatedBy(createdBy);
            } else {
                calendarEvent.setCreatedBy("SYSTEM");
            }
            
            // CREATED_DATE - required field, set current timestamp
            calendarEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            
            // Save the entity
            WsslCalendarMod savedEvent = calendarRepository.save(calendarEvent);
            
            response.put("success", true);
            response.put("message", "Event created successfully");
            response.put("data", savedEvent);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error creating event: " + e.getMessage());
            errorResponse.put("errorDetails", e.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update event - FIXED to handle duplicate check
    @PutMapping("/update/calendar/{id}")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable("id") Long id, 
                                                          @RequestBody Map<String, Object> requestMap) {
        try {
            Map<String, Object> response = new HashMap<>();
            Optional<WsslCalendarMod> eventData = calendarRepository.findById(id);
            
            if (eventData.isPresent()) {
                WsslCalendarMod existingEvent = eventData.get();
                
                Date newEventDate = existingEvent.getEventDate(); // Default to existing date
                
                // Update event date if provided
                if (requestMap.containsKey("eventDate")) {
                    String eventDateStr = (String) requestMap.get("eventDate");
                    if (eventDateStr != null && !eventDateStr.trim().isEmpty()) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.setLenient(false);
                            java.util.Date parsedDate = sdf.parse(eventDateStr);
                            newEventDate = new Date(parsedDate.getTime());
                        } catch (ParseException e) {
                            response.put("success", false);
                            response.put("message", "Invalid date format. Use yyyy-MM-dd format");
                            return ResponseEntity.badRequest().body(response);
                        }
                    }
                }
                
                // Check if the new date already has an event (excluding the current event being updated)
                if (!newEventDate.equals(existingEvent.getEventDate())) {
                    List<WsslCalendarMod> existingEvents = calendarRepository.findByEventDate(newEventDate);
                    if (!existingEvents.isEmpty()) {
                        // Check if it's not the same event
                        boolean isDifferentEvent = existingEvents.stream()
                            .anyMatch(event -> !event.getCalenderId().equals(id));
                        
                        if (isDifferentEvent) {
                            WsslCalendarMod conflictingEvent = existingEvents.get(0);
                            response.put("success", false);
                            response.put("message", "A holiday already exists for date " + 
                                new SimpleDateFormat("dd-MM-yyyy").format(newEventDate) + 
                                " (" + conflictingEvent.getEventName() + "). Please choose a different date.");
                            return ResponseEntity.badRequest().body(response);
                        }
                    }
                }
                
                // Update event name if provided
                if (requestMap.containsKey("eventName")) {
                    String eventName = (String) requestMap.get("eventName");
                    if (eventName != null && !eventName.trim().isEmpty()) {
                        existingEvent.setEventName(eventName);
                    }
                }
                
                // Update event date
                existingEvent.setEventDate(newEventDate);
                
                // Update event type if provided
                if (requestMap.containsKey("eventType")) {
                    String eventType = (String) requestMap.get("eventType");
                    if (eventType != null && !eventType.trim().isEmpty()) {
                        existingEvent.setEventType(eventType);
                    }
                }
                
                // Update description
                if (requestMap.containsKey("description")) {
                    existingEvent.setDescription((String) requestMap.get("description"));
                }
                
                // Update isPublic
                if (requestMap.containsKey("isPublic")) {
                    String isPublic = (String) requestMap.get("isPublic");
                    if (isPublic != null && !isPublic.trim().isEmpty()) {
                        existingEvent.setIsPublic(isPublic);
                    }
                }
                
                // Set updated by
                String updatedBy = "SYSTEM";
                if (requestMap.containsKey("updatedBy")) {
                    String tempUpdatedBy = (String) requestMap.get("updatedBy");
                    if (tempUpdatedBy != null && !tempUpdatedBy.trim().isEmpty()) {
                        updatedBy = tempUpdatedBy;
                    }
                }
                existingEvent.setUpdatedBy(updatedBy);
                
                // Set update timestamp
                existingEvent.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                
                WsslCalendarMod updatedEvent = calendarRepository.save(existingEvent);
                
                response.put("success", true);
                response.put("message", "Event updated successfully");
                response.put("data", updatedEvent);
                return new ResponseEntity<>(response, HttpStatus.OK);
                
            } else {
                response.put("success", false);
                response.put("message", "Event not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error updating event: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Rest of your methods remain the same...
    // Get all events
    @GetMapping("/getall/calendar")
    public ResponseEntity<Map<String, Object>> getAllEvents() {
        try {
            Map<String, Object> response = new HashMap<>();
            List<WsslCalendarMod> events = calendarRepository.findAll();
            
            response.put("success", true);
            response.put("data", events);
            response.put("count", events.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching events: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get event by ID
    @GetMapping("/calendar/{id}")
    public ResponseEntity<Map<String, Object>> getEventById(@PathVariable("id") Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            Optional<WsslCalendarMod> eventData = calendarRepository.findById(id);
            
            if (eventData.isPresent()) {
                response.put("success", true);
                response.put("data", eventData.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "Event not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching event: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete event
    @DeleteMapping("/delete/calendar/{id}")
    public ResponseEntity<Map<String, Object>> deleteEvent(@PathVariable("id") Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            if (calendarRepository.existsById(id)) {
                calendarRepository.deleteById(id);
                response.put("success", true);
                response.put("message", "Event deleted successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "Event not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error deleting event: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get upcoming events
    @GetMapping("/calendar/upcoming")
    public ResponseEntity<Map<String, Object>> getUpcomingEvents() {
        try {
            Map<String, Object> response = new HashMap<>();
            Date currentDate = new Date(System.currentTimeMillis());
            List<WsslCalendarMod> events = calendarRepository.findByEventDateAfter(currentDate);
            
            response.put("success", true);
            response.put("data", events);
            response.put("count", events.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching upcoming events: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get events between dates
    @GetMapping("/calendar/between")
    public ResponseEntity<Map<String, Object>> getEventsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            if (startDate.after(endDate)) {
                response.put("success", false);
                response.put("message", "Start date cannot be after end date");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<WsslCalendarMod> events = calendarRepository.findByEventDateBetween(startDate, endDate);
            
            response.put("success", true);
            response.put("data", events);
            response.put("count", events.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching events: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get events by month
    @GetMapping("/calendar/month/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getEventsByMonth(
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Validate month
            if (month < 1 || month > 12) {
                response.put("success", false);
                response.put("message", "Invalid month. Must be between 1 and 12");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create start and end dates for the month
            String startDateStr = String.format("%d-%02d-01", year, month);
            Date startDate = Date.valueOf(startDateStr);
            
            // Calculate last day of month
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month - 1, 1);
            int lastDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            String endDateStr = String.format("%d-%02d-%02d", year, month, lastDay);
            Date endDate = Date.valueOf(endDateStr);
            
            List<WsslCalendarMod> events = calendarRepository.findByEventDateBetween(startDate, endDate);
            
            response.put("success", true);
            response.put("data", events);
            response.put("count", events.size());
            response.put("month", month);
            response.put("year", year);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching monthly events: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get public events
    @GetMapping("/calendar/public")
    public ResponseEntity<Map<String, Object>> getPublicEvents() {
        try {
            Map<String, Object> response = new HashMap<>();
            List<WsslCalendarMod> allEvents = calendarRepository.findAll();
            
            // Filter public events
            List<WsslCalendarMod> publicEvents = allEvents.stream()
                    .filter(event -> "Y".equalsIgnoreCase(event.getIsPublic()))
                    .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("data", publicEvents);
            response.put("count", publicEvents.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching public events: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}