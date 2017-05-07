package com.example.coolweather.activity;

import java.security.PublicKey;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity{
	
	private LinearLayout weatherInfoLayout;
	
	/*
	 * ������ʾ��������
	 */
	private TextView cityNameText;
	
	/*
	 *������ʾ����ʱ��
	 */
	private TextView publishTimeText;
	
	/*
	 *������ʾ��ǰ����
	 */
	private TextView currentDateText;
	
	/*
	 * ������ʾ����������Ϣ
	 */
	private TextView weatherDeshText;
	
	/*
	 * ������ʾ����¶�
	 */
	private TextView temp1Text;
	
	/*
	 * ������ʾ����¶�;
	 */
	private TextView temp2Text;

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
		String countryCode = getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			publishTimeText.setText("ͬ����...");
			cityNameText.setVisibility(View.INVISIBLE);
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			//û�д��ţ�ֱ����ʾ����
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
						publishTimeText.setText("ͬ��ʧ�ܣ�");
						Toast.makeText(WeatherActivity.this, "ͬ��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		String defValue = "";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", defValue));
		publishTimeText.setText("����" + prefs.getString("publish_time", defValue) + "����");
		currentDateText.setText(prefs.getString("current_time", defValue));
		weatherDeshText.setText(prefs.getString("weather_desh", defValue));
		temp1Text.setText(prefs.getString("temp1", defValue));
		temp2Text.setText(prefs.getString("temp2", defValue));
		cityNameText.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
	}

}
