package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.enige.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class CarAccountActivity extends AppCompatActivity {
    private ImageView ivReturnActivity;
    private Spinner spinnerSearch;
    private Button btnSearch;
    private Spinner spinnerRecharge;
    private Button btnRecharge;
    private EditText etAccountNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_account);
        initUI();
        setSpinner();
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
                convertView=View.inflate(CarAccountActivity.this,R.layout.selector_carid_item,null);
                viewHolder.tvCarIdItem = (TextView) convertView.findViewById(R.id.tv_carId_item);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tvCarIdItem.setText(getItem(position)+"");
            return convertView;
        }
    }
    class ViewHolder{
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
