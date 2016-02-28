package com.jadyn.coolweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JadynAi on 2016/2/28.
 */
public class DBManager {
    private static String TAG = DBManager.class.getSimpleName();
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME="tong_weather.db";
    public static final String PACKGE_NAME="com.jadyn.coolweather";
    //手机里存放数据库文件的路径
    public static final String DB_PATH="/data"+ Environment.getDataDirectory().getAbsolutePath()
            +"/"+PACKGE_NAME;
    private SQLiteDatabase database;
    private Context context;


    public DBManager(Context context) {
        this.context = context;
    }


    public SQLiteDatabase getDatabase() {
        return database;
    }


    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }


    public void openDatabase() {
        Log.e(TAG, DB_PATH + "/" + DB_NAME);
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }


    private SQLiteDatabase openDatabase(String dbfile) {

        try {
            if (!(new File(dbfile).exists())) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(R.raw.tong_weather); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;
        } catch (FileNotFoundException e) {
            CoolLog.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            CoolLog.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }


    public void closeDatabase() {
        this.database.close();
    }
}
