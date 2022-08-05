package com.qianxia.number;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class TopLayout extends LinearLayout implements OnClickListener {
	
	public TopLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.top, this);
		ImageView topBack = (ImageView) findViewById(R.id.topBack);
		topBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topBack:
				((Activity) getContext()).finish();
				break;
			default:
			    break;
		}
	}
	
}
