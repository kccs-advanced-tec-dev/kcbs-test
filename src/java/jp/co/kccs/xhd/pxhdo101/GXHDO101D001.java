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

        /**
         * 以下はBean化前の処理 *
         */
        // (1)初期表示
        // 工程不良から取得する項目
        // 登録Noの値
        String torokuNoValue = findTorokuNoFromIndex(torokuNoIndex);
        createKoteifuryoTable(torokuNoValue);
        // 実績Noの最大値
        String maxJissekiNo = findMaxJissekiNoFromKoteifuryoSiji(torokuNoValue);
        // (4)[工程不良指示]から、初期表示する情報を取得
        List<SrKoteifuryoSiji> koteifuryosijiList = new ArrayList<>();
        try {
            koteifuryosijiList = getSrKoteifuryoSiji(queryRunnerQcdb, torokuNoValue, maxJissekiNo);
            for (SrKoteifuryoSiji koteifuryo : koteifuryosijiList) {
                LOGGER.info(koteifuryo.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String maxJissekiNoFromKekka = getMaxJissekiNoFromSrKoteifuryoKekka(queryRunnerQcdb, torokuNoValue).toString();
            LOGGER.info(maxJissekiNoFromKekka);
        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<SrKoteifuryoKekka> koteifuryokekkaList = createKoteifuryoKekkaTable(torokuNoValue, maxJissekiNo);
        for (SrKoteifuryoKekka koteifuryokekka : koteifuryokekkaList) {
            LOGGER.info(koteifuryokekka.toString());
        }

    }

    /**
     * 初期表示 ①工程不良から取得する項目 画面表示仕様(1)～(2)を発行する
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

            //TODO: 残りの画面項目を設定
            // 取得結果を保存
            setGxhdo101d001Model(model);

        } catch (SQLException ex) {
            Logger.getLogger(GXHDO101D001.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 初期表示 ②工程不良指示から取得する項目 画面表示仕様(3)～(4)を発行する。
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
