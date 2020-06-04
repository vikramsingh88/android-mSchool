package com.vikram.school.ui.addfee.printer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.vikram.school.utility.Constants;

import java.util.HashMap;
import java.util.Iterator;

public class USBPrinter {
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private PendingIntent mPermissionIntent;
    EditText ed_txt;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static Boolean forceCLaim = true;
    private String TAG = "USBPrinter";

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;
    byte[] testBytes;
    String usbDevice = "";
    byte [] dataToPrint;

    void initAndPrint(Context context, byte [] dataToPrint) {
        this.dataToPrint = dataToPrint;
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mDeviceList = mUsbManager.getDeviceList();
        if (mDeviceList != null && mDeviceList.size() > 0) {
            mDeviceIterator = mDeviceList.values().iterator();
            Log.d(Constants.TAG, TAG+" Device list : "+mDeviceList.size());
            while (mDeviceIterator.hasNext()) {
                UsbDevice usbDevice1 = mDeviceIterator.next();
                usbDevice += "\n" +
                        "DeviceID: " + usbDevice1.getDeviceId() + "\n" +
                        "DeviceName: " + usbDevice1.getDeviceName() + "\n" +
                        "Protocol: " + usbDevice1.getDeviceProtocol() + "\n" +
                        "Product Name: " + usbDevice1.getProductName() + "\n" +
                        "Manufacturer Name: " + usbDevice1.getManufacturerName() + "\n" +
                        "DeviceClass: " + usbDevice1.getDeviceClass() + " - " + translateDeviceClass(usbDevice1.getDeviceClass()) + "\n" +
                        "DeviceSubClass: " + usbDevice1.getDeviceSubclass() + "\n" +
                        "VendorID: " + usbDevice1.getVendorId() + "\n" +
                        "ProductID: " + usbDevice1.getProductId() + "\n";
                Log.d(Constants.TAG, TAG+" usbDevice : "+usbDevice);
                int interfaceCount = usbDevice1.getInterfaceCount();
                Log.d(Constants.TAG, TAG+" interfaceCount : "+interfaceCount);
                mDevice = usbDevice1;
                Toast.makeText(context, "Device is attached", Toast.LENGTH_SHORT).show();
            }

            mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            context.registerReceiver(mUsbReceiver, filter);

            mUsbManager.requestPermission(mDevice, mPermissionIntent);
        } else {
            Toast.makeText(context, "Please attach printer via USB", Toast.LENGTH_SHORT).show();
        }
    }

    private void print(Context  context) {
        final String test = ed_txt.getText().toString() + "\n\n";
        testBytes = test.getBytes();
        if (mInterface == null) {
            Toast.makeText(context, "INTERFACE IS NULL", Toast.LENGTH_SHORT).show();
        } else if (mConnection == null) {
            Toast.makeText(context, "CONNECTION IS NULL", Toast.LENGTH_SHORT).show();
        } else if (forceCLaim == null) {
            Toast.makeText(context, "FORCE CLAIM IS NULL", Toast.LENGTH_SHORT).show();
        } else {
            mConnection.claimInterface(mInterface, forceCLaim);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] cut_paper = {0x1D, 0x56, 0x41, 0x10};
                    mConnection.bulkTransfer(mEndPoint, usbDevice.getBytes(), testBytes.length, 0);
                    mConnection.bulkTransfer(mEndPoint, cut_paper, cut_paper.length, 0);
                }
            });
            thread.run();
        }
    }

    final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            mInterface = device.getInterface(0);
                            mEndPoint = mInterface.getEndpoint(1);// 0 IN and  1 OUT to printer.
                            mConnection = mUsbManager.openDevice(device);
                            //print
                            print(context);
                        }
                    } else {
                        Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    private String translateDeviceClass(int deviceClass) {
        switch (deviceClass) {
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default:
                return "Unknown USB class!";
        }
    }
}
