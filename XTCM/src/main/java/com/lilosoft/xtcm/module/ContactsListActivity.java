package com.lilosoft.xtcm.module;

import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView.OnChildClickListener;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.dao.CommonNumberDao;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

public class ContactsListActivity extends NormalBaseActivity {

    private TitleBar mTitleBar;
    private ExpandableListView elv;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_contactslist);
        initTitleBar();
        initValues();
    }

    private void initValues() {
        elv = (ExpandableListView) findViewById(R.id.elv);

//		if (!CommonNumberDao.isExist()) {
//			CommonNumberDao.copyDB(this);
//		}

        // 组数据
        List<Map<String, Object>> groupData = CommonNumberDao.getGroupData(mContext);
        // 子数据
        List<List<Map<String, Object>>> childData = CommonNumberDao
                .getChildData(mContext);

        final SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,// 上下文
                groupData,// 组数据 List<Map<String,Object>>
                android.R.layout.simple_expandable_list_item_1,// 组布局 R.layout.
                new String[] { "dept" }, // 组数据的key String[]
                new int[] { android.R.id.text1 }, // 组布局控件的id int[]
                childData, // 孩子的数据 List<List<Map<String,Object>>>
                android.R.layout.simple_expandable_list_item_2,// 孩子的布局 R.layout
                new String[] { "name", "phone" }, // 孩子数据的key String[]
                new int[] { android.R.id.text1, android.R.id.text2 });// 孩子布局控件的id
        // int[]
        elv.setAdapter(adapter);

        // 设置孩子条目设置点击事件
        elv.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Map<String, String> map = (Map<String, String>) adapter
                        .getChild(groupPosition, childPosition);
                String mobilephone = map.get("mobilephone");
                String shortnum = map.get("shortnum");
                // 弹出选择拨号对话框
                showSelectDialog(mobilephone, shortnum);
                // 直接拨号
                // Intent intent = new Intent(Intent.ACTION_CALL);
                // intent.setData(Uri.parse("tel:" + mobilephone));
                // context.startActivity(intent);
                return false;
            }
        });
    }

    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText("联系人列表");
    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub

    }

    protected void showSelectDialog(String longnum, String shortnum) {
        // TODO Auto-generated method stub
        // AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        // dialogBuilder.setTitle("请选择拨号类型：");
        // dialogBuilder.setView(View.inflate(context,
        // R.layout.ldt_selectdialog, dialogBuilder));
        SelectDialog dialog = new SelectDialog(mContext, longnum, shortnum);
        dialog.setTitle("请选择拨号类型");
        dialog.show();
    }

    public class SelectDialog extends Dialog {

        String longnum;
        String shortnum;

        public SelectDialog(Context context, String longnum, String shortnum) {
            super(context);
            this.longnum = longnum;
            this.shortnum = shortnum;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.selectdialog);
            Button bt_long = (Button) this.findViewById(R.id.bt_long);
            Button bt_short = (Button) this.findViewById(R.id.bt_short);

            bt_long.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + longnum));
                    mContext.startActivity(intent);
                }
            });

            bt_short.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + shortnum));
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
