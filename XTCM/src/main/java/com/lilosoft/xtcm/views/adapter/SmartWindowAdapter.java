package com.lilosoft.xtcm.views.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.instantiation.AdminApproveBean;
import com.lilosoft.xtcm.instantiation.ExamineListBean;
import com.lilosoft.xtcm.instantiation.HistoryDisposeListBean;
import com.lilosoft.xtcm.instantiation.HistoryExamineListBean;
import com.lilosoft.xtcm.instantiation.HistoryReportListBean;
import com.lilosoft.xtcm.instantiation.HistoryVerifyListBean;
import com.lilosoft.xtcm.instantiation.ReadyDisposeBean;
import com.lilosoft.xtcm.instantiation.VerifyListBean;

/***
 *
 * @category 元素适配器
 * @author William Liu
 *
 */
public class SmartWindowAdapter extends SimpleAdapter {

    /**
     * @category content
     */
    private Context context;

    /**
     * @category 数据集
     */
    private List<? extends Map<String, ?>> data;

    /**
     * @category 目标
     */
    private int resource;

    /**
     * @category 元素
     */
    private int[] to;

    /**
     * @category 构造方法
     * @param context
     * @param data
     * @param resource
     * @param from
     * @param to
     */
    public SmartWindowAdapter(Context context,
                              List<? extends Map<String, ?>> data, int resource, String[] from,
                              int[] to) {
        super(context, data, resource, from, to);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.resource = resource;
        this.to = to;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ExamineHolder examineHolder = null;
        ReadyDisposeHolder readyDisposeHolder = null;
        ReadyVerifyHolder readyVerifyHolder = null;
        HistoryReportHolder historyReportHolder = null;
        HistoryExamineHolder historyExamineHolder = null;
        HistoryDisposeHolder historyDisposeHolder = null;
        HistoryVerifyHolder historyVerifyHolder = null;
        HistoryAdminApproveHolder historyAdminApproveHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
            switch (resource) {
                case R.layout.view_examine_item:
                    examineHolder = new ExamineHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]),
                            (TextView) convertView.findViewById(to[9]),
                            (TextView) convertView.findViewById(to[10]),
                            (TextView) convertView.findViewById(to[11]));
                    convertView.setTag(examineHolder);
                    break;
                case R.layout.view_ready_dispose_item:
                    readyDisposeHolder = new ReadyDisposeHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]),
                            (TextView) convertView.findViewById(to[9]),
                            (TextView) convertView.findViewById(to[10]),
                            (TextView) convertView.findViewById(to[11]),
                            (TextView) convertView.findViewById(to[12]),
                            (TextView) convertView.findViewById(to[13]),
                            (TextView) convertView.findViewById(to[14]));

                    // ReadyDisposeBean e1 = (ReadyDisposeBean)
                    // data.get(position).get(
                    // "data");
                    // if (e1.getCASESTATUS().equals("421")
                    // || e1.getCASESTATUS().equals("411")) {
                    // ((TextView)
                    // convertView.findViewById(to[13])).setTextColor(Color.RED);
                    // ((TextView)
                    // convertView.findViewById(R.id.q_small_casetitle)).setTextColor(Color.RED);
                    // }
                    convertView.setTag(readyDisposeHolder);

                    break;
                case R.layout.view_ready_verify_item:
                    readyVerifyHolder = new ReadyVerifyHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]),
                            (TextView) convertView.findViewById(to[9]),
                            (TextView) convertView.findViewById(to[10]),
                            (TextView) convertView.findViewById(to[11]),
                            (TextView) convertView.findViewById(to[12]),
                            (TextView) convertView.findViewById(to[13]),
                            (TextView) convertView.findViewById(to[14]),
                            (TextView) convertView.findViewById(to[15]),
                            (TextView) convertView.findViewById(to[16]),
                            (TextView) convertView.findViewById(to[17]));
                    convertView.setTag(readyVerifyHolder);
                    break;
                case R.layout.view_history_report_item:
                    historyReportHolder = new HistoryReportHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]));
                    convertView.setTag(historyReportHolder);
                    break;
                case R.layout.view_history_examine_item:
                    historyExamineHolder = new HistoryExamineHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]),
                            (TextView) convertView.findViewById(to[9]),
                            (TextView) convertView.findViewById(to[10]),
                            (TextView) convertView.findViewById(to[11]));
                    convertView.setTag(historyExamineHolder);
                    break;
                case R.layout.view_history_dispose_item:
                    historyDisposeHolder = new HistoryDisposeHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]),
                            (TextView) convertView.findViewById(to[3]),
                            (TextView) convertView.findViewById(to[4]),
                            (TextView) convertView.findViewById(to[5]),
                            (TextView) convertView.findViewById(to[6]),
                            (TextView) convertView.findViewById(to[7]),
                            (TextView) convertView.findViewById(to[8]),
                            (TextView) convertView.findViewById(to[9]),
                            (TextView) convertView.findViewById(to[10]),
                            (TextView) convertView.findViewById(to[11]),
                            (TextView) convertView.findViewById(to[12]),
                            (TextView) convertView.findViewById(to[13]),
                            (TextView) convertView.findViewById(to[14]));
                    convertView.setTag(historyDisposeHolder);
                    break;
                case R.layout.view_history_verify_item:
                    historyVerifyHolder = new HistoryVerifyHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]),
                            (TextView) convertView.findViewById(to[2]));
                    convertView.setTag(historyVerifyHolder);
                    break;
                case R.layout.view_admin_approve_item:
                    historyAdminApproveHolder = new HistoryAdminApproveHolder(
                            (TextView) convertView.findViewById(to[0]),
                            (TextView) convertView.findViewById(to[1]));
                    convertView.setTag(historyAdminApproveHolder);
                    break;
            }
        } else {
            switch (resource) {
                case R.layout.view_examine_item:
                    examineHolder = (ExamineHolder) convertView.getTag();
                    break;
                case R.layout.view_ready_dispose_item:
                    readyDisposeHolder = (ReadyDisposeHolder) convertView.getTag();
                    break;
                case R.layout.view_ready_verify_item:
                    readyVerifyHolder = (ReadyVerifyHolder) convertView.getTag();
                    break;
                case R.layout.view_history_report_item:
                    historyReportHolder = (HistoryReportHolder) convertView
                            .getTag();
                    break;
                case R.layout.view_history_examine_item:
                    historyExamineHolder = (HistoryExamineHolder) convertView
                            .getTag();
                    break;
                case R.layout.view_history_verify_item:
                    historyVerifyHolder = (HistoryVerifyHolder) convertView
                            .getTag();
                    break;
                case R.layout.view_history_dispose_item:
                    historyDisposeHolder = (HistoryDisposeHolder) convertView
                            .getTag();
                    break;
                case R.layout.view_admin_approve_item:
                    historyAdminApproveHolder = (HistoryAdminApproveHolder) convertView
                            .getTag();
                    break;
            }

        }
        switch (resource) {
            case R.layout.view_examine_item:
                ExamineListBean e = (ExamineListBean) data.get(position)
                        .get("data");
                examineHolder.even1.setText(e.getINSPECTID());
                examineHolder.even2.setText(e.getCASEID());
                examineHolder.even3.setText(e.getCASECODE());
                examineHolder.even4.setText(e.getCASESTATUS());
                examineHolder.even5.setText(e.getCASEPARENTITEM1());
                examineHolder.even6.setText(e.getCASEPARENTITEM2());
                examineHolder.even7.setText(e.getCASEITEM());
                examineHolder.even8.setText(e.getPUTONRECORDEXTENDEDTIME());
                examineHolder.even9.setText(e.getPUTONRECORDUSERID());
                examineHolder.even10.setText(e.getBEGINVERIFYTIME());
                examineHolder.even11.setText(e.getCASETITLE());
                examineHolder.even12.setText(e.getPUTONRECORDWARNINGTIME());
                break;

            case R.layout.view_ready_dispose_item:

                ReadyDisposeBean e1 = (ReadyDisposeBean) data.get(position).get(
                        "data");
                readyDisposeHolder.even1.setText(e1.getCASEID());
                readyDisposeHolder.even2.setText(e1.getTASKID());
                readyDisposeHolder.even3.setText(e1.getSIGNID());
                readyDisposeHolder.even4.setText(e1.getHANDLEID());
                readyDisposeHolder.even5.setText(e1.getCASECODE());
                readyDisposeHolder.even6.setText(e1.getDISPATCHTYPE());
                readyDisposeHolder.even7.setText(e1.getCASESTATUS());
                readyDisposeHolder.even8.setText(e1.getCASEPARENTITEM1());
                readyDisposeHolder.even9.setText(e1.getCASEPARENTITEM2());
                readyDisposeHolder.even10.setText(e1.getCASEITEM());
                readyDisposeHolder.even11.setText(e1.getDISPATCHTIME());
                readyDisposeHolder.even12.setText(e1.getDISPATCHPERSONID());
                readyDisposeHolder.even13.setText(e1.getDEALEXTENDEDTIME());
                readyDisposeHolder.even14.setText(e1.getCASETITLE());

                readyDisposeHolder.even15.setText(e1.getDEALWARMINGTIME());
                //e1.getCASESTATUS().equals("421")||e1.getCASESTATUS().equals("411")
//			if (position==1) {
//				readyDisposeHolder.even14.setTextColor(Color.RED);
//
//				Log.e("cao", "red" + position + "," + e1.getCASESTATUS());
//			}

                break;
            case R.layout.view_ready_verify_item:
                VerifyListBean e2 = (VerifyListBean) data.get(position).get("data");
                readyVerifyHolder.even1.setText(e2.getCASEID());
                readyVerifyHolder.even2.setText(e2.getTASKID());
                readyVerifyHolder.even3.setText(e2.getSIGNID());
                readyVerifyHolder.even4.setText(e2.getHANDLEID());
                readyVerifyHolder.even5.setText(e2.getVERIFICATIONID());
                readyVerifyHolder.even6.setText(e2.getINSPECTID());
                readyVerifyHolder.even7.setText(e2.getCASECODE());
                readyVerifyHolder.even8.setText(e2.getCASESTATUS());
                readyVerifyHolder.even9.setText(e2.getCASEPARENTITEM1());
                readyVerifyHolder.even10.setText(e2.getCASEPARENTITEM2());
                readyVerifyHolder.even11.setText(e2.getCASEITEM());
                readyVerifyHolder.even12.setText(e2.getSIGNDEPTCODE());
                readyVerifyHolder.even13.setText(e2.getVERIFYPERSON());
                readyVerifyHolder.even14.setText(e2.getBEGINVERIFYTIME());
                readyVerifyHolder.even15.setText(e2.getVERIFYPERSON1());
                readyVerifyHolder.even16.setText(e2.getDEALUSERID());
                readyVerifyHolder.even17.setText(e2.getCASETITLE());
                readyVerifyHolder.even18.setText(e2.getFEEDBACKDEALTIME());
                break;
            case R.layout.view_history_report_item:

                HistoryReportListBean e3 = (HistoryReportListBean) data.get(
                        position).get("data");
                historyReportHolder.even1.setText(e3.getID());
                historyReportHolder.even2.setText(e3.getCASECODE());
                historyReportHolder.even3.setText(e3.getCASESTATUS());
                historyReportHolder.even4.setText(e3.getCASEPARENTITEM1());
                historyReportHolder.even5.setText(e3.getCASEPARENTITEM2());
                historyReportHolder.even6.setText(e3.getCASEITEM());
                historyReportHolder.even7.setText(e3.getCOMPLAINANTERID());
                // historyReportHolder.even8.setText(e3.getCASETITLE());
                historyReportHolder.even8.setText(e3.getCASEDESCRIPTION());// 上报描述
                historyReportHolder.even9.setText(e3.getCREATETIME());
                break;
            case R.layout.view_history_examine_item:

                HistoryExamineListBean e4 = (HistoryExamineListBean) data.get(
                        position).get("data");
                historyExamineHolder.even1.setText(e4.getCASEID());
                historyExamineHolder.even2.setText(e4.getINSPECTID());
                historyExamineHolder.even3.setText(e4.getCASECODE());
                historyExamineHolder.even4.setText(e4.getCASESTATUS());
                historyExamineHolder.even5.setText(e4.getCASEPARENTITEM1());
                historyExamineHolder.even6.setText(e4.getCASEPARENTITEM2());
                historyExamineHolder.even7.setText(e4.getCASEITEM());
                historyExamineHolder.even8.setText(e4.getVERIFYPERSON());
                historyExamineHolder.even9.setText(e4.getISPASSVERIFY());
                historyExamineHolder.even10.setText(e4.getFINISHVERIFYTIME());
                // historyExamineHolder.even11.setText(e4.getCASETITLE());
                historyExamineHolder.even11.setText(e4.getCASEDESCRIPTION());
                historyExamineHolder.even12.setText(e4.getBEGINVERIFYTIME());
                break;
            case R.layout.view_history_verify_item:
                HistoryVerifyListBean e5 = (HistoryVerifyListBean) data.get(
                        position).get("data");
                historyVerifyHolder.even1.setText(e5.getCASEID());
                // historyVerifyHolder.even2.setText(e5.getCASETITLE());
                historyVerifyHolder.even2.setText(e5.getCASEDESCRIPTION());
                historyVerifyHolder.even3.setText(e5.getFINISHVERIFYTIME());
                break;
            case R.layout.view_history_dispose_item:
                HistoryDisposeListBean e6 = (HistoryDisposeListBean) data.get(
                        position).get("data");
                historyDisposeHolder.even1.setText(e6.getCASEID());
                historyDisposeHolder.even2.setText(e6.getTASKID());
                historyDisposeHolder.even3.setText(e6.getSIGNID());
                historyDisposeHolder.even4.setText(e6.getHANDLEID());
                historyDisposeHolder.even5.setText(e6.getCASECODE());
                historyDisposeHolder.even6.setText(e6.getCASESTATUS());
                historyDisposeHolder.even7.setText(e6.getCASEPARENTITEM1());
                historyDisposeHolder.even8.setText(e6.getCASEPARENTITEM2());
                historyDisposeHolder.even9.setText(e6.getCASEITEM());
                historyDisposeHolder.even10.setText(e6.getDISPATCHTYPE());
                historyDisposeHolder.even11.setText(e6.getDISPATCHPERSONID());
                historyDisposeHolder.even12.setText(e6.getFEEDBACKDEALTIME());
                historyDisposeHolder.even13.setText(e6.getRN());
                historyDisposeHolder.even14.setText(e6.getCASEDESCRIPTION());
                historyDisposeHolder.even15.setText(e6.getDISPATCHTIME());
                break;
            case R.layout.view_admin_approve_item:
                AdminApproveBean e7 = (AdminApproveBean) data.get(position).get(
                        "data");
                // historyDisposeHolder.even1.setText(e7.getADTIVETITLE());
                // historyDisposeHolder.even2.setText(e7.getADTIVEDATA());
                historyAdminApproveHolder.even1.setText(e7.getADTIVETITLE());
                historyAdminApproveHolder.even2.setText(e7.getADTIVEDATA());
                break;

            default:
                break;
        }

        return convertView;
    }

    /**
     * @category Examine子项内容
     * @author William Liu
     *
     */
    class ExamineHolder {

        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;
        TextView even10;
        TextView even11;
        TextView even12;

        public ExamineHolder(TextView even1, TextView even2, TextView even3,
                             TextView even4, TextView even5, TextView even6, TextView even7,
                             TextView even8, TextView even9, TextView even10,
                             TextView even11, TextView even12) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
            this.even10 = even10;
            this.even11 = even11;
            this.even12 = even12;
        }
    }

    /**
     * @category Examine子项内容
     * @author William Liu
     *
     */
    class ReadyDisposeHolder {

        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;
        TextView even10;
        TextView even11;
        TextView even12;
        TextView even13;
        TextView even14;
        TextView even15;

        public ReadyDisposeHolder(TextView even1, TextView even2,
                                  TextView even3, TextView even4, TextView even5, TextView even6,
                                  TextView even7, TextView even8, TextView even9,
                                  TextView even10, TextView even11, TextView even12,
                                  TextView even13, TextView even14, TextView even15) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
            this.even10 = even10;
            this.even11 = even11;
            this.even12 = even12;
            this.even13 = even13;
            this.even14 = even14;
            this.even15 = even15;
        }
    }

    /**
     * @category Verify子项内容
     * @author William Liu
     *
     */
    class ReadyVerifyHolder {

        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;
        TextView even10;
        TextView even11;
        TextView even12;
        TextView even13;
        TextView even14;
        TextView even15;
        TextView even16;
        TextView even17;
        TextView even18;

        public ReadyVerifyHolder(TextView even1, TextView even2,
                                 TextView even3, TextView even4, TextView even5, TextView even6,
                                 TextView even7, TextView even8, TextView even9,
                                 TextView even10, TextView even11, TextView even12,
                                 TextView even13, TextView even14, TextView even15,
                                 TextView even16, TextView even17, TextView even18) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
            this.even10 = even10;
            this.even11 = even11;
            this.even12 = even12;
            this.even13 = even13;
            this.even14 = even14;
            this.even15 = even15;
            this.even16 = even16;
            this.even17 = even17;
            this.even18 = even18;
        }
    }

    /**
     * @category 历史Report子项内容
     * @author William Liu
     *
     */
    class HistoryReportHolder {

        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;

        public HistoryReportHolder(TextView even1, TextView even2,
                                   TextView even3, TextView even4, TextView even5, TextView even6,
                                   TextView even7, TextView even8, TextView even9) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
        }

    }

    /**
     * @category 历史Exmine子项内容
     * @author William Liu
     *
     */
    class HistoryExamineHolder {

        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;
        TextView even10;
        TextView even11;
        TextView even12;

        public HistoryExamineHolder(TextView even1, TextView even2,
                                    TextView even3, TextView even4, TextView even5, TextView even6,
                                    TextView even7, TextView even8, TextView even9,
                                    TextView even10, TextView even11, TextView even12) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
            this.even10 = even10;
            this.even11 = even11;
            this.even12 = even12;
        }

    }

    /**
     * @category 历史Verify子项内容
     * @author William Liu
     *
     */
    class HistoryVerifyHolder {

        TextView even1;
        TextView even2;
        TextView even3;

        public HistoryVerifyHolder(TextView even1, TextView even2,
                                   TextView even3) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
        }
    }

    /**
     * @category 历史Dispose子项内容
     * @author William Liu
     *
     */
    class HistoryDisposeHolder {
        TextView even1;
        TextView even2;
        TextView even3;
        TextView even4;
        TextView even5;
        TextView even6;
        TextView even7;
        TextView even8;
        TextView even9;
        TextView even10;
        TextView even11;
        TextView even12;
        TextView even13;
        TextView even14;
        TextView even15;

        public HistoryDisposeHolder(TextView even1, TextView even2,
                                    TextView even3, TextView even4, TextView even5, TextView even6,
                                    TextView even7, TextView even8, TextView even9,
                                    TextView even10, TextView even11, TextView even12,
                                    TextView even13, TextView even14, TextView even15) {

            this.even1 = even1;
            this.even2 = even2;
            this.even3 = even3;
            this.even4 = even4;
            this.even5 = even5;
            this.even6 = even6;
            this.even7 = even7;
            this.even8 = even8;
            this.even9 = even9;
            this.even10 = even10;
            this.even11 = even11;
            this.even12 = even12;
            this.even13 = even13;
            this.even14 = even14;
            this.even15 = even15;
        }
    }

    /**
     * @category 行政审查子项内容
     * @author William Liu
     *
     */
    class HistoryAdminApproveHolder {

        TextView even1;
        TextView even2;

        public HistoryAdminApproveHolder(TextView even1, TextView even2) {

            this.even1 = even1;
            this.even2 = even2;
        }
    }

}
