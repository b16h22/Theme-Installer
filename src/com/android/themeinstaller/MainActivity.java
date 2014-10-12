package com.android.themeinstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
	private ProgressDialog pd;
    List<Theme> ThemeList;
    String[] ThemeNames;
    String[] ThemePackages;
    String[] ThemeColors;
    Theme currentTheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        ThemeList = new ArrayList<Theme>();

        ThemeNames = getResources().getStringArray(R.array.theme_names);
        ThemePackages = getResources().getStringArray(R.array.theme_packages);
        ThemeColors = getResources().getStringArray(R.array.theme_colors);

        PopulateThemeList();
        PopulateThemeListview();
        RegisterThemeClickCallback();

		String launchinfo = getSharedPreferences("PrefsFile", MODE_PRIVATE).getString("version", "0");

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
		sharedPreferences = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //opens the editor
        editor.putString("version", getResources().getString(R.string.current_version));
        editor.apply();
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
	        	System.out.println("File name => " + themes);
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	            	File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Themes/Data");

			    	wallpaperDirectory.mkdirs();
			    		
	              in = assetManager.open("Files/" + themes);   // if files resides inside the "Files" directory itself
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
                Share(getResources().getString(R.string.share_link));
                break;
 
            case R.id.rate:
                Rate(getResources().getString(R.string.rate_link));
                break;
 
            case R.id.mail:
                Mail(getResources().getString(R.string.email_subject), getResources().getString(R.string.email_address));
                break;
 
        }
        return true;
 
    }

    public void Share(String playStoreLink){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, playStoreLink);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void Rate(String playStoreLink){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
        startActivity(browserIntent);
    }

    public void Mail(String themeName, String email){
        Intent mailIntent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + email));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, themeName);
        startActivity(mailIntent);
    }

    public void Link(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    public int GetDrawable(String path){
        return getResources().getIdentifier(path, null, getPackageName());
    }

    public void PopulateThemeList(){
        int i = 0;
        while (i < getResources().getStringArray(R.array.theme_names).length){
            ThemeList.add(new Theme(ThemeNames[i], ThemePackages[i], GetDrawable("drawable/theme_icon" + String.valueOf(i + 1)), GetDrawable("drawable/theme_preview" + String.valueOf(i + 1)), ThemeColors[i]));
            i++;
        }
    }


    public void PopulateThemeListview(){
        ThemeListAdapter ThemeListAdapter = new MainActivity.ThemeListAdapter();
        ListView ThemeListView = (ListView) findViewById(R.id.theme_listview);
        ThemeListView.setAdapter(ThemeListAdapter);
        RegisterThemeClickCallback();
    }

    public void RegisterThemeClickCallback(){
        ListView ThemeListView = (ListView) findViewById(R.id.theme_listview);
        ThemeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ThemeActivity.class);

                intent.putExtra("SelectedTheme", String.valueOf(position + 1));
                intent.putExtra("ThemeName", ThemeList.get(position).getTheme_name());
                intent.putExtra("ThemePackage", ThemeList.get(position).getTheme_package());
                intent.putExtra("ThemeIcon", ThemeList.get(position).getTheme_icon());
                intent.putExtra("ThemePreview", ThemeList.get(position).getTheme_preview());
                intent.putExtra("ThemeColor", ThemeList.get(position).getTheme_color());
                intent.putExtra("ThemeColorString", ThemeList.get(position).getTheme_color_string());

                MainActivity.this.startActivity(intent);
            }
        });
    }

    private class ThemeListAdapter extends ArrayAdapter<Theme> {
        public ThemeListAdapter() {
            super(MainActivity.this, R.layout.listview_item_theme, ThemeList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Make sure we have a view to work with (may have been given null)
            View ThemeItemTemplate = convertView;
            if (ThemeItemTemplate == null) {
                ThemeItemTemplate = getLayoutInflater().inflate(R.layout.listview_item_theme, parent, false);
            }

            currentTheme = ThemeList.get(position);

            TextView themeName = (TextView) ThemeItemTemplate.findViewById(R.id.theme_name);
            themeName.setText(currentTheme.getTheme_name());

            ImageView themeIcon = (ImageView) ThemeItemTemplate.findViewById(R.id.theme_icon);
            themeIcon.setImageResource(currentTheme.getTheme_icon());

            return ThemeItemTemplate;
        }
    }
}

class Theme {
    String theme_name;
    String theme_package;
    int theme_icon;
    int theme_preview;
    int theme_color;
    String theme_color_string;

    public Theme(String theme_name, String theme_package, int theme_icon, int theme_preview, String theme_color){
        this.theme_name = theme_name;
        this.theme_package = theme_package;
        this.theme_icon = theme_icon;
        this.theme_preview = theme_preview;
        this.theme_color = Color.parseColor(theme_color);
        this.theme_color_string = theme_color;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public String getTheme_package() {
        return theme_package;
    }

    public int getTheme_icon() {
        return theme_icon;
    }

    public int getTheme_preview() {
        return theme_preview;
    }

    public int getTheme_color() {
        return theme_color;
    }

    public String getTheme_color_string() {
        return theme_color_string;
    }
}

