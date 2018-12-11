package com.marliao.intelligenttransportation.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetCarAccountBalance;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;
import org.xml.sax.helpers.LocatorImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAccountActivity extends AppCompatActivity {
    private static final int GETCARACCOUNTBALANCE = 100;
    private static final int SETCATACCOUNT = 101;
    private ImageView ivReturnActivity;
    private Spinner spinnerSearch;
    private Button btnSearch;
    private Spinner spinnerRecharge;
    private Button btnRecharge;
    private EditText etAccountNumber;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETCARACCOUNTBALANCE:
                    Integer carAccountBalance = (Integer) msg.obj;
                    showCarAccountBalanceDialog(carAccountBalance);
                    break;
                case SETCATACCOUNT:
                    String result = (String) msg.obj;
                    Log.i("*result",result);
                    if (result.equals("ok")){
                        MyApplication.showToast("充值成功！");
                    }else {
                        MyApplication.showToast("充值失败，请稍后重试！");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     *  @param carId
     * @param account
     */
    private void showRechargeAccount(final Integer carId, final String account) {
        //获取系统日期和时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        //对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.styletest);//TODO 背景透明了，需要半透明
        builder.setIcon(R.drawable.rmb);
        builder.setTitle("小车账户充值");
        builder.setMessage("在"+simpleDateFormat.format(date)+" 将要给"+carId+"号小车充值"+account+"元");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            searchAccount(carId,account);
                        } catch (JSONException e) {
                            try {
                                searchAccount(carId,account);
                            } catch (JSONException e1) {
                                try {
                                    searchAccount(carId,account);
                                } catch (JSONException e2) {
                                    MyApplication.showToast("网络连接异常，请稍后再试！");
                                }
                            }
                        }
                        super.run();
                    }
                }.start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无操作
            }
        });
        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无操作
            }
        });
        builder.show();
    }

    private void searchAccount(Integer carId, String account) throws JSONException {
        String http=null;
        if (SpUtil.getBoolean(CarAccountActivity.this, ConstantValue.IPSETTING, false)) {
            http=GenerateJsonUtil.GenerateHttp(SpUtil.getString(CarAccountActivity.this,ConstantValue.IPVALUE,""));
        }else {
            http=MyApplication.HTTP;
        }
        Integer rechargeNumber=Integer.parseInt(account);
        String path=http+MyApplication.HTTPSETCARACCOUNTRECHARGE;
        String generateResult = GenerateJsonUtil.GenerateSetCarAccountRecharge(carId, rechargeNumber);
        String httpResult = HttpUtil.doPost(path, generateResult);
        String resolveSimple = ResolveJson.ResolveSimple(httpResult);
        Message msg = Message.obtain();
        msg.what=SETCATACCOUNT;
        msg.obj=resolveSimple;
        mHandler.sendMessage(msg);
    }

    /**
     * 账户查询的对话框
     *
     * @param carAccountBalance
     */
    private void showCarAccountBalanceDialog(Integer carAccountBalance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.styletest);//TODO 背景透明了，需要半透明
        builder.setIcon(R.drawable.rmb);
        builder.setTitle("小车账户查询");
        builder.setMessage("小车账户余额：" + carAccountBalance + "元");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无操作
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无操作
            }
        });
        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无操作
            }
        });
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_account);
        initUI();
        setSpinner();
        spinnerSearchData();//查询逻辑
        spinnerRechargeData();//充值逻辑
    }

    /**
     * 充值逻辑处理
     */
    private void spinnerRechargeData() {
        spinnerRecharge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //要充值的车辆id
                Integer CarId = position + 1;
                getSpinnerRecharge(CarId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getSpinnerRecharge(final Integer carId) {
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccountNumber.getText().toString().trim();
                if (account != null&&!TextUtils.isEmpty(account)) {
                    showRechargeAccount(carId,account);
                }else {
                    MyApplication.showToast("请输入要充值的金额");
                }
            }
        });
    }

    /**
     * 查询逻辑处理
     */
    private void spinnerSearchData() {
        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //要查询的车辆id
                Integer CarId = position + 1;
                getCarAccountBalance(CarId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getCarAccountBalance(final Integer carId) {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            rechargeCarBalance(carId);
                        } catch (JSONException e) {
                            try {
                                rechargeCarBalance(carId);
                            } catch (JSONException e1) {
                                try {
                                    rechargeCarBalance(carId);
                                } catch (JSONException e2) {
                                    MyApplication.showToast("网络连接异常，请稍后再试！");
                                }
                            }
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }

    private void rechargeCarBalance(Integer carId) throws JSONException {
        String http=null;
        if (SpUtil.getBoolean(CarAccountActivity.this, ConstantValue.IPSETTING, false)) {
            http=GenerateJsonUtil.GenerateHttp(SpUtil.getString(CarAccountActivity.this,ConstantValue.IPVALUE,""));
        }else {
            http=MyApplication.HTTP;
        }
        String path = http + MyApplication.HTTPGETCARACCOUNTBALANCE;
        String generateResult = GenerateJsonUtil.GenerateSimple(carId);
        String httpResult = HttpUtil.doPost(path, generateResult);
        GetCarAccountBalance getCarAccountBalance = ResolveJson.ResolveGetCarAccountBalance(httpResult);
        Integer carAccountBalance = getCarAccountBalance.getBanlance();
        Message msg = Message.obtain();
        msg.what = GETCARACCOUNTBALANCE;
        msg.obj = carAccountBalance;
        mHandler.sendMessage(msg);
    }

    private void setSpinner() {
        //数据
        List<Integer> CarIdList = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            CarIdList.add(i);
        }
        MyAdapter myAdapter = new MyAdapter(CarIdList);
        spinnerSearch.setAdapter(myAdapter);
        spinnerRecharge.setAdapter(myAdapter);
    }

    public class MyAdapter extends BaseAdapter {
        private List<Integer> carIdList;

        public MyAdapter(List<Integer> carIdList) {
            this.carIdList = carIdList;
        }

        @Override
        public int getCount() {
            return carIdList.size();
        }

        @Override
        public Integer getItem(int position) {
            return carIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(CarAccountActivity.this, R.layout.selector_carid_item, null);
                viewHolder.tvCarIdItem = (TextView) convertView.findViewById(R.id.tv_carId_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvCarIdItem.setText(getItem(position) + "");
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvCarIdItem;
    }

    private void initUI() {
        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        ivReturnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarAccountActivity.this, MyCarActivity.class));
                finish();
            }
        });
        spinnerSearch = (Spinner) findViewById(R.id.spinner_search);
        btnSearch = (Button) findViewById(R.id.btn_search);
        spinnerRecharge = (Spinner) findViewById(R.id.spinner_recharge);
        btnRecharge = (Button) findViewById(R.id.btn_recharge);
        etAccountNumber = (EditText) findViewById(R.id.et_account_number);
    }
}
