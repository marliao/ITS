package com.marliao.intelligenttransportation.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.marliao.intelligenttransportation.R;

import java.util.ArrayList;
import java.util.List;

public class RoadStatusActivity extends AppCompatActivity {
    private Spinner spinnerSequence;
    private Button btnSearch;
    private ListView lvTableItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_status);
        initUI();
        setSpinnerData();
    }

    private void setSpinnerData() {
        List<String> collationlist=new ArrayList<>();
        collationlist.add("红灯时长/s");
        collationlist.add("绿灯时长/s");
        collationlist.add("黄灯时长/s");
    }

    private void initUI() {
        spinnerSequence = (Spinner) findViewById(R.id.spinner_sequence);
        btnSearch = (Button) findViewById(R.id.btn_search);
        lvTableItem = (ListView) findViewById(R.id.lv_table_item);
    }
}
