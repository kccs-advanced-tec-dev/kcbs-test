/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.SrKoteifuryo;
import jp.co.kccs.xhd.db.model.SrKoteifuryoKekka;
import jp.co.kccs.xhd.db.model.SrKoteifuryoSiji;
import jp.co.kccs.xhd.model.GXHDO101D001Model;
import jp.co.kccs.xhd.model.GXHDO101D001Model.KoteifuryoSiji;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

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
     * 品質確認連絡書も出る
     */
    private GXHDO101D001Model gxhdo101d001Model;

    /**
     * コンストラクタ
     */
    public GXHDO101D001() {

    }

    /**
     * 初期化処理
     */
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

        // sessionで渡されたB･Cﾗﾝｸ連絡書一覧画面からの引数をbeanに格納する
        // 登録NoIndex
        setTorokuNoIndex((String) session.getAttribute("torokuNoIndex"));
        // 登録No配列 String → String[] に変換
        String[] tmpTorokuNoArray = ((String) session.getAttribute("torokuNoArrayStr")).split(",");
        setTorokuNoArray(tmpTorokuNoArray);
        // 現在の登録Noを取得
        setCurrentTorokuNoValue(findTorokuNoFromIndex(getTorokuNoIndex()));
        // Model初期化
        GXHDO101D001Model model = new GXHDO101D001Model();
        setGxhdo101d001Model(model);

        // 工程不良テーブル作成
        createKoteifuryoTable();
        // 工程不良指示テーブル作成
        createKoteifuryosijiTable();
        // 工程不良結果テーブル作成
        createKoteifuryokekkaTable();

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
            List<SrKoteifuryo> listSrKoteifuryo = getSrKoteifuryoList(queryRunnerQcdb, findTorokuNoFromIndex(torokuNoIndex));

            // 取得したテーブル情報を画面表示モデルに設定する
            GXHDO101D001Model model = getGxhdo101d001Model();

            // 工程不良テーブルは1行である前提
            SrKoteifuryo rowKoteifuryo = listSrKoteifuryo.get(0);

            // 必要な値を設定する
            model.setRank(rowKoteifuryo.getRank()); // KF.ﾗﾝｸ
            model.setHakkobi(rowKoteifuryo.getHakkobi().toString()); // KF.発行日
            model.setHakkenkotei(rowKoteifuryo.getHakkenkotei()); // KF.発見工程ID
            model.setTorokuno(rowKoteifuryo.getTorokuno()); // KF.登録No
            model.setKcpno(rowKoteifuryo.getKcpno()); // KF.KCPNO
            model.setSikasuuryo(Integer.toString(rowKoteifuryo.getSikasuuryo())); // KF.仕掛数 // ★TODO: 型確認
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
            model.setQakakuninnichiji(rowKoteifuryo.getQakakuninnichiji().toString()); // KF.日付 //TODO:対応カラム確認 (torokunitiji, koshinnichijiのどちらか？)

            // 取得結果を保存
            setGxhdo101d001Model(model);

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
            // 実績Noの最大値
            String maxJissekiNo = findMaxJissekiNoFromKoteifuryoSiji(getCurrentTorokuNoValue());
            // (4)[工程不良指示]から、初期表示する情報を取得
            List<SrKoteifuryoSiji> koteifuryosijiList = getSrKoteifuryoSiji(queryRunnerQcdb, getCurrentTorokuNoValue(), maxJissekiNo);

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
            // 実績Noの最大値
            String maxJissekiNo = getMaxJissekiNoFromSrKoteifuryoKekka(queryRunnerQcdb, getCurrentTorokuNoValue()).toString();
            // (6)[工程不良結果]から、初期表示する情報を取得
            List<SrKoteifuryoKekka> koteifuryokekkaList = createKoteifuryoKekkaTable(getCurrentTorokuNoValue(), maxJissekiNo);

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

    private void createKoteifuryoTable(String torokuNo) {
        try {

            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

            // 工程不良テーブルを取得する
            List<SrKoteifuryo> listSrKoteifuryo = getSrKoteifuryoList(queryRunnerQcdb, findTorokuNoFromIndex(torokuNoIndex));

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("工程不良テーブル取得エラー", ex, LOGGER);
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
            BigDecimal maxNo = getMaxJissekiNoFromSrKoteifuryoSiji(queryRunnerQcdb, torokuNo);
            return maxNo.toString();
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実績No最大値取得エラー", ex, LOGGER);
        }
        return "";
    }

    private List<SrKoteifuryoKekka> createKoteifuryoKekkaTable(String torokuNo, String maxJissekiNo) {
        try {

            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

            // 工程不良テーブルを取得する
            List<SrKoteifuryoKekka> listSrKoteifuryo = getSrKoteifuryoKekka(queryRunnerQcdb, torokuNo, maxJissekiNo);

            return listSrKoteifuryo;

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("工程不良テーブル取得エラー", ex, LOGGER);
        }
        return null;
    }

    /**
     * 登録No配列のIndexから登録Noの値を取得する
     *
     * @param torokuNoIndex
     * @return
     */
    private String findTorokuNoFromIndex(String torokuNoIndex) {
        return this.torokuNoArray[Integer.parseInt(torokuNoIndex)];
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
    private BigDecimal getMaxJissekiNoFromSrKoteifuryoSiji(QueryRunner queryRunnerXHD, String torokuNo) throws SQLException {
        BigDecimal maxJissekiNo = BigDecimal.ZERO;
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM sr_koteifuryo_siji "
                + "WHERE torokuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);

        Map map = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            maxJissekiNo = new BigDecimal(String.valueOf(map.get("jissekino")));
        }

        return maxJissekiNo;
    }

    private List<SrKoteifuryoSiji> getSrKoteifuryoSiji(QueryRunner queryRunnerXHD, String torokuNo, String maxJissekiNo) throws SQLException {
        String sql = "SELECT * "
                + "FROM sr_koteifuryo_siji "
                + "WHERE torokuno = ? AND jissekino = ? "
                + "ORDER BY torokuno ";
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
    private BigDecimal getMaxJissekiNoFromSrKoteifuryoKekka(QueryRunner queryRunnerXHD, String torokuNo) throws SQLException {
        BigDecimal maxJissekiNo = BigDecimal.ZERO;
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM sr_koteifuryo_kekka "
                + "WHERE torokuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(torokuNo);

        Map map = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            maxJissekiNo = new BigDecimal(String.valueOf(map.get("jissekino")));
        }

        return maxJissekiNo;
    }

    private List<SrKoteifuryoKekka> getSrKoteifuryoKekka(QueryRunner queryRunnerXHD, String torokuNo, String maxJissekiNo) throws SQLException {
        String sql = "SELECT * "
                + "FROM sr_koteifuryo_kekka "
                + "WHERE torokuno = ? AND jissekino = ? "
                + "ORDER BY torokuno ";
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
}
