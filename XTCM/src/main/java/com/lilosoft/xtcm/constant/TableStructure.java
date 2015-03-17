package com.lilosoft.xtcm.constant;

/**
 * @category 表结构
 * @author William Liu
 *
 */
public class TableStructure {

    /**
     * @category 外层 json 字段名 begin
     */
    public final static String COVER_HEAD = "head";
    public final static String COVER_BODY = "body";
    /**
     * 外层 json 字段名 end
     */
    // 打卡
    public final static String PUNCHCARD = "InsertRecord";// 方法名
    public final static String PUNCHCARD_SETUP = "GetKQconfig";// 打卡配置
    public final static String PUNCHCARD_Record = "GetKQRecored";
    public final static String PUNCHCARD_place = "GetPersonKQPlace";



    public final static String pcard_morning_time = "SWSBSJ";
    public final static String pcard_afternoon_time = "ZWSBSJ";
    public final static String pcard_evening_time = "XWXBSJ";
    public final static String pcard_allowlate_time = "YXCDSJ";
    public final static String pcard_allow_leave_time = "YXZTSJ";
    public final static String pcard_gap_time = "DKJGSJ";

    // 字段
    public final static String PCARD_USERNAME = "USERLOGINNAME";
    public final static String PCARD_DATE = "GetRecordDate";
    public final static String PCARD_WORKTIME = "GETRECORDTIME";
    public final static String PCARD_OFFWORKTIME = "OffRecordDate";
    public final static String PCARD_UPTYPE = "UpType";
    public final static String PCARD_OFFTYPE = "OffType";
    public final static String PCARD_ZWUPTIME = "ZwUpTime";
    public final static String PCARD_ZWUPTYPE = "ZwUpType";
    public final static String PCARD_SWOFFTYPE = "SwOffType";
    public final static String PCARD_SWOFFTIME = "SwOffTime";
    public final static String PCARD_PLACEID="PLID";
    /**
     * @category 行为 json head值 begin
     */
    // 登录
    public final static String V_ACT_LOGIN = "login";
    // 问题上报
    public final static String V_ACT_REPORT = "approve";
    // 部件上报
    public final static String V_PART_REPORT = "Partapprove";

    // 待核查任务列表
    public final static String V_ACT_READY_EXAMINE_LIST = "ReadyExamineList";
    // 待核查任务单个数据
    public final static String V_ACT_READY_EXAMINE = "GetCaseInfo";
    // 确认核查
    public final static String V_ACT_READY_EXAMINE_SUBMIT = "inspect";

    // 待核实任务列表
    public final static String V_ACT_READY_VERIFY_LIST = "WaitingforInspectVerification";
    // 待核实任务单个数据
    public final static String V_ACT_READY_VERIFY = "WaitingforInspectCaseInfo"; //
    // 确认核实
    public final static String V_ACT_READY_VERIFY_SUBMIT = "ReadyVerifySubmit"; //

    // 待办理任务列表
    public final static String V_ACT_READY_DISPOSE_LIST = "WaitingforHandle";
    // 待办理任务单个数据
    public final static String V_ACT_READY_DISPOSE = "HandleCaseInfo"; //
    // 确认办理
    public final static String V_ACT_READY_DISPOSE_SUBMIT = "Handle"; //

    // 历史记录列表 （*HISTORY 块）
    public final static String V_ACT_QUESTION_HISTORY_LIST = "QuestionHistoryList"; // -------------------
    // 历史记录单个数据
    public final static String V_ACT_QUESTION_HISTORY = "QuestionHistory"; // -------------------

    // 自行处理上报
    public final static String V_ACT_QUESTION_DISPOSED = "QuestionDisposed"; // -------------------
    // 轨迹上报
    public final static String V_ACT_LOCATION_REPORT = "SubmitTrack";

    // 问题自处理
    public final static String WENTIZHICHULI = "CaseSelf";

    // 行政审批列表
    public final static String V_ACT_ADMINI_APPROVE_LIST = "AdminiApproveList";

    // 行政审批详情
    public final static String V_ACT_ADMINI_APPROVE_DETAIL = "GetAdminiApprove";

    // 获取事件分类信息
    public final static String V_ACT_ADMINI_GET_ALLTYPE = "GetAllTypeNew";
    public final static String V_ACT_ADMINI_EVENT_TYPE = "eventtype";

    /**
     * 行为 json head值 end
     */

    /**
     * *HISTORY BTGIN
     */
    public final static String V_ACT_QUESTION_HISTORY_TYPE_REPORT = "sb";
    public final static String V_ACT_QUESTION_HISTORY_TYPE_EXMINE = "hc";
    public final static String V_ACT_QUESTION_HISTORY_TYPE_VERIFY = "hs";
    public final static String V_ACT_QUESTION_HISTORY_TYPE_DISPOSE = "bl";
    public final static int V_ACT_QUESTION_HISTORY_LIST_MAX = 10;
    /**
     * *HISTORY END
     */

    /**
     * @category Login json 字段名 begin
     */
    // request
    public final static String S_USER_INFO = "uInfo";
    public final static String S_AUTO_LOGIN = "autoLogin";
    // 用户名
    public final static String R_USER_REQUEST_USERNAME_NAME = "LoginName";
    // 密码
    public final static String R_USER_REQUEST_PASSCODE = "LoginPassWord";
    /*
     * response
     */
    // 登录成功
    public final static String R_USER_RESPONSE_KEY = "isSuccess";
    // 返回消息
    public final static String R_USER_RESPONSE_MSG = "message";
    // 权限
    public final static String R_USER_RESPONSE_LIMIT = "Limit";
    // 管理网格
    public final static String R_USER_RESPONSE_GRIDINFO = "GridInfo";
    /**
     * Login json 字段名 end
     */

    /**
     * @category 问题上报 json 字段名 begin
     */
    // 电话
    public final static String R_REPORT_RESPONSE_CASES_NUM = "casesNum";
    // 电话
    public final static String R_REPORT_RESPONSE_TEL = "ComplainterTel";
    // 姓名
    public final static String R_REPORT_RESPONSE_NEME = "ComplainterName";
    // 居住地址
    public final static String R_REPORT_RESPONSE_LIVE_ADRES = "ComplainterAddress";
    // 登录帐号
    public final static String R_REPORT_RESPONSE_USER_NAME = "ACCEPTUSERID";
    // 选择事项
    public final static String R_REPORT_RESPONSE_TYPE = "CASEITEM";
    // 简要概述
    public final static String R_REPORT_RESPONSE_ALL_TYPE = "CASETITLE";
    // 详细描述
    public final static String R_REPORT_RESPONSE_DESCRIPT = "CASEDESCRIPTION";
    // 地址
    public final static String R_REPORT_RESPONSE_ADRES = "CASEADDRESS";
    // x坐标
    public final static String R_REPORT_RESPONSE_X = "X";
    // y坐标
    public final static String R_REPORT_RESPONSE_Y = "Y";
    // 网格编号
    public final static String R_REPORT_RESPONSE_GRID_CODE = "GridCode";
    // 图片数组
    public final static String R_REPORT_RESPONSE_FLIST = "FList";
    // 图片名
    public final static String R_REPORT_RESPONSE_FNAME = "FName";
    // 图片数据
    public final static String R_REPORT_RESPONSE_FDATA = "FData";
    // 图片类型
    public final static String R_REPORT_RESPONSE_FTYPE = "FType";
    /**
     * 问题上报 json 字段名 end
     *
     */

    public final static String OBJECTID = "OBJECTID";
    public final static String OBJCODE = "OBJCODE";
    public final static String OBJNAME = "OBJNAME";
    public final static String DEPTCODE1 = "DEPTCODE1";
    public final static String DEPTNAME1 = "DEPTNAME1";
    public final static String DEPTCODE2 = "DEPTCODE2";
    public final static String DEPTNAME2 = "DEPTNAME2";
    public final static String DEPTCODE3 = "DEPTCODE3";
    public final static String DEPTNAME3 = "DEPTNAME3";
    public final static String EXTEND4 = "EXTEND4";

    /**
     * @category 确认核查详细 json 字段名 begin
     */
    public final static String R_EXAMINE_REQUEST_CASE_ID = "CaseID";

    /**
     * 确认核查详细 json 字段名 end
     */

    /**
     * @category 确认核查 json 字段名 begin
     */
    // 1==通过/ 0==不通过
    public final static String R_EXAMINE_REQUEST_IS_OK = "IsOK";

    public final static String R_EXAMINE_REQUEST_INSPECT_ID = "InspectID";

    public final static String R_EXAMINE_REQUEST_VERIFYEED_BACK_CONTENT = "VerifyeedBackContent";

    public final static String R_EXAMINE_REQUEST_CASE_CODE = "CaseCode";

    public final static String R_EXAMINE_REQUEST_LOGIN_NAME = "LoginName";

    /**
     * 确认核查 json 字段名 end
     */

    /**
     * @category 待办理任务单个数据 json 字段名 begin
     */
    public final static String R_DISPOSE_REQUEST_CASE_ID2 = "CaseID";

    /**
     * 待办理任务单个数据 json 字段名 end
     */

    /**
     * @category 确认办理 json 字段名 begin
     */
    // 1==通过/ 0==不通过
    public final static String R_DISPOSE_REQUEST_IS_OK = "IsOK";

    public final static String R_DISPOSE_REQUEST_DEAL_RESULT = "DealResult";

    public final static String R_DISPOSE_REQUEST_TASK_ID = "TaskID";

    public final static String R_DISPOSE_REQUEST_HAND_ID = "HandID";

    public final static String R_DISPOSE_REQUEST_CASE_ID = "CaseID";

    public final static String R_DISPOSE_REQUEST_LOGIN_NAME = "LoginName";
    // 图片数组
    public final static String R_DISPOSE_REQUEST_FLIST = "FList";
    // 图片名
    public final static String R_DISPOSE_REQUEST_FNAME = "FName";
    // 图片数据
    public final static String R_DISPOSE_REQUEST_FDATA = "FData";
    // 图片类型
    public final static String R_DISPOSE_REQUEST_FTYPE = "FType";
    /**
     * 确认办理 json 字段名 end
     */

    /**
     * @category 待核实单个 json 字段名 begin
     */
    public final static String R_READY_VERIFY_CASE_ID = "CaseID";

    /**
     * 待核实单个 json 字段名 end
     */

    /**
     * @category 待核实提交 json 字段名 begin
     */
    // 1==通过/ 0==不通过
    public final static String R_READY_VERIFY_SUBMIT_IS_OK = "IsOK";

    public final static String R_READY_VERIFY_SUBMIT_INSPECT_ID = "InspectID";

    public final static String R_READY_VERIFY_SUBMIT_VERIFYEED_BACK_CONTENT = "VerifyeedBackContent";

    public final static String R_READY_VERIFY_SUBMIT_LOGIN_NAME = "LoginName";

    public final static String R_READY_VERIFY_SUBMIT_CASE_CODE = "CaseCode";
    // 图片数组
    public final static String R_READY_VERIFY_SUBMIT_FLIST = "FList";
    // 图片名
    public final static String R_READY_VERIFY_SUBMIT_FNAME = "FName";
    // 图片数据
    public final static String R_READY_VERIFY_SUBMIT_FDATA = "FData";
    // 图片类型
    public final static String R_READY_VERIFY_SUBMIT_FTYPE = "FType";

    /**
     * 待核实提交 json 字段名 end
     */

    /**
     * @category 历史列表 json 字段名 begin
     */
    public final static String R_READY_HISTORY_LIST_SEARCH_TYPE = "SearchType";

    public final static String R_READY_HISTORY_LIST_PAGE_INDEX = "PageIndex";

    public final static String R_READY_HISTORY_LIST_PAGE_SIZE = "PageSize";

    public final static String R_READY_HISTORY_LIST_RECORD_COUNT = "RecordCount";

    /**
     * 历史列表 json 字段名 end
     */

    /**
     * @category 历史单条 json 字段名 begin
     */
    public final static String R_READY_HISTORY_CASE_ID = "CaseID";
    // 行政审批ID
    public final static String R_READY_APPROVE_ADTIVE_ID = "ADTIVEID";
    /**
     * 历史列表 json 字段名 end
     */

    /**
     * @category 轨迹上传 json 字段名 begin
     */
    public final static String R_LOCAL_REQUEST_KEY = "Key";

    public final static String R_LOCAL_REQUEST_TYPE = "Type";

    public final static String R_LOCAL_REQUEST_X = "X";

    public final static String R_LOCAL_REQUEST_Y = "Y";

    /**
     * 轨迹上传 json 字段名 end
     */

    /**
     * @category 问题上报数据库暂存字段 begin
     */
    public final static String TABLE_NAME_QUESTION = "T_QUESTION";

    public final static String Q_QUESTION_ID = "Q_QUESTION_ID";

    public final static String Q_QUESTION_TYPE = "Q_QUESTION_TYPE";

    public final static String Q_QUESTION_TYPE1 = "Q_QUESTION_TYPE_1";

    public final static String Q_QUESTION_TYPE2 = "Q_QUESTION_TYPE_2";

    public final static String Q_QUESTION_LOCATION = "Q_QUESTION_LOCATION";

    public final static String Q_QUESTION_DESCRIPT = "Q_QUESTION_DESCRIPT";

    public final static String Q_QUESTION_BEFOR_IMG1 = "Q_QUESTION_BEFOR_IMG_1";

    public final static String Q_QUESTION_BEFOR_IMG2 = "Q_QUESTION_BEFOR_IMG_2";

    public final static String Q_QUESTION_BEFOR_IMG3 = "Q_QUESTION_BEFOR_IMG_3";

    public final static String Q_QUESTION_AFTER_IMG1 = "Q_QUESTION_AFTER_IMG_1";

    public final static String Q_QUESTION_AFTER_IMG2 = "Q_QUESTION_AFTER_IMG_2";

    public final static String Q_QUESTION_AFTER_IMG3 = "Q_QUESTION_AFTER_IMG_3";

    public final static String Q_QUESTION_REC1 = "Q_QUESTION_REC_1";

    public final static String Q_QUESTION_REC2 = "Q_QUESTION_REC_2";

    public final static String Q_QUESTION_REC3 = "Q_QUESTION_REC_3";

    public final static String Q_QUESTION_CASESNUM = "Q_QUESTION_CASESNUM";
    /**
     * 问题上报数据库暂存字段 end
     */
    public final static String TypeVison = "TypeVison";

    // 联系人
    public final static String GetLinkManInfo = "GetLinkManInfo";
}
