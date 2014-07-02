/**
 * 
 */
package org.hekmatof.chesswatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Esa Hekmatizadeh
 * 
 */
public class WatchActivity extends Activity {
	private static final int normalColor = Color.WHITE;
	private static final int activeColor = 0xAA00BB00;
	private static final int finishedColor = Color.RED;

	private ChessTimer bTimer;
	private ChessTimer wTimer;

	private TextView wText;
	private TextView bText;

	private Button btnWhite;
	private Button btnBlack;
	private boolean isWhite = true;

	private Button btnPause;

	private boolean onPause = false;

	private int moveNum = 1;
	private TextView tvMoveNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.watch);

		int initTime = getIntent().getExtras().getInt("initTime");
		int moveTime = getIntent().getExtras().getInt("moveTime");

		setupWidget();
		initClocks(initTime, moveTime);
		wTimer.start();
	}

	private void setupWidget() {

		btnWhite = (Button) WatchActivity.this.findViewById(R.id.tickWhite);
		btnBlack = (Button) WatchActivity.this.findViewById(R.id.tickBlack);
		btnPause = (Button) WatchActivity.this.findViewById(R.id.btnPause);

		tvMoveNum = (TextView) WatchActivity.this.findViewById(R.id.tvMoveNum);
		tvMoveNum.setText(getText(R.string.moveNum).toString() + moveNum);

		wText = (TextView) findViewById(R.id.whiteTimer);
		bText = (TextView) findViewById(R.id.blackTimer);

		wText.setRotation(180);
		btnWhite.setRotation(180);

		btnWhite.setOnClickListener(new ButtonWhiteClick());
		btnBlack.setOnClickListener(new ButtonBlackClick());
		btnPause.setOnClickListener(new ButtonPauseClick());
	}

	private void initClocks(int initTime, int moveTime) {
		wText.setText("White: " + initTime + ":00");
		bText.setText("Black: " + initTime + ":00");

		wTimer = new ChessTimer(initTime, moveTime) {
			public void onTick() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateWhiteText();
						if (getRemainTimeInSecond() <= 0) {
							bTimer.stop();
							wTimer.stop();
							btnWhite.setClickable(false);
							btnBlack.setClickable(false);
							wText.setTextColor(finishedColor);
						}
					}
				});
			}

			@Override
			public void onPause() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateWhiteText();
						wText.setTextColor(normalColor);
					}
				});
			}

			@Override
			public void onStart() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateWhiteText();
						wText.setTextColor(activeColor);
					}
				});
			}
		};

		bTimer = new ChessTimer(initTime, moveTime) {

			@Override
			public void onTick() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateBlackText();
						if (getRemainTimeInSecond() <= 0) {
							bTimer.stop();
							wTimer.stop();
							btnBlack.setClickable(false);
							btnWhite.setClickable(false);
							bText.setTextColor(finishedColor);
						}
					}
				});

			}

			@Override
			public void onPause() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateBlackText();
						bText.setTextColor(normalColor);
					}
				});
			}

			@Override
			public void onStart() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateBlackText();
						bText.setTextColor(activeColor);
					}
				});
			}
		};

	}

	private void updateWhiteText() {
		int minute = wTimer.getRemainTimeInSecond() / 60;
		int second = wTimer.getRemainTimeInSecond() % 60;
		wText.setText("White: " + minute + ":" + second);
	}

	private void updateBlackText() {
		long minute = bTimer.getRemainTimeInSecond() / 60;
		long second = bTimer.getRemainTimeInSecond() % 60;
		bText.setText("Black: " + minute + ":" + second);
	}

	private class ButtonWhiteClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			wTimer.pause();
			bTimer.start();
			runOnUiThread(new Runnable() {
				public void run() {
					btnWhite.setClickable(false);
					btnBlack.setClickable(true);
				}
			});
			isWhite = false;
		}

	}

	private class ButtonBlackClick implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			bTimer.pause();
			wTimer.start();
			runOnUiThread(new Runnable() {
				public void run() {
					btnBlack.setClickable(false);
					btnWhite.setClickable(true);
				}
			});
			isWhite = true;

			moveNum++;
			runOnUiThread(new Runnable() {
				public void run() {
					tvMoveNum.setText(getText(R.string.moveNum).toString()
							+ moveNum);
				}
			});
		}
	}

	private class ButtonPauseClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (onPause) {
				if (isWhite) {
					wTimer.start();
					runOnUiThread(new Runnable() {
						public void run() {
							btnBlack.setClickable(false);
							btnWhite.setClickable(true);
						}
					});
				} else {
					bTimer.start();
					runOnUiThread(new Runnable() {
						public void run() {
							btnBlack.setClickable(true);
							btnWhite.setClickable(false);
						}
					});
				}
				runOnUiThread(new Runnable() {
					public void run() {
						btnPause.setText(R.string.pause);
						onPause = false;
					}
				});
			} else {
				bTimer.pauseOnPauseBtn();
				wTimer.pauseOnPauseBtn();
				onPause = true;
				runOnUiThread(new Runnable() {
					public void run() {
						btnPause.setText(R.string.unpause);
						btnBlack.setClickable(false);
						btnWhite.setClickable(false);
					}
				});
			}
		}
	}
}
