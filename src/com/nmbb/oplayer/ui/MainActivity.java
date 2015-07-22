package com.nmbb.oplayer.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.nmbb.oplayer.OPlayerApplication;
import com.nmbb.oplayer.OPreference;
import com.nmbb.oplayer.R;
import com.nmbb.oplayer.service.MediaScannerService;
import com.nmbb.oplayer.ui.helper.FileDownloadHelper;
import com.nmbb.oplayer.ui.vitamio.LibsChecker;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener  {

	
	private ViewPager mPager;
	private final int TabCount=3;
	public FileDownloadHelper mFileDownload;
	
	public static MainActivity mMainActivity = null;
	
	public static Recommemed mRecommemed = null;
	public static FragmentOnline mFragmentOnline = null;
	public static FragmentFileOld mFragmentFileOld = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!LibsChecker.checkVitamioLibs(this, R.string.init_decoders))
			return;

		OPreference pref = new OPreference(this);
		//	首次运行，扫描SD卡
		//if (pref.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
			getApplicationContext().startService(new Intent(getApplicationContext(), MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));
		//}

		setContentView(R.layout.fragment_pager);
		
		mRecommemed = new Recommemed();
		mFragmentOnline = new FragmentOnline();
		mFragmentFileOld = new FragmentFileOld();
		
		// ~~~~~~ 添加导航栏
		setUpActionBar();		
		// ~~~~~~ 添加滑动
		setUpViewPager();
		// ~~~~~~ 添加导航项
		setUpTabs();
		
		mMainActivity = MainActivity.this;

	}
	
	private ActionBar actionbar ;
	
	private void setUpActionBar(){
		actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	private void setUpViewPager(){
		mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		// ~~~~~~ 绑定控件
		mPager = (ViewPager) findViewById(R.id.pager);
		// ~~~~~~ 绑定数据
		mPager.setAdapter(mAdapter);
		// ~~~~~~ 设置监听
		mPager.setOnPageChangeListener(mPagerListener);
	}
	
	private void setUpTabs(){
		actionbar = getActionBar();
		
		for(int i=0;i<TabCount;i++){
			Tab tab=actionbar.newTab();
			tab.setText(mAdapter.getPageTitle(i));
			tab.setTabListener(this);
			actionbar.addTab(tab);
		}
	}
	
	private ViewPager.SimpleOnPageChangeListener mPagerListener = new ViewPager.SimpleOnPageChangeListener() {
		public void onPageSelected(int position) {
			actionbar = getActionBar();  
            actionbar.setSelectedNavigationItem(position);
		}
	};
	
	public class ViewPagerAdapter extends FragmentPagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int arg0) {
			Fragment result = null;
			switch (arg0) {
			case 0:
				result = mRecommemed;
				break;
			case 1:
				result = mFragmentOnline;// 在线视频
				break;
			case 2:
			default:
				result = mFragmentFileOld;// 本地视频
				mFileDownload = new FileDownloadHelper(((FragmentFileOld) result).mDownloadHandler);
				break;
			}
			return result;
		}
		@Override
		public int getCount() {
			return TabCount;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			String tabLabel = null;  
            switch (position) {
                case 2:  
                    tabLabel = "本地视频";  
                    break;  
                case 1:  
                    tabLabel = "在线视频";  
                    break;   
                case 0:
                	tabLabel = "V客推荐";
            }  
            return tabLabel;  
		}
		
	}
	private ViewPagerAdapter mAdapter;
	
	
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		mPager.setCurrentItem(arg0.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	

	// 后退键的使用
	@Override
	public void onBackPressed() {
		if (getFragmentByPosition(mPager.getCurrentItem()).onBackPressed())
			return;
		else
			super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mFileDownload != null)
			mFileDownload.stopALl();
	}

	/** 查找Fragment */
	private FragmentBase getFragmentByPosition(int position) {
		return (FragmentBase) getSupportFragmentManager().findFragmentByTag("android:switcher:" + mPager.getId() + ":" + position);
	}


}
