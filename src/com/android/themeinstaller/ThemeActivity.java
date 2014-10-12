package com.android.themeinstaller;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeActivity extends Activity {

    String SelectedTheme;
    String ThemeName;
    String ThemePackage;
    int ThemeColor;
    String ThemeColorString;
    int ThemeIcon;
    int ThemePreview;
    TextView InstallButton;
    ImageView ApplyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(true);
		setContentView(R.layout.activity_theme_selector);

        Intent intent = getIntent();

        if (intent != null) {
            SelectedTheme = intent.getStringExtra("SelectedTheme");
            ThemeName = intent.getStringExtra("ThemeName");
            ThemePackage = intent.getStringExtra("ThemePackage");
            ThemeIcon = intent.getIntExtra("ThemeIcon", 0);
            ThemePreview = intent.getIntExtra("ThemePreview", 0);
            ThemeColor = intent.getIntExtra("ThemeColor", 0);
            ThemeColorString = intent.getStringExtra("ThemeColorString");
        }

        setTitle(ThemeName);

        InstallButton = (TextView) findViewById(R.id.install_button);
        ApplyButton = (ImageView) findViewById(R.id.fab);

        if (PackageInstalled(ThemePackage)) {
            InstallButton.setVisibility(View.GONE);
            InstallButton.setClickable(false);
            ApplyButton.setClickable(true);
            ApplyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenThemeInSettings(ThemePackage);
                }
            });
        } else {
            InstallButton.setVisibility(View.VISIBLE);
            InstallButton.setClickable(true);
            InstallButton.setText("Install " + ThemeName);
            InstallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            ApplyButton.setClickable(false);
        }

	}


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first


        if (PackageInstalled(ThemePackage)) {
            InstallButton.setVisibility(View.GONE);
            InstallButton.setClickable(false);
            ApplyButton.setClickable(true);
            ApplyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenThemeInSettings(ThemePackage);
                }
            });
        } else {
            InstallButton.setVisibility(View.VISIBLE);
            InstallButton.setClickable(true);
            InstallButton.setText("Install " + ThemeName);
            InstallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            ApplyButton.setClickable(false);
        }
    }
	
	protected void showDialog(){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThemeActivity.this);
        alertDialog.setTitle("Install");
        alertDialog.setMessage("You are going to install " + ThemeName + ". After installing please press the 'Apply' Floating Action Button to open the Theme in CM Theme Chooser and apply it.");
 
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Themes/Data/theme" + SelectedTheme + ".apk")), "application/vnd.android.package-archive");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.theme, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.uninstall:
                if (PackageInstalled(ThemePackage)){
                Uninstall(ThemePackage);
                }
                break;

            case R.id.pick_color:CopyToClipBoard("Color", ThemeColorString);
                break;
        }
        return super.onOptionsItemSelected(item);
	}


    public boolean PackageInstalled(String target_package) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(target_package, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public void OpenThemeInSettings(String target_package) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.putExtra("pkgName", target_package);
        intent.setComponent(new ComponentName("org.cyanogenmod.theme.chooser", "org.cyanogenmod.theme.chooser.ChooserActivity"));
        startActivity(intent);
    }

    public void Uninstall(String targetPackage) {
        Uri packageUri = Uri.parse("package:" + targetPackage);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                packageUri);
        startActivity(uninstallIntent);
    }

    public void CopyToClipBoard(String label, String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), label + " copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
