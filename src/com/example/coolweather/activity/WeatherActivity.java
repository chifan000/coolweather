package com.example.coolweather.activity;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.R.anim;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements View.OnClickListener{
	
	private LinearLayout weatherInfoLayout;
	
	/*
	 * 用于显示城市名字
	 */
	private TextView cityNameText;
	
	/*
	 *用于显示更新时间
	 */
	private TextView publishTimeText;
	
	/*
	 *用于显示当前日期
	 */
	private TextView currentDateText;
	
	/*
	 * 用于显示天气描述信息
	 */
	private TextView weatherDeshText;
	
	/*
	 * 用于显示最高温度
	 */
	private TextView temp1Text;
	
	/*
	 * 用于显示最低温度;
	 */
	private TextView temp2Text;
	
	private Button switchCity, refreshWeather;
	

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishTimeText = (TextView) findViewById(R.id.publish_time);
		currentDateText = (TextView) findViewById(R.id.current_data);
		weatherDeshText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);

		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		String countryCode = getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			publishTimeText.setText("同步中...");
			cityNameText.setVisibility(View.INVISIBLE);
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			//没有代号，直接显示本地
			showWeather();
		}
		
		
	}

	private void queryWeatherCode(String countryCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
		queryFromServer(address, "countryCode");
	}
	
	private void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	

	private void queryFromServer(final String address, final String type) {

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if("countryCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String weatherCode = response.split("\\|")[1];
						queryWeatherInfo(weatherCode);
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}});
				}
			}
			
			

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishTimeText.setText("同步失败！");
						Toast.makeText(WeatherActivity.this, "同步失败！", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		final String defValue = "";
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		showPublishTime();
		cityNameText.setText(prefs.getString("city_name", defValue));
		currentDateText.setText(prefs.getString("current_time", defValue));
		weatherDeshText.setText(prefs.getString("weather_desh", defValue));
		temp1Text.setText(prefs.getString("temp1", defValue));
		temp2Text.setText(prefs.getString("temp2", defValue));
		cityNameText.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
	}

	

	private void showPublishTime() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String publishTime = prefs.getString("publish_time", "");
		Date publishDate;
		Date currentDate;
		try {
			publishDate = sdf.parse(publishTime);
			currentDate = sdf.parse(sdf.format(new Date()));

			if (publishDate.getTime() > currentDate.getTime()) {
				publishTimeText.setText("昨天");
			} else {
				publishTimeText.setText("今天");
			}
			publishTimeText.append(publishTime + "发布");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city:
			ScaleAnimation sa1 = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			sa1.setDuration(200);
			ScaleAnimation sa2 = new ScaleAnimation(0.5f, 2.0f, 0.5f, 2.0f, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			sa2.setDuration(100);
			switchCity.startAnimation(sa1);
			switchCity.startAnimation(sa2);
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
					 Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(500);
			ra.setInterpolator(new AccelerateInterpolator());
			refreshWeather.startAnimation(ra);
			publishTimeText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

}
