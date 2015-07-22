package com.nmbb.oplayer.vke;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class M3u8Url {
	
	/**
     * 将Json对象转换成Map
     * 
     * @throws JSONException
     */
	public static Map<String, String> JsonToMap(String jsonstring)throws JSONException{
		
		JSONObject jsonObject = new JSONObject(jsonstring);
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null, value = null;
		while(iterator.hasNext()){
			key = (String)iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}
	
	public static String GetM3u8Url(String url){
		if(url.equals("")){
			url="http://v.youku.com/v_show/id_XOTQ4MTE2OTQw.html";
		}
		
		
		
		String vid = Algorithm.getSuffix(url);
		String JsonUrl = "http://v.youku.com/player/getPlayList/VideoIDS/" + vid + "/Pf/4/ctype/12/ev/1";
		System.out.println(JsonUrl);
		
		String JsonContent = MyAsyncTask.ReadURL(JsonUrl);
//		Map<String, String> result = null;
//		try {
//			result = JsonToMap(JsonContent);
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		
		String oip = "" , now = "";
		
		for(int i=0;i<JsonContent.length();i++){
			if(i+3<JsonContent.length()&&JsonContent.substring(i, i+4).equals("\"ip\"")){
				for(int j=i+5;;j++){
					if(JsonContent.charAt(j)==',')break;
					oip += JsonContent.charAt(j);
				}
			}
			
			if(i+3<JsonContent.length()&&JsonContent.substring(i, i+4).equals("\"ep\"")){
				for(int j=i+6;;j++){
					if(JsonContent.charAt(j)=='"')break;
					now += JsonContent.charAt(j);
				}
				break;
			}
		}
		
//		Log.e("zhouchaoyuan", oip);
//		Log.e("zhouchaoyuan", now);
		
//		if(result.containsKey("ip"))
//			oip = result.get("ip");
//		if(result.containsKey("ep"))
//			now = result.get("ep");

		
		Algorithm.getEp(vid, now);
		String ep = Algorithm.pNew,token = Algorithm.token,sid = Algorithm.sid;
		String ep1 ="";
		try {
			ep1 = URLEncoder.encode(ep, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
//		System.out.println(vid);
//		System.out.println(oip);
//		System.out.println(ep1);
//		System.out.println(token);
//		System.out.println(sid);		

		
		//String http="http://pl.youku.com/playlist/m3u8?ctype=12&ep={0}&ev=1&keyframe=1&oip={1}&sid={2}&token={3}&type={4}&vid={5}";
		String http="http://pl.youku.com/playlist/m3u8?ctype=12&ep="+ep1+
				"&ev=1&keyframe=1&oip="+oip+"&sid="+sid+"&token="+token+"&type=mp4&vid="+vid;
		//System.out.println(http);
		return http;
	}

}
