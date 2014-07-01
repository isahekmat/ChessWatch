package org.hekmatof.chesswatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	private int initTime;
	private int moveTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btnStart).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							initTime = Integer
									.parseInt(((EditText) findViewById(R.id.etItime))
											.getText().toString());
							moveTime = Integer
									.parseInt(((EditText) findViewById(R.id.etMtime))
											.getText().toString());
							Intent i = new Intent(MainActivity.this,
									WatchActivity.class);
							i.putExtra("initTime", initTime);
							i.putExtra("moveTime", moveTime);
							startActivity(i);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}

					}

				});

	}

}
