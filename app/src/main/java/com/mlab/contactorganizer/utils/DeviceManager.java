package com.mlab.contactorganizer.utils;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import org.apache.commons.lang3.StringUtils;

public class DeviceManager {

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return StringUtils.capitalize(model);
        } else {
            return StringUtils.capitalize(manufacturer) + " " + model;
        }
    }

    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }

    //TODO get Imei
    // https://stackoverflow.com/questions/1972381/how-to-get-the-devices-imei-esn-programmatically-in-android
}
