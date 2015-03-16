package com.lilosoft.xtcm.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.constant.TableStructure;
import com.lilosoft.xtcm.utils.LogFactory;

/**
 * @category 网络连接类
 * @author William Liu
 *
 */
public class HttpConnection {

    /**
     * @category 网络请求指令--登录 [用户名] [密码]
     */
    public final static int CONNECTION_COMMON_PCARD_SETUP = 0xA9881888;
    public final static int CONNECTION_COMMON_PCARD = 0xA9881999;
    public final static int CONNECTION_COMMON_PCARD_GETRECORD = 0xA9881777;
    public final static int CONNECTION_COMMON_PCARD_PLACE = 0xA9881669;
    public final static int CONNECTION_COMMON_LOGIN = 0xA9881025;
    /**
     * @category 网络请求指令--问题上报 [登录帐号] [选择事项] [简要概述] [详细描述] [x坐标] [y坐标] [网格编号]
     *           [图片集合*MAX9]
     */
    public final static int CONNECTION_COMMON_REPORT = 0xA9883025;
    public final static int CONNECTION_PART_REPORT = 0xA6884029;
    /**
     * @category 网络请求指令--待核查任务列表[用户名]
     */
    public final static int CONNECTION_READY_EXAMINE_LIST = 0xA9883225;
    /**
     * @category 网络请求指令--待核查任务单个数据 [CaseID]
     */
    public final static int CONNECTION_READY_EXAMINE = 0xA9883325;
    /**
     * @category 网络请求指令--确认核查[IsOK] [InspectID] [提交内容] [CaseCode] [用户名]
     *           [图片集合*MAX9]
     */
    public final static int CONNECTION_READY_EXAMINE_SUBMIT = 0xA9813325;
    /**
     * @category 网络请求指令--待核实任务列表[用户名]
     */
    public final static int CONNECTION_READY_VERIFY_LIST = 0xA1823325;
    /**
     * @category 网络请求指令--待核实任务单个数据[CaseID]
     */
    public final static int CONNECTION_READY_VERIFY = 0xA1823305;
    /**
     * @category 网络请求指令--确认核实[IsOK] [InspectID] [提交内容] [用户名] [CaseCode]
     *           [图片集合*MAX9]
     */
    public final static int CONNECTION_READY_VERIFY_SUBMIT = 0xA1713300;
    /**
     * @category 网络请求指令--待办理任务列表[用户名]
     */
    public final static int CONNECTION_READY_DISPOSE_LIST = 0xA171B300;
    /**
     * @category 网络请求指令--待办理任务单个数据[CaseID]
     */
    public final static int CONNECTION_READY_DISPOSE = 0xA171B200;
    /**
     * @category 网络请求指令--确认办理[IsOK] [内容] [TaskID] [HandID] [CaseID] [用户名]
     *           [图片集合*MAX9]
     */
    public final static int CONNECTION_READY_DISPOSE_SUBMIT = 0xA1F1B200;
    /**
     * @category 网络请求指令--历史记录列表[用户名] [搜索类型] [页] [每页数量]
     *
     *           搜索类型(sb-上报历史|hc-核查历史|hs-核实历史|bl-办理历史)
     */
    public final static int CONNECTION_QUESTION_HISTORY_LIST = 0xA2F1B201;
    /**
     * @category 网络请求指令--历史记录单个数据[CaseID]
     */
    public final static int CONNECTION_QUESTION_HISTORY = 0xA2F1C207;
    /**
     * @category 网络请求指令--自行处理上报[...]
     */
    public final static int CONNECTION_QUESTION_DISPOSED = 0xA2E1B211;
    /**
     * @category 请求指令--行政审查[用户名] [SearchType] [页] [每页数量]
     */
    public final static int CONNECTION_ADMIN_APPROVE = 0xA231B24F;
    /**
     * 行政审批详情
     */
    public final static int CONNECTION_ADMIN_APPROVE_DETAIL = 123;
    /**
     * @category 网络请求指令--轨迹上报[Y] [X] [Type] [用户名]
     */
    public final static int CONNECTION_LOCATION_REPORT = 0xA2F1C24F;
    /**
     * 获取联系人数据
     */
    public static final int CONNECTION_CONTACTS_DATA = 0x00B1C3;
    /**
     * 事件分类信息
     */
    public final static int CONNECTION_EVENT_INFO = 888;
    private final String TAG = "HttpConnection";
    private JSONArray array;

    /**
     * @category 客户端请求
     * @param httpPost
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    private String clientRequest(HttpPost httpPost)
            throws ClientProtocolException, IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                Config.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, Config.SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpResponse h = client.execute(httpPost);

        if (h.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            return EntityUtils.toString(h.getEntity(), Config.CHARSET);
        return null;
    }

    /**
     * @category json实体参数
     * @param URL
     * @param jsonParam
     * @return String
     * @throws IOException
     * @throws ClientProtocolException
     */
    private String setStringEntity(String URL, String jsonParam)
            throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(URL);
        LogFactory.e("HttpConnection_url", URL);
        if (null != jsonParam) {
            httpPost.setEntity(new StringEntity(jsonParam, Config.CHARSET));
            LogFactory.e("HttpConnection Request Value", jsonParam);
        }

        return clientRequest(httpPost);

    }

    /**
     * 调用此方法请仔细核对参数顺序
     *
     * @category 获取服务器返回的数据
     * @author William Liu
     * @param 网络连接指令
     * @param 所需参数请查看指令说明
     * @return String
     * @throws JSONException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String getData(int connectionCommon, String... arg)
            throws JSONException, ClientProtocolException, IOException {

        String data = "";

        switch (connectionCommon) {
            case CONNECTION_COMMON_PCARD_PLACE:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.PUNCHCARD_place)
                                .put(TableStructure.COVER_BODY,
                                        new JSONObject().put(
                                                TableStructure.PCARD_USERNAME,
                                                arg[0]))

                                .toString());
                break;
            case CONNECTION_COMMON_PCARD_GETRECORD:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.PUNCHCARD_Record)
                                .put(TableStructure.COVER_BODY,
                                        new JSONObject().put(
                                                TableStructure.PCARD_USERNAME,
                                                arg[0]))

                                .toString());
                break;
            case CONNECTION_COMMON_PCARD_SETUP:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject().put(TableStructure.COVER_HEAD,
                                TableStructure.PUNCHCARD_SETUP)

                                .toString());
                break;
            case CONNECTION_COMMON_PCARD:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.PUNCHCARD)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.PCARD_PLACEID,
                                                        arg[8])
                                                .put(TableStructure.PCARD_SWOFFTYPE,
                                                        arg[9])
                                                .put(TableStructure.PCARD_SWOFFTIME,
                                                        arg[10])
                                                .put(TableStructure.PCARD_USERNAME,
                                                        arg[0])
                                                .put(TableStructure.PCARD_DATE,
                                                        arg[1])
                                                .put(TableStructure.PCARD_WORKTIME,
                                                        arg[2])
                                                .put(TableStructure.PCARD_OFFWORKTIME,
                                                        arg[3])
                                                .put(TableStructure.PCARD_UPTYPE,
                                                        arg[4])
                                                .put(TableStructure.PCARD_OFFTYPE,
                                                        arg[5])
                                                .put(TableStructure.PCARD_ZWUPTIME,
                                                        arg[6]).put(
                                                        TableStructure.PCARD_ZWUPTYPE,
                                                        arg[7]))).toString());
                break;
            case CONNECTION_COMMON_LOGIN:
                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_LOGIN)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0])
                                                .put(TableStructure.R_USER_REQUEST_PASSCODE,
                                                        arg[1]))).toString());
                break;
            case CONNECTION_PART_REPORT:
                array = new JSONArray();
                if ("".equals(arg[12])) {
                    if ("".equals(arg[9])) {
                        if ("".equals(arg[6])) {

                        } else {
                            array.put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[6])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[7])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[8]));
                        }
                    } else {
                        array.put(
                                new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[6])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[7])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[8]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[9])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[10])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[11]));
                    }
                } else {
                    array.put(
                            new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[6])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[7])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[8]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[10])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[11]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[12])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[13])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[14]));
                }
                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_PART_REPORT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                // 案件数
                                                .put(TableStructure.R_REPORT_RESPONSE_CASES_NUM,
                                                        arg[25])
                                                        // 图层编号
                                                .put(TableStructure.EXTEND4,
                                                        arg[26])
                                                        // 电话
                                                .put(TableStructure.R_REPORT_RESPONSE_TEL,
                                                        "")
                                                        // 姓名
                                                .put(TableStructure.R_REPORT_RESPONSE_NEME,
                                                        "")
                                                        // 居住地址
                                                .put(TableStructure.R_REPORT_RESPONSE_LIVE_ADRES,
                                                        "")
                                                        // 登录帐号
                                                .put(TableStructure.R_REPORT_RESPONSE_USER_NAME,
                                                        arg[0])
                                                        // 选择事项
                                                .put(TableStructure.R_REPORT_RESPONSE_TYPE,
                                                        arg[1])
                                                        // 简要概述
                                                .put(TableStructure.R_REPORT_RESPONSE_ALL_TYPE,
                                                        arg[2])
                                                        // 详细描述
                                                .put(TableStructure.R_REPORT_RESPONSE_DESCRIPT,
                                                        arg[3])
                                                        // 地址
                                                .put(TableStructure.R_REPORT_RESPONSE_ADRES,
                                                        "")
                                                        // x坐标
                                                .put(TableStructure.R_REPORT_RESPONSE_X,
                                                        arg[4])
                                                        // y坐标
                                                .put(TableStructure.R_REPORT_RESPONSE_Y,
                                                        arg[5])
                                                        // 网格编号
                                                .put(TableStructure.R_REPORT_RESPONSE_GRID_CODE,
                                                        arg[18])
                                                .put(TableStructure.OBJECTID,
                                                        arg[15])
                                                .put(TableStructure.OBJCODE,
                                                        arg[16])
                                                .put(TableStructure.OBJNAME,
                                                        arg[17])
                                                .put(TableStructure.DEPTCODE1,
                                                        arg[19])
                                                .put(TableStructure.DEPTNAME1,
                                                        arg[20])
                                                .put(TableStructure.DEPTCODE2,
                                                        arg[21])
                                                .put(TableStructure.DEPTNAME2,
                                                        arg[22])
                                                .put(TableStructure.DEPTCODE3,
                                                        arg[23])
                                                .put(TableStructure.DEPTNAME3,
                                                        arg[24])
                                                        // 图片集合
                                                .put(TableStructure.R_REPORT_RESPONSE_FLIST,
                                                        array))).toString());
                break;
            case CONNECTION_COMMON_REPORT:

                array = new JSONArray();
                if ("".equals(arg[14])) {
                    if ("".equals(arg[11])) {
                        if ("".equals(arg[8])) {

                        } else {
                            array.put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[10]));
                        }
                    } else {
                        array.put(
                                new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[8])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[9])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[10]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[11])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[12])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[13]));
                    }
                } else {
                    array.put(
                            new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[10]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[11])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[12])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[13]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[14])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[15])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[16]));
                }

                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_REPORT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                // 案件数
                                                .put(TableStructure.R_REPORT_RESPONSE_CASES_NUM,
                                                        arg[0])
                                                        // 电话
                                                .put(TableStructure.R_REPORT_RESPONSE_TEL,
                                                        "")
                                                        // 姓名
                                                .put(TableStructure.R_REPORT_RESPONSE_NEME,
                                                        "")
                                                        // 居住地址
                                                .put(TableStructure.R_REPORT_RESPONSE_LIVE_ADRES,
                                                        "")
                                                        // 登录帐号
                                                .put(TableStructure.R_REPORT_RESPONSE_USER_NAME,
                                                        arg[1])
                                                        // 选择事项
                                                .put(TableStructure.R_REPORT_RESPONSE_TYPE,
                                                        arg[2])
                                                        // 简要概述
                                                .put(TableStructure.R_REPORT_RESPONSE_ALL_TYPE,
                                                        arg[3])
                                                        // 详细描述
                                                .put(TableStructure.R_REPORT_RESPONSE_DESCRIPT,
                                                        arg[4])
                                                        // 地址
                                                .put(TableStructure.R_REPORT_RESPONSE_ADRES,
                                                        "")
                                                        // x坐标
                                                .put(TableStructure.R_REPORT_RESPONSE_X,
                                                        arg[5])
                                                        // y坐标
                                                .put(TableStructure.R_REPORT_RESPONSE_Y,
                                                        arg[6])
                                                        // 网格编号
                                                .put(TableStructure.R_REPORT_RESPONSE_GRID_CODE,
                                                        arg[7])
                                                        // 图片集合
                                                .put(TableStructure.R_REPORT_RESPONSE_FLIST,
                                                        array))).toString());

                break;
            case CONNECTION_READY_EXAMINE_LIST:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_EXAMINE_LIST)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_READY_EXAMINE:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_EXAMINE)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_EXAMINE_REQUEST_CASE_ID,
                                                        arg[0]))).toString());

                break;
            case CONNECTION_READY_EXAMINE_SUBMIT:
                JSONArray arrayExamine = new JSONArray();
                if ("".equals(arg[11])) {
                    if ("".equals(arg[8])) {
                        if ("".equals(arg[5])) {

                        } else {
                            arrayExamine.put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[5])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[6])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[7]));
                        }
                    } else {
                        arrayExamine
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[5])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[6])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[7]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[8])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[9])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[10]));
                    }
                } else {
                    arrayExamine
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[5])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[6])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[7]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[10]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[11])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[12])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[13]));
                }
                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_EXAMINE_SUBMIT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_EXAMINE_REQUEST_IS_OK,
                                                        arg[0])
                                                .put(TableStructure.R_EXAMINE_REQUEST_INSPECT_ID,
                                                        arg[1])
                                                .put(TableStructure.R_EXAMINE_REQUEST_VERIFYEED_BACK_CONTENT,
                                                        arg[2])
                                                .put(TableStructure.R_EXAMINE_REQUEST_CASE_CODE,
                                                        arg[3])
                                                .put(TableStructure.R_EXAMINE_REQUEST_LOGIN_NAME,
                                                        arg[4])
                                                .put(TableStructure.R_REPORT_RESPONSE_FLIST,
                                                        arrayExamine))).toString());
                break;
            case CONNECTION_READY_VERIFY_LIST:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_VERIFY_LIST)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_READY_VERIFY:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_VERIFY)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_READY_VERIFY_CASE_ID,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_READY_VERIFY_SUBMIT:
                JSONArray arrayVerify = new JSONArray();
                if ("".equals(arg[11])) {
                    if ("".equals(arg[8])) {
                        if ("".equals(arg[5])) {

                        } else {
                            arrayVerify
                                    .put(new JSONObject()
                                            .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                                    arg[5])
                                            .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                                    arg[6])
                                            .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                                    arg[7]));
                        }
                    } else {
                        arrayVerify
                                .put(new JSONObject()
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                                arg[5])
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                                arg[6])
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                                arg[7]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                                arg[8])
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                                arg[9])
                                        .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                                arg[10]));
                    }
                } else {
                    arrayVerify
                            .put(new JSONObject()
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                            arg[5])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                            arg[6])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                            arg[7]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                            arg[10]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FNAME,
                                            arg[11])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FDATA,
                                            arg[12])
                                    .put(TableStructure.R_READY_VERIFY_SUBMIT_FTYPE,
                                            arg[13]));
                }
                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_VERIFY_SUBMIT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_IS_OK,
                                                        arg[0])
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_INSPECT_ID,
                                                        arg[1])
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_VERIFYEED_BACK_CONTENT,
                                                        arg[2])
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_LOGIN_NAME,
                                                        arg[3])
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_CASE_CODE,
                                                        arg[4])
                                                .put(TableStructure.R_READY_VERIFY_SUBMIT_FLIST,
                                                        arrayVerify))).toString());
                break;
            case CONNECTION_READY_DISPOSE_LIST:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_DISPOSE_LIST)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_READY_DISPOSE:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_DISPOSE)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_DISPOSE_REQUEST_CASE_ID2,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_READY_DISPOSE_SUBMIT:
                JSONArray arrayDispose = new JSONArray();
                if ("".equals(arg[12])) {
                    if ("".equals(arg[9])) {
                        if ("".equals(arg[6])) {

                        } else {
                            arrayDispose.put(new JSONObject()
                                    .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                            arg[6])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                            arg[7])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                            arg[8]));
                        }
                    } else {
                        arrayDispose
                                .put(new JSONObject()
                                        .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                                arg[6])
                                        .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                                arg[7])
                                        .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                                arg[8]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                                arg[9])
                                        .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                                arg[10])
                                        .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                                arg[11]));
                    }
                } else {
                    arrayDispose
                            .put(new JSONObject()
                                    .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                            arg[6])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                            arg[7])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                            arg[8]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                            arg[9])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                            arg[10])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                            arg[11]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_DISPOSE_REQUEST_FNAME,
                                            arg[12])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FDATA,
                                            arg[13])
                                    .put(TableStructure.R_DISPOSE_REQUEST_FTYPE,
                                            arg[14]));
                }
                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_READY_DISPOSE_SUBMIT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_DISPOSE_REQUEST_IS_OK,
                                                        arg[0])
                                                .put(TableStructure.R_DISPOSE_REQUEST_DEAL_RESULT,
                                                        arg[1])
                                                .put(TableStructure.R_DISPOSE_REQUEST_TASK_ID,
                                                        arg[2])
                                                .put(TableStructure.R_DISPOSE_REQUEST_HAND_ID,
                                                        arg[3])
                                                .put(TableStructure.R_DISPOSE_REQUEST_CASE_ID,
                                                        arg[4])
                                                .put(TableStructure.R_DISPOSE_REQUEST_LOGIN_NAME,
                                                        arg[5])
                                                .put(TableStructure.R_DISPOSE_REQUEST_FLIST,
                                                        arrayDispose))).toString());
                break;
            case CONNECTION_QUESTION_HISTORY_LIST:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_QUESTION_HISTORY_LIST)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0])
                                                .put(TableStructure.R_READY_HISTORY_LIST_SEARCH_TYPE,
                                                        arg[1])
                                                .put(TableStructure.R_READY_HISTORY_LIST_PAGE_INDEX,
                                                        arg[2])
                                                .put(TableStructure.R_READY_HISTORY_LIST_PAGE_SIZE,
                                                        arg[3]))).toString());
                break;
            case CONNECTION_QUESTION_HISTORY:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_QUESTION_HISTORY)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_READY_HISTORY_CASE_ID,
                                                        arg[0]))).toString());
                break;
            case CONNECTION_QUESTION_DISPOSED:
                JSONArray arrayddd = new JSONArray();
                if ("".equals(arg[14])) {
                    if ("".equals(arg[11])) {
                        if ("".equals(arg[8])) {

                        } else {
                            arrayddd.put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[10]));
                        }
                    } else {
                        arrayddd.put(
                                new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[8])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[9])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[10]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[11])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[12])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[13]));
                    }
                } else {
                    arrayddd.put(
                            new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[8])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[9])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[10]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[11])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[12])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[13]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[14])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[15])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[16]));
                }
                JSONArray arrayccc = new JSONArray();
                if ("".equals(arg[23])) {
                    if ("".equals(arg[20])) {
                        if ("".equals(arg[17])) {

                        } else {
                            arrayccc.put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[17])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[18])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[19]));
                        }
                    } else {
                        arrayccc.put(
                                new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[17])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[18])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[19]))
                                .put(new JSONObject()
                                        .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                                arg[20])
                                        .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                                arg[21])
                                        .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                                arg[22]));
                    }
                } else {
                    arrayccc.put(
                            new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[17])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[18])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[19]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[20])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[21])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[22]))
                            .put(new JSONObject()
                                    .put(TableStructure.R_REPORT_RESPONSE_FNAME,
                                            arg[23])
                                    .put(TableStructure.R_REPORT_RESPONSE_FDATA,
                                            arg[24])
                                    .put(TableStructure.R_REPORT_RESPONSE_FTYPE,
                                            arg[25]));
                }

                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.WENTIZHICHULI)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject().put("CASEITEM", arg[0])
                                                .put("CASETITLE", arg[1])
                                                .put("CASECONTENT", arg[2])
                                                .put("X", arg[3]).put("Y", arg[4])
                                                .put("GridCode", arg[5])
                                                .put("ACCEPTUSERID", arg[6])
                                                .put("DEALUSERNAME", arg[7])
                                                .put("ReportList", arrayddd).put(
                                                        "DealList", arrayccc))).toString());
                break;

            case CONNECTION_ADMIN_APPROVE:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_ADMINI_APPROVE_LIST)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_USER_REQUEST_USERNAME_NAME,
                                                        arg[0])
                                                .put(TableStructure.R_READY_HISTORY_LIST_SEARCH_TYPE,
                                                        arg[1])
                                                .put(TableStructure.R_READY_HISTORY_LIST_PAGE_INDEX,
                                                        arg[2])
                                                .put(TableStructure.R_READY_HISTORY_LIST_PAGE_SIZE,
                                                        arg[3]))).toString());
                break;
            /**
             * 行政审批详情
             */
            case CONNECTION_ADMIN_APPROVE_DETAIL:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_ADMINI_APPROVE_DETAIL)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_READY_APPROVE_ADTIVE_ID,
                                                        arg[0]))).toString());
                break;

            case CONNECTION_LOCATION_REPORT:

                data = setStringEntity(
                        Config.URL_REPORT,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_LOCATION_REPORT)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject()
                                                .put(TableStructure.R_LOCAL_REQUEST_KEY,
                                                        arg[0])
                                                .put(TableStructure.R_LOCAL_REQUEST_TYPE,
                                                        arg[1])
                                                .put(TableStructure.R_LOCAL_REQUEST_X,
                                                        arg[2]).put(
                                                        TableStructure.R_LOCAL_REQUEST_Y,
                                                        arg[3]))).toString());
                break;

            /**
             * 获取事件分类信息
             */
            case CONNECTION_EVENT_INFO:
                String type = arg[0];

                String rejson = new JSONObject()
                        .put(TableStructure.COVER_HEAD,
                                TableStructure.V_ACT_ADMINI_GET_ALLTYPE)
                        .put(TableStructure.COVER_BODY,
                                (new JSONObject().put(TableStructure.TypeVison,
                                        type))).toString();
                Log.i("JSON", rejson);
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.V_ACT_ADMINI_GET_ALLTYPE)
                                .put(TableStructure.COVER_BODY,
                                        (new JSONObject().put(
                                                TableStructure.TypeVison, type)))
                                .toString());
                break;
            case CONNECTION_CONTACTS_DATA:
                data = setStringEntity(
                        Config.URL_READY_ISSUED,
                        new JSONObject()
                                .put(TableStructure.COVER_HEAD,
                                        TableStructure.GetLinkManInfo)
                                .put(TableStructure.COVER_BODY, new JSONObject())
                                .toString());
        }

        if (null != data)
            LogFactory.e(TAG + "_" + connectionCommon, data);
        return data;
    }
}
