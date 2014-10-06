package com.android.themeinstaller;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class ThemeTwoSelector extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_two_selector);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(true);
		RelativeLayout theme;
		theme = (RelativeLayout) findViewById(R.id.theme2);		
		theme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {            		
                	showDialog();        		

            }
        });
	}
	
	protected void showDialog(){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThemeTwoSelector.this);		 
        alertDialog.setTitle("Install");
        alertDialog.setMessage("You are going to install " + getResources().getString(R.string.title_activity_theme_two_selector) + ". After installing please select the theme from CM theme chooser");
 
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	
            	Intent intent = new Intent(Intent.ACTION_VIEW);
            	 intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Themes/Data/Theme2.apk")), "application/vnd.android.package-archive");
            	startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
            }
        });

        alertDialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
	    }
	    return super.onOptionsItemSelected(item);
	}
}
