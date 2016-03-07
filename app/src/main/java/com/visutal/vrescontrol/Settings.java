package com.visutal.vrescontrol;

import java.net.UnknownHostException;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Window;
import com.visutal.utils.ServerComms;

//public class Settings extends PreferenceActivity {
public class Settings extends AppCompatPreferenceActivity {

	public static boolean LOCAL_DEBUG = true;

	SharedPreferences appSettings;
	private Dialog dialog;

	ServerComms serverComms;
	public String localIp;
	public int localUdpPort;
	public String resolumeIp;
	public int resolumeUdpPort;

	//public boolean vResOSCserver;
	//public String vResOSCserverIp;

	public boolean keepScreenOn;
	//public boolean haptic;

	public int prevLayerSlidersMode;
	public String layerSlidersModeString;
	public int layerSlidersModeInt;

	public int prevFxSlidersMode;
	public String fxSlidersModeString;
	public int fxSlidersModeInt;

	public boolean rotateLayout;
	public boolean layout_r;
	public boolean invertTabs;
	//public boolean swapLayerSliders;
	//public boolean swapSliderBts;
	public boolean fullScreen = true;

	public boolean confirmX;
	public boolean confirmExit;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("Run", "Settings onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

        setupActionBar();   // new

		appSettings = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = appSettings.edit();

		keepScreenOn = appSettings.getBoolean("keepScreenOn", true);
		//haptic = appSettings.getBoolean("haptic", false);
		rotateLayout = appSettings.getBoolean("rotateLayout", false);

		prevLayerSlidersMode = appSettings.getInt("prevLayerSlidersMode", 2);
		layerSlidersModeString = appSettings.getString("layerSlidersModeString", "2");
		layerSlidersModeInt = appSettings.getInt("layerSlidersModeInt", 2);

		prevFxSlidersMode = appSettings.getInt("prevFxSlidersMode", 0);
		fxSlidersModeString = appSettings.getString("fxSlidersModeString", "0");
		fxSlidersModeInt = appSettings.getInt("fxSlidersModeInt", 0);

		layout_r = appSettings.getBoolean("layout_r", false);
		invertTabs = appSettings.getBoolean("invertTabs", false);
		//swapLayerSliders = appSettings.getBoolean("swapLayerSliders", false);
		//swapSliderBts = appSettings.getBoolean("swapSliderBts", false);
		fullScreen = appSettings.getBoolean("fullScreen", true);

		localIp = appSettings.getString("localIp", "0.0.0.0");
        localUdpPort = appSettings.getInt("localUdpPort", 7001);
        resolumeIp = appSettings.getString("resolumeIp", "192.168.0.25");
        resolumeUdpPort = appSettings.getInt("resolumeUdpPort", 7000);
        //vResOSCserver = appSettings.getBoolean("vResOSCserver", false);
        //vResOSCserverIp = appSettings.getString("vResOSCserverIp", "192.168.0.25");

        confirmX = appSettings.getBoolean("confirmX", false);
        confirmExit = appSettings.getBoolean("confirmExit", false);

        final String preferencesName = this.getPreferenceManager().getSharedPreferencesName();

        try {serverComms = new ServerComms();}
        catch (UnknownHostException ex) {System.out.println(ex.toString());}

        if (fullScreen) {getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);}
        if (keepScreenOn) {getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);}
        if (rotateLayout) {this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);}
        else {this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);}

		addPreferencesFromResource(R.xml.settings);

		Preference prefKey_centreTheDesk = (Preference) findPreference("prefKey_centreTheDesk");
		Preference prefKey_controls = (Preference) findPreference("prefKey_controls");
		CheckBoxPreference prefKey_keepScreenOn = (CheckBoxPreference) findPreference("prefKey_keepScreenOn");
		//CheckBoxPreference prefKey_haptic = (CheckBoxPreference) findPreference("prefKey_haptic");
		CheckBoxPreference prefKey_fullScreen = (CheckBoxPreference) findPreference("prefKey_fullScreen");
		final ListPreference prefKey_layerSlidersMode = (ListPreference) findPreference("prefKey_layerSlidersMode");
		final ListPreference prefKey_fxSlidersMode = (ListPreference) findPreference("prefKey_fxSlidersMode");
		CheckBoxPreference prefKey_rotateLayout = (CheckBoxPreference) findPreference("prefKey_rotateLayout");
		CheckBoxPreference prefKey_layout_r = (CheckBoxPreference) findPreference("prefKey_layout_r");
		CheckBoxPreference prefKey_invertTabs = (CheckBoxPreference) findPreference("prefKey_invertTabs");
		//CheckBoxPreference prefKey_swapLayerSliders = (CheckBoxPreference) findPreference("prefKey_swapLayerSliders");
		//CheckBoxPreference prefKey_swapSliderBts = (CheckBoxPreference) findPreference("prefKey_swapSliderBts");

	    Preference prefKey_deviceIp = (Preference) findPreference("prefKey_deviceIp");
	    final EditTextPreference prefKey_deviceUdp = (EditTextPreference) findPreference("prefKey_deviceUdp");
		final EditTextPreference prefKey_resolumeIp = (EditTextPreference) findPreference("prefKey_resolumeIp");
	    final EditTextPreference prefKey_resolumeUdpPort = (EditTextPreference) findPreference("prefKey_resolumeUdpPort");
	   // CheckBoxPreference prefKey_vResOSCserver = (CheckBoxPreference) findPreference("prefKey_vResOSCserver");
	    //final EditTextPreference prefKey_vResOSCserverIp = (EditTextPreference) findPreference("prefKey_vResOSCserverIp");


	    prefKey_layerSlidersMode.setValue(layerSlidersModeString);
	    prefKey_fxSlidersMode.setValue(fxSlidersModeString);
	    prefKey_deviceUdp.setSummary(String.valueOf(localUdpPort) + " (default 7001)");
	    prefKey_resolumeIp.setSummary(resolumeIp);
	    prefKey_resolumeUdpPort.setSummary(String.valueOf(resolumeUdpPort) + " (default 7000)");
	    Log.w("HowFar", "prefKey_resolumeUdpPort setSummary " + String.valueOf(resolumeUdpPort));
	    //prefKey_vResOSCserverIp.setSummary(vResOSCserverIp);

	    Preference prefKey_about = (Preference) findPreference("prefKey_about");
	    Preference prefKey_librariesUsed = (Preference) findPreference("prefKey_librariesUsed");
	    Preference prefKey_reset = (Preference) findPreference("prefKey_reset");
        Preference prefKey_help = (Preference) findPreference("prefKey_help");

	    Preference prefKey_confirmX = (Preference) findPreference("prefKey_confirmX");
	    Preference prefKey_confirmExit = (Preference) findPreference("prefKey_confirmExit");


	//  prefKey_keepScreenOn
	    prefKey_keepScreenOn.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    Boolean boolVal = (Boolean) newValue;
                    editor.putBoolean("keepScreenOn", boolVal);
                    editor.commit();
                    Log.w("appSettings", "prefKey_keepScreenOn " + String.valueOf(boolVal));
                    runToast("..please restart", 1);
                    setResult(1);
                    finish();
                }
                return true;
            }
        });

	//  prefKey_haptic
//	    prefKey_haptic.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	        public boolean onPreferenceChange(Preference preference, Object newValue) {
//	        	if(newValue instanceof Boolean){
//		        	notYetImplemented();
//	                Boolean boolVal = (Boolean) newValue;
//	                //editor.putBoolean("haptic", boolVal);
//		            //editor.commit();
//		            Log.w("appSettings", "prefKey_haptic " + String.valueOf(boolVal));
//		            //runToast("..please restart", 1);
//		            //setResult(1);
//		            //finish();
//	            }
//	            return true;
//	        }
//	    });

	// prefKey_fullScreen
	    prefKey_fullScreen.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	if(newValue instanceof Boolean){
	                Boolean boolVal = (Boolean) newValue;
	                editor.putBoolean("fullScreen", boolVal);
		            editor.commit();
		            runToast("..please restart", 1);
		            Log.w("appSettings", "prefKey_fullScreen " + String.valueOf(boolVal));
		            setResult(1);
		     		finish();
	            }
	            return true;
	        }
	    });

	// prefKey_layerSlidersMode
	    prefKey_layerSlidersMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {

				int convert1 = Integer.parseInt(layerSlidersModeString);
				if (convert1 != prevLayerSlidersMode) {
					editor.putInt("prevLayerSlidersMode", convert1);
				}

				prevLayerSlidersMode = convert1;
				Log.w("appSettings", "prevLayerSlidersMode " + prevLayerSlidersMode + " " + convert1);

				layerSlidersModeString = (String) arg1;
				prefKey_layerSlidersMode.setValue(layerSlidersModeString);

				Log.w("appSettings", "layerSlidersModeString " + layerSlidersModeString);
				Log.w("appSettings", "prevLayerSlidersMode " + prevLayerSlidersMode);

				editor.putString("layerSlidersModeString", layerSlidersModeString);
				layerSlidersModeInt = Integer.parseInt(layerSlidersModeString);
				editor.putInt("layerSlidersModeInt", layerSlidersModeInt);

	            editor.commit();
				return false;
			}
	    });
	//  prefKey_fxSlidersMode
	    prefKey_fxSlidersMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				// 0=Link 1=Opacity

				int convert2 = Integer.parseInt(fxSlidersModeString);
				if (convert2 != prevFxSlidersMode) {
					editor.putInt("prevFxSlidersMode", convert2);
				}

				prevFxSlidersMode = convert2;
				Log.w("appSettings", "prevFxSlidersMode " + prevFxSlidersMode + " " + convert2);

				fxSlidersModeString = (String) arg1;
				prefKey_fxSlidersMode.setValue(fxSlidersModeString);

				Log.w("appSettings", "fxSlidersModeString " + fxSlidersModeString);
				Log.w("appSettings", "prevFxSlidersMode " + prevFxSlidersMode);

				editor.putString("fxSlidersModeString", fxSlidersModeString);

				fxSlidersModeInt = Integer.parseInt(fxSlidersModeString);
				editor.putInt("fxSlidersModeInt", fxSlidersModeInt);

	            editor.commit();
				return false;
			}
	    });

	//	prefKey_rotateLayout
		prefKey_rotateLayout.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	if(newValue instanceof Boolean){
	                Boolean boolVal = (Boolean) newValue;
	                editor.putBoolean("rotateLayout", boolVal);
		            editor.commit();
		            runToast("..please restart", 1);
		            Log.w("appSettings", "rotateLayout " + String.valueOf(boolVal));
		            setResult(1);
		            finish();
	            }
	            return true;
	        }
	    });

	// prefKey_layout_r
		prefKey_layout_r.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	if(newValue instanceof Boolean){
	                Boolean boolVal = (Boolean) newValue;
	                editor.putBoolean("layout_r", boolVal);
		            editor.commit();
		            runToast("..please restart", 1);
		            Log.w("appSettings", "prefKey_layout_r " + String.valueOf(boolVal));
		            setResult(1);
		     		finish();
	            }
	            return true;
	        }
	    });

	// prefKey_invertTabs
		prefKey_invertTabs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	if(newValue instanceof Boolean){
	                Boolean boolVal = (Boolean) newValue;
	                editor.putBoolean("invertTabs", boolVal);
		            editor.commit();
		            runToast("..please restart", 1);
		            Log.w("appSettings", "prefKey_invertTabs " + String.valueOf(boolVal));
		            setResult(1);
		     		finish();
	            }
	            return true;
	        }
	    });

	//	prefKey_swapLayerSliders
//		prefKey_swapLayerSliders.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	        public boolean onPreferenceChange(Preference preference, Object newValue) {
//	        	if(newValue instanceof Boolean){
//		        	notYetImplemented();
//	                Boolean boolVal = (Boolean) newValue;
//	                editor.putBoolean("swapLayerSliders", boolVal);
//		            editor.commit();
//		            runToast("..please restart", 1);
//		            Log.w("appSettings", "prefKey_swapLayerSliders " + String.valueOf(boolVal));
//		            setResult(1);
//		     		//finish();
//	            }
//	            return true;
//	        }
//	    });
//
//	//	prefKey_swapSliderBts
//		prefKey_swapSliderBts.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	        public boolean onPreferenceChange(Preference preference, Object newValue) {
//	        	if(newValue instanceof Boolean){
//		        	notYetImplemented();
//	                Boolean boolVal = (Boolean) newValue;
//	                //editor.putBoolean("swapSliderBts", boolVal);
//		            //editor.commit();
//		            //runToast("..please restart", 1);
//		            Log.w("appSettings", "prefKey_swapSliderBts " + String.valueOf(boolVal));
//		            //setResult(1);
//		     		//finish();
//	            }
//	            return true;
//	        }
//	    });

	 // prefKey_deviceIp
		prefKey_deviceIp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (LOCAL_DEBUG) {
                    dialog = new Dialog(Settings.this);
                    dialog.setContentView(R.layout.dialog_settings);
                    dialog.setTitle(R.string.dialog_stats_title);
                    dialog.setCancelable(true);
                    TextView text = (TextView) dialog.findViewById(R.id.textView);
                    text.setText("localIp - " + localIp
                                    + " \nisKansas - false"
                                    + " \nlocalUdpPort - " + localUdpPort
                                    + " \nresolumeIp - " + resolumeIp
                                    + " \nresolumeUdpPort - " + resolumeUdpPort
                                    + " \nmonkeysFed - true"
                                    + " \nprefsName - " + preferencesName
                    );
                    dialog.show();
                }

                return true;
            }
        });

	//	prefKey_deviceUdp
		prefKey_deviceUdp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.w("HowFar", "prefKey_deviceUdp onPreferenceChange");

                if (newValue instanceof String) {
                    Log.d("Stats", "prefKey_deviceUdp newValue = String");
                    String stringValue = (String) newValue;
                    Log.d("Stats", "prefKey_deviceUdp stringValue = " + stringValue);
                    int intValue = Integer.parseInt(stringValue);
                    Log.w("HowFar", "prefKey_deviceUdp parseInt = " + String.valueOf(intValue));
                    localUdpPort = intValue;
                    Log.w("HowFar", "prefKey_deviceUdp localUdpPort = " + intValue);
                    prefKey_resolumeUdpPort.setSummary(stringValue + " ( default 7001)");
                    Log.w("HowFar", "prefKey_deviceUdp onPreferenceChange setSummary " + localUdpPort);

                    editor.putInt("localUdpPort", localUdpPort);
                    editor.commit();

                } else {
                    Log.d("Stats", "prefKey_resolumeUdpPort newValue != String");
                }

                return true;
            }
        });

	// prefKey_resolumeIp
	    prefKey_resolumeIp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {

	        	if (newValue instanceof String) {
	        		String stringValue = (String) newValue;
	        		resolumeIp = stringValue;
		        	prefKey_resolumeIp.setSummary(resolumeIp);
		            editor.putString("resolumeIp", resolumeIp);
		            editor.commit();
		            Log.w("appSettings", "prefKey_resolumeIp " + stringValue);
	        	}

	            return true;
	        }
	    });

	// prefKey_resolumeUdpPort
	    prefKey_resolumeUdpPort.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	Log.w("HowFar", "prefKey_resolumeUdpPort onPreferenceChange");
	        	if (newValue instanceof String) {
	        		Log.d("Stats", "prefKey_resolumeUdpPort newValue = String");
	        		String stringValue = (String) newValue;
	        		Log.d("Stats", "prefKey_resolumeUdpPort stringValue = " + stringValue);
	        		int intValue = Integer.parseInt(stringValue);
	        		Log.w("HowFar", "prefKey_resolumeUdpPort parseInt = " + String.valueOf(intValue));
	        		resolumeUdpPort = intValue;
					Log.w("HowFar", "prefKey_resolumeUdpPort resolumeUdpPort = intValue");
					prefKey_resolumeUdpPort.setSummary(stringValue + " ( default 7000)");
					Log.w("HowFar", "prefKey_resolumeUdpPort onPreferenceChange setSummary");

					editor.putInt("resolumeUdpPort", resolumeUdpPort);
					editor.commit();

	        	} else {
	        		Log.d("Stats", "prefKey_resolumeUdpPort newValue != String");
	        	}

	            return true;
	        }
	    });

	 // prefKey_vResOSCserver
//	    prefKey_vResOSCserver.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	        public boolean onPreferenceChange(Preference preference, Object newValue) {
//	        	if(newValue instanceof Boolean){
//		        	notYetImplemented();
//	                Boolean boolVal = (Boolean) newValue;
//	                editor.putBoolean("vResOSCserver", boolVal);
//		            editor.commit();
//		            Log.w("appSettings", "prefKey_vResOSCserver " + String.valueOf(boolVal));
//	            }
//	            return true;
//	        }
//	    });
//
//	 // prefKey_vResOSCserverIp
//	    prefKey_vResOSCserverIp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	        public boolean onPreferenceChange(Preference preference, Object newValue) {
//	        	if (newValue instanceof String) {
//	        		String stringValue = (String) newValue;
//	        		vResOSCserverIp = stringValue;
//		        	prefKey_resolumeIp.setSummary(vResOSCserverIp);
//		            editor.putString("vResOSCserverIp", vResOSCserverIp);
//		            editor.commit();
//		            Log.w("appSettings", "prefKey_vResOSCserverIp " + stringValue);
//	        	}
//	            return true;
//	        }
//	    });

        // prefKey_controls
        prefKey_controls.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                runDialog(R.string.dialog_controls_title, R.string.dialog_controls_text);
                return true;
            }
        });

	 // aboutPrefKey
	    prefKey_about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	        public boolean onPreferenceClick(Preference preference) {
                runDialog(R.string.dialog_about_title, R.string.dialog_about_text);
                return true;
	        }
	    });

	// prefKey_librariesUsed
	    prefKey_librariesUsed.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	        public boolean onPreferenceClick(Preference preference) {
                runDialog(R.string.dialog_librariesUsed_title, R.string.dialog_librariesUsed_text);
	            return true;
	        }
	    });

        // prefKey_help
        prefKey_help.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                runDialog(R.string.dialog_help_title, R.string.dialog_help_text);
                return true;
            }
        });

	//  prefKey_confirmX
		prefKey_confirmX.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    //notYetImplemented();
                    Boolean boolVal = (Boolean) newValue;
                    editor.putBoolean("confirmX", boolVal);
                    editor.commit();
                    Log.w("appSettings", "prefKey_confirmX " + String.valueOf(boolVal));
                }
                return true;
            }
        });

		//  prefKey_confirmExit
		prefKey_confirmExit.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	if(newValue instanceof Boolean){
	        		//notYetImplemented();
	                Boolean boolVal = (Boolean) newValue;
	                editor.putBoolean("confirmExit", boolVal);
		            editor.commit();
		            Log.w("appSettings", "prefKey_confirmExit " + String.valueOf(boolVal));
	            }
	            return true;
	        }
	    });

        //  prefKey_centreTheDesk
        prefKey_centreTheDesk.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.w("appSettings", "prefKey_centreTheDesk");
                runYesNoDialog(R.string.dialog_settings_centrethedesk_title, R.string.dialog_settings_centrethedesk_text, true);
                return true;
            }
        });

        // prefKey_reset
        prefKey_reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.d("HowFar", "settings | reset triggered");
                runYesNoDialog(R.string.dialog_settings_yesno_title, R.string.dialog_settings_yesno_text, false);
                return true;
            }
        });

}	//end onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		Preference prefKey_deviceIp = (Preference) findPreference("prefKey_deviceIp");
		if (localIp != null) {
			prefKey_deviceIp.setSummary(localIp);
		} else {Toast.makeText(this, R.string.wifiMessage, Toast.LENGTH_LONG).show();}

		super.onResume();
	}


    private void runDialog(int title, int text) {
        Log.d("HowFar", "settings | runDialog");
        dialog = new Dialog(Settings.this);
        dialog.setContentView(R.layout.dialog_settings);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        TextView tv = (TextView) dialog.findViewById(R.id.textView);
        tv.setText(text);
        Button button = (Button) dialog.findViewById(R.id.dialog_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bt", "runDialog dialog Cancel bt triggered");
                dialog.dismiss();
            }
        });
        dialog.show();
        return;
    }

    private void runYesNoDialog(int title, int text, final boolean resetOrCenter) {
		Log.d("HowFar", "settings | runYesNoDialog");
    	dialog = new Dialog(Settings.this);
        dialog.setContentView(R.layout.dialog_settings_yesno);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        TextView t = (TextView) dialog.findViewById(R.id.textView);
        t.setText(text);
        Button button = (Button) dialog.findViewById(R.id.dialog_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bt", "runYesNoDialog dialog Cancel bt triggered");
                dialog.dismiss();
            }
        });
        Button button2 = (Button) dialog.findViewById(R.id.dialog_ok);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
        		Log.d("Bt", "runYesNoDialog dialog Ok bt triggered");
        		dialog.dismiss();
        		if (resetOrCenter) {
                    // center the desk
                    clearSavedStates();
                    runToast("Buttons and sliders reset!", 0);
                    setResult(0);
                } else {
                    // reset everything and exit
                    clearSettings();
                    runToast("reset ..please restart", 1);
                    setResult(1);
                    finish();
                }
            }
        });
        dialog.show();
        return;
	}

	private void clearSettings() {
    	Editor editor = appSettings.edit();
    	editor.clear();
    	editor.commit();
    	Log.d("HowFar", "ALL settings have been cleared! please restart");
    	return;
    }

	private void clearSavedStates() {
		// should probably just direct each fragment to do this itself...
    	Editor editor = appSettings.edit();

    	//main activity

		//fragment1
    	editor.putInt("prevLayer", 0);
		editor.putInt("currentLayer", 0);
		editor.putBoolean("bt_compFlip", false);
		editor.putBoolean("bt_layer1Flip", false);
		editor.putBoolean("bt_layer2Flip", false);
		editor.putBoolean("bt_layer3Flip", false);
		editor.putBoolean("bt_layer4Flip", false);

		editor.putInt("prevBeatloop", 0);
		editor.putInt("currentBeatloop", 0);
		editor.putBoolean("bt_beatloop_1Flip", false);
		editor.putBoolean("bt_beatloop_2Flip", false);
		editor.putBoolean("bt_beatloop_3Flip", false);
		editor.putBoolean("bt_beatloop_4Flip", false);
		editor.putBoolean("bt_beatloop_5Flip", false);
		editor.putBoolean("bt_beatloop_6Flip", false);
		editor.putBoolean("bt_beatloop_7Flip", false);

		//editor.putDouble("editText_bpm", bpmInDouble);
		editor.putBoolean("bt_pausedFlip", false);
		editor.putBoolean("bt_bypassedFlip", false);
		editor.putInt("progress_opacityandvolumeInt", 100);

		//fragment2
		editor.putInt("verticalSeekbar1Progress_audio", 0);
		editor.putInt("verticalSeekbar2Progress_audio", 0);
		editor.putInt("verticalSeekbar3Progress_audio", 0);
		editor.putInt("verticalSeekbar4Progress_audio", 0);

		editor.putInt("verticalSeekbar1Progress_av", 0);
		editor.putInt("verticalSeekbar2Progress_av", 0);
		editor.putInt("verticalSeekbar3Progress_av", 0);
		editor.putInt("verticalSeekbar4Progress_av", 0);

		editor.putInt("verticalSeekbar1Progress_video", 0);
		editor.putInt("verticalSeekbar2Progress_video", 0);
		editor.putInt("verticalSeekbar3Progress_video", 0);
		editor.putInt("verticalSeekbar4Progress_video", 0);

		editor.putBoolean("bt_1_bFlip", false);
		editor.putBoolean("bt_2_bFlip", false);
		editor.putBoolean("bt_3_bFlip", false);
		editor.putBoolean("bt_4_bFlip", false);

		editor.putBoolean("bt_1_sFlip", false);
		editor.putBoolean("bt_2_sFlip", false);
		editor.putBoolean("bt_3_sFlip", false);
		editor.putBoolean("bt_4_sFlip", false);

		editor.putInt("bt_1_abState", 0);
		editor.putInt("bt_2_abState", 0);
		editor.putInt("bt_3_abState", 0);
		editor.putInt("bt_4_abState", 0);

		editor.putInt("seekBar5Prog", 50);

		editor.putBoolean("bt_seekBar_a_state", false);
		editor.putBoolean("bt_seekBar_b_state", false);

		//fragment3

		//fragment4
		editor.putInt("verticalSeekbar_fx1Progress_link", 0);
		editor.putInt("verticalSeekbar_fx2Progress_link", 0);
		editor.putInt("verticalSeekbar_fx3Progress_link", 0);
		editor.putInt("verticalSeekbar_fx4Progress_link", 0);
		editor.putInt("verticalSeekbar_fx5Progress_link", 0);
		editor.putInt("verticalSeekbar_fx6Progress_link", 0);
		editor.putInt("verticalSeekbar_fx7Progress_link", 0);
		editor.putInt("verticalSeekbar_fx8Progress_link", 0);

		editor.putInt("verticalSeekbar_fx1Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx2Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx3Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx4Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx5Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx6Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx7Progress_opacity", 0);
		editor.putInt("verticalSeekbar_fx8Progress_opacity", 0);

		editor.putBoolean("bt_fx1_bFlip", false);
		editor.putBoolean("bt_fx2_bFlip", false);
		editor.putBoolean("bt_fx3_bFlip", false);
		editor.putBoolean("bt_fx4_bFlip", false);
		editor.putBoolean("bt_fx5_bFlip", false);
		editor.putBoolean("bt_fx6_bFlip", false);
		editor.putBoolean("bt_fx7_bFlip", false);
		editor.putBoolean("bt_fx8_bFlip", false);

    	editor.commit();

    	Log.d("HowFar", "ALL button and slider states have been cleared!");
    	return;
	}

	public void runToast(String text, int length) {
		int length2;
		if (length == 1) {
			length2 = Toast.LENGTH_LONG;
		} else {
			length2 = Toast.LENGTH_SHORT;
		}
		Toast.makeText(Settings.this, text, length2).show();
	}

	private void notYetImplemented() {
		runToast("..not implemented yet!", 0);
	};

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            //actionBar.setHomeButtonEnabled(true);     // ???
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            runToast("..no actionBar!", 0);
        }
    }
}