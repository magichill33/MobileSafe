package com.lilo.sm;


import java.util.ArrayList;
import java.util.List;

import com.lilo.model.CaseModel;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    // SuperMap iServer提供的地图采用固定地址传递
    private Button btnParts;
    private Button btnHouse;
    private Button btnAnalyze; //案件核实
    private Button btnReport;
    private Button btnCaseDetail; //案件详情
    private Button btnLeaderCaseCheck;
    private Button btnLeaderSingCase;
    private Button btnCaseLocate;
    private Button btnHouseLocate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParts = (Button) findViewById(R.id.btnParts);
        btnHouse = (Button) findViewById(R.id.btnHouse);
        btnAnalyze = (Button) findViewById(R.id.btnAnalyze);
        btnReport = (Button) findViewById(R.id.btnReport);
        btnCaseDetail = (Button) findViewById(R.id.btnCaseDetail);
        btnLeaderCaseCheck = (Button) findViewById(R.id.btnleaderCaseCheck);
        btnLeaderSingCase = (Button) findViewById(R.id.btnleaderSingleCaseCheck);
        btnCaseLocate = (Button) findViewById(R.id.btnCaseLocate);
        btnHouseLocate = (Button) findViewById(R.id.btnHouseLocate);

        final Intent intent = new Intent();
        intent.setClass(MainActivity.this, LiloMapActivity.class);
        //部件更新
        btnParts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 8);
                bundle.putString("pType", "0118");
                bundle.putString("gridCodes", "62120210200002,62120210200003");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //事件上报
        btnReport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 0);
                ArrayList<CaseModel> caselist = new ArrayList<CaseModel>();
                CaseModel  model1 = new CaseModel(104.926879,33.388646,"62120210000602");
                CaseModel  model2 = new CaseModel(104.925386,33.390110,"62120210000603");
                CaseModel  model3 = new CaseModel(104.926917,33.388884,"62120210000602");
                caselist.add(model1);
                caselist.add(model2);
                caselist.add(model3);
                bundle.putParcelableArrayList("caselist", caselist);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //房屋更新
        btnHouse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 1);
                bundle.putString("gridCodes", "62120210000601,62120210000602");
                bundle.putString("houseCodes","62120210000601004,62120210000601003,62120210000601011,62120210000602007,62120210000602008");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //案件核实
        btnAnalyze.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 2);
                ArrayList<CaseModel> caseList = new ArrayList<CaseModel>();
                CaseModel c1 = new CaseModel("1001","城管案件1",104.939528,33.374305);
                CaseModel c2 = new CaseModel("1002","城管案件2",104.942456,33.375698);
                CaseModel c3 = new CaseModel("1003","城管案件3",104.943611,33.374319);
                CaseModel c4 = new CaseModel("1004","城管案件4",104.944951,33.374131);
                caseList.add(c1);
                caseList.add(c2);
                caseList.add(c3);
                caseList.add(c4);

                bundle.putParcelableArrayList("caselist", caseList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //案件详情
        btnCaseDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //领导通案件核实
        btnLeaderCaseCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //领导通单个案件核实
        btnLeaderSingCase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 5);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //事件定位
        btnCaseLocate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 6);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //事件定位
        btnHouseLocate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleType", 7);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}