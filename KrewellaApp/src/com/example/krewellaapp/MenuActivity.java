package com.example.krewellaapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener {
	private boolean play = true;
	private TextView txtviewMusica;
	private TextView nomCognom;
	private static final String AUDIO_PATH = ("krewella.mp3");
	private MediaPlayer mediaPlayer;
	private AssetFileDescriptor afd;
	private ImageView imatgePersonal;
	private ImageView imatgeReproduccio;
	private ImageView imatgeVideo;
	private ImageView imatgeBloc;
	private ImageView imatgeGravar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		inicialitzarComponents();
	}

	private void inicialitzarComponents() {
		imatgePersonal = (ImageView) findViewById(R.id.imatgePersona);
		try {
			Bitmap bmp = Media.getBitmap(getContentResolver(),
					Uri.fromFile((File) getIntent().getExtras().get("imatge")));
			bmp = escalarBitmap(bmp, 100, this);
			imatgePersonal.setImageBitmap(bmp);
			imatgePersonal.setRotation(90);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		nomCognom = (TextView) findViewById(R.id.salutacio_mes_nom);
		nomCognom.setText("Benvingut " + getIntent().getExtras().get("nom"));
		imatgeVideo = (ImageView) findViewById(R.id.imageVideo);
		imatgeVideo.setOnClickListener(this);
		imatgeBloc = (ImageView) findViewById(R.id.imageBlog);
		imatgeBloc.setOnClickListener(this);
		imatgeGravar = (ImageView) findViewById(R.id.imageGravar);
		imatgeGravar.setOnClickListener(this);
		afd = null;
		imatgeReproduccio = (ImageView) findViewById(R.id.imageViewReproduir);
		imatgeReproduccio.setOnClickListener(this);
		try {
			afd = getAssets().openFd("sound/" + AUDIO_PATH);
			new TascaCarrega().execute(afd);
			txtviewMusica = (TextView) findViewById(R.id.textViewNomMusica);
			txtviewMusica.setText(AUDIO_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		txtviewMusica.setText(AUDIO_PATH, null);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imageVideo: {
			startActivity(new Intent(this, VideoActivity.class));
			break;
		}
		case R.id.imageBlog: {
			startActivity(new Intent(this, ImatgesActivity.class));
			break;
		}
		case R.id.imageGravar: {// Gravar opinó
			mediaPlayer.pause();// Parem musica
			imatgeReproduccio.setImageResource(R.drawable.button_play);
			DialogGravacio dialeg = new DialogGravacio();
			dialeg.show(getFragmentManager(), "Dialeg");

			break;
		}
		case R.id.imageViewReproduir: {
			// canviem la imatge per una altre
			if (play) {
				mediaPlayer.pause();
				imatgeReproduccio.setImageResource(R.drawable.button_play);
			} else {
				mediaPlayer.start();
				imatgeReproduccio.setImageResource(R.drawable.button_pause);
			}
			play = !play;
			break;
		}
		}
	}

	private Bitmap escalarBitmap(Bitmap foto, int newHeight, Context context) {
		float densityMultiplier = context.getResources().getDisplayMetrics().density;
		int h = (int) (newHeight * densityMultiplier);
		int w = h * foto.getWidth() / foto.getHeight();
		foto = Bitmap.createScaledBitmap(foto, w, h, true);
		return foto;
	}

	// Quant acaba deixa anar els recursos que esta fent servir
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}

	@Override
	protected void onStop() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			imatgeReproduccio.setImageResource(R.drawable.button_play);
			play = !play;
		}
		super.onStop();
	}

	@Override
	protected void onPause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			imatgeReproduccio.setImageResource(R.drawable.button_play);
			play = !play;
		}
		super.onPause();
	}

	class TascaCarrega extends AsyncTask<AssetFileDescriptor, Void, Void> {
		ProgressDialog pd = new ProgressDialog(MenuActivity.this);

		@Override
		protected Void doInBackground(AssetFileDescriptor... params) {
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(params[0].getFileDescriptor());
				mediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaPlayer.start();
			return null;
		}
	}
}
