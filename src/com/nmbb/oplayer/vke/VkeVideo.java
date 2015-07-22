package com.nmbb.oplayer.vke;

import android.graphics.Bitmap;


public class VkeVideo {
	private String mTitle;
	private String path;
	private String vid;
	private Bitmap bitmap;
	
	public VkeVideo() {
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public VkeVideo(String mTitle){
		this.mTitle=mTitle;
	}
	public VkeVideo(String mTitle, String path){
		this.mTitle=mTitle;
		this.path=path;
	}
	
	public VkeVideo(String mTitle, String path ,Bitmap bitmap){
		this.mTitle = mTitle;
		this.path = path;
		this.bitmap = bitmap;
	}
	
	public String getPath() {
		return path;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
