/**
 * 
 */
package org.hekmatof.chesswatch;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Esa Hekmatizadeh
 * 
 */
public abstract class ChessTimer {

	private TimerTask timer;

	private Timer taskTimer;
	private int remainTimeInSecond = 0;

	private int moveTime;
	private boolean running = false;

	public ChessTimer(int minute, int moveTime) {
		remainTimeInSecond = minute * 60;
		this.moveTime = moveTime;
		timer = new TimerTask() {

			@Override
			public void run() {
				if (running) {
					remainTimeInSecond--;
					onTick();
				}
			}
		};
		this.taskTimer = new Timer();
		this.taskTimer.schedule(timer, 0, 1000);
	}

	public void start() {
		onStart();
		running = true;
	}

	public void pause() {
		running = false;
		remainTimeInSecond += moveTime;
		onPause();
	}

	public void stop() {
		this.taskTimer.cancel();
	}

	public int getRemainTimeInSecond() {
		return remainTimeInSecond;
	}

	public abstract void onTick();

	public abstract void onPause();

	public abstract void onStart();

}
