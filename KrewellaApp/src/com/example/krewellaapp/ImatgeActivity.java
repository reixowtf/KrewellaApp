package com.example.krewellaapp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ImatgeActivity extends Activity {

	private ImageView imatge;
	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imatge);
		
		imatge = (ImageView) findViewById(R.id.imatge_ampliada);
		text = (TextView) findViewById(R.id.text_imatge);
		
		Imatge img = (Imatge) getIntent().getSerializableExtra("imatge");
		imatge.setImageBitmap(BitmapFactory.decodeResource(getResources(), img.getBmp()));
		text.setText(img.getDescripcio());
	}
}
