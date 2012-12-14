package com.example.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;



public class HttpPack {
	public static boolean hasConnected(Activity t) {
		final ConnectivityManager connMgr = (ConnectivityManager) t
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			return true;
		} else if (mobile.isAvailable()) {
			return true;
		}
		return false;

	}

	public static JSONObject getJsonByResponse(HttpResponse response) {
		StringBuilder builder = new StringBuilder();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}

			return new JSONObject(builder.toString());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<NameValuePair> buildParams(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		return params;
	}
	
	
	public static String getCookieByResponse(HttpResponse response) {
		Header[] header_list = response.getAllHeaders();
		Log.d(" -------- Current Header start --------", "----");
		for (Header h : header_list) {
			Log.d(h.getName(), h.getValue());
		}
		Log.d(" -------- Current Header end --------", "----");
		
		Header[] cookies = response.getHeaders("SET-COOKIE");
		String cookie_store = null;
		for (Header cookie : cookies) {
			cookie_store = cookie.getValue();
		}
		return cookie_store;
	}

	public static HttpResponse sendPost(String url, Map<String, String> params) {
		CookieStore cookieStore = new BasicCookieStore();  
        HttpContext localContext = new BasicHttpContext();  
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		List<NameValuePair> user_pairs = HttpPack.buildParams(params);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		
		try {
			// httppost.setHeader("Accept", "application/json");
			httppost.setHeader("User-Agent", "android");
			httppost.setEntity(new UrlEncodedFormEntity(user_pairs, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost, localContext);
			
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
