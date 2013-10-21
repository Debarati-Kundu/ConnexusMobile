package com.example.connexusmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DisplayAllStreamsActivity extends Activity {

	public String AppURL = "http://dkconnexus.appspot.com/";
	public String AppServlet = "AllStreamsAPI";
	public String AppTotalTarget = AppURL + AppServlet;
	public String tstURL = "https://lh5.googleusercontent.com/-6M5SeBloZWg/UlL5OfD_apI/AAAAAAAAYME/w6VWLIjdp2A/w500-h400-no/no-cover-image.jpg";
	public List<Stream> recvStream;
	public ArrayList<String> coverURLs = new ArrayList<String>();
	public ArrayList<String> streamNames = new ArrayList<String>();
	public ArrayList<Bitmap> urlBitmaps = new ArrayList<Bitmap>();
	
	private GetSteamsFromServer mGetSteamsFromServer;
	private GetCoverImage mGetCoverImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_streams);
		setupActionBar();		
		mGetSteamsFromServer  = new GetSteamsFromServer();
	    mGetSteamsFromServer.execute(AppTotalTarget);

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(getResources(), R.id.gridView, options);
	    Integer imageHeight = options.outHeight;
	    Integer imageWidth = options.outWidth;
	    String imageType = options.outMimeType;
	    Log.d("Left", imageHeight.toString());
	    Log.d("Left", imageWidth.toString());
	    Log.d("Left", imageType);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("Left", "onCreateView()");
        return null;
    }

	public class GetCoverImage extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... coverlinks) {
			Bitmap ImageBitMap;
			ImageBitMap = GetBitMap(coverlinks[0]);
			urlBitmaps.add(ImageBitMap);
			return null;
		}
		
		Bitmap GetBitMap(String myURL) {
			Bitmap mBitmap = null;
			URL url = null;
			try
			{
				String urlPath = myURL;
				urlPath = urlPath.replace(" ", "%20"); // to replace any blank spaces
				url = new URL(urlPath);
				
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("GET");
				mBitmap = BitmapFactory.decodeStream(connection.getInputStream());
				return mBitmap;
			}
			catch (IOException e) 
			        {
			            e.printStackTrace();
			return null;
			        }
		}
	}
	public class GetSteamsFromServer extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... fromWebsite) {
			Gson gson = new Gson();
			Selector sel = new Selector();
			String tstJson = gson.toJson(sel);
			HttpResponse r = null;
            r = makeHTTPPOSTRequest(AppTotalTarget, tstJson);
            
            HttpEntity entity = r.getEntity();
            StringBuilder sb = new StringBuilder();
            InputStream content = null;
			try {
				content = entity.getContent();
			} catch (IllegalStateException e1) {
			} catch (IOException e1) {
			}
            BufferedReader br = new BufferedReader(new InputStreamReader(content));
            String str;
            try {
				while ((str = br.readLine()) != null) {
				        sb.append(str);
				}
			} catch (IOException e) {
			}
			return sb.toString();
		}
		
		public HttpResponse makeHTTPPOSTRequest(String apiUrl, String tstJson) {
			HttpResponse r = null;
	        try {
	            HttpClient c = new DefaultHttpClient();
	            HttpPost p = new HttpPost(apiUrl);
	            StringEntity se = new StringEntity(tstJson);
	            se.setContentEncoding("UTF-8");
	            se.setContentType("application/json");
	            p.setEntity(se);            
	            r = c.execute(p);             
	            final int statusCode = r.getStatusLine().getStatusCode();
	            Log.d(AppTotalTarget, "Return Code " + statusCode); // 200 indicates success          
	        }
	        catch(IOException e) {
	            System.out.println(e);
	        }
			return r;      
	    }
		
		protected void onPostExecute(String s) {
			Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Stream>>() {}.getType();
            recvStream = gson.fromJson(s, collectionType);
            for (Stream str: recvStream) {
            	streamNames.add(str.name);
            	if(str.coverImageUrl == "") str.coverImageUrl = tstURL;
            	coverURLs.add(str.coverImageUrl);
            	new GetCoverImage().execute(str.coverImageUrl);
            }			
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
