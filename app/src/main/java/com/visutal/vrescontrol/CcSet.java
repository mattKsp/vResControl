package com.visutal.vrescontrol;

import android.content.SharedPreferences;
import static com.visutal.vrescontrol.R.drawable.ic_bt_left;
import static com.visutal.vrescontrol.R.drawable.ic_bt_right;
import static com.visutal.vrescontrol.R.drawable.ic_bt_green_nostroke;
import static com.visutal.vrescontrol.R.drawable.shape_bt1_nostroke;
import static com.visutal.vrescontrol.R.drawable.shape_bt1_orange;
import static com.visutal.vrescontrol.R.drawable.shape_bt1b_blue_nostroke;
import static com.visutal.vrescontrol.R.drawable.shape_bt1b_orange;
import static com.visutal.vrescontrol.R.drawable.shape_bt1c_orange;

/**
 * Created by ksp on 04/03/2016.
 * Control Class Settings
 * global class to hold all ins/outs from osc
 *
 * ..started off as extending Application, but cannot needed Static access, so changed to singleton/instance
 *
 * do not have a fail-safe way of getting the main activity wotnot and getting prefs out of it..
 * not actually sure i am getting the same copy,
 * so passing from activity/fragments for now
 *
 *
 * protected - can be accessed by classes which are using this class
 * static - only 1 copy (final has this one covered most times). associated with the class as a whole, rather than with any object of the class.
 * final - cos there can only be one (and it can't be changed)
 *
 */

public class CcSet {

    private static CcSet ourInstance = new CcSet();
    public static CcSet i() { return ourInstance; }

    public final int blue_nostroke_left = ic_bt_left;
    public final int blue_nostroke_right = ic_bt_right;
    public final int blue_off = shape_bt1_nostroke;
    public final int blue_on = shape_bt1b_blue_nostroke;
    public final int orange_off = shape_bt1_orange;
    public final int orange_on = shape_bt1b_orange;
    public final int orange_c = shape_bt1c_orange;

    public Res1FragData data1 = new Res1FragData();
    public Res2FragData data2 = new Res2FragData();
    // we do not store data for fragment 3, only send it..
    public Res4FragData data4 = new Res4FragData();


    public void saveAll(SharedPreferences sp) {
        // save all stats to prefs that need saving
        saveData1(sp);
        saveData2(sp);
        saveData4(sp);
    }
    public void saveData1(SharedPreferences sp) {
        SharedPreferences.Editor ed = sp.edit();

        ed.putInt("prevLayer", data1.prevLayer);
        ed.putInt("currentLayer", data1.currentLayer);
        ed.putInt("prevBeatloop", data1.prevBeatloop);
        ed.putInt("currentBeatloop", data1.currentBeatloop);
        ed.putInt("progress_opacityandvolumeInt", data1.progress_opacityandvolumeInt);

        ed.putBoolean("bt_comp_flip", data1.bt_comp_flip);
        ed.putBoolean("bt_paused_flip", data1.bt_paused_flip);
        ed.putBoolean("bt_bypassed_flip", data1.bt_bypassed_flip);
        ed.putBoolean("confirmX", data1.confirmX);

        for (int i = 0; i < 4; i++) {
            String sb = "bt_layer_flip" + i;
            ed.putBoolean(sb, data1.bt_layer_flip[i]);
        }
        for (int i = 0; i < 7; i++) {
            String sb = "bt_beatloop_flip" + i;
            ed.putBoolean(sb, data1.bt_beatloop_flip[i]);
        }

        ed.commit();
    }
    public void saveData2(SharedPreferences sp) {
        SharedPreferences.Editor ed = sp.edit();

        ed.putInt("prevLayerSlidersMode", data2.prevLayerSlidersMode);
        ed.putInt("layerSlidersModeInt", data2.layerSlidersModeInt);
        ed.putInt("seekBar5Prog", data2.seekBar5Prog);

        //ed.putBoolean("bt_seekBar_a_state", data2.bt_seekBar_a_state);
        //ed.putBoolean("bt_seekBar_b_state", data2.bt_seekBar_b_state);

        for (int i = 0; i < 4; i++) {
            String sb0 = "verticalSeekbarProgress_audio" + i;
            ed.putInt(sb0, data2.verticalSeekbarProgress_audio[i]);

            String sb1 = "verticalSeekbarProgress_av" + i;
            ed.putInt(sb1, data2.verticalSeekbarProgress_av[i]);

            String sb2 = "verticalSeekbarProgress_video" + i;
            ed.putInt(sb2, data2.verticalSeekbarProgress_video[i]);

            String sb5 = "bt_ab_state" + i;
            ed.putInt(sb5, data2.bt_ab_state[i]);

            String sb3 = "bt_b_flip" + i;
            ed.putBoolean(sb3 , data2.bt_b_flip[i]);

            String sb4 = "bt_s_flip" + i;
            ed.putBoolean(sb4, data2.bt_s_flip[i]);
        }

        ed.commit();
    }
    public void saveData4(SharedPreferences sp) {
        SharedPreferences.Editor ed = sp.edit();

        ed.putInt("fxSlidersModeInt", data4.fxSlidersModeInt);

        for (int i = 0; i < 8; i++) {
            String sb0 = "verticalSeekbar_fxProgress_link" + i;
            ed.putInt(sb0, data4.verticalSeekbar_fxProgress_link[i]);

            String sb1 = "verticalSeekbar_fxProgress_opacity" + i;
            ed.putInt(sb1, data4.verticalSeekbar_fxProgress_opacity[i]);

            String sb2 = "bt_fx_b_flip" + i;
            ed.putBoolean(sb2, data4.bt_fx_b_flip[i]);
        }

        ed.commit();
    }

    public void loadAll(SharedPreferences sp) {
        // load all stats from prefs and set vals

        loadData1(sp);
        loadData2(sp);
        loadData4(sp);
    }
    public void loadData1(SharedPreferences sp) {
        // create temp data
        Res1FragData t1 = new Res1FragData();

        // load and set vars from prefs
        t1.prevLayer = sp.getInt("prevLayer", 0);
        t1.currentLayer = sp.getInt("currentLayer", 0);
        t1.prevBeatloop = sp.getInt("prevBeatloop", 0);
        t1.currentBeatloop = sp.getInt("currentBeatloop", 0);
        t1.progress_opacityandvolumeInt = sp.getInt("progress_opacityandvolumeInt", 100);

        t1.bt_comp_flip = sp.getBoolean("bt_comp_flip", false);
        t1.bt_paused_flip = sp.getBoolean("bt_paused_flip", false);
        t1.bt_bypassed_flip = sp.getBoolean("bt_bypassed_flip", false);
        t1.confirmX = sp.getBoolean("confirmX", false);

        for (int i = 0; i < 4; i++) {
            String sb = "bt_layer_flip" + i;
            t1.bt_layer_flip[i] = sp.getBoolean(sb, false);
        }
        for (int i = 0; i < 7; i++) {
            String sb = "bt_beatloop_flip" + i;
            t1.bt_beatloop_flip[i] = sp.getBoolean(sb, false);
        }

        // copy temp to actual
        data1 = t1;

    }
    public void loadData2(SharedPreferences sp) {
        Res2FragData t2 = new Res2FragData();

        t2.prevLayerSlidersMode = sp.getInt("prevLayerSlidersMode", 2);
        t2.layerSlidersModeInt = sp.getInt("layerSlidersModeInt", 2);
        t2.seekBar5Prog = sp.getInt("seekBar5Prog", 50);

        //t2.bt_seekBar_a_state = sp.getBoolean("bt_seekBar_a_state", false);
        //t2.bt_seekBar_b_state = sp.getBoolean("bt_seekBar_b_state", false);

        for (int i = 0; i < 4; i++) {
            String sb0 = "verticalSeekbarProgress_audio" + i;
            t2.verticalSeekbarProgress_audio[i] = sp.getInt(sb0, 0);

            String sb1 = "verticalSeekbarProgress_av" + i;
            t2.verticalSeekbarProgress_av[i] = sp.getInt(sb1, 0);

            String sb2 = "verticalSeekbarProgress_video" + i;
            t2.verticalSeekbarProgress_video[i] = sp.getInt(sb2, 0);

            String sb5 = "bt_ab_state" + i;
            t2.bt_ab_state[i] = sp.getInt(sb5, 0);

            String sb3 = "bt_b_flip" + i;
            t2.bt_b_flip[i] = sp.getBoolean(sb3, false);

            String sb4 = "bt_s_flip" + i;
            t2.bt_s_flip[i] = sp.getBoolean(sb4, false);
        }

        data2 = t2;
    }
    public void loadData4(SharedPreferences sp) {
        Res4FragData t4 = new Res4FragData();

        t4.fxSlidersModeInt = sp.getInt("fxSlidersModeInt", 0);

        for (int i = 0; i < 8; i++) {
            String sb0 = "verticalSeekbar_fxProgress_link" + i;
            t4.verticalSeekbar_fxProgress_link[i] = sp.getInt(sb0, 0);

            String sb1 = "verticalSeekbar_fxProgress_opacity" + i;
            t4.verticalSeekbar_fxProgress_opacity[i] = sp.getInt(sb1, 0);

            String sb2 = "bt_fx_b_flip" + i;
            t4.bt_fx_b_flip[i] = sp.getBoolean(sb2, false);
        }

        data4 = t4;
    }

    public void resetAll(SharedPreferences sp) {
        // see Settings clearSavedStates ..until all moved
    }
}
