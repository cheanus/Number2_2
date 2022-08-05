package com.qianxia.number;

import android.content.*;
import android.content.res.*;
import android.media.*;
import android.os.*;
import android.view.*;

public class MainActivity extends BaseActivity implements View.OnClickListener{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			AssetManager assetManager = this.getAssets();
			AssetFileDescriptor afd = assetManager.openFd("Subwoofer Lullaby.mp3");
			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(),
				afd.getStartOffset(), afd.getLength());
			player.setLooping(true);
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		findViewById(R.id.main1game).setOnClickListener(this);
		findViewById(R.id.main2game).setOnClickListener(this);
		findViewById(R.id.mainSet).setOnClickListener(this);
		findViewById(R.id.mainHelp).setOnClickListener(this);
		findViewById(R.id.mainAbout).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.main1game:
				intent = new Intent(MainActivity.this, GameOne.class);
				break;
			case R.id.main2game:
				intent = new Intent(MainActivity.this, GameTwo.class);
				break;
			case R.id.mainHelp:
			    intent = new Intent(MainActivity.this, HelpActivity.class);
			    break;
			case R.id.mainSet:
			    intent = new Intent(MainActivity.this, SetActivity.class);
			    break;
			case R.id.mainAbout:
				intent = new Intent(MainActivity.this, AboutActivity.class);
				break;
			default:
			    break;
		}
		if (intent != null) {
		startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		player.stop();
		super.onDestroy();
	}
}
