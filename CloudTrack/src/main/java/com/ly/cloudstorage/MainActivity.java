package com.ly.cloudstorage;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ly.cloudstorage.bean.EntryWrapper;
import com.ly.cloudstorage.bean.UploadTask;
import com.ly.cloudstorage.net.IDataCallBack;
import com.ly.cloudstorage.upload.net.UploadManager;
import com.ly.cloudstorage.utils.Logger;
import com.ly.cloudstorage.utils.Utils;
import com.vdisk.net.RESTUtility;
import com.vdisk.net.VDiskAPI;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends SherlockActivity implements IDataCallBack {

    public static final int BATCH_MOVE_REQUEST_CODE = 33;
    protected static final int REQUEST_DEL_FILE = 2;
    protected static final int REQUEST_BATCH_DELETE = 3;
    protected static final int REQUEST_MOVE = 106;
    protected static final int REQUEST_RENAME = 101;
    private static final int REQUEST_CODE_GET_FILE_LIST = 0;
    private static final int REQUEST_CREATE_DIRECTORY = 1;
    private static final String TAG = "MainActivity";
    UploadManager.UploadStatusListener mUploadStatusListener = new UploadManager.SimpleUploadStatusListener() {

        @Override
        public void onSuccess(VDiskAPI.Entry entry, UploadTask uploadTask) {
            Logger.d(TAG, "UploadManager onSuccess entry: " + entry.fileName());
            Utils.showToastString(MainActivity.this, entry.fileName() + "上传成功",
                    Toast.LENGTH_SHORT);
        }
    };
    protected ArrayList<EntryWrapper> mCurrentDataItems = new ArrayList<EntryWrapper>();
    private PullToRefreshListView mHomeList = null;
    private View mRootView = null;
    private View mViewTag = null;
    private ListView mActualHomeList = null;
    private CloudListAdapter mListAdapter = null;
    private String mCurrentPath = "/";
    private boolean isEditMode = false;
    //private ActionModeCallback mActionMode;
    private MenuItem mMenuMoreItem;
    private MenuItem mMenuUploadItem;
    private MenuItem mMenuDownloadItem;
    private MenuItem mMenuSelectItem;
    private TextView mEmptyView;
    private ProgressDialog mProgressDialog;
    // 批量移动
    private ArrayList<String> mDirPathList;
    private ArrayList<String> mFileList;

    //private ActionModeC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void handleServiceResult(int requestCode, int errCode, Object data, Bundle session) {

    }

    private void updateActionMenuSelectAllTitle(boolean showSelectionAll) {
        mMenuSelectItem.setTitle(showSelectionAll ? "全选" : "取消");
    }

    class CloudListAdapter extends BaseAdapter implements View.OnClickListener {

        PopupWindow mPopupWindow;
        View.OnClickListener mEditListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = (Integer) v.getTag();
                showPopup(v);
            }
        };
        private Integer mPosition;
        private int mCheckedCount = 0;

        @Override
        public int getCount() {
            return mCurrentDataItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mCurrentDataItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = MainActivity.this.getLayoutInflater().inflate(
                        R.layout.file_item,null);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tvSize = (TextView) convertView
                        .findViewById(R.id.tv_size);
                holder.ivIcon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                holder.ivOption = (ImageView) convertView
                        .findViewById(R.id.iv_option);
                holder.cbCheck = (CheckBox) convertView
                        .findViewById(R.id.cb_checkbox);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            EntryWrapper entryWrapper = mCurrentDataItems.get(position);

            holder.tvName.setText(entryWrapper.entry.fileName());
            holder.tvTime.setText(Utils.getFormateTime(RESTUtility.
                    parseDate(entryWrapper.entry.modified)));
            if (entryWrapper.entry.isDir){
                holder.tvSize.setVisibility(View.GONE);
                holder.ivIcon.setImageResource(R.drawable.directory_icon);
            }else {
                holder.tvSize.setVisibility(View.VISIBLE);
                holder.tvSize.setText(entryWrapper.entry.size);

                Object[] mimeType = Utils.getMIMEType(entryWrapper.entry.fileName());
                holder.ivIcon.setImageResource((Integer) mimeType[1]);
            }

            //如果是编辑模式
            if (isEditMode){
                holder.cbCheck.setVisibility(View.VISIBLE);
                holder.cbCheck.setChecked(entryWrapper.isChecked);
                holder.ivOption.setVisibility(View.GONE);
            }else {
                holder.cbCheck.setVisibility(View.GONE);
                holder.ivOption.setVisibility(View.VISIBLE);

                holder.ivOption.setTag(position);
                holder.ivOption.setOnClickListener(mEditListener);
            }
            return convertView;
        }

        private void showPopup(View v){
            View listItemView = (View) v.getParent();
            if (mPopupWindow == null){
                View view = MainActivity.this.getLayoutInflater().
                        inflate(R.layout.file_item_pop, null, true);
                view.findViewById(R.id.ll_move).setOnClickListener(this);
                view.findViewById(R.id.ll_delete).setOnClickListener(this);
                view.findViewById(R.id.ll_rename).setOnClickListener(this);

                mPopupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,listItemView.getHeight(),true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            }

            boolean checkPopupUpOrDown = checkPopupUpOrDown(listItemView);
            if (!checkPopupUpOrDown){
                mPopupWindow.showAsDropDown(v,0,
                        Utils.dip2px(MainActivity.this,19));
            }else{
                mPopupWindow.showAsDropDown(v,0,
                        -listItemView.getHeight() - Utils.dip2px(MainActivity.this,44));
            }

        }

        /**
         * 判断弹出框是在上方还是下方显示
         * @param listItemView
         * @return
         */
        private boolean checkPopupUpOrDown(View listItemView){
            int[] pos = new int[2];
            listItemView.getLocationOnScreen(pos);
            int offsetY = pos[1] + listItemView.getHeight();

            WindowManager wm = (WindowManager) MainActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);
            int screenHeight = wm.getDefaultDisplay().getHeight();
            if (screenHeight - offsetY < listItemView.getHeight() + 2){
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void onClick(View v) {
            if (mPopupWindow!=null){
                mPopupWindow.dismiss();
            }
            switch (v.getId()){
                case R.id.ll_delete:
                    Log.d(TAG,"item delete!-->" + mPosition);
                    break;
                case R.id.ll_move:
                    break;
                case R.id.ll_rename:
                    break;
                default:
                    break;
            }
        }

        public void removeSelection(){
            for (EntryWrapper entryWrapper:mCurrentDataItems){
                mCheckedCount = 0;
                notifyDataSetChanged();

            }
        }

        public void toggleChecked(int mPosition,View view){
            EntryWrapper entryWrapper = (EntryWrapper) getItem(mPosition);
            entryWrapper.isChecked = !entryWrapper.isChecked;
            Logger.d(TAG, "entry is checked-->" + entryWrapper.isChecked);

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_checkbox);
            checkBox.setChecked(entryWrapper.isChecked);

            Logger.d(TAG, "checkBox.isChecked()-->" + checkBox.isChecked());
            Logger.d(TAG, "mCheckedCount1-->" + mCheckedCount);

            if (checkBox.isChecked()){
                mCheckedCount++;
            }else {
                mCheckedCount--;
            }

            Logger.d(TAG, "mCheckedCount2-->" + mCheckedCount);

            if (mCheckedCount == mCurrentDataItems.size()){

            }
        }
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvSize;

        ImageView ivIcon;
        ImageView ivOption;

        CheckBox cbCheck;
    }

}
