package com.visutal.vrescontrol;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import com.visutal.utils.ServerComms;
import com.visutal.utils.Utils;

public class VresControlActivity extends AppCompatActivity
	implements Resolume1Fragment.Frag1OSCSend, Resolume2Fragment.Frag2OSCSend, Resolume3Fragment.Frag3OSCSend, Resolume4Fragment.Frag4OSCSend{

	private boolean LOCAL_DEBUG = Settings.LOCAL_DEBUG;

	SharedPreferences appSettings;
	private Dialog dialog;
	public static boolean isLeaving = false;
	public static boolean isGoingToSettings = false;

	public boolean keepScreenOn = true;
	public boolean rotateLayout;
	public boolean layout_r;
	public boolean invertTabs = false;
	public boolean swapLayerSliders = false;
	public boolean swapSliderBts = false;
	public boolean fullScreen = true;
	public boolean confirmExit = false;
	
	public int layerSlidersModeInt = 2;
	
	int curCheckPosition = 0;
	int prevCheckPosition = 0;
	private Fragment replacementPage;
	private Resolume1Fragment pageT1 = null;
	private Resolume2Fragment pageT2 = null;
	private Resolume3Fragment pageT3 = null;
	private Resolume4Fragment pageT4 = null;
	boolean doSwap;
	private boolean pageT1_active = false;
	private boolean pageT2_active = false;
	private boolean pageT3_active = false;
	private boolean pageT4_active = false;
	
	OSCPortIn receiver;
	
	public static boolean sendOSC = false;
    private boolean receiveOSC = true;
    
	ServerComms serverComms;
	public String localIp;
	public int localUdpPort;
	public String resolumeIp;
	public int resolumeUdpPort;
    private String mServerCommsIp;
    private int mServerCommsUdpPort;
    private String mServerCommsResolumeIp;
    private int mServerCommsResolumeUdpPort;
    
    public boolean vResOSCserver;
	public String vResOSCserverIp;

	private int layoutri = R.layout.activity_vrescontrol_r_i;	// layout right inverted
    private int layoutr = R.layout.activity_vrescontrol_r;		// layout right
    private int layouti = R.layout.activity_vrescontrol_i;		// layout (left) inverted
    private int layout = R.layout.activity_vrescontrol;			// layout (left)

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("HowFar", "VresControlActivity | onCreate");

		// http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception#
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);

	//so it doesnt do all this when leaving from the settings
	if (!isLeaving) {
		appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        runSharedPreferences();
        getSavedState();
        Log.d("HowFar", "VresControlActivity | onCreate | back from runSharedPreferences and getSavedState");

        setupView();
        setupButtons();

		// create all tabs at once, that means can update OSC from start cos they exist...
        if (pageT1 == null) {pageT1 = new Resolume1Fragment(); pageT1 = Resolume1Fragment.newInstance(0);}
        if (pageT2 == null) {pageT2 = new Resolume2Fragment(); pageT2 = Resolume2Fragment.newInstance(1);}
        if (pageT3 == null) {pageT3 = new Resolume3Fragment(); pageT3 = Resolume3Fragment.newInstance(2);}
        if (pageT4 == null) {pageT4 = new Resolume4Fragment(); pageT4 = Resolume4Fragment.newInstance(3);}

		showTab(curCheckPosition);
	}	// END of !isLeaving
	}
	
	@Override
    public void onResume() {
        super.onResume();
		Log.d("HowFar", "VresControlActivity | onResume");
		if (!isLeaving) // to stop it doing this when leaving the app from settings
		{
			getSavedState();
			Log.d("Stats", "VresControlActivity | onResume layerSlidersModeInt = " + layerSlidersModeInt);
			Log.d("Stats", "VresControlActivity | onResume fxSlidersModeInt = " + CcSet.i().data4.fxSlidersModeInt);
			localIp = getLocalIpAddress();
			if (LOCAL_DEBUG) {runToast("localIp | " + localIp, 0);}
			if (localIp != null) {
				sendOSC = true;
				receiveOSC = true;
				Editor editor = appSettings.edit();
				editor.putString("localIp", localIp);
				editor.commit();
				if (!isGoingToSettings) {
					OSCListen();
				}
			} else {
				Toast.makeText(VresControlActivity.this, R.string.wifiMessage, Toast.LENGTH_LONG).show();
				sendOSC = false;
				receiveOSC = false;
				// probably should do something else here
				// either exit or give option to go to wifi settings
			}
		}
		isLeaving = false;
		isGoingToSettings = false;
    }
	
	@Override
    public void onPause() {
        super.onPause();
		Log.d("HowFar", "VresControlActivity | onPause");
		if (!isLeaving) {
			setSavedState();
	    	if (!isGoingToSettings && receiveOSC) {
	    		try {receiver.stopListening(); receiver.close();} 
				catch (Exception e) {e.printStackTrace();}
	    		//receiveOSC = false;									//dont think need this as it is pausing
	    	}
		}
    }
	
	void setupView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (fullScreen) {getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);}
        if (keepScreenOn) {getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);}
        if (rotateLayout) {this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);} 
        else {this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);}

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getSupportActionBar().hide();

        if (!layout_r) {
        	if (invertTabs) {setContentView(layoutri);} 
        	else {setContentView(layoutr);}
        } else {
        	if (invertTabs) {
        		setContentView(layouti);} 
        	else {setContentView(layout);}
		}

		Log.d("HowFar", "VresControlActivity | view setup");
	}

	void setupButtons() {
		ImageView bt_tab0 = (ImageView) findViewById(R.id.bt_tab0);
		ImageView bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
		ImageView bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
		ImageView bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);
		ImageView bt_tabSettings = (ImageView) findViewById(R.id.bt_tabSettings);
		ImageView bt_tabExit = (ImageView) findViewById(R.id.bt_tabExit);

		bt_tabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmExit) {
                    doYouWantToExit();
                } else {
                    doExit();
                }
            }
        });

		final Intent settingsIntent = new Intent(VresControlActivity.this, Settings.class);
		bt_tabSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Bt", "VresControlActivity | bt_tabSettings");
	        	isGoingToSettings = true;
				startActivityForResult(settingsIntent, 0);
			}
		});
		bt_tab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Bt", "bt_tab0");
                prevCheckPosition = curCheckPosition;
                showTab(0);
            }
        });
		bt_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Bt", "bt_tab1");
                prevCheckPosition = curCheckPosition;
                showTab(1);
            }
        });
		bt_tab2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Bt", "bt_tab2");
				prevCheckPosition = curCheckPosition;
				showTab(2);
			}
		});
		bt_tab3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Bt", "bt_tab3");
				prevCheckPosition = curCheckPosition;
				showTab(3);
			}
		});
		
		Log.d("HowFar", "VresControlActivity | buttons setup");
	}
	
	void showTab(int index) {
		Log.d("HowFar", "VresControlActivity | showDetails");
		Log.d("Stats", "VresControlActivity | curCheckPosition = " + curCheckPosition);
		Log.d("Stats", "VresControlActivity | prevCheckPosition = " + prevCheckPosition);

        curCheckPosition = index;
        doSwap = true; 

        	if (index == 0 && prevCheckPosition != 0) {
        		replacementPage = pageT1;
        		pageT1_active = true;
        		pageT2_active = false;
        		pageT3_active = false;
        		pageT4_active = false;
        		Log.d("Stats", "VresControlActivity | showTab | pageT1_active = true");
        	} else if (index == 1 && prevCheckPosition != 1) {
            	replacementPage = pageT2;
        		pageT1_active = false;
        		pageT2_active = true;
        		pageT3_active = false;
        		pageT4_active = false;
        		Log.d("Stats", "VresControlActivity | showTab | pageT2_active = true");
        	} else if (index == 2 && prevCheckPosition != 2) {
        		replacementPage = pageT3;
        		pageT1_active = false;
        		pageT2_active = false;
        		pageT3_active = true;
        		pageT4_active = false;
        		Log.d("Stats", "VresControlActivity | showTab | pageT3_active = true");
        	} else if (index == 3 && prevCheckPosition != 3) {
        		replacementPage = pageT4;
        		pageT1_active = false;
        		pageT2_active = false;
        		pageT3_active = false;
        		pageT4_active = true;
        		Log.d("Stats", "VresControlActivity | showTab | pageT4_active = true");
        	} else if (index == 0 && prevCheckPosition == 0){
        		// 1st setup
            	replacementPage = pageT1;
        		pageT1_active = true;
        		pageT2_active = false;
        		pageT3_active = false;
        		pageT4_active = false;
        		Log.d("Stats", "VresControlActivity | showTab | pageT1_active = true");
        	} else {
        		Log.d("HowFar", "VresControlActivity showDetails | else..");
            	doSwap = false;
        	}
        	Log.d("Stats", "VresControlActivity  | index == " + index);
        	
        	if (doSwap) {
	        	Log.d("HowFar", "VpActivity showDetails  | about to replace fragment");
	        	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        	ft.replace(R.id.page2, replacementPage);
	        	
	        	ft.setTransition(FragmentTransaction.TRANSIT_NONE);
	        	ft.commit();
	        	swapTabIcons(index);
        	}
    		
        	Log.d("HowFar", "VresControlActivity | Tabs setup");
    }
	
	private void swapTabIcons(int index) {
		Log.d("HowFar", "VresControlActivity | swapTabIcons");
		
		ImageView bt_tab0 = (ImageView) findViewById(R.id.bt_tab0);
		ImageView bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
		ImageView bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
		ImageView bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);
		
		if (index == 0) {
			bt_tab0.setBackgroundResource(R.drawable.shape_tab1b);
			bt_tab1.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab2.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab3.setBackgroundResource(R.drawable.shape_tab1);
		} else if (index == 1) {
			bt_tab0.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab1.setBackgroundResource(R.drawable.shape_tab1b);
			bt_tab2.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab3.setBackgroundResource(R.drawable.shape_tab1);
		} else if (index == 2) {
			bt_tab0.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab1.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab2.setBackgroundResource(R.drawable.shape_tab1b);
			bt_tab3.setBackgroundResource(R.drawable.shape_tab1);
		} else if (index == 3) {
			bt_tab0.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab1.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab2.setBackgroundResource(R.drawable.shape_tab1);
			bt_tab3.setBackgroundResource(R.drawable.shape_tab1b);
		}
		
	}
	
	private void runSharedPreferences() {
		
    	if (appSettings.getBoolean("hasRunBefore", false))
    	{
    		// has run before
    		int hasRunBeforeCount = appSettings.getInt("hasRunBeforeCount", 0);
     	    int hasRunBeforeCountPlus = hasRunBeforeCount + 1;
    		Editor editor = appSettings.edit();
     		editor.putInt("hasRunBeforeCount", hasRunBeforeCountPlus);
     		editor.commit();
     		Log.d("Stats", "hasRunBefore " + hasRunBeforeCountPlus + " times");
    	}
    	else
    	{
    		// has not run before
    		// this will also get wiped if app is uninstalled, etc
    		Editor editor = appSettings.edit();
    		editor.putBoolean("hasRunBefore", true);
    		editor.commit();

            //CcSet.i().saveAll(appSettings); // is this needed ?   eg. as it will initialise an array with 0 default
    		
    		Log.d("HowFar", "has NOT run before");
    	 }

		Log.d("HowFar", "VresControlActivity |  runSharedPreferences");
    }
	
	private String getLocalIpAddress() {
 	   try {
 	       for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
 	           NetworkInterface intf = en.nextElement();
 	           for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
 	               InetAddress inetAddress = enumIpAddr.nextElement();
 	               if (!inetAddress.isLoopbackAddress()) {
 	                   if (inetAddress instanceof Inet4Address) {
 	                       return ((Inet4Address)inetAddress).getHostAddress().toString();
 	                   }
 	               }
 	           }
 	       }
 	   } catch (SocketException ex) {
 	       Log.e("getLocalIpAddress", ex.toString());
 	   }
 	   return null;
 	}
	
	protected void OSCListen() {
		Log.d("HowFar", "VresControlActivity | OSCListen started");
		
	if (receiveOSC) {
		final Editor editor = appSettings.edit();
		try {
		receiver = new OSCPortIn(localUdpPort);

		//fragment1
		OSCListener bt_compListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Integer message1 = (Integer) obj.get(0);
				Log.d("HowFar", "VresControlActivity | bt_compListener " + message1);
				// layer 0 = comp On, 5 = comp Off
				if (pageT1_active) {
					if (message1 == 0) {
							Resolume1Fragment.updateOSCCurentLayer05(5);
					} else if (message1 == 1) {
							Resolume1Fragment.updateOSCCurentLayer05(0);
					}
				} else {
					int currentLayer = appSettings.getInt("currentLayer", 0);
					editor.putInt("prevLayer", currentLayer);
					int setLayer;
					if (message1 == 0) {setLayer = 5;}
					else {setLayer = 0;}
					editor.putInt("currentLayer", setLayer);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_layer1Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				if (pageT1_active) {
						Resolume1Fragment.updateOSCCurentLayer1(1);
				} else {
					int currentLayer = appSettings.getInt("currentLayer", 0);
					editor.putInt("prevLayer", currentLayer);
					editor.putInt("currentLayer", 1);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_layer2Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				if (pageT1_active) {
						Resolume1Fragment.updateOSCCurentLayer2(2);
				} else {
					int currentLayer = appSettings.getInt("currentLayer", 0);
					editor.putInt("prevLayer", currentLayer);
					editor.putInt("currentLayer", 2);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_layer3Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				if (pageT1_active) {
						Resolume1Fragment.updateOSCCurentLayer3(3);
				} else {
					int currentLayer = appSettings.getInt("currentLayer", 0);
					editor.putInt("prevLayer", currentLayer);
					editor.putInt("currentLayer", 3);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_layer4Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				if (pageT1_active) {
						Resolume1Fragment.updateOSCCurentLayer4(4);
				} else {
					int currentLayer = appSettings.getInt("currentLayer", 0);
					editor.putInt("prevLayer", currentLayer);
					editor.putInt("currentLayer", 4);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_beatloopListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Integer bt_beatloopListenerObj = (Integer) obj.get(0);
				if (pageT1_active) {
					Resolume1Fragment.swapBtBeatLoop(bt_beatloopListenerObj);
				} else {
					int currentBeatloop = appSettings.getInt("currentBeatloop", 0);
					editor.putInt("prevBeatloop", currentBeatloop);
					editor.putInt("currentBeatloop", bt_beatloopListenerObj);
			 		editor.commit();
				}
				Log.d("OSC", "bt_beatloop Listener received " + (Integer) obj.get(0));
			}
		};
		OSCListener pausedListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "paused Listener received ");
				Integer pausedListenerObj = (Integer) obj.get(0);
				if (pageT1_active) {
					Resolume1Fragment.updateOSCpaused(pausedListenerObj);
//					if (pausedListenerObj == 0) {
//						if (Resolume1Fragment.bt_pausedFlip) {
//							Resolume1Fragment.updateOSCpaused(pausedListenerObj);
//						}
//					} else if (pausedListenerObj ==1) {
//						if (!Resolume1Fragment.bt_pausedFlip) {
//							Resolume1Fragment.updateOSCpaused(pausedListenerObj);
//						}
//					}

				} else {
					boolean pausedListenerObjBool;
					if (pausedListenerObj == 0) {pausedListenerObjBool = false;}
					else {pausedListenerObjBool = true;}
					editor.putBoolean("bt_pausedFlip", pausedListenerObjBool);
			 		editor.commit();
				}
			}
		};
		OSCListener compBypassedListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "compBypassed Listener received ");
				Integer compBypassedListenerObj = (Integer) obj.get(0);
				if (pageT1_active) {
					Resolume1Fragment.updateOSCcompBypassed(compBypassedListenerObj);
//					if (compBypassedListenerObj == 0) {
//						if (Resolume1Fragment.bt_bypassedFlip) {
//							Resolume1Fragment.updateOSCcompBypassed(compBypassedListenerObj);
//						}
//					} else if (compBypassedListenerObj == 1) {
//						if (!Resolume1Fragment.bt_bypassedFlip) {
//							Resolume1Fragment.updateOSCcompBypassed(compBypassedListenerObj);
//						}
//					}
					
				} else {
					boolean compBypassedListenerObjBool;
					if (compBypassedListenerObj == 0) {compBypassedListenerObjBool = false;}
					else {compBypassedListenerObjBool = true;}
					editor.putBoolean("bt_bypassedFlip", compBypassedListenerObjBool);
			 		editor.commit();
				}
			}
		};
		OSCListener opacityandvolumeListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "opacityandvolume Listener received ");
				float opacityandvolumeListenerObj = (Float) obj.get(0);
				if (pageT1_active) {
					Resolume1Fragment.updateOSCopacityandvolume(opacityandvolumeListenerObj);
				} else {
					final int seekBar_opacityandvolumeIn = Utils.ConvertRange(0, 1, 0, 100, opacityandvolumeListenerObj);
					editor.putInt("progress_opacityandvolumeInt", seekBar_opacityandvolumeIn);
			 		editor.commit();
				}
			}
		};
		OSCListener bpmListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "listener3 received ");
				double bpmListenerObj = (Float) obj.get(0);
				if (pageT1_active) {
					Resolume1Fragment.updateOSCbpm(bpmListenerObj);
				} else {
					/*float bpmListenerObj2 = (Float) obj[0];
					float bpmListenerObj3 = Utils.ConvertBpmInReturnFloat(bpmListenerObj2);
					Log.d("OSC", "listener3 bpmListenerObj3 " + bpmListenerObj3);
					float bpmFloat2 = (float) (bpmListenerObj3*1000);
					editor.putFloat("bpmFloat", bpmFloat2);
			 		editor.commit();
					Log.d("OSC", "listener3 bpmFloat2 " + bpmFloat2);*/
				}
			}
		};
		
		//fragment2
		OSCListener verticalSeekbar1_avListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar1_av Listener received ");
				float verticalSeekbar1_avListenerObj = (Float) obj.get(0);

                int verticalSeekBar1_avProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar1_avListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_av[0] = verticalSeekBar1_avProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 1) {
					Resolume2Fragment.updateOSCverticalSeekBar(0);
				}
			}
		};
		OSCListener verticalSeekbar2_avListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar2_av Listener received ");
				float verticalSeekbar2_avListenerObj = (Float) obj.get(0);

                int verticalSeekBar2_avProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar2_avListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_av[1] = verticalSeekBar2_avProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 1) {
					Resolume2Fragment.updateOSCverticalSeekBar(1);
				}
			}
		};
		OSCListener verticalSeekbar3_avListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar3_av Listener received ");
				float verticalSeekbar3_avListenerObj = (Float) obj.get(0);

                int verticalSeekBar3_avProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar3_avListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_av[2] = verticalSeekBar3_avProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 1) {
					Resolume2Fragment.updateOSCverticalSeekBar(2);
				}
			}
		};
		OSCListener verticalSeekbar4_avListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar4_av Listener received ");
				float verticalSeekbar4_avListenerObj = (Float) obj.get(0);

                int verticalSeekBar4_avProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar4_avListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_av[3] = verticalSeekBar4_avProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 1) {
					Resolume2Fragment.updateOSCverticalSeekBar(3);
				}
			}
		};
	
		OSCListener verticalSeekbar1_audioListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar1_audio Listener received ");
				float verticalSeekbar1_audioListenerObj = (Float) obj.get(0);

                int verticalSeekBar1_audioProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar1_audioListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_audio[0] = verticalSeekBar1_audioProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 0) {
					Resolume2Fragment.updateOSCverticalSeekBar(0);
				}
			}
		};
		OSCListener verticalSeekbar2_audioListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar2_audio Listener received ");
				float verticalSeekbar2_audioListenerObj = (Float) obj.get(0);

                int verticalSeekBar2_audioProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar2_audioListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_audio[1] = verticalSeekBar2_audioProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 0) {
					Resolume2Fragment.updateOSCverticalSeekBar(1);
				}
			}
		};
		OSCListener verticalSeekbar3_audioListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar3_audio Listener received ");
				float verticalSeekbar3_audioListenerObj = (Float) obj.get(0);

                int verticalSeekBar3_audioProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar3_audioListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_audio[2] = verticalSeekBar3_audioProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 0) {
					Resolume2Fragment.updateOSCverticalSeekBar(2);
				}
			}
		};
		OSCListener verticalSeekbar4_audioListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar4_audio Listener received ");
				float verticalSeekbar4_audioListenerObj = (Float) obj.get(0);

                int verticalSeekBar4_audioProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar4_audioListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_audio[3] = verticalSeekBar4_audioProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 0) {
					Resolume2Fragment.updateOSCverticalSeekBar(3);
				}
			}
		};
		
		OSCListener verticalSeekbar1Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar1_video Listener received " + (Float) obj.get(0));
				float verticalSeekbar1ListenerObj = (Float) obj.get(0);

                int verticalSeekBar1ProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar1ListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_video[0] = verticalSeekBar1ProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 2) {
					Resolume2Fragment.updateOSCverticalSeekBar(0);
				}
			}
		};
		OSCListener verticalSeekbar2Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar2_video Listener received ");
				float verticalSeekbar2ListenerObj = (Float) obj.get(0);

                int verticalSeekBar2ProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar2ListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_video[1] = verticalSeekBar2ProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 2) {
					Resolume2Fragment.updateOSCverticalSeekBar(1);
				}
			}
		};
		OSCListener verticalSeekbar3Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar3_video Listener received ");
				float verticalSeekbar3ListenerObj = (Float) obj.get(0);

                int verticalSeekBar3ProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar3ListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_video[2] = verticalSeekBar3ProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 2) {
					Resolume2Fragment.updateOSCverticalSeekBar(2);
				}
			}
		};
		OSCListener verticalSeekbar4Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbar4_video Listener received ");
				float verticalSeekbar4ListenerObj = (Float) obj.get(0);

                int verticalSeekBar4ProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbar4ListenerObj);
                CcSet.i().data2.verticalSeekbarProgress_video[3] = verticalSeekBar4ProgIn;

				if (pageT2_active &&  layerSlidersModeInt == 2) {
					Resolume2Fragment.updateOSCverticalSeekBar(3);
				}
			}
		};
	
		OSCListener bt_1_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_1_b Listener received ");
				Integer bt_1_bListenerObj = (Integer) obj.get(0);

                if (bt_1_bListenerObj == 0) {CcSet.i().data2.bt_b_flip[0] = false;}
                else {CcSet.i().data2.bt_b_flip[0] = true;}

				if (pageT2_active) {
					//Resolume2Fragment.updateOSCbt_1_b(bt_1_bListenerObj);
					Resolume2Fragment.updateOSCbt_b(0);
//					if (bt_1_bListenerObj == 0) {
//						if (Resolume2Fragment.bt_1_bFlip) {
//							Resolume2Fragment.updateOSCbt_1_b(bt_1_bListenerObj);
//						}
//					} else if (bt_1_bListenerObj == 1) {
//						if (!Resolume2Fragment.bt_1_bFlip) {
//							Resolume2Fragment.updateOSCbt_1_b(bt_1_bListenerObj);
//						}
//					}
				}
//                else {
//					boolean bt_1_bFlip;
//					if (bt_1_bListenerObj == 0) {bt_1_bFlip = false;}
//					else {bt_1_bFlip = true;}
//					editor.putBoolean("bt_1_bFlip", bt_1_bFlip);
//			 		editor.commit();
//				}
			}
		};
		OSCListener bt_2_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_2_b Listener received ");
				Integer bt_2_bListenerObj = (Integer) obj.get(0);

                if (bt_2_bListenerObj == 0) {CcSet.i().data2.bt_b_flip[1] = false;}
                else {CcSet.i().data2.bt_b_flip[1] = true;}

                if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_b(1);
				}
			}
		};
		OSCListener bt_3_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_3_b Listener received ");
				Integer bt_3_bListenerObj = (Integer) obj.get(0);

                if (bt_3_bListenerObj == 0) {CcSet.i().data2.bt_b_flip[2] = false;}
                else {CcSet.i().data2.bt_b_flip[2] = true;}

                if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_b(2);
				}
			}
		};
		OSCListener bt_4_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_4_b Listener received ");
				Integer bt_4_bListenerObj = (Integer) obj.get(0);

                if (bt_4_bListenerObj == 0) {CcSet.i().data2.bt_b_flip[3] = false;}
                else {CcSet.i().data2.bt_b_flip[3] = true;}

				if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_b(3);
				}
			}
		};
		
		OSCListener bt_1_sListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_1_s Listener received ");
				Integer bt_1_sListenerObj = (Integer) obj.get(0);

                if (bt_1_sListenerObj == 0) {CcSet.i().data2.bt_s_flip[0] = false;}
                else {CcSet.i().data2.bt_s_flip[0] = true;}

				if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_s(0);
				}
			}
		};
		OSCListener bt_2_sListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_2_s Listener received ");
				Integer bt_2_sListenerObj = (Integer) obj.get(0);

                if (bt_2_sListenerObj == 0) {CcSet.i().data2.bt_s_flip[1] = false;}
                else {CcSet.i().data2.bt_s_flip[1] = true;}

                if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_s(1);
				}
			}
		};
		OSCListener bt_3_sListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_3_s Listener received ");
				Integer bt_3_sListenerObj = (Integer) obj.get(0);

                if (bt_3_sListenerObj == 0) {CcSet.i().data2.bt_s_flip[2] = false;}
                else {CcSet.i().data2.bt_s_flip[2] = true;}

                if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_s(2);
				}
			}
		};
		OSCListener bt_4_sListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_4_s Listener received ");
				Integer bt_4_sListenerObj = (Integer) obj.get(0);

                if (bt_4_sListenerObj == 0) {CcSet.i().data2.bt_s_flip[3] = false;}
                else {CcSet.i().data2.bt_s_flip[3] = true;}

                if (pageT2_active) {
                    Resolume2Fragment.updateOSCbt_s(3);
				}
			}
		};

		OSCListener bt_aListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_a Listener received ");
				Integer bt_aListenerObj = (Integer) obj.get(0);
				if (pageT2_active) {
					Resolume2Fragment.updateOSCbt_seek_a(bt_aListenerObj);
				} else {
					boolean bt_aState;
					if (bt_aListenerObj == 0) {bt_aState = false;}
					else {bt_aState = true;}
					editor.putBoolean("bt_seekBar_a_state", bt_aState);
			 		editor.commit();
				}
			}
		};
		OSCListener seekbar5Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "seekbar5Listener Listener received ");
				float seekbar5ListenerObj = (Float) obj.get(0);
				if (pageT2_active) {
					Resolume2Fragment.updateOSCseekbar5(seekbar5ListenerObj);
				} else {
					final int seekBar5ProgIn = Utils.ConvertRange(0, 1, 0, 100, seekbar5ListenerObj);
					editor.putInt("seekBar5Prog", seekBar5ProgIn);
			 		editor.commit();
				}
			}
		};
		OSCListener bt_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_b Listener received ");
				Integer bt_bListenerObj = (Integer) obj.get(0);
				if (pageT2_active) {
					Resolume2Fragment.updateOSCbt_seek_b(bt_bListenerObj);
				} else {
					boolean bt_bState;
					if (bt_bListenerObj == 0) {bt_bState = false;}
					else {bt_bState = true;}
					editor.putBoolean("bt_seekBar_b_state", bt_bState);
			 		editor.commit();
				}
			}
		};
		
		//fragment3

		//fragment4
		OSCListener verticalSeekbarFx1Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx1 Listener received ");
				float verticalSeekbarFx1Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx1Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[0] = verticalSeekBarFxProgIn;

				if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
					Log.d("HowFar", "VresControlActivity | verticalSeekbarFx1Listener | pageT4_active && fxSlidersModeInt == 0");
					//Resolume4Fragment.updateOSCverticalSeekbarFx1(verticalSeekbarFx1Obj);
                    Resolume4Fragment.updateOSCverticalSeekbarFx(0, verticalSeekBarFxProgIn);
				} else {
//					final int verticalSeekBarFx1ProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx1Obj);
//					editor.putInt("verticalSeekbar_fx1Progress_link", verticalSeekBarFx1ProgIn);
//					editor.commit();
//					Log.d("HowFar", "VresControlActivity | verticalSeekbarFx1Listener | !pageT4_active OR fxSlidersModeInt == 1");
//					Log.e("appSettingsReadWrite", "verticalSeekbar_fx1Progress_link - " + verticalSeekBarFx1ProgIn);
				}
			}
		};
		OSCListener verticalSeekbarFx2Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx2 Listener received ");
				float verticalSeekbarFx2Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx2Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[1] = verticalSeekBarFxProgIn;

				if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(1, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx3Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx3 Listener received ");
				float verticalSeekbarFx3Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx3Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[2] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(2, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx4Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx4 Listener received ");
				float verticalSeekbarFx4Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx4Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[3] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(3, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx5Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx5 Listener received ");
				float verticalSeekbarFx5Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx5Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[4] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(4, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx6Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx6 Listener received ");
				float verticalSeekbarFx6Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx6Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[5] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(5, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx7Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx7 Listener received ");
				float verticalSeekbarFx7Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx7Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[6] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(6, verticalSeekBarFxProgIn);
                }
			}
		};
		OSCListener verticalSeekbarFx8Listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx8 Listener received ");
				float verticalSeekbarFx8Obj = (Float) obj.get(0);

                final int verticalSeekBarFxProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx8Obj);
                CcSet.i().data4.verticalSeekbar_fxProgress_link[7] = verticalSeekBarFxProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 0) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(7, verticalSeekBarFxProgIn);
                }
			}
		};
		
		OSCListener verticalSeekbarFx1_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx1_Opacity Listener received ");
				float verticalSeekbarFx1_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx1_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[0] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
					Resolume4Fragment.updateOSCverticalSeekbarFx(0, verticalSeekBarFx_OpacityProgIn);
				} else {
//					final int verticalSeekBarFx1_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx1_OpacityObj);
//					editor.putInt("verticalSeekbar_fx1Progress_opacity", verticalSeekBarFx1_OpacityProgIn);
//					editor.commit();
				}
			}
		};
		OSCListener verticalSeekbarFx2_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx2_Opacity Listener received ");
				float verticalSeekbarFx2_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx2_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[1] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(1, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx3_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx3_Opacity Listener received ");
				float verticalSeekbarFx3_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx3_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[2] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(2, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx4_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx4_Opacity Listener received ");
				float verticalSeekbarFx4_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx4_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[3] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(3, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx5_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx5_Opacity Listener received ");
				float verticalSeekbarFx5_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx5_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[4] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(4, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx6_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx6_Opacity Listener received ");
				float verticalSeekbarFx6_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx6_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[5] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(5, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx7_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx7_Opacity Listener received ");
                float verticalSeekbarFx7_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx7_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[6] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(6, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		OSCListener verticalSeekbarFx8_OpacityListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "verticalSeekbarFx8_Opacity Listener received ");
				float verticalSeekbarFx8_OpacityObj = (Float) obj.get(0);

                final int verticalSeekBarFx_OpacityProgIn = Utils.ConvertRange(0, 1, 0, 100, verticalSeekbarFx8_OpacityObj);
                CcSet.i().data4.verticalSeekbar_fxProgress_opacity[7] = verticalSeekBarFx_OpacityProgIn;

                if (pageT4_active && CcSet.i().data4.fxSlidersModeInt == 1) {
                    Resolume4Fragment.updateOSCverticalSeekbarFx(7, verticalSeekBarFx_OpacityProgIn);
                }
            }
		};
		
		OSCListener bt_fx1_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx1_b Listener received ");
				Integer bt_fx1_bListenerObj = (Integer) obj.get(0);

                if (bt_fx1_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[0] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[0] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(0);
				}
			}
		};
		OSCListener bt_fx2_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx2_b Listener received ");
				Integer bt_fx2_bListenerObj = (Integer) obj.get(0);

                if (bt_fx2_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[1] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[1] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(1);
				}
			}
		};
		OSCListener bt_fx3_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx3_b Listener received ");
				Integer bt_fx3_bListenerObj = (Integer) obj.get(0);

                if (bt_fx3_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[2] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[2] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(2);
				}
			}
		};
		OSCListener bt_fx4_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx4_b Listener received ");
				Integer bt_fx4_bListenerObj = (Integer) obj.get(0);

                if (bt_fx4_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[3] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[3] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(3);
				}
			}
		};
		OSCListener bt_fx5_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx5_b Listener received ");
				Integer bt_fx5_bListenerObj = (Integer) obj.get(0);

                if (bt_fx5_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[4] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[4] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(4);
				}
			}
		};
		OSCListener bt_fx6_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx6_b Listener received ");
				Integer bt_fx6_bListenerObj = (Integer) obj.get(0);

                if (bt_fx6_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[5] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[5] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(5);
				}
			}
		};
		OSCListener bt_fx7_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx7_b Listener received ");
				Integer bt_fx7_bListenerObj = (Integer) obj.get(0);

                if (bt_fx7_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[6] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[6] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(6);
				}
			}
		};
		OSCListener bt_fx8_bListener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> obj = message.getArguments();
				Log.d("OSC", "bt_fx8_b Listener received ");
				Integer bt_fx8_bListenerObj = (Integer) obj.get(0);

                if (bt_fx8_bListenerObj == 0) {CcSet.i().data4.bt_fx_b_flip[7] = false;}
                else {CcSet.i().data4.bt_fx_b_flip[7] = true;}

				if (pageT4_active) {
                    Resolume4Fragment.updataOSCbt_fx_b(7);
				}
			}
		};
		
		//fragment1
		receiver.addListener("/composition/select", bt_compListener);
		receiver.addListener("/layer1/select", bt_layer1Listener);
		receiver.addListener("/layer2/select", bt_layer2Listener);
		receiver.addListener("/layer3/select", bt_layer3Listener);
		receiver.addListener("/layer4/select", bt_layer4Listener);
		receiver.addListener("/activeclip/audio/beatloop", bt_beatloopListener);
		receiver.addListener("/playbackcontroller/paused", pausedListener);
		receiver.addListener("/composition/bypassed", compBypassedListener);
		receiver.addListener("/playbackcontroller/bpm", bpmListener);
		receiver.addListener("/composition/opacityandvolume", opacityandvolumeListener);
		
		//fragment2
		receiver.addListener("/layer1/audio/volume/values", verticalSeekbar1_audioListener);
		receiver.addListener("/layer2/audio/volume/values", verticalSeekbar2_audioListener);
		receiver.addListener("/layer3/audio/volume/values", verticalSeekbar3_audioListener);
		receiver.addListener("/layer4/audio/volume/values", verticalSeekbar4_audioListener);
		
		receiver.addListener("/layer1/video/opacityandvolume", verticalSeekbar1_avListener);
		receiver.addListener("/layer2/video/opacityandvolume", verticalSeekbar2_avListener);
		receiver.addListener("/layer3/video/opacityandvolume", verticalSeekbar3_avListener);
		receiver.addListener("/layer4/video/opacityandvolume", verticalSeekbar4_avListener);
		
		receiver.addListener("/layer1/video/opacity/values", verticalSeekbar1Listener);
		receiver.addListener("/layer2/video/opacity/values", verticalSeekbar2Listener);
		receiver.addListener("/layer3/video/opacity/values", verticalSeekbar3Listener);
		receiver.addListener("/layer4/video/opacity/values", verticalSeekbar4Listener);
		
		receiver.addListener("/layer1/bypassed", bt_1_bListener);
		receiver.addListener("/layer2/bypassed", bt_2_bListener);
		receiver.addListener("/layer3/bypassed", bt_3_bListener);
		receiver.addListener("/layer4/bypassed", bt_4_bListener);
		receiver.addListener("/layer1/solo", bt_1_sListener);
		receiver.addListener("/layer2/solo", bt_2_sListener);
		receiver.addListener("/layer3/solo", bt_3_sListener);
		receiver.addListener("/layer4/solo", bt_4_sListener);
		receiver.addListener("/composition/fadetogroupa", bt_aListener);
		receiver.addListener("/composition/cross/values", seekbar5Listener);
		receiver.addListener("/composition/fadetogroupb", bt_bListener);
		//fragment3
		//fragment4
		receiver.addListener("/composition/link1/values", verticalSeekbarFx1Listener);
		receiver.addListener("/composition/link2/values", verticalSeekbarFx2Listener);
		receiver.addListener("/composition/link3/values", verticalSeekbarFx3Listener);
		receiver.addListener("/composition/link4/values", verticalSeekbarFx4Listener);
		receiver.addListener("/composition/link5/values", verticalSeekbarFx5Listener);
		receiver.addListener("/composition/link6/values", verticalSeekbarFx6Listener);
		receiver.addListener("/composition/link7/values", verticalSeekbarFx7Listener);
		receiver.addListener("/composition/link8/values", verticalSeekbarFx8Listener);
		receiver.addListener("/composition/video/effect1/opacity/values", verticalSeekbarFx1_OpacityListener);
		receiver.addListener("/composition/video/effect2/opacity/values", verticalSeekbarFx2_OpacityListener);
		receiver.addListener("/composition/video/effect3/opacity/values", verticalSeekbarFx3_OpacityListener);
		receiver.addListener("/composition/video/effect4/opacity/values", verticalSeekbarFx4_OpacityListener);
		receiver.addListener("/composition/video/effect5/opacity/values", verticalSeekbarFx5_OpacityListener);
		receiver.addListener("/composition/video/effect6/opacity/values", verticalSeekbarFx6_OpacityListener);
		receiver.addListener("/composition/video/effect7/opacity/values", verticalSeekbarFx7_OpacityListener);
		receiver.addListener("/composition/video/effect8/opacity/values", verticalSeekbarFx8_OpacityListener);
		receiver.addListener("/composition/video/effect1/bypassed", bt_fx1_bListener);
		receiver.addListener("/composition/video/effect2/bypassed", bt_fx2_bListener);
		receiver.addListener("/composition/video/effect3/bypassed", bt_fx3_bListener);
		receiver.addListener("/composition/video/effect4/bypassed", bt_fx4_bListener);
		receiver.addListener("/composition/video/effect5/bypassed", bt_fx5_bListener);
		receiver.addListener("/composition/video/effect6/bypassed", bt_fx6_bListener);
		receiver.addListener("/composition/video/effect7/bypassed", bt_fx7_bListener);
		receiver.addListener("/composition/video/effect8/bypassed", bt_fx8_bListener);
		
		receiver.startListening();

		} catch (SocketException e) {e.printStackTrace();
		}
		
		Log.d("HowFar", "VresControlActivity | OSCListen setup");
	}	// END if receiveOSC
	}

	protected void doOSCSend(final String OSCAddress, final Object args) {
		Log.d("HowFar", "VresControlActivity | doOSCSend");
		if (sendOSC) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					//
			try {
				OSCPortOut sender = new OSCPortOut(serverComms.getResolumeAddr(), serverComms.getResolumeUdpPort());
				//Object argsB [] = {args};
				List<Object> argsB = new ArrayList<Object>(1);
				argsB.add(args);
				OSCMessage msg = new OSCMessage("/" + OSCAddress, argsB);
				
				try {
					sender.send(msg);
					Log.d("UDP", "/" + OSCAddress + " sent to " + serverComms.getResolumeAddr() + " + " + serverComms.getResolumeUdpPort());
				} catch (Exception e) {
					Log.e("UDP", "runOSCSend - /" + OSCAddress + " NOT sent"); e.printStackTrace();
				}
				
			} catch (SocketException e) {
				Log.e("UDP", "runOSCSend - SocketException /" + OSCAddress);
				e.printStackTrace();
			}
					//
				}
			}).start();

		} else {Log.e("UDP", "runOSCSend - NOT sent cos no IP");}
	}
	
	public void onFrag1OSCSendInt(String OSCAddress, int argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag1OSCSendInt");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
	public void onFrag1OSCSendFloat(String OSCAddress, float argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag1OSCSendFloat");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
//	public void onFrag1OSCSendString(String OSCAddress, String argsOut) {
//		Log.d("HowFar", "VresControlActivity | onFrag1OSCSendString");
//		Object args = argsOut;
//		doOSCSend(OSCAddress, args);
//	}
	
	public void onFrag2OSCSendInt(String OSCAddress, int argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag2OSCSendInt");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
	public void onFrag2OSCSendFloat(String OSCAddress, float argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag2OSCSendFloat");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
//	public void onFrag2OSCSendString(String OSCAddress, String argsOut) {
//		Log.d("HowFar", "VresControlActivity | onFrag2OSCSendString");
//		Object args = argsOut;
//		doOSCSend(OSCAddress, args);
//	}
	
	public void onFrag3OSCSendInt(String OSCAddress, int argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag3OSCSendInt");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
//	public void onFrag3OSCSendFloat(String OSCAddress, float argsOut) {
//		Log.d("HowFar", "VresControlActivity | onFrag3OSCSendFloat");
//		Object args = argsOut;
//		doOSCSend(OSCAddress, args);
//	}
//	public void onFrag3OSCSendString(String OSCAddress, String argsOut) {
//		Log.d("HowFar", "VresControlActivity | onFrag3OSCSendString");
//		Object args = argsOut;
//		doOSCSend(OSCAddress, args);
//	}
	
	public void onFrag4OSCSendInt(String OSCAddress, int argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag4OSCSendInt");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
	public void onFrag4OSCSendFloat(String OSCAddress, float argsOut) {
		Log.d("HowFar", "VresControlActivity | onFrag4OSCSendFloat");
		Object args = argsOut;
		doOSCSend(OSCAddress, args);
	}
//	public void onFrag4OSCSendString(String OSCAddress, String argsOut) {
//		Log.d("HowFar", "VresControlActivity | onFrag4OSCSendString");
//		Object args = argsOut;
//		doOSCSend(OSCAddress, args);
//	}

	public void runToast(String text, int length) {
		int length2;
		if (length == 1) {
			length2 = Toast.LENGTH_LONG;
		} else {
			length2 = Toast.LENGTH_SHORT;
		}
		Toast.makeText(VresControlActivity.this, text, length2).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == 1) {
			Log.d("Bt", "VresControlActivity | onActivityResult == 1");
			isLeaving = true;
	        finish();
	    } else {
			Log.d("Bt", "VresControlActivity | onActivityResult == 0");
			getSavedState();
	    }
	}
	
	private void getSavedState() {
		curCheckPosition = appSettings.getInt("curChoice", 0);
		prevCheckPosition = appSettings.getInt("prevChoice", 0);
		keepScreenOn = appSettings.getBoolean("keepScreenOn", true);
		fullScreen = appSettings.getBoolean("fullScreen", true);
		rotateLayout = appSettings.getBoolean("rotateLayout", false);
		layout_r = appSettings.getBoolean("layout_r", false);
		invertTabs = appSettings.getBoolean("invertTabs", false);
		swapLayerSliders = appSettings.getBoolean("swapLayerSliders", false);
		swapSliderBts = appSettings.getBoolean("swapSliderBts", false);
		vResOSCserver = appSettings.getBoolean("vResOSCserver", false);
		vResOSCserverIp = appSettings.getString("vResOSCserverIp", "192.168.0.25");
		mServerCommsResolumeIp = appSettings.getString("resolumeIp", "192.168.0.25");
		mServerCommsResolumeUdpPort = appSettings.getInt("resolumeUdpPort", 7000);
		localUdpPort = appSettings.getInt("localUdpPort", 7001);
		layerSlidersModeInt = appSettings.getInt("layerSlidersModeInt", 2);
        confirmExit = appSettings.getBoolean("confirmExit", false);

        CcSet.i().loadAll(appSettings);

		// using serverComms to pass numbers through so if it throws exceptions they can be handled well
		try {
			serverComms = new ServerComms(mServerCommsIp, mServerCommsUdpPort, mServerCommsResolumeIp, mServerCommsResolumeUdpPort);
		} catch (Exception ex) {System.out.println(ex.toString());}

		Log.d("HowFar", "VresControlActivity | getSavedState");
	}
	
	private void setSavedState() {
		Editor editor = appSettings.edit();
		editor.putInt("curChoice", curCheckPosition);
		editor.putInt("prevChoice", prevCheckPosition);
 		editor.commit();

        CcSet.i().saveAll(appSettings);
 		
		Log.d("HowFar", "VresControlActivity | setSavedState");
	}


	private void doYouWantToExit() {
		Log.d("HowFar", "settings | runCentrethedeskDialog");
		dialog = new Dialog(VresControlActivity.this);
		dialog.setContentView(R.layout.dialog_settings_yesno);
		dialog.setTitle(R.string.dialog_settings_doyouwanttoexit_title);
		dialog.setCancelable(true);
		TextView text = (TextView) dialog.findViewById(R.id.textView);
		text.setText(R.string.dialog_settings_doyouwanttoexit_title);
		Button button = (Button) dialog.findViewById(R.id.dialog_cancel);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bt", "doYouWantToExit dialog Cancel bt triggered");
                dialog.dismiss();
            }
        });
		Button button2 = (Button) dialog.findViewById(R.id.dialog_ok);
		button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bt", "doYouWantToExit dialog Ok bt triggered");
                dialog.dismiss();
                runToast("Bye!", 0);
                doExit();
            }
        });
		dialog.show();
	}

    private void doExit() {
        finish();
    }
}
