package com.marliao.intelligenttransportation.Fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marliao.intelligenttransportation.Activity.MainActivity;
import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.Bus2BusStation;
import com.marliao.intelligenttransportation.db.dao.GetAllSense;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BusStation1Fragment extends Fragment {

    private TextView tvBus1;
    private TextView tvWaitBus1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bus_station1,null);
        tvBus1 = (TextView) view.findViewById(R.id.tv_bus1);
        tvWaitBus1 = (TextView) view.findViewById(R.id.tv_wait_bus1);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置定时器,true，程序结束，timer就结束
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //拿取数据
                initData();
            }
        };
        //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
        timer.schedule(timerTask, 0, 5000);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    getBus1Rate();
                } catch (Exception e) {
                    try {
                        getBus1Rate();
                    } catch (JSONException e1) {
                        try {
                            getBus1Rate();
                        } catch (JSONException e2) {
                            MyApplication.showToast("网络连接异常，请稍后再试！");
                        }
                    }
                }
                super.run();
            }
        }.start();
    }

    private void getBus1Rate() throws JSONException {
        String http = null;
        if (SpUtil.getBoolean(MyApplication.getContext(), ConstantValue.IPSETTING, false)) {
            String string = SpUtil.getString(MyApplication.getContext(), ConstantValue.IPVALUE, "");
            http = GenerateJsonUtil.GenerateHttp(string);
        } else {
            http = MyApplication.HTTP;
        }

        String allSense = http + MyApplication.HTTPGETALLSENSE;
        String sllSenseResult = HttpUtil.doPost(allSense, null);
        final GetAllSense getAllSense = ResolveJson.ResolveGetAllSense(sllSenseResult);

        String busStationInfo = http + MyApplication.HTTPGETBUSSTATIONINFO;
        String busStationInfoResult1 = HttpUtil.doPost(busStationInfo, GenerateJsonUtil.GenerateGetBusStationInfo(1));
        Log.i("+++++++",busStationInfoResult1);
        final List<Bus2BusStation> bus2BusStationList1 = ResolveJson.ResolveGetBusStationInfo(busStationInfoResult1);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String station1 = "\n";
                for (int i = 0; i < bus2BusStationList1.size(); i++) {
                    station1 += bus2BusStationList1.get(i).getBusId() + "号公交：" + bus2BusStationList1.get(i).getDistance() + "m \n\n";
                }
                tvBus1.setText(station1);
                //候车环境
                tvWaitBus1.setText("\nPM2.5：" + getAllSense.getPm_2_5() + "μg/m3 \n\n温度：" + getAllSense.getTemperature() + "℃ \n\n湿度：" +
                        "" + getAllSense.getHumidity() + "% \n\nCO2：" + getAllSense.getCo_2());
            }
        });
    }

}
