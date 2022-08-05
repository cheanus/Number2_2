package com.qianxia.number;
import android.app.*;
import android.os.*;
import android.media.*;

public class BaseActivity extends Activity {

    static protected  MediaPlayer player = new MediaPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
	}

	@Override
	protected void onPause() {
		player.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		player.start();
		super.onResume();
	}
	
}
