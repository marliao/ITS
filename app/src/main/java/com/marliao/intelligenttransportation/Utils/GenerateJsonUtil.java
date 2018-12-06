package com.marliao.intelligenttransportation.Utils;

/**
 * 生成Json字符串用作请求参数的工具类
 */
public class GenerateJsonUtil {
    /**
     *  查询小车当前速度 \ 查询小车账户余额 \
     * @param CarId
     * @return
     */
    public static String GenerateSimple(Integer CarId){
        String jsonStr="{\"CarId\":"+CarId+"}";
        return jsonStr;
    }

    /**
     * 设置小车动作
     * @param CarId
     * @param CarAction
     * @return
     */
    public static String GenerateSetCarMove(Integer CarId,String CarAction){
        String jsonStr="{\"CarId\":"+CarId+",\"CarAction\":\""+CarAction+"\"}";
        return jsonStr;
    }

    /**
     * 小车账户充值
     * @param CarId
     * @param Money
     * @return
     */
    public static String GenerateSetCarAccountRecharge(Integer CarId,Integer Money){
        String jsonStr="{\"CarId\":"+CarId+",\"CarAction\":"+Money+"}";
        return jsonStr;
    }

    /**
     * 查询道路状态
     * @param RoadId
     * @return
     */
    public static String GenerateGetRoadStatus(Integer RoadId){
        String jsonStr="{\"RoadId\":"+RoadId+"}";
        return jsonStr;
    }

    /**
     * 费率设置
     * @param RateType
     * @param Money
     * @return
     */
    public static String GenerateSetParkRate(String RateType,Integer Money){
        String jsonStr="{\"RateType\":"+RateType+",\"Money\":"+Money+"}";
        return jsonStr;
    }

    /**
     * 公交车查询
     * @param BusStationId
     * @return
     */
    public static String GenerateGetBusStationInfo(Integer BusStationId){
        String jsonStr="{\"BusStationId\":"+BusStationId+"}";
        return jsonStr;
    }

}
