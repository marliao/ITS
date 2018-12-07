package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetAllSense;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class RoadEnvironmentActivity extends AppCompatActivity {
    private static final int AIRQUALITY = 100;
    private static final int LIGHTINTENSITY = 101;
    private ImageView ivReturnActivity;
    private Button btnSearchAirQuality;
    private VideoView vvAlarming;
    private TextView tvPmTemCoShidu;
    private TextView tvWarningInformationAir;
    private Button btnSearchLightIntensity;
    private TextView tvLightStrength;
    private TextView tvWarningInformationLight;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case AIRQUALITY:
                    GetAllSense getAllSense = (GetAllSense) msg.obj;
                    tvPmTemCoShidu.setText("PM2.5：" + getAllSense.getPm_2_5() + "μg/m3 ,温度：" + getAllSense.getTemperature() + "℃ ,湿度：" +
                            "" + getAllSense.getHumidity() + "% ,CO2：" + getAllSense.getCo_2());
                    if (getAllSense.getPm_2_5() > 200 || getAllSense.getTemperature() > 40 || getAllSense.getTemperature() < 10 || getAllSense.getHumidity() > 50 || getAllSense.getHumidity() < 0) {
                        vvAlarming.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/raw/alarming"));
                        vvAlarming.start();
                        vvAlarming.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                mp.setLooping(true);
                            }
                        });
                        vvAlarming.setVisibility(View.VISIBLE);
                        tvWarningInformationAir.setVisibility(View.VISIBLE);
                    } else {
                        vvAlarming.setVisibility(View.INVISIBLE);
                        tvWarningInformationAir.setVisibility(View.INVISIBLE);
                    }
                    break;
                case LIGHTINTENSITY:
                    Integer lightIntensity = (Integer) msg.obj;
                    tvLightStrength.setText("光照强度：" + lightIntensity + " Lux");
                    if (lightIntensity > 2500 || lightIntensity < 1000) {
                        tvWarningInformationLight.setVisibility(View.VISIBLE);
                    }else {
                        tvWarningInformationLight.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_environment);
        initUI();
        airQuality();//空气质量
        lightIntensity();//光照强度
    }

    private void lightIntensity() {
        btnSearchLightIntensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置定时器,true，程序结束，timer就结束
                Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        //拿取数据
                        initLightIntensityData();
                    }
                };
                //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
                timer.schedule(timerTask, 0, 10000);
            }
        });
    }

    private void initLightIntensityData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String http = null;
                    if (SpUtil.getBoolean(RoadEnvironmentActivity.this, ConstantValue.IPSETTING, false)) {
                        String string = SpUtil.getString(RoadEnvironmentActivity.this, ConstantValue.IPVALUE, "");
                        http = GenerateJsonUtil.GenerateHttp(string);
                    } else {
                        http = MyApplication.HTTP;
                    }

                    String allSense = http + MyApplication.HTTPGETALLSENSE;
                    String sllSenseResult = HttpUtil.doPost(allSense, null);
                    GetAllSense getAllSense = ResolveJson.ResolveGetAllSense(sllSenseResult);
                    Integer lightIntensity = getAllSense.getLightIntensity();
                    Message msg = Message.obtain();
                    msg.what = LIGHTINTENSITY;
                    msg.obj = lightIntensity;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void airQuality() {
        btnSearchAirQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置定时器,true，程序结束，timer就结束
                Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        //拿取数据
                        initairQualityData();
                    }
                };
                //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
                timer.schedule(timerTask, 0, 10000);
            }
        });
    }

    private void initairQualityData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String http = null;
                    if (SpUtil.getBoolean(RoadEnvironmentActivity.this, ConstantValue.IPSETTING, false)) {
                        String string = SpUtil.getString(RoadEnvironmentActivity.this, ConstantValue.IPVALUE, "");
                        http = GenerateJsonUtil.GenerateHttp(string);
                    } else {
                        http = MyApplication.HTTP;
                    }

                    String allSense = http + MyApplication.HTTPGETALLSENSE;
                    String sllSenseResult = HttpUtil.doPost(allSense, null);
                    GetAllSense getAllSense = ResolveJson.ResolveGetAllSense(sllSenseResult);
                    Message msg = Message.obtain();
                    msg.what = AIRQUALITY;
                    msg.obj = getAllSense;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void initUI() {
        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        ivReturnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoadEnvironmentActivity.this, MainActivity.class));
                finish();
            }
        });
        btnSearchAirQuality = (Button) findViewById(R.id.btn_search_air_quality);
        vvAlarming = (VideoView) findViewById(R.id.vv_alarming);
        tvPmTemCoShidu = (TextView) findViewById(R.id.tv_pm_tem_co_shidu);
        tvWarningInformationAir = (TextView) findViewById(R.id.tv_warning_information_air);
        btnSearchLightIntensity = (Button) findViewById(R.id.btn_search_light_intensity);
        tvLightStrength = (TextView) findViewById(R.id.tv_light_strength);
        tvWarningInformationLight = (TextView) findViewById(R.id.tv_warning_information_light);

    }
}
