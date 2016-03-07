package com.visutal.vrescontrol;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Resolume3Fragment extends Fragment {
	
	private boolean LOCAL_DEBUG = Settings.LOCAL_DEBUG;

    View view;

	private static Button[] bt_c = new Button[8];   // trigger column bt
    private static Button[] bt_l4 = new Button[8];  // layer 4 bt
    private static Button[] bt_l3 = new Button[8];  // layer 3 bt
    private static Button[] bt_l2 = new Button[8];  // layer 2 bt
    private static Button[] bt_l1 = new Button[8];  // layer 1 bt
	
	Frag3OSCSend mCallbackOSCSend;

    // Container Activity must implement this interface
	public interface Frag3OSCSend {
        public void onFrag3OSCSendInt(String OSCAddress, int argsOut);
        //public void onFrag3OSCSendFloat(String OSCAddress, float argsOut);
        //public void onFrag3OSCSendString(String OSCAddress, String argsOut);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	mCallbackOSCSend = (Frag3OSCSend) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Frag3OSCSendInt");
                    //+ " must implement Frag3OSCSendInt, Frag3OSCSendFloat, Frag3OSCSendString");
        }
    }
	

	public static Resolume3Fragment newInstance(int index) {
		Log.w("HowFar", "Resolume3Fragment | newInstance");
		Resolume3Fragment r3 = new Resolume3Fragment();
		
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		r3.setArguments(args);
		
		return r3;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		Log.d("HowFar", "Resolume3Fragment | onCreateView");

		if (container == null) {
			// Currently in a layout without a container, so no reason to create our view.
			Log.d("HowFar", "Resolume3Fragment - Currently in a layout without a container, so no reason to create our view.");
			return null;
		}
	    view = inflater.inflate(R.layout.fragment_resolume3, container, false);
	    Log.d("HowFar", "Resolume3Fragment | inflated fragment_resolume3");
	    
	    setupBt_c();
	    setupBt_l4();
	    setupBt_l3();
	    setupBt_l2();
	    setupBt_l1();
	    Log.d("Bt", "Resolume3Fragment | onCreateView | all bts' set..");

	    Log.d("Bt", "Resolume3Fragment | onCreateView setup");
	    return view;
	  }
	
	void setupBt_c() {
        int[] viewID_bt_c = new int[8];
        viewID_bt_c[0] = R.id.bt_c_1;
        viewID_bt_c[1] = R.id.bt_c_2;
        viewID_bt_c[2] = R.id.bt_c_3;
        viewID_bt_c[3] = R.id.bt_c_4;
        viewID_bt_c[4] = R.id.bt_c_5;
        viewID_bt_c[5] = R.id.bt_c_6;
        viewID_bt_c[6] = R.id.bt_c_7;
        viewID_bt_c[7] = R.id.bt_c_8;

        for (int i = 0; i < 8; i++) {
            bt_c[i] = (Button) view.findViewById(viewID_bt_c[i]);
            final String i2 = String.valueOf(i + 1);
            bt_c[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder(14);
                    sb.append("track").append(i2).append("/connect");
                    mCallbackOSCSend.onFrag3OSCSendInt(sb.toString(), 1);
                    Log.d("Bt", "bt_c");
                }
            });
        }

//	    bt_c_1 = (Button) view.findViewById(R.id.bt_c_1);
//	    bt_c_2 = (Button) view.findViewById(R.id.bt_c_2);
//	    bt_c_3 = (Button) view.findViewById(R.id.bt_c_3);
//	    bt_c_4 = (Button) view.findViewById(R.id.bt_c_4);
//	    bt_c_5 = (Button) view.findViewById(R.id.bt_c_5);
//	    bt_c_6 = (Button) view.findViewById(R.id.bt_c_6);
//	    bt_c_7 = (Button) view.findViewById(R.id.bt_c_7);
//	    bt_c_8 = (Button) view.findViewById(R.id.bt_c_8);
	    
//	    bt_c_1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track1/connect", 1);
//				Log.d("Bt", "bt_c_1");
//				}
//		});
//	    bt_c_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track2/connect", 1);
//				Log.d("Bt", "bt_c_2");
//				}
//		});
//	    bt_c_3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track3/connect", 1);
//				Log.d("Bt", "bt_c_3");
//				}
//		});
//	    bt_c_4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track4/connect", 1);
//				Log.d("Bt", "bt_c_4");
//				}
//		});
//	    bt_c_5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track5/connect", 1);
//				Log.d("Bt", "bt_c_5");
//				}
//		});
//	    bt_c_6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track6/connect", 1);
//				Log.d("Bt", "bt_c_6");
//				}
//		});
//	    bt_c_7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track7/connect", 1);
//				Log.d("Bt", "bt_c_7");
//				}
//		});
//	    bt_c_8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("track8/connect", 1);
//				Log.d("Bt", "bt_c_8");
//				}
//		});

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_c setup");
		//return;
	}
	
	void setupBt_l4() {
        int[] viewID_bt_l4 = new int[8];
        viewID_bt_l4[0] = R.id.bt_l4_1;
        viewID_bt_l4[1] = R.id.bt_l4_2;
        viewID_bt_l4[2] = R.id.bt_l4_3;
        viewID_bt_l4[3] = R.id.bt_l4_4;
        viewID_bt_l4[4] = R.id.bt_l4_5;
        viewID_bt_l4[5] = R.id.bt_l4_6;
        viewID_bt_l4[6] = R.id.bt_l4_7;
        viewID_bt_l4[7] = R.id.bt_l4_8;

        for (int i = 0; i < 8; i++) {
            bt_l4[i] = (Button) view.findViewById(viewID_bt_l4[i]);
            final String i2 = String.valueOf(i + 1);
            bt_l4[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder(20);
                    sb.append("layer4/clip").append(i2).append("/connect");
                    mCallbackOSCSend.onFrag3OSCSendInt(sb.toString(), 1);
                    Log.d("Bt", "bt_l4");
                }
            });
        }

//	    bt_l4_1 = (Button) view.findViewById(R.id.bt_l4_1);	//not 14.. but 'L' 4
//	    bt_l4_2 = (Button) view.findViewById(R.id.bt_l4_2);
//	    bt_l4_3 = (Button) view.findViewById(R.id.bt_l4_3);
//	    bt_l4_4 = (Button) view.findViewById(R.id.bt_l4_4);
//	    bt_l4_5 = (Button) view.findViewById(R.id.bt_l4_5);
//	    bt_l4_6 = (Button) view.findViewById(R.id.bt_l4_6);
//	    bt_l4_7 = (Button) view.findViewById(R.id.bt_l4_7);
//	    bt_l4_8 = (Button) view.findViewById(R.id.bt_l4_8);
	    
//	    bt_l4_1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip1/connect", 1);
//				Log.d("Bt", "bt_l4_1");
//				}
//		});
//	    bt_l4_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip2/connect", 1);
//				Log.d("Bt", "bt_l4_2");
//				}
//		});
//	    bt_l4_3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip3/connect", 1);
//				Log.d("Bt", "bt_l4_3");
//				}
//		});
//	    bt_l4_4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip4/connect", 1);
//				Log.d("Bt", "bt_l4_4");
//				}
//		});
//	    bt_l4_5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip5/connect", 1);
//				Log.d("Bt", "bt_l4_5");
//				}
//		});
//	    bt_l4_6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip6/connect", 1);
//				Log.d("Bt", "bt_l4_6");
//				}
//		});
//	    bt_l4_7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip7/connect", 1);
//				Log.d("Bt", "bt_l4_7");
//				}
//		});
//	    bt_l4_8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer4/clip8/connect", 1);
//				Log.d("Bt", "bt_l4_8");
//				}
//		});

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_4 setup");
		//return;
	}
	
	void setupBt_l3() {
        int[] viewID_bt_l3 = new int[8];
        viewID_bt_l3[0] = R.id.bt_l3_1;
        viewID_bt_l3[1] = R.id.bt_l3_2;
        viewID_bt_l3[2] = R.id.bt_l3_3;
        viewID_bt_l3[3] = R.id.bt_l3_4;
        viewID_bt_l3[4] = R.id.bt_l3_5;
        viewID_bt_l3[5] = R.id.bt_l3_6;
        viewID_bt_l3[6] = R.id.bt_l3_7;
        viewID_bt_l3[7] = R.id.bt_l3_8;

        for (int i = 0; i < 8; i++) {
            bt_l3[i] = (Button) view.findViewById(viewID_bt_l3[i]);
            final String i2 = String.valueOf(i + 1);
            bt_l3[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder(20);
                    sb.append("layer3/clip").append(i2).append("/connect");
                    mCallbackOSCSend.onFrag3OSCSendInt(sb.toString(), 1);
                    Log.d("Bt", "bt_l3");
                }
            });
        }

//	    bt_l3_1 = (Button) view.findViewById(R.id.bt_l3_1);
//	    bt_l3_2 = (Button) view.findViewById(R.id.bt_l3_2);
//	    bt_l3_3 = (Button) view.findViewById(R.id.bt_l3_3);
//	    bt_l3_4 = (Button) view.findViewById(R.id.bt_l3_4);
//	    bt_l3_5 = (Button) view.findViewById(R.id.bt_l3_5);
//	    bt_l3_6 = (Button) view.findViewById(R.id.bt_l3_6);
//	    bt_l3_7 = (Button) view.findViewById(R.id.bt_l3_7);
//	    bt_l3_8 = (Button) view.findViewById(R.id.bt_l3_8);
	    
//	    bt_l3_1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip1/connect", 1);
//				Log.d("Bt", "bt_l3_1");
//				}
//		});
//	    bt_l3_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip2/connect", 1);
//				Log.d("Bt", "bt_l3_2");
//				}
//		});
//	    bt_l3_3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip3/connect", 1);
//				Log.d("Bt", "bt_l3_3");
//				}
//		});
//	    bt_l3_4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip4/connect", 1);
//				Log.d("Bt", "bt_l3_4");
//				}
//		});
//	    bt_l3_5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip5/connect", 1);
//				Log.d("Bt", "bt_l3_5");
//				}
//		});
//	    bt_l3_6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip6/connect", 1);
//				Log.d("Bt", "bt_l3_6");
//				}
//		});
//	    bt_l3_7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip7/connect", 1);
//				Log.d("Bt", "bt_l3_7");
//				}
//		});
//	    bt_l3_8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer3/clip8/connect", 1);
//				Log.d("Bt", "bt_l3_8");
//				}
//		});
		
	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_3 setup");
		//return;
	}
	
	void setupBt_l2() {
        int[] viewID_bt_l2 = new int[8];
        viewID_bt_l2[0] = R.id.bt_l2_1;
        viewID_bt_l2[1] = R.id.bt_l2_2;
        viewID_bt_l2[2] = R.id.bt_l2_3;
        viewID_bt_l2[3] = R.id.bt_l2_4;
        viewID_bt_l2[4] = R.id.bt_l2_5;
        viewID_bt_l2[5] = R.id.bt_l2_6;
        viewID_bt_l2[6] = R.id.bt_l2_7;
        viewID_bt_l2[7] = R.id.bt_l2_8;

        for (int i = 0; i < 8; i++) {
            bt_l2[i] = (Button) view.findViewById(viewID_bt_l2[i]);
            final String i2 = String.valueOf(i + 1);
            bt_l2[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder(20);
                    sb.append("layer2/clip").append(i2).append("/connect");
                    mCallbackOSCSend.onFrag3OSCSendInt(sb.toString(), 1);
                    Log.d("Bt", "bt_l2");
                }
            });
        }

//	    bt_l2_1 = (Button) view.findViewById(R.id.bt_l2_1);
//	    bt_l2_2 = (Button) view.findViewById(R.id.bt_l2_2);
//	    bt_l2_3 = (Button) view.findViewById(R.id.bt_l2_3);
//	    bt_l2_4 = (Button) view.findViewById(R.id.bt_l2_4);
//	    bt_l2_5 = (Button) view.findViewById(R.id.bt_l2_5);
//	    bt_l2_6 = (Button) view.findViewById(R.id.bt_l2_6);
//	    bt_l2_7 = (Button) view.findViewById(R.id.bt_l2_7);
//	    bt_l2_8 = (Button) view.findViewById(R.id.bt_l2_8);
	    
//	    bt_l2_1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip1/connect", 1);
//				Log.d("Bt", "bt_l2_1");
//				}
//		});
//	    bt_l2_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip2/connect", 1);
//				Log.d("Bt", "bt_l2_2");
//				}
//		});
//	    bt_l2_3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip3/connect", 1);
//				Log.d("Bt", "bt_l2_3");
//				}
//		});
//	    bt_l2_4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip4/connect", 1);
//				Log.d("Bt", "bt_l2_4");
//				}
//		});
//	    bt_l2_5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip5/connect", 1);
//				Log.d("Bt", "bt_l2_5");
//				}
//		});
//	    bt_l2_6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip6/connect", 1);
//				Log.d("Bt", "bt_l2_6");
//				}
//		});
//	    bt_l2_7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip7/connect", 1);
//				Log.d("Bt", "bt_l2_7");
//				}
//		});
//	    bt_l2_8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer2/clip8/connect", 1);
//				Log.d("Bt", "bt_l2_8");
//				}
//		});

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_2 setup");
		//return;
	}
	
	void setupBt_l1() {
        int[] viewID_bt_l1 = new int[8];
        viewID_bt_l1[0] = R.id.bt_l1_1;
        viewID_bt_l1[1] = R.id.bt_l1_2;
        viewID_bt_l1[2] = R.id.bt_l1_3;
        viewID_bt_l1[3] = R.id.bt_l1_4;
        viewID_bt_l1[4] = R.id.bt_l1_5;
        viewID_bt_l1[5] = R.id.bt_l1_6;
        viewID_bt_l1[6] = R.id.bt_l1_7;
        viewID_bt_l1[7] = R.id.bt_l1_8;

        for (int i = 0; i < 8; i++) {
            bt_l1[i] = (Button) view.findViewById(viewID_bt_l1[i]);
            final String i2 = String.valueOf(i + 1);
            bt_l1[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder(20);
                    sb.append("layer1/clip").append(i2).append("/connect");
                    mCallbackOSCSend.onFrag3OSCSendInt(sb.toString(), 1);
                    Log.d("Bt", "bt_l1");
                }
            });
        }

//	    bt_l1_1 = (Button) view.findViewById(R.id.bt_l1_1);
//	    bt_l1_2 = (Button) view.findViewById(R.id.bt_l1_2);
//	    bt_l1_3 = (Button) view.findViewById(R.id.bt_l1_3);
//	    bt_l1_4 = (Button) view.findViewById(R.id.bt_l1_4);
//	    bt_l1_5 = (Button) view.findViewById(R.id.bt_l1_5);
//	    bt_l1_6 = (Button) view.findViewById(R.id.bt_l1_6);
//	    bt_l1_7 = (Button) view.findViewById(R.id.bt_l1_7);
//	    bt_l1_8 = (Button) view.findViewById(R.id.bt_l1_8);

//	    bt_l1_1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip1/connect", 1);
//				Log.d("Bt", "bt_l1_1");
//				}
//		});
//	    bt_l1_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip2/connect", 1);
//				Log.d("Bt", "bt_l1_2");
//				}
//		});
//	    bt_l1_3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip3/connect", 1);
//				Log.d("Bt", "bt_l1_3");
//				}
//		});
//	    bt_l1_4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip4/connect", 1);
//				Log.d("Bt", "bt_l1_4");
//				}
//		});
//	    bt_l1_5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip5/connect", 1);
//				Log.d("Bt", "bt_l1_5");
//				}
//		});
//	    bt_l1_6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip6/connect", 1);
//				Log.d("Bt", "bt_l1_6");
//				}
//		});
//	    bt_l1_7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip7/connect", 1);
//				Log.d("Bt", "bt_l1_7");
//				}
//		});
//	    bt_l1_8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallbackOSCSend.onFrag3OSCSendInt("layer1/clip8/connect", 1);
//				Log.d("Bt", "bt_l1_8");
//				}
//		});

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_1 setup");
        //return;
    }

    @Override
    public void onResume() {
        super.onResume();
		Log.d("HowFar", "Resolume3Fragment | onResume");
		//getSavedState();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d("HowFar", "Resolume3Fragment | onPause");
	}

//	private void getSavedState() {
//		appSettings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
//
//		Log.d("HowFar", "Resolume3Fragment | getSavedState");
//	}

}
