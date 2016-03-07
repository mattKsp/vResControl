package com.visutal.utils;

import com.illposed.osc.OSCPortIn;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public final class Clipboard {

    public static int LOCAL_DEBUG = 1;
    
    public static final String PREF_FILE_NAME = "vpSettings";
	SharedPreferences appSettings;
	public static Boolean isLeaving = false;
	
	ServerComms serverComms;

    private String serverIp;
    private String mServerCommsIp;
    private int mServerCommsUdpPort;
    private int clientID;    		// try not to actually use clientID from settings, thats for reference, keep only as runtime number
    private String clientIDString;
    private String localIp;
    private int localUdpPort;
    
    private String vpCurrentMessage;
	private String vpCurrentLogo;
	private String vpCurrentEffect; // this IS used ..WTF?
	private int vpCurrentBreakDrop;		// 0=off, 1=break, 2=drop - for use with switching use of the centre button

	private void Eg1() {

		int prevLayerSlidersMode = 2;
		Log.d("Stats", "Eg1 | prevLayerSlidersMode " + String.valueOf(prevLayerSlidersMode));
		
		String layerSlidersMode = "2";
		
		// Integer.parseInt(layerSlidersMode)	// cast from String to int
		//if (index == 0 && prevCheckPosition != 0)
		
		if (Integer.parseInt(layerSlidersMode) != prevLayerSlidersMode){
			prevLayerSlidersMode = Integer.parseInt(layerSlidersMode);
		}
		
		
	
	}
	
	private void Logging() {
		//Log.d  = info (blue)
		Log.d("Stats", "");
		Log.d("HowFar", "");
		Log.d("Bt", "");	// buttons
		Log.d("UDP", "");
		Log.d("OSC", "");
		
		// Log.w  = info (orange)
		Log.w("appSettings", "");	// eg. show any keys written
		
		// Log.e  = errors (red)
		Log.e("appSettingsReadWrite", "");
		Log.e("UDP", "");
		Log.e("OSC", "");
		Log.e("Network ", "");
	}
	
	private void Toasting() {
		// needs a view to operated in!
		//if (LOCAL_DEBUG == 1 && !isLeaving) {Toast.makeText(Clipboard.this, "server found @ ", Toast.LENGTH_SHORT).show();}
		//if (LOCAL_DEBUG == 1) {Toast.makeText(Clipboard.this, "server found @ ", Toast.LENGTH_LONG).show();}
	}

}
