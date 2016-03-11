package com.visutal.vrescontrol;

/**
 * Created by ksp on 04/03/2016.
 *
 * data holder for incoming OSC for fragment 4
 */
public class Res4FragData {
    public final String sliderOSCSend0 = "composition/link";
    public final String sliderOSCSend0b = "/values";
    public final String sliderOSCSend1 = "composition/video/effect";
    public final String sliderOSCSend1b = "/opacity/values";

    // saved to prefs
    public int fxSlidersModeInt = 0;                            // 0=Link 1=Opacity
    public int[] verticalSeekbar_fxProgress_link = new int[8];  // 1-8
    public int[] verticalSeekbar_fxProgress_opacity = new int[8]; // 1-8

    public boolean[] bt_fx_b_flip = new boolean[8];              // 1-8

    // not saved to prefs
    public String fxSliderOSCSendObjPrt1;
    public String fxSliderOSCSendObjPrt2;
}
