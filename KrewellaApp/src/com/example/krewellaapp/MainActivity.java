package com.example.krewellaapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private String STATE_PHOTO = "fotografia_feta";
	private String PHOTO_PATH = "path_fotografia";
	private String NOM_COGNOMS = "nom_cognoms";
	private Button ferFoto;
	private Button entrar;
	private ImageView imatge;
	private EditText nomCognoms;
	private boolean imatgeFeta;
	static final int CAMERA_APP_CODE = 100;
	private File tempImageFile;
	private Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			imatgeFeta = false;
		}
		nomCognoms = (EditText) findViewById(R.id.eT_nom_cognom);
		imatge = (ImageView) findViewById(R.id.imgV_fotografia);
		ferFoto = (Button) findViewById(R.id.btn_ferFoto);
		entrar = (Button) findViewById(R.id.btn_entrar);
		
		ferFoto.setOnClickListener(this);
		entrar.setOnClickListener(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PHOTO, imatgeFeta);
		if (imatgeFeta) {
			outState.putString(NOM_COGNOMS, nomCognoms.getText().toString());
			outState.putString(PHOTO_PATH, tempImageFile.getAbsolutePath());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	@Deprecated
	public Object onRetainNonConfigurationInstance() {
		return bmp;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		imatgeFeta = savedInstanceState.getBoolean(STATE_PHOTO);
		if (imatgeFeta) {
			nomCognoms.setText(savedInstanceState.getString(NOM_COGNOMS));
			imatge = (ImageView) findViewById(R.id.imgV_fotografia);
			imatge.setImageBitmap(bmp);
			//canviarOrientacio();
			imatgeFeta = true;
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ferFoto:
			ferFoto();
			break;
		case R.id.btn_entrar:
			if (nomCognoms.getText().toString().equalsIgnoreCase("")
					|| nomCognoms.getText().toString().equalsIgnoreCase(null)) {
				Toast.makeText(this, "Falta entrar el nom i el cognom",
						Toast.LENGTH_SHORT).show();
			} else if (!imatgeFeta) {
				Toast.makeText(this, "Falta fer la fotografia",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(this, MenuActivity.class);
				i.putExtra("nom", nomCognoms.getText().toString());
				i.putExtra("imatge", tempImageFile);
				startActivity(i);
				finish();
			}
			break;
		}
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void ferFoto() {
		if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			tempImageFile = crearFitxer();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(tempImageFile));
			startActivityForResult(takePictureIntent, CAMERA_APP_CODE);
		} else {
			Toast.makeText(this, "No hi ha cap aplicació per capturar fotos",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_APP_CODE) {
			if (resultCode == RESULT_OK) {
				try {
					bmp = Media.getBitmap(getContentResolver(),
							Uri.fromFile(tempImageFile));
					canviarOrientacio();
					imatge.setImageBitmap(bmp);
					imatgeFeta = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void canviarOrientacio() {
		if (bmp.getWidth() > bmp.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 70, bos);
		}
	}

	private File crearFitxer() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "foto" + timeStamp + ".jpg";
		File path = new File(Environment.getExternalStorageDirectory(),
				this.getPackageName());
		if (!path.exists())
			path.mkdirs();
		return new File(path, imageFileName);
	}

}
