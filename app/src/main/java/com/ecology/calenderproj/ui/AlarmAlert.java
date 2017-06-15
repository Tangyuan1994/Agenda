package com.ecology.calenderproj.ui;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;


import com.ecology.calenderproj.R;

public class AlarmAlert extends Activity {
	// add by Wanyanyuan Tang
	MediaPlayer mediaPlayer1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaPlayer1 = MediaPlayer.create(this, R.raw.music);
		mediaPlayer1.start();

		new AlertDialog.Builder(AlarmAlert.this)
				.setIcon(R.drawable.pig)
				.setTitle("Réveil a sonné!!")
				.setMessage("BIG PIG Layez-vous！！！")
				.setPositiveButton("Je vais dormir encore~~~",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								mediaPlayer1.stop();
								AlarmAlert.this.finish();
								System.exit(0);
								android.os.Process.killProcess(android.os.Process.myPid());
							}
						}).show();
	}
}

