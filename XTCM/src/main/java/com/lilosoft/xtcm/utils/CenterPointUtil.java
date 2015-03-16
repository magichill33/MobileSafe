package com.lilosoft.xtcm.utils;

import java.util.ArrayList;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;

/**
 * @category 地图网格绘制
 * @author William Liu
 *
 */
public class CenterPointUtil {
	
	/**
	 * @category 取得中心点
	 * @param enveList
	 * @return
	 */
	public static Point calcCenterPoint(ArrayList<Envelope> enveList)
	{
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		for(Envelope enve:enveList)
		{
			if(maxX < enve.getXMax())
			{
				maxX = enve.getXMax();
			}
			
			if(minX > enve.getXMin())
			{
				minX = enve.getXMin();
			}
			
			if(maxY < enve.getYMax())
			{
				maxY = enve.getYMax();
			}
			
			if(minY > enve.getYMin())
			{
				minY = enve.getYMin();
			}
			
		}
		
		double Xcenter = (maxX + minX)/2;
		double Ycenter = (maxY + minY)/2;
		Point point = new Point(Xcenter, Ycenter);
		return point;
	}
	
	public static Envelope getEnvelope(ArrayList<Envelope> enveList)
	{
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		for(Envelope enve:enveList)
		{
			if(maxX < enve.getXMax())
			{
				maxX = enve.getXMax();
			}
			
			if(minX > enve.getXMin())
			{
				minX = enve.getXMin();
			}
			
			if(maxY < enve.getYMax())
			{
				maxY = enve.getYMax();
			}
			
			if(minY > enve.getYMin())
			{
				minY = enve.getYMin();
			}
			
		}
		return new Envelope(minX,minY,maxX,maxY);
	}
}

