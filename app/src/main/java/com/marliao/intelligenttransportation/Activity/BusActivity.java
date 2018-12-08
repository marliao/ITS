package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.marliao.intelligenttransportation.Fragment.BusStation1Fragment;
import com.marliao.intelligenttransportation.Fragment.BusStation2Fragment;
import com.marliao.intelligenttransportation.R;

public class BusActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llLayout;
    private Button btnBusStation1;
    private Button btnBusStation2;
    private ImageView ivReturnActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        initUI();
        defaultInterface();
    }

    private void defaultInterface() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.replace(R.id.ll_layout,new BusStation1Fragment());
        btnBusStation1.setBackgroundResource(R.drawable.bus_pressed);
        btnBusStation1.setTextColor(Color.WHITE);
        btnBusStation2.setBackgroundResource(R.drawable.bus_unpressed);
        btnBusStation2.setTextColor(Color.BLACK);
        beginTransaction.commit();
    }

    private void initUI() {
        llLayout = (LinearLayout) findViewById(R.id.ll_layout);
        btnBusStation1 = (Button) findViewById(R.id.btn_bus_station1);
        btnBusStation2 = (Button) findViewById(R.id.btn_bus_station2);
        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        btnBusStation1.setOnClickListener(this);
        btnBusStation2.setOnClickListener(this);
        ivReturnActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_bus_station1:
                beginTransaction.replace(R.id.ll_layout,new BusStation1Fragment());
                btnBusStation1.setBackgroundResource(R.drawable.bus_pressed);
                btnBusStation1.setTextColor(Color.WHITE);
                btnBusStation2.setBackgroundResource(R.drawable.bus_unpressed);
                btnBusStation2.setTextColor(Color.BLACK);
                break;
            case R.id.btn_bus_station2:
                beginTransaction.replace(R.id.ll_layout,new BusStation2Fragment());
                btnBusStation2.setBackgroundResource(R.drawable.bus_pressed);
                btnBusStation2.setTextColor(Color.WHITE);
                btnBusStation1.setBackgroundResource(R.drawable.bus_unpressed);
                btnBusStation1.setTextColor(Color.BLACK);
                break;
            case R.id.iv_return_activity:
                startActivity(new Intent(BusActivity.this,MainActivity.class));
                finish();
                break;
        }
        beginTransaction.commit();
    }
}
