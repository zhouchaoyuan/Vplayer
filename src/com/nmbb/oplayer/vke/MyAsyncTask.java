/**
 * created by chaoyuan
 * on 2015年7月14日
 */
package com.nmbb.oplayer.vke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.MainActivity;
import com.nmbb.oplayer.ui.Recommemed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

/**
 * @author chaoyuan
 *
 */
public class MyAsyncTask extends AsyncTask<String, Void, ArrayList<VkeVideo> > {

	// ~~~访问的网站链接
	private static final String conToUrl="http://www.xtvke.com/";
	
	// ~~~加载的视频列表
	public static ArrayList<VkeVideo> mList = new ArrayList<VkeVideo>();

	public MyAsyncTask(){
		;
	}
	
	public static String ReadURL(String arg0){
		
		URL url; 
		try {
			url = new URL(arg0);
			URLConnection connecttion  = url.openConnection();
			InputStream inStream = connecttion.getInputStream();
			InputStreamReader isr = new InputStreamReader(inStream,"gb2312");///网站原因，只能设置成gb2312
			BufferedReader bfr = new BufferedReader(isr);
			String Line;
			StringBuilder builder = new StringBuilder();
			while((Line = bfr.readLine()) != null){
				builder.append(Line+"\n");
			}				
			
			bfr.close();
			isr.close();
			inStream.close();
			return builder.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	// ~~~网络链接获取位图
	public Bitmap returnBitMap(String arg0){
		URL url;
		Bitmap bitmap=null;
		try {
			url = new URL(arg0);
			HttpURLConnection  connecttion  = (HttpURLConnection) url.openConnection();
			connecttion.setDoInput(true);
			connecttion.connect();
			InputStream is = connecttion.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	public String M3u8UrlToPlayPath(String arg0){
		String result = "";
		String []temp = ReadURL(arg0).split("\n");
		for(int i=0;i<temp.length;i++){
			if(temp[i].startsWith("http:")){
				for(int j=0;j<temp[i].length();j++){
					result += temp[i].charAt(j);
					
					if(j-3>=0&&temp[i].charAt(j)=='4'&&temp[i].charAt(j-1)=='p'
							&&temp[i].charAt(j-2)=='m'&&temp[i].charAt(j-3)=='.'){
						break;
					}
				}
				break;
			}
		}
		return result;
	}
	
	
	// ~~~返回视频播放列表
	@Override
	protected ArrayList<VkeVideo> doInBackground(String... arg0) {
		
		// ~~~http://www.xtvke.com/
		String result = ReadURL(arg0[0]);
		
		addToList(result);
		
		return mList;
	}
	@Override
	protected void onPostExecute(ArrayList<VkeVideo> result) {
		VkeVideoAdapter adapter = new VkeVideoAdapter(MainActivity.mMainActivity, R.layout.vke_video_item, result);
		Recommemed.mListView.setAdapter(adapter);
		
		Recommemed.mProgressBar.setVisibility(View.GONE);  
		Recommemed.tv.setVisibility(View.GONE);
		
		super.onPostExecute(result);
	}
	
	
	
	// ~~~添加视频到列表
	public void addToList(String result){
		boolean be =  false;
		String []temp = result.split("\n");
		for(int i=0;i<temp.length;i++){
			if(temp[i].equals("<body>"))be = true;
			if(temp[i].equals("</body>"))break;
			if(!be)continue;
			if(temp[i].contains("<li class=\"clearfix\">")){
				
				// ~~~非优酷视频，无法解析
				if(temp[i].contains("/a/channel/ad/2014/0501/345.html"))continue;
				
				Bitmap bitmap = null;
				int len=temp[i].length();				
				String mTitle = "", path = "", bitmapPath = "" ,now = "";
				
				for(int j=0;j<len;j++){
					//href
					if(j+3<temp[i].length()&&temp[i].substring(j, j+4).equals("href")){
						now = conToUrl;
						for(int k=j+6+1;k<len;k++){
							if(temp[i].charAt(k)=='"'){
								j=k;
								break;
							}
							now += temp[i].charAt(k);
						}
						now = getVideoPlayPath(now);
						String M3u8UrlPath = M3u8Url.GetM3u8Url(now);
						path = M3u8UrlToPlayPath(M3u8UrlPath);
						
					}
					//title atl
					if(j+2<len&&temp[i].substring(j, j+3).equals("alt")){
						for(int k=j+5;k<len;k++){
							if(temp[i].charAt(k)=='"'){
								j=k;
								break;
							}
							mTitle+=temp[i].charAt(k);
						}
					}
					//src
					if(j+2<len&&temp[i].substring(j, j+3).equals("src")){
						bitmapPath = conToUrl;
						for(int k=j+5+1;k<len;k++){
							if(temp[i].charAt(k)=='"'){
								j=k;
								break;
							}
							bitmapPath += temp[i].charAt(k);
						}
						bitmap = returnBitMap(bitmapPath);
					}
				}				mList.add(new VkeVideo(mTitle,path,bitmap));
				Log.e("zhouchaoyuan", mTitle);
				Log.e("zhouchaoyuan", path);
				Log.e("zhouchaoyuan", bitmapPath);
			}
		}
		
		
	}
	
	
	// ~~~把V客视频播放链接转换成优酷视频播放链接
	public String getVideoPlayPath(String url){
		
		String result = "";
		
		String []temp = ReadURL(url).split("\n");
		
		for(int i=0;i<temp.length;i++){
			if(temp[i].contains(".swf")){
				for(int j=0;j<temp[i].length();j++){
					if(j+3<temp[i].length()&&temp[i].subSequence(j, j+4).equals("sid/")){
						for(int k=j+4;k<temp[i].length();k++){
							if(temp[i].charAt(k)=='/')break;
							result+=temp[i].charAt(k);
						}
						break;
					}
				}
				break;
			}
		}
//		Log.e("weiyiping", "http://v.youku.com/v_show/id_"+result+".html");
		return "http://v.youku.com/v_show/id_"+result+".html";
	}
		

}
