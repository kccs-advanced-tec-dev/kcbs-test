/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo201;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.SelectParam;
import jp.co.kccs.xhd.common.ColumnInfoParser;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.db.model.SrMekki;
import jp.co.kccs.xhd.model.GXHDO201B044Model;
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
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2020/02/20<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒｯｷ膜厚履歴検索画面
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/02/20
 */
@Named
@ViewScoped
public class GXHDO201B044 implements Serializable {

    /**
     * 検索LIMIT値
     */
    private static final int SEARCH_LIMIT = 200;

    private static final Logger LOGGER = Logger.getLogger(GXHDO201B044.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * DataSource(equipment)
     */
    @Resource(mappedName = "jdbc/equipment")
    protected transient DataSource dataSourceEquipment;

    /**
     * パラメータマスタ操作
     */
    @Inject
    private SelectParam selectParam;

    /**
     * 一覧表示データ
     */
    private List<GXHDO201B044Model> listData = null;

    /**
     * 一覧表示最大件数
     */
    private int listCountMax = -1;
    /**
     * 一覧表示警告件数
     */
    private int listCountWarn = -1;

    /**
     * 警告メッセージ
     */
    private String warnMessage = "";
    /**
     * 1ページ当たりの表示件数
     */
    private int listDisplayPageCount = 30;

    /**
     * 検索条件：ロットNo
     */
    private String lotNo = "";
    /**
     * 検索条件：ﾒｯｷ開始日(FROM)
     */
    private String startMekkiDateF = "";
    /**
     * 検索条件：ﾒｯｷ開始日(TO)
     */
    private String startMekkiDateT = "";
    /**
     * 検索条件：ﾒｯｷ開始時刻(FROM)
     */
    private String startMekkiTimeF = "";
    /**
     * 検索条件：ﾒｯｷ開始時刻(TO)
     */
    private String startMekkiTimeT = "";
    /**
     * 検索条件：号機
     */
    private String gouki = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B044() {
    }

//<editor-fold defaultstate="collapsed" desc="#getter setter">
    /**
     * 警告メッセージ
     *
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     *
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    /**
     * 一覧表示データ取得
     *
     * @return 一覧表示データ
     */
    public List<GXHDO201B044Model> getListData() {
        return listData;
    }

    /**
     * 検索条件：ロットNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：ロットNo
     *
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 検索条件：ﾒｯｷ開始日(FROM)
     *
     * @return the startMekkiDateF
     */
    public String getStartMekkiDateF() {
        return startMekkiDateF;
    }

    /**
     * 検索条件：ﾒｯｷ開始日(FROM)
     *
     * @param startMekkiDateF the startMekkiDateF to set
     */
    public void setStartMekkiDateF(String startMekkiDateF) {
        this.startMekkiDateF = startMekkiDateF;
    }

    /**
     * 検索条件：ﾒｯｷ開始日(TO)
     *
     * @return the startMekkiDateT
     */
    public String getStartMekkiDateT() {
        return startMekkiDateT;
    }

    /**
     * 検索条件：ﾒｯｷ開始日(TO)
     *
     * @param startMekkiDateT the startMekkiDateT to set
     */
    public void setStartMekkiDateT(String startMekkiDateT) {
        this.startMekkiDateT = startMekkiDateT;
    }

    /**
     * 検索条件：ﾒｯｷ開始時刻(FROM)
     *
     * @return the startMekkiTimeF
     */
    public String getStartMekkiTimeF() {
        return startMekkiTimeF;
    }

    /**
     * 検索条件：ﾒｯｷ開始時刻(FROM)
     *
     * @param startMekkiTimeF the startMekkiTimeF to set
     */
    public void setStartMekkiTimeF(String startMekkiTimeF) {
        this.startMekkiTimeF = startMekkiTimeF;
    }

    /**
     * 検索条件：ﾒｯｷ開始時刻(TO)
     *
     * @return the startMekkiTimeT
     */
    public String getStartMekkiTimeT() {
        return startMekkiTimeT;
    }

    /**
     * 検索条件：ﾒｯｷ開始時刻(TO)
     *
     * @param startMekkiTimeT the startMekkiTimeT to set
     */
    public void setStartMekkiTimeT(String startMekkiTimeT) {
        this.startMekkiTimeT = startMekkiTimeT;
    }

    /**
     * 検索条件：号機
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 検索条件：号機
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

//</editor-fold>
    /**
     * ページング用の件数を返却
     *
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
            }
            return;
        }

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B044_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B044_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;

        // 画面クリア
        clear();
    }

    /**
     * Excel保存ボタン非活性制御
     *
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
        lotNo = "";
        startMekkiDateF = "";
        startMekkiDateT = "";
        startMekkiTimeF = "";
        startMekkiTimeT = "";
        gouki = "";

        listData = new ArrayList<>();
    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        try {
            // 入力チェック処理
            ValidateUtil validateUtil = new ValidateUtil();

            // ロットNo
            if (!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            if (!StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
                return;
            }
            // ﾒｯｷ開始日(FROM)
            if (existError(validateUtil.checkC101(getStartMekkiDateF(), "ﾒｯｷ開始日(from)", 6))
                    || existError(validateUtil.checkC201ForDate(getStartMekkiDateF(), "ﾒｯｷ開始日(from)"))
                    || existError(validateUtil.checkC501(getStartMekkiDateF(), "ﾒｯｷ開始日(from)"))) {
                return;
            }
            // ﾒｯｷ開始日(TO)
            if (existError(validateUtil.checkC101(getStartMekkiDateT(), "ﾒｯｷ開始日(to)", 6))
                    || existError(validateUtil.checkC201ForDate(getStartMekkiDateT(), "ﾒｯｷ開始日(to)"))
                    || existError(validateUtil.checkC501(getStartMekkiDateT(), "ﾒｯｷ開始日(to)"))) {
                return;
            }
            // ﾒｯｷ開始時刻(FROM)
            if (existError(validateUtil.checkC101(getStartMekkiTimeF(), "ﾒｯｷ開始時刻(from)", 4))
                    || existError(validateUtil.checkC201ForDate(getStartMekkiTimeF(), "ﾒｯｷ開始時刻(from)"))
                    || existError(validateUtil.checkC502(getStartMekkiTimeF(), "ﾒｯｷ開始時刻(from)"))) {
                return;
            }
            if (!StringUtil.isEmpty(startMekkiTimeF) && existError(validateUtil.checkC001(getStartMekkiDateF(), "ﾒｯｷ開始日(from)"))) {
                return;
            }
            // ﾒｯｷ開始時刻(TO)
            if (existError(validateUtil.checkC101(getStartMekkiTimeT(), "ﾒｯｷ開始時刻(to)", 4))
                    || existError(validateUtil.checkC201ForDate(getStartMekkiTimeT(), "ﾒｯｷ開始時刻(to)"))
                    || existError(validateUtil.checkC502(getStartMekkiTimeT(), "ﾒｯｷ開始時刻(to)"))) {
                return;
            }
            if (!StringUtil.isEmpty(startMekkiTimeT) && existError(validateUtil.checkC001(getStartMekkiDateT(), "ﾒｯｷ開始日(to)"))) {
                return;
            }

            // 一覧表示件数を取得
            long count = loadMekkiDataCount();

            if (count == 0) {
                // 検索結果が0件の場合エラー終了
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            if (listCountMax > 0 && count > listCountMax) {
                // 検索結果が上限件数以上の場合エラー終了
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000046", listCountMax), null);
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

        } catch (SQLException ex) {

            this.listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 警告ダイアログで「OK」押下時
     */
    public void processWarnOk() {
        try {

            // 検索処理実行
            selectListData();

        } catch (SQLException ex) {

            this.listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 一覧表示データ検索
     */
    private void selectListData() throws SQLException {
        // 入力チェックでエラーが存在しない場合検索処理を実行する
        List<SrMekki> mekkiList = loadMekkiData();

        // ﾛｯﾄNoのリストを取得
        List<Map<String, String>> lotnoList = getLotNoList(mekkiList);

        // 取得ﾛｯﾄを対象に検索を行う。
        BigDecimal mekkiListSize = BigDecimal.valueOf(lotnoList.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = mekkiListSize.divide(limit, 0, RoundingMode.UP).longValue();
        List<GXHDO201B044Model> makuatsuDataList = new ArrayList<>();
        //外部電極ﾒｯｷ情報取得
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = ((i + 1) * SEARCH_LIMIT);
            if (lotnoList.size() < endIdx) {
                endIdx = lotnoList.size();
            }
            makuatsuDataList.addAll(loadSrMkmakuatsuData(lotnoList.subList(startIdx, endIdx)));
        }

        // マージしたデータを一覧にセットする。
        this.listData = mergeData(mekkiList, makuatsuDataList);
    }

    /**
     * 外部電極ﾒｯｷと外部電極ﾒｯｷ膜厚の取得データをマージして取得
     *
     * @param mekkiList 外部電極ﾒｯｷリスト
     * @param makuatsuDataList 外部電極ﾒｯｷ膜厚リスト
     * @return
     */
    private List<GXHDO201B044Model> mergeData(List<SrMekki> mekkiList, List<GXHDO201B044Model> makuatsuDataList) {

        List<GXHDO201B044Model> mergeDataList = new ArrayList<>();
        for (SrMekki mekki : mekkiList) {

            // ﾛｯﾄ、分割No単位でデータを取得
            List<GXHDO201B044Model> makuatsuDataListFil = makuatsuDataList.stream()
                    .filter(n -> mekki.getKojyo().equals(n.getKojyo()))
                    .filter(n -> mekki.getLotno().equals(n.getLotno()))
                    .filter(n -> mekki.getEdaban().equals(n.getEdaban()))
//                    .filter(n -> mekkirireki.getBunkatuNo().equals(n.getBunkatuno()))
                    .collect(Collectors.toList());

            // 外部電極ﾒｯｷにだけﾃﾞｰﾀが存在する場合
            if (makuatsuDataListFil.isEmpty()) {
                GXHDO201B044Model model = new GXHDO201B044Model();
                mergeDataList.add(setModelData(model, mekki));
                continue;
            }

            // 外部電極ﾒｯｷ膜厚が存在する場合
            for (GXHDO201B044Model model : makuatsuDataListFil) {
                mergeDataList.add(setModelData(model, mekki));
            }
        }
        // マージ結果をリターン
        return mergeDataList;
    }

    /**
     * 表示用のmodelに履歴データをセット
     *
     * @param model model
     * @param srMekki 外部電極ﾒｯｷﾃﾞｰﾀ
     * @return 編集したモデルデータ
     */
    private GXHDO201B044Model setModelData(GXHDO201B044Model model, SrMekki srMekki) {

        model.setLotnoView(srMekki.getKojyo() + srMekki.getLotno() + srMekki.getEdaban());//ﾛｯﾄNo
        model.setKcpno(srMekki.getKcpno());//KCPNO
        model.setGouki(srMekki.getGouki());//号機

        return model;
    }

    /**
     * sr_mekki:ﾒｯｷ品質検査データ件数取得
     *
     * @return 検索結果件数
     * @throws SQLException 例外エラー
     */
    private long loadMekkiDataCount() throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT COUNT(LotNo) AS CNT "
                + "FROM sr_mekki "
                + "WHERE (? IS NULL OR Kojyo = ?) "
                + "AND   (? IS NULL OR LotNo = ?) "
                + "AND   (? IS NULL OR EdaBan = ?) "
                + "AND   (? IS NULL OR Mekkikaishinichiji >= ?) "
                + "AND   (? IS NULL OR Mekkikaishinichiji <= ?) "
                + "AND   (? IS NULL OR Gouki = ?) ";

        // パラメータ設定
        List<Object> params = createSearchParamMekkiList();

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map result = queryRunner.query(sql, new MapHandler(), params.toArray());

        return (long) result.get("CNT");
    }

    /**
     * sr_mekki:外部電極ﾒｯｷﾃﾞｰﾀ検索
     *
     * @return 外部電極ﾒｯｷデータリスト
     * @throws SQLException 例外エラー
     */
    private List<SrMekki> loadMekkiData() throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT kojyo,lotno,edaban,kcpno,gouki "
                + "FROM sr_mekki "
                + "WHERE (? IS NULL OR kojyo = ?) "
                + "AND   (? IS NULL OR lotno = ?) "
                + "AND   (? IS NULL OR edaban = ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji >= ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji <= ?) "
                + "AND   (? IS NULL OR gouki = ?) "
                + "ORDER BY "
                + "   kojyo "
                + "  ,lotno "
                + "  ,edaban ";

        Map mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");
        mapping.put("lotno", "lotno");
        mapping.put("edaban", "edaban");
        mapping.put("kcpno", "kcpno");
        mapping.put("gouki", "gouki");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMekki>> beanHandler = new BeanListHandler<>(SrMekki.class, rowProcessor);

        List<Object> params = createSearchParamMekkiList();
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunner.query(sql, beanHandler, params.toArray());

    }

    /**
     * sr_mkmakuatsu:外部電極ﾒｯｷ膜厚
     *
     * @param lotNoList ﾛｯﾄNoリスト
     * @return 外部電極ﾒｯｷ膜厚データリスト
     * @throws SQLException 例外エラー
     */
    private List<GXHDO201B044Model> loadSrMkmakuatsuData(List<Map<String, String>> lotNoList) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        // パラメータ
        List<Object> params = new ArrayList<>();

        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        sbSql.append("  SELECT ");
        sbSql.append("     kojyo ");
        sbSql.append("    ,lotno ");
        sbSql.append("    ,edaban ");
        sbSql.append("    ,barelno ");
        sbSql.append("    ,sokuteikaisuu ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '1' THEN nimakuatsu ELSE NULL END) AS makuatsuni01 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '2' THEN nimakuatsu ELSE NULL END) AS makuatsuni02 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '3' THEN nimakuatsu ELSE NULL END) AS makuatsuni03 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '4' THEN nimakuatsu ELSE NULL END) AS makuatsuni04 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '5' THEN nimakuatsu ELSE NULL END) AS makuatsuni05 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '6' THEN nimakuatsu ELSE NULL END) AS makuatsuni06 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '7' THEN nimakuatsu ELSE NULL END) AS makuatsuni07 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '8' THEN nimakuatsu ELSE NULL END) AS makuatsuni08 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '9' THEN nimakuatsu ELSE NULL END) AS makuatsuni09 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '10' THEN nimakuatsu ELSE NULL END) AS makuatsuni10 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '11' THEN nimakuatsu ELSE NULL END) AS makuatsuni11 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '12' THEN nimakuatsu ELSE NULL END) AS makuatsuni12 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '13' THEN nimakuatsu ELSE NULL END) AS makuatsuni13 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '14' THEN nimakuatsu ELSE NULL END) AS makuatsuni14 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '15' THEN nimakuatsu ELSE NULL END) AS makuatsuni15 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '16' THEN nimakuatsu ELSE NULL END) AS makuatsuni16 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '17' THEN nimakuatsu ELSE NULL END) AS makuatsuni17 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '18' THEN nimakuatsu ELSE NULL END) AS makuatsuni18 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '19' THEN nimakuatsu ELSE NULL END) AS makuatsuni19 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '20' THEN nimakuatsu ELSE NULL END) AS makuatsuni20 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '1' THEN snmakuatsu ELSE NULL END) AS makuatsusn01 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '2' THEN snmakuatsu ELSE NULL END) AS makuatsusn02 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '3' THEN snmakuatsu ELSE NULL END) AS makuatsusn03 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '4' THEN snmakuatsu ELSE NULL END) AS makuatsusn04 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '5' THEN snmakuatsu ELSE NULL END) AS makuatsusn05 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '6' THEN snmakuatsu ELSE NULL END) AS makuatsusn06 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '7' THEN snmakuatsu ELSE NULL END) AS makuatsusn07 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '8' THEN snmakuatsu ELSE NULL END) AS makuatsusn08 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '9' THEN snmakuatsu ELSE NULL END) AS makuatsusn09 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '10' THEN snmakuatsu ELSE NULL END) AS makuatsusn10 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '11' THEN snmakuatsu ELSE NULL END) AS makuatsusn11 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '12' THEN snmakuatsu ELSE NULL END) AS makuatsusn12 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '13' THEN snmakuatsu ELSE NULL END) AS makuatsusn13 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '14' THEN snmakuatsu ELSE NULL END) AS makuatsusn14 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '15' THEN snmakuatsu ELSE NULL END) AS makuatsusn15 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '16' THEN snmakuatsu ELSE NULL END) AS makuatsusn16 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '17' THEN snmakuatsu ELSE NULL END) AS makuatsusn17 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '18' THEN snmakuatsu ELSE NULL END) AS makuatsusn18 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '19' THEN snmakuatsu ELSE NULL END) AS makuatsusn19 ");
        sbSql.append("    ,MAX(CASE WHEN sokuteino = '20' THEN snmakuatsu ELSE NULL END) AS makuatsusn20 ");
        sbSql.append("  FROM sr_mkmakuatsu ");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (Map lotnoInfo : lotNoList) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append("kojyo = ? ");
            sbSql.append(" AND ");
            sbSql.append("lotno = ? ");
            sbSql.append(" AND ");
            sbSql.append("edaban = ? ");
            sbSql.append(")");
            // パラメータをセット
            params.add(lotnoInfo.get("kojyo"));
            params.add(lotnoInfo.get("lotno"));
            params.add(lotnoInfo.get("edaban"));
        }
        sbSql.append(")");
        sbSql.append("  GROUP BY ");
        sbSql.append("    kojyo ");
        sbSql.append("    ,lotno ");
        sbSql.append("    ,edaban ");
        sbSql.append("    ,barelno ");
        sbSql.append("    , sokuteikaisuu ");
        sbSql.append("ORDER BY ");
        sbSql.append("  kojyo ");
        sbSql.append("  ,lotno ");
        sbSql.append("  ,edaban ");
        sbSql.append("  ,barelno desc ");
        sbSql.append("  ,sokuteikaisuu desc ");

        // モデルクラスとのマッピング定義
        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo.
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("barelno", "bunkatuno"); // ﾊﾞﾚﾙNo(分割No)
        mapping.put("sokuteikaisuu", "sokuteikaisuu"); // 測定回数
        mapping.put("makuatsuni01", "makuatsuni01"); // Ni膜厚01
        mapping.put("makuatsuni02", "makuatsuni02"); // Ni膜厚02
        mapping.put("makuatsuni03", "makuatsuni03"); // Ni膜厚03
        mapping.put("makuatsuni04", "makuatsuni04"); // Ni膜厚04
        mapping.put("makuatsuni05", "makuatsuni05"); // Ni膜厚05
        mapping.put("makuatsuni06", "makuatsuni06"); // Ni膜厚06
        mapping.put("makuatsuni07", "makuatsuni07"); // Ni膜厚07
        mapping.put("makuatsuni08", "makuatsuni08"); // Ni膜厚08
        mapping.put("makuatsuni09", "makuatsuni09"); // Ni膜厚09
        mapping.put("makuatsuni10", "makuatsuni10"); // Ni膜厚10
        mapping.put("makuatsuni11", "makuatsuni11"); // Ni膜厚11
        mapping.put("makuatsuni12", "makuatsuni12"); // Ni膜厚12
        mapping.put("makuatsuni13", "makuatsuni13"); // Ni膜厚13
        mapping.put("makuatsuni14", "makuatsuni14"); // Ni膜厚14
        mapping.put("makuatsuni15", "makuatsuni15"); // Ni膜厚15
        mapping.put("makuatsuni16", "makuatsuni16"); // Ni膜厚16
        mapping.put("makuatsuni17", "makuatsuni17"); // Ni膜厚17
        mapping.put("makuatsuni18", "makuatsuni18"); // Ni膜厚18
        mapping.put("makuatsuni19", "makuatsuni19"); // Ni膜厚19
        mapping.put("makuatsuni20", "makuatsuni20"); // Ni膜厚20
        mapping.put("makuatsusn01", "makuatsusn01"); // Sn膜厚01
        mapping.put("makuatsusn02", "makuatsusn02"); // Sn膜厚02
        mapping.put("makuatsusn03", "makuatsusn03"); // Sn膜厚03
        mapping.put("makuatsusn04", "makuatsusn04"); // Sn膜厚04
        mapping.put("makuatsusn05", "makuatsusn05"); // Sn膜厚05
        mapping.put("makuatsusn06", "makuatsusn06"); // Sn膜厚06
        mapping.put("makuatsusn07", "makuatsusn07"); // Sn膜厚07
        mapping.put("makuatsusn08", "makuatsusn08"); // Sn膜厚08
        mapping.put("makuatsusn09", "makuatsusn09"); // Sn膜厚09
        mapping.put("makuatsusn10", "makuatsusn10"); // Sn膜厚10
        mapping.put("makuatsusn11", "makuatsusn11"); // Sn膜厚11
        mapping.put("makuatsusn12", "makuatsusn12"); // Sn膜厚12
        mapping.put("makuatsusn13", "makuatsusn13"); // Sn膜厚13
        mapping.put("makuatsusn14", "makuatsusn14"); // Sn膜厚14
        mapping.put("makuatsusn15", "makuatsusn15"); // Sn膜厚15
        mapping.put("makuatsusn16", "makuatsusn16"); // Sn膜厚16
        mapping.put("makuatsusn17", "makuatsusn17"); // Sn膜厚17
        mapping.put("makuatsusn18", "makuatsusn18"); // Sn膜厚18
        mapping.put("makuatsusn19", "makuatsusn19"); // Sn膜厚19
        mapping.put("makuatsusn20", "makuatsusn20"); // Sn膜厚20

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<GXHDO201B044Model>> beanHandler
                = new BeanListHandler<>(GXHDO201B044Model.class, rowProcessor);

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunner.query(sbSql.toString(), beanHandler, params.toArray());

    }

    /**
     * 文字列をバイトでカットします。
     *
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
            // 処理無し
        }
    }

    /**
     * Excelダウンロード
     *
     * @throws Throwable
     */
    public void downloadExcel() throws Throwable {
        File excel = null;

        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();

            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b044.json"));
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "外部電極・メッキ膜厚");

            // ダウンロードファイル名
            String downloadFileName = "外部電極・メッキ膜厚_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
                }
            }
        }
    }

    /**
     * 検索パラメータ生成
     *
     * @return パラメータ
     */
    private List<Object> createSearchParamMekkiList() {
        // パラメータ設定
        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 11);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 11, 14));
        }
        Date paramStartDateF = null;
        if (!StringUtil.isEmpty(startMekkiDateF)) {
            paramStartDateF = DateUtil.convertStringToDateInSeconds(getStartMekkiDateF(), StringUtil.isEmpty(getStartMekkiTimeF()) ? "000000" : getStartMekkiTimeF() + "00");
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startMekkiDateT)) {
            paramStartDateT = DateUtil.convertStringToDateInSeconds(getStartMekkiDateT(), StringUtil.isEmpty(getStartMekkiTimeT()) ? "235959" : getStartMekkiTimeT() + "59");
        }
        String paramGouki = null;
        if (!StringUtil.isEmpty(gouki)) {
            paramGouki = gouki;
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));
        params.addAll(Arrays.asList(paramGouki, paramGouki));

        return params;
    }

    /**
     * エラーチェック： エラーが存在する場合ポップアップ用メッセージをセットする
     *
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
     *
     * @return 一覧表示データが存在する場合true
     */
    private boolean isExistListData() {
        if (listData == null || listData.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ファイル転送
     *
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

    /**
     * ﾛｯﾄNo(工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番)のリストを取得(重複排除)
     *
     * @param mainData メインデータ
     * @return ﾛｯﾄNoリスト
     */
    private List<Map<String, String>> getLotNoList(List<SrMekki> mainData) {
        // 工場ｺｰﾄﾞとﾛｯﾄNoだけのリストを作成
        List<Map<String, String>> list = new ArrayList<>();
        for (SrMekki mekki : mainData) {
            Map<String, String> map = new HashMap<>();
            map.put("kojyo", mekki.getKojyo());
            map.put("lotno", mekki.getLotno());
            map.put("edaban", mekki.getEdaban());
            list.add(map);
        }
        // 重複を排除してリターン
        return list.stream().distinct().collect(Collectors.toList());
    }
}
