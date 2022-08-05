package com.qianxia.number;
import android.app.*;
import android.os.*;
import android.widget.*;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView topTitle = (TextView) findViewById(R.id.topTitle);
		topTitle.setText("关   于");
	}
	
}
