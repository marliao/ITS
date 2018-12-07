package com.marliao.intelligenttransportation.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.marliao.intelligenttransportation.db.dao.Bus2BusStation;
import com.marliao.intelligenttransportation.db.dao.GetAllSense;
import com.marliao.intelligenttransportation.db.dao.GetCarAccountBalance;
import com.marliao.intelligenttransportation.db.dao.GetCarSpeed;
import com.marliao.intelligenttransportation.db.dao.GetLightSenseValue;
import com.marliao.intelligenttransportation.db.dao.GetParkFree;
import com.marliao.intelligenttransportation.db.dao.GetParkRate;
import com.marliao.intelligenttransportation.db.dao.GetRoadStatus;
import com.marliao.intelligenttransportation.db.dao.ParkFreeId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Json数据解析工具类
 */
public class ResolveJson {
    /**
     * 查询“所有传感器”的当前值
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetAllSense ResolveGetAllSense(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetAllSense getAllSense = new GetAllSense();
        getAllSense.setPm_2_5(object.getInt("pm2.5"));
        getAllSense.setCo_2(object.getInt("co2"));
        getAllSense.setLightIntensity(object.getInt("LightIntensity"));
        getAllSense.setHumidity(object.getInt("humidity"));
        getAllSense.setTemperature(object.getInt("temperature"));
        return getAllSense;
    }

    /**
     * 查询光照传感器阈值
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetLightSenseValue ResolveGetLightSenseValue(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetLightSenseValue getLightSenseValue = new GetLightSenseValue();
        getLightSenseValue.setDown(object.getString("Down"));
        getLightSenseValue.setUp(object.getString("Up"));
        return getLightSenseValue;
    }

    /**
     * 查询小车当前速度
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetCarSpeed ResolveGetCarSpeed(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetCarSpeed getCarSpeed = new GetCarSpeed();
        getCarSpeed.setCarSpeed(object.getInt("CarSpeed"));
        return getCarSpeed;
    }

    /**
     * 设置小车动作  /  小车账户充值  /  费率设置（返回ok/failed）
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static String ResolveSimple(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        String result = object.getString("result");
        return result;
    }

    /**
     * 查询小车账户余额
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetCarAccountBalance ResolveGetCarAccountBalance(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetCarAccountBalance getCarAccountBalance = new GetCarAccountBalance();
        getCarAccountBalance.setBanlance(object.getInt("Balance"));
        return getCarAccountBalance;
    }

    /**
     * 查询道路状态
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetRoadStatus ResolveGetRoadStatus(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetRoadStatus getRoadStatus = new GetRoadStatus();
        getRoadStatus.setStatus(object.getInt("Status"));
        return getRoadStatus;
    }

    /**
     * 查询当前费率信息
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetParkRate ResolveGetParkRate(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONObject object = new JSONObject(serverinfo);
        GetParkRate getParkRate = new GetParkRate();
        getParkRate.setCount(object.getString("RateType"));
        getParkRate.setMoney(object.getInt("Money"));
        return getParkRate;
    }

    /**
     * 查询当前空闲车位
     *
     * @param jsonStr
     * @return
     * @throws JSONException
     */
    public static GetParkFree ResolveGetParkFree(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONArray jsonArray = new JSONArray(serverinfo);
        GetParkFree getParkFree = new GetParkFree();
        List<ParkFreeId> parkFreeIdList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ParkFreeId parkFreeId = new ParkFreeId();
            parkFreeId.setParkFreeId(jsonArray.getJSONObject(i).getInt("ParkFreeId"));
            parkFreeIdList.add(parkFreeId);
        }
        getParkFree.setParkFreeIdLit(parkFreeIdList);
        return getParkFree;
    }

    /**
     * 解析公交车距站台的距离
     * @param jsonStr
     * @return
     */
    public static List<Bus2BusStation> ResolveGetBusStationInfo(String jsonStr) throws JSONException {
        String jsonTokener = JSONTokener(jsonStr);
        JSONObject jsonObject = new JSONObject(jsonTokener);
        String serverinfo = jsonObject.getString("serverinfo");
        JSONArray jsonArray = new JSONArray(serverinfo);
        List<Bus2BusStation> bus2BusStationList=new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Bus2BusStation bus2BusStation = new Bus2BusStation();
            bus2BusStation.setDistance(jsonArray.getJSONObject(i).getInt("Distance"));
            bus2BusStation.setBusId(jsonArray.getJSONObject(i).getInt("BusId"));
            bus2BusStationList.add(bus2BusStation);
        }
        return bus2BusStationList;
    }

    /**
     * Json字符串转码
     *
     * @param in
     * @return
     */
    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }

}
