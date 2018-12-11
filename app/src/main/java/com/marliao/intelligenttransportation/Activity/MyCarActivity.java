package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.hardware.camera2.CaptureFailure;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetCarSpeed;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MyCarActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int DATA = 100;
    private ImageView ivReturnActivity;
    private TextView tvMyCar1;
    private ImageView ivButton1;
    private TextView tvMyCar2;
    private ImageView ivButton2;
    private TextView tvMyCar3;
    private ImageView ivButton3;
    private TextView tvMyCar4;
    private ImageView ivButton4;
    private TextView tvAccountSearchRecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        initUI();
        returnStatus();
    }

    private void returnStatus() {
        boolean mycar1 = SpUtil.getBoolean(this, ConstantValue.START_STOP_CAR1, false);
        if (mycar1) {
            initData(1);
            ivButton1.setBackgroundResource(R.drawable.stop);
        } else {
            tvMyCar1.setText("停止");
        }
        boolean mycar2 = SpUtil.getBoolean(this, ConstantValue.START_STOP_CAR2, false);
        if (mycar2) {
            initData(2);
            ivButton2.setBackgroundResource(R.drawable.stop);
        } else {
            tvMyCar2.setText("停止");
        }
        boolean mycar3 = SpUtil.getBoolean(this, ConstantValue.START_STOP_CAR3, false);
        if (mycar3) {
            initData(3);
            ivButton3.setBackgroundResource(R.drawable.stop);
        } else {
            tvMyCar3.setText("停止");
        }
        boolean mycar4 = SpUtil.getBoolean(this, ConstantValue.START_STOP_CAR4, false);
        if (mycar4) {
            initData(4);
            ivButton4.setBackgroundResource(R.drawable.stop);
        } else {
            tvMyCar4.setText("停止");
        }
    }

    private void initData(final Integer CarId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    getCarSpeed(CarId);

                } catch (JSONException e) {
                    try {
                        getCarSpeed(CarId);
                    } catch (JSONException e1) {
                        try {
                            getCarSpeed(CarId);
                        } catch (JSONException e2) {
                            MyApplication.showToast("网络连接异常，请稍后再试！");
                        }
                    }
                }
                super.run();
            }
        }.start();
    }

    private void getCarSpeed(final Integer CarId) throws JSONException {
        String http=null;
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.IPSETTING, false)) {
            http=GenerateJsonUtil.GenerateHttp(SpUtil.getString(MyCarActivity.this,ConstantValue.IPVALUE,""));
        }else {
            http=MyApplication.HTTP;
        }
        String path = http + MyApplication.HTTPGETCARSPEED;
        String params = GenerateJsonUtil.GenerateSimple(CarId);
        String getCarSpeedReuslt = HttpUtil.doPost(path, params);
        final GetCarSpeed getCarSpeed = ResolveJson.ResolveGetCarSpeed(getCarSpeedReuslt);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (CarId) {
                    case 1:
                        tvMyCar1.setText("车速：" + getCarSpeed.getCarSpeed() + "km/h");
                        break;
                    case 2:
                        tvMyCar2.setText("车速：" + getCarSpeed.getCarSpeed() + "km/h");
                        break;
                    case 3:
                        tvMyCar3.setText("车速：" + getCarSpeed.getCarSpeed() + "km/h");
                        break;
                    case 4:
                        tvMyCar4.setText("车速：" + getCarSpeed.getCarSpeed() + "km/h");
                        break;
                }
            }
        });
    }

    private void initUI() {
        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        tvMyCar1 = (TextView) findViewById(R.id.tv_my_car1);
        ivButton1 = (ImageView) findViewById(R.id.iv_button1);
        tvMyCar2 = (TextView) findViewById(R.id.tv_my_car2);
        ivButton2 = (ImageView) findViewById(R.id.iv_button2);
        tvMyCar3 = (TextView) findViewById(R.id.tv_my_car3);
        ivButton3 = (ImageView) findViewById(R.id.iv_button3);
        tvMyCar4 = (TextView) findViewById(R.id.tv_my_car4);
        ivButton4 = (ImageView) findViewById(R.id.iv_button4);
        tvAccountSearchRecharge = (TextView) findViewById(R.id.tv_account_search_recharge);

        //按钮的点击事件
        ivReturnActivity.setOnClickListener(this);
        ivButton1.setOnClickListener(this);
        ivButton2.setOnClickListener(this);
        ivButton3.setOnClickListener(this);
        ivButton4.setOnClickListener(this);
        tvAccountSearchRecharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_activity:
                startActivity(new Intent(MyCarActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.iv_button1:
                boolean car1 = SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR1, false);
                if (!car1) {
                    initData(1);
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR1, true);
                    ivButton1.setBackgroundResource(R.drawable.stop);
                    setCarAction(1,"Start");
                } else {
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR1, false);
                    ivButton1.setBackgroundResource(R.drawable.start);
                    tvMyCar1.setText("停止");
                    setCarAction(1,"Stop");
                }
                break;
            case R.id.iv_button2:
                boolean car2 = SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR2, false);
                if (!car2) {
                    initData(2);
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR2, true);
                    ivButton2.setBackgroundResource(R.drawable.stop);
                    setCarAction(2,"Start");
                } else {
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR2, false);
                    ivButton2.setBackgroundResource(R.drawable.start);
                    tvMyCar2.setText("停止");
                    setCarAction(2,"Stop");
                }
                break;
            case R.id.iv_button3:
                boolean car3 = SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR3, false);
                if (!car3) {
                    initData(3);
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR3, true);
                    ivButton3.setBackgroundResource(R.drawable.stop);
                    setCarAction(3,"Start");
                } else {
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR3, false);
                    ivButton3.setBackgroundResource(R.drawable.start);
                    tvMyCar3.setText("停止");
                    setCarAction(3,"Stop");
                }
                break;
            case R.id.iv_button4:
                boolean car4 = SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR4, false);
                if (!car4) {
                    initData(4);
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR4, true);
                    ivButton4.setBackgroundResource(R.drawable.stop);
                    setCarAction(4,"Start");
                } else {
                    SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR4, false);
                    ivButton4.setBackgroundResource(R.drawable.start);
                    tvMyCar4.setText("停止");
                    setCarAction(4,"Stop");
                }
                break;
            case R.id.tv_account_search_recharge:
                startActivity(new Intent(MyCarActivity.this,CarAccountActivity.class));
                break;
        }
    }

    private void setCarAction(final int CarId, final String CarAction) {
        new Thread(){
            @Override
            public void run() {
                try {
                    setTheCarStatus(CarId,CarAction);
                } catch (JSONException e) {
                    try {
                        setTheCarStatus(CarId,CarAction);
                    } catch (JSONException e1) {
                        try {
                            setTheCarStatus(CarId,CarAction);
                        } catch (JSONException e2) {
                            MyApplication.showToast("网络连接异常，请稍后再试！");
                        }
                    }
                }
                super.run();
            }
        }.start();
    }

    private void setTheCarStatus(int CarId, String CarAction) throws JSONException {
        String http=null;
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.IPSETTING, false)) {
            http=GenerateJsonUtil.GenerateHttp(SpUtil.getString(MyCarActivity.this,ConstantValue.IPVALUE,""));
        }else {
            http=MyApplication.HTTP;
        }
        String path = http + MyApplication.HTTPSETCARMOVE;
        String generateResult = GenerateJsonUtil.GenerateSetCarMove(CarId, CarAction);
        String httpResult = HttpUtil.doPost(path, generateResult);
        final String result = ResolveJson.ResolveSimple(httpResult);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!result.equals("ok")) {
                    MyApplication.showToast("服务器连接失败！");
                }
            }
        });
    }
}
