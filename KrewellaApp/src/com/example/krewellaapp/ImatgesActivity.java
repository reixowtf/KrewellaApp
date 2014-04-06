package com.example.krewellaapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImatgesActivity extends Activity {

	private ArrayList<Imatge> imatges = new ArrayList<Imatge>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imatges);
		
		String[] imatges_desc = getResources().getStringArray(R.array.ImatgesKrewella);

		imatges.add(new Imatge(imatges_desc[0], R.drawable.krew1));
		imatges.add(new Imatge(imatges_desc[1], R.drawable.krew2));
		imatges.add(new Imatge(imatges_desc[2], R.drawable.krew3));
		imatges.add(new Imatge(imatges_desc[3], R.drawable.krew4));
		imatges.add(new Imatge(imatges_desc[4], R.drawable.krew5));
		imatges.add(new Imatge(imatges_desc[5], R.drawable.krew6));
		imatges.add(new Imatge(imatges_desc[6], R.drawable.krew7));
		imatges.add(new Imatge(imatges_desc[7], R.drawable.krew8));
		imatges.add(new Imatge(imatges_desc[8], R.drawable.krew9));
		
		GridView gridview = (GridView) findViewById(R.id.gridView_krewellaImatges);
		gridview.setAdapter(new ImageAdapter(this, imatges));

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {
				Intent i = new Intent(v.getContext(), ImatgeActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("imatge", imatges.get(p));
				i.putExtras(b);
				startActivity(i);
			}
		});
	}
}
