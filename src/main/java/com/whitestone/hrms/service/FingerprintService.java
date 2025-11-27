package com.whitestone.hrms.service;

import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.MorfinAuth_Callback;
import com.mantra.morfinauth.enums.DeviceDetection;
import com.mantra.morfinauth.enums.DeviceModel;
import com.mantra.morfinauth.enums.TemplateFormat;
import com.mantra.morfinauth.enums.FingerPostion;

@Service
public class FingerprintService implements MorfinAuth_Callback {

    private MorfinAuth auth;

    @PostConstruct
    public void init() {
        auth = new MorfinAuth(this);
    }

    @Override
    public void OnDeviceDetection(String deviceName, DeviceDetection detection) {
        System.out.println("Device: " + deviceName + " => " + detection);
    }

    @Override
    public void OnPreview(int errorCode, int quality, byte[] image) {
        System.out.println("Preview Q=" + quality);
    }

    @Override
    public void OnComplete(int errorCode, int quality, int nfiq) {
        System.out.println("Completed Q=" + quality + ", NFIQ=" + nfiq);
    }

    @Override
    public void OnFingerPostionDetection(int errorCode, FingerPostion fingerPosition) {
        // This is the required method from the SDK
        System.out.println("Finger position: " + fingerPosition + ", error=" + errorCode);
    }

    public String captureFinger() throws Exception {

        DeviceInfo deviceInfo = new DeviceInfo();

        byte[] clientKey = "YOUR_CLIENT_KEY".getBytes();

        int init = auth.Init(DeviceModel.MFS500, clientKey, deviceInfo);

        if (init != 0) {
            throw new RuntimeException("Init failed: " + auth.GetErrorMessage(init));
        }

        int cap = auth.StartCapture(50, 10000);

        if (cap != 0) {
            throw new RuntimeException("Capture error: " + auth.GetErrorMessage(cap));
        }

        Thread.sleep(3000);

        byte[] template = new byte[2000];
        int[] len = new int[1];

        int tempRes = auth.GetTemplate(template, len, TemplateFormat.FMR_V2011);

        if (tempRes != 0) {
            throw new RuntimeException("Template error: " + auth.GetErrorMessage(tempRes));
        }

        return Base64.getEncoder().encodeToString(java.util.Arrays.copyOf(template, len[0]));
    }
}
