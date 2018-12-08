package com.marliao.intelligenttransportation.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.marliao.intelligenttransportation.Utils.Sequence;
import com.marliao.intelligenttransportation.Utils.SpUtil;
import com.marliao.intelligenttransportation.db.dao.GetRoadStatus;
import com.marliao.intelligenttransportation.db.dao.GetTrafficLightConfigAction;
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
    private List<String> collationlist;
    private List<String> mCollationlist1;
    private String mItemSelected;
    private List<GetTrafficLightConfigAction> mGetTrafficLightConfigActionList;
    private TableAdapter mTableAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_status);
        initUI();
        tabularData();
        setSpinnerData();//下拉列表的数据
        //获取下拉列表选择的选项
        spinnerSequence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mItemSelected = mCollationlist1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按指定规则排序
                if (mItemSelected.equals("红灯降序")) {
                    Sequence.redLightDescending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("绿灯降序")) {
                    Sequence.greenLightDescending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("黄灯降序")) {
                    Sequence.yellowLightDescending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("路口降序")) {
                    Sequence.roadDescending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("绿灯升序")) {
                    Sequence.greenLightAscending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("黄灯升序")) {
                    Sequence.yellowLightAscending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("路口升序")) {
                    Sequence.roadLightAscending(mGetTrafficLightConfigActionList);
                } else if (mItemSelected.equals("红灯升序")) {
                    Sequence.redLightAscending(mGetTrafficLightConfigActionList);
                }
                if (mTableAdapter != null) {
                    mTableAdapter.notifyDataSetChanged();
                }
                for (int i = 0; i < mGetTrafficLightConfigActionList.size(); i++) {
                    Log.i("+++++", String.valueOf(mGetTrafficLightConfigActionList.get(i).getRoadId()));
                }
            }
        });

    }


    private void tabularData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String http = null;
                    if (SpUtil.getBoolean(RoadStatusActivity.this, ConstantValue.IPSETTING, false)) {
                        http = GenerateJsonUtil.GenerateHttp(SpUtil.getString(RoadStatusActivity.this, ConstantValue.IPVALUE, ""));
                    } else {
                        http = MyApplication.HTTP;
                    }
                    String path = http + MyApplication.HTTPGETTRAFFICLIGHTCONFIGACTION;
                    mGetTrafficLightConfigActionList = new ArrayList<>();
                    for (int i = 1; i < 6; i++) {
                        String generateResult = GenerateJsonUtil.GenerateGetTrafficLightConfigAction(i);
                        String httpResult = HttpUtil.doPost(path, generateResult);
                        GetTrafficLightConfigAction getTrafficLightConfigAction = ResolveJson.ResolveGetTrafficLightConfigAction(httpResult, i);
                        mGetTrafficLightConfigActionList.add(getTrafficLightConfigAction);
                    }
                    //默认按红灯时长升序排列
                    Sequence.redLightAscending(mGetTrafficLightConfigActionList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTableAdapter != null) {
                                mTableAdapter.notifyDataSetChanged();
                            }
                            //准备数据适配器
                            mTableAdapter = new TableAdapter(mGetTrafficLightConfigActionList);
                            lvTableItem.setAdapter(mTableAdapter);
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
        mCollationlist1 = new ArrayList<>();
        mCollationlist1.add("红灯升序");
        mCollationlist1.add("绿灯升序");
        mCollationlist1.add("黄灯升序");
        mCollationlist1.add("路口升序");
        mCollationlist1.add("红灯降序");
        mCollationlist1.add("绿灯降序");
        mCollationlist1.add("黄灯降序");
        mCollationlist1.add("路口降序");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(mCollationlist1);
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

    public class TableAdapter extends BaseAdapter {
        List<GetTrafficLightConfigAction> getTrafficLightConfigActionList;

        public TableAdapter(List<GetTrafficLightConfigAction> mGetTrafficLightConfigActionList) {
            this.getTrafficLightConfigActionList = mGetTrafficLightConfigActionList;
        }

        @Override
        public int getCount() {
            return getTrafficLightConfigActionList.size();
        }

        @Override
        public GetTrafficLightConfigAction getItem(int position) {
            return getTrafficLightConfigActionList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(RoadStatusActivity.this, R.layout.table_light_item, null);
                viewHolder.tvRoadId = (TextView) convertView.findViewById(R.id.tv_road_id);
                viewHolder.tvRedLight = (TextView) convertView.findViewById(R.id.tv_red_light);
                viewHolder.tvGreenLight = (TextView) convertView.findViewById(R.id.tv_green_light);
                viewHolder.tvYellowLight = (TextView) convertView.findViewById(R.id.tv_yellow_light);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvRoadId.setText(getItem(position).getRoadId() + "号路口");
            viewHolder.tvRedLight.setText(getItem(position).getRedTime() + "");
            viewHolder.tvGreenLight.setText(getItem(position).getGreenTime() + "");
            viewHolder.tvYellowLight.setText(getItem(position).getYellowTime() + "");
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvSpinnerItem;
        TextView tvRoadId;
        TextView tvRedLight;
        TextView tvGreenLight;
        TextView tvYellowLight;
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
