package com.example.krewellaapp;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button ferFoto;
	private Button entrar;
	private ImageView imatge;
	private EditText nomCognoms;
	private boolean imatgeFeta;
	static final int CAMERA_APP_CODE = 100;
	private File tempImageFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imatgeFeta = false;

		nomCognoms = (EditText) findViewById(R.id.eT_nom_cognom);
		imatge = (ImageView) findViewById(R.id.imgV_fotografia);
		ferFoto = (Button) findViewById(R.id.btn_ferFoto);
		entrar = (Button) findViewById(R.id.btn_entrar);

		ferFoto.setOnClickListener(this);
		entrar.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ferFoto:
			ferFoto();
			break;
		case R.id.btn_entrar:
			if (nomCognoms.getText().toString().isEmpty()) {
				Toast.makeText(this, "Falta entrar el nom i el cognom", Toast.LENGTH_SHORT).show();
				/*if(nomCognoms.getText().toString().matches("[a-z|A-Z]+[\\s]*^[a-z|A-Z]+")){
					Toast.makeText(this, "Falta entrar el cognom", Toast.LENGTH_SHORT).show();
				}*/
			} else if (imatgeFeta) {
				Toast.makeText(this, "Falta fer la fotografia", Toast.LENGTH_SHORT).show();
			} else{
				//fer intent
			}
			break;
		default:
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
					imatge.setImageBitmap( Media.getBitmap(getContentResolver(),
							Uri.fromFile(tempImageFile)));
					imatge.setRotation(90);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
