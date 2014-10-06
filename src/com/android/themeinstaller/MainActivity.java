package com.android.themeinstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	SharedPreferences sharedPreferences;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LinearLayout layout ;
		LinearLayout layout1 ;
		LinearLayout layout2 ;
		LinearLayout layout3 ;
		
		layout = (LinearLayout) findViewById(R.id.theme1);
		layout1 = (LinearLayout) findViewById(R.id.theme2);
		layout2 = (LinearLayout) findViewById(R.id.theme3);
		layout3 = (LinearLayout) findViewById(R.id.theme4);
		
		layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(),ThemeOneSelector.class);
                startActivity(i);
            }
        });
		
		layout1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(),ThemeTwoSelector.class);
                startActivity(i);
            }
        });
		
		layout2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(),ThemeThreeSelector.class);
                startActivity(i);
            }
        });
		
		layout3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(),ThemeFourSelector.class);
                startActivity(i);
            }
        });
		
		String launchinfo = getSharedPreferences("PrefsFile",MODE_PRIVATE).getString("version", "0");
		if (launchinfo.equals(getResources().getString(R.string.current_version))) {
			
		} 
		else  {
			progressDialog();
			CopyThemeTask task = new CopyThemeTask();
            task.execute();
		}
		
		storeSharedPrefs();
	}
	
	protected void progressDialog(){
		pd = new ProgressDialog(MainActivity.this);
		pd.setTitle("Copying files...");
		pd.setMessage("Copying necessary files. Dont exit now...");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.show();
	}
	
	protected void dismissDialog(){
		pd.dismiss();
	}
	
	protected void storeSharedPrefs() {
		SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //opens the editor
        editor.putString("version", getResources().getString(R.string.current_version));
        editor.commit();
    } 
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	private class CopyThemeTask extends AsyncTask<String, Void, String> {

		  
	    @Override
	    protected String doInBackground(String... filename) {
	      String response = "";
	      AssetManager assetManager = getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("Files");
	        } catch (IOException e) {
	            Log.e("tag", e.getMessage());
	        }

	        for(String themes : files) {
	        	System.out.println("File name => "+themes);
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	            	File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Themes/Data");

			    	wallpaperDirectory.mkdirs();
			    		
	              in = assetManager.open("Files/"+themes);   // if files resides inside the "Files" directory itself
	              out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/Themes/Data/" + themes);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;

        } catch (Exception e) {
        	Log.e("tag", "Failed to copy asset file: " + themes, e);
        }

	      }
	      return response;
	    }
	  

	    @Override
	    protected void onPostExecute(String result) {
            super.onPostExecute(result);
            
            dismissDialog();
	    }
	  }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow, menu);
        return super.onCreateOptionsMenu(menu);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        super.onOptionsItemSelected(item);
 
        switch(item.getItemId()){
            case R.id.share:
            	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
                sharingIntent.setType("text/plain");
                String shareBody = getResources().getString(R.string.play_store_link);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
 
            case R.id.rate:
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_link)));
            	startActivity(browserIntent);
                break;
 
            case R.id.mail:
            	Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + getResources().getString(R.string.email_id)));
            	intent.putExtra(Intent.EXTRA_SUBJECT, "Nero PA/CM11 Theme");
            	startActivity(intent);
                break;
 
        }
        return true;
 
    }
}
