package com.lilosoft.xtcm.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.HomeBaseActivity;
import com.lilosoft.xtcm.base.TabBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.LayoutStructure;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.instantiation.ExamineBean;
import com.lilosoft.xtcm.instantiation.ExamineListBean;
import com.lilosoft.xtcm.instantiation.FileBean;
import com.lilosoft.xtcm.instantiation.User;
import com.lilosoft.xtcm.network.HttpConnection;
import com.lilosoft.xtcm.utils.JsonParseInterface;
import com.lilosoft.xtcm.utils.LogFactory;
import com.lilosoft.xtcm.views.MPProgressBar;
import com.lilosoft.xtcm.views.adapter.SmartWindowAdapter;

/**
 * @category 待核查
 * @author William Liu
 * 
 */
public class ReadyExamineActivity extends TabBaseActivity implements
		JsonParseInterface {

	/**
	 * 上报成功
	 */
	private final static int MSG_EXAMINE_SUCCESS_ORDER = 0x0FFFFFFF;
	/**
	 * 上报失败
	 */
	private final static int MSG_EXAMINE_LOST_ORDER = 0x00FFFFFF;
	/**
	 * 没有数据
	 */
	private final static int MSG_EXAMINE_NONE_DATA = 0x000FFFFF;
	/**
	 * 单条数据采集完毕
	 */
	private final static int MSG_EXAMINE_GATHER_DONE = 0x0000FFFF;
	public static ExamineBean bean = null;
	private final String TAG = "ReadyExamineActivity";
	private ListView listView;
	private List<? extends Map<String, ?>> data;
	private SmartWindowAdapter adapter;
	private Message m;
	/**
	 * @category 核查列表请求处理
	 */
	private Thread examineListThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			LogFactory.e(TAG, "examineList thread start");
			m = new Message();
			m.what = Config.SHOW_PROGRESS_DIALOG;
			myHandle.sendMessage(m);
			try {
				HttpConnection httpConnection = new HttpConnection();
				action(httpConnection.getData(
						HttpConnection.CONNECTION_READY_EXAMINE_LIST,
						User.username));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "ClientProtocolException 404");
				m = new Message();
				m.what = Config.MSG_LOST_404;
				myHandle.sendMessage(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "IOException");
				m = new Message();
				m.what = Config.MSG_LOST_CONN;
				myHandle.sendMessage(m);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "JSONException");
				m = new Message();
				m.what = Config.MSG_LOST_JSON;
				myHandle.sendMessage(m);
			} finally {
				m = new Message();
				m.what = Config.DISMISS_PROGRESS_DIALOG;
				myHandle.sendMessage(m);
				LogFactory.e(TAG, "examineList thread end");
			}
		}

	});
	/**
	 * @category 核查单条数据请求
	 */
	private Thread examineThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			LogFactory.e(TAG, "examineOne thread start");
			m = new Message();
			m.what = Config.SHOW_PROGRESS_DIALOG;
			myHandle.sendMessage(m);
			try {
				HttpConnection httpConnection = new HttpConnection();
				ExamineListBean examineBean = (ExamineListBean) data.get(key)
						.get("data");
				action(httpConnection.getData(
						HttpConnection.CONNECTION_READY_EXAMINE,
						examineBean.getCASEID()));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "ClientProtocolException 404");
				m = new Message();
				m.what = Config.MSG_LOST_404;
				myHandle.sendMessage(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "IOException");
				m = new Message();
				m.what = Config.MSG_LOST_CONN;
				myHandle.sendMessage(m);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				LogFactory.e(TAG, "JSONException");
				m = new Message();
				m.what = Config.MSG_LOST_JSON;
				myHandle.sendMessage(m);
			} finally {
				m = new Message();
				m.what = Config.DISMISS_PROGRESS_DIALOG;
				myHandle.sendMessage(m);
				LogFactory.e(TAG, "examineOne thread end");
			}
		}

	});
	private Thread threadG = null;
	private String err_Msg;
	/**
	 * @category 主线程处理
	 */
	@SuppressLint("HandlerLeak")
	private Handler myHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Config.SHOW_PROGRESS_DIALOG:
				HomeBaseActivity.showProgressDialog("交互中…");
				break;
			case Config.DISMISS_PROGRESS_DIALOG:
				HomeBaseActivity.dismissProgressDialog();
				break;
			case MSG_EXAMINE_GATHER_DONE:
				LogFactory.e(TAG, "Data gather_done");
				break;
			case MSG_EXAMINE_NONE_DATA:
				Toast.makeText(mContext, R.string.none_data, Toast.LENGTH_LONG)
						.show();
				adapter = new SmartWindowAdapter(mContext,
						new ArrayList<Map<String, Object>>(),
						R.layout.view_examine_item, null,
						LayoutStructure.toExamineItem);
				listView.setAdapter(adapter);
				break;
			case MSG_EXAMINE_SUCCESS_ORDER:
				LogFactory.e(TAG, "examine Success");
				adapter = new SmartWindowAdapter(mContext, data,
						R.layout.view_examine_item, null,
						LayoutStructure.toExamineItem);
				listView.setAdapter(adapter);
				break;
			case MSG_EXAMINE_LOST_ORDER:
				if (null != err_Msg && !"".equals(err_Msg)) {
					Toast.makeText(mContext, err_Msg, Toast.LENGTH_LONG).show();
					err_Msg = "";
				} else {
					Toast.makeText(mContext, R.string.error_data,
							Toast.LENGTH_LONG).show();
				}
				LogFactory.e(TAG, "examine lost");
				break;
			case Config.MSG_LOST_404:
				Toast.makeText(mContext, "Sorry 404", Toast.LENGTH_LONG).show();
				break;
			case Config.MSG_LOST_CONN:
				Toast.makeText(mContext, R.string.error_connection,
						Toast.LENGTH_LONG).show();
				break;
			case Config.MSG_LOST_JSON:
				Toast.makeText(mContext, R.string.error_data, Toast.LENGTH_LONG)
						.show();
				break;
			}

		}

	};
	private int key = 0;

	@Override
	protected void initListView() {
		// TODO Auto-generated method stub
		if (Config.NETWORK) {
			threadG = new Thread(examineListThread);
			threadG.start();
		} else {
			data = getListData();
			adapter = new SmartWindowAdapter(mContext, data,
					R.layout.view_examine_item, null,
					LayoutStructure.toExamineItem);
			listView.setAdapter(adapter);
		}
	}

	@Override
	protected void installViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_ready_examine);
		listView = (ListView) findViewById(R.id.ready_report_list);
		mPProgressBar = (MPProgressBar) findViewById(R.id.mPProgressBar);

	}

	@Override
	protected void registerEvents() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (Config.NETWORK) {
					key = arg2;
					threadG = new Thread(examineThread);
					threadG.start();
				} else {
					bean = new ExamineBean("test", "test", "test", "test",
							"test", "test", "test", "test", "test", "test",
							"test", "test", "test", "test", null, "test");

					startActivity(new Intent(mContext,
							QuestionExamineActivity.class));
				}

				// try {
				//
				// ExamineBean examineBean = (ExamineBean) data.get(arg2).get(
				// "data");
				// // 核查确认接口传参
				// Toast.makeText(
				// mContext,
				// httpConnection
				// .getData(
				// HttpConnection.CONNECTION_READY_EXAMINE_SUBMIT,
				// examineBean.getINSPECTID(), "",
				// examineBean.getCASECODE(),
				// User.username), Toast.LENGTH_LONG)
				// .show();
				//
				// } catch (ClientProtocolException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}

		});

	}

	/**
	 * @category 指令分发
	 * @param response
	 */
	private void action(String response) {
		m = new Message();
		try {
			m = new Message();
			m.what = jsonParseToOrder(response);
			myHandle.sendMessage(m);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogFactory.e(TAG, "action JSONException");
		}
	}

	@Override
	public int jsonParseToOrder(String response) throws JSONException {
		// TODO Auto-generated method stub
		// {"head":"ReadyExamineList","body":"[]"}
		if (null != response && !"".equals(response)) {
			JSONObject jsobj = new JSONObject(response);
			if (TableStructure.V_ACT_READY_EXAMINE_LIST.equals(jsobj
					.getString(TableStructure.COVER_HEAD))) {
				if (!"[]".equals(jsobj.getString(TableStructure.COVER_BODY))) {
					JSONArray jsonTypeData = jsobj
							.getJSONArray(TableStructure.COVER_BODY);
					if (null != jsonTypeData) {
						data = getListData(jsonTypeData);
					}
					return MSG_EXAMINE_SUCCESS_ORDER;

				} else {
					data = null;
					return MSG_EXAMINE_NONE_DATA;
				}

			} else if (TableStructure.V_ACT_READY_EXAMINE.equals(jsobj
					.getString(TableStructure.COVER_HEAD))) {
				JSONObject examine = jsobj
						.getJSONObject(TableStructure.COVER_BODY);
				List<FileBean> fileList = null;
				if (!"[]".equals(examine.getString("ApproveFileList"))) {
					JSONArray fileBeans = examine
							.getJSONArray("ApproveFileList");
					fileList = new ArrayList<FileBean>();
					for (int i = 0; i < fileBeans.length(); i++) {
						JSONObject obj = fileBeans.getJSONObject(i);
						FileBean bean = new FileBean(obj.getString("FName"),
								obj.getString("FType"), obj.getString("FData"));
						fileList.add(bean);
					}
				}

				bean = new ExamineBean(((ExamineListBean) data.get(key).get(
						"data")).getINSPECTID(),
						examine.getString("DISPATCHWARMINGTIME"),
						examine.getString("CASEITEM"),
						examine.getString("CASECODE"),
						examine.getString("CASEDESCRIPTION"),
						examine.getString("CASESOURCE"),
						examine.getString("CASETITLE"),
						examine.getString("SIGNTIME"),
						examine.getString("GRIDCODE"),
						examine.getString("CREATETIME"),
						examine.getString("PUTONRECORDWARNINGTIME"),
						examine.getString("PUTONRECORDTIME"),
						examine.getString("DeptName"),
						examine.getString("FEEDBACKDEALRESULT"), fileList,
						examine.getString("DeptFileList"));

				startActivity(new Intent(mContext,
						QuestionExamineActivity.class));
				return MSG_EXAMINE_GATHER_DONE;

			}
		} else {
			err_Msg = "数据异常！";
			LogFactory.e(TAG, "not data!");
		}
		return MSG_EXAMINE_LOST_ORDER;
	}

	private List<? extends Map<String, ?>> getListData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("data", new ExamineListBean("test", "test", "test", "test",
				"test", "test", "test", "test", "test", "test", "test", "test"));

		data.add(m);

		return data;
	}

	private List<? extends Map<String, ?>> getListData(JSONArray jsonTypeData) {

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		try {
			for (int i = 0; i < jsonTypeData.length(); i++) {
				JSONObject obj = jsonTypeData.getJSONObject(i);

				Map<String, Object> m = new HashMap<String, Object>();
				m.put("data",
						new ExamineListBean(obj.getString("INSPECTID"), obj
								.getString("CASEID"),
								obj.getString("CASECODE"), obj
										.getString("CASETITLE"), obj
										.getString("CASESTATUS"), obj
										.getString("CASEPARENTITEM1"), obj
										.getString("CASEPARENTITEM2"), obj
										.getString("CASEITEM"), obj
										.getString("PUTONRECORDWARNINGTIME"),
								obj.getString("PUTONRECORDEXTENDEDTIME"), obj
										.getString("PUTONRECORDUSERID"), obj
										.getString("BEGINVERIFYTIME")));

				data.add(m);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mContext, R.string.error_data, Toast.LENGTH_LONG)
					.show();
		}

		return data;
	}
}
