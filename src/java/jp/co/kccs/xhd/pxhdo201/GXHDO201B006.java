/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo201;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.SelectParam;
import jp.co.kccs.xhd.common.ColumnInfoParser;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO201B006Model;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/04/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2019/09/20<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * 変更日        2022/03/10<br>
 * 計画書No      MB2202-D013<br>
 * 変更者        KCSS E.Ryu<br>
 * 変更理由      項目追加・変更<br>
 * <br>
 * 変更日	2022/06/14<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷積層・RHAPS履歴検索画面
 *
 * @author KCCS D.Yanagida
 * @since 2019/04/08
 */
@Named
@ViewScoped
public class GXHDO201B006 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B006.class.getName());
    
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B006Model> listData = null;
    
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 1ページ当たりの表示件数 */
    private int listDisplayPageCount = 30;
    
    /** 検索条件：ロットNo */
    private String lotNo = "";
    /** 検索条件：KCPNo */
    private String kcpNo = "";
    /** 検索条件：型式 */
    private String katashiki = "";
    /** 検索条件：容量 */
    private String yoryo = "";
    /** 検索条件：印刷開始日(FROM) */
    private String startDateF = "";
    /** 検索条件：印刷開始日(TO) */
    private String startDateT = "";
    /** 検索条件：印刷開始時刻(FROM) */
    private String startTimeF = "";
    /** 検索条件：印刷開始時刻(TO) */
    private String startTimeT = "";
    /** 検索条件：印刷終了日(FROM) */
    private String endDateF = "";
    /** 検索条件：印刷終了日(TO) */
    private String endDateT = "";
    /** 検索条件：印刷終了時刻(FROM) */
    private String endTimeF = "";
    /** 検索条件：印刷終了時刻(TO) */
    private String endTimeT = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B006() {
    }

//<editor-fold defaultstate="collapsed" desc="#getter setter">
    /**
     * 警告メッセージ
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }
    
    /**
     * 一覧表示データ取得
     * @return 一覧表示データ
     */
    public List<GXHDO201B006Model> getListData() {
        return listData;
    }
    
    /**
     * 検索条件：ロットNo
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：ロットNo
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 検索条件：KCPNo
     * @return the kcpNo
     */
    public String getKcpNo() {
        return kcpNo;
    }

    /**
     * 検索条件：KCPNo
     * @param kcpNo the kcpNo to set
     */
    public void setKcpNo(String kcpNo) {
        this.kcpNo = kcpNo;
    }

    /**
     * 検索条件：型式
     * @return the katashiki
     */
    public String getKatashiki() {
        return katashiki;
    }

    /**
     * 検索条件：型式
     * @param katashiki the katashiki to set
     */
    public void setKatashiki(String katashiki) {
        this.katashiki = katashiki;
    }

    /**
     * 検索条件：容量
     * @return the yoryo
     */
    public String getYoryo() {
        return yoryo;
    }

    /**
     * 検索条件：容量
     * @param yoryo the yoryo to set
     */
    public void setYoryo(String yoryo) {
        this.yoryo = yoryo;
    }

    /**
     * 検索条件：印刷開始日(FROM)
     * @return the startDateF
     */
    public String getStartDateF() {
        return startDateF;
    }

    /**
     * 検索条件：印刷開始日(FROM)
     * @param startDateF the startDateF to set
     */
    public void setStartDateF(String startDateF) {
        this.startDateF = startDateF;
    }

    /**
     * 検索条件：印刷開始日(TO)
     * @return the startDateT
     */
    public String getStartDateT() {
        return startDateT;
    }

    /**
     * 検索条件：印刷開始日(TO)
     * @param startDateT the startDateT to set
     */
    public void setStartDateT(String startDateT) {
        this.startDateT = startDateT;
    }

    /**
     * 検索条件：印刷開始時刻(FROM)
     * @return the startTimeF
     */
    public String getStartTimeF() {
        return startTimeF;
    }

    /**
     * 検索条件：印刷開始時刻(FROM)
     * @param startTimeF the startTimeF to set
     */
    public void setStartTimeF(String startTimeF) {
        this.startTimeF = startTimeF;
    }

    /**
     * 検索条件：印刷開始時刻(TO)
     * @return the startTimeT
     */
    public String getStartTimeT() {
        return startTimeT;
    }

    /**
     * 検索条件：印刷開始時刻(TO)
     * @param startTimeT the startTimeT to set
     */
    public void setStartTimeT(String startTimeT) {
        this.startTimeT = startTimeT;
    }

    /**
     * 検索条件：印刷終了日(FROM)
     * @return the endDateF
     */
    public String getEndDateF() {
        return endDateF;
    }

    /**
     * 検索条件：印刷終了日(FROM)
     * @param endDateF the endDateF to set
     */
    public void setEndDateF(String endDateF) {
        this.endDateF = endDateF;
    }

    /**
     * 検索条件：印刷終了日(TO)
     * @return the endDateT
     */
    public String getEndDateT() {
        return endDateT;
    }

    /**
     * 検索条件：印刷終了日(TO)
     * @param endDateT the endDateT to set
     */
    public void setEndDateT(String endDateT) {
        this.endDateT = endDateT;
    }

    /**
     * 検索条件：印刷終了時刻(FROM)
     * @return the endTimeF
     */
    public String getEndTimeF() {
        return endTimeF;
    }

    /**
     * 検索条件：印刷終了時刻(FROM)
     * @param endTimeF the endTimeF to set
     */
    public void setEndTimeF(String endTimeF) {
        this.endTimeF = endTimeF;
    }

    /**
     * 検索条件：印刷終了時刻(TO)
     * @return the endTimeT
     */
    public String getEndTimeT() {
        return endTimeT;
    }

    /**
     * 検索条件：印刷終了時刻(TO)
     * @param endTimeT the endTimeT to set
     */
    public void setEndTimeT(String endTimeT) {
        this.endTimeT = endTimeT;
    }
//</editor-fold>
    
    /**
     * ページング用の件数を返却
     * @return 1ページあたりの表示件数
     */
    public String getPagenatorCount() {
        if (listDisplayPageCount > 0) {
            return String.valueOf(listDisplayPageCount);
        } else {
            return "";
        }
    }

    /**
     * 画面起動時処理
     */
    public void init() {
        // セッション情報から画面パラメータを取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }
        
        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B006_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B006_display_page_count", session));
        }
        
        // 画面クリア
        clear();
    }
    
    /**
     * Excel保存ボタン非活性制御
     * @return 一覧データが表示しない場合true
     */
    public String getExcelButtonDisable() {
        if (isExistListData()) {
            return "false";
        } else {
            return "true";
        }
    }
    
    /**
     * 画面クリア
     */
    public void clear() {
        lotNo = "";
        kcpNo = "";
        katashiki = "";
        yoryo = "";
        startDateF = "";
        startDateT = "";
        startTimeF = "";
        startTimeT = "";
        endDateF = "";
        endDateT = "";
        endTimeF = "";
        endTimeT = "";
        
        listData = new ArrayList<>();
    }
           
    /**
     * 入力値チェック：
     * 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        
        // ロットNo
        if (!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)) {
            // エラー対象をリストに追加
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        if (!StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
            return;
        }
        // KCPNO
        if (existError(validateUtil.checkC103(getKcpNo(), "KCPNO", 25))) {
            return;
        }
        // 型式
        if (existError(validateUtil.checkC101(getKatashiki(), "型式", 2))) {
            return;
        }
        // 容量
        if (existError(validateUtil.checkC101(getYoryo(), "容量", 4))) {
            return;
        }
        // 開始日(FROM)
        if (existError(validateUtil.checkC101(getStartDateF(), "開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateF(), "開始日(from)")) ||
            existError(validateUtil.checkC501(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始日(TO)
        if (existError(validateUtil.checkC101(getStartDateT(), "開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateT(), "開始日(to)")) ||
            existError(validateUtil.checkC501(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了日(FROM)
        if (existError(validateUtil.checkC101(getEndDateF(), "終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateF(), "終了日(from)")) ||
            existError(validateUtil.checkC501(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了日(TO)
        if (existError(validateUtil.checkC101(getEndDateT(), "終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateT(), "終了日(to)")) ||
            existError(validateUtil.checkC501(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeF(), "開始時刻(from)")) ||
            existError(validateUtil.checkC502(getStartTimeF(), "開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeT(), "開始時刻(to)")) ||
            existError(validateUtil.checkC502(getStartTimeT(), "開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了時刻(FROM)
        if (existError(validateUtil.checkC101(getEndTimeF(), "終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeF(), "終了時刻(from)")) ||
            existError(validateUtil.checkC502(getEndTimeF(), "終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeF) && existError(validateUtil.checkC001(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了時刻(TO)
        if (existError(validateUtil.checkC101(getEndTimeT(), "終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeT(), "終了時刻(to)")) ||
            existError(validateUtil.checkC502(getEndTimeT(), "終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeT) && existError(validateUtil.checkC001(getEndDateT(), "終了日(to)"))) {
            return;
        }
        
        // 一覧表示件数を取得
        long count = selectListDataCount();
        
        if (count == 0) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (listCountMax > 0 && count > listCountMax) {
            // 検索結果が上限件数以上の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000046", listCountMax), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } 
        
        if (listCountWarn > 0 && count > listCountWarn) {
            // 検索結果が警告件数以上の場合、警告ダイアログを表示する
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param1", "warning");
            
            warnMessage = String.format("検索結果が%d件を超えています。<br/>継続しますか?<br/>%d件", listCountWarn, count);
            return;
        }
        
        // 入力チェックでエラーが存在しない場合検索処理を実行する
        selectListData();
    }
    
    /**
     * 警告ダイアログで「OK」押下時
     */
    public void processWarnOk() {
        // 検索処理実行
        selectListData();
    }
    
    /**
     * 一覧表示データ件数取得
     * @return 検索結果件数
     */
    public long selectListDataCount() {
        long count = 0;
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(*) AS CNT "
                    + "FROM sr_rhaps "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 10, 4) = ?) "
                    + "AND   (? IS NULL OR KAISINICHIJI >= ?) "
                    + "AND   (? IS NULL OR KAISINICHIJI <= ?) "
                    + "AND   (? IS NULL OR SYURYONICHIJI >= ?) "
                    + "AND   (? IS NULL OR SYURYONICHIJI <= ?) ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long)result.get("CNT");
            
        } catch (SQLException ex) {
            count = 0;
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        
        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT CONCAT(IFNULL(T1.KOJYO, ''), IFNULL(T1.LOTNO, ''), IFNULL(T1.EDABAN, '')) AS LOTNO"
                    + ", T1.KCPNO"
                    + ", T1.KAISINICHIJI"
                    + ", T1.SYURYONICHIJI"
                    + ", T1.TTAPESYURUI"
                    + ", T1.TTAPELOTNO"
                    + ", T1.TTapeSlipKigo"
                    + ", T1.TTapeRollNo1"
                    + ", T1.TTapeRollNo2"
                    + ", T1.TTapeRollNo3"
                    + ", T1.TTapeRollNo4"
                    + ", T1.TTapeRollNo5"
                    + ", T1.SITATTAPELOTNO"
                    + ", T1.SITATTapeSlipKigo"
                    + ", T1.SITATTapeRollNo1"
                    + ", T1.SITATTapeRollNo2"
                    + ", T1.SITATTapeRollNo3"
                    + ", T1.SITATTapeRollNo4"
                    + ", T1.SITATTapeRollNo5"
                    + ", T1.TGENRYOKIGO"
                    + ", T1.STSIYO"
                    + ", T1.ESEKISOSIYO"
                    + ", T1.ETAPESYURUI"
                    + ", T1.ETAPEGLOT"
                    + ", T1.ETAPELOT"
                    + ", T1.ETapeSlipKigo"
                    + ", T1.ETapeRollNo1"
                    + ", T1.ETapeRollNo1kaishi"
                    + ", T1.ETapeRollNo1syuryou"
                    + ", T1.ETapeRollNo1insatsu"
                    + ", T1.ETapeRollNo2"
                    + ", T1.ETapeRollNo2kaishi"
                    + ", T1.ETapeRollNo2syuryou"
                    + ", T1.ETapeRollNo2insatsu"
                    + ", T1.ETapeRollNo3"
                    + ", T1.ETapeRollNo3kaishi"
                    + ", T1.ETapeRollNo3syuryou"
                    + ", T1.ETapeRollNo3insatsu"
                    + ", T1.ETapeRollNo4"
                    + ", T1.ETapeRollNo4kaishi"
                    + ", T1.ETapeRollNo4syuryou"
                    + ", T1.ETapeRollNo4insatsu"
                    + ", T1.ETapeRollNo5"
                    + ", T1.ETapeRollNo5kaishi"
                    + ", T1.ETapeRollNo5syuryou"
                    + ", T1.ETapeRollNo5insatsu"
                    + ", T1.SPTUDENJIKAN"
                    + ", T1.SKAATURYOKU"
                    + ", T1.SKHEADNO"
                    + ", T1.SUSSKAISUU"
                    + ", T1.ECPASTEMEI"
                    + ", T1.EPASTELOTNO"
                    + ", T1.EPASTENENDO"
                    + ", T1.EPASTEONDO"
                    + ", T1.ESEIHANMEI"
                    + ", T1.buzaizaikonodenkyoku"
                    + ", T1.ESEIHANNO"
                    + ", T1.ESEIMAISUU"
                    + ", T1.saidaisyorisuudenkyoku"
                    + ", T1.ruikeisyorisuudenkyoku"
                    + ", T1.ECLEARANCE"
                    + ", T1.ESAATU"
                    + ", T1.ESKEEGENO"
                    + ", T1.ESKMAISUU"
                    + ", T1.ESKSPEED"
                    + ", T1.ESCCLEARANCE"
                    + ", T1.ESKKMJIKAN"
                    + ", T1.ELDSTART"
                    + ", T1.ESEIMENSEKI"
                    + ", T1.EMAKUATU"
                    + ", T1.ESLIDERYO"
                    + ", T1.EKANSOONDO"
                    + ", T1.EKANSOJIKAN"
                    + ", T1.CPASTELOTNO"
                    + ", T1.CPASTENENDO"
                    + ", T1.CPASTEONDO"
                    + ", T1.CSEIHANMEI"
                    + ", T1.buzaizaikonoyuudentai"
                    + ", T1.CSEIHANNO"
                    + ", T1.CSEIMAISUU"
                    + ", T1.saidaisyorisuuyuudentai"
                    + ", T1.ruikeisyorisuuyuudentai"
                    + ", T1.CCLEARANCE"
                    + ", T1.CSAATU"
                    + ", T1.CSKEEGENO"
                    + ", T1.CSKMAISUU"
                    + ", T1.CSCCLEARANCE"
                    + ", T1.CSKKMJIKAN"
                    + ", T1.CSHIFTINSATU"
                    + ", T1.CLDSTART"
                    + ", T1.CSEIMENSEKI"
                    + ", T1.CSLIDERYO"
                    + ", T1.CKANSOONDO"
                    + ", T1.CKANSOJIKAN"
                    + ", T1.CMAKUATU"
                    + ", T1.AINSATUSRZ1"
                    + ", T1.AINSATUSRZ2"
                    + ", T1.AINSATUSRZ3"
                    + ", T1.AINSATUSRZ4"
                    + ", T1.AINSATUSRZ5"
                    + ", T1.AINSATUSRZAVE"
                    + ", T1.UTSIYO"
                    + ", T1.UTTUDENJIKAN"
                    + ", T1.UTKAATURYOKU"
                    + ", T1.STAHOSEI"
                    + ", T1.TICLEARANCE"
                    + ", T1.TISAATU"
                    + ", T1.TISKSPEED"
                    + ", T1.FSTHUX1"
                    + ", T1.FSTHUX2"
                    + ", T1.FSTHUY1"
                    + ", T1.FSTHUY2"
                    + ", T1.FSTHSX1"
                    + ", T1.FSTHSX2"
                    + ", T1.FSTHSY1"
                    + ", T1.FSTHSY2"
                    + ", T1.FSTCX1"
                    + ", T1.FSTCX2"
                    + ", T1.FSTCY1"
                    + ", T1.FSTCY2"
                    + ", T1.FSTMUX1"
                    + ", T1.FSTMUX2"
                    + ", T1.FSTMUY1"
                    + ", T1.FSTMUY2"
                    + ", T1.FSTMSX1"
                    + ", T1.FSTMSX2"
                    + ", T1.FSTMSY1"
                    + ", T1.FSTMSY2"
                    + ", T1.LSTHUX1"
                    + ", T1.LSTHUX2"
                    + ", T1.LSTHUY1"
                    + ", T1.LSTHUY2"
                    + ", T1.LSTHSX1"
                    + ", T1.LSTHSX2"
                    + ", T1.LSTHSY1"
                    + ", T1.LSTHSY2"
                    + ", T1.LSTCX1"
                    + ", T1.LSTCX2"
                    + ", T1.LSTCY1"
                    + ", T1.LSTCY2"
                    + ", T1.LSTMUX1"
                    + ", T1.LSTMUX2"
                    + ", T1.LSTMUY1"
                    + ", T1.LSTMUY2"
                    + ", T1.LSTMSX1"
                    + ", T1.LSTMSX2"
                    + ", T1.LSTMSY1"
                    + ", T1.LSTMSY2"
                    + ", T1.seikeinagasa"
                    + ", T1.BIKO1"
                    + ", T1.BIKO2"
                    + ", T1.bikou3"
                    + ", T1.bikou4"
                    + ", T1.bikou5"
                    + ", T1.GOKI"
                    + ", T1.TTANSISUUU"
                    + ", T1.TTANSISUUS"
                    + ", T1.SHUNKANKANETSUJIKAN"
                    + ", T1.PETFILMSYURUI"
                    + ", T1.KAATURYOKU"
                    + ", (CASE WHEN T1.GAIKANKAKUNIN = 0 THEN 'NG' WHEN T1.GAIKANKAKUNIN = 1 THEN 'OK' ELSE NULL END) AS GAIKANKAKUNIN"
                    + ", T1.SEKIJSSKIRIKAEICHI"
                    + ", T1.SEKIKKSKIRIKAEICHI"
                    + ", T1.KAATUJIKAN"
                    + ", T1.TAPEHANSOUPITCH"
                    + ", (CASE WHEN T1.TAPEHANSOUKAKUNIN = 0 THEN 'NG' WHEN T1.TAPEHANSOUKAKUNIN = 1 THEN 'OK' ELSE NULL END) AS TAPEHANSOUKAKUNIN"
                    + ", T1.EMAKUATSUSETTEI"
                    + ", T1.ENEPPUFURYOU"
                    + ", T1.EMAKUATSUAVE"
                    + ", T1.EMAKUATSUMAX"
                    + ", T1.EMAKUATSUMIN"
                    + ", T1.NIJIMISOKUTEIPTN"
                    + ", (CASE WHEN T1.PRNSAMPLEGAIKAN = 0 THEN 'NG' WHEN T1.PRNSAMPLEGAIKAN = 1 THEN 'OK' ELSE NULL END) AS PRNSAMPLEGAIKAN"
                    + ", (CASE WHEN T1.PRNICHIYOHAKUNAGASA = 0 THEN 'NG' WHEN T1.PRNICHIYOHAKUNAGASA = 1 THEN 'OK' ELSE NULL END) AS PRNICHIYOHAKUNAGASA"
                    + ", T1.CTABLECLEARANCE"
                    + ", T1.CMAKUATSUSETTEI"
                    + ", T1.CSKSPEED"
                    + ", T1.CNEPPUFURYOU"
                    + ", (CASE WHEN T1.KABURIRYOU = 0 THEN 'NG' WHEN T1.KABURIRYOU = 1 THEN 'OK' ELSE NULL END) AS KABURIRYOU"
                    + ", (CASE WHEN T1.SGAIKAN = 0 THEN 'NG' WHEN T1.SGAIKAN = 1 THEN 'OK' ELSE NULL END) AS SGAIKAN"
                    + ", T1.NIJIMISOKUTEISEKISOUGO"
                    + ", (CASE WHEN T1.SEKISOUHINGAIKAN = 0 THEN 'NG' WHEN T1.SEKISOUHINGAIKAN = 1 THEN 'OK' ELSE NULL END) AS SEKISOUHINGAIKAN"
                    + ", (CASE WHEN T1.SEKISOUZURE = 0 THEN 'NG' WHEN T1.SEKISOUZURE = 1 THEN 'OK' ELSE NULL END) AS SEKISOUZURE"
                    + ", T1.UWAJSSKIRIKAEICHI"
                    + ", T1.SHITAKKSKIRIKAEICHI"
                    + ", T1.TINKSYURYUI"
                    + ", T1.TINKLOT"
                    + ", (CASE WHEN T1.TGAIKAN = 0 THEN 'NG' WHEN T1.TGAIKAN = 1 THEN 'OK' ELSE NULL END) AS TGAIKAN"
                    + ", T1.STARTTANTOU"
                    + ", T1.ENDTANTOU"
                    + ", T1.TENDDAY"
                    + ", T1.TENDTANTOU"
                    + ", T1.SYORISETSUU"
                    + ", T1.RYOUHINSETSUU"
                    + ", T1.HEADKOUKANTANTOU"
                    + ", T1.SEKISOUJOUKENTANTOU"
                    + ", T1.ESEIHANSETTANTOU"
                    + ", T1.CSEIHANSETTANTOU"
                    + ", T1.DANSASOKUTEITANTOU"
                    + ", T1.startkakunin"
                    + ", T1.TUMU"
                    + ", T2.EMAKUATSU1"
                    + ", T2.EMAKUATSU2"
                    + ", T2.EMAKUATSU3"
                    + ", T2.EMAKUATSU4"
                    + ", T2.EMAKUATSU5"
                    + ", T2.EMAKUATSU6"
                    + ", T2.EMAKUATSU7"
                    + ", T2.EMAKUATSU8"
                    + ", T2.EMAKUATSU9"
                    + ", T2.PTNDIST1"
                    + ", T2.PTNDIST2"
                    + ", T2.PTNDIST3"
                    + ", T2.PTNDIST4"
                    + ", T2.PTNDIST5"
                    + ", T2.AWASERZ1"
                    + ", T2.AWASERZ2"
                    + ", T2.AWASERZ3"
                    + ", T2.AWASERZ4"
                    + ", T2.AWASERZ5"
                    + ", T2.AWASERZ6"
                    + ", T2.AWASERZ7"
                    + ", T2.AWASERZ8"
                    + ", T2.AWASERZ9"
                    + ", T2.KABURIHIDARIUEX1"
                    + ", T2.KABURIHIDARIUEX2"
                    + ", T2.KABURIHIDARIUEY1"
                    + ", T2.KABURIHIDARIUEY2"
                    + ", T2.KABURIHIDARISITAX1"
                    + ", T2.KABURIHIDARISITAX2"
                    + ", T2.KABURIHIDARISITAY1"
                    + ", T2.KABURIHIDARISITAY2"
                    + ", T2.KABURIHIDARICENTERX1"
                    + ", T2.KABURIHIDARICENTERX2"
                    + ", T2.KABURIHIDARICENTERY1"
                    + ", T2.KABURIHIDARICENTERY2"
                    + ", T2.KABURIMIGIUEX1"
                    + ", T2.KABURIMIGIUEX2"
                    + ", T2.KABURIMIGIUEY1"
                    + ", T2.KABURIMIGIUEY2"
                    + ", T2.KABURIMIGISITAX1"
                    + ", T2.KABURIMIGISITAX2"
                    + ", T2.KABURIMIGISITAY1"
                    + ", T2.KABURIMIGISITAY2"
                    + " FROM sr_rhaps T1 "
                    + "LEFT JOIN sub_sr_rhaps T2 ON (T1.KOJYO = T2.KOJYO AND T1.LOTNO = T2.LOTNO AND T1.EDABAN = T2.EDABAN) "
                    + "WHERE (? IS NULL OR T1.KOJYO = ?) "
                    + "AND   (? IS NULL OR T1.LOTNO = ?) "
                    + "AND   (? IS NULL OR T1.EDABAN = ?) "
                    + "AND   (? IS NULL OR T1.KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(T1.KCPNO, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(T1.KCPNO, 10, 4) = ?) "
                    + "AND   (? IS NULL OR T1.KAISINICHIJI >= ?) "
                    + "AND   (? IS NULL OR T1.KAISINICHIJI <= ?) "
                    + "AND   (? IS NULL OR T1.SYURYONICHIJI >= ?) "
                    + "AND   (? IS NULL OR T1.SYURYONICHIJI <= ?) "
                    + "ORDER BY T1.KAISINICHIJI";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("LOTNO", "lotno");
            mapping.put("KCPNO", "kcpno");
            mapping.put("KAISINICHIJI", "kaisinichiji");
            mapping.put("SYURYONICHIJI", "syuryonichiji");
            mapping.put("TTAPESYURUI", "ttapesyurui");
            mapping.put("TTAPELOTNO", "ttapelotno");
            mapping.put("TTapeSlipKigo", "ttapeslipkigo");
            mapping.put("TTapeRollNo1", "ttaperollno1");
            mapping.put("TTapeRollNo2", "ttaperollno2");
            mapping.put("TTapeRollNo3", "ttaperollno3");
            mapping.put("TTapeRollNo4", "ttaperollno4");
            mapping.put("TTapeRollNo5", "ttaperollno5");
            mapping.put("SITATTAPELOTNO", "sitattapelotno");
            mapping.put("SITATTapeSlipKigo", "sitattapeslipkigo");
            mapping.put("SITATTapeRollNo1", "sitattaperollno1");
            mapping.put("SITATTapeRollNo2", "sitattaperollno2");
            mapping.put("SITATTapeRollNo3", "sitattaperollno3");
            mapping.put("SITATTapeRollNo4", "sitattaperollno4");
            mapping.put("SITATTapeRollNo5", "sitattaperollno5");
            mapping.put("TGENRYOKIGO", "tgenryokigo");
            mapping.put("STSIYO", "stsiyo");
            mapping.put("ESEKISOSIYO", "esekisosiyo");
            mapping.put("ETAPESYURUI", "etapesyurui");
            mapping.put("ETAPEGLOT", "etapeglot");
            mapping.put("ETAPELOT", "etapelot");
            mapping.put("ETapeSlipKigo", "etapeslipkigo");
            mapping.put("ETapeRollNo1", "etaperollno1");
            mapping.put("ETapeRollNo1kaishi", "etaperollno1kaishi");
            mapping.put("ETapeRollNo1syuryou", "etaperollno1syuryou");
            mapping.put("ETapeRollNo1insatsu", "etaperollno1insatsu");
            mapping.put("ETapeRollNo2", "etaperollno2");
            mapping.put("ETapeRollNo2kaishi", "etaperollno2kaishi");
            mapping.put("ETapeRollNo2syuryou", "etaperollno2syuryou");
            mapping.put("ETapeRollNo2insatsu", "etaperollno2insatsu");
            mapping.put("ETapeRollNo3", "etaperollno3");
            mapping.put("ETapeRollNo3kaishi", "etaperollno3kaishi");
            mapping.put("ETapeRollNo3syuryou", "etaperollno3syuryou");
            mapping.put("ETapeRollNo3insatsu", "etaperollno3insatsu");
            mapping.put("ETapeRollNo4", "etaperollno4");
            mapping.put("ETapeRollNo4kaishi", "etaperollno4kaishi");
            mapping.put("ETapeRollNo4syuryou", "etaperollno4syuryou");
            mapping.put("ETapeRollNo4insatsu", "etaperollno4insatsu");
            mapping.put("ETapeRollNo5", "etaperollno5");
            mapping.put("ETapeRollNo5kaishi", "etaperollno5kaishi");
            mapping.put("ETapeRollNo5syuryou", "etaperollno5syuryou");
            mapping.put("ETapeRollNo5insatsu", "etaperollno5insatsu");
            mapping.put("SPTUDENJIKAN", "sptudenjikan");
            mapping.put("SKAATURYOKU", "skaaturyoku");
            mapping.put("SKHEADNO", "skheadno");
            mapping.put("SUSSKAISUU", "susskaisuu");
            mapping.put("ECPASTEMEI", "ecpastemei");
            mapping.put("EPASTELOTNO", "epastelotno");
            mapping.put("EPASTENENDO", "epastenendo");
            mapping.put("EPASTEONDO", "epasteondo");
            mapping.put("ESEIHANMEI", "eseihanmei");
            mapping.put("buzaizaikonodenkyoku", "buzaizaikonodenkyoku");
            mapping.put("ESEIHANNO", "eseihanno");
            mapping.put("ESEIMAISUU", "eseimaisuu");
            mapping.put("saidaisyorisuudenkyoku", "saidaisyorisuudenkyoku");
            mapping.put("ruikeisyorisuudenkyoku", "ruikeisyorisuudenkyoku");
            mapping.put("ECLEARANCE", "eclearance");
            mapping.put("ESAATU", "esaatu");
            mapping.put("ESKEEGENO", "eskeegeno");
            mapping.put("ESKMAISUU", "eskmaisuu");
            mapping.put("ESKSPEED", "eskspeed");
            mapping.put("ESCCLEARANCE", "escclearance");
            mapping.put("ESKKMJIKAN", "eskkmjikan");
            mapping.put("ELDSTART", "eldstart");
            mapping.put("ESEIMENSEKI", "eseimenseki");
            mapping.put("EMAKUATU", "emakuatu");
            mapping.put("ESLIDERYO", "eslideryo");
            mapping.put("EKANSOONDO", "ekansoondo");
            mapping.put("EKANSOJIKAN", "ekansojikan");
            mapping.put("CPASTELOTNO", "cpastelotno");
            mapping.put("CPASTENENDO", "cpastenendo");
            mapping.put("CPASTEONDO", "cpasteondo");
            mapping.put("CSEIHANMEI", "cseihanmei");
            mapping.put("buzaizaikonoyuudentai", "buzaizaikonoyuudentai");
            mapping.put("CSEIHANNO", "cseihanno");
            mapping.put("CSEIMAISUU", "cseimaisuu");
            mapping.put("saidaisyorisuuyuudentai", "saidaisyorisuuyuudentai");
            mapping.put("ruikeisyorisuuyuudentai", "ruikeisyorisuuyuudentai");
            mapping.put("CCLEARANCE", "cclearance");
            mapping.put("CSAATU", "csaatu");
            mapping.put("CSKEEGENO", "cskeegeno");
            mapping.put("CSKMAISUU", "cskmaisuu");
            mapping.put("CSCCLEARANCE", "cscclearance");
            mapping.put("CSKKMJIKAN", "cskkmjikan");
            mapping.put("CSHIFTINSATU", "cshiftinsatu");
            mapping.put("CLDSTART", "cldstart");
            mapping.put("CSEIMENSEKI", "cseimenseki");
            mapping.put("CSLIDERYO", "cslideryo");
            mapping.put("CKANSOONDO", "ckansoondo");
            mapping.put("CKANSOJIKAN", "ckansojikan");
            mapping.put("CMAKUATU", "cmakuatu");
            mapping.put("AINSATUSRZ1", "ainsatusrz1");
            mapping.put("AINSATUSRZ2", "ainsatusrz2");
            mapping.put("AINSATUSRZ3", "ainsatusrz3");
            mapping.put("AINSATUSRZ4", "ainsatusrz4");
            mapping.put("AINSATUSRZ5", "ainsatusrz5");
            mapping.put("AINSATUSRZAVE", "ainsatusrzave");
            mapping.put("UTSIYO", "utsiyo");
            mapping.put("UTTUDENJIKAN", "uttudenjikan");
            mapping.put("UTKAATURYOKU", "utkaaturyoku");
            mapping.put("STAHOSEI", "stahosei");
            mapping.put("TICLEARANCE", "ticlearance");
            mapping.put("TISAATU", "tisaatu");
            mapping.put("TISKSPEED", "tiskspeed");
            mapping.put("FSTHUX1", "fsthux1");
            mapping.put("FSTHUX2", "fsthux2");
            mapping.put("FSTHUY1", "fsthuy1");
            mapping.put("FSTHUY2", "fsthuy2");
            mapping.put("FSTHSX1", "fsthsx1");
            mapping.put("FSTHSX2", "fsthsx2");
            mapping.put("FSTHSY1", "fsthsy1");
            mapping.put("FSTHSY2", "fsthsy2");
            mapping.put("FSTCX1", "fstcx1");
            mapping.put("FSTCX2", "fstcx2");
            mapping.put("FSTCY1", "fstcy1");
            mapping.put("FSTCY2", "fstcy2");
            mapping.put("FSTMUX1", "fstmux1");
            mapping.put("FSTMUX2", "fstmux2");
            mapping.put("FSTMUY1", "fstmuy1");
            mapping.put("FSTMUY2", "fstmuy2");
            mapping.put("FSTMSX1", "fstmsx1");
            mapping.put("FSTMSX2", "fstmsx2");
            mapping.put("FSTMSY1", "fstmsy1");
            mapping.put("FSTMSY2", "fstmsy2");
            mapping.put("LSTHUX1", "lsthux1");
            mapping.put("LSTHUX2", "lsthux2");
            mapping.put("LSTHUY1", "lsthuy1");
            mapping.put("LSTHUY2", "lsthuy2");
            mapping.put("LSTHSX1", "lsthsx1");
            mapping.put("LSTHSX2", "lsthsx2");
            mapping.put("LSTHSY1", "lsthsy1");
            mapping.put("LSTHSY2", "lsthsy2");
            mapping.put("LSTCX1", "lstcx1");
            mapping.put("LSTCX2", "lstcx2");
            mapping.put("LSTCY1", "lstcy1");
            mapping.put("LSTCY2", "lstcy2");
            mapping.put("LSTMUX1", "lstmux1");
            mapping.put("LSTMUX2", "lstmux2");
            mapping.put("LSTMUY1", "lstmuy1");
            mapping.put("LSTMUY2", "lstmuy2");
            mapping.put("LSTMSX1", "lstmsx1");
            mapping.put("LSTMSX2", "lstmsx2");
            mapping.put("LSTMSY1", "lstmsy1");
            mapping.put("LSTMSY2", "lstmsy2");
            mapping.put("seikeinagasa", "seikeinagasa");
            mapping.put("BIKO1", "biko1");
            mapping.put("BIKO2", "biko2");
            mapping.put("bikou3", "bikou3");
            mapping.put("bikou4", "bikou4");
            mapping.put("bikou5", "bikou5");
            mapping.put("GOKI", "goki");
            mapping.put("TTANSISUUU", "ttansisuuu");
            mapping.put("TTANSISUUS", "ttansisuus");
            mapping.put("SHUNKANKANETSUJIKAN", "shunkankanetsujikan");
            mapping.put("PETFILMSYURUI", "petfilmsyurui");
            mapping.put("KAATURYOKU", "kaaturyoku");
            mapping.put("GAIKANKAKUNIN", "gaikankakunin");
            mapping.put("SEKIJSSKIRIKAEICHI", "sekijsskirikaeichi");
            mapping.put("SEKIKKSKIRIKAEICHI", "sekikkskirikaeichi");
            mapping.put("KAATUJIKAN", "kaatujikan");
            mapping.put("TAPEHANSOUPITCH", "tapehansoupitch");
            mapping.put("TAPEHANSOUKAKUNIN", "tapehansoukakunin");
            mapping.put("EMAKUATSUSETTEI", "emakuatsusettei");
            mapping.put("ENEPPUFURYOU", "eneppufuryou");
            mapping.put("EMAKUATSUAVE", "emakuatsuave");
            mapping.put("EMAKUATSUMAX", "emakuatsumax");
            mapping.put("EMAKUATSUMIN", "emakuatsumin");
            mapping.put("NIJIMISOKUTEIPTN", "nijimisokuteiptn");
            mapping.put("PRNSAMPLEGAIKAN", "prnsamplegaikan");
            mapping.put("PRNICHIYOHAKUNAGASA", "prnichiyohakunagasa");
            mapping.put("CTABLECLEARANCE", "ctableclearance");
            mapping.put("CMAKUATSUSETTEI", "cmakuatsusettei");
            mapping.put("CSKSPEED", "cskspeed");
            mapping.put("CNEPPUFURYOU", "cneppufuryou");
            mapping.put("KABURIRYOU", "kaburiryou");
            mapping.put("SGAIKAN", "sgaikan");
            mapping.put("NIJIMISOKUTEISEKISOUGO", "nijimisokuteisekisougo");
            mapping.put("SEKISOUHINGAIKAN", "sekisouhingaikan");
            mapping.put("SEKISOUZURE", "sekisouzure");
            mapping.put("UWAJSSKIRIKAEICHI", "uwajsskirikaeichi");
            mapping.put("SHITAKKSKIRIKAEICHI", "shitakkskirikaeichi");
            mapping.put("TINKSYURYUI", "tinksyuryui");
            mapping.put("TINKLOT", "tinklot");
            mapping.put("TGAIKAN", "tgaikan");
            mapping.put("STARTTANTOU", "starttantou");
            mapping.put("ENDTANTOU", "endtantou");
            mapping.put("TENDDAY", "tendday");
            mapping.put("TENDTANTOU", "tendtantou");
            mapping.put("SYORISETSUU", "syorisetsuu");
            mapping.put("RYOUHINSETSUU", "ryouhinsetsuu");
            mapping.put("HEADKOUKANTANTOU", "headkoukantantou");
            mapping.put("SEKISOUJOUKENTANTOU", "sekisoujoukentantou");
            mapping.put("ESEIHANSETTANTOU", "eseihansettantou");
            mapping.put("CSEIHANSETTANTOU", "cseihansettantou");
            mapping.put("DANSASOKUTEITANTOU", "dansasokuteitantou");
            mapping.put("startkakunin", "startkakunin");
            mapping.put("TUMU", "tumu");
            mapping.put("EMAKUATSU1", "emakuatsu1");
            mapping.put("EMAKUATSU2", "emakuatsu2");
            mapping.put("EMAKUATSU3", "emakuatsu3");
            mapping.put("EMAKUATSU4", "emakuatsu4");
            mapping.put("EMAKUATSU5", "emakuatsu5");
            mapping.put("EMAKUATSU6", "emakuatsu6");
            mapping.put("EMAKUATSU7", "emakuatsu7");
            mapping.put("EMAKUATSU8", "emakuatsu8");
            mapping.put("EMAKUATSU9", "emakuatsu9");
            mapping.put("PTNDIST1", "ptndist1");
            mapping.put("PTNDIST2", "ptndist2");
            mapping.put("PTNDIST3", "ptndist3");
            mapping.put("PTNDIST4", "ptndist4");
            mapping.put("PTNDIST5", "ptndist5");
            mapping.put("AWASERZ1", "awaserz1");
            mapping.put("AWASERZ2", "awaserz2");
            mapping.put("AWASERZ3", "awaserz3");
            mapping.put("AWASERZ4", "awaserz4");
            mapping.put("AWASERZ5", "awaserz5");
            mapping.put("AWASERZ6", "awaserz6");
            mapping.put("AWASERZ7", "awaserz7");
            mapping.put("AWASERZ8", "awaserz8");
            mapping.put("AWASERZ9", "awaserz9");
            mapping.put("KABURIHIDARIUEX1", "kaburihidariuex1");
            mapping.put("KABURIHIDARIUEX2", "kaburihidariuex2");
            mapping.put("KABURIHIDARIUEY1", "kaburihidariuey1");
            mapping.put("KABURIHIDARIUEY2", "kaburihidariuey2");
            mapping.put("KABURIHIDARISITAX1", "kaburihidarisitax1");
            mapping.put("KABURIHIDARISITAX2", "kaburihidarisitax2");
            mapping.put("KABURIHIDARISITAY1", "kaburihidarisitay1");
            mapping.put("KABURIHIDARISITAY2", "kaburihidarisitay2");
            mapping.put("KABURIHIDARICENTERX1", "kaburihidaricenterx1");
            mapping.put("KABURIHIDARICENTERX2", "kaburihidaricenterx2");
            mapping.put("KABURIHIDARICENTERY1", "kaburihidaricentery1");
            mapping.put("KABURIHIDARICENTERY2", "kaburihidaricentery2");
            mapping.put("KABURIMIGIUEX1", "kaburimigiuex1");
            mapping.put("KABURIMIGIUEX2", "kaburimigiuex2");
            mapping.put("KABURIMIGIUEY1", "kaburimigiuey1");
            mapping.put("KABURIMIGIUEY2", "kaburimigiuey2");
            mapping.put("KABURIMIGISITAX1", "kaburimigisitax1");
            mapping.put("KABURIMIGISITAX2", "kaburimigisitax2");
            mapping.put("KABURIMIGISITAY1", "kaburimigisitay1");
            mapping.put("KABURIMIGISITAY2", "kaburimigisitay2");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B006Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B006Model.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
            
        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }
    
    /**
     * 文字列をバイトでカットします。
     * @param fieldName フィールド
     * @param length バイト数
     */
    public void checkByte(String fieldName, int length) {
        try {
            if (length < 1) {
                return;
            }

            // 指定フィールドの値を取得する
            Field f = this.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object value = f.get(this);

            // 切り捨て処理
            String cutValue = StringUtil.left(value.toString(), length);
            
            // 値をセット
            f.set(this, cutValue);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            // 処理無し
        }
    }
    
    /**
     * Excelダウンロード
     * @throws Throwable 
     */
    public void downloadExcel() throws Throwable {
        File excel = null;
        
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b006.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "印刷積層・RHAPS");

            // ダウンロードファイル名
            String downloadFileName = "印刷積層・RHAPS_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
            // outputstreamにファイルを転送
            ec.responseReset();
            ec.setResponseContentType("application/octet-stream");
            ec.setResponseHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(downloadFileName, "UTF-8") + "\"");

            try (OutputStream os = ec.getResponseOutputStream()) {
                output(excel, os);
                os.flush();        
                ec.responseFlushBuffer();
            }
            
            // サーバの物理ファイルを削除
            // ※削除失敗時も処理継続
            try {
                excel.delete();
            } catch (Exception e) {
            }

            fc.responseComplete();
        } catch (Exception ex) {
            ErrUtil.outputErrorLog("Excel出力に失敗", ex, LOGGER);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excel出力に失敗しました。", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            // 物理ファイルが削除されていない場合削除する
            if (excel != null && excel.exists()) {
                try {
                    excel.delete();
                } catch (Exception e) {
                }
            }
        }
    }
    
    /**
     * 検索パラメータ生成
     * @return パラメータ
     */
    private List<Object> createSearchParam() {
        // パラメータ設定
        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 11);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 11, 14));
        }
        String paramKcpno = null;
        if (!StringUtil.isEmpty(kcpNo)) {
            paramKcpno = "%" + DBUtil.escapeString(getKcpNo()) + "%";
        }
        String paramKatashiki = StringUtil.blankToNull(getKatashiki());
        String paramYoryo = StringUtil.blankToNull(getYoryo());
        Date paramStartDateF = null;
        if (!StringUtil.isEmpty(startDateF)) {
            paramStartDateF = DateUtil.convertStringToDate(getStartDateF(), StringUtil.isEmpty(getStartTimeF()) ? "0000" : getStartTimeF());
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startDateT)) {
            paramStartDateT = DateUtil.convertStringToDate(getStartDateT(), StringUtil.isEmpty(getStartTimeT()) ? "2359" : getStartTimeT());
        }
        Date paramEndDateF = null;
        if (!StringUtil.isEmpty(endDateF)) {
            paramEndDateF = DateUtil.convertStringToDate(getEndDateF(), StringUtil.isEmpty(getEndTimeF()) ? "0000" : getEndTimeF());
        }
        Date paramEndDateT = null;
        if (!StringUtil.isEmpty(endDateT)) {
            paramEndDateT = DateUtil.convertStringToDate(getEndDateT(), StringUtil.isEmpty(getEndTimeT()) ? "2359" : getEndTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        params.addAll(Arrays.asList(paramKatashiki, paramKatashiki));
        params.addAll(Arrays.asList(paramYoryo, paramYoryo));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));
        params.addAll(Arrays.asList(paramEndDateF, paramEndDateF));
        params.addAll(Arrays.asList(paramEndDateT, paramEndDateT));

        return params;
    }
    
    /**
     * エラーチェック：
     * エラーが存在する場合ポップアップ用メッセージをセットする
     * @param errorMessage エラーメッセージ
     * @return エラーが存在する場合true
     */
    private boolean existError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return true;
    }
    
    /**
     * 一覧表示データ存在チェック
     * @return 一覧表示データが存在する場合true
     */
    private boolean isExistListData() {
        if (listData == null || listData.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * ファイル転送
     * @param file ファイルオブジェクト
     * @param os outputstream
     * @throws IOException 
     */
    private void output(File file, OutputStream os) throws IOException {
        byte buffer[] = new byte[4096];
        try (FileInputStream fis = new FileInputStream(file)) {
            int size;
            while ((size = fis.read(buffer)) != -1) {
                os.write(buffer, 0, size);
            }
        }
    }
}
