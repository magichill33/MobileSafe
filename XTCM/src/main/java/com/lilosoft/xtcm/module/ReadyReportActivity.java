package com.lilosoft.xtcm.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.LayoutStructure;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.constant.TypeContent;
import com.lilosoft.xtcm.database.DatabaseFactory;
import com.lilosoft.xtcm.instantiation.EventKings;
import com.lilosoft.xtcm.instantiation.ReadyReportBean;

/**
 * @category 待上报
 * @author William Liu
 *
 */
public class ReadyReportActivity extends TabBaseActivity {

    public static ReadyReportBean readyReportBean = null;
    public static boolean operation = false;
    public static List<EventKings> eventList = null;
    private ListView listView;
    private List<? extends Map<String, String>> data;

    @Override
    protected void initListView() {
        // TODO Auto-generated method stub

        readyReportBean = null;

        DatabaseFactory databaseFactory = new DatabaseFactory(mContext);
        List<ReadyReportBean> list = databaseFactory.check();
        data = getListData(list);
        databaseFactory.close();

        SimpleAdapter adapter = new SimpleAdapter(mContext, data,
                R.layout.view_ready_question_item, LayoutStructure.from,
                new int[] { R.id.rq_id, R.id.rq_type_id, R.id.rq_type2 });
        listView.setAdapter(adapter);

    }

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_ready_report);
        listView = (ListView) findViewById(R.id.ready_report_list);

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        listView.setOnItemClickListener(new OnItemClickListener() {

            @SuppressLint("UseValueOf")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String id = data.get(arg2).get(LayoutStructure.from[0]);
                DatabaseFactory databaseFactory = new DatabaseFactory(mContext);
                readyReportBean = databaseFactory.check(id).get(0);
                databaseFactory.close();

                HomeBaseActivity.tabHost.setCurrentTabByTag(Config.B_TAB);
                HomeBaseActivity.tabBt2.setChecked(true);

                operation = true;

            }
        });
    }

    private List<? extends Map<String, String>> getListData(
            List<ReadyReportBean> list) {

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        Map<String, String> map = null;
        ReadyReportBean bean = null;
        for (int i = list.size(); i > 0; i--) {
            map = new HashMap<String, String>();
            bean = list.get(i - 1);

            String typeText = "";
            String type1="";
            String type2="";
            String type3="";
            map.put(LayoutStructure.from[0], bean.getQ_QUESTION_ID());
            map.put(LayoutStructure.from[1], bean.getQ_QUESTION_TYPE2());
            String questionid=bean.getQ_QUESTION_TYPE();
            String questionid1=bean.getQ_QUESTION_TYPE1();
            String questionid2=bean.getQ_QUESTION_TYPE2();
            DatabaseFactory database = new DatabaseFactory(this);
            eventList = database.getEventSpinnerList(TableStructure.V_ACT_ADMINI_EVENT_TYPE);// 获取自动更新事件分类信息
            for (int j = 0; j < eventList.size(); j++) {
                EventKings ek = eventList.get(j);
                String ids= ek.getId();
                if (questionid.equals(ids)) {
                    type1=ek.getMc();
                }
                if (questionid1.equals(ids)) {
                    type2=ek.getMc();
                }
                if (questionid2.equals(ids)) {
                    type3=ek.getMc();
                }
            }
            typeText=type1+"-"+type2+"-"+type3;
			
			/*String[][][] typeNum = TypeContent.TYPE2_ID;
			for (int j = 0; j < typeNum.length; j++) {
				String[][] typeNum1 = typeNum[j];
				for (int k = 0; k < typeNum1.length; k++) {
					String[] typeNum2 = typeNum[j][k];
					for (int l = 0; l < typeNum2.length; l++) {
						if (bean.getQ_QUESTION_TYPE2().equals(typeNum2[l])) {
							typeText = TypeContent.TYPE_TEXT[j] + "-"
									+ TypeContent.TYPE1_TEXT[j][k] + "-"
									+ TypeContent.TYPE2_TEXT[j][k][l];
						}
					}
				}
			}*/
            map.put(LayoutStructure.from[2], typeText);
            data.add(map);

        }

        return data;
    }

}
