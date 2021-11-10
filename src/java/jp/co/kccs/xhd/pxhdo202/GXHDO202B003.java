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
import jp.co.kccs.xhd.model.GXHDO202B007Model;
import jp.co.kccs.xhd.model.GXHDO202B008Model;
import jp.co.kccs.xhd.model.GXHDO202B009Model;
import jp.co.kccs.xhd.model.GXHDO202B010Model;
import jp.co.kccs.xhd.model.GXHDO202B011Model;
import jp.co.kccs.xhd.model.GXHDO202B012Model;
import jp.co.kccs.xhd.model.GXHDO202B013Model;
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
 * 変更日       2021/10/15<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 添加材ｽﾗﾘｰ作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
@Named
@ViewScoped
public class GXHDO202B003 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B003.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ 一覧表示データ */
    private List<GXHDO202B007Model> b007ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・添加材調合 一覧表示データ */
    private List<GXHDO202B008Model> b008ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・溶剤調合 一覧表示データ */
    private List<GXHDO202B009Model> b009ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・予備混合 一覧表示データ */
    private List<GXHDO202B010Model> b010ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・粉砕 一覧表示データ */
    private List<GXHDO202B011Model> b011ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・FP排出 一覧表示データ */
    private List<GXHDO202B012Model> b012ListData = null;
    
    /** 添加材ｽﾗﾘｰ作製・BET 一覧表示データ */
    private List<GXHDO202B013Model> b013ListData = null;
    
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
    /** 検索条件：固形分測定日(FROM) */
    private String kokeibunsokuteiDateF = "";
    /** 検索条件：固形分測定日(TO) */
    private String kokeibunsokuteiDateT = "";
    /** 検索条件：秤量開始日(FROM) */
    private String hyoryoDateF = "";
    /** 検索条件：秤量開始日(TO) */
    private String hyoryoDateT = "";
    /** 検索条件：撹拌開始日(FROM) */
    private String kakuhankaisiDateF = "";
    /** 検索条件：撹拌開始日(TO) */
    private String kakuhankaisiDateT = "";
    /** 検索条件：循環開始日(FROM) */
    private String jyunkankaisiDateF = "";
    /** 検索条件：循環開始日(TO) */
    private String jyunkankaisiDateT = "";
    /** 検索条件：F/P開始日(FROM) */
    private String fpkaisiDateF = "";
    /** 検索条件：F/P開始日(TO) */
    private String fpkaisiDateT = "";
    /** 検索条件：比表面積測定開始日(FROM) */
    private String hihyoumensekisokuteikaisiDateF = "";
    /** 検索条件：比表面積測定開始日(TO) */
    private String hihyoumensekisokuteikaisiDateT = "";
    /** b007Listの制御 */
    private String b007DTdisplay;
    /** b008Listの制御 */
    private String b008DTdisplay;
    /** b009Listの制御 */
    private String b009DTdisplay;
    /** b010Listの制御 */
    private String b010DTdisplay;
    /** b011Listの制御 */
    private String b011DTdisplay;
    /** b012Listの制御 */
    private String b012DTdisplay;
    /** b013Listの制御 */
    private String b013DTdisplay;
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"ｶﾞﾗｽ", "添加材調合", "溶剤調合", "予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)", "粉砕", "FP排出", "BET"};
    //画面名称 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ
    private static final String GAMEN_NAME_202B007 = "添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ";
    //画面名称 添加材ｽﾗﾘｰ作製・添加材調合
    private static final String GAMEN_NAME_202B008 = "添加材ｽﾗﾘｰ作製・添加材調合";
    //画面名称 添加材ｽﾗﾘｰ作製・溶剤調合
    private static final String GAMEN_NAME_202B009 = "添加材ｽﾗﾘｰ作製・溶剤調合";
    //画面名称 添加材ｽﾗﾘｰ作製・予備混合
    private static final String GAMEN_NAME_202B010 = "添加材ｽﾗﾘｰ作製・予備混合";
    //画面名称 添加材ｽﾗﾘｰ作製・粉砕
    private static final String GAMEN_NAME_202B011 = "添加材ｽﾗﾘｰ作製・粉砕";
    //画面名称 添加材ｽﾗﾘｰ作製・FP排出
    private static final String GAMEN_NAME_202B012 = "添加材ｽﾗﾘｰ作製・FP排出";
    //画面名称 添加材ｽﾗﾘｰ作製・BET
    private static final String GAMEN_NAME_202B013 = "添加材ｽﾗﾘｰ作製・BET";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ
    private static final String JSON_FILE_PATH_202B007 = "/WEB-INF/classes/resources/json/gxhdo202b007.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・添加材調合
    private static final String JSON_FILE_PATH_202B008 = "/WEB-INF/classes/resources/json/gxhdo202b008.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・溶剤調合
    private static final String JSON_FILE_PATH_202B009 = "/WEB-INF/classes/resources/json/gxhdo202b009.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・予備混合
    private static final String JSON_FILE_PATH_202B010 = "/WEB-INF/classes/resources/json/gxhdo202b010.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・粉砕
    private static final String JSON_FILE_PATH_202B011 = "/WEB-INF/classes/resources/json/gxhdo202b011.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・FP排出
    private static final String JSON_FILE_PATH_202B012 = "/WEB-INF/classes/resources/json/gxhdo202b012.json";
    //エクセル出力ファイルパス 添加材ｽﾗﾘｰ作製・BET
    private static final String JSON_FILE_PATH_202B013 = "/WEB-INF/classes/resources/json/gxhdo202b013.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B003() {
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
     * 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ 一覧表示データ
     */
    public List<GXHDO202B007Model> getB007ListData() {
        return b007ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・添加材調合 一覧表示データ
     */
    public List<GXHDO202B008Model> getB008ListData() {
        return b008ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・溶剤調合 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・溶剤調合 一覧表示データ
     */
    public List<GXHDO202B009Model> getB009ListData() {
        return b009ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・予備混合 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・予備混合 一覧表示データ
     */
    public List<GXHDO202B010Model> getB010ListData() {
        return b010ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・粉砕 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・粉砕 一覧表示データ
     */
    public List<GXHDO202B011Model> getB011ListData() {
        return b011ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・FP排出 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・FP排出 一覧表示データ
     */
    public List<GXHDO202B012Model> getB012ListData() {
        return b012ListData;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・BET 一覧表示データ取得
     * @return 添加材ｽﾗﾘｰ作製・BET 一覧表示データ
     */
    public List<GXHDO202B013Model> getB013ListData() {
        return b013ListData;
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
     * 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ 一覧表示データ
     * @param b007ListData the b007ListData to set
     */
    public void setB007ListData(List<GXHDO202B007Model> b007ListData) {
        this.b007ListData = b007ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合 一覧表示データ
     * @param b008ListData the b008ListData to set
     */
    public void setB008ListData(List<GXHDO202B008Model> b008ListData) {
        this.b008ListData = b008ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・溶剤調合 一覧表示データ
     * @param b009ListData the b009ListData to set
     */
    public void setB009ListData(List<GXHDO202B009Model> b009ListData) {
        this.b009ListData = b009ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合 一覧表示データ
     * @param b010ListData the b010ListData to set
     */
    public void setB010ListData(List<GXHDO202B010Model> b010ListData) {
        this.b010ListData = b010ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕 一覧表示データ
     * @param b011ListData the b011ListData to set
     */
    public void setB011ListData(List<GXHDO202B011Model> b011ListData) {
        this.b011ListData = b011ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出 一覧表示データ
     * @param b012ListData the b012ListData to set
     */
    public void setB012ListData(List<GXHDO202B012Model> b012ListData) {
        this.b012ListData = b012ListData;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET 一覧表示データ
     * @param b013ListData the b013ListData to set
     */
    public void setB013ListData(List<GXHDO202B013Model> b013ListData) {
        this.b013ListData = b013ListData;
    }

    /**
     * 検索条件：固形分測定日(FROM)
     * @return the kokeibunsokuteiDateF
     */
    public String getKokeibunsokuteiDateF() {
        return kokeibunsokuteiDateF;
    }

    /**
     * 検索条件：固形分測定日(FROM)
     * @param kokeibunsokuteiDateF the kokeibunsokuteiDateF to set
     */
    public void setKokeibunsokuteiDateF(String kokeibunsokuteiDateF) {
        this.kokeibunsokuteiDateF = kokeibunsokuteiDateF;
    }

    /**
     * 検索条件：固形分測定日(TO)
     * @return the kokeibunsokuteiDateT
     */
    public String getKokeibunsokuteiDateT() {
        return kokeibunsokuteiDateT;
    }

    /**
     * 検索条件：固形分測定日(TO)
     * @param kokeibunsokuteiDateT the kokeibunsokuteiDateT to set
     */
    public void setKokeibunsokuteiDateT(String kokeibunsokuteiDateT) {
        this.kokeibunsokuteiDateT = kokeibunsokuteiDateT;
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
     * 検索条件：撹拌開始日(FROM)
     * @return the kakuhankaisiDateF
     */
    public String getKakuhankaisiDateF() {
        return kakuhankaisiDateF;
    }

    /**
     * 検索条件：撹拌開始日(FROM)
     * @param kakuhankaisiDateF the kakuhankaisiDateF to set
     */
    public void setKakuhankaisiDateF(String kakuhankaisiDateF) {
        this.kakuhankaisiDateF = kakuhankaisiDateF;
    }

    /**
     * 検索条件：撹拌開始日(TO)
     * @return the kakuhankaisiDateT
     */
    public String getKakuhankaisiDateT() {
        return kakuhankaisiDateT;
    }

    /**
     * 検索条件：撹拌開始日(TO)
     * @param kakuhankaisiDateT the kakuhankaisiDateT to set
     */
    public void setKakuhankaisiDateT(String kakuhankaisiDateT) {
        this.kakuhankaisiDateT = kakuhankaisiDateT;
    }

    /**
     * 検索条件：循環開始日(FROM)
     * @return the jyunkankaisiDateF
     */
    public String getJyunkankaisiDateF() {
        return jyunkankaisiDateF;
    }

    /**
     * 検索条件：循環開始日(FROM)
     * @param jyunkankaisiDateF the jyunkankaisiDateF to set
     */
    public void setJyunkankaisiDateF(String jyunkankaisiDateF) {
        this.jyunkankaisiDateF = jyunkankaisiDateF;
    }

    /**
     * 検索条件：循環開始日(TO)
     * @return the jyunkankaisiDateT
     */
    public String getJyunkankaisiDateT() {
        return jyunkankaisiDateT;
    }

    /**
     * 検索条件：循環開始日(TO)
     * @param jyunkankaisiDateT the jyunkankaisiDateT to set
     */
    public void setJyunkankaisiDateT(String jyunkankaisiDateT) {
        this.jyunkankaisiDateT = jyunkankaisiDateT;
    }

    /**
     * 検索条件：F/P開始日(FROM)
     * @return the fpkaisiDateF
     */
    public String getFpkaisiDateF() {
        return fpkaisiDateF;
    }

    /**
     * 検索条件：F/P開始日(FROM)
     * @param fpkaisiDateF the fpkaisiDateF to set
     */
    public void setFpkaisiDateF(String fpkaisiDateF) {
        this.fpkaisiDateF = fpkaisiDateF;
    }

    /**
     * 検索条件：F/P開始日(TO)
     * @return the fpkaisiDateT
     */
    public String getFpkaisiDateT() {
        return fpkaisiDateT;
    }

    /**
     * 検索条件：F/P開始日(TO)
     * @param fpkaisiDateT the fpkaisiDateT to set
     */
    public void setFpkaisiDateT(String fpkaisiDateT) {
        this.fpkaisiDateT = fpkaisiDateT;
    }

    /**
     * 検索条件：比表面積測定開始日(FROM)
     * @return the hihyoumensekisokuteikaisiDateF
     */
    public String getHihyoumensekisokuteikaisiDateF() {
        return hihyoumensekisokuteikaisiDateF;
    }

    /**
     * 検索条件：比表面積測定開始日(FROM)
     * @param hihyoumensekisokuteikaisiDateF the hihyoumensekisokuteikaisiDateF to set
     */
    public void setHihyoumensekisokuteikaisiDateF(String hihyoumensekisokuteikaisiDateF) {
        this.hihyoumensekisokuteikaisiDateF = hihyoumensekisokuteikaisiDateF;
    }

    /**
     * 検索条件：比表面積測定開始日(TO)
     * @return the hihyoumensekisokuteikaisiDateT
     */
    public String getHihyoumensekisokuteikaisiDateT() {
        return hihyoumensekisokuteikaisiDateT;
    }

    /**
     * 検索条件：比表面積測定開始日(TO)
     * @param hihyoumensekisokuteikaisiDateT the hihyoumensekisokuteikaisiDateT to set
     */
    public void setHihyoumensekisokuteikaisiDateT(String hihyoumensekisokuteikaisiDateT) {
        this.hihyoumensekisokuteikaisiDateT = hihyoumensekisokuteikaisiDateT;
    }

    /**
     * b007Listの制御
     * @return the b007DTdisplay
     */
    public String getB007DTdisplay() {
        return b007DTdisplay;
    }

    /**
     * b007Listの制御
     * @param b007DTdisplay the b007DTdisplay to set
     */
    public void setB007DTdisplay(String b007DTdisplay) {
        this.b007DTdisplay = b007DTdisplay;
    }

    /**
     * b008Listの制御
     * @return the b008DTdisplay
     */
    public String getB008DTdisplay() {
        return b008DTdisplay;
    }

    /**
     * b008Listの制御
     * @param b008DTdisplay the b008DTdisplay to set
     */
    public void setB008DTdisplay(String b008DTdisplay) {
        this.b008DTdisplay = b008DTdisplay;
    }

    /**
     * b009Listの制御
     * @return the b009DTdisplay
     */
    public String getB009DTdisplay() {
        return b009DTdisplay;
    }

    /**
     * b009Listの制御
     * @param b009DTdisplay the b009DTdisplay to set
     */
    public void setB009DTdisplay(String b009DTdisplay) {
        this.b009DTdisplay = b009DTdisplay;
    }

    /**
     * b010Listの制御
     * @return the b010DTdisplay
     */
    public String getB010DTdisplay() {
        return b010DTdisplay;
    }

    /**
     * b010Listの制御
     * @param b010DTdisplay the b010DTdisplay to set
     */
    public void setB010DTdisplay(String b010DTdisplay) {
        this.b010DTdisplay = b010DTdisplay;
    }

    /**
     * b011Listの制御
     * @return the b011DTdisplay
     */
    public String getB011DTdisplay() {
        return b011DTdisplay;
    }

    /**
     * b011Listの制御
     * @param b011DTdisplay the b011DTdisplay to set
     */
    public void setB011DTdisplay(String b011DTdisplay) {
        this.b011DTdisplay = b011DTdisplay;
    }

    /**
     * b012Listの制御
     * @return the b012DTdisplay
     */
    public String getB012DTdisplay() {
        return b012DTdisplay;
    }

    /**
     * b012Listの制御
     * @param b012DTdisplay the b012DTdisplay to set
     */
    public void setB012DTdisplay(String b012DTdisplay) {
        this.b012DTdisplay = b012DTdisplay;
    }

    /**
     * b013Listの制御
     * @return the b013DTdisplay
     */
    public String getB013DTdisplay() {
        return b013DTdisplay;
    }

    /**
     * b013Listの制御
     * @param b013DTdisplay the b013DTdisplay to set
     */
    public void setB013DTdisplay(String b013DTdisplay) {
        this.b013DTdisplay = b013DTdisplay;
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
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B003_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B003_display_page_count", session));
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
        setKokeibunsokuteiDateF("");
        setKokeibunsokuteiDateT("");
        setHyoryoDateF("");
        setHyoryoDateT("");
        setKakuhankaisiDateF("");
        setKakuhankaisiDateT("");
        setJyunkankaisiDateF("");
        setJyunkankaisiDateT("");
        setFpkaisiDateF("");
        setFpkaisiDateT("");
        setHihyoumensekisokuteikaisiDateF("");
        setHihyoumensekisokuteikaisiDateT("");

        b007ListData = new ArrayList<>();
        b008ListData = new ArrayList<>();
        b009ListData = new ArrayList<>();
        b010ListData = new ArrayList<>();
        b011ListData = new ArrayList<>();
        b012ListData = new ArrayList<>();
        b013ListData = new ArrayList<>();

        setB007DTdisplay(DISPLAY_NONE);
        setB008DTdisplay(DISPLAY_NONE);
        setB009DTdisplay(DISPLAY_NONE);
        setB010DTdisplay(DISPLAY_NONE);
        setB011DTdisplay(DISPLAY_NONE);
        setB012DTdisplay(DISPLAY_NONE);
        setB013DTdisplay(DISPLAY_NONE);
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
        // 固形分測定日(FROM)
        if (existError(validateUtil.checkC101(getKokeibunsokuteiDateF(), "固形分測定日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKokeibunsokuteiDateF(), "固形分測定日(from)")) ||
            existError(validateUtil.checkC501(getKokeibunsokuteiDateF(), "固形分測定日(from)"))) {
            return;
        }
        // 固形分測定日(TO)
        if (existError(validateUtil.checkC101(getKokeibunsokuteiDateT(), "固形分測定日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKokeibunsokuteiDateT(), "固形分測定日(to)")) ||
            existError(validateUtil.checkC501(getKokeibunsokuteiDateT(), "固形分測定日(to)"))) {
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
        // 撹拌開始日(FROM)
        if (existError(validateUtil.checkC101(getKakuhankaisiDateF(), "撹拌開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKakuhankaisiDateF(), "撹拌開始日(from)")) ||
            existError(validateUtil.checkC501(getKakuhankaisiDateF(), "撹拌開始日(from)"))) {
            return;
        }
        // 撹拌開始日(TO)
        if (existError(validateUtil.checkC101(getKakuhankaisiDateT(), "撹拌開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKakuhankaisiDateT(), "撹拌開始日(to)")) ||
            existError(validateUtil.checkC501(getKakuhankaisiDateT(), "撹拌開始日(to)"))) {
            return;
        }
        // 循環開始日(FROM)
        if (existError(validateUtil.checkC101(getJyunkankaisiDateF(), "循環開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getJyunkankaisiDateF(), "循環開始日(from)")) ||
            existError(validateUtil.checkC501(getJyunkankaisiDateF(), "循環開始日(from)"))) {
            return;
        }
        // 循環開始日(TO)
        if (existError(validateUtil.checkC101(getJyunkankaisiDateT(), "循環開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getJyunkankaisiDateT(), "循環開始日(to)")) ||
            existError(validateUtil.checkC501(getJyunkankaisiDateT(), "循環開始日(to)"))) {
            return;
        }
        // F/P開始日(FROM)
        if (existError(validateUtil.checkC101(getFpkaisiDateF(), "F/P開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFpkaisiDateF(), "F/P開始日(from)")) ||
            existError(validateUtil.checkC501(getFpkaisiDateF(), "F/P開始日(from)"))) {
            return;
        }
        // F/P開始日(TO)
        if (existError(validateUtil.checkC101(getFpkaisiDateT(), "F/P開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFpkaisiDateT(), "F/P開始日(to)")) ||
            existError(validateUtil.checkC501(getFpkaisiDateT(), "F/P開始日(to)"))) {
            return;
        }
        // 比表面積測定開始日(FROM)
        if (existError(validateUtil.checkC101(getHihyoumensekisokuteikaisiDateF(), "比表面積測定開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHihyoumensekisokuteikaisiDateF(), "比表面積測定開始日(from)")) ||
            existError(validateUtil.checkC501(getHihyoumensekisokuteikaisiDateF(), "比表面積測定開始日(from)"))) {
            return;
        }
        // 比表面積測定開始日(TO)
        if (existError(validateUtil.checkC101(getHihyoumensekisokuteikaisiDateT(), "比表面積測定開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getHihyoumensekisokuteikaisiDateT(), "比表面積測定開始日(to)")) ||
            existError(validateUtil.checkC501(getHihyoumensekisokuteikaisiDateT(), "比表面積測定開始日(to)"))) {
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
                case "ｶﾞﾗｽ":
                    // 工程が「ｶﾞﾗｽ」の場合、Ⅲ.画面表示仕様(3)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_glass "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkahinmei = ?) "
                            + "   AND (? IS NULL OR kokeibunsokuteinichiji >= ?) "
                            + "   AND (? IS NULL OR kokeibunsokuteinichiji <= ?) ";
                    break;
                case "添加材調合":
                    // 工程が「添加材調合」の場合、Ⅲ.画面表示仕様(7)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_tyogo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) ";
                    break;
                case "溶剤調合":
                    // 工程が「溶剤調合」の場合、Ⅲ.画面表示仕様(9)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) ";
                    break;
                case "予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)":
                    // 工程が「予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)」の場合、Ⅲ.画面表示仕様(11)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_premixing "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji <= ?) ";
                    break;
                case "粉砕":
                    // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(13)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_funsai F "
                            + " LEFT JOIN sub_sr_tenka_funsai SF "
                            + "        ON (F.kojyo = SF.kojyo) "
                            + "       AND (F.lotno = SF.lotno) "
                            + "       AND (F.edaban = SF.edaban) "
                            + " WHERE (? IS NULL OR F.kojyo = ?) "
                            + "   AND (? IS NULL OR F.lotno = ?) "
                            + "   AND (? IS NULL OR F.edaban = ?) "
                            + "   AND (? IS NULL OR F.tenkazaislurryhinmei = ?) " 
                            + "   AND (? IS NULL OR F.jyunkankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR F.jyunkankaisinichiji <= ?) ";
                    break;
                case "FP排出":
                    // 工程が「FP排出」の場合、Ⅲ.画面表示仕様(15)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR FPkaisinichiji >= ?) "
                            + "   AND (? IS NULL OR FPkaisinichiji <= ?) ";
                    break;
                case "BET":
                    // 工程が「BET」の場合、Ⅲ.画面表示仕様(17)を発行する。
                    sql += "SELECT COUNT(*) AS CNT "
                            + "  FROM sr_tenka_bet "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hihyoumensekisokuteikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hihyoumensekisokuteikaisinichiji <= ?) ";
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
            b007ListData = new ArrayList<>();
            b008ListData = new ArrayList<>();
            b009ListData = new ArrayList<>();
            b010ListData = new ArrayList<>();
            b011ListData = new ArrayList<>();
            b012ListData = new ArrayList<>();
            b013ListData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB007DTdisplay(DISPLAY_NONE);
        setB008DTdisplay(DISPLAY_NONE);
        setB009DTdisplay(DISPLAY_NONE);
        setB010DTdisplay(DISPLAY_NONE);
        setB011DTdisplay(DISPLAY_NONE);
        setB012DTdisplay(DISPLAY_NONE);
        setB013DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "ｶﾞﾗｽ":{
                    // 工程が「ｶﾞﾗｽ」の場合、Ⅲ.画面表示仕様(3)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkahinmei"
                            + ", tenkalotno"
                            + ", lotkubun"
                            + ", kokeibunsokuteinichiji"
                            + ", youkijyuuryou"
                            + ", soujyuuryou"
                            + ", syoumijyuuryou"
                            + ", kakuhanki"
                            + ", kakuhajikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", kakuhantantousya"
                            + ", rutubo"
                            + ", fuutaijyuuryou"
                            + ", kansoumaejyuuryou"
                            + ", kansouhouhou1"
                            + ", kansouondo1"
                            + ", kansoujikan1"
                            + ", kansoukaisinichiji1"
                            + ", kansousyuuryounichiji1"
                            + ", kansouhouhou2"
                            + ", kansouondo2"
                            + ", kansoujikan2"
                            + ", kansoukaisinichiji2"
                            + ", kansousyuuryounichiji2"
                            + ", kansougosoujyuuryou"
                            + ", kansougojyuuryou"
                            + ", kokeibun"
                            + ", kokeibunsokuteitantousya"
                            + ", kokeibunhoseiryou"
                            + ", youzai1tenkaryou"
                            + ", youzai1tyougouryou"
                            + ", youzai2tenkaryou"
                            + ", youzai2tyougouryou"
                            + ", kakuhanki2"
                            + ", kakuhajikan2"
                            + ", kakuhankaisinichiji2"
                            + ", kakuhansyuuryounichiji2"
                            + ", kakuhantantousya2"
                            + ", rutubo2"
                            + ", fuutaijyuuryou2"
                            + ", kansoumaejyuuryou2"
                            + ", kansouhouhou1_2"
                            + ", kansouondo1_2"
                            + ", kansoujikan1_2"
                            + ", kansoukaisinichiji1_2"
                            + ", kansousyuuryounichiji1_2"
                            + ", kansougosoujyuuryou2"
                            + ", kansougojyuuryou2"
                            + ", kokeibun2"
                            + ", kokeibunsokuteitantousya2"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_tenka_glass "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkahinmei = ?) "
                            + "   AND (? IS NULL OR kokeibunsokuteinichiji >= ?) "
                            + "   AND (? IS NULL OR kokeibunsokuteinichiji <= ?) "
                            + " ORDER BY kokeibunsokuteinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
                    mapping.put("tenkahinmei", "tenkahinmei");                             // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkalotno", "tenkalotno");                               // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
                    mapping.put("kokeibunsokuteinichiji", "kokeibunsokuteinichiji");       // 固形分測定日時
                    mapping.put("youkijyuuryou", "youkijyuuryou");                         // 容器重量
                    mapping.put("soujyuuryou", "soujyuuryou");                             // 総重量
                    mapping.put("syoumijyuuryou", "syoumijyuuryou");                       // 正味重量
                    mapping.put("kakuhanki", "kakuhanki");                                 // 撹拌機
                    mapping.put("kakuhajikan", "kakuhajikan");                             // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");             // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");       // 撹拌終了日時
                    mapping.put("kakuhantantousya", "kakuhantantousya");                   // 撹拌担当者
                    mapping.put("rutubo", "rutubo");                                       // ﾙﾂﾎﾞの種類
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                       // 風袋重量
                    mapping.put("kansoumaejyuuryou", "kansoumaejyuuryou");                 // 乾燥前重量
                    mapping.put("kansouhouhou1", "kansouhouhou1");                         // 乾燥方法①
                    mapping.put("kansouondo1", "kansouondo1");                             // 乾燥温度①
                    mapping.put("kansoujikan1", "kansoujikan1");                           // 乾燥時間①
                    mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");             // 乾燥開始日時①
                    mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");       // 乾燥終了日時①
                    mapping.put("kansouhouhou2", "kansouhouhou2");                         // 乾燥方法②
                    mapping.put("kansouondo2", "kansouondo2");                             // 乾燥温度②
                    mapping.put("kansoujikan2", "kansoujikan2");                           // 乾燥時間②
                    mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");             // 乾燥開始日時②
                    mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");       // 乾燥終了日時②
                    mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");             // 乾燥後総重量
                    mapping.put("kansougojyuuryou", "kansougojyuuryou");                   // 乾燥後重量
                    mapping.put("kokeibun", "kokeibun");                                   // 固形分
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");   // 固形分測定担当者
                    mapping.put("kokeibunhoseiryou", "kokeibunhoseiryou");                 // 固形分補正量
                    mapping.put("youzai1tenkaryou", "youzai1tenkaryou");                   // 溶剤①添加量
                    mapping.put("youzai1tyougouryou", "youzai1tyougouryou");               // 溶剤①調合量
                    mapping.put("youzai2tenkaryou", "youzai2tenkaryou");                   // 溶剤②添加量
                    mapping.put("youzai2tyougouryou", "youzai2tyougouryou");               // 溶剤②調合量
                    mapping.put("kakuhanki2", "kakuhanki2");                               // 2回目撹拌機
                    mapping.put("kakuhajikan2", "kakuhajikan2");                           // 2回目撹拌時間
                    mapping.put("kakuhankaisinichiji2", "kakuhankaisinichiji2");           // 2回目撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji2", "kakuhansyuuryounichiji2");     // 2回目撹拌終了日時
                    mapping.put("kakuhantantousya2", "kakuhantantousya2");                 // 2回目撹拌担当者
                    mapping.put("rutubo2", "rutubo2");                                     // 2回目ﾙﾂﾎﾞの種類
                    mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                     // 2回目風袋重量
                    mapping.put("kansoumaejyuuryou2", "kansoumaejyuuryou2");               // 2回目乾燥前重量
                    mapping.put("kansouhouhou1_2", "kansouhouhou1_2");                     // 2回目乾燥方法①
                    mapping.put("kansouondo1_2", "kansouondo1_2");                         // 2回目乾燥温度①
                    mapping.put("kansoujikan1_2", "kansoujikan1_2");                       // 2回目乾燥時間①
                    mapping.put("kansoukaisinichiji1_2", "kansoukaisinichiji1_2");         // 2回目乾燥開始日時①
                    mapping.put("kansousyuuryounichiji1_2", "kansousyuuryounichiji1_2");   // 2回目乾燥終了日時①
                    mapping.put("kansougosoujyuuryou2", "kansougosoujyuuryou2");           // 2回目乾燥後総重量
                    mapping.put("kansougojyuuryou2", "kansougojyuuryou2");                 // 2回目乾燥後重量
                    mapping.put("kokeibun2", "kokeibun2");                                 // 2回目固形分
                    mapping.put("kokeibunsokuteitantousya2", "kokeibunsokuteitantousya2"); // 2回目固形分測定担当者
                    mapping.put("kakuninsya", "kakuninsya");                               // 確認者
                    mapping.put("bikou1", "bikou1");                                       // 備考1
                    mapping.put("bikou2", "bikou2");                                       // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B007Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B007Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b007ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB007DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "添加材調合":{
                    // 工程が「添加材調合」の場合、Ⅲ.画面表示仕様(7)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", lotkubun"
                            + ", hyouryougouki"
                            + ", fuutaijyuuryou"
                            + ", hyouryoukaisinichiji"
                            + ", tenkazai1_zairyoumei"
                            + ", tenkazai1_tyougouryoukikaku"
                            + ", tenkazai1_buzaizaikolotno1"
                            + ", cast(tenkazai1_tyougouryou1 as SIGNED INTEGER) tenkazai1_tyougouryou1 "
                            + ", tenkazai1_buzaizaikolotno2"
                            + ", cast(tenkazai1_tyougouryou2 as SIGNED INTEGER) tenkazai1_tyougouryou2 "
                            + ", tenkazai2_zairyoumei"
                            + ", tenkazai2_tyougouryoukikaku"
                            + ", tenkazai2_buzaizaikolotno1"
                            + ", cast(tenkazai2_tyougouryou1 as SIGNED INTEGER) tenkazai2_tyougouryou1 "
                            + ", tenkazai2_buzaizaikolotno2"
                            + ", cast(tenkazai2_tyougouryou2 as SIGNED INTEGER) tenkazai2_tyougouryou2 "
                            + ", tenkazai3_zairyoumei"
                            + ", tenkazai3_tyougouryoukikaku"
                            + ", tenkazai3_buzaizaikolotno1"
                            + ", cast(tenkazai3_tyougouryou1 as SIGNED INTEGER) tenkazai3_tyougouryou1 "
                            + ", tenkazai3_buzaizaikolotno2"
                            + ", cast(tenkazai3_tyougouryou2 as SIGNED INTEGER) tenkazai3_tyougouryou2 "
                            + ", tenkazai4_zairyoumei"
                            + ", tenkazai4_tyougouryoukikaku"
                            + ", tenkazai4_buzaizaikolotno1"
                            + ", cast(tenkazai4_tyougouryou1 as SIGNED INTEGER) tenkazai4_tyougouryou1 "
                            + ", tenkazai4_buzaizaikolotno2"
                            + ", cast(tenkazai4_tyougouryou2 as SIGNED INTEGER) tenkazai4_tyougouryou2 "
                            + ", tenkazai5_zairyoumei"
                            + ", tenkazai5_tyougouryoukikaku"
                            + ", tenkazai5_buzaizaikolotno1"
                            + ", cast(tenkazai5_tyougouryou1 as SIGNED INTEGER) tenkazai5_tyougouryou1 "
                            + ", tenkazai5_buzaizaikolotno2"
                            + ", cast(tenkazai5_tyougouryou2 as SIGNED INTEGER) tenkazai5_tyougouryou2 "
                            + ", tenkazai6_zairyoumei"
                            + ", tenkazai6_tyougouryoukikaku"
                            + ", tenkazai6_buzaizaikolotno1"
                            + ", cast(tenkazai6_tyougouryou1 as SIGNED INTEGER) tenkazai6_tyougouryou1 "
                            + ", tenkazai6_buzaizaikolotno2"
                            + ", cast(tenkazai6_tyougouryou2 as SIGNED INTEGER) tenkazai6_tyougouryou2 "
                            + ", tenkazai7_zairyoumei"
                            + ", tenkazai7_tyougouryoukikaku"
                            + ", tenkazai7_buzaizaikolotno1"
                            + ", cast(tenkazai7_tyougouryou1 as SIGNED INTEGER) tenkazai7_tyougouryou1 "
                            + ", tenkazai7_buzaizaikolotno2"
                            + ", cast(tenkazai7_tyougouryou2 as SIGNED INTEGER) tenkazai7_tyougouryou2 "
                            + ", tenkazai8_zairyoumei"
                            + ", tenkazai8_tyougouryoukikaku"
                            + ", tenkazai8_buzaizaikolotno1"
                            + ", cast(tenkazai8_tyougouryou1 as SIGNED INTEGER) tenkazai8_tyougouryou1 "
                            + ", tenkazai8_buzaizaikolotno2"
                            + ", cast(tenkazai8_tyougouryou2 as SIGNED INTEGER) tenkazai8_tyougouryou2 "
                            + ", tenkazai9_zairyoumei"
                            + ", tenkazai9_tyougouryoukikaku"
                            + ", tenkazai9_buzaizaikolotno1"
                            + ", cast(tenkazai9_tyougouryou1 as SIGNED INTEGER) tenkazai9_tyougouryou1 "
                            + ", tenkazai9_buzaizaikolotno2"
                            + ", cast(tenkazai9_tyougouryou2 as SIGNED INTEGER) tenkazai9_tyougouryou2 "
                            + ", hyouryousyuuryounichiji"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_tenka_tyogo "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) "
                            + " ORDER BY hyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                               // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                 // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                   // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                         // ﾛｯﾄ区分
                    mapping.put("hyouryougouki", "hyouryougouki");                               // 秤量号機
                    mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                             // 風袋重量
                    mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                 // 秤量開始日時
                    mapping.put("tenkazai1_zairyoumei", "tenkazai1_zairyoumei");                 // 添加材①_材料品名
                    mapping.put("tenkazai1_tyougouryoukikaku", "tenkazai1_tyougouryoukikaku");   // 添加材①_調合量規格
                    mapping.put("tenkazai1_buzaizaikolotno1", "tenkazai1_buzaizaikolotno1");     // 添加材①_部材在庫No1
                    mapping.put("tenkazai1_tyougouryou1", "tenkazai1_tyougouryou1");             // 添加材①_調合量1
                    mapping.put("tenkazai1_buzaizaikolotno2", "tenkazai1_buzaizaikolotno2");     // 添加材①_部材在庫No2
                    mapping.put("tenkazai1_tyougouryou2", "tenkazai1_tyougouryou2");             // 添加材①_調合量2
                    mapping.put("tenkazai2_zairyoumei", "tenkazai2_zairyoumei");                 // 添加材②_材料品名
                    mapping.put("tenkazai2_tyougouryoukikaku", "tenkazai2_tyougouryoukikaku");   // 添加材②_調合量規格
                    mapping.put("tenkazai2_buzaizaikolotno1", "tenkazai2_buzaizaikolotno1");     // 添加材②_部材在庫No1
                    mapping.put("tenkazai2_tyougouryou1", "tenkazai2_tyougouryou1");             // 添加材②_調合量1
                    mapping.put("tenkazai2_buzaizaikolotno2", "tenkazai2_buzaizaikolotno2");     // 添加材②_部材在庫No2
                    mapping.put("tenkazai2_tyougouryou2", "tenkazai2_tyougouryou2");             // 添加材②_調合量2
                    mapping.put("tenkazai3_zairyoumei", "tenkazai3_zairyoumei");                 // 添加材③_材料品名
                    mapping.put("tenkazai3_tyougouryoukikaku", "tenkazai3_tyougouryoukikaku");   // 添加材③_調合量規格
                    mapping.put("tenkazai3_buzaizaikolotno1", "tenkazai3_buzaizaikolotno1");     // 添加材③_部材在庫No1
                    mapping.put("tenkazai3_tyougouryou1", "tenkazai3_tyougouryou1");             // 添加材③_調合量1
                    mapping.put("tenkazai3_buzaizaikolotno2", "tenkazai3_buzaizaikolotno2");     // 添加材③_部材在庫No2
                    mapping.put("tenkazai3_tyougouryou2", "tenkazai3_tyougouryou2");             // 添加材③_調合量2
                    mapping.put("tenkazai4_zairyoumei", "tenkazai4_zairyoumei");                 // 添加材④_材料品名
                    mapping.put("tenkazai4_tyougouryoukikaku", "tenkazai4_tyougouryoukikaku");   // 添加材④_調合量規格
                    mapping.put("tenkazai4_buzaizaikolotno1", "tenkazai4_buzaizaikolotno1");     // 添加材④_部材在庫No1
                    mapping.put("tenkazai4_tyougouryou1", "tenkazai4_tyougouryou1");             // 添加材④_調合量1
                    mapping.put("tenkazai4_buzaizaikolotno2", "tenkazai4_buzaizaikolotno2");     // 添加材④_部材在庫No2
                    mapping.put("tenkazai4_tyougouryou2", "tenkazai4_tyougouryou2");             // 添加材④_調合量2
                    mapping.put("tenkazai5_zairyoumei", "tenkazai5_zairyoumei");                 // 添加材⑤_材料品名
                    mapping.put("tenkazai5_tyougouryoukikaku", "tenkazai5_tyougouryoukikaku");   // 添加材⑤_調合量規格
                    mapping.put("tenkazai5_buzaizaikolotno1", "tenkazai5_buzaizaikolotno1");     // 添加材⑤_部材在庫No1
                    mapping.put("tenkazai5_tyougouryou1", "tenkazai5_tyougouryou1");             // 添加材⑤_調合量1
                    mapping.put("tenkazai5_buzaizaikolotno2", "tenkazai5_buzaizaikolotno2");     // 添加材⑤_部材在庫No2
                    mapping.put("tenkazai5_tyougouryou2", "tenkazai5_tyougouryou2");             // 添加材⑤_調合量2
                    mapping.put("tenkazai6_zairyoumei", "tenkazai6_zairyoumei");                 // 添加材⑥_材料品名
                    mapping.put("tenkazai6_tyougouryoukikaku", "tenkazai6_tyougouryoukikaku");   // 添加材⑥_調合量規格
                    mapping.put("tenkazai6_buzaizaikolotno1", "tenkazai6_buzaizaikolotno1");     // 添加材⑥_部材在庫No1
                    mapping.put("tenkazai6_tyougouryou1", "tenkazai6_tyougouryou1");             // 添加材⑥_調合量1
                    mapping.put("tenkazai6_buzaizaikolotno2", "tenkazai6_buzaizaikolotno2");     // 添加材⑥_部材在庫No2
                    mapping.put("tenkazai6_tyougouryou2", "tenkazai6_tyougouryou2");             // 添加材⑥_調合量2
                    mapping.put("tenkazai7_zairyoumei", "tenkazai7_zairyoumei");                 // 添加材⑦_材料品名
                    mapping.put("tenkazai7_tyougouryoukikaku", "tenkazai7_tyougouryoukikaku");   // 添加材⑦_調合量規格
                    mapping.put("tenkazai7_buzaizaikolotno1", "tenkazai7_buzaizaikolotno1");     // 添加材⑦_部材在庫No1
                    mapping.put("tenkazai7_tyougouryou1", "tenkazai7_tyougouryou1");             // 添加材⑦_調合量1
                    mapping.put("tenkazai7_buzaizaikolotno2", "tenkazai7_buzaizaikolotno2");     // 添加材⑦_部材在庫No2
                    mapping.put("tenkazai7_tyougouryou2", "tenkazai7_tyougouryou2");             // 添加材⑦_調合量2
                    mapping.put("tenkazai8_zairyoumei", "tenkazai8_zairyoumei");                 // 添加材⑧_材料品名
                    mapping.put("tenkazai8_tyougouryoukikaku", "tenkazai8_tyougouryoukikaku");   // 添加材⑧_調合量規格
                    mapping.put("tenkazai8_buzaizaikolotno1", "tenkazai8_buzaizaikolotno1");     // 添加材⑧_部材在庫No1
                    mapping.put("tenkazai8_tyougouryou1", "tenkazai8_tyougouryou1");             // 添加材⑧_調合量1
                    mapping.put("tenkazai8_buzaizaikolotno2", "tenkazai8_buzaizaikolotno2");     // 添加材⑧_部材在庫No2
                    mapping.put("tenkazai8_tyougouryou2", "tenkazai8_tyougouryou2");             // 添加材⑧_調合量2
                    mapping.put("tenkazai9_zairyoumei", "tenkazai9_zairyoumei");                 // 添加材⑨_材料品名
                    mapping.put("tenkazai9_tyougouryoukikaku", "tenkazai9_tyougouryoukikaku");   // 添加材⑨_調合量規格
                    mapping.put("tenkazai9_buzaizaikolotno1", "tenkazai9_buzaizaikolotno1");     // 添加材⑨_部材在庫No1
                    mapping.put("tenkazai9_tyougouryou1", "tenkazai9_tyougouryou1");             // 添加材⑨_調合量1
                    mapping.put("tenkazai9_buzaizaikolotno2", "tenkazai9_buzaizaikolotno2");     // 添加材⑨_部材在庫No2
                    mapping.put("tenkazai9_tyougouryou2", "tenkazai9_tyougouryou2");             // 添加材⑨_調合量2
                    mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");           // 秤量終了日時
                    mapping.put("tantousya", "tantousya");                                       // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                     // 確認者
                    mapping.put("bikou1", "bikou1");                                             // 備考1
                    mapping.put("bikou2", "bikou2");                                             // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B008Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B008Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b008ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB008DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "溶剤調合":{
                    // 工程が「溶剤調合」の場合、Ⅲ.画面表示仕様(9)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", lotkubun"
                            + ", hyouryougouki"
                            + ", hyouryoukaisinichiji"
                            + ", bunsanzai1_zairyouhinmei"
                            + ", bunsanzai1_tyougouryoukikaku"
                            + ", bunsanzai1_buzaizaikolotno1"
                            + ", cast(bunsanzai1_tyougouryou1 as SIGNED INTEGER) bunsanzai1_tyougouryou1 "
                            + ", bunsanzai1_buzaizaikolotno2"
                            + ", cast(bunsanzai1_tyougouryou2 as SIGNED INTEGER) bunsanzai1_tyougouryou2 "
                            + ", bunsanzai2_zairyouhinmei"
                            + ", bunsanzai2_tyougouryoukikaku"
                            + ", bunsanzai2_buzaizaikolotno1"
                            + ", cast(bunsanzai2_tyougouryou1 as SIGNED INTEGER) bunsanzai2_tyougouryou1 "
                            + ", bunsanzai2_buzaizaikolotno2"
                            + ", cast(bunsanzai2_tyougouryou2 as SIGNED INTEGER) bunsanzai2_tyougouryou2 "
                            + ", youzai1_zairyouhinmei"
                            + ", youzai1_tyougouryoukikaku"
                            + ", youzai1_buzaizaikolotno1"
                            + ", cast(youzai1_tyougouryou1 as SIGNED INTEGER) youzai1_tyougouryou1 "
                            + ", youzai1_buzaizaikolotno2"
                            + ", cast(youzai1_tyougouryou2 as SIGNED INTEGER) youzai1_tyougouryou2 "
                            + ", youzai2_zairyouhinmei"
                            + ", youzai2_tyougouryoukikaku"
                            + ", youzai2_buzaizaikolotno1"
                            + ", cast(youzai2_tyougouryou1 as SIGNED INTEGER) youzai2_tyougouryou1 "
                            + ", youzai2_buzaizaikolotno2"
                            + ", cast(youzai2_tyougouryou2 as SIGNED INTEGER) youzai2_tyougouryou2 "
                            + ", youzai3_zairyouhinmei"
                            + ", youzai3_tyougouryoukikaku"
                            + ", youzai3_buzaizaikolotno1"
                            + ", cast(youzai3_tyougouryou1 as SIGNED INTEGER) youzai3_tyougouryou1 "
                            + ", youzai3_buzaizaikolotno2"
                            + ", cast(youzai3_tyougouryou2 as SIGNED INTEGER) youzai3_tyougouryou2 "
                            + ", youzai4_zairyouhinmei"
                            + ", youzai4_tyougouryoukikaku"
                            + ", youzai4_buzaizaikolotno1"
                            + ", cast(youzai4_tyougouryou1 as SIGNED INTEGER) youzai4_tyougouryou1 "
                            + ", youzai4_buzaizaikolotno2"
                            + ", cast(youzai4_tyougouryou2 as SIGNED INTEGER) youzai4_tyougouryou2 "
                            + ", glassslurryhinmei"
                            + ", glassslurrytyougouryoukikaku"
                            + ", glassslurrylotno"
                            + ", cast(glassslurrytyougouryou as SIGNED INTEGER) glassslurrytyougouryou "
                            + ", hyouryousyuuryounichiji"
                            + ", kakuhanki"
                            + ", cast(kakuhanjikan as SIGNED INTEGER) kakuhanjikan "
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_tenka_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) "
                            + " ORDER BY hyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                   // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                     // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                           // ﾛｯﾄ区分
                    mapping.put("hyouryougouki", "hyouryougouki");                                 // 秤量号機
                    mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                   // 秤量開始日時
                    mapping.put("bunsanzai1_zairyouhinmei", "bunsanzai1_zairyouhinmei");           // 分散材①_材料品名
                    mapping.put("bunsanzai1_tyougouryoukikaku", "bunsanzai1_tyougouryoukikaku");   // 分散材①_調合量規格
                    mapping.put("bunsanzai1_buzaizaikolotno1", "bunsanzai1_buzaizaikolotno1");     // 分散材①_部材在庫No1
                    mapping.put("bunsanzai1_tyougouryou1", "bunsanzai1_tyougouryou1");             // 分散材①_調合量1
                    mapping.put("bunsanzai1_buzaizaikolotno2", "bunsanzai1_buzaizaikolotno2");     // 分散材①_部材在庫No2
                    mapping.put("bunsanzai1_tyougouryou2", "bunsanzai1_tyougouryou2");             // 分散材①_調合量2
                    mapping.put("bunsanzai2_zairyouhinmei", "bunsanzai2_zairyouhinmei");           // 分散材②_材料品名
                    mapping.put("bunsanzai2_tyougouryoukikaku", "bunsanzai2_tyougouryoukikaku");   // 分散材②_調合量規格
                    mapping.put("bunsanzai2_buzaizaikolotno1", "bunsanzai2_buzaizaikolotno1");     // 分散材②_部材在庫No1
                    mapping.put("bunsanzai2_tyougouryou1", "bunsanzai2_tyougouryou1");             // 分散材②_調合量1
                    mapping.put("bunsanzai2_buzaizaikolotno2", "bunsanzai2_buzaizaikolotno2");     // 分散材②_部材在庫No2
                    mapping.put("bunsanzai2_tyougouryou2", "bunsanzai2_tyougouryou2");             // 分散材②_調合量2
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                 // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");         // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");           // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                   // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");           // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                   // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                 // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");         // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");           // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                   // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");           // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                   // 溶剤②_調合量2
                    mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");                 // 溶剤③_材料品名
                    mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");         // 溶剤③_調合量規格
                    mapping.put("youzai3_buzaizaikolotno1", "youzai3_buzaizaikolotno1");           // 溶剤③_部材在庫No1
                    mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");                   // 溶剤③_調合量1
                    mapping.put("youzai3_buzaizaikolotno2", "youzai3_buzaizaikolotno2");           // 溶剤③_部材在庫No2
                    mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");                   // 溶剤③_調合量2
                    mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");                 // 溶剤④_材料品名
                    mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");         // 溶剤④_調合量規格
                    mapping.put("youzai4_buzaizaikolotno1", "youzai4_buzaizaikolotno1");           // 溶剤④_部材在庫No1
                    mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");                   // 溶剤④_調合量1
                    mapping.put("youzai4_buzaizaikolotno2", "youzai4_buzaizaikolotno2");           // 溶剤④_部材在庫No2
                    mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");                   // 溶剤④_調合量2
                    mapping.put("glassslurryhinmei", "glassslurryhinmei");                         // ｶﾞﾗｽｽﾗﾘｰ品名
                    mapping.put("glassslurrytyougouryoukikaku", "glassslurrytyougouryoukikaku");   // ｶﾞﾗｽｽﾗﾘｰ調合量規格
                    mapping.put("glassslurrylotno", "glassslurrylotno");                           // ｶﾞﾗｽｽﾗﾘｰLotNo
                    mapping.put("glassslurrytyougouryou", "glassslurrytyougouryou");               // ｶﾞﾗｽｽﾗﾘｰ調合量
                    mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");             // 秤量終了日時
                    mapping.put("kakuhanki", "kakuhanki");                                         // 撹拌機
                    mapping.put("kakuhanjikan", "kakuhanjikan");                                   // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                     // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");               // 撹拌終了日時
                    mapping.put("tantousya", "tantousya");                                         // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                                       // 確認者
                    mapping.put("bikou1", "bikou1");                                               // 備考1
                    mapping.put("bikou2", "bikou2");                                               // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B009Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B009Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b009ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB009DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)":{
                    // 工程が「予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)」の場合、Ⅲ.画面表示仕様(11)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", lotkubun"
                            + ", kakuhanki"
                            + ", tanku"
                            + ", youzai"
                            + ", senjojikan"
                            + ", shujiku"
                            + ", ponpu"
                            + ", desupakaitensuu"
                            + ", tounyu1"
                            + ", tounyu2"
                            + ", totyukakuhanjikan"
                            + ", totyukakuhankaisinichiji"
                            + ", totyukakuhansyuryonichiji"
                            + ", tonyu3"
                            + ", tonyu4"
                            + ", tonyu5"
                            + ", kakuhanjikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuryonichiji"
                            + ", kaitentai"
                            + ", tantousya"
                            + ", kakuninsya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_tenka_premixing "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji <= ?) "
                            + " ORDER BY kakuhankaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");           // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");             // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
                    mapping.put("kakuhanki", "kakuhanki");                                 // 撹拌機
                    mapping.put("tanku", "tanku");                                         // ﾀﾝｸ
                    mapping.put("youzai", "youzai");                                       // 溶剤
                    mapping.put("senjojikan", "senjojikan");                               // 洗浄時間
                    mapping.put("shujiku", "shujiku");                                     // 主軸
                    mapping.put("ponpu", "ponpu");                                         // ﾎﾟﾝﾌﾟ
                    mapping.put("desupakaitensuu", "desupakaitensuu");                     // ﾃﾞｽﾊﾟ回転数
                    mapping.put("tounyu1", "tounyu1");                                     // 投入①
                    mapping.put("tounyu2", "tounyu2");                                     // 投入②
                    mapping.put("totyukakuhanjikan", "totyukakuhanjikan");                 // 途中撹拌時間
                    mapping.put("totyukakuhankaisinichiji", "totyukakuhankaisinichiji");   // 途中撹拌開始日時
                    mapping.put("totyukakuhansyuryonichiji", "totyukakuhansyuryonichiji"); // 途中撹拌終了日時
                    mapping.put("tonyu3", "tonyu3");                                       // 投入③
                    mapping.put("tonyu4", "tonyu4");                                       // 投入④
                    mapping.put("tonyu5", "tonyu5");                                       // 投入⑤
                    mapping.put("kakuhanjikan", "kakuhanjikan");                           // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");             // 撹拌開始日時
                    mapping.put("kakuhansyuryonichiji", "kakuhansyuryonichiji");           // 撹拌終了日時
                    mapping.put("kaitentai", "kaitentai");                                 // 回転体の接触確認
                    mapping.put("tantousya", "tantousya");                                 // 担当者
                    mapping.put("kakuninsya", "kakuninsya");                               // 確認者
                    mapping.put("bikou1", "bikou1");                                       // 備考1
                    mapping.put("bikou2", "bikou2");                                       // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B010Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B010Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b010ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB010DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "粉砕":{
                    // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(13)を発行する。
                    sql ="SELECT CONCAT(F.kojyo , F.lotno , F.edaban) AS lotno "
                            + ", F.tenkazaislurryhinmei"
                            + ", F.tenkazaislurrylotno"
                            + ", F.lotkubun"
                            + ", F.hyouryougouki"
                            + ", (CASE WHEN F.funsaiki = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS funsaiki "
                            + ", (CASE WHEN F.funsaigouki = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS funsaigouki "
                            + ", F.medialotno"
                            + ", (CASE WHEN F.renzokuuntenkaisuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS renzokuuntenkaisuu "
                            + ", (CASE WHEN F.tounyuuryou = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS tounyuuryou "
                            + ", (CASE WHEN F.jikan_passkaisuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS jikan_passkaisuu "
                            + ", (CASE WHEN F.millsyuuhasuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS millsyuuhasuu "
                            + ", (CASE WHEN F.syuusoku = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS syuusoku "
                            + ", (CASE WHEN F.pumpsyuturyokcheck = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS pumpsyuturyokcheck "
                            + ", (CASE WHEN F.ryuuryou = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS ryuuryou "
                            + ", (CASE WHEN F.passkaisuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS passkaisuu "
                            + ", (CASE WHEN F.dispanosyurui = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS dispanosyurui "
                            + ", (CASE WHEN F.dispakaitensuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS dispakaitensuu "
                            + ", SF.passkaisuu passkaisuu_sf"
                            + ", SF.kaisinichiji"
                            + ", SF.syuuryouyoteinichiji"
                            + ", SF.fukadenryuuti"
                            + ", SF.seihinondo"
                            + ", SF.stickerondo"
                            + ", SF.pumpmemori"
                            + ", SF.pumpatu"
                            + ", SF.ryuuryou ryuuryou_sf"
                            + ", SF.bikou1"
                            + ", SF.bikou2"
                            + ", SF.ondo_ou"
                            + ", SF.ondo_kan"
                            + ", SF.aturyoku_ou"
                            + ", SF.aturyoku_kan"
                            + ", SF.syuuryounichiji"
                            + ", SF.tantousya tantousya_sf"
                            + ", F.youzai1_zairyouhinmei"
                            + ", F.youzai1_tyougouryoukikaku"
                            + ", F.youzai1_buzaizaikolotno1"
                            + ", cast(F.youzai1_tyougouryou1 as SIGNED INTEGER) youzai1_tyougouryou1 "
                            + ", F.youzai1_buzaizaikolotno2"
                            + ", cast(F.youzai1_tyougouryou2 as SIGNED INTEGER) youzai1_tyougouryou2 "
                            + ", F.youzai2_zairyouhinmei"
                            + ", F.youzai2_tyougouryoukikaku"
                            + ", F.youzai2_buzaizaikolotno1"
                            + ", cast(F.youzai2_tyougouryou1 as SIGNED INTEGER) youzai2_tyougouryou1 "
                            + ", F.youzai2_buzaizaikolotno2"
                            + ", cast(F.youzai2_tyougouryou2 as SIGNED INTEGER) youzai2_tyougouryou2 "
                            + ", F.tantousya"
                            + ", F.pumpsyuturyoku"
                            + ", F.millsyuuhasuu2"
                            + ", (CASE WHEN F.kisyakuyouzaitenka = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS kisyakuyouzaitenka "
                            + ", cast(F.youzaijyunkanjikan as SIGNED INTEGER) youzaijyunkanjikan "
                            + ", F.jyunkankaisinichiji"
                            + ", F.jyunkansyuuryounichiji"
                            + ", F.jyunkantantousya"
                            + "  FROM sr_tenka_funsai F "
                            + " LEFT JOIN sub_sr_tenka_funsai SF "
                            + "        ON (F.kojyo = SF.kojyo) "
                            + "       AND (F.lotno = SF.lotno) "
                            + "       AND (F.edaban = SF.edaban) "
                            + " WHERE (? IS NULL OR F.kojyo = ?) "
                            + "   AND (? IS NULL OR F.lotno = ?) "
                            + "   AND (? IS NULL OR F.edaban = ?) "
                            + "   AND (? IS NULL OR F.tenkazaislurryhinmei = ?) " 
                            + "   AND (? IS NULL OR F.jyunkankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR F.jyunkankaisinichiji <= ?) "
                            + " ORDER BY F.jyunkankaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                           // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");             // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");               // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                     // ﾛｯﾄ区分
                    mapping.put("hyouryougouki", "hyouryougouki");                           // 秤量号機
                    mapping.put("funsaiki", "funsaiki");                                     // 粉砕機
                    mapping.put("funsaigouki", "funsaigouki");                               // 粉砕号機
                    mapping.put("medialotno", "medialotno");                                 // ﾒﾃﾞｨｱLotNo
                    mapping.put("renzokuuntenkaisuu", "renzokuuntenkaisuu");                 // 連続運転回数
                    mapping.put("tounyuuryou", "tounyuuryou");                               // 投入量
                    mapping.put("jikan_passkaisuu", "jikan_passkaisuu");                     // 時間/ﾊﾟｽ回数
                    mapping.put("millsyuuhasuu", "millsyuuhasuu");                           // ﾐﾙ周波数
                    mapping.put("syuusoku", "syuusoku");                                     // 周速
                    mapping.put("pumpsyuturyokcheck", "pumpsyuturyokcheck");                 // ﾎﾟﾝﾌﾟ出力
                    mapping.put("ryuuryou", "ryuuryou");                                     // 流量
                    mapping.put("passkaisuu", "passkaisuu");                                 // ﾊﾟｽ回数
                    mapping.put("dispanosyurui", "dispanosyurui");                           // ﾃﾞｨｽﾊﾟの種類
                    mapping.put("dispakaitensuu", "dispakaitensuu");                         // ﾃﾞｨｽﾊﾟ回転数
                    mapping.put("passkaisuu_sf", "passkaisuu_sf");                           // ﾊﾟｽ回数
                    mapping.put("kaisinichiji", "kaisinichiji");                             // 開始日時
                    mapping.put("syuuryouyoteinichiji", "syuuryouyoteinichiji");             // 終了予定日時
                    mapping.put("fukadenryuuti", "fukadenryuuti");                           // 負荷電流値
                    mapping.put("seihinondo", "seihinondo");                                 // 製品温度
                    mapping.put("stickerondo", "stickerondo");                               // ｼｰﾙ温度
                    mapping.put("pumpmemori", "pumpmemori");                                 // ﾎﾟﾝﾌﾟ目盛
                    mapping.put("pumpatu", "pumpatu");                                       // ﾎﾟﾝﾌﾟ圧
                    mapping.put("ryuuryou_sf", "ryuuryou_sf");                               // 流量
                    mapping.put("bikou1", "bikou1");                                         // 備考1
                    mapping.put("bikou2", "bikou2");                                         // 備考2
                    mapping.put("ondo_ou", "ondo_ou");                                       // 温度(往)
                    mapping.put("ondo_kan", "ondo_kan");                                     // 温度(還)
                    mapping.put("aturyoku_ou", "aturyoku_ou");                               // 圧力(往)
                    mapping.put("aturyoku_kan", "aturyoku_kan");                             // 圧力(還)
                    mapping.put("syuuryounichiji", "syuuryounichiji");                       // 終了日時
                    mapping.put("tantousya_sf", "tantousya_sf");                             // 担当者
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");           // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");   // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");     // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");             // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");     // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");             // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");           // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");   // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");     // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");             // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");     // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");             // 溶剤②_調合量2
                    mapping.put("tantousya", "tantousya");                                   // 担当者
                    mapping.put("pumpsyuturyoku", "pumpsyuturyoku");                         // ﾎﾟﾝﾌﾟ出力
                    mapping.put("millsyuuhasuu2", "millsyuuhasuu2");                         // ﾐﾙ周波数
                    mapping.put("kisyakuyouzaitenka", "kisyakuyouzaitenka");                 // 希釈溶剤添加
                    mapping.put("youzaijyunkanjikan", "youzaijyunkanjikan");                 // 溶剤循環時間
                    mapping.put("jyunkankaisinichiji", "jyunkankaisinichiji");               // 循環開始日時
                    mapping.put("jyunkansyuuryounichiji", "jyunkansyuuryounichiji");         // 循環終了日時
                    mapping.put("jyunkantantousya", "jyunkantantousya");                     // 担当者
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B011Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B011Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b011ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB011DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "FP排出":{
                    // 工程が「FP排出」の場合、Ⅲ.画面表示仕様(15)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", lotkubun"
                            + ", saisyuupasskaisuu"
                            + ", (CASE WHEN hozonyousamplekaisyuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS hozonyousamplekaisyuu "
                            + ", fuutaijyuuryou1"
                            + ", fuutaijyuuryou2"
                            + ", fuutaijyuuryou3"
                            + ", fuutaijyuuryou4"
                            + ", fuutaijyuuryou5"
                            + ", fuutaijyuuryou6"
                            + ", FPjyunbi_tantousya"
                            + ", FPjyunbi_filterrenketu"
                            + ", FPjyunbi_filterhinmei1"
                            + ", FPjyunbi_lotno1"
                            + ", FPjyunbi_toritukehonsuu1"
                            + ", FPjyunbi_filterhinmei2"
                            + ", FPjyunbi_lotno2"
                            + ", FPjyunbi_toritukehonsuu2"
                            + ", (CASE WHEN haisyutuyoukinoutibukuro = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS haisyutuyoukinoutibukuro "
                            + ", filterkaisuu"
                            + ", FPtankno"
                            + ", (CASE WHEN senjyoukakunin = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS senjyoukakunin "
                            + ", FPkaisinichiji"
                            + ", assouregulatorNo"
                            + ", assouaturyoku"
                            + ", FPkaisi_tantousya"
                            + ", FPteisinichiji"
                            + ", FPkoukan_filterrenketu"
                            + ", FPkoukan_filterhinmei1"
                            + ", FPkoukan_lotno1"
                            + ", FPkoukan_toritukehonsuu1"
                            + ", FPkoukan_filterhinmei2"
                            + ", FPkoukan_lotno2"
                            + ", FPkoukan_toritukehonsuu2"
                            + ", FPsaikainichiji"
                            + ", FPkoukan_tantousya"
                            + ", FPsyuuryounichiji"
                            + ", FPjikan"
                            + ", FPsyuurou_tantousya"
                            + ", soujyuurou1"
                            + ", soujyuurou2"
                            + ", soujyuurou3"
                            + ", soujyuurou4"
                            + ", soujyuurou5"
                            + ", soujyuurou6"
                            + ", tenkazaislurryjyuuryou1"
                            + ", tenkazaislurryjyuuryou2"
                            + ", tenkazaislurryjyuuryou3"
                            + ", tenkazaislurryjyuuryou4"
                            + ", tenkazaislurryjyuuryou5"
                            + ", tenkazaislurryjyuuryou6"
                            + ", tenkazaislurryjyuuryougoukei"
                            + ", budomari"
                            + ", tenkazaislurryyuukoukigen"
                            + ", (CASE WHEN funsaihantei = 0 THEN '不合格' WHEN funsaihantei = 1 THEN '合格' ELSE '' END) AS funsaihantei "
                            + ", tantousya "
                            + "  FROM sr_tenka_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR FPkaisinichiji >= ?) "
                            + "   AND (? IS NULL OR FPkaisinichiji <= ?) "
                            + " ORDER BY FPkaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                  // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                    // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                          // ﾛｯﾄ区分
                    mapping.put("saisyuupasskaisuu", "saisyuupasskaisuu");                        // 最終パス回数
                    mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                // 保存用ｻﾝﾌﾟﾙ回収
                    mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                            // 風袋重量①
                    mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                            // 風袋重量②
                    mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                            // 風袋重量③
                    mapping.put("fuutaijyuuryou4", "fuutaijyuuryou4");                            // 風袋重量④
                    mapping.put("fuutaijyuuryou5", "fuutaijyuuryou5");                            // 風袋重量⑤
                    mapping.put("fuutaijyuuryou6", "fuutaijyuuryou6");                            // 風袋重量⑥
                    mapping.put("FPjyunbi_tantousya", "FPjyunbi_tantousya");                      // F/P準備_担当者
                    mapping.put("FPjyunbi_filterrenketu", "FPjyunbi_filterrenketu");              // F/P準備_ﾌｨﾙﾀｰ連結
                    mapping.put("FPjyunbi_filterhinmei1", "FPjyunbi_filterhinmei1");              // F/P準備_ﾌｨﾙﾀｰ品名①
                    mapping.put("FPjyunbi_lotno1", "FPjyunbi_lotno1");                            // F/P準備_LotNo①
                    mapping.put("FPjyunbi_toritukehonsuu1", "FPjyunbi_toritukehonsuu1");          // F/P準備_取り付け本数①
                    mapping.put("FPjyunbi_filterhinmei2", "FPjyunbi_filterhinmei2");              // F/P準備_ﾌｨﾙﾀｰ品名①
                    mapping.put("FPjyunbi_lotno2", "FPjyunbi_lotno2");                            // F/P準備_LotNo②
                    mapping.put("FPjyunbi_toritukehonsuu2", "FPjyunbi_toritukehonsuu2");          // F/P準備_取り付け本数②
                    mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");          // 排出容器の内袋
                    mapping.put("filterkaisuu", "filterkaisuu");                                  // ﾌｨﾙﾀｰ使用回数
                    mapping.put("FPtankno", "FPtankno");                                          // F/PﾀﾝｸNo
                    mapping.put("senjyoukakunin", "senjyoukakunin");                              // 洗浄確認
                    mapping.put("FPkaisinichiji", "FPkaisinichiji");                              // F/P開始日時
                    mapping.put("assouregulatorNo", "assouregulatorNo");                          // 圧送ﾚｷﾞｭﾚｰﾀｰNo
                    mapping.put("assouaturyoku", "assouaturyoku");                                // 圧送圧力
                    mapping.put("FPkaisi_tantousya", "FPkaisi_tantousya");                        // F/P開始_担当者
                    mapping.put("FPteisinichiji", "FPteisinichiji");                              // F/P停止日時
                    mapping.put("FPkoukan_filterrenketu", "FPkoukan_filterrenketu");              // F/P交換_ﾌｨﾙﾀｰ連結
                    mapping.put("FPkoukan_filterhinmei1", "FPkoukan_filterhinmei1");              // F/P交換_ﾌｨﾙﾀｰ品名①
                    mapping.put("FPkoukan_lotno1", "FPkoukan_lotno1");                            // F/P交換_LotNo①
                    mapping.put("FPkoukan_toritukehonsuu1", "FPkoukan_toritukehonsuu1");          // F/P交換_取り付け本数①
                    mapping.put("FPkoukan_filterhinmei2", "FPkoukan_filterhinmei2");              // F/P交換_ﾌｨﾙﾀｰ品名①
                    mapping.put("FPkoukan_lotno2", "FPkoukan_lotno2");                            // F/P交換_LotNo②
                    mapping.put("FPkoukan_toritukehonsuu2", "FPkoukan_toritukehonsuu2");          // F/P交換_取り付け本数②
                    mapping.put("FPsaikainichiji", "FPsaikainichiji");                            // F/P再開日時
                    mapping.put("FPkoukan_tantousya", "FPkoukan_tantousya");                      // F/P交換_担当者
                    mapping.put("FPsyuuryounichiji", "FPsyuuryounichiji");                        // F/P終了日時
                    mapping.put("FPjikan", "FPjikan");                                            // F/P時間
                    mapping.put("FPsyuurou_tantousya", "FPsyuurou_tantousya");                    // F/P終了_担当者
                    mapping.put("soujyuurou1", "soujyuurou1");                                    // 総重量①
                    mapping.put("soujyuurou2", "soujyuurou2");                                    // 総重量②
                    mapping.put("soujyuurou3", "soujyuurou3");                                    // 総重量③
                    mapping.put("soujyuurou4", "soujyuurou4");                                    // 総重量④
                    mapping.put("soujyuurou5", "soujyuurou5");                                    // 総重量⑤
                    mapping.put("soujyuurou6", "soujyuurou6");                                    // 総重量⑥
                    mapping.put("tenkazaislurryjyuuryou1", "tenkazaislurryjyuuryou1");            // 添加材ｽﾗﾘｰ重量①
                    mapping.put("tenkazaislurryjyuuryou2", "tenkazaislurryjyuuryou2");            // 添加材ｽﾗﾘｰ重量②
                    mapping.put("tenkazaislurryjyuuryou3", "tenkazaislurryjyuuryou3");            // 添加材ｽﾗﾘｰ重量③
                    mapping.put("tenkazaislurryjyuuryou4", "tenkazaislurryjyuuryou4");            // 添加材ｽﾗﾘｰ重量④
                    mapping.put("tenkazaislurryjyuuryou5", "tenkazaislurryjyuuryou5");            // 添加材ｽﾗﾘｰ重量⑤
                    mapping.put("tenkazaislurryjyuuryou6", "tenkazaislurryjyuuryou6");            // 添加材ｽﾗﾘｰ重量⑥
                    mapping.put("tenkazaislurryjyuuryougoukei", "tenkazaislurryjyuuryougoukei");  // 添加材ｽﾗﾘｰ重量合計
                    mapping.put("budomari", "budomari");                                          // 歩留まり
                    mapping.put("tenkazaislurryyuukoukigen", "tenkazaislurryyuukoukigen");        // 添加材ｽﾗﾘｰ有効期限
                    mapping.put("funsaihantei", "funsaihantei");                                  // 粉砕判定
                    mapping.put("tantousya", "tantousya");                                        // 担当者
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B012Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B012Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b012ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB012DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "BET":{
                    // 工程が「BET」の場合、Ⅲ.画面表示仕様(17)を発行する。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", lotkubun"
                            + ", (CASE WHEN kansouzaranosyurui = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS kansouzaranosyurui "
                            + ", slurryjyuuryou"
                            + ", kansouondo"
                            + ", kansoujikankikaku"
                            + ", kansoukaisinichiji"
                            + ", kansousyuuryounichiji"
                            + ", (CASE WHEN dassirogouki = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS dassirogouki "
                            + ", dassiondo"
                            + ", dassijikankikaku"
                            + ", dassikaisinichiji"
                            + ", dassisyuuryounichiji"
                            + ", kansoutantousya"
                            + ", (CASE WHEN sokuteisample = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS sokuteisample "
                            + ", (CASE WHEN samplejyuuryou = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS samplejyuuryou "
                            + ", maesyoriondo"
                            + ", maesyorijikan"
                            + ", maesyorikaisinichiji"
                            + ", maesyorisyuuryounichiji"
                            + ", maesyoritantousya"
                            + ", hihyoumensekisokuteikaisinichiji"
                            + ", hihyoumensekisokuteisyuuryounichiji"
                            + ", hihyoumensekisokuteikekka"
                            + ", (CASE WHEN hantei = 0 THEN '不合格' WHEN hantei = 1 THEN '合格' ELSE '' END) AS hantei "
                            + ", hihyoumensekisokuteitantousya "
                            + "  FROM sr_tenka_bet "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR tenkazaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR hihyoumensekisokuteikaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hihyoumensekisokuteikaisinichiji <= ?) "
                            + " ORDER BY hihyoumensekisokuteikaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                             // ﾛｯﾄNo
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                               // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                                 // 添加材ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                                       // ﾛｯﾄ区分
                    mapping.put("kansouzaranosyurui", "kansouzaranosyurui");                                   // 乾燥皿の種類
                    mapping.put("slurryjyuuryou", "slurryjyuuryou");                                           // ｽﾗﾘｰ重量
                    mapping.put("kansouondo", "kansouondo");                                                   // 乾燥温度
                    mapping.put("kansoujikankikaku", "kansoujikankikaku");                                     // 乾燥時間規格
                    mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                                   // 乾燥開始日時
                    mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                             // 乾燥終了日時
                    mapping.put("dassirogouki", "dassirogouki");                                               // 脱脂炉号機
                    mapping.put("dassiondo", "dassiondo");                                                     // 脱脂温度
                    mapping.put("dassijikankikaku", "dassijikankikaku");                                       // 脱脂時間規格
                    mapping.put("dassikaisinichiji", "dassikaisinichiji");                                     // 脱脂開始日時
                    mapping.put("dassisyuuryounichiji", "dassisyuuryounichiji");                               // 脱脂終了日時
                    mapping.put("kansoutantousya", "kansoutantousya");                                         // 乾燥担当者
                    mapping.put("sokuteisample", "sokuteisample");                                             // 測定ｻﾝﾌﾟﾙ数
                    mapping.put("samplejyuuryou", "samplejyuuryou");                                           // ｻﾝﾌﾟﾙ重量
                    mapping.put("maesyoriondo", "maesyoriondo");                                               // 前処理温度
                    mapping.put("maesyorijikan", "maesyorijikan");                                             // 前処理時間
                    mapping.put("maesyorikaisinichiji", "maesyorikaisinichiji");                               // 前処理開始日時
                    mapping.put("maesyorisyuuryounichiji", "maesyorisyuuryounichiji");                         // 前処理終了日時
                    mapping.put("maesyoritantousya", "maesyoritantousya");                                     // 前処理担当者
                    mapping.put("hihyoumensekisokuteikaisinichiji", "hihyoumensekisokuteikaisinichiji");       // 比表面積測定開始日時
                    mapping.put("hihyoumensekisokuteisyuuryounichiji", "hihyoumensekisokuteisyuuryounichiji"); // 比表面積測定終了日時
                    mapping.put("hihyoumensekisokuteikekka", "hihyoumensekisokuteikekka");                     // 比表面積測定結果
                    mapping.put("hantei", "hantei");                                                           // 判定
                    mapping.put("hihyoumensekisokuteitantousya", "hihyoumensekisokuteitantousya");             // 比表面積測定担当者
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B013Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B013Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    b013ListData = queryRunner.query(sql, beanHandler, params.toArray());
                    setB013DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }
            
        } catch (SQLException ex) {
            b007ListData = new ArrayList<>();
            b008ListData = new ArrayList<>();
            b009ListData = new ArrayList<>();
            b010ListData = new ArrayList<>();
            b011ListData = new ArrayList<>();
            b012ListData = new ArrayList<>();
            b013ListData = new ArrayList<>();
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
        if(DISPLAY_BLOCK.equals(b007DTdisplay)) {
            //工程が「ｶﾞﾗｽ」の場合
            excelRealPath = JSON_FILE_PATH_202B007;
            excelFileHeadName = GAMEN_NAME_202B007;
            outputList = b007ListData;
        }else if(DISPLAY_BLOCK.equals(b008DTdisplay)) {
            //工程が「添加材調合」の場合
            excelRealPath = JSON_FILE_PATH_202B008;
            excelFileHeadName = GAMEN_NAME_202B008;
            outputList = b008ListData;
        }else if(DISPLAY_BLOCK.equals(b009DTdisplay)) {
            //工程が「溶剤調合」の場合
            excelRealPath = JSON_FILE_PATH_202B009;
            excelFileHeadName = GAMEN_NAME_202B009;
            outputList = b009ListData;
        }else if(DISPLAY_BLOCK.equals(b010DTdisplay)) {
            //工程が「予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)」の場合
            excelRealPath = JSON_FILE_PATH_202B010;
            excelFileHeadName = GAMEN_NAME_202B010;
            outputList = b010ListData;
        }else if(DISPLAY_BLOCK.equals(b011DTdisplay)) {
            //工程が「粉砕」の場合
            excelRealPath = JSON_FILE_PATH_202B011;
            excelFileHeadName = GAMEN_NAME_202B011;
            outputList = b011ListData;
        }else if(DISPLAY_BLOCK.equals(b012DTdisplay)) {
            //工程が「FP排出」の場合
            excelRealPath = JSON_FILE_PATH_202B012;
            excelFileHeadName = GAMEN_NAME_202B012;
            outputList = b012ListData;
        }else if(DISPLAY_BLOCK.equals(b013DTdisplay)) {
            //工程が「BET」の場合
            excelRealPath = JSON_FILE_PATH_202B013;
            excelFileHeadName = GAMEN_NAME_202B013;
            outputList = b013ListData;
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
        
        //固形分測定日(FROM)
        Date paramKokeibunsokuteiDateF = null;
        if (!StringUtil.isEmpty(kokeibunsokuteiDateF)) {
            paramKokeibunsokuteiDateF = DateUtil.convertStringToDateInSeconds(getKokeibunsokuteiDateF(), "000000");
        }
        
        //固形分測定日(TO)
        Date paramKokeibunsokuteiDateT = null;
        if (!StringUtil.isEmpty(kokeibunsokuteiDateT)) {
            paramKokeibunsokuteiDateT = DateUtil.convertStringToDateInSeconds(getKokeibunsokuteiDateT(), "235959");
        }
                
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
        
        //撹拌開始日(FROM)
        Date paramKakuhankaisiDateF = null;
        if (!StringUtil.isEmpty(kakuhankaisiDateF)) {
            paramKakuhankaisiDateF = DateUtil.convertStringToDateInSeconds(getKakuhankaisiDateF(), "000000");
        }
        
        //撹拌開始日(TO)
        Date paramKakuhankaisiDateT = null;
        if (!StringUtil.isEmpty(kakuhankaisiDateT)) {
            paramKakuhankaisiDateT = DateUtil.convertStringToDateInSeconds(getKakuhankaisiDateT(), "235959");
        }
        
        //循環開始日(FROM)
        Date paramJyunkankaisiDateF = null;
        if (!StringUtil.isEmpty(jyunkankaisiDateF)) {
            paramJyunkankaisiDateF = DateUtil.convertStringToDateInSeconds(getJyunkankaisiDateF(), "000000");
        }
        
        //循環開始日(TO)
        Date paramJyunkankaisiDateT = null;
        if (!StringUtil.isEmpty(jyunkankaisiDateT)) {
            paramJyunkankaisiDateT = DateUtil.convertStringToDateInSeconds(getJyunkankaisiDateT(), "235959");
        }
        
        //F/P開始日(FROM)
        Date paramFpkaisiDateF = null;
        if (!StringUtil.isEmpty(fpkaisiDateF)) {
            paramFpkaisiDateF = DateUtil.convertStringToDateInSeconds(getFpkaisiDateF(), "000000");
        }
        
        //F/P開始日(TO)
        Date paramFpkaisiDateT = null;
        if (!StringUtil.isEmpty(fpkaisiDateT)) {
            paramFpkaisiDateT = DateUtil.convertStringToDateInSeconds(getFpkaisiDateT(), "235959");
        }
        
        //比表面積測定開始日(FROM)
        Date paramHihyoumensekisokuteikaisiDateF = null;
        if (!StringUtil.isEmpty(hihyoumensekisokuteikaisiDateF)) {
            paramHihyoumensekisokuteikaisiDateF = DateUtil.convertStringToDateInSeconds(getHihyoumensekisokuteikaisiDateF(), "000000");
        }
        
        //比表面積測定開始日(TO)
        Date paramHihyoumensekisokuteikaisiDateT = null;
        if (!StringUtil.isEmpty(hihyoumensekisokuteikaisiDateT)) {
            paramHihyoumensekisokuteikaisiDateT = DateUtil.convertStringToDateInSeconds(getHihyoumensekisokuteikaisiDateT(), "235959");
        }
                
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));
        
        if(null != cmbKotei)switch (cmbKotei) {
            case "ｶﾞﾗｽ":
                // 工程が「ｶﾞﾗｽ」の場合、Ⅲ.画面表示仕様(3)を発行する。
                params.addAll(Arrays.asList(paramKokeibunsokuteiDateF, paramKokeibunsokuteiDateF));
                params.addAll(Arrays.asList(paramKokeibunsokuteiDateT, paramKokeibunsokuteiDateT));
                break;
            case "添加材調合":
                // 工程が「添加材調合」の場合、Ⅲ.画面表示仕様(7)を発行する。
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                break;
            case "溶剤調合":
                // 工程が「溶剤調合」の場合、Ⅲ.画面表示仕様(9)を発行する。
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                break;
            case "予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)":
                // 工程が「予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)」の場合、Ⅲ.画面表示仕様(11)を発行する。
                params.addAll(Arrays.asList(paramKakuhankaisiDateF, paramKakuhankaisiDateF));
                params.addAll(Arrays.asList(paramKakuhankaisiDateT, paramKakuhankaisiDateT));
                break;
            case "粉砕":
                // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(13)を発行する。
                params.addAll(Arrays.asList(paramJyunkankaisiDateF, paramJyunkankaisiDateF));
                params.addAll(Arrays.asList(paramJyunkankaisiDateT, paramJyunkankaisiDateT));
                break;
            case "FP排出":
                // 工程が「FP排出」の場合、Ⅲ.画面表示仕様(15)を発行する。
                params.addAll(Arrays.asList(paramFpkaisiDateF, paramFpkaisiDateF));
                params.addAll(Arrays.asList(paramFpkaisiDateT, paramFpkaisiDateT));
                break;
            case "BET":
                // 工程が「BET」の場合、Ⅲ.画面表示仕様(17)を発行する。
                params.addAll(Arrays.asList(paramHihyoumensekisokuteikaisiDateF, paramHihyoumensekisokuteikaisiDateF));
                params.addAll(Arrays.asList(paramHihyoumensekisokuteikaisiDateT, paramHihyoumensekisokuteikaisiDateT));
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
        if(DISPLAY_BLOCK.equals(b007DTdisplay)) {
            //工程が「ｶﾞﾗｽ」の場合
            return !(b007ListData == null || b007ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b008DTdisplay)) {
            //工程が「添加材調合」の場合
            return !(b008ListData == null || b008ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b009DTdisplay)) {
            //工程が「溶剤調合」の場合
            return !(b009ListData == null || b009ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b010DTdisplay)) {
            //工程が「予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)」の場合
            return !(b010ListData == null || b010ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b011DTdisplay)) {
            //工程が「粉砕」の場合
            return !(b011ListData == null || b011ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b012DTdisplay)) {
            //工程が「FP排出」の場合
            return !(b012ListData == null || b012ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(b013DTdisplay)) {
            //工程が「BET」の場合
            return !(b013ListData == null || b013ListData.isEmpty());
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