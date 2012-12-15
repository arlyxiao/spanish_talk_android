package com.example.logic;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;

abstract public class SpanishTalkBaseActivity extends Activity {
	public static String login_url = "http://192.168.1.17:3000/users/do_login";
	public static String register_url = "http://192.168.1.17:3000/users";
	
	
	public class RequestCode {
		public final static int NEW_TEXT = 9;
		public final static int FROM_ALBUM = 1;
		public final static int FROM_CAMERA = 2;
	}

	// ���ڶ��� go_back ��ť�ϵ��¼�����
	final public void go_back(View view) {
		on_go_back();
		this.finish();
	}
	
	// �˳��¼�
	public void clickLogout(View view) {
		SessionManagement session = new SessionManagement(getApplicationContext());
		session.logoutUser();
	}

	// ��һ���µ�activity���˷��������򻯵���
	final public void openActivity(Class<?> cls) {
		startActivity(new Intent(getApplicationContext(), cls));
	}
	
	final public void showFormNotice(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	// ���ӣ���������
	public void on_go_back() {
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public void saveUserSessionByResponse(HttpResponse response) {
		JSONObject user = HttpPack.getJsonByResponse(response);
		
		try {
			String username = user.getString("username");
			String user_id = user.getString("user_id");

			SessionManagement session = new SessionManagement(getApplicationContext());
			session.createLoginSession(user_id, username);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
