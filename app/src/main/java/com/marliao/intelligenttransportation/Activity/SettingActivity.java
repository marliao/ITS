package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.enige.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity implements TextWatcher {

    private ImageView iv_return_activity;
    private Button btn_determine;
    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private String iPv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        initEdittext();
    }

    private void initEdittext() {
        String[] temp=SpUtil.getString(this,ConstantValue.IPVALUE,"").split("\\.");
        et_ip1.setHint(temp[0]);
        et_ip2.setHint(temp[1]);
        et_ip3.setHint(temp[2]);
        et_ip4.setHint(temp[3]);
    }

    private void initUI() {
        iv_return_activity = (ImageView) findViewById(R.id.iv_return_activity);
        iv_return_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });

        btn_determine = (Button) findViewById(R.id.btn_determine);
        btn_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.putBoolean(SettingActivity.this,ConstantValue.IPSETTING,true);
                SpUtil.putString(SettingActivity.this,ConstantValue.IPVALUE,iPv4);
                MyApplication.showToast("IP地址设置成功！");
            }
        });

        et_ip1 = (EditText) findViewById(R.id.et_ip1);
        et_ip2 = (EditText) findViewById(R.id.et_ip2);
        et_ip3 = (EditText) findViewById(R.id.et_ip3);
        et_ip4 = (EditText) findViewById(R.id.et_ip4);

        //给EditText设置内容监听事件
        et_ip1.addTextChangedListener(this);
        et_ip2.addTextChangedListener(this);
        et_ip3.addTextChangedListener(this);
        et_ip4.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String ip1 = et_ip1.getText().toString().trim();
        String ip2 = et_ip2.getText().toString().trim();
        String ip3 = et_ip3.getText().toString().trim();
        String ip4 = et_ip4.getText().toString().trim();
        //利用正则判断IP地址是否格式正确，不正确的加红色下划线
        if (getMatches(ip1) && getMatches(ip2) && getMatches(ip3) && getMatches(ip4)) {
            //将四个IP拼接起来
            iPv4 = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
            btn_determine.setEnabled(true);
        } else {
            btn_determine.setEnabled(false);
            MyApplication.showToast("IP地址输入不合法，请重新输入！");
        }
    }

    private boolean getMatches(String ip) {
        Pattern pattern = Pattern.compile("((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
