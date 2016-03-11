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

    View view;

	private static Button[] bt_c = new Button[8];   // trigger column bt
    private static Button[] bt_l4 = new Button[8];  // layer 4 bt
    private static Button[] bt_l3 = new Button[8];  // layer 3 bt
    private static Button[] bt_l2 = new Button[8];  // layer 2 bt
    private static Button[] bt_l1 = new Button[8];  // layer 1 bt
	
	Frag3OSCSend mCallbackOSCSend;

    // Container Activity must implement this interface
	public interface Frag3OSCSend {
        void onFrag3OSCSendInt(String OSCAddress, int argsOut);
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
                    //StringBuilder sb = new StringBuilder(14);
                    //sb.append("track").append(i2).append("/connect");
                    String sb = "track" + i2 + "/connect";
                    mCallbackOSCSend.onFrag3OSCSendInt(sb, 1);
                    Log.d("Bt", "bt_c");
                }
            });
        }

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_c setup");
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
                    String sb = "layer4/clip" + i2 + "/connect";
                    mCallbackOSCSend.onFrag3OSCSendInt(sb, 1);
                    Log.d("Bt", "bt_l4");
                }
            });
        }

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_4 setup");
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
                    String sb = "layer3/clip" + i2 + "/connect";
                    mCallbackOSCSend.onFrag3OSCSendInt(sb, 1);
                    Log.d("Bt", "bt_l3");
                }
            });
        }

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_3 setup");
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
                    String sb = "layer2/clip" + i2 + "/connect";
                    mCallbackOSCSend.onFrag3OSCSendInt(sb, 1);
                    Log.d("Bt", "bt_l2");
                }
            });
        }

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_2 setup");
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
                    String sb = "layer1/clip" + i2 + "/connect";
                    mCallbackOSCSend.onFrag3OSCSendInt(sb, 1);
                    Log.d("Bt", "bt_l1");
                }
            });
        }

	    Log.d("Bt", "Resolume3Fragment | onCreateView | setupBt_1 setup");
    }

    @Override
    public void onResume() {
        super.onResume();
		Log.d("HowFar", "Resolume3Fragment | onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d("HowFar", "Resolume3Fragment | onPause");
	}

}
