package com.example.connexusmobile;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

public class DisplayAllStreamsActivity extends Activity {

	public static String AppURL = "http://apt-connexus.appspot.com/";
	public static String AppServlet = "AllStreamsServletAPI";
	public static String AppTotalTarget = AppURL + AppServlet;
	
	private GetSteamsFromServer mGetSteamsFromServer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_streams);
		setupActionBar();
		
		mGetSteamsFromServer  = new GetSteamsFromServer();
	    mGetSteamsFromServer.execute(AppTotalTarget);
	    
	}

	public class GetSteamsFromServer extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... fromWebsite) {
			Log.d(AppTotalTarget, fromWebsite.toString());
			Gson gson = new Gson();
			Selector sel = new Selector();
			String tstJson = gson.toJson(sel);
            makeHTTPPOSTRequest(AppTotalTarget, tstJson);
			return null;
		}
	}
	
	public static void makeHTTPPOSTRequest(String apiUrl, String tstJson) {
        try {
            HttpClient c = new DefaultHttpClient();
            HttpPost p = new HttpPost("http://apt-connexus.appspot.com/AllStreamsServletAPI");//(apiUrl);
            StringEntity se = new StringEntity(tstJson);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            p.setEntity(se);            
            HttpResponse r = c.execute(p);              
            HttpEntity entity = r.getEntity();
            final int statusCode = r.getStatusLine().getStatusCode();
            Log.d(AppTotalTarget, "Error Code " + statusCode);
        }
        catch(IOException e) {
            System.out.println(e);
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
