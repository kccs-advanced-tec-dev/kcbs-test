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
import java.math.BigDecimal;
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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO201B053Model;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
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
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2024/12/09<br>
 * 計画書No	MB2408-D009<br>
 * 変更者	KCCS A.Hayashi<br>
 * 変更理由	新規作成<br>
 * <br>

 * ===============================================================================<br>
 */
/**
 * 電気特性・TWA履歴検索画面
 *
 * @author KCCS A.Hayashi
 * @since  2024/12/09
 */
@Named
@ViewScoped
public class GXHDO201B053 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B053.class.getName());
    
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B053Model> listData = null;
    
    /** 一覧表示最大件数 */
    private BigDecimal listCountMax = new BigDecimal(-1);
    /** 一覧表示警告件数 */
    private BigDecimal listCountWarn = new BigDecimal(-1);
    
    /** 一覧表示警告件数 */
    private BigDecimal decimal_Zero = new BigDecimal(0);
    
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 1ページ当たりの表示件数 */
    private int listDisplayPageCount = 30;
    
    /** 検索条件：ロットNo */
    private String lotNo = "";
    /** 検索条件：KCPNo */
    private String kcpNo = "";
    /** 検索条件：検査場所 */
    private String kensaBasyo = "";
    /** 検索条件：開始日(FROM) */
    private String senbetuStartDateF = "";
    /** 検索条件：開始日(TO) */
    private String senbetuStartDateT = "";
    /** 検索条件：開始時刻(FROM) */
    private String senbetuStartTimeF = "";
    /** 検索条件：開始時刻(TO) */
    private String senbetuStartTimeT = "";
    /** 検索条件：終了日(FROM) */
    private String senbetuEndDateF = "";
    /** 検索条件：終了日(TO) */
    private String senbetuEndDateT = "";
    /** 検索条件：終了時刻(FROM) */
    private String senbetuEndTimeF = "";
    /** 検索条件：終了時刻(TO) */
    private String senbetuEndTimeT = "";
    /** 検索条件：号機 */
    private String goki = "";
    /** 検索条件：設備区分 */
    private String setubiKubun = "";
    //表示可能ﾃﾞｰﾀ
    private String possibleData[];
    
    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B053() {
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
    public List<GXHDO201B053Model> getListData() {
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
     * 検索条件：検査場所
     * @return the kensaBasyo
     */
    public String getKensaBasyo() {
        return kensaBasyo;
    }

    /**
     * 検索条件：検査場所
     * @param kensaBasyo the kensaBasyo to set
     */
    public void setKensaBasyo(String kensaBasyo) {
        this.kensaBasyo = kensaBasyo;
    }

    /**
     * 検索条件：選別開始日(from)
     * @return the senbetuStartDateF
     */
    public String getSenbetuStartDateF() {
        return senbetuStartDateF;
    }

    /**
     * 検索条件：選別開始日(from)
     * @param senbetuStartDateF the senbetuStartDateF to set
     */
    public void setSenbetuStartDateF(String senbetuStartDateF) {
        this.senbetuStartDateF = senbetuStartDateF;
    }

    /**
     * 検索条件：選別開始日(to)
     * @return the senbetuStartDateT
     */
    public String getSenbetuStartDateT() {
        return senbetuStartDateT;
    }

    /**
     * 検索条件：選別開始日(to)
     * @param senbetuStartDateT the senbetuStartDateT to set
     */
    public void setSenbetuStartDateT(String senbetuStartDateT) {
        this.senbetuStartDateT = senbetuStartDateT;
    }

    /**
     * 検索条件：選別開始時刻(from)
     * @return the senbetuStartTimeF
     */
    public String getSenbetuStartTimeF() {
        return senbetuStartTimeF;
    }

    /**
     * 検索条件：選別開始時刻(from)
     * @param senbetuStartTimeF the senbetuStartTimeF to set
     */
    public void setSenbetuStartTimeF(String senbetuStartTimeF) {
        this.senbetuStartTimeF = senbetuStartTimeF;
    }

    /**
     * 検索条件：選別開始時刻(to)
     * @return the senbetuStartTimeT
     */
    public String getSenbetuStartTimeT() {
        return senbetuStartTimeT;
    }

    /**
     * 検索条件：選別開始時刻(to)
     * @param senbetuStartTimeT the senbetuStartTimeT to set
     */
    public void setSenbetuStartTimeT(String senbetuStartTimeT) {
        this.senbetuStartTimeT = senbetuStartTimeT;
    }

    /**
     * 検索条件：選別終了日(from)
     * @return the senbetuEndDateF
     */
    public String getSenbetuEndDateF() {
        return senbetuEndDateF;
    }

    /**
     * 検索条件：選別終了日(from)
     * @param senbetuEndDateF the senbetuEndDateF to set
     */
    public void setSenbetuEndDateF(String senbetuEndDateF) {
        this.senbetuEndDateF = senbetuEndDateF;
    }

    /**
     * 検索条件：選別終了日(to)
     * @return the senbetuEndDateT
     */
    public String getSenbetuEndDateT() {
        return senbetuEndDateT;
    }

    /**
     * 検索条件：選別終了日(to)
     * @param senbetuEndDateT the senbetuEndDateT to set
     */
    public void setSenbetuEndDateT(String senbetuEndDateT) {
        this.senbetuEndDateT = senbetuEndDateT;
    }

    /**
     * 検索条件：選別終了時刻(from)
     * @return the senbetuEndTimeF
     */
    public String getSenbetuEndTimeF() {
        return senbetuEndTimeF;
    }

    /**
     * 検索条件：選別終了時刻(from)
     * @param senbetuEndTimeF the senbetuEndTimeF to set
     */
    public void setSenbetuEndTimeF(String senbetuEndTimeF) {
        this.senbetuEndTimeF = senbetuEndTimeF;
    }

    /**
     * 検索条件：選別終了時刻(to)
     * @return the senbetuEndTimeT
     */
    public String getSenbetuEndTimeT() {
        return senbetuEndTimeT;
    }
    
    /**
     * 検索条件：選別終了時刻(to)
     * @param senbetuEndTimeT the senbetuEndTimeT to set
     */
    public void setSenbetuEndTimeT(String senbetuEndTimeT) {
        this.senbetuEndTimeT = senbetuEndTimeT;
    }

    /**
     * 検索条件：号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 検索条件：号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }
    
    /**
     * 検索条件：設備区分
     * @return the setubiKubun
     */
    public String getSetubiKubun() {
        return setubiKubun;
    }

    /**
     * 検索条件：設備区分
     * @param setubiKubun the setubiKubun to set
     */
    public void setSetubiKubun(String setubiKubun) {
        this.setubiKubun = setubiKubun;
    }
    
    /**
     * メインデータの件数を保持
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * メインデータの件数を保持
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
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
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group"); 

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B053_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B053_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? new BigDecimal(Integer.parseInt(session.getAttribute("menuParam").toString())) : new BigDecimal(-1);
        listCountWarn = session.getAttribute("hyojiKensu") != null ? new BigDecimal(Integer.parseInt(session.getAttribute("hyojiKensu").toString())) : new BigDecimal(-1);
        
                // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List7 = "";
        //■表示可能ﾃﾞｰﾀ取得処理
        //①Ⅲ.画面表示仕様(7)を発行する。
        Map fxhbm03Data7 = loadFxhbm03Data(userGrpList);
        //  1.取得できなかった場合、ｴﾗｰ。以降の処理は実行しない。
        if (fxhbm03Data7 == null || fxhbm03Data7.isEmpty()) {
            // ・ｴﾗｰｺｰﾄﾞ:XHD-000172
            // ・ｴﾗ-ﾒｯｾｰｼﾞ:電気特性履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
            // メッセージを画面に渡す
            settingError();
            return;
        } else {
            for (Object data : fxhbm03Data7.values()) {
                //取得したﾃﾞｰﾀが ALL の場合
                if ("ALL".equals(fxhbm03Data7.get(data))) {
                    strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                    possibleData = strfxhbm03List7.split(",");
                    //取得したﾃﾞｰﾀが NULL の場合、ｴﾗｰ。以降の処理は実行しない。
                } else if (data == null || "".equals(data)) {
                    //・ｴﾗｰｺｰﾄﾞ:XHD-000172
                    //・ｴﾗ-ﾒｯｾｰｼﾞ:電気特性履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
                    settingError();
                    return;
                } else {
                    //取得したﾃﾞｰﾀが ALL以外の場合
                    strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                    possibleData = strfxhbm03List7.split(",");
                }
            }
        }
            
        // 画面クリア
        clear();
    }
    
    /**
     * 設定エラー
     */
    private void settingError() {
        List<String> messageList = new ArrayList<>();
        messageList.add(MessageUtil.getMessage("XHD-000172"));
        displayStyle = "display:none;";
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(messageList);
        RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
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
        senbetuStartDateF = "";
        senbetuStartDateT = "";
        senbetuStartTimeF = "";
        senbetuStartTimeT = "";
        senbetuEndDateF = "";
        senbetuEndDateT = "";
        senbetuEndTimeF = "";
        senbetuEndTimeT = "";
        setGoki("");
        
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
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
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
        // 選別開始日(FROM)
        if (existError(validateUtil.checkC101(getSenbetuStartDateF(), "選別開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartDateF(), "選別開始日(from)")) ||
            existError(validateUtil.checkC501(getSenbetuStartDateF(), "選別開始日(from)"))) {
            return;
        }
        // 選別開始日(TO)
        if (existError(validateUtil.checkC101(getSenbetuStartDateT(), "選別開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartDateT(), "選別開始日(to)")) ||
            existError(validateUtil.checkC501(getSenbetuStartDateT(), "選別開始日(to)"))) {
            return;
        }
        // 選別終了日(FROM)
        if (existError(validateUtil.checkC101(getSenbetuEndDateF(), "選別終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndDateF(), "選別終了日(from)")) ||
            existError(validateUtil.checkC501(getSenbetuEndDateF(), "選別終了日(from)"))) {
            return;
        }
        // 選別終了日(TO)
        if (existError(validateUtil.checkC101(getSenbetuEndDateT(), "選別終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndDateT(), "選別終了日(to)")) ||
            existError(validateUtil.checkC501(getSenbetuEndDateT(), "選別終了日(to)"))) {
            return;
        }
        // 選別開始時刻(FROM)
        if (existError(validateUtil.checkC101(getSenbetuStartTimeF(), "選別開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartTimeF(), "選別開始時刻(from)")) ||
            existError(validateUtil.checkC502(getSenbetuStartTimeF(), "選別開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuStartTimeF) && existError(validateUtil.checkC001(getSenbetuStartDateF(), "選別開始日(from)"))) {
            return;
        }
        // 選別開始時刻(TO)
        if (existError(validateUtil.checkC101(getSenbetuStartTimeT(), "選別開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartTimeT(), "選別開始時刻(to)")) ||
            existError(validateUtil.checkC502(getSenbetuStartTimeT(), "選別開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuStartTimeT) && existError(validateUtil.checkC001(getSenbetuStartDateT(), "選別開始日(to)"))) {
            return;
        }
        // 選別終了時刻(FROM)
        if (existError(validateUtil.checkC101(getSenbetuEndTimeF(), "選別終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndTimeF(), "選別終了時刻(from)")) ||
            existError(validateUtil.checkC502(getSenbetuEndTimeF(), "選別終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuEndTimeF) && existError(validateUtil.checkC001(getSenbetuEndDateF(), "選別終了日(from)"))) {
            return;
        }
        // 選別終了時刻(TO)
        if (existError(validateUtil.checkC101(getSenbetuEndTimeT(), "選別終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndTimeT(), "選別終了時刻(to)")) ||
            existError(validateUtil.checkC502(getSenbetuEndTimeT(), "選別終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuEndTimeT) && existError(validateUtil.checkC001(getSenbetuEndDateT(), "選別終了日(to)"))) {
            return;
        }       
        // 号機
        if (existError(validateUtil.checkC103(getGoki(), "号機", 4))) {
            return;
        } 
        
        if (possibleData == null) {
            return;
        }
        
        // 一覧表示件数を取得
        BigDecimal count = selectListDataCount();
        
        if (count.compareTo(decimal_Zero) == 0) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (listCountMax.compareTo(decimal_Zero) > 0 && count.compareTo(listCountMax) > 0 ) {
            // 検索結果が上限件数以上の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000046", listCountMax), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } 
        
        if (listCountWarn.compareTo(decimal_Zero) > 0  && count.compareTo(listCountWarn) > 0) {
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
    public BigDecimal selectListDataCount() {
        BigDecimal count = new BigDecimal(0);
        //検査場所データリスト
        List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM sr_denkitokuseiesi "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR kensagouki LIKE ? ESCAPE '\\\\') ";

            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    sql += " AND ";
                    sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
                }
            }

            sql += " AND   (? IS NULL OR SENBETUKAISINITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUKAISINITIJI <= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI <= ?) "
                    + " AND   (? IS NULL OR setubikubun = ?) ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = new BigDecimal(result.get("CNT").toString());

        } catch (SQLException ex) {
            count =  new BigDecimal(0);
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
            //検査場所データリスト
            List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));  
            String sql = "SELECT CONCAT(IFNULL(KOJYO, ''), IFNULL(LOTNO, ''), IFNULL(EDABAN, '')) AS LOTNO"
                    + ", kaisuu "
                    + ", kcpno "
                    + ", tokuisaki "
                    + ", ownercode "
                    + ", lotkubuncode "
                    + ", siteikousa "
                    + ", atokouteisijinaiyou "
                    + ", okuriryouhinsuu "
                    + ", ukeiretannijyuryo "
                    + ", ukeiresoujyuryou "
                    + ", kensabasyo "
                    + ", senbetukaisinitiji "
                    + ", senbetusyuryounitiji "
                    + ", siteikousabudomari1 "
                    + ", siteikousabudomari2 "
                    + ", siteikousabudomari3 "
                    + ", kensagouki "
                    + ", testplatekanrino "
                    + ", testplatekeijo "
                    + ", bunruiairatu "
                    + ", testplatekakunin "
                    + ", gentenhukkidousa "
                    + ", sokuteiki12dousakakunin "
                    + ", sokuteipinfront "
                    + ", sokuteipinrear "
                    + ", sokuteisyuhasuu "
                    + ", sokuteidenatu "
                    + ", douhinsyu "
                    + ", hoseiyoutippuyoryou "
                    + ", hoseiyoutipputan "
                    + ", hoseimae "
                    + ", hoseigo "
                    + ", hoseiritu "
                    + ", tan "
                    + ", binboxseisoucheck "
                    + ", bunruikakunin "
                    + ", gaikankakunin "
                    + ", netsusyorinitiji "
                    + ", agingjikan "
                    + ", setsya "
                    + ", kakuninsya "
                    + ", syoninsha "
                    + ", furimukesya "
                    + ", bikou1 "
                    + ", bikou2 "
                    + ", irdenatu1 "
                    + ", ir1jikan "
                    + ", ir1jikantani "
                    + ", ir1denryustart "
                    + ", ir1denryustarttani "
                    + ", ir1denryuend "
                    + ", ir1denryuendtani "
                    + ", ir1sokuteihanistart "
                    + ", ir1sokuteihanistarttani "
                    + ", ir1sokuteihaniend "
                    + ", ir1sokuteihaniendtani "
                    + ", irhanteiti1 "
                    + ", irhanteiti1tani "
                    + ", irhanteiti1low "
                    + ", irhanteiti1tanilow "
                    + ", irdenatu2 "
                    + ", ir2jikan "
                    + ", ir2jikantani "
                    + ", ir2denryustart "
                    + ", ir2denryustarttani "
                    + ", ir2denryuend "
                    + ", ir2denryuendtani "
                    + ", ir2sokuteihanistart "
                    + ", ir2sokuteihanistarttani "
                    + ", ir2sokuteihaniend "
                    + ", ir2sokuteihaniendtani "
                    + ", irhanteiti2 "
                    + ", irhanteiti2tani "
                    + ", irhanteiti2low "
                    + ", irhanteiti2tanilow "
                    + ", irdenatu3 "
                    + ", ir3jikan "
                    + ", ir3jikantani "
                    + ", ir3denryustart "
                    + ", ir3denryustarttani "
                    + ", ir3denryuend "
                    + ", ir3denryuendtani "
                    + ", ir3sokuteihanistart "
                    + ", ir3sokuteihanistarttani "
                    + ", ir3sokuteihaniend "
                    + ", ir3sokuteihaniendtani "
                    + ", irhanteiti3 "
                    + ", irhanteiti3tani "
                    + ", irhanteiti3low "
                    + ", irhanteiti3tanilow "
                    + ", irdenatu4 "
                    + ", ir4jikan "
                    + ", ir4jikantani "
                    + ", ir4denryustart "
                    + ", ir4denryustarttani "
                    + ", ir4denryuend "
                    + ", ir4denryuendtani "
                    + ", ir4sokuteihanistart "
                    + ", ir4sokuteihanistarttani "
                    + ", ir4sokuteihaniend "
                    + ", ir4sokuteihaniendtani "
                    + ", irhanteiti4 "
                    + ", irhanteiti4tani "
                    + ", irhanteiti4low "
                    + ", irhanteiti4tanilow "
                    + ", irdenatu5 "
                    + ", ir5jikan "
                    + ", ir5jikantani "
                    + ", ir5denryustart "
                    + ", ir5denryustarttani "
                    + ", ir5denryuend "
                    + ", ir5denryuendtani "
                    + ", ir5sokuteihanistart "
                    + ", ir5sokuteihanistarttani "
                    + ", ir5sokuteihaniend "
                    + ", ir5sokuteihaniendtani "
                    + ", irhanteiti5 "
                    + ", irhanteiti5tani "
                    + ", irhanteiti5low "
                    + ", irhanteiti5tanilow "
                    + ", irdenatu6 "
                    + ", ir6jikan "
                    + ", ir6jikantani "
                    + ", ir6denryustart "
                    + ", ir6denryustarttani "
                    + ", ir6denryuend "
                    + ", ir6denryuendtani "
                    + ", ir6sokuteihanistart "
                    + ", ir6sokuteihanistarttani "
                    + ", ir6sokuteihaniend "
                    + ", ir6sokuteihaniendtani "
                    + ", irhanteiti6 "
                    + ", irhanteiti6tani "
                    + ", irhanteiti6low "
                    + ", irhanteiti6tanilow "
                    + ", irdenatu7 "
                    + ", ir7jikan "
                    + ", ir7jikantani "
                    + ", ir7denryustart "
                    + ", ir7denryustarttani "
                    + ", ir7denryuend "
                    + ", ir7denryuendtani "
                    + ", ir7sokuteihanistart "
                    + ", ir7sokuteihanistarttani "
                    + ", ir7sokuteihaniend "
                    + ", ir7sokuteihaniendtani "
                    + ", irhanteiti7 "
                    + ", irhanteiti7tani "
                    + ", irhanteiti7low "
                    + ", irhanteiti7tanilow "
                    + ", irdenatu8 "
                    + ", ir8jikan "
                    + ", ir8jikantani "
                    + ", ir8denryustart "
                    + ", ir8denryustarttani "
                    + ", ir8denryuend "
                    + ", ir8denryuendtani "
                    + ", ir8sokuteihanistart "
                    + ", ir8sokuteihanistarttani "
                    + ", ir8sokuteihaniend "
                    + ", ir8sokuteihaniendtani "
                    + ", irhanteiti8 "
                    + ", irhanteiti8tani "
                    + ", irhanteiti8low "
                    + ", irhanteiti8tanilow "
                    + ", bin1setteiti "
                    + ", bin1senbetukubun "
                    + ", bin1keiryougosuryou "
                    + ", bin1countersuu "
                    + ", bin1gosaritu "
                    + ", bin1masinfuryouritu "
                    + ", bin1nukitorikekka "
                    + ", bin1nukitorikekkabosuu "
                    + ", bin1sinnofuryouritu "
                    + ", bin1kekkacheck "
                    + ", bin1fukurocheck "
                    + ", bin2setteiti "
                    + ", bin2senbetukubun "
                    + ", bin2keiryougosuryou "
                    + ", bin2countersuu "
                    + ", bin2gosaritu "
                    + ", bin2masinfuryouritu "
                    + ", bin2nukitorikekka "
                    + ", bin2nukitorikekkabosuu "
                    + ", bin2sinnofuryouritu "
                    + ", bin2kekkacheck "
                    + ", bin2fukurocheck "
                    + ", bin3setteiti "
                    + ", bin3senbetukubun "
                    + ", bin3keiryougosuryou "
                    + ", bin3countersuu "
                    + ", bin3gosaritu "
                    + ", bin3masinfuryouritu "
                    + ", bin3nukitorikekka "
                    + ", bin3nukitorikekkabosuu "
                    + ", bin3sinnofuryouritu "
                    + ", bin3kekkacheck "
                    + ", bin3fukurocheck "
                    + ", bin4setteiti "
                    + ", bin4senbetukubun "
                    + ", bin4keiryougosuryou "
                    + ", bin4countersuu "
                    + ", bin4gosaritu "
                    + ", bin4masinfuryouritu "
                    + ", bin4nukitorikekka "
                    + ", bin4nukitorikekkabosuu "
                    + ", bin4sinnofuryouritu "
                    + ", bin4kekkacheck "
                    + ", bin4fukurocheck "
                    + ", bin5setteiti "
                    + ", bin5senbetukubun "
                    + ", bin5keiryougosuryou "
                    + ", bin5countersuu "
                    + ", bin5gosaritu "
                    + ", bin5masinfuryouritu "
                    + ", bin5nukitorikekka "
                    + ", bin5nukitorikekkabosuu "
                    + ", bin5sinnofuryouritu "
                    + ", bin5kekkacheck "
                    + ", bin5fukurocheck "
                    + ", bin6setteiti "
                    + ", bin6senbetukubun "
                    + ", bin6keiryougosuryou "
                    + ", bin6countersuu "
                    + ", bin6gosaritu "
                    + ", bin6masinfuryouritu "
                    + ", bin6nukitorikekka "
                    + ", bin6nukitorikekkabosuu "
                    + ", bin6sinnofuryouritu "
                    + ", bin6kekkacheck "
                    + ", bin6fukurocheck "
                    + ", bin7setteiti "
                    + ", bin7senbetukubun "
                    + ", bin7keiryougosuryou "
                    + ", bin7countersuu "
                    + ", bin7gosaritu "
                    + ", bin7masinfuryouritu "
                    + ", bin7nukitorikekka "
                    + ", bin7nukitorikekkabosuu "
                    + ", bin7sinnofuryouritu "
                    + ", bin7kekkacheck "
                    + ", bin7fukurocheck "
                    + ", bin8setteiti "
                    + ", bin8senbetukubun "
                    + ", bin8keiryougosuryou "
                    + ", bin8countersuu "
                    + ", bin8gosaritu "
                    + ", bin8masinfuryouritu "
                    + ", bin8nukitorikekka "
                    + ", bin8nukitorikekkabosuu "
                    + ", bin8sinnofuryouritu "
                    + ", bin8kekkacheck "
                    + ", bin8fukurocheck "
                    + ", bin9keiryougosuryou "
                    + ", bin9masinfuryouritu "
                    + ", rakkakeiryougosuryou "
                    + ", rakkamasinfuryouritu "
                    + ", handasample "
                    + ", sinraiseisample "
                    + ", satsample "
                    + ", sinfuryouhanteisya "
                    + ", hanteinyuuryokusya "
                    + ", toridasisya "
                    + ", kousa1 "
                    + ", juryou1 "
                    + ", kosuu1 "
                    + ", kousa2 "
                    + ", juryou2 "
                    + ", kosuu2 "
                    + ", kousa3 "
                    + ", juryou3 "
                    + ", kosuu3 "
                    + ", kousa4 "
                    + ", juryou4 "
                    + ", kosuu4 "
                    + ", countersousuu "
                    + ", ryohinjuryou "
                    + ", ryohinkosuu "
                    + ", budomari "
                    + ", binkakuninsya "
                    + ", saiken "
                    + ", setubikubun "
                    + "  FROM sr_denkitokuseiesi "
                    + " WHERE (? IS NULL OR KOJYO = ?) "
                    + " AND   (? IS NULL OR LOTNO = ?) "
                    + " AND   (? IS NULL OR EDABAN = ?) "
                    + " AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + " AND   (? IS NULL OR kensagouki LIKE ? ESCAPE '\\\\') ";
            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    sql += " AND ";
                    sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
                }
            }
            sql += " AND   (? IS NULL OR SENBETUKAISINITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUKAISINITIJI <= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI <= ?) "
                    + " AND   (? IS NULL OR setubikubun = ?) "
                    + " ORDER BY SENBETUKAISINITIJI ";              
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");// ﾛｯﾄNo.
            mapping.put("kaisuu", "kaisuu");// 回数
            mapping.put("kcpno", "kcpno");// KCPNO
            mapping.put("tokuisaki", "tokuisaki");// 客先
            mapping.put("ownercode", "ownercode");// ｵｰﾅｰ
            mapping.put("lotkubuncode", "lotkubuncode");// ﾛｯﾄ区分
            mapping.put("siteikousa", "siteikousa");// 指定公差
            mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou");// 後工程指示内容
            mapping.put("okuriryouhinsuu", "okuriryouhinsuu");// 送り良品数
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo");// 受入れ単位重量
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou");// 受入れ総重量
            mapping.put("kensabasyo", "kensabasyo");// 検査場所
            mapping.put("senbetukaisinitiji", "senbetukaisinitiji");// 選別開始日時
            mapping.put("senbetusyuryounitiji", "senbetusyuryounitiji");// 選別終了日時
            mapping.put("siteikousabudomari1", "siteikousabudomari1");// 特性第1公差
            mapping.put("siteikousabudomari2", "siteikousabudomari2");// 特性第2公差
            mapping.put("siteikousabudomari3", "siteikousabudomari3");// 特性第3公差
            mapping.put("kensagouki", "kensagouki");// 検査号機
            mapping.put("testplatekanrino", "testplatekanrino");// インデックステーブル管理No.
            mapping.put("testplatekeijo", "testplatekeijo");// インデックステーブル目視
            mapping.put("bunruiairatu", "bunruiairatu");// メインエアー圧
            mapping.put("testplatekakunin", "testplatekakunin");// インデックステーブル位置確認(穴位置)
            mapping.put("gentenhukkidousa", "gentenhukkidousa");// 原点復帰動作
            mapping.put("sokuteiki12dousakakunin", "sokuteiki12dousakakunin");// 測定機1,2動作確認
            mapping.put("sokuteipinfront", "sokuteipinfront");// 測定ピン(フロント)外観
            mapping.put("sokuteipinrear", "sokuteipinrear");// 測定ピン(リア)外観
            mapping.put("sokuteisyuhasuu", "sokuteisyuhasuu");// 測定周波数
            mapping.put("sokuteidenatu", "sokuteidenatu");// 測定電圧
            mapping.put("douhinsyu", "douhinsyu");// 同品種
            mapping.put("hoseiyoutippuyoryou", "hoseiyoutippuyoryou");// 補正用ﾁｯﾌﾟ容量
            mapping.put("hoseiyoutipputan", "hoseiyoutipputan");// 補正用ﾁｯﾌﾟTanδ
            mapping.put("hoseimae", "hoseimae");// 補正前
            mapping.put("hoseigo", "hoseigo");// 補正後
            mapping.put("hoseiritu", "hoseiritu");// 補正率
            mapping.put("tan", "tan");// Tanδ
            mapping.put("binboxseisoucheck", "binboxseisoucheck");// BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
            mapping.put("bunruikakunin", "bunruikakunin");// 分類確認
            mapping.put("gaikankakunin", "gaikankakunin");// 外観確認
            mapping.put("netsusyorinitiji", "netsusyorinitiji");// 熱処理日時
            mapping.put("agingjikan", "agingjikan");// ｴｰｼﾞﾝｸﾞ時間
            mapping.put("setsya", "setsya");// ｾｯﾄ者
            mapping.put("kakuninsya", "kakuninsya");// 確認者
            mapping.put("syoninsha", "syoninsha");// 承認者
            mapping.put("furimukesya", "furimukesya");// 振向者
            mapping.put("bikou1", "bikou1");// 備考1
            mapping.put("bikou2", "bikou2");// 備考2
            mapping.put("irdenatu1", "irdenatu1");// IR① 電圧
            mapping.put("ir1jikan", "ir1jikan");// IR① 時間
            mapping.put("ir1jikantani", "ir1jikantani");// IR① 時間 単位
            mapping.put("ir1denryustart", "ir1denryustart");// IR① 電流中心値スタート
            mapping.put("ir1denryustarttani", "ir1denryustarttani");// IR① 電流中心値スタート 単位
            mapping.put("ir1denryuend", "ir1denryuend");// IR① 電流中心値エンド
            mapping.put("ir1denryuendtani", "ir1denryuendtani");// IR① 電流中心値エンド 単位
            mapping.put("ir1sokuteihanistart", "ir1sokuteihanistart");// IR① 測定範囲スタート
            mapping.put("ir1sokuteihanistarttani", "ir1sokuteihanistarttani");// IR① 測定範囲スタート 単位
            mapping.put("ir1sokuteihaniend", "ir1sokuteihaniend");// IR① 測定範囲エンド
            mapping.put("ir1sokuteihaniendtani", "ir1sokuteihaniendtani");// IR① 測定範囲エンド 単位
            mapping.put("irhanteiti1", "irhanteiti1");// IR① 良品範囲上限
            mapping.put("irhanteiti1tani", "irhanteiti1tani");// IR① 良品範囲上限 単位
            mapping.put("irhanteiti1low", "irhanteiti1low");// IR① 良品範囲下限
            mapping.put("irhanteiti1tanilow", "irhanteiti1tanilow");// IR① 良品範囲下限 単位
            mapping.put("irdenatu2", "irdenatu2");// IR② 電圧
            mapping.put("ir2jikan", "ir2jikan");// IR② 時間
            mapping.put("ir2jikantani", "ir2jikantani");// IR② 時間 単位
            mapping.put("ir2denryustart", "ir2denryustart");// IR② 電流中心値スタート
            mapping.put("ir2denryustarttani", "ir2denryustarttani");// IR② 電流中心値スタート 単位
            mapping.put("ir2denryuend", "ir2denryuend");// IR② 電流中心値エンド
            mapping.put("ir2denryuendtani", "ir2denryuendtani");// IR② 電流中心値エンド 単位
            mapping.put("ir2sokuteihanistart", "ir2sokuteihanistart");// IR② 測定範囲スタート
            mapping.put("ir2sokuteihanistarttani", "ir2sokuteihanistarttani");// IR② 測定範囲スタート 単位
            mapping.put("ir2sokuteihaniend", "ir2sokuteihaniend");// IR② 測定範囲エンド
            mapping.put("ir2sokuteihaniendtani", "ir2sokuteihaniendtani");// IR② 測定範囲エンド 単位
            mapping.put("irhanteiti2", "irhanteiti2");// IR② 良品範囲上限
            mapping.put("irhanteiti2tani", "irhanteiti2tani");// IR② 良品範囲上限 単位
            mapping.put("irhanteiti2low", "irhanteiti2low");// IR② 良品範囲下限
            mapping.put("irhanteiti2tanilow", "irhanteiti2tanilow");// IR② 良品範囲下限 単位
            mapping.put("irdenatu3", "irdenatu3");// IR③ 電圧
            mapping.put("ir3jikan", "ir3jikan");// IR③ 時間
            mapping.put("ir3jikantani", "ir3jikantani");// IR③ 時間 単位
            mapping.put("ir3denryustart", "ir3denryustart");// IR③ 電流中心値スタート
            mapping.put("ir3denryustarttani", "ir3denryustarttani");// IR③ 電流中心値スタート 単位
            mapping.put("ir3denryuend", "ir3denryuend");// IR③ 電流中心値エンド
            mapping.put("ir3denryuendtani", "ir3denryuendtani");// IR③ 電流中心値エンド 単位
            mapping.put("ir3sokuteihanistart", "ir3sokuteihanistart");// IR③ 測定範囲スタート
            mapping.put("ir3sokuteihanistarttani", "ir3sokuteihanistarttani");// IR③ 測定範囲スタート 単位
            mapping.put("ir3sokuteihaniend", "ir3sokuteihaniend");// IR③ 測定範囲エンド
            mapping.put("ir3sokuteihaniendtani", "ir3sokuteihaniendtani");// IR③ 測定範囲エンド 単位
            mapping.put("irhanteiti3", "irhanteiti3");// IR③ 良品範囲上限
            mapping.put("irhanteiti3tani", "irhanteiti3tani");// IR③ 良品範囲上限 単位
            mapping.put("irhanteiti3low", "irhanteiti3low");// IR③ 良品範囲下限
            mapping.put("irhanteiti3tanilow", "irhanteiti3tanilow");// IR③ 良品範囲下限 単位
            mapping.put("irdenatu4", "irdenatu4");// IR④ 電圧
            mapping.put("ir4jikan", "ir4jikan");// IR④ 時間
            mapping.put("ir4jikantani", "ir4jikantani");// IR④ 時間 単位
            mapping.put("ir4denryustart", "ir4denryustart");// IR④ 電流中心値スタート
            mapping.put("ir4denryustarttani", "ir4denryustarttani");// IR④ 電流中心値スタート 単位
            mapping.put("ir4denryuend", "ir4denryuend");// IR④ 電流中心値エンド
            mapping.put("ir4denryuendtani", "ir4denryuendtani");// IR④ 電流中心値エンド 単位
            mapping.put("ir4sokuteihanistart", "ir4sokuteihanistart");// IR④ 測定範囲スタート
            mapping.put("ir4sokuteihanistarttani", "ir4sokuteihanistarttani");// IR④ 測定範囲スタート 単位
            mapping.put("ir4sokuteihaniend", "ir4sokuteihaniend");// IR④ 測定範囲エンド
            mapping.put("ir4sokuteihaniendtani", "ir4sokuteihaniendtani");// IR④ 測定範囲エンド 単位
            mapping.put("irhanteiti4", "irhanteiti4");// IR④ 良品範囲上限
            mapping.put("irhanteiti4tani", "irhanteiti4tani");// IR④ 良品範囲上限 単位
            mapping.put("irhanteiti4low", "irhanteiti4low");// IR④ 良品範囲下限
            mapping.put("irhanteiti4tanilow", "irhanteiti4tanilow");// IR④ 良品範囲下限 単位
            mapping.put("irdenatu5", "irdenatu5");// IR⑤ 電圧
            mapping.put("ir5jikan", "ir5jikan");// IR⑤ 時間
            mapping.put("ir5jikantani", "ir5jikantani");// IR⑤ 時間 単位
            mapping.put("ir5denryustart", "ir5denryustart");// IR⑤ 電流中心値スタート
            mapping.put("ir5denryustarttani", "ir5denryustarttani");// IR⑤ 電流中心値スタート 単位
            mapping.put("ir5denryuend", "ir5denryuend");// IR⑤ 電流中心値エンド
            mapping.put("ir5denryuendtani", "ir5denryuendtani");// IR⑤ 電流中心値エンド 単位
            mapping.put("ir5sokuteihanistart", "ir5sokuteihanistart");// IR⑤ 測定範囲スタート
            mapping.put("ir5sokuteihanistarttani", "ir5sokuteihanistarttani");// IR⑤ 測定範囲スタート 単位
            mapping.put("ir5sokuteihaniend", "ir5sokuteihaniend");// IR⑤ 測定範囲エンド
            mapping.put("ir5sokuteihaniendtani", "ir5sokuteihaniendtani");// IR⑤ 測定範囲エンド 単位
            mapping.put("irhanteiti5", "irhanteiti5");// IR⑤ 良品範囲上限
            mapping.put("irhanteiti5tani", "irhanteiti5tani");// IR⑤ 良品範囲上限 単位
            mapping.put("irhanteiti5low", "irhanteiti5low");// IR⑤ 良品範囲下限
            mapping.put("irhanteiti5tanilow", "irhanteiti5tanilow");// IR⑤ 良品範囲下限 単位
            mapping.put("irdenatu6", "irdenatu6");// IR⑥ 電圧
            mapping.put("ir6jikan", "ir6jikan");// IR⑥ 時間
            mapping.put("ir6jikantani", "ir6jikantani");// IR⑥ 時間 単位
            mapping.put("ir6denryustart", "ir6denryustart");// IR⑥ 電流中心値スタート
            mapping.put("ir6denryustarttani", "ir6denryustarttani");// IR⑥ 電流中心値スタート 単位
            mapping.put("ir6denryuend", "ir6denryuend");// IR⑥ 電流中心値エンド
            mapping.put("ir6denryuendtani", "ir6denryuendtani");// IR⑥ 電流中心値エンド 単位
            mapping.put("ir6sokuteihanistart", "ir6sokuteihanistart");// IR⑥ 測定範囲スタート
            mapping.put("ir6sokuteihanistarttani", "ir6sokuteihanistarttani");// IR⑥ 測定範囲スタート 単位
            mapping.put("ir6sokuteihaniend", "ir6sokuteihaniend");// IR⑥ 測定範囲エンド
            mapping.put("ir6sokuteihaniendtani", "ir6sokuteihaniendtani");// IR⑥ 測定範囲エンド 単位
            mapping.put("irhanteiti6", "irhanteiti6");// IR⑥ 良品範囲上限
            mapping.put("irhanteiti6tani", "irhanteiti6tani");// IR⑥ 良品範囲上限 単位
            mapping.put("irhanteiti6low", "irhanteiti6low");// IR⑥ 良品範囲下限
            mapping.put("irhanteiti6tanilow", "irhanteiti6tanilow");// IR⑥ 良品範囲下限 単位
            mapping.put("irdenatu7", "irdenatu7");// IR⑦ 電圧
            mapping.put("ir7jikan", "ir7jikan");// IR⑦ 時間
            mapping.put("ir7jikantani", "ir7jikantani");// IR⑦ 時間 単位
            mapping.put("ir7denryustart", "ir7denryustart");// IR⑦ 電流中心値スタート
            mapping.put("ir7denryustarttani", "ir7denryustarttani");// IR⑦ 電流中心値スタート 単位
            mapping.put("ir7denryuend", "ir7denryuend");// IR⑦ 電流中心値エンド
            mapping.put("ir7denryuendtani", "ir7denryuendtani");// IR⑦ 電流中心値エンド 単位
            mapping.put("ir7sokuteihanistart", "ir7sokuteihanistart");// IR⑦ 測定範囲スタート
            mapping.put("ir7sokuteihanistarttani", "ir7sokuteihanistarttani");// IR⑦ 測定範囲スタート 単位
            mapping.put("ir7sokuteihaniend", "ir7sokuteihaniend");// IR⑦ 測定範囲エンド
            mapping.put("ir7sokuteihaniendtani", "ir7sokuteihaniendtani");// IR⑦ 測定範囲エンド 単位
            mapping.put("irhanteiti7", "irhanteiti7");// IR⑦ 良品範囲上限
            mapping.put("irhanteiti7tani", "irhanteiti7tani");// IR⑦ 良品範囲上限 単位
            mapping.put("irhanteiti7low", "irhanteiti7low");// IR⑦ 良品範囲下限
            mapping.put("irhanteiti7tanilow", "irhanteiti7tanilow");// IR⑦ 良品範囲下限 単位
            mapping.put("irdenatu8", "irdenatu8");// IR⑧ 電圧
            mapping.put("ir8jikan", "ir8jikan");// IR⑧ 時間
            mapping.put("ir8jikantani", "ir8jikantani");// IR⑧ 時間 単位
            mapping.put("ir8denryustart", "ir8denryustart");// IR⑧ 電流中心値スタート
            mapping.put("ir8denryustarttani", "ir8denryustarttani");// IR⑧ 電流中心値スタート 単位
            mapping.put("ir8denryuend", "ir8denryuend");// IR⑧ 電流中心値エンド
            mapping.put("ir8denryuendtani", "ir8denryuendtani");// IR⑧ 電流中心値エンド 単位
            mapping.put("ir8sokuteihanistart", "ir8sokuteihanistart");// IR⑧ 測定範囲スタート
            mapping.put("ir8sokuteihanistarttani", "ir8sokuteihanistarttani");// IR⑧ 測定範囲スタート 単位
            mapping.put("ir8sokuteihaniend", "ir8sokuteihaniend");// IR⑧ 測定範囲エンド
            mapping.put("ir8sokuteihaniendtani", "ir8sokuteihaniendtani");// IR⑧ 測定範囲エンド 単位
            mapping.put("irhanteiti8", "irhanteiti8");// IR⑧ 良品範囲上限
            mapping.put("irhanteiti8tani", "irhanteiti8tani");// IR⑧ 良品範囲上限 単位
            mapping.put("irhanteiti8low", "irhanteiti8low");// IR⑧ 良品範囲下限
            mapping.put("irhanteiti8tanilow", "irhanteiti8tanilow");// IR⑧ 良品範囲下限 単位
            mapping.put("bin1setteiti", "bin1setteiti");// BIN1 %区分(設定値)
            mapping.put("bin1senbetukubun", "bin1senbetukubun");// BIN1 選別区分
            mapping.put("bin1keiryougosuryou", "bin1keiryougosuryou");// BIN1 計量後数量
            mapping.put("bin1countersuu", "bin1countersuu");// BIN1 ｶｳﾝﾀｰ数
            mapping.put("bin1gosaritu", "bin1gosaritu");// BIN1 誤差率(%)
            mapping.put("bin1masinfuryouritu", "bin1masinfuryouritu");// BIN1 ﾏｼﾝ不良率(%)
            mapping.put("bin1nukitorikekka", "bin1nukitorikekka");// BIN1 抜き取り結果(子数)
            mapping.put("bin1nukitorikekkabosuu", "bin1nukitorikekkabosuu");// BIN1 抜き取り結果(母数)
            mapping.put("bin1sinnofuryouritu", "bin1sinnofuryouritu");// BIN1 真の不良率(%)
            mapping.put("bin1kekkacheck", "bin1kekkacheck");// BIN1 結果ﾁｪｯｸ
            mapping.put("bin1fukurocheck", "bin1fukurocheck");// BIN1 袋ﾁｪｯｸ
            mapping.put("bin2setteiti", "bin2setteiti");// BIN2 %区分(設定値)
            mapping.put("bin2senbetukubun", "bin2senbetukubun");// BIN2 選別区分
            mapping.put("bin2keiryougosuryou", "bin2keiryougosuryou");// BIN2 計量後数量
            mapping.put("bin2countersuu", "bin2countersuu");// BIN2 ｶｳﾝﾀｰ数
            mapping.put("bin2gosaritu", "bin2gosaritu");// BIN2 誤差率(%)
            mapping.put("bin2masinfuryouritu", "bin2masinfuryouritu");// BIN2 ﾏｼﾝ不良率(%)
            mapping.put("bin2nukitorikekka", "bin2nukitorikekka");// BIN2 抜き取り結果(子数)
            mapping.put("bin2nukitorikekkabosuu", "bin2nukitorikekkabosuu");// BIN2 抜き取り結果(母数)
            mapping.put("bin2sinnofuryouritu", "bin2sinnofuryouritu");// BIN2 真の不良率(%)
            mapping.put("bin2kekkacheck", "bin2kekkacheck");// BIN2 結果ﾁｪｯｸ
            mapping.put("bin2fukurocheck", "bin2fukurocheck");// BIN2 袋ﾁｪｯｸ
            mapping.put("bin3setteiti", "bin3setteiti");// BIN3 %区分(設定値)
            mapping.put("bin3senbetukubun", "bin3senbetukubun");// BIN3 選別区分
            mapping.put("bin3keiryougosuryou", "bin3keiryougosuryou");// BIN3 計量後数量
            mapping.put("bin3countersuu", "bin3countersuu");// BIN3 ｶｳﾝﾀｰ数
            mapping.put("bin3gosaritu", "bin3gosaritu");// BIN3 誤差率(%)
            mapping.put("bin3masinfuryouritu", "bin3masinfuryouritu");// BIN3 ﾏｼﾝ不良率(%)
            mapping.put("bin3nukitorikekka", "bin3nukitorikekka");// BIN3 抜き取り結果(子数)
            mapping.put("bin3nukitorikekkabosuu", "bin3nukitorikekkabosuu");// BIN3 抜き取り結果(母数)
            mapping.put("bin3sinnofuryouritu", "bin3sinnofuryouritu");// BIN3 真の不良率(%)
            mapping.put("bin3kekkacheck", "bin3kekkacheck");// BIN3 結果ﾁｪｯｸ
            mapping.put("bin3fukurocheck", "bin3fukurocheck");// BIN3 袋ﾁｪｯｸ
            mapping.put("bin4setteiti", "bin4setteiti");// BIN4 %区分(設定値)
            mapping.put("bin4senbetukubun", "bin4senbetukubun");// BIN4 選別区分
            mapping.put("bin4keiryougosuryou", "bin4keiryougosuryou");// BIN4 計量後数量
            mapping.put("bin4countersuu", "bin4countersuu");// BIN4 ｶｳﾝﾀｰ数
            mapping.put("bin4gosaritu", "bin4gosaritu");// BIN4 誤差率(%)
            mapping.put("bin4masinfuryouritu", "bin4masinfuryouritu");// BIN4 ﾏｼﾝ不良率(%)
            mapping.put("bin4nukitorikekka", "bin4nukitorikekka");// BIN4 抜き取り結果(子数)
            mapping.put("bin4nukitorikekkabosuu", "bin4nukitorikekkabosuu");// BIN4 抜き取り結果(母数)
            mapping.put("bin4sinnofuryouritu", "bin4sinnofuryouritu");// BIN4 真の不良率(%)
            mapping.put("bin4kekkacheck", "bin4kekkacheck");// BIN4 結果ﾁｪｯｸ
            mapping.put("bin4fukurocheck", "bin4fukurocheck");// BIN4 袋ﾁｪｯｸ
            mapping.put("bin5setteiti", "bin5setteiti");// BIN5 %区分(設定値)
            mapping.put("bin5senbetukubun", "bin5senbetukubun");// BIN5 選別区分
            mapping.put("bin5keiryougosuryou", "bin5keiryougosuryou");// BIN5 計量後数量
            mapping.put("bin5countersuu", "bin5countersuu");// BIN5 ｶｳﾝﾀｰ数
            mapping.put("bin5gosaritu", "bin5gosaritu");// BIN5 誤差率(%)
            mapping.put("bin5masinfuryouritu", "bin5masinfuryouritu");// BIN5 ﾏｼﾝ不良率(%)
            mapping.put("bin5nukitorikekka", "bin5nukitorikekka");// BIN5 抜き取り結果(子数)
            mapping.put("bin5nukitorikekkabosuu", "bin5nukitorikekkabosuu");// BIN5 抜き取り結果(母数)
            mapping.put("bin5sinnofuryouritu", "bin5sinnofuryouritu");// BIN5 真の不良率(%)
            mapping.put("bin5kekkacheck", "bin5kekkacheck");// BIN5 結果ﾁｪｯｸ
            mapping.put("bin5fukurocheck", "bin5fukurocheck");// BIN5 袋ﾁｪｯｸ
            mapping.put("bin6setteiti", "bin6setteiti");// BIN6 %区分(設定値)
            mapping.put("bin6senbetukubun", "bin6senbetukubun");// BIN6 選別区分
            mapping.put("bin6keiryougosuryou", "bin6keiryougosuryou");// BIN6 計量後数量
            mapping.put("bin6countersuu", "bin6countersuu");// BIN6 ｶｳﾝﾀｰ数
            mapping.put("bin6gosaritu", "bin6gosaritu");// BIN6 誤差率(%)
            mapping.put("bin6masinfuryouritu", "bin6masinfuryouritu");// BIN6 ﾏｼﾝ不良率(%)
            mapping.put("bin6nukitorikekka", "bin6nukitorikekka");// BIN6 抜き取り結果(子数)
            mapping.put("bin6nukitorikekkabosuu", "bin6nukitorikekkabosuu");// BIN6 抜き取り結果(母数)
            mapping.put("bin6sinnofuryouritu", "bin6sinnofuryouritu");// BIN6 真の不良率(%)
            mapping.put("bin6kekkacheck", "bin6kekkacheck");// BIN6 結果ﾁｪｯｸ
            mapping.put("bin6fukurocheck", "bin6fukurocheck");// BIN6 袋ﾁｪｯｸ
            mapping.put("bin7setteiti", "bin7setteiti");// BIN7 %区分(設定値)
            mapping.put("bin7senbetukubun", "bin7senbetukubun");// BIN7 選別区分
            mapping.put("bin7keiryougosuryou", "bin7keiryougosuryou");// BIN7 計量後数量
            mapping.put("bin7countersuu", "bin7countersuu");// BIN7 ｶｳﾝﾀｰ数
            mapping.put("bin7gosaritu", "bin7gosaritu");// BIN7 誤差率(%)
            mapping.put("bin7masinfuryouritu", "bin7masinfuryouritu");// BIN7 ﾏｼﾝ不良率(%)
            mapping.put("bin7nukitorikekka", "bin7nukitorikekka");// BIN7 抜き取り結果(子数)
            mapping.put("bin7nukitorikekkabosuu", "bin7nukitorikekkabosuu");// BIN7 抜き取り結果(母数)
            mapping.put("bin7sinnofuryouritu", "bin7sinnofuryouritu");// BIN7 真の不良率(%)
            mapping.put("bin7kekkacheck", "bin7kekkacheck");// BIN7 結果ﾁｪｯｸ
            mapping.put("bin7fukurocheck", "bin7fukurocheck");// BIN7 袋ﾁｪｯｸ
            mapping.put("bin8setteiti", "bin8setteiti");// BIN8 %区分(設定値)
            mapping.put("bin8senbetukubun", "bin8senbetukubun");// BIN8 選別区分
            mapping.put("bin8keiryougosuryou", "bin8keiryougosuryou");// BIN8 計量後数量
            mapping.put("bin8countersuu", "bin8countersuu");// BIN8 ｶｳﾝﾀｰ数
            mapping.put("bin8gosaritu", "bin8gosaritu");// BIN8 誤差率(%)
            mapping.put("bin8masinfuryouritu", "bin8masinfuryouritu");// BIN8 ﾏｼﾝ不良率(%)
            mapping.put("bin8nukitorikekka", "bin8nukitorikekka");// BIN8 抜き取り結果(子数)
            mapping.put("bin8nukitorikekkabosuu", "bin8nukitorikekkabosuu");// BIN8 抜き取り結果(母数)
            mapping.put("bin8sinnofuryouritu", "bin8sinnofuryouritu");// BIN8 真の不良率(%)
            mapping.put("bin8kekkacheck", "bin8kekkacheck");// BIN8 結果ﾁｪｯｸ
            mapping.put("bin8fukurocheck", "bin8fukurocheck");// BIN8 袋ﾁｪｯｸ
            mapping.put("bin9keiryougosuryou", "bin9keiryougosuryou");// BIN9 強制排出 計量後数量
            mapping.put("bin9masinfuryouritu", "bin9masinfuryouritu");// BIN9 強制排出 ﾏｼﾝ不良率
            mapping.put("rakkakeiryougosuryou", "rakkakeiryougosuryou");// 落下 計量後数量 
            mapping.put("rakkamasinfuryouritu", "rakkamasinfuryouritu");// 落下 ﾏｼﾝ不良率
            mapping.put("handasample", "handasample");// 半田ｻﾝﾌﾟﾙ
            mapping.put("sinraiseisample", "sinraiseisample");// 信頼性ｻﾝﾌﾟﾙ
            mapping.put("satsample", "satsample");// SATｻﾝﾌﾟﾙ
            mapping.put("sinfuryouhanteisya", "sinfuryouhanteisya");// 真不良判定者
            mapping.put("hanteinyuuryokusya", "hanteinyuuryokusya");// 判定入力者
            mapping.put("toridasisya", "toridasisya");// 取出者
            mapping.put("kousa1", "kousa1");// 公差①
            mapping.put("juryou1", "juryou1");// 重量①
            mapping.put("kosuu1", "kosuu1");// 個数①
            mapping.put("kousa2", "kousa2");// 公差②
            mapping.put("juryou2", "juryou2");// 重量②
            mapping.put("kosuu2", "kosuu2");// 個数②
            mapping.put("kousa3", "kousa3");// 公差③
            mapping.put("juryou3", "juryou3");// 重量③
            mapping.put("kosuu3", "kosuu3");// 個数③
            mapping.put("kousa4", "kousa4");// 公差④
            mapping.put("juryou4", "juryou4");// 重量④
            mapping.put("kosuu4", "kosuu4");// 個数④
            mapping.put("countersousuu", "countersousuu");// 計量総数
            mapping.put("ryohinjuryou", "ryohinjuryou");// 良品重量
            mapping.put("ryohinkosuu", "ryohinkosuu");// 良品個数
            mapping.put("budomari", "budomari");// 歩留まり
            mapping.put("binkakuninsya", "binkakuninsya");// BIN確認者
            mapping.put("saiken", "saiken");// 電気特性再検
            mapping.put("setubikubun", "setubikubun");// 設備区分

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B053Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B053Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b053.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "電気特性");

            // ダウンロードファイル名
            String downloadFileName = "電気特性_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        //検査場所データリスト
        List<String> kensaBasyoDataList= new ArrayList<>(Arrays.asList(possibleData));
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
        String paramGoki = null;
        if (!StringUtil.isEmpty(goki)) {
            paramGoki = DBUtil.escapeString(getGoki()) + "%";
        }
        Date paramSenbetuStartDateF = null;
        if (!StringUtil.isEmpty(senbetuStartDateF)) {
            paramSenbetuStartDateF = DateUtil.convertStringToDate(getSenbetuStartDateF(), StringUtil.isEmpty(getSenbetuStartTimeF()) ? "0000" : getSenbetuStartTimeF());
        }
        Date paramSenbetuStartDateT = null;
        if (!StringUtil.isEmpty(senbetuStartDateT)) {
            paramSenbetuStartDateT = DateUtil.convertStringToDate(getSenbetuStartDateT(), StringUtil.isEmpty(getSenbetuStartTimeT()) ? "2359" : getSenbetuStartTimeT());
        }
        Date paramSenbetuEndDateF = null;
        if (!StringUtil.isEmpty(senbetuEndDateF)) {
            paramSenbetuEndDateF = DateUtil.convertStringToDate(getSenbetuEndDateF(), StringUtil.isEmpty(getSenbetuEndTimeF()) ? "0000" : getSenbetuEndTimeF());
        }
        Date paramSenbetuEndDateT = null;
        if (!StringUtil.isEmpty(senbetuEndDateT)) {
            paramSenbetuEndDateT = DateUtil.convertStringToDate(getSenbetuEndDateT(), StringUtil.isEmpty(getSenbetuEndTimeT()) ? "2359" : getSenbetuEndTimeT());
        }
        
        String paramSetubiKubun = "GXHDO101B053";   


        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        params.addAll(Arrays.asList(paramGoki, paramGoki));
                for (String data : kensaBasyoDataList) {
            if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                params.addAll(kensaBasyoDataList);
            }
        }
        params.addAll(Arrays.asList(paramSenbetuStartDateF, paramSenbetuStartDateF));
        params.addAll(Arrays.asList(paramSenbetuStartDateT, paramSenbetuStartDateT));
        params.addAll(Arrays.asList(paramSenbetuEndDateF, paramSenbetuEndDateF));
        params.addAll(Arrays.asList(paramSenbetuEndDateT, paramSenbetuEndDateT));
        params.addAll(Arrays.asList(paramSetubiKubun, paramSetubiKubun));

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
    
    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @return 取得データ
     */
    private Map loadFxhbm03Data(List<String> userGrpList) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 WHERE ";
            sql = sql + DBUtil.getInConditionPreparedStatement("user_name", userGrpList.size());
            sql = sql + " AND key = '電気特性履歴_表示可能ﾃﾞｰﾀ'";

            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            return queryRunner.query(sql, new MapHandler(), userGrpList.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
    }
    
    /**
     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
     *
     * @param map マップ
     * @param mapId ID
     * @return マップから取得した値
     */
    private Object getMapData(Map map, String mapId) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.get(mapId);
    }
}
