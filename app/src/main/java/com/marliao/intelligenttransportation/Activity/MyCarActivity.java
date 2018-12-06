package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private GetCarSpeed mGetCarSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        initUI();
        StatusEcho();
    }

    private void StatusEcho() {
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR1, false)) {
            GetCarSpeed carSpeed1 = initData(1);
            tvMyCar1.setText("车速："+carSpeed1.getCarSpeed()+"km/h");
            ivButton1.setBackgroundResource(R.drawable.start);
        } else {
            ivButton1.setBackgroundResource(R.drawable.stop);
        }
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR2, false)) {
            GetCarSpeed carSpeed2 = initData(2);
            tvMyCar1.setText("车速："+carSpeed2.getCarSpeed()+"km/h");
            ivButton2.setBackgroundResource(R.drawable.start);
        } else {
            ivButton2.setBackgroundResource(R.drawable.stop);
        }
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR3, false)) {
            GetCarSpeed carSpeed3 = initData(3);
            tvMyCar1.setText("车速："+carSpeed3.getCarSpeed()+"km/h");
            ivButton3.setBackgroundResource(R.drawable.start);
        } else {
            ivButton3.setBackgroundResource(R.drawable.stop);
        }
        if (SpUtil.getBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR4, false)) {
            GetCarSpeed carSpeed4 = initData(4);
            tvMyCar1.setText("车速："+carSpeed4.getCarSpeed()+"km/h");
            ivButton4.setBackgroundResource(R.drawable.start);
        } else {
            ivButton4.setBackgroundResource(R.drawable.stop);
        }
    }

    private GetCarSpeed initData(final Integer CarId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String path = MyApplication.HTTP + MyApplication.HTTPGETCARSPEED;
                    String params = GenerateJsonUtil.GenerateSimple(CarId);
                    String getCarSpeedReuslt = HttpUtil.doPost(path, params);
                    mGetCarSpeed = ResolveJson.ResolveGetCarSpeed(getCarSpeedReuslt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
        return mGetCarSpeed;
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
                GetCarSpeed carSpeed1 = initData(1);
                tvMyCar1.setText("车速："+carSpeed1.getCarSpeed()+"km/h");
                ivButton1.setBackgroundResource(R.drawable.start);
                SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR1, true);
                break;
            case R.id.iv_button2:
                GetCarSpeed carSpeed2 = initData(2);
                tvMyCar1.setText("车速："+carSpeed2.getCarSpeed()+"km/h");
                ivButton1.setBackgroundResource(R.drawable.start);
                SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR2, true);
                break;
            case R.id.iv_button3:
                GetCarSpeed carSpeed3 = initData(3);
                tvMyCar1.setText("车速："+carSpeed3.getCarSpeed()+"km/h");
                ivButton1.setBackgroundResource(R.drawable.start);
                SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR3, true);
                break;
            case R.id.iv_button4:
                GetCarSpeed carSpeed4 = initData(4);
                tvMyCar4.setText("车速："+carSpeed4.getCarSpeed()+"km/h");
                ivButton1.setBackgroundResource(R.drawable.start);
                SpUtil.putBoolean(MyCarActivity.this, ConstantValue.START_STOP_CAR4, true);
                break;
            case R.id.tv_account_search_recharge:
                break;
        }
    }
}
