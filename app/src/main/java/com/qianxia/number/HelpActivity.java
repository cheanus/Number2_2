package com.qianxia.number;
import android.app.*;
import android.os.*;
import android.widget.*;

public class HelpActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		TextView topTitle = (TextView) findViewById(R.id.topTitle);
		topTitle.setText("帮   助");
	}
	
}
