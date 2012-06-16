package com.Android.CodeInTheAir.Types;

import org.json.JSONArray;

public class TypeUtils 
{
	public static JSONArray encodeJSON(String[] strs)
	{
		JSONArray jStrs = new JSONArray();
		try
		{
			for (int i = 0; i < strs.length; i++)
			{
				jStrs.put(strs[i]);				
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return jStrs;
	}
	
	public static String[] JSONToStringArr(String jsonString)
	{
		String[] strs = new String[0];
		
		try
		{
			JSONArray jArr = new JSONArray(jsonString);
			strs = new String[jArr.length()];
			for (int i = 0; i < jArr.length(); i++)
			{
				strs[i] = jArr.getString(0);
			}
		}
		catch (Exception e)
		{
			
		}		
		return strs;
	}	
}
