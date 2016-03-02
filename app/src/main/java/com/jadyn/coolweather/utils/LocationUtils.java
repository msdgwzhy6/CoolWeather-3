package com.jadyn.coolweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by JadynAi on 2016/3/1.
 */
public class LocationUtils {
    public Context context;

    String provider;//位置提供器

    LocationManager locationManager;

    public LocationUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器，在不确定GPS定位功能是否开启的情况下
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(context, "没有可用的定位器，暂时不能提供位置呢，请小主自主选择位置",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void getLocatiton() {
        //权限检查
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != 
                PackageManager.PERMISSION_GRANTED && 
                ActivityCompat.checkSelfPermission(context, 
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //显示当前设备的位置信息
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新设备信息
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //获得当前的经纬度
    private String showLocation(Location location) {
        //当前的经纬度
        String currentPosition = "纬度是" + location.getLatitude() + "\n" + "经度是" +
                location.getLongitude();
        return currentPosition;
    }

    //移除位置监听器
    public void closeLocation() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, 
                    Manifest.permission.ACCESS_FINE_LOCATION) != 
                    PackageManager.PERMISSION_GRANTED && 
                    ActivityCompat.checkSelfPermission(context, 
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

}
