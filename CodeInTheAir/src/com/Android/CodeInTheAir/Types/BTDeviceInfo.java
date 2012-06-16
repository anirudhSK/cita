package com.Android.CodeInTheAir.Types;

import org.json.JSONObject;

public class BTDeviceInfo 
{
	public String name;
	public String address;
	public int bondState;
	
	public BTDeviceInfo()
	{
		
	}
	
	public BTDeviceInfo(String name, String address, int bondState)
	{
		this.name = name;
		this.address = address;
		this.bondState = bondState;
	}
	
	public JSONObject encodeJSON()
	{
		JSONObject jBT = null;
		try
		{
			jBT = new JSONObject();
			jBT.put("name", name);
			jBT.put("address", address);
			jBT.put("bondState", bondState);
		}	
		catch (Exception e)
		{
			return null;
		}
		return jBT;
	}
}
