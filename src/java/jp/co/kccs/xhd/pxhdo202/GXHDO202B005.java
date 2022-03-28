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
import jp.co.kccs.xhd.model.GXHDO202B019Model;
import jp.co.kccs.xhd.model.GXHDO202B020Model;
import jp.co.kccs.xhd.model.GXHDO202B021Model;
import jp.co.kccs.xhd.model.GXHDO202B022Model;
import jp.co.kccs.xhd.model.GXHDO202B023Model;
import jp.co.kccs.xhd.model.GXHDO202B024Model;
import jp.co.kccs.xhd.model.GXHDO202B025Model;
import jp.co.kccs.xhd.model.GXHDO202B026Model;
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
 * 変更日       2021/11/23<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 誘電体ｽﾗﾘｰ作製履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2021/11/23
 */
@Named
@ViewScoped
public class GXHDO202B005 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO202B005.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定 一覧表示データ */
    private List<GXHDO202B019Model> b019ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量 一覧表示データ */
    private List<GXHDO202B020Model> b020ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量 一覧表示データ */
    private List<GXHDO202B021Model> b021ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・主原料秤量 一覧表示データ */
    private List<GXHDO202B022Model> b022ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ 一覧表示データ */
    private List<GXHDO202B023Model> b023ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・粉砕 一覧表示データ */
    private List<GXHDO202B024Model> b024ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・比表面積測定 一覧表示データ */
    private List<GXHDO202B025Model> b025ListData = null;
    
    /** 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管 一覧表示データ */
    private List<GXHDO202B026Model> b026ListData = null;
    
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
    /** 検索条件：秤量号機 */
    private String goki = "";

    /** 検索条件：撹拌開始日(FROM) */
    private String kakuhankaisiDateF = "";
    /** 検索条件：撹拌開始日(TO) */
    private String kakuhankaisiDateT = "";
    /** 検索条件：溶剤秤量開始日(FROM) */
    private String youzaihyouryoukaisiDateF = "";
    /** 検索条件：溶剤秤量開始日(TO) */
    private String youzaihyouryoukaisiDateT = "";
    /** 検索条件：秤量開始日(FROM) */
    private String hyoryoDateF = "";
    /** 検索条件：秤量開始日(TO) */
    private String hyoryoDateT = "";
    /** 検索条件：主原料秤量開始日(FROM) */
    private String syugenryouhyouryoukaisiDateF = "";
    /** 検索条件：主原料秤量開始日(TO) */
    private String syugenryouhyouryoukaisiDateT = "";
    /** 検索条件：投入開始日(FROM) */
    private String tounyuukaisiDateF = "";
    /** 検索条件：投入開始日(TO) */
    private String tounyuukaisiDateT = "";
    /** 検索条件：粉砕開始日(FROM) */
    private String funsaikaisiDateF = "";
    /** 検索条件：粉砕開始日(TO) */
    private String funsaikaisiDateT = "";
    /** 検索条件：乾燥開始日(FROM) */
    private String kansoukaisiDateF = "";
    /** 検索条件：乾燥開始日(TO) */
    private String kansoukaisiDateT = "";
    /** 検索条件：F/P開始日(FROM) */
    private String fpkaisiDateF = "";
    /** 検索条件：F/P開始日(TO) */
    private String fpkaisiDateT = "";

    /** b019Listの制御 */
    private String b019DTdisplay;
    /** b020Listの制御 */
    private String b020DTdisplay;
    /** b021Listの制御 */
    private String b021DTdisplay;
    /** b022Listの制御 */
    private String b022DTdisplay;
    /** b023Listの制御 */
    private String b023DTdisplay;
    /** b024Listの制御 */
    private String b024DTdisplay;
    /** b025Listの制御 */
    private String b025DTdisplay;
    /** b026Listの制御 */
    private String b026DTdisplay;
    //スタイル設定・非表示
    private static final String DISPLAY_NONE = "none";
    //スタイル設定・表示
    private static final String DISPLAY_BLOCK = "block";
    // 工程リスト:表示ﾃﾞｰﾀ
    private static final String[] KOTEI_CMB_LIST = {"添加材ｽﾗﾘｰ固形分測定","溶剤・添加材ｽﾗﾘｰ秤量","添加材・ｿﾞﾙ秤量","主原料秤量","ﾌﾟﾚﾐｷｼﾝｸﾞ","粉砕","比表面積測定","ﾌｨﾙﾀｰﾊﾟｽ・保管"};
    //画面名称 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定
    private static final String GAMEN_NAME_202B019 = "誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定";
    //画面名称 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量
    private static final String GAMEN_NAME_202B020 = "誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量";
    //画面名称 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量
    private static final String GAMEN_NAME_202B021 = "誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量";
    //画面名称 誘電体ｽﾗﾘｰ作製・主原料秤量
    private static final String GAMEN_NAME_202B022 = "誘電体ｽﾗﾘｰ作製・主原料秤量";
    //画面名称 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ
    private static final String GAMEN_NAME_202B023 = "誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ";
    //画面名称 誘電体ｽﾗﾘｰ作製・粉砕
    private static final String GAMEN_NAME_202B024 = "誘電体ｽﾗﾘｰ作製・粉砕";
    //画面名称 誘電体ｽﾗﾘｰ作製・比表面積測定
    private static final String GAMEN_NAME_202B025 = "誘電体ｽﾗﾘｰ作製・比表面積測定";
    //画面名称 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管
    private static final String GAMEN_NAME_202B026 = "誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定
    private static final String JSON_FILE_PATH_202B019 = "/WEB-INF/classes/resources/json/gxhdo202b019.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量
    private static final String JSON_FILE_PATH_202B020 = "/WEB-INF/classes/resources/json/gxhdo202b020.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量
    private static final String JSON_FILE_PATH_202B021 = "/WEB-INF/classes/resources/json/gxhdo202b021.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・主原料秤量
    private static final String JSON_FILE_PATH_202B022 = "/WEB-INF/classes/resources/json/gxhdo202b022.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ
    private static final String JSON_FILE_PATH_202B023 = "/WEB-INF/classes/resources/json/gxhdo202b023.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・粉砕
    private static final String JSON_FILE_PATH_202B024 = "/WEB-INF/classes/resources/json/gxhdo202b024.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・比表面積測定
    private static final String JSON_FILE_PATH_202B025 = "/WEB-INF/classes/resources/json/gxhdo202b025.json";
    //エクセル出力ファイルパス 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管
    private static final String JSON_FILE_PATH_202B026 = "/WEB-INF/classes/resources/json/gxhdo202b026.json";
    /**
     * コンストラクタ
     */
    public GXHDO202B005() {
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
     * 検索条件：溶剤秤量開始日(FROM)
     * @return the youzaihyouryoukaisiDateF
     */
    public String getYouzaihyouryoukaisiDateF() {
        return youzaihyouryoukaisiDateF;
    }

    /**
     * 検索条件：溶剤秤量開始日(FROM)
     * @param youzaihyouryoukaisiDateF the youzaihyouryoukaisiDateF to set
     */
    public void setYouzaihyouryoukaisiDateF(String youzaihyouryoukaisiDateF) {
        this.youzaihyouryoukaisiDateF = youzaihyouryoukaisiDateF;
    }

    /**
     * 検索条件：溶剤秤量開始日(TO)
     * @return the youzaihyouryoukaisiDateT
     */
    public String getYouzaihyouryoukaisiDateT() {
        return youzaihyouryoukaisiDateT;
    }

    /**
     * 検索条件：溶剤秤量開始日(TO)
     * @param youzaihyouryoukaisiDateT the youzaihyouryoukaisiDateT to set
     */
    public void setYouzaihyouryoukaisiDateT(String youzaihyouryoukaisiDateT) {
        this.youzaihyouryoukaisiDateT = youzaihyouryoukaisiDateT;
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
     * 検索条件：主原料秤量開始日(FROM)
     * @return the syugenryouhyouryoukaisiDateF
     */
    public String getSyugenryouhyouryoukaisiDateF() {
        return syugenryouhyouryoukaisiDateF;
    }

    /**
     * 検索条件：主原料秤量開始日(FROM)
     * @param syugenryouhyouryoukaisiDateF the syugenryouhyouryoukaisiDateF to set
     */
    public void setSyugenryouhyouryoukaisiDateF(String syugenryouhyouryoukaisiDateF) {
        this.syugenryouhyouryoukaisiDateF = syugenryouhyouryoukaisiDateF;
    }

    /**
     * 検索条件：主原料秤量開始日(TO)
     * @return the syugenryouhyouryoukaisiDateT
     */
    public String getSyugenryouhyouryoukaisiDateT() {
        return syugenryouhyouryoukaisiDateT;
    }

    /**
     * 検索条件：主原料秤量開始日(TO)
     * @param syugenryouhyouryoukaisiDateT the syugenryouhyouryoukaisiDateT to set
     */
    public void setSyugenryouhyouryoukaisiDateT(String syugenryouhyouryoukaisiDateT) {
        this.syugenryouhyouryoukaisiDateT = syugenryouhyouryoukaisiDateT;
    }

    /**
     * 検索条件：投入開始日(FROM)
     * @return the tounyuukaisiDateF
     */
    public String getTounyuukaisiDateF() {
        return tounyuukaisiDateF;
    }

    /**
     * 検索条件：投入開始日(FROM)
     * @param tounyuukaisiDateF the tounyuukaisiDateF to set
     */
    public void setTounyuukaisiDateF(String tounyuukaisiDateF) {
        this.tounyuukaisiDateF = tounyuukaisiDateF;
    }

    /**
     * 検索条件：投入開始日(TO)
     * @return the tounyuukaisiDateT
     */
    public String getTounyuukaisiDateT() {
        return tounyuukaisiDateT;
    }

    /**
     * 検索条件：投入開始日(TO)
     * @param tounyuukaisiDateT the tounyuukaisiDateT to set
     */
    public void setTounyuukaisiDateT(String tounyuukaisiDateT) {
        this.tounyuukaisiDateT = tounyuukaisiDateT;
    }

    /**
     * 検索条件：粉砕開始日(FROM)
     * @return the funsaikaisiDateF
     */
    public String getFunsaikaisiDateF() {
        return funsaikaisiDateF;
    }

    /**
     * 検索条件：粉砕開始日(FROM)
     * @param funsaikaisiDateF the funsaikaisiDateF to set
     */
    public void setFunsaikaisiDateF(String funsaikaisiDateF) {
        this.funsaikaisiDateF = funsaikaisiDateF;
    }

    /**
     * 検索条件：粉砕開始日(TO)
     * @return the funsaikaisiDateT
     */
    public String getFunsaikaisiDateT() {
        return funsaikaisiDateT;
    }

    /**
     * 検索条件：粉砕開始日(TO)
     * @param funsaikaisiDateT the funsaikaisiDateT to set
     */
    public void setFunsaikaisiDateT(String funsaikaisiDateT) {
        this.funsaikaisiDateT = funsaikaisiDateT;
    }

    /**
     * 検索条件：乾燥開始日(FROM)
     * @return the kansoukaisiDateF
     */
    public String getKansoukaisiDateF() {
        return kansoukaisiDateF;
    }

    /**
     * 検索条件：乾燥開始日(FROM)
     * @param kansoukaisiDateF the kansoukaisiDateF to set
     */
    public void setKansoukaisiDateF(String kansoukaisiDateF) {
        this.kansoukaisiDateF = kansoukaisiDateF;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @return the kansoukaisiDateT
     */
    public String getKansoukaisiDateT() {
        return kansoukaisiDateT;
    }

    /**
     * 検索条件：乾燥開始日(TO)
     * @param kansoukaisiDateT the kansoukaisiDateT to set
     */
    public void setKansoukaisiDateT(String kansoukaisiDateT) {
        this.kansoukaisiDateT = kansoukaisiDateT;
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
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定 一覧表示データ
     * @return the b019ListData
     */
    public List<GXHDO202B019Model> getB019ListData() {
        return b019ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定 一覧表示データ
     * @param b019ListData the b019ListData to set
     */
    public void setB019ListData(List<GXHDO202B019Model> b019ListData) {
        this.b019ListData = b019ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量 一覧表示データ
     * @return the b020ListData
     */
    public List<GXHDO202B020Model> getB020ListData() {
        return b020ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量 一覧表示データ
     * @param b020ListData the b020ListData to set
     */
    public void setB020ListData(List<GXHDO202B020Model> b020ListData) {
        this.b020ListData = b020ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量 一覧表示データ
     * @return the b021ListData
     */
    public List<GXHDO202B021Model> getB021ListData() {
        return b021ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量 一覧表示データ
     * @param b021ListData the b021ListData to set
     */
    public void setB021ListData(List<GXHDO202B021Model> b021ListData) {
        this.b021ListData = b021ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量 一覧表示データ
     * @return the b022ListData
     */
    public List<GXHDO202B022Model> getB022ListData() {
        return b022ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量 一覧表示データ
     * @param b022ListData the b022ListData to set
     */
    public void setB022ListData(List<GXHDO202B022Model> b022ListData) {
        this.b022ListData = b022ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ 一覧表示データ
     * @return the b023ListData
     */
    public List<GXHDO202B023Model> getB023ListData() {
        return b023ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ 一覧表示データ
     * @param b023ListData the b023ListData to set
     */
    public void setB023ListData(List<GXHDO202B023Model> b023ListData) {
        this.b023ListData = b023ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕 一覧表示データ
     * @return the b024ListData
     */
    public List<GXHDO202B024Model> getB024ListData() {
        return b024ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕 一覧表示データ
     * @param b024ListData the b024ListData to set
     */
    public void setB024ListData(List<GXHDO202B024Model> b024ListData) {
        this.b024ListData = b024ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・比表面積測定 一覧表示データ
     * @return the b025ListData
     */
    public List<GXHDO202B025Model> getB025ListData() {
        return b025ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・比表面積測定 一覧表示データ
     * @param b025ListData the b025ListData to set
     */
    public void setB025ListData(List<GXHDO202B025Model> b025ListData) {
        this.b025ListData = b025ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管 一覧表示データ
     * @return the b026ListData
     */
    public List<GXHDO202B026Model> getB026ListData() {
        return b026ListData;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管 一覧表示データ
     * @param b026ListData the b026ListData to set
     */
    public void setB026ListData(List<GXHDO202B026Model> b026ListData) {
        this.b026ListData = b026ListData;
    }

    /**
     * b019Listの制御
     * @return the b019DTdisplay
     */
    public String getB019DTdisplay() {
        return b019DTdisplay;
    }

    /**
     * b019Listの制御
     * @param b019DTdisplay the b019DTdisplay to set
     */
    public void setB019DTdisplay(String b019DTdisplay) {
        this.b019DTdisplay = b019DTdisplay;
    }

    /**
     * b020Listの制御
     * @return the b020DTdisplay
     */
    public String getB020DTdisplay() {
        return b020DTdisplay;
    }

    /**
     * b020Listの制御
     * @param b020DTdisplay the b020DTdisplay to set
     */
    public void setB020DTdisplay(String b020DTdisplay) {
        this.b020DTdisplay = b020DTdisplay;
    }

    /**
     * b021Listの制御
     * @return the b021DTdisplay
     */
    public String getB021DTdisplay() {
        return b021DTdisplay;
    }

    /**
     * b021Listの制御
     * @param b021DTdisplay the b021DTdisplay to set
     */
    public void setB021DTdisplay(String b021DTdisplay) {
        this.b021DTdisplay = b021DTdisplay;
    }

    /**
     * b022Listの制御
     * @return the b022DTdisplay
     */
    public String getB022DTdisplay() {
        return b022DTdisplay;
    }

    /**
     * b022Listの制御
     * @param b022DTdisplay the b022DTdisplay to set
     */
    public void setB022DTdisplay(String b022DTdisplay) {
        this.b022DTdisplay = b022DTdisplay;
    }

    /**
     * b023Listの制御
     * @return the b023DTdisplay
     */
    public String getB023DTdisplay() {
        return b023DTdisplay;
    }

    /**
     * b023Listの制御
     * @param b023DTdisplay the b023DTdisplay to set
     */
    public void setB023DTdisplay(String b023DTdisplay) {
        this.b023DTdisplay = b023DTdisplay;
    }

    /**
     * b024Listの制御
     * @return the b024DTdisplay
     */
    public String getB024DTdisplay() {
        return b024DTdisplay;
    }

    /**
     * b024Listの制御
     * @param b024DTdisplay the b024DTdisplay to set
     */
    public void setB024DTdisplay(String b024DTdisplay) {
        this.b024DTdisplay = b024DTdisplay;
    }

    /**
     * b025Listの制御
     * @return the b025DTdisplay
     */
    public String getB025DTdisplay() {
        return b025DTdisplay;
    }

    /**
     * b025Listの制御
     * @param b025DTdisplay the b025DTdisplay to set
     */
    public void setB025DTdisplay(String b025DTdisplay) {
        this.b025DTdisplay = b025DTdisplay;
    }

    /**
     * b026Listの制御
     * @return the b026DTdisplay
     */
    public String getB026DTdisplay() {
        return b026DTdisplay;
    }

    /**
     * b026Listの制御
     * @param b026DTdisplay the b026DTdisplay to set
     */
    public void setB026DTdisplay(String b026DTdisplay) {
        this.b026DTdisplay = b026DTdisplay;
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
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO202B005_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO202B005_display_page_count", session));
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
        
        setKakuhankaisiDateF("");
        setKakuhankaisiDateT("");
        setYouzaihyouryoukaisiDateF("");
        setYouzaihyouryoukaisiDateT("");
        setHyoryoDateF("");
        setHyoryoDateT("");
        setSyugenryouhyouryoukaisiDateF("");
        setSyugenryouhyouryoukaisiDateT("");
        setTounyuukaisiDateF("");
        setTounyuukaisiDateT("");
        setFunsaikaisiDateF("");
        setFunsaikaisiDateT("");
        setKansoukaisiDateF("");
        setKansoukaisiDateT("");
        setFpkaisiDateF("");
        setFpkaisiDateT("");

        setB019ListData(new ArrayList<>());
        setB020ListData(new ArrayList<>());
        setB021ListData(new ArrayList<>());
        setB022ListData(new ArrayList<>());
        setB023ListData(new ArrayList<>());
        setB024ListData(new ArrayList<>());
        setB025ListData(new ArrayList<>());
        setB026ListData(new ArrayList<>());

        setB019DTdisplay(DISPLAY_NONE);
        setB020DTdisplay(DISPLAY_NONE);
        setB021DTdisplay(DISPLAY_NONE);
        setB022DTdisplay(DISPLAY_NONE);
        setB023DTdisplay(DISPLAY_NONE);
        setB024DTdisplay(DISPLAY_NONE);
        setB025DTdisplay(DISPLAY_NONE);
        setB026DTdisplay(DISPLAY_NONE);
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
        // 検索条件：溶剤秤量開始日(FROM)
        if (existError(validateUtil.checkC101(getYouzaihyouryoukaisiDateF(), "溶剤秤量開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getYouzaihyouryoukaisiDateF(), "溶剤秤量開始日(from)")) ||
            existError(validateUtil.checkC501(getYouzaihyouryoukaisiDateF(), "溶剤秤量開始日(from)"))) {
            return;
        }
        // 検索条件：溶剤秤量開始日(TO)
        if (existError(validateUtil.checkC101(getYouzaihyouryoukaisiDateT(), "溶剤秤量開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getYouzaihyouryoukaisiDateT(), "溶剤秤量開始日(to)")) ||
            existError(validateUtil.checkC501(getYouzaihyouryoukaisiDateT(), "溶剤秤量開始日(to)"))) {
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
        // 主原料秤量開始日(FROM)
        if (existError(validateUtil.checkC101(getSyugenryouhyouryoukaisiDateF(), "主原料秤量開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSyugenryouhyouryoukaisiDateF(), "主原料秤量開始日(from)")) ||
            existError(validateUtil.checkC501(getSyugenryouhyouryoukaisiDateF(), "主原料秤量開始日(from)"))) {
            return;
        }
        // 主原料秤量開始日(TO)
        if (existError(validateUtil.checkC101(getSyugenryouhyouryoukaisiDateT(), "主原料秤量開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSyugenryouhyouryoukaisiDateT(), "主原料秤量開始日(to)")) ||
            existError(validateUtil.checkC501(getSyugenryouhyouryoukaisiDateT(), "主原料秤量開始日(to)"))) {
            return;
        }
        // 投入開始日(FROM)
        if (existError(validateUtil.checkC101(getTounyuukaisiDateF(), "投入開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getTounyuukaisiDateF(), "投入開始日(from)")) ||
            existError(validateUtil.checkC501(getTounyuukaisiDateF(), "投入開始日(from)"))) {
            return;
        }
        // 投入開始日(TO)
        if (existError(validateUtil.checkC101(getTounyuukaisiDateT(), "投入開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getTounyuukaisiDateT(), "投入開始日(to)")) ||
            existError(validateUtil.checkC501(getTounyuukaisiDateT(), "投入開始日(to)"))) {
            return;
        }
        // 粉砕開始日(FROM)
        if (existError(validateUtil.checkC101(getFunsaikaisiDateF(), "粉砕開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaikaisiDateF(), "粉砕開始日(from)")) ||
            existError(validateUtil.checkC501(getFunsaikaisiDateF(), "粉砕開始日(from)"))) {
            return;
        }
        // 粉砕開始日(TO)
        if (existError(validateUtil.checkC101(getFunsaikaisiDateT(), "粉砕開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getFunsaikaisiDateT(), "粉砕開始日(to)")) ||
            existError(validateUtil.checkC501(getFunsaikaisiDateT(), "粉砕開始日(to)"))) {
            return;
        }
        // 乾燥開始日(FROM)
        if (existError(validateUtil.checkC101(getKansoukaisiDateF(), "乾燥開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaisiDateF(), "乾燥開始日(from)")) ||
            existError(validateUtil.checkC501(getKansoukaisiDateF(), "乾燥開始日(from)"))) {
            return;
        }
        // 乾燥開始日(TO)
        if (existError(validateUtil.checkC101(getKansoukaisiDateT(), "乾燥開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKansoukaisiDateT(), "乾燥開始日(to)")) ||
            existError(validateUtil.checkC501(getKansoukaisiDateT(), "乾燥開始日(to)"))) {
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
                case "添加材ｽﾗﾘｰ固形分測定":
                    // 工程が「添加材ｽﾗﾘｰ固形分測定」の場合、Ⅲ.画面表示仕様(3)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_tenkazai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji <= ?) ";
                    break;
                case "溶剤・添加材ｽﾗﾘｰ秤量":
                    // 工程が「溶剤・添加材ｽﾗﾘｰ秤量」の場合、Ⅲ.画面表示仕様(7)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaihyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR youzaihyouryoukaisinichiji <= ?) ";
                    break;
                case "添加材・ｿﾞﾙ秤量":
                    // 工程が「添加材・ｿﾞﾙ秤量」の場合、Ⅲ.画面表示仕様(9)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_sol "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) ";
                    break;
                case "主原料秤量":
                    // 工程が「主原料秤量」の場合、Ⅲ.画面表示仕様(11)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_syugenryou "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR syugenryouhyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR syugenryouhyouryoukaisinichiji <= ?) ";
                    break;
                case "ﾌﾟﾚﾐｷｼﾝｸﾞ":
                    // 工程が「ﾌﾟﾚﾐｷｼﾝｸﾞ」の場合、Ⅲ.画面表示仕様(13)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_premixing "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR tounyuukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR tounyuukaisinichiji <= ?) ";
                    break;
                case "粉砕":
                    // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(15)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_funsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaihinmei = ?) " 
                            + "   AND (? IS NULL OR kaishinichiji >= ?) "
                            + "   AND (? IS NULL OR kaishinichiji <= ?) ";
                    break;
                case "比表面積測定":
                    // 工程が「比表面積測定」の場合、Ⅲ.画面表示仕様(17)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_hihyoumensekisokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichij >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichij <= ?) ";
                    break;
                case "ﾌｨﾙﾀｰﾊﾟｽ・保管":
                    // 工程が「ﾌｨﾙﾀｰﾊﾟｽ・保管」の場合、Ⅲ.画面表示仕様(19)を発行する。
                    sql += "SELECT COUNT(lotno) AS CNT "
                            + "  FROM sr_yuudentai_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR Fpkaisinichiji >= ?) "
                            + "   AND (? IS NULL OR Fpkaisinichiji <= ?) ";
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
            setB019ListData(new ArrayList<>());
            setB020ListData(new ArrayList<>());
            setB021ListData(new ArrayList<>());
            setB022ListData(new ArrayList<>());
            setB023ListData(new ArrayList<>());
            setB024ListData(new ArrayList<>());
            setB025ListData(new ArrayList<>());
            setB026ListData(new ArrayList<>());
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        setB019DTdisplay(DISPLAY_NONE);
        setB020DTdisplay(DISPLAY_NONE);
        setB021DTdisplay(DISPLAY_NONE);
        setB022DTdisplay(DISPLAY_NONE);
        setB023DTdisplay(DISPLAY_NONE);
        setB024DTdisplay(DISPLAY_NONE);
        setB025DTdisplay(DISPLAY_NONE);
        setB026DTdisplay(DISPLAY_NONE);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "";
            if(null != cmbKotei)switch (cmbKotei) {
                case "添加材ｽﾗﾘｰ固形分測定":{
                    // 工程が「添加材ｽﾗﾘｰ固形分測定」の場合、Ⅲ.画面表示仕様(2)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", tenkazaislurryhinmei"
                            + ", tenkazaislurrylotno"
                            + ", fuutaijyuuryou1"
                            + ", fuutaijyuuryou2"
                            + ", fuutaijyuuryou3"
                            + ", soujyuuryou1"
                            + ", soujyuuryou2"
                            + ", soujyuuryou3"
                            + ", tenkazaislurryjyuuryou"
                            + ", kakuhanki"
                            + ", kaitensuu"
                            + ", kakuhanjikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", kansouzarasyurui"
                            + ", arumizarafuutaijyuuryou1"
                            + ", kansoumaeslurryjyuuryou1"
                            + ", arumizarafuutaijyuuryou2"
                            + ", kansoumaeslurryjyuuryou2"
                            + ", arumizarafuutaijyuuryou3"
                            + ", kansoumaeslurryjyuuryou3"
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
                            + ", reikyakujikan"
                            + ", kansougosoujyuuryou1"
                            + ", kansougosoujyuuryou2"
                            + ", kansougosoujyuuryou3"
                            + ", kansougosyoumijyuuryou1"
                            + ", kansougosyoumijyuuryou2"
                            + ", kansougosyoumijyuuryou3"
                            + ", kokeibunhiritu1"
                            + ", kokeibunhiritu2"
                            + ", kokeibunhiritu3"
                            + ", kokeibunhirituheikin"
                            + ", kokeibunsokuteitantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_tenkazai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji >= ?) "
                            + "   AND (? IS NULL OR kakuhankaisinichiji <= ?) "
                            + " ORDER BY kakuhankaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");          // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");            // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                            // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                            // 原料記号
                    mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");            // 添加材ｽﾗﾘｰ品名
                    mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");              // 添加材ｽﾗﾘｰLotNo
                    mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                      // 風袋重量①
                    mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                      // 風袋重量②
                    mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                      // 風袋重量③
                    mapping.put("soujyuuryou1", "soujyuuryou1");                            // 総重量①
                    mapping.put("soujyuuryou2", "soujyuuryou2");                            // 総重量②
                    mapping.put("soujyuuryou3", "soujyuuryou3");                            // 総重量③
                    mapping.put("tenkazaislurryjyuuryou", "tenkazaislurryjyuuryou");        // 添加材ｽﾗﾘｰ重量
                    mapping.put("kakuhanki", "kakuhanki");                                  // 撹拌機
                    mapping.put("kaitensuu", "kaitensuu");                                  // 回転数
                    mapping.put("kakuhanjikan", "kakuhanjikan");                            // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");              // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");        // 撹拌終了日時
                    mapping.put("kansouzarasyurui", "kansouzarasyurui");                    // 乾燥皿種類
                    mapping.put("arumizarafuutaijyuuryou1", "arumizarafuutaijyuuryou1");    // ｱﾙﾐ皿風袋重量①
                    mapping.put("kansoumaeslurryjyuuryou1", "kansoumaeslurryjyuuryou1");    // 乾燥前ｽﾗﾘｰ重量①
                    mapping.put("arumizarafuutaijyuuryou2", "arumizarafuutaijyuuryou2");    // ｱﾙﾐ皿風袋重量②
                    mapping.put("kansoumaeslurryjyuuryou2", "kansoumaeslurryjyuuryou2");    // 乾燥前ｽﾗﾘｰ重量②
                    mapping.put("arumizarafuutaijyuuryou3", "arumizarafuutaijyuuryou3");    // ｱﾙﾐ皿風袋重量③
                    mapping.put("kansoumaeslurryjyuuryou3", "kansoumaeslurryjyuuryou3");    // 乾燥前ｽﾗﾘｰ重量③
                    mapping.put("kansouki1", "kansouki1");                                  // 乾燥機①
                    mapping.put("kansouondo1", "kansouondo1");                              // 乾燥温度①
                    mapping.put("kansoujikan1", "kansoujikan1");                            // 乾燥時間①
                    mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");              // 乾燥開始日時①
                    mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");        // 乾燥終了日時①
                    mapping.put("kansouki2", "kansouki2");                                  // 乾燥機②
                    mapping.put("kansouondo2", "kansouondo2");                              // 乾燥温度②
                    mapping.put("kansoujikan2", "kansoujikan2");                            // 乾燥時間②
                    mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");              // 乾燥開始日時②
                    mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");        // 乾燥終了日時②
                    mapping.put("reikyakujikan", "reikyakujikan");                          // 冷却時間
                    mapping.put("kansougosoujyuuryou1", "kansougosoujyuuryou1");            // 乾燥後総重量①
                    mapping.put("kansougosoujyuuryou2", "kansougosoujyuuryou2");            // 乾燥後総重量②
                    mapping.put("kansougosoujyuuryou3", "kansougosoujyuuryou3");            // 乾燥後総重量③
                    mapping.put("kansougosyoumijyuuryou1", "kansougosyoumijyuuryou1");      // 乾燥後正味重量①
                    mapping.put("kansougosyoumijyuuryou2", "kansougosyoumijyuuryou2");      // 乾燥後正味重量②
                    mapping.put("kansougosyoumijyuuryou3", "kansougosyoumijyuuryou3");      // 乾燥後正味重量③
                    mapping.put("kokeibunhiritu1", "kokeibunhiritu1");                      // 固形分比率①
                    mapping.put("kokeibunhiritu2", "kokeibunhiritu2");                      // 固形分比率②
                    mapping.put("kokeibunhiritu3", "kokeibunhiritu3");                      // 固形分比率③
                    mapping.put("kokeibunhirituheikin", "kokeibunhirituheikin");            // 固形分比率平均
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");    // 固形分測定担当者
                    mapping.put("bikou1", "bikou1");                                        // 備考1
                    mapping.put("bikou2", "bikou2");                                        // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B019Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B019Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB019ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB019DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "溶剤・添加材ｽﾗﾘｰ秤量":{
                    // 工程が「溶剤・添加材ｽﾗﾘｰ秤量」の場合、Ⅲ.画面表示仕様(6)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", goki"
                            + ", youzaihyouryoukaisinichiji"
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
                            + ", youzai5_zairyouhinmei"
                            + ", youzai5_tyougouryoukikaku"
                            + ", youzai5_buzaizaikolotno1"
                            + ", youzai5_tyougouryou1"
                            + ", youzai5_buzaizaikolotno2"
                            + ", youzai5_tyougouryou2"
                            + ", youzai6_zairyouhinmei"
                            + ", youzai6_tyougouryoukikaku"
                            + ", youzai6_buzaizaikolotno1"
                            + ", youzai6_tyougouryou1"
                            + ", youzai6_buzaizaikolotno2"
                            + ", youzai6_tyougouryou2"
                            + ", youzai7_zairyouhinmei"
                            + ", youzai7_tyougouryoukikaku"
                            + ", youzai7_buzaizaikolotno1"
                            + ", youzai7_tyougouryou1"
                            + ", youzai7_buzaizaikolotno2"
                            + ", youzai7_tyougouryou2"
                            + ", youzai8_zairyouhinmei"
                            + ", youzai8_tyougouryoukikaku"
                            + ", youzai8_buzaizaikolotno1"
                            + ", youzai8_tyougouryou1"
                            + ", youzai8_buzaizaikolotno2"
                            + ", youzai8_tyougouryou2"
                            + ", youzai9_zairyouhinmei"
                            + ", youzai9_tyougouryoukikaku"
                            + ", youzai9_buzaizaikolotno1"
                            + ", youzai9_tyougouryou1"
                            + ", youzai9_buzaizaikolotno2"
                            + ", youzai9_tyougouryou2"
                            + ", youzaihyouryousyuuryounichiji"
                            + ", kakuhanki"
                            + ", kaitensuu"
                            + ", kakuhanjikan"
                            + ", kakuhankaisinichiji"
                            + ", kakuhansyuuryounichiji"
                            + ", tenkazaislurryhyouryoukaisinichiji"
                            + ", tenkazaislurry_zairyouhinmei"
                            + ", tenkazaislurry_WIPlotno"
                            + ", tenkazaislurry_tyougouryoukikaku"
                            + ", tenkazaislurry_fuutaijyuuryou1"
                            + ", tenkazaislurry_tyougouryou1"
                            + ", tenkazaislurry_fuutaijyuuryou2"
                            + ", tenkazaislurry_tyougouryou2"
                            + ", tenkazaislurryhyouryousyuuryounichiji"
                            + ", kokeibunsokuteitantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_youzai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR youzaihyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR youzaihyouryoukaisinichiji <= ?) "
                            + " ORDER BY youzaihyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                                   // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                                   // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                                     // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                                             // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                                     // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                                     // 原料記号
                    mapping.put("goki", "goki");                                                                     // 秤量号機
                    mapping.put("youzaihyouryoukaisinichiji", "youzaihyouryoukaisinichiji");                         // 溶剤秤量開始日時
                    mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                             // 分散材①_材料品名
                    mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");                     // 分散材①_調合量規格
                    mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                       // 分散材①_部材在庫No1
                    mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                               // 分散材①_調合量1
                    mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                       // 分散材①_部材在庫No2
                    mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                               // 分散材①_調合量2
                    mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                             // 分散材②_材料品名
                    mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");                     // 分散材②_調合量規格
                    mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                       // 分散材②_部材在庫No1
                    mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                               // 分散材②_調合量1
                    mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                       // 分散材②_部材在庫No2
                    mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                               // 分散材②_調合量2
                    mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                   // 溶剤①_材料品名
                    mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                           // 溶剤①_調合量規格
                    mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                             // 溶剤①_部材在庫No1
                    mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                     // 溶剤①_調合量1
                    mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                             // 溶剤①_部材在庫No2
                    mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                     // 溶剤①_調合量2
                    mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                   // 溶剤②_材料品名
                    mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                           // 溶剤②_調合量規格
                    mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                             // 溶剤②_部材在庫No1
                    mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                     // 溶剤②_調合量1
                    mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                             // 溶剤②_部材在庫No2
                    mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                     // 溶剤②_調合量2
                    mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");                                   // 溶剤③_材料品名
                    mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");                           // 溶剤③_調合量規格
                    mapping.put("youzai3_buzaizaikolotno1", "youzai3_buzaizaikolotno1");                             // 溶剤③_部材在庫No1
                    mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");                                     // 溶剤③_調合量1
                    mapping.put("youzai3_buzaizaikolotno2", "youzai3_buzaizaikolotno2");                             // 溶剤③_部材在庫No2
                    mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");                                     // 溶剤③_調合量2
                    mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");                                   // 溶剤④_材料品名
                    mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");                           // 溶剤④_調合量規格
                    mapping.put("youzai4_buzaizaikolotno1", "youzai4_buzaizaikolotno1");                             // 溶剤④_部材在庫No1
                    mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");                                     // 溶剤④_調合量1
                    mapping.put("youzai4_buzaizaikolotno2", "youzai4_buzaizaikolotno2");                             // 溶剤④_部材在庫No2
                    mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");                                     // 溶剤④_調合量2
                    mapping.put("youzai5_zairyouhinmei", "youzai5_zairyouhinmei");                                   // 溶剤⑤_材料品名
                    mapping.put("youzai5_tyougouryoukikaku", "youzai5_tyougouryoukikaku");                           // 溶剤⑤_調合量規格
                    mapping.put("youzai5_buzaizaikolotno1", "youzai5_buzaizaikolotno1");                             // 溶剤⑤_部材在庫No1
                    mapping.put("youzai5_tyougouryou1", "youzai5_tyougouryou1");                                     // 溶剤⑤_調合量1
                    mapping.put("youzai5_buzaizaikolotno2", "youzai5_buzaizaikolotno2");                             // 溶剤⑤_部材在庫No2
                    mapping.put("youzai5_tyougouryou2", "youzai5_tyougouryou2");                                     // 溶剤⑤_調合量2
                    mapping.put("youzai6_zairyouhinmei", "youzai6_zairyouhinmei");                                   // 溶剤⑥_材料品名
                    mapping.put("youzai6_tyougouryoukikaku", "youzai6_tyougouryoukikaku");                           // 溶剤⑥_調合量規格
                    mapping.put("youzai6_buzaizaikolotno1", "youzai6_buzaizaikolotno1");                             // 溶剤⑥_部材在庫No1
                    mapping.put("youzai6_tyougouryou1", "youzai6_tyougouryou1");                                     // 溶剤⑥_調合量1
                    mapping.put("youzai6_buzaizaikolotno2", "youzai6_buzaizaikolotno2");                             // 溶剤⑥_部材在庫No2
                    mapping.put("youzai6_tyougouryou2", "youzai6_tyougouryou2");                                     // 溶剤⑥_調合量2
                    mapping.put("youzai7_zairyouhinmei", "youzai7_zairyouhinmei");                                   // 溶剤⑦_材料品名
                    mapping.put("youzai7_tyougouryoukikaku", "youzai7_tyougouryoukikaku");                           // 溶剤⑦_調合量規格
                    mapping.put("youzai7_buzaizaikolotno1", "youzai7_buzaizaikolotno1");                             // 溶剤⑦_部材在庫No1
                    mapping.put("youzai7_tyougouryou1", "youzai7_tyougouryou1");                                     // 溶剤⑦_調合量1
                    mapping.put("youzai7_buzaizaikolotno2", "youzai7_buzaizaikolotno2");                             // 溶剤⑦_部材在庫No2
                    mapping.put("youzai7_tyougouryou2", "youzai7_tyougouryou2");                                     // 溶剤⑦_調合量2
                    mapping.put("youzai8_zairyouhinmei", "youzai8_zairyouhinmei");                                   // 溶剤⑧_材料品名
                    mapping.put("youzai8_tyougouryoukikaku", "youzai8_tyougouryoukikaku");                           // 溶剤⑧_調合量規格
                    mapping.put("youzai8_buzaizaikolotno1", "youzai8_buzaizaikolotno1");                             // 溶剤⑧_部材在庫No1
                    mapping.put("youzai8_tyougouryou1", "youzai8_tyougouryou1");                                     // 溶剤⑧_調合量1
                    mapping.put("youzai8_buzaizaikolotno2", "youzai8_buzaizaikolotno2");                             // 溶剤⑧_部材在庫No2
                    mapping.put("youzai8_tyougouryou2", "youzai8_tyougouryou2");                                     // 溶剤⑧_調合量2
                    mapping.put("youzai9_zairyouhinmei", "youzai9_zairyouhinmei");                                   // 溶剤⑨_材料品名
                    mapping.put("youzai9_tyougouryoukikaku", "youzai9_tyougouryoukikaku");                           // 溶剤⑨_調合量規格
                    mapping.put("youzai9_buzaizaikolotno1", "youzai9_buzaizaikolotno1");                             // 溶剤⑨_部材在庫No1
                    mapping.put("youzai9_tyougouryou1", "youzai9_tyougouryou1");                                     // 溶剤⑨_調合量1
                    mapping.put("youzai9_buzaizaikolotno2", "youzai9_buzaizaikolotno2");                             // 溶剤⑨_部材在庫No2
                    mapping.put("youzai9_tyougouryou2", "youzai9_tyougouryou2");                                     // 溶剤⑨_調合量2
                    mapping.put("youzaihyouryousyuuryounichiji", "youzaihyouryousyuuryounichiji");                   // 溶剤秤量終了日時
                    mapping.put("kakuhanki", "kakuhanki");                                                           // 撹拌機
                    mapping.put("kaitensuu", "kaitensuu");                                                           // 回転数
                    mapping.put("kakuhanjikan", "kakuhanjikan");                                                     // 撹拌時間
                    mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                       // 撹拌開始日時
                    mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                                 // 撹拌終了日時
                    mapping.put("tenkazaislurryhyouryoukaisinichiji", "tenkazaislurryhyouryoukaisinichiji");         // 添加材ｽﾗﾘｰ秤量開始日時
                    mapping.put("tenkazaislurry_zairyouhinmei", "tenkazaislurry_zairyouhinmei");                     // 添加材ｽﾗﾘｰ_材料品名
                    mapping.put("tenkazaislurry_WIPlotno", "tenkazaislurry_WIPlotno");                               // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
                    mapping.put("tenkazaislurry_tyougouryoukikaku", "tenkazaislurry_tyougouryoukikaku");             // 添加材ｽﾗﾘｰ_調合量規格
                    mapping.put("tenkazaislurry_fuutaijyuuryou1", "tenkazaislurry_fuutaijyuuryou1");                 // 添加材ｽﾗﾘｰ_風袋重量1
                    mapping.put("tenkazaislurry_tyougouryou1", "tenkazaislurry_tyougouryou1");                       // 添加材ｽﾗﾘｰ_調合量1
                    mapping.put("tenkazaislurry_fuutaijyuuryou2", "tenkazaislurry_fuutaijyuuryou2");                 // 添加材ｽﾗﾘｰ_風袋重量2
                    mapping.put("tenkazaislurry_tyougouryou2", "tenkazaislurry_tyougouryou2");                       // 添加材ｽﾗﾘｰ_調合量2
                    mapping.put("tenkazaislurryhyouryousyuuryounichiji", "tenkazaislurryhyouryousyuuryounichiji");   // 添加材ｽﾗﾘｰ秤量終了日時
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                             // 固形分測定担当者
                    mapping.put("bikou1", "bikou1");                                                                 // 備考1
                    mapping.put("bikou2", "bikou2");                                                                 // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B020Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B020Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB020ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB020DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "添加材・ｿﾞﾙ秤量":{
                    // 工程が「添加材・ｿﾞﾙ秤量」の場合、Ⅲ.画面表示仕様(8)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", goki"
                            + ", hyouryoukaisinichiji"
                            + ", tenkazai1_zairyouhinmei"
                            + ", tenkazai1_tyougouryoukikaku"
                            + ", tenkazai1_buzaizaikolotno1"
                            + ", tenkazai1_tyougouryou1"
                            + ", tenkazai1_buzaizaikolotno2"
                            + ", tenkazai1_tyougouryou2"
                            + ", tenkazai2_zairyouhinmei"
                            + ", tenkazai2_tyougouryoukikaku"
                            + ", tenkazai2_buzaizaikolotno1"
                            + ", tenkazai2_tyougouryou1"
                            + ", tenkazai2_buzaizaikolotno2"
                            + ", tenkazai2_tyougouryou2"
                            + ", tenkazai3_zairyouhinmei"
                            + ", tenkazai3_tyougouryoukikaku"
                            + ", tenkazai3_buzaizaikolotno1"
                            + ", tenkazai3_tyougouryou1"
                            + ", tenkazai3_buzaizaikolotno2"
                            + ", tenkazai3_tyougouryou2"
                            + ", tenkazai4_zairyouhinmei"
                            + ", tenkazai4_tyougouryoukikaku"
                            + ", tenkazai4_buzaizaikolotno1"
                            + ", tenkazai4_tyougouryou1"
                            + ", tenkazai4_buzaizaikolotno2"
                            + ", tenkazai4_tyougouryou2"
                            + ", tenkazai5_zairyouhinmei"
                            + ", tenkazai5_tyougouryoukikaku"
                            + ", tenkazai5_buzaizaikolotno1"
                            + ", tenkazai5_tyougouryou1"
                            + ", tenkazai5_buzaizaikolotno2"
                            + ", tenkazai5_tyougouryou2"
                            + ", sol1_zairyouhinmei"
                            + ", sol1_tyougouryoukikaku"
                            + ", sol1_buzaizaikolotno1"
                            + ", sol1_tyougouryou1"
                            + ", sol1_buzaizaikolotno2"
                            + ", sol1_tyougouryou2"
                            + ", sol2_zairyouhinmei"
                            + ", sol2_tyougouryoukikaku"
                            + ", sol2_buzaizaikolotno1"
                            + ", sol2_tyougouryou1"
                            + ", sol2_buzaizaikolotno2"
                            + ", sol2_tyougouryou2"
                            + ", sol3_zairyouhinmei"
                            + ", sol3_tyougouryoukikaku"
                            + ", sol3_buzaizaikolotno1"
                            + ", sol3_tyougouryou1"
                            + ", sol3_buzaizaikolotno2"
                            + ", sol3_tyougouryou2"
                            + ", sol4_zairyouhinmei"
                            + ", sol4_tyougouryoukikaku"
                            + ", sol4_buzaizaikolotno1"
                            + ", sol4_tyougouryou1"
                            + ", sol4_buzaizaikolotno2"
                            + ", sol4_tyougouryou2"
                            + ", hyouryousyuuryounichiji"
                            + ", kokeibunsokuteitantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_sol "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR hyouryoukaisinichiji <= ?) "
                            + " ORDER BY hyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                  // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                          // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                  // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                  // 原料記号
                    mapping.put("goki", "goki");                                                  // 秤量号機
                    mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                  // 秤量開始日時
                    mapping.put("tenkazai1_zairyouhinmei", "tenkazai1_zairyouhinmei");            // 添加材①_材料品名
                    mapping.put("tenkazai1_tyougouryoukikaku", "tenkazai1_tyougouryoukikaku");    // 添加材①_調合量規格
                    mapping.put("tenkazai1_buzaizaikolotno1", "tenkazai1_buzaizaikolotno1");      // 添加材①_部材在庫No1
                    mapping.put("tenkazai1_tyougouryou1", "tenkazai1_tyougouryou1");              // 添加材①_調合量1
                    mapping.put("tenkazai1_buzaizaikolotno2", "tenkazai1_buzaizaikolotno2");      // 添加材①_部材在庫No2
                    mapping.put("tenkazai1_tyougouryou2", "tenkazai1_tyougouryou2");              // 添加材①_調合量2
                    mapping.put("tenkazai2_zairyouhinmei", "tenkazai2_zairyouhinmei");            // 添加材②_材料品名
                    mapping.put("tenkazai2_tyougouryoukikaku", "tenkazai2_tyougouryoukikaku");    // 添加材②_調合量規格
                    mapping.put("tenkazai2_buzaizaikolotno1", "tenkazai2_buzaizaikolotno1");      // 添加材②_部材在庫No1
                    mapping.put("tenkazai2_tyougouryou1", "tenkazai2_tyougouryou1");              // 添加材②_調合量1
                    mapping.put("tenkazai2_buzaizaikolotno2", "tenkazai2_buzaizaikolotno2");      // 添加材②_部材在庫No2
                    mapping.put("tenkazai2_tyougouryou2", "tenkazai2_tyougouryou2");              // 添加材②_調合量2
                    mapping.put("tenkazai3_zairyouhinmei", "tenkazai3_zairyouhinmei");            // 添加材③_材料品名
                    mapping.put("tenkazai3_tyougouryoukikaku", "tenkazai3_tyougouryoukikaku");    // 添加材③_調合量規格
                    mapping.put("tenkazai3_buzaizaikolotno1", "tenkazai3_buzaizaikolotno1");      // 添加材③_部材在庫No1
                    mapping.put("tenkazai3_tyougouryou1", "tenkazai3_tyougouryou1");              // 添加材③_調合量1
                    mapping.put("tenkazai3_buzaizaikolotno2", "tenkazai3_buzaizaikolotno2");      // 添加材③_部材在庫No2
                    mapping.put("tenkazai3_tyougouryou2", "tenkazai3_tyougouryou2");              // 添加材③_調合量2
                    mapping.put("tenkazai4_zairyouhinmei", "tenkazai4_zairyouhinmei");            // 添加材④_材料品名
                    mapping.put("tenkazai4_tyougouryoukikaku", "tenkazai4_tyougouryoukikaku");    // 添加材④_調合量規格
                    mapping.put("tenkazai4_buzaizaikolotno1", "tenkazai4_buzaizaikolotno1");      // 添加材④_部材在庫No1
                    mapping.put("tenkazai4_tyougouryou1", "tenkazai4_tyougouryou1");              // 添加材④_調合量1
                    mapping.put("tenkazai4_buzaizaikolotno2", "tenkazai4_buzaizaikolotno2");      // 添加材④_部材在庫No2
                    mapping.put("tenkazai4_tyougouryou2", "tenkazai4_tyougouryou2");              // 添加材④_調合量2
                    mapping.put("tenkazai5_zairyouhinmei", "tenkazai5_zairyouhinmei");            // 添加材⑤_材料品名
                    mapping.put("tenkazai5_tyougouryoukikaku", "tenkazai5_tyougouryoukikaku");    // 添加材⑤_調合量規格
                    mapping.put("tenkazai5_buzaizaikolotno1", "tenkazai5_buzaizaikolotno1");      // 添加材⑤_部材在庫No1
                    mapping.put("tenkazai5_tyougouryou1", "tenkazai5_tyougouryou1");              // 添加材⑤_調合量1
                    mapping.put("tenkazai5_buzaizaikolotno2", "tenkazai5_buzaizaikolotno2");      // 添加材⑤_部材在庫No2
                    mapping.put("tenkazai5_tyougouryou2", "tenkazai5_tyougouryou2");              // 添加材⑤_調合量2
                    mapping.put("sol1_zairyouhinmei", "sol1_zairyouhinmei");                      // ｿﾞﾙ①_材料品名
                    mapping.put("sol1_tyougouryoukikaku", "sol1_tyougouryoukikaku");              // ｿﾞﾙ①_調合量規格
                    mapping.put("sol1_buzaizaikolotno1", "sol1_buzaizaikolotno1");                // ｿﾞﾙ①_部材在庫No1
                    mapping.put("sol1_tyougouryou1", "sol1_tyougouryou1");                        // ｿﾞﾙ①_調合量1
                    mapping.put("sol1_buzaizaikolotno2", "sol1_buzaizaikolotno2");                // ｿﾞﾙ①_部材在庫No2
                    mapping.put("sol1_tyougouryou2", "sol1_tyougouryou2");                        // ｿﾞﾙ①_調合量2
                    mapping.put("sol2_zairyouhinmei", "sol2_zairyouhinmei");                      // ｿﾞﾙ②_材料品名
                    mapping.put("sol2_tyougouryoukikaku", "sol2_tyougouryoukikaku");              // ｿﾞﾙ②_調合量規格
                    mapping.put("sol2_buzaizaikolotno1", "sol2_buzaizaikolotno1");                // ｿﾞﾙ②_部材在庫No1
                    mapping.put("sol2_tyougouryou1", "sol2_tyougouryou1");                        // ｿﾞﾙ②_調合量1
                    mapping.put("sol2_buzaizaikolotno2", "sol2_buzaizaikolotno2");                // ｿﾞﾙ②_部材在庫No2
                    mapping.put("sol2_tyougouryou2", "sol2_tyougouryou2");                        // ｿﾞﾙ②_調合量2
                    mapping.put("sol3_zairyouhinmei", "sol3_zairyouhinmei");                      // ｿﾞﾙ③_材料品名
                    mapping.put("sol3_tyougouryoukikaku", "sol3_tyougouryoukikaku");              // ｿﾞﾙ③_調合量規格
                    mapping.put("sol3_buzaizaikolotno1", "sol3_buzaizaikolotno1");                // ｿﾞﾙ③_部材在庫No1
                    mapping.put("sol3_tyougouryou1", "sol3_tyougouryou1");                        // ｿﾞﾙ③_調合量1
                    mapping.put("sol3_buzaizaikolotno2", "sol3_buzaizaikolotno2");                // ｿﾞﾙ③_部材在庫No2
                    mapping.put("sol3_tyougouryou2", "sol3_tyougouryou2");                        // ｿﾞﾙ③_調合量2
                    mapping.put("sol4_zairyouhinmei", "sol4_zairyouhinmei");                      // ｿﾞﾙ④_材料品名
                    mapping.put("sol4_tyougouryoukikaku", "sol4_tyougouryoukikaku");              // ｿﾞﾙ④_調合量規格
                    mapping.put("sol4_buzaizaikolotno1", "sol4_buzaizaikolotno1");                // ｿﾞﾙ④_部材在庫No1
                    mapping.put("sol4_tyougouryou1", "sol4_tyougouryou1");                        // ｿﾞﾙ④_調合量1
                    mapping.put("sol4_buzaizaikolotno2", "sol4_buzaizaikolotno2");                // ｿﾞﾙ④_部材在庫No2
                    mapping.put("sol4_tyougouryou2", "sol4_tyougouryou2");                        // ｿﾞﾙ④_調合量2
                    mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");            // 秤量終了日時
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");          // 固形分測定担当者
                    mapping.put("bikou1", "bikou1");                                              // 備考1
                    mapping.put("bikou2", "bikou2");                                              // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B021Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B021Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB021ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB021DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "主原料秤量":{
                    // 工程が「主原料秤量」の場合、Ⅲ.画面表示仕様(10)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", goki"
                            + ", syugenryouhyouryoukaisinichiji"
                            + ", syugenryou1_zairyouhinmei"
                            + ", syugenryou1_tyougouryoukikaku"
                            + ", syugenryou1_buzaizaikolotno"
                            + ", syugenryou1_tyougouryou1"
                            + ", syugenryou1_tyougouryou2"
                            + ", syugenryou1_tyougouryou3"
                            + ", syugenryou1_tyougouryou4"
                            + ", syugenryou1_tyougouryou5"
                            + ", syugenryou1_tyougouryou6"
                            + ", syugenryou1_tyougouryou7"
                            + ", syugenryou2_zairyouhinmei"
                            + ", syugenryou2_tyougouryoukikaku"
                            + ", syugenryou2_buzaizaikolotno"
                            + ", syugenryou2_tyougouryou1"
                            + ", syugenryou2_tyougouryou2"
                            + ", syugenryou2_tyougouryou3"
                            + ", syugenryou3_zairyouhinmei"
                            + ", syugenryou3_tyougouryoukikaku"
                            + ", syugenryou3_buzaizaikolotno"
                            + ", syugenryou3_tyougouryou1"
                            + ", syugenryou3_tyougouryou2"
                            + ", syugenryou3_tyougouryou3"
                            + ", syugenryou4_zairyouhinmei"
                            + ", syugenryou4_tyougouryoukikaku"
                            + ", syugenryou4_buzaizaikolotno"
                            + ", syugenryou4_tyougouryou1"
                            + ", syugenryou4_tyougouryou2"
                            + ", syugenryou5_zairyouhinmei"
                            + ", syugenryou5_tyougouryoukikaku"
                            + ", syugenryou5_buzaizaikolotno"
                            + ", syugenryou5_tyougouryou1"
                            + ", syugenryou5_tyougouryou2"
                            + ", syugenryou6_zairyouhinmei"
                            + ", syugenryou6_tyougouryoukikaku"
                            + ", syugenryou6_buzaizaikolotno"
                            + ", syugenryou6_tyougouryou1"
                            + ", tyougouryougoukei"
                            + ", syugenryouhyouryousyuuryounichiji"
                            + ", kokeibunsokuteitantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_syugenryou "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR goki = ?) "
                            + "   AND (? IS NULL OR syugenryouhyouryoukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR syugenryouhyouryoukaisinichiji <= ?) "
                            + " ORDER BY syugenryouhyouryoukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                           // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                           // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                             // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                                     // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                             // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                             // 原料記号
                    mapping.put("goki", "goki");                                                             // 秤量号機
                    mapping.put("syugenryouhyouryoukaisinichiji", "syugenryouhyouryoukaisinichiji");         // 主原料秤量開始日時
                    mapping.put("syugenryou1_zairyouhinmei", "syugenryou1_zairyouhinmei");                   // 主原料①_材料品名
                    mapping.put("syugenryou1_tyougouryoukikaku", "syugenryou1_tyougouryoukikaku");           // 主原料①_調合量規格
                    mapping.put("syugenryou1_buzaizaikolotno", "syugenryou1_buzaizaikolotno");               // 主原料①_部材在庫No
                    mapping.put("syugenryou1_tyougouryou1", "syugenryou1_tyougouryou1");                     // 主原料①_調合量1
                    mapping.put("syugenryou1_tyougouryou2", "syugenryou1_tyougouryou2");                     // 主原料①_調合量2
                    mapping.put("syugenryou1_tyougouryou3", "syugenryou1_tyougouryou3");                     // 主原料①_調合量3
                    mapping.put("syugenryou1_tyougouryou4", "syugenryou1_tyougouryou4");                     // 主原料①_調合量4
                    mapping.put("syugenryou1_tyougouryou5", "syugenryou1_tyougouryou5");                     // 主原料①_調合量5
                    mapping.put("syugenryou1_tyougouryou6", "syugenryou1_tyougouryou6");                     // 主原料①_調合量6
                    mapping.put("syugenryou1_tyougouryou7", "syugenryou1_tyougouryou7");                     // 主原料①_調合量7
                    mapping.put("syugenryou2_zairyouhinmei", "syugenryou2_zairyouhinmei");                   // 主原料②_材料品名
                    mapping.put("syugenryou2_tyougouryoukikaku", "syugenryou2_tyougouryoukikaku");           // 主原料②_調合量規格
                    mapping.put("syugenryou2_buzaizaikolotno", "syugenryou2_buzaizaikolotno");               // 主原料②_部材在庫No
                    mapping.put("syugenryou2_tyougouryou1", "syugenryou2_tyougouryou1");                     // 主原料②_調合量1
                    mapping.put("syugenryou2_tyougouryou2", "syugenryou2_tyougouryou2");                     // 主原料②_調合量2
                    mapping.put("syugenryou2_tyougouryou3", "syugenryou2_tyougouryou3");                     // 主原料②_調合量3
                    mapping.put("syugenryou3_zairyouhinmei", "syugenryou3_zairyouhinmei");                   // 主原料③_材料品名
                    mapping.put("syugenryou3_tyougouryoukikaku", "syugenryou3_tyougouryoukikaku");           // 主原料③_調合量規格
                    mapping.put("syugenryou3_buzaizaikolotno", "syugenryou3_buzaizaikolotno");               // 主原料③_部材在庫No
                    mapping.put("syugenryou3_tyougouryou1", "syugenryou3_tyougouryou1");                     // 主原料③_調合量1
                    mapping.put("syugenryou3_tyougouryou2", "syugenryou3_tyougouryou2");                     // 主原料③_調合量2
                    mapping.put("syugenryou3_tyougouryou3", "syugenryou3_tyougouryou3");                     // 主原料③_調合量3
                    mapping.put("syugenryou4_zairyouhinmei", "syugenryou4_zairyouhinmei");                   // 主原料④_材料品名
                    mapping.put("syugenryou4_tyougouryoukikaku", "syugenryou4_tyougouryoukikaku");           // 主原料④_調合量規格
                    mapping.put("syugenryou4_buzaizaikolotno", "syugenryou4_buzaizaikolotno");               // 主原料④_部材在庫No
                    mapping.put("syugenryou4_tyougouryou1", "syugenryou4_tyougouryou1");                     // 主原料④_調合量1
                    mapping.put("syugenryou4_tyougouryou2", "syugenryou4_tyougouryou2");                     // 主原料④_調合量2
                    mapping.put("syugenryou5_zairyouhinmei", "syugenryou5_zairyouhinmei");                   // 主原料⑤_材料品名
                    mapping.put("syugenryou5_tyougouryoukikaku", "syugenryou5_tyougouryoukikaku");           // 主原料⑤_調合量規格
                    mapping.put("syugenryou5_buzaizaikolotno", "syugenryou5_buzaizaikolotno");               // 主原料⑤_部材在庫No
                    mapping.put("syugenryou5_tyougouryou1", "syugenryou5_tyougouryou1");                     // 主原料⑤_調合量1
                    mapping.put("syugenryou5_tyougouryou2", "syugenryou5_tyougouryou2");                     // 主原料⑤_調合量2
                    mapping.put("syugenryou6_zairyouhinmei", "syugenryou6_zairyouhinmei");                   // 主原料⑥_材料品名
                    mapping.put("syugenryou6_tyougouryoukikaku", "syugenryou6_tyougouryoukikaku");           // 主原料⑥_調合量規格
                    mapping.put("syugenryou6_buzaizaikolotno", "syugenryou6_buzaizaikolotno");               // 主原料⑥_部材在庫No
                    mapping.put("syugenryou6_tyougouryou1", "syugenryou6_tyougouryou1");                     // 主原料⑥_調合量1
                    mapping.put("tyougouryougoukei", "tyougouryougoukei");                                   // 調合量合計
                    mapping.put("syugenryouhyouryousyuuryounichiji", "syugenryouhyouryousyuuryounichiji");   // 主原料秤量終了日時
                    mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                     // 固形分測定担当者
                    mapping.put("bikou1", "bikou1");                                                         // 備考1
                    mapping.put("bikou2", "bikou2");                                                         // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B022Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B022Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB022ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB022DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾌﾟﾚﾐｷｼﾝｸﾞ":{
                    // 工程が「ﾌﾟﾚﾐｷｼﾝｸﾞ」の場合、Ⅲ.画面表示仕様(12)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", youzaisenjyou_kakuhanki"
                            + ", youzaisenjyou_siyoutank"
                            + ", youzairyou"
                            + ", (CASE WHEN youzaitounyuu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS youzaitounyuu"
                            + ", syuziku"
                            + ", pump"
                            + ", senjyoujyouken_dispakaitensuu"
                            + ", separate"
                            + ", agitator"
                            + ", (CASE WHEN jidouunten = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS jidouunten"
                            + ", (CASE WHEN tankABjyunkan = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS tankABjyunkan"
                            + ", passsuu"
                            + ", senjyoujikan"
                            + ", raptime"
                            + ", premixing_kakuhanki"
                            + ", premixing_siyoutank"
                            + ", (CASE WHEN earthgripsetuzokukakunin = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS earthgripsetuzokukakunin"
                            + ", premixing_dispakaitensuu"
                            + ", tounyuukaisinichiji"
                            + ", tounyuu1"
                            + ", tounyuu2"
                            + ", kakuhanjikankikaku1"
                            + ", kakuhankaisinichiji1"
                            + ", kakuhansyuuryounichiji1"
                            + ", tounyuu3"
                            + ", kakuhanjikankikaku2"
                            + ", kakuhankaisinichiji2"
                            + ", kakuhansyuuryounichiji2"
                            + ", tounyuu4"
                            + ", kakuhanjikankikaku3"
                            + ", kakuhankaisinichiji3"
                            + ", kakuhansyuuryounichiji3"
                            + ", tounyuu5"
                            + ", kakuhanjikankikaku4"
                            + ", kakuhankaisinichiji4"
                            + ", kakuhansyuuryounichiji4"
                            + ", tounyuu6"
                            + ", kakuhanjikankikaku5"
                            + ", kakuhankaisinichiji5"
                            + ", kakuhansyuuryounichiji5"
                            + ", kaitensuuhenkou"
                            + ", tounyuu7"
                            + ", tounyuu8"
                            + ", tounyuu9"
                            + ", kakuhanjikankikaku6"
                            + ", kakuhankaisinichiji6"
                            + ", kakuhansyuuryounichiji6"
                            + ", tounyuu10"
                            + ", tounyuu11"
                            + ", tounyuu12"
                            + ", tounyuu13"
                            + ", tounyuu14"
                            + ", tounyuu15"
                            + ", tounyuu16"
                            + ", tounyuu17"
                            + ", tounyuu18"
                            + ", tounyuu19"
                            + ", tounyuusyuuryounichiji"
                            + ", kakuhanki"
                            + ", kaitensuu"
                            + ", kakuhanjikankikaku7"
                            + ", kakuhankaisinichiji7"
                            + ", (CASE WHEN kaitentaihenosessyokunokakunin = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS kaitentaihenosessyokunokakunin"
                            + ", kakuhansyuuryounichiji7"
                            + ", tantousya"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_premixing "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR tounyuukaisinichiji >= ?) "
                            + "   AND (? IS NULL OR tounyuukaisinichiji <= ?) "
                            + " ORDER BY tounyuukaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                     // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                     // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                       // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                               // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                       // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                       // 原料記号
                    mapping.put("youzaisenjyou_kakuhanki", "youzaisenjyou_kakuhanki");                 // 溶剤洗浄_撹拌機
                    mapping.put("youzaisenjyou_siyoutank", "youzaisenjyou_siyoutank");                 // 溶剤洗浄_使用ﾀﾝｸ
                    mapping.put("youzairyou", "youzairyou");                                           // 溶剤量
                    mapping.put("youzaitounyuu", "youzaitounyuu");                                     // 溶剤投入
                    mapping.put("syuziku", "syuziku");                                                 // 主軸
                    mapping.put("pump", "pump");                                                       // ﾎﾟﾝﾌﾟ
                    mapping.put("senjyoujyouken_dispakaitensuu", "senjyoujyouken_dispakaitensuu");     // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
                    mapping.put("separate", "separate");                                               // ｾﾊﾟﾚｰﾀ
                    mapping.put("agitator", "agitator");                                               // ｱｼﾞﾃｰﾀ
                    mapping.put("jidouunten", "jidouunten");                                           // 自動運転
                    mapping.put("tankABjyunkan", "tankABjyunkan");                                     // ﾀﾝｸAB循環
                    mapping.put("passsuu", "passsuu");                                                 // ﾊﾟｽ数
                    mapping.put("senjyoujikan", "senjyoujikan");                                       // 洗浄時間
                    mapping.put("raptime", "raptime");                                                 // ﾗｯﾌﾟﾀｲﾑ
                    mapping.put("premixing_kakuhanki", "premixing_kakuhanki");                         // ﾌﾟﾚﾐｷ_撹拌機
                    mapping.put("premixing_siyoutank", "premixing_siyoutank");                         // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
                    mapping.put("earthgripsetuzokukakunin", "earthgripsetuzokukakunin");               // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
                    mapping.put("premixing_dispakaitensuu", "premixing_dispakaitensuu");               // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
                    mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");                         // 投入開始日時
                    mapping.put("tounyuu1", "tounyuu1");                                               // 投入①
                    mapping.put("tounyuu2", "tounyuu2");                                               // 投入②
                    mapping.put("kakuhanjikankikaku1", "kakuhanjikankikaku1");                         // 撹拌時間規格①
                    mapping.put("kakuhankaisinichiji1", "kakuhankaisinichiji1");                       // 撹拌開始日時①
                    mapping.put("kakuhansyuuryounichiji1", "kakuhansyuuryounichiji1");                 // 撹拌終了日時①
                    mapping.put("tounyuu3", "tounyuu3");                                               // 投入③
                    mapping.put("kakuhanjikankikaku2", "kakuhanjikankikaku2");                         // 撹拌時間規格②
                    mapping.put("kakuhankaisinichiji2", "kakuhankaisinichiji2");                       // 撹拌開始日時②
                    mapping.put("kakuhansyuuryounichiji2", "kakuhansyuuryounichiji2");                 // 撹拌終了日時②
                    mapping.put("tounyuu4", "tounyuu4");                                               // 投入④
                    mapping.put("kakuhanjikankikaku3", "kakuhanjikankikaku3");                         // 撹拌時間規格③
                    mapping.put("kakuhankaisinichiji3", "kakuhankaisinichiji3");                       // 撹拌開始日時③
                    mapping.put("kakuhansyuuryounichiji3", "kakuhansyuuryounichiji3");                 // 撹拌終了日時③
                    mapping.put("tounyuu5", "tounyuu5");                                               // 投入⑤
                    mapping.put("kakuhanjikankikaku4", "kakuhanjikankikaku4");                         // 撹拌時間規格④
                    mapping.put("kakuhankaisinichiji4", "kakuhankaisinichiji4");                       // 撹拌開始日時④
                    mapping.put("kakuhansyuuryounichiji4", "kakuhansyuuryounichiji4");                 // 撹拌終了日時④
                    mapping.put("tounyuu6", "tounyuu6");                                               // 投入⑥
                    mapping.put("kakuhanjikankikaku5", "kakuhanjikankikaku5");                         // 撹拌時間規格⑤
                    mapping.put("kakuhankaisinichiji5", "kakuhankaisinichiji5");                       // 撹拌開始日時⑤
                    mapping.put("kakuhansyuuryounichiji5", "kakuhansyuuryounichiji5");                 // 撹拌終了日時⑤
                    mapping.put("kaitensuuhenkou", "kaitensuuhenkou");                                 // 回転数変更
                    mapping.put("tounyuu7", "tounyuu7");                                               // 投入⑦
                    mapping.put("tounyuu8", "tounyuu8");                                               // 投入⑧
                    mapping.put("tounyuu9", "tounyuu9");                                               // 投入⑨
                    mapping.put("kakuhanjikankikaku6", "kakuhanjikankikaku6");                         // 撹拌時間規格⑥
                    mapping.put("kakuhankaisinichiji6", "kakuhankaisinichiji6");                       // 撹拌開始日時⑥
                    mapping.put("kakuhansyuuryounichiji6", "kakuhansyuuryounichiji6");                 // 撹拌終了日時⑥
                    mapping.put("tounyuu10", "tounyuu10");                                             // 投入⑩
                    mapping.put("tounyuu11", "tounyuu11");                                             // 投入⑪
                    mapping.put("tounyuu12", "tounyuu12");                                             // 投入⑫
                    mapping.put("tounyuu13", "tounyuu13");                                             // 投入⑬
                    mapping.put("tounyuu14", "tounyuu14");                                             // 投入⑭
                    mapping.put("tounyuu15", "tounyuu15");                                             // 投入⑮
                    mapping.put("tounyuu16", "tounyuu16");                                             // 投入⑯
                    mapping.put("tounyuu17", "tounyuu17");                                             // 投入⑰
                    mapping.put("tounyuu18", "tounyuu18");                                             // 投入⑱
                    mapping.put("tounyuu19", "tounyuu19");                                             // 投入⑲
                    mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");                   // 投入終了日時
                    mapping.put("kakuhanki", "kakuhanki");                                             // 撹拌機
                    mapping.put("kaitensuu", "kaitensuu");                                             // 回転数
                    mapping.put("kakuhanjikankikaku7", "kakuhanjikankikaku7");                         // 撹拌時間規格⑦
                    mapping.put("kakuhankaisinichiji7", "kakuhankaisinichiji7");                       // 撹拌開始日時⑦
                    mapping.put("kaitentaihenosessyokunokakunin", "kaitentaihenosessyokunokakunin");   // 回転体への接触の確認
                    mapping.put("kakuhansyuuryounichiji7", "kakuhansyuuryounichiji7");                 // 撹拌終了日時⑦
                    mapping.put("tantousya", "tantousya");                                             // 担当者
                    mapping.put("bikou1", "bikou1");                                                   // 備考1
                    mapping.put("bikou2", "bikou2");                                                   // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B023Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B023Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB023ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB023DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "粉砕":{
                    // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(14)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaihinmei"
                            + ", yuudentailotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", funsaiki"
                            + ", funsaikisenjyou1"
                            + ", funsaikisenjyou2"
                            + ", renzokuunten1"
                            + ", renzokuunten2"
                            + ", gyokusekijyuryou"
                            + ", gyokusekilot"
                            + ", gyokusekimediakei"
                            + ", tounyuuryou"
                            + ", zikanpass"
                            + ", screen"
                            + ", kaitensuudisp"
                            + ", kaitensuusyujiku"
                            + ", pompsyutsuryoku"
                            + ", ryuuryou"
                            + ", passkaisuu"
                            + ", kaishinichiji"
                            + ", syuuryounichiji "
                            + "  FROM sr_yuudentai_funsai "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaihinmei = ?) " 
                            + "   AND (? IS NULL OR kaishinichiji >= ?) "
                            + "   AND (? IS NULL OR kaishinichiji <= ?) "
                            + " ORDER BY kaishinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                         // ﾛｯﾄNo
                    mapping.put("yuudentaihinmei", "yuudentaihinmei");     // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentailotno", "yuudentailotno");       // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                   // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");           // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");           // 原料記号
                    mapping.put("funsaiki", "funsaiki");                   // 粉砕機
                    mapping.put("funsaikisenjyou1", "funsaikisenjyou1");   // 粉砕機洗浄①
                    mapping.put("funsaikisenjyou2", "funsaikisenjyou2");   // 粉砕機洗浄②
                    mapping.put("renzokuunten1", "renzokuunten1");         // 連続運転回数①
                    mapping.put("renzokuunten2", "renzokuunten2");         // 連続運転回数②
                    mapping.put("gyokusekijyuryou", "gyokusekijyuryou");   // 玉石_重量
                    mapping.put("gyokusekilot", "gyokusekilot");           // 玉石_ﾛｯﾄ
                    mapping.put("gyokusekimediakei", "gyokusekimediakei"); // 玉石_ﾒﾃﾞｨｱ径
                    mapping.put("tounyuuryou", "tounyuuryou");             // 投入量
                    mapping.put("zikanpass", "zikanpass");                 // 時間/ﾊﾟｽ回数
                    mapping.put("screen", "screen");                       // ｽｸﾘｰﾝ
                    mapping.put("kaitensuudisp", "kaitensuudisp");         // 回転数_ﾃﾞｨｽﾊﾟ
                    mapping.put("kaitensuusyujiku", "kaitensuusyujiku");   // 回転数_主軸
                    mapping.put("pompsyutsuryoku", "pompsyutsuryoku");     // ﾎﾟﾝﾌﾟ出力
                    mapping.put("ryuuryou", "ryuuryou");                   // 流量
                    mapping.put("passkaisuu", "passkaisuu");               // ﾊﾟｽ回数
                    mapping.put("kaishinichiji", "kaishinichiji");         // 開始日時
                    mapping.put("syuuryounichiji", "syuuryounichiji");     // 終了日時
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B024Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B024Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB024ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB024DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "比表面積測定":{
                    // 工程が「比表面積測定」の場合、Ⅲ.画面表示仕様(16)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", kansouzaranosyurui"
                            + ", rutubono_monitor"
                            + ", rutubono_seihin"
                            + ", slurryjyuuryou_monitor"
                            + ", slurryjyuuryou_seihin"
                            + ", kansouondo"
                            + ", kansoujikankikaku"
                            + ", kansoukaisinichij"
                            + ", kansousyuuryounichiji"
                            + ", dassirogouki"
                            + ", dassiondo"
                            + ", dassijikankikaku"
                            + ", dassikaisinichiji"
                            + ", dassisyuuryounichiji"
                            + ", kansoutantousya"
                            + ", maesyori"
                            + ", maesyoriondo"
                            + ", maesyorijikan"
                            + ", maesyorikaisinichiji"
                            + ", maesyorisyuuryounichiji"
                            + ", maesyoritantousya"
                            + ", hihyoumensekisokuteikaisinichiji"
                            + ", hihyoumensekisokuteisyuuryounichiji"
                            + ", monitorjissokuti"
                            + ", monitorhoseiti"
                            + ", hihyoumensekikikaku"
                            + ", hihyoumensekineraiti"
                            + ", hihyoumensekisankouti"
                            + ", hihyoumensekisokuteikekka1"
                            + ", hihyoumensekisokuteikekka2"
                            + ", hihyoumensekisokuteikekka3"
                            + ", hihyoumensekisokuteikekka"
                            + ", SSAhikikaku AS ssahikikaku"
                            + ", SSAhisankouti AS ssahisankouti"
                            + ", SSAhi AS ssahi"
                            + ", hihyoumensekisokuteitantousya"
                            + ", bikou1"
                            + ", bikou2"
                            + "  FROM sr_yuudentai_hihyoumensekisokutei "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR kansoukaisinichij >= ?) "
                            + "   AND (? IS NULL OR kansoukaisinichij <= ?) "
                            + " ORDER BY kansoukaisinichij ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                              // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                              // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                                // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                                        // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                                // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                                // 原料記号
                    mapping.put("kansouzaranosyurui", "kansouzaranosyurui");                                    // 乾燥皿の種類
                    mapping.put("rutubono_monitor", "rutubono_monitor");                                        // ﾙﾂﾎﾞNo(ﾓﾆﾀｰ)
                    mapping.put("rutubono_seihin", "rutubono_seihin");                                          // ﾙﾂﾎﾞNo(製品)
                    mapping.put("slurryjyuuryou_monitor", "slurryjyuuryou_monitor");                            // ｽﾗﾘｰ重量(ﾓﾆﾀｰ)
                    mapping.put("slurryjyuuryou_seihin", "slurryjyuuryou_seihin");                              // ｽﾗﾘｰ重量(製品)
                    mapping.put("kansouondo", "kansouondo");                                                    // 乾燥温度
                    mapping.put("kansoujikankikaku", "kansoujikankikaku");                                      // 乾燥時間規格
                    mapping.put("kansoukaisinichij", "kansoukaisinichij");                                      // 乾燥開始日時
                    mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                              // 乾燥終了日時
                    mapping.put("dassirogouki", "dassirogouki");                                                // 脱脂炉号機
                    mapping.put("dassiondo", "dassiondo");                                                      // 脱脂温度
                    mapping.put("dassijikankikaku", "dassijikankikaku");                                        // 脱脂時間規格
                    mapping.put("dassikaisinichiji", "dassikaisinichiji");                                      // 脱脂開始日時
                    mapping.put("dassisyuuryounichiji", "dassisyuuryounichiji");                                // 脱脂終了日時
                    mapping.put("kansoutantousya", "kansoutantousya");                                          // 乾燥担当者
                    mapping.put("maesyori", "maesyori");                                                        // 前処理
                    mapping.put("maesyoriondo", "maesyoriondo");                                                // 前処理温度
                    mapping.put("maesyorijikan", "maesyorijikan");                                              // 前処理時間
                    mapping.put("maesyorikaisinichiji", "maesyorikaisinichiji");                                // 前処理開始日時
                    mapping.put("maesyorisyuuryounichiji", "maesyorisyuuryounichiji");                          // 前処理終了日時
                    mapping.put("maesyoritantousya", "maesyoritantousya");                                      // 前処理担当者
                    mapping.put("hihyoumensekisokuteikaisinichiji", "hihyoumensekisokuteikaisinichiji");        // 比表面積測定開始日時
                    mapping.put("hihyoumensekisokuteisyuuryounichiji", "hihyoumensekisokuteisyuuryounichiji");  // 比表面積測定終了日時
                    mapping.put("monitorjissokuti", "monitorjissokuti");                                        // ﾓﾆﾀｰ実測値
                    mapping.put("monitorhoseiti", "monitorhoseiti");                                            // ﾓﾆﾀｰ補正値
                    mapping.put("hihyoumensekikikaku", "hihyoumensekikikaku");                                  // 比表面積規格
                    mapping.put("hihyoumensekineraiti", "hihyoumensekineraiti");                                // 比表面積狙い値
                    mapping.put("hihyoumensekisankouti", "hihyoumensekisankouti");                              // 比表面積参考値
                    mapping.put("hihyoumensekisokuteikekka1", "hihyoumensekisokuteikekka1");                    // 比表面積測定結果①
                    mapping.put("hihyoumensekisokuteikekka2", "hihyoumensekisokuteikekka2");                    // 比表面積測定結果②
                    mapping.put("hihyoumensekisokuteikekka3", "hihyoumensekisokuteikekka3");                    // 比表面積測定結果③
                    mapping.put("hihyoumensekisokuteikekka", "hihyoumensekisokuteikekka");                      // 比表面積測定結果
                    mapping.put("ssahikikaku", "ssahikikaku");                                                  // SSA比規格
                    mapping.put("ssahisankouti", "ssahisankouti");                                              // SSA比参考値
                    mapping.put("ssahi", "ssahi");                                                              // SSA比
                    mapping.put("hihyoumensekisokuteitantousya", "hihyoumensekisokuteitantousya");              // 比表面積測定担当者
                    mapping.put("bikou1", "bikou1");                                                            // 備考1
                    mapping.put("bikou2", "bikou2");                                                            // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B025Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B025Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB025ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB025DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                case "ﾌｨﾙﾀｰﾊﾟｽ・保管":{
                    // 工程が「ﾌｨﾙﾀｰﾊﾟｽ・保管」の場合、Ⅲ.画面表示仕様(18)を発行し、【Ⅵ.画面項目制御・出力仕様.検索】を元に画面表示を行う。
                    sql ="SELECT CONCAT(kojyo , lotno , edaban) AS lotno "
                            + ", yuudentaislurryhinmei"
                            + ", yuudentaislurrylotno"
                            + ", lotkubun"
                            + ", genryoulotno"
                            + ", genryoukigou"
                            + ", fuutaijyuuryousokutei_sutenyouki1"
                            + ", fuutaijyuuryousokutei_sutenyouki2"
                            + ", fuutaijyuuryousokutei_sutenyouki3"
                            + ", fuutaijyuuryousokutei_sutenyouki4"
                            + ", fuutaijyuuryousokutei_sutenyouki5"
                            + ", fuutaijyuuryousokutei_sutenyouki6"
                            + ", fuutaijyuuryousokutei_siropori1"
                            + ", fuutaijyuuryousokutei_siropori2"
                            + ", fuutaijyuuryousokutei_siropori3"
                            + ", fuutaijyuuryousokutei_siropori4"
                            + ", fuutaijyuuryousokutei_siropori5"
                            + ", fuutaijyuuryousokutei_siropori6"
                            + ", fuutaijyuuryousokutei_siropori7"
                            + ", fuutaijyuuryousokutei_siropori8"
                            + ", fuutaijyuuryousokutei_siropori9"
                            + ", fuutaijyuuryousokutei_siropori10"
                            + ", fuutaijyuuryousokutei_siropori11"
                            + ", fuutaijyuuryousokutei_siropori12"
                            + ", fuutaijyuuryousokutei_tantousya"
                            + ", filterrenketu"
                            + ", filtertorituke_itijifilterhinmei"
                            + ", filtertorituke_lotno1"
                            + ", filtertorituke_toritukehonsuu1"
                            + ", filtertorituke_nijifilterhinmei"
                            + ", filtertorituke_lotno2"
                            + ", filtertorituke_toritukehonsuu2"
                            + ", filtertorituke_sanjifilterhinmei"
                            + ", filtertorituke_lotno3"
                            + ", filtertorituke_toritukehonsuu3"
                            + ", filtertorituke_tantousya"
                            + ", (CASE WHEN haisyutuyoukinoutibukuro = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS haisyutuyoukinoutibukuro"
                            + ", filtersiyoukaisuu"
                            + ", Fptankno AS fptankno"
                            + ", (CASE WHEN senjyoukakunin = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS senjyoukakunin"
                            + ", Fpkaisinichiji AS fpkaisinichiji"
                            + ", assouregulatorNo"
                            + ", assouaturyoku"
                            + ", filterpasskaisi_tantousya"
                            + ", filterkoukan1_Fpteisinichiji"
                            + ", filterkoukan1_itijifilterhinmei"
                            + ", filterkoukan1_lotno1"
                            + ", filterkoukan1_toritukehonsuu1"
                            + ", filterkoukan1_nijifilterhinmei"
                            + ", filterkoukan1_lotno2"
                            + ", filterkoukan1_toritukehonsuu2"
                            + ", filterkoukan1_sanjifilterhinmei"
                            + ", filterkoukan1_lotno3"
                            + ", filterkoukan1_toritukehonsuu3"
                            + ", filterkoukan1_Fpsaikainichiji"
                            + ", filterkoukan1_tantousya"
                            + ", Fpsyuuryounichiji AS fpsyuuryounichiji"
                            + ", Fptotaljikan AS fptotaljikan"
                            + ", itijifiltersousiyouhonsuu"
                            + ", nijifiltersousiyouhonsuu"
                            + ", sanjifiltersousiyouhonsuu"
                            + ", filterpasssyuuryou_tantousya"
                            + ", soujyuurousokutei1"
                            + ", soujyuurousokutei2"
                            + ", soujyuurousokutei3"
                            + ", soujyuurousokutei4"
                            + ", soujyuurousokutei5"
                            + ", soujyuurousokutei6"
                            + ", soujyuurousokutei7"
                            + ", soujyuurousokutei8"
                            + ", soujyuurousokutei9"
                            + ", soujyuurousokutei10"
                            + ", soujyuurousokutei11"
                            + ", soujyuurousokutei12"
                            + ", yuudentaislurryjyuurou1"
                            + ", yuudentaislurryjyuurou2"
                            + ", yuudentaislurryjyuurou3"
                            + ", yuudentaislurryjyuurou4"
                            + ", yuudentaislurryjyuurou5"
                            + ", yuudentaislurryjyuurou6"
                            + ", yuudentaislurryjyuurou7"
                            + ", yuudentaislurryjyuurou8"
                            + ", yuudentaislurryjyuurou9"
                            + ", yuudentaislurryjyuurou10"
                            + ", yuudentaislurryjyuurou11"
                            + ", yuudentaislurryjyuurou12"
                            + ", yuudentaislurryjyuurougoukei"
                            + ", tounyuuryou"
                            + ", budomarikeisan"
                            + ", yuudentaislurryyuukoukigen"
                            + ", (CASE WHEN funsaihantei = 0 THEN '不合格' WHEN funsaihantei = 1 THEN '合格' ELSE '' END) AS funsaihantei "
                            + ", seihinjyuuryoukakunin_tantousya"
                            + ", (CASE WHEN hozonyousamplekaisyu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS hozonyousamplekaisyu"
                            + ", (CASE WHEN zunsekiyousamplekaisyu = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS zunsekiyousamplekaisyu"
                            + ", kansouzara"
                            + ", arumizarafuutaijyuuryou"
                            + ", slurrysamplejyuuryoukikaku"
                            + ", kansoumaeslurryjyuuryou"
                            + ", kansouki"
                            + ", kansouondokikaku"
                            + ", kansoujikankikaku"
                            + ", kansoukaisinichiji"
                            + ", kansousyuuryounichiji"
                            + ", kansoujikantotal"
                            + ", kansougosoujyuuryou"
                            + ", kansougosyoumijyuuryou"
                            + ", kokeibunhiritu"
                            + ", bikou1"
                            + ", bikou2 "
                            + "  FROM sr_yuudentai_fp "
                            + " WHERE (? IS NULL OR kojyo = ?) "
                            + "   AND (? IS NULL OR lotno = ?) "
                            + "   AND (? IS NULL OR edaban = ?) "
                            + "   AND (? IS NULL OR yuudentaislurryhinmei = ?) "
                            + "   AND (? IS NULL OR Fpkaisinichiji >= ?) "
                            + "   AND (? IS NULL OR Fpkaisinichiji <= ?) "
                            + " ORDER BY Fpkaisinichiji ";
                    // パラメータ設定
                    List<Object> params = createSearchParam();
                    // モデルクラスとのマッピング定義
                    Map<String, String> mapping = new HashMap<>();
                    mapping.put("lotno", "lotno");                                                        // ﾛｯﾄNo
                    mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                        // 誘電体ｽﾗﾘｰ品名
                    mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                          // 誘電体ｽﾗﾘｰLotNo
                    mapping.put("lotkubun", "lotkubun");                                                  // ﾛｯﾄ区分
                    mapping.put("genryoulotno", "genryoulotno");                                          // 原料LotNo
                    mapping.put("genryoukigou", "genryoukigou");                                          // 原料記号
                    mapping.put("fuutaijyuuryousokutei_sutenyouki1", "fuutaijyuuryousokutei_sutenyouki1");// 風袋重量測定_ｽﾃﾝ容器1
                    mapping.put("fuutaijyuuryousokutei_sutenyouki2", "fuutaijyuuryousokutei_sutenyouki2");// 風袋重量測定_ｽﾃﾝ容器2
                    mapping.put("fuutaijyuuryousokutei_sutenyouki3", "fuutaijyuuryousokutei_sutenyouki3");// 風袋重量測定_ｽﾃﾝ容器3
                    mapping.put("fuutaijyuuryousokutei_sutenyouki4", "fuutaijyuuryousokutei_sutenyouki4");// 風袋重量測定_ｽﾃﾝ容器4
                    mapping.put("fuutaijyuuryousokutei_sutenyouki5", "fuutaijyuuryousokutei_sutenyouki5");// 風袋重量測定_ｽﾃﾝ容器5
                    mapping.put("fuutaijyuuryousokutei_sutenyouki6", "fuutaijyuuryousokutei_sutenyouki6");// 風袋重量測定_ｽﾃﾝ容器6
                    mapping.put("fuutaijyuuryousokutei_siropori1", "fuutaijyuuryousokutei_siropori1");    // 風袋重量測定_白ﾎﾟﾘ容器1
                    mapping.put("fuutaijyuuryousokutei_siropori2", "fuutaijyuuryousokutei_siropori2");    // 風袋重量測定_白ﾎﾟﾘ容器2
                    mapping.put("fuutaijyuuryousokutei_siropori3", "fuutaijyuuryousokutei_siropori3");    // 風袋重量測定_白ﾎﾟﾘ容器3
                    mapping.put("fuutaijyuuryousokutei_siropori4", "fuutaijyuuryousokutei_siropori4");    // 風袋重量測定_白ﾎﾟﾘ容器4
                    mapping.put("fuutaijyuuryousokutei_siropori5", "fuutaijyuuryousokutei_siropori5");    // 風袋重量測定_白ﾎﾟﾘ容器5
                    mapping.put("fuutaijyuuryousokutei_siropori6", "fuutaijyuuryousokutei_siropori6");    // 風袋重量測定_白ﾎﾟﾘ容器6
                    mapping.put("fuutaijyuuryousokutei_siropori7", "fuutaijyuuryousokutei_siropori7");    // 風袋重量測定_白ﾎﾟﾘ容器7
                    mapping.put("fuutaijyuuryousokutei_siropori8", "fuutaijyuuryousokutei_siropori8");    // 風袋重量測定_白ﾎﾟﾘ容器8
                    mapping.put("fuutaijyuuryousokutei_siropori9", "fuutaijyuuryousokutei_siropori9");    // 風袋重量測定_白ﾎﾟﾘ容器9
                    mapping.put("fuutaijyuuryousokutei_siropori10", "fuutaijyuuryousokutei_siropori10");  // 風袋重量測定_白ﾎﾟﾘ容器10
                    mapping.put("fuutaijyuuryousokutei_siropori11", "fuutaijyuuryousokutei_siropori11");  // 風袋重量測定_白ﾎﾟﾘ容器11
                    mapping.put("fuutaijyuuryousokutei_siropori12", "fuutaijyuuryousokutei_siropori12");  // 風袋重量測定_白ﾎﾟﾘ容器12
                    mapping.put("fuutaijyuuryousokutei_tantousya", "fuutaijyuuryousokutei_tantousya");    // 保管容器準備_担当者
                    mapping.put("filterrenketu", "filterrenketu");                                        // ﾌｨﾙﾀｰ連結
                    mapping.put("filtertorituke_itijifilterhinmei", "filtertorituke_itijifilterhinmei");  // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertorituke_lotno1", "filtertorituke_lotno1");                        // ﾌｨﾙﾀｰ取り付け_LotNo1
                    mapping.put("filtertorituke_toritukehonsuu1", "filtertorituke_toritukehonsuu1");      // ﾌｨﾙﾀｰ取り付け_取り付け本数1
                    mapping.put("filtertorituke_nijifilterhinmei", "filtertorituke_nijifilterhinmei");    // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertorituke_lotno2", "filtertorituke_lotno2");                        // ﾌｨﾙﾀｰ取り付け_LotNo2
                    mapping.put("filtertorituke_toritukehonsuu2", "filtertorituke_toritukehonsuu2");      // ﾌｨﾙﾀｰ取り付け_取り付け本数2
                    mapping.put("filtertorituke_sanjifilterhinmei", "filtertorituke_sanjifilterhinmei");  // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
                    mapping.put("filtertorituke_lotno3", "filtertorituke_lotno3");                        // ﾌｨﾙﾀｰ取り付け_LotNo3
                    mapping.put("filtertorituke_toritukehonsuu3", "filtertorituke_toritukehonsuu3");      // ﾌｨﾙﾀｰ取り付け_取り付け本数3
                    mapping.put("filtertorituke_tantousya", "filtertorituke_tantousya");                  // ﾌｨﾙﾀｰ取り付け_担当者
                    mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                  // 排出容器の内袋
                    mapping.put("filtersiyoukaisuu", "filtersiyoukaisuu");                                // ﾌｨﾙﾀｰ使用回数
                    mapping.put("fptankno", "fptankno");                                                  // F/PﾀﾝｸNo
                    mapping.put("senjyoukakunin", "senjyoukakunin");                                      // 洗浄確認
                    mapping.put("fpkaisinichiji", "fpkaisinichiji");                                      // F/P開始日時
                    mapping.put("assouregulatorNo", "assouregulatorNo");                                  // 圧送ﾚｷﾞｭﾚｰﾀｰNo
                    mapping.put("assouaturyoku", "assouaturyoku");                                        // 圧送圧力
                    mapping.put("filterpasskaisi_tantousya", "filterpasskaisi_tantousya");                // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
                    mapping.put("filterkoukan1_Fpteisinichiji", "filterkoukan1_Fpteisinichiji");          // ﾌｨﾙﾀｰ交換①_F/P停止日時
                    mapping.put("filterkoukan1_itijifilterhinmei", "filterkoukan1_itijifilterhinmei");    // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1_lotno1", "filterkoukan1_lotno1");                          // ﾌｨﾙﾀｰ交換①_LotNo1
                    mapping.put("filterkoukan1_toritukehonsuu1", "filterkoukan1_toritukehonsuu1");        // ﾌｨﾙﾀｰ交換①_取り付け本数1
                    mapping.put("filterkoukan1_nijifilterhinmei", "filterkoukan1_nijifilterhinmei");      // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1_lotno2", "filterkoukan1_lotno2");                          // ﾌｨﾙﾀｰ交換①_LotNo2
                    mapping.put("filterkoukan1_toritukehonsuu2", "filterkoukan1_toritukehonsuu2");        // ﾌｨﾙﾀｰ交換①_取り付け本数2
                    mapping.put("filterkoukan1_sanjifilterhinmei", "filterkoukan1_sanjifilterhinmei");    // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
                    mapping.put("filterkoukan1_lotno3", "filterkoukan1_lotno3");                          // ﾌｨﾙﾀｰ交換①_LotNo3
                    mapping.put("filterkoukan1_toritukehonsuu3", "filterkoukan1_toritukehonsuu3");        // ﾌｨﾙﾀｰ交換①_取り付け本数3
                    mapping.put("filterkoukan1_Fpsaikainichiji", "filterkoukan1_Fpsaikainichiji");        // ﾌｨﾙﾀｰ交換①_F/P再開日時
                    mapping.put("filterkoukan1_tantousya", "filterkoukan1_tantousya");                    // ﾌｨﾙﾀｰ交換①_担当者
                    mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");                                // F/P終了日時
                    mapping.put("fptotaljikan", "fptotaljikan");                                          // F/Pﾄｰﾀﾙ時間
                    mapping.put("itijifiltersousiyouhonsuu", "itijifiltersousiyouhonsuu");                // 1次ﾌｨﾙﾀｰ総使用本数
                    mapping.put("nijifiltersousiyouhonsuu", "nijifiltersousiyouhonsuu");                  // 2次ﾌｨﾙﾀｰ総使用本数
                    mapping.put("sanjifiltersousiyouhonsuu", "sanjifiltersousiyouhonsuu");                // 3次ﾌｨﾙﾀｰ総使用本数
                    mapping.put("filterpasssyuuryou_tantousya", "filterpasssyuuryou_tantousya");          // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
                    mapping.put("soujyuurousokutei1", "soujyuurousokutei1");                              // 総重量測定1
                    mapping.put("soujyuurousokutei2", "soujyuurousokutei2");                              // 総重量測定2
                    mapping.put("soujyuurousokutei3", "soujyuurousokutei3");                              // 総重量測定3
                    mapping.put("soujyuurousokutei4", "soujyuurousokutei4");                              // 総重量測定4
                    mapping.put("soujyuurousokutei5", "soujyuurousokutei5");                              // 総重量測定5
                    mapping.put("soujyuurousokutei6", "soujyuurousokutei6");                              // 総重量測定6
                    mapping.put("soujyuurousokutei7", "soujyuurousokutei7");                              // 総重量測定7
                    mapping.put("soujyuurousokutei8", "soujyuurousokutei8");                              // 総重量測定8
                    mapping.put("soujyuurousokutei9", "soujyuurousokutei9");                              // 総重量測定9
                    mapping.put("soujyuurousokutei10", "soujyuurousokutei10");                            // 総重量測定10
                    mapping.put("soujyuurousokutei11", "soujyuurousokutei11");                            // 総重量測定11
                    mapping.put("soujyuurousokutei12", "soujyuurousokutei12");                            // 総重量測定12
                    mapping.put("yuudentaislurryjyuurou1", "yuudentaislurryjyuurou1");                    // 誘電体ｽﾗﾘｰ重量1
                    mapping.put("yuudentaislurryjyuurou2", "yuudentaislurryjyuurou2");                    // 誘電体ｽﾗﾘｰ重量2
                    mapping.put("yuudentaislurryjyuurou3", "yuudentaislurryjyuurou3");                    // 誘電体ｽﾗﾘｰ重量3
                    mapping.put("yuudentaislurryjyuurou4", "yuudentaislurryjyuurou4");                    // 誘電体ｽﾗﾘｰ重量4
                    mapping.put("yuudentaislurryjyuurou5", "yuudentaislurryjyuurou5");                    // 誘電体ｽﾗﾘｰ重量5
                    mapping.put("yuudentaislurryjyuurou6", "yuudentaislurryjyuurou6");                    // 誘電体ｽﾗﾘｰ重量6
                    mapping.put("yuudentaislurryjyuurou7", "yuudentaislurryjyuurou7");                    // 誘電体ｽﾗﾘｰ重量7
                    mapping.put("yuudentaislurryjyuurou8", "yuudentaislurryjyuurou8");                    // 誘電体ｽﾗﾘｰ重量8
                    mapping.put("yuudentaislurryjyuurou9", "yuudentaislurryjyuurou9");                    // 誘電体ｽﾗﾘｰ重量9
                    mapping.put("yuudentaislurryjyuurou10", "yuudentaislurryjyuurou10");                  // 誘電体ｽﾗﾘｰ重量10
                    mapping.put("yuudentaislurryjyuurou11", "yuudentaislurryjyuurou11");                  // 誘電体ｽﾗﾘｰ重量11
                    mapping.put("yuudentaislurryjyuurou12", "yuudentaislurryjyuurou12");                  // 誘電体ｽﾗﾘｰ重量12
                    mapping.put("yuudentaislurryjyuurougoukei", "yuudentaislurryjyuurougoukei");          // 誘電体ｽﾗﾘｰ重量合計
                    mapping.put("tounyuuryou", "tounyuuryou");                                            // 投入量
                    mapping.put("budomarikeisan", "budomarikeisan");                                      // 歩留まり計算
                    mapping.put("yuudentaislurryyuukoukigen", "yuudentaislurryyuukoukigen");              // 誘電体ｽﾗﾘｰ有効期限
                    mapping.put("funsaihantei", "funsaihantei");                                          // 粉砕判定
                    mapping.put("seihinjyuuryoukakunin_tantousya", "seihinjyuuryoukakunin_tantousya");    // 製品重量確認_担当者
                    mapping.put("hozonyousamplekaisyu", "hozonyousamplekaisyu");                          // 保存用ｻﾝﾌﾟﾙ回収
                    mapping.put("zunsekiyousamplekaisyu", "zunsekiyousamplekaisyu");                      // 分析用ｻﾝﾌﾟﾙ回収
                    mapping.put("kansouzara", "kansouzara");                                              // 乾燥皿
                    mapping.put("arumizarafuutaijyuuryou", "arumizarafuutaijyuuryou");                    // ｱﾙﾐ皿風袋重量
                    mapping.put("slurrysamplejyuuryoukikaku", "slurrysamplejyuuryoukikaku");              // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
                    mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                    // 乾燥前ｽﾗﾘｰ重量
                    mapping.put("kansouki", "kansouki");                                                  // 乾燥機
                    mapping.put("kansouondokikaku", "kansouondokikaku");                                  // 乾燥温度規格
                    mapping.put("kansoujikankikaku", "kansoujikankikaku");                                // 乾燥時間規格
                    mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                              // 乾燥開始日時
                    mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                        // 乾燥終了日時
                    mapping.put("kansoujikantotal", "kansoujikantotal");                                  // 乾燥時間ﾄｰﾀﾙ
                    mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                            // 乾燥後総重量
                    mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                      // 乾燥後正味重量
                    mapping.put("kokeibunhiritu", "kokeibunhiritu");                                      // 固形分比率
                    mapping.put("bikou1", "bikou1");                                                      // 備考1
                    mapping.put("bikou2", "bikou2");                                                      // 備考2
                    BeanProcessor beanProcessor = new BeanProcessor(mapping);
                    RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                    ResultSetHandler<List<GXHDO202B026Model>> beanHandler =
                            new BeanListHandler<>(GXHDO202B026Model.class, rowProcessor);
                    DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                    setB026ListData(queryRunner.query(sql, beanHandler, params.toArray()));
                    setB026DTdisplay(DISPLAY_BLOCK);
                    break;
                }
                default:
                    break;
            }
            
        } catch (SQLException ex) {
            setB019ListData(new ArrayList<>());
            setB020ListData(new ArrayList<>());
            setB021ListData(new ArrayList<>());
            setB022ListData(new ArrayList<>());
            setB023ListData(new ArrayList<>());
            setB024ListData(new ArrayList<>());
            setB025ListData(new ArrayList<>());
            setB026ListData(new ArrayList<>());
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
        if(DISPLAY_BLOCK.equals(getB019DTdisplay())) {
            //工程が「添加材ｽﾗﾘｰ固形分測定」の場合
            excelRealPath = JSON_FILE_PATH_202B019;
            excelFileHeadName = GAMEN_NAME_202B019;
            outputList = getB019ListData();
        }else if(DISPLAY_BLOCK.equals(getB020DTdisplay())) {
            //工程が「溶剤・添加材ｽﾗﾘｰ秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B020;
            excelFileHeadName = GAMEN_NAME_202B020;
            outputList = getB020ListData();
        }else if(DISPLAY_BLOCK.equals(getB021DTdisplay())) {
            //工程が「添加材・ｿﾞﾙ秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B021;
            excelFileHeadName = GAMEN_NAME_202B021;
            outputList = getB021ListData();
        }else if(DISPLAY_BLOCK.equals(getB022DTdisplay())) {
            //工程が「主原料秤量」の場合
            excelRealPath = JSON_FILE_PATH_202B022;
            excelFileHeadName = GAMEN_NAME_202B022;
            outputList = getB022ListData();
        }else if(DISPLAY_BLOCK.equals(getB023DTdisplay())) {
            //工程が「ﾌﾟﾚﾐｷｼﾝｸﾞ」の場合
            excelRealPath = JSON_FILE_PATH_202B023;
            excelFileHeadName = GAMEN_NAME_202B023;
            outputList = getB023ListData();
        }else if(DISPLAY_BLOCK.equals(getB024DTdisplay())) {
            //工程が「粉砕」の場合
            excelRealPath = JSON_FILE_PATH_202B024;
            excelFileHeadName = GAMEN_NAME_202B024;
            outputList = getB024ListData();
        }else if(DISPLAY_BLOCK.equals(getB025DTdisplay())) {
            //工程が「比表面積測定」の場合
            excelRealPath = JSON_FILE_PATH_202B025;
            excelFileHeadName = GAMEN_NAME_202B025;
            outputList = getB025ListData();
        }else if(DISPLAY_BLOCK.equals(getB026DTdisplay())) {
            //工程が「ﾌｨﾙﾀｰﾊﾟｽ・保管」の場合
            excelRealPath = JSON_FILE_PATH_202B026;
            excelFileHeadName = GAMEN_NAME_202B026;
            outputList = getB026ListData();
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

        //溶剤秤量開始日(FROM)
        Date paramYouzaihyouryoukaisiDateF = null;
        if (!StringUtil.isEmpty(youzaihyouryoukaisiDateF)) {
            paramYouzaihyouryoukaisiDateF = DateUtil.convertStringToDateInSeconds(getYouzaihyouryoukaisiDateF(), "000000");
        }
        
        //溶剤秤量開始日(TO)
        Date paramYouzaihyouryoukaisiDateT = null;
        if (!StringUtil.isEmpty(youzaihyouryoukaisiDateT)) {
            paramYouzaihyouryoukaisiDateT = DateUtil.convertStringToDateInSeconds(getYouzaihyouryoukaisiDateT(), "235959");
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
        
        //主原料秤量開始日(FROM)
        Date paramSyugenryouhyouryoukaisiDateF = null;
        if (!StringUtil.isEmpty(syugenryouhyouryoukaisiDateF)) {
            paramSyugenryouhyouryoukaisiDateF = DateUtil.convertStringToDateInSeconds(getSyugenryouhyouryoukaisiDateF(), "000000");
        }
        
        //主原料秤量開始日(TO)
        Date paramSyugenryouhyouryoukaisiDateT = null;
        if (!StringUtil.isEmpty(syugenryouhyouryoukaisiDateT)) {
            paramSyugenryouhyouryoukaisiDateT = DateUtil.convertStringToDateInSeconds(getSyugenryouhyouryoukaisiDateT(), "235959");
        }
        
        //投入開始日(FROM)
        Date paramTounyuukaisiDateF = null;
        if (!StringUtil.isEmpty(tounyuukaisiDateF)) {
            paramTounyuukaisiDateF = DateUtil.convertStringToDateInSeconds(getTounyuukaisiDateF(), "000000");
        }
        
        //投入開始日(TO)
        Date paramTounyuukaisiDateT = null;
        if (!StringUtil.isEmpty(tounyuukaisiDateT)) {
            paramTounyuukaisiDateT = DateUtil.convertStringToDateInSeconds(getTounyuukaisiDateT(), "235959");
        }
        
        //粉砕開始日(FROM)
        Date paramFunsaikaisiDateF = null;
        if (!StringUtil.isEmpty(funsaikaisiDateF)) {
            paramFunsaikaisiDateF = DateUtil.convertStringToDateInSeconds(getFunsaikaisiDateF(), "000000");
        }
        
        //粉砕開始日(TO)
        Date paramFunsaikaisiDateT = null;
        if (!StringUtil.isEmpty(funsaikaisiDateT)) {
            paramFunsaikaisiDateT = DateUtil.convertStringToDateInSeconds(getFunsaikaisiDateT(), "235959");
        }
        
        //乾燥開始日(FROM)
        Date paramKansoukaisiDateF = null;
        if (!StringUtil.isEmpty(kansoukaisiDateF)) {
            paramKansoukaisiDateF = DateUtil.convertStringToDateInSeconds(getKansoukaisiDateF(), "000000");
        }
        
        //乾燥開始日(TO)
        Date paramKansoukaisiDateT = null;
        if (!StringUtil.isEmpty(kansoukaisiDateT)) {
            paramKansoukaisiDateT = DateUtil.convertStringToDateInSeconds(getKansoukaisiDateT(), "235959");
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
                
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));
        
        if(null != cmbKotei)switch (cmbKotei) {
            case "添加材ｽﾗﾘｰ固形分測定":
                // 工程が「添加材ｽﾗﾘｰ固形分測定」の場合、Ⅲ.画面表示仕様(3)を発行する。
                params.addAll(Arrays.asList(paramKakuhankaisiDateF, paramKakuhankaisiDateF));
                params.addAll(Arrays.asList(paramKakuhankaisiDateT, paramKakuhankaisiDateT));
                break;
            case "溶剤・添加材ｽﾗﾘｰ秤量":
                // 工程が「溶剤・添加材ｽﾗﾘｰ秤量」の場合、Ⅲ.画面表示仕様(7)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramYouzaihyouryoukaisiDateF, paramYouzaihyouryoukaisiDateF));
                params.addAll(Arrays.asList(paramYouzaihyouryoukaisiDateT, paramYouzaihyouryoukaisiDateT));
                break;
            case "添加材・ｿﾞﾙ秤量":
                // 工程が「添加材・ｿﾞﾙ秤量」の場合、Ⅲ.画面表示仕様(9)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramHyoryoDateF, paramHyoryoDateF));
                params.addAll(Arrays.asList(paramHyoryoDateT, paramHyoryoDateT));
                break;
            case "主原料秤量":
                // 工程が「主原料秤量」の場合、Ⅲ.画面表示仕様(11)を発行する。
                params.addAll(Arrays.asList(paramGoki, paramGoki));
                params.addAll(Arrays.asList(paramSyugenryouhyouryoukaisiDateF, paramSyugenryouhyouryoukaisiDateF));
                params.addAll(Arrays.asList(paramSyugenryouhyouryoukaisiDateT, paramSyugenryouhyouryoukaisiDateT));
                break;
            case "ﾌﾟﾚﾐｷｼﾝｸﾞ":
                // 工程が「ﾌﾟﾚﾐｷｼﾝｸﾞ」の場合、Ⅲ.画面表示仕様(13)を発行する。
                params.addAll(Arrays.asList(paramTounyuukaisiDateF, paramTounyuukaisiDateF));
                params.addAll(Arrays.asList(paramTounyuukaisiDateT, paramTounyuukaisiDateT));
                break;
            case "粉砕":
                // 工程が「粉砕」の場合、Ⅲ.画面表示仕様(15)を発行する。
                params.addAll(Arrays.asList(paramFunsaikaisiDateF, paramFunsaikaisiDateF));
                params.addAll(Arrays.asList(paramFunsaikaisiDateT, paramFunsaikaisiDateT));
                break;
            case "比表面積測定":
                // 工程が「比表面積測定」の場合、Ⅲ.画面表示仕様(17)を発行する。
                params.addAll(Arrays.asList(paramKansoukaisiDateF, paramKansoukaisiDateF));
                params.addAll(Arrays.asList(paramKansoukaisiDateT, paramKansoukaisiDateT));
                break;
            case "ﾌｨﾙﾀｰﾊﾟｽ・保管":
                // 工程が「ﾌｨﾙﾀｰﾊﾟｽ・保管」の場合、Ⅲ.画面表示仕様(19)を発行する。
                params.addAll(Arrays.asList(paramFpkaisiDateF, paramFpkaisiDateF));
                params.addAll(Arrays.asList(paramFpkaisiDateT, paramFpkaisiDateT));
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
        if(DISPLAY_BLOCK.equals(getB019DTdisplay())) {
            //工程が「添加材ｽﾗﾘｰ固形分測定」の場合
            return !(b019ListData == null || b019ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB020DTdisplay())) {
            //工程が「溶剤・添加材ｽﾗﾘｰ秤量」の場合
            return !(b020ListData == null || b020ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB021DTdisplay())) {
            //工程が「添加材・ｿﾞﾙ秤量」の場合
            return !(b021ListData == null || b021ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB022DTdisplay())) {
            //工程が「主原料秤量」の場合
            return !(b022ListData == null || b022ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB023DTdisplay())) {
            //工程が「ﾌﾟﾚﾐｷｼﾝｸﾞ」の場合
            return !(b023ListData == null || b023ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB024DTdisplay())) {
            //工程が「粉砕」の場合
            return !(b024ListData == null || b024ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB025DTdisplay())) {
            //工程が「比表面積測定」の場合
            return !(b025ListData == null || b025ListData.isEmpty());
        }else if(DISPLAY_BLOCK.equals(getB026DTdisplay())) {
            //工程が「ﾌｨﾙﾀｰﾊﾟｽ・保管」の場合
            return !(b026ListData == null || b026ListData.isEmpty());
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