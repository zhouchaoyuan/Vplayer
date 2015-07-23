package com.nmbb.oplayer.ui;

import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.player.VideoActivity;
import com.nmbb.oplayer.vke.MyAsyncTask;
import com.nmbb.oplayer.vke.VkeVideo;
import com.nmbb.oplayer.vke.VkeVideoAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Recommemed extends Fragment {
	
	// ~~~访问的网站链接
	private static final String conToUrl="http://www.xtvke.com/";


	public static ListView mListView = null;
	public static ProgressBar mProgressBar = null;
	public static TextView tv;
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.heiya_recommended, container, false);
		tv = (TextView) view.findViewById(R.id.MyTv);
		mListView = (ListView) view.findViewById(R.id.mylistview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.MyprogressBar);
		
		tv.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);  
		
		initVideoFromVke();
				
		mListView.setOnItemClickListener(new OnItemClickListener() {  
  
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						VideoActivity.class);
				VkeVideo video = MyAsyncTask.mList.get(arg2);
				intent.setData(Uri.parse(video.getPath()));
				intent.putExtra("title", video.getmTitle());
				startActivity(intent);
			}  
        });  
		return view;
		
	}
	
	public void initVideoFromVke(){
		
//		Log.e("zhouchaoyuan", "initVideoFromVke");
		
		// ~~~后台加载视频,没有加载过才继续加载
		if(MyAsyncTask.mList.size()==0){
			MyAsyncTask Task = new MyAsyncTask();
			Task.execute(conToUrl,"1");
		}
		else{
			// ~~~加载过，直接复用
			VkeVideoAdapter adapter = new VkeVideoAdapter(MainActivity.mMainActivity, R.layout.vke_video_item, MyAsyncTask.mList);
			mListView.setAdapter(adapter);			
			mProgressBar.setVisibility(View.GONE);  
			tv.setVisibility(View.GONE);
		}
	}	
	
	
}
