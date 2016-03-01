package com.jadyn.coolweather.common;

import java.util.Calendar;

/**
 * Created by JadynAi on 2016/3/1.
 */
public class CoolDate {
    public static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final int MONTH = Calendar.getInstance().get(Calendar.MONTH)+1;
    public static final int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
}
