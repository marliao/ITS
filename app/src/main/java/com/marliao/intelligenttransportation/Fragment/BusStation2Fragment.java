package com.marliao.intelligenttransportation.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.Bus2BusStation;
import com.marliao.intelligenttransportation.db.dao.GetAllSense;
import com.marliao.intelligenttransportation.enige.MyApplication;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusStation2Fragment extends Fragment {

    private TextView tvBus2;
    private TextView tvWaitBus2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bus_station2,null);
        tvBus2 = (TextView) view.findViewById(R.id.tv_bus2);
        tvWaitBus2 = (TextView) view.findViewById(R.id.tv_wait_bus2);
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
                    String busStationInfoResult2 = HttpUtil.doPost(busStationInfo, GenerateJsonUtil.GenerateGetBusStationInfo(2));
                    final List<Bus2BusStation> bus2BusStationList2 = ResolveJson.ResolveGetBusStationInfo(busStationInfoResult2);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String station1 = "\n";
                            for (int i = 0; i < bus2BusStationList2.size(); i++) {
                                station1 += bus2BusStationList2.get(i).getBusId() + "号公交：" + bus2BusStationList2.get(i).getDistance() + "m \n\n";
                            }
                            tvBus2.setText(station1);
                            //候车环境
                            tvWaitBus2.setText("\nPM2.5：" + getAllSense.getPm_2_5() + "μg/m3 \n\n温度：" + getAllSense.getTemperature() + "℃ \n\n湿度：" +
                                    "" + getAllSense.getHumidity() + "% \n\nCO2：" + getAllSense.getCo_2());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

}
