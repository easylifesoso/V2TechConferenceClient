package com.v2tech.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import com.v2tech.R;
import com.v2tech.util.V2Log;

public class LogoActivity extends Activity {

	private final static int START_LOG_ON = 1;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		mHandler = new MessageHandler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Message m = Message.obtain(mHandler, START_LOG_ON);
		mHandler.sendMessageDelayed(m, 500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}

	private void start_log_on_activity() {
		this.startActivity(new Intent(this, com.v2tech.view.LoginActivity.class));
		finish();
	}

	class MessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_LOG_ON:
				start_log_on_activity();
				break;
			default:
				V2Log.e("Can't not handler msg type:" + msg.what);
			}
		}

	}
	
	
}
