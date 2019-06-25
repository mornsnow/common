package com.mornsnow.common.util;

import com.mornsnow.common.util.model.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2018/3/21
 */
public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;

    /**
     * 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
     */
    private static final boolean BOUND_OR_VERTEX = true;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static final String POINT_SEPARATOR = ";";
    public static final String LOCATION_SEPARATOR = ",";

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 格式化后的距离
     */
    public static String getFormattedDistance(double lat1, double lng1, double lat2, double lng2) {
        return formatDistance(getDistance(lat1, lng1, lat2, lng2));
    }

    /**
     * 判断点是否在多边形内
     *
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return 点在多边形内返回true, 否则返回false
     */
    public static boolean isPointInPolygon(Location point, List<Location> pts) {

        int N = pts.size();
        //cross points count of x
        int intersectCount = 0;
        //浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;
        //neighbour bound vertices
        Location p1, p2;
        //当前点
        Location p = point;

        //left vertex
        p1 = pts.get(0);
        //check all rays
        for (int i = 1; i <= N; ++i) {
            if (p.equals(p1)) {
                return BOUND_OR_VERTEX;
            }
            //right vertex
            p2 = pts.get(i % N);
            //ray is outside of our interests
            if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
                p1 = p2;
                continue;//next ray left point
            }
            //ray is crossing over by the algorithm (common part of)
            if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
                //x is before of ray
                if (p.y <= Math.max(p1.y, p2.y)) {
                    //overlies on a horizontal ray
                    if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
                        return BOUND_OR_VERTEX;
                    }

                    //ray is vertical
                    if (p1.y == p2.y) {
                        //overlies on a vertical ray
                        if (p1.y == p.y) {
                            return BOUND_OR_VERTEX;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        //cross point of y
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        //overlies on a ray
                        if (Math.abs(p.y - xinters) < precision) {
                            return BOUND_OR_VERTEX;
                        }
                        //before ray
                        if (p.y < xinters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                //p crossing over p2
                if (p.x == p2.x && p.y <= p2.y) {
                    //next vertex
                    Location p3 = pts.get((i + 1) % N);
                    //p.x lies between p1.x & p3.x
                    if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            //next ray left point
            p1 = p2;
        }
        //偶数则在多边形外
        if (intersectCount % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(String lat1, String lng1, String lat2, String lng2) {
        return getDistance(Double.parseDouble(lat1), Double.parseDouble(lng1), Double.parseDouble(lat2), Double.parseDouble(lng2));
    }

    /**
     * 计算点是否在多边形内
     *
     * @param polygonStr 经纬度字符串，lng,lat示例：
     * @param lng
     * @param lat
     * @return
     */
    public static boolean isPointInPolygon(String polygonStr, Double lng, Double lat) {
        if (polygonStr == null || polygonStr.equals("")) {
            return false;
        }
        if (lng == null || lat == null) {
            return false;
        }
        List<Location> pdList = new ArrayList<>();
        for (String string : polygonStr.split(POINT_SEPARATOR)) {
            String[] lngLat = string.split(LOCATION_SEPARATOR);
            pdList.add(new Location(Double.parseDouble(lngLat[0]), Double.parseDouble(lngLat[1])));
        }
        if (pdList.isEmpty()) {
            return false;
        }
        return isPointInPolygon(new Location(lng, lat), pdList);
    }


    public static String formatDistance(double dis) {
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#");
        if (dis < 1000) {
            return df2.format(dis) + "m";
        } else {
            return df.format(dis / 1000) + "km";
        }
    }
}

