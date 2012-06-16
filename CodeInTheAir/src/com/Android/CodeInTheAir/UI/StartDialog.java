package com.Android.CodeInTheAir.UI;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.Android.CodeInTheAir.Global.AppContext;

public class StartDialog extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AppContext.initContext(this);
        
        Components.start();
        
        /*String code = "<script language=\"JavaScript\"> function start() { var p = new Object(); p.sampleType = \"event\"; p.period = 1; p.lifeTime = \"timed\"; p.runTime = 10000; accl.add(\"accl.sample\", JSON.stringify(p), \"rcv\", \"abcd\");  } function rcv(e) { log.v(\"fromJS\", e.value.accl.z); } </script>";
        String jsName = "main";
        
        JSONObject jMainObj = new JSONObject();
        try
        {
        	JSONObject jObj = new JSONObject();
	        jObj.put("file", jsName);
	        jObj.put("code", code);
	        
	        JSONArray jArr = new JSONArray();
	        jArr.put(jObj);
	        jMainObj.put("source", jArr);
	        jMainObj.put("mainFile", jsName);
	        jMainObj.put("mainFunc", "start();");	        
        }
        catch (Exception e)
        {
        	
        }
        
        Task task = TaskManager.createTask();
        task.start(jMainObj.toString());*/
    }
    

	
	@Override 
	public void onConfigurationChanged(Configuration newConfig) 
	{ 
		super.onConfigurationChanged(newConfig); 
	}
}