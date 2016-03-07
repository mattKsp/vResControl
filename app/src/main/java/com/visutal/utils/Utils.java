package com.visutal.utils;

import java.math.BigDecimal;

import android.util.Log;

public class Utils {
	
	private static final boolean LOCAL_DEBUG = false;
	
	static float OldMinB = 0;
	static float OldMaxB = 1;
	static float NewMinB = 2;
	static float NewMaxB = 500;

	//if (LOCAL_DEBUG) {Log.d("HowFar", "ConvertBpm - (double) " + NewValue3);}
	//convert 0-1 to 2-500
	//convert 2-500 to 0-1

	public static double ConvertBpmIn(double OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmIn2(NewValue);
		
		return NewValue2;
	}
	
	public static double ConvertBpmOut(double OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmOut2(NewValue);
		
		return NewValue2;
	}

	public static double ConvertBpmIn(float OldMin, float OldMax, float NewMin, float NewMax, double OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmIn2B(OldMin, OldMax, NewMin, NewMax, NewValue);
		return NewValue2;
	}
	
	public static double ConvertBpmOut(float OldMin, float OldMax, float NewMin, float NewMax, double OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmOut2B(OldMin, OldMax, NewMin, NewMax, NewValue);
		return NewValue2;
	}
	
	public static double ConvertBpmIn(float OldValue)
	{
		float NewValue = PreCConvertBpmFloat(OldValue);
		double NewValue2 = ConvertBpmIn2(NewValue);
		return NewValue2;
	}
	
	public static float ConvertBpmInReturnFloat(float OldValue)
	{
		float NewValue = PreCConvertBpmFloat(OldValue);
		float NewValue2 = ConvertBpmIn2ReturnFloat(NewValue);
		return NewValue2;
	}
	
	public static double ConvertBpmOut(float OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmOut2(NewValue);
		return NewValue2;
	}
	
	public static double ConvertBpmIn(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue)
	{
		float NewValue = PreCConvertBpmFloat(OldValue);
		double NewValue2 = ConvertBpmIn2B(OldMin, OldMax, NewMin, NewMax, NewValue);
		return NewValue2;
	}
	
	public static double ConvertBpmOut(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue)
	{
		float NewValue = PreConvertBpmDouble(OldValue);
		double NewValue2 = ConvertBpmOut2B(OldMin, OldMax, NewMin, NewMax, NewValue);
		return NewValue2;
	}
		
	private static double ConvertBpmIn2(float OldValue)
	{
		float NewValue1 = ConvertBpm(OldMinB, OldMaxB, NewMinB, NewMaxB, OldValue);
		double NewValue2 = BpmRoundUp(NewValue1);
		return NewValue2;
	}
	private static float ConvertBpmIn2ReturnFloat(float OldValue)
	{
		float NewValue1 = ConvertBpm(OldMinB, OldMaxB, NewMinB, NewMaxB, OldValue);
		double NewValue2 = BpmRoundUp(NewValue1);
		return (float) NewValue2;
	}
	private static double ConvertBpmOut2(float OldValue)
	{
		float NewValue1 = ConvertBpm(NewMinB, NewMaxB, OldMinB, OldMaxB, OldValue);
		double NewValue2 = (double) NewValue1;
		return NewValue2;
	}
	
	private static double ConvertBpmIn2B(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue)
	{
		float NewValue1 = ConvertBpm(OldMin, OldMax, NewMin, NewMax, OldValue);
		double NewValue2 = BpmRoundUp(NewValue1);
		return NewValue2;
	}
	
	private static double ConvertBpmOut2B(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue)
	{
		float NewValue1 = ConvertBpm(OldMin, OldMax, NewMin, NewMax, OldValue);
		double NewValue2 = (double) NewValue1;
		return NewValue2;
	}

	private static float PreConvertBpmDouble(double OldValue)
	{
		double OldValue1 = (OldValue * 10000);
		float OldValue2 = (float) OldValue1;
		return OldValue2;
		
	}
	private static float PreCConvertBpmFloat(float OldValue)
	{
		float OldValue1 = (OldValue * 10000);
		return OldValue1;
		
	}
	
	private static float ConvertBpm(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue2)
		{
			float OldMin1 = (OldMin * 10000);
			float OldMax1 = (OldMax * 10000);
			float NewMin1 = (NewMin * 10000);
			float NewMax1 = (NewMax * 10000);
			float OldRange = (OldMax1 - OldMin1);
			float NewRange = (NewMax1 - NewMin1);
			float NewValue = (((OldValue2 - OldMin1) * NewRange) / OldRange) + NewMin1;
			float NewValue1 = (NewValue / 10000);
			//int NewValue2 = (int) NewValue1;
			return NewValue1;
		}
	
	private static double BpmRoundUp(float NewValue1)
	{
		BigDecimal bd = new BigDecimal(Float.toString(NewValue1));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		double NewValue3 = bd.doubleValue();
		return NewValue3;
	}
	
	public static float ConvertRange(int OldMin, int OldMax, int NewMin, int NewMax, int OldValue)
	{
	    double scale = (double)(NewMax - NewMin) / (OldMax - OldMin);
		return (float)(NewMin + ((OldValue - OldMin) * scale));
	}
	
	public static int ConvertRange(float OldMin, float OldMax, float NewMin, float NewMax, float OldValue)
	{
		double scale = (double)(NewMax - NewMin) / (OldMax - OldMin);
		return (int)(NewMin + ((OldValue - OldMin) * scale));
	}

}











