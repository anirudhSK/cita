package com.Android.CodeInTheAir.Events;

import java.util.UUID;

public class TaskUtils 
{
	public static String getUniqueId()
	{
		UUID g = UUID.randomUUID();
		return g.toString();
	}
}
