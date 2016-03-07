package com.visutal.vrescontrol;

//import com.actionbarsherlock.app.SherlockFragment;
import android.support.v4.app.Fragment;
import com.visutal.utils.Utils;
import com.visutal.utils.VerticalSeekBar;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Resolume2Fragment extends Fragment {
	
	private boolean LOCAL_DEBUG = Settings.LOCAL_DEBUG;

    View view;

    private static int[] viewID_verticalSeekBar = new int[4];
    private static int[] viewID_verticalSeekBar_up = new int[4];
    private static int[] viewID_verticalSeekBar_down = new int[4];
    private static int[] viewID_bt_b = new int[4];
    private static int[] viewID_bt_s = new int[4];
    private static int[] viewID_bt_x = new int[4];
    private static int[] viewID_bt_ab = new int[4];

    private static VerticalSeekBar[] verticalSeekbar = new VerticalSeekBar[4];
    private static Button[] verticalSeekbar_down = new Button[4];
    private static Button[] verticalSeekbar_up = new Button[4];
    private static boolean[] skdbu = new boolean[4];
    private static Button[] bt_x = new Button[4];
    private static Button[] bt_b = new Button[4];
    private static Button[] bt_s = new Button[4];
    private static TextView[] bt_ab = new TextView[4];
    private static boolean[] verticalSeekbar_inUse = new boolean[4];
    //private static int[] verticalSeekbarProgress = new int[4];    // switch between audio/av/video
    private static int[] verticalSeekbarProgress2 = new int[4];
    private static int[] verticalSeekbarProgressOpacity = new int[4];   // hmm... ???
    private static Button bt_centerTheSeekBar5;
    private static Button bt_seekBar_a;
    private static SeekBar seekBar5;
    private static Button bt_seekBar_b;
    private static boolean bt_seekBar_a_state = false;
    private static boolean bt_seekBar_b_state = false;

    private static String sliderOSCSendObjPrt;

    Frag2OSCSend mCallbackOSCSend;

    // Container Activity must implement this interface
	public interface Frag2OSCSend {
        public void onFrag2OSCSendInt(String OSCAddress, int argsOut);
        public void onFrag2OSCSendFloat(String OSCAddress, float argsOut);
        public void onFrag2OSCSendString(String OSCAddress, String argsOut);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	mCallbackOSCSend = (Frag2OSCSend) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Frag2OSCSendInt, Frag2OSCSendFloat, Frag2OSCSendString");
        }
    }
	

	public static Resolume2Fragment newInstance(int index) {
		Log.w("HowFar", "Resolume2Fragment | newInstance");
		Resolume2Fragment r2 = new Resolume2Fragment();
		
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		r2.setArguments(args);
		
		return r2;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("HowFar", "Resolume2Fragment | onCreateView");
		
		//getSavedState();
		
		if (container == null) {
			// Currently in a layout without a container, so no reason to create our view.
			Log.d("HowFar", "LogoSlotFragment - Currently in a layout without a container, so no reason to create our view.");
			return null;
		}
		
		// start setup view
	    view = inflater.inflate(R.layout.fragment_resolume2, container, false);
	    Log.d("HowFar", "Resolume2Fragment | onCreateView | inflated fragment_resolume2");
	    //Log.d("HowFar", "Resolume2Fragment | onCreateView | verticalSeekbar1Progress = " + verticalSeekbar1Progress);

        setViewIDs();
        setupLayer(0);
        setupLayer(1);
        setupLayer(2);
        setupLayer(3);
//	    setupLayer1();
//	    setupLayer2();
//	    setupLayer3();
//	    setupLayer4();
	    setupCrossbar();

	    //setSlidersMode();		// think this might need redoing as might need to come between declare and setuponclick
        Log.d("Bt", "Resolume2Fragment | onCreateView | all bts' set..");

        Log.d("Bt", "Resolume2Fragment | onCreateView setup");
	    return view;
	  }
	
	private void setViewIDs() {
        viewID_verticalSeekBar[0] = R.id.verticalSeekbar1;
        viewID_verticalSeekBar[1] = R.id.verticalSeekbar2;
        viewID_verticalSeekBar[2] = R.id.verticalSeekbar3;
        viewID_verticalSeekBar[3] = R.id.verticalSeekbar4;

        viewID_verticalSeekBar_up[0] = R.id.verticalSeekbar1_up;
        viewID_verticalSeekBar_up[1] = R.id.verticalSeekbar2_up;
        viewID_verticalSeekBar_up[2] = R.id.verticalSeekbar3_up;
        viewID_verticalSeekBar_up[3] = R.id.verticalSeekbar4_up;

        viewID_verticalSeekBar_down[0] = R.id.verticalSeekbar1_down;
        viewID_verticalSeekBar_down[1] = R.id.verticalSeekbar2_down;
        viewID_verticalSeekBar_down[2] = R.id.verticalSeekbar3_down;
        viewID_verticalSeekBar_down[3] = R.id.verticalSeekbar4_down;

        viewID_bt_b[0] = R.id.bt_1_b;
        viewID_bt_b[1] = R.id.bt_2_b;
        viewID_bt_b[2] = R.id.bt_3_b;
        viewID_bt_b[3] = R.id.bt_4_b;

        viewID_bt_s[0] = R.id.bt_1_s;
        viewID_bt_s[1] = R.id.bt_2_s;
        viewID_bt_s[2] = R.id.bt_3_s;
        viewID_bt_s[3] = R.id.bt_4_s;

        viewID_bt_s[0] = R.id.bt_1_x;
        viewID_bt_s[1] = R.id.bt_2_x;
        viewID_bt_s[2] = R.id.bt_3_x;
        viewID_bt_s[3] = R.id.bt_4_x;

        viewID_bt_ab[0] = R.id.bt_1_ab;
        viewID_bt_ab[1] = R.id.bt_2_ab;
        viewID_bt_ab[2] = R.id.bt_3_ab;
        viewID_bt_ab[3] = R.id.bt_4_ab;

    }

    private void setupLayer(final int i) {
        StringBuilder sb0 = new StringBuilder(7);
        sb0.append("layer").append(i).append("/");
        String s_layer = sb0.toString();

        StringBuilder sb1 = new StringBuilder(32);
        sb1.append(s_layer).append(sliderOSCSendObjPrt);
        final String s_seek = sb1.toString();

        StringBuilder sb2 = new StringBuilder(15);
        sb2.append(s_layer).append("bypassed");
        final String s_bypass = sb2.toString();

        StringBuilder sb3 = new StringBuilder(11);
        sb3.append(s_layer).append("solo");
        final String s_solo = sb3.toString();

        StringBuilder sb4 = new StringBuilder(12);
        sb4.append(s_layer).append("clear");
        final String s_clear = sb4.toString();

        verticalSeekbar_up[i] = (Button) view.findViewById(viewID_verticalSeekBar_up[i]);
        verticalSeekbar[i] = (VerticalSeekBar) view.findViewById(viewID_verticalSeekBar[i]);
        verticalSeekbar_down[i] = (Button) view.findViewById(viewID_verticalSeekBar_down[i]);

//        int[] verticalSeekbarProgress_TEMP = new int[8];
//        if (CcSet.i().data2.layerSlidersModeInt == 0) { verticalSeekbarProgress_TEMP = CcSet.i().data2.verticalSeekbarProgress_audio; }
//        else if (CcSet.i().data2.layerSlidersModeInt == 1) { verticalSeekbarProgress_TEMP = CcSet.i().data2.verticalSeekbarProgress_av; }
//        else if (CcSet.i().data2.layerSlidersModeInt == 2) { verticalSeekbarProgress_TEMP = CcSet.i().data2.verticalSeekbarProgress_video; }

//        switch (CcSet.i().data2.layerSlidersModeInt) {
//            case '0':
//
//                break;
//            case '1':
//
//                break;
//            case '2':
//
//                break;
//        }

        bt_b[i] = (Button) view.findViewById(viewID_bt_b[i]);
        bt_s[i] = (Button) view.findViewById(viewID_bt_s[i]);
        bt_x[i] = (Button) view.findViewById(viewID_bt_x[i]);
        bt_ab[i] = (Button) view.findViewById(viewID_bt_ab[i]);

        verticalSeekbar_up[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendFloat(s_seek, 1);
                //verticalSeekbarProgress[i] = 100;
                //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                verticalSeekbar[i].setSecondaryProgress(verticalSeekbarProgress2[i]);   // not saved
                //verticalSeekbar[i].setProgressAndThumb(verticalSeekbarProgress[i]);
                switch (CcSet.i().data2.layerSlidersModeInt) {
                    case '0':
                        CcSet.i().data2.verticalSeekbarProgress_audio[i] = 100;
                        break;
                    case '1':
                        CcSet.i().data2.verticalSeekbarProgress_av[i] = 100;
                        break;
                    case '2':
                        CcSet.i().data2.verticalSeekbarProgress_video[i] = 100;
                        break;
                }
                verticalSeekbarProgress2[i] = 100;
                verticalSeekbar[i].setProgressAndThumb(100);
                Log.d("Bt", "verticalSeekbar_up " + i);
            }
        });
        verticalSeekbar[i].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                                                          @Override
                                                          public void onStopTrackingTouch(SeekBar seekBar1) {
                                                              verticalSeekbar_inUse[i] = false;
                                                              skdbu[i] = false;
                                                              //seekBar1.setSecondaryProgress(verticalSeekbarProgress[i]);
                                                              //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                                                              switch (CcSet.i().data2.layerSlidersModeInt) {
                                                                  case '0':
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_audio[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_audio[i];
                                                                      break;
                                                                  case '1':
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_av[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_av[i];
                                                                      break;
                                                                  case '2':
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_video[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_audio[i];
                                                                      break;
                                                              }
                                                              Log.d("OSC", "Resolume2Fragment seekBar | onStopTrackingTouch");
                                                          }

                                                          @Override
                                                          public void onStartTrackingTouch(SeekBar seekBar1) {
                                                              verticalSeekbar_inUse[i] = true;
//                                                              if (verticalSeekbarProgress[i] == 0) {
//                                                                  skdbu[i] = true;
//                                                              }
                                                              //seekBar1.setSecondaryProgress(verticalSeekbarProgress[i]);
                                                              //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                                                              switch (CcSet.i().data2.layerSlidersModeInt) {
                                                                  case '0':
                                                                      if (CcSet.i().data2.verticalSeekbarProgress_audio[i] == 0) { skdbu[i] = true; }
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_audio[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_audio[i];
                                                                      break;
                                                                  case '1':
                                                                      if (CcSet.i().data2.verticalSeekbarProgress_av[i] == 0) { skdbu[i] = true; }
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_av[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_av[i];
                                                                      break;
                                                                  case '2':
                                                                      if (CcSet.i().data2.verticalSeekbarProgress_video[i] == 0) { skdbu[i] = true; }
                                                                      seekBar1.setSecondaryProgress(CcSet.i().data2.verticalSeekbarProgress_video[i]);
                                                                      verticalSeekbarProgress2[i] = CcSet.i().data2.verticalSeekbarProgress_video[i];
                                                                      break;
                                                              }
                                                              Log.d("OSC", "Resolume2Fragment seekBar | onStartTrackingTouch");
                                                          }

                                                          @Override
                                                          public void onProgressChanged(SeekBar seekBar1, int progress1, boolean fromUser) {
                                                              Log.d("OSC", "Resolume2Fragment verticalSeekbar | onProgressChanged | progress = " + String.valueOf(progress1));
                                                              if (verticalSeekbar_inUse[i]) {
                                                                  if (fromUser) {
                                                                      float progress1b = Utils.ConvertRange(0, 100, 0, 1, progress1);
                                                                      mCallbackOSCSend.onFrag2OSCSendFloat(s_seek, progress1b);

                                                                      if (skdbu[i]) {
                                                                          seekBar1.setSecondaryProgress(progress1);
                                                                      }

                                                                      Log.d("OSC", "Resolume2Fragment verticalSeekbar | onProgressChanged | fromUser");
                                                                      Log.d("OSC", "Resolume2Fragment verticalSeekbar | onProgressChanged | verticalSeekbar1Progress = " + String.valueOf(progress1));
                                                                      Log.d("OSC", "Resolume2Fragment layerX/video/opacity/values " + Float.toString(progress1b));
                                                                  } else {
                                                                      Log.d("OSC", "Resolume2Fragment verticalSeekbar | onProgressChanged | !fromUser");
                                                                  }
                                                              }
                                                              //verticalSeekbarProgress[i] = progress1;
                                                              switch (CcSet.i().data2.layerSlidersModeInt) {
                                                                    case '0':
                                                                        CcSet.i().data2.verticalSeekbarProgress_audio[i] = progress1;
                                                                        break;
                                                                    case '1':
                                                                        CcSet.i().data2.verticalSeekbarProgress_av[i] = progress1;
                                                                        break;
                                                                    case '2':
                                                                        CcSet.i().data2.verticalSeekbarProgress_video[i] = progress1;
                                                                        break;
                                                              }
                                                          }
                                                      }
        );
        verticalSeekbar_down[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendFloat(s_seek, 0);
                //verticalSeekbarProgress[i] = 0;
                //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                switch (CcSet.i().data2.layerSlidersModeInt) {
                    case '0':
                        CcSet.i().data2.verticalSeekbarProgress_audio[i] = 0;
                        break;
                    case '1':
                        CcSet.i().data2.verticalSeekbarProgress_av[i] = 0;
                        break;
                    case '2':
                        CcSet.i().data2.verticalSeekbarProgress_video[i] = 0;
                        break;
                }
                verticalSeekbarProgress2[i] = 0;
                verticalSeekbar[i].setSecondaryProgress(verticalSeekbarProgress2[i]);
                //verticalSeekbar[i].setProgressAndThumb(verticalSeekbarProgress[i]);
                verticalSeekbar[i].setProgressAndThumb(0);
                Log.d("Bt", "verticalSeekbar_down");
            }
        });

        bt_b[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CcSet.i().data2.bt_b_flip[i]) {
                    mCallbackOSCSend.onFrag2OSCSendInt(s_bypass, 0);
                } else {
                    mCallbackOSCSend.onFrag2OSCSendInt(s_bypass, 1);
                }
            }
        });
        bt_s[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (CcSet.i().data2.bt_s_flip[i]) {
                        mCallbackOSCSend.onFrag2OSCSendInt(s_solo, 0);
                    } else {
                        mCallbackOSCSend.onFrag2OSCSendInt(s_solo, 1);
                    }
            }
        });
        bt_x[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendInt(s_clear, 1);
                Log.d("Bt", "bt_x " + i);
            }
        });

        bt_ab[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // this should all be set from when bounceback from resolume received
//                switch (CcSet.i().data2.bt_ab_state[i]) {
//                    case 0:
//                        bt_ab[i].setBackgroundResource(R.drawable.shape_bt1_orange_left_nostroke);
//                        bt_ab[i].setText("A");
//                        CcSet.i().data2.bt_ab_state[i] = 1;
//                        break;
//                    case 1:
//                        bt_ab[i].setBackgroundResource(R.drawable.shape_bt1_orange_right_nostroke);
//                        bt_ab[i].setText("B");
//                        CcSet.i().data2.bt_ab_state[i] = 2;
//                        break;
//                    case 2:
//                        bt_ab[i].setBackgroundResource(R.drawable.shape_bt1_nostroke);
//                        bt_ab[i].setText("A/B");
//                        CcSet.i().data2.bt_ab_state[i] = 0;
//                        break;
//                }
                //Log.d("Bt", "bt_abState");
            }
        });

        Log.d("Bt", "Resolume2Fragment | onCreateView | setupLayer setup " + i);
    }

//    void setupLayer1() {
//	    verticalSeekbar1 = (VerticalSeekBar) view.findViewById(R.id.verticalSeekbar1);
//        verticalSeekbar1_up = (Button) view.findViewById(R.id.verticalSeekbar1_up);
//	    verticalSeekbar1_down = (Button) view.findViewById(R.id.verticalSeekbar1_down);
//		//bt_1_b = (ToggleButton) view.findViewById(R.id.bt_1_b);
//		//bt_1_s = (ToggleButton) view.findViewById(R.id.bt_1_s);
//		bt_1_b = (Button) view.findViewById(R.id.bt_1_b);
//		bt_1_s = (Button) view.findViewById(R.id.bt_1_s);
//	    bt_1_x = (Button) view.findViewById(R.id.bt_1_x);
//        bt_1_ab = (TextView) view.findViewById(R.id.bt_1_ab);
//
//   		verticalSeekbar1_up.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer1/" + sliderOSCSendObjPrt, 1);
//   				verticalSeekbar1Progress = 100;
//   				verticalSeekbar1Progress2 = verticalSeekbar1Progress;
//   				verticalSeekbar1.setSecondaryProgress(verticalSeekbar1Progress2);
//   				verticalSeekbar1.setProgressAndThumb(verticalSeekbar1Progress);
//   				Log.d("Bt", "verticalSeekbar1_up" + String.valueOf(verticalSeekbar1Progress));
//   			}
//   		});
//        verticalSeekbar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//   			@Override
//   			public void onStopTrackingTouch(SeekBar seekBar1) {
//   				verticalSeekbar1_inUse = false;
//   				skdbu1 = false;
//   				seekBar1.setSecondaryProgress(verticalSeekbar1Progress);
//   				verticalSeekbar1Progress2 = verticalSeekbar1Progress;
//   				Log.d("OSC", "Resolume2Fragment seekBar1 | onStopTrackingTouch");
//   			}
//   			@Override
//   			public void onStartTrackingTouch(SeekBar seekBar1) {
//   				verticalSeekbar1_inUse = true;
//   				if (verticalSeekbar1Progress == 0) {skdbu1 = true;}
//   				seekBar1.setSecondaryProgress(verticalSeekbar1Progress);
//   				verticalSeekbar1Progress2 = verticalSeekbar1Progress;
//   				Log.d("OSC", "Resolume2Fragment seekBar1 | onStartTrackingTouch");
//   			}
//   			@Override
//   			public void onProgressChanged(SeekBar seekBar1, int progress1, boolean fromUser) {
//   				Log.d("OSC", "Resolume2Fragment verticalSeekbar1 | onProgressChanged | progress = " + String.valueOf(progress1));
//   				if (verticalSeekbar1_inUse) {
//		   			if (fromUser) {
//		   				float progress1b = Utils.ConvertRange(0, 100, 0, 1, progress1);
//		   				mCallbackOSCSend.onFrag2OSCSendFloat("layer1/" + sliderOSCSendObjPrt, progress1b);
//		   				//runOSCSendFloat("layer1/" + sliderOSCSendObjPrt, progress1b);
//
//		   				if (skdbu1) {seekBar1.setSecondaryProgress(progress1);}
//
//		   				Log.d("OSC", "Resolume2Fragment verticalSeekbar1 | onProgressChanged | fromUser");
//		   				Log.d("OSC", "Resolume2Fragment verticalSeekbar1 | onProgressChanged | verticalSeekbar1Progress = " + String.valueOf(progress1));
//		   				Log.d("OSC", "Resolume2Fragment layer1/video/opacity/values " + Float.toString(progress1b));
//		   			} else {
//		   				Log.d("OSC", "Resolume2Fragment verticalSeekbar1 | onProgressChanged | !fromUser");
//		   			}
//   				}
//   				verticalSeekbar1Progress = progress1;
//   			}
//   		}
//   		);
//   		verticalSeekbar1_down.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer1/" + sliderOSCSendObjPrt, 0);
//   				verticalSeekbar1Progress = 0;
//   				verticalSeekbar1Progress2 = verticalSeekbar1Progress;
//   				verticalSeekbar1.setSecondaryProgress(verticalSeekbar1Progress2);
//   				verticalSeekbar1.setProgressAndThumb(verticalSeekbar1Progress);
//   				Log.d("Bt", "verticalSeekbar1_down" + String.valueOf(verticalSeekbar1Progress));
//   			}
//   		});
////        bt_1_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////   			@Override
////   			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////   				if (!bt_1_bLock) {
////	   				String OSCAddress = "layer1/bypassed";
////	   				if (isChecked) {
////	   	   				mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
////	   					Log.d("Bt", "bt_1_b enabled");
////	   				}
////	   				else {
////	   	   				mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
////	   					Log.d("Bt", "bt_1_b disabled");
////	   				}
////   				}
////   			}
////   		});
////        bt_1_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////   			@Override
////   			public void onCheckedChanged(CompoundButton buttonView, boolean isCheckeds1) {
////   				if (!bt_1_sLock) {
////	   				String OSCAddress = "layer1/solo";
////	   				if (isCheckeds1) {
////	   	   				mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
////	   					Log.d("Bt", "bt_1_s enabled");
////	   				}
////	   				else {
////	   	   				mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
////	   					Log.d("Bt", "bt_1_s disabled");
////	   				}
////   				}
////   			}
////   		});
//		bt_1_b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_1_bLock) {
//					String OSCAddress = "layer1/bypassed";
//					if (bt_1_bFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//		bt_1_s.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_1_sLock) {
//					String OSCAddress = "layer1/solo";
//					if (bt_1_sFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_1_x.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendInt("layer1/clear", 1);
//   				Log.d("Bt", "bt_1_x");
//   			}
//   		});
//        bt_1_ab.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				switch (bt_1_abState) {
//   					case 0:
//   						bt_1_ab.setBackgroundResource(R.drawable.shape_bt1_orange_left_nostroke);
//   						bt_1_ab.setText("A");
//   						bt_1_abState = 1;
//   						break;
//   					case 1:
//   						bt_1_ab.setBackgroundResource(R.drawable.shape_bt1_orange_right_nostroke);
//   						bt_1_ab.setText("B");
//   						bt_1_abState = 2;
//   						break;
//   					case 2:
//   						bt_1_ab.setBackgroundResource(R.drawable.shape_bt1_nostroke);
//   						bt_1_ab.setText("A/B");
//   						bt_1_abState = 0;
//   						break;
//   				}
//   				Log.d("Bt", "bt_1_abState");
//   			}
//   		});
//
//		Log.d("Bt", "Resolume2Fragment | onCreateView | setupLayer1 setup");
//		return;
//	}
//
//	void setupLayer2() {
//		verticalSeekbar2 = (VerticalSeekBar) view.findViewById(R.id.verticalSeekbar2);
//	    verticalSeekbar2_up = (Button) view.findViewById(R.id.verticalSeekbar2_up);
//	    verticalSeekbar2_down = (Button) view.findViewById(R.id.verticalSeekbar2_down);
//	    bt_2_b = (ToggleButton) view.findViewById(R.id.bt_2_b);
//	    bt_2_s = (ToggleButton) view.findViewById(R.id.bt_2_s);
//	    bt_2_x = (Button) view.findViewById(R.id.bt_2_x);
//        bt_2_ab = (TextView) view.findViewById(R.id.bt_2_ab);
//
//		verticalSeekbar2_up.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer2/" + sliderOSCSendObjPrt, 1);
//				verticalSeekbar2Progress = 100;
//				verticalSeekbar2Progress2 = verticalSeekbar2Progress;
//				verticalSeekbar2.setSecondaryProgress(verticalSeekbar2Progress);
//				verticalSeekbar2.setProgressAndThumb(verticalSeekbar2Progress);
//				Log.d("Bt", "verticalSeekbar2_up");
//			}
//		});
//        verticalSeekbar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar2) {
//				verticalSeekbar2_inUse = false;
//				skdbu2 = false;
//				seekBar2.setSecondaryProgress(seekBar2.getProgress());
//				verticalSeekbar2Progress2 = verticalSeekbar2Progress;
//			}
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar2) {
//				verticalSeekbar2_inUse = true;
//				if (verticalSeekbar2Progress == 0) {skdbu2 = true;}
//				seekBar2.setSecondaryProgress(verticalSeekbar2Progress);
//				verticalSeekbar2Progress2 = verticalSeekbar2Progress;
//			}
//			@Override
//			public void onProgressChanged(SeekBar seekBar2, int progress2, boolean fromUser) {
//				if (verticalSeekbar2_inUse) {
//					if (fromUser) {
//						float progress2b = Utils.ConvertRange(0, 100, 0, 1, progress2);
//		   				mCallbackOSCSend.onFrag2OSCSendFloat("layer2/" + sliderOSCSendObjPrt, progress2b);
//						if (skdbu2) {seekBar2.setSecondaryProgress(progress2);}
//					}
//				}
//				verticalSeekbar2Progress = progress2;
//			}
//		}
//		);
//		verticalSeekbar2_down.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer2/" + sliderOSCSendObjPrt, 0);
//				verticalSeekbar2Progress = 0;
//				verticalSeekbar2Progress2 = verticalSeekbar2Progress;
//				verticalSeekbar2.setProgressAndThumb(verticalSeekbar2Progress);
//				verticalSeekbar2.setSecondaryProgress(verticalSeekbar2Progress);
//				Log.d("Bt", "verticalSeekbar2_down");
//			}
//		});
//        bt_2_b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_2_bLock) {
//					String OSCAddress = "layer2/bypassed";
//					if (bt_2_bFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_2_s.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_2_sLock) {
//					String OSCAddress = "layer2/solo";
//					if (bt_2_sFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					}
//					else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_2_x.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag2OSCSendInt("layer2/clear", 1);
//				Log.d("Bt", "bt_2_x");
//				}
//		});
//        bt_2_ab.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				switch (bt_2_abState) {
//					case 0:
//						bt_2_ab.setBackgroundResource(R.drawable.shape_bt1_orange_left_nostroke);
//						bt_2_ab.setText("A");
//						bt_2_abState = 1;
//						break;
//					case 1:
//						bt_2_ab.setBackgroundResource(R.drawable.shape_bt1_orange_right_nostroke);
//						bt_2_ab.setText("B");
//						bt_2_abState = 2;
//						break;
//					case 2:
//						bt_2_ab.setBackgroundResource(R.drawable.shape_bt1_nostroke);
//						bt_2_ab.setText("A/B");
//						bt_2_abState = 0;
//						break;
//				}
//				Log.d("Bt", "bt_2_abState");
//			}
//		});
//
//		Log.d("Bt", "Resolume2Fragment | onCreateView | setupLayer2 setup");
//		return;
//	}
//
//	void setupLayer3() {
//		verticalSeekbar3 = (VerticalSeekBar) view.findViewById(R.id.verticalSeekbar3);
//	    verticalSeekbar3_up = (Button) view.findViewById(R.id.verticalSeekbar3_up);
//	    verticalSeekbar3_down = (Button) view.findViewById(R.id.verticalSeekbar3_down);
//	    bt_3_b = (ToggleButton) view.findViewById(R.id.bt_3_b);
//	    bt_3_s = (ToggleButton) view.findViewById(R.id.bt_3_s);
//	    bt_3_x = (Button) view.findViewById(R.id.bt_3_x);
//        bt_3_ab = (TextView) view.findViewById(R.id.bt_3_ab);
//
//   		verticalSeekbar3_up.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer3/" + sliderOSCSendObjPrt, 1);
//   				verticalSeekbar3Progress = 100;
//   				verticalSeekbar3Progress2 = verticalSeekbar3Progress;
//   				verticalSeekbar3.setSecondaryProgress(verticalSeekbar3Progress);
//   				verticalSeekbar3.setProgressAndThumb(verticalSeekbar3Progress);
//   				Log.d("Bt", "verticalSeekbar3_up");
//   			}
//   		});
//        verticalSeekbar3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//   			@Override
//   			public void onStopTrackingTouch(SeekBar seekBar3) {
//   				verticalSeekbar3_inUse = false;
//   				skdbu3 = false;
//   				seekBar3.setSecondaryProgress(seekBar3.getProgress());
//   				verticalSeekbar3Progress2 = verticalSeekbar3Progress;
//   			}
//   			@Override
//   			public void onStartTrackingTouch(SeekBar seekBar3) {
//   				verticalSeekbar3_inUse = true;
//   				if (verticalSeekbar3Progress == 0) {skdbu3 = true;}
//   				seekBar3.setSecondaryProgress(verticalSeekbar3Progress);
//   				verticalSeekbar3Progress2 = verticalSeekbar3Progress;
//   			}
//   			@Override
//   			public void onProgressChanged(SeekBar seekBar3, int progress3, boolean fromUser) {
//   				if (verticalSeekbar3_inUse) {
//		   			if (fromUser) {
//		   				float progress3b = Utils.ConvertRange(0, 100, 0, 1, progress3);
//		   				mCallbackOSCSend.onFrag2OSCSendFloat("layer3/" + sliderOSCSendObjPrt, progress3b);
//		   				if (skdbu3) {seekBar3.setSecondaryProgress(progress3);}
//		   			}
//   				}
//   				verticalSeekbar3Progress = progress3;
//   			}
//   		}
//   		);
//   		verticalSeekbar3_down.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer3/" + sliderOSCSendObjPrt, 0);
//   				verticalSeekbar3Progress = 0;
//   				verticalSeekbar3Progress2 = verticalSeekbar3Progress;
//   				verticalSeekbar3.setSecondaryProgress(verticalSeekbar3Progress);
//   				verticalSeekbar3.setProgressAndThumb(verticalSeekbar3Progress);
//   				Log.d("Bt", "verticalSeekbar3_down");
//   			}
//   		});
//        bt_3_b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_3_bLock) {
//					String OSCAddress = "layer3/bypassed";
//					if (bt_3_bFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_3_s.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_3_sLock) {
//					String OSCAddress = "layer3/solo";
//					if (bt_3_sFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_3_x.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag2OSCSendInt("layer3/clear", 1);
//   				Log.d("Bt", "bt_3_x");
//   				}
//   		});
//        bt_3_ab.setOnClickListener(new OnClickListener() {
//   			@Override
//   			public void onClick(View arg0) {
//   				switch (bt_3_abState) {
//   					case 0:
//   						bt_3_ab.setBackgroundResource(R.drawable.shape_bt1_orange_left_nostroke);
//   						bt_3_ab.setText("A");
//   						bt_3_abState = 1;
//   						break;
//   					case 1:
//   						bt_3_ab.setBackgroundResource(R.drawable.shape_bt1_orange_right_nostroke);
//   						bt_3_ab.setText("B");
//   						bt_3_abState = 2;
//   						break;
//   					case 2:
//   						bt_3_ab.setBackgroundResource(R.drawable.shape_bt1_nostroke);
//   						bt_3_ab.setText("A/B");
//   						bt_3_abState = 0;
//   						break;
//   				}
//   				Log.d("Bt", "bt_3_abState");
//   			}
//   		});
//
//		Log.d("Bt", "Resolume2Fragment | onCreateView | setupLayer3 setup");
//		return;
//	}
//
//	void setupLayer4() {
//		verticalSeekbar4 = (VerticalSeekBar) view.findViewById(R.id.verticalSeekbar4);
//		verticalSeekbar4_up = (Button) view.findViewById(R.id.verticalSeekbar4_up);
//        verticalSeekbar4_down = (Button) view.findViewById(R.id.verticalSeekbar4_down);
//        bt_4_b = (ToggleButton) view.findViewById(R.id.bt_4_b);
//        bt_4_s = (ToggleButton) view.findViewById(R.id.bt_4_s);
//        bt_4_x = (Button) view.findViewById(R.id.bt_4_x);
//        bt_4_ab = (TextView) view.findViewById(R.id.bt_4_ab);
//
//  		verticalSeekbar4_up.setOnClickListener(new OnClickListener() {
//  			@Override
//  			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer4/" + sliderOSCSendObjPrt, 1);
//  				verticalSeekbar4Progress = 100;
//  				verticalSeekbar4Progress2 = verticalSeekbar4Progress;
//  				verticalSeekbar4.setSecondaryProgress(verticalSeekbar4Progress);
//  				verticalSeekbar4.setProgressAndThumb(verticalSeekbar4Progress);
//  				Log.d("Bt", "verticalSeekbar4_up");
//  			}
//  		});
//        verticalSeekbar4.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//  			@Override
//  			public void onStopTrackingTouch(SeekBar seekBar4) {
//  				verticalSeekbar4_inUse = false;
//  				skdbu4 = false;
//  				seekBar4.setSecondaryProgress(seekBar4.getProgress());
//  				verticalSeekbar4Progress2 = verticalSeekbar4Progress;
//  			}
//  			@Override
//  			public void onStartTrackingTouch(SeekBar seekBar4) {
//  				verticalSeekbar4_inUse = true;
//  				if (verticalSeekbar4Progress == 0) {skdbu4 = true;}
//  				seekBar4.setSecondaryProgress(verticalSeekbar4Progress);
//  				verticalSeekbar4Progress2 = verticalSeekbar4Progress;
//  			}
//  			@Override
//  			public void onProgressChanged(SeekBar seekBar4, int progress4, boolean fromUser) {
//  				if (verticalSeekbar4_inUse) {
//		  			if (fromUser) {
//		  				float progress4b = Utils.ConvertRange(0, 100, 0, 1, progress4);
//		   				mCallbackOSCSend.onFrag2OSCSendFloat("layer4/" + sliderOSCSendObjPrt, progress4b);
//		  				if (skdbu4) {seekBar4.setSecondaryProgress(progress4);}
//		  			}
//  				}
//  				verticalSeekbar4Progress = progress4;
//  			}
//  		}
//  		);
//  		verticalSeekbar4_down.setOnClickListener(new OnClickListener() {
//  			@Override
//  			public void onClick(View arg0) {
//   				mCallbackOSCSend.onFrag2OSCSendFloat("layer4/" + sliderOSCSendObjPrt, 0);
//  				verticalSeekbar4Progress = 0;
//  				verticalSeekbar4Progress2 = verticalSeekbar4Progress;
//  				verticalSeekbar4.setSecondaryProgress(verticalSeekbar4Progress);
//  				verticalSeekbar4.setProgressAndThumb(verticalSeekbar4Progress);
//  				Log.d("Bt", "verticalSeekbar4_down");
//  			}
//  		});
//        bt_4_b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_4_bLock) {
//					String OSCAddress = "layer4/bypassed";
//					if (bt_4_bFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_4_s.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!bt_4_sLock) {
//					String OSCAddress = "layer4/solo";
//					if (bt_4_sFlip) {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 0);
//					} else {
//						mCallbackOSCSend.onFrag2OSCSendInt(OSCAddress, 1);
//					}
//				}
//			}
//		});
//        bt_4_x.setOnClickListener(new OnClickListener() {
//  			@Override
//  			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag2OSCSendInt("layer4/clear", 1);
//  				Log.d("Bt", "bt_4_x");
//  				}
//  		});
//        bt_4_ab.setOnClickListener(new OnClickListener() {
//  			@Override
//  			public void onClick(View arg0) {
//  				switch (bt_4_abState) {
//  					case 0:
//  						bt_4_ab.setBackgroundResource(R.drawable.shape_bt1_orange_left_nostroke);
//  						bt_4_ab.setText("A");
//  						bt_4_abState = 1;
//  						break;
//  					case 1:
//  						bt_4_ab.setBackgroundResource(R.drawable.shape_bt1_orange_right_nostroke);
//  						bt_4_ab.setText("B");
//  						bt_4_abState = 2;
//  						break;
//  					case 2:
//  						bt_4_ab.setBackgroundResource(R.drawable.shape_bt1_nostroke);
//  						bt_4_ab.setText("A/B");
//  						bt_4_abState = 0;
//  						break;
//  				}
//  				Log.d("Bt", "bt_4_abState");
//  			}
//  		});
//
//		Log.d("Bt", "Resolume2Fragment | onCreateView | setupLayer4 setup");
//		return;
//	}

	void setupCrossbar() {
		seekBar5 = (SeekBar) view.findViewById(R.id.seekBar5);
	    bt_seekBar_a = (Button) view.findViewById(R.id.bt_seekBar_a);
        bt_seekBar_b = (Button) view.findViewById(R.id.bt_seekBar_b);
        bt_centerTheSeekBar5 = (Button) view.findViewById(R.id.bt_centerSeekBar5);

//        if (CcSet.i().data2.bt_seekBar_a_state) { bt_seekBar_a.setBackgroundResource(R.drawable.shape_bt1_orange_nostroke); }
//        else { bt_seekBar_a.setBackgroundResource(R.drawable.ic_bt_left);}
//        if (CcSet.i().data2.bt_seekBar_b_state) { bt_seekBar_b.setBackgroundResource(R.drawable.shape_bt1_orange_nostroke); }
//        else { bt_seekBar_b.setBackgroundResource(R.drawable.ic_bt_right);}
        //bt_seekBar_a.setBackgroundResource(CcSet.i().blue_nostroke_left);
        //bt_seekBar_b.setBackgroundResource(CcSet.i().blue_nostroke_right);

    // bt_centerTheSeekBar5
        bt_centerTheSeekBar5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                float progress5b = Utils.ConvertRange(0, 100, 0, 1, 50);        //float 0-1 range -1-1
                mCallbackOSCSend.onFrag2OSCSendFloat("composition/cross/values", progress5b);
                //seekBar5.setProgress(50);
                //seekBar5Prog = 50;
                Log.d("Bt", "bt_centerTheSeekBar5 centered");
            }
        });
        
        bt_seekBar_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendInt("composition/fadetogroupa", 1);
                //seekBar5.setProgress(0);
                //seekBar5Prog = 0;
                //bt_seekBar_a_state = true;
                //bt_seekBar_a.setBackgroundResource(R.drawable.shape_bt1_orange_nostroke);
                Log.d("Bt", "bt_seekBar_a");
            }
        });
        seekBar5.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar5) {
				Log.d("OSC", "Resolume2Fragment seekBar5 | onStopTrackingTouch");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar5) {
				Log.d("OSC", "Resolume2Fragment seekBar5 | onStartTrackingTouch");
			}
			@Override
			public void onProgressChanged(SeekBar seekBar5, int progress5, boolean fromUser) {
				Log.d("OSC", "Resolume2Fragment seekBar5 | onProgressChanged | progress = " + String.valueOf(progress5));
				bt_seekBar_a_state = false;
				bt_seekBar_b_state = false;
				bt_seekBar_a.setBackgroundResource(R.drawable.ic_bt_left);
				bt_seekBar_b.setBackgroundResource(R.drawable.ic_bt_right);
				if (fromUser) {
					float progress5b = Utils.ConvertRange(0, 100, 0, 1, progress5);		//float 0-1 range -1-1
					mCallbackOSCSend.onFrag2OSCSendFloat("composition/cross/values", progress5b);
					//runOSCSendFloat("composition/cross/values", progress5b);
			        CcSet.i().data2.seekBar5Prog = progress5;
						Log.d("OSC", "Resolume2Fragment seekBar5 | onProgressChanged | fromUser");
						Log.d("OSC", "Resolume2Fragment seekBar5 | onProgressChanged | seekBar5Progress = " + String.valueOf(progress5));
						Log.d("OSC", "Resolume2Fragment composition/cross/values " + Float.toString(progress5b));
				} else {
					Log.d("OSC", "Resolume2Fragment seekbar5 | onProgressChanged | !fromUser");
				}
			}
		}
		);
        bt_seekBar_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendInt("composition/fadetogroupb", 1);
                //seekBar5.setProgress(100);
                //seekBar5Prog = 100;
                //bt_seekBar_b_state = true;
                //bt_seekBar_b.setBackgroundResource(R.drawable.shape_bt1_orange_nostroke);
                Log.d("Bt", "bt_seekBar_b");
            }
        });
        
		Log.d("Bt", "Resolume2Fragment | onCreateView | setupCrossbar setup");
		return;
	}

	@Override
    public void onResume() {
        super.onResume();
		Log.d("HowFar", "Resolume2Fragment | onResume");
		getSavedState();
		setSlidersMode();
		setState();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("HowFar", "Resolume2Fragment | onPause");
		setSavedState();
	}

	public static void updateOSCverticalSeekBar(final int pos) {
        if (!verticalSeekbar_inUse[pos]) {
            verticalSeekbar[pos].post(new Runnable() {
                public void run() {
                    int i = 0;
                    if(CcSet.i().data2.layerSlidersModeInt == 0) { i = CcSet.i().data2.verticalSeekbarProgress_audio[pos]; }
                    else if(CcSet.i().data2.layerSlidersModeInt == 1) { i = CcSet.i().data2.verticalSeekbarProgress_av[pos]; }
                    else if(CcSet.i().data2.layerSlidersModeInt == 2) { i = CcSet.i().data2.verticalSeekbarProgress_video[pos]; }

                    verticalSeekbar[pos].setProgressAndThumbIn(i);
                    verticalSeekbar[pos].setSecondaryProgress(i);
                }
            });
        }
    }

//    public static void updateOSCverticalSeekbar1(float argsVS1) {
//		if (!verticalSeekbar1_inUse) {
//			final int verticalSeekBar1ProgIn = Utils.ConvertRange(0, 1, 0, 100, argsVS1);
//			verticalSeekbar1.post(new Runnable() {
//				public void run() {
//					verticalSeekbar1.setProgressAndThumbIn(verticalSeekBar1ProgIn);
//					verticalSeekbar1.setSecondaryProgress(verticalSeekBar1ProgIn);
//					}
//				});
//		}
//	}
//	public static void updateOSCverticalSeekbar2(float argsVS2) {
//		if (!verticalSeekbar2_inUse) {
//			final int verticalSeekBar2ProgIn = Utils.ConvertRange(0, 1, 0, 100, argsVS2);
//			verticalSeekbar2.post(new Runnable() {
//				public void run() {
//					verticalSeekbar2.setProgressAndThumbIn(verticalSeekBar2ProgIn);
//					verticalSeekbar2.setSecondaryProgress(verticalSeekBar2ProgIn);
//					}
//				});
//		}
//	}
//	public static void updateOSCverticalSeekbar3(float argsVS3) {
//		if (!verticalSeekbar3_inUse) {
//			final int verticalSeekBar3ProgIn = Utils.ConvertRange(0, 1, 0, 100, argsVS3);
//			verticalSeekbar3.post(new Runnable() {
//				public void run() {
//					verticalSeekbar3.setProgressAndThumbIn(verticalSeekBar3ProgIn);
//					verticalSeekbar3.setSecondaryProgress(verticalSeekBar3ProgIn);
//					}
//				});
//		}
//	}
//	public static void updateOSCverticalSeekbar4(float argsVS4) {
//		if (!verticalSeekbar4_inUse) {
//			final int verticalSeekBar4ProgIn = Utils.ConvertRange(0, 1, 0, 100, argsVS4);
//			verticalSeekbar4.post(new Runnable() {
//				public void run() {
//					verticalSeekbar4.setProgressAndThumbIn(verticalSeekBar4ProgIn);
//					verticalSeekbar4.setSecondaryProgress(verticalSeekBar4ProgIn);
//					}
//				});
//		}
//	}

	public static void updateOSCbt_b (final int pos) {
        bt_b[pos].post(new Runnable() {
            public void run() {
                if (CcSet.i().data2.bt_b_flip[pos]) {
                    bt_b[pos].setBackgroundResource(CcSet.i().orange_on);
                } else {
                    bt_b[pos].setBackgroundResource(CcSet.i().orange_off);
                }
            }
        });
        Log.d("HowFar", "updateOSCInt bt_b In - " + pos);
    }

    public static void updateOSCbt_s (final int pos) {
        bt_s[pos].post(new Runnable() {
            public void run() {
                if (CcSet.i().data2.bt_s_flip[pos]) {
                    bt_s[pos].setBackgroundResource(CcSet.i().orange_on);
                } else {
                    bt_s[pos].setBackgroundResource(CcSet.i().orange_off);
                }
            }
        });
        Log.d("HowFar", "updateOSCInt bt_s In - " + pos);
    }

//    public static void updateOSCbt_1_b(int argBy1) {
//		bt_1_bLock = true;
//		int bt_1_bIn = argBy1;
//		if (bt_1_bIn == 0) {bt_1_bFlip = false;}
//		else {bt_1_bFlip = true;}
//		bt_1_b.post(new Runnable() {
//			public void run() {
//				//bt_1_b.setChecked(bt_1_bFlip);
//				if (bt_1_bFlip) {
//					bt_1_b.setBackgroundResource(orange_on);
//				} else {
//					bt_1_b.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_1_b In");
//	}
//	public static void updateOSCbt_2_b(int argBy2) {
//		bt_2_bLock = true;
//		int bt_2_bIn = argBy2;
//		if (bt_2_bIn == 0) {bt_2_bFlip = false;}
//		else {bt_2_bFlip = true;}
//		bt_2_b.post(new Runnable() {
//			public void run() {
//				if (bt_2_bFlip) {
//					bt_2_b.setBackgroundResource(orange_on);
//				} else {
//					bt_2_b.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_2_b In");
//	}
//	public static void updateOSCbt_3_b(int argBy3) {
//		bt_3_bLock = true;
//		int bt_3_bIn = argBy3;
//		if (bt_3_bIn == 0) {bt_3_bFlip = false;}
//		else {bt_3_bFlip = true;}
//		bt_3_b.post(new Runnable() {
//			public void run() {
//				if (bt_3_bFlip) {
//					bt_3_b.setBackgroundResource(orange_on);
//				} else {
//					bt_3_b.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_3_b In");
//	}
//	public static void updateOSCbt_4_b(int argBy4) {
//		bt_4_bLock = true;
//		int bt_4_bIn = argBy4;
//		if (bt_4_bIn == 0) {bt_4_bFlip = false;}
//		else {bt_4_bFlip = true;}
//		bt_4_b.post(new Runnable() {
//			public void run() {
//				if (bt_4_bFlip) {
//					bt_4_b.setBackgroundResource(orange_on);
//				} else {
//					bt_4_b.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_4_b In");
//	}

//	public static void updateOSCbt_1_s(int argSo1) {
//		bt_1_sLock = true;
//		int bt_1_sIn = argSo1;
//		if (bt_1_sIn == 0) {bt_1_sFlip = false;}
//		else {bt_1_sFlip = true;}
//		bt_1_s.post(new Runnable() {
//			public void run() {
//				if (bt_1_sFlip) {
//					bt_1_s.setBackgroundResource(orange_on);
//				} else {
//					bt_1_s.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_1_s In");
//	}
//	public static void updateOSCbt_2_s(int argSo2) {
//		bt_2_sLock = true;
//		int bt_2_sIn = argSo2;
//		if (bt_2_sIn == 0) {bt_2_sFlip = false;}
//		else {bt_2_sFlip = true;}
//		bt_2_s.post(new Runnable() {
//			public void run() {
//				if (bt_2_sFlip) {
//					bt_2_s.setBackgroundResource(orange_on);
//				} else {
//					bt_2_s.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_2_s In");
//	}
//	public static void updateOSCbt_3_s(int argSo3) {
//		bt_3_sLock = true;
//		int bt_3_sIn = argSo3;
//		if (bt_3_sIn == 0) {bt_3_sFlip = false;}
//		else {bt_3_sFlip = true;}
//		bt_3_s.post(new Runnable() {
//			public void run() {
//				if (bt_3_sFlip) {
//					bt_3_s.setBackgroundResource(orange_on);
//				} else {
//					bt_3_s.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_3_s In");
//	}
//	public static void updateOSCbt_4_s(int argSo4) {
//		bt_4_sLock = true;
//		int bt_4_sIn = argSo4;
//		if (bt_4_sIn == 0) {bt_4_sFlip = false;}
//		else {bt_4_sFlip = true;}
//		bt_4_s.post(new Runnable() {
//			public void run() {
//				if (bt_4_sFlip) {
//					bt_4_s.setBackgroundResource(orange_on);
//				} else {
//					bt_4_s.setBackgroundResource(orange_off);
//				}
//				}
//			});
//		Log.d("HowFar", "updateOSCInt bt_4_s In");
//	}
	
	//composition/fadetogroupa
	public static void updateOSCbt_seek_a(int argp) {
		//final int bt_aIn = argp; 
		seekBar5.post(new Runnable() {
			public void run() {
				seekBar5.setProgress(0);
				}
			});
		Log.d("HowFar", "updateOSCInt fadetogroupa In");
	}
	public static void updateOSCseekbar5(float argsS5) {
		final int seekBar5ProgIn = Utils.ConvertRange(0, 1, 0, 100, argsS5);
		seekBar5.post(new Runnable() {
			public void run() {
				seekBar5.setProgress(seekBar5ProgIn);
				}
			});
	}
	//composition/fadetogroupb
	public static void updateOSCbt_seek_b(int argp) {
		//final int bt_bIn = argp; 
		seekBar5.post(new Runnable() {
			public void run() {
				seekBar5.setProgress(100);
				}
			});
		Log.d("HowFar", "updateOSCInt fadetogroupb In");
	}
		
	private void swapBtSolo(int index) {
		Log.d("HowFar", "VresControlActivity swapBtSolo");
		
		if (index == 0) {
		} else if (index == 1) {
			CcSet.i().data2.bt_s_flip[0] = true;
            CcSet.i().data2.bt_s_flip[1] = false;
            CcSet.i().data2.bt_s_flip[2] = false;
            CcSet.i().data2.bt_s_flip[3] = false;
		} else if (index == 2) {
            CcSet.i().data2.bt_s_flip[0] = false;
            CcSet.i().data2.bt_s_flip[1] = true;
            CcSet.i().data2.bt_s_flip[2] = false;
            CcSet.i().data2.bt_s_flip[3] = false;
		} else if (index == 3) {
            CcSet.i().data2.bt_s_flip[0] = false;
            CcSet.i().data2.bt_s_flip[1] = false;
            CcSet.i().data2.bt_s_flip[2] = true;
            CcSet.i().data2.bt_s_flip[3] = false;
		} else if (index == 4) {
            CcSet.i().data2.bt_s_flip[0] = false;
            CcSet.i().data2.bt_s_flip[1] = false;
            CcSet.i().data2.bt_s_flip[2] = false;
            CcSet.i().data2.bt_s_flip[3] = true;
		}

//	    bt_1_s.setChecked(bt_1_sFlip);
//	    bt_2_s.setChecked(bt_2_sFlip);
//	    bt_3_s.setChecked(bt_3_sFlip);
//        bt_4_s.setChecked(bt_4_sFlip);

//		if (bt_1_sFlip) {
//			bt_1_s.setBackgroundResource(orange_on);
//		} else {
//			bt_1_s.setBackgroundResource(orange_off);
//		}
//		if (bt_2_sFlip) {
//			bt_2_s.setBackgroundResource(orange_on);
//		} else {
//			bt_2_s.setBackgroundResource(orange_off);
//		}
//		if (bt_3_sFlip) {
//			bt_3_s.setBackgroundResource(orange_on);
//		} else {
//			bt_3_s.setBackgroundResource(orange_off);
//		}
//		if (bt_4_sFlip) {
//			bt_4_s.setBackgroundResource(orange_on);
//		} else {
//			bt_4_s.setBackgroundResource(orange_off);
//		}

        for (int i = 0; i < 4; i++) {
            if (CcSet.i().data2.bt_s_flip[i]) { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_s[i].setBackgroundResource(CcSet.i().orange_off); }
        }
	}
	
	private void setSlidersMode() {
		// WTF is going on here...

		//set sliders OSC message layerX/sliderOSCSendObjPrt
    	//if (layerSlidersModeString.equals("0") && prevLayerSlidersMode != 0) {
    	if (CcSet.i().data2.layerSlidersModeInt == 0 && CcSet.i().data2.prevLayerSlidersMode != 0) {
        	sliderOSCSendObjPrt = CcSet.i().data2.sliderOSCSend0;
        	Log.d("Stats", "sliderOSCSendObjPrt " + sliderOSCSendObjPrt);
        } else if (CcSet.i().data2.layerSlidersModeInt == 1 && CcSet.i().data2.prevLayerSlidersMode != 1) {
        	sliderOSCSendObjPrt = CcSet.i().data2.sliderOSCSend1;
        	Log.d("Stats", "sliderOSCSendObjPrt " + sliderOSCSendObjPrt);
        } else if (CcSet.i().data2.layerSlidersModeInt == 2 && CcSet.i().data2.prevLayerSlidersMode != 2) {
        	sliderOSCSendObjPrt = CcSet.i().data2.sliderOSCSend2;
        	Log.d("Stats", "sliderOSCSendObjPrt " + sliderOSCSendObjPrt);
        } else if (CcSet.i().data2.layerSlidersModeInt == 2 && CcSet.i().data2.prevLayerSlidersMode == 2) {
        	// first setup
        	sliderOSCSendObjPrt = CcSet.i().data2.sliderOSCSend2;
        	Log.d("Stats", "layerSlidersMode first setup");
        }
    	
		//return;
	}
	
	public void runToast(String text, int length) {
		int length2;
		if (length == 1) {
			length2 = Toast.LENGTH_LONG;
		} else {
			length2 = Toast.LENGTH_SHORT;
		}
		Toast.makeText(this.getActivity(), text, length2).show();
	}

	private void getSavedState() {
		//appSettings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());		// ?

		CcSet.i().loadData2(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
//		prevLayerSlidersMode = appSettings.getInt("prevLayerSlidersMode", 2);	//this is so dont needlessly double select stuff
//		layerSlidersModeInt = appSettings.getInt("layerSlidersModeInt", 2);
//		// 0=Audio 1=AV 2=Video
//
//		verticalSeekbar1Progress_audio = appSettings.getInt("verticalSeekbar1Progress_audio", 0);
//		verticalSeekbar2Progress_audio = appSettings.getInt("verticalSeekbar2Progress_audio", 0);
//		verticalSeekbar3Progress_audio = appSettings.getInt("verticalSeekbar3Progress_audio", 0);
//		verticalSeekbar4Progress_audio = appSettings.getInt("verticalSeekbar4Progress_audio", 0);
//		verticalSeekbar1Progress_av = appSettings.getInt("verticalSeekbar1Progress_av", 0);
//		verticalSeekbar2Progress_av = appSettings.getInt("verticalSeekbar2Progress_av", 0);
//		verticalSeekbar3Progress_av = appSettings.getInt("verticalSeekbar3Progress_av", 0);
//		verticalSeekbar4Progress_av = appSettings.getInt("verticalSeekbar4Progress_av", 0);
//		verticalSeekbar1Progress_video = appSettings.getInt("verticalSeekbar1Progress_video", 0);
//		verticalSeekbar2Progress_video = appSettings.getInt("verticalSeekbar2Progress_video", 0);
//		verticalSeekbar3Progress_video = appSettings.getInt("verticalSeekbar3Progress_video", 0);
//		verticalSeekbar4Progress_video = appSettings.getInt("verticalSeekbar4Progress_video", 0);
//
//		bt_1_bFlip = appSettings.getBoolean("bt_1_bFlip", false);
//		bt_2_bFlip = appSettings.getBoolean("bt_2_bFlip", false);
//		bt_3_bFlip = appSettings.getBoolean("bt_3_bFlip", false);
//		bt_4_bFlip = appSettings.getBoolean("bt_4_bFlip", false);
//
//		bt_1_sFlip = appSettings.getBoolean("bt_1_sFlip", false);
//		bt_2_sFlip = appSettings.getBoolean("bt_2_sFlip", false);
//		bt_3_sFlip = appSettings.getBoolean("bt_3_sFlip", false);
//		bt_4_sFlip = appSettings.getBoolean("bt_4_sFlip", false);
//
//		bt_1_abState = appSettings.getInt("bt_1_abState", 0);
//		bt_2_abState = appSettings.getInt("bt_2_abState", 0);
//		bt_3_abState = appSettings.getInt("bt_3_abState", 0);
//		bt_4_abState = appSettings.getInt("bt_4_abState", 0);
//
//		seekBar5Prog = appSettings.getInt("seekBar5Prog", 50);
//
//		bt_seekBar_a_state = appSettings.getBoolean("bt_seekBar_a_state", false);
//		bt_seekBar_b_state = appSettings.getBoolean("bt_seekBar_b_state", false);

		Log.d("HowFar", "Resolume2Fragment | getSavedState");
        //return;
	}

	private void setState() {

	    //bt_1_b.setChecked(bt_1_bFlip);
	    //bt_1_s.setChecked(bt_1_sFlip);
	    //bt_2_b.setChecked(bt_2_bFlip);
	    //bt_2_s.setChecked(bt_2_sFlip);
	    //bt_3_b.setChecked(bt_3_bFlip);
	    //bt_3_s.setChecked(bt_3_sFlip);
        //bt_4_b.setChecked(bt_4_bFlip);
        //bt_4_s.setChecked(bt_4_sFlip);

//		if (bt_1_bFlip) { bt_1_b.setBackgroundResource(orange_on); }
//		else { bt_1_b.setBackgroundResource(orange_off); }
//		if (bt_2_bFlip) { bt_2_b.setBackgroundResource(orange_on);}
//		else { bt_2_b.setBackgroundResource(orange_off); }
//		if (bt_3_bFlip) { bt_3_b.setBackgroundResource(orange_on); }
//		else { bt_3_b.setBackgroundResource(orange_off); }
//		if (bt_4_bFlip) { bt_4_b.setBackgroundResource(orange_on); }
//		else { bt_4_b.setBackgroundResource(orange_off);
//		}
//
//		if (bt_1_sFlip) { bt_1_s.setBackgroundResource(orange_on); }
//		else { bt_1_s.setBackgroundResource(orange_off); }
//		if (bt_2_sFlip) { bt_2_s.setBackgroundResource(orange_on);}
//		else { bt_2_s.setBackgroundResource(orange_off); }
//		if (bt_3_sFlip) { bt_3_s.setBackgroundResource(orange_on); }
//		else { bt_3_s.setBackgroundResource(orange_off); }
//		if (bt_4_sFlip) { bt_4_s.setBackgroundResource(orange_on); }
//		else { bt_4_s.setBackgroundResource(orange_off);}

        //set vars for verticalSeekbar after setting up the button listeners, otherwise it returns a NULL pointer on resuming..
//        verticalSeekbar1.setMax(100);
//	    verticalSeekbar2.setMax(100);
//	    verticalSeekbar3.setMax(100);
//	    verticalSeekbar4.setMax(100);
	    //seekBar5.setMax(100);
	    
	    //verticalSeekbar1_inUse = true;
	    //Log.d("HowFar", "Resolume2Fragment | setState | layerSlidersModeInt = " + CcSet.i().data2.layerSlidersModeInt);
//	    if (CcSet.i().data2.layerSlidersModeInt == 0) {
//	    	verticalSeekbar1Progress = verticalSeekbar1Progress_audio;
//	    	verticalSeekbar2Progress = verticalSeekbar2Progress_audio;
//	    	verticalSeekbar3Progress = verticalSeekbar3Progress_audio;
//	    	verticalSeekbar4Progress = verticalSeekbar4Progress_audio;
//	    } else if (CcSet.i().data2.layerSlidersModeInt == 1) {
//	    	verticalSeekbar1Progress = verticalSeekbar1Progress_av;
//	    	verticalSeekbar2Progress = verticalSeekbar2Progress_av;
//	    	verticalSeekbar3Progress = verticalSeekbar3Progress_av;
//	    	verticalSeekbar4Progress = verticalSeekbar4Progress_av;
//	    } else if (CcSet.i().data2.layerSlidersModeInt == 2) {
//	    	verticalSeekbar1Progress = verticalSeekbar1Progress_video;
//	    	verticalSeekbar2Progress = verticalSeekbar2Progress_video;
//	    	verticalSeekbar3Progress = verticalSeekbar3Progress_video;
//	    	verticalSeekbar4Progress = verticalSeekbar4Progress_video;
//	    }

//	    verticalSeekbar1.setSecondaryProgress(verticalSeekbar1Progress);
//	    verticalSeekbar2.setSecondaryProgress(verticalSeekbar2Progress);
//	    verticalSeekbar3.setSecondaryProgress(verticalSeekbar3Progress);
//	    verticalSeekbar4.setSecondaryProgress(verticalSeekbar4Progress);
//
//        verticalSeekbar1.setProgressAndThumb(verticalSeekbar1Progress);
//        verticalSeekbar2.setProgressAndThumb(verticalSeekbar2Progress);
//        verticalSeekbar3.setProgressAndThumb(verticalSeekbar3Progress);
//        verticalSeekbar4.setProgressAndThumb(verticalSeekbar4Progress);
        
        //verticalSeekbar1_inUse = false;

        for (int i = 0; i < 4; i++) {
            if (CcSet.i().data2.bt_b_flip[i]) { bt_b[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_b[i].setBackgroundResource(CcSet.i().orange_on); }
            if (CcSet.i().data2.bt_s_flip[i]) { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }

            verticalSeekbar[i].setMax(100);

            int vsp = 0;
            if (CcSet.i().data2.layerSlidersModeInt == 0) { vsp = CcSet.i().data2.verticalSeekbarProgress_audio[i]; }
            else if (CcSet.i().data2.layerSlidersModeInt == 1) { vsp = CcSet.i().data2.verticalSeekbarProgress_av[i]; }
            else if (CcSet.i().data2.layerSlidersModeInt == 2) { vsp = CcSet.i().data2.verticalSeekbarProgress_video[i]; }
            verticalSeekbar[i].setSecondaryProgress(vsp);
            verticalSeekbar[i].setProgressAndThumb(vsp);
        }

        seekBar5.setMax(100);
        seekBar5.setProgress(CcSet.i().data2.seekBar5Prog);
        
        Log.d("HowFar", "Resolume2Fragment | setState");
        //return;
	}
	
	private void setSavedState() {
//		appSettings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());		// ?
//
//		Editor editor = appSettings.edit();
//
//		if (layerSlidersModeInt == 0) {
//			editor.putInt("verticalSeekbar1Progress_audio", verticalSeekbar1Progress);
//			editor.putInt("verticalSeekbar2Progress_audio", verticalSeekbar2Progress);
//			editor.putInt("verticalSeekbar3Progress_audio", verticalSeekbar3Progress);
//			editor.putInt("verticalSeekbar4Progress_audio", verticalSeekbar4Progress);
//		} else if (layerSlidersModeInt == 1) {
//			editor.putInt("verticalSeekbar1Progress_av", verticalSeekbar1Progress);
//			editor.putInt("verticalSeekbar2Progress_av", verticalSeekbar2Progress);
//			editor.putInt("verticalSeekbar3Progress_av", verticalSeekbar3Progress);
//			editor.putInt("verticalSeekbar4Progress_av", verticalSeekbar4Progress);
//		} else if (layerSlidersModeInt == 2) {
//			editor.putInt("verticalSeekbar1Progress_video", verticalSeekbar1Progress);
//			editor.putInt("verticalSeekbar2Progress_video", verticalSeekbar2Progress);
//			editor.putInt("verticalSeekbar3Progress_video", verticalSeekbar3Progress);
//			editor.putInt("verticalSeekbar4Progress_video", verticalSeekbar4Progress);
//		}
//
//		editor.putBoolean("bt_1_bFlip", bt_1_bFlip);
//		editor.putBoolean("bt_2_bFlip", bt_2_bFlip);
//		editor.putBoolean("bt_3_bFlip", bt_3_bFlip);
//		editor.putBoolean("bt_4_bFlip", bt_4_bFlip);
//
//		editor.putBoolean("bt_1_sFlip", bt_1_sFlip);
//		editor.putBoolean("bt_2_sFlip", bt_2_sFlip);
//		editor.putBoolean("bt_3_sFlip", bt_3_sFlip);
//		editor.putBoolean("bt_4_sFlip", bt_4_sFlip);
//
//		editor.putInt("bt_1_abState", bt_1_abState);
//		editor.putInt("bt_2_abState", bt_2_abState);
//		editor.putInt("bt_3_abState", bt_3_abState);
//		editor.putInt("bt_4_abState", bt_4_abState);
//
//		editor.putInt("seekBar5Prog", seekBar5Prog);
//
//		editor.putBoolean("bt_seekBar_a_state", bt_seekBar_a_state);
//		editor.putBoolean("bt_seekBar_b_state", bt_seekBar_b_state);
//
// 		editor.commit();
 		CcSet.i().saveData2(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
		Log.d("HowFar", "Resolume2Fragment | setSavedState");
        //return;
	}
	
}