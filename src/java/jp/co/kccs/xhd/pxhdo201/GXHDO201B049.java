/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO201B049Model;
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
 * 変更日	2019/03/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾃｰﾋﾟﾝｸﾞﾁｪｯｸ履歴検索画面
 *
 * @author 863 K.Zhang
 * @since  2020/03/02
 */
@Named
@ViewScoped
public class GXHDO201B049 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B049.class.getName());
    
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
    private List<GXHDO201B049Model> listData = null;
    
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
    /** 検索条件：検査場所 */
    private String kensaBasyo = "";
    /** 検索条件：検査開始日(FROM) */
    private String kensaStartDateF = "";
    /** 検索条件：検査開始日(TO) */
    private String kensaStartDateT = "";
    /** 検索条件：検査開始時刻(FROM) */
    private String kensaStartTimeF = "";
    /** 検索条件：検査開始時刻(TO) */
    private String kensaStartTimeT = "";
    /** 検索条件：検査終了日(FROM) */
    private String kensaEndDateF = "";
    /** 検索条件：検査終了日(TO) */
    private String kensaEndDateT = "";
    /** 検索条件：検査終了時刻(FROM) */
    private String kensaEndTimeF = "";
    /** 検索条件：検査終了時刻(TO) */
    private String kensaEndTimeT = "";
    //検査場所リスト:表示可能ﾃﾞｰﾀ
    private String kensabasyoData[];
    
    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B049() {
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
    public List<GXHDO201B049Model> getListData() {
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
     * 検索条件：検査開始日(from)
     * @return the kensaStartDateF
     */
    public String getKensaStartDateF() {
        return kensaStartDateF;
    }

    /**
     * 検索条件：検査開始日(from)
     * @param kensaStartDateF the kensaStartDateF to set
     */
    public void setKensaStartDateF(String kensaStartDateF) {
        this.kensaStartDateF = kensaStartDateF;
    }

    /**
     * 検索条件：検査開始日(to)
     * @return the kensaStartDateT
     */
    public String getKensaStartDateT() {
        return kensaStartDateT;
    }

    /**
     * 検索条件：検査開始日(to)
     * @param kensaStartDateT the kensaStartDateT to set
     */
    public void setKensaStartDateT(String kensaStartDateT) {
        this.kensaStartDateT = kensaStartDateT;
    }

    /**
     * 検索条件：検査開始時刻(from)
     * @return the kensaStartTimeF
     */
    public String getKensaStartTimeF() {
        return kensaStartTimeF;
    }

    /**
     * 検索条件：検査開始時刻(from)
     * @param kensaStartTimeF the kensaStartTimeF to set
     */
    public void setKensaStartTimeF(String kensaStartTimeF) {
        this.kensaStartTimeF = kensaStartTimeF;
    }

    /**
     * 検索条件：検査開始時刻(to)
     * @return the kensaStartTimeT
     */
    public String getKensaStartTimeT() {
        return kensaStartTimeT;
    }

    /**
     * 検索条件：検査開始時刻(to)
     * @param kensaStartTimeT the kensaStartTimeT to set
     */
    public void setKensaStartTimeT(String kensaStartTimeT) {
        this.kensaStartTimeT = kensaStartTimeT;
    }

    /**
     * 検索条件：検査終了日(from)
     * @return the kensaEndDateF
     */
    public String getKensaEndDateF() {
        return kensaEndDateF;
    }

    /**
     * 検索条件：検査終了日(from)
     * @param kensaEndDateF the kensaEndDateF to set
     */
    public void setKensaEndDateF(String kensaEndDateF) {
        this.kensaEndDateF = kensaEndDateF;
    }

    /**
     * 検索条件：検査終了日(to)
     * @return the kensaEndDateT
     */
    public String getKensaEndDateT() {
        return kensaEndDateT;
    }

    /**
     * 検索条件：検査終了日(to)
     * @param kensaEndDateT the kensaEndDateT to set
     */
    public void setKensaEndDateT(String kensaEndDateT) {
        this.kensaEndDateT = kensaEndDateT;
    }

    /**
     * 検索条件：検査終了時刻(from)
     * @return the kensaEndTimeF
     */
    public String getKensaEndTimeF() {
        return kensaEndTimeF;
    }

    /**
     * 検索条件：検査終了時刻(from)
     * @param kensaEndTimeF the kensaEndTimeF to set
     */
    public void setKensaEndTimeF(String kensaEndTimeF) {
        this.kensaEndTimeF = kensaEndTimeF;
    }

    /**
     * 検索条件：検査終了時刻(to)
     * @return the kensaEndTimeT
     */
    public String getKensaEndTimeT() {
        return kensaEndTimeT;
    }
    
    /**
     * 検索条件：検査終了時刻(to)
     * @param kensaEndTimeT the kensaEndTimeT to set
     */
    public void setKensaEndTimeT(String kensaEndTimeT) {
        this.kensaEndTimeT = kensaEndTimeT;
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
    
    /**
     * 検査場所リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the kensabasyoData
     */
    public String[] getKensabasyoData() {
        return kensabasyoData;
    }

    /**
     * 検査場所リスト:表示可能ﾃﾞｰﾀ
     *
     * @param kensabasyoData the kensabasyoData to set
     */
    public void setKensabasyoData(String[] kensabasyoData) {
        this.kensabasyoData = kensabasyoData;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B049_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B049_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
            
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List7 = "";
        //■表示可能ﾃﾞｰﾀ取得処理
        //①Ⅲ.画面表示仕様(7)を発行する。
        Map fxhbm03Data7 = loadFxhbm03Data(userGrpList);
        //  1.取得できなかった場合、ｴﾗｰ。以降の処理は実行しない。
        if (fxhbm03Data7 == null || fxhbm03Data7.isEmpty()) {
            // ・ｴﾗｰｺｰﾄﾞ:XHD-000183
            // ・ｴﾗ-ﾒｯｾｰｼﾞ:ﾃｰﾋﾟﾝｸﾞﾁｪｯｸ履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
            // メッセージを画面に渡す
            settingError();
            return;
        } else {
            //取得したﾃﾞｰﾀが NULL の場合、ｴﾗｰ。以降の処理は実行しない。
            if (getMapData(fxhbm03Data7, "data") == null || "".equals(getMapData(fxhbm03Data7, "data"))) {
                //・ｴﾗｰｺｰﾄﾞ:XHD-000183
                //・ｴﾗ-ﾒｯｾｰｼﾞ:ﾃｰﾋﾟﾝｸﾞﾁｪｯｸ履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
                settingError();
                return;
            } else {
                //取得したﾃﾞｰﾀが ALL以外の場合
                strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                kensabasyoData = strfxhbm03List7.split(",");
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
        messageList.add(MessageUtil.getMessage("XHD-000183"));
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
        kensaBasyo = "";
        kensaStartDateF = "";
        kensaStartDateT = "";
        kensaStartTimeF = "";
        kensaStartTimeT = "";
        kensaEndDateF = "";
        kensaEndDateT = "";
        kensaEndTimeF = "";
        kensaEndTimeT = "";
        
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
        //検査場所
        // 仕様変更:画面項目[検査場所]の属性がComboBoxを変更した、チェック処理に意味がない
        //if (existError(validateUtil.checkC101(getKensaBasyo(), "検査場所", 4))) {
        //    return;
        //} 
        // 検査開始日(FROM)
        if (existError(validateUtil.checkC101(getKensaStartDateF(), "検査開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartDateF(), "検査開始日(from)")) ||
            existError(validateUtil.checkC501(getKensaStartDateF(), "検査開始日(from)"))) {
            return;
        }
        // 検査開始日(TO)
        if (existError(validateUtil.checkC101(getKensaStartDateT(), "検査開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartDateT(), "検査開始日(to)")) ||
            existError(validateUtil.checkC501(getKensaStartDateT(), "検査開始日(to)"))) {
            return;
        }
        // 検査終了日(FROM)
        if (existError(validateUtil.checkC101(getKensaEndDateF(), "検査終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndDateF(), "検査終了日(from)")) ||
            existError(validateUtil.checkC501(getKensaEndDateF(), "検査終了日(from)"))) {
            return;
        }
        // 検査終了日(TO)
        if (existError(validateUtil.checkC101(getKensaEndDateT(), "検査終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndDateT(), "検査終了日(to)")) ||
            existError(validateUtil.checkC501(getKensaEndDateT(), "検査終了日(to)"))) {
            return;
        }
        // 検査開始時刻(FROM)
        if (existError(validateUtil.checkC101(getKensaStartTimeF(), "検査開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartTimeF(), "検査開始時刻(from)")) ||
            existError(validateUtil.checkC502(getKensaStartTimeF(), "検査開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaStartTimeF) && existError(validateUtil.checkC001(getKensaStartDateF(), "検査開始日(from)"))) {
            return;
        }
        // 検査開始時刻(TO)
        if (existError(validateUtil.checkC101(getKensaStartTimeT(), "検査開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartTimeT(), "検査開始時刻(to)")) ||
            existError(validateUtil.checkC502(getKensaStartTimeT(), "検査開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaStartTimeT) && existError(validateUtil.checkC001(getKensaStartDateT(), "検査開始日(to)"))) {
            return;
        }
        // 検査終了時刻(FROM)
        if (existError(validateUtil.checkC101(getKensaEndTimeF(), "検査終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndTimeF(), "検査終了時刻(from)")) ||
            existError(validateUtil.checkC502(getKensaEndTimeF(), "検査終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaEndTimeF) && existError(validateUtil.checkC001(getKensaEndDateF(), "検査終了日(from)"))) {
            return;
        }
        // 検査終了時刻(TO)
        if (existError(validateUtil.checkC101(getKensaEndTimeT(), "検査終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndTimeT(), "検査終了時刻(to)")) ||
            existError(validateUtil.checkC502(getKensaEndTimeT(), "検査終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaEndTimeT) && existError(validateUtil.checkC001(getKensaEndDateT(), "検査終了日(to)"))) {
            return;
        }       
        
        if (kensabasyoData == null) {
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
        //検査場所データリスト
        List<String> kensaBasyoDataList = null;
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM sr_taping_check "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') ";

            if (!StringUtil.isEmpty(kensaBasyo) && !"ALL".equals(kensaBasyo)) {
                kensaBasyoDataList = new ArrayList<>();
                kensaBasyoDataList.add(kensaBasyo);
            } else {
                boolean jyokenAdd = false;
                for (String data : kensabasyoData) {
                    if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                        jyokenAdd = true;
                    }
                }
                if (jyokenAdd) {
                    kensaBasyoDataList = new ArrayList<>(Arrays.asList(kensabasyoData));
                }
            }
            if (kensaBasyoDataList != null && !kensaBasyoDataList.isEmpty()) {
                sql += " AND ";
                sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
            }
            
            sql += " AND   (? IS NULL OR kensakaisinichiji >= ?) "
                    + " AND   (? IS NULL OR kensakaisinichiji <= ?) "
                    + " AND   (? IS NULL OR kensasyuryonichiji >= ?) "
                    + " AND   (? IS NULL OR kensasyuryonichiji <= ?) ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");

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
            //検査場所データリスト
            List<String> kensaBasyoDataList = null;  
            String sql = "SELECT CONCAT(IFNULL(T1.kojyo, ''), IFNULL(T1.lotno, ''), IFNULL(T1.edaban, '')) AS LOTNO "
                    + ", T1.kaisuu "
                    + ", T1.kcpno "
                    + ", T1.tokuisaki "
                    + ", T1.lotkubuncode "
                    + ", T1.ownercode "
                    + ", T1.ryouhintopreelmaki1 "
                    + ", T1.ryouhintopreelhonsu1 "
                    + ", T1.ryouhintopreelmaki2 "
                    + ", T1.ryouhintopreelhonsu2 "
                    + ", T1.tapinggouki "
                    + ", T1.kensabasyo "
                    + ", T1.reelchecksu "
                    + ", T1.kensakaisinichiji "
                    + ", T1.kensakaisitantou "
                    + ", T1.monotati "
                    + ", T1.hakuri "
                    + ", T1.hanuke "
                    + ", T1.rabure "
                    + ", T1.kakeng "
                    + ", T1.dipfuryo "
                    + ", T1.sonota "
                    + ", T1.tapeijyo "
                    + ", T1.reelcheckkekka "
                    + ", T1.kensasyuryonichiji "
                    + ", T1.kensasyuryotantou "
                    + ", T1.tapeng1 "
                    + ", T1.tapeng2 "
                    + ", T1.denkitokuseisaikensa "
                    + ", T1.gaikansaikensa "
                    + ", T1.bikou1 "
                    + ", T1.bikou2 "
                    + ", T8.jissekino AS tpng1jissekino "
                    + ", T8.kensabi AS tpng1kensabi "
                    + ", (CASE WHEN T8.kensasya IS NULL THEN '' WHEN T8.kensasya = '' THEN '' ELSE T8.kensasya END) AS tpng1kensasya "
                    + ", (CASE WHEN T8.kensakubun = '1' THEN '容量' WHEN T8.kensakubun = '2' THEN '絶縁抵抗' ELSE '' END) AS tpng1kensakubun "
                    + ", T8.tpkosuu AS tpng1tpkosuu "
                    + ", T8.ng1kosuu AS tpng1ng1kosuu "
                    + ", T8.nukitorikosuu AS tpng1nukitorikosuu "
                    + ", T8.cap_ir_ngkosuu AS tpng1cap_ir_ngkosuu "
                    + ", T8.shortkosuu AS tpng1shortkosuu "
                    + ", T8.etckosuu AS tpng1etckosuu "
                    + ", (CASE WHEN T8.hantei = '1' THEN 'OK' WHEN T8.hantei = '2' THEN 'NG' ELSE '' END) AS tpng1hantei "
                    + ", (CASE WHEN T8.biko1 IS NULL THEN '' WHEN T8.biko1 = '' THEN '' ELSE T8.biko1 END) AS tpng1biko1 "
                    + ", (CASE WHEN T8.biko2 IS NULL THEN '' WHEN T8.biko2 = '' THEN '' ELSE T8.biko2 END) AS tpng1biko2 "
                    + ", T9.jissekino AS tpng2jissekino "
                    + ", T9.kensabi AS tpng2kensabi "
                    + ", (CASE WHEN T9.kensasya IS NULL THEN '' WHEN T9.kensasya = '' THEN '' ELSE T9.kensasya END) AS tpng2kensasya "
                    + ", (CASE WHEN T9.kensakubun = '1' THEN '流れ品' WHEN T9.kensakubun = '2' THEN 'ﾊﾞﾗｼ品' ELSE '' END) AS tpng2kensakubun "
                    + ", T9.tpkosuu AS tpng2tpkosuu "
                    + ", T9.ng2kosuu AS tpng2ng2kosuu "
                    + ", T9.nukitorikosuu AS tpng2nukitorikosuu "
                    + ", T9.a_modekosuu AS tpng2a_modekosuu "
                    + ", T9.c_modekosuu AS tpng2c_modekosuu "
                    + ", (CASE WHEN T9.hantei = '1' THEN 'OK' WHEN T9.hantei = '2' THEN 'NG' ELSE '' END) AS tpng2hantei "
                    + ", (CASE WHEN T9.biko1 IS NULL THEN '' WHEN T9.biko1 = '' THEN '' ELSE T9.biko1 END) AS tpng2biko1 "
                    + ", (CASE WHEN T9.biko2 IS NULL THEN '' WHEN T9.biko2 = '' THEN '' ELSE T9.biko2 END) AS tpng2biko2 "
                    + ", T9.d_modekosuu AS tpng2d_modekosuu "
                    + ", T9.etckosuu AS tpng2etckosuu "
                    + ", T9.dhagarekosuu AS tpng2dhagarekosuu "
                    + ", T9.dkizukosuu AS tpng2dkizukosuu "
                    + ", T9.jcrackkosuu AS tpng2jcrackkosuu "
                    + ", T9.mekkinasikosuu AS tpng2mekkinasikosuu "
                    + ", T9.mekkiukikosuu AS tpng2mekkiukikosuu "
                    + ", T10.jissekino AS tpng1jissekino2 "
                    + ", T10.kensabi AS tpng1kensabi2 "
                    + ", (CASE WHEN T10.kensasya IS NULL THEN '' WHEN T10.kensasya = '' THEN '' ELSE T10.kensasya END) AS tpng1kensasya2 "
                    + ", (CASE WHEN T10.kensakubun = '1' THEN '容量' WHEN T10.kensakubun = '2' THEN '絶縁抵抗' ELSE '' END) AS tpng1kensakubun2 "
                    + ", T10.tpkosuu AS tpng1tpkosuu2 "
                    + ", T10.ng1kosuu AS tpng1ng1kosuu2 "
                    + ", T10.nukitorikosuu AS tpng1nukitorikosuu2 "
                    + ", T10.cap_ir_ngkosuu AS tpng1cap_ir_ngkosuu2 "
                    + ", T10.shortkosuu AS tpng1shortkosuu2 "
                    + ", T10.etckosuu AS tpng1etckosuu2 "
                    + ", (CASE WHEN T10.hantei = '1' THEN 'OK' WHEN T10.hantei = '2' THEN 'NG' ELSE '' END) AS tpng1hantei2 "
                    + ", (CASE WHEN T10.biko1 IS NULL THEN '' WHEN T10.biko1 = '' THEN '' ELSE T10.biko1 END) AS tpng1biko1_2 "
                    + ", (CASE WHEN T10.biko2 IS NULL THEN '' WHEN T10.biko2 = '' THEN '' ELSE T10.biko2 END) AS tpng1biko2_2 "
                    + ", T11.jissekino AS tpng2jissekino2 "
                    + ", T11.kensabi AS tpng2kensabi2 "
                    + ", (CASE WHEN T11.kensasya IS NULL THEN '' WHEN T11.kensasya = '' THEN '' ELSE T11.kensasya END) AS tpng2kensasya2 "
                    + ", (CASE WHEN T11.kensakubun = '1' THEN '流れ品' WHEN T11.kensakubun = '2' THEN 'ﾊﾞﾗｼ品' ELSE '' END) AS tpng2kensakubun2 "
                    + ", T11.tpkosuu AS tpng2tpkosuu2 "
                    + ", T11.ng2kosuu AS tpng2ng2kosuu2 "
                    + ", T11.nukitorikosuu AS tpng2nukitorikosuu2 "
                    + ", T11.a_modekosuu AS tpng2a_modekosuu2 "
                    + ", T11.c_modekosuu AS tpng2c_modekosuu2 "
                    + ", (CASE WHEN T11.hantei = '1' THEN 'OK' WHEN T11.hantei = '2' THEN 'NG' ELSE '' END) AS tpng2hantei2 "
                    + ", (CASE WHEN T11.biko1 IS NULL THEN '' WHEN T11.biko1 = '' THEN '' ELSE T11.biko1 END) AS tpng2biko1_2 "
                    + ", (CASE WHEN T11.biko2 IS NULL THEN '' WHEN T11.biko2 = '' THEN '' ELSE T11.biko2 END) AS tpng2biko2_2 "
                    + ", T11.d_modekosuu AS tpng2d_modekosuu2 "
                    + ", T11.etckosuu AS tpng2etckosuu2 "
                    + ", T11.dhagarekosuu AS tpng2dhagarekosuu2 "
                    + ", T11.dkizukosuu AS tpng2dkizukosuu2 "
                    + ", T11.jcrackkosuu AS tpng2jcrackkosuu2 "
                    + ", T11.mekkinasikosuu AS tpng2mekkinasikosuu2 "
                    + ", T11.mekkiukikosuu AS tpng2mekkiukikosuu2 "
                    + ", T12.jissekino AS tpng1jissekino3 "
                    + ", T12.kensabi AS tpng1kensabi3 "
                    + ", (CASE WHEN T12.kensasya IS NULL THEN '' WHEN T12.kensasya = '' THEN '' ELSE T12.kensasya END) AS tpng1kensasya3 "
                    + ", (CASE WHEN T12.kensakubun = '1' THEN '容量' WHEN T12.kensakubun = '2' THEN '絶縁抵抗' ELSE '' END) AS tpng1kensakubun3 "
                    + ", T12.tpkosuu AS tpng1tpkosuu3 "
                    + ", T12.ng1kosuu AS tpng1ng1kosuu3 "
                    + ", T12.nukitorikosuu AS tpng1nukitorikosuu3 "
                    + ", T12.cap_ir_ngkosuu AS tpng1cap_ir_ngkosuu3 "
                    + ", T12.shortkosuu AS tpng1shortkosuu3 "
                    + ", T12.etckosuu AS tpng1etckosuu3 "
                    + ", (CASE WHEN T12.hantei = '1' THEN 'OK' WHEN T12.hantei = '2' THEN 'NG' ELSE '' END) AS tpng1hantei3 "
                    + ", (CASE WHEN T12.biko1 IS NULL THEN '' WHEN T12.biko1 = '' THEN '' ELSE T12.biko1 END) AS tpng1biko1_3 "
                    + ", (CASE WHEN T12.biko2 IS NULL THEN '' WHEN T12.biko2 = '' THEN '' ELSE T12.biko2 END) AS tpng1biko2_3 "
                    + ", T13.jissekino AS tpng2jissekino3 "
                    + ", T13.kensabi AS tpng2kensabi3 "
                    + ", (CASE WHEN T13.kensasya IS NULL THEN '' WHEN T13.kensasya = '' THEN '' ELSE T13.kensasya END) AS tpng2kensasya3 "
                    + ", (CASE WHEN T13.kensakubun = '1' THEN '流れ品' WHEN T13.kensakubun = '2' THEN 'ﾊﾞﾗｼ品' ELSE '' END) AS tpng2kensakubun3 "
                    + ", T13.tpkosuu AS tpng2tpkosuu3 "
                    + ", T13.ng2kosuu AS tpng2ng2kosuu3 "
                    + ", T13.nukitorikosuu AS tpng2nukitorikosuu3 "
                    + ", T13.a_modekosuu AS tpng2a_modekosuu3 "
                    + ", T13.c_modekosuu AS tpng2c_modekosuu3 "
                    + ", (CASE WHEN T13.hantei = '1' THEN 'OK' WHEN T13.hantei = '2' THEN 'NG' ELSE '' END) AS tpng2hantei3 "
                    + ", (CASE WHEN T13.biko1 IS NULL THEN '' WHEN T13.biko1 = '' THEN '' ELSE T13.biko1 END) AS tpng2biko1_3 "
                    + ", (CASE WHEN T13.biko2 IS NULL THEN '' WHEN T13.biko2 = '' THEN '' ELSE T13.biko2 END) AS tpng2biko2_3 "
                    + ", T13.d_modekosuu AS tpng2d_modekosuu3 "
                    + ", T13.etckosuu AS tpng2etckosuu3 "
                    + ", T13.dhagarekosuu AS tpng2dhagarekosuu3 "
                    + ", T13.dkizukosuu AS tpng2dkizukosuu3 "
                    + ", T13.jcrackkosuu AS tpng2jcrackkosuu3 "
                    + ", T13.mekkinasikosuu AS tpng2mekkinasikosuu3 "
                    + ", T13.mekkiukikosuu AS tpng2mekkiukikosuu3 "
                    + "  FROM sr_taping_check T1 "
                    + "  LEFT JOIN sr_tpng1 T8 "
                    + "  ON T8.kojyo = T1.kojyo "
                    + "  AND T8.lotno = T1.lotno "
                    + "  AND T8.edaban = T1.edaban "
                    + "  AND T8.jissekino = 1 "
                    + "  LEFT JOIN sr_tpng2 T9 "
                    + "  ON T9.kojyo = T1.kojyo "
                    + "  AND T9.lotno = T1.lotno "
                    + "  AND T9.edaban = T1.edaban "
                    + "  AND T9.jissekino = 1 "
                    + "  LEFT JOIN sr_tpng1 T10 "
                    + "  ON T10.kojyo = T1.kojyo "
                    + "  AND T10.lotno = T1.lotno "
                    + "  AND T10.edaban = T1.edaban "
                    + "  AND T10.jissekino = 2 "
                    + "  LEFT JOIN sr_tpng2 T11 "
                    + "  ON T11.kojyo = T1.kojyo "
                    + "  AND T11.lotno = T1.lotno "
                    + "  AND T11.edaban = T1.edaban "
                    + "  AND T11.jissekino = 2 "
                    + "  LEFT JOIN sr_tpng1 T12 "
                    + "  ON T12.kojyo = T1.kojyo "
                    + "  AND T12.lotno = T1.lotno "
                    + "  AND T12.edaban = T1.edaban "
                    + "  AND T12.jissekino = 3 "
                    + "  LEFT JOIN sr_tpng2 T13 "
                    + "  ON T13.kojyo = T1.kojyo "
                    + "  AND T13.lotno = T1.lotno "
                    + "  AND T13.edaban = T1.edaban "
                    + "  AND T13.jissekino = 3"
                    + " WHERE (? IS NULL OR T1.kojyo = ?) "
                    + " AND   (? IS NULL OR T1.lotno = ?) "
                    + " AND   (? IS NULL OR T1.edaban = ?) "
                    + " AND   (? IS NULL OR T1.kcpno LIKE ? ESCAPE '\\\\') ";
            if (!StringUtil.isEmpty(kensaBasyo) && !"ALL".equals(kensaBasyo)) {
                kensaBasyoDataList = new ArrayList<>();
                kensaBasyoDataList.add(kensaBasyo);
            } else {
                boolean jyokenAdd = false;
                for (String data : kensabasyoData) {
                    if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                        jyokenAdd = true;
                    }
                }
                if (jyokenAdd) {
                    kensaBasyoDataList = new ArrayList<>(Arrays.asList(kensabasyoData));
                }
            }
            if (kensaBasyoDataList != null && !kensaBasyoDataList.isEmpty()) {
                sql += " AND ";
                sql += DBUtil.getInConditionPreparedStatement("T1.kensabasyo", kensaBasyoDataList.size());
            }
            sql += " AND   (? IS NULL OR T1.kensakaisinichiji >= ?) "
                    + " AND   (? IS NULL OR T1.kensakaisinichiji <= ?) "
                    + " AND   (? IS NULL OR T1.kensasyuryonichiji >= ?) "
                    + " AND   (? IS NULL OR T1.kensasyuryonichiji <= ?) "
                    + " ORDER BY T1.kensakaisinichiji ";            
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");//ﾛｯﾄNo.
            mapping.put("kcpno", "kcpno");//KCPNO
            mapping.put("tokuisaki", "tokuisaki");//客先
            mapping.put("lotkubuncode", "lotkubuncode");//ﾛｯﾄ区分
            mapping.put("ownercode", "ownercode");//ｵｰﾅｰ
            mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1");//良品TPﾘｰﾙ巻数①
            mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1");//良品TPﾘｰﾙ本数①
            mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2");//良品TPﾘｰﾙ巻数②
            mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2");//良品TPﾘｰﾙ本数②
            mapping.put("tapinggouki", "tapinggouki");//ﾃｰﾋﾟﾝｸﾞ号機
            mapping.put("kaisuu", "kaisuu");//検査回数
            mapping.put("kensabasyo", "kensabasyo");//検査場所
            mapping.put("reelchecksu", "reelchecksu");//ﾘｰﾙﾁｪｯｸ数
            mapping.put("kensakaisinichiji", "kensakaisinichiji");//検査開始日時
            mapping.put("kensakaisitantou", "kensakaisitantou");//検査開始担当者
            mapping.put("monotati", "monotati");//物立ち
            mapping.put("hakuri", "hakuri");//剥離
            mapping.put("hanuke", "hanuke");//歯抜け
            mapping.put("rabure", "rabure");//破れ
            mapping.put("kakeng", "kakeng");//ｶｹNG
            mapping.put("dipfuryo", "dipfuryo");//DIP不良
            mapping.put("sonota", "sonota");//その他
            mapping.put("tapeijyo", "tapeijyo");//ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
            mapping.put("reelcheckkekka", "reelcheckkekka");//ﾘｰﾙﾁｪｯｸ結果
            mapping.put("kensasyuryonichiji", "kensasyuryonichiji");//検査終了日時
            mapping.put("kensasyuryotantou", "kensasyuryotantou");//検査終了担当者
            mapping.put("tapeng1", "tapeng1");//TPNG1
            mapping.put("tapeng2", "tapeng2");//TPNG2
            mapping.put("denkitokuseisaikensa", "denkitokuseisaikensa");//電気特性再検査
            mapping.put("gaikansaikensa", "gaikansaikensa");//外観再検査
            mapping.put("bikou1", "bikou1");//備考1
            mapping.put("bikou2", "bikou2");//備考2
            mapping.put("tpng1jissekino", "tpng1jissekino");//TPNG1:何回目(1回目)
            mapping.put("tpng1kensabi", "tpng1kensabi");//TPNG1:検査日(1回目)
            mapping.put("tpng1kensakubun", "tpng1kensakubun");//TPNG1:検査区分(1回目)
            mapping.put("tpng1kensasya", "tpng1kensasya");//TPNG1:検査者(1回目)
            mapping.put("tpng1tpkosuu", "tpng1tpkosuu");//TPNG1:TP個数(1回目)
            mapping.put("tpng1ng1kosuu", "tpng1ng1kosuu");//TPNG1:NG1個数(1回目)
            mapping.put("tpng1nukitorikosuu", "tpng1nukitorikosuu");//TPNG1:抜き取り個数(1回目)
            mapping.put("tpng1cap_ir_ngkosuu", "tpng1cap_ir_ngkosuu");//TPNG1:容量/絶縁抵抗NG個数(1回目)
            mapping.put("tpng1shortkosuu", "tpng1shortkosuu");//TPNG1:ｼｮｰﾄ個数(1回目)
            mapping.put("tpng1etckosuu", "tpng1etckosuu");//TPNG1:その他個数(1回目)
            mapping.put("tpng1biko1", "tpng1biko1");//TPNG1:備考1(1回目)
            mapping.put("tpng1biko2", "tpng1biko2");//TPNG1:備考2(1回目)
            mapping.put("tpng1hantei", "tpng1hantei");//TPNG1:判定(1回目)
            mapping.put("tpng2jissekino", "tpng2jissekino");//TPNG2:何回目(1回目)
            mapping.put("tpng2kensabi", "tpng2kensabi");//TPNG2:検査日(1回目)
            mapping.put("tpng2kensakubun", "tpng2kensakubun");//TPNG2:検査区分(1回目)
            mapping.put("tpng2kensasya", "tpng2kensasya");//TPNG2:検査者(1回目)
            mapping.put("tpng2tpkosuu", "tpng2tpkosuu");//TPNG2:TP個数(1回目)
            mapping.put("tpng2ng2kosuu", "tpng2ng2kosuu");//TPNG2:NG2個数(1回目)
            mapping.put("tpng2nukitorikosuu", "tpng2nukitorikosuu");//TPNG2:抜き取り個数(1回目)
            mapping.put("tpng2a_modekosuu", "tpng2a_modekosuu");//TPNG2:Aﾓｰﾄﾞ個数(1回目)
            mapping.put("tpng2c_modekosuu", "tpng2c_modekosuu");//TPNG2:Cﾓｰﾄﾞ個数(1回目)
            mapping.put("tpng2d_modekosuu", "tpng2d_modekosuu");//TPNG2:Dﾓｰﾄﾞ個数(1回目)
            mapping.put("tpng2etckosuu", "tpng2etckosuu");//TPNG2:その他個数(1回目)
            mapping.put("tpng2dhagarekosuu", "tpng2dhagarekosuu");//TPNG2:端子電極ﾊｶﾞﾚ個数(1回目)
            mapping.put("tpng2dkizukosuu", "tpng2dkizukosuu");//TPNG2:端子電極ｷｽﾞ個数(1回目)
            mapping.put("tpng2jcrackkosuu", "tpng2jcrackkosuu");//TPNG2:磁器ｸﾗｯｸ個数(1回目)
            mapping.put("tpng2mekkinasikosuu", "tpng2mekkinasikosuu");//TPNG2:めっきなし個数(1回目)
            mapping.put("tpng2mekkiukikosuu", "tpng2mekkiukikosuu");//TPNG2:めっき浮き個数(1回目)
            mapping.put("tpng2biko1", "tpng2biko1");//TPNG2:備考1(1回目)
            mapping.put("tpng2biko2", "tpng2biko2");//TPNG2:備考2(1回目)
            mapping.put("tpng2hantei", "tpng2hantei");//TPNG2:判定(1回目)
            mapping.put("tpng1jissekino2", "tpng1jissekino2");//TPNG1:何回目(2回目)
            mapping.put("tpng1kensabi2", "tpng1kensabi2");//TPNG1:検査日(2回目)
            mapping.put("tpng1kensakubun2", "tpng1kensakubun2");//TPNG1:検査区分(2回目)
            mapping.put("tpng1kensasya2", "tpng1kensasya2");//TPNG1:検査者(2回目)
            mapping.put("tpng1tpkosuu2", "tpng1tpkosuu2");//TPNG1:TP個数(2回目)
            mapping.put("tpng1ng1kosuu2", "tpng1ng1kosuu2");//TPNG1:NG1個数(2回目)
            mapping.put("tpng1nukitorikosuu2", "tpng1nukitorikosuu2");//TPNG1:抜き取り個数(2回目)
            mapping.put("tpng1cap_ir_ngkosuu2", "tpng1cap_ir_ngkosuu2");//TPNG1:容量/絶縁抵抗NG個数(2回目)
            mapping.put("tpng1shortkosuu2", "tpng1shortkosuu2");//TPNG1:ｼｮｰﾄ個数(2回目)
            mapping.put("tpng1etckosuu2", "tpng1etckosuu2");//TPNG1:その他個数(2回目)
            mapping.put("tpng1biko1_2", "tpng1biko1_2");//TPNG1:備考1(2回目)
            mapping.put("tpng1biko2_2", "tpng1biko2_2");//TPNG1:備考2(2回目)
            mapping.put("tpng1hantei2", "tpng1hantei2");//TPNG1:判定(2回目)
            mapping.put("tpng2jissekino2", "tpng2jissekino2");//TPNG2:何回目(2回目)
            mapping.put("tpng2kensabi2", "tpng2kensabi2");//TPNG2:検査日(2回目)
            mapping.put("tpng2kensakubun2", "tpng2kensakubun2");//TPNG2:検査区分(2回目)
            mapping.put("tpng2kensasya2", "tpng2kensasya2");//TPNG2:検査者(2回目)
            mapping.put("tpng2tpkosuu2", "tpng2tpkosuu2");//TPNG2:TP個数(2回目)
            mapping.put("tpng2ng2kosuu2", "tpng2ng2kosuu2");//TPNG2:NG2個数(2回目)
            mapping.put("tpng2nukitorikosuu2", "tpng2nukitorikosuu2");//TPNG2:抜き取り個数(2回目)
            mapping.put("tpng2a_modekosuu2", "tpng2a_modekosuu2");//TPNG2:Aﾓｰﾄﾞ個数(2回目)
            mapping.put("tpng2c_modekosuu2", "tpng2c_modekosuu2");//TPNG2:Cﾓｰﾄﾞ個数(2回目)
            mapping.put("tpng2d_modekosuu2", "tpng2d_modekosuu2");//TPNG2:Dﾓｰﾄﾞ個数(2回目)
            mapping.put("tpng2etckosuu2", "tpng2etckosuu2");//TPNG2:その他個数(2回目)
            mapping.put("tpng2dhagarekosuu2", "tpng2dhagarekosuu2");//TPNG2:端子電極ﾊｶﾞﾚ個数(2回目)
            mapping.put("tpng2dkizukosuu2", "tpng2dkizukosuu2");//TPNG2:端子電極ｷｽﾞ個数(2回目)
            mapping.put("tpng2jcrackkosuu2", "tpng2jcrackkosuu2");//TPNG2:磁器ｸﾗｯｸ個数(2回目)
            mapping.put("tpng2mekkinasikosuu2", "tpng2mekkinasikosuu2");//TPNG2:めっきなし個数(2回目)
            mapping.put("tpng2mekkiukikosuu2", "tpng2mekkiukikosuu2");//TPNG2:めっき浮き個数(2回目)
            mapping.put("tpng2biko1_2", "tpng2biko1_2");//TPNG2:備考1(2回目)
            mapping.put("tpng2biko2_2", "tpng2biko2_2");//TPNG2:備考2(2回目)
            mapping.put("tpng2hantei2", "tpng2hantei2");//TPNG2:判定(2回目)
            mapping.put("tpng1jissekino3", "tpng1jissekino3");//TPNG1:何回目(3回目)
            mapping.put("tpng1kensabi3", "tpng1kensabi3");//TPNG1:検査日(3回目)
            mapping.put("tpng1kensakubun3", "tpng1kensakubun3");//TPNG1:検査区分(3回目)
            mapping.put("tpng1kensasya3", "tpng1kensasya3");//TPNG1:検査者(3回目)
            mapping.put("tpng1tpkosuu3", "tpng1tpkosuu3");//TPNG1:TP個数(3回目)
            mapping.put("tpng1ng1kosuu3", "tpng1ng1kosuu3");//TPNG1:NG1個数(3回目)
            mapping.put("tpng1nukitorikosuu3", "tpng1nukitorikosuu3");//TPNG1:抜き取り個数(3回目)
            mapping.put("tpng1cap_ir_ngkosuu3", "tpng1cap_ir_ngkosuu3");//TPNG1:容量/絶縁抵抗NG個数(3回目)
            mapping.put("tpng1shortkosuu3", "tpng1shortkosuu3");//TPNG1:ｼｮｰﾄ個数(3回目)
            mapping.put("tpng1etckosuu3", "tpng1etckosuu3");//TPNG1:その他個数(3回目)
            mapping.put("tpng1biko1_3", "tpng1biko1_3");//TPNG1:備考1(3回目)
            mapping.put("tpng1biko2_3", "tpng1biko2_3");//TPNG1:備考2(3回目)
            mapping.put("tpng1hantei3", "tpng1hantei3");//TPNG1:判定(3回目)
            mapping.put("tpng2jissekino3", "tpng2jissekino3");//TPNG2:何回目(3回目)
            mapping.put("tpng2kensabi3", "tpng2kensabi3");//TPNG2:検査日(3回目)
            mapping.put("tpng2kensakubun3", "tpng2kensakubun3");//TPNG2:検査区分(3回目)
            mapping.put("tpng2kensasya3", "tpng2kensasya3");//TPNG2:検査者(3回目)
            mapping.put("tpng2tpkosuu3", "tpng2tpkosuu3");//TPNG2:TP個数(3回目)
            mapping.put("tpng2ng2kosuu3", "tpng2ng2kosuu3");//TPNG2:NG2個数(3回目)
            mapping.put("tpng2nukitorikosuu3", "tpng2nukitorikosuu3");//TPNG2:抜き取り個数(3回目)
            mapping.put("tpng2a_modekosuu3", "tpng2a_modekosuu3");//TPNG2:Aﾓｰﾄﾞ個数(3回目)
            mapping.put("tpng2c_modekosuu3", "tpng2c_modekosuu3");//TPNG2:Cﾓｰﾄﾞ個数(3回目)
            mapping.put("tpng2d_modekosuu3", "tpng2d_modekosuu3");//TPNG2:Dﾓｰﾄﾞ個数(3回目)
            mapping.put("tpng2etckosuu3", "tpng2etckosuu3");//TPNG2:その他個数(3回目)
            mapping.put("tpng2dhagarekosuu3", "tpng2dhagarekosuu3");//TPNG2:端子電極ﾊｶﾞﾚ個数(3回目)
            mapping.put("tpng2dkizukosuu3", "tpng2dkizukosuu3");//TPNG2:端子電極ｷｽﾞ個数(3回目)
            mapping.put("tpng2jcrackkosuu3", "tpng2jcrackkosuu3");//TPNG2:磁器ｸﾗｯｸ個数(3回目)
            mapping.put("tpng2mekkinasikosuu3", "tpng2mekkinasikosuu3");//TPNG2:めっきなし個数(3回目)
            mapping.put("tpng2mekkiukikosuu3", "tpng2mekkiukikosuu3");//TPNG2:めっき浮き個数(3回目)
            mapping.put("tpng2biko1_3", "tpng2biko1_3");//TPNG2:備考1(3回目)
            mapping.put("tpng2biko2_3", "tpng2biko2_3");//TPNG2:備考2(3回目)
            mapping.put("tpng2hantei3", "tpng2hantei3");//TPNG2:判定(3回目)
                               
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B049Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B049Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b049.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "テーピングチェック");

            // ダウンロードファイル名
            String downloadFileName = "テーピングチェック_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        List<String> kensaBasyoDataList= null;
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
        Date paramKensaStartDateF = null;
        if (!StringUtil.isEmpty(kensaStartDateF)) {
            paramKensaStartDateF = DateUtil.convertStringToDate(getKensaStartDateF(), StringUtil.isEmpty(getKensaStartTimeF()) ? "0000" : getKensaStartTimeF());
        }
        Date paramKensaStartDateT = null;
        if (!StringUtil.isEmpty(kensaStartDateT)) {
            paramKensaStartDateT = DateUtil.convertStringToDate(getKensaStartDateT(), StringUtil.isEmpty(getKensaStartTimeT()) ? "2359" : getKensaStartTimeT());
        }
        Date paramKensaEndDateF = null;
        if (!StringUtil.isEmpty(kensaEndDateF)) {
            paramKensaEndDateF = DateUtil.convertStringToDate(getKensaEndDateF(), StringUtil.isEmpty(getKensaEndTimeF()) ? "0000" : getKensaEndTimeF());
        }
        Date paramKensaEndDateT = null;
        if (!StringUtil.isEmpty(kensaEndDateT)) {
            paramKensaEndDateT = DateUtil.convertStringToDate(getKensaEndDateT(), StringUtil.isEmpty(getKensaEndTimeT()) ? "2359" : getKensaEndTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        if (!StringUtil.isEmpty(kensaBasyo) && !"ALL".equals(kensaBasyo)) {
            kensaBasyoDataList = new ArrayList<>();
            kensaBasyoDataList.add(kensaBasyo);
        } else {
            boolean jyokenAdd = false;
            for (String data : kensabasyoData) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    jyokenAdd = true;
                }
            }
            if (jyokenAdd) {
                kensaBasyoDataList = new ArrayList<>(Arrays.asList(kensabasyoData));
            }
        }
        if (kensaBasyoDataList !=null && !kensaBasyoDataList.isEmpty()) {
            params.addAll(kensaBasyoDataList);
        }
        params.addAll(Arrays.asList(paramKensaStartDateF, paramKensaStartDateF));
        params.addAll(Arrays.asList(paramKensaStartDateT, paramKensaStartDateT));
        params.addAll(Arrays.asList(paramKensaEndDateF, paramKensaEndDateF));
        params.addAll(Arrays.asList(paramKensaEndDateT, paramKensaEndDateT));


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
            sql = sql + " AND key = 'ﾃｰﾋﾟﾝｸﾞﾁｪｯｸ履歴_表示可能ﾃﾞｰﾀ'";

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
