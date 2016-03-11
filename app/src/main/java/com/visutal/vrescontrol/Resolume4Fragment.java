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
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Resolume4Fragment extends Fragment {

    View view;

    public static Button[] verticalSeekbar_fx_up = new Button[8];                   // up button
    public static VerticalSeekBar[] verticalSeekbar_fx = new VerticalSeekBar[8];    // slider
    public static boolean[] verticalSeekbar_fx_inUse = new boolean[8];              // 1-8
    public static Button[] verticalSeekbar_fx_down = new Button[8];                 // down button
    public static Button[] bt_fx_b = new Button[8];                                 // bypass button
    public static boolean[] skdbu = new boolean[8];

    Frag4OSCSend mCallbackOSCSend;

    // Container Activity must implement this interface
	public interface Frag4OSCSend {
        void onFrag4OSCSendInt(String OSCAddress, int argsOut);
        void onFrag4OSCSendFloat(String OSCAddress, float argsOut);
        //public void onFrag4OSCSendString(String OSCAddress, String argsOut);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	mCallbackOSCSend = (Frag4OSCSend) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Frag4OSCSendInt, Frag4OSCSendFloat");
                    //+ " must implement Frag4OSCSendInt, Frag4OSCSendFloat, Frag4OSCSendString");
        }
    }
	

	public static Resolume4Fragment newInstance(int index) {
		Log.w("HowFar", "Resolume4Fragment | newInstance");
		Resolume4Fragment r4 = new Resolume4Fragment();
		
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		r4.setArguments(args);
		
		return r4;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		Log.d("HowFar", "Resolume4Fragment | onCreateView");

		if (container == null) {
			// Currently in a layout without a container, so no reason to create our view.
			Log.d("HowFar", "Resolume4Fragment - Currently in a layout without a container, so no reason to create our view.");
			return null;
		}
	    view = inflater.inflate(R.layout.fragment_resolume4, container, false);
	    Log.d("HowFar", "Resolume4Fragment | inflated fragment_resolume4");

	    setupVerticalSeekbar_fx_up();   // up buttons
	    setupVerticalSeekbar_fx();      // slider
	    setupVerticalSeekbar_fx_down(); // down buttons
	    setupVerticalSeekbar_fx_b();    // bypass buttons
	    Log.d("HowFar", "Resolume4Fragment | onCreateView | buttons setup");

	    Log.d("HowFar", "Resolume4Fragment | onCreateView setup");
	    return view;
	  }
	
	void setupVerticalSeekbar_fx_up() {
        int[] viewID_verticalSeekbar_fx_up = new int[8];
        viewID_verticalSeekbar_fx_up[0] = R.id.verticalSeekbar_fx1_up;
        viewID_verticalSeekbar_fx_up[1] = R.id.verticalSeekbar_fx2_up;
        viewID_verticalSeekbar_fx_up[2] = R.id.verticalSeekbar_fx3_up;
        viewID_verticalSeekbar_fx_up[3] = R.id.verticalSeekbar_fx4_up;
        viewID_verticalSeekbar_fx_up[4] = R.id.verticalSeekbar_fx5_up;
        viewID_verticalSeekbar_fx_up[5] = R.id.verticalSeekbar_fx6_up;
        viewID_verticalSeekbar_fx_up[6] = R.id.verticalSeekbar_fx7_up;
        viewID_verticalSeekbar_fx_up[7] = R.id.verticalSeekbar_fx8_up;

        for (int i = 0; i < verticalSeekbar_fx_up.length; i++) {
            verticalSeekbar_fx_up[i] = (Button) view.findViewById(viewID_verticalSeekbar_fx_up[i]);
            final String i2 = String.valueOf(i+1);
            verticalSeekbar_fx_up[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sb = CcSet.i().data4.fxSliderOSCSendObjPrt1 + i2 + CcSet.i().data4.fxSliderOSCSendObjPrt2;
                    mCallbackOSCSend.onFrag4OSCSendFloat(sb, 1);
                }
            });
        }

	    Log.d("HowFar", "Resolume4Fragment | onCreateView | setupVerticalSeekbar_fx_up setup");
	}
	
    void setupVerticalSeekbar_fx() {
        int[] viewID_verticalSeekbar_fx = new int[8];
        viewID_verticalSeekbar_fx[0] = R.id.verticalSeekbar_fx1;
        viewID_verticalSeekbar_fx[1] = R.id.verticalSeekbar_fx2;
        viewID_verticalSeekbar_fx[2] = R.id.verticalSeekbar_fx3;
        viewID_verticalSeekbar_fx[3] = R.id.verticalSeekbar_fx4;
        viewID_verticalSeekbar_fx[4] = R.id.verticalSeekbar_fx5;
        viewID_verticalSeekbar_fx[5] = R.id.verticalSeekbar_fx6;
        viewID_verticalSeekbar_fx[6] = R.id.verticalSeekbar_fx7;
        viewID_verticalSeekbar_fx[7] = R.id.verticalSeekbar_fx8;

        for (int i = 0; i < verticalSeekbar_fx.length; i++) {
            verticalSeekbar_fx[i] = (VerticalSeekBar) view.findViewById(viewID_verticalSeekbar_fx[i]);
            final int ii = i;
            final String i2 = String.valueOf(i + 1);

            verticalSeekbar_fx[i].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                   verticalSeekbar_fx_inUse[ii] = false;
                   skdbu[ii] = false;
//				seekBar.setSecondaryProgress(verticalSeekbar_fx1Progress);
//				verticalSeekbar_fx1Progress2 = verticalSeekbar_fx1Progress;
                   Log.d("HowFar", "Resolume4Fragment | verticalSeekbar_fx | onStopTrackingTouch");
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {
                   verticalSeekbar_fx_inUse[ii] = true;
                   if (CcSet.i().data4.fxSlidersModeInt == 0) {
                       if (CcSet.i().data4.verticalSeekbar_fxProgress_link[ii] == 0) {
                           skdbu[ii] = true;
                       }
                   } else {
                       if (CcSet.i().data4.verticalSeekbar_fxProgress_opacity[ii] == 0) {
                           skdbu[ii] = true;
                       }
                   }
//				seekBar.setSecondaryProgress(verticalSeekbar_fx1Progress);
//				verticalSeekbar_fx1Progress2 = verticalSeekbar_fx1Progress;
                   Log.d("HowFar", "Resolume4Fragment | verticalSeekbar_fx | onStartTrackingTouch");
               }

               @Override
               public void onProgressChanged(SeekBar seekBar, int progress1, boolean fromUser) {
                   Log.d("HowFar", "Resolume4Fragment | verticalSeekbar_fx | onProgressChanged");
                   if (verticalSeekbar_fx_inUse[ii]) {
                       if (fromUser) {
                           Log.d("HowFar", "Resolume4Fragment | verticalSeekbar_fx | onProgressChanged | fromUser");
                           float progress1b = Utils.ConvertRange(0, 100, 0, 1, progress1);
                           String sb = CcSet.i().data4.fxSliderOSCSendObjPrt1 + i2 + CcSet.i().data4.fxSliderOSCSendObjPrt2;
                           mCallbackOSCSend.onFrag4OSCSendFloat(sb, progress1b);
                           //if (skdbu1) {seekBar.setSecondaryProgress(progress1);}
                           Log.d("OSC", "Resolume4Fragment composition/link1/values " + Float.toString(progress1b));
                       }
                   }
//                   if (CcSet.i().data4.fxSlidersModeInt == 0) {
//                       CcSet.i().data4.verticalSeekbar_fxProgress_link[ii] = progress1;
//                   } else {
//                       CcSet.i().data4.verticalSeekbar_fxProgress_opacity[ii] = progress1;
//                   }
//				verticalSeekbar_fx1Progress = progress1;
                }
             }
            );
        }
	    Log.d("HowFar", "Resolume4Fragment | onCreateView | setupVerticalSeekbar_fx setup");
		//return;
	}
    
    void setupVerticalSeekbar_fx_down() {
        int[] viewID_verticalSeekbar_fx_down = new int[8];
        viewID_verticalSeekbar_fx_down[0] = R.id.verticalSeekbar_fx1_down;
        viewID_verticalSeekbar_fx_down[1] = R.id.verticalSeekbar_fx2_down;
        viewID_verticalSeekbar_fx_down[2] = R.id.verticalSeekbar_fx3_down;
        viewID_verticalSeekbar_fx_down[3] = R.id.verticalSeekbar_fx4_down;
        viewID_verticalSeekbar_fx_down[4] = R.id.verticalSeekbar_fx5_down;
        viewID_verticalSeekbar_fx_down[5] = R.id.verticalSeekbar_fx6_down;
        viewID_verticalSeekbar_fx_down[6] = R.id.verticalSeekbar_fx7_down;
        viewID_verticalSeekbar_fx_down[7] = R.id.verticalSeekbar_fx8_down;

        for (int i = 0; i < verticalSeekbar_fx_down.length; i++) {
            verticalSeekbar_fx_down[i] = (Button) view.findViewById(viewID_verticalSeekbar_fx_down[i]);
            final String i2 = String.valueOf(i+1);
            verticalSeekbar_fx_down[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sb = CcSet.i().data4.fxSliderOSCSendObjPrt1 + i2 + CcSet.i().data4.fxSliderOSCSendObjPrt2;
                    mCallbackOSCSend.onFrag4OSCSendFloat(sb, 0);
                }
            });
        }

	    Log.d("HowFar", "Resolume4Fragment | onCreateView | setupVerticalSeekbar_fx_down setup");
	}
	
	void setupVerticalSeekbar_fx_b() {
        int[] viewID_bt_fx_b = new int[8];
        viewID_bt_fx_b[0] = R.id.bt_fx1_b;
        viewID_bt_fx_b[1] = R.id.bt_fx2_b;
        viewID_bt_fx_b[2] = R.id.bt_fx3_b;
        viewID_bt_fx_b[3] = R.id.bt_fx4_b;
        viewID_bt_fx_b[4] = R.id.bt_fx5_b;
        viewID_bt_fx_b[5] = R.id.bt_fx6_b;
        viewID_bt_fx_b[6] = R.id.bt_fx7_b;
        viewID_bt_fx_b[7] = R.id.bt_fx8_b;

        for (int i = 0; i < bt_fx_b.length; i++) {
            bt_fx_b[i] = (Button) view.findViewById(viewID_bt_fx_b[i]);
            final int ii = i;
            final String i2 = String.valueOf(i + 1);
            bt_fx_b[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder OSCAddress = new StringBuilder(34);
                    OSCAddress.append("composition/video/effect").append(i2).append("/bypassed");
                    if (CcSet.i().data4.bt_fx_b_flip[ii]) {
                        mCallbackOSCSend.onFrag4OSCSendInt(OSCAddress.toString(), 0);
                    } else {
                        mCallbackOSCSend.onFrag4OSCSendInt(OSCAddress.toString(), 1);
                    }
                }
            });
        }

	    Log.d("HowFar", "Resolume4Fragment | onCreateView | setupVerticalSeekbar_fx_b setup");
	}

	@Override
    public void onResume() {
        super.onResume();
		Log.d("HowFar", "Resolume4Fragment | onResume");
		getSavedState();
		setSlidersMode();
		setState();
		Log.d("Stats", "Resolume4Fragment | onResume fxSlidersModeInt = " + CcSet.i().data4.fxSlidersModeInt);
	}

	@Override
	public void onPause() {
		super.onPause();
		setSavedState();
		Log.d("HowFar", "Resolume4Fragment | onPause");
	}

    public static void updateOSCverticalSeekbarFx(final int pos, final int arg) {
        if (!verticalSeekbar_fx_inUse[pos]) {
            Log.d("HowFar", "Resolume4Fragment | updateOSCverticalSeekbarFx !verticalSeekbar_fx_inUse - " + pos);
            verticalSeekbar_fx[pos].post(new Runnable() {
                public void run() {
                    verticalSeekbar_fx[pos].setProgressAndThumbIn(arg);
                    verticalSeekbar_fx[pos].setSecondaryProgress(arg);
                    Log.d("HowFar", "Resolume4Fragment | updateOSCverticalSeekbarFx | post | run - " + pos);
                }
            });
        }
    }

    public static void updataOSCbt_fx_b(final int pos) {
        bt_fx_b[pos].post(new Runnable() {
            public void run() {
                if (CcSet.i().data4.bt_fx_b_flip[pos]) {
                    bt_fx_b[pos].setBackgroundResource(CcSet.i().orange_on);
                } else {
                    bt_fx_b[pos].setBackgroundResource(CcSet.i().orange_off);
                }
            }
        });
        Log.d("HowFar", "updateOSCInt bt_fx_b In");
    }

	private void getSavedState() {
        CcSet.i().loadData4(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
		Log.d("HowFar", "Resolume4Fragment | getSavedState");
	}
	
	void setSlidersMode() {
		// 0=Link 1=Opacity
		if (CcSet.i().data4.fxSlidersModeInt == 0) {
            CcSet.i().data4.fxSliderOSCSendObjPrt1 = CcSet.i().data4.sliderOSCSend0;
            CcSet.i().data4.fxSliderOSCSendObjPrt2 = CcSet.i().data4.sliderOSCSend0b;
	    } else {
            CcSet.i().data4.fxSliderOSCSendObjPrt1 = CcSet.i().data4.sliderOSCSend1;
            CcSet.i().data4.fxSliderOSCSendObjPrt2 = CcSet.i().data4.sliderOSCSend1b;
	    }
	}
	
	private void setState() {
        for (int i = 0; i < 8; i++) {
            if (CcSet.i().data4.bt_fx_b_flip[i]) {
                bt_fx_b[i].setBackgroundResource(CcSet.i().orange_on);
            } else {
                bt_fx_b[i].setBackgroundResource(CcSet.i().orange_off);
            }
        }

	    //set vars for verticalSeekbar after setting up the button listeners, otherwise it returns a NULL pointer on resuming..
        for (int i = 0; i < 8; i++) {
            StringBuilder sb = new StringBuilder(19);
            sb.append("verticalSeekbar_fx").append(i);
            verticalSeekbar_fx[i].setMax(100);
        }

        if (CcSet.i().data4.fxSlidersModeInt == 0) {
            for (int i = 0; i < 8; i++) {
                verticalSeekbar_fx[i].setSecondaryProgress(CcSet.i().data4.verticalSeekbar_fxProgress_link[i]);
                verticalSeekbar_fx[i].setProgressAndThumb(CcSet.i().data4.verticalSeekbar_fxProgress_link[i]);
            }
	    } else {
            for (int i = 0; i < 8; i++) {
                verticalSeekbar_fx[i].setSecondaryProgress(CcSet.i().data4.verticalSeekbar_fxProgress_opacity[i]);
                verticalSeekbar_fx[i].setProgressAndThumb(CcSet.i().data4.verticalSeekbar_fxProgress_opacity[i]);
            }
	    }

	    Log.d("HowFar", "Resolume4Fragment | setState");
	}
	
	private void setSavedState() {
        CcSet.i().saveData4(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));   // this may be redundant here
		Log.d("HowFar", "Resolume4Fragment | setSavedState");
	}
	
}