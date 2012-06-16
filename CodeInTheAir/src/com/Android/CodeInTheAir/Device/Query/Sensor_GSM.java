package com.Android.CodeInTheAir.Device.Query;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.Android.CodeInTheAir.Global.AppContext;
import com.Android.CodeInTheAir.Types.CellInfo;

public class Sensor_GSM 
{
	private static TelephonyManager telephonyManager;
	
	static int connectedTowerRSSI = 0;
	static int connectedTowerBER = 0;
	
	public static void init()
	{
		telephonyManager = (TelephonyManager)AppContext.context.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	public static CellInfo getConnectedCell()
	{
		// TODO: Assumes GSM, should be edited for CDMA
		
		CellInfo cellInfo = new CellInfo();
		GsmCellLocation gloc = (GsmCellLocation)telephonyManager.getCellLocation();	
		
		if (gloc != null && gloc.getCid() != -1)
		{
			cellInfo.type = telephonyManager.getNetworkType();
			cellInfo.cid = gloc.getCid();
			cellInfo.lac = gloc.getLac();
			cellInfo.rssi = connectedTowerRSSI;
			cellInfo.ber = connectedTowerBER;
			cellInfo.connected = true;
		}
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);		
		
		return cellInfo;		
	}
	
	public static CellInfo getStrongestCell()
	{
		List<CellInfo> cells = getSortedCells();
		if (cells.size() > 0)
		{
			return cells.get(0);
		}
		else
		{
			return new CellInfo();
		}
	}
	
	public static List<CellInfo> getAroundCells()
	{
		List<CellInfo> cells = getSortedCells();
		return cells;
	}
	
	public static List<CellInfo> getAroundCells(int topN)
	{
		List<CellInfo> cells = getSortedCells();
		List<CellInfo> truncLstCellInfo = new ArrayList<CellInfo>();
		for (int i = 0; i < cells.size(); i++)
		{
			if (topN > 0)
			{
				if (i < topN)
				{
					truncLstCellInfo.add(cells.get(i));
				}
			}
			else
			{
				if (cells.size() - i <= -topN)
				{
					truncLstCellInfo.add(cells.get(i));
				}
			}
		}
		
		
		
		return truncLstCellInfo;
	}
	
	public static boolean isEnabled()
	{
		if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
		{
			return false;
		}
		return true;
	}
	
	
	public static boolean isGSMPhone()
	{
		return (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM);
	}
	
	/* 
	 * 
	 * Private methods 
	 * 
	 * */
	
	private static List<CellInfo> getSortedCells()
	{
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		List<CellInfo> cells = new ArrayList<CellInfo>();
		
		CellInfo cellInfo = new CellInfo();
		GsmCellLocation gloc = (GsmCellLocation)telephonyManager.getCellLocation();

		if (gloc != null && gloc.getCid() != -1)
		{
			cellInfo.type = telephonyManager.getNetworkType();
			cellInfo.cid = gloc.getCid();
			cellInfo.lac = gloc.getLac();
			cellInfo.rssi = connectedTowerRSSI;
			cellInfo.ber = connectedTowerBER;
			cellInfo.connected = true;
			
			cells.add(cellInfo);
		}
		
		List<NeighboringCellInfo> neighbors = telephonyManager.getNeighboringCellInfo();
		
		if (neighbors != null)	
		{
			for (int i = 0; i < neighbors.size(); i++)
			{
				CellInfo nCell = new CellInfo();
				
				if (neighbors.get(i).getCid() != NeighboringCellInfo.UNKNOWN_CID)
				{
					//nCell.cell = (neighbors.get(i).getCid()&0xFFFF)+"";
					//nCell.lac = ((neighbors.get(i).getCid()&0xFFFF0000)>>16)+"";					
					nCell.type = neighbors.get(i).getNetworkType();
					nCell.cid = (neighbors.get(i).getCid()&0xFFFF);
					nCell.lac = neighbors.get(i).getLac();
					nCell.rssi = neighbors.get(i).getRssi();
					nCell.connected = false;
					
					cells.add(nCell);
				}
			}
		}
		
		CellInfo[] arrCellInfo = (CellInfo[])cells.toArray(new CellInfo[cells.size()]);
		arrCellInfo = sortCellInfo(arrCellInfo);
		
		cells = new ArrayList<CellInfo>();
		for (int i = 0; i < arrCellInfo.length; i++)
		{
			cells.add(arrCellInfo[i]);
		}
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		return cells;
	}
	
	private static CellInfo[] sortCellInfo(CellInfo[] cellInfo)
	{
		for (int i = 0; i < cellInfo.length - 1; i++)
		{
			for (int j = i+1; j < cellInfo.length; j++)
			{
				if (cellInfo[i].rssi < cellInfo[j].rssi)
				{
					CellInfo tmpCellInfo = cellInfo[i];
					cellInfo[i] = cellInfo[j];
					cellInfo[j] = tmpCellInfo;
				}
			}
		}
		return cellInfo;
	}
	
	private static PhoneStateListener phoneStateListener = new PhoneStateListener()
	{
		
		public void onSignalStrengthsChanged(SignalStrength ss)
		{
			connectedTowerRSSI = ss.getGsmSignalStrength();
			connectedTowerBER = ss.getGsmBitErrorRate();
		}
		
		public void  onCellLocationChanged  (CellLocation location)
		{
			
		}
	};
	
	
	/* 
	 * 
	 * Listeners and Events 
	 * 
	 * */
	
	/* Cell Listener */
	public static void addCellChangeListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_CELL_LOCATION);
	}
	
	public static void removeCellChangeListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
	}
	
	/* RSSI Listener */
	public static void addRSSIChangeListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	public static void removeRSSIChangeListener(PhoneStateListener psListener)
	{
		telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
	}
}

