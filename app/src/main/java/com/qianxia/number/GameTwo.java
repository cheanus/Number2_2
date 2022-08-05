package com.qianxia.number;

import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.widget.*;
import java.text.*;
import java.util.*;

public class GameTwo extends GameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		oneName = "玩家甲";twoName = "玩家乙";people = 2;
		super.onCreate(savedInstanceState);
		TextView topTitle = (TextView) findViewById(R.id.topTitle);
		topTitle.setText("双人游戏");
	}

}
