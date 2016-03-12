package com.visutal.vrescontrol;

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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Resolume2Fragment extends Fragment {

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
    private static int[] verticalSeekbarProgress2 = new int[4];
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
        void onFrag2OSCSendInt(String OSCAddress, int argsOut);
        void onFrag2OSCSendFloat(String OSCAddress, float argsOut);
        //public void onFrag2OSCSendString(String OSCAddress, String argsOut);
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
	    setupCrossbar();

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

        viewID_bt_x[0] = R.id.bt_1_x;
        viewID_bt_x[1] = R.id.bt_2_x;
        viewID_bt_x[2] = R.id.bt_3_x;
        viewID_bt_x[3] = R.id.bt_4_x;

        viewID_bt_ab[0] = R.id.bt_1_ab;
        viewID_bt_ab[1] = R.id.bt_2_ab;
        viewID_bt_ab[2] = R.id.bt_3_ab;
        viewID_bt_ab[3] = R.id.bt_4_ab;

    }

    private void setupLayer(final int i) {
        String sb0 = "layer" + i + "/";
        String s_layer = sb0;

        String sb1 = s_layer + sliderOSCSendObjPrt;
        final String s_seek = sb1;

        String sb2 = s_layer + "bypassed";
        final String s_bypass = sb2;

        String sb3 = s_layer + "solo";
        final String s_solo = sb3;

        String sb4 = s_layer + "clear";
        final String s_clear = sb4;

        verticalSeekbar_up[i] = (Button) view.findViewById(viewID_verticalSeekBar_up[i]);
        verticalSeekbar[i] = (VerticalSeekBar) view.findViewById(viewID_verticalSeekBar[i]);
        verticalSeekbar_down[i] = (Button) view.findViewById(viewID_verticalSeekBar_down[i]);

        bt_b[i] = (Button) view.findViewById(viewID_bt_b[i]);
        bt_s[i] = (Button) view.findViewById(viewID_bt_s[i]);
        bt_x[i] = (Button) view.findViewById(viewID_bt_x[i]);
        bt_ab[i] = (TextView) view.findViewById(viewID_bt_ab[i]);

        verticalSeekbar_up[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendFloat(s_seek, 1);
                //verticalSeekbarProgress[i] = 100;
                //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                verticalSeekbar[i].setSecondaryProgress(verticalSeekbarProgress2[i]);   // not saved
                //verticalSeekbar[i].setProgressAndThumb(verticalSeekbarProgress[i]);

                CcSet.i().data2.SetVerticalSeekbarProgress(i, 100);

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

                                                              seekBar1.setSecondaryProgress(CcSet.i().data2.GetVerticalSeekbarProgress(i));
                                                              verticalSeekbarProgress2[i] = CcSet.i().data2.GetVerticalSeekbarProgress(i);

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
                                                              CcSet.i().data2.SetVerticalSeekbarProgress(i, progress1);
                                                          }
                                                      }
        );
        verticalSeekbar_down[i].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendFloat(s_seek, 0);
                //verticalSeekbarProgress[i] = 0;
                //verticalSeekbarProgress2[i] = verticalSeekbarProgress[i];
                CcSet.i().data2.SetVerticalSeekbarProgress(i, 0);
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

	void setupCrossbar() {
		seekBar5 = (SeekBar) view.findViewById(R.id.seekBar5);
	    bt_seekBar_a = (Button) view.findViewById(R.id.bt_seekBar_a);
        bt_seekBar_b = (Button) view.findViewById(R.id.bt_seekBar_b);
        bt_centerTheSeekBar5 = (Button) view.findViewById(R.id.bt_centerSeekBar5);

    // bt_centerTheSeekBar5
        bt_centerTheSeekBar5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                float progress5b = Utils.ConvertRange(0, 100, 0, 1, 50);        //float 0-1 range -1-1
                mCallbackOSCSend.onFrag2OSCSendFloat("composition/cross/values", progress5b);
                Log.d("Bt", "bt_centerTheSeekBar5 centered");
            }
        });
        
        bt_seekBar_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag2OSCSendInt("composition/fadetogroupa", 1);
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
                Log.d("Bt", "bt_seekBar_b");
            }
        });
        
		Log.d("Bt", "Resolume2Fragment | onCreateView | setupCrossbar setup");
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
//                    if(CcSet.i().data2.layerSlidersModeInt == 0) { i = CcSet.i().data2.verticalSeekbarProgress_audio[pos]; }
//                    else if(CcSet.i().data2.layerSlidersModeInt == 1) { i = CcSet.i().data2.verticalSeekbarProgress_av[pos]; }
//                    else if(CcSet.i().data2.layerSlidersModeInt == 2) { i = CcSet.i().data2.verticalSeekbarProgress_video[pos]; }

                    i = CcSet.i().data2.GetVerticalSeekbarProgress(pos);
                    verticalSeekbar[pos].setProgressAndThumbIn(i);
                    verticalSeekbar[pos].setSecondaryProgress(i);
                }
            });
        }
    }

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

        for (int i = 0; i < 4; i++) {
            if (CcSet.i().data2.bt_s_flip[i]) { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_s[i].setBackgroundResource(CcSet.i().orange_off); }
        }
	}
	
	private void setSlidersMode() {
		// WTF is going on here...

		//set sliders OSC message layerX/sliderOSCSendObjPrt
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
		CcSet.i().loadData2(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
    	Log.d("HowFar", "Resolume2Fragment | getSavedState");
	}

	private void setState() {
        for (int i = 0; i < 4; i++) {
            if (CcSet.i().data2.bt_b_flip[i]) { bt_b[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_b[i].setBackgroundResource(CcSet.i().orange_on); }
            if (CcSet.i().data2.bt_s_flip[i]) { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }
            else { bt_s[i].setBackgroundResource(CcSet.i().orange_on); }

            verticalSeekbar[i].setMax(100);

            int vsp = 0;
//            if (CcSet.i().data2.layerSlidersModeInt == 0) { vsp = CcSet.i().data2.verticalSeekbarProgress_audio[i]; }
//            else if (CcSet.i().data2.layerSlidersModeInt == 1) { vsp = CcSet.i().data2.verticalSeekbarProgress_av[i]; }
//            else if (CcSet.i().data2.layerSlidersModeInt == 2) { vsp = CcSet.i().data2.verticalSeekbarProgress_video[i]; }
            vsp = CcSet.i().data2.GetVerticalSeekbarProgress(i);
            verticalSeekbar[i].setSecondaryProgress(vsp);
            verticalSeekbar[i].setProgressAndThumb(vsp);
        }

        seekBar5.setMax(100);
        seekBar5.setProgress(CcSet.i().data2.seekBar5Prog);
        
        Log.d("HowFar", "Resolume2Fragment | setState");
	}
	
	private void setSavedState() {
 		CcSet.i().saveData2(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
		Log.d("HowFar", "Resolume2Fragment | setSavedState");
	}
	
}