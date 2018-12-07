package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.marliao.intelligenttransportation.R;
import com.marliao.intelligenttransportation.Utils.ConstantValue;
import com.marliao.intelligenttransportation.Utils.GenerateJsonUtil;
import com.marliao.intelligenttransportation.Utils.HttpUtil;
import com.marliao.intelligenttransportation.Utils.ResolveJson;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetRoadStatus;
import com.marliao.intelligenttransportation.enige.MyApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RoadStatusActivity extends AppCompatActivity {
    private Spinner spinnerSequence;
    private Button btnSearch;
    private ListView lvTableItem;
    private ImageView ivReturnActivity;
    private List<GetRoadStatus> mGetRoadStatusList;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_status);
        initUI();
        initData();
        setSpinnerData();//下拉列表的数据
    }

    private void initData() {
        mGetRoadStatusList = new ArrayList<>();
        for (int i=1;i<6;i++){
            initRoadStatusData(i);
        }
    }

    /**
     * 路况信息数据
     */
    private void initRoadStatusData(final Integer RoadId) {
        new Thread(){
            @Override
            public void run() {
                try {
                    String http=null;
                    if (SpUtil.getBoolean(RoadStatusActivity.this, ConstantValue.IPSETTING, false)) {
                        http=GenerateJsonUtil.GenerateHttp(SpUtil.getString(RoadStatusActivity.this,ConstantValue.IPVALUE,""));
                    }else {
                        http=MyApplication.HTTP;
                    }
                    String path=http+MyApplication.HTTPGETROADSTATUS;
                    String generateResult = GenerateJsonUtil.GenerateGetRoadStatus(RoadId);
                    String httpResult = HttpUtil.doPost(path, generateResult);
                    GetRoadStatus getRoadStatus = ResolveJson.ResolveGetRoadStatus(httpResult);
                    mGetRoadStatusList.add(getRoadStatus);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void setSpinnerData() {
        List<String> collationlist = new ArrayList<>();
        collationlist.add("红灯时长/s");
        collationlist.add("绿灯时长/s");
        collationlist.add("黄灯时长/s");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(collationlist);
        spinnerSequence.setAdapter(spinnerAdapter);
    }

    public class SpinnerAdapter extends BaseAdapter {
        private List<String> collationlist;

        public SpinnerAdapter(List<String> collationlist) {
            this.collationlist = collationlist;
        }

        @Override
        public int getCount() {
            return collationlist.size();
        }

        @Override
        public String getItem(int position) {
            return collationlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(RoadStatusActivity.this, R.layout.spinner_collation_item, null);
                viewHolder.tvSpinnerItem = (TextView) convertView.findViewById(R.id.tv_spinner_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvSpinnerItem.setText(getItem(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvSpinnerItem;
    }

    private void initUI() {
        spinnerSequence = (Spinner) findViewById(R.id.spinner_sequence);
        btnSearch = (Button) findViewById(R.id.btn_search);
        lvTableItem = (ListView) findViewById(R.id.lv_table_item);

        ivReturnActivity = (ImageView) findViewById(R.id.iv_return_activity);
        ivReturnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoadStatusActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
