package com.example.krewellaapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Imatge> imatges = new ArrayList<Imatge>();

	public ImageAdapter(Context context, ArrayList<Imatge> imatges) {
		mContext = context;
		this.imatges = imatges;
	}

	@Override
	public int getCount() {
		return imatges.size();
	}

	@Override
	public Object getItem(int arg0) {
		return imatges.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setPadding(8, 8, 8, 8);

		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageBitmap(BitmapFactory.decodeResource(
				mContext.getResources(), imatges.get(position).getBmp()));
		return imageView;
	}
}
