package com.qianxia.number;

import android.os.*;
import android.widget.*;
import java.util.*;

public class GameOne extends GameActivity {

	private Random ran = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		oneName = "玩家";twoName = "AI";people = 1;
		super.onCreate(savedInstanceState);
		TextView topTitle = (TextView) findViewById(R.id.topTitle);
		topTitle.setText("单人游戏");
		if (first == SetActivity.COMPUTER) {
			computerAction();
			reInit();
			isFinish();
		}
	}

	@Override
	protected void computerAction() {
		int again = 0, max = 0, methodSize = 50;
		int[][][] method;int[] weight;
	    if (difficulty == SetActivity.EASY) {
			cache = createMethod(state);
		} else {
			if (difficulty == SetActivity.MEDIUM) {again = 50;}
			if (difficulty == SetActivity.HARD) {again = 100;}
		    method = new int[methodSize][x][y];weight = new int[methodSize];
			for (int i = 0;i < methodSize;i++) {method[i] = createMethod(state);}
			for (int i = 0;i < methodSize;i++) {weight[i] = wonNumber(method[i], again);}
			for (int i = 1,a = weight[0];i < methodSize;i++) {
				if (weight[i] > a) {
					a = weight[i];max = i;
				}
			}
			cache = method[max];
		}
	}
	
	private int[][] createMethod(int[][] create) {
		int length = 0,length2 = 0 ,start, longsize, a = 0, b = 0;
		int useful[] = new int[map],
		    useful2[] = new int[map],
		    method[][] = new int[x][y];
		for (int i = 0;i < map;i++) {
			method[i%x][(int)i/x]=0;useful2[i]=0;
			useful[i]=-1;a = i % x;b =(int)i / x;
			if (create[a][b]==0&&
			    ((a!=0&&create[a-1][b]==0)||
				 (a!=x-1&&create[a+1][b]==0)||
				 (b!=0&&create[a][b-1]==0)||
				 (b!=y-1&&create[a][b+1]==0))) {
				useful[length] = i;length++;
			}
		}
		if (length == 0) {method[0][0] = -1;return method;}
		a = ran.nextInt(length);
		start = useful[a];
		method[start%x][(int)start/x] = CACHECLICK;
		longsize = ran.nextInt(4)+2;
		int numberSum;
		for (int i = 0;i < longsize;i++, length2 = 0) {
			for (int j = 0;j < length;j++) {
				a = useful[j] % x;b = (int)useful[j] / x;
				if (method[a][b] == 0 &&
				    ((a!=0&&method[a-1][b]==CACHECLICK)||
					 (a!=x-1&&method[a+1][b]==CACHECLICK)||
					 (b!=0&&method[a][b-1]==CACHECLICK)||
					 (b!=y-1&&method[a][b+1]==CACHECLICK)) ) {
					useful2[length2] = useful[j];length2++;
				}
			}
			if (length2 == 0) {break;}
			a = useful2[ran.nextInt(length2)];
			numberSum = 0;
			for (int j = 0;j < map;j++) {
				if (method[j%x][(int)j/x] == CACHECLICK) {
				    numberSum += Integer.valueOf(number[j%x][(int)j/x]);
				}
			}
			if (numberSum + Integer.valueOf(number[a%x][(int)a/x]) > map/2*3) {
				break;
			}
			method[a%x][(int)a/x] = CACHECLICK;
			for (int k:useful2) {k=0;}
		}
		return method;
	}
	
	private int wonNumber(int[][] method, int again) {
		int wonSize = 0, even = 0;
		int[][] create = new int[x][y], manner = new int[x][y];
		for (int i = 0;i < again;i++, even = 0) {
			for (int j = 0;j < map;j++) {
				create[j%x][(int)j/x] = state[j%x][(int)j/x]
				    + method[j%x][(int)j/x];
			}
			while(true) {
				manner = createMethod(create);
				if (manner[0][0] == -1) {break;}
				for (int j = 0;j < map;j++) {
					if (manner[j%x][(int)j/x] == CACHECLICK) {
						create[j%x][(int)j/x] = 1;
					}
				}even++;
			}
			if (even % 2 == 0) {wonSize++;}
		}
		return wonSize;
	}
	
}
