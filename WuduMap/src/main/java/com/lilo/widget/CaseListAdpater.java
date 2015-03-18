package com.lilo.widget;

import java.util.List;

import com.lilo.model.CaseModel;
import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.R;
import com.lilo.util.CaseCheckUtil;
import com.supermap.android.maps.Point2D;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CaseListAdpater extends BaseAdapter {

    private List<CaseModel> listItems;
    private CaseCheckUtil caseCheckUtil;
    private List<Point2D> geoPoints = null;
    private CaseListDialog caseListDialog;
    private Context context;

    public CaseListAdpater(List<CaseModel> caseModels, CaseCheckUtil caseCheckUtil,
                           CaseListDialog dialog,Context context) {
        super();
        this.listItems = caseModels;
        this.caseCheckUtil = caseCheckUtil;
        caseListDialog = dialog;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if(convertView == null){
            listItemView = new ListItemView();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            listItemView.caseImage = (ImageButton) convertView.findViewById(R.id.caseImage);
            listItemView.txtView = (TextView) convertView.findViewById(R.id.caseView);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        }else{
            listItemView = (ListItemView) convertView.getTag();
        }

        final CaseModel caseModel = listItems.get(position);
        listItemView.txtView.setText("" + caseModel.getCaseName());
        listItemView.txtView.setWidth((int) (caseListDialog.getWindow().getAttributes().width*0.8));
        listItemView.txtView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //此处填写跳转到核实的界面代码
                if(caseModel!=null)
                {
                    TipForm.showToast("案件名：" + caseModel.getCaseName(), context);
                }
                caseListDialog.dismiss();
                return false;
            }
        });

        listItemView.caseImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(geoPoints == null)
                {
                    geoPoints = caseCheckUtil.getGeoPoints();
                }

                Point2D mp = new Point2D(caseModel.getLon(),caseModel.getLat());
                if(geoPoints.size() > 1)
                {
                    geoPoints.remove(1);
                    geoPoints.add(mp);
                }else{
                    geoPoints.add(mp);
                }
                caseCheckUtil.drawCaseOverlay(mp,false);
                caseListDialog.dismiss();
                caseCheckUtil.analyzePath(geoPoints);

            }
        });

        return convertView;
    }

    class ListItemView{
        public TextView txtView;
        public ImageButton caseImage;
    }

}
