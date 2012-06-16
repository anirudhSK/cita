package com.Android.CodeInTheAir.Events;

import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.JSInterface.JS_Accl;
import com.Android.CodeInTheAir.JSInterface.JS_Call;
import com.Android.CodeInTheAir.JSInterface.JS_Callback;
import com.Android.CodeInTheAir.JSInterface.JS_GPS;
import com.Android.CodeInTheAir.JSInterface.JS_GSM;
import com.Android.CodeInTheAir.JSInterface.JS_Loc;
import com.Android.CodeInTheAir.JSInterface.JS_Log_Interface;
import com.Android.CodeInTheAir.JSInterface.JS_NGPS;
import com.Android.CodeInTheAir.JSInterface.JS_PGPS;
import com.Android.CodeInTheAir.JSInterface.JS_Phone;
import com.Android.CodeInTheAir.JSInterface.JS_Wifi;

public class WebViewWrapper 
{
	String code;
	
	WebView webView;	

	JS_GSM gsm;
	JS_Wifi wifi;
	JS_GPS gps;
	JS_NGPS ngps;
	JS_Loc loc;
	JS_PGPS pgps;	
	JS_Accl accl;
	JS_Callback callback;
	JS_Call call;
	
	JS_Phone phone;	

	
	public WebViewWrapper(String fileName, TaskContext taskContext)
	{
		webView = new WebView(AppContext.context);
		
		gsm = new JS_GSM(fileName, taskContext);
		wifi = new JS_Wifi(fileName, taskContext);	
		gps = new JS_GPS(fileName, taskContext);
		ngps = new JS_NGPS(fileName, taskContext);
		loc = new JS_Loc(fileName, taskContext);
		pgps = new JS_PGPS(fileName, taskContext);		
		accl = new JS_Accl(fileName, taskContext);		
		phone = new JS_Phone(fileName, taskContext);		
		callback = new JS_Callback(fileName, taskContext);
		call = new JS_Call(fileName, taskContext);
		
		initWebView();
	}
	
	public void load(String code)
	{
		webView.loadData(code, "text/html", "utf-8");		
	}
	
	public void callFuncJSON(String func, String params)
	{
		String callString = "javascript:" + func + "(JSON.parse('";
		callString = callString + params;	
		callString = callString + "'))";		
		Log.v("CITA:callString", callString);
		webView.loadUrl(callString);
	}
	
	public void callFunc(String func, String params)
	{
		String callString = "javascript:" + func + "(";
		callString = callString + params;
		callString = callString + ")";		
		Log.v("CITA:callString", callString);
		webView.loadUrl(callString);
	}
	
	public void callFunc(String func)
	{
		String callString = "javascript:" + func + "(";
		callString = callString + ")";
		webView.loadUrl(callString);  
	}
	
	private void initWebView()
	{
		webView = new WebView(AppContext.context);		
		webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onJsAlert(final WebView view,
                    final String url, final String message,
                    JsResult result) 
            {
                result.confirm();
                return true;
            }
        });
		
        webView.addJavascriptInterface(gsm, "gsm");
        webView.addJavascriptInterface(wifi, "wifi");
        webView.addJavascriptInterface(gps, "gps");
        webView.addJavascriptInterface(ngps, "ngps");
        webView.addJavascriptInterface(loc, "loc");
        webView.addJavascriptInterface(pgps, "pgps");        
        webView.addJavascriptInterface(accl, "accl");
        webView.addJavascriptInterface(callback, "callback"); 
        webView.addJavascriptInterface(call, "call"); 
        
        webView.addJavascriptInterface(phone, "phone");
	}
	
	
	public void addLogInterface(JS_Log_Interface js_log)
	{
		webView.addJavascriptInterface(js_log, "log");
	}
}
