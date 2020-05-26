package com.whiteboard.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;

public class DistanceCalculate {

    // 以下为 获得 两点之间最短距离
    private static final BigDecimal EARTH_RADIUS = new BigDecimal(6378.137);//定义 地球半径
    private static final BigDecimal MATH_PI = new BigDecimal(Math.PI);//定义Math.PI

    /**计算弧长**/
    private static BigDecimal getRed(BigDecimal big){
        return big.multiply(MATH_PI).divide(new BigDecimal(180.0), BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算 两点 之间  的 最短距离 <br/>
     * 返回 数据 为 两点之间的 公里数
     */
    public static BigDecimal getShortestDistance(BigDecimal lat1,BigDecimal lng1,
                                                 BigDecimal lat2,BigDecimal lng2 ){

        BigDecimal radLat1 = getRed(lat1);
        BigDecimal radLat2 = getRed(lat2);
        BigDecimal a = radLat1.subtract(radLat2);
        BigDecimal b = getRed(lng1).subtract(getRed(lng2));

        Double sinA = Math.sin(a.doubleValue()/2);
        Double sinB = Math.sin(b.doubleValue()/2);
        Double cosA = radLat1.doubleValue();
        Double cosB = radLat2.doubleValue();

        Object obj = 2 * Math.asin(Math.sqrt(Math.pow(sinA,2) + Math.cos(cosA)*Math.cos(cosB)*Math.pow(sinB, 2)));
        BigDecimal s = new BigDecimal(obj.toString());
        s = s.multiply(EARTH_RADIUS);
        return format(s, 3);
    }
    public static BigDecimal format(BigDecimal big, int scale) {
        scale = 0 > scale ? 0 : scale;
        big = big.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return big;
    }

    public static void main(String[] args) {
        BigDecimal lat1 = new BigDecimal(28.1939842);
        BigDecimal lng1 = new BigDecimal(113.0108862);
        BigDecimal lat2 = new BigDecimal(22.636828);
        BigDecimal lng2 = new BigDecimal(113.814606);

        System.out.println("最短距离："+getShortestDistance(lat1, lng1, lat2, lng2));

    }

}