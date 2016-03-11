package com.visutal.vrescontrol;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import com.visutal.utils.DoneOnEditorActionListener;
import com.visutal.utils.Utils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class Resolume1Fragment extends Fragment {

	SharedPreferences appSettings;
    private Dialog dialog;
    static View view;
	
    //private static EditText hiddenForFocus1;

	private static int blue_off = R.drawable.shape_bt1_nostroke;
	private static int blue_on = R.drawable.shape_bt1b_blue_nostroke;
	private static int orange_off = R.drawable.shape_bt1_orange;
	private static int orange_on = R.drawable.shape_bt1b_orange;
	private static int orange_c = R.drawable.shape_bt1c_orange;
	
	private static int currentLayer = 0;
	private static int prevLayer = 0;
	private static Button bt_comp;
    private static Button bt_layer1;
    private static Button bt_layer2;
    private static Button bt_layer3;
    private static Button bt_layer4;
    public static boolean bt_compFlip = false;
    public static boolean bt_compState = false;
	public static boolean bt_layer1Flip = false;
	public static boolean bt_layer2Flip = false;
	public static boolean bt_layer3Flip = false;
	public static boolean bt_layer4Flip = false;

	public static boolean bt_compLock = false;
	private static boolean bt_layer1Lock = false;
	private static boolean bt_layer2Lock = false;
	private static boolean bt_layer3Lock = false;
	private static boolean bt_layer4Lock = false;
	
	private Button bt_comp_dir0;
	private Button bt_comp_dir1;
	private Button bt_comp_dir2;
	private Button bt_comp_dir3;
	private static String compDirOSCadrPrt = "composition";		// gets swapped about in swapBtLayer

	private static Button[] bt_jumptopoi;
	private Button bt_jumptopoi_0;
	private Button bt_jumptopoi_1;
	private Button bt_jumptopoi_2;
	private Button bt_jumptopoi_3;
	private Button bt_jumptopoi_4;
	private Button bt_jumptopoi_5;
	
	private static int currentBeatloop = 0;
	private static int prevBeatloop = 0;
	private static Button bt_beatloop_1;
	private static Button bt_beatloop_2;
	private static Button bt_beatloop_3;
	private static Button bt_beatloop_4;
	private static Button bt_beatloop_5;
	private static Button bt_beatloop_6;
	private static Button bt_beatloop_7;
	public static boolean bt_beatloop_1Flip = false;
	public static boolean bt_beatloop_2Flip = false;
	public static boolean bt_beatloop_3Flip = false;
	public static boolean bt_beatloop_4Flip = false;
	public static boolean bt_beatloop_5Flip = false;
	public static boolean bt_beatloop_6Flip = false;
	public static boolean bt_beatloop_7Flip = false;

	private static Button bt_bypassed;
	public static boolean bt_bypassedFlip = false;
	private static boolean bt_bypassedLock = false;
	public boolean confirmX = false;
	private Button bt_disconnectall;
	private static Button bt_paused;
	public static boolean bt_pausedFlip = false;
	private static boolean bt_pausedLock = false;
	private Button bt_resync;
	private Button bt_tap;
	private static EditText editText_bpm;
	private String editText_bpm_String;
	private static double bpmInDouble = 120.00;
	private static float bpmFloat = 12000;		// times 1000 so avoid double problem  -  need to fix this!!!
	private double bpmOutDouble;
	
	private Button bt_opacityandvolume_down;
	private static SeekBar seekBar_opacityandvolume;
	private static Boolean seekBar_opacityandvolume_inUse = false;
	private static int progress_opacityandvolumeInt = 100;
	private Button bt_opacityandvolume_up;
	
	private EditText hiddenForFocus1;

	
	Frag1OSCSend mCallbackOSCSend;

    // Container Activity must implement this interface
	public interface Frag1OSCSend {
        void onFrag1OSCSendInt(String OSCAddress, int argsOut);
        void onFrag1OSCSendFloat(String OSCAddress, float argsOut);
        //public void onFrag1OSCSendString(String OSCAddress, String argsOut);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	mCallbackOSCSend = (Frag1OSCSend) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Frag1OSCSendInt, Frag1OSCSendFloat, Frag1OSCSendString");
        }
    }
	
	
	public static Resolume1Fragment newInstance(int index) {
		Log.w("HowFar", "Resolume1Fragment | newInstance");
		Resolume1Fragment r1 = new Resolume1Fragment();
		
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		r1.setArguments(args);
		
		return r1;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		Log.d("HowFar", "Resolume1Fragment | onCreateView");

		if (container == null) {
			// Currently in a layout without a container, so no reason to create our view.
			Log.d("HowFar", "Resolume1Fragment - Currently in a layout without a container, so no reason to create our view.");
			return null;
		}
	    view = inflater.inflate(R.layout.fragment_resolume1, container, false);
	    Log.d("HowFar", "Resolume1Fragment | onCreateView | inflated fragment_resolume1");

	    // ...buttons....
		hiddenForFocus1 = (EditText) view.findViewById(R.id.hiddenForFocus1);
		
	    setupLayerBt();
	    setupCompDirBt();
	    setupJumpToPoiBt();
	    setupBeatLoopBt();
	    setupTransportBt();
	    
	    Log.d("Bt", "Resolume1Fragment | onCreateView | all bts' set..");

		hiddenForFocus1.requestFocus();
		
		Log.d("Bt", "Resolume1Fragment | onCreateView | setup");
	    return view;
	  }
	
	void setupLayerBt() {
		bt_comp = (Button) view.findViewById(R.id.bt_comp);
	    bt_layer1 = (Button) view.findViewById(R.id.bt_layer1);
	    bt_layer2 = (Button) view.findViewById(R.id.bt_layer2);
	    bt_layer3 = (Button) view.findViewById(R.id.bt_layer3);
	    bt_layer4 = (Button) view.findViewById(R.id.bt_layer4);

		bt_comp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!bt_compLock) {mCallbackOSCSend.onFrag1OSCSendInt("composition/select", 1);}
				Log.d("Bt", "bt_comp");
			}
		});
		bt_layer1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("layer1/select", 1);
				Log.d("Bt", "bt_layer1");
			}
		});
		bt_layer2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("layer2/select", 1);
				Log.d("Bt", "bt_layer2");
			}
		});
		bt_layer3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("layer3/select", 1);
				Log.d("Bt", "bt_layer3");
			}
		});
		bt_layer4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag1OSCSendInt("layer4/select", 1);
                Log.d("Bt", "bt_layer4");
            }
        });
		
		Log.d("Bt", "Resolume1Fragment | onCreateView | setupLayerBt setup");
	}
	
	void setupCompDirBt() {
		bt_comp_dir0 = (Button) view.findViewById(R.id.bt_comp_dir0);
	    bt_comp_dir1 = (Button) view.findViewById(R.id.bt_comp_dir1);
	    bt_comp_dir2 = (Button) view.findViewById(R.id.bt_comp_dir2);
	    bt_comp_dir3 = (Button) view.findViewById(R.id.bt_comp_dir3);
		
		bt_comp_dir0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag1OSCSendInt(compDirOSCadrPrt + "/direction", 0);
                Log.d("Bt", "bt_comp_dir0");
            }
        });
		bt_comp_dir1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag1OSCSendInt(compDirOSCadrPrt + "/direction", 1);
                Log.d("Bt", "bt_comp_dir1");
            }
        });
		bt_comp_dir2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag1OSCSendInt(compDirOSCadrPrt + "/direction", 2);
                Log.d("Bt", "bt_comp_dir2");
            }
        });
		bt_comp_dir3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallbackOSCSend.onFrag1OSCSendInt(compDirOSCadrPrt + "/direction", 3);
                Log.d("Bt", "bt_comp_dir3");
            }
        });

		Log.d("Bt", "Resolume1Fragment | onCreateView | setupCompDirBt setup");
	}
	
	void setupJumpToPoiBt() {
		bt_jumptopoi = new Button[]{
				(Button) view.findViewById(R.id.bt_jumptopoi_0),
				(Button) view.findViewById(R.id.bt_jumptopoi_1),
				(Button) view.findViewById(R.id.bt_jumptopoi_2),
				(Button) view.findViewById(R.id.bt_jumptopoi_3),
				(Button) view.findViewById(R.id.bt_jumptopoi_4),
				(Button) view.findViewById(R.id.bt_jumptopoi_5),
		};

		bt_jumptopoi_0 = (Button) view.findViewById(R.id.bt_jumptopoi_0);
	    bt_jumptopoi_1 = (Button) view.findViewById(R.id.bt_jumptopoi_1);
	    bt_jumptopoi_2 = (Button) view.findViewById(R.id.bt_jumptopoi_2);
	    bt_jumptopoi_3 = (Button) view.findViewById(R.id.bt_jumptopoi_3);
	    bt_jumptopoi_4 = (Button) view.findViewById(R.id.bt_jumptopoi_4);
	    bt_jumptopoi_5 = (Button) view.findViewById(R.id.bt_jumptopoi_5);
		
		bt_jumptopoi_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 0);
				Log.d("Bt", "bt_jumptopoi_0");
			}
		});
		bt_jumptopoi_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 1);
				Log.d("Bt", "bt_jumptopoi_1");
			}
		});
		bt_jumptopoi_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 2);
				Log.d("Bt", "bt_jumptopoi_2");
			}
		});
		bt_jumptopoi_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 3);
				Log.d("Bt", "bt_jumptopoi_3");
			}
		});
		bt_jumptopoi_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 4);
				Log.d("Bt", "bt_jumptopoi_4");
			}
		});
		bt_jumptopoi_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("activeclip/audio/position/jumptopointsofinterest", 5);
				Log.d("Bt", "bt_jumptopoi_5");
			}
		});

		Log.d("Bt", "Resolume1Fragment | onCreateView | setupJumpToPoiBt setup");
	}
	
	void setupBeatLoopBt() {
		bt_beatloop_1 = (Button) view.findViewById(R.id.bt_beatloop_1);
	    bt_beatloop_2 = (Button) view.findViewById(R.id.bt_beatloop_2);
	    bt_beatloop_3 = (Button) view.findViewById(R.id.bt_beatloop_3);
	    bt_beatloop_4 = (Button) view.findViewById(R.id.bt_beatloop_4);
	    bt_beatloop_5 = (Button) view.findViewById(R.id.bt_beatloop_5);
	    bt_beatloop_6 = (Button) view.findViewById(R.id.bt_beatloop_6);
	    bt_beatloop_7 = (Button) view.findViewById(R.id.bt_beatloop_7);
	    
		bt_beatloop_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_1Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 1);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_1");
			}
		});
		bt_beatloop_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_2Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 2);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_2");
			}
		});
		bt_beatloop_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_3Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 3);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_3");
			}
		});
		bt_beatloop_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_4Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 5);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_4");
			}
		});
		bt_beatloop_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String OSCAddress = "activeclip/audio/beatloop";
                if (!bt_beatloop_5Flip) {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 7);
                } else {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
                }
                Log.d("Bt", "bt_beatloop_5");
            }
        });
		bt_beatloop_6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_6Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 4);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_6");
			}
		});
		bt_beatloop_7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String OSCAddress = "activeclip/audio/beatloop";
				if (!bt_beatloop_7Flip) {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 6);
				} else {
					mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
				}
				Log.d("Bt", "bt_beatloop_7");
			}
		});

		Log.d("Bt", "Resolume1Fragment | onCreateView | setupBeatLoopBt setup");
	}
	
	void setupTransportBt() {
        bt_bypassed = (Button) view.findViewById(R.id.bt_bypassed);
		bt_disconnectall = (Button) view.findViewById(R.id.bt_disconnectall);
        bt_paused = (Button) view.findViewById(R.id.bt_paused);
        bt_resync = (Button) view.findViewById(R.id.bt_resync);
		bt_opacityandvolume_down = (Button) view.findViewById(R.id.bt_opacityandvolume_down);
		seekBar_opacityandvolume = (SeekBar) view.findViewById(R.id.seekBar_opacityandvolume);
		bt_opacityandvolume_up = (Button) view.findViewById(R.id.bt_opacityandvolume_up);
		editText_bpm = (EditText) view.findViewById(R.id.editText_bpm);
		bt_tap = (Button) view.findViewById(R.id.bt_tap);

        bt_bypassed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v0) {
                String OSCAddress = "composition/bypassed";
                if (bt_bypassedFlip) {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
                } else {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 1);
                }
            }
        });

		bt_disconnectall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                if (confirmX) {
                    // err.. this is b****in difficult as we are inside a fragment,
                    // and want to open  a simple dialog,
                    // as opposed to the new way of transitioning to a dialog fragment,
                } else {
                    mCallbackOSCSend.onFrag1OSCSendInt("composition/disconnectall", 1);
                    Log.d("Bt", "bt_disconnectall");
                }
			}
		});

        bt_paused.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String OSCAddress = "playbackcontroller/paused";
                if (bt_pausedFlip) {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 0);
                } else {
                    mCallbackOSCSend.onFrag1OSCSendInt(OSCAddress, 1);
                }
            }
        });

		bt_opacityandvolume_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("composition/opacityandvolume", 0);
				//progress_opacityandvolumeInt = 0;
				//seekBar_opacityandvolume.setProgress(0);
				Log.d("Bt", "bt_opacityandvolume_down");
			}
		});
		seekBar_opacityandvolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                                                                @Override
                                                                public void onStopTrackingTouch(SeekBar seekBar) {
                                                                    seekBar_opacityandvolume_inUse = false;
                                                                }

                                                                @Override
                                                                public void onStartTrackingTouch(SeekBar seekBar) {
                                                                    seekBar_opacityandvolume_inUse = true;
                                                                }

                                                                @Override
                                                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                                    if (fromUser) {
                                                                        float progress_opacityandvolume = Utils.ConvertRange(0, 100, 0, 1, progress);
                                                                        mCallbackOSCSend.onFrag1OSCSendFloat("composition/opacityandvolume", progress_opacityandvolume);
                                                                        progress_opacityandvolumeInt = progress;
                                                                        Log.d("Stats", "SeekBar | progress_opacityandvolumeInt " + String.valueOf(progress));
                                                                    }
                                                                }
                                                            }
        );
		bt_opacityandvolume_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("composition/opacityandvolume", 1);
				//progress_opacityandvolumeInt = 100;
				//seekBar_opacityandvolume.setProgress(100);
				Log.d("Bt", "bt_opacityandvolume_up");
			}
		});
		editText_bpm.setOnEditorActionListener(new DoneOnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("EditText ", editText_bpm.getText().toString());
                editText_bpm_String = editText_bpm.getText().toString();
                if (editText_bpm_String.matches("")) {
                    editText_bpm_String = "0";
                }
                bpmInDouble = Double.parseDouble(editText_bpm_String);

                if (bpmInDouble >= 500.00) {
                    editText_bpm_String = String.valueOf(500);
                    bpmInDouble = 500.00;
                } else if (bpmInDouble <= 2.00) {
                    editText_bpm_String = String.valueOf(2);
                    bpmInDouble = 2.00;
                }
                editText_bpm.setText(String.valueOf(bpmInDouble));

                double bpmOldValue = bpmInDouble;
                bpmOutDouble = Utils.ConvertBpmOut(bpmOldValue);

                mCallbackOSCSend.onFrag1OSCSendFloat("composition/bpm", (float) bpmOutDouble);

                editText_bpm.clearFocus();
                hiddenForFocus1.requestFocus();

                Log.d("UDP", "editText_bpm onEditorAction");
                Log.d("UDP", "editText_bpm_String = " + editText_bpm_String);
                Log.d("UDP", "bpmInDouble = " + String.valueOf(bpmInDouble));
                Log.d("UDP", "bpmOutDouble = " + String.valueOf(bpmOutDouble));

                return false;
			}
		});
		bt_tap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("playbackcontroller/tap", 1);
				Log.d("Bt", "bt_tap");
			}
		});
		bt_resync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallbackOSCSend.onFrag1OSCSendInt("playbackcontroller/resync", 1);
				Log.d("Bt", "bt_resync");
			}
		});

		Log.d("Bt", "Resolume1Fragment | onCreateView | setupTransportBt setup");
	}
	
	@Override
    public void onResume() {
		super.onResume();
		Log.d("HowFar", "Resolume1Fragment | onResume");
		getSavedState();
		setState();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("HowFar", "Resolume1Fragment | onPause");
		setSavedState();
	}
	
	public static void updateOSCCurentLayer05(final int argl05) {
		bt_compLock = true;
		bt_comp.post(new Runnable() {
			@Override
			public void run() {
				if (argl05 == 5 && bt_compState) {
					swapBtLayer(5);
					
				} else if (argl05 == 0 && !bt_compState) {
					swapBtLayer(0);
				}
				bt_compLock = false;
			}
		});
		Log.d("HowFar", "Resolume1Fragment | updateOSCCurentLayer05 In");
	}
	public static void updateOSCCurentLayer1(final int argl1) {
		bt_layer1Lock = true;
		bt_layer1.post(new Runnable() {
			@Override
			public void run() {
				if (!bt_layer1Flip) {
					swapBtLayer(1);
				}
				bt_layer1Lock = false;
			}
		});
		Log.d("HowFar", "Resolume1Fragment | updateOSCCurentLayer1 In");
	}
	public static void updateOSCCurentLayer2(final int argl2) {
		bt_layer2Lock = true;
		bt_layer2.post(new Runnable() {
			@Override
			public void run() {
				if (!bt_layer2Flip) {
					swapBtLayer(2);
				}
				bt_layer2Lock = false;
			}
		});
		Log.d("HowFar", "Resolume1Fragment | updateOSCCurentLayer2 In");
	}
	public static void updateOSCCurentLayer3(final int argl3) {
		bt_layer3Lock = true;
		bt_layer3.post(new Runnable() {
			@Override
			public void run() {
				if (!bt_layer3Flip) {
					swapBtLayer(3);
				}
				bt_layer3Lock = false;
			}
		});
		Log.d("HowFar", "Resolume1Fragment | updateOSCCurentLayer3 In");
	}
	public static void updateOSCCurentLayer4(final int argl4) {
		bt_layer4Lock = true;
		bt_layer4.post(new Runnable() {
			@Override
			public void run() {
				if (!bt_layer4Flip) {
					swapBtLayer(4);
				}
				bt_layer4Lock = false;
			}
		});
		Log.d("HowFar", "Resolume1Fragment | updateOSCCurentLayer4 In");
	}
	
	public static void updateOSCCurentBeatLoop(final int argbl) {
		new Runnable() {
		    public void run() {
				swapBtBeatLoop(argbl);
		    }
		};
		Log.d("HowFar", "updateOSCCurentLayer In");
	}
	
	public static void updateOSCpaused(int argp) {
		//bt_pausedLock = true;
		if (argp == 0) {bt_pausedFlip = false;}
		else {bt_pausedFlip = true;}
		bt_paused.post(new Runnable() {
			public void run() {
				//bt_paused.setChecked(bt_pausedFlip);
				if (bt_pausedFlip) {
					bt_paused.setBackgroundResource(orange_on);
				} else {
					bt_paused.setBackgroundResource(orange_off);
				}
                //bt_pausedLock = false;  // ???
			}
		});
		Log.d("HowFar", "updateOSCInt bt_paused In");
	}

	public static void updateOSCcompBypassed(int argb) {
		bt_bypassedLock = true;
		if (argb == 0) {bt_bypassedFlip = false;}
		else {bt_bypassedFlip = true;}
		bt_bypassed.post(new Runnable() {
			public void run() {
				if (bt_bypassedFlip) {
					bt_bypassed.setBackgroundResource(orange_on);
				} else {
					bt_bypassed.setBackgroundResource(orange_off);
				}
                bt_bypassedLock = false; // ???
			}
		});
		Log.d("HowFar", "OSCListen bt_bypassed In - bt_bypassedFlip = true");
	}
	
	public static void updateOSCopacityandvolume(float argo) {
		if (!seekBar_opacityandvolume_inUse) {
			final int seekBar_opacityandvolumeIn = Utils.ConvertRange(0, 1, 0, 100, argo);
			seekBar_opacityandvolume.post(new Runnable() {
				public void run() {
					seekBar_opacityandvolume.setProgress(seekBar_opacityandvolumeIn);
					}
				});
			Log.d("HowFar", "OSCListen updateOSCopacityandvolume " + String.valueOf(progress_opacityandvolumeInt));
		}
	}

	public static void updateOSCbpm(double argbpm) {
		//double bpmOldValue = argbpm;
		Log.d("HowFar", "OSCListen bpmOldValue - (Double) " + argbpm);
		bpmInDouble = Utils.ConvertBpmIn(argbpm);
		editText_bpm.post(new Runnable() {
			public void run() {
				editText_bpm.setText(String.valueOf(bpmInDouble));
				}
			});
		Log.d("HowFar", "OSCListen editText_bpm In");
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
	
	public static void swapBtLayer(int index) {
		Log.d("HowFar", "Resolume1Fragment | swapBtLayer " + index);
		
		if (index != currentLayer) {swapBtBeatLoop(0);}
		
		prevLayer = currentLayer;
		currentLayer = index;
		
		if (index == 0) {
			bt_compFlip = true;
			bt_compState = true;
			bt_comp.setBackgroundResource(orange_on);
		} else if (index == 1) {
			bt_layer1Flip = true;
			bt_layer1.setBackgroundResource(orange_on);
		} else if (index == 2) {
			bt_layer2Flip = true;
			bt_layer2.setBackgroundResource(orange_on);
		} else if (index == 3) {
			bt_layer3Flip = true;
			bt_layer3.setBackgroundResource(orange_on);
		} else if (index == 4) {
			bt_layer4Flip = true;
			bt_layer4.setBackgroundResource(orange_on);
		} else if (index == 5) {
			bt_compFlip = true;
			bt_compState = false;
			bt_comp.setBackgroundResource(orange_c);
		}
		
		if (index == 0 | index == 5) {compDirOSCadrPrt = "composition";} 
		else {compDirOSCadrPrt = "layer"+index+"/video/position";}		// + /direction

		if (prevLayer == 0 && index != 5) {bt_comp.setBackgroundResource(orange_off); bt_compFlip = false;}
		if (prevLayer == 1) {bt_layer1.setBackgroundResource(orange_off); bt_layer1Flip = false;}
		if (prevLayer == 2) {bt_layer2.setBackgroundResource(orange_off); bt_layer2Flip = false;}
		if (prevLayer == 3) {bt_layer3.setBackgroundResource(orange_off); bt_layer3Flip = false;}
		if (prevLayer == 4) {bt_layer4.setBackgroundResource(orange_off); bt_layer4Flip = false;}
		if (prevLayer == 5 && index != 0) {bt_comp.setBackgroundResource(orange_off); bt_compFlip = false;}
	}
	
	public static void swapBtBeatLoop(int index) {
		Log.d("HowFar", "Resolume1Fragment | swapBtBeatLoop (" + index + ")");

		prevBeatloop = currentBeatloop;
		currentBeatloop = index;
		
		if (index == 0) {
			if (bt_beatloop_1Flip) {bt_beatloop_1.setBackgroundResource(blue_off); bt_beatloop_1Flip = false;}
			if (bt_beatloop_2Flip) {bt_beatloop_2.setBackgroundResource(blue_off); bt_beatloop_2Flip = false;}
			if (bt_beatloop_3Flip) {bt_beatloop_3.setBackgroundResource(blue_off); bt_beatloop_3Flip = false;}
			if (bt_beatloop_4Flip) {bt_beatloop_4.setBackgroundResource(blue_off); bt_beatloop_4Flip = false;}
			if (bt_beatloop_5Flip) {bt_beatloop_5.setBackgroundResource(blue_off); bt_beatloop_5Flip = false;}
			if (bt_beatloop_6Flip) {bt_beatloop_6.setBackgroundResource(blue_off); bt_beatloop_6Flip = false;}
			if (bt_beatloop_7Flip) {bt_beatloop_7.setBackgroundResource(blue_off); bt_beatloop_7Flip = false;}
		} else if (index == 1) {
			bt_beatloop_1Flip = true;
			bt_beatloop_1.setBackgroundResource(blue_on);
		} else if (index == 2) {
			bt_beatloop_2Flip = true;
			bt_beatloop_2.setBackgroundResource(blue_on);
		} else if (index == 3) {
			bt_beatloop_3Flip = true;
			bt_beatloop_3.setBackgroundResource(blue_on);
		} else if (index == 4) {
			bt_beatloop_4Flip = true;
			bt_beatloop_4.setBackgroundResource(blue_on);
		} else if (index == 5) {
			bt_beatloop_5Flip = true;
			bt_beatloop_5.setBackgroundResource(blue_on);
		} else if (index == 6) {
			bt_beatloop_6Flip = true;
			bt_beatloop_6.setBackgroundResource(blue_on);
		} else if (index == 7) {
			bt_beatloop_7Flip = true;
			bt_beatloop_7.setBackgroundResource(blue_on);
		}

		if (prevBeatloop == 1) {bt_beatloop_1.setBackgroundResource(blue_off); bt_beatloop_1Flip = false;}
		if (prevBeatloop == 2) {bt_beatloop_2.setBackgroundResource(blue_off); bt_beatloop_2Flip = false;}
		if (prevBeatloop == 3) {bt_beatloop_3.setBackgroundResource(blue_off); bt_beatloop_3Flip = false;}
		if (prevBeatloop == 4) {bt_beatloop_4.setBackgroundResource(blue_off); bt_beatloop_4Flip = false;}
		if (prevBeatloop == 5) {bt_beatloop_5.setBackgroundResource(blue_off); bt_beatloop_5Flip = false;}
		if (prevBeatloop == 6) {bt_beatloop_6.setBackgroundResource(blue_off); bt_beatloop_6Flip = false;}
		if (prevBeatloop == 7) {bt_beatloop_7.setBackgroundResource(blue_off); bt_beatloop_7Flip = false;}
	}
	
	private void getSavedState() {
		appSettings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

		prevLayer = appSettings.getInt("prevLayer", 0);
		currentLayer = appSettings.getInt("currentLayer", 0);
		bt_compFlip = appSettings.getBoolean("bt_compFlip", false);
		bt_layer1Flip = appSettings.getBoolean("bt_layer1Flip", false);
		bt_layer2Flip = appSettings.getBoolean("bt_layer2Flip", false);
		bt_layer3Flip = appSettings.getBoolean("bt_layer3Flip", false);
		bt_layer4Flip = appSettings.getBoolean("bt_layer4Flip", false);
		
		prevBeatloop = appSettings.getInt("prevBeatloop", 0);
		currentBeatloop = appSettings.getInt("currentBeatloop", 0);
		bt_beatloop_1Flip = appSettings.getBoolean("bt_beatloop_1Flip", false);
		bt_beatloop_2Flip = appSettings.getBoolean("bt_beatloop_2Flip", false);
		bt_beatloop_3Flip = appSettings.getBoolean("bt_beatloop_3Flip", false);
		bt_beatloop_4Flip = appSettings.getBoolean("bt_beatloop_4Flip", false);
		bt_beatloop_5Flip = appSettings.getBoolean("bt_beatloop_5Flip", false);
		bt_beatloop_6Flip = appSettings.getBoolean("bt_beatloop_6Flip", false);
		bt_beatloop_7Flip = appSettings.getBoolean("bt_beatloop_7Flip", false);
		
		// bpmInDouble = appSettings.getDouble("editText_bpm", 120.00);		//cannot save doubles in sharedprefs ???
		//bpmFloat = appSettings.getFloat("bpmFloat", 12000);
		//bpmInDouble = (double) (bpmFloat/1000);
		//Log.d("HowFar", "Resolume1Fragment | getSavedState | getFloat bpmFloat " + bpmFloat);
		bt_pausedFlip = appSettings.getBoolean("bt_pausedFlip", false);
		bt_bypassedFlip = appSettings.getBoolean("bt_bypassedFlip", false);
		progress_opacityandvolumeInt = appSettings.getInt("progress_opacityandvolumeInt", 100);

        confirmX = appSettings.getBoolean("confirmX", false);
		Log.d("HowFar", "Resolume1Fragment | getSavedState");
	}

	private void setState() {
		seekBar_opacityandvolume.setMax(100);
		editText_bpm.clearFocus();
		
		if (currentLayer == 0) {compDirOSCadrPrt = "composition";}
		else {compDirOSCadrPrt = "layer"+currentLayer+"/video/position";}
		
		if (currentLayer == 0) {bt_comp.setBackgroundResource(orange_on); bt_compFlip = true;}
		if (currentLayer == 1) {bt_layer1.setBackgroundResource(orange_on); bt_layer1Flip = true;}
		if (currentLayer == 2) {bt_layer2.setBackgroundResource(orange_on); bt_layer2Flip = true;}
		if (currentLayer == 3) {bt_layer3.setBackgroundResource(orange_on); bt_layer3Flip = true;}
		if (currentLayer == 4) {bt_layer4.setBackgroundResource(orange_on); bt_layer4Flip = true;}
		if (currentLayer == 5) {bt_comp.setBackgroundResource(orange_c); bt_compFlip = false;}

		if (currentBeatloop == 1) {bt_beatloop_1.setBackgroundResource(blue_on); bt_beatloop_1Flip = true;}
		if (currentBeatloop == 2) {bt_beatloop_2.setBackgroundResource(blue_on); bt_beatloop_2Flip = true;}
		if (currentBeatloop == 3) {bt_beatloop_3.setBackgroundResource(blue_on); bt_beatloop_3Flip = true;}
		if (currentBeatloop == 4) {bt_beatloop_4.setBackgroundResource(blue_on); bt_beatloop_4Flip = true;}
		if (currentBeatloop == 5) {bt_beatloop_5.setBackgroundResource(blue_on); bt_beatloop_5Flip = true;}
		if (currentBeatloop == 6) {bt_beatloop_6.setBackgroundResource(blue_on); bt_beatloop_6Flip = true;}
		if (currentBeatloop == 7) {bt_beatloop_7.setBackgroundResource(blue_on); bt_beatloop_7Flip = true;}

		if (bt_bypassedFlip) {
			bt_bypassed.setBackgroundResource(orange_on);
		} else {
			bt_bypassed.setBackgroundResource(orange_off);
		}
		if (bt_pausedFlip) {
			bt_bypassed.setBackgroundResource(orange_on);
		} else {
			bt_bypassed.setBackgroundResource(orange_off);
		}

		seekBar_opacityandvolume.setProgress(progress_opacityandvolumeInt);
		editText_bpm.setText(String.valueOf(bpmInDouble));
		
		Log.d("HowFar", "Resolume1Fragment | setState");
	}
	
	private void setSavedState() {
		// as all bts are triggered by bounce-back from resolume, this should not be needed
		Log.d("HowFar", "Resolume1Fragment | setSavedState");
	}
	
}