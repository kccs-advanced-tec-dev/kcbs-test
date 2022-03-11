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
import jp.co.kccs.xhd.model.GXHDO202B014Model;
import jp.co.kccs.xhd.model.GXHDO202B015Model;
import jp.co.kccs.xhd.model.GXHDO202B016Model;
import jp.co.kccs.xhd.model.GXHDO202B017Model;
import jp.co.kccs.xhd.model.GXHDO202B018Model;
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
 * 変更日       2021/11/22<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾊﾞｲﾝﾀﾞｰ溶液作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/11/22
 */
@Named
@ViewScoped
public class GXHDO202B004 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B004.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量 一覧表示データ */
    private List<GXHDO202B014Model> b014ListData = null;
    
    /** ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ */
    private List<GXHDO202B015Model> b015ListData = null;
    
    /** ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量 一覧表示データ */
    private List<GXHDO202B016Model> b016ListData = null;
    
    /** ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌 一覧表示データ */
    private List<GXHDO202B017Model> b017ListData = null;
    
    /** ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ */
    private List<GXHDO202B018Model> b018ListData = null;

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
    /** 検索条件：秤量号機 */
    private String goki = "";
    
    /** 検索条件：秤量開始日(FROM) */
    private String hyoryoDateF = "";
    /** 検索条件：秤量開始日(TO) */
    private String hyoryoDateT = "";
    /** 検索条件：ﾌｨﾙﾀｰ交換日(FROM) */
    private String filterkoukanDateF = "";
    /** 検索条件：ﾌｨﾙﾀｰ交換日(TO) */
    private String filterkoukanDateT = "";    
    /** 検索条件：粉末投入開始日(FROM) */
    private String funmatutounyuuDateF = "";
    /** 検索条件：粉末投入開始日(TO) */
    private String funmatutounyuuDateT = "";    
    /** 検索条件：圧送開始日(FROM) */
    private String assoukaisiDateF = "";
    /** 検索条件：圧送開始日(TO) */
    private String assoukaisiDateT = "";
    
    /** b014Listの制御 */
    private String b014DTdisplay;
    /** b015Listの制御 */
    private String b015DTdisplay;
    /** b016Listの制御 */
    private String b016DTdisplay;
    /** b017Listの制御 */
    private String b017DTdisplay;
    /** b018Listの制御 */
    private String b018DTdisplay;
    
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"溶剤秤量","ﾌｨﾙﾀｰﾊﾟｽ","ﾊﾞｲﾝﾀﾞｰ粉秤量","撹拌","ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ"};
    //画面名称 ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量
    private static final String GAMEN_NAME_202B014 = "ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量";
    //画面名称 ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ
    private static final String GAMEN_NAME_202B015 = "ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ";
    //画面名称 ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量
    private static final String GAMEN_NAME_202B016 = "ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量";
    //画面名称 ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌
    private static final String GAMEN_NAME_202B017 = "ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌";
    //画面名称 ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ
    private static final String GAMEN_NAME_202B018 = "ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ";
    //エクセル出力ファイルパス ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量
    private static final String JSON_FILE_PATH_202B014 = "/WEB-INF/classes/resources/json/gxhdo202b014.json";
    //エクセル出力ファイルパス ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ
    private static final String JSON_FILE_PATH_202B015 = "/WEB-INF/classes/resources/json/gxhdo202b015.json";
    //エクセル出力ファイルパス ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量
    private static final String JSON_FILE_PATH_202B016 = "/WEB-INF/classes/resources/json/gxhdo202b016.json";
    //エクセル出力ファイルパス ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌
    private static final String JSON_FILE_PATH_202B017 = "/WEB-INF/classes/resources/json/gxhdo202b017.json";
    //エクセル出力ファイルパス ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ
    private static final String JSON_FILE_PATH_202B018 = "/WEB-INF/classes/resources/json/gxhdo202b018.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B004() {
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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量 一覧表示データ
     * @return the b014ListData
     */
    public List<GXHDO202B014Model> getB014ListData() {
        return b014ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量 一覧表示データ
     * @param b014ListData the b014ListData to set
     */
    public void setB014ListData(List<GXHDO202B014Model> b014ListData) {
        this.b014ListData = b014ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ
     * @return the b015ListData
     */
    public List<GXHDO202B015Model> getB015ListData() {
        return b015ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ
     * @param b015ListData the b015ListData to set
     */
    public void setB015ListData(List<GXHDO202B015Model> b015ListData) {
        this.b015ListData = b015ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量 一覧表示データ
     * @return the b016ListData
     */
    public List<GXHDO202B016Model> getB016ListData() {
        return b016ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量 一覧表示データ
     * @param b016ListData the b016ListData to set
     */
    public void setB016ListData(List<GXHDO202B016Model> b016ListData) {
        this.b016ListData = b016ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌 一覧表示データ
     * @return the b017ListData
     */
    public List<GXHDO202B017Model> getB017ListData() {
        return b017ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌 一覧表示データ
     * @param b017ListData the b017ListData to set
     */
    public void setB017ListData(List<GXHDO202B017Model> b017ListData) {
        this.b017ListData = b017ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ
     * @return the b018ListData
     */
    public List<GXHDO202B018Model> getB018ListData() {
        return b018ListData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ 一覧表示データ
     * @param b018ListData the b018ListData to set
     */
    public void setB018ListData(List<GXHDO202B018Model> b018ListData) {
        this.b018ListData = b018ListData;
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
     * 検索条件：秤量開始日(FROM)
     * @return the hyoryoDateF
     */
    public String getHyoryoDateF() {
        return hyoryoDateF;
    }

    /**
     * 検索条件：秤量開始日(FROM)
     * @param hyoryoDateF the hyoryoDateF to set
     */
    public void setHyoryoDateF(String hyoryoDateF) {
        this.hyoryoDateF = hyoryoDateF;
    }

    /**
     * 検索条件：秤量開始日(TO)
     * @return the hyoryoDateT
     */
    public String getHyoryoDateT() {
        return hyoryoDateT;
    }

    /**
     * 検索条件：秤量開始日(TO)
     * @param hyoryoDateT the hyoryoDateT to set
     */
    public void setHyoryoDateT(String hyoryoDateT) {
        this.hyoryoDateT = hyoryoDateT;
    }

    /**
     * 検索条件：ﾌｨﾙﾀｰ交換日(FROM)
     * @return the filterkoukanDateF
     */
    public String getFilterkoukanDateF() {
        return filterkoukanDateF;
    }

    /**
     * 検索条件：ﾌｨﾙﾀｰ交換日(FROM)
     * @param filterkoukanDateF the filterkoukanDateF to set
     */
    public void setFilterkoukanDateF(String filterkoukanDateF) {
        this.filterkoukanDateF = filterkoukanDateF;
    }

    /**
     * 検索条件：ﾌｨﾙﾀｰ交換日(TO)
     * @return the filterkoukanDateT
     */
    public String getFilterkoukanDateT() {
        return filterkoukanDateT;
    }

    /**
     * 検索条件：ﾌｨﾙﾀｰ交換日(TO)
     * @param filterkoukanDateT the filterkoukanDateT to set
     */
    public void setFilterkoukanDateT(String filterkoukanDateT) {
        this.filterkoukanDateT = filterkoukanDateT;
    }

    /**
     * 検索条件：粉末投入開始日(FROM)
     * @return the funmatutounyuuDateF
     */
    public String getFunmatutounyuuDateF() {
        return funmatutounyuuDateF;
    }

    /**
     * 検索条件：粉末投入開始日(FROM)
     * @param funmatutounyuuDateF the funmatutounyuuDateF to set
     */
    public void setFunmatutounyuuDateF(String funmatutounyuuDateF) {
        this.funmatutounyuuDateF = funmatutounyuuDateF;
    }

    /**
     * 検索条件：粉末投入開始日(TO)
     * @return the funmatutounyuuDateT
     */
    public String getFunmatutounyuuDateT() {
        return funmatutounyuuDateT;
    }

    /**
     * 検索条件：粉末投入開始日(TO)
     * @param funmatutounyuuDateT the funmatutounyuuDateT to set
     */
    public void setFunmatutounyuuDateT(String funmatutounyuuDateT) {
        this.funmatutounyuuDateT = funmatutounyuuDateT;
    }

    /**
     * 検索条件：圧送開始日(FROM)
     * @return the assoukaisiDateF
     */
    public String getAssoukaisiDateF() {
        return assoukaisiDateF;
    }

    /**
     * 検索条件：圧送開始日(FROM)
     * @param assoukaisiDateF the assoukaisiDateF to set
     */
    public void setAssoukaisiDateF(String assoukaisiDateF) {
        this.assoukaisiDateF = assoukaisiDateF;
    }

    /**
     * 検索条件：圧送開始日(TO)
     * @return the assoukaisiDateT
     */
    public String getAssoukaisiDateT() {
        return assoukaisiDateT;
    }

    /**
     * 検索条件：圧送開始日(TO)
     * @param assoukaisiDateT the assoukaisiDateT to set
     */
    public void setAssoukaisiDateT(String assoukaisiDateT) {
        this.assoukaisiDateT = assoukaisiDateT;
    }

    /**
     * b014Listの制御
     * @return the b014DTdisplay
     */
    public String getB014DTdisplay() {
        return b014DTdisplay;
    }

    /**
     * b014Listの制御
     * @param b014DTdisplay the b014DTdisplay to set
     */
    public void setB014DTdisplay(String b014DTdisplay) {
        this.b014DTdisplay = b014DTdisplay;
    }

    /**
     * b015Listの制御
     * @return the b015DTdisplay
     */
    public String getB015DTdisplay() {
        return b015DTdisplay;
    }

    /**
     * b015Listの制御
     * @param b015DTdisplay the b015DTdisplay to set
     */
    public void setB015DTdisplay(String b015DTdisplay) {
        this.b015DTdisplay = b015DTdisplay;
    }

    /**
     * b016Listの制御
     * @return the b016DTdisplay
     */
    public String getB016DTdisplay() {
        return b016DTdisplay;
    }

    /**
     * b016Listの制御
     * @param b016DTdisplay the b016DTdisplay to set
     */
    public void setB016DTdisplay(String b016DTdisplay) {
        this.b016DTdisplay = b016DTdisplay;
    }

    /**
     * b017Listの制御
     * @return the b017DTdisplay
     */
    public String getB017DTdisplay() {
        return b017DTdisplay;
    }

    /**
     * b017Listの制御
     * @param b017DTdisplay the b017DTdisplay to set
     */
    public void setB017DTdisplay(String b017DTdisplay) {
        this.b017DTdisplay = b017DTdisplay;
    }

    /**
     * b018Listの制御
     * @return the b018DTdisplay
     */
    public String getB018DTdisplay() {
        return b018DTdisplay;
    }

    /**
     * b018Listの制御
     * @param b018DTdisplay the b018DTdisplay to set
     */
    public void setB018DTdisplay(String b018DTdisplay) {
        this.b018DTdisplay = b018DTdisplay;
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
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B004_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B004_display_page_count", session));
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
        goki = "";
        setHyoryoDateF("");
        setHyoryoDateT("");
        setFilterkoukanDateF("");
        setFilterkoukanDateT("");
        setFunmatutounyuuDateF("");
        setFunmatutounyuuDateT("");
        setAssoukaisiDateF("");
        setAssoukaisiDateT("");

        setB014ListData(new ArrayList<>());
        setB015ListData(new ArrayList<>());
        setB016ListData(new ArrayList<>());
        setB017ListData(new ArrayList<>());
        setB018ListData(new ArrayList<>());

        setB014DTdisplay(DISPLAY_NONE);
        setB015DTdisplay(DISPLAY_NONE);
        setB016DTdisplay(DISPLAY_NONE);
        setB017DTdisplay(DISPLAY_NONE);
        setB018DTdisplay(DISPLAY_NONE);
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
        if(!StringUtil.isEmpty(getLotNo()) && StringUtil.getLength(getLotNo()) != 15){
         FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000004", "ﾛｯﾄNo", "15"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        // 秤量開始日(FROM)
        if (existError(validateUtil.checkC101(getHyoryoDateF(), "秤量開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHyoryoDateF(), "秤量開始日(from)")) ||
            existError(validateUtil.checkC501(getHyoryoDateF(), "秤量開始日(from)"))) {
            return;
        }
        // 秤量開始日(TO)
        if (existError(validateUtil.checkC101(getHyoryoDateT(), "秤量開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHyoryoDateT(), "秤量開始日(to)")) ||
            existError(validateUtil.checkC501(getHyoryoDateT(), "秤量開始日(to)"))) {
            return;
        }
        // ﾌｨﾙﾀｰ交換日(FROM)
        if (existError(validateUtil.checkC101(getFilterkoukanDateF(), "ﾌｨﾙﾀｰ交換日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFilterkoukanDateF(), "ﾌｨﾙﾀｰ交換日(from)")) ||
            existError(validateUtil.checkC501(getFilterkoukanDateF(), "ﾌｨﾙﾀｰ交換日(from)"))) {
            return;
        }
        // ﾌｨﾙﾀｰ交換日(TO)
        if (existError(validateUtil.checkC101(getFilterkoukanDateT(), "ﾌｨﾙﾀｰ交換日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFilterkoukanDateT(), "ﾌｨﾙﾀｰ交換日(to)")) ||
            existError(validateUtil.checkC501(getFilterkoukanDateT(), "ﾌｨﾙﾀｰ交換日(to)"))) {
            return;
        }
        // 粉末投入開始日(FROM)
        if (existError(validateUtil.checkC101(getFunmatutounyuuDateF(), "粉末投入開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunmatutounyuuDateF(), "粉末投入開始日(from)")) ||
            existError(validateUtil.checkC501(getFunmatutounyuuDateF(), "粉末投入開始日(from)"))) {
            return;
        }
        // 粉末投入開始日(TO)
        if (existError(validateUtil.checkC101(getFunmatutounyuuDateT(), "粉末投入開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunmatutounyuuDateT(), "粉末投入開始日(to)")) ||
            existError(validateUtil.checkC501(getFunmatutounyuuDateT(), "粉末投入開始日(to)"))) {
            return;
        }
        // 圧送開始日(FROM)
        if (existError(validateUtil.checkC101(getAssoukaisiDateF(), "圧送開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getAssoukaisiDateF(), "圧送開始日(from)")) ||
            existError(validateUtil.checkC501(getAssoukaisiDateF(), "圧送開始日(from)"))) {
            return;
        }
        // 圧送開始日(TO)
        if (existError(validateUtil.checkC101(getAssoukaisiDateT(), "圧送開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getAssoukaisiDateT(), "圧送開始日(to)")) ||
            existError(validateUtil.checkC501(getAssoukaisiDateT(), "圧送開始日(to)"))) {
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
                case "溶剤秤量":
                    // 工程が「溶剤秤量」の場合、Ⅲ.画面表示仕様(3)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_binder_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) ";
                    break;
                case "ﾌｨﾙﾀｰﾊﾟｽ":
                    // 工程が「ﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(7)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_binder_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR filterkoukannichiji >= ?) "
                            + "   AND (? IS NULL OR filterkoukannichiji <= ?) ";
                    break;
                case "ﾊﾞｲﾝﾀﾞｰ粉秤量":
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ粉秤量」の場合、Ⅲ.画面表示仕様(9)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_binder_powder "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) ";
                    break;
                case "撹拌":
                    // 工程が「撹拌」の場合、Ⅲ.画面表示仕様(11)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_binder_kakuhan "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR funmatutounyuu_kaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funmatutounyuu_kaisinichiji <= ?) ";
                    break;
                case "ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ":
                    // 工程が「ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(13)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_binder_bfp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR assoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR assoukaisinichiji <= ?) ";
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
            setB014ListData(new ArrayList<>());
            setB015ListData(new ArrayList<>());
            setB016ListData(new ArrayList<>());
            setB017ListData(new ArrayList<>());
            setB018ListData(new ArrayList<>());
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB014DTdisplay(DISPLAY_NONE);
        setB015DTdisplay(DISPLAY_NONE);
        setB016DTdisplay(DISPLAY_NONE);
        setB017DTdisplay(DISPLAY_NONE);
        setB018DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "溶剤秤量":{
                    // 工程が「溶剤秤量」の場合、Ⅲ.画面表示仕様(2)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", binderyouekihinmei"
                            + ", binderyouekilotno"
                            + ", lotkubun"
                            + ", goki"
                            + ", hyouryoukaisinichiji"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikono1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikono2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikono1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikono2"
                            + ", youzai2_tyougouryou2"
                            + ", youzai3_zairyouhinmei"
                            + ", youzai3_tyougouryoukikaku"
                            + ", youzai3_buzaizaikono1"
                            + ", youzai3_tyougouryou1"
                            + ", youzai3_buzaizaikono2"
                            + ", youzai3_tyougouryou2"
                            + ", youzai4_zairyouhinmei"
                            + ", youzai4_tyougouryoukikaku"
                            + ", youzai4_buzaizaikono1"
                            + ", youzai4_tyougouryou1"
                            + ", youzai4_buzaizaikono2"
                            + ", youzai4_tyougouryou2"
                            + ", youzai5_zairyouhinmei"
                            + ", youzai5_tyougouryoukikaku"
                            + ", youzai5_buzaizaikono1"
                            + ", youzai5_tyougouryou1"
                            + ", youzai5_buzaizaikono2"
                            + ", youzai5_tyougouryou2"
                            + ", youzai6_zairyouhinmei"
                            + ", youzai6_tyougouryoukikaku"
                            + ", youzai6_buzaizaikono1"
                            + ", youzai6_tyougouryou1"
                            + ", youzai6_buzaizaikono2"
                            + ", youzai6_tyougouryou2"
                            + ", hyouryousyuuryounichiji"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_binder_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) "
                            + " ORDER BY hyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("binderyouekihinmei", "binderyouekihinmei");                // ﾊﾞｲﾝﾀﾞｰ溶液品名
                    mapping.put("binderyouekilotno", "binderyouekilotno");                  // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("goki", "goki");                                            // 秤量号機
                    mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");            // 秤量開始日時
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");          // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");  // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikono1", "youzai1_buzaizaikono1");          // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");            // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikono2", "youzai1_buzaizaikono2");          // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");            // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");          // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");  // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikono1", "youzai2_buzaizaikono1");          // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");            // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikono2", "youzai2_buzaizaikono2");          // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");            // 溶剤②_調合量2
                    mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");          // 溶剤③_材料品名
                    mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");  // 溶剤③_調合量規格
                    mapping.put("youzai3_buzaizaikono1", "youzai3_buzaizaikono1");          // 溶剤③_部材在庫No1
                    mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");            // 溶剤③_調合量1
                    mapping.put("youzai3_buzaizaikono2", "youzai3_buzaizaikono2");          // 溶剤③_部材在庫No2
                    mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");            // 溶剤③_調合量2
                    mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");          // 溶剤④_材料品名
                    mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");  // 溶剤④_調合量規格
                    mapping.put("youzai4_buzaizaikono1", "youzai4_buzaizaikono1");          // 溶剤④_部材在庫No1
                    mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");            // 溶剤④_調合量1
                    mapping.put("youzai4_buzaizaikono2", "youzai4_buzaizaikono2");          // 溶剤④_部材在庫No2
                    mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");            // 溶剤④_調合量2
                    mapping.put("youzai5_zairyouhinmei", "youzai5_zairyouhinmei");          // 溶剤⑤_材料品名
                    mapping.put("youzai5_tyougouryoukikaku", "youzai5_tyougouryoukikaku");  // 溶剤⑤_調合量規格
                    mapping.put("youzai5_buzaizaikono1", "youzai5_buzaizaikono1");          // 溶剤⑤_部材在庫No1
                    mapping.put("youzai5_tyougouryou1", "youzai5_tyougouryou1");            // 溶剤⑤_調合量1
                    mapping.put("youzai5_buzaizaikono2", "youzai5_buzaizaikono2");          // 溶剤⑤_部材在庫No2
                    mapping.put("youzai5_tyougouryou2", "youzai5_tyougouryou2");            // 溶剤⑤_調合量2
                    mapping.put("youzai6_zairyouhinmei", "youzai6_zairyouhinmei");          // 溶剤⑥_材料品名
                    mapping.put("youzai6_tyougouryoukikaku", "youzai6_tyougouryoukikaku");  // 溶剤⑥_調合量規格
                    mapping.put("youzai6_buzaizaikono1", "youzai6_buzaizaikono1");          // 溶剤⑥_部材在庫No1
                    mapping.put("youzai6_tyougouryou1", "youzai6_tyougouryou1");            // 溶剤⑥_調合量1
                    mapping.put("youzai6_buzaizaikono2", "youzai6_buzaizaikono2");          // 溶剤⑥_部材在庫No2
                    mapping.put("youzai6_tyougouryou2", "youzai6_tyougouryou2");            // 溶剤⑥_調合量2
                    mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");      // 秤量終了日時
                    mapping.put("tantousya", "tantousya");                                  // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                // 確認者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B014Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B014Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB014ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB014DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾌｨﾙﾀｰﾊﾟｽ":{
                    // 工程が「ﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(6)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", binderyouekihinmei"
                            + ", binderyouekilotno"
                            + ", lotkubun"
                            + ", filterkoukannichiji"
                            + ", filterhinmei"
                            + ", fplotno"
                            + ", toritukehonsuu"
                            + ", fpjyunbi_tantousya"
                            + ", fpkaisinichiji"
                            + ", fpkaisi_tantousya"
                            + ", fpsyuuryounichiji"
                            + ", fpsyuuryou_tantousya"
                            + ", fpjikan"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_binder_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR filterkoukannichiji >= ?) "
                            + "   AND (? IS NULL OR filterkoukannichiji <= ?) "
                            + " ORDER BY filterkoukannichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                // ﾛｯﾄNo
                    mapping.put("binderyouekihinmei", "binderyouekihinmei");      // ﾊﾞｲﾝﾀﾞｰ溶液品名
                    mapping.put("binderyouekilotno", "binderyouekilotno");        // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
                    mapping.put("lotkubun", "lotkubun");                          // ﾛｯﾄ区分
                    mapping.put("filterkoukannichiji", "filterkoukannichiji");    // ﾌｨﾙﾀｰ交換日時
                    mapping.put("filterhinmei", "filterhinmei");                  // ﾌｨﾙﾀｰ品名
                    mapping.put("fplotno", "fplotno");                            // LotNo
                    mapping.put("toritukehonsuu", "toritukehonsuu");              // 取り付け本数
                    mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");      // F/P準備_担当者
                    mapping.put("fpkaisinichiji", "fpkaisinichiji");              // F/P開始日時
                    mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");        // F/P開始_担当者
                    mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");        // F/P終了日時
                    mapping.put("fpsyuuryou_tantousya", "fpsyuuryou_tantousya");  // F/P終了_担当者
                    mapping.put("fpjikan", "fpjikan");                            // F/P時間
                    mapping.put("bikou1", "bikou1");                              // 備考1
                    mapping.put("bikou2", "bikou2");                              // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B015Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B015Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB015ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB015DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾊﾞｲﾝﾀﾞｰ粉秤量":{
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ粉秤量」の場合、Ⅲ.画面表示仕様(8)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", binderyouekihinmei"
                            + ", binderyouekilotno"
                            + ", lotkubun"
                            + ", goki"
                            + ", Dispagouki as dispagouki"
                            + ", fuutaijyuuryou"
                            + ", hyouryoukaisinichiji"
                            + ", binderjyusi1_zairyouhinmei"
                            + ", binderjyusi1_tyougouryoukikaku"
                            + ", binderjyusi1_buzaizaikono1"
                            + ", binderjyusi1_tyougouryou1"
                            + ", binderjyusi1_buzaizaikono2"
                            + ", binderjyusi1_tyougouryou2"
                            + ", binderjyusi2_zairyouhinmei"
                            + ", binderjyusi2_tyougouryoukikaku"
                            + ", binderjyusi2_buzaizaikono1"
                            + ", binderjyusi2_tyougouryou1"
                            + ", binderjyusi2_buzaizaikono2"
                            + ", binderjyusi2_tyougouryou2"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikono1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikono2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikono1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikono2"
                            + ", youzai2_tyougouryou2"
                            + ", hyouryousyuuryounichiji"
                            + ", soujyuuryou"
                            + ", syoumijyuuryou"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_binder_powder "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) "
                            + " ORDER BY hyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                    // ﾛｯﾄNo
                    mapping.put("binderyouekihinmei", "binderyouekihinmei");                          // ﾊﾞｲﾝﾀﾞｰ溶液品名
                    mapping.put("binderyouekilotno", "binderyouekilotno");                            // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
                    mapping.put("lotkubun", "lotkubun");                                              // ﾛｯﾄ区分
                    mapping.put("goki", "goki");                                                      // 秤量号機
                    mapping.put("dispagouki", "dispagouki");                                          // ﾃﾞｨｽﾊﾟ号機
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                                  // 風袋重量
                    mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                      // 秤量開始日時
                    mapping.put("binderjyusi1_zairyouhinmei", "binderjyusi1_zairyouhinmei");          // ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
                    mapping.put("binderjyusi1_tyougouryoukikaku", "binderjyusi1_tyougouryoukikaku");  // ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
                    mapping.put("binderjyusi1_buzaizaikono1", "binderjyusi1_buzaizaikono1");          // ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
                    mapping.put("binderjyusi1_tyougouryou1", "binderjyusi1_tyougouryou1");            // ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
                    mapping.put("binderjyusi1_buzaizaikono2", "binderjyusi1_buzaizaikono2");          // ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
                    mapping.put("binderjyusi1_tyougouryou2", "binderjyusi1_tyougouryou2");            // ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
                    mapping.put("binderjyusi2_zairyouhinmei", "binderjyusi2_zairyouhinmei");          // ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
                    mapping.put("binderjyusi2_tyougouryoukikaku", "binderjyusi2_tyougouryoukikaku");  // ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
                    mapping.put("binderjyusi2_buzaizaikono1", "binderjyusi2_buzaizaikono1");          // ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
                    mapping.put("binderjyusi2_tyougouryou1", "binderjyusi2_tyougouryou1");            // ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
                    mapping.put("binderjyusi2_buzaizaikono2", "binderjyusi2_buzaizaikono2");          // ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
                    mapping.put("binderjyusi2_tyougouryou2", "binderjyusi2_tyougouryou2");            // ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                    // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");            // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikono1", "youzai1_buzaizaikono1");                    // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                      // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikono2", "youzai1_buzaizaikono2");                    // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                      // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                    // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");            // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikono1", "youzai2_buzaizaikono1");                    // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                      // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikono2", "youzai2_buzaizaikono2");                    // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                      // 溶剤②_調合量2
                    mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");                // 秤量終了日時
                    mapping.put("soujyuuryou", "soujyuuryou");                                        // 総重量
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");                                  // 正味重量
                    mapping.put("tantousya", "tantousya");                                            // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                          // 確認者
                    mapping.put("bikou1", "bikou1");                                                  // 備考1
                    mapping.put("bikou2", "bikou2");                                                  // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B016Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B016Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB016ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB016DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "撹拌":{
                    // 工程が「撹拌」の場合、Ⅲ.画面表示仕様(10)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", binderyouekihinmei"
                            + ", binderyouekilotno"
                            + ", lotkubun"
                            + ", dispagouki"
                            + ", fuutaijyuuryou"
                            + ", funmatutounyuu_kaisinichiji"
                            + ", funmatutounyuu_syuuryounichiji"
                            + ", hanenosyurui"
                            + ", kakuhan_kaisinichiji"
                            + ", kakuhan_syuuryounichiji"
                            + ", kakuhanjikan"
                            + ", setteikaitensuu"
                            + ", kaitensuu"
                            + ", agingjikankitei"
                            + ", aging_kaisinichiji"
                            + ", aging_syuuryounichiji"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_binder_kakuhan "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR funmatutounyuu_kaisinichiji >= ?) "
                            + "   AND (? IS NULL OR funmatutounyuu_kaisinichiji <= ?) "
                            + " ORDER BY funmatutounyuu_kaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                      // ﾛｯﾄNo
                    mapping.put("binderyouekihinmei", "binderyouekihinmei");                            // ﾊﾞｲﾝﾀﾞｰ溶液品名
                    mapping.put("binderyouekilotno", "binderyouekilotno");                              // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
                    mapping.put("lotkubun", "lotkubun");                                                // ﾛｯﾄ区分
                    mapping.put("dispagouki", "dispagouki");                                            // ﾃﾞｨｽﾊﾟ号機
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                                    // 風袋重量
                    mapping.put("funmatutounyuu_kaisinichiji", "funmatutounyuu_kaisinichiji");          // 粉末投入_開始日時
                    mapping.put("funmatutounyuu_syuuryounichiji", "funmatutounyuu_syuuryounichiji");    // 粉末投入_終了日時
                    mapping.put("hanenosyurui", "hanenosyurui");                                        // 羽根の種類
                    mapping.put("kakuhan_kaisinichiji", "kakuhan_kaisinichiji");                        // 撹拌_開始日時
                    mapping.put("kakuhan_syuuryounichiji", "kakuhan_syuuryounichiji");                  // 撹拌_終了日時
                    mapping.put("kakuhanjikan", "kakuhanjikan");                                        // 撹拌時間
                    mapping.put("setteikaitensuu", "setteikaitensuu");                                  // 設定回転数
                    mapping.put("kaitensuu", "kaitensuu");                                              // 回転数
                    mapping.put("agingjikankitei", "agingjikankitei");                                  // ｴｰｼﾞﾝｸﾞ時間規定
                    mapping.put("aging_kaisinichiji", "aging_kaisinichiji");                            // ｴｰｼﾞﾝｸﾞ_開始日時
                    mapping.put("aging_syuuryounichiji", "aging_syuuryounichiji");                      // ｴｰｼﾞﾝｸﾞ_終了日時
                    mapping.put("tantousya", "tantousya");                                              // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                            // 確認者
                    mapping.put("bikou1", "bikou1");                                                    // 備考1
                    mapping.put("bikou2", "bikou2");                                                    // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B017Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B017Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB017ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB017DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ":{
                    // 工程が「ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(12)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", binderyouekihinmei"
                            + ", binderyouekilotno"
                            + ", lotkubun"
                            + ", dispagouki"
                            + ", fuutaijyuuryou"
                            + ", filterhinmei"
                            + ", filterlotno"
                            + ", aturyoku"
                            + ", assoukaisinichiji"
                            + ", assousyuuryounichiji"
                            + ", assoujikan"
                            + ", filtersiyouhonsuu"
                            + ", soujyuuryousokutei"
                            + ", syoumijyuuryou"
                            + ", binderyuukoukigen"
                            + ", sokuteinichiji"
                            + ", nendosokuteiti"
                            + ", ondo"
                            + ", (CASE WHEN gouhihantei = 0 THEN '不合格' WHEN gouhihantei = 1 THEN '合格' ELSE '' END) AS gouhihantei "
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_binder_bfp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR binderyouekihinmei = ?) "
                            + "   AND (? IS NULL OR assoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR assoukaisinichiji <= ?) "
                            + " ORDER BY assoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                // ﾛｯﾄNo
                    mapping.put("binderyouekihinmei", "binderyouekihinmei");      // ﾊﾞｲﾝﾀﾞｰ溶液品名
                    mapping.put("binderyouekilotno", "binderyouekilotno");        // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
                    mapping.put("lotkubun", "lotkubun");                          // ﾛｯﾄ区分
                    mapping.put("dispagouki", "dispagouki");                      // ﾃﾞｨｽﾊﾟ号機
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");              // 風袋重量
                    mapping.put("filterhinmei", "filterhinmei");                  // ﾌｨﾙﾀｰ品名
                    mapping.put("filterlotno", "filterlotno");                    // ﾌｨﾙﾀｰLotNo
                    mapping.put("aturyoku", "aturyoku");                          // 圧力
                    mapping.put("assoukaisinichiji", "assoukaisinichiji");        // 圧送開始日時
                    mapping.put("assousyuuryounichiji", "assousyuuryounichiji");  // 圧送終了日時
                    mapping.put("assoujikan", "assoujikan");                      // 圧送時間
                    mapping.put("filtersiyouhonsuu", "filtersiyouhonsuu");        // ﾌｨﾙﾀｰ使用本数
                    mapping.put("soujyuuryousokutei", "soujyuuryousokutei");      // 総重量測定
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");              // 正味重量
                    mapping.put("binderyuukoukigen", "binderyuukoukigen");        // ﾊﾞｲﾝﾀﾞｰ有効期限
                    mapping.put("sokuteinichiji", "sokuteinichiji");              // 測定日時
                    mapping.put("nendosokuteiti", "nendosokuteiti");              // 粘度測定値
                    mapping.put("ondo", "ondo");                                  // 温度
                    mapping.put("gouhihantei", "gouhihantei");                    // 合否判定
                    mapping.put("tantousya", "tantousya");                        // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                      // 確認者
                    mapping.put("bikou1", "bikou1");                              // 備考1
                    mapping.put("bikou2", "bikou2");                              // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B018Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B018Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b018ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB018DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }
            
        } catch (SQLException ex) {
            setB014ListData(new ArrayList<>());
            setB015ListData(new ArrayList<>());
            setB016ListData(new ArrayList<>());
            setB017ListData(new ArrayList<>());
            setB018ListData(new ArrayList<>());
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
        if(DISPLAY_BLOCK.equals(getB014DTdisplay())) {
            //工程が「溶剤秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B014;
            excelFileHeadName = GAMEN_NAME_202B014;
            outputList = getB014ListData();
        }else if(DISPLAY_BLOCK.equals(getB015DTdisplay())) {
            //工程が「ﾌｨﾙﾀｰﾊﾟｽ」の場合
            excelRealPath = JSON_FILE_PATH_202B015;
            excelFileHeadName = GAMEN_NAME_202B015;
            outputList = getB015ListData();
        }else if(DISPLAY_BLOCK.equals(getB016DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ粉秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B016;
            excelFileHeadName = GAMEN_NAME_202B016;
            outputList = getB016ListData();
        }else if(DISPLAY_BLOCK.equals(getB017DTdisplay())) {
            //工程が「撹拌」の場合
            excelRealPath = JSON_FILE_PATH_202B017;
            excelFileHeadName = GAMEN_NAME_202B017;
            outputList = getB017ListData();
        }else if(DISPLAY_BLOCK.equals(getB018DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ」の場合
            excelRealPath = JSON_FILE_PATH_202B018;
            excelFileHeadName = GAMEN_NAME_202B018;
            outputList = getB018ListData();
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
            paramLotNo = StringUtils.substring(getLotNo(), 3, 12);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 12, 15));
        }
        //品名
        String paramHinmei = StringUtil.blankToNull(getHinmei());
        
        //秤量号機
        String paramGoki = StringUtil.blankToNull(getGoki());
           
        //秤量開始日(FROM)
        Date paramHyoryoDateF = null;
        if (!StringUtil.isEmpty(hyoryoDateF)) {
            paramHyoryoDateF = DateUtil.convertStringToDateInSeconds(getHyoryoDateF(), "000000");
        }
        
        //秤量開始日(TO)
        Date paramHyoryoDateT = null;
        if (!StringUtil.isEmpty(hyoryoDateT)) {
            paramHyoryoDateT = DateUtil.convertStringToDateInSeconds(getHyoryoDateT(), "235959");
        }
        
        //ﾌｨﾙﾀｰ交換日(FROM)
        Date paramFilterkoukanDateF = null;
        if (!StringUtil.isEmpty(filterkoukanDateF)) {
            paramFilterkoukanDateF = DateUtil.convertStringToDateInSeconds(getFilterkoukanDateF(), "000000");
        }
        
        //ﾌｨﾙﾀｰ交換日(TO)
        Date paramFilterkoukanDateT = null;
        if (!StringUtil.isEmpty(filterkoukanDateT)) {
            paramFilterkoukanDateT = DateUtil.convertStringToDateInSeconds(getFilterkoukanDateT(), "235959");
        }
        
        //粉末投入開始日(FROM)
        Date paramFunmatutounyuuDateF = null;
        if (!StringUtil.isEmpty(funmatutounyuuDateF)) {
            paramFunmatutounyuuDateF = DateUtil.convertStringToDateInSeconds(getFunmatutounyuuDateF(), "000000");
        }
        
        //粉末投入開始日(TO)
        Date paramFunmatutounyuuDateT = null;
        if (!StringUtil.isEmpty(funmatutounyuuDateT)) {
            paramFunmatutounyuuDateT = DateUtil.convertStringToDateInSeconds(getFunmatutounyuuDateT(), "235959");
        }
        
        //圧送開始日(FROM)
        Date paramAssoukaisiDateF = null;
        if (!StringUtil.isEmpty(assoukaisiDateF)) {
            paramAssoukaisiDateF = DateUtil.convertStringToDateInSeconds(getAssoukaisiDateF(), "000000");
        }
        
        //圧送開始日(TO)
        Date paramAssoukaisiDateT = null;
        if (!StringUtil.isEmpty(assoukaisiDateT)) {
            paramAssoukaisiDateT = DateUtil.convertStringToDateInSeconds(getAssoukaisiDateT(), "235959");
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));
        
        if(null != cmbKotei)switch (cmbKotei) {
            case "溶剤秤量":
                // 工程が「溶剤秤量」の場合、Ⅲ.画面表示仕様(3)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                break;
            case "ﾌｨﾙﾀｰﾊﾟｽ":
                // 工程が「ﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(7)を発行する。
                params.addAll(Arrays.asList(paramFilterkoukanDateF, paramFilterkoukanDateF));
                params.addAll(Arrays.asList(paramFilterkoukanDateT, paramFilterkoukanDateT));
                break;
            case "ﾊﾞｲﾝﾀﾞｰ粉秤量":
                // 工程が「ﾊﾞｲﾝﾀﾞｰ粉秤量」の場合、Ⅲ.画面表示仕様(9)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                break;
            case "撹拌":
                // 工程が「撹拌」の場合、Ⅲ.画面表示仕様(11)を発行する。
                params.addAll(Arrays.asList(paramFunmatutounyuuDateF, paramFunmatutounyuuDateF));
                params.addAll(Arrays.asList(paramFunmatutounyuuDateT, paramFunmatutounyuuDateT));
                break;
            case "ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ":
                // 工程が「ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ」の場合、Ⅲ.画面表示仕様(13)を発行する。
                params.addAll(Arrays.asList(paramAssoukaisiDateF, paramAssoukaisiDateF));
                params.addAll(Arrays.asList(paramAssoukaisiDateT, paramAssoukaisiDateT));
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
        if(DISPLAY_BLOCK.equals(getB014DTdisplay())) {
            //工程が「溶剤秤量」の場合
            return !(b014ListData == null || b014ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB015DTdisplay())) {
            //工程が「ﾌｨﾙﾀｰﾊﾟｽ」の場合
            return !(b015ListData == null || b015ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB016DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ粉秤量」の場合
            return !(b016ListData == null || b016ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB017DTdisplay())) {
            //工程が「撹拌」の場合
            return !(b017ListData == null || b017ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB018DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ」の場合
            return !(b018ListData == null || b018ListData.isEmpty());
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