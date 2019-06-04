package com.example.lenovo.learndb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;

public class DbHelper extends OrmLiteSqliteOpenHelper {

    //Database name
    private static final String DATABASE_NAME = "test.db";
    //Version of the database. Changing the version will call {@Link OrmLite.onUpgrade}
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION,R.raw.ormlite_config);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource,UserDetails.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private RuntimeExceptionDao<UserDetails, Integer> userDetails;

    public RuntimeExceptionDao<UserDetails, Integer> getUserDetails() throws SQLException {
        if (userDetails == null) {
            userDetails = getRuntimeExceptionDao(UserDetails.class);
        }
        return userDetails;
    }
}
