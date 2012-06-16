package com.Android.CodeInTheAir.Global;

import android.content.Context;

public class AppContext {

	public static Context context;
	public static String DeviceName = "nexus";
	
	public static void initContext(Context context)
	{
		AppContext.context = context;
	}
	
	
}
