package com.Android.CodeInTheAir.JSInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.Android.CodeInTheAir.Device.Actuate.System_Notify;
import com.Android.CodeInTheAir.Events.TaskContext;
import com.Android.CodeInTheAir.Global.AppContext;

public class JS_Phone 
{
	public JS_Phone(String fileName, TaskContext taskContext)
	{
		
	}
	
	public void vibrate()
	{
		System_Notify.vibrate();
	}
	
	public void vibrate(long ms)
	{
		System_Notify.vibrate(ms);
	}
	
	public void notify(String msg)
	{
		AlertDialog ad = new AlertDialog.Builder(AppContext.context).create();
		ad.setCancelable(false);
		ad.setMessage(msg);
		
		ad.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		ad.show();
	}
	
	public void toast(String msg)
	{
		Toast.makeText(AppContext.context, msg, Toast.LENGTH_LONG).show();
	}
}
