package com.gzp.statisticssdk.utils.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * author: Gzp
 * Create on 2018/7/2
 * Description:
 */
public abstract class AbstractDbHelper {

    private static final String TAG = "Statistics";

    /**
     * 数据库操作对象
     */
    protected SQLiteDatabase mDatabase = null;

    /**
     * 数据库管理对象
     */
    protected DBHelper mDBHelper = null;

    /**
     * @return 数据库名称
     */
    protected abstract String dbName();

    /**
     * @return 数据库版本号
     */
    protected abstract int dbVersion();

    protected abstract List<String> createTables();

    /**
     *
     * @param context
     */
    protected void open(Context context) {
        if (context == null) {
            Log.e(TAG, "Context can't not a null object");
            return;
        }
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context);
        }
        mDatabase = mDBHelper.getWritableDatabase();
    }

    protected void close() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }


    /**
     * 数据库管理类
     */
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, dbName(), null, dbVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(TAG, "start to create database tables!");
            if (sqLiteDatabase != null) {
                createTable(createTables(), sqLiteDatabase);
            }
            Log.d(TAG, "end create database tables!");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        }

        /**
         * 创建数据库表格
         *
         * @param tables
         */
        private void createTable(List<String> tables, SQLiteDatabase db) {
            if (tables == null || tables.size() == 0 || db == null) {
                Log.e(TAG, "创建数据库参数有误！");
                return;
            }

            try {
                db.beginTransaction();
                for (String table : tables) {
                    Log.d(TAG, "execute SQL:" + table);
                    db.execSQL(table);
                }
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                Log.e(TAG, "execute SQL error:" + e.toString());
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }

        }
    }




}
