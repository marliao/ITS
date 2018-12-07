package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetParkFree;
import com.marliao.intelligenttransportation.db.dao.GetParkRate;
import com.marliao.intelligenttransportation.db.dao.ParkFreeId;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class StopCarActivity extends AppCompatActivity {
    private ImageView ivReturnActivity;
    private TextView tvCurrentRate;
    private Button btnCurrent;
    private EditText etRate;
    private EditText etUnit;
    private Button btnSettingRate;
    private Button btnSearchPark;
    private ImageView ivPark1;
    private ImageView ivPark2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_car);
        initUI();
        currentParkingRate();//当前停车费率
        modifyParkingRate();//修改停车费率
        currentParkingSpaceInquiry();//当前车位查询
    }

    private void currentParkingSpaceInquiry() {
        btnSearchPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String http = null;
                            if (SpUtil.getBoolean(StopCarActivity.this, ConstantValue.IPSETTING, false)) {
                                http = GenerateJsonUtil.GenerateHttp(SpUtil.getString(StopCarActivity.this, ConstantValue.IPVALUE, ""));
                            } else {
                                http = MyApplication.HTTP;
                            }
                            String path = http + MyApplication.HTTPGETPARKFREE;
                            String httpResult = HttpUtil.doPost(path, null);
                            final GetParkFree getParkFree = ResolveJson.ResolveGetParkFree(httpResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<ParkFreeId> parkFreeIdLit = getParkFree.getParkFreeIdLit();
                                    Integer parkFree = 0;
                                    for (int i = 0; i < parkFreeIdLit.size(); i++) {
                                        if (parkFreeIdLit.get(i).getParkFreeId() == 1) {
                                            parkFree = parkFreeIdLit.get(i).getParkFreeId();
                                        } else {
                                            parkFree = 2;
                                        }
                                    }
                                    if (parkFree == 1) {
                                        ivPark1.setBackgroundResource(R.drawable.parkfree);
                                        ivPark2.setBackgroundResource(R.drawable.parknotfree);
                                    } else {
                                        ivPark1.setBackgroundResource(R.drawable.parknotfree);
                                        ivPark2.setBackgroundResource(R.drawable.parkfree);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }

    private void modifyParkingRate() {
        btnSettingRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit = etUnit.getText().toString().trim();
                if (unit.equals("Hour") || unit.equals("Count")) {
                    String rate = etRate.getText().toString().trim();
                    if (rate != null && !TextUtils.isEmpty(rate)) {
                        settingRate(rate, unit);
                    } else {
                        MyApplication.showToast("费率不能为空！");
                    }
                } else {
                    MyApplication.showToast("费率单位为：   Hour   or   Count   ");
                }
            }
        });
    }

    private void settingRate( final String rate,final String unit) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String http = null;
                    if (SpUtil.getBoolean(StopCarActivity.this, ConstantValue.IPSETTING, false)) {
                        http = GenerateJsonUtil.GenerateHttp(SpUtil.getString(StopCarActivity.this, ConstantValue.IPVALUE, ""));
                    } else {
                        http = MyApplication.HTTP;
                    }
                    String path = http + MyApplication.HTTPSETPARKRATE;
                    String generateResult = GenerateJsonUtil.GenerateSetParkRate(unit, Integer.parseInt(rate));
                    String httpResult = HttpUtil.doPost(path, generateResult);
                    final String result = ResolveJson.ResolveSimple(httpResult);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("ok")) {
                                MyApplication.showToast("费率设置成功！");
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void currentParkingRate() {
        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String http = null;
                            if (SpUtil.getBoolean(StopCarActivity.this, ConstantValue.IPSETTING, false)) {
                                http = GenerateJsonUtil.GenerateHttp(SpUtil.getString(StopCarActivity.this, ConstantValue.IPVALUE, ""));
                            } else {
                                http = MyApplication.HTTP;
                            }
                            String path = http + MyApplication.HTTPGETPARKRATE;
                            String httpResult = HttpUtil.doPost(path, null);
                            Log.i("********", httpResult);
                            final GetParkRate getParkRate = ResolveJson.ResolveGetParkRate(httpResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (getParkRate.getCount().equals("Count")) {
                                        tvCurrentRate.setText(getParkRate.getMoney() + "次/元");
                                    } else {
                                        tvCurrentRate.setText(getParkRate.getMoney() + "小时/元");
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }

    private void initUI() {
        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        ivReturnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StopCarActivity.this, MainActivity.class));
                finish();
            }
        });
        tvCurrentRate = (TextView) findViewById(R.id.tv_current_rate);
        btnCurrent = (Button) findViewById(R.id.btn_current);
        etRate = (EditText) findViewById(R.id.et_rate);
        etUnit = (EditText) findViewById(R.id.et_unit);
        btnSettingRate = (Button) findViewById(R.id.btn_setting_rate);
        btnSearchPark = (Button) findViewById(R.id.btn_search_park);
        ivPark1 = (ImageView) findViewById(R.id.iv_park1);
        ivPark2 = (ImageView) findViewById(R.id.iv_park2);
    }
}
