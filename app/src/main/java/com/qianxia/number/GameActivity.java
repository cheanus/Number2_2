package com.qianxia.number;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import android.app.*;
import java.text.*;

public class GameActivity extends BaseActivity implements View.OnClickListener{

	protected static final int ONE = 1;
	protected static final int TWO = 2;
	protected static final int NOCLICK = 3;
	protected static final int ONECLICK = 4;
	protected static final int TWOCLICK = 5;
	protected static final int CACHECLICK = 6;
	protected static final int ZERO = 7;
	protected static final int LONELY = 8;
	protected static final int FAR = 9;
	protected static final int BIG = 10;
	protected int alpha = 170;
	
	private SharedPreferences pref;
	protected int difficulty;
	protected int first;
	protected int map;
	private List<Map<String, Object>> data;
	protected Date date;
	protected String[][] number;
	protected int x = 0, y = 0, now = ONE, correct, people, size, win = 0;
	protected int[][] state, cache;
	protected String oneName = null, twoName = null;
	protected TextView nowName;
    protected AlertDialog.Builder dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		difficulty = pref.getInt("difficulty", SetActivity.EASY);
	    first = pref.getInt("first", SetActivity.PLAYER);
	    map = pref.getInt("map", SetActivity.FOURFIVE);
        dialog = new AlertDialog.Builder(this);
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.gameLayout);
		switch (difficulty) {
			case SetActivity.EASY:
				gameLayout.setBackgroundResource(R.drawable.easy);
				break;
			case SetActivity.MEDIUM:
				gameLayout.setBackgroundResource(R.drawable.medium);
				break;
			case SetActivity.HARD:
				gameLayout.setBackgroundResource(R.drawable.hard);
				break;
		}
	    switch (map) {
			case SetActivity.FOURFIVE:
				x = 4;y = 5;break;
			case SetActivity.FIVESIX:
				x = 5;y = 6;break;
			case SetActivity.SIXSEVEN:
			    x = 6;y = 7;break;
			case SetActivity.SEVENEIGHT:
				x = 7;y = 8;break;
			default:
			    break;
		}
		state = new int[x][y];cache = new int[x][y];
		for (int i = 0; i < map; i++) {
		    state[i%x][(int)i/x] = 0;cache[i%x][(int)i/x] = 0;
		}
		nowName = (TextView) findViewById(R.id.nowtext);
		reInit();now= now % 2 + 1;
		if (now == ONE) {
			nowName.setText(oneName);
		} else {
			nowName.setText(twoName);
		}
		number = new String[x][y];
		initData();
		if (people == 1) {
			if (first == SetActivity.PLAYER) {
				now = ONE;
			} else {
				now = TWO;
			}
		}
		
		GridLayout grid = (GridLayout) findViewById(R.id.grid);
		grid.setColumnCount(x);
		grid.setRowCount(y);
		for (int i = 0; i < map; i++) {
            
            Button item = new Button(this);
            item.setText(number[i % x][i / x]);
            item.setId(i);
			item.setBackgroundColor(Color.LTGRAY);
			item.getBackground().setAlpha(alpha);
			item.setOnClickListener(this);

            GridLayout.Spec rowSpec = GridLayout.spec(i / x, 1.0f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % x, 1.0f);
    
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.height = 0;
            layoutParams.width = 0;
			layoutParams.setGravity(Gravity.FILL);
			
            grid.addView(item, layoutParams);
        }
		Button yesButton = (Button) findViewById(R.id.yes);
		yesButton.setOnClickListener(this);
		date = new Date();
	}
	
	public void onClick(View v) {
		int a = v.getId() % x, b = (int) v.getId() / x, c = v.getId();
		if (win == 0 && 0<=v.getId() && v.getId()<map && state[a][b] == 0) {
			playerAction(a, b, c);
		}
		if (win == 0 && v.getId() == R.id.yes && ((people==1 && now==ONE)||(people==2))) {
			if (win != 0) {return;}
			if (correct == ZERO) {
				Toast.makeText(this, "请选择数后再确认", Toast.LENGTH_SHORT).show();return;
			}
			if (correct == LONELY) {
				Toast.makeText(this, "不能只点一个数", Toast.LENGTH_SHORT).show();return;
			} 
			if (correct == FAR) {
				Toast.makeText(this, "所点之数必须相邻", Toast.LENGTH_SHORT).show();return;
			}
			int numberSum = 0;
			for (int i = 0; i < map; i++) {
				if (cache[i%x][(int)i/x] == CACHECLICK) {
					numberSum += Integer.valueOf(number[i%x][(int)i/x]);
				}
			}
			if (numberSum > map/2*3) {
				Toast.makeText(this, "数之和大于规定"+map/2*3, Toast.LENGTH_SHORT).show();return;
			}
			reInit();
			isFinish();
			if (win == 0 && people == 1) {
			    computerAction();
				reInit();
			    isFinish();
			}
		}
	}

	protected void playerAction(int a, int b, int c) {
		if (cache[a][b] == 0) {
		    cache[a][b] = CACHECLICK;size++;
            ((Button) findViewById(c)).setBackgroundColor(Color.WHITE);

		} else {
			cache[a][b] = 0;size--;
			((Button) findViewById(c)).setBackgroundColor(Color.LTGRAY);
		}
		((Button) findViewById(c)).getBackground().setAlpha(alpha);
		if (isCorrect() == 0) {
			for (int i = 0; i < map; i++) {
				if (cache[i%x][(int)i/x] == CACHECLICK) {
					((Button) findViewById(i)).setBackgroundColor(Color.WHITE);
					((Button) findViewById(i)).getBackground().setAlpha(alpha);
				}
			}
		} else {
			for (int i = 0; i < map; i++) {
				if (cache[i%x][(int)i/x] == CACHECLICK && difficulty != SetActivity.HARD) {
					((Button) findViewById(i)).setBackgroundColor(Color.RED);
					((Button) findViewById(i)).getBackground().setAlpha(alpha);
				}
			} 
		}
		
	}

	protected void computerAction() {}
	
	protected void isFinish() {
		int a = 0,b = 0;
		for (int i = 0;i < map;i++) {
			a = i % x;b = (int) i / x;
			if ( state[a][b]==0&&
			    ((a!=0&&state[a-1][b]==0)||
				 (a!=x-1&&state[a+1][b]==0)||
				 (b!=0&&state[a][b-1]==0)||
				 (b!=y-1&&state[a][b+1]==0))
				) {win = 0;return;}
		}
		win = 1;long overTime = 0;Date passTime = null;
		String difficult = null, message = "";
		dialog.setTitle("结束");
		SimpleDateFormat ft = new SimpleDateFormat("mm:ss");
		date.setTime(new Date().getTime()-date.getTime());
	    SharedPreferences.Editor editor = pref.edit();
		if (people == 1) {
			switch (difficulty) {
				case SetActivity.EASY:difficult = "简单";break;
				case SetActivity.MEDIUM:difficult = "中等";break;
				case SetActivity.HARD:difficult = "困难";break;
			}
			if (now == ONE) {
				message = "AI胜利";
				dialog.setTitle("失败");
			} else {
				message = "玩家胜利";win = 2;
				dialog.setTitle("胜利");
				overTime = pref.getLong(String.valueOf(100*map+10*first+difficulty), 0);
				if (date.getTime() < overTime || overTime == 0 && win == 2) {
					passTime = new Date(overTime);
					message += "\n已破历史记录"+ft.format(passTime);
					editor.putLong(String.valueOf(100*map+10*first+difficulty), date.getTime());
				}
			}
		}
		else {
			if (now == TWO) {message = "玩家甲胜利";}
			else {message = "玩家乙胜利";}
		}
		editor.apply();
		if (people == 1) {
			message += "\n难度: "+difficult;
		    if (first == SetActivity.PLAYER) {
				message += "   先手：玩家先手";
			} else {
				message += "   先手：电脑先手";
			}
		}
		message += "\n时间: "+ft.format(date);
		
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.setPositiveButton("确定", new DialogInterface.
		    OnClickListener() {
		    @Override
		    public void onClick(DialogInterface p1, int p2) {
			}
		});
		dialog.show();
	}

	private void initData() {
		Random ran = new Random();
		int[] num = new int[map];
		int a, b, c;
		for (int i = 0; i < map; i++) {
	        num[i] = i + 1;
		}
		for (int i = 0; i < map / 3; i++) {
		    a = ran.nextInt(map / 2);
			b = ran.nextInt(map / 2);
			c = num[a];num[a] = num[b];num[b] = c;
			a = map / 2 + ran.nextInt(map / 2);
			b = map / 2 + ran.nextInt(map / 2);
			c = num[a];num[a] = num[b];num[b] = c;
		}
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0, odd = 0, even = map / 2; i < map; i++) {
			if (i%x%2 == ((int)i/x)%2) {
			    number[i%x][(int)i/x] = String.valueOf(num[odd]);odd++;
			} else {
				number[i%x][(int)i/x] = String.valueOf(num[even]);even++;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", number[i%x][(int)i/x]);
			data.add(map);
		}
	}
	
	protected void reInit() {
		Button item;
		for (int i = 0;i < map;i++) {
			if (cache[i%x][(int)i/x]==CACHECLICK) {
				if (now == ONE) {
					state[i%x][(int)i/x] = ONECLICK;
				} else {
					state[i%x][(int)i/x] = TWOCLICK;
				}
				item = (Button) findViewById(i);
				item.setVisibility(View.INVISIBLE);
			}
		}
		now = now % 2 + 1;
		if (now == ONE) {
			nowName.setText(oneName);
		} else {
			nowName.setText(twoName);
		}
		for (int i = 0; i < map; i++) {
		    cache[i%x][(int)i/x] = 0;
		}
		size = 0;correct = ZERO;
	}
	
	protected int isCorrect() {
		int a = 0, b = 0;
		correct = 0;
		if (size == 0) {
			correct = ZERO;
		} else if (size == 1) {
			correct = LONELY;
		} else {
			int numberSum = 0;
			for (int i = 0; i < map; i++) {
				if (cache[i%x][(int)i/x] == CACHECLICK) {
					numberSum += Integer.valueOf(number[i%x][(int)i/x]);
				}
			}
			if (numberSum > map/2*3) {correct = BIG;}
			for (int i = 0;i < map;i++) {
				a = i % x;b = (int) i / x;
				if ( cache[a][b]==CACHECLICK&&
				      (a==0||cache[a-1][b]==0)&&
				      (a==x-1||cache[a+1][b]==0)&&
					  (b==0||cache[a][b-1]==0)&&
					  (b==y-1||cache[a][b+1]==0)
				) {correct = FAR;break;}
			}
		}
		return correct;
	}
	
}
