package com.lilo.util;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lilo.layer.CaseReportOverlay;
import com.lilo.layer.GatherHouseOverlay;
import com.lilo.model.MessageEnum;
import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.R;
import com.lilo.widget.TipForm;
import com.supermap.android.data.GetFeaturesResult;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.PolygonOverlay;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;

public class HouseGatherUtil {

    private MapView mapView;
    private Context context;
    private PolygonOverlay buildOverlay = null; //楼栋图层

    public HouseGatherUtil(MapView mapView, Context context) {
        super();
        this.mapView = mapView;
        this.context = context;
    }

    public  void initHouseGather(String url,String region_grid,String sm_house,String gridCodes,
                                 String houseCodes)
    {
        Handler touchHouseHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageEnum.QUERY_SUCCESS:
                        TipForm.newInstance().dismissDialog();
                        GetFeaturesResult queryResult = (GetFeaturesResult) msg.obj;
                        drawSelectedHouse(queryResult);
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
        GatherHouseOverlay gatherHouseOverlay = new GatherHouseOverlay(
                new DrawUtil().getPolygonPaint(Color.WHITE,0,0), context,
                url,sm_house,touchHouseHandler);
        DrawRegionUtil drawRegionUtil = new DrawRegionUtil(mapView, context, gatherHouseOverlay);
        drawRegionUtil.clearRegionLayer();
        drawRegionUtil.drawRegionByCodes(url, region_grid, gridCodes);
        DrawHouseUtil drawHouseUtil = new DrawHouseUtil(mapView, context);
        drawHouseUtil.clearHouselayer();
        drawHouseUtil.drawHouseByGridCodes(url,sm_house, gridCodes);
        drawHouseUtil.drawHouseByHouseCodes(url,sm_house, houseCodes);

    }

    /**
     * 根据房屋编码定位
     * @param url
     * @param sm_house
     * @param houseCode
     */
    public void drawHouseByCode(String url,String sm_house,String houseCode)
    {
        DrawHouseUtil drawHouseUtil = new DrawHouseUtil(mapView, context);
        drawHouseUtil.drawHouseByCode(url, sm_house, houseCode);
    }

    /**
     * 绘制选中的房屋
     * @param queryResult
     */
    protected void drawSelectedHouse(GetFeaturesResult houseResult) {
        if (houseResult == null || houseResult.features == null || houseResult.featureCount == 0) {
            TipForm.showToast("查询结果为空!", context);
            return;
        }

        Feature feature = houseResult.features[0];
        Geometry geometry = feature.geometry;
        List<Point2D> points = DataUtil.getPiontsFromGeometry(geometry);
        // 把所有查询的几何对象都高亮显示

        if(buildOverlay!=null && mapView.getOverlays().contains(buildOverlay))
        {
            mapView.getOverlays().remove(buildOverlay);
            buildOverlay.destroy();
        }

        PolygonOverlay overlay = new PolygonOverlay(new DrawUtil().getPolygonPaint(0xff0000,150,1,Paint.Style.FILL));
        mapView.getOverlays().add(overlay);
        overlay.setData(points);
        overlay.setShowPoints(false);
        mapView.invalidate();
        buildOverlay = overlay;

        String houseCode = "";
        String gridCode = "";
        int k = 0;
        for(int i = 0;i<feature.fieldNames.length&&k<=2;i++)
        {
            if(feature.fieldNames[i].equals(context.getResources().getString(R.string.houseCode)))
            {
                houseCode = feature.fieldValues[i];
                k++;
            }

            if(feature.fieldNames[i].equals("WGBM"))
            {
                gridCode = feature.fieldValues[i];
                k++;
            }
        }

        TipForm.showToast("房屋编码：" + houseCode + "，网格编码：" + gridCode,context);
    }
}
