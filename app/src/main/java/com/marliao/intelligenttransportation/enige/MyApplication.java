package com.marliao.intelligenttransportation.enige;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class MyApplication extends Application {

    public static final String HTTP = "http://192.168.1.101:8080/transportservice/type/jason/action/";
    public static final String HTTPGETALLSENSE = "GetAllSense.do";
    public static final String HTTPGETLIGHTSENSEVALVE = "GetLightSenseValve.do";
    public static final String HTTPGETCARSPEED = "GetCarSpeed.do";
    public static final String HTTPSETCARMOVE = "SetCarMove.do";
    public static final String HTTPGETCARACCOUNTBALANCE = "GetCarAccountBalance.do";
    public static final String HTTPSETCARACCOUNTRECHARGE = "SetCarAccountRecharge.do";
    public static final String HTTPGETROADSTATUS = "GetRoadStatus.do";
    public static final String HTTPSETPARKRATE = "SetParkRate.do";
    public static final String HTTPGETPARKRATE = "GetParkRate.do";
    public static final String HTTPGETPARKFREE = "GetParkFree.do";
    public static final String HTTPGETBUSSTATIONINFO = "GetBusStationInfo.do";

    public static Context context;
    public static Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void showToast(String test) {
        toast.setText(test);
        toast.show();
    }

    public static Context getContext() {
        return context;
    }
}
