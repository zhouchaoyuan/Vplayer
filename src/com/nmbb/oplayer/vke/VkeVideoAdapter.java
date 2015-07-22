package com.nmbb.oplayer.vke;

import java.util.List;

import com.nmbb.oplayer.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VkeVideoAdapter extends ArrayAdapter<VkeVideo> {

	private int resourceId;
	public VkeVideoAdapter(Context context, int resource, List<VkeVideo> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}
	
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VkeVideo video = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.video_image);
		TextView textView  = (TextView) view.findViewById(R.id.video_name);
		imageView.setImageBitmap(video.getBitmap());
		textView.setText(video.getmTitle());
		
		LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
		lp.height = 200;
		lp.width = 250;
		imageView.setLayoutParams(lp);
		
		return view;
	}

}
