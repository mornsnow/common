package com.mornsnow.common.util.model;

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
public class Location {
    private Double lng;
    private Double lat;
    public double x;
    public double y;

    public Location(Double lng, Double lat) {
        this.lat = lat;
        this.lng = lng;
        this.x = lng == null ? 0d : lng;
        this.y = lat == null ? 0d : lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
        this.x = lng == null ? 0d : lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
        this.y = lat == null ? 0d : lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location location = (Location) obj;
            return location != null && this.x == location.x && this.y == location.y;
        }

        return false;
    }
}
