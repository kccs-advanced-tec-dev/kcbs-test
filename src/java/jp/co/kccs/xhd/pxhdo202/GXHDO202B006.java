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
import jp.co.kccs.xhd.model.GXHDO202B027Model;
import jp.co.kccs.xhd.model.GXHDO202B028Model;
import jp.co.kccs.xhd.model.GXHDO202B029Model;
import jp.co.kccs.xhd.model.GXHDO202B030Model;
import jp.co.kccs.xhd.model.GXHDO202B031Model;
import jp.co.kccs.xhd.model.GXHDO202B032Model;
import jp.co.kccs.xhd.model.GXHDO202B033Model;
import jp.co.kccs.xhd.model.GXHDO202B034Model;
import jp.co.kccs.xhd.model.GXHDO202B035Model;
import jp.co.kccs.xhd.model.GXHDO202B036Model;
import jp.co.kccs.xhd.model.GXHDO202B037Model;
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
 * 変更日       2021/12/25<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
@Named
@ViewScoped
public class GXHDO202B006 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B006.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ) 一覧表示データ */
    private List<GXHDO202B027Model> b027ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器) 一覧表示データ */
    private List<GXHDO202B028Model> b028ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ) 一覧表示データ */
    private List<GXHDO202B029Model> b029ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器) 一覧表示データ */
    private List<GXHDO202B030Model> b030ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入 一覧表示データ */
    private List<GXHDO202B031Model> b031ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合 一覧表示データ */
    private List<GXHDO202B032Model> b032ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定 一覧表示データ */
    private List<GXHDO202B033Model> b033ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・高圧分散 一覧表示データ */
    private List<GXHDO202B034Model> b034ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・FP(バケツ) 一覧表示データ */
    private List<GXHDO202B035Model> b035ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ) 一覧表示データ */
    private List<GXHDO202B036Model> b036ListData = null;
    
    /** ｽﾘｯﾌﾟ作製・出荷検査 一覧表示データ */
    private List<GXHDO202B037Model> b037ListData = null;

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
    
    /** 検索条件：粉砕終了日(FROM) */
    private String funsaisyuuryouDateF = "";
    /** 検索条件：粉砕終了日(TO) */
    private String funsaisyuuryouDateT = "";
    
    /** 検索条件：溶剤秤量日(FROM) */
    private String youzaikeiryouDateF = "";
    /** 検索条件：溶剤秤量日(TO) */
    private String youzaikeiryouDateT = "";
    
    /** 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(FROM) */
    private String binderkeiryouDateF = "";
    /** 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(TO) */
    private String binderkeiryouDateT = "";
    
    /** 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(FROM) */
    private String binderkongoukaisiDateF = "";
    /** 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(TO) */
    private String binderkongoukaisiDateT = "";
    
    /** 検索条件：乾燥開始日(FROM) */
    private String kansoukaishiDateF = "";
    /** 検索条件：乾燥開始日(TO) */
    private String kansoukaishiDateT = "";
    
    /** 検索条件：高圧分散開始日(FROM) */
    private String kouatsubunsankaishiDateF = "";
    /** 検索条件：高圧分散開始日(TO) */
    private String kouatsubunsankaishiDateT = "";
    
    /** 検索条件：F/P開始日(FROM) */
    private String fpkaishiDateF = "";
    /** 検索条件：F/P開始日(TO) */
    private String fpkaishiDateT = "";
    
    /** 検索条件：乾燥開始日①(FROM) */
    private String kansoukaishi1DateF = "";
    /** 検索条件：乾燥開始日①(TO) */
    private String kansoukaishi1DateT = "";
    
    /** b027Listの制御 */
    private String b027DTdisplay;
    /** b028Listの制御 */
    private String b028DTdisplay;
    /** b029Listの制御 */
    private String b029DTdisplay;
    /** b030Listの制御 */
    private String b030DTdisplay;
    /** b031Listの制御 */
    private String b031DTdisplay;
    /** b032Listの制御 */
    private String b032DTdisplay;
    /** b033Listの制御 */
    private String b033DTdisplay;
    /** b034Listの制御 */
    private String b034DTdisplay;
    /** b035Listの制御 */
    private String b035DTdisplay;
    /** b036Listの制御 */
    private String b036DTdisplay;
    /** b037Listの制御 */
    private String b037DTdisplay;
    
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)","ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)","溶剤秤量・投入(白ﾎﾟﾘ)","溶剤秤量・投入(ｽﾃﾝ容器)","ﾊﾞｲﾝﾀﾞｰ秤量・投入","ﾊﾞｲﾝﾀﾞｰ混合","ｽﾘｯﾌﾟ固形分測定","高圧分散","FP(ﾊﾞｹﾂ)","FP(成形ﾀﾝｸ)","出荷検査"};
    //画面名称 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)
    private static final String GAMEN_NAME_202B027 = "ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)";
    //画面名称 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)
    private static final String GAMEN_NAME_202B028 = "ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)";
    //画面名称 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)
    private static final String GAMEN_NAME_202B029 = "ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)";
    //画面名称 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)
    private static final String GAMEN_NAME_202B030 = "ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)";
    //画面名称 ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入
    private static final String GAMEN_NAME_202B031 = "ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入";
    //画面名称 ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合
    private static final String GAMEN_NAME_202B032 = "ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合";
    //画面名称 ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定
    private static final String GAMEN_NAME_202B033 = "ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定";
    //画面名称 ｽﾘｯﾌﾟ作製・高圧分散
    private static final String GAMEN_NAME_202B034 = "ｽﾘｯﾌﾟ作製・高圧分散";
    //画面名称 ｽﾘｯﾌﾟ作製・FP(バケツ)
    private static final String GAMEN_NAME_202B035 = "ｽﾘｯﾌﾟ作製・FP(バケツ)";
    //画面名称 ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ)
    private static final String GAMEN_NAME_202B036 = "ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ)";
    //画面名称 ｽﾘｯﾌﾟ作製・出荷検査
    private static final String GAMEN_NAME_202B037 = "ｽﾘｯﾌﾟ作製・出荷検査";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)
    private static final String JSON_FILE_PATH_202B027 = "/WEB-INF/classes/resources/json/gxhdo202b027.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)
    private static final String JSON_FILE_PATH_202B028 = "/WEB-INF/classes/resources/json/gxhdo202b028.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)
    private static final String JSON_FILE_PATH_202B029 = "/WEB-INF/classes/resources/json/gxhdo202b029.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)
    private static final String JSON_FILE_PATH_202B030 = "/WEB-INF/classes/resources/json/gxhdo202b030.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入
    private static final String JSON_FILE_PATH_202B031 = "/WEB-INF/classes/resources/json/gxhdo202b031.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合
    private static final String JSON_FILE_PATH_202B032 = "/WEB-INF/classes/resources/json/gxhdo202b032.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定
    private static final String JSON_FILE_PATH_202B033 = "/WEB-INF/classes/resources/json/gxhdo202b033.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・高圧分散
    private static final String JSON_FILE_PATH_202B034 = "/WEB-INF/classes/resources/json/gxhdo202b034.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・FP(バケツ)
    private static final String JSON_FILE_PATH_202B035 = "/WEB-INF/classes/resources/json/gxhdo202b035.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ)
    private static final String JSON_FILE_PATH_202B036 = "/WEB-INF/classes/resources/json/gxhdo202b036.json";
    //エクセル出力ファイルパス ｽﾘｯﾌﾟ作製・出荷検査
    private static final String JSON_FILE_PATH_202B037 = "/WEB-INF/classes/resources/json/gxhdo202b037.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B006() {
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ) 一覧表示データ
     * @return the b027ListData
     */
    public List<GXHDO202B027Model> getB027ListData() {
        return b027ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ) 一覧表示データ
     * @param b027ListData the b027ListData to set
     */
    public void setB027ListData(List<GXHDO202B027Model> b027ListData) {
        this.b027ListData = b027ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器) 一覧表示データ
     * @return the b028ListData
     */
    public List<GXHDO202B028Model> getB028ListData() {
        return b028ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器) 一覧表示データ
     * @param b028ListData the b028ListData to set
     */
    public void setB028ListData(List<GXHDO202B028Model> b028ListData) {
        this.b028ListData = b028ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ) 一覧表示データ
     * @return the b029ListData
     */
    public List<GXHDO202B029Model> getB029ListData() {
        return b029ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ) 一覧表示データ
     * @param b029ListData the b029ListData to set
     */
    public void setB029ListData(List<GXHDO202B029Model> b029ListData) {
        this.b029ListData = b029ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器) 一覧表示データ
     * @return the b030ListData
     */
    public List<GXHDO202B030Model> getB030ListData() {
        return b030ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器) 一覧表示データ
     * @param b030ListData the b030ListData to set
     */
    public void setB030ListData(List<GXHDO202B030Model> b030ListData) {
        this.b030ListData = b030ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入 一覧表示データ
     * @return the b031ListData
     */
    public List<GXHDO202B031Model> getB031ListData() {
        return b031ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入 一覧表示データ
     * @param b031ListData the b031ListData to set
     */
    public void setB031ListData(List<GXHDO202B031Model> b031ListData) {
        this.b031ListData = b031ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合 一覧表示データ
     * @return the b032ListData
     */
    public List<GXHDO202B032Model> getB032ListData() {
        return b032ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合 一覧表示データ
     * @param b032ListData the b032ListData to set
     */
    public void setB032ListData(List<GXHDO202B032Model> b032ListData) {
        this.b032ListData = b032ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定 一覧表示データ
     * @return the b033ListData
     */
    public List<GXHDO202B033Model> getB033ListData() {
        return b033ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定 一覧表示データ
     * @param b033ListData the b033ListData to set
     */
    public void setB033ListData(List<GXHDO202B033Model> b033ListData) {
        this.b033ListData = b033ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散 一覧表示データ
     * @return the b034ListData
     */
    public List<GXHDO202B034Model> getB034ListData() {
        return b034ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散 一覧表示データ
     * @param b034ListData the b034ListData to set
     */
    public void setB034ListData(List<GXHDO202B034Model> b034ListData) {
        this.b034ListData = b034ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ) 一覧表示データ
     * @return the b035ListData
     */
    public List<GXHDO202B035Model> getB035ListData() {
        return b035ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ) 一覧表示データ
     * @param b035ListData the b035ListData to set
     */
    public void setB035ListData(List<GXHDO202B035Model> b035ListData) {
        this.b035ListData = b035ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ) 一覧表示データ
     * @return the b036ListData
     */
    public List<GXHDO202B036Model> getB036ListData() {
        return b036ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ) 一覧表示データ
     * @param b036ListData the b036ListData to set
     */
    public void setB036ListData(List<GXHDO202B036Model> b036ListData) {
        this.b036ListData = b036ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査 一覧表示データ
     * @return the b037ListData
     */
    public List<GXHDO202B037Model> getB037ListData() {
        return b037ListData;
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査 一覧表示データ
     * @param b037ListData the b037ListData to set
     */
    public void setB037ListData(List<GXHDO202B037Model> b037ListData) {
        this.b037ListData = b037ListData;
    }

    /**
     * 検索条件：粉砕終了日(FROM)
     * @return the funsaisyuuryouDateF
     */
    public String getFunsaisyuuryouDateF() {
        return funsaisyuuryouDateF;
    }

    /**
     * 検索条件：粉砕終了日(FROM)
     * @param funsaisyuuryouDateF the funsaisyuuryouDateF to set
     */
    public void setFunsaisyuuryouDateF(String funsaisyuuryouDateF) {
        this.funsaisyuuryouDateF = funsaisyuuryouDateF;
    }

    /**
     * 検索条件：粉砕終了日(TO)
     * @return the funsaisyuuryouDateT
     */
    public String getFunsaisyuuryouDateT() {
        return funsaisyuuryouDateT;
    }

    /**
     * 検索条件：粉砕終了日(TO)
     * @param funsaisyuuryouDateT the funsaisyuuryouDateT to set
     */
    public void setFunsaisyuuryouDateT(String funsaisyuuryouDateT) {
        this.funsaisyuuryouDateT = funsaisyuuryouDateT;
    }

    /**
     * 検索条件：溶剤秤量日(FROM)
     * @return the youzaikeiryouDateF
     */
    public String getYouzaikeiryouDateF() {
        return youzaikeiryouDateF;
    }

    /**
     * 検索条件：溶剤秤量日(FROM)
     * @param youzaikeiryouDateF the youzaikeiryouDateF to set
     */
    public void setYouzaikeiryouDateF(String youzaikeiryouDateF) {
        this.youzaikeiryouDateF = youzaikeiryouDateF;
    }

    /**
     * 検索条件：溶剤秤量日(TO)
     * @return the youzaikeiryouDateT
     */
    public String getYouzaikeiryouDateT() {
        return youzaikeiryouDateT;
    }

    /**
     * 検索条件：溶剤秤量日(TO)
     * @param youzaikeiryouDateT the youzaikeiryouDateT to set
     */
    public void setYouzaikeiryouDateT(String youzaikeiryouDateT) {
        this.youzaikeiryouDateT = youzaikeiryouDateT;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(FROM)
     * @return the binderkeiryouDateF
     */
    public String getBinderkeiryouDateF() {
        return binderkeiryouDateF;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(FROM)
     * @param binderkeiryouDateF the binderkeiryouDateF to set
     */
    public void setBinderkeiryouDateF(String binderkeiryouDateF) {
        this.binderkeiryouDateF = binderkeiryouDateF;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(TO)
     * @return the binderkeiryouDateT
     */
    public String getBinderkeiryouDateT() {
        return binderkeiryouDateT;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ秤量日(TO)
     * @param binderkeiryouDateT the binderkeiryouDateT to set
     */
    public void setBinderkeiryouDateT(String binderkeiryouDateT) {
        this.binderkeiryouDateT = binderkeiryouDateT;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(FROM)
     * @return the binderkongoukaisiDateF
     */
    public String getBinderkongoukaisiDateF() {
        return binderkongoukaisiDateF;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(FROM)
     * @param binderkongoukaisiDateF the binderkongoukaisiDateF to set
     */
    public void setBinderkongoukaisiDateF(String binderkongoukaisiDateF) {
        this.binderkongoukaisiDateF = binderkongoukaisiDateF;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(TO)
     * @return the binderkongoukaisiDateT
     */
    public String getBinderkongoukaisiDateT() {
        return binderkongoukaisiDateT;
    }

    /**
     * 検索条件：ﾊﾞｲﾝﾀﾞｰ混合開始日(TO)
     * @param binderkongoukaisiDateT the binderkongoukaisiDateT to set
     */
    public void setBinderkongoukaisiDateT(String binderkongoukaisiDateT) {
        this.binderkongoukaisiDateT = binderkongoukaisiDateT;
    }

    /**
     * 検索条件：乾燥開始日(FROM)
     * @return the kansoukaishiDateF
     */
    public String getKansoukaishiDateF() {
        return kansoukaishiDateF;
    }

    /**
     * 検索条件：乾燥開始日(FROM)
     * @param kansoukaishiDateF the kansoukaishiDateF to set
     */
    public void setKansoukaishiDateF(String kansoukaishiDateF) {
        this.kansoukaishiDateF = kansoukaishiDateF;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @return the kansoukaishiDateT
     */
    public String getKansoukaishiDateT() {
        return kansoukaishiDateT;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @param kansoukaishiDateT the kansoukaishiDateT to set
     */
    public void setKansoukaishiDateT(String kansoukaishiDateT) {
        this.kansoukaishiDateT = kansoukaishiDateT;
    }

    /**
     * 検索条件：高圧分散開始日(FROM)
     * @return the kouatsubunsankaishiDateF
     */
    public String getKouatsubunsankaishiDateF() {
        return kouatsubunsankaishiDateF;
    }

    /**
     * 検索条件：高圧分散開始日(FROM)
     * @param kouatsubunsankaishiDateF the kouatsubunsankaishiDateF to set
     */
    public void setKouatsubunsankaishiDateF(String kouatsubunsankaishiDateF) {
        this.kouatsubunsankaishiDateF = kouatsubunsankaishiDateF;
    }

    /**
     * 検索条件：高圧分散開始日(TO)
     * @return the kouatsubunsankaishiDateT
     */
    public String getKouatsubunsankaishiDateT() {
        return kouatsubunsankaishiDateT;
    }

    /**
     * 検索条件：高圧分散開始日(TO)
     * @param kouatsubunsankaishiDateT the kouatsubunsankaishiDateT to set
     */
    public void setKouatsubunsankaishiDateT(String kouatsubunsankaishiDateT) {
        this.kouatsubunsankaishiDateT = kouatsubunsankaishiDateT;
    }

    /**
     * 検索条件：F/P開始日(FROM)
     * @return the fpkaishiDateF
     */
    public String getFpkaishiDateF() {
        return fpkaishiDateF;
    }

    /**
     * 検索条件：F/P開始日(FROM)
     * @param fpkaishiDateF the fpkaishiDateF to set
     */
    public void setFpkaishiDateF(String fpkaishiDateF) {
        this.fpkaishiDateF = fpkaishiDateF;
    }

    /**
     * 検索条件：F/P開始日(TO)
     * @return the fpkaishiDateT
     */
    public String getFpkaishiDateT() {
        return fpkaishiDateT;
    }

    /**
     * 検索条件：F/P開始日(TO)
     * @param fpkaishiDateT the fpkaishiDateT to set
     */
    public void setFpkaishiDateT(String fpkaishiDateT) {
        this.fpkaishiDateT = fpkaishiDateT;
    }

    /**
     * 検索条件：乾燥開始日①(FROM)
     * @return the kansoukaishi1DateF
     */
    public String getKansoukaishi1DateF() {
        return kansoukaishi1DateF;
    }

    /**
     * 検索条件：乾燥開始日①(FROM)
     * @param kansoukaishi1DateF the kansoukaishi1DateF to set
     */
    public void setKansoukaishi1DateF(String kansoukaishi1DateF) {
        this.kansoukaishi1DateF = kansoukaishi1DateF;
    }

    /**
     * 検索条件：乾燥開始日①(TO)
     * @return the kansoukaishi1DateT
     */
    public String getKansoukaishi1DateT() {
        return kansoukaishi1DateT;
    }

    /**
     * 検索条件：乾燥開始日①(TO)
     * @param kansoukaishi1DateT the kansoukaishi1DateT to set
     */
    public void setKansoukaishi1DateT(String kansoukaishi1DateT) {
        this.kansoukaishi1DateT = kansoukaishi1DateT;
    }

    /**
     * b027Listの制御
     * @return the b027DTdisplay
     */
    public String getB027DTdisplay() {
        return b027DTdisplay;
    }

    /**
     * b027Listの制御
     * @param b027DTdisplay the b027DTdisplay to set
     */
    public void setB027DTdisplay(String b027DTdisplay) {
        this.b027DTdisplay = b027DTdisplay;
    }

    /**
     * b028Listの制御
     * @return the b028DTdisplay
     */
    public String getB028DTdisplay() {
        return b028DTdisplay;
    }

    /**
     * b028Listの制御
     * @param b028DTdisplay the b028DTdisplay to set
     */
    public void setB028DTdisplay(String b028DTdisplay) {
        this.b028DTdisplay = b028DTdisplay;
    }

    /**
     * b029Listの制御
     * @return the b029DTdisplay
     */
    public String getB029DTdisplay() {
        return b029DTdisplay;
    }

    /**
     * b029Listの制御
     * @param b029DTdisplay the b029DTdisplay to set
     */
    public void setB029DTdisplay(String b029DTdisplay) {
        this.b029DTdisplay = b029DTdisplay;
    }

    /**
     * b030Listの制御
     * @return the b030DTdisplay
     */
    public String getB030DTdisplay() {
        return b030DTdisplay;
    }

    /**
     * b030Listの制御
     * @param b030DTdisplay the b030DTdisplay to set
     */
    public void setB030DTdisplay(String b030DTdisplay) {
        this.b030DTdisplay = b030DTdisplay;
    }

    /**
     * b031Listの制御
     * @return the b031DTdisplay
     */
    public String getB031DTdisplay() {
        return b031DTdisplay;
    }

    /**
     * b031Listの制御
     * @param b031DTdisplay the b031DTdisplay to set
     */
    public void setB031DTdisplay(String b031DTdisplay) {
        this.b031DTdisplay = b031DTdisplay;
    }

    /**
     * b032Listの制御
     * @return the b032DTdisplay
     */
    public String getB032DTdisplay() {
        return b032DTdisplay;
    }

    /**
     * b032Listの制御
     * @param b032DTdisplay the b032DTdisplay to set
     */
    public void setB032DTdisplay(String b032DTdisplay) {
        this.b032DTdisplay = b032DTdisplay;
    }

    /**
     * b033Listの制御
     * @return the b033DTdisplay
     */
    public String getB033DTdisplay() {
        return b033DTdisplay;
    }

    /**
     * b033Listの制御
     * @param b033DTdisplay the b033DTdisplay to set
     */
    public void setB033DTdisplay(String b033DTdisplay) {
        this.b033DTdisplay = b033DTdisplay;
    }

    /**
     * b034Listの制御
     * @return the b034DTdisplay
     */
    public String getB034DTdisplay() {
        return b034DTdisplay;
    }

    /**
     * b034Listの制御
     * @param b034DTdisplay the b034DTdisplay to set
     */
    public void setB034DTdisplay(String b034DTdisplay) {
        this.b034DTdisplay = b034DTdisplay;
    }

    /**
     * b035Listの制御
     * @return the b035DTdisplay
     */
    public String getB035DTdisplay() {
        return b035DTdisplay;
    }

    /**
     * b035Listの制御
     * @param b035DTdisplay the b035DTdisplay to set
     */
    public void setB035DTdisplay(String b035DTdisplay) {
        this.b035DTdisplay = b035DTdisplay;
    }

    /**
     * b036Listの制御
     * @return the b036DTdisplay
     */
    public String getB036DTdisplay() {
        return b036DTdisplay;
    }

    /**
     * b036Listの制御
     * @param b036DTdisplay the b036DTdisplay to set
     */
    public void setB036DTdisplay(String b036DTdisplay) {
        this.b036DTdisplay = b036DTdisplay;
    }

    /**
     * b037Listの制御
     * @return the b037DTdisplay
     */
    public String getB037DTdisplay() {
        return b037DTdisplay;
    }

    /**
     * b037Listの制御
     * @param b037DTdisplay the b037DTdisplay to set
     */
    public void setB037DTdisplay(String b037DTdisplay) {
        this.b037DTdisplay = b037DTdisplay;
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
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B006_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B006_display_page_count", session));
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
        setFunsaisyuuryouDateF("");
        setFunsaisyuuryouDateT("");
        setYouzaikeiryouDateF("");
        setYouzaikeiryouDateT("");
        setBinderkeiryouDateF("");
        setBinderkeiryouDateT("");
        setBinderkongoukaisiDateF("");
        setBinderkongoukaisiDateT("");
        setKansoukaishiDateF("");
        setKansoukaishiDateT("");
        setKouatsubunsankaishiDateF("");
        setKouatsubunsankaishiDateT("");
        setFpkaishiDateF("");
        setFpkaishiDateT("");
        setKansoukaishi1DateF("");
        setKansoukaishi1DateT("");

        setB027ListData(new ArrayList<>());
        setB028ListData(new ArrayList<>());
        setB029ListData(new ArrayList<>());
        setB030ListData(new ArrayList<>());
        setB031ListData(new ArrayList<>());
        setB032ListData(new ArrayList<>());
        setB033ListData(new ArrayList<>());
        setB034ListData(new ArrayList<>());
        setB035ListData(new ArrayList<>());
        setB036ListData(new ArrayList<>());
        setB037ListData(new ArrayList<>());

        setB027DTdisplay(DISPLAY_NONE);
        setB028DTdisplay(DISPLAY_NONE);
        setB029DTdisplay(DISPLAY_NONE);
        setB030DTdisplay(DISPLAY_NONE);
        setB031DTdisplay(DISPLAY_NONE);
        setB032DTdisplay(DISPLAY_NONE);
        setB033DTdisplay(DISPLAY_NONE);
        setB034DTdisplay(DISPLAY_NONE);
        setB035DTdisplay(DISPLAY_NONE);
        setB036DTdisplay(DISPLAY_NONE);
        setB037DTdisplay(DISPLAY_NONE);

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
        // ﾛｯﾄNo
        if(!StringUtil.isEmpty(getLotNo()) && StringUtil.getLength(getLotNo()) != 15){
         FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000004", "ﾛｯﾄNo", "15"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        // 粉砕終了日(FROM)
        if (existError(validateUtil.checkC101(getFunsaisyuuryouDateF(), "粉砕終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaisyuuryouDateF(), "粉砕終了日(from)")) ||
            existError(validateUtil.checkC501(getFunsaisyuuryouDateF(), "粉砕終了日(from)"))) {
            return;
        }
        // 粉砕終了日(TO)
        if (existError(validateUtil.checkC101(getFunsaisyuuryouDateT(), "粉砕終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaisyuuryouDateT(), "粉砕終了日(to)")) ||
            existError(validateUtil.checkC501(getFunsaisyuuryouDateT(), "粉砕終了日(to)"))) {
            return;
        }
        // 溶剤秤量日(FROM)
        if (existError(validateUtil.checkC101(getYouzaikeiryouDateF(), "溶剤秤量日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getYouzaikeiryouDateF(), "溶剤秤量日(from)")) ||
            existError(validateUtil.checkC501(getYouzaikeiryouDateF(), "溶剤秤量日(from)"))) {
            return;
        }
        // 溶剤秤量日(TO)
        if (existError(validateUtil.checkC101(getYouzaikeiryouDateT(), "溶剤秤量日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getYouzaikeiryouDateT(), "溶剤秤量日(to)")) ||
            existError(validateUtil.checkC501(getYouzaikeiryouDateT(), "溶剤秤量日(to)"))) {
            return;
        }
        // ﾊﾞｲﾝﾀﾞｰ秤量日(FROM)
        if (existError(validateUtil.checkC101(getBinderkeiryouDateF(), "ﾊﾞｲﾝﾀﾞｰ秤量日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBinderkeiryouDateF(), "ﾊﾞｲﾝﾀﾞｰ秤量日(from)")) ||
            existError(validateUtil.checkC501(getBinderkeiryouDateF(), "ﾊﾞｲﾝﾀﾞｰ秤量日(from)"))) {
            return;
        }
        // ﾊﾞｲﾝﾀﾞｰ秤量日(TO)
        if (existError(validateUtil.checkC101(getBinderkeiryouDateT(), "ﾊﾞｲﾝﾀﾞｰ秤量日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBinderkeiryouDateT(), "ﾊﾞｲﾝﾀﾞｰ秤量日(to)")) ||
            existError(validateUtil.checkC501(getBinderkeiryouDateT(), "ﾊﾞｲﾝﾀﾞｰ秤量日(to)"))) {
            return;
        }
        // ﾊﾞｲﾝﾀﾞｰ混合開始日(FROM)
        if (existError(validateUtil.checkC101(getBinderkongoukaisiDateF(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBinderkongoukaisiDateF(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(from)")) ||
            existError(validateUtil.checkC501(getBinderkongoukaisiDateF(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(from)"))) {
            return;
        }
        // ﾊﾞｲﾝﾀﾞｰ混合開始日(TO)
        if (existError(validateUtil.checkC101(getBinderkongoukaisiDateT(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBinderkongoukaisiDateT(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(to)")) ||
            existError(validateUtil.checkC501(getBinderkongoukaisiDateT(), "ﾊﾞｲﾝﾀﾞｰ混合開始日(to)"))) {
            return;
        }
        // 乾燥開始日(FROM)
        if (existError(validateUtil.checkC101(getKansoukaishiDateF(), "乾燥開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaishiDateF(), "乾燥開始日(from)")) ||
            existError(validateUtil.checkC501(getKansoukaishiDateF(), "乾燥開始日(from)"))) {
            return;
        }
        // 乾燥開始日(TO)
        if (existError(validateUtil.checkC101(getKansoukaishiDateT(), "乾燥開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaishiDateT(), "乾燥開始日(to)")) ||
            existError(validateUtil.checkC501(getKansoukaishiDateT(), "乾燥開始日(to)"))) {
            return;
        }
        // 高圧分散開始日(FROM)
        if (existError(validateUtil.checkC101(getKouatsubunsankaishiDateF(), "高圧分散開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKouatsubunsankaishiDateF(), "高圧分散開始日(from)")) ||
            existError(validateUtil.checkC501(getKouatsubunsankaishiDateF(), "高圧分散開始日(from)"))) {
            return;
        }
        // 高圧分散開始日(TO)
        if (existError(validateUtil.checkC101(getKouatsubunsankaishiDateT(), "高圧分散開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKouatsubunsankaishiDateT(), "高圧分散開始日(to)")) ||
            existError(validateUtil.checkC501(getKouatsubunsankaishiDateT(), "高圧分散開始日(to)"))) {
            return;
        }
        // F/P開始日(FROM)
        if (existError(validateUtil.checkC101(getFpkaishiDateF(), "F/P開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFpkaishiDateF(), "F/P開始日(from)")) ||
            existError(validateUtil.checkC501(getFpkaishiDateF(), "F/P開始日(from)"))) {
            return;
        }
        // F/P開始日(TO)
        if (existError(validateUtil.checkC101(getFpkaishiDateT(), "F/P開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFpkaishiDateT(), "F/P開始日(to)")) ||
            existError(validateUtil.checkC501(getFpkaishiDateT(), "F/P開始日(to)"))) {
            return;
        }
        // 乾燥開始日①(FROM)
        if (existError(validateUtil.checkC101(getKansoukaishi1DateF(), "乾燥開始日①(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaishi1DateF(), "乾燥開始日①(from)")) ||
            existError(validateUtil.checkC501(getKansoukaishi1DateF(), "乾燥開始日①(from)"))) {
            return;
        }
        // 乾燥開始日①(TO)
        if (existError(validateUtil.checkC101(getKansoukaishi1DateT(), "乾燥開始日①(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaishi1DateT(), "乾燥開始日①(to)")) ||
            existError(validateUtil.checkC501(getKansoukaishi1DateT(), "乾燥開始日①(to)"))) {
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
                case "ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)":
                    // 工程が「ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(5)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_slurrykokeibuntyousei_siropori "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) ";
                    break;
                case "ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)":
                    // 工程が「ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(7)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_slurrykokeibuntyousei_sutenyouki "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) ";
                    break;
                case "溶剤秤量・投入(白ﾎﾟﾘ)":
                    // 工程が「溶剤秤量・投入(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(9)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_youzaihyouryou_tounyuu_siropori "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji <= ?) ";
                    break;
                case "溶剤秤量・投入(ｽﾃﾝ容器)":
                    // 工程が「溶剤秤量・投入(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(11)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji <= ?) ";
                    break;
                case "ﾊﾞｲﾝﾀﾞｰ秤量・投入":
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ秤量・投入」の場合、Ⅲ.画面表示仕様(13)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_binderhyouryou_tounyuu "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR binderkeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR binderkeiryounichiji <= ?) ";
                    break;
                case "ﾊﾞｲﾝﾀﾞｰ混合":
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ混合」の場合、Ⅲ.画面表示仕様(15)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_binderkongou "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR binderkongoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR binderkongoukaisinichiji <= ?) ";
                    break;
                case "ｽﾘｯﾌﾟ固形分測定":
                    // 工程が「ｽﾘｯﾌﾟ固形分測定」の場合、Ⅲ.画面表示仕様(17)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_slipkokeibunsokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji1 >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji1 <= ?) ";
                    break;
                case "高圧分散":
                    // 工程が「高圧分散」の場合、Ⅲ.画面表示仕様(19)を発行する。
                    sql += "SELECT COUNT(SK.lotno) AS CNT "
                            + "  FROM sr_slip_kouatsubunsan SK "
                            + " INNER JOIN sub_sr_slip_kouatsubunsan SKS "
                            + "        ON (SK.kojyo = SKS.kojyo) "
                            + "       AND (SK.lotno = SKS.lotno) "
                            + "       AND (SK.edaban = SKS.edaban) "
                            + "       AND (SK.jissekino = SKS.jissekino) "
                            + " WHERE (? IS NULL OR SK.kojyo = ?) "
                            + "   AND (? IS NULL OR SK.lotno = ?) "
                            + "   AND (? IS NULL OR SK.edaban = ?) "
                            + "   AND (? IS NULL OR SK.sliphinmei = ?) " 
                            + "   AND (? IS NULL OR SKS.kouatsubunsankaishinichiji >= ?) "
                            + "   AND (? IS NULL OR SKS.kouatsubunsankaishinichiji <= ?) "
                            + "   AND (SKS.pass = 1) ";
                    break;
                case "FP(ﾊﾞｹﾂ)":
                    // 工程が「FP(ﾊﾞｹﾂ)」の場合、Ⅲ.画面表示仕様(21)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_fp_baketsu "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji >= ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji <= ?) ";
                    break;
                case "FP(成形ﾀﾝｸ)":
                    // 工程が「FP(成形ﾀﾝｸ)」の場合、Ⅲ.画面表示仕様(23)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_fp_seikeitank "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji >= ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji <= ?) ";
                    break;
                case "出荷検査":
                    // 工程が「出荷検査」の場合、Ⅲ.画面表示仕様(25)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_slip_syukkakensa "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaishijikan1 >= ?) "
                            + "   AND (? IS NULL OR kansoukaishijikan1 <= ?) ";
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
            setB027ListData(new ArrayList<>());
            setB028ListData(new ArrayList<>());
            setB029ListData(new ArrayList<>());
            setB030ListData(new ArrayList<>());
            setB031ListData(new ArrayList<>());
            setB032ListData(new ArrayList<>());
            setB033ListData(new ArrayList<>());
            setB034ListData(new ArrayList<>());
            setB035ListData(new ArrayList<>());
            setB036ListData(new ArrayList<>());
            setB037ListData(new ArrayList<>());
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB027DTdisplay(DISPLAY_NONE);
        setB028DTdisplay(DISPLAY_NONE);
        setB029DTdisplay(DISPLAY_NONE);
        setB030DTdisplay(DISPLAY_NONE);
        setB031DTdisplay(DISPLAY_NONE);
        setB032DTdisplay(DISPLAY_NONE);
        setB033DTdisplay(DISPLAY_NONE);
        setB034DTdisplay(DISPLAY_NONE);
        setB035DTdisplay(DISPLAY_NONE);
        setB036DTdisplay(DISPLAY_NONE);
        setB037DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)":{
                    // 工程が「ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(4)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", slurryhinmei"
                            + ", slurrylotno1"
                            + ", slurrylotno2"
                            + ", slurrylotno3"
                            + ", slurryyuukoukigen"
                            + ", kansoukokeibun"
                            + ", dassikokeibun"
                            + ", funsaisyuuryounichiji"
                            + ", binderkongounichij"
                            + ", slurrykeikanisuu"
                            + ", slurryjyuuryou1"
                            + ", slurryjyuuryou2"
                            + ", slurryjyuuryou3"
                            + ", slurryjyuuryou4"
                            + ", slurryjyuuryou5"
                            + ", slurryjyuuryou6"
                            + ", slurrygoukeijyuuryou"
                            + ", kokeibunhiritu"
                            + ", kokeibuntyouseiryou1"
                            + ", kokeibuntyouseiryou2"
                            + ", kokeibuntyouseiryou"
                            + ", toluenetenkaryou"
                            + ", solmixtenkaryou"
                            + ", tantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_slurrykokeibuntyousei_siropori "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) "
                            + " ORDER BY funsaisyuuryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                    // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                          // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                            // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                              // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                      // 原料記号
                    mapping.put("slurryhinmei", "slurryhinmei");                      // ｽﾗﾘｰ品名
                    mapping.put("slurrylotno1", "slurrylotno1");                      // ｽﾗﾘｰLotNo①
                    mapping.put("slurrylotno2", "slurrylotno2");                      // ｽﾗﾘｰLotNo②
                    mapping.put("slurrylotno3", "slurrylotno3");                      // ｽﾗﾘｰLotNo③
                    mapping.put("slurryyuukoukigen", "slurryyuukoukigen");            // ｽﾗﾘｰ有効期限
                    mapping.put("kansoukokeibun", "kansoukokeibun");                  // 乾燥固形分
                    mapping.put("dassikokeibun", "dassikokeibun");                    // 脱脂固形分
                    mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");    // 粉砕終了日時
                    mapping.put("binderkongounichij", "binderkongounichij");          // ﾊﾞｲﾝﾀﾞｰ混合日時
                    mapping.put("slurrykeikanisuu", "slurrykeikanisuu");              // ｽﾗﾘｰ経過日数
                    mapping.put("slurryjyuuryou1", "slurryjyuuryou1");                // ｽﾗﾘｰ重量①
                    mapping.put("slurryjyuuryou2", "slurryjyuuryou2");                // ｽﾗﾘｰ重量②
                    mapping.put("slurryjyuuryou3", "slurryjyuuryou3");                // ｽﾗﾘｰ重量③
                    mapping.put("slurryjyuuryou4", "slurryjyuuryou4");                // ｽﾗﾘｰ重量④
                    mapping.put("slurryjyuuryou5", "slurryjyuuryou5");                // ｽﾗﾘｰ重量⑤
                    mapping.put("slurryjyuuryou6", "slurryjyuuryou6");                // ｽﾗﾘｰ重量⑥
                    mapping.put("slurrygoukeijyuuryou", "slurrygoukeijyuuryou");      // ｽﾗﾘｰ合計重量
                    mapping.put("kokeibunhiritu", "kokeibunhiritu");                  // 固形分比率
                    mapping.put("kokeibuntyouseiryou1", "kokeibuntyouseiryou1");      // 固形分調整量➀
                    mapping.put("kokeibuntyouseiryou2", "kokeibuntyouseiryou2");      // 固形分調整量➁
                    mapping.put("kokeibuntyouseiryou", "kokeibuntyouseiryou");        // 固形分調整量
                    mapping.put("toluenetenkaryou", "toluenetenkaryou");              // ﾄﾙｴﾝ添加量
                    mapping.put("solmixtenkaryou", "solmixtenkaryou");                // ｿﾙﾐｯｸｽ添加量
                    mapping.put("tantousya", "tantousya");                            // 担当者
                    mapping.put("bikou1", "bikou1");                                  // 備考1
                    mapping.put("bikou2", "bikou2");                                  // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B027Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B027Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB027ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB027DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)":{
                    // 工程が「ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(6)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", goki"
                            + ", slurryhinmei"
                            + ", slurrylotno"
                            + ", youkino"
                            + ", slurryyuukoukigen"
                            + ", kansoukokeibun"
                            + ", dassikokeibun"
                            + ", funsaisyuuryounichiji"
                            + ", binderkongounichij"
                            + ", slurraging"
                            + ", slurrykeikanisuu"
                            + ", slipjyouhou_fuutaijyuuryou"
                            + ", slurrysoujyuuryou"
                            + ", slurryjyuuryou"
                            + ", kakuhansetubi"
                            + ", kakuhangoki"
                            + ", kakuhankaitensuu"
                            + ", kakuhanjikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", dassizaranosyurui"
                            + ", kokeibunsokutei_fuutaijyuuryou"
                            + ", kansoumaeslurryjyuuryou"
                            + ", kansouki"
                            + ", kansouondo"
                            + ", kansoujikan"
                            + ", kansoukaisinichij"
                            + ", kansousyuuryounichiji"
                            + ", kansougosoujyuuryou"
                            + ", kansougosyoumijyuuryou"
                            + ", kokeibunhiritu"
                            + ", kokeibunsokuteitantousya"
                            + ", youzaityouseiryou"
                            + ", toluenetyouseiryou"
                            + ", solmixtyouseiryou"
                            + ", youzaikeiryounichiji"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikolotno1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikolotno2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikolotno1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikolotno2"
                            + ", youzai2_tyougouryou2"
                            + ", tantousya"
                            + ", 2kaimekakuhansetubi AS nikaimekakuhansetubi"
                            + ", 2kaimekakuhanjikan AS nikaimekakuhanjikan"
                            + ", 2kaimekakuhankaisinichiji AS nikaimekakuhankaisinichiji"
                            + ", 2kaimekakuhansyuuryounichiji AS nikaimekakuhansyuuryounichiji"
                            + ", 2kaimedassizaranosyurui AS nikaimedassizaranosyurui"
                            + ", 2kaimearumizarafuutaijyuuryou AS nikaimearumizarafuutaijyuuryou"
                            + ", 2kaimekansoumaeslurryjyuuryou AS nikaimekansoumaeslurryjyuuryou"
                            + ", 2kaimekansouki AS nikaimekansouki"
                            + ", 2kaimekansouondo AS nikaimekansouondo"
                            + ", 2kaimekansoujikan AS nikaimekansoujikan"
                            + ", 2kaimekansoukaisinichij AS nikaimekansoukaisinichij"
                            + ", 2kaimekansousyuuryounichiji AS nikaimekansousyuuryounichiji"
                            + ", 2kaimekansougosoujyuuryou AS nikaimekansougosoujyuuryou"
                            + ", 2kaimekansougosyoumijyuuryou AS nikaimekansougosyoumijyuuryou"
                            + ", 2kaimekokeibunhiritu AS nikaimekokeibunhiritu"
                            + ", 2kaimekokeibunsokuteitantousya AS nikaimekokeibunsokuteitantousya"
                            + ", 2kaimeyouzaityouseiryou AS nikaimeyouzaityouseiryou"
                            + ", 2kaimetoluenetyouseiryou AS nikaimetoluenetyouseiryou"
                            + ", 2kaimesolmixtyouseiryou AS nikaimesolmixtyouseiryou"
                            + ", 2kaimeyouzaikeiryounichiji AS nikaimeyouzaikeiryounichiji"
                            + ", 2kaimeyouzai1_zairyouhinmei AS nikaimeyouzai1_zairyouhinmei"
                            + ", 2kaimeyouzai1_tyougouryoukikaku AS nikaimeyouzai1_tyougouryoukikaku"
                            + ", 2kaimeyouzai1_buzaizaikolotno1 AS nikaimeyouzai1_buzaizaikolotno1"
                            + ", 2kaimeyouzai1_tyougouryou1 AS nikaimeyouzai1_tyougouryou1"
                            + ", 2kaimeyouzai1_buzaizaikolotno2 AS nikaimeyouzai1_buzaizaikolotno2"
                            + ", 2kaimeyouzai1_tyougouryou2 AS nikaimeyouzai1_tyougouryou2"
                            + ", 2kaimeyouzai2_zairyouhinmei AS nikaimeyouzai2_zairyouhinmei"
                            + ", 2kaimeyouzai2_tyougouryoukikaku AS nikaimeyouzai2_tyougouryoukikaku"
                            + ", 2kaimeyouzai2_buzaizaikolotno1 AS nikaimeyouzai2_buzaizaikolotno1"
                            + ", 2kaimeyouzai2_tyougouryou1 AS nikaimeyouzai2_tyougouryou1"
                            + ", 2kaimeyouzai2_buzaizaikolotno2 AS nikaimeyouzai2_buzaizaikolotno2"
                            + ", 2kaimeyouzai2_tyougouryou2 AS nikaimeyouzai2_tyougouryou2"
                            + ", 2kaimetantousya AS nikaimetantousya"
                            + ", 3kaimekakuhansetubi AS sankaimekakuhansetubi"
                            + ", 3kaimekakuhanjikan AS sankaimekakuhanjikan"
                            + ", 3kaimekakuhankaisinichiji AS sankaimekakuhankaisinichiji"
                            + ", 3kaimekakuhansyuuryounichiji AS sankaimekakuhansyuuryounichiji"
                            + ", 3kaimedassizaranosyurui AS sankaimedassizaranosyurui"
                            + ", 3kaimearumizarafuutaijyuuryou AS sankaimearumizarafuutaijyuuryou"
                            + ", 3kaimekansoumaeslurryjyuuryou AS sankaimekansoumaeslurryjyuuryou"
                            + ", 3kaimekansouki AS sankaimekansouki"
                            + ", 3kaimekansouondo AS sankaimekansouondo"
                            + ", 3kaimekansoujikan AS sankaimekansoujikan"
                            + ", 3kaimekansoukaisinichij AS sankaimekansoukaisinichij"
                            + ", 3kaimekansousyuuryounichiji AS sankaimekansousyuuryounichiji"
                            + ", 3kaimekansougosoujyuuryou AS sankaimekansougosoujyuuryou"
                            + ", 3kaimekansougosyoumijyuuryou AS sankaimekansougosyoumijyuuryou"
                            + ", 3kaimekokeibunhiritu AS sankaimekokeibunhiritu"
                            + ", 3kaimekokeibunsokuteitantousya AS sankaimekokeibunsokuteitantousya"
                            + ", FPjyunbi_fuutaijyuuryou AS fpjyunbi_fuutaijyuuryou"
                            + ", FPjyunbi_tantousya AS fpjyunbi_tantousya"
                            + ", filterrenketu"
                            + ", FPjyunbi_filterhinmei1 AS fpjyunbi_filterhinmei1"
                            + ", FPjyunbi_lotno1 AS fpjyunbi_lotno1"
                            + ", FPjyunbi_toritukehonsuu1 AS fpjyunbi_toritukehonsuu1"
                            + ", FPjyunbi_filterhinmei2 AS fpjyunbi_filterhinmei2"
                            + ", FPjyunbi_lotno2 AS fpjyunbi_lotno2"
                            + ", FPjyunbi_toritukehonsuu2 AS fpjyunbi_toritukehonsuu2"
                            + ", assoutanknosenjyou"
                            + ", haisyutuyoukinoutibukuro"
                            + ", Fptankno AS fptankno"
                            + ", Fpkaisinichiji AS fpkaisinichiji"
                            + ", assouregulatorNo"
                            + ", assouaturyoku"
                            + ", FPkaisi_tantousya AS fpkaisi_tantousya"
                            + ", Fpteisinichiji AS fpteisinichiji"
                            + ", FPkoukan_filterhinmei1 AS fpkoukan_filterhinmei1"
                            + ", FPkoukan_lotno1 AS fpkoukan_lotno1"
                            + ", FPkoukan_toritukehonsuu1 AS fpkoukan_toritukehonsuu1"
                            + ", FPkoukan_filterhinmei2 AS fpkoukan_filterhinmei2"
                            + ", FPkoukan_lotno2 AS fpkoukan_lotno2"
                            + ", FPkoukan_toritukehonsuu2 AS fpkoukan_toritukehonsuu2"
                            + ", FPsaikainichiji AS fpsaikainichiji"
                            + ", FPkoukan_tantousya AS fpkoukan_tantousya"
                            + ", FPsyuuryounichiji AS fpsyuuryounichiji"
                            + ", FPjikan AS fpjikan"
                            + ", FPsyuurou_tantousya AS fpsyuurou_tantousya"
                            + ", soujyuuryou"
                            + ", syoumijyuuryou"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_slurrykokeibuntyousei_sutenyouki "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji >= ?) "
                            + "   AND (? IS NULL OR funsaisyuuryounichiji <= ?) "
                            + " ORDER BY funsaisyuuryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                             // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                                                   // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                                     // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                       // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                               // 原料記号
                    mapping.put("goki", "goki");                                                               // 秤量号機
                    mapping.put("slurryhinmei", "slurryhinmei");                                               // ｽﾗﾘｰ品名
                    mapping.put("slurrylotno", "slurrylotno");                                                 // ｽﾗﾘｰLotNo
                    mapping.put("youkino", "youkino");                                                         // 容器No
                    mapping.put("slurryyuukoukigen", "slurryyuukoukigen");                                     // ｽﾗﾘｰ有効期限
                    mapping.put("kansoukokeibun", "kansoukokeibun");                                           // 乾燥固形分
                    mapping.put("dassikokeibun", "dassikokeibun");                                             // 脱脂固形分
                    mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");                             // 粉砕終了日時
                    mapping.put("binderkongounichij", "binderkongounichij");                                   // ﾊﾞｲﾝﾀﾞｰ混合日時
                    mapping.put("slurraging", "slurraging");                                                   // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
                    mapping.put("slurrykeikanisuu", "slurrykeikanisuu");                                       // ｽﾗﾘｰ経過日数
                    mapping.put("slipjyouhou_fuutaijyuuryou", "slipjyouhou_fuutaijyuuryou");                   // ｽﾘｯﾌﾟ情報_風袋重量
                    mapping.put("slurrysoujyuuryou", "slurrysoujyuuryou");                                     // 総重量
                    mapping.put("slurryjyuuryou", "slurryjyuuryou");                                           // ｽﾗﾘｰ重量
                    mapping.put("kakuhansetubi", "kakuhansetubi");                                             // 撹拌設備
                    mapping.put("kakuhangoki", "kakuhangoki");                                                 // 撹拌号機
                    mapping.put("kakuhankaitensuu", "kakuhankaitensuu");                                       // 撹拌回転数
                    mapping.put("kakuhanjikan", "kakuhanjikan");                                               // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                 // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                           // 撹拌終了日時
                    mapping.put("dassizaranosyurui", "dassizaranosyurui");                                     // 脱脂皿の種類
                    mapping.put("kokeibunsokutei_fuutaijyuuryou", "kokeibunsokutei_fuutaijyuuryou");           // 固形分測定_風袋重量
                    mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                         // 乾燥前ｽﾗﾘｰ重量
                    mapping.put("kansouki", "kansouki");                                                       // 乾燥機
                    mapping.put("kansouondo", "kansouondo");                                                   // 乾燥温度
                    mapping.put("kansoujikan", "kansoujikan");                                                 // 乾燥時間
                    mapping.put("kansoukaisinichij", "kansoukaisinichij");                                     // 乾燥開始日時
                    mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                             // 乾燥終了日時
                    mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                 // 乾燥後総重量
                    mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                           // 乾燥後正味重量
                    mapping.put("kokeibunhiritu", "kokeibunhiritu");                                           // 固形分比率
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                       // 固形分測定担当者
                    mapping.put("youzaityouseiryou", "youzaityouseiryou");                                     // 溶剤調整量
                    mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                   // ﾄﾙｴﾝ調整量
                    mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                     // ｿﾙﾐｯｸｽ調整量
                    mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                               // 溶剤秤量日時
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                             // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                     // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                       // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                               // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                       // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                               // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                             // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                     // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                       // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                               // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                       // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                               // 溶剤②_調合量2
                    mapping.put("tantousya", "tantousya");                                                     // 担当者
                    mapping.put("nikaimekakuhansetubi", "nikaimekakuhansetubi");                               // 2回目撹拌設備
                    mapping.put("nikaimekakuhanjikan", "nikaimekakuhanjikan");                                 // 2回目撹拌時間
                    mapping.put("nikaimekakuhankaisinichiji", "nikaimekakuhankaisinichiji");                   // 2回目撹拌開始日時
                    mapping.put("nikaimekakuhansyuuryounichiji", "nikaimekakuhansyuuryounichiji");             // 2回目撹拌終了日時
                    mapping.put("nikaimedassizaranosyurui", "nikaimedassizaranosyurui");                       // 2回目脱脂皿の種類
                    mapping.put("nikaimearumizarafuutaijyuuryou", "nikaimearumizarafuutaijyuuryou");           // 2回目ｱﾙﾐ皿風袋重量
                    mapping.put("nikaimekansoumaeslurryjyuuryou", "nikaimekansoumaeslurryjyuuryou");           // 2回目乾燥前ｽﾗﾘｰ重量
                    mapping.put("nikaimekansouki", "nikaimekansouki");                                         // 2回目乾燥機
                    mapping.put("nikaimekansouondo", "nikaimekansouondo");                                     // 2回目乾燥温度
                    mapping.put("nikaimekansoujikan", "nikaimekansoujikan");                                   // 2回目乾燥時間
                    mapping.put("nikaimekansoukaisinichij", "nikaimekansoukaisinichij");                       // 2回目乾燥開始日時
                    mapping.put("nikaimekansousyuuryounichiji", "nikaimekansousyuuryounichiji");               // 2回目乾燥終了日時
                    mapping.put("nikaimekansougosoujyuuryou", "nikaimekansougosoujyuuryou");                   // 2回目乾燥後総重量
                    mapping.put("nikaimekansougosyoumijyuuryou", "nikaimekansougosyoumijyuuryou");             // 2回目乾燥後正味重量
                    mapping.put("nikaimekokeibunhiritu", "nikaimekokeibunhiritu");                             // 2回目固形分比率
                    mapping.put("nikaimekokeibunsokuteitantousya", "nikaimekokeibunsokuteitantousya");         // 2回目固形分測定担当者
                    mapping.put("nikaimeyouzaityouseiryou", "nikaimeyouzaityouseiryou");                       // 2回目溶剤調整量
                    mapping.put("nikaimetoluenetyouseiryou", "nikaimetoluenetyouseiryou");                     // 2回目ﾄﾙｴﾝ調整量
                    mapping.put("nikaimesolmixtyouseiryou", "nikaimesolmixtyouseiryou");                       // 2回目ｿﾙﾐｯｸｽ調整量
                    mapping.put("nikaimeyouzaikeiryounichiji", "nikaimeyouzaikeiryounichiji");                 // 2回目溶剤秤量日時
                    mapping.put("nikaimeyouzai1_zairyouhinmei", "nikaimeyouzai1_zairyouhinmei");               // 2回目溶剤①_材料品名
                    mapping.put("nikaimeyouzai1_tyougouryoukikaku", "nikaimeyouzai1_tyougouryoukikaku");       // 2回目溶剤①_調合量規格
                    mapping.put("nikaimeyouzai1_buzaizaikolotno1", "nikaimeyouzai1_buzaizaikolotno1");         // 2回目溶剤①_部材在庫No1
                    mapping.put("nikaimeyouzai1_tyougouryou1", "nikaimeyouzai1_tyougouryou1");                 // 2回目溶剤①_調合量1
                    mapping.put("nikaimeyouzai1_buzaizaikolotno2", "nikaimeyouzai1_buzaizaikolotno2");         // 2回目溶剤①_部材在庫No2
                    mapping.put("nikaimeyouzai1_tyougouryou2", "nikaimeyouzai1_tyougouryou2");                 // 2回目溶剤①_調合量2
                    mapping.put("nikaimeyouzai2_zairyouhinmei", "nikaimeyouzai2_zairyouhinmei");               // 2回目溶剤②_材料品名
                    mapping.put("nikaimeyouzai2_tyougouryoukikaku", "nikaimeyouzai2_tyougouryoukikaku");       // 2回目溶剤②_調合量規格
                    mapping.put("nikaimeyouzai2_buzaizaikolotno1", "nikaimeyouzai2_buzaizaikolotno1");         // 2回目溶剤②_部材在庫No1
                    mapping.put("nikaimeyouzai2_tyougouryou1", "nikaimeyouzai2_tyougouryou1");                 // 2回目溶剤②_調合量1
                    mapping.put("nikaimeyouzai2_buzaizaikolotno2", "nikaimeyouzai2_buzaizaikolotno2");         // 2回目溶剤②_部材在庫No2
                    mapping.put("nikaimeyouzai2_tyougouryou2", "nikaimeyouzai2_tyougouryou2");                 // 2回目溶剤②_調合量2
                    mapping.put("nikaimetantousya", "nikaimetantousya");                                       // 2回目担当者
                    mapping.put("sankaimekakuhansetubi", "sankaimekakuhansetubi");                             // 3回目撹拌設備
                    mapping.put("sankaimekakuhanjikan", "sankaimekakuhanjikan");                               // 3回目撹拌時間
                    mapping.put("sankaimekakuhankaisinichiji", "sankaimekakuhankaisinichiji");                 // 3回目撹拌開始日時
                    mapping.put("sankaimekakuhansyuuryounichiji", "sankaimekakuhansyuuryounichiji");           // 3回目撹拌終了日時
                    mapping.put("sankaimedassizaranosyurui", "sankaimedassizaranosyurui");                     // 3回目脱脂皿の種類
                    mapping.put("sankaimearumizarafuutaijyuuryou", "sankaimearumizarafuutaijyuuryou");         // 3回目ｱﾙﾐ皿風袋重量
                    mapping.put("sankaimekansoumaeslurryjyuuryou", "sankaimekansoumaeslurryjyuuryou");         // 3回目乾燥前ｽﾗﾘｰ重量
                    mapping.put("sankaimekansouki", "sankaimekansouki");                                       // 3回目乾燥機
                    mapping.put("sankaimekansouondo", "sankaimekansouondo");                                   // 3回目乾燥温度
                    mapping.put("sankaimekansoujikan", "sankaimekansoujikan");                                 // 3回目乾燥時間
                    mapping.put("sankaimekansoukaisinichij", "sankaimekansoukaisinichij");                     // 3回目乾燥開始日時
                    mapping.put("sankaimekansousyuuryounichiji", "sankaimekansousyuuryounichiji");             // 3回目乾燥終了日時
                    mapping.put("sankaimekansougosoujyuuryou", "sankaimekansougosoujyuuryou");                 // 3回目乾燥後総重量
                    mapping.put("sankaimekansougosyoumijyuuryou", "sankaimekansougosyoumijyuuryou");           // 3回目乾燥後正味重量
                    mapping.put("sankaimekokeibunhiritu", "sankaimekokeibunhiritu");                           // 3回目固形分比率
                    mapping.put("sankaimekokeibunsokuteitantousya", "sankaimekokeibunsokuteitantousya");       // 3回目固形分測定担当者
                    mapping.put("fpjyunbi_fuutaijyuuryou", "fpjyunbi_fuutaijyuuryou");                         // F/P準備_風袋重量
                    mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");                                   // F/P準備_担当者
                    mapping.put("filterrenketu", "filterrenketu");                                             // ﾌｨﾙﾀｰ連結
                    mapping.put("fpjyunbi_filterhinmei1", "fpjyunbi_filterhinmei1");                           // F/P準備_ﾌｨﾙﾀｰ品名①
                    mapping.put("fpjyunbi_lotno1", "fpjyunbi_lotno1");                                         // F/P準備_LotNo①
                    mapping.put("fpjyunbi_toritukehonsuu1", "fpjyunbi_toritukehonsuu1");                       // F/P準備_取り付け本数①
                    mapping.put("fpjyunbi_filterhinmei2", "fpjyunbi_filterhinmei2");                           // F/P準備_ﾌｨﾙﾀｰ品名②
                    mapping.put("fpjyunbi_lotno2", "fpjyunbi_lotno2");                                         // F/P準備_LotNo②
                    mapping.put("fpjyunbi_toritukehonsuu2", "fpjyunbi_toritukehonsuu2");                       // F/P準備_取り付け本数②
                    mapping.put("assoutanknosenjyou", "assoutanknosenjyou");                                   // 圧送ﾀﾝｸの洗浄
                    mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                       // 排出容器の内袋
                    mapping.put("fptankno", "fptankno");                                                       // F/PﾀﾝｸNo
                    mapping.put("fpkaisinichiji", "fpkaisinichiji");                                           // F/P開始日時
                    mapping.put("assouregulatorno", "assouregulatorno");                                       // 圧送ﾚｷﾞｭﾚｰﾀｰNo
                    mapping.put("assouaturyoku", "assouaturyoku");                                             // 圧送圧力
                    mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");                                     // F/P開始_担当者
                    mapping.put("fpteisinichiji", "fpteisinichiji");                                           // F/P停止日時
                    mapping.put("fpkoukan_filterhinmei1", "fpkoukan_filterhinmei1");                           // F/P交換_ﾌｨﾙﾀｰ品名①
                    mapping.put("fpkoukan_lotno1", "fpkoukan_lotno1");                                         // F/P交換_LotNo①
                    mapping.put("fpkoukan_toritukehonsuu1", "fpkoukan_toritukehonsuu1");                       // F/P交換_取り付け本数①
                    mapping.put("fpkoukan_filterhinmei2", "fpkoukan_filterhinmei2");                           // F/P交換_ﾌｨﾙﾀｰ品名②
                    mapping.put("fpkoukan_lotno2", "fpkoukan_lotno2");                                         // F/P交換_LotNo②
                    mapping.put("fpkoukan_toritukehonsuu2", "fpkoukan_toritukehonsuu2");                       // F/P交換_取り付け本数②
                    mapping.put("fpsaikainichiji", "fpsaikainichiji");                                         // F/P再開日時
                    mapping.put("fpkoukan_tantousya", "fpkoukan_tantousya");                                   // F/P交換_担当者
                    mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");                                     // F/P終了日時
                    mapping.put("fpjikan", "fpjikan");                                                         // F/P時間
                    mapping.put("fpsyuurou_tantousya", "fpsyuurou_tantousya");                                 // F/P終了_担当者
                    mapping.put("soujyuuryou", "soujyuuryou");                                                 // 総重量
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");                                           // 正味重量
                    mapping.put("bikou1", "bikou1");                                                           // 備考1
                    mapping.put("bikou2", "bikou2");                                                           // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B028Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B028Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB028ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB028DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "溶剤秤量・投入(白ﾎﾟﾘ)":{
                    // 工程が「溶剤秤量・投入(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(8)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", goki"
                            + ", youzaikeiryounichiji"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikolotno1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikolotno2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikolotno1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikolotno2"
                            + ", youzai2_tyougouryou2"
                            + ", youzai3_zairyouhinmei"
                            + ", youzai3_tyougouryoukikaku"
                            + ", youzai3_buzaizaikolotno1"
                            + ", youzai3_tyougouryou1"
                            + ", youzai3_buzaizaikolotno2"
                            + ", youzai3_tyougouryou2"
                            + ", youzai4_zairyouhinmei"
                            + ", youzai4_tyougouryoukikaku"
                            + ", youzai4_buzaizaikolotno1"
                            + ", youzai4_tyougouryou1"
                            + ", youzai4_buzaizaikolotno2"
                            + ", youzai4_tyougouryou2"
                            + ", tantousya"
                            + ", binderkongousetub"
                            + ", binderkongougoki"
                            + ", kongoutanksyurui"
                            + ", kongoutankno"
                            + ", tanknaisenjyoukakunin"
                            + ", tanknaiutibukurokakunin"
                            + ", kakuhanhanesenjyoukakunin"
                            + ", kakuhanjikusenjyoukakunin"
                            + ", tounyuu1"
                            + ", tounyuu2"
                            + ", tounyuu3"
                            + ", tounyuu4"
                            + ", tounyuu5"
                            + ", tounyuu6"
                            + ", slurrytounyuukakuninsya"
                            + ", tounyuu7"
                            + ", tounyuu8"
                            + ", youzaitounyuukakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_youzaihyouryou_tounyuu_siropori "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji <= ?) "
                            + " ORDER BY youzaikeiryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                             // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                                   // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                     // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                       // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                               // 原料記号
                    mapping.put("goki", "goki");                                               // 秤量号機
                    mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");               // 溶剤秤量日時
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");             // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");     // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");       // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");               // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");       // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");               // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");             // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");     // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");       // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");               // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");       // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");               // 溶剤②_調合量2
                    mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");             // 溶剤③_材料品名
                    mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");     // 溶剤③_調合量規格
                    mapping.put("youzai3_buzaizaikolotno1", "youzai3_buzaizaikolotno1");       // 溶剤③_部材在庫No1
                    mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");               // 溶剤③_調合量1
                    mapping.put("youzai3_buzaizaikolotno2", "youzai3_buzaizaikolotno2");       // 溶剤③_部材在庫No2
                    mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");               // 溶剤③_調合量2
                    mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");             // 溶剤④_材料品名
                    mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");     // 溶剤④_調合量規格
                    mapping.put("youzai4_buzaizaikolotno1", "youzai4_buzaizaikolotno1");       // 溶剤④_部材在庫No1
                    mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");               // 溶剤④_調合量1
                    mapping.put("youzai4_buzaizaikolotno2", "youzai4_buzaizaikolotno2");       // 溶剤④_部材在庫No2
                    mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");               // 溶剤④_調合量2
                    mapping.put("tantousya", "tantousya");                                     // 担当者
                    mapping.put("binderkongousetub", "binderkongousetub");                     // ﾊﾞｲﾝﾀﾞｰ混合設備
                    mapping.put("binderkongougoki", "binderkongougoki");                       // ﾊﾞｲﾝﾀﾞｰ混合号機
                    mapping.put("kongoutanksyurui", "kongoutanksyurui");                       // 混合ﾀﾝｸ種類
                    mapping.put("kongoutankno", "kongoutankno");                               // 混合ﾀﾝｸNo
                    mapping.put("tanknaisenjyoukakunin", "tanknaisenjyoukakunin");             // ﾀﾝｸ内洗浄確認
                    mapping.put("tanknaiutibukurokakunin", "tanknaiutibukurokakunin");         // ﾀﾝｸ内内袋確認
                    mapping.put("kakuhanhanesenjyoukakunin", "kakuhanhanesenjyoukakunin");     // 撹拌羽根洗浄確認
                    mapping.put("kakuhanjikusenjyoukakunin", "kakuhanjikusenjyoukakunin");     // 撹拌軸洗浄確認
                    mapping.put("tounyuu1", "tounyuu1");                                       // 投入①
                    mapping.put("tounyuu2", "tounyuu2");                                       // 投入②
                    mapping.put("tounyuu3", "tounyuu3");                                       // 投入③
                    mapping.put("tounyuu4", "tounyuu4");                                       // 投入④
                    mapping.put("tounyuu5", "tounyuu5");                                       // 投入⑤
                    mapping.put("tounyuu6", "tounyuu6");                                       // 投入⑥
                    mapping.put("slurrytounyuukakuninsya", "slurrytounyuukakuninsya");         // ｽﾗﾘｰ投入確認者
                    mapping.put("tounyuu7", "tounyuu7");                                       // 投入⑦
                    mapping.put("tounyuu8", "tounyuu8");                                       // 投入⑧
                    mapping.put("youzaitounyuukakuninsya", "youzaitounyuukakuninsya");         // 溶剤投入確認者
                    mapping.put("bikou1", "bikou1");                                           // 備考1
                    mapping.put("bikou2", "bikou2");                                           // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B029Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B029Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB029ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB029DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "溶剤秤量・投入(ｽﾃﾝ容器)":{
                    // 工程が「溶剤秤量・投入(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(10)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", goki"
                            + ", yuudentaislurryjyuuryou"
                            + ", yuudentaislurry_zairyouhinmei"
                            + ", yuudentaislurry_tyougouryoukikaku"
                            + ", yuudentaislurry_buzaizaikolotno1"
                            + ", yuudentaislurry_tyougouryou1"
                            + ", yuudentaislurry_buzaizaikolotno2"
                            + ", yuudentaislurry_tyougouryou2"
                            + ", tantousya"
                            + ", yuudentaislurrytounyuu1"
                            + ", yuudentaislurrytounyuu2"
                            + ", yuudentaislurrytounyuu3"
                            + ", yuudentaislurrytounyuu4"
                            + ", yuudentaislurrytounyuu5"
                            + ", yuudentaislurrytounyuu6"
                            + ", yuudentaislurrytounyuutantousya"
                            + ", youzaityouseiryou"
                            + ", toluenetyouseiryou"
                            + ", solmixtyouseiryou"
                            + ", youzaikeiryounichiji"
                            + ", zunsanzai1_zairyouhinmei"
                            + ", zunsanzai1_tyougouryoukikaku"
                            + ", zunsanzai1_buzaizaikolotno1"
                            + ", zunsanzai1_tyougouryou1"
                            + ", zunsanzai1_buzaizaikolotno2"
                            + ", zunsanzai1_tyougouryou2"
                            + ", zunsanzai2_zairyouhinmei"
                            + ", zunsanzai2_tyougouryoukikaku"
                            + ", zunsanzai2_buzaizaikolotno1"
                            + ", zunsanzai2_tyougouryou1"
                            + ", zunsanzai2_buzaizaikolotno2"
                            + ", zunsanzai2_tyougouryou2"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikolotno1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikolotno2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikolotno1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikolotno2"
                            + ", youzai2_tyougouryou2"
                            + ", kakuhanki"
                            + ", kakuhanjikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", binderkongousetub"
                            + ", setubisize"
                            + ", binderkongougoki"
                            + ", kongoutanksyurui"
                            + ", kongoutankno"
                            + ", tanknaisenjyoukakunin"
                            + ", tanknaiutibukurokakunin"
                            + ", kakuhanhanesenjyoukakunin"
                            + ", kakuhanjikusenjyoukakunin"
                            + ", tenryuubannotakasa"
                            + ", tankniearthgripsetuzoku"
                            + ", zunsanzaiyouzaitounyuu"
                            + ", youzaitounyuu1"
                            + ", youzaitounyuu2"
                            + ", youzaitounyuutantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR youzaikeiryounichiji <= ?) "
                            + " ORDER BY youzaikeiryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                            // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                                                  // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                                    // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                      // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                              // 原料記号
                    mapping.put("goki", "goki");                                                              // 秤量号機
                    mapping.put("yuudentaislurryjyuuryou", "yuudentaislurryjyuuryou");                        // 誘電体ｽﾗﾘｰ重量
                    mapping.put("yuudentaislurry_zairyouhinmei", "yuudentaislurry_zairyouhinmei");            // 誘電体ｽﾗﾘｰ_材料品名
                    mapping.put("yuudentaislurry_tyougouryoukikaku", "yuudentaislurry_tyougouryoukikaku");    // 誘電体ｽﾗﾘｰ_調合量規格
                    mapping.put("yuudentaislurry_buzaizaikolotno1", "yuudentaislurry_buzaizaikolotno1");      // 誘電体ｽﾗﾘｰ_部材在庫No1
                    mapping.put("yuudentaislurry_tyougouryou1", "yuudentaislurry_tyougouryou1");              // 誘電体ｽﾗﾘｰ_調合量1
                    mapping.put("yuudentaislurry_buzaizaikolotno2", "yuudentaislurry_buzaizaikolotno2");      // 誘電体ｽﾗﾘｰ_部材在庫No2
                    mapping.put("yuudentaislurry_tyougouryou2", "yuudentaislurry_tyougouryou2");              // 誘電体ｽﾗﾘｰ_調合量2
                    mapping.put("tantousya", "tantousya");                                                    // 担当者
                    mapping.put("yuudentaislurrytounyuu1", "yuudentaislurrytounyuu1");                        // 誘電体ｽﾗﾘｰ投入①
                    mapping.put("yuudentaislurrytounyuu2", "yuudentaislurrytounyuu2");                        // 誘電体ｽﾗﾘｰ投入②
                    mapping.put("yuudentaislurrytounyuu3", "yuudentaislurrytounyuu3");                        // 誘電体ｽﾗﾘｰ投入③
                    mapping.put("yuudentaislurrytounyuu4", "yuudentaislurrytounyuu4");                        // 誘電体ｽﾗﾘｰ投入④
                    mapping.put("yuudentaislurrytounyuu5", "yuudentaislurrytounyuu5");                        // 誘電体ｽﾗﾘｰ投入⑤
                    mapping.put("yuudentaislurrytounyuu6", "yuudentaislurrytounyuu6");                        // 誘電体ｽﾗﾘｰ投入⑥
                    mapping.put("yuudentaislurrytounyuutantousya", "yuudentaislurrytounyuutantousya");        // 誘電体ｽﾗﾘｰ投入担当者
                    mapping.put("youzaityouseiryou", "youzaityouseiryou");                                    // 溶剤調整量
                    mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                  // ﾄﾙｴﾝ調整量
                    mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                    // ｿﾙﾐｯｸｽ調整量
                    mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                              // 溶剤秤量日時
                    mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                      // 分散材①_材料品名
                    mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");              // 分散材①_調合量規格
                    mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                // 分散材①_部材在庫No1
                    mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                        // 分散材①_調合量1
                    mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                // 分散材①_部材在庫No2
                    mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                        // 分散材①_調合量2
                    mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                      // 分散材②_材料品名
                    mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");              // 分散材②_調合量規格
                    mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                // 分散材②_部材在庫No1
                    mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                        // 分散材②_調合量1
                    mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                // 分散材②_部材在庫No2
                    mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                        // 分散材②_調合量2
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                            // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                    // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                      // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                              // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                      // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                              // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                            // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                    // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                      // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                              // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                      // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                              // 溶剤②_調合量2
                    mapping.put("kakuhanki", "kakuhanki");                                                    // 撹拌機
                    mapping.put("kakuhanjikan", "kakuhanjikan");                                              // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                          // 撹拌終了日時
                    mapping.put("binderkongousetub", "binderkongousetub");                                    // ﾊﾞｲﾝﾀﾞｰ混合設備
                    mapping.put("setubisize", "setubisize");                                                  // 設備ｻｲｽﾞ
                    mapping.put("binderkongougoki", "binderkongougoki");                                      // ﾊﾞｲﾝﾀﾞｰ混合号機
                    mapping.put("kongoutanksyurui", "kongoutanksyurui");                                      // 混合ﾀﾝｸ種類
                    mapping.put("kongoutankno", "kongoutankno");                                              // 混合ﾀﾝｸNo
                    mapping.put("tanknaisenjyoukakunin", "tanknaisenjyoukakunin");                            // ﾀﾝｸ内洗浄確認
                    mapping.put("tanknaiutibukurokakunin", "tanknaiutibukurokakunin");                        // ﾀﾝｸ内内袋確認
                    mapping.put("kakuhanhanesenjyoukakunin", "kakuhanhanesenjyoukakunin");                    // 撹拌羽根洗浄確認
                    mapping.put("kakuhanjikusenjyoukakunin", "kakuhanjikusenjyoukakunin");                    // 撹拌軸洗浄確認
                    mapping.put("tenryuubannotakasa", "tenryuubannotakasa");                                  // 転流板の高さ
                    mapping.put("tankniearthgripsetuzoku", "tankniearthgripsetuzoku");                        // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
                    mapping.put("zunsanzaiyouzaitounyuu", "zunsanzaiyouzaitounyuu");                          // 分散材溶剤投入
                    mapping.put("youzaitounyuu1", "youzaitounyuu1");                                          // 溶剤投入①
                    mapping.put("youzaitounyuu2", "youzaitounyuu2");                                          // 溶剤投入②
                    mapping.put("youzaitounyuutantousya", "youzaitounyuutantousya");                          // 溶剤投入担当者
                    mapping.put("bikou1", "bikou1");                                                          // 備考1
                    mapping.put("bikou2", "bikou2");                                                          // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B030Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B030Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB030ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB030DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾊﾞｲﾝﾀﾞｰ秤量・投入":{
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ秤量・投入」の場合、Ⅲ.画面表示仕様(12)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", binderkongousetub"
                            + ", binderkongougoki"
                            + ", kongoutanksyurui"
                            + ", kongoutankno"
                            + ", binderkeiryounichiji"
                            + ", binderhinmei"
                            + ", binderlotno"
                            + ", binderkokeibun"
                            + ", binderyuukoukigen"
                            + ", bindertenkaryoukikaku"
                            + ", hoppersiyou"
                            + ", tounyuukaisinichiji"
                            + ", tounyuujikaitensuu"
                            + ", hyouryou1"
                            + ", tounyuukakunin1"
                            + ", hyouryou2"
                            + ", tounyuukakunin2"
                            + ", hyouryou3"
                            + ", tounyuukakunin3"
                            + ", hyouryou4"
                            + ", tounyuukakunin4"
                            + ", hyouryou5"
                            + ", tounyuukakunin5"
                            + ", hyouryou6"
                            + ", tounyuukakunin6"
                            + ", tounyuusyuuryounichiji"
                            + ", bindertenkaryougoukei"
                            + ", tantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_binderhyouryou_tounyuu "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR binderkeiryounichiji >= ?) "
                            + "   AND (? IS NULL OR binderkeiryounichiji <= ?) "
                            + " ORDER BY binderkeiryounichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                    // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                          // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                            // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                              // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                      // 原料記号
                    mapping.put("binderkongousetub", "binderkongousetub");            // ﾊﾞｲﾝﾀﾞｰ混合設備
                    mapping.put("binderkongougoki", "binderkongougoki");              // ﾊﾞｲﾝﾀﾞｰ混合号機
                    mapping.put("kongoutanksyurui", "kongoutanksyurui");              // 混合ﾀﾝｸ種類
                    mapping.put("kongoutankno", "kongoutankno");                      // 混合ﾀﾝｸNo
                    mapping.put("binderkeiryounichiji", "binderkeiryounichiji");      // ﾊﾞｲﾝﾀﾞｰ秤量日時
                    mapping.put("binderhinmei", "binderhinmei");                      // ﾊﾞｲﾝﾀﾞｰ品名
                    mapping.put("binderlotno", "binderlotno");                        // ﾊﾞｲﾝﾀﾞｰLotNo
                    mapping.put("binderkokeibun", "binderkokeibun");                  // ﾊﾞｲﾝﾀﾞｰ固形分
                    mapping.put("binderyuukoukigen", "binderyuukoukigen");            // ﾊﾞｲﾝﾀﾞｰ有効期限
                    mapping.put("bindertenkaryoukikaku", "bindertenkaryoukikaku");    // ﾊﾞｲﾝﾀﾞｰ添加量規格
                    mapping.put("hoppersiyou", "hoppersiyou");                        // ﾎｯﾊﾟｰ使用
                    mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");        // 投入開始日時
                    mapping.put("tounyuujikaitensuu", "tounyuujikaitensuu");          // 投入時回転数
                    mapping.put("hyouryou1", "hyouryou1");                            // 秤量①
                    mapping.put("tounyuukakunin1", "tounyuukakunin1");                // 投入確認①
                    mapping.put("hyouryou2", "hyouryou2");                            // 秤量②
                    mapping.put("tounyuukakunin2", "tounyuukakunin2");                // 投入確認②
                    mapping.put("hyouryou3", "hyouryou3");                            // 秤量③
                    mapping.put("tounyuukakunin3", "tounyuukakunin3");                // 投入確認③
                    mapping.put("hyouryou4", "hyouryou4");                            // 秤量④
                    mapping.put("tounyuukakunin4", "tounyuukakunin4");                // 投入確認④
                    mapping.put("hyouryou5", "hyouryou5");                            // 秤量⑤
                    mapping.put("tounyuukakunin5", "tounyuukakunin5");                // 投入確認⑤
                    mapping.put("hyouryou6", "hyouryou6");                            // 秤量⑥
                    mapping.put("tounyuukakunin6", "tounyuukakunin6");                // 投入確認⑥
                    mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");  // 投入終了日時
                    mapping.put("bindertenkaryougoukei", "bindertenkaryougoukei");    // ﾊﾞｲﾝﾀﾞｰ添加量合計
                    mapping.put("tantousya", "tantousya");                            // 担当者
                    mapping.put("bikou1", "bikou1");                                  // 備考1
                    mapping.put("bikou2", "bikou2");                                  // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B031Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B031Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB031ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB031DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾊﾞｲﾝﾀﾞｰ混合":{
                    // 工程が「ﾊﾞｲﾝﾀﾞｰ混合」の場合、Ⅲ.画面表示仕様(14)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", kakuhanmode"
                            + ", reikyakusuivalvekai"
                            + ", kaitenhoukou"
                            + ", kaitensuu"
                            + ", binderkongoujikan"
                            + ", binderkongoukaisinichiji"
                            + ", kaisidenryuuti"
                            + ", ondo_ou"
                            + ", ondo_kan"
                            + ", aturyoku_ou"
                            + ", aturyoku_kan"
                            + ", binderkongousyuuryouyoteinichiji"
                            + ", 1lotatarislurryjyuuryou AS ichilotatarislurryjyuuryou"
                            + ", slipyoteijyuuryou"
                            + ", binderkongoukaisitantousya"
                            + ", slipkokeibunsokutei"
                            + ", binderkongousyuuryounichiji"
                            + ", syuuryoudenryuuti"
                            + ", binderkongousyuuryoutantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_binderkongou "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR binderkongoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR binderkongoukaisinichiji <= ?) "
                            + " ORDER BY binderkongoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                       // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                                             // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                               // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                 // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                         // 原料記号
                    mapping.put("kakuhanmode", "kakuhanmode");                                           // 撹拌ﾓｰﾄﾞ
                    mapping.put("reikyakusuivalvekai", "reikyakusuivalvekai");                           // 冷却水ﾊﾞﾙﾌﾞ開
                    mapping.put("kaitenhoukou", "kaitenhoukou");                                         // 回転方向
                    mapping.put("kaitensuu", "kaitensuu");                                               // 回転数(rpm)
                    mapping.put("binderkongoujikan", "binderkongoujikan");                               // ﾊﾞｲﾝﾀﾞｰ混合時間
                    mapping.put("binderkongoukaisinichiji", "binderkongoukaisinichiji");                 // ﾊﾞｲﾝﾀﾞｰ混合開始日時
                    mapping.put("kaisidenryuuti", "kaisidenryuuti");                                     // 開始電流値(A)
                    mapping.put("ondo_ou", "ondo_ou");                                                   // 温度(往)
                    mapping.put("ondo_kan", "ondo_kan");                                                 // 温度(還)
                    mapping.put("aturyoku_ou", "aturyoku_ou");                                           // 圧力(往)
                    mapping.put("aturyoku_kan", "aturyoku_kan");                                         // 圧力(還)
                    mapping.put("binderkongousyuuryouyoteinichiji", "binderkongousyuuryouyoteinichiji"); // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
                    mapping.put("ichilotatarislurryjyuuryou", "ichilotatarislurryjyuuryou");             // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
                    mapping.put("slipyoteijyuuryou", "slipyoteijyuuryou");                               // ｽﾘｯﾌﾟ予定重量
                    mapping.put("binderkongoukaisitantousya", "binderkongoukaisitantousya");             // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
                    mapping.put("slipkokeibunsokutei", "slipkokeibunsokutei");                           // ｽﾘｯﾌﾟ固形分測定
                    mapping.put("binderkongousyuuryounichiji", "binderkongousyuuryounichiji");           // ﾊﾞｲﾝﾀﾞｰ混合終了日時
                    mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                               // 終了電流値(A)
                    mapping.put("binderkongousyuuryoutantousya", "binderkongousyuuryoutantousya");       // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
                    mapping.put("bikou1", "bikou1");                                                     // 備考1
                    mapping.put("bikou2", "bikou2");                                                     // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B032Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B032Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB032ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB032DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ｽﾘｯﾌﾟ固形分測定":{
                    // 工程が「ｽﾘｯﾌﾟ固形分測定」の場合、Ⅲ.画面表示仕様(16)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", goki"
                            + ", dassizaranosyurui"
                            + ", rutubono"
                            + ", rutubofuutaijyuuryou"
                            + ", kansoumaeslipjyuuryou"
                            + ", kansouki1"
                            + ", kansouondo1"
                            + ", kansoujikan1"
                            + ", kansoukaisinichiji1"
                            + ", kansousyuuryounichiji1"
                            + ", kansouki2"
                            + ", kansouondo2"
                            + ", kansoujikan2"
                            + ", kansoukaisinichiji2"
                            + ", kansousyuuryounichiji2"
                            + ", kansougosoujyuuryou"
                            + ", kansougosyoumijyuuryou"
                            + ", kokeibunhiritu"
                            + ", kokeibunsokuteitantousya"
                            + ", youzaityouseiryou"
                            + ", toluenetyouseiryou"
                            + ", solmixtyouseiryou"
                            + ", youzaikeiryounichiji"
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikolotno1"
                            + ", youzai1_tyougouryou1"
                            + ", youzai1_buzaizaikolotno2"
                            + ", youzai1_tyougouryou2"
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikolotno1"
                            + ", youzai2_tyougouryou1"
                            + ", youzai2_buzaizaikolotno2"
                            + ", youzai2_tyougouryou2"
                            + ", tantousya"
                            + ", kakuhan_kakuhanmode"
                            + ", kakuhan_kaitenhoukou"
                            + ", kakuhan_kaitensuu"
                            + ", kakuhan_kakuhanjikan"
                            + ", kakuhan_kakuhankaisinichiji"
                            + ", kaisidenryuuti"
                            + ", kakuhan_kakuhansyuuryouyoteinichiji"
                            + ", kakuhan_kakuhansyuuryounichiji"
                            + ", syuuryoudenryuuti"
                            + ", kakuhan_kakuhantantousya"
                            + ", haisyutumaekakuhan_kakuhanmode"
                            + ", haisyutumaekakuhan_kaitenhoukou"
                            + ", haisyutumaekakuhan_kaitensuu"
                            + ", haisyutumaekakuhan_kakuhanjikan"
                            + ", haisyutumaekakuhan_kakuhankaisinichiji"
                            + ", haisyutumaekakuhan_kakuhansyuuryouyoteinichiji"
                            + ", haisyutumaekakuhan_kakuhansyuuryounichiji"
                            + ", haisyutumaekakuhan_kakuhantantousya"
                            + ", haisyutukaisinichiji"
                            + ", haisyutusyuuryounichiji"
                            + ", haisyututantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_slipkokeibunsokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji1 >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichiji1 <= ?) "
                            + " ORDER BY kansoukaisinichiji1 ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                                                     // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                                                                           // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                                                             // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                                               // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                                                       // 原料記号
                    mapping.put("goki", "goki");                                                                                       // 秤量号機
                    mapping.put("dassizaranosyurui", "dassizaranosyurui");                                                             // 脱脂皿の種類
                    mapping.put("rutubono", "rutubono");                                                                               // ﾙﾂﾎﾞNo
                    mapping.put("rutubofuutaijyuuryou", "rutubofuutaijyuuryou");                                                       // ﾙﾂﾎﾞ風袋重量
                    mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");                                                     // 乾燥前ｽﾘｯﾌﾟ重量
                    mapping.put("kansouki1", "kansouki1");                                                                             // 乾燥機①
                    mapping.put("kansouondo1", "kansouondo1");                                                                         // 乾燥温度①
                    mapping.put("kansoujikan1", "kansoujikan1");                                                                       // 乾燥時間①
                    mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");                                                         // 乾燥開始日時①
                    mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");                                                   // 乾燥終了日時①
                    mapping.put("kansouki2", "kansouki2");                                                                             // 乾燥機②
                    mapping.put("kansouondo2", "kansouondo2");                                                                         // 乾燥温度②
                    mapping.put("kansoujikan2", "kansoujikan2");                                                                       // 乾燥時間②
                    mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");                                                         // 乾燥開始日時②
                    mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");                                                   // 乾燥終了日時②
                    mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                                         // 乾燥後総重量
                    mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                                                   // 乾燥後正味重量
                    mapping.put("kokeibunhiritu", "kokeibunhiritu");                                                                   // 固形分比率
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                                               // 固形分測定担当者
                    mapping.put("youzaityouseiryou", "youzaityouseiryou");                                                             // 溶剤調整量
                    mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                                           // ﾄﾙｴﾝ調整量
                    mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                                             // ｿﾙﾐｯｸｽ調整量
                    mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                                       // 溶剤秤量日時
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                                     // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                                             // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                                               // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                                       // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                                               // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                                       // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                                     // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                                             // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                                               // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                                       // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                                               // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                                       // 溶剤②_調合量2
                    mapping.put("tantousya", "tantousya");                                                                             // 担当者
                    mapping.put("kakuhan_kakuhanmode", "kakuhan_kakuhanmode");                                                         // 撹拌_撹拌ﾓｰﾄﾞ
                    mapping.put("kakuhan_kaitenhoukou", "kakuhan_kaitenhoukou");                                                       // 撹拌_回転方向
                    mapping.put("kakuhan_kaitensuu", "kakuhan_kaitensuu");                                                             // 撹拌_回転数(rpm)
                    mapping.put("kakuhan_kakuhanjikan", "kakuhan_kakuhanjikan");                                                       // 撹拌_撹拌時間
                    mapping.put("kakuhan_kakuhankaisinichiji", "kakuhan_kakuhankaisinichiji");                                         // 撹拌_撹拌開始日時
                    mapping.put("kaisidenryuuti", "kaisidenryuuti");                                                                   // 開始電流値(A)
                    mapping.put("kakuhan_kakuhansyuuryouyoteinichiji", "kakuhan_kakuhansyuuryouyoteinichiji");                         // 撹拌_撹拌終了予定日時
                    mapping.put("kakuhan_kakuhansyuuryounichiji", "kakuhan_kakuhansyuuryounichiji");                                   // 撹拌_撹拌終了日時
                    mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                                                             // 終了電流値(A)
                    mapping.put("kakuhan_kakuhantantousya", "kakuhan_kakuhantantousya");                                               // 撹拌_撹拌担当者
                    mapping.put("haisyutumaekakuhan_kakuhanmode", "haisyutumaekakuhan_kakuhanmode");                                   // 排出前撹拌_撹拌ﾓｰﾄﾞ
                    mapping.put("haisyutumaekakuhan_kaitenhoukou", "haisyutumaekakuhan_kaitenhoukou");                                 // 排出前撹拌_回転方向
                    mapping.put("haisyutumaekakuhan_kaitensuu", "haisyutumaekakuhan_kaitensuu");                                       // 排出前撹拌_回転数(rpm)
                    mapping.put("haisyutumaekakuhan_kakuhanjikan", "haisyutumaekakuhan_kakuhanjikan");                                 // 排出前撹拌_撹拌時間
                    mapping.put("haisyutumaekakuhan_kakuhankaisinichiji", "haisyutumaekakuhan_kakuhankaisinichiji");                   // 排出前撹拌_撹拌開始日時
                    mapping.put("haisyutumaekakuhan_kakuhansyuuryouyoteinichiji", "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji");   // 排出前撹拌_撹拌終了予定日時
                    mapping.put("haisyutumaekakuhan_kakuhansyuuryounichiji", "haisyutumaekakuhan_kakuhansyuuryounichiji");             // 排出前撹拌_撹拌終了日時
                    mapping.put("haisyutumaekakuhan_kakuhantantousya", "haisyutumaekakuhan_kakuhantantousya");                         // 排出前撹拌_撹拌担当者
                    mapping.put("haisyutukaisinichiji", "haisyutukaisinichiji");                                                       // 排出開始日時
                    mapping.put("haisyutusyuuryounichiji", "haisyutusyuuryounichiji");                                                 // 排出終了日時
                    mapping.put("haisyututantousya", "haisyututantousya");                                                             // 排出担当者
                    mapping.put("bikou1", "bikou1");                                                                                   // 備考1
                    mapping.put("bikou2", "bikou2");                                                                                   // 備考2

                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B033Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B033Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB033ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB033DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "高圧分散":{
                    // 工程が「高圧分散」の場合、Ⅲ.画面表示仕様(18)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(SK.kojyo , SK.lotno , SK.edaban) AS lotno "
                            + ", SK.sliphinmei"
                            + ", SK.sliplotno"
                            + ", SK.lotkubun"
                            + ", SK.genryoukigou"
                            + ", SK.kouatsubunsanki"
                            + ", SK.kouatsubunsangouki"
                            + ", SK.haisyutsuyoukiuchibukuro"
                            + ", SK.haisyutsuyoukisenjyou"
                            + ", SK.setsubinaisenjyou"
                            + ", SK.nozzlekei"
                            + ", SK.reikyakusuivalve"
                            + ", SK.reikyakusuiondokikaku"
                            + ", SK.reikyakusuiondo"
                            + ", SK.kouatsubunsankaisuu"
                            + ", SK.tankearthgripsetsuzoku"
                            + ", SK.bikou1"
                            + ", SK.bikou2 "
                            + "  FROM sr_slip_kouatsubunsan SK "
                            + " INNER JOIN sub_sr_slip_kouatsubunsan SKS "
                            + "        ON (SK.kojyo = SKS.kojyo) "
                            + "       AND (SK.lotno = SKS.lotno) "
                            + "       AND (SK.edaban = SKS.edaban) "
                            + "       AND (SK.jissekino = SKS.jissekino) "
                            + " WHERE (? IS NULL OR SK.kojyo = ?) "
                            + "   AND (? IS NULL OR SK.lotno = ?) "
                            + "   AND (? IS NULL OR SK.edaban = ?) "
                            + "   AND (? IS NULL OR SK.sliphinmei = ?) " 
                            + "   AND (? IS NULL OR SKS.kouatsubunsankaishinichiji >= ?) "
                            + "   AND (? IS NULL OR SKS.kouatsubunsankaishinichiji <= ?) "
                            + "   AND (SKS.pass = 1) "
                            + " ORDER BY SKS.kouatsubunsankaishinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
                    mapping.put("sliphinmei", "sliphinmei");                               // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                 // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                           // 原料記号
                    mapping.put("kouatsubunsanki", "kouatsubunsanki");                     // 高圧分散機
                    mapping.put("kouatsubunsangouki", "kouatsubunsangouki");               // 高圧分散号機
                    mapping.put("haisyutsuyoukiuchibukuro", "haisyutsuyoukiuchibukuro");   // 排出容器内袋確認
                    mapping.put("haisyutsuyoukisenjyou", "haisyutsuyoukisenjyou");         // 排出容器洗浄確認
                    mapping.put("setsubinaisenjyou", "setsubinaisenjyou");                 // 設備内洗浄確認
                    mapping.put("nozzlekei", "nozzlekei");                                 // ﾉｽﾞﾙ径
                    mapping.put("reikyakusuivalve", "reikyakusuivalve");                   // 冷却水ﾊﾞﾙﾌﾞ開
                    mapping.put("reikyakusuiondokikaku", "reikyakusuiondokikaku");         // 冷却水温度規格
                    mapping.put("reikyakusuiondo", "reikyakusuiondo");                     // 冷却水温度
                    mapping.put("kouatsubunsankaisuu", "kouatsubunsankaisuu");             // 高圧分散回数
                    mapping.put("tankearthgripsetsuzoku", "tankearthgripsetsuzoku");       // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
                    mapping.put("bikou1", "bikou1");                                       // 備考1
                    mapping.put("bikou2", "bikou2");                                       // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B034Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B034Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB034ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB034DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "FP(ﾊﾞｹﾂ)":{
                    // 工程が「FP(ﾊﾞｹﾂ)」の場合、Ⅲ.画面表示仕様(20)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", jissekino"
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", haisyutsuyouki"
                            + ", hutaijyuuryou1"
                            + ", hutaijyuuryou2"
                            + ", hutaijyuuryou3"
                            + ", hutaijyuuryou4"
                            + ", hutaijyuuryou5"
                            + ", hutaijyuuryou6"
                            + ", hokanyoukijyunbitantousya"
                            + ", filterrenketsu"
                            + ", filtertoritsuke1filterhinmei"
                            + ", filtertoritsukefilterlotno1"
                            + ", filtertoritsuketoritsukehonsuu1"
                            + ", filtertoritsuke2filterhinmei"
                            + ", filtertoritsukefilterlotno2"
                            + ", filtertoritsuketoritsukehonsuu2"
                            + ", filtertoritsuketantousya"
                            + ", assoutankno"
                            + ", fpkaishinichiji"
                            + ", assouregulatorno"
                            + ", assouatsuryokukikaku"
                            + ", assouatsuryoku"
                            + ", filterpasskaishitantousya"
                            + ", hozonyousamplekaisyuu"
                            + ", filterkoukan1fpteishinichiji"
                            + ", filterkoukan11filterhinmei"
                            + ", filterkoukan1lotno1"
                            + ", filterkoukan1toritsukehonnsuu1"
                            + ", filterkoukan12filterhinmei"
                            + ", filterkoukan1lotno2"
                            + ", filterkoukan1toritsukehonnsuu2"
                            + ", filterkoukan13filterhinmei"
                            + ", filterkoukan1lotno3"
                            + ", filterkoukan1toritsukehonnsuu3"
                            + ", filterkoukan1fpsaikainichiji"
                            + ", filterkoukan1tantousya"
                            + ", fpsyuryounichiji"
                            + ", fpzikan"
                            + ", filterpasssyuuryoutantousya"
                            + ", soujyuryou1"
                            + ", soujyuryou2"
                            + ", soujyuryou3"
                            + ", soujyuryou4"
                            + ", soujyuryou5"
                            + ", soujyuryou6"
                            + ", syoumijyuuryou1"
                            + ", syoumijyuuryou2"
                            + ", syoumijyuuryou3"
                            + ", syoumijyuuryou4"
                            + ", syoumijyuuryou5"
                            + ", syoumijyuuryou6"
                            + ", slipjyuuryougoukei"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_fp_baketsu "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji >= ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji <= ?) "
                            + " ORDER BY fpkaishinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                          // ﾛｯﾄNo
                    mapping.put("jissekino", "jissekino");                                                  // 実績No
                    mapping.put("sliphinmei", "sliphinmei");                                                // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                                  // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                    // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                            // 原料記号
                    mapping.put("haisyutsuyouki", "haisyutsuyouki");                                        // 排出容器の内袋
                    mapping.put("hutaijyuuryou1", "hutaijyuuryou1");                                        // 風袋重量①
                    mapping.put("hutaijyuuryou2", "hutaijyuuryou2");                                        // 風袋重量②
                    mapping.put("hutaijyuuryou3", "hutaijyuuryou3");                                        // 風袋重量③
                    mapping.put("hutaijyuuryou4", "hutaijyuuryou4");                                        // 風袋重量④
                    mapping.put("hutaijyuuryou5", "hutaijyuuryou5");                                        // 風袋重量⑤
                    mapping.put("hutaijyuuryou6", "hutaijyuuryou6");                                        // 風袋重量⑥
                    mapping.put("hokanyoukijyunbitantousya", "hokanyoukijyunbitantousya");                  // 保管容器準備_担当者
                    mapping.put("filterrenketsu", "filterrenketsu");                                        // ﾌｨﾙﾀｰ連結
                    mapping.put("filtertoritsuke1filterhinmei", "filtertoritsuke1filterhinmei");            // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertoritsukefilterlotno1", "filtertoritsukefilterlotno1");              // ﾌｨﾙﾀｰ取り付け_LotNo1
                    mapping.put("filtertoritsuketoritsukehonsuu1", "filtertoritsuketoritsukehonsuu1");      // ﾌｨﾙﾀｰ取り付け_取り付け本数1
                    mapping.put("filtertoritsuke2filterhinmei", "filtertoritsuke2filterhinmei");            // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertoritsukefilterlotno2", "filtertoritsukefilterlotno2");              // ﾌｨﾙﾀｰ取り付け_LotNo2
                    mapping.put("filtertoritsuketoritsukehonsuu2", "filtertoritsuketoritsukehonsuu2");      // ﾌｨﾙﾀｰ取り付け_取り付け本数2
                    mapping.put("filtertoritsuketantousya", "filtertoritsuketantousya");                    // ﾌｨﾙﾀｰ取り付け_担当者
                    mapping.put("assoutankno", "assoutankno");                                              // 圧送ﾀﾝｸNo
                    mapping.put("fpkaishinichiji", "fpkaishinichiji");                                      // F/P開始日時
                    mapping.put("assouregulatorno", "assouregulatorno");                                    // 圧送ﾚｷﾞｭﾚｰﾀｰNo
                    mapping.put("assouatsuryokukikaku", "assouatsuryokukikaku");                            // 圧送圧力規格
                    mapping.put("assouatsuryoku", "assouatsuryoku");                                        // 圧送圧力
                    mapping.put("filterpasskaishitantousya", "filterpasskaishitantousya");                  // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
                    mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                          // 保存用ｻﾝﾌﾟﾙ回収
                    mapping.put("filterkoukan1fpteishinichiji", "filterkoukan1fpteishinichiji");            // ﾌｨﾙﾀｰ交換①_F/P停止日時
                    mapping.put("filterkoukan11filterhinmei", "filterkoukan11filterhinmei");                // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno1", "filterkoukan1lotno1");                              // ﾌｨﾙﾀｰ交換①_LotNo1
                    mapping.put("filterkoukan1toritsukehonnsuu1", "filterkoukan1toritsukehonnsuu1");        // ﾌｨﾙﾀｰ交換①_取り付け本数1
                    mapping.put("filterkoukan12filterhinmei", "filterkoukan12filterhinmei");                // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno2", "filterkoukan1lotno2");                              // ﾌｨﾙﾀｰ交換①_LotNo2
                    mapping.put("filterkoukan1toritsukehonnsuu2", "filterkoukan1toritsukehonnsuu2");        // ﾌｨﾙﾀｰ交換①_取り付け本数2
                    mapping.put("filterkoukan13filterhinmei", "filterkoukan13filterhinmei");                // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno3", "filterkoukan1lotno3");                              // ﾌｨﾙﾀｰ交換①_LotNo3
                    mapping.put("filterkoukan1toritsukehonnsuu3", "filterkoukan1toritsukehonnsuu3");        // ﾌｨﾙﾀｰ交換①_取り付け本数3
                    mapping.put("filterkoukan1fpsaikainichiji", "filterkoukan1fpsaikainichiji");            // ﾌｨﾙﾀｰ交換①_F/P再開日時
                    mapping.put("filterkoukan1tantousya", "filterkoukan1tantousya");                        // ﾌｨﾙﾀｰ交換①_担当者
                    mapping.put("fpsyuryounichiji", "fpsyuryounichiji");                                    // F/P終了日時
                    mapping.put("fpzikan", "fpzikan");                                                      // F/P時間
                    mapping.put("filterpasssyuuryoutantousya", "filterpasssyuuryoutantousya");              // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
                    mapping.put("soujyuryou1", "soujyuryou1");                                              // 総重量1
                    mapping.put("soujyuryou2", "soujyuryou2");                                              // 総重量2
                    mapping.put("soujyuryou3", "soujyuryou3");                                              // 総重量3
                    mapping.put("soujyuryou4", "soujyuryou4");                                              // 総重量4
                    mapping.put("soujyuryou5", "soujyuryou5");                                              // 総重量5
                    mapping.put("soujyuryou6", "soujyuryou6");                                              // 総重量6
                    mapping.put("syoumijyuuryou1", "syoumijyuuryou1");                                      // 正味重量1
                    mapping.put("syoumijyuuryou2", "syoumijyuuryou2");                                      // 正味重量2
                    mapping.put("syoumijyuuryou3", "syoumijyuuryou3");                                      // 正味重量3
                    mapping.put("syoumijyuuryou4", "syoumijyuuryou4");                                      // 正味重量4
                    mapping.put("syoumijyuuryou5", "syoumijyuuryou5");                                      // 正味重量5
                    mapping.put("syoumijyuuryou6", "syoumijyuuryou6");                                      // 正味重量6
                    mapping.put("slipjyuuryougoukei", "slipjyuuryougoukei");                                // ｽﾘｯﾌﾟ重量合計
                    mapping.put("bikou1", "bikou1");                                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B035Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B035Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB035ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB035DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "FP(成形ﾀﾝｸ)":{
                    // 工程が「FP(成形ﾀﾝｸ)」の場合、Ⅲ.画面表示仕様(22)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", jissekino"
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", hutaijyuuryou"
                            + ", seikeitankno"
                            + ", seikeitankkabuvalve"
                            + ", hokanyoukijyunbitantousya"
                            + ", filterrenketsu"
                            + ", filtertoritsuke1filterhinmei"
                            + ", filtertoritsukefilterlotno1"
                            + ", filtertoritsuketoritsukehonsuu1"
                            + ", filtertoritsuke2filterhinmei"
                            + ", filtertoritsukefilterlotno2"
                            + ", filtertoritsuketoritsukehonsuu2"
                            + ", filtertoritsuketantousya"
                            + ", assoutankno"
                            + ", fpkaishinichiji"
                            + ", assouregulatorno"
                            + ", assouatsuryokukikaku"
                            + ", assouatsuryoku"
                            + ", filterpasskaishitantousya"
                            + ", hozonyousamplekaisyuu"
                            + ", filterkoukan1fpteishinichiji"
                            + ", filterkoukan11filterhinmei"
                            + ", filterkoukan1lotno1"
                            + ", filterkoukan1toritsukehonnsuu1"
                            + ", filterkoukan12filterhinmei"
                            + ", filterkoukan1lotno2"
                            + ", filterkoukan1toritsukehonnsuu2"
                            + ", filterkoukan13filterhinmei"
                            + ", filterkoukan1lotno3"
                            + ", filterkoukan1toritsukehonnsuu3"
                            + ", filterkoukan1fpsaikainichiji"
                            + ", filterkoukan1tantousya"
                            + ", fpsyuryounichiji"
                            + ", fpzikan"
                            + ", filterpasssyuuryoutantousya"
                            + ", soujyuryou"
                            + ", syoumijyuuryou"
                            + ", slipjyuuryougoukei"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_fp_seikeitank "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji >= ?) "
                            + "   AND (? IS NULL OR fpkaishinichiji <= ?) "
                            + " ORDER BY fpkaishinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                         // ﾛｯﾄNo
                    mapping.put("jissekino", "jissekino");                                                 // 実績No
                    mapping.put("sliphinmei", "sliphinmei");                                               // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                                 // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                                   // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                                           // 原料記号
                    mapping.put("hutaijyuuryou", "hutaijyuuryou");                                         // 風袋重量
                    mapping.put("seikeitankno", "seikeitankno");                                           // 成形ﾀﾝｸNo
                    mapping.put("seikeitankkabuvalve", "seikeitankkabuvalve");                             // 成形ﾀﾝｸ下部ﾊﾞﾙﾌﾞ閉
                    mapping.put("hokanyoukijyunbitantousya", "hokanyoukijyunbitantousya");                 // 保管容器準備_担当者
                    mapping.put("filterrenketsu", "filterrenketsu");                                       // ﾌｨﾙﾀｰ連結
                    mapping.put("filtertoritsuke1filterhinmei", "filtertoritsuke1filterhinmei");           // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertoritsukefilterlotno1", "filtertoritsukefilterlotno1");             // ﾌｨﾙﾀｰ取り付け_LotNo1
                    mapping.put("filtertoritsuketoritsukehonsuu1", "filtertoritsuketoritsukehonsuu1");     // ﾌｨﾙﾀｰ取り付け_取り付け本数1
                    mapping.put("filtertoritsuke2filterhinmei", "filtertoritsuke2filterhinmei");           // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertoritsukefilterlotno2", "filtertoritsukefilterlotno2");             // ﾌｨﾙﾀｰ取り付け_LotNo2
                    mapping.put("filtertoritsuketoritsukehonsuu2", "filtertoritsuketoritsukehonsuu2");     // ﾌｨﾙﾀｰ取り付け_取り付け本数2
                    mapping.put("filtertoritsuketantousya", "filtertoritsuketantousya");                   // ﾌｨﾙﾀｰ取り付け_担当者
                    mapping.put("assoutankno", "assoutankno");                                             // 圧送ﾀﾝｸNo
                    mapping.put("fpkaishinichiji", "fpkaishinichiji");                                     // F/P開始日時
                    mapping.put("assouregulatorno", "assouregulatorno");                                   // 圧送ﾚｷﾞｭﾚｰﾀｰNo
                    mapping.put("assouatsuryokukikaku", "assouatsuryokukikaku");                           // 圧送圧力規格
                    mapping.put("assouatsuryoku", "assouatsuryoku");                                       // 圧送圧力
                    mapping.put("filterpasskaishitantousya", "filterpasskaishitantousya");                 // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
                    mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                         // 保存用ｻﾝﾌﾟﾙ回収
                    mapping.put("filterkoukan1fpteishinichiji", "filterkoukan1fpteishinichiji");           // ﾌｨﾙﾀｰ交換①_F/P停止日時
                    mapping.put("filterkoukan11filterhinmei", "filterkoukan11filterhinmei");               // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno1", "filterkoukan1lotno1");                             // ﾌｨﾙﾀｰ交換①_LotNo1
                    mapping.put("filterkoukan1toritsukehonnsuu1", "filterkoukan1toritsukehonnsuu1");       // ﾌｨﾙﾀｰ交換①_取り付け本数1
                    mapping.put("filterkoukan12filterhinmei", "filterkoukan12filterhinmei");               // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno2", "filterkoukan1lotno2");                             // ﾌｨﾙﾀｰ交換①_LotNo2
                    mapping.put("filterkoukan1toritsukehonnsuu2", "filterkoukan1toritsukehonnsuu2");       // ﾌｨﾙﾀｰ交換①_取り付け本数2
                    mapping.put("filterkoukan13filterhinmei", "filterkoukan13filterhinmei");               // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1lotno3", "filterkoukan1lotno3");                             // ﾌｨﾙﾀｰ交換①_LotNo3
                    mapping.put("filterkoukan1toritsukehonnsuu3", "filterkoukan1toritsukehonnsuu3");       // ﾌｨﾙﾀｰ交換①_取り付け本数3
                    mapping.put("filterkoukan1fpsaikainichiji", "filterkoukan1fpsaikainichiji");           // ﾌｨﾙﾀｰ交換①_F/P再開日時
                    mapping.put("filterkoukan1tantousya", "filterkoukan1tantousya");                       // ﾌｨﾙﾀｰ交換①_担当者
                    mapping.put("fpsyuryounichiji", "fpsyuryounichiji");                                   // F/P終了日時
                    mapping.put("fpzikan", "fpzikan");                                                     // F/P時間
                    mapping.put("filterpasssyuuryoutantousya", "filterpasssyuuryoutantousya");             // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
                    mapping.put("soujyuryou", "soujyuryou");                                               // 総重量
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");                                       // 正味重量
                    mapping.put("slipjyuuryougoukei", "slipjyuuryougoukei");                               // ｽﾘｯﾌﾟ重量合計
                    mapping.put("bikou1", "bikou1");                                                       // 備考1
                    mapping.put("bikou2", "bikou2");                                                       // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B036Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B036Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB036ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB036DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "出荷検査":{
                    // 工程が「出荷検査」の場合、Ⅲ.画面表示仕様(24)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", jissekino"
                            + ", sliphinmei"
                            + ", sliplotno"
                            + ", lotkubun"
                            + ", genryoukigou"
                            + ", dasshisara"
                            + ", rutsubono"
                            + ", rutsubohuutaijyuuryou"
                            + ", kansoumaeslipjyuuryoukikaku"
                            + ", kansoumaeslipjyuuryou"
                            + ", kannsouki1"
                            + ", kannsouondo1"
                            + ", kansoujikan1"
                            + ", kansoukaishijikan1"
                            + ", kansousyuuryoujikan1"
                            + ", kannsouki2"
                            + ", kannsouondo2"
                            + ", kansoujikan2"
                            + ", kansoukaishijikan2"
                            + ", kansousyuuryoujikan2"
                            + ", kansougosoujyuuryou"
                            + ", kansougosyoumijyuuryou"
                            + ", kokeibunkikaku"
                            + ", kokeibunhiritsu"
                            + ", kokeibunsokuteitantousya"
                            + ", sokuteiki"
                            + ", sokuteigouki"
                            + ", spindlesyurui"
                            + ", kaitensuu"
                            + ", sokuteinichiji"
                            + ", nendosokuteikikaku"
                            + ", nendosokuteikekka"
                            + ", ondosokuteikikaku"
                            + ", ondosokuteikekka"
                            + ", nendosokuteitantousya"
                            + ", syuuritsu"
                            + ", slipyuukoukigen"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_slip_syukkakensa "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR sliphinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaishijikan1 >= ?) "
                            + "   AND (? IS NULL OR kansoukaishijikan1 <= ?) "
                            + " ORDER BY kansoukaishijikan1 ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                        // ﾛｯﾄNo
                    mapping.put("jissekino", "jissekino");                                // 実績No
                    mapping.put("sliphinmei", "sliphinmei");                              // ｽﾘｯﾌﾟ品名
                    mapping.put("sliplotno", "sliplotno");                                // ｽﾘｯﾌﾟLotNo
                    mapping.put("lotkubun", "lotkubun");                                  // ﾛｯﾄ区分
                    mapping.put("genryoukigou", "genryoukigou");                          // 原料記号
                    mapping.put("dasshisara", "dasshisara");                              // 脱脂皿の種類
                    mapping.put("rutsubono", "rutsubono");                                // ﾙﾂﾎﾞNo
                    mapping.put("rutsubohuutaijyuuryou", "rutsubohuutaijyuuryou");        // ﾙﾂﾎﾞ風袋重量
                    mapping.put("kansoumaeslipjyuuryoukikaku", "kansoumaeslipjyuuryoukikaku");  // 乾燥前ｽﾘｯﾌﾟ重量規格
                    mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");        // 乾燥前ｽﾘｯﾌﾟ重量
                    mapping.put("kannsouki1", "kannsouki1");                              // 乾燥機①
                    mapping.put("kannsouondo1", "kannsouondo1");                          // 乾燥温度①
                    mapping.put("kansoujikan1", "kansoujikan1");                          // 乾燥時間①
                    mapping.put("kansoukaishijikan1", "kansoukaishijikan1");              // 乾燥開始日時①
                    mapping.put("kansousyuuryoujikan1", "kansousyuuryoujikan1");          // 乾燥終了日時①
                    mapping.put("kannsouki2", "kannsouki2");                              // 乾燥機②
                    mapping.put("kannsouondo2", "kannsouondo2");                          // 乾燥温度②
                    mapping.put("kansoujikan2", "kansoujikan2");                          // 乾燥時間②
                    mapping.put("kansoukaishijikan2", "kansoukaishijikan2");              // 乾燥開始日時②
                    mapping.put("kansousyuuryoujikan2", "kansousyuuryoujikan2");          // 乾燥終了日時②
                    mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");            // 乾燥後総重量
                    mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");      // 乾燥後正味重量
                    mapping.put("kokeibunkikaku", "kokeibunkikaku");                      // 固形分規格
                    mapping.put("kokeibunhiritsu", "kokeibunhiritsu");                    // 固形分比率
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");  // 固形分測定担当者
                    mapping.put("sokuteiki", "sokuteiki");                                // 測定器
                    mapping.put("sokuteigouki", "sokuteigouki");                          // 測定号機
                    mapping.put("spindlesyurui", "spindlesyurui");                        // ｽﾋﾟﾝﾄﾞﾙの種類
                    mapping.put("kaitensuu", "kaitensuu");                                // 回転数
                    mapping.put("sokuteinichiji", "sokuteinichiji");                      // 測定日時
                    mapping.put("nendosokuteikikaku", "nendosokuteikikaku");              // 粘度測定規格
                    mapping.put("nendosokuteikekka", "nendosokuteikekka");                // 粘度測定結果
                    mapping.put("ondosokuteikikaku", "ondosokuteikikaku");                // 温度測定規格
                    mapping.put("ondosokuteikekka", "ondosokuteikekka");                  // 温度測定結果
                    mapping.put("nendosokuteitantousya", "nendosokuteitantousya");        // 粘度測定担当者
                    mapping.put("syuuritsu", "syuuritsu");                                // 収率(%)
                    mapping.put("slipyuukoukigen", "slipyuukoukigen");                    // ｽﾘｯﾌﾟ有効期限
                    mapping.put("bikou1", "bikou1");                                      // 備考1
                    mapping.put("bikou2", "bikou2");                                      // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B037Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B037Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB037ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB037DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }

        } catch (SQLException ex) {
            setB027ListData(new ArrayList<>());
            setB028ListData(new ArrayList<>());
            setB029ListData(new ArrayList<>());
            setB030ListData(new ArrayList<>());
            setB031ListData(new ArrayList<>());
            setB032ListData(new ArrayList<>());
            setB033ListData(new ArrayList<>());
            setB034ListData(new ArrayList<>());
            setB035ListData(new ArrayList<>());
            setB036ListData(new ArrayList<>());
            setB037ListData(new ArrayList<>());
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
        if(DISPLAY_BLOCK.equals(getB027DTdisplay())) {
            //工程が「ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)」の場合
            excelRealPath = JSON_FILE_PATH_202B027;
            excelFileHeadName = GAMEN_NAME_202B027;
            outputList = getB027ListData();
        }else if(DISPLAY_BLOCK.equals(getB028DTdisplay())) {
            //工程が「ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)」の場合
            excelRealPath = JSON_FILE_PATH_202B028;
            excelFileHeadName = GAMEN_NAME_202B028;
            outputList = getB028ListData();
        }else if(DISPLAY_BLOCK.equals(getB029DTdisplay())) {
            //工程が「溶剤秤量・投入(白ﾎﾟﾘ)」の場合
            excelRealPath = JSON_FILE_PATH_202B029;
            excelFileHeadName = GAMEN_NAME_202B029;
            outputList = getB029ListData();
        }else if(DISPLAY_BLOCK.equals(getB030DTdisplay())) {
            //工程が「溶剤秤量・投入(ｽﾃﾝ容器)」の場合
            excelRealPath = JSON_FILE_PATH_202B030;
            excelFileHeadName = GAMEN_NAME_202B030;
            outputList = getB030ListData();
        }else if(DISPLAY_BLOCK.equals(getB031DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ秤量・投入」の場合
            excelRealPath = JSON_FILE_PATH_202B031;
            excelFileHeadName = GAMEN_NAME_202B031;
            outputList = getB031ListData();
        }else if(DISPLAY_BLOCK.equals(getB032DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ混合」の場合
            excelRealPath = JSON_FILE_PATH_202B032;
            excelFileHeadName = GAMEN_NAME_202B032;
            outputList = getB032ListData();
        }else if(DISPLAY_BLOCK.equals(getB033DTdisplay())) {
            //工程が「ｽﾘｯﾌﾟ固形分測定」の場合
            excelRealPath = JSON_FILE_PATH_202B033;
            excelFileHeadName = GAMEN_NAME_202B033;
            outputList = getB033ListData();
        }else if(DISPLAY_BLOCK.equals(getB034DTdisplay())) {
            //工程が「高圧分散」の場合
            excelRealPath = JSON_FILE_PATH_202B034;
            excelFileHeadName = GAMEN_NAME_202B034;
            outputList = getB034ListData();
        }else if(DISPLAY_BLOCK.equals(getB035DTdisplay())) {
            //工程が「FP(ﾊﾞｹﾂ)」の場合
            excelRealPath = JSON_FILE_PATH_202B035;
            excelFileHeadName = GAMEN_NAME_202B035;
            outputList = getB035ListData();
        }else if(DISPLAY_BLOCK.equals(getB036DTdisplay())) {
            //工程が「FP(成形ﾀﾝｸ)」の場合
            excelRealPath = JSON_FILE_PATH_202B036;
            excelFileHeadName = GAMEN_NAME_202B036;
            outputList = getB036ListData();
        }else if(DISPLAY_BLOCK.equals(getB037DTdisplay())) {
            //工程が「出荷検査」の場合
            excelRealPath = JSON_FILE_PATH_202B037;
            excelFileHeadName = GAMEN_NAME_202B037;
            outputList = getB037ListData();
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
           
        //粉砕終了日(FROM)
        Date paramFunsaisyuuryouDateF = null;
        if (!StringUtil.isEmpty(funsaisyuuryouDateF)) {
            paramFunsaisyuuryouDateF = DateUtil.convertStringToDateInSeconds(getFunsaisyuuryouDateF(), "000000");
        }
        
        //粉砕終了日(TO)
        Date paramFunsaisyuuryouDateT = null;
        if (!StringUtil.isEmpty(funsaisyuuryouDateT)) {
            paramFunsaisyuuryouDateT = DateUtil.convertStringToDateInSeconds(getFunsaisyuuryouDateT(), "235959");
        }
        
        //溶剤秤量日(FROM)
        Date paramYouzaikeiryouDateF = null;
        if (!StringUtil.isEmpty(youzaikeiryouDateF)) {
            paramYouzaikeiryouDateF = DateUtil.convertStringToDateInSeconds(getYouzaikeiryouDateF(), "000000");
        }
        
        //溶剤秤量日(TO)
        Date paramYouzaikeiryouDateT = null;
        if (!StringUtil.isEmpty(youzaikeiryouDateT)) {
            paramYouzaikeiryouDateT = DateUtil.convertStringToDateInSeconds(getYouzaikeiryouDateT(), "235959");
        }
        
        //ﾊﾞｲﾝﾀﾞｰ秤量日(FROM)
        Date paramBinderkeiryouDateF = null;
        if (!StringUtil.isEmpty(binderkeiryouDateF)) {
            paramBinderkeiryouDateF = DateUtil.convertStringToDateInSeconds(getBinderkeiryouDateF(), "000000");
        }
        
        //ﾊﾞｲﾝﾀﾞｰ秤量日(TO)
        Date paramBinderkeiryouDateT = null;
        if (!StringUtil.isEmpty(binderkeiryouDateT)) {
            paramBinderkeiryouDateT = DateUtil.convertStringToDateInSeconds(getBinderkeiryouDateT(), "235959");
        }
        
        //ﾊﾞｲﾝﾀﾞｰ混合開始日(FROM)
        Date paramBinderkongoukaisiDateF = null;
        if (!StringUtil.isEmpty(binderkongoukaisiDateF)) {
            paramBinderkongoukaisiDateF = DateUtil.convertStringToDateInSeconds(getBinderkongoukaisiDateF(), "000000");
        }
        
        //ﾊﾞｲﾝﾀﾞｰ混合開始日(TO)
        Date paramBinderkongoukaisiDateT = null;
        if (!StringUtil.isEmpty(binderkongoukaisiDateT)) {
            paramBinderkongoukaisiDateT = DateUtil.convertStringToDateInSeconds(getBinderkongoukaisiDateT(), "235959");
        }

        //乾燥開始日(FROM)
        Date paramKansoukaishiDateF = null;
        if (!StringUtil.isEmpty(kansoukaishiDateF)) {
            paramKansoukaishiDateF = DateUtil.convertStringToDateInSeconds(getKansoukaishiDateF(), "000000");
        }

        //乾燥開始日(TO)
        Date paramKansoukaishiDateT = null;
        if (!StringUtil.isEmpty(kansoukaishiDateT)) {
            paramKansoukaishiDateT = DateUtil.convertStringToDateInSeconds(getKansoukaishiDateT(), "235959");
        }
        
        //高圧分散開始日(FROM)
        Date paramKouatsubunsankaishiDateF = null;
        if (!StringUtil.isEmpty(kouatsubunsankaishiDateF)) {
            paramKouatsubunsankaishiDateF = DateUtil.convertStringToDateInSeconds(getKouatsubunsankaishiDateF(), "000000");
        }
        
        //高圧分散開始日(TO)
        Date paramKouatsubunsankaishiDateT = null;
        if (!StringUtil.isEmpty(kouatsubunsankaishiDateT)) {
            paramKouatsubunsankaishiDateT = DateUtil.convertStringToDateInSeconds(getKouatsubunsankaishiDateT(), "235959");
        }
        
        //F/P開始日(FROM)
        Date paramFpkaishiDateF = null;
        if (!StringUtil.isEmpty(fpkaishiDateF)) {
            paramFpkaishiDateF = DateUtil.convertStringToDateInSeconds(getFpkaishiDateF(), "000000");
        }
        
        //F/P開始日(TO)
        Date paramFpkaishiDateT = null;
        if (!StringUtil.isEmpty(fpkaishiDateT)) {
            paramFpkaishiDateT = DateUtil.convertStringToDateInSeconds(getFpkaishiDateT(), "235959");
        }

        //乾燥開始日①(FROM)
        Date paramKansoukaishi1DateF = null;
        if (!StringUtil.isEmpty(kansoukaishi1DateF)) {
            paramKansoukaishi1DateF = DateUtil.convertStringToDateInSeconds(getKansoukaishi1DateF(), "000000");
        }

        //乾燥開始日①(TO)
        Date paramKansoukaishi1DateT = null;
        if (!StringUtil.isEmpty(kansoukaishi1DateT)) {
            paramKansoukaishi1DateT = DateUtil.convertStringToDateInSeconds(getKansoukaishi1DateT(), "235959");
        }
        
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));
        
        if(null != cmbKotei)switch (cmbKotei) {
            case "ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)":
                // 工程が「ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(5)を発行する。
                params.addAll(Arrays.asList(paramFunsaisyuuryouDateF, paramFunsaisyuuryouDateF));
                params.addAll(Arrays.asList(paramFunsaisyuuryouDateT, paramFunsaisyuuryouDateT));
                break;
            case "ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)":
                // 工程が「ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(7)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramFunsaisyuuryouDateF, paramFunsaisyuuryouDateF));
                params.addAll(Arrays.asList(paramFunsaisyuuryouDateT, paramFunsaisyuuryouDateT));
                break;
            case "溶剤秤量・投入(白ﾎﾟﾘ)":
                // 工程が「溶剤秤量・投入(白ﾎﾟﾘ)」の場合、Ⅲ.画面表示仕様(9)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramYouzaikeiryouDateF, paramYouzaikeiryouDateF));
                params.addAll(Arrays.asList(paramYouzaikeiryouDateT, paramYouzaikeiryouDateT));
                break;
            case "溶剤秤量・投入(ｽﾃﾝ容器)":
                // 工程が「溶剤秤量・投入(ｽﾃﾝ容器)」の場合、Ⅲ.画面表示仕様(11)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramYouzaikeiryouDateF, paramYouzaikeiryouDateF));
                params.addAll(Arrays.asList(paramYouzaikeiryouDateT, paramYouzaikeiryouDateT));
                break;
            case "ﾊﾞｲﾝﾀﾞｰ秤量・投入":
                // 工程が「ﾊﾞｲﾝﾀﾞｰ秤量・投入」の場合、Ⅲ.画面表示仕様(13)を発行する。
                params.addAll(Arrays.asList(paramBinderkeiryouDateF, paramBinderkeiryouDateF));
                params.addAll(Arrays.asList(paramBinderkeiryouDateT, paramBinderkeiryouDateT));
                break;
            case "ﾊﾞｲﾝﾀﾞｰ混合":
                // 工程が「ﾊﾞｲﾝﾀﾞｰ混合」の場合、Ⅲ.画面表示仕様(15)を発行する。
                params.addAll(Arrays.asList(paramBinderkongoukaisiDateF, paramBinderkongoukaisiDateF));
                params.addAll(Arrays.asList(paramBinderkongoukaisiDateT, paramBinderkongoukaisiDateT));
                break;
            case "ｽﾘｯﾌﾟ固形分測定":
                // 工程が「ｽﾘｯﾌﾟ固形分測定」の場合、Ⅲ.画面表示仕様(17)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramKansoukaishiDateF, paramKansoukaishiDateF));
                params.addAll(Arrays.asList(paramKansoukaishiDateT, paramKansoukaishiDateT));
                break;
            case "高圧分散":
                // 工程が「高圧分散」の場合、Ⅲ.画面表示仕様(19)を発行する。
                params.addAll(Arrays.asList(paramKouatsubunsankaishiDateF, paramKouatsubunsankaishiDateF));
                params.addAll(Arrays.asList(paramKouatsubunsankaishiDateT, paramKouatsubunsankaishiDateT));
                break;
            case "FP(ﾊﾞｹﾂ)":
                // 工程が「FP(ﾊﾞｹﾂ)」の場合、Ⅲ.画面表示仕様(21)を発行する。
                params.addAll(Arrays.asList(paramFpkaishiDateF, paramFpkaishiDateF));
                params.addAll(Arrays.asList(paramFpkaishiDateT, paramFpkaishiDateT));
                break;
            case "FP(成形ﾀﾝｸ)":
                // 工程が「FP(成形ﾀﾝｸ)」の場合、Ⅲ.画面表示仕様(23)を発行する。
                params.addAll(Arrays.asList(paramFpkaishiDateF, paramFpkaishiDateF));
                params.addAll(Arrays.asList(paramFpkaishiDateT, paramFpkaishiDateT));
                break;
            case "出荷検査":
                // 工程が「出荷検査」の場合、Ⅲ.画面表示仕様(25)を発行する。
                params.addAll(Arrays.asList(paramKansoukaishi1DateF, paramKansoukaishi1DateF));
                params.addAll(Arrays.asList(paramKansoukaishi1DateT, paramKansoukaishi1DateT));
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
        if(DISPLAY_BLOCK.equals(getB027DTdisplay())) {
            //工程が「ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)」の場合
            return !(b027ListData == null || b027ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB028DTdisplay())) {
            //工程が「ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)」の場合
            return !(b028ListData == null || b028ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB029DTdisplay())) {
            //工程が「溶剤秤量・投入(白ﾎﾟﾘ)」の場合
            return !(b029ListData == null || b029ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB030DTdisplay())) {
            //工程が「溶剤秤量・投入(ｽﾃﾝ容器)」の場合
            return !(b030ListData == null || b030ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB031DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ秤量・投入」の場合
            return !(b031ListData == null || b031ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB032DTdisplay())) {
            //工程が「ﾊﾞｲﾝﾀﾞｰ混合」の場合
            return !(b032ListData == null || b032ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB033DTdisplay())) {
            //工程が「ｽﾘｯﾌﾟ固形分測定」の場合
            return !(b033ListData == null || b033ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB034DTdisplay())) {
            //工程が「高圧分散」の場合
            return !(b034ListData == null || b034ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB035DTdisplay())) {
            //工程が「FP(ﾊﾞｹﾂ)」の場合
            return !(b035ListData == null || b035ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB036DTdisplay())) {
            //工程が「FP(成形ﾀﾝｸ)」の場合
            return !(b036ListData == null || b036ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB037DTdisplay())) {
            //工程が「出荷検査」の場合
            return !(b037ListData == null || b037ListData.isEmpty());
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