package com.example.coolweather.util;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

import android.text.TextUtils;

public class Utility {
	
	public static final String TAG = "Utility";
	
	/*
	 * 解析处理返回的省级数据
	 */
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, 
			String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length >0){
				for(String s : allProvinces){
					Province province = new Province();
					String[] array = s.split("\\|");
					province.setProviceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.savaProvince(province);
					LogUtil.d(TAG, province.toString());
				}
				return true;
			}
			
		}
		return false;
	}
	
	/*
	 * 解析处理返回的市级数据
	 */
	public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, 
			int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length >0 ){
				for(String s : allCities){
					String[] array = s.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析处理返回的县级数据
	 */
	public synchronized static boolean handleCountryResponse(CoolWeatherDB coolWeatherDB, String response, 
			int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String s : allCounties){
					String[] array = s.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}
}
