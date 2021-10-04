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
import jp.co.kccs.xhd.model.GXHDO202B001Model;
import jp.co.kccs.xhd.model.GXHDO202B002Model;
import jp.co.kccs.xhd.model.GXHDO202B003Model;
import jp.co.kccs.xhd.model.GXHDO202B004Model;
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
 * 変更日       2021/09/10<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶﾞﾗｽ作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/09/10
 */
@Named
@ViewScoped
public class GXHDO202B001 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B001.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** ｶﾞﾗｽ作製・秤量 一覧表示データ */
    private List<GXHDO202B001Model> b001ListData = null;
    
    /** ｶﾞﾗｽ作製・SC粉砕 一覧表示データ */
    private List<GXHDO202B002Model> b002ListData = null;
    
    /** ｶﾞﾗｽ作製・乾燥 一覧表示データ */
    private List<GXHDO202B003Model> b003ListData = null;
    
    /** ｶﾞﾗｽ作製・測定 一覧表示データ */
    private List<GXHDO202B004Model> b004ListData = null;
    
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
    /** 検索条件：ロットNo */
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
    /** 検索条件：乾燥開始日(FROM) */
    private String kansoStartDateF = "";
    /** 検索条件：乾燥開始日(TO) */
    private String kansoStartDateT = "";
    /** 検索条件：乾燥終了日(FROM) */
    private String kansoEndDateF = "";
    /** 検索条件：乾燥終了日(TO) */
    private String kansoEndDateT = "";
    /** b001Listの制御 */
    private String b001DTdisplay;
    /** b002Listの制御 */
    private String b002DTdisplay;
    /** b003Listの制御 */
    private String b003DTdisplay;
    /** b004Listの制御 */
    private String b004DTdisplay;
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"秤量", "SC粉砕", "乾燥", "測定"};
    //画面名称 ｶﾞﾗｽ作製・秤量
    private static final String GAMEN_NAME_202B001 = "ｶﾞﾗｽ作製・秤量";
    //画面名称 ｶﾞﾗｽ作製・SC粉砕
    private static final String GAMEN_NAME_202B002 = "ｶﾞﾗｽ作製・SC粉砕";
    //画面名称 ｶﾞﾗｽ作製・乾燥
    private static final String GAMEN_NAME_202B003 = "ｶﾞﾗｽ作製・乾燥";
    //画面名称 ｶﾞﾗｽ作製・測定
    private static final String GAMEN_NAME_202B004 = "ｶﾞﾗｽ作製・測定";
    //エクセル出力ファイルパス ｶﾞﾗｽ作製・秤量
    private static final String JSON_FILE_PATH_202B001 = "/WEB-INF/classes/resources/json/gxhdo202b001.json";
    //エクセル出力ファイルパス ｶﾞﾗｽ作製・SC粉砕
    private static final String JSON_FILE_PATH_202B002 = "/WEB-INF/classes/resources/json/gxhdo202b002.json";
    //エクセル出力ファイルパス ｶﾞﾗｽ作製・乾燥
    private static final String JSON_FILE_PATH_202B003 = "/WEB-INF/classes/resources/json/gxhdo202b003.json";
    //エクセル出力ファイルパス ｶﾞﾗｽ作製・測定
    private static final String JSON_FILE_PATH_202B004 = "/WEB-INF/classes/resources/json/gxhdo202b004.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B001() {
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
     * ｶﾞﾗｽ作製・秤量 一覧表示データ取得
     * @return ｶﾞﾗｽ作製・秤量 一覧表示データ
     */
    public List<GXHDO202B001Model> getB001ListData() {
        return b001ListData;
    }
    
    /**
     * ｶﾞﾗｽ作製・SC粉砕 一覧表示データ取得
     * @return ｶﾞﾗｽ作製・SC粉砕 一覧表示データ
     */
    public List<GXHDO202B002Model> getB002ListData() {
        return b002ListData;
    }
    
    /**
     * ｶﾞﾗｽ作製・乾燥 一覧表示データ取得
     * @return ｶﾞﾗｽ作製・乾燥 一覧表示データ
     */
    public List<GXHDO202B003Model> getB003ListData() {
        return b003ListData;
    }
    
    /**
     * ｶﾞﾗｽ作製・測定 一覧表示データ取得
     * @return ｶﾞﾗｽ作製・測定 一覧表示データ
     */
    public List<GXHDO202B004Model> getB004ListData() {
        return b004ListData;
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
     * 検索条件：乾燥開始日(FROM)
     * @return the kansoStartDateF
     */
    public String getKansoStartDateF() {
        return kansoStartDateF;
    }

    /**
     * 検索条件：乾燥開始日(FROM)
     * @param kansoStartDateF the kansoStartDateF to set
     */
    public void setKansoStartDateF(String kansoStartDateF) {
        this.kansoStartDateF = kansoStartDateF;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @return the kansoStartDateT
     */
    public String getKansoStartDateT() {
        return kansoStartDateT;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @param kansoStartDateT the kansoStartDateT to set
     */
    public void setKansoStartDateT(String kansoStartDateT) {
        this.kansoStartDateT = kansoStartDateT;
    }

    /**
     * 検索条件：乾燥終了日(FROM)
     * @return the kansoEndDateF
     */
    public String getKansoEndDateF() {
        return kansoEndDateF;
    }

    /**
     * 検索条件：乾燥終了日(FROM)
     * @param kansoEndDateF the kansoEndDateF to set
     */
    public void setKansoEndDateF(String kansoEndDateF) {
        this.kansoEndDateF = kansoEndDateF;
    }

    /**
     * 検索条件：乾燥終了日(TO)
     * @return the kansoEndDateT
     */
    public String getKansoEndDateT() {
        return kansoEndDateT;
    }

    /**
     * 検索条件：乾燥終了日(TO)
     * @param kansoEndDateT the kansoEndDateT to set
     */
    public void setKansoEndDateT(String kansoEndDateT) {
        this.kansoEndDateT = kansoEndDateT;
    }

    /**
     * b001Listの制御
     * @return the b001DTdisplay
     */
    public String getB001DTdisplay() {
        return b001DTdisplay;
    }

    /**
     * b001Listの制御
     * @param b001DTdisplay the b001DTdisplay to set
     */
    public void setB001DTdisplay(String b001DTdisplay) {
        this.b001DTdisplay = b001DTdisplay;
    }

    /**
     * b002Listの制御
     * @return the b002DTdisplay
     */
    public String getB002DTdisplay() {
        return b002DTdisplay;
    }

    /**
     * b002Listの制御
     * @param b002DTdisplay the b002DTdisplay to set
     */
    public void setB002DTdisplay(String b002DTdisplay) {
        this.b002DTdisplay = b002DTdisplay;
    }

    /**
     * b003Listの制御
     * @return the b003DTdisplay
     */
    public String getB003DTdisplay() {
        return b003DTdisplay;
    }

    /**
     * b003Listの制御
     * @param b003DTdisplay the b003DTdisplay to set
     */
    public void setB003DTdisplay(String b003DTdisplay) {
        this.b003DTdisplay = b003DTdisplay;
    }

    /**
     * b004Listの制御
     * @return the b004DTdisplay
     */
    public String getB004DTdisplay() {
        return b004DTdisplay;
    }

    /**
     * b004Listの制御
     * @param b004DTdisplay the b004DTdisplay to set
     */
    public void setB004DTdisplay(String b004DTdisplay) {
        this.b004DTdisplay = b004DTdisplay;
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
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B001_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B001_display_page_count", session));
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
        kansoStartDateF = "";
        kansoStartDateT = "";
        kansoEndDateF = "";
        kansoEndDateT = "";

        b001ListData = new ArrayList<>();
        b002ListData = new ArrayList<>();
        b003ListData = new ArrayList<>();
        b004ListData = new ArrayList<>();

        setB001DTdisplay(DISPLAY_NONE);
        setB002DTdisplay(DISPLAY_NONE);
        setB003DTdisplay(DISPLAY_NONE);
        setB004DTdisplay(DISPLAY_NONE);
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
        // 乾燥開始日(FROM)
        if (existError(validateUtil.checkC101(getKansoStartDateF(), "乾燥開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoStartDateF(), "乾燥開始日(from)")) ||
            existError(validateUtil.checkC501(getKansoStartDateF(), "乾燥開始日(from)"))) {
            return;
        }
        // 乾燥開始日(TO)
        if (existError(validateUtil.checkC101(getKansoStartDateT(), "乾燥開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoStartDateT(), "乾燥開始日(to)")) ||
            existError(validateUtil.checkC501(getKansoStartDateT(), "乾燥開始日(to)"))) {
            return;
        }
        // 乾燥終了日(FROM)
        if (existError(validateUtil.checkC101(getKansoEndDateF(), "乾燥終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoEndDateF(), "乾燥終了日(from)")) ||
            existError(validateUtil.checkC501(getKansoEndDateF(), "乾燥終了日(from)"))) {
            return;
        }
        // 乾燥終了日(TO)
        if (existError(validateUtil.checkC101(getKansoEndDateT(), "乾燥終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoEndDateT(), "乾燥終了日(to)")) ||
            existError(validateUtil.checkC501(getKansoEndDateT(), "乾燥終了日(to)"))) {
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
                            + "  FROM sr_glasshyoryo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR keiryounichiji >= ?) "
                            + "   AND (? IS NULL OR keiryounichiji <= ?) "
                            + "   AND (? IS NULL OR goki = ?) ";
                    break;
                case "SC粉砕":
                    //工程が「SC粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_glassscfunsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji <= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) ";
                    break;
                case "乾燥":
                    //工程が「乾燥」の場合、Ⅲ.画面表示仕様(8)を発行
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_glasskanso "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji <= ?) "
                            + "   AND (? IS NULL OR kansousyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR kansousyuuryounichiji <= ?) " ;
                    break;
                case "測定":
                    //工程が「測定」の場合、Ⅲ.画面表示仕様(10)を発行
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_glasssokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) " ;
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
            b001ListData = new ArrayList<>();
            b002ListData = new ArrayList<>();
            b003ListData = new ArrayList<>();
            b004ListData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB001DTdisplay(DISPLAY_NONE);
        setB002DTdisplay(DISPLAY_NONE);
        setB003DTdisplay(DISPLAY_NONE);
        setB004DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "秤量":{
                    //工程が「秤量」の場合、Ⅲ.画面表示仕様(2)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glasshinmei "
                            + ", glasslotno "
                            + ", lotkubun "
                            + ", goki "
                            + ", zairyohinmei1 "
                            + ", tyogouryoukikaku1 "
                            + ", sizailotno1_1 "
                            + ", tyougouryou1_1 "
                            + ", sizailotno1_2 "
                            + ", tyougouryou1_2 "
                            + ", zairyohinmei2 "
                            + ", tyogouryoukikaku2 "
                            + ", sizailotno2_1 "
                            + ", tyougouryou2_1 "
                            + ", sizailotno2_2 "
                            + ", tyougouryou2_2 "
                            + ", keiryounichiji "
                            + ", tantousya "
                            + ", kakuninsya "
                            + ", bikou1 "
                            + ", bikou2 "
                            + "  FROM sr_glasshyoryo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR keiryounichiji >= ?) "
                            + "   AND (? IS NULL OR keiryounichiji <= ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + " ORDER BY keiryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("glasshinmei", "glasshinmei");                              // ｶﾞﾗｽ品名
                    mapping.put("glasslotno", "glasslotno");                                // ｶﾞﾗｽ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("goki", "goki");                                            // 秤量号機
                    mapping.put("zairyohinmei1", "zairyohinmei1");                          // 材料品名1
                    mapping.put("tyogouryoukikaku1", "tyogouryoukikaku1");                  // 調合量規格1
                    mapping.put("sizailotno1_1", "sizailotno1_1");                          // 資材ﾛｯﾄNo.1_1
                    mapping.put("tyougouryou1_1", "tyougouryou1_1");                        // 調合量1_1
                    mapping.put("sizailotno1_2", "sizailotno1_2");                          // 資材ﾛｯﾄNo.1_2
                    mapping.put("tyougouryou1_2", "tyougouryou1_2");                        // 調合量1_2
                    mapping.put("zairyohinmei2", "zairyohinmei2");                          // 材料品名2
                    mapping.put("tyogouryoukikaku2", "tyogouryoukikaku2");                  // 調合量規格2
                    mapping.put("sizailotno2_1", "sizailotno2_1");                          // 資材ﾛｯﾄNo.2_1
                    mapping.put("tyougouryou2_1", "tyougouryou2_1");                        // 調合量2_1
                    mapping.put("sizailotno2_2", "sizailotno2_2");                          // 資材ﾛｯﾄNo.2_2
                    mapping.put("tyougouryou2_2", "tyougouryou2_2");                        // 調合量2_2
                    mapping.put("keiryounichiji", "keiryounichiji");                        // 計量日時
                    mapping.put("tantousya", "tantousya");                                  // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                // 確認者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B001Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B001Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b001ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB001DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "SC粉砕":{
                    //工程が「SC粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glasshinmei "
                            + ", glasslotno "
                            + ", lotkubun "
                            + ", funsaiki "
                            + ", tamaishikei "
                            + ", tamaishijuryo "
                            + ", kaitensuu "
                            + ", junkanshuhasuu "
                            + ", funsaijikan "
                            + ", kadoujikan "
                            + ", si_ruekikakunin "
                            + ", funsaikaisinichiji "
                            + ", kaisitantosya "
                            + ", funsaisyuuryouyoteinichiji "
                            + ", fukadenryuuti_10min "
                            + ", mirudegutiekion_10min "
                            + ", naiatu_10min "
                            + ", ponpusyuuhasuu_10min "
                            + ", funsaisyuuryounichiji "
                            + ", syuryotantosya "
                            + ", bikou1 "
                            + ", bikou2 "
                            + "  FROM sr_glassscfunsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funsaikaisinichiji <= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) "
                            + " ORDER BY funsaikaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("glasshinmei", "glasshinmei");                              // ｶﾞﾗｽ品名
                    mapping.put("glasslotno", "glasslotno");                                // ｶﾞﾗｽ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("funsaiki", "funsaiki");                                    // 粉砕機
                    mapping.put("tamaishikei", "tamaishikei");                              // 玉石径
                    mapping.put("tamaishijuryo", "tamaishijuryo");                          // 玉石重量
                    mapping.put("kaitensuu", "kaitensuu");                                  // 回転数
                    mapping.put("junkanshuhasuu", "junkanshuhasuu");                        // 循環周波数
                    mapping.put("funsaijikan", "funsaijikan");                              // 粉砕時間
                    mapping.put("kadoujikan", "kadoujikan");                                // 累計稼働時間
                    mapping.put("si_ruekikakunin", "si_ruekikakunin");                      // ｼｰﾙ液量確認
                    mapping.put("funsaikaisinichiji", "funsaikaisinichiji");                // 粉砕開始日時
                    mapping.put("kaisitantosya", "kaisitantosya");                          // 粉砕開始担当者
                    mapping.put("funsaisyuuryouyoteinichiji", "funsaisyuuryouyoteinichiji");// 粉砕終了予定日時
                    mapping.put("fukadenryuuti_10min", "fukadenryuuti_10min");              // 負荷電流値
                    mapping.put("mirudegutiekion_10min", "mirudegutiekion_10min");          // ﾐﾙ出口液温
                    mapping.put("naiatu_10min", "naiatu_10min");                            // 内圧
                    mapping.put("ponpusyuuhasuu_10min", "ponpusyuuhasuu_10min");            // ﾎﾟﾝﾌﾟ周波数
                    mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");          // 粉砕終了日時
                    mapping.put("syuryotantosya", "syuryotantosya");                        // 粉砕終了担当者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B002Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B002Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b002ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB002DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "乾燥":{
                    //工程が「乾燥」の場合、Ⅲ.画面表示仕様(8)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glasshinmei "
                            + ", glasslotno "
                            + ", lotkubun "
                            + ", kansouki "
                            + ", kansoujouken "
                            + ", shindousuu "
                            + ", tamaishikei "
                            + ", tamaishisyurui "
                            + ", tamaishijuryo "
                            + ", tounyuujyuuryou "
                            + ", kansoukaisinichiji "
                            + ", kaisitantosya "
                            + ", kansousyuuryounichiji "
                            + ", syuryotantosya "
                            + ", kansojikan "
                            + ", seitikansoujikan "
                            + ", kanketusindoujikan "
                            + ", kanketuuntenjisindoujikan "
                            + ", kanketuuntenjiseitijikan "
                            + ", garasukaisyuujyuuryou "
                            + ", budomari "
                            + ", bikou1 "
                            + ", bikou2 "
                            + "  FROM sr_glasskanso "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji <= ?) "
                            + "   AND (? IS NULL OR kansousyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR kansousyuuryounichiji <= ?) "
                            + " ORDER BY kansoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("glasshinmei", "glasshinmei");                              // ｶﾞﾗｽ品名
                    mapping.put("glasslotno", "glasslotno");                                // ｶﾞﾗｽ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("kansouki", "kansouki");                                    // 乾燥機
                    mapping.put("kansoujouken", "kansoujouken");                            // 乾燥条件
                    mapping.put("shindousuu", "shindousuu");                                // 振動数
                    mapping.put("tamaishikei", "tamaishikei");                              // 玉石径
                    mapping.put("tamaishisyurui", "tamaishisyurui");                        // 玉石種類
                    mapping.put("tamaishijuryo", "tamaishijuryo");                          // 玉石重量
                    mapping.put("tounyuujyuuryou", "tounyuujyuuryou");                      // ｶﾞﾗｽ投入重量
                    mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                // 乾燥開始日時
                    mapping.put("kaisitantosya", "kaisitantosya");                          // 乾燥開始担当者
                    mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");          // 乾燥終了日時
                    mapping.put("syuryotantosya", "syuryotantosya");                        // 乾燥終了担当者
                    mapping.put("kansojikan", "kansojikan");                                // 乾燥時間
                    mapping.put("seitikansoujikan", "seitikansoujikan");                    // 静置乾燥時間
                    mapping.put("kanketusindoujikan", "kanketusindoujikan");                // 間欠振動時間
                    mapping.put("kanketuuntenjisindoujikan", "kanketuuntenjisindoujikan");  // 間欠運転時振動時間
                    mapping.put("kanketuuntenjiseitijikan", "kanketuuntenjiseitijikan");    // 間欠運転時静置時間
                    mapping.put("garasukaisyuujyuuryou", "garasukaisyuujyuuryou");          // ｶﾞﾗｽ回収重量
                    mapping.put("budomari", "budomari");                                    // 歩留まり
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B003Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B003Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b003ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB003DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "測定":{
                    //工程が「測定」の場合、Ⅲ.画面表示仕様(10)を発行
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", glasshinmei "
                            + ", glasslotno "
                            + ", lotkubun "
                            + ", bet "
                            + ", tantosya "
                            + ", bikou1 "
                            + ", bikou2 "
                            + "  FROM sr_glasssokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR glasshinmei = ?) "
                            + " ORDER BY lotno ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("glasshinmei", "glasshinmei");                              // ｶﾞﾗｽ品名
                    mapping.put("glasslotno", "glasslotno");                                // ｶﾞﾗｽ品名LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("bet", "bet");                                              // BET測定値
                    mapping.put("tantosya", "tantosya");                                    // 担当者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B004Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B004Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b004ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB004DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }
            
        } catch (SQLException ex) {
            b001ListData = new ArrayList<>();
            b002ListData = new ArrayList<>();
            b003ListData = new ArrayList<>();
            b004ListData = new ArrayList<>();
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
        if(DISPLAY_BLOCK.equals(b001DTdisplay)) {
            //工程が「秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B001;
            excelFileHeadName = GAMEN_NAME_202B001;
            outputList = b001ListData;
        }else if(DISPLAY_BLOCK.equals(b002DTdisplay)) {
            //工程が「SC粉砕」の場合
            excelRealPath = JSON_FILE_PATH_202B002;
            excelFileHeadName = GAMEN_NAME_202B002;
            outputList = b002ListData;
        }else if(DISPLAY_BLOCK.equals(b003DTdisplay)) {
            //工程が「乾燥」の場合
            excelRealPath = JSON_FILE_PATH_202B003;
            excelFileHeadName = GAMEN_NAME_202B003;
            outputList = b003ListData;
        }else if(DISPLAY_BLOCK.equals(b004DTdisplay)) {
            //工程が「測定」の場合
            excelRealPath = JSON_FILE_PATH_202B004;
            excelFileHeadName = GAMEN_NAME_202B004;
            outputList = b004ListData;
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
        
        //乾燥開始日(FROM)
        Date paramKansoStartDateF = null;
        if (!StringUtil.isEmpty(kansoStartDateF)) {
            paramKansoStartDateF = DateUtil.convertStringToDate(getKansoStartDateF(), "0000");
        }
        
        //乾燥開始日(TO)
        Date paramKansoStartDateT = null;
        if (!StringUtil.isEmpty(kansoStartDateT)) {
            paramKansoStartDateT = DateUtil.convertStringToDate(getKansoStartDateT(), "2359");
        }
        
        //乾燥終了日(FROM)
        Date paramKansoEndDateF = null;
        if (!StringUtil.isEmpty(kansoEndDateF)) {
            paramKansoEndDateF = DateUtil.convertStringToDate(getKansoEndDateF(), "0000");
        }
        
        //乾燥終了日(TO)
        Date paramKansoEndDateT = null;
        if (!StringUtil.isEmpty(kansoEndDateT)) {
            paramKansoEndDateT = DateUtil.convertStringToDate(getKansoEndDateT(), "2359");
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
            case "SC粉砕":
                //工程が「SC粉砕」の場合、Ⅲ.画面表示仕様(6)を発行
                params.addAll(Arrays.asList(paramFunsaiStartDateF, paramFunsaiStartDateF));
                params.addAll(Arrays.asList(paramFunsaiStartDateT, paramFunsaiStartDateT));
                params.addAll(Arrays.asList(paramFunsaiEndDateF, paramFunsaiEndDateF));
                params.addAll(Arrays.asList(paramFunsaiEndDateT, paramFunsaiEndDateT));
                break;
            case "乾燥":
                //工程が「乾燥」の場合、Ⅲ.画面表示仕様(8)を発行
                params.addAll(Arrays.asList(paramKansoStartDateF, paramKansoStartDateF));
                params.addAll(Arrays.asList(paramKansoStartDateT, paramKansoStartDateT));
                params.addAll(Arrays.asList(paramKansoEndDateF, paramKansoEndDateF));
                params.addAll(Arrays.asList(paramKansoEndDateT, paramKansoEndDateT));
                break;
            case "測定":
                //工程が「測定」の場合、Ⅲ.画面表示仕様(10)を発行
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
        if(DISPLAY_BLOCK.equals(b001DTdisplay)) {
            //工程が「秤量」の場合
            return !(b001ListData == null || b001ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b002DTdisplay)) {
            //工程が「SC粉砕」の場合
            return !(b002ListData == null || b002ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b003DTdisplay)) {
            //工程が「乾燥」の場合
            return !(b003ListData == null || b003ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b004DTdisplay)) {
            //工程が「測定」の場合
            return !(b004ListData == null || b004ListData.isEmpty());
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