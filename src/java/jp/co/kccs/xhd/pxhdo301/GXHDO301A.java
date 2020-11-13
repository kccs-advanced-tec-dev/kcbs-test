/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo301;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO301AModel;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	設計仕様検索画面<br>
 * <br>
 * 変更日	2020/08/29<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS R.Yamakiri<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	<br>
 * 計画書No	<br>
 * 変更者	<br>
 * 変更理由	<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO301A(設計仕様検索画面)
 *
 * @author KCSS D.Yanagida
 * @since 2020/08/29
 */
@Named
@ViewScoped
public class GXHDO301A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO301A.class.getName());
    private static final int LOTNO_BYTE = 14;
    private static final String CHARSET = "MS932";
    private static final String PRINT_URL = "/secure/pxhdo301/gxhdo301a002.xhtml?faces-redirect=true";
    private static final String SEKISO_URL = "/secure/pxhdo301/gxhdo301a003.xhtml?faces-redirect=true";
    private static final String RHAPS_URL = "/secure/pxhdo301/gxhdo301a001.xhtml?faces-redirect=true";
    private static final String YUDENTAI_PASTE_KOTEIMEI = "設計仕様";
    private static final String YUDENTAI_PASTE_KOMOKUMEI = "誘電体";
    private static final String YUDENTAI_PASTE_KANRIKOMOKU = "ﾍﾟｰｽﾄ";
    private static final String PETFILEM_KOTEIMEI = "設計仕様";
    private static final String PETFILEM_KOMOKUMEI = "PETﾌｨﾙﾑ種類";
    private static final String PETFILEM_KANRIKOMOKU = "PETﾌｨﾙﾑ種類";
    private static final String EA_PASTE_KOTEIMEI = "設計仕様";
    private static final String EA_PASTE_KOMOKUMEI = "電極";
    private static final String EA_PASTE_KANRIKOMOKU = "ﾍﾟｰｽﾄ";
    private static final String EA_SEIHAN_KOTEIMEI = "設計仕様";
    private static final String EA_SEIHAN_KOMOKUMEI = "電極";
    private static final String EA_SEIHAN_KANRIKOMOKU = "製版仕様";
    private static final String EA_SEIHAN_KANRIKOMOKU_RHAPS = "電極製版仕様";
    private static final String SEKI_SLIDE_KOTEIMEI = "設計仕様";
    private static final String SEKI_SLIDE_KOMOKUMEI = "積層ｽﾗｲﾄﾞ量";
    private static final String SEKI_SLIDE_KANRIKOMOKU = "積層ｽﾗｲﾄﾞ量";
    private static final String YUDENTAI_SEIHANMEI_KOTEIMEI = "設計仕様";
    private static final String YUDENTAI_SEIHANMEI_KOMOKUMEI = "誘電体";
    private static final String YUDENTAI_SEIHANMEI_KANRIKOMOKU = "誘電体製版名";
    private static final String YUDENTAI_SEIHAN_KOTEIMEI = "設計仕様";
    private static final String YUDENTAI_SEIHAN_KOMOKUMEI = "誘電体";
    private static final String YUDENTAI_SEIHAN_KANRIKOMOKU = "誘電体製版仕様";
    private static final String KOCHAKU_SITE_KOTEIMEI = "設計仕様";
    private static final String KOCHAKU_SITE_KOMOKUMEI = "固着ｼｰﾄ";
    private static final String KOCHAKU_SITE_KANRIKOMOKU = "固着ｼｰﾄ";
    private static final String SEKISO_GOKI_KOTEIMEI = "積層";
    private static final String SEKISO_GOKI_KOMOKUMEI = "設備";
    private static final String SEKISO_GOKI_KANRIKOMOKU = "指定号機";

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;

    /** 検索条件：ロットNo */
    private String lotNo = "";

    /** 一覧表示データ */
    private GXHDO301AModel data = new GXHDO301AModel();
        
    /**
     * コンストラクタ
     */
    public GXHDO301A() {
    }
    
    /**
     * 画面起動時処理
     */
    public void init() {
        // セッション情報から画面パラメータを取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        setLotNo(StringUtil.nullToBlank(session.getAttribute("lotNo")));
        
        // 設計仕様を検索する
        search();
    }

    /**
     * 検索：
     * 設計仕様を検索する
     */    
    public void search() {

        try {
            
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);

            // ﾛｯﾄNoの入力ﾁｪｯｸ
            ValidateUtil validateUtil = new ValidateUtil();
            if(!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)){
             FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            if (!StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
                return;
            }
            // 設計仕様の取得
            Map sekkeiData = loadSekkeiData(queryRunnerQcdb, getLotNo());
            if (sekkeiData == null || sekkeiData.isEmpty()) {
                // ｴﾗｰﾒｯｾｰｼﾞを画面に表示する
                return;
            }
            
            // 上ｶﾊﾞｰﾃｰﾌﾟ1
            String ueCoverTape1 = "";
            // 上ｶﾊﾞｰﾃｰﾌﾟ2
            String ueCoverTape2 = "";
            // 下ｶﾊﾞｰﾃｰﾌﾟ1
            String shitaCoverTape1 = "";
            // 下ｶﾊﾞｰﾃｰﾌﾟ2
            String shitaCoverTape2 = "";
            // 印刷ﾛｰﾙNo
            String rollno = "";
            
            //上ｶﾊﾞｰﾃｰﾌﾟ1対象項目の決定
            List<String> checkListDataVal = checkYoutoItems(sekkeiData, getMapYoutoAssociation(),"CT",getMapSekkeiYotoAssociation());
            for(int i=0; i<=checkListDataVal.size()-1; i++){
                if(i > 0){
                    if("OK".equals(checkListDataVal.get(0))){
                        ueCoverTape1 = checkListDataVal.get(i);
                    }
                }
            }

            //上ｶﾊﾞｰﾃｰﾌﾟ2対象項目の決定
            checkListDataVal.clear();
            checkListDataVal = checkYoutoItems(sekkeiData, getMapYoutoAssociation(),"ST",getMapSekkeiYotoAssociation());
            for(int i=0; i<=checkListDataVal.size()-1; i++){
                if(i > 0){
                    if("OK".equals(checkListDataVal.get(0))){
                        ueCoverTape2 = checkListDataVal.get(i);
                    }
                }
            }

            //下ｶﾊﾞｰﾃｰﾌﾟ1対象項目の決定
            checkListDataVal.clear();
            checkListDataVal = checkYoutoItems(sekkeiData, getMapYoutoAssociation(),"CB",getMapSekkeiYotoAssociation());
            for(int i=0; i<=checkListDataVal.size()-1; i++){
                if(i > 0){
                    if("OK".equals(checkListDataVal.get(0))){
                        shitaCoverTape1 = checkListDataVal.get(i);
                    }
                }
            }

            //下ｶﾊﾞｰﾃｰﾌﾟ2対象項目の決定
            checkListDataVal.clear();
            checkListDataVal = checkYoutoItems(sekkeiData, getMapYoutoAssociation(),"SB",getMapSekkeiYotoAssociation());
            for(int i=0; i<=checkListDataVal.size()-1; i++){
                if(i > 0){
                    if("OK".equals(checkListDataVal.get(0))){
                        shitaCoverTape2 = checkListDataVal.get(i);
                    }
                }
            }
            // 印刷ﾛｰﾙNo対象項目の決定
            checkListDataVal.clear();
            checkListDataVal = checkYoutoItems(sekkeiData, getMapYoutoAssociation(),"EA",getMapSekkeiYotoAssociation());
            for(int i=0; i<=checkListDataVal.size()-1; i++){
                if(i > 0){
                    if("OK".equals(checkListDataVal.get(0))){
                        rollno = checkListDataVal.get(i);
                    }
                }
            }

            // ﾛｯﾄ区分ﾏｽﾀ情報の取得
            String lotkubuncode = StringUtil.nullToBlank(getMapData(sekkeiData, "KUBUN1")); //ﾛｯﾄ区分ｺｰﾄﾞ
            Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);

            // ｵｰﾅｰﾏｽﾀ情報の取得
            String ownercode = StringUtil.nullToBlank(getMapData(sekkeiData, "OWNER"));// ｵｰﾅｰｺｰﾄﾞ
            Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
            
            // 製造条件マスタの取得
            // 誘電体ﾍﾟｰｽﾄ
            String yuPaste = loadJoken(sekkeiData.get("SEKKEINO").toString(), YUDENTAI_PASTE_KOTEIMEI, YUDENTAI_PASTE_KOMOKUMEI, YUDENTAI_PASTE_KANRIKOMOKU, queryRunnerQcdb);
            // PETﾌｨﾙﾑ種類
            String petFilem = loadJoken(sekkeiData.get("SEKKEINO").toString(), PETFILEM_KOTEIMEI, PETFILEM_KOMOKUMEI, PETFILEM_KANRIKOMOKU, queryRunnerQcdb);
            // 電極ﾍﾟｰｽﾄ
            String eaPaste = loadJoken(sekkeiData.get("SEKKEINO").toString(), EA_PASTE_KOTEIMEI, EA_PASTE_KOMOKUMEI, EA_PASTE_KANRIKOMOKU, queryRunnerQcdb);
            // 電極製版仕様
            String eaSeihan = loadJoken(sekkeiData.get("SEKKEINO").toString(), EA_SEIHAN_KOTEIMEI, EA_SEIHAN_KOMOKUMEI, EA_SEIHAN_KANRIKOMOKU, queryRunnerQcdb);
            // 電極製版仕様
            if (eaSeihan == null || eaSeihan.equals("")) {
                eaSeihan = loadJoken(sekkeiData.get("SEKKEINO").toString(), EA_SEIHAN_KOTEIMEI, EA_SEIHAN_KOMOKUMEI, EA_SEIHAN_KANRIKOMOKU_RHAPS, queryRunnerQcdb);
            }
            // 積層ｽﾗｲﾄﾞ量
            String sekiSlide = loadJoken(sekkeiData.get("SEKKEINO").toString(), SEKI_SLIDE_KOTEIMEI, SEKI_SLIDE_KOMOKUMEI, SEKI_SLIDE_KANRIKOMOKU, queryRunnerQcdb);
            // 誘電体製版名
            String yuSeihanmei = loadJoken(sekkeiData.get("SEKKEINO").toString(), YUDENTAI_SEIHANMEI_KOTEIMEI, YUDENTAI_SEIHANMEI_KOMOKUMEI, YUDENTAI_SEIHANMEI_KANRIKOMOKU, queryRunnerQcdb);
            // 誘電体製版仕様
            String yuSeihan = loadJoken(sekkeiData.get("SEKKEINO").toString(), YUDENTAI_SEIHAN_KOTEIMEI, YUDENTAI_SEIHAN_KOMOKUMEI, YUDENTAI_SEIHAN_KANRIKOMOKU, queryRunnerQcdb);
            // 固着ｼｰﾄ
            String kochakuSite = loadJoken(sekkeiData.get("SEKKEINO").toString(), KOCHAKU_SITE_KOTEIMEI, KOCHAKU_SITE_KOMOKUMEI, KOCHAKU_SITE_KANRIKOMOKU, queryRunnerQcdb);
            // 号機
            String goki = loadJoken(sekkeiData.get("SEKKEINO").toString(), SEKISO_GOKI_KOTEIMEI, SEKISO_GOKI_KOMOKUMEI, SEKISO_GOKI_KANRIKOMOKU, queryRunnerQcdb);

            // 取得した項目を画面にｾｯﾄする
            setViewItemData(sekkeiData, lotKbnMasData, ownerMasData, yuPaste, petFilem, eaPaste, eaSeihan, sekiSlide, yuSeihanmei, yuSeihan, kochakuSite, ueCoverTape1, ueCoverTape2, shitaCoverTape1, shitaCoverTape2, rollno, goki);
        } catch(SQLException ex) {
        }
        
    }

    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        String sql = "SELECT SEKKEINO,HINMEI,SETSUU,ABSlide,TOKUISAKI,OWNER,KUBUN1,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,SCRNLOT,KOUSA,TORIKOSUU,"
                + "YOUTO1,YOUTO2,YOUTO3,YOUTO4,YOUTO5,YOUTO6,YOUTO7,YOUTO8,"
                + "SYURUI1,SYURUI2,SYURUI3,SYURUI4,SYURUI5,SYURUI6,SYURUI7,SYURUI8,"
                + "ATUMI1,ATUMI2,ATUMI3,ATUMI4,ATUMI5,ATUMI6,ATUMI7,ATUMI8,"
                + "MAISUU1,MAISUU2,MAISUU3,MAISUU4,MAISUU5,MAISUU6,MAISUU7,MAISUU8,"
                + "PATTERN,ROLLNO1,ROLLNO2,ROLLNO3,ROLLNO4,ROLLNO5,ROLLNO6,ROLLNO7,ROLLNO8,CSIYOU,LASTLAYERSLIDERYO,ELOT "
                + "FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    /**
     * 段取用設計仕様印刷検索
     * 
     * @return 遷移先URL文字列
     */
    public String sekkeiSerchPrint() {

        try {
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // ロットNoのチェック
            if (LOTNO_BYTE != StringUtil.getByte(getLotNo(), CHARSET, LOGGER)) {
                // エラーメッセージ
                addErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                return null;
            }
            // プロセスから遷移先の画面を判定する
            if (processSearch()) {
                session.setAttribute("lotNo", getLotNo());
                return RHAPS_URL;
            }
            session.setAttribute("lotNo", getLotNo());
            return PRINT_URL;
        } catch (SQLException e) {
            // エラーメッセージ
            return null;
        }
    }

    /**
     * 段取用設計仕様積層検索
     * 
     * @return 遷移先URL文字列
     */
    public String sekkeiSerchSekiso() {

        try {
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // ロットNoのチェック
            if (LOTNO_BYTE != StringUtil.getByte(getLotNo(), CHARSET, LOGGER)) {
                // エラーメッセージ
                addErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                return null;
            }
            // プロセスから遷移先の画面を判定する
            if (processSearch()) {
                session.setAttribute("lotNo", getLotNo());
                return RHAPS_URL;
            }
            session.setAttribute("lotNo", getLotNo());
            return SEKISO_URL;
        } catch (SQLException e) {
            // エラーメッセージ
            return null;
        }
    }


    /**
     * 段取用設計仕様印刷検索
     * 
     * @return 遷移先URL文字列
     * @throws java.sql.SQLException
     */
    public boolean processSearch() throws SQLException {
        
        boolean res = false;
        
        // ﾌﾟﾘﾝﾄﾌｫｰﾏｯﾄの検索
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
        // 検索用ロットNo
        String lotNo1 = getLotNo().substring(0, 3);
        String lotNo2 = getLotNo().substring(3, 11);
        
        // SQL生成
        String sql = "SELECT PRINTFMT AS printfmt"
                + " FROM DA_SEKKEI "
                + "WHERE KOJYO = ? "
                + "AND LOTNO = ? "
                + "AND EDABAN = '001';";
        
        // パラメータの設定
        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sekkeiData = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        if (sekkeiData == null || sekkeiData.isEmpty()) {
            // ｴﾗｰﾒｯｾｰｼﾞを画面に表示する
            addErrorMessage(MessageUtil.getMessage("XHD-000014"));
            return res;
        }
        String printFmt = StringUtil.nullToBlank(sekkeiData.get("printfmt"));
        // 画面遷移
        if (printFmt.equals("LotCardForm_HAPS_OLD.xlsm") 
                || printFmt.equals("LotCardForm_RHAPS.xlsm")) {

            // RHAPS用の画面に遷移
            res = true;
        }

        return res;        
    }
    
    /**
     * 製造条件マスタ
     * 
     * @param sekkeino
     * @param koteimei
     * @param koumokumei
     * @param kanrikoumoku
     * @param queryRunnerQcdb
     * @return 遷移先URL文字列
     * @throws java.sql.SQLException
     */
    public String loadJoken(String sekkeino, String koteimei, String koumokumei, String kanrikoumoku, QueryRunner queryRunnerQcdb) throws SQLException {
        
        // 検索用ロットNo
        
        // SQL生成
        String sql = "SELECT KIKAKUCHI AS kikakuchi"
                + " FROM DA_JOKEN "
                + "WHERE SEKKEINO = ? "
                + "AND KOUTEIMEI = ? "
                + "AND KOUMOKUMEI = ? "
                + "AND KANRIKOUMOKU = ?;";
        
        // パラメータの設定
        List<Object> params = new ArrayList<>();
        params.add(sekkeino);
        params.add(koteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sekkeiData = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        if (sekkeiData == null || sekkeiData.isEmpty()) {
            return "";
        }
        String kikakuchi = StringUtil.nullToBlank(sekkeiData.get("kikakuchi"));
        
        return StringUtil.blankToNull(kikakuchi);
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
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotKubunCode ﾛｯﾄ区分ｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerDoc, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT lotkubuncode, lotkubun "
                + "FROM lotkumas "
                + "WHERE lotkubuncode = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
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

    /**
     * [ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param ownerCode ｵｰﾅｰｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadOwnerMas(QueryRunner queryRunnerWip, String ownerCode) throws SQLException {

        // オーナーデータの取得
        String sql = "SELECT \"owner\" AS ownername "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 取得したデータを画面項目に設定
     *
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     */
    private void setViewItemData(Map sekkeiData, Map lotKbnMasData, Map ownerMasData, String yuPaste, String petFilem, String eaPaste, 
            String eaSeihan, String sekiSlide, String yuSeihanmei, String yuSeihan, String kochakuSite, String ueCoverTape1, String ueCoverTape2, String shitaCoverTape1, String shitaCoverTape2, String rollno, String goki) {

        // ﾛｯﾄNo
        getData().setLotno(lotNo);
        // KCPNO
        getData().setHinmei(StringUtil.nullToBlank(getMapData(sekkeiData, "HINMEI")));
        // ｾｯﾄ数
        getData().setSetsuu(StringUtil.nullToBlank(getMapData(sekkeiData, "SETSUU")));
        // 客先
        getData().setTokuisaki(StringUtil.nullToBlank(getMapData(sekkeiData, "TOKUISAKI")));
        // ﾛｯﾄ区分
        getData().setKubun1(StringUtil.nullToBlank(getMapData(sekkeiData, "KUBUN1"))
                + ":" + StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun")));
        // 電極ﾃｰﾌﾟ上段
        getData().setEtapetop(StringUtil.nullToBlank(getMapData(sekkeiData, "GENRYOU"))
                + " " + StringUtil.nullToBlank(getMapData(sekkeiData, "ETAPE")) + " " + StringUtil.nullToBlank(getMapData(sekkeiData, "ELOT")));
        // 電極ﾃｰﾌﾟ下段
        getData().setEtapebuttom(StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");
        // 最上層
        getData().setAbslide(StringUtil.nullToBlank(getMapData(sekkeiData, "LASTLAYERSLIDERYO")));
        // 印刷ﾛｰﾙNo
        getData().setRollno(rollno);
        // ｶﾊﾞｰﾃｰﾌﾟ仕様
        getData().setCovertape(StringUtil.nullToBlank(getMapData(sekkeiData, "CSIYOU")));
        // 上ｶﾊﾞｰﾃｰﾌﾟ仕様1
        getData().setCtcovertape1(ueCoverTape1);
        // 上ｶﾊﾞｰﾃｰﾌﾟ仕様2
        getData().setStcovertape2(ueCoverTape2);
        // 下ｶﾊﾞｰﾃｰﾌﾟ仕様1
        getData().setCbcovertape1(shitaCoverTape1);
        // 下ｶﾊﾞｰﾃｰﾌﾟ仕様2
        getData().setSbcovertape2(shitaCoverTape2);
        // 誘電体ﾍﾟｰｽﾄ
        getData().setYpaste(yuPaste);
        // PETﾌｨﾙﾑ種類
        getData().setPetfilm(petFilem);
        // 固着ｼｰﾄ
        getData().setKochakusite(kochakuSite);
        // ｵｰﾅｰ
        getData().setOwner(StringUtil.nullToBlank(getMapData(sekkeiData, "OWNER"))
                + ":" + StringUtil.nullToBlank(getMapData(ownerMasData, "ownername")));
        // 電極ﾍﾟｰｽﾄ
        getData().setEpaste(eaPaste);
        // 電極製版名
        getData().setEseihanmei(StringUtil.nullToBlank(getMapData(sekkeiData, "PATTERN")));
        // 電極製版ﾛｯﾄ
        getData().setScrnlot(StringUtil.nullToBlank(getMapData(sekkeiData, "SCRNLOT")));
        // 電極製版仕様
        getData().setEseihan(eaSeihan);
        // 積層ｽﾗｲﾄﾞ量
        if ("0.0".equals(StringUtil.nullToBlank(getMapData(sekkeiData, "Abslide")))) {
        getData().setSekislide(sekiSlide);
        } else {
            getData().setSekislide(StringUtil.nullToBlank(getMapData(sekkeiData, "Abslide")));
        }
        // 誘電体製版名
        getData().setYseihanmei(yuSeihanmei);
        // 誘電体製版仕様
        getData().setYseihan(yuSeihan);
        // 指定公差
        getData().setKousa(StringUtil.nullToBlank(getMapData(sekkeiData, "KOUSA")));
        // 取り個数
        getData().setTorikosuu(StringUtil.nullToBlank(getMapData(sekkeiData, "TORIKOSUU")));
        // 個数
        String setsuu = StringUtil.nullToBlank(getMapData(sekkeiData, "SETSUU"));
        String torikosuu = StringUtil.nullToBlank(getMapData(sekkeiData, "TORIKOSUU"));
        if (StringUtil.isEmpty(setsuu) || "0".equals(setsuu) 
                || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            getData().setKosuu("0");
        } else {
            BigDecimal decSetsuu = new BigDecimal(setsuu);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal kosuu = decSetsuu.multiply(decTorikosuu);
            getData().setKosuu(kosuu.toPlainString());

        }
        //号機
        getData().setGoki(goki);
    }
        /**
     * 設計データ関連付けマップ取得(用途関連)
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiYotoAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("YOUTO1", "用途1");
                put("YOUTO2", "用途2");
                put("YOUTO3", "用途3");
                put("YOUTO4", "用途4");
                put("YOUTO5", "用途5");
                put("YOUTO6", "用途6");
                put("YOUTO7", "用途7");
                put("YOUTO8", "用途8");
                put("SYURUI1", "種類1");
                put("SYURUI2", "種類2");
                put("SYURUI3", "種類3");
                put("SYURUI4", "種類4");
                put("SYURUI5", "種類5");
                put("SYURUI6", "種類6");
                put("SYURUI7", "種類7");
                put("SYURUI8", "種類8");
                put("ATUMI1", "厚み1");
                put("ATUMI2", "厚み2");
                put("ATUMI3", "厚み3");
                put("ATUMI4", "厚み4");
                put("ATUMI5", "厚み5");
                put("ATUMI6", "厚み6");
                put("ATUMI7", "厚み7");
                put("ATUMI8", "厚み8");
                put("MAISUU1", "枚数1");
                put("MAISUU2", "枚数2");
                put("MAISUU3", "枚数3");
                put("MAISUU4", "枚数4");
                put("MAISUU5", "枚数5");
                put("MAISUU6", "枚数6");
                put("MAISUU7", "枚数7");
                put("MAISUU8", "枚数8");
                put("ROLLNO1", "ﾛｰﾙNo1");
                put("ROLLNO2", "ﾛｰﾙNo2");
                put("ROLLNO3", "ﾛｰﾙNo3");
                put("ROLLNO4", "ﾛｰﾙNo4");
                put("ROLLNO5", "ﾛｰﾙNo5");
                put("ROLLNO6", "ﾛｰﾙNo6");
                put("ROLLNO7", "ﾛｰﾙNo7");
                put("ROLLNO8", "ﾛｰﾙNo8");
            }
        };

        return map;
    }

    /**
     * 用途関連付けマップ取得
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapYoutoAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("YOUTO1", "用途1");
                put("YOUTO2", "用途2");
                put("YOUTO3", "用途3");
                put("YOUTO4", "用途4");
                put("YOUTO5", "用途5");
                put("YOUTO6", "用途6");
                put("YOUTO7", "用途7");
                put("YOUTO8", "用途8");
            }
        };

        return map;
    }
    /**
     * 関連付けMapに定義されている項目が用途データで['CT','CB']が存在しない場合エラーとしエラー情報を返す
     * ※関連付けMapには設計データに持っている項目IDが設定されていること
     *
     * @param sekkeiData 設計データ
     * @param mapYoutoAssociation 用途関連付けMap
     * @param youtoType 用途データ型
     * @param mapSekkeiAssociation 設計データ関連付けMap
     * @return エラーメッセージリスト
     */
    public List<String> checkYoutoItems(Map<String, String> sekkeiData,
            Map<String, String> mapYoutoAssociation, String youtoType, Map<String, String> mapSekkeiAssociation) {

        List<String> retListData = new ArrayList<>();
        boolean checkExistFlag = false;
        boolean checkCTCBExistFlag = false;
        String sekkeiDataKey = "";

        for (Map.Entry<String, String> entry : mapYoutoAssociation.entrySet()) {
            String checkData = "";
            if (sekkeiData.get(entry.getKey()) != null) {
                checkData = String.valueOf(sekkeiData.get(entry.getKey()));
            }

            if (youtoType.equals(checkData)) {
                checkExistFlag = true;
                sekkeiDataKey = entry.getKey();
                break;
            }
        }

        if (!checkExistFlag) {
            retListData.add("ERROR");
            return retListData;
        }

        String sekkeiDataRowNo = sekkeiDataKey.substring(5);
        String syuruiDataKey = "SYURUI" + sekkeiDataRowNo;
        String atumiDataKey = "ATUMI" + sekkeiDataRowNo;
        String maisuuDataKey = "MAISUU" + sekkeiDataRowNo;
        String rollnoDataKey = "ROLLNO" + sekkeiDataRowNo;

        String checkSyuruiData = StringUtil.nullToBlank(sekkeiData.get(syuruiDataKey));
        String checkAtumiData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(atumiDataKey)));
        String checkMaisuuData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(maisuuDataKey)));
        String checkRollnoData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(rollnoDataKey)));

        if ("CT".equals(youtoType) || "CB".equals(youtoType) || "ST".equals(youtoType) || "SB".equals(youtoType)) {
            if ("".equals(checkSyuruiData) || "null".equals(checkSyuruiData)) {
                checkCTCBExistFlag = true;
                retListData.add("ERROR");
            }

            if ("".equals(checkAtumiData) || "null".equals(checkAtumiData)) {
                if (!checkCTCBExistFlag) {
                    retListData.add("ERROR");
                    checkCTCBExistFlag = true;
                }
            }

            if ("".equals(checkMaisuuData) || "null".equals(checkMaisuuData)) {
                if (!checkCTCBExistFlag) {
                    retListData.add("ERROR");
                    checkCTCBExistFlag = true;
                }
            }

            if (!checkCTCBExistFlag) {
                retListData.add("OK");
                String retCTCBValue = checkSyuruiData + "  " + checkAtumiData + "μm×" + checkMaisuuData + "枚";
                retListData.add(retCTCBValue);
            }
            return retListData;
        }
        
        if ("EA".equals(youtoType)) {
            if ("".equals(checkRollnoData) || "null".equals(checkRollnoData)) {
                retListData.add("ERROR");
            } else {
                retListData.add("OK");
                retListData.add(checkRollnoData);
            }

        }

        return retListData;
    }

        /**
     * //エラーメッセージ追加処理
     *
     * @param errorMessage エラーメッセージ
     */
    private void addErrorMessage(String errorMessage) {
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * ロットNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * ロットNo
     *
     * @param lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    
    /**
     * @return the data
     */
    public GXHDO301AModel getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(GXHDO301AModel data) {
        this.data = data;
    }

}
