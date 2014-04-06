package com.example.krewellaapp;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DialogGravacio extends DialogFragment implements
		View.OnClickListener {
	private static final String LOG_TAG = "DialogGravacio";
	private static String mFileName = null;
	private Button reproduir;
	private Button play;
	private MediaPlayer mPlay;
	private boolean gravar = true;
	private MediaRecorder mGravar = null;

	public DialogGravacio() {
		mFileName = "gravacioAudio.3gp";
		File path = new File(Environment.getExternalStorageDirectory(), LOG_TAG);
		if (!path.exists())
			path.mkdirs();

		mFileName = new File(path, mFileName).getAbsolutePath();
	}

	/**
	 * Iniciar la gravació
	 */
	private void startRecording() {
		mGravar = new MediaRecorder();
		mGravar.setAudioSource(MediaRecorder.AudioSource.MIC);
		mGravar.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mGravar.setOutputFile(mFileName);
		mGravar.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mGravar.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mGravar.start();
	}

	/**
	 * Aturar la gravació
	 */
	private void stopRecording() {
		mGravar.stop();
		mGravar.release();
		mGravar = null;
	}

	/**
	 * Iniciar la reproducció
	 */
	private void startPlaying() {
		mPlay = new MediaPlayer();
		try {
			mPlay.setDataSource(mFileName);
			mPlay.prepare();
			mPlay.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialeg_gravacio, null);
		builder.setView(v);

		reproduir = (Button) v.findViewById(R.id.btnreproduir);
		reproduir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startPlaying();
			}
		});
		play = (Button) v.findViewById(R.id.btnplay);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (gravar) {
					startRecording();
					play.setText(R.string.parar);
				} else {
					stopRecording();
					play.setText(R.string.reproduir);
				}
				gravar = !gravar;
			}
		});

		builder.setPositiveButton("Retornar", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(play.getText().toString().equalsIgnoreCase(getString(R.string.parar))){
					stopRecording();
				}
				dialog.cancel();
			}
		});

		AlertDialog dialeg = builder.create();
		return dialeg;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
