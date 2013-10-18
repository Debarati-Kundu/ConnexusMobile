package com.example.connexusmobile;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

public class DisplayAllStreamsActivity extends Activity {

	public String AppURL = "http://apt-connexus.appspot.com/";
	public String AppServlet = "AllStreamsServletAPI";
	public String AppTotalTarget = AppURL + AppServlet;
	
	private GetSteamsFromServer mGetSteamsFromServer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_streams);
		setupActionBar();
		
		URL targetServer = null;
		try {
			targetServer = new URL("http://apt-connexus.appspot.com/AllStreamsServletAPI");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mGetSteamsFromServer  = new GetSteamsFromServer();
	    mGetSteamsFromServer.execute(AppTotalTarget);
	    

	}

	public class GetSteamsFromServer extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... AppTotalTarget) {
	//		Log.d(AppURL, AppTotalTarget.toString());
			Gson gson = new Gson();
			return null;
		}
	}
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_all_streams, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onDestroy() {
	    super.onDestroy();
	    if (mGetSteamsFromServer != null) {
	        mGetSteamsFromServer.cancel(true);
	    }
	}

}
