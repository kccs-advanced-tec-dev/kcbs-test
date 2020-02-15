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
import jp.co.kccs.xhd.db.model.MekkiRireki;
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
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/12/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒｯｷ膜厚履歴検索画面
 *
 * @author 863 F.Zhang
 * @since 2019/12/02
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
            long count = loadSrMekkiListDataCount();

            if (count == 0) {
                // 検索結果が0件の場合エラー終了
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            // 入力チェックでエラーが存在しない場合検索処理を実行する
            List<Map<String, Object>> srMekkiList = loadSrMekkiListData();
            
            // ﾒｯｷ履歴ﾃｰﾌﾞﾙの件数を取得
            long mekkiLirekiCount = getMekkiRirekiDataCount(srMekkiList);
            if (mekkiLirekiCount == 0) {
                // 検索結果が0件の場合エラー終了
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            // ﾒｯｷ履歴ﾃｰﾌﾞﾙのﾃﾞｰﾀを取得
            List<MekkiRireki> mekkiRirekiList = getMekkiRirekiData(srMekkiList);
           

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
            loadSrMekkiListData();

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
            loadSrMekkiListData();

        } catch (SQLException ex) {

            this.listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

    }

    /**
     * sr_mekki:ﾒｯｷ品質検査データ件数取得
     *
     * @return 検索結果件数
     */
    private long loadSrMekkiListDataCount() throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT COUNT(*) AS CNT "
                + "FROM sr_mekki "
                + "WHERE (? IS NULL OR kojyo = ?) "
                + "AND   (? IS NULL OR lotno = ?) "
                + "AND   (? IS NULL OR edaban = ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji >= ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji <= ?) ";

        // パラメータ設定
        List<Object> params = createSearchParamSrMekkiList();

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map result = queryRunner.query(sql, new MapHandler(), params.toArray());

        return (long) result.get("CNT");
    }

    /**
     * sr_mekki:ﾒｯｷ品質検査データ検索
     */
    private List loadSrMekkiListData() throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT kojyo,lotno,edaban "
                + "FROM sr_mekki "
                + "WHERE (? IS NULL OR kojyo = ?) "
                + "AND   (? IS NULL OR lotno = ?) "
                + "AND   (? IS NULL OR edaban = ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji >= ?) "
                + "AND   (? IS NULL OR mekkikaishinichiji <= ?) "
                + "GROUP BY "
                + "   kojyo "
                + "  ,lotno "
                + "  ,edaban "
                + "ORDER BY "
                + "   kojyo "
                + "  ,lotno "
                + "  ,edaban ";

        // パラメータ設定
        List<Object> params = createSearchParamSrMekkiList();

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunner.query(sql, new MapListHandler(), params.toArray());

    }
    
    private long getMekkiRirekiDataCount(List<Map<String, Object>> srMekkiList) throws SQLException{
        // 取得ﾛｯﾄを対象に検索を行う。
            BigDecimal mekkiListSize = BigDecimal.valueOf(srMekkiList.size());
            BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
            long loopCount = mekkiListSize.divide(limit, 0, RoundingMode.UP).longValue();

            long mekkiRirekiCount = 0;
            //ﾒｯｷ履歴情報取得
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = ((i + 1) * SEARCH_LIMIT);
            if (srMekkiList.size() < endIdx) {
                endIdx = srMekkiList.size();
            }
            mekkiRirekiCount += loadMekkiRirekiDataCount(srMekkiList.subList(startIdx, endIdx));
            
        }
        
        return mekkiRirekiCount;
    }

    /**
     * [ﾒｯｷ履歴]データ件数取得取得
     *
     * @param srMekkiList ﾒｯｷ品質検査データリスト
     * @return 検索結果件数
     * @throws SQLException 例外エラー
     */
    private long loadMekkiRirekiDataCount(List<Map<String, Object>> srMekkiList) throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceEquipment);
        List<Object> params = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        sbSql.append(" SELECT SELECT COUNT(*) AS CNT ");
        sbSql.append(" FROM mekki_rireki ");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (Map lotnoInfo : srMekkiList) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append("Kojyo = ? ");
            sbSql.append(" AND ");
            sbSql.append("LotNo = ? ");
            sbSql.append(" AND ");
            sbSql.append("EdaBan = ? ");
            sbSql.append(")");
            // パラメータをセット
            params.add(lotnoInfo.get("kojyo"));
            params.add(lotnoInfo.get("lotno"));
            params.add(lotnoInfo.get("edaban"));
        }
        sbSql.append(")");
        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        Map result = queryRunner.query(sbSql.toString(), new MapHandler(), params.toArray());

        return (long) result.get("CNT");

    }
    
    private List<MekkiRireki> getMekkiRirekiData(List<Map<String, Object>> srMekkiList) throws SQLException{
        // 取得ﾛｯﾄを対象に検索を行う。
            BigDecimal mekkiListSize = BigDecimal.valueOf(srMekkiList.size());
            BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
            long loopCount = mekkiListSize.divide(limit, 0, RoundingMode.UP).longValue();

            List<MekkiRireki> mekkiRirekiList = new ArrayList<>();
            //ﾒｯｷ履歴情報取得
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = ((i + 1) * SEARCH_LIMIT);
            if (srMekkiList.size() < endIdx) {
                endIdx = srMekkiList.size();
            }
            mekkiRirekiList.addAll(loadMekkiRirekiData(srMekkiList.subList(startIdx, endIdx)));
            
        }
        
        return mekkiRirekiList;
    }
    
    

    /**
     * [ﾒｯｷ履歴]データ取得取得
     *
     * @param SrMekkiList ﾒｯｷ品質検査データリスト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<MekkiRireki> loadMekkiRirekiData(List<Map<String, Object>> SrMekkiList) throws SQLException {

        QueryRunner queryRunner = new QueryRunner(dataSourceEquipment);
        List<Object> params = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        sbSql.append(" SELECT Kojyo,LotNo,EdaBan,BunkatuNo,GoukiCode,KCPNO,BunkatuSuu,MediaName1,Mediacc1,");
        sbSql.append(" MediaName2,Mediacc2,NiA,NiAM,SnA,SnAM,JokenNo,TorokuSyaCode,TonyuSyaCode,DomeZanChk,DomeNo,");
        sbSql.append(" NiSou,SnSou,TorokuNichiji,StartNichiji,EndNichiji,KaisyuSyaCode,ContainerNo,NiTime,SnTime,");
        sbSql.append(" StartNichiji_Sn,EndNichiji_Ni,Nurekensakekka,Tainetsukensakekka,Gaikankensakekka");
        sbSql.append(" FROM mekki_rireki ");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (Map lotnoInfo : SrMekkiList) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append("Kojyo = ? ");
            sbSql.append(" AND ");
            sbSql.append("LotNo = ? ");
            sbSql.append(" AND ");
            sbSql.append("EdaBan = ? ");
            sbSql.append(")");
            // パラメータをセット
            params.add(lotnoInfo.get("kojyo"));
            params.add(lotnoInfo.get("lotno"));
            params.add(lotnoInfo.get("edaban"));
        }
        sbSql.append(")");
        sbSql.append(" ORDER BY Kojyo");
        sbSql.append(" ,LotNo");
        sbSql.append(" ,EdaBan");
        
        Map mapping = new HashMap<>();
        mapping.put("Kojyo", "kojyo");
        mapping.put("LotNo", "lotNo");
        mapping.put("EdaBan", "edaBan");
        mapping.put("BunkatuNo", "bunkatuNo");
        mapping.put("GoukiCode", "goukiCode");
        mapping.put("KCPNO", "kcpno");
        mapping.put("BunkatuSuu", "bunkatuSuu");
        mapping.put("MediaName1", "mediaName1");
        mapping.put("Mediacc1", "mediacc1");
        mapping.put("MediaName2", "mediaName2");
        mapping.put("Mediacc2", "mediacc2");
        mapping.put("NiA", "niA");
        mapping.put("NiAM", "niAM");
        mapping.put("SnA", "snA");
        mapping.put("SnAM", "snAM");
        mapping.put("JokenNo", "jokenNo");
        mapping.put("TorokuSyaCode", "torokuSyaCode");
        mapping.put("TonyuSyaCode", "tonyuSyaCode");
        mapping.put("DomeZanChk", "domeZanChk");
        mapping.put("DomeNo", "domeNo");
        mapping.put("NiSou", "niSou");
        mapping.put("SnSou", "snSou");
        mapping.put("TorokuNichiji", "torokuNichiji");
        mapping.put("StartNichiji", "startNichiji");
        mapping.put("EndNichiji", "endNichiji");
        mapping.put("KaisyuSyaCode", "kaisyuSyaCode");
        mapping.put("ContainerNo", "containerNo");
        mapping.put("NiTime", "niTime");
        mapping.put("SnTime", "snTime");
        mapping.put("StartNichiji_Sn", "startNichiji_Sn");
        mapping.put("EndNichiji_Ni", "endNichiji_Ni");
        mapping.put("Nurekensakekka", "nurekensakekka");
        mapping.put("Tainetsukensakekka", "tainetsukensakekka");
        mapping.put("Gaikankensakekka", "gaikankensakekka");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<MekkiRireki>> beanHandler = new BeanListHandler<>(MekkiRireki.class, rowProcessor);

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunner.query(sbSql.toString(), beanHandler, params.toArray());
    }

    private void setListData(List<MekkiRireki> mekkiRirekiList){
        
    }
    
//    /**
//     * 一覧表示データ検索
//     */
//    public void loadSrMekkiListData() {
//
//        try {
//            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
//            String sql = "SELECT "
//                    + "  CONCAT(IFNULL(SR_MEKKI.KOJYO, ''), IFNULL(SR_MEKKI.LOTNO, ''), IFNULL(SR_MEKKI.EDABAN, '')) AS LOTNO "
//                    + "  , SR_MEKKI.kcpno "
//                    + "  , SR_MEKKI.ukeiresuu "
//                    + "  , SR_MEKKI.domekosuu "
//                    + "  , SR_MEKKI.gouki "
//                    + "  , SR_MEKKI.tantousya "
//                    + "  , SR_MEKKI.mekkikaishinichiji "
//                    + "  , SR_MEKKI.mekkijyoukennia "
//                    + "  , SR_MEKKI.mekkijyoukenniam "
//                    + "  , SR_MEKKI.mekkijyoukensna "
//                    + "  , SR_MEKKI.mekkijyoukensnam "
//                    + "  , SR_MEKKI.shukkakosuu "
//                    + "  , SR_MEKKI.budomari "
//                    + "  , SR_MEKKI.makuatsunimin "
//                    + "  , SR_MEKKI.makuatsunimax "
//                    + "  , SR_MEKKI.makuatsuniave "
//                    + "  , SR_MEKKI.makuatsunistd "
//                    + "  , SR_MEKKI.makuatsusnmin "
//                    + "  , SR_MEKKI.makuatsusnmax "
//                    + "  , SR_MEKKI.makuatsusnave "
//                    + "  , SR_MEKKI.makuatsusnstd "
//                    + "  , SR_MEKKI.nurekensakekka "
//                    + "  , SR_MEKKI.tainetsukensakekka "
//                    + "  , SR_MEKKI.gaikankensakekka "
//                    + "  , SR_MEKKI.bikou1 "
//                    + "  , SR_MEKKI.bikou2 "
//                    + "  , SR_MEKKI.bikou3 "
//                    + "  , SR_MEKKI.jissekino "
//                    + "  , SR_MEKKI.domemeisai "
//                    + "  , SR_MEKKI.tyoseimaeph1 "
//                    + "  , SR_MEKKI.tyoseigoph1 "
//                    + "  , SR_MEKKI.tyoseijikan1 "
//                    + "  , SR_MEKKI.tyoseimaeph2 "
//                    + "  , SR_MEKKI.tyoseigoph2 "
//                    + "  , SR_MEKKI.tyoseijikan2 "
//                    + "  , SR_MEKKI.tsunpou "
//                    + "  , SR_MEKKI.barrelno "
//                    + "  , SR_MEKKI.makuatsunicpl "
//                    + "  , SR_MEKKI.makuatsusncpl "
//                    + "  , SR_MEKKI.sokuteinichiji "
//                    + "  , SR_MEKKI.makuatsunicv "
//                    + "  , SR_MEKKI.makuatsusncv "
//                    + "  , SR_MEKKI.kensanichiji "
//                    + "  , SR_MEKKI.kensatantousya "
//                    + "  , SR_MEKKI.makuatsutantosya "
//                    + "  , SR_MEKKI.kaishinichiji_sn "
//                    + "  , SR_MEKKI.tokuisaki "
//                    + "  , SR_MEKKI.lotkubuncode "
//                    + "  , SR_MEKKI.ownercode "
//                    + "  , SR_MEKKI.ukeiretannijyuryo "
//                    + "  , SR_MEKKI.ukeiresoujyuryou "
//                    + "  , SR_MEKKI.mekkibasyo "
//                    + "  , SR_MEKKI.mekkibasyosetubi "
//                    + "  , SR_MEKKI.mekkisyuryounichiji "
//                    + "  , SR_MEKKI.syuryousya "
//                    + "  , SR_MEKKI.kensatannijyuryo "
//                    + "  , SR_MEKKI.kensasoujyuryou "
//                    + "  , SR_MEKKI.netusyorijyouken "
//                    + "  , SR_MEKKI.netusyorikaisinichiji "
//                    + "  , SR_MEKKI.netusyoritantousya "
//                    + "  , SR_MEKKI.jisyakusenbetukaisinichiji "
//                    + "  , SR_MEKKI.jisyakusenbetutantousya "
//                    + "  , SR_MEKKI.ijouhakkou "
//                    + "  , SR_MEKKI.ijourank "
//                    + "  , SR_MEKKI.makuatsukakunin "
//                    + "  , SR_MEKKI.testhin "
//                    + "  , SR_MEKKI.tsunpouave "
//                    + "  , SR_MEKKI.mekkisyurui "
//                    + "  , SRMK_LOT.makuatsuni01 "
//                    + "  , SRMK_LOT.makuatsuni02 "
//                    + "  , SRMK_LOT.makuatsuni03 "
//                    + "  , SRMK_LOT.makuatsuni04 "
//                    + "  , SRMK_LOT.makuatsuni05 "
//                    + "  , SRMK_LOT.makuatsuni06 "
//                    + "  , SRMK_LOT.makuatsuni07 "
//                    + "  , SRMK_LOT.makuatsuni08 "
//                    + "  , SRMK_LOT.makuatsuni09 "
//                    + "  , SRMK_LOT.makuatsuni10 "
//                    + "  , SRMK_LOT.makuatsuni11 "
//                    + "  , SRMK_LOT.makuatsuni12 "
//                    + "  , SRMK_LOT.makuatsuni13 "
//                    + "  , SRMK_LOT.makuatsuni14 "
//                    + "  , SRMK_LOT.makuatsuni15 "
//                    + "  , SRMK_LOT.makuatsuni16 "
//                    + "  , SRMK_LOT.makuatsuni17 "
//                    + "  , SRMK_LOT.makuatsuni18 "
//                    + "  , SRMK_LOT.makuatsuni19 "
//                    + "  , SRMK_LOT.makuatsuni20 "
//                    + "  , SRMK_LOT.makuatsusn01 "
//                    + "  , SRMK_LOT.makuatsusn02 "
//                    + "  , SRMK_LOT.makuatsusn03 "
//                    + "  , SRMK_LOT.makuatsusn04 "
//                    + "  , SRMK_LOT.makuatsusn05 "
//                    + "  , SRMK_LOT.makuatsusn06 "
//                    + "  , SRMK_LOT.makuatsusn07 "
//                    + "  , SRMK_LOT.makuatsusn08 "
//                    + "  , SRMK_LOT.makuatsusn09 "
//                    + "  , SRMK_LOT.makuatsusn10 "
//                    + "  , SRMK_LOT.makuatsusn11 "
//                    + "  , SRMK_LOT.makuatsusn12 "
//                    + "  , SRMK_LOT.makuatsusn13 "
//                    + "  , SRMK_LOT.makuatsusn14 "
//                    + "  , SRMK_LOT.makuatsusn15 "
//                    + "  , SRMK_LOT.makuatsusn16 "
//                    + "  , SRMK_LOT.makuatsusn17 "
//                    + "  , SRMK_LOT.makuatsusn18 "
//                    + "  , SRMK_LOT.makuatsusn19 "
//                    + "  , SRMK_LOT.makuatsusn20 "
//                    + "FROM sr_mekki SR_MEKKI "
//                    + "LEFT JOIN ( "
//                    + "  SELECT "
//                    + "    SRMK.kojyo "
//                    + "    , SRMK.lotno "
//                    + "    , SRMK.edaban "
//                    + "    , SRMK.sokuteikaisuu "
//                    + "    , MAX(CASE WHEN sokuteino = '1' THEN nimakuatsu ELSE NULL END) AS makuatsuni01 "
//                    + "    , MAX(CASE WHEN sokuteino = '2' THEN nimakuatsu ELSE NULL END) AS makuatsuni02 "
//                    + "    , MAX(CASE WHEN sokuteino = '3' THEN nimakuatsu ELSE NULL END) AS makuatsuni03 "
//                    + "    , MAX(CASE WHEN sokuteino = '4' THEN nimakuatsu ELSE NULL END) AS makuatsuni04 "
//                    + "    , MAX(CASE WHEN sokuteino = '5' THEN nimakuatsu ELSE NULL END) AS makuatsuni05 "
//                    + "    , MAX(CASE WHEN sokuteino = '6' THEN nimakuatsu ELSE NULL END) AS makuatsuni06 "
//                    + "    , MAX(CASE WHEN sokuteino = '7' THEN nimakuatsu ELSE NULL END) AS makuatsuni07 "
//                    + "    , MAX(CASE WHEN sokuteino = '8' THEN nimakuatsu ELSE NULL END) AS makuatsuni08 "
//                    + "    , MAX(CASE WHEN sokuteino = '9' THEN nimakuatsu ELSE NULL END) AS makuatsuni09 "
//                    + "    , MAX(CASE WHEN sokuteino = '10' THEN nimakuatsu ELSE NULL END) AS makuatsuni10 "
//                    + "    , MAX(CASE WHEN sokuteino = '11' THEN nimakuatsu ELSE NULL END) AS makuatsuni11 "
//                    + "    , MAX(CASE WHEN sokuteino = '12' THEN nimakuatsu ELSE NULL END) AS makuatsuni12 "
//                    + "    , MAX(CASE WHEN sokuteino = '13' THEN nimakuatsu ELSE NULL END) AS makuatsuni13 "
//                    + "    , MAX(CASE WHEN sokuteino = '14' THEN nimakuatsu ELSE NULL END) AS makuatsuni14 "
//                    + "    , MAX(CASE WHEN sokuteino = '15' THEN nimakuatsu ELSE NULL END) AS makuatsuni15 "
//                    + "    , MAX(CASE WHEN sokuteino = '16' THEN nimakuatsu ELSE NULL END) AS makuatsuni16 "
//                    + "    , MAX(CASE WHEN sokuteino = '17' THEN nimakuatsu ELSE NULL END) AS makuatsuni17 "
//                    + "    , MAX(CASE WHEN sokuteino = '18' THEN nimakuatsu ELSE NULL END) AS makuatsuni18 "
//                    + "    , MAX(CASE WHEN sokuteino = '19' THEN nimakuatsu ELSE NULL END) AS makuatsuni19 "
//                    + "    , MAX(CASE WHEN sokuteino = '20' THEN nimakuatsu ELSE NULL END) AS makuatsuni20 "
//                    + "    , MAX(CASE WHEN sokuteino = '1' THEN snmakuatsu ELSE NULL END) AS makuatsusn01 "
//                    + "    , MAX(CASE WHEN sokuteino = '2' THEN snmakuatsu ELSE NULL END) AS makuatsusn02 "
//                    + "    , MAX(CASE WHEN sokuteino = '3' THEN snmakuatsu ELSE NULL END) AS makuatsusn03 "
//                    + "    , MAX(CASE WHEN sokuteino = '4' THEN snmakuatsu ELSE NULL END) AS makuatsusn04 "
//                    + "    , MAX(CASE WHEN sokuteino = '5' THEN snmakuatsu ELSE NULL END) AS makuatsusn05 "
//                    + "    , MAX(CASE WHEN sokuteino = '6' THEN snmakuatsu ELSE NULL END) AS makuatsusn06 "
//                    + "    , MAX(CASE WHEN sokuteino = '7' THEN snmakuatsu ELSE NULL END) AS makuatsusn07 "
//                    + "    , MAX(CASE WHEN sokuteino = '8' THEN snmakuatsu ELSE NULL END) AS makuatsusn08 "
//                    + "    , MAX(CASE WHEN sokuteino = '9' THEN snmakuatsu ELSE NULL END) AS makuatsusn09 "
//                    + "    , MAX(CASE WHEN sokuteino = '10' THEN snmakuatsu ELSE NULL END) AS makuatsusn10 "
//                    + "    , MAX(CASE WHEN sokuteino = '11' THEN snmakuatsu ELSE NULL END) AS makuatsusn11 "
//                    + "    , MAX(CASE WHEN sokuteino = '12' THEN snmakuatsu ELSE NULL END) AS makuatsusn12 "
//                    + "    , MAX(CASE WHEN sokuteino = '13' THEN snmakuatsu ELSE NULL END) AS makuatsusn13 "
//                    + "    , MAX(CASE WHEN sokuteino = '14' THEN snmakuatsu ELSE NULL END) AS makuatsusn14 "
//                    + "    , MAX(CASE WHEN sokuteino = '15' THEN snmakuatsu ELSE NULL END) AS makuatsusn15 "
//                    + "    , MAX(CASE WHEN sokuteino = '16' THEN snmakuatsu ELSE NULL END) AS makuatsusn16 "
//                    + "    , MAX(CASE WHEN sokuteino = '17' THEN snmakuatsu ELSE NULL END) AS makuatsusn17 "
//                    + "    , MAX(CASE WHEN sokuteino = '18' THEN snmakuatsu ELSE NULL END) AS makuatsusn18 "
//                    + "    , MAX(CASE WHEN sokuteino = '19' THEN snmakuatsu ELSE NULL END) AS makuatsusn19 "
//                    + "    , MAX(CASE WHEN sokuteino = '20' THEN snmakuatsu ELSE NULL END) AS makuatsusn20 "
//                    + "  FROM "
//                    + "  ( "
//                    + "      SELECT "
//                    + "        SRMK_1.kojyo "
//                    + "        , SRMK_1.lotno "
//                    + "        , SRMK_1.edaban "
//                    + "        , SRMK_1.sokuteikaisuu "
//                    + "        , SRMK_1.sokuteino "
//                    + "        , SRMK_1.barelno "
//                    + "        , SRMK_1.nimakuatsu "
//                    + "        , SRMK_1.snmakuatsu "
//                    + "      FROM "
//                    + "        sr_mkmakuatsu SRMK_1 "
//                    + "        INNER JOIN ( "
//                    + "            SELECT "
//                    + "              SRMK_BMIN.kojyo "
//                    + "              , SRMK_BMIN.lotno "
//                    + "              , SRMK_BMIN.edaban "
//                    + "              , SRMK_BMIN.sokuteikaisuu "
//                    + "              , SRMK_BMIN.sokuteino "
//                    + "              , min(SRMK_BMIN.barelno) as min_barelno "
//                    + "            FROM "
//                    + "              sr_mkmakuatsu SRMK_BMIN "
//                    + "            WHERE "
//                    + "              SRMK_BMIN.sokuteikaisuu = ( "
//                    + "                  SELECT MAX(sokuteikaisuu) "
//                    + "                  FROM   sr_mkmakuatsu SRMK_SMAX "
//                    + "                  WHERE  SRMK_SMAX.kojyo = SRMK_BMIN.kojyo "
//                    + "                  AND    SRMK_SMAX.lotno = SRMK_BMIN.lotno "
//                    + "                  AND    SRMK_SMAX.edaban = SRMK_BMIN.edaban "
//                    + "                  GROUP BY "
//                    + "                         SRMK_SMAX.kojyo "
//                    + "                         , SRMK_SMAX.lotno "
//                    + "                         , SRMK_SMAX.edaban "
//                    + "              ) "
//                    + "            GROUP BY "
//                    + "              SRMK_BMIN.kojyo "
//                    + "              , SRMK_BMIN.lotno "
//                    + "              , SRMK_BMIN.edaban "
//                    + "              , SRMK_BMIN.sokuteikaisuu "
//                    + "              , SRMK_BMIN.sokuteino "
//                    + "        ) SRMK_2 ON ( "
//                    + "                            SRMK_1.kojyo = SRMK_2.kojyo "
//                    + "                        AND SRMK_1.lotno = SRMK_2.lotno "
//                    + "                        AND SRMK_1.edaban = SRMK_2.edaban "
//                    + "                        AND SRMK_1.sokuteikaisuu = SRMK_2.sokuteikaisuu "
//                    + "                        AND SRMK_1.sokuteino = SRMK_2.sokuteino "
//                    + "                        AND SRMK_1.barelno = SRMK_2.min_barelno "
//                    + "                    ) "
//                    + "  ) SRMK "
//                    + "  GROUP BY "
//                    + "    SRMK.kojyo "
//                    + "    , SRMK.lotno "
//                    + "    , SRMK.edaban "
//                    + "    , SRMK.sokuteikaisuu "
//                    + ") SRMK_LOT "
//                    + "    ON SR_MEKKI.kojyo = SRMK_LOT.kojyo "
//                    + "    AND SR_MEKKI.lotno = SRMK_LOT.lotno "
//                    + "    AND SR_MEKKI.edaban = SRMK_LOT.edaban "
//                    + "WHERE (? IS NULL OR SR_MEKKI.kojyo = ?) "
//                    + "AND   (? IS NULL OR SR_MEKKI.lotno = ?) "
//                    + "AND   (? IS NULL OR SR_MEKKI.edaban = ?) "
//                    + "AND   (? IS NULL OR SR_MEKKI.mekkikaishinichiji >= ?) "
//                    + "AND   (? IS NULL OR SR_MEKKI.mekkikaishinichiji <= ?) "
//                    + "ORDER BY "
//                    + "  SR_MEKKI.kojyo "
//                    + "  , SR_MEKKI.lotno "
//                    + "  , SR_MEKKI.edaban ";
//
//            // パラメータ設定
//            List<Object> params = createSearchParamSrMekkiList();
//
//            // モデルクラスとのマッピング定義
//            Map<String, String> mapping = new HashMap<>();
//            mapping.put("LOTNO", "lotno"); // ﾛｯﾄNo.
//            mapping.put("kcpno", "kcpno"); // KCPNO
//            mapping.put("ukeiresuu", "ukeiresuu"); // 処理数(個)
//            mapping.put("domekosuu", "domekosuu"); // ﾄﾞｰﾑ個数
//            mapping.put("gouki", "gouki"); // 号機
//            mapping.put("tantousya", "starttantosyacode"); // 開始担当者
//            mapping.put("mekkikaishinichiji", "mekkikaishinichiji"); // ﾒｯｷ開始日時
//            mapping.put("mekkijyoukennia", "mekkijyoukennia"); // 条件NI(A)
//            mapping.put("mekkijyoukenniam", "mekkijyoukenniam"); // 条件NI(AM)
//            mapping.put("mekkijyoukensna", "mekkijyoukensna"); // 条件SN(A)
//            mapping.put("mekkijyoukensnam", "mekkijyoukensnam"); // 条件SN(AM)
//            mapping.put("shukkakosuu", "shukkakosuu"); // 良品数
//            mapping.put("budomari", "budomari"); // 歩留まり
//            mapping.put("makuatsunimin", "makuatsunimin"); // Ni膜厚(MIN)
//            mapping.put("makuatsunimax", "makuatsunimax"); // Ni膜厚(MAX)
//            mapping.put("makuatsuniave", "makuatsuniave"); // Ni膜厚(AVE)
//            mapping.put("makuatsunistd", "makuatsunistd"); // Ni膜厚(STD)
//            mapping.put("makuatsusnmin", "makuatsusnmin"); // Sn膜厚(MIN)
//            mapping.put("makuatsusnmax", "makuatsusnmax"); // Sn膜厚(MAX)
//            mapping.put("makuatsusnave", "makuatsusnave"); // Sn膜厚(AVE)
//            mapping.put("makuatsusnstd", "makuatsusnstd"); // Sn膜厚(STD)
//            mapping.put("nurekensakekka", "nurekensakekka"); // 半田ﾇﾚ性
//            mapping.put("tainetsukensakekka", "tainetsukensakekka"); // 半田耐熱性
//            mapping.put("gaikankensakekka", "gaikankensakekka"); // 外観
//            mapping.put("bikou1", "biko1"); // 備考1
//            mapping.put("bikou2", "biko2"); // 備考2
//            mapping.put("bikou3", "biko3"); // 備考3
//            mapping.put("jissekino", "jissekino"); // 回数
//            mapping.put("domemeisai", "domemeisai"); // 使用ﾄﾞｰﾑ明細
//            mapping.put("tyoseimaeph1", "tyoseimaeph1"); // 1回目調整前PH値
//            mapping.put("tyoseigoph1", "tyoseigoph1"); // 1回目調整後PH値
//            mapping.put("tyoseijikan1", "tyoseijikan1"); // 1回目調整時間
//            mapping.put("tyoseimaeph2", "tyoseimaeph2"); // 2回目調整前PH値
//            mapping.put("tyoseigoph2", "tyoseigoph2"); // 2回目調整後PH値
//            mapping.put("tyoseijikan2", "tyoseijikan2"); // 2回目調整時間
//            mapping.put("tsunpou", "tsunpou"); // Ｔ寸法
//            mapping.put("barrelno", "barrelno"); // ﾊﾞﾚﾙNo
//            mapping.put("makuatsunicpl", "makuatsunicpl"); // Ni膜厚(CPL)
//            mapping.put("makuatsusncpl", "makuatsusncpl"); // Sn膜厚(CPL)
//            mapping.put("sokuteinichiji", "sokuteinichiji"); // 測定日時
//            mapping.put("makuatsunicv", "makuatsunicv"); // Ni膜厚(CV)
//            mapping.put("makuatsusncv", "makuatsusncv"); // Sn膜厚(CV)
//            mapping.put("kensanichiji", "kensanichiji"); // 検査日時
//            mapping.put("kensatantousya", "kensatantousya"); // 検査・外観担当者
//            mapping.put("makuatsutantosya", "makuatsutantosya"); // 膜厚担当者
//            mapping.put("kaishinichiji_sn", "kaishinichiji_sn"); // Sn開始日時
//            mapping.put("tokuisaki", "tokuisaki"); // 客先
//            mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
//            mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
//            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量(g/100個)
//            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量(g)
//            mapping.put("mekkibasyo", "mekkibasyo"); // ﾒｯｷ場所
//            mapping.put("mekkibasyosetubi", "mekkibasyosetubi"); // ﾒｯｷ場所設備
//            mapping.put("mekkisyuryounichiji", "mekkisyuryounichiji"); // ﾒｯｷ終了日時
//            mapping.put("syuryousya", "syuryousya"); // 終了担当者
//            mapping.put("kensatannijyuryo", "kensatannijyuryo"); // 検査単位重量
//            mapping.put("kensasoujyuryou", "kensasoujyuryou"); // 検査総重量
//            mapping.put("netusyorijyouken", "netusyorijyouken"); // 熱処理条件
//            mapping.put("netusyorikaisinichiji", "netusyorikaisinichiji"); // 熱処理開始日時
//            mapping.put("netusyoritantousya", "netusyoritantousya"); // 熱処理担当者
//            mapping.put("jisyakusenbetukaisinichiji", "jisyakusenbetukaisinichiji"); // 磁石選別開始日時
//            mapping.put("jisyakusenbetutantousya", "jisyakusenbetutantousya"); // 磁石選別担当者
//            mapping.put("ijouhakkou", "ijouhakkou"); // 異常発行
//            mapping.put("ijourank", "ijourank"); // 異常品ﾗﾝｸ
//            mapping.put("makuatsukakunin", "makuatsukakunin"); // 膜厚確認
//            mapping.put("testhin", "testhin"); // ﾃｽﾄ品
//            mapping.put("tsunpouave", "tsunpouave"); // T寸法AVE(mm)
//            mapping.put("mekkisyurui", "mekkisyurui"); // ﾒｯｷ種類
//            mapping.put("makuatsuni01", "makuatsuni01"); // Ni膜厚01
//            mapping.put("makuatsuni02", "makuatsuni02"); // Ni膜厚02
//            mapping.put("makuatsuni03", "makuatsuni03"); // Ni膜厚03
//            mapping.put("makuatsuni04", "makuatsuni04"); // Ni膜厚04
//            mapping.put("makuatsuni05", "makuatsuni05"); // Ni膜厚05
//            mapping.put("makuatsuni06", "makuatsuni06"); // Ni膜厚06
//            mapping.put("makuatsuni07", "makuatsuni07"); // Ni膜厚07
//            mapping.put("makuatsuni08", "makuatsuni08"); // Ni膜厚08
//            mapping.put("makuatsuni09", "makuatsuni09"); // Ni膜厚09
//            mapping.put("makuatsuni10", "makuatsuni10"); // Ni膜厚10
//            mapping.put("makuatsuni11", "makuatsuni11"); // Ni膜厚11
//            mapping.put("makuatsuni12", "makuatsuni12"); // Ni膜厚12
//            mapping.put("makuatsuni13", "makuatsuni13"); // Ni膜厚13
//            mapping.put("makuatsuni14", "makuatsuni14"); // Ni膜厚14
//            mapping.put("makuatsuni15", "makuatsuni15"); // Ni膜厚15
//            mapping.put("makuatsuni16", "makuatsuni16"); // Ni膜厚16
//            mapping.put("makuatsuni17", "makuatsuni17"); // Ni膜厚17
//            mapping.put("makuatsuni18", "makuatsuni18"); // Ni膜厚18
//            mapping.put("makuatsuni19", "makuatsuni19"); // Ni膜厚19
//            mapping.put("makuatsuni20", "makuatsuni20"); // Ni膜厚20
//            mapping.put("makuatsusn01", "makuatsusn01"); // Sn膜厚01
//            mapping.put("makuatsusn02", "makuatsusn02"); // Sn膜厚02
//            mapping.put("makuatsusn03", "makuatsusn03"); // Sn膜厚03
//            mapping.put("makuatsusn04", "makuatsusn04"); // Sn膜厚04
//            mapping.put("makuatsusn05", "makuatsusn05"); // Sn膜厚05
//            mapping.put("makuatsusn06", "makuatsusn06"); // Sn膜厚06
//            mapping.put("makuatsusn07", "makuatsusn07"); // Sn膜厚07
//            mapping.put("makuatsusn08", "makuatsusn08"); // Sn膜厚08
//            mapping.put("makuatsusn09", "makuatsusn09"); // Sn膜厚09
//            mapping.put("makuatsusn10", "makuatsusn10"); // Sn膜厚10
//            mapping.put("makuatsusn11", "makuatsusn11"); // Sn膜厚11
//            mapping.put("makuatsusn12", "makuatsusn12"); // Sn膜厚12
//            mapping.put("makuatsusn13", "makuatsusn13"); // Sn膜厚13
//            mapping.put("makuatsusn14", "makuatsusn14"); // Sn膜厚14
//            mapping.put("makuatsusn15", "makuatsusn15"); // Sn膜厚15
//            mapping.put("makuatsusn16", "makuatsusn16"); // Sn膜厚16
//            mapping.put("makuatsusn17", "makuatsusn17"); // Sn膜厚17
//            mapping.put("makuatsusn18", "makuatsusn18"); // Sn膜厚18
//            mapping.put("makuatsusn19", "makuatsusn19"); // Sn膜厚19
//            mapping.put("makuatsusn20", "makuatsusn20"); // Sn膜厚20
//
//            BeanProcessor beanProcessor = new BeanProcessor(mapping);
//            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
//            ResultSetHandler<List<GXHDO201B044Model>> beanHandler
//                    = new BeanListHandler<>(GXHDO201B044Model.class, rowProcessor);
//
//            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//            listData = queryRunner.query(sql, beanHandler, params.toArray());
//        } catch (SQLException ex) {
//            listData = new ArrayList<>();
//            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//        }
//    }
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
    private List<Object> createSearchParamSrMekkiList() {
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
            paramStartDateF = DateUtil.convertStringToDate(getStartMekkiDateF(), StringUtil.isEmpty(getStartMekkiTimeF()) ? "0000" : getStartMekkiTimeF());
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startMekkiDateT)) {
            paramStartDateT = DateUtil.convertStringToDate(getStartMekkiDateT(), StringUtil.isEmpty(getStartMekkiTimeT()) ? "2359" : getStartMekkiTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));

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
}
