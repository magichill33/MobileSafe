package com.lilo.util;

import java.util.List;

import com.supermap.android.maps.Point2D;

public class GeoUtil {
	/*
	 *  判断点是否在多边形内
	 
	public Boolean isPointInPolygon(Point2D point, List<Point2D> pointArray)
	{
		var isInside:Boolean = false;
		var ESP:Number =  1e-9;
		var count:Number = 0;
		var linePoint1x:Number;
		var linePoint1y:Number;
		var linePoint2x:Number = 180;
		var linePoint2y:Number;
		
		linePoint1x = point.x;
		linePoint1y = point.y;
		linePoint2y = point.y;
		
		if(pointArray != null && pointArray.length >2)
		{
			pointArray.push(pointArray[0]);
		}
		
		for (var i:Number = 0; i < pointArray.length-1; i++) 
		{
			var cx1:Number = (pointArray[i] as MapPoint).x;
			var cy1:Number = (pointArray[i] as MapPoint).y;
			var cx2:Number = (pointArray[i+1] as MapPoint).x;
			var cy2:Number = (pointArray[i+1] as MapPoint).y;
			
			if (isPointOnLine(point.x, point.y, cx1, cy1, cx2, cy2))
			{
				return true;
			}
			
			if (Math.abs(cy2 - cy1) < ESP) 
			{
				continue;
			}
			
			if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) 
			{
				if (cy1 > cy2)
					count++;
			} 
			else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y))
			{
				if (cy2 > cy1)
					count++;
			} 
			else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) 
			{
				count++;
			}
		}
		if (count % 2 == 1)
		{
			isInside = true;
		}
		return isInside;
	}
	
	
	*  判断点是否在多边形内
	
	public function isPointInPolygon2(point:MapPoint, pointArray:Array):Boolean
	{
		var isInside:Boolean = false;
		var ESP:Number =  1e-9;
		var count:Number = 0;
		var linePoint1x:Number;
		var linePoint1y:Number;
		var linePoint2x:Number = 180;
		var linePoint2y:Number;
		
		linePoint1x = point.x;
		linePoint1y = point.y;
		linePoint2y = point.y;
		
		if(pointArray != null && pointArray.length >2)
		{
			pointArray.push(pointArray[0]);
		}
		
		for (var i:Number = 0; i < pointArray.length-1; i++) 
		{
			var cx1:Number = (pointArray[i] as MapPoint).x;
			var cy1:Number = (pointArray[i] as MapPoint).y;
			var cx2:Number = (pointArray[i+1] as MapPoint).x;
			var cy2:Number = (pointArray[i+1] as MapPoint).y;
			
			if (isPointOnLine(point.x, point.y, cx1, cy1, cx2, cy2))
			{
				return true;
			}
			
			if (Math.abs(cy2 - cy1) < ESP) 
			{
				continue;
			}
			
			if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) 
			{
				if (cy1 > cy2)
					count++;
			} 
			else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y))
			{
				if (cy2 > cy1)
					count++;
			} 
			else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) 
			{
				count++;
			}
		}
		if (count % 2 == 1)
		{
			isInside = true;
		}
		return isInside;
	}
	
	public function Multiply(px0:Number, py0:Number, px1:Number, py1:Number, px2:Number, py2:Number):Number
	{
		return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
	}
	
	public function isPointOnLine(px0:Number, py0:Number, px1:Number, py1:Number, px2:Number, py2:Number):Boolean
	{
		var flag:Boolean = false;
		var ESP:Number = 1e-9;
		if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP)
			&& ((px0 - px1) * (px0 - px2) <= 0) && ((py0 - py1) * (py0 - py2) <= 0)) 
		{
			flag = true;
		}
		return flag;
	}
	
	public function isIntersect(px1:Number, py1:Number, px2:Number, py2:Number, px3:Number, py3:Number, px4:Number, py4:Number):Boolean 
	{
		var flag:Boolean = false;
		var d:Number = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
		if (d != 0)
		{
			var r:Number = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
			var s:Number = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1))
			{
				flag = true;
			}
		}
		return flag;
	}*/
}
