package com.lilo.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lilo.layer.GatherPartOverlay;
import com.lilo.model.CommonFacility;
import com.lilo.model.ExtendPart;
import com.lilo.model.HouseLand;
import com.lilo.model.LandScaping;
import com.lilo.model.MessageEnum;
import com.lilo.model.OtherFacility;
import com.lilo.model.Part;
import com.lilo.model.RoadTraffic;
import com.lilo.model.Sanitation;
import com.lilo.service.RunQueryDataTask;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LineOverlay;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Overlay;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;

public class PartUpdateUtil {

    private MapView mapView;
    private Context context;
    private DefaultItemizedOverlay partsOverlay = null;
    private List<Overlay> partsOverlays = new ArrayList<Overlay>(); //部件overlay
    private Overlay partOverlay = null;

    public PartUpdateUtil(MapView mapView, Context context) {
        super();
        this.mapView = mapView;
        this.context = context;
    }

    public void drawPartsOnMap(final String pType,String partsUrl,String regionUrl,String wudu_cmp,
                               String region_grid,String gridCodes)
    {
        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        postPartInfo(queryResult,pType);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        TipForm.newInstance().dismissDialog();
                        Toast.makeText(context,"获取区域信息失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        TipForm.newInstance().dismissDialog();
                        break;
                }
            }
        };

        GatherPartOverlay gatherPartOverlay = new GatherPartOverlay(new DrawUtil().getPolygonPaint(Color.WHITE,0,0),
                context,partsUrl,wudu_cmp + "_" + pType,handler);
        DrawRegionUtil drawRegionUtil = new DrawRegionUtil(mapView, context, gatherPartOverlay);
        drawRegionUtil.clearRegionLayer();
        drawRegionUtil.drawRegionByCodes(regionUrl, region_grid, gridCodes);

        int imgId = context.getResources().getIdentifier("p"+pType, "drawable", "com.lilo.sm");
        Drawable iconPart = context.getResources().getDrawable(imgId);
        iconPart.setAlpha(200);
        partsOverlay = new DefaultItemizedOverlay(iconPart);

        drawPartsByCodes(partsUrl, wudu_cmp, pType, gridCodes);

    }

    protected void drawPartsByCodes(String url,String dataSet,String pType,String partCodes)
    {
        Handler partsHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        drawParts(queryResult);
                        break;
                    case MessageEnum.QUERY_FAILED:
                        TipForm.newInstance().dismissDialog();
                        Toast.makeText(context,"获取区域信息失败", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        TipForm.newInstance().dismissDialog();
                        break;
                }
            }
        };

        String params = DataUtil.getFormatConditon(partCodes);
        TipForm.newInstance().showProgressDialog(context);
        new RunQueryDataTask(url,dataSet + "_" + pType, "BGCODE in (" + params + ")", partsHandler).execute("*");
    }

    /**
     * 绘制部件信息
     * @param result
     */
    protected void drawParts(GetFeaturesResult result)
    {
        if (result == null || result.features == null || result.featureCount == 0) {
            Toast.makeText(context, "查询部件结果为空!", Toast.LENGTH_LONG).show();
            return;
        }

        if(mapView.getOverlays().contains(partsOverlay))
        {
            mapView.getOverlays().remove(partsOverlay);
            if(partsOverlay.size() > 0)
            {
                partsOverlay.clear();
            }
        }
        if(mapView.getOverlays().contains(partsOverlays))
        {
            mapView.getOverlays().remove(partsOverlays);
            if(partsOverlays.size() > 0)
            {
                partsOverlays.clear();
            }
        }

        List<List<Point2D>> pointsLists = new ArrayList<List<Point2D>>();
        Feature[] features = result.features;
        GeometryType type = features[0].geometry.type;

        for (int i = 0; i < features.length; i++) {
            Geometry geometry = features[i].geometry;
            List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
            if(type == GeometryType.POINT)
            {
                //List<Point2D> geoPoints = DataUtil.getPiontsFromGeometry(geometry);
                if (points != null && points.size() > 0) {
                    OverlayItem overlayItem = new OverlayItem(points.get(0), null, null);
                    partsOverlay.addItem(overlayItem);
                }
            }else
            {

                if (geometry.parts.length > 1) {
                    int num = 0;
                    for (int j = 0; j < geometry.parts.length; j++) {
                        int count = geometry.parts[j];
                        List<Point2D> partList = points.subList(num, num + count);
                        pointsLists.add(partList);
                        num = num + count;
                    }
                } else {
                    pointsLists.add(points);
                }

            }

        }

        if(type == GeometryType.LINE)
        {
            // 把所有查询的几何对象都高亮显示
            for (int m = 0; m < pointsLists.size(); m++) {
                List<Point2D> geoPointList = pointsLists.get(m);
                //PolygonOverlay polygonOverlay = new PolygonOverlay(getPolygonPaint(Color.BLUE,180,3));
                LineOverlay part = new LineOverlay(new DrawUtil().getPolygonPaint(0x045ff5, 150,3));
                mapView.getOverlays().add(part);
                partsOverlays.add(part);
                part.setData(geoPointList);
                part.setShowPoints(false);

            }
        }else
        {
            for (int m = 0; m < pointsLists.size(); m++) {
                List<Point2D> geoPointList = pointsLists.get(m);
                PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(0x0000ff,150,1,Paint.Style.FILL));
                //PolygonOverlay polygonOverlay = new TouchPolygonOverlays(getPolygonPaint(0x9696fe,50,1,Paint.Style.FILL));
                mapView.getOverlays().add(polygonOverlay);
                partsOverlays.add(polygonOverlay);
                polygonOverlay.setData(geoPointList);
                polygonOverlay.setShowPoints(false);

            }
        }

        if(partsOverlay.size()>0)
        {
            mapView.getOverlays().add(partsOverlay);
        }

        mapView.invalidate();
    }

    /**
     * 将采集的部件信息提交出去
     * @param result
     */
    protected void postPartInfo(GetFeaturesResult result,String pType)
    {
        if (result == null || result.features == null || result.featureCount == 0) {
            Toast.makeText(context, "查询部件信息为空!", Toast.LENGTH_LONG).show();
            return;
        }

        Feature feature = result.features[0];
        Geometry geometry = feature.geometry;
        List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
        if(partOverlay!=null && mapView.getOverlays().contains(partOverlay))
        {
            mapView.getOverlays().remove(partOverlay);
            if(partOverlay instanceof DefaultItemizedOverlay)
            {
                ((DefaultItemizedOverlay) partOverlay).clear();
            }
        }

        if(geometry.type == GeometryType.POINT)
        {
            int imgId = context.getResources().getIdentifier("p"+pType, "drawable", "com.lilo.sm");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),imgId);
            Drawable da1 = context.getResources().getDrawable(imgId);
            //bitmap.setDensity(da.)
            int width = da1.getIntrinsicWidth();
            int height = da1.getIntrinsicHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(2.0f, 2.0f);
            Bitmap newmp = Bitmap.createBitmap(bitmap,0,0,
                    width,height,matrix,true);
            //newmp.setPixel(0, 0, 0xff0000);
            BitmapDrawable da = new BitmapDrawable(newmp);

            //da.setTargetDensity(metrics)
            //ColorFilter filter = new LightingColorFilter(0x111111, 0xffff00);
            da.setAlpha(244);
            //da.setFilterBitmap(true);
            //da.setColorFilter(filter);
            DefaultItemizedOverlay itemOverlay = new DefaultItemizedOverlay(da);
            Point2D mp = points.get(0);
            OverlayItem item = new OverlayItem(mp,"","");
            itemOverlay.addItem(item);
            mapView.getOverlays().add(itemOverlay);
            partOverlay = itemOverlay;
            //mapController.setCenter(mp);
        }else if(geometry.type == GeometryType.LINE)
        {
            LineOverlay part = new LineOverlay(new DrawUtil().getPolygonPaint(0xff0000, 255,3));
            mapView.getOverlays().add(part);
            partsOverlays.add(part);
            part.setData(points);
            part.setShowPoints(false);
            partOverlay = part;
        }else
        {
            PolygonOverlay polygonOverlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(0xff0000,255,1,Paint.Style.FILL));
            //PolygonOverlay polygonOverlay = new TouchPolygonOverlays(getPolygonPaint(0x9696fe,50,1,Paint.Style.FILL));
            mapView.getOverlays().add(polygonOverlay);
            partsOverlays.add(polygonOverlay);
            polygonOverlay.setData(points);
            polygonOverlay.setShowPoints(false);
            partOverlay = polygonOverlay;
        }

        mapView.invalidate();
        String parentType = pType.substring(0, 2);
        Part part = null;
        switch (Integer.parseInt(parentType)) {
            case 1:
                part = new CommonFacility();
                break;
            case 2:
                part = new RoadTraffic();
                break;
            case 3:
                part = new Sanitation();
                break;
            case 4:
                part = new LandScaping();
                break;
            case 5:
                part = new HouseLand();
                break;
            case 6:
                part = new OtherFacility();
                break;
            default:
                part = new ExtendPart();
                break;
        }

        List<String> fields = part.getFieldNames();
        String[] keys = feature.fieldNames;
        String[] values = feature.fieldValues;
        for(int i = 0;i < keys.length;i++)
        {
            try {
                part.setValue(keys[i], values[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        TipForm.showToast(part.toString(), context.getApplicationContext());
        Log.i("ly", part.toString());
    }
}
