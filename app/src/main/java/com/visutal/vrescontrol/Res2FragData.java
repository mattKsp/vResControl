package com.visutal.vrescontrol;

/**
 * Created by ksp on 04/03/2016.
 *
 * data holder for incoming OSC for fragment 2
 */
public class Res2FragData {
    public final String sliderOSCSend0 = "audio/volume/values";		// (Float 0.0 - 1.0)
    public final String sliderOSCSend1 = "video/opacityandvolume";	// (Float 0.0 - 1.0)
    public final String sliderOSCSend2 = "video/opacity/values";		// (Float 0.0 - 1.0)

    // saved to prefs
    public int prevLayerSlidersMode = 2;                            //this is so dont needlessly double select stuff
    public int layerSlidersModeInt = 2;                             // 0=Audio 1=AV 2=Video
    public int[] verticalSeekbarProgress_audio = new int[4];        // 1-4
    public int[] verticalSeekbarProgress_av = new int[4];           // 1-4
    public int[] verticalSeekbarProgress_video = new int[4];        // 1-4
    public int[] bt_ab_state = new int[4];                          // 1-4
    public int seekBar5Prog = 50;

    public boolean[] bt_b_flip = new boolean[4];        // 1-4
    public boolean[] bt_s_flip = new boolean[4];        // 1-4

    // not saved to prefs
}
