package org.hekmatof.chesswatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int normalColor = Color.BLACK;
	private static final int activeColor = 0xAA00BB00;
	private static final int finishedColor = Color.RED;
	private int initTime;
	private int moveTime;

	private ChessTimer bTimer;
	private ChessTimer wTimer;
	private boolean isWhite = true;

	private TextView wText;
	private TextView bText;

	private Button btnToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btnStart).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try{
						initTime = Integer
								.parseInt(((EditText) findViewById(R.id.etItime))
										.getText().toString());
						moveTime = Integer
								.parseInt(((EditText) findViewById(R.id.etMtime))
										.getText().toString());
						}
						catch(Exception e){
							return;
						}
						setContentView(R.layout.watch);
						initClocks();
						btnToggle = (Button) MainActivity.this
								.findViewById(R.id.btnToggle);
						btnToggle
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										if (isWhite) {
											wTimer.pause();
											bTimer.start();
											isWhite = false;
										} else {
											bTimer.pause();
											wTimer.start();
											isWhite = true;
										}
									}
								});

						wTimer.start();
					}

				});

	}

	private void initClocks() {
		wText = (TextView) findViewById(R.id.whiteTimer);
		bText = (TextView) findViewById(R.id.blackTimer);

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
							btnToggle.setClickable(false);
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
							btnToggle.setClickable(false);
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
}
