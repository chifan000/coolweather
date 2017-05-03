package com.example.coolweather.db;

import com.example.coolweather.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	public static final String TAG = "CoolWeatherOpenHelper";
	
	/*
	 * Province的建表语句
	 */
	public static final String CREATE_PROVINCE = "create table Province(" + 
			"id integer primary key autoincrement, " + 
			"province_name text, province_code text)";
	
	/*
	 * City的建表语句
	 */
	public static final String CREATE_CITY = "create table City(id integer primary key autoincrement, "
			+ "city_name text, city_code text, province_id integer)";
	
	/*
	 * Country的建表语句
	 */
	public static final String CREATE_COUNTRY = "create table Country(" + 
			"id integer primary key autoincrement, country_name text, " + 
			"country_code text, city_id integer)";
	 

	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		LogUtil.d(TAG, "表Province创建成功");
		db.execSQL(CREATE_CITY);
		LogUtil.d(TAG, "表City创建成功");
		db.execSQL(CREATE_COUNTRY);
		LogUtil.d(TAG, "表Country创建成功");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
