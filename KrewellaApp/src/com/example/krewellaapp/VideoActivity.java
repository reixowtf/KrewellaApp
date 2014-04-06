package com.example.krewellaapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	private VideoView videoView;
	private MediaController mediaController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		String fileName = "android.resource://" + getPackageName()
				+ "/raw/krewella";
		videoView = (VideoView) findViewById(R.id.player_view);
		mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);

		videoView.setVideoURI(Uri.parse(fileName));
		videoView.start();
		mediaController.show();
		videoView.requestFocus();
	}
}
