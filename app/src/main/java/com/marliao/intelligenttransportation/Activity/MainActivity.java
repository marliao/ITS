package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.db.dao.Bus2BusStation;
import com.marliao.intelligenttransportation.db.dao.GetAllSense;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DATA = 100;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private ImageView iv_menu;
    private LinearLayout ll_title_my_car;
    private LinearLayout ll_title_bus;
    private LinearLayout ll_title_road;
    private LinearLayout ll_title_road_rate;
    private LinearLayout ll_title_stop_bus;
    private TextView tv_setting;
    private ImageView iv_road;
    private TextView tv_pm_tem_co_shidu;
    private TextView tv_bus1;
    private TextView tv_bus2;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DATA:
                    Map<Object, Object> mainAllInfo = (Map<Object, Object>) (Map<Object, Object>) msg.obj;
                    //展示数据
                    showData(mainAllInfo);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showData(Map<Object, Object> mainAllInfo) {
        GetAllSense getAllSense = (GetAllSense) mainAllInfo.get("getAllSense");
        tv_pm_tem_co_shidu.setText("\nPM2.5："+getAllSense.getPm_2_5()+"μg/m3 \n\n温度："+getAllSense.getTemperature()+"℃ \n\n湿度：" +
                ""+getAllSense.getHumidity()+"% \n\nCO2："+getAllSense.getCo_2());
        List<Bus2BusStation> busStation1 = (List<Bus2BusStation>) mainAllInfo.get("BusStation1");
        String station1="\n";
        for (int i = 0; i < busStation1.size(); i++) {
            station1+=busStation1.get(i).getBusId()+"号公交："+busStation1.get(i).getDistance()+"m \n\n";
        }
        tv_bus1.setText(station1);
        List<Bus2BusStation> busStation2 = (List<Bus2BusStation>) mainAllInfo.get("BusStation2");
        String station2="\n";
        for (int i = 0; i < busStation2.size(); i++) {
            station2+=busStation2.get(i).getBusId()+"号公交："+busStation2.get(i).getDistance()+"m \n\n";
        }
        tv_bus2.setText(station2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        //设置定时器,true，程序结束，timer就结束
        Timer timer = new Timer(true);
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                //拿取数据
                initData();
            }
        };
        //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
        timer.schedule(timerTask,0,5000);
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    String allSense=MyApplication.HTTP+MyApplication.HTTPGETALLSENSE;
                    String sllSenseResult = HttpUtil.doPost(allSense, null);
                    GetAllSense getAllSense = ResolveJson.ResolveGetAllSense(sllSenseResult);

                    String busStationInfo=MyApplication.HTTP+MyApplication.HTTPGETBUSSTATIONINFO;
                    String busStationInfoResult1 = HttpUtil.doPost(busStationInfo, GenerateJsonUtil.GenerateGetBusStationInfo(1));
                    List<Bus2BusStation> bus2BusStationList1 = ResolveJson.ResolveGetBusStationInfo(busStationInfoResult1);
                    String busStationInfoResult2 = HttpUtil.doPost(busStationInfo, GenerateJsonUtil.GenerateGetBusStationInfo(2));
                    List<Bus2BusStation> bus2BusStationList2 = ResolveJson.ResolveGetBusStationInfo(busStationInfoResult2);

                    Map<Object,Object> mainAllInfo=new HashMap<>();
                    mainAllInfo.put("getAllSense",getAllSense);
                    mainAllInfo.put("BusStation1",bus2BusStationList1);
                    mainAllInfo.put("BusStation2",bus2BusStationList2);

                    Message msg = Message.obtain();
                    msg.what=DATA;
                    msg.obj=mainAllInfo;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void initUI() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        ll_title_my_car = (LinearLayout) findViewById(R.id.ll_title_my_car);
        ll_title_bus = (LinearLayout) findViewById(R.id.ll_title_bus);
        ll_title_road = (LinearLayout) findViewById(R.id.ll_title_road);
        ll_title_road_rate = (LinearLayout) findViewById(R.id.ll_title_road_rate);
        ll_title_stop_bus = (LinearLayout) findViewById(R.id.ll_title_stop_bus);
        tv_setting = (TextView) findViewById(R.id.tv_setting);

        iv_road = (ImageView) findViewById(R.id.iv_road);
        tv_pm_tem_co_shidu = (TextView) findViewById(R.id.tv_pm_tem_co_shidu);
        tv_bus1 = (TextView) findViewById(R.id.tv_bus1);
        tv_bus2 = (TextView) findViewById(R.id.tv_bus2);

        //控件点击事件
        ll_title_my_car.setOnClickListener(this);
        ll_title_bus.setOnClickListener(this);
        ll_title_road.setOnClickListener(this);
        ll_title_road_rate.setOnClickListener(this);
        ll_title_stop_bus.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_my_car://我的座驾
                startActivity(new Intent(MainActivity.this,MyCarActivity.class));
                break;
            case R.id.ll_title_road://我的路况
                break;
            case R.id.ll_title_stop_bus://停车查询
                break;
            case R.id.ll_title_bus://公交查询
                break;
            case R.id.ll_title_road_rate://道路环境
                break;
            case R.id.tv_setting://设置
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.iv_menu://侧滑栏
                if (drawer_layout.isDrawerOpen(nav_view)) {
                    drawer_layout.closeDrawer(nav_view);
                } else {
                    drawer_layout.openDrawer(nav_view);
                }
                break;
        }
    }

}
