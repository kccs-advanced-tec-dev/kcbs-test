/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo202;

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
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.SelectParam;
import jp.co.kccs.xhd.common.ColumnInfoParser;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO202B005Model;
import jp.co.kccs.xhd.model.GXHDO202B006Model;
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
 * 変更日       2021/09/14<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶﾞﾗｽｽﾗﾘ-作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/09/14
 */
@Named
@ViewScoped
public class GXHDO202B002 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B002.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** ｶﾞﾗｽｽﾗﾘ-作製・秤量 一覧表示データ */
    private List<GXHDO202B005Model> b005ListData = null;
    
    /** ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕 一覧表示データ */
    private List<GXHDO202B006Model> b006ListData = null;
    
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 1ページ当たりの表示件数 */
    private int listDisplayPageCount = 30;

    /** 工程リスト:表示可能ﾃﾞｰﾀ */
    private String cmbKoteiData[];    
    /** 検索条件：工程 */
    private String cmbKotei = "";
    /** 検索条件：ﾛｯﾄNo */
    private String lotNo = "";
    /** 検索条件：品名 */
    private String hinmei = "";
    /** 検索条件：秤量日(FROM) */
    private String hyoryoDateF = "";
    /** 検索条件：秤量日(TO) */
    private String hyoryoDateT = "";
    /** 検索条件：秤量号機 */
    private String goki = "";
    /** 検索条件：粉砕開始日(FROM) */
    private String funsaiStartDateF = "";
    /** 検索条件：粉砕開始日(TO) */
    private String funsaiStartDateT = "";    
    /** 検索条件：粉砕終了日(FROM) */
    private String funsaiEndDateF = "";
    /** 検索条件：粉砕終了日(TO) */
    private String funsaiEndDateT = "";
    /** b005Listの制御 */
    private String b005DTdisplay;
    /** b006Listの制御 */
    private String b006DTdisplay;
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"秤量", "ﾎﾟｯﾄ粉砕"};
    //画面名称 ｶﾞﾗｽｽﾗﾘ-作製・秤量
    private static final String GAMEN_NAME_202B005 = "ｶﾞﾗｽｽﾗﾘ-作製・秤量";
    //画面名称 ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕
    private static final String GAMEN_NAME_202B006 = "ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕";
    //エクセル出力ファイルパス ｶﾞﾗｽｽﾗﾘ-作製・秤量
    private static final String JSON_FILE_PATH_202B005 = "/WEB-INF/classes/resources/json/gxhdo202b005.json";
    //エクセル出力ファイルパス ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕
    private static final String JSON_FILE_PATH_202B006 = "/WEB-INF/classes/resources/json/gxhdo202b006.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B002() {
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
     * ｶﾞﾗｽｽﾗﾘ-作製・秤量 一覧表示データ取得
     * @return ｶﾞﾗｽｽﾗﾘ-作製・秤量 一覧表示データ
     */
    public List<GXHDO202B005Model> getB005ListData() {
        return b005ListData;
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕 一覧表示データ取得
     * @return ｶﾞﾗｽｽﾗﾘ-作製・ﾎﾟｯﾄ粉砕 一覧表示データ
     */
    public List<GXHDO202B006Model> getB006ListData() {
        return b006ListData;
    }
    
    /**
     * 検索条件：工程
     *
     * @return the cmbKotei
     */
    public String getCmbKotei() {
        return cmbKotei;
    }

    /**
     * 検索条件：工程
     *
     * @param cmbKotei the cmbKotei to set
     */
    public void setCmbKotei(String cmbKotei) {
        this.cmbKotei = cmbKotei;
    }

    /**
     * 検索条件：工程リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbKoteiData[]
     */
    public String[] getCmbKoteiData() {
        return cmbKoteiData;
    }

    /**
     * 検索条件：工程リスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbKoteiData[] the cmbKoteiData[] to set
     */
    public void setCmbKoteiData(String[] cmbKoteiData) {
        this.cmbKoteiData = cmbKoteiData;
    }
    
    /**
     * 検索条件：ﾛｯﾄNo
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：ﾛｯﾄNo
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    
    /**
     * 検索条件：品名
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 検索条件：品名
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }
    
    /**
     * 検索条件：秤量日(FROM)
     * @return the hyoryoDateF
     */
    public String getHyoryoDateF() {
        return hyoryoDateF;
    }

    /**
     * 検索条件：秤量日(FROM)
     * @param hyoryoDateF the hyoryoDateF to set
     */
    public void setHyoryoDateF(String hyoryoDateF) {
        this.hyoryoDateF = hyoryoDateF;
    }

    /**
     * 検索条件：秤量日(TO)
     * @return the hyoryoDateT
     */
    public String getHyoryoDateT() {
        return hyoryoDateT;
    }

    /**
     * 検索条件：秤量日(TO)
     * @param hyoryoDateT the hyoryoDateT to set
     */
    public void setHyoryoDateT(String hyoryoDateT) {
        this.hyoryoDateT = hyoryoDateT;
    }
    
    /**
     * 検索条件：秤量号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 検索条件：秤量号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }
    
    /**
     * 検索条件：粉砕開始日(FROM)
     * @return the funsaiStartDateF
     */
    public String getFunsaiStartDateF() {
        return funsaiStartDateF;
    }

    /**
     * 検索条件：粉砕開始日(FROM)
     * @param funsaiStartDateF the funsaiStartDateF to set
     */
    public void setFunsaiStartDateF(String funsaiStartDateF) {
        this.funsaiStartDateF = funsaiStartDateF;
    }

    /**
     * 検索条件：粉砕開始日(TO)
     * @return the funsaiStartDateT
     */
    public String getFunsaiStartDateT() {
        return funsaiStartDateT;
    }

    /**
     * 検索条件：粉砕開始日(TO)
     * @param funsaiStartDateT the funsaiStartDateT to set
     */
    public void setFunsaiStartDateT(String funsaiStartDateT) {
        this.funsaiStartDateT = funsaiStartDateT;
    }

    /**
     * 検索条件：粉砕終了日(FROM)
     * @return the funsaiEndDateF
     */
    public String getFunsaiEndDateF() {
        return funsaiEndDateF;
    }

    /**
     * 検索条件：粉砕終了日(FROM)
     * @param funsaiEndDateF the funsaiEndDateF to set
     */
    public void setFunsaiEndDateF(String funsaiEndDateF) {
        this.funsaiEndDateF = funsaiEndDateF;
    }

    /**
     * 検索条件：粉砕終了日(TO)
     * @return the funsaiEndDateT
     */
    public String getFunsaiEndDateT() {
        return funsaiEndDateT;
    }

    /**
     * 検索条件：粉砕終了日(TO)
     * @param funsaiEndDateT the funsaiEndDateT to set
     */
    public void setFunsaiEndDateT(String funsaiEndDateT) {
        this.funsaiEndDateT = funsaiEndDateT;
    }

    /**
     * b005Listの制御
     * @return the b005DTdisplay
     */
    public String getB005DTdisplay() {
        return b005DTdisplay;
    }

    /**
     * b005Listの制御
     * @param b005DTdisplay the b005DTdisplay to set
     */
    public void setB005DTdisplay(String b005DTdisplay) {
        this.b005DTdisplay = b005DTdisplay;
    }

    /**
     * b006Listの制御
     * @return the b006DTdisplay
     */
    public String getB006DTdisplay() {
        return b006DTdisplay;
    }

    /**
     * b006Listの制御
     * @param b006DTdisplay the b006DTdisplay to set
     */
    public void setB006DTdisplay(String b006DTdisplay) {
        this.b006DTdisplay = b006DTdisplay;
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
                ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
            }
            return;
        }
        
        // 工程コンボボックス設定
        setCmbKoteiData(KOTEI_CMB_LIST);
        
        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B002_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B002_display_page_count", session));
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
        cmbKotei = "";
        lotNo = "";
        hinmei = "";
        hyoryoDateF = "";
        hyoryoDateT = "";
        goki = "";
        funsaiStartDateF = "";
        funsaiStartDateT = "";
        funsaiEndDateF = "";
        funsaiEndDateT = "";

        b005ListData = new ArrayList<>();
        b006ListData = new ArrayList<>();

        setB005DTdisplay(DISPLAY_NONE);
        setB006DTdisplay(DISPLAY_NONE);
    }
    
    /**
     * 入力値チェック：
     * 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();

        // 工程
        if (existError(validateUtil.checkC001(StringUtil.nullToBlank(cmbKotei), "工程"))) {
            return;
        }
        // ロットNo
        if(!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)){
         FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        if (!StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
            return;
        }
        // 秤量日(FROM)
        if (existError(validateUtil.checkC101(getHyoryoDateF(), "秤量日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHyoryoDateF(), "秤量日(from)")) ||
            existError(validateUtil.checkC501(getHyoryoDateF(), "秤量日(from)"))) {
            return;
        }
        // 秤量日(TO)
        if (existError(validateUtil.checkC101(getHyoryoDateT(), "秤量日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHyoryoDateT(), "秤量日(to)")) ||
            existError(validateUtil.checkC501(getHyoryoDateT(), "秤量日(to)"))) {
            return;
        }
        // 粉砕開始日(FROM)
        if (existError(validateUtil.checkC101(getFunsaiStartDateF(), "粉砕開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaiStartDateF(), "粉砕開始日(from)")) ||
            existError(validateUtil.checkC501(getFunsaiStartDateF(), "粉砕開始日(from)"))) {
            return;
        }
        // 粉砕開始日(TO)
        if (existError(validateUtil.checkC101(getFunsaiStartDateT(), "粉砕開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaiStartDateT(), "粉砕開始日(to)")) ||
            existError(validateUtil.checkC501(getFunsaiStartDateT(), "粉砕開始日(to)"))) {
            return;
        }
        // 粉砕終了日(FROM)
        if (existError(validateUtil.checkC101(getFunsaiEndDateF(), "粉砕終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaiEndDateF(), "粉砕終了日(from)")) ||
            existError(validateUtil.checkC501(getFunsaiEndDateF(), "粉砕終了日(from)"))) {
            return;
        }
        // 粉砕終了日(TO)
        if (existError(validateUtil.checkC101(getFunsaiEndDateT(), "粉砕終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaiEndDateT(), "粉砕終了日(to)")) ||
            existError(validateUtil.checkC501(getFunsaiEndDateT(), "粉砕終了日(to)"))) {
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
            String sql = "";

            if(null != cmbKotei)switch (cmbKotei) {
                case "秤量":
                    //工程が「秤量」の場合、Ⅲ.画面表示仕様(2)を発行
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_glassslurryhyoryo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glassslurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryounichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryounichiji <= ?) "
                            + "   AND (? IS NULL OR goki = ?) ";
                    break;
                case "ﾎﾟｯﾄ粉砕":
                    //工程が「ﾎﾟｯﾄ粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_glassslurryfunsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glassslurryhinmei = ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji <= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) ";
                    break;
                default:
                    break;
            }

            if(!"".equals(sql)){
                // パラメータ設定
                List<Object> params = createSearchParam();

                DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
                count = (long)result.get("CNT");
            }

        } catch (SQLException ex) {
            count = 0;
            b005ListData = new ArrayList<>();
            b006ListData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB005DTdisplay(DISPLAY_NONE);
        setB006DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "秤量":{
                    //工程が「秤量」の場合、Ⅲ.画面表示仕様(2)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glassslurryhinmei"
                            + ", glassslurrylotno"
                            + ", lotkubun"
                            + ", fusaipottosize"
                            + ", tamaishikei"
                            + ", goki"
                            + ", potto1_zairyohinmei1"
                            + ", potto1_tyogouryoukikaku1"
                            + ", potto1_sizailotno1_1"
                            + ", potto1_tyougouryou1_1"
                            + ", potto1_sizailotno1_2"
                            + ", potto1_tyougouryou1_2"
                            + ", potto1_zairyohinmei2"
                            + ", potto1_tyogouryoukikaku2"
                            + ", potto1_sizailotno2_1"
                            + ", potto1_tyougouryou2_1"
                            + ", potto1_sizailotno2_2"
                            + ", potto1_tyougouryou2_2"
                            + ", potto1_zairyohinmei3"
                            + ", potto1_tyogouryoukikaku3"
                            + ", potto1_sizailotno3_1"
                            + ", potto1_tyougouryou3_1"
                            + ", potto1_sizailotno3_2"
                            + ", potto1_tyougouryou3_2"
                            + ", potto1_zairyohinmei4"
                            + ", potto1_tyogouryoukikaku4"
                            + ", potto1_sizailotno4_1"
                            + ", potto1_tyougouryou4_1"
                            + ", potto1_sizailotno4_2"
                            + ", potto1_tyougouryou4_2"
                            + ", potto2_zairyohinmei1"
                            + ", potto2_tyogouryoukikaku1"
                            + ", potto2_sizailotno1_1"
                            + ", potto2_tyougouryou1_1"
                            + ", potto2_sizailotno1_2"
                            + ", potto2_tyougouryou1_2"
                            + ", potto2_zairyohinmei2"
                            + ", potto2_tyogouryoukikaku2"
                            + ", potto2_sizailotno2_1"
                            + ", potto2_tyougouryou2_1"
                            + ", potto2_sizailotno2_2"
                            + ", potto2_tyougouryou2_2"
                            + ", potto2_zairyohinmei3"
                            + ", potto2_tyogouryoukikaku3"
                            + ", potto2_sizailotno3_1"
                            + ", potto2_tyougouryou3_1"
                            + ", potto2_sizailotno3_2"
                            + ", potto2_tyougouryou3_2"
                            + ", potto2_zairyohinmei4"
                            + ", potto2_tyogouryoukikaku4"
                            + ", potto2_sizailotno4_1"
                            + ", potto2_tyougouryou4_1"
                            + ", potto2_sizailotno4_2"
                            + ", potto2_tyougouryou4_2"
                            + ", potto3_zairyohinmei1"
                            + ", potto3_tyogouryoukikaku1"
                            + ", potto3_sizailotno1_1"
                            + ", potto3_tyougouryou1_1"
                            + ", potto3_sizailotno1_2"
                            + ", potto3_tyougouryou1_2"
                            + ", potto3_zairyohinmei2"
                            + ", potto3_tyogouryoukikaku2"
                            + ", potto3_sizailotno2_1"
                            + ", potto3_tyougouryou2_1"
                            + ", potto3_sizailotno2_2"
                            + ", potto3_tyougouryou2_2"
                            + ", potto3_zairyohinmei3"
                            + ", potto3_tyogouryoukikaku3"
                            + ", potto3_sizailotno3_1"
                            + ", potto3_tyougouryou3_1"
                            + ", potto3_sizailotno3_2"
                            + ", potto3_tyougouryou3_2"
                            + ", potto3_zairyohinmei4"
                            + ", potto3_tyogouryoukikaku4"
                            + ", potto3_sizailotno4_1"
                            + ", potto3_tyougouryou4_1"
                            + ", potto3_sizailotno4_2"
                            + ", potto3_tyougouryou4_2"
                            + ", potto4_zairyohinmei1"
                            + ", potto4_tyogouryoukikaku1"
                            + ", potto4_sizailotno1_1"
                            + ", potto4_tyougouryou1_1"
                            + ", potto4_sizailotno1_2"
                            + ", potto4_tyougouryou1_2"
                            + ", potto4_zairyohinmei2"
                            + ", potto4_tyogouryoukikaku2"
                            + ", potto4_sizailotno2_1"
                            + ", potto4_tyougouryou2_1"
                            + ", potto4_sizailotno2_2"
                            + ", potto4_tyougouryou2_2"
                            + ", potto4_zairyohinmei3"
                            + ", potto4_tyogouryoukikaku3"
                            + ", potto4_sizailotno3_1"
                            + ", potto4_tyougouryou3_1"
                            + ", potto4_sizailotno3_2"
                            + ", potto4_tyougouryou3_2"
                            + ", potto4_zairyohinmei4"
                            + ", potto4_tyogouryoukikaku4"
                            + ", potto4_sizailotno4_1"
                            + ", potto4_tyougouryou4_1"
                            + ", potto4_sizailotno4_2"
                            + ", potto4_tyougouryou4_2"
                            + ", potto5_zairyohinmei1"
                            + ", potto5_tyogouryoukikaku1"
                            + ", potto5_sizailotno1_1"
                            + ", potto5_tyougouryou1_1"
                            + ", potto5_sizailotno1_2"
                            + ", potto5_tyougouryou1_2"
                            + ", potto5_zairyohinmei2"
                            + ", potto5_tyogouryoukikaku2"
                            + ", potto5_sizailotno2_1"
                            + ", potto5_tyougouryou2_1"
                            + ", potto5_sizailotno2_2"
                            + ", potto5_tyougouryou2_2"
                            + ", potto5_zairyohinmei3"
                            + ", potto5_tyogouryoukikaku3"
                            + ", potto5_sizailotno3_1"
                            + ", potto5_tyougouryou3_1"
                            + ", potto5_sizailotno3_2"
                            + ", potto5_tyougouryou3_2"
                            + ", potto5_zairyohinmei4"
                            + ", potto5_tyogouryoukikaku4"
                            + ", potto5_sizailotno4_1"
                            + ", potto5_tyougouryou4_1"
                            + ", potto5_sizailotno4_2"
                            + ", potto5_tyougouryou4_2"
                            + ", potto6_zairyohinmei1"
                            + ", potto6_tyogouryoukikaku1"
                            + ", potto6_sizailotno1_1"
                            + ", potto6_tyougouryou1_1"
                            + ", potto6_sizailotno1_2"
                            + ", potto6_tyougouryou1_2"
                            + ", potto6_zairyohinmei2"
                            + ", potto6_tyogouryoukikaku2"
                            + ", potto6_sizailotno2_1"
                            + ", potto6_tyougouryou2_1"
                            + ", potto6_sizailotno2_2"
                            + ", potto6_tyougouryou2_2"
                            + ", potto6_zairyohinmei3"
                            + ", potto6_tyogouryoukikaku3"
                            + ", potto6_sizailotno3_1"
                            + ", potto6_tyougouryou3_1"
                            + ", potto6_sizailotno3_2"
                            + ", potto6_tyougouryou3_2"
                            + ", potto6_zairyohinmei4"
                            + ", potto6_tyogouryoukikaku4"
                            + ", potto6_sizailotno4_1"
                            + ", potto6_tyougouryou4_1"
                            + ", potto6_sizailotno4_2"
                            + ", potto6_tyougouryou4_2"
                            + ", potto7_zairyohinmei1"
                            + ", potto7_tyogouryoukikaku1"
                            + ", potto7_sizailotno1_1"
                            + ", potto7_tyougouryou1_1"
                            + ", potto7_sizailotno1_2"
                            + ", potto7_tyougouryou1_2"
                            + ", potto7_zairyohinmei2"
                            + ", potto7_tyogouryoukikaku2"
                            + ", potto7_sizailotno2_1"
                            + ", potto7_tyougouryou2_1"
                            + ", potto7_sizailotno2_2"
                            + ", potto7_tyougouryou2_2"
                            + ", potto7_zairyohinmei3"
                            + ", potto7_tyogouryoukikaku3"
                            + ", potto7_sizailotno3_1"
                            + ", potto7_tyougouryou3_1"
                            + ", potto7_sizailotno3_2"
                            + ", potto7_tyougouryou3_2"
                            + ", potto7_zairyohinmei4"
                            + ", potto7_tyogouryoukikaku4"
                            + ", potto7_sizailotno4_1"
                            + ", potto7_tyougouryou4_1"
                            + ", potto7_sizailotno4_2"
                            + ", potto7_tyougouryou4_2"
                            + ", potto8_zairyohinmei1"
                            + ", potto8_tyogouryoukikaku1"
                            + ", potto8_sizailotno1_1"
                            + ", potto8_tyougouryou1_1"
                            + ", potto8_sizailotno1_2"
                            + ", potto8_tyougouryou1_2"
                            + ", potto8_zairyohinmei2"
                            + ", potto8_tyogouryoukikaku2"
                            + ", potto8_sizailotno2_1"
                            + ", potto8_tyougouryou2_1"
                            + ", potto8_sizailotno2_2"
                            + ", potto8_tyougouryou2_2"
                            + ", potto8_zairyohinmei3"
                            + ", potto8_tyogouryoukikaku3"
                            + ", potto8_sizailotno3_1"
                            + ", potto8_tyougouryou3_1"
                            + ", potto8_sizailotno3_2"
                            + ", potto8_tyougouryou3_2"
                            + ", potto8_zairyohinmei4"
                            + ", potto8_tyogouryoukikaku4"
                            + ", potto8_sizailotno4_1"
                            + ", potto8_tyougouryou4_1"
                            + ", potto8_sizailotno4_2"
                            + ", potto8_tyougouryou4_2"
                            + ", hyouryounichiji"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_glassslurryhyoryo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glassslurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryounichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryounichiji <= ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + " ORDER BY hyouryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("glassslurryhinmei", "glassslurryhinmei");                  // ｶﾞﾗｽｽﾗﾘｰ品名
                    mapping.put("glassslurrylotno", "glassslurrylotno");                    // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("fusaipottosize", "fusaipottosize");                        // 粉砕ﾎﾟｯﾄｻｲｽﾞ
                    mapping.put("tamaishikei", "tamaishikei");                              // 玉石径
                    mapping.put("goki", "goki");                                            // 秤量号機
                    mapping.put("potto1_zairyohinmei1", "potto1_zairyohinmei1");            // ﾎﾟｯﾄ1_材料品名1
                    mapping.put("potto1_tyogouryoukikaku1", "potto1_tyogouryoukikaku1");    // ﾎﾟｯﾄ1_調合量規格1
                    mapping.put("potto1_sizailotno1_1", "potto1_sizailotno1_1");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
                    mapping.put("potto1_tyougouryou1_1", "potto1_tyougouryou1_1");          // ﾎﾟｯﾄ1_調合量1_1
                    mapping.put("potto1_sizailotno1_2", "potto1_sizailotno1_2");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
                    mapping.put("potto1_tyougouryou1_2", "potto1_tyougouryou1_2");          // ﾎﾟｯﾄ1_調合量1_2
                    mapping.put("potto1_zairyohinmei2", "potto1_zairyohinmei2");            // ﾎﾟｯﾄ1_材料品名2
                    mapping.put("potto1_tyogouryoukikaku2", "potto1_tyogouryoukikaku2");    // ﾎﾟｯﾄ1_調合量規格2
                    mapping.put("potto1_sizailotno2_1", "potto1_sizailotno2_1");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
                    mapping.put("potto1_tyougouryou2_1", "potto1_tyougouryou2_1");          // ﾎﾟｯﾄ1_調合量2_1
                    mapping.put("potto1_sizailotno2_2", "potto1_sizailotno2_2");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
                    mapping.put("potto1_tyougouryou2_2", "potto1_tyougouryou2_2");          // ﾎﾟｯﾄ1_調合量2_2
                    mapping.put("potto1_zairyohinmei3", "potto1_zairyohinmei3");            // ﾎﾟｯﾄ1_材料品名3
                    mapping.put("potto1_tyogouryoukikaku3", "potto1_tyogouryoukikaku3");    // ﾎﾟｯﾄ1_調合量規格3
                    mapping.put("potto1_sizailotno3_1", "potto1_sizailotno3_1");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
                    mapping.put("potto1_tyougouryou3_1", "potto1_tyougouryou3_1");          // ﾎﾟｯﾄ1_調合量3_1
                    mapping.put("potto1_sizailotno3_2", "potto1_sizailotno3_2");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
                    mapping.put("potto1_tyougouryou3_2", "potto1_tyougouryou3_2");          // ﾎﾟｯﾄ1_調合量3_2
                    mapping.put("potto1_zairyohinmei4", "potto1_zairyohinmei4");            // ﾎﾟｯﾄ1_材料品名4
                    mapping.put("potto1_tyogouryoukikaku4", "potto1_tyogouryoukikaku4");    // ﾎﾟｯﾄ1_調合量規格4
                    mapping.put("potto1_sizailotno4_1", "potto1_sizailotno4_1");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
                    mapping.put("potto1_tyougouryou4_1", "potto1_tyougouryou4_1");          // ﾎﾟｯﾄ1_調合量4_1
                    mapping.put("potto1_sizailotno4_2", "potto1_sizailotno4_2");            // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
                    mapping.put("potto1_tyougouryou4_2", "potto1_tyougouryou4_2");          // ﾎﾟｯﾄ1_調合量4_2
                    mapping.put("potto2_zairyohinmei1", "potto2_zairyohinmei1");            // ﾎﾟｯﾄ2_材料品名1
                    mapping.put("potto2_tyogouryoukikaku1", "potto2_tyogouryoukikaku1");    // ﾎﾟｯﾄ2_調合量規格1
                    mapping.put("potto2_sizailotno1_1", "potto2_sizailotno1_1");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
                    mapping.put("potto2_tyougouryou1_1", "potto2_tyougouryou1_1");          // ﾎﾟｯﾄ2_調合量1_1
                    mapping.put("potto2_sizailotno1_2", "potto2_sizailotno1_2");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
                    mapping.put("potto2_tyougouryou1_2", "potto2_tyougouryou1_2");          // ﾎﾟｯﾄ2_調合量1_2
                    mapping.put("potto2_zairyohinmei2", "potto2_zairyohinmei2");            // ﾎﾟｯﾄ2_材料品名2
                    mapping.put("potto2_tyogouryoukikaku2", "potto2_tyogouryoukikaku2");    // ﾎﾟｯﾄ2_調合量規格2
                    mapping.put("potto2_sizailotno2_1", "potto2_sizailotno2_1");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
                    mapping.put("potto2_tyougouryou2_1", "potto2_tyougouryou2_1");          // ﾎﾟｯﾄ2_調合量2_1
                    mapping.put("potto2_sizailotno2_2", "potto2_sizailotno2_2");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
                    mapping.put("potto2_tyougouryou2_2", "potto2_tyougouryou2_2");          // ﾎﾟｯﾄ2_調合量2_2
                    mapping.put("potto2_zairyohinmei3", "potto2_zairyohinmei3");            // ﾎﾟｯﾄ2_材料品名3
                    mapping.put("potto2_tyogouryoukikaku3", "potto2_tyogouryoukikaku3");    // ﾎﾟｯﾄ2_調合量規格3
                    mapping.put("potto2_sizailotno3_1", "potto2_sizailotno3_1");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
                    mapping.put("potto2_tyougouryou3_1", "potto2_tyougouryou3_1");          // ﾎﾟｯﾄ2_調合量3_1
                    mapping.put("potto2_sizailotno3_2", "potto2_sizailotno3_2");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
                    mapping.put("potto2_tyougouryou3_2", "potto2_tyougouryou3_2");          // ﾎﾟｯﾄ2_調合量3_2
                    mapping.put("potto2_zairyohinmei4", "potto2_zairyohinmei4");            // ﾎﾟｯﾄ2_材料品名4
                    mapping.put("potto2_tyogouryoukikaku4", "potto2_tyogouryoukikaku4");    // ﾎﾟｯﾄ2_調合量規格4
                    mapping.put("potto2_sizailotno4_1", "potto2_sizailotno4_1");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
                    mapping.put("potto2_tyougouryou4_1", "potto2_tyougouryou4_1");          // ﾎﾟｯﾄ2_調合量4_1
                    mapping.put("potto2_sizailotno4_2", "potto2_sizailotno4_2");            // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
                    mapping.put("potto2_tyougouryou4_2", "potto2_tyougouryou4_2");          // ﾎﾟｯﾄ2_調合量4_2
                    mapping.put("potto3_zairyohinmei1", "potto3_zairyohinmei1");            // ﾎﾟｯﾄ3_材料品名1
                    mapping.put("potto3_tyogouryoukikaku1", "potto3_tyogouryoukikaku1");    // ﾎﾟｯﾄ3_調合量規格1
                    mapping.put("potto3_sizailotno1_1", "potto3_sizailotno1_1");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
                    mapping.put("potto3_tyougouryou1_1", "potto3_tyougouryou1_1");          // ﾎﾟｯﾄ3_調合量1_1
                    mapping.put("potto3_sizailotno1_2", "potto3_sizailotno1_2");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
                    mapping.put("potto3_tyougouryou1_2", "potto3_tyougouryou1_2");          // ﾎﾟｯﾄ3_調合量1_2
                    mapping.put("potto3_zairyohinmei2", "potto3_zairyohinmei2");            // ﾎﾟｯﾄ3_材料品名2
                    mapping.put("potto3_tyogouryoukikaku2", "potto3_tyogouryoukikaku2");    // ﾎﾟｯﾄ3_調合量規格2
                    mapping.put("potto3_sizailotno2_1", "potto3_sizailotno2_1");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
                    mapping.put("potto3_tyougouryou2_1", "potto3_tyougouryou2_1");          // ﾎﾟｯﾄ3_調合量2_1
                    mapping.put("potto3_sizailotno2_2", "potto3_sizailotno2_2");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
                    mapping.put("potto3_tyougouryou2_2", "potto3_tyougouryou2_2");          // ﾎﾟｯﾄ3_調合量2_2
                    mapping.put("potto3_zairyohinmei3", "potto3_zairyohinmei3");            // ﾎﾟｯﾄ3_材料品名3
                    mapping.put("potto3_tyogouryoukikaku3", "potto3_tyogouryoukikaku3");    // ﾎﾟｯﾄ3_調合量規格3
                    mapping.put("potto3_sizailotno3_1", "potto3_sizailotno3_1");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
                    mapping.put("potto3_tyougouryou3_1", "potto3_tyougouryou3_1");          // ﾎﾟｯﾄ3_調合量3_1
                    mapping.put("potto3_sizailotno3_2", "potto3_sizailotno3_2");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
                    mapping.put("potto3_tyougouryou3_2", "potto3_tyougouryou3_2");          // ﾎﾟｯﾄ3_調合量3_2
                    mapping.put("potto3_zairyohinmei4", "potto3_zairyohinmei4");            // ﾎﾟｯﾄ3_材料品名4
                    mapping.put("potto3_tyogouryoukikaku4", "potto3_tyogouryoukikaku4");    // ﾎﾟｯﾄ3_調合量規格4
                    mapping.put("potto3_sizailotno4_1", "potto3_sizailotno4_1");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
                    mapping.put("potto3_tyougouryou4_1", "potto3_tyougouryou4_1");          // ﾎﾟｯﾄ3_調合量4_1
                    mapping.put("potto3_sizailotno4_2", "potto3_sizailotno4_2");            // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
                    mapping.put("potto3_tyougouryou4_2", "potto3_tyougouryou4_2");          // ﾎﾟｯﾄ3_調合量4_2
                    mapping.put("potto4_zairyohinmei1", "potto4_zairyohinmei1");            // ﾎﾟｯﾄ4_材料品名1
                    mapping.put("potto4_tyogouryoukikaku1", "potto4_tyogouryoukikaku1");    // ﾎﾟｯﾄ4_調合量規格1
                    mapping.put("potto4_sizailotno1_1", "potto4_sizailotno1_1");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
                    mapping.put("potto4_tyougouryou1_1", "potto4_tyougouryou1_1");          // ﾎﾟｯﾄ4_調合量1_1
                    mapping.put("potto4_sizailotno1_2", "potto4_sizailotno1_2");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
                    mapping.put("potto4_tyougouryou1_2", "potto4_tyougouryou1_2");          // ﾎﾟｯﾄ4_調合量1_2
                    mapping.put("potto4_zairyohinmei2", "potto4_zairyohinmei2");            // ﾎﾟｯﾄ4_材料品名2
                    mapping.put("potto4_tyogouryoukikaku2", "potto4_tyogouryoukikaku2");    // ﾎﾟｯﾄ4_調合量規格2
                    mapping.put("potto4_sizailotno2_1", "potto4_sizailotno2_1");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
                    mapping.put("potto4_tyougouryou2_1", "potto4_tyougouryou2_1");          // ﾎﾟｯﾄ4_調合量2_1
                    mapping.put("potto4_sizailotno2_2", "potto4_sizailotno2_2");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
                    mapping.put("potto4_tyougouryou2_2", "potto4_tyougouryou2_2");          // ﾎﾟｯﾄ4_調合量2_2
                    mapping.put("potto4_zairyohinmei3", "potto4_zairyohinmei3");            // ﾎﾟｯﾄ4_材料品名3
                    mapping.put("potto4_tyogouryoukikaku3", "potto4_tyogouryoukikaku3");    // ﾎﾟｯﾄ4_調合量規格3
                    mapping.put("potto4_sizailotno3_1", "potto4_sizailotno3_1");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
                    mapping.put("potto4_tyougouryou3_1", "potto4_tyougouryou3_1");          // ﾎﾟｯﾄ4_調合量3_1
                    mapping.put("potto4_sizailotno3_2", "potto4_sizailotno3_2");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
                    mapping.put("potto4_tyougouryou3_2", "potto4_tyougouryou3_2");          // ﾎﾟｯﾄ4_調合量3_2
                    mapping.put("potto4_zairyohinmei4", "potto4_zairyohinmei4");            // ﾎﾟｯﾄ4_材料品名4
                    mapping.put("potto4_tyogouryoukikaku4", "potto4_tyogouryoukikaku4");    // ﾎﾟｯﾄ4_調合量規格4
                    mapping.put("potto4_sizailotno4_1", "potto4_sizailotno4_1");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
                    mapping.put("potto4_tyougouryou4_1", "potto4_tyougouryou4_1");          // ﾎﾟｯﾄ4_調合量4_1
                    mapping.put("potto4_sizailotno4_2", "potto4_sizailotno4_2");            // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
                    mapping.put("potto4_tyougouryou4_2", "potto4_tyougouryou4_2");          // ﾎﾟｯﾄ4_調合量4_2
                    mapping.put("potto5_zairyohinmei1", "potto5_zairyohinmei1");            // ﾎﾟｯﾄ5_材料品名1
                    mapping.put("potto5_tyogouryoukikaku1", "potto5_tyogouryoukikaku1");    // ﾎﾟｯﾄ5_調合量規格1
                    mapping.put("potto5_sizailotno1_1", "potto5_sizailotno1_1");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
                    mapping.put("potto5_tyougouryou1_1", "potto5_tyougouryou1_1");          // ﾎﾟｯﾄ5_調合量1_1
                    mapping.put("potto5_sizailotno1_2", "potto5_sizailotno1_2");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
                    mapping.put("potto5_tyougouryou1_2", "potto5_tyougouryou1_2");          // ﾎﾟｯﾄ5_調合量1_2
                    mapping.put("potto5_zairyohinmei2", "potto5_zairyohinmei2");            // ﾎﾟｯﾄ5_材料品名2
                    mapping.put("potto5_tyogouryoukikaku2", "potto5_tyogouryoukikaku2");    // ﾎﾟｯﾄ5_調合量規格2
                    mapping.put("potto5_sizailotno2_1", "potto5_sizailotno2_1");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
                    mapping.put("potto5_tyougouryou2_1", "potto5_tyougouryou2_1");          // ﾎﾟｯﾄ5_調合量2_1
                    mapping.put("potto5_sizailotno2_2", "potto5_sizailotno2_2");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
                    mapping.put("potto5_tyougouryou2_2", "potto5_tyougouryou2_2");          // ﾎﾟｯﾄ5_調合量2_2
                    mapping.put("potto5_zairyohinmei3", "potto5_zairyohinmei3");            // ﾎﾟｯﾄ5_材料品名3
                    mapping.put("potto5_tyogouryoukikaku3", "potto5_tyogouryoukikaku3");    // ﾎﾟｯﾄ5_調合量規格3
                    mapping.put("potto5_sizailotno3_1", "potto5_sizailotno3_1");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
                    mapping.put("potto5_tyougouryou3_1", "potto5_tyougouryou3_1");          // ﾎﾟｯﾄ5_調合量3_1
                    mapping.put("potto5_sizailotno3_2", "potto5_sizailotno3_2");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
                    mapping.put("potto5_tyougouryou3_2", "potto5_tyougouryou3_2");          // ﾎﾟｯﾄ5_調合量3_2
                    mapping.put("potto5_zairyohinmei4", "potto5_zairyohinmei4");            // ﾎﾟｯﾄ5_材料品名4
                    mapping.put("potto5_tyogouryoukikaku4", "potto5_tyogouryoukikaku4");    // ﾎﾟｯﾄ5_調合量規格4
                    mapping.put("potto5_sizailotno4_1", "potto5_sizailotno4_1");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
                    mapping.put("potto5_tyougouryou4_1", "potto5_tyougouryou4_1");          // ﾎﾟｯﾄ5_調合量4_1
                    mapping.put("potto5_sizailotno4_2", "potto5_sizailotno4_2");            // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
                    mapping.put("potto5_tyougouryou4_2", "potto5_tyougouryou4_2");          // ﾎﾟｯﾄ5_調合量4_2
                    mapping.put("potto6_zairyohinmei1", "potto6_zairyohinmei1");            // ﾎﾟｯﾄ6_材料品名1
                    mapping.put("potto6_tyogouryoukikaku1", "potto6_tyogouryoukikaku1");    // ﾎﾟｯﾄ6_調合量規格1
                    mapping.put("potto6_sizailotno1_1", "potto6_sizailotno1_1");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
                    mapping.put("potto6_tyougouryou1_1", "potto6_tyougouryou1_1");          // ﾎﾟｯﾄ6_調合量1_1
                    mapping.put("potto6_sizailotno1_2", "potto6_sizailotno1_2");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
                    mapping.put("potto6_tyougouryou1_2", "potto6_tyougouryou1_2");          // ﾎﾟｯﾄ6_調合量1_2
                    mapping.put("potto6_zairyohinmei2", "potto6_zairyohinmei2");            // ﾎﾟｯﾄ6_材料品名2
                    mapping.put("potto6_tyogouryoukikaku2", "potto6_tyogouryoukikaku2");    // ﾎﾟｯﾄ6_調合量規格2
                    mapping.put("potto6_sizailotno2_1", "potto6_sizailotno2_1");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
                    mapping.put("potto6_tyougouryou2_1", "potto6_tyougouryou2_1");          // ﾎﾟｯﾄ6_調合量2_1
                    mapping.put("potto6_sizailotno2_2", "potto6_sizailotno2_2");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
                    mapping.put("potto6_tyougouryou2_2", "potto6_tyougouryou2_2");          // ﾎﾟｯﾄ6_調合量2_2
                    mapping.put("potto6_zairyohinmei3", "potto6_zairyohinmei3");            // ﾎﾟｯﾄ6_材料品名3
                    mapping.put("potto6_tyogouryoukikaku3", "potto6_tyogouryoukikaku3");    // ﾎﾟｯﾄ6_調合量規格3
                    mapping.put("potto6_sizailotno3_1", "potto6_sizailotno3_1");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
                    mapping.put("potto6_tyougouryou3_1", "potto6_tyougouryou3_1");          // ﾎﾟｯﾄ6_調合量3_1
                    mapping.put("potto6_sizailotno3_2", "potto6_sizailotno3_2");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
                    mapping.put("potto6_tyougouryou3_2", "potto6_tyougouryou3_2");          // ﾎﾟｯﾄ6_調合量3_2
                    mapping.put("potto6_zairyohinmei4", "potto6_zairyohinmei4");            // ﾎﾟｯﾄ6_材料品名4
                    mapping.put("potto6_tyogouryoukikaku4", "potto6_tyogouryoukikaku4");    // ﾎﾟｯﾄ6_調合量規格4
                    mapping.put("potto6_sizailotno4_1", "potto6_sizailotno4_1");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
                    mapping.put("potto6_tyougouryou4_1", "potto6_tyougouryou4_1");          // ﾎﾟｯﾄ6_調合量4_1
                    mapping.put("potto6_sizailotno4_2", "potto6_sizailotno4_2");            // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
                    mapping.put("potto6_tyougouryou4_2", "potto6_tyougouryou4_2");          // ﾎﾟｯﾄ6_調合量4_2
                    mapping.put("potto7_zairyohinmei1", "potto7_zairyohinmei1");            // ﾎﾟｯﾄ7_材料品名1
                    mapping.put("potto7_tyogouryoukikaku1", "potto7_tyogouryoukikaku1");    // ﾎﾟｯﾄ7_調合量規格1
                    mapping.put("potto7_sizailotno1_1", "potto7_sizailotno1_1");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
                    mapping.put("potto7_tyougouryou1_1", "potto7_tyougouryou1_1");          // ﾎﾟｯﾄ7_調合量1_1
                    mapping.put("potto7_sizailotno1_2", "potto7_sizailotno1_2");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
                    mapping.put("potto7_tyougouryou1_2", "potto7_tyougouryou1_2");          // ﾎﾟｯﾄ7_調合量1_2
                    mapping.put("potto7_zairyohinmei2", "potto7_zairyohinmei2");            // ﾎﾟｯﾄ7_材料品名2
                    mapping.put("potto7_tyogouryoukikaku2", "potto7_tyogouryoukikaku2");    // ﾎﾟｯﾄ7_調合量規格2
                    mapping.put("potto7_sizailotno2_1", "potto7_sizailotno2_1");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
                    mapping.put("potto7_tyougouryou2_1", "potto7_tyougouryou2_1");          // ﾎﾟｯﾄ7_調合量2_1
                    mapping.put("potto7_sizailotno2_2", "potto7_sizailotno2_2");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
                    mapping.put("potto7_tyougouryou2_2", "potto7_tyougouryou2_2");          // ﾎﾟｯﾄ7_調合量2_2
                    mapping.put("potto7_zairyohinmei3", "potto7_zairyohinmei3");            // ﾎﾟｯﾄ7_材料品名3
                    mapping.put("potto7_tyogouryoukikaku3", "potto7_tyogouryoukikaku3");    // ﾎﾟｯﾄ7_調合量規格3
                    mapping.put("potto7_sizailotno3_1", "potto7_sizailotno3_1");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
                    mapping.put("potto7_tyougouryou3_1", "potto7_tyougouryou3_1");          // ﾎﾟｯﾄ7_調合量3_1
                    mapping.put("potto7_sizailotno3_2", "potto7_sizailotno3_2");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
                    mapping.put("potto7_tyougouryou3_2", "potto7_tyougouryou3_2");          // ﾎﾟｯﾄ7_調合量3_2
                    mapping.put("potto7_zairyohinmei4", "potto7_zairyohinmei4");            // ﾎﾟｯﾄ7_材料品名4
                    mapping.put("potto7_tyogouryoukikaku4", "potto7_tyogouryoukikaku4");    // ﾎﾟｯﾄ7_調合量規格4
                    mapping.put("potto7_sizailotno4_1", "potto7_sizailotno4_1");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
                    mapping.put("potto7_tyougouryou4_1", "potto7_tyougouryou4_1");          // ﾎﾟｯﾄ7_調合量4_1
                    mapping.put("potto7_sizailotno4_2", "potto7_sizailotno4_2");            // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
                    mapping.put("potto7_tyougouryou4_2", "potto7_tyougouryou4_2");          // ﾎﾟｯﾄ7_調合量4_2
                    mapping.put("potto8_zairyohinmei1", "potto8_zairyohinmei1");            // ﾎﾟｯﾄ8_材料品名1
                    mapping.put("potto8_tyogouryoukikaku1", "potto8_tyogouryoukikaku1");    // ﾎﾟｯﾄ8_調合量規格1
                    mapping.put("potto8_sizailotno1_1", "potto8_sizailotno1_1");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
                    mapping.put("potto8_tyougouryou1_1", "potto8_tyougouryou1_1");          // ﾎﾟｯﾄ8_調合量1_1
                    mapping.put("potto8_sizailotno1_2", "potto8_sizailotno1_2");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
                    mapping.put("potto8_tyougouryou1_2", "potto8_tyougouryou1_2");          // ﾎﾟｯﾄ8_調合量1_2
                    mapping.put("potto8_zairyohinmei2", "potto8_zairyohinmei2");            // ﾎﾟｯﾄ8_材料品名2
                    mapping.put("potto8_tyogouryoukikaku2", "potto8_tyogouryoukikaku2");    // ﾎﾟｯﾄ8_調合量規格2
                    mapping.put("potto8_sizailotno2_1", "potto8_sizailotno2_1");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
                    mapping.put("potto8_tyougouryou2_1", "potto8_tyougouryou2_1");          // ﾎﾟｯﾄ8_調合量2_1
                    mapping.put("potto8_sizailotno2_2", "potto8_sizailotno2_2");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
                    mapping.put("potto8_tyougouryou2_2", "potto8_tyougouryou2_2");          // ﾎﾟｯﾄ8_調合量2_2
                    mapping.put("potto8_zairyohinmei3", "potto8_zairyohinmei3");            // ﾎﾟｯﾄ8_材料品名3
                    mapping.put("potto8_tyogouryoukikaku3", "potto8_tyogouryoukikaku3");    // ﾎﾟｯﾄ8_調合量規格3
                    mapping.put("potto8_sizailotno3_1", "potto8_sizailotno3_1");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
                    mapping.put("potto8_tyougouryou3_1", "potto8_tyougouryou3_1");          // ﾎﾟｯﾄ8_調合量3_1
                    mapping.put("potto8_sizailotno3_2", "potto8_sizailotno3_2");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
                    mapping.put("potto8_tyougouryou3_2", "potto8_tyougouryou3_2");          // ﾎﾟｯﾄ8_調合量3_2
                    mapping.put("potto8_zairyohinmei4", "potto8_zairyohinmei4");            // ﾎﾟｯﾄ8_材料品名4
                    mapping.put("potto8_tyogouryoukikaku4", "potto8_tyogouryoukikaku4");    // ﾎﾟｯﾄ8_調合量規格4
                    mapping.put("potto8_sizailotno4_1", "potto8_sizailotno4_1");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
                    mapping.put("potto8_tyougouryou4_1", "potto8_tyougouryou4_1");          // ﾎﾟｯﾄ8_調合量4_1
                    mapping.put("potto8_sizailotno4_2", "potto8_sizailotno4_2");            // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
                    mapping.put("potto8_tyougouryou4_2", "potto8_tyougouryou4_2");          // ﾎﾟｯﾄ8_調合量4_2
                    mapping.put("hyouryounichiji", "hyouryounichiji");                      // 秤量日時
                    mapping.put("tantousya", "tantousya");                                  // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                // 確認者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B005Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B005Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b005ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB005DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾎﾟｯﾄ粉砕":{
                    //工程が「ﾎﾟｯﾄ粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glassslurryhinmei"
                            + ", glassslurrylotno"
                            + ", lotkubun"
                            + ", syuusoku"
                            + ", kaitendaigouki"
                            + ", funsaikaisinichiji"
                            + ", funsaiyoteisyuuryounichiji"
                            + ", kaisitantosya"
                            + ", funsaisyuuryounichiji"
                            + ", syuryotantosya"
                            + ", funsaijikan"
                            + ", zairyohinmei"
                            + ", buzaizaikono"
                            + ", tyougouryou"
                            + ", fuutaijyuuryou"
                            + ", soujyuuryou"
                            + ", syoumijyuuryou"
                            + ", budomari"
                            + ", haisyututantousya"
                            + ", hokankaisinichiji"
                            + ", hokanbasyo"
                            + ", hokankaitengouki"
                            + ", kaitensuu"
                            + ", hokantantosya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_glassslurryfunsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glassslurryhinmei = ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji <= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) "
                            + " ORDER BY funsaikaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                             // ﾛｯﾄNo
                    mapping.put("glassslurryhinmei", "glassslurryhinmei");                     // ｶﾞﾗｽｽﾗﾘｰ品名
                    mapping.put("glassslurrylotno", "glassslurrylotno");                       // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                       // ﾛｯﾄ区分
                    mapping.put("syuusoku", "syuusoku");                                       // 周速
                    mapping.put("kaitendaigouki", "kaitendaigouki");                           // 粉砕回転台号機
                    mapping.put("funsaikaisinichiji", "funsaikaisinichiji");                   // 粉砕開始日時
                    mapping.put("funsaiyoteisyuuryounichiji", "funsaiyoteisyuuryounichiji");   // 粉砕終了予定日時
                    mapping.put("kaisitantosya", "kaisitantosya");                             // 粉砕開始担当者
                    mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");             // 粉砕終了日時
                    mapping.put("syuryotantosya", "syuryotantosya");                           // 粉砕終了担当者
                    mapping.put("funsaijikan", "funsaijikan");                                 // 粉砕時間
                    mapping.put("zairyohinmei", "zairyohinmei");                               // 材料品名
                    mapping.put("buzaizaikono", "buzaizaikono");                               // 部材在庫No
                    mapping.put("tyougouryou", "tyougouryou");                                 // 調合量
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                           // 風袋重量
                    mapping.put("soujyuuryou", "soujyuuryou");                                 // 総重量
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");                           // 正味重量
                    mapping.put("budomari", "budomari");                                       // 歩留まり
                    mapping.put("haisyututantousya", "haisyututantousya");                     // 排出担当者
                    mapping.put("hokankaisinichiji", "hokankaisinichiji");                     // 保管開始日時
                    mapping.put("hokanbasyo", "hokanbasyo");                                   // 保管場所
                    mapping.put("hokankaitengouki", "hokankaitengouki");                       // 保管回転台号機
                    mapping.put("kaitensuu", "kaitensuu");                                     // 回転数
                    mapping.put("hokantantosya", "hokantantosya");                             // 保管担当者
                    mapping.put("bikou1", "bikou1");                                           // 備考1
                    mapping.put("bikou2", "bikou2");                                           // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B006Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B006Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b006ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB006DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }
            
        } catch (SQLException ex) {
            b005ListData = new ArrayList<>();
            b006ListData = new ArrayList<>();
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
            ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
        }
    }

    /**
     * Excelダウンロード
     * @throws Throwable 
     */
    public void downloadExcel() throws Throwable {
        File excel = null;
        String excelRealPath = "";
        String excelFileHeadName = "";
        List outputList = null;
        if(DISPLAY_BLOCK.equals(b005DTdisplay)) {
            //工程が「秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B005;
            excelFileHeadName = GAMEN_NAME_202B005;
            outputList = b005ListData;
        }else if(DISPLAY_BLOCK.equals(b006DTdisplay)) {
            //工程が「ﾎﾟｯﾄ粉砕」の場合
            excelRealPath = JSON_FILE_PATH_202B006;
            excelFileHeadName = GAMEN_NAME_202B006;
            outputList = b006ListData;
        }
        
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath(excelRealPath)); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(outputList, list, myParam.getString("download_temp"), excelFileHeadName);

            // ダウンロードファイル名
            String downloadFileName = excelFileHeadName + "_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
                ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
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
                    ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
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
        //品名
        String paramHinmei = StringUtil.blankToNull(getHinmei());       
        
        //秤量日(FROM)
        Date paramHyoryoDateF = null;
        if (!StringUtil.isEmpty(hyoryoDateF)) {
            paramHyoryoDateF = DateUtil.convertStringToDate(getHyoryoDateF(), "0000");
        }
        
        //秤量日(TO)
        Date paramHyoryoDateT = null;
        if (!StringUtil.isEmpty(hyoryoDateT)) {
            paramHyoryoDateT = DateUtil.convertStringToDate(getHyoryoDateT(), "2359");
        }
        
        //秤量号機
        String paramGoki = StringUtil.blankToNull(getGoki());
        
        //粉砕開始日(FROM)
        Date paramFunsaiStartDateF = null;
        if (!StringUtil.isEmpty(funsaiStartDateF)) {
            paramFunsaiStartDateF = DateUtil.convertStringToDate(getFunsaiStartDateF(), "0000");
        }
        
        //粉砕開始日(TO)
        Date paramFunsaiStartDateT = null;
        if (!StringUtil.isEmpty(funsaiStartDateT)) {
            paramFunsaiStartDateT = DateUtil.convertStringToDate(getFunsaiStartDateT(), "2359");
        }
        
        //粉砕終了日(FROM)
        Date paramFunsaiEndDateF = null;
        if (!StringUtil.isEmpty(funsaiEndDateF)) {
            paramFunsaiEndDateF = DateUtil.convertStringToDate(getFunsaiEndDateF(), "0000");
        }
        
        //粉砕終了日(TO)
        Date paramFunsaiEndDateT = null;
        if (!StringUtil.isEmpty(funsaiEndDateT)) {
            paramFunsaiEndDateT = DateUtil.convertStringToDate(getFunsaiEndDateT(), "2359");
        }
                        
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));
        
        if(null != cmbKotei)switch (cmbKotei) {
            case "秤量":
                //工程が「秤量」の場合、Ⅲ.画面表示仕様(2)を発行
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                break;
            case "ﾎﾟｯﾄ粉砕":
                //工程が「ﾎﾟｯﾄ粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                params.addAll(Arrays.asList(paramFunsaiStartDateF, paramFunsaiStartDateF));
                params.addAll(Arrays.asList(paramFunsaiStartDateT, paramFunsaiStartDateT));
                params.addAll(Arrays.asList(paramFunsaiEndDateF, paramFunsaiEndDateF));
                params.addAll(Arrays.asList(paramFunsaiEndDateT, paramFunsaiEndDateT));
                break;
            default:
                break;
        }

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
        if(DISPLAY_BLOCK.equals(b005DTdisplay)) {
            //工程が「秤量」の場合
            return !(b005ListData == null || b005ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b006DTdisplay)) {
            //工程が「ﾎﾟｯﾄ粉砕」の場合
            return !(b006ListData == null || b006ListData.isEmpty());
        }
        return false;
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