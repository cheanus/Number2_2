package com.qianxia.number;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import java.util.*;
import java.text.*;

public class SetActivity extends BaseActivity {

	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;
	public static final int PLAYER = 4;
	public static final int COMPUTER = 5;
	public static final int FOURFIVE = 20;
	public static final int FIVESIX = 30;
	public static final int SIXSEVEN = 42;
	public static final int SEVENEIGHT = 56;
	
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private Date passTime = null;
	private String message;
    private SimpleDateFormat ft = new SimpleDateFormat("mm:ss");
	
	private long record;
	private int difficulty;
	private int first;
	private int map;
	
	private TextView recordTime;
	private RadioButton easy, medium, hard, playerFirst, computer,
	    map20, map30, map42, map56;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		ImageView back = (ImageView) findViewById(R.id.Back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    SetActivity.this.finish();
			}
		});
		recordTime = (TextView) findViewById(R.id.recordTime);
	    easy = (RadioButton) findViewById(R.id.easy);
		medium = (RadioButton) findViewById(R.id.medium);
		hard = (RadioButton) findViewById(R.id.hard);
		playerFirst = (RadioButton) findViewById(R.id.playerFirst);
		computer = (RadioButton) findViewById(R.id.computerFirst);
		map20 = (RadioButton) findViewById(R.id.map20);
		map30 = (RadioButton) findViewById(R.id.map30);
		map42 = (RadioButton) findViewById(R.id.map42);
		map56 = (RadioButton) findViewById(R.id.map56);
		init();updateRecord();
	    RadioGroup groupDifficulty = (RadioGroup) findViewById(R.id.groupDifficulty);
	    RadioGroup groupFirst = (RadioGroup) findViewById(R.id.groupFirst);
	    RadioGroup groupMap = (RadioGroup) findViewById(R.id.groupMap);
		groupDifficulty.setOnCheckedChangeListener(
		    new RadioGroup.OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup groupView, int checkedId) {
		        switch (checkedId) {
			        case R.id.easy:editor.putInt("difficulty", EASY);break;
			        case R.id.medium:editor.putInt("difficulty", MEDIUM);break;
			        case R.id.hard:editor.putInt("difficulty", HARD);break;
		        }editor.apply();updateRecord();
		    }
		});
		groupFirst.setOnCheckedChangeListener(
		    new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup groupView, int checkedId) {
				switch (checkedId) {
					case R.id.playerFirst:editor.putInt("first", PLAYER);break;
					case R.id.computerFirst:editor.putInt("first", COMPUTER);break;
				}editor.apply();updateRecord();
			}
		});
		groupMap.setOnCheckedChangeListener(
		    new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup groupView, int checkedId) {
				switch (checkedId) {
					case R.id.map20:editor.putInt("map", FOURFIVE);break;
					case R.id.map30:editor.putInt("map", FIVESIX);break;
					case R.id.map42:editor.putInt("map", SIXSEVEN);break;
					case R.id.map56:editor.putInt("map", SEVENEIGHT);break;
				}editor.apply();updateRecord();
			}
		});
	}
	
	private void init() {
		difficulty = pref.getInt("difficulty", EASY);
	    first = pref.getInt("first", PLAYER);
	    map = pref.getInt("map", FOURFIVE);
		switch (difficulty) {
			case EASY:
				easy.setChecked(true);
				break;
			case MEDIUM:
				medium.setChecked(true);
				break;
			case HARD:
				hard.setChecked(true);
				break;
		}
		switch (first) {
			case PLAYER:
				playerFirst.setChecked(true);
				break;
			case COMPUTER:
				computer.setChecked(true);
				break;
		}
		switch (map) {
			case 20:
				map20.setChecked(true);
				break;
			case 30:
				map30.setChecked(true);
				break;
			case 42:
				map42.setChecked(true);
				break;
			case 56:
				map56.setChecked(true);
				break;
		}
	}
	
	private void updateRecord() {
		difficulty = pref.getInt("difficulty", EASY);
	    first = pref.getInt("first", PLAYER);
	    map = pref.getInt("map", FOURFIVE);
		record = pref.getLong(String.valueOf(100*map+10*first+difficulty), 0);
		passTime = new Date(record);
		message = "纪录:"+ft.format(passTime);
		recordTime.setText(message);
	}

}
