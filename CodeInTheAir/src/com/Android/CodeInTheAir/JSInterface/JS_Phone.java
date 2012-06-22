package com.Android.CodeInTheAir.JSInterface;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
	
	public void ring(){
			
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(AppContext.context, notification);
			r.play();

	}
	public void notify(String msg)
	{
		AlertDialog ad = new AlertDialog.Builder(AppContext.context).create();
		ad.setCancelable(false);
		ad.setTitle("Code in the air");
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
	
	public void openMytracks(){
		PackageManager pm = AppContext.context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage("com.google.android.maps.mytracks");
		if(intent != null){
			AppContext.context.startActivity(intent);

		}
	}
	public void openMusic(){
		PackageManager pm = AppContext.context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage("com.google.android.music");
		if(intent != null){
			AppContext.context.startActivity(intent);

		}
	}
}
