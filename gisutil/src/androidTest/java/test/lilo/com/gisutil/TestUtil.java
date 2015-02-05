package test.lilo.com.gisutil;

import android.test.InstrumentationTestCase;

import com.esri.core.geometry.Point;
import com.lilosoft.xtcm.utils.GeometryUtil;

import junit.framework.Assert;

/**
 * Created by Administrator on 2015/2/4.
 */
public class TestUtil extends InstrumentationTestCase{
    public static void testIn(){
        GeometryUtil util = GeometryUtil.newInstance();
        util.createBufferErea("1423015218467,1423015218468","CHECK_NUM",
                "http://192.168.0.110:6080/arcgis/rest/services/xiantao/cm_check/MapServer/0",100);
        Point mp = new Point(113.456207,30.372846);
        String addr = null;
        Assert.assertEquals(util.isInErea(mp,addr),true);
    }
}
