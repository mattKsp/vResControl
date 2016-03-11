package com.visutal.vrescontrol;

/**
 * Created by ksp on 04/03/2016.
 *
 * data holder for incoming OSC for fragment 1
 */
public class Res1FragData {
    // saved to prefs
    public int prevLayer = 0;
    public int currentLayer = 0;
    public int prevBeatloop = 0;
    public int currentBeatloop = 0;
    public int progress_opacityandvolumeInt = 100;

    public boolean bt_comp_flip = false;
    public boolean bt_paused_flip = false;
    public boolean bt_bypassed_flip = false;
    public boolean confirmX = false;
    public boolean[] bt_layer_flip = new boolean[4];    // 1-4
    public boolean[] bt_beatloop_flip = new boolean[7]; // 1-7

    // not saved to prefs
    public String compDirOSCadrPrt = "composition";		// gets swapped about in swapBtLayer
    public String editText_bpm_String = "0";
    public double bpmInDouble = 120.00;
    public float bpmFloat = 12000;		// times 1000 so avoid double problem  -  need to fix this!!!
    public double bpmOutDouble;
}
