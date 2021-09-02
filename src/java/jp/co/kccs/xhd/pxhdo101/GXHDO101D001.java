/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.db.model.SrKoteifuryo;
import jp.co.kccs.xhd.db.model.SrKoteifuryoKekka;
import jp.co.kccs.xhd.db.model.SrKoteifuryoSiji;
import jp.co.kccs.xhd.model.GXHDO101D001Model;
import jp.co.kccs.xhd.model.GXHDO101D001Model.KoteifuryoSiji;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101D001(B･Cﾗﾝｸ発行)
 *
 * @author SRC K.Ijuin
 * @since 2021/08/21
 */
@SessionScoped
@Named("beanGXHDO101D001")
public class GXHDO101D001 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101D001.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceXHD;

    /**
     * B･Cﾗﾝｸ連絡書一覧画面からの引数: 登録No配列のIndex
     */
    private String torokuNoIndex;

    /**
     * B･Cﾗﾝｸ連絡書一覧画面からの引数: 登録No配列
     */
    private String[] torokuNoArray;

    /**
     * 現在の登録Noの値
     */
    private String currentTorokuNoValue;

    /**
     * [工程不良指示]から取得した実績Noの最大値
     */
    private String maxJissekiNoKS;

    /**
     * [工程不良結果]から取得した実績Noの最大値
     */
    private String maxJissekiNoKK;

    /**
     * ロットNo(検索値)
     */
    private String searchLotNo;

    /**
     * 担当者ｺｰﾄﾞ(検索値)
     */
    private String searchTantoshaCd;

    /**
     * 品質確認連絡書も出る
     */
    private GXHDO101D001Model gxhdo101d001Model;

    /**
     * 表示Render
     */
    private boolean btnPrevRender;

    /**
     * 表示Render
     */
    private boolean btnNextRender;

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * メッセージリスト
     */
    private List<String> messageListGXHDO101D001 = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public GXHDO101D001() {
    }

    /**
     * 初期化処理
     */
    public void init() {
        LOGGER.info("GXHDO101D001 init() called.");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        // session値をbeanに格納する
        // 登録NoIndex
        String tNoIndex = StringUtil.nullToBlank(session.getAttribute("torokuNoIndex"));
        this.torokuNoIndex = tNoIndex;
        LOGGER.info(String.format("登録NoIndex: %s", tNoIndex));
        // 登録No配列
        Object torokuNoArrayObjSession = session.getAttribute("torokuNoArray");
        String[] torokuNoArray = convertFromSessionToStringArray(torokuNoArrayObjSession);
        setTorokuNoArray(torokuNoArray);
        LOGGER.info(String.format("登録No配列: %s", Arrays.toString(torokuNoArray)));
        // ロットNo(検索値)
        String sLotNo = StringUtil.nullToBlank(session.getAttribute("searchLotNo"));
        this.searchLotNo = sLotNo;
        LOGGER.info(String.format("ロットNo(検索値): %s", sLotNo));
        // 担当者ｺｰﾄﾞ(検索値)
        String sTantoshaCd = StringUtil.nullToBlank(session.getAttribute("searchTantoshaCd"));
        this.searchTantoshaCd = sTantoshaCd;
        LOGGER.info(String.format("担当者ｺｰﾄﾞ(検索値): %s", sTantoshaCd));

        // 登録NoIndexが「0」の場合は「前へ」ボタンを表示しない
        if (Integer.valueOf(this.torokuNoIndex) == 0) {
            setBtnPrevRender(false);
        } else {
            setBtnPrevRender(true);
        }
        // 登録NoIndexが配列終端の場合は「次へ」ボタンを表示しない
        if (Integer.valueOf(this.torokuNoIndex) == this.torokuNoArray.length - 1) {
            setBtnNextRender(false);
        } else {
            setBtnNextRender(true);
        }

        // 現在の登録Noを取得
        this.currentTorokuNoValue = this.torokuNoArray[Integer.parseInt(torokuNoIndex)];

        // Model初期化
        GXHDO101D001Model model = new GXHDO101D001Model();
        setGxhdo101d001Model(model);

        // 工程不良テーブル作成
        createKoteifuryoTable();

        // 工程不良テーブル取得に失敗した場合はエラーダイアログを表示させて後続処理を中断
        if (!this.messageListGXHDO101D001.isEmpty()) {
            LOGGER.warning("GXHDO101D001 工程不良テーブル取得失敗");
            // メッセージを画面に渡す
            InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
            beanInitMessage.setInitMessageList(this.getMessageListGXHDO101D001());
//            RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
            RequestContext context = RequestContext.getCurrentInstance();
//            context.addCallbackParam("firstParam", "initMessage");
            context.addCallbackParam("firstParam", "error");
            return;
        }

        // 工程不良指示テーブル作成
        createKoteifuryosijiTable();
        // 工程不良結果テーブル作成
        createKoteifuryokekkaTable();

    }

    /**
     * sessionで受信したObjectを送信時のString[]配列に変換する
     *
     * @param obj
     * @return
     */
    private String[] convertFromSessionToStringArray(Object obj) {
        Object[] objArr = convertToObjectArray(obj);
        String[] strArr = convertToStringArray(objArr);
        return strArr;
    }

    /**
     * Objectを配列Object[] に変換
     *
     * @param array セッションから受け取ったObject
     * @return
     */
    private Object[] convertToObjectArray(Object array) {
        Class ofArray = array.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                ar.add(Array.get(array, i));
            }
            return ar.toArray();
        } else {
            return (Object[]) array;
        }
    }

    /**
     * Object[]配列をString[]配列に変換する
     *
     * @param objArray
     * @return
     */
    private String[] convertToStringArray(Object[] objArray) {

        List<String> strList = new ArrayList<>();
        for (Object obj : objArray) {
            strList.add((String) obj);
        }
        String[] strArray = new String[strList.size()];
        return strList.toArray(strArray);
    }

    /**
     * 初期表示
     * <br>
     * ①工程不良から取得する項目 画面表示仕様(1)～(2)を発行する
     */
    private void createKoteifuryoTable() {
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
        try {
            // 工程不良テーブルを取得する
            List<SrKoteifuryo> listSrKoteifuryo = getSrKoteifuryoList(queryRunnerQcdb, this.torokuNoArray[Integer.parseInt(torokuNoIndex)]);

            // メッセージリスト
            this.setMessageListGXHDO101D001(new ArrayList<>());

            // チェック処理：レコードが取得出来なかった場合
            if (listSrKoteifuryo.isEmpty()) {
                setErrorMessage(MessageUtil.getMessage("XHD-000218"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                List<String> messagelist = new ArrayList<>();
                messagelist.add(getErrorMessage());
                setMessageListGXHDO101D001(messagelist);
                return;
            }

            // 取得したテーブル情報を画面表示モデルに設定する
            GXHDO101D001Model model = getGxhdo101d001Model();

            // 工程不良テーブルは1行である前提
            SrKoteifuryo rowKoteifuryo = listSrKoteifuryo.get(0);

            // 必要な値を設定する
            model.setRank(rowKoteifuryo.getRank()); // KF.ﾗﾝｸ
            model.setHakkobi(rowKoteifuryo.getHakkobi()); // KF.発行日
            model.setTokuisaki(rowKoteifuryo.getTokuisaki()); // KF.tokuisaki
            model.setHakkenkotei(rowKoteifuryo.getHakkenkotei()); // KF.発見工程ID
            model.setTorokuno(rowKoteifuryo.getTorokuno()); // KF.登録No
            model.setKcpno(rowKoteifuryo.getKcpno()); // KF.KCPNO
            model.setSikasuuryo(rowKoteifuryo.getSikasuuryo()); // KF.仕掛数
            model.setOwnercode(rowKoteifuryo.getOwnercode()); // KF.OWNERｺｰﾄﾞ
            model.setLotkubuncode(rowKoteifuryo.getLotkubuncode()); // KF.Lot区分
            model.setLotno(rowKoteifuryo.getLotno()); // KF.製造LotNo
            model.setGoukicode(rowKoteifuryo.getGoukicode()); // KF.号機ｺｰﾄﾞ

            model.setFuryocode1(rowKoteifuryo.getFuryocode1()); // KF.不良ｺｰﾄﾞ1
            model.setFuryocode2(rowKoteifuryo.getFuryocode2()); // KF.不良ｺｰﾄﾞ2
            model.setFuryocode3(rowKoteifuryo.getFuryocode3()); // KF.不良ｺｰﾄﾞ3
            model.setFuryocode4(rowKoteifuryo.getFuryocode4()); // KF.不良ｺｰﾄﾞ4
            model.setFuryocode5(rowKoteifuryo.getFuryocode5()); // KF.不良ｺｰﾄﾞ5
            model.setFuryocode6(rowKoteifuryo.getFuryocode6()); // KF.不良ｺｰﾄﾞ6
            model.setFuryocode7(rowKoteifuryo.getFuryocode7()); // KF.不良ｺｰﾄﾞ7
            model.setFuryocode8(rowKoteifuryo.getFuryocode8()); // KF.不良ｺｰﾄﾞ8
            model.setFuryocode9(rowKoteifuryo.getFuryocode9()); // KF.不良ｺｰﾄﾞ9
            model.setFuryocode10(rowKoteifuryo.getFuryocode10()); // KF.不良ｺｰﾄﾞ10

            model.setFuryomeisai1(rowKoteifuryo.getFuryomeisai1()); // KF.不良名1
            model.setFuryomeisai2(rowKoteifuryo.getFuryomeisai2()); // KF.不良名2
            model.setFuryomeisai3(rowKoteifuryo.getFuryomeisai3()); // KF.不良名3
            model.setFuryomeisai4(rowKoteifuryo.getFuryomeisai4()); // KF.不良名4
            model.setFuryomeisai5(rowKoteifuryo.getFuryomeisai5()); // KF.不良名5
            model.setFuryomeisai6(rowKoteifuryo.getFuryomeisai6()); // KF.不良名6
            model.setFuryomeisai7(rowKoteifuryo.getFuryomeisai7()); // KF.不良名7
            model.setFuryomeisai8(rowKoteifuryo.getFuryomeisai8()); // KF.不良名8
            model.setFuryomeisai9(rowKoteifuryo.getFuryomeisai9()); // KF.不良名9
            model.setFuryomeisai10(rowKoteifuryo.getFuryomeisai10()); // KF.不良名10

            model.setFuryoritiu1(rowKoteifuryo.getFuryoritiu1()); // KF.不良率1
            model.setFuryoritiu2(rowKoteifuryo.getFuryoritiu2()); // KF.不良率2
            model.setFuryoritiu3(rowKoteifuryo.getFuryoritiu3()); // KF.不良率3
            model.setFuryoritiu4(rowKoteifuryo.getFuryoritiu4()); // KF.不良率4
            model.setFuryoritiu5(rowKoteifuryo.getFuryoritiu5()); // KF.不良率5
            model.setFuryoritiu6(rowKoteifuryo.getFuryoritiu6()); // KF.不良率6
            model.setFuryoritiu7(rowKoteifuryo.getFuryoritiu7()); // KF.不良率7
            model.setFuryoritiu8(rowKoteifuryo.getFuryoritiu8()); // KF.不良率8
            model.setFuryoritiu9(rowKoteifuryo.getFuryoritiu9()); // KF.不良率9
            model.setFuryoritiu10(rowKoteifuryo.getFuryoritiu10()); // KF.不良率10

            model.setFuryobunrui1(rowKoteifuryo.getFuryobunrui1()); // KF.詳細内容1
            model.setFuryobunrui2(rowKoteifuryo.getFuryobunrui2()); // KF.詳細内容2
            model.setFuryobunrui3(rowKoteifuryo.getFuryobunrui3()); // KF.詳細内容3
            model.setFuryobunrui4(rowKoteifuryo.getFuryobunrui4()); // KF.詳細内容4
            model.setFuryobunrui5(rowKoteifuryo.getFuryobunrui5()); // KF.詳細内容5
            model.setFuryobunrui6(rowKoteifuryo.getFuryobunrui6()); // KF.詳細内容6
            model.setFuryobunrui7(rowKoteifuryo.getFuryobunrui7()); // KF.詳細内容7
            model.setFuryobunrui8(rowKoteifuryo.getFuryobunrui8()); // KF.詳細内容8
            model.setFuryobunrui9(rowKoteifuryo.getFuryobunrui9()); // KF.詳細内容9
            model.setFuryobunrui10(rowKoteifuryo.getFuryobunrui10()); // KF.詳細内容10

            model.setZaikono1(rowKoteifuryo.getZaikono1()); // KF.在庫No1
            model.setZaikono2(rowKoteifuryo.getZaikono2()); // KF.在庫No2
            model.setZaikono3(rowKoteifuryo.getZaikono3()); // KF.在庫No3
            model.setZaikono4(rowKoteifuryo.getZaikono4()); // KF.在庫No4
            model.setZaikono5(rowKoteifuryo.getZaikono5()); // KF.在庫No5
            model.setZaikono6(rowKoteifuryo.getZaikono6()); // KF.在庫No6
            model.setZaikono7(rowKoteifuryo.getZaikono7()); // KF.在庫No7
            model.setZaikono8(rowKoteifuryo.getZaikono8()); // KF.在庫No8
            model.setZaikono9(rowKoteifuryo.getZaikono9()); // KF.在庫No9
            model.setZaikono10(rowKoteifuryo.getZaikono10()); // KF.在庫No10

            model.setQakakuninsya(rowKoteifuryo.getQakakuninsya()); // KF.確認者
            model.setQakakuninnichiji(rowKoteifuryo.getQakakuninnichiji()); // KF.日付

            // 取得結果を保存
            setGxhdo101d001Model(model);

            // 不良判定の設定
            model.setHuryoDisp();

        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 初期表示
     * <br>
     * ②工程不良指示から取得する項目 画面表示仕様(3)～(4)を発行する。
     *
     */
    private void createKoteifuryosijiTable() {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
            // (3)[工程不良指示]から取得した実績Noの最大値
            setMaxJissekiNoKS(findMaxJissekiNoFromKoteifuryoSiji(getCurrentTorokuNoValue()));
            // (4)[工程不良指示]から、初期表示する情報を取得
            List<SrKoteifuryoSiji> koteifuryosijiList = getSrKoteifuryoSiji(queryRunnerQcdb, getCurrentTorokuNoValue(), getMaxJissekiNoKS());

            // 取得したテーブル情報を画面表示モデルに設定する
            GXHDO101D001Model model = getGxhdo101d001Model();

            // 必要な値を設定する
            List<GXHDO101D001Model.KoteifuryoSiji> sijiList = new ArrayList<>();
            for (SrKoteifuryoSiji row : koteifuryosijiList) {
                GXHDO101D001Model.KoteifuryoSiji item = (new GXHDO101D001Model()).new KoteifuryoSiji();
                item.setFuryono(row.getFuryono());
                item.setSijisyacode(row.getSijisyacode());
                item.setSijibi(row.getSijibi());
                item.setSyochikotei(row.getSyochikotei());
                item.setSijinaiyo(row.getSijinaiyo());
                sijiList.add(item);
            }

            // 画面表示モデルにセット
            model.setSijiList(sijiList);

        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 初期表示
     * <br>
     * ③工程不良結果から取得する項目 画面表示仕様(5)～(6)を発行する。
     */
    public void createKoteifuryokekkaTable() {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
            // (5)[工程不良結果]から取得した実績Noの最大値
            setMaxJissekiNoKK(getMaxJissekiNoFromSrKoteifuryoKekka(queryRunnerQcdb, this.currentTorokuNoValue));
            // (6)[工程不良結果]から、初期表示する情報を取得
            List<SrKoteifuryoKekka> koteifuryokekkaList = getSrKoteifuryoKekka(queryRunnerQcdb, this.currentTorokuNoValue, getMaxJissekiNoKK());

            // 取得したテーブル情報を画面表示モデルに設定する
            GXHDO101D001Model model = getGxhdo101d001Model();

            // 必要な値を設定する
            List<GXHDO101D001Model.KoteifuryoKekka> kekkaList = new ArrayList<>();
            for (SrKoteifuryoKekka row : koteifuryokekkaList) {
                GXHDO101D001Model.KoteifuryoKekka item = (new GXHDO101D001Model()).new KoteifuryoKekka();
                item.setFuryono(row.getFuryono());
                item.setSyotisyacode(row.getSyotisyacode());
                item.setSyotibi(row.getSyotibi());
                item.setSyotinaiyo(row.getSyotinaiyo());
                item.setHantei(row.getHantei());
                kekkaList.add(item);
            }

            // 画面表示モデルにセット
            model.setKekkaList(kekkaList);

        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 実績Noの最大値を取得する
     *
     * @param torokuNo
     * @return
     */
    private String findMaxJissekiNoFromKoteifuryoSiji(String torokuNo) {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
            String maxNo = getMaxJissekiNoFromSrKoteifuryoSiji(queryRunnerQcdb, torokuNo);
            return maxNo;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実績No最大値取得エラー", ex, LOGGER);
        }
        return "";
    }

    /**
     * [工程不良]から、初期表示する情報を取得
     *
     * @param queryRunnerXHD
     * @param index
     * @return
     * @throws SQLException
     */
    private List<SrKoteifuryo> getSrKoteifuryoList(QueryRunner queryRunnerXHD, String torokuNo) throws SQLException {
        String sql = "SELECT * "
                + "FROM sr_koteifuryo "
                + "WHERE torokuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);

        Map<String, String> mapping = new HashMap<>();
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrKoteifuryo>> beanHandler = new BeanListHandler<>(SrKoteifuryo.class, rowProcessor);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

        return queryRunnerXHD.query(sql, beanHandler, params.toArray());
    }

    /**
     * 工程不良と紐づく工程不良指示から実績Noの最大値を取得
     *
     * @param queryRunnerXHD
     * @param torokuNo
     * @return
     * @throws SQLException
     */
    private String getMaxJissekiNoFromSrKoteifuryoSiji(QueryRunner queryRunnerXHD, String torokuNo) throws SQLException {
        String maxJissekiNo = "";
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM sr_koteifuryo_siji "
                + "WHERE torokuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map map = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            maxJissekiNo = String.valueOf(map.get("jissekino"));
        }

        return maxJissekiNo;
    }

    /**
     * [工程不良指示]テーブルから初期表示する情報を取得
     *
     * @param queryRunnerXHD
     * @param torokuNo
     * @param maxJissekiNo
     * @return
     * @throws SQLException
     */
    private List<SrKoteifuryoSiji> getSrKoteifuryoSiji(QueryRunner queryRunnerXHD, String torokuNo, String maxJissekiNo) throws SQLException {
        String sql = "SELECT * "
                + "FROM sr_koteifuryo_siji "
                + "WHERE torokuno = ? AND jissekino = ? "
                + "ORDER BY sijino ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);
        params.add(maxJissekiNo);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("torokuno", "torokuno");
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrKoteifuryoSiji>> beanHandler = new BeanListHandler<>(SrKoteifuryoSiji.class, rowProcessor);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerXHD.query(sql, beanHandler, params.toArray());

    }

    /**
     * 工程不良と紐づく工程不良結果から実績Noの最大値を取得
     *
     * @param queryRunnerXHD
     * @param torokuNo
     * @return
     * @throws SQLException
     */
    private String getMaxJissekiNoFromSrKoteifuryoKekka(QueryRunner queryRunnerXHD, String torokuNo) throws SQLException {
        String maxJissekiNo = "";
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM sr_koteifuryo_kekka "
                + "WHERE torokuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map map = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            maxJissekiNo = String.valueOf(map.get("jissekino"));
        }

        return maxJissekiNo;
    }

    /**
     * [工程不良結果]テーブルから初期表示する情報を取得
     *
     * @param queryRunnerXHD
     * @param torokuNo
     * @param maxJissekiNo
     * @return
     * @throws SQLException
     */
    private List<SrKoteifuryoKekka> getSrKoteifuryoKekka(QueryRunner queryRunnerXHD, String torokuNo, String maxJissekiNo) throws SQLException {
        String sql = "SELECT * "
                + "FROM sr_koteifuryo_kekka "
                + "WHERE torokuno = ? AND jissekino = ? "
                + "ORDER BY sijino ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);
        params.add(maxJissekiNo);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("torokuno", "torokuno");
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrKoteifuryoKekka>> beanHandler = new BeanListHandler<>(SrKoteifuryoKekka.class, rowProcessor);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerXHD.query(sql, beanHandler, params.toArray());

    }

    /**
     * 「入力画面へ戻る」押下時
     *
     * @return 遷移先URL
     */
    public String doReturnInput() {
        // セッション情報
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        // ロットNo(検索値)を保持
        String sLotNo = this.searchLotNo;
        session.setAttribute("searchLotNo", sLotNo);
        // 担当者ｺｰﾄﾞ(検索値)を保持
        String sTantoshaCd = this.searchTantoshaCd;
        session.setAttribute("searchTantoshaCd", sTantoshaCd);
        // 入力画面選択の画面にmenuTable再表示フラグを渡す
        session.setAttribute("flgReOpenMenutable", "true");
        // 入力画面選択の画面にB･Cﾗﾝｸ連絡書一覧再表示フラグ「false」を渡す
        session.setAttribute("flgReOpenGXHDOC021", "false");

        return "/secure/pxhdo101/gxhdo101a.xhtml?faces-redirect=true";
    }

    /**
     * 「一覧へ戻る」押下時
     *
     * @return 遷移先URL
     */
    public String doReturnList() {
        // セッション情報
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        // ロットNo(検索値)を保持
        String sLotNo = this.searchLotNo;
        session.setAttribute("searchLotNo", sLotNo);
        // 担当者ｺｰﾄﾞ(検索値)を保持
        String sTantoshaCd = this.searchTantoshaCd;
        session.setAttribute("searchTantoshaCd", sTantoshaCd);
        // 入力画面選択の画面にmenuTable再表示フラグを渡す
        session.setAttribute("flgReOpenMenutable", "true");
        // 入力画面選択の画面にB･Cﾗﾝｸ連絡書一覧再表示フラグ「true」を渡す
        session.setAttribute("flgReOpenGXHDOC021", "true");

        return "/secure/pxhdo101/gxhdo101a.xhtml?faces-redirect=true";
    }

    /**
     * 【前へ】ﾎﾞﾀﾝ押下時
     *
     * @return 画面URL
     */
    public String doPrev() {
        LOGGER.info("GXHDO101D001 doPrev() called.");

        // 現在の登録No配列のIndex
        int currentIndex = Integer.parseInt(getTorokuNoIndex());

        if (currentIndex > 0) {
            // ①B･Cﾗﾝｸ連絡書一覧画面から引き継いだ引数の行数に1減算する。
            int prevIndex = currentIndex - 1;
            LOGGER.info(String.format("doPrev() prevIndex: %d", prevIndex));
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // 登録No配列Index
            session.setAttribute("torokuNoIndex", Integer.toString(prevIndex));
        }
        // ②画面を初期化し、再度初期表示処理を実施する。
        return "/secure/pxhdo101/gxhdo101d001.xhtml?faces-redirect=true";

    }

    /**
     * 【次へ】ﾎﾞﾀﾝ押下時
     *
     * @return 画面URL
     */
    public String doNext() {
        LOGGER.info("GXHDO101D001 doNext() called.");

        // 登録No配列のサイズ
        int torokuNoArrayLength = getTorokuNoArray().length;

        // 現在の登録No配列のIndex
        int currentIndex = Integer.parseInt(getTorokuNoIndex());

        // 配列終端チェック
        if (currentIndex < torokuNoArrayLength - 1) {
            // ①B･Cﾗﾝｸ連絡書一覧画面から引き継いだ引数の行数に1加算する。
            int nextIndex = currentIndex + 1;
            LOGGER.info(String.format("doPrev() nextIndex: %d", nextIndex));
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // 登録No配列Index
            session.setAttribute("torokuNoIndex", Integer.toString(nextIndex));
        }
        // ②画面を初期化し、再度初期表示処理を実施する。
        return "/secure/pxhdo101/gxhdo101d001.xhtml?faces-redirect=true";
    }

    /**
     * @return the searchLotNo
     */
    public String getSearchLotNo() {
        return searchLotNo;
    }

    /**
     * @param searchLotNo the searchLotNo to set
     */
    public void setSearchLotNo(String searchLotNo) {
        this.searchLotNo = searchLotNo;
    }

    /**
     * @return the searchTantoshaCd
     */
    public String getSearchTantoshaCd() {
        return searchTantoshaCd;
    }

    /**
     * @param searchTantoshaCd the searchTantoshaCd to set
     */
    public void setSearchTantoshaCd(String searchTantoshaCd) {
        this.searchTantoshaCd = searchTantoshaCd;
    }

    /**
     * @return the torokuNoIndex
     */
    public String getTorokuNoIndex() {
        return torokuNoIndex;
    }

    /**
     * @param torokuNoIndex the torokuNoIndex to set
     */
    public void setTorokuNoIndex(String torokuNoIndex) {
        this.torokuNoIndex = torokuNoIndex;
    }

    /**
     * @return the tmpTorokuNoArray
     */
    public String[] getTorokuNoArray() {
        return torokuNoArray;
    }

    /**
     * @param torokuNoArray the tmpTorokuNoArray to set
     */
    public void setTorokuNoArray(String[] torokuNoArray) {
        this.torokuNoArray = torokuNoArray;
    }

    /**
     * @return the currentTorokuNoValue
     */
    public String getCurrentTorokuNoValue() {
        return currentTorokuNoValue;
    }

    /**
     * @param currentTorokuNoValue the currentTorokuNoValue to set
     */
    public void setCurrentTorokuNoValue(String currentTorokuNoValue) {
        this.currentTorokuNoValue = currentTorokuNoValue;
    }

    /**
     * @return the gxhdo101d001Model
     */
    public GXHDO101D001Model getGxhdo101d001Model() {
        return gxhdo101d001Model;
    }

    /**
     * @param gxhdo101d001Model the gxhdo101d001Model to set
     */
    public void setGxhdo101d001Model(GXHDO101D001Model gxhdo101d001Model) {
        this.gxhdo101d001Model = gxhdo101d001Model;
    }

    /**
     * @return the maxJissekiNoKS
     */
    public String getMaxJissekiNoKS() {
        return maxJissekiNoKS;
    }

    /**
     * @param maxJissekiNoKS the maxJissekiNoKS to set
     */
    public void setMaxJissekiNoKS(String maxJissekiNoKS) {
        this.maxJissekiNoKS = maxJissekiNoKS;
    }

    /**
     * @return the maxJissekiNoKK
     */
    public String getMaxJissekiNoKK() {
        return maxJissekiNoKK;
    }

    /**
     * @param maxJissekiNoKK the maxJissekiNoKK to set
     */
    public void setMaxJissekiNoKK(String maxJissekiNoKK) {
        this.maxJissekiNoKK = maxJissekiNoKK;
    }

    /**
     * @return the btnPrevRender
     */
    public boolean isBtnPrevRender() {
        return btnPrevRender;
    }

    /**
     * @param btnPrevRender the btnPrevRender to set
     */
    public void setBtnPrevRender(boolean btnPrevRender) {
        this.btnPrevRender = btnPrevRender;
    }

    /**
     * @return the btnNextRender
     */
    public boolean isBtnNextRender() {
        return btnNextRender;
    }

    /**
     * @param btnNextRender the btnNextRender to set
     */
    public void setBtnNextRender(boolean btnNextRender) {
        this.btnNextRender = btnNextRender;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the messageListGXHDO101D001
     */
    public List<String> getMessageListGXHDO101D001() {
        return messageListGXHDO101D001;
    }

    /**
     * @param messageListGXHDO101D001 the messageListGXHDO101D001 to set
     */
    public void setMessageListGXHDO101D001(List<String> messageListGXHDO101D001) {
        this.messageListGXHDO101D001 = messageListGXHDO101D001;
    }
}
