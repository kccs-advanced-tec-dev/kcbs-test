/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo502;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.common.ColumnInfoParser;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.ParameterEJB;
import jp.co.kccs.xhd.util.DBUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import jp.co.kccs.xhd.common.ResultMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.model.DaMkJoken;
import jp.co.kccs.xhd.db.model.DaMkhyojunjoken;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.model.GXHDO502BModel;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2022/01/11<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO502B(比表面積測定記録機能)
 *
 * @author KCSS wxf
 * @since 2022/01/11
 */
@Named
@ViewScoped
public class GXHDO502B implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO502B.class.getName());
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    protected transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    protected transient DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    protected transient DataSource dataSourceWip;
    /**
     * パラメータ操作(DB)
     */
    @Inject
    protected ParameterEJB parameterEJB;
    /**
     * 一覧表示データ
     */
    private List<GXHDO502BModel> listData = null;
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
     * 警告時処理
     */
    private String warnProcess = "";
    /**
     * 検索条件：WIPLotNo
     */
    private String wipLotNo = "";
    /**
     * 検索条件：品名
     */
    private String hinmei = "";
    /**
     * 検索条件：乾燥開始日時(from)
     */
    private String kansoukaisiDateF = "";
    /**
     * 検索条件：乾燥開始日時(to)
     */
    private String kansoukaisiDateT = "";
    /**
     * 検索条件：流れ品
     */
    private String nagarehin = "";
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantoshaCd = "";
    /**
     *
     * 項目データ
     */
    private List<FXHDD01> itemList;
    /**
     * * 件数0の場合「戻る」のみが表示された画面を表示。
     */
    private String displayStyle = "";
    /**
     * WIPLotNo
     */
    private String paramWIPLotNo = "";
    /**
     * 品名
     */
    private String paramHinmei = "";

    /**
     * ユーザー認証：ユーザー
     */
    private String authUser;
    /**
     * ユーザー認証：パスワード
     */
    private String authPassword;
    /**
     * Excel保存ボタン使用可、使用不可
     */
    private String btnExceldisabled;
    /**
     * 削除使用可、使用不可
     */
    private String btnDeletedisabled;
    /**
     * DaMkJokenリスト
     */
    private List<DaMkJoken> listDaMkJoken;
    /**
     * DaMkhyojunjokenリスト
     */
    private List<DaMkhyojunjoken> listDaMkhyojunjoken;
    //エクセル出力ファイルパス 比表面積測定記録
    private static final String JSON_FILE_PATH_502B = "/WEB-INF/classes/resources/json/gxhdo502b.json";
    //画面名称
    private static final String GAMEN_NAME_502B = "比表面積測定記録";

    /**
     * コンストラクタ
     */
    public GXHDO502B() {
    }

    /**
     * 画面起動時処理
     */
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (IOException e) {
            }
            return;
        }

        // 画面クリア
        clear();
        // 一覧表示最大件数、警告件数を取得処理
        getListCountMaxAndWarnKensu();
        // 流れ品初期表示
        setNagarehin("1");
        // 規格情報ﾁｪｯｸ処理用規格情報を取得
        if (itemList == null || itemList.isEmpty()) {
            this.loadItemSettings();
        }
    }

    /**
     * 一覧表示最大件数、警告件数を取得処理
     *
     */
    public void getListCountMaxAndWarnKensu() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        // user-agent
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        String model = getModel.getModel();

        // userAgentでPC or タブレットを判定
        boolean isPC = false;
        if (model.equals("ie11")) {
            isPC = true;
        }

        // ユーザーグループでメニューマスタを検索
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT gamen_id, gamen_title, title_setting, link_char, menu_name, menu_comment, menu_parameter, gamen_classname, hyouji_kensu FROM fxhdm01 "
                    + "WHERE menu_group_id = 'qcdb_mainMenu' AND gamen_id = 'GXHDO502B' AND "
                    + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

            if (!isPC) {
                sql += " AND pc_flg = '0'";
            }

            sql += " ORDER BY menu_no ";

            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            Map fxhdm01Map = queryRunner.query(sql, new MapHandler(), userGrpList.toArray());
            if (fxhdm01Map != null && !fxhdm01Map.isEmpty()) {
                // 一覧表示最大件数
                listCountMax = fxhdm01Map.get("menu_parameter") != null ? Integer.parseInt(fxhdm01Map.get("menu_parameter").toString()) : -1;
                // 一覧表示警告件数
                listCountWarn = fxhdm01Map.get("hyouji_kensu") != null ? Integer.parseInt(fxhdm01Map.get("hyouji_kensu").toString()) : -1;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog(" 一覧表示最大件数、警告件数を取得処理エラー発生", ex, LOGGER);
        }
    }

    /**
     * 警告OK選択時処理
     *
     * @throws java.sql.SQLException
     */
    public void processWarnOkClick() throws SQLException {
        switch (this.getWarnProcess()) {
            case "OK":
                selectListData();
                break;
            case "checkTorokuUserAuth":
                checkTorokuUserAuth();
                break;
            case "checkDeleteUserAuth":
                checkDeleteUserAuth();
                break;
        }
    }

    /**
     * 【ｸﾘｱ】ﾎﾞﾀﾝ押下時処理
     */
    public void clear() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "clear");
        // 検索条件部の値をクリア
        setWipLotNo("");
        setHinmei("");
        setKansoukaisiDateF("");
        setKansoukaisiDateT("");
        setNagarehin("1");
        setTantoshaCd("");
        // 項目制御:Excel保存ﾎﾞﾀﾝ使用不可
        setBtnExceldisabled("true");
        // 項目制御:削除ﾎﾞﾀﾝ使用不可
        setBtnDeletedisabled("true");
        // 一覧表示部の値を画面初期のように設定：一覧表示部に8行(空白行)を表示する。
        initGXHDO102B024BData();
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、乾燥開始日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setKansoukaisinichij(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getKansoukaisinichij())) {
            setDateTimeItem(listData.get(rowIndex), "kansoukaisinichij");
        }
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、乾燥終了日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setKansousyuuryounichiji(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getKansousyuuryounichiji())) {
            setDateTimeItem(listData.get(rowIndex), "kansousyuuryounichiji");
        }
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、脱脂開始日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setDassikaisinichiji(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getDassikaisinichiji())) {
            setDateTimeItem(listData.get(rowIndex), "dassikaisinichiji");
        }
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、脱脂終了日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setDassisyuuryounichiji(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getDassisyuuryounichiji())) {
            setDateTimeItem(listData.get(rowIndex), "dassisyuuryounichiji");
        }
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、前処理開始日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setMaesyorikaisinichiji(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getMaesyorikaisinichiji())) {
            setDateTimeItem(listData.get(rowIndex), "maesyorikaisinichiji");
        }
    }

    /**
     * 【日時】ﾎﾞﾀﾝ押下時、前処理終了日時の値を設定
     *
     * @param rowIndex 行番号
     */
    public void setMaesyorisyuuryounichiji(int rowIndex) {
        // ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
        GXHDO502BModel gxhdo502bmodel = listData.get(rowIndex);
        if (StringUtil.isEmpty(gxhdo502bmodel.getMaesyorisyuuryounichiji())) {
            setDateTimeItem(listData.get(rowIndex), "maesyorisyuuryounichiji");
        }
    }

    /**
     * ｼｽﾃﾑ日時(YYMMDDHHMM)で項目に設定
     *
     * @param gxhdo502bmodel モデルデータ
     * @param focusItemId ﾌｫｰｶｽの項目
     */
    private void setDateTimeItem(GXHDO502BModel gxhdo502bmodel, String focusItemId) {
        Date date = new Date();
        String dateValue = new SimpleDateFormat("yyMMddHHmm").format(date);
        switch (focusItemId) {
            case "kansoukaisinichij":
                // 乾燥開始日時
                gxhdo502bmodel.setKansoukaisinichij(dateValue);
                break;
            case "kansousyuuryounichiji":
                // 乾燥終了日時
                gxhdo502bmodel.setKansousyuuryounichiji(dateValue);
                break;
            case "dassikaisinichiji":
                // 脱脂開始日時
                gxhdo502bmodel.setDassikaisinichiji(dateValue);
                break;
            case "dassisyuuryounichiji":
                // 脱脂終了日時
                gxhdo502bmodel.setDassisyuuryounichiji(dateValue);
                break;
            case "maesyorikaisinichiji":
                // 前処理開始日時
                gxhdo502bmodel.setMaesyorikaisinichiji(dateValue);
                break;
            case "maesyorisyuuryounichiji":
                // 前処理終了日時
                gxhdo502bmodel.setMaesyorisyuuryounichiji(dateValue);
                break;
        }
    }

    /**
     * 初期表示：一覧表示部に8行(空白行)を表示する。
     *
     */
    private void initGXHDO102B024BData() {
        // 一覧表示部に8行(空白行)を表示する
        if (listData != null) {
            listData.clear();
        } else {
            listData = new ArrayList<>();
        }
        for (int i = 1; i <= 8; i++) {
            listData.add(createGXHDO502BModel(i));
        }
    }

    /**
     * 空白行を作成する
     *
     * @param rowIndx 行番号
     * @return 空白ﾃﾞｰﾀ
     */
    private GXHDO502BModel createGXHDO502BModel(int rowIndx) {
        GXHDO502BModel gxhdo502bmodel = new GXHDO502BModel();
        gxhdo502bmodel.setRowIndx(rowIndx);
        // 削除ﾁｪｯｸﾎﾞｯｸｽ非表示
        gxhdo502bmodel.setChkboxrender("false");
        // 品名使用可
        gxhdo502bmodel.setHinmeidisabled("false");
        // ﾛｯﾄ使用可
        gxhdo502bmodel.setLotdisabled("false");
        // ﾊﾟｽ回数(PASS)使用可
        gxhdo502bmodel.setPasskaisuudisabled("false");
        // 測定回数使用可
        gxhdo502bmodel.setSokuteikaisuudisabled("false");
        // 測定重量規格使用可
        gxhdo502bmodel.setSokuteijyuuryoukikakudisabled("false");
        // 乾燥時間規格使用可
        gxhdo502bmodel.setKansoujikankikakudisabled("false");
        // 脱脂時間規格使用可
        gxhdo502bmodel.setDassijikankikakudisabled("false");
        // 前処理時間使用可
        gxhdo502bmodel.setMaesyorijikandisabled("false");
        // 状態Flag: 0:登録
        gxhdo502bmodel.setJyoutaiFlg("0");
        return gxhdo502bmodel;
    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "search");
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        ArrayList<String> errorItemList = new ArrayList<>();
        // ロットNo
        addError(validateUtil.checkC101(getWipLotNo(), "WIPLotNo", 15), errorItemList);
        // 取込日(FROM)
        boolean errorFlg = addError(validateUtil.checkC101(getKansoukaisiDateF(), "乾燥開始日時(from)", 10), errorItemList);
        if (!errorFlg) {
            errorFlg = addError(validateUtil.checkC201ForDate(getKansoukaisiDateF(), "乾燥開始日時(from)"), errorItemList);
        }
        if (!errorFlg && !StringUtil.isEmpty(getKansoukaisiDateF())) {
            errorFlg = addError(validateUtil.checkC501(getKansoukaisiDateF().substring(0, 6), "乾燥開始日時(from)"), errorItemList);
            if (!errorFlg) {
                addError(validateUtil.checkC502(getKansoukaisiDateF().substring(6, 10), "乾燥開始日時(from)"), errorItemList);
            }
        }

        // 取込日(TO)
        errorFlg = addError(validateUtil.checkC101(getKansoukaisiDateT(), "乾燥開始日時(to)", 10), errorItemList);
        if (!errorFlg) {
            errorFlg = addError(validateUtil.checkC201ForDate(getKansoukaisiDateT(), "乾燥開始日時(to)"), errorItemList);
        }
        if (!errorFlg && !StringUtil.isEmpty(getKansoukaisiDateT())) {
            errorFlg = addError(validateUtil.checkC501(getKansoukaisiDateT().substring(0, 6), "乾燥開始日時(to)"), errorItemList);
            if (!errorFlg) {
                addError(validateUtil.checkC502(getKansoukaisiDateT().substring(6, 10), "乾燥開始日時(to)"), errorItemList);
            }
        }
        if (errorItemList.size() > 0) {
            // 画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
            showResultMsg(errorItemList);
            return;
        }

        // 一覧表示件数を取得
        long count = selectListDataCount();

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
            context.addCallbackParam("param1", "warning");
            this.warnMessage = String.format("検索結果が%d件を超えています。<br/>継続しますか?<br/>%d件", listCountWarn, count);
            this.warnProcess = "OK";
            return;
        }

        // 入力チェックでエラーが存在しない場合検索処理を実行する
        selectListData();
    }

    /**
     * 画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
     *
     * @param resultMessageList メッセージリスト
     */
    private void showResultMsg(List<String> resultMessageList) {
        if (resultMessageList.isEmpty()) {
            return;
        }
        // メッセージを画面に渡す
        ResultMessage resultMessageError = (ResultMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_RESULT_MESSAGE);
        resultMessageError.setResultMessageList(resultMessageList);
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "resultMessage");
    }

    /**
     * 一覧表示データ件数取得
     *
     * @return 検索結果件数
     */
    public long selectListDataCount() {
        long count;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);

            // パラメータ設定
            List<Object> params = createSearchParam();
            String sql = "SELECT COUNT(lot) AS CNT "
                    + " FROM sr_hihyoumensekisokuteikiroku "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR hinmei LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR kansoukaisinichij >= ?) "
                    + "AND   (? IS NULL OR kansoukaisinichij <= ?) "
                    + "AND   (? IS NULL OR nagarehin = ?) ";

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");
        } catch (SQLException ex) {
            count = 8;
            initGXHDO102B024BData();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }

    /**
     * 検索パラメータ生成
     *
     * @return パラメータ
     */
    private List<Object> createSearchParam() {
        // パラメータ設定
        String kojyoParam = null;
        String lotNo9Param = null;
        String edabanParam = null;
        if (!StringUtil.isEmpty(getWipLotNo())) {
            kojyoParam = StringUtil.blankToNull(getWipLotNo()).substring(0, 3); // 工場ｺｰﾄﾞ
            lotNo9Param = StringUtil.blankToNull(getWipLotNo()).substring(3, 12); // ﾛｯﾄNo
            edabanParam = StringUtil.blankToNull(getWipLotNo()).substring(12, 15); // 枝番
        }
        String nagarehinParam = null;
        if (!StringUtil.isEmpty(getNagarehin())) {
            nagarehinParam = StringUtil.blankToNull(getNagarehin()); // 流れ品
        }
        String hinmeiParam = null;
        if (!StringUtil.isEmpty(getHinmei())) {
            hinmeiParam = "%" + DBUtil.escapeString(StringUtil.blankToNull(getHinmei())) + "%"; // 品名
        }

        // 乾燥開始日時From
        Date kansoukaisiDateFParam = null;
        if (!StringUtil.isEmpty(getKansoukaisiDateF())) {
            kansoukaisiDateFParam = DateUtil.convertStringToDateInSeconds(getKansoukaisiDateF().substring(0, 6), getKansoukaisiDateF().substring(6, 10) + "00");
        }
        // 乾燥開始日時To
        Date kansoukaisiDateTParam = null;
        if (!StringUtil.isEmpty(getKansoukaisiDateT())) {
            kansoukaisiDateTParam = DateUtil.convertStringToDateInSeconds(getKansoukaisiDateT().substring(0, 6), getKansoukaisiDateT().substring(6, 10) + "59");

        }
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(kojyoParam, kojyoParam));
        params.addAll(Arrays.asList(lotNo9Param, lotNo9Param));
        params.addAll(Arrays.asList(edabanParam, edabanParam));
        params.addAll(Arrays.asList(hinmeiParam, hinmeiParam));
        params.addAll(Arrays.asList(kansoukaisiDateFParam, kansoukaisiDateFParam));
        params.addAll(Arrays.asList(kansoukaisiDateTParam, kansoukaisiDateTParam));
        params.addAll(Arrays.asList(nagarehinParam, nagarehinParam));

        return params;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);

            // パラメータ設定
            List<Object> params = createSearchParam();

            String sql = "select hinmei, lot, passkaisuu, sokuteikaisuu, nagarehin, CONCAT(kojyo,lotno, edaban) wiplotno, sokuteibutu, no, koutei, "
                    + "sokuteisya, kansouzaranosyurui, rutubono, sokuteijyuuryoukikaku, sokuteijyuuryou, kansouondo, kansoujikankikaku,  "
                    + "DATE_FORMAT(kansoukaisinichij, '%y%m%d%H%i') kansoukaisinichij, DATE_FORMAT(kansousyuuryounichiji, '%y%m%d%H%i') kansousyuuryounichiji, "
                    + "dassirogouki, dassiondo, dassijikankikaku,DATE_FORMAT(dassikaisinichiji, '%y%m%d%H%i') dassikaisinichiji, "
                    + "DATE_FORMAT(dassisyuuryounichiji, '%y%m%d%H%i') dassisyuuryounichiji, kansoutantousya, maesyoriondo, maesyorijikan, "
                    + "DATE_FORMAT(maesyorikaisinichiji, '%y%m%d%H%i') maesyorikaisinichiji, DATE_FORMAT(maesyorisyuuryounichiji, '%y%m%d%H%i') maesyorisyuuryounichiji, "
                    + "maesyoritantousya, sokuteigosoujyuuryou, hihyoumensekisokuteiti, sokuteitantousya, bikou, kosinnichiji "
                    + " FROM sr_hihyoumensekisokuteikiroku "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR hinmei LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR kansoukaisinichij >= ?) "
                    + "AND   (? IS NULL OR kansoukaisinichij <= ?) "
                    + "AND   (? IS NULL OR nagarehin = ?) "
                    + " ORDER BY kansoukaisinichij ASC";

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("hinmei", "hinmei");                                             // 品名
            mapping.put("lot", "lot");                                                   // ﾛｯﾄ
            mapping.put("passkaisuu", "passkaisuu");                                     // ﾊﾟｽ回数(PASS)
            mapping.put("sokuteikaisuu", "sokuteikaisuu");                               // 測定回数
            mapping.put("nagarehin", "nagarehin");                                       // 流れ品
            mapping.put("wiplotno", "wiplotno");                                         // WIPLotNo
            mapping.put("sokuteibutu", "sokuteibutu");                                   // 測定物
            mapping.put("no", "no");                                                     // No
            mapping.put("koutei", "koutei");                                             // 工程
            mapping.put("sokuteisya", "sokuteisya");                                     // 測定者
            mapping.put("kansouzaranosyurui", "kansouzaranosyurui");                     // 乾燥皿の種類
            mapping.put("rutubono", "rutubono");                                         // ﾙﾂﾎﾞNo
            mapping.put("sokuteijyuuryoukikaku", "sokuteijyuuryoukikaku");               // 測定重量規格
            mapping.put("sokuteijyuuryou", "sokuteijyuuryou");                           // 測定重量(g)
            mapping.put("kansouondo", "kansouondo");                                     // 乾燥温度(℃)
            mapping.put("kansoujikankikaku", "kansoujikankikaku");                       // 乾燥時間規格
            mapping.put("kansoukaisinichij", "kansoukaisinichij");                       // 乾燥開始日時
            mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");               // 乾燥終了日時
            mapping.put("dassirogouki", "dassirogouki");                                 // 脱脂炉号機(号機)
            mapping.put("dassiondo", "dassiondo");                                       // 脱脂温度(℃)
            mapping.put("dassijikankikaku", "dassijikankikaku");                         // 脱脂時間規格
            mapping.put("dassikaisinichiji", "dassikaisinichiji");                       // 脱脂開始日時
            mapping.put("dassisyuuryounichiji", "dassisyuuryounichiji");                 // 脱脂終了日時
            mapping.put("kansoutantousya", "kansoutantousya");                           // 乾燥担当者
            mapping.put("maesyoriondo", "maesyoriondo");                                 // 前処理温度(℃)
            mapping.put("maesyorijikan", "maesyorijikan");                               // 前処理時間
            mapping.put("maesyorikaisinichiji", "maesyorikaisinichiji");                 // 前処理開始日時
            mapping.put("maesyorisyuuryounichiji", "maesyorisyuuryounichiji");           // 前処理終了日時
            mapping.put("maesyoritantousya", "maesyoritantousya");                       // 前処理担当者
            mapping.put("sokuteigosoujyuuryou", "sokuteigosoujyuuryou");                 // 測定後総重量(g)
            mapping.put("hihyoumensekisokuteiti", "hihyoumensekisokuteiti");             // 比表面積測定値(㎡/g)
            mapping.put("sokuteitantousya", "sokuteitantousya");                         // 測定担当者
            mapping.put("bikou", "bikou");                                               // 備考
            mapping.put("kosinnichiji", "kosinnichiji");                                 // 更新日時

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO502BModel>> beanHandler
                    = new BeanListHandler<>(GXHDO502BModel.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

            listData = queryRunner.query(sql, beanHandler, params.toArray());
            for (int i = 0; i < listData.size(); i++) {
                GXHDO502BModel gxhdo502bmodel = listData.get(i);
                // 削除ﾁｪｯｸﾎﾞｯｸｽ表示
                gxhdo502bmodel.setChkboxrender("true");
                // 品名使用不可
                gxhdo502bmodel.setHinmeidisabled("true");
                // ﾛｯﾄ使用不可
                gxhdo502bmodel.setLotdisabled("true");
                // ﾊﾟｽ回数(PASS)使用不可
                gxhdo502bmodel.setPasskaisuudisabled("true");
                // 測定回数使用不可
                gxhdo502bmodel.setSokuteikaisuudisabled("true");
                // 測定重量規格使用不可
                gxhdo502bmodel.setSokuteijyuuryoukikakudisabled("true");
                // 乾燥時間規格使用不可
                gxhdo502bmodel.setKansoujikankikakudisabled("true");
                // 脱脂時間規格使用不可
                gxhdo502bmodel.setDassijikankikakudisabled("true");
                // 前処理時間使用不可
                gxhdo502bmodel.setMaesyorijikandisabled("true");
                // 状態Flag: 1:更新
                gxhdo502bmodel.setJyoutaiFlg("1");
                gxhdo502bmodel.setRowIndx(i + 1);
            }
            if (listData.size() < 8) {
                for (int i = listData.size() + 1; i <= 8; i++) {
                    listData.add(createGXHDO502BModel(i));
                }
            }
            // 項目制御:Excel保存ﾎﾞﾀﾝ使用可
            setBtnExceldisabled("false");
            // 項目制御:削除ﾎﾞﾀﾝ使用可
            setBtnDeletedisabled("false");
        } catch (SQLException ex) {
            initGXHDO102B024BData();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 追加処理
     */
    public void addDataRow() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "addRow");
        // 一覧表示件数を取得
        int count = listData.size();
        if (listCountMax > 0 && count > listCountMax) {
            // 検索結果が上限件数以上の場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000224", listCountMax), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        // 項目制御:Excel保存ﾎﾞﾀﾝ使用不可
        setBtnExceldisabled("true");
        // 項目制御:削除ﾎﾞﾀﾝ使用不可
        setBtnDeletedisabled("true");
        listData.add(createGXHDO502BModel(count + 1));
    }

    /**
     * 削除処理確認
     */
    public void confirmDelete() {
        List<GXHDO502BModel> deleteListData = this.getListData().stream().filter(gxhdo502bmodel -> "true".equals(gxhdo502bmodel.getChkboxvalue())).collect(Collectors.toList());
        // 「sr_hihyoumensekisokuteikiroku:比表面積測定記録」から情報取得
        List<GXHDO502BModel> srHihyoumensekisokuteikirokuListData = getSrHihyoumensekisokuteikirokuListData(deleteListData);
        boolean errorFlg = false;
        // 削除の場合、キーのデータがない、あるいは更新日時が表示時と変わっている場合、排他エラーとする。
        for (GXHDO502BModel gxhdo502bmodel : deleteListData) {
            List<GXHDO502BModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502bmodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502bmodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502bmodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && gxhdo502bmodel.getSokuteikaisuu().equals(o.getSokuteikaisuu())
            ).collect(Collectors.toList());
            if (collect.isEmpty()) {
                errorFlg = true;
                break;
            }
            GXHDO502BModel gxhdo502bmodelTableData = collect.get(0);
            Timestamp gamenKosinnichiji = (Timestamp) gxhdo502bmodel.getKosinnichiji();
            Timestamp tableKosinnichiji = (Timestamp) gxhdo502bmodelTableData.getKosinnichiji();
            if (gamenKosinnichiji == null || gamenKosinnichiji.compareTo(tableKosinnichiji) != 0) {
                errorFlg = true;
                break;
            }
        }
        if (errorFlg) {
            showError(MessageUtil.getMessage("XHD-000025"));
            return;
        }
        
        // チェック処理
        long count = this.getListData().stream().filter(gxhdo502bmodel -> "true".equals(gxhdo502bmodel.getChkboxvalue())).count();
        if (count == 0) {
            showError(MessageUtil.getMessage("XHD-000199", "削除ﾁｪｯｸﾎﾞｯｸｽ"));
            return;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "warning");
        this.setWarnMessage("削除します。よろしいですか？");
        this.setWarnProcess("checkDeleteUserAuth");
    }

    /**
     * 削除処理ユーザ認証処理
     */
    public void checkDeleteUserAuth() {

        // ログインユーザーの権限チェックを実施する
        if (!this.userAuthLogin()) {
            // 権限がない場合はユーザー認証画面を表示
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "auth");
        } else {
            doDelete();
        }
    }

    /**
     * 削除処理
     *
     */
    public void doDelete() {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        Connection conDoc = null;
        List<GXHDO502BModel> deleteListData = this.getListData().stream().filter(gxhdo502bmodel -> "true".equals(gxhdo502bmodel.getChkboxvalue())).collect(Collectors.toList());
        try {
            conDoc = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());

            for (GXHDO502BModel gxhdo502bmodel : deleteListData) {
                // 規格情報のﾃﾞｰﾀ削除を行う
                deleteFromSrHihyoumensekisokuteikiroku(queryRunner, conDoc, gxhdo502bmodel);
            }

            List<String> messageList = new ArrayList<>();
            messageList.add(String.format("比表面積測定記録を%d件削除しました", deleteListData.size()));
            ResultMessage beanResultMessage = (ResultMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_RESULT_MESSAGE);
            beanResultMessage.setResultMessageList(messageList);
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "deleteResultMessage");
            DbUtils.commitAndCloseQuietly(conDoc);
            // 画面再表示
            selectListData();

        } catch (SQLException e) {
            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);

            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "実行時エラー", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * 削除
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param gxhdo502bmodel モデルデータ
     * @throws java.sql.SQLException
     */
    public void deleteFromSrHihyoumensekisokuteikiroku(QueryRunner queryRunnerDoc, Connection conDoc, GXHDO502BModel gxhdo502bmodel) throws SQLException {
        String sql = "DELETE FROM sr_hihyoumensekisokuteikiroku WHERE hinmei = ? "
                + "AND lot = ? AND passkaisuu = ? AND sokuteikaisuu = ? ";

        List<Object> params = new ArrayList<>();
        //検索条件設定
        params.add(gxhdo502bmodel.getHinmei());
        params.add(gxhdo502bmodel.getLot());
        params.add(gxhdo502bmodel.getPasskaisuu());
        params.add(gxhdo502bmodel.getSokuteikaisuu());
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 入力値があるデータを取得
     *
     * @return 入力値があるデータリスト
     */
    public List<GXHDO502BModel> getHasValueListdata() {
        if (getListData() == null || getListData().isEmpty()) {
            return new ArrayList<>();
        }
        return getListData().stream().filter(o -> !(StringUtil.isEmpty(o.getHinmei()) && StringUtil.isEmpty(o.getLot()) && o.getPasskaisuu() == null
                && StringUtil.isEmpty(o.getWiplotno()) && StringUtil.isEmpty(o.getSokuteibutu()) && StringUtil.isEmpty(o.getSokuteikaisuu()) && o.getNo() == null && StringUtil.isEmpty(o.getKoutei())
                && StringUtil.isEmpty(o.getSokuteisya()) && StringUtil.isEmpty(o.getKansouzaranosyurui()) && o.getRutubono() == null && StringUtil.isEmpty(o.getSokuteijyuuryoukikaku())
                && o.getSokuteijyuuryou() == null && o.getKansouondo() == null && StringUtil.isEmpty(o.getKansoujikankikaku()) && StringUtil.isEmpty(o.getKansoukaisinichij())
                && StringUtil.isEmpty(o.getKansousyuuryounichiji()) && o.getDassirogouki() == null && o.getDassiondo() == null && StringUtil.isEmpty(o.getDassijikankikaku())
                && StringUtil.isEmpty(o.getDassikaisinichiji()) && StringUtil.isEmpty(o.getDassisyuuryounichiji()) && StringUtil.isEmpty(o.getKansoutantousya())
                && o.getMaesyoriondo() == null && StringUtil.isEmpty(o.getMaesyorijikan()) && StringUtil.isEmpty(o.getMaesyorikaisinichiji()) && StringUtil.isEmpty(o.getMaesyorisyuuryounichiji())
                && StringUtil.isEmpty(o.getMaesyoritantousya()) && o.getSokuteigosoujyuuryou() == null && o.getHihyoumensekisokuteiti() == null && StringUtil.isEmpty(o.getSokuteitantousya())
                && StringUtil.isEmpty(o.getBikou()))
        ).collect(Collectors.toList());
    }

    /**
     * 状態が 0:登録のデータを取得
     *
     * @param hasValueListdata 入力値があるデータリスト
     * @return 状態が 0:登録のデータリスト
     */
    public List<GXHDO502BModel> getInsertListData(List<GXHDO502BModel> hasValueListdata) {
        // 状態が 0:登録 の行を取得する
        List<GXHDO502BModel> insertListData = hasValueListdata.stream().filter(o -> "0".equals(o.getJyoutaiFlg())).collect(Collectors.toList());
        insertListData.sort(Comparator.comparing(GXHDO502BModel::getRowIndx));
        return insertListData;
    }

    /**
     * 状態が 1:更新のデータを取得
     *
     * @param hasValueListdata 入力値があるデータリスト
     * @return 状態が 1:更新のデータリスト
     */
    public List<GXHDO502BModel> getUpdateListData(List<GXHDO502BModel> hasValueListdata) {
        // 状態が 1:更新 の行を取得する
        List<GXHDO502BModel> updateListData = hasValueListdata.stream().filter(o -> "1".equals(o.getJyoutaiFlg())).collect(Collectors.toList());
        updateListData.sort(Comparator.comparing(GXHDO502BModel::getRowIndx));
        return updateListData;
    }

    /**
     * 【登録】ﾎﾞﾀﾝ押下時処理
     */
    public void confirmToroku() {
        // 背景色をクリア
        clearListDataBgcolor();

        // 入力値があるデータを取得
        List<GXHDO502BModel> hasValueListdata = getHasValueListdata();
        // PKが入力しているデータを取得
        List<GXHDO502BModel> hasKeyValueListdata = hasValueListdata.stream().filter(
                o -> !StringUtil.isEmpty(o.getHinmei()) && !StringUtil.isEmpty(o.getLot()) && o.getPasskaisuu() != null && !StringUtil.isEmpty(o.getSokuteikaisuu())
        ).collect(Collectors.toList());
        // 「sr_hihyoumensekisokuteikiroku:比表面積測定記録」から情報取得
        List<GXHDO502BModel> srHihyoumensekisokuteikirokuListData = getSrHihyoumensekisokuteikirokuListData(hasKeyValueListdata);
        // 状態が 0:登録 のﾃﾞｰﾀを取得する
        List<GXHDO502BModel> insertListData = getInsertListData(hasKeyValueListdata);
        // 状態が 1:更新 のﾃﾞｰﾀを取得する
        List<GXHDO502BModel> updateListData = getUpdateListData(hasKeyValueListdata);
        boolean errorFlg = false;
        // 登録の場合、キーのデータがすでにある場合、排他エラーとする。
        for (GXHDO502BModel gxhdo502bmodel : insertListData) {
            List<GXHDO502BModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502bmodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502bmodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502bmodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && gxhdo502bmodel.getSokuteikaisuu().equals(o.getSokuteikaisuu())
            ).collect(Collectors.toList());
            if (collect.size() > 0) {
                errorFlg = true;
                break;
            }
        }
        if (errorFlg) {
            showError(MessageUtil.getMessage("XHD-000026"));
            return;
        }
        // 更新の場合、キーのデータがない、あるいは更新日時が表示時と変わっている場合場合、排他エラーとする。
        for (GXHDO502BModel gxhdo502bmodel : updateListData) {
            List<GXHDO502BModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502bmodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502bmodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502bmodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && gxhdo502bmodel.getSokuteikaisuu().equals(o.getSokuteikaisuu())
            ).collect(Collectors.toList());
            if (collect.isEmpty()) {
                errorFlg = true;
                break;
            }
            GXHDO502BModel gxhdo502bmodelTableData = collect.get(0);
            Timestamp gamenKosinnichiji = (Timestamp) gxhdo502bmodel.getKosinnichiji();
            Timestamp tableKosinnichiji = (Timestamp) gxhdo502bmodelTableData.getKosinnichiji();
            if (gamenKosinnichiji == null || gamenKosinnichiji.compareTo(tableKosinnichiji) != 0) {
                errorFlg = true;
                break;
            }
        }
        if (errorFlg) {
            showError(MessageUtil.getMessage("XHD-000025"));
            return;
        }
        
        // 一覧表示部に表示されているﾃﾞｰﾀにPKが重複している場合、エラーを出る
        Map<String, Long> groupCollect = hasKeyValueListdata.stream().collect(Collectors.groupingBy(o
                -> o.getHinmei() + "_" + o.getLot() + "_" + o.getPasskaisuu() + "_" + o.getSokuteikaisuu(), Collectors.counting()));
        // 重複ﾁｪｯｸ
        List<String> jyuufukuPkeycollect = groupCollect.keySet().stream().filter(key -> groupCollect.get(key) > 1).collect(Collectors.toList());
        if (jyuufukuPkeycollect.size() > 0) {
            showError(MessageUtil.getMessage("XHD-000225"));
            return;
        }

        if (hasValueListdata.isEmpty()) {
            showError(MessageUtil.getMessage("XHD-000226"));
            return;
        }

        // 【登録】ﾎﾞﾀﾝ押下時、値がある行のチェック処理
        ArrayList<String> errorItemList = new ArrayList<>();
        hasValueListdata.forEach((gxhdo502bmodel) -> {
            // 【登録】ﾎﾞﾀﾝ押下時、値がある行のチェック処理
            checkGxhdo502bmodelValue(gxhdo502bmodel, errorItemList);
        });
        // NG数(変数)が0以外の場合ﾒｯｾｰｼﾞの表示
        if (errorItemList.size() > 0) {
            List<String> errorMassageList = new ArrayList<>();
            errorMassageList.add(String.format("NGは%d件です。", errorItemList.size()));
            errorMassageList.addAll(errorItemList);
            // 画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
            showResultMsg(errorMassageList);
            return;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        // 規格情報ﾁｪｯｸ処理用規格情報を取得
        if (itemList == null || itemList.isEmpty()) {
            this.loadItemSettings();
        }
        if (itemList != null && !itemList.isEmpty()) {
            try {
                ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(kikakuchiInputErrorInfoList, hasValueListdata);

                // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
                if (errorMessageInfo != null) {
                    showError(errorMessageInfo.getErrorMessage());
                    return;
                }
                // 規格値エラーがある場合は規格値エラーをセット(警告表示)
                if (!kikakuchiInputErrorInfoList.isEmpty()) {
                    // メッセージを画面に渡す
                    KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
                    kikakuError.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);

                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("firstParam", "kikakuError");
                    return;
                }
            } catch (CloneNotSupportedException ex) {
                ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            }
        }

        // ﾒｯｾｰｼﾞの表示を行う:登録します。よろしいですか？
        showTorokuKakuninDialog();
    }

    /**
     * 登録処理
     *
     * @throws java.sql.SQLException
     */
    public void doToroku() throws SQLException {
        // 入力値があるデータを取得
        List<GXHDO502BModel> hasValueListdata = getHasValueListdata();
        // 状態が 0:登録 のﾃﾞｰﾀを取得する
        List<GXHDO502BModel> insertListData = getInsertListData(hasValueListdata);
        // 状態が 1:更新 のﾃﾞｰﾀを取得する
        List<GXHDO502BModel> updateListData = getUpdateListData(hasValueListdata);
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        Connection conDoc = null;
        try {
            conDoc = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());

            // 状態が 0:登録 のﾃﾞｰﾀを比表面積測定記録にﾃﾞｰﾀ登録を行う
            insertSrHihyoumensekisokuteikiroku(queryRunner, conDoc, insertListData);
            // 状態が 1:更新 のﾃﾞｰﾀを比表面積測定記録にﾃﾞｰﾀ更新を行う
            updateSrHihyoumensekisokuteikiroku(queryRunner, conDoc, updateListData);

            List<String> messageList = new ArrayList<>();
            messageList.add(String.format("比表面積測定記録を%d件登録しました", insertListData.size()));
            messageList.add(String.format("比表面積測定記録を%d件更新しました", updateListData.size()));
            ResultMessage beanResultMessage = (ResultMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_RESULT_MESSAGE);
            beanResultMessage.setResultMessageList(messageList);
            RequestContext.getCurrentInstance().execute("PF('W_dlg_resultMessage').show();");

            DbUtils.commitAndCloseQuietly(conDoc);

            // 画面再表示
            selectListData();
        } catch (SQLException e) {
            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);

            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "実行時エラー", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * 比表面積測定記録(sr_hihyoumensekisokuteikiroku)登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param insertListData 状態が 0:登録 のﾃﾞｰﾀ
     * @throws SQLException 例外エラー
     */
    private void insertSrHihyoumensekisokuteikiroku(QueryRunner queryRunnerDoc, Connection conDoc, List<GXHDO502BModel> insertListData) throws SQLException {

        for (int i = 0; i < insertListData.size(); i++) {
            GXHDO502BModel gxhdo502bmodel = insertListData.get(i);
            String sql = "INSERT INTO sr_hihyoumensekisokuteikiroku ("
                    + "hinmei,lot,passkaisuu,sokuteikaisuu,nagarehin,kojyo,lotno,edaban,sokuteibutu,No,koutei,sokuteisya,kansouzaranosyurui,rutubono,sokuteijyuuryoukikaku,"
                    + "sokuteijyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichij,kansousyuuryounichiji,dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,"
                    + "dassisyuuryounichiji,kansoutantousya,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,maesyoritantousya,sokuteigosoujyuuryou,"
                    + "hihyoumensekisokuteiti,sokuteitantousya,bikou,torokunichiji,kosinnichiji"
                    + ") VALUES ("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            List<Object> params = setUpdateParameterSrHihyoumensekisokuteikiroku(true, gxhdo502bmodel);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(conDoc, sql, params.toArray());
        }
    }

    /**
     * 比表面積測定記録(sr_hihyoumensekisokuteikiroku)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param updateListData 状態が 1:更新 のﾃﾞｰﾀ
     * @throws SQLException 例外エラー
     */
    private void updateSrHihyoumensekisokuteikiroku(QueryRunner queryRunnerDoc, Connection conDoc, List<GXHDO502BModel> updateListData) throws SQLException {

        for (int i = 0; i < updateListData.size(); i++) {
            GXHDO502BModel gxhdo502bmodel = updateListData.get(i);
            String sql = "UPDATE sr_hihyoumensekisokuteikiroku SET "
                    + " nagarehin = ?,kojyo = ?,lotno = ?,edaban = ?,sokuteibutu = ?,No = ?,koutei = ?,sokuteisya = ?,kansouzaranosyurui = ?,rutubono = ?,"
                    + " sokuteijyuuryoukikaku = ?,sokuteijyuuryou = ?,kansouondo = ?,kansoujikankikaku = ?,kansoukaisinichij = ?,kansousyuuryounichiji = ?,"
                    + " dassirogouki = ?,dassiondo = ?,dassijikankikaku = ?,dassikaisinichiji = ?,dassisyuuryounichiji = ?,kansoutantousya = ?,maesyoriondo = ?,"
                    + " maesyorijikan = ?,maesyorikaisinichiji = ?,maesyorisyuuryounichiji = ?,maesyoritantousya = ?,sokuteigosoujyuuryou = ?,hihyoumensekisokuteiti = ?,"
                    + " sokuteitantousya = ?,bikou = ?,kosinnichiji = ?"
                    + "WHERE hinmei = ? AND lot = ? AND passkaisuu = ? AND sokuteikaisuu = ? ";

            List<Object> params = setUpdateParameterSrHihyoumensekisokuteikiroku(false, gxhdo502bmodel);

            //検索条件設定
            params.add(gxhdo502bmodel.getHinmei()); // 品名
            params.add(gxhdo502bmodel.getLot()); // ﾛｯﾄ
            params.add(gxhdo502bmodel.getPasskaisuu()); // ﾊﾟｽ回数(PASS)
            params.add(gxhdo502bmodel.getSokuteikaisuu()); // 測定回数
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(conDoc, sql, params.toArray());
        }
    }

    /**
     * 比表面積測定記録(sr_hihyoumensekisokuteikiroku)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param gxhdo502bmodel モデルデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrHihyoumensekisokuteikiroku(boolean isInsert, GXHDO502BModel gxhdo502bmodel) {
        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp systemTime = new Timestamp(System.currentTimeMillis());
        String strSystime = sdf.format(systemTime);
        String lotNo = gxhdo502bmodel.getWiplotno(); // WIPLotNo
        String kojyo = ""; // 工場ｺｰﾄﾞ
        String lotNo9 = ""; // ﾛｯﾄNo
        String edaban = ""; // 枝番
        if (!StringUtil.isEmpty(lotNo)) {
            kojyo = lotNo.substring(0, 3); // 工場ｺｰﾄﾞ
            lotNo9 = lotNo.substring(3, 12); // ﾛｯﾄNo
            edaban = lotNo.substring(12, 15); // 枝番
        }

        if (isInsert) {
            params.add(gxhdo502bmodel.getHinmei()); // 品名
            params.add(gxhdo502bmodel.getLot()); // ﾛｯﾄ
            params.add(gxhdo502bmodel.getPasskaisuu()); // ﾊﾟｽ回数(PASS)
            params.add(gxhdo502bmodel.getSokuteikaisuu()); // 測定回数
        }
        // 流れ品ﾗｼﾞｵﾎﾞﾀﾝが、流れ品の場合:1 その他の場合:0
        params.add(getNagarehin()); // 流れ品
        params.add(DBUtil.stringToStringObjectDefaultNull(kojyo)); // 工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(lotNo9)); // ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(edaban)); // 枝番
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo502bmodel.getSokuteibutu())); // 測定物
        params.add(gxhdo502bmodel.getNo()); // No
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo502bmodel.getKoutei())); // 工程
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getSokuteisya())); // 測定者
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo502bmodel.getKansouzaranosyurui())); // 乾燥皿の種類
        params.add(gxhdo502bmodel.getRutubono()); // ﾙﾂﾎﾞNo
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getSokuteijyuuryoukikaku())); // 測定重量規格
        params.add(gxhdo502bmodel.getSokuteijyuuryou()); // 測定重量(g)
        params.add(gxhdo502bmodel.getKansouondo()); // 乾燥温度(℃)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getKansoujikankikaku())); // 乾燥時間規格
        String day = StringUtil.isEmpty(gxhdo502bmodel.getKansoukaisinichij()) ? "" : gxhdo502bmodel.getKansoukaisinichij().substring(0, 6);
        String time = StringUtil.isEmpty(gxhdo502bmodel.getKansoukaisinichij()) ? "" : gxhdo502bmodel.getKansoukaisinichij().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 乾燥開始日時
        day = StringUtil.isEmpty(gxhdo502bmodel.getKansousyuuryounichiji()) ? "" : gxhdo502bmodel.getKansousyuuryounichiji().substring(0, 6);
        time = StringUtil.isEmpty(gxhdo502bmodel.getKansousyuuryounichiji()) ? "" : gxhdo502bmodel.getKansousyuuryounichiji().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 乾燥終了日時
        params.add(gxhdo502bmodel.getDassirogouki()); // 脱脂炉号機(号機)
        params.add(gxhdo502bmodel.getDassiondo()); // 脱脂温度(℃)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getDassijikankikaku())); // 脱脂時間規格
        day = StringUtil.isEmpty(gxhdo502bmodel.getDassikaisinichiji()) ? "" : gxhdo502bmodel.getDassikaisinichiji().substring(0, 6);
        time = StringUtil.isEmpty(gxhdo502bmodel.getDassikaisinichiji()) ? "" : gxhdo502bmodel.getDassikaisinichiji().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 脱脂開始日時
        day = StringUtil.isEmpty(gxhdo502bmodel.getDassisyuuryounichiji()) ? "" : gxhdo502bmodel.getDassisyuuryounichiji().substring(0, 6);
        time = StringUtil.isEmpty(gxhdo502bmodel.getDassisyuuryounichiji()) ? "" : gxhdo502bmodel.getDassisyuuryounichiji().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 脱脂終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getKansoutantousya())); // 乾燥担当者
        params.add(gxhdo502bmodel.getMaesyoriondo()); // 前処理温度(℃)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getMaesyorijikan())); // 前処理時間
        day = StringUtil.isEmpty(gxhdo502bmodel.getMaesyorikaisinichiji()) ? "" : gxhdo502bmodel.getMaesyorikaisinichiji().substring(0, 6);
        time = StringUtil.isEmpty(gxhdo502bmodel.getMaesyorikaisinichiji()) ? "" : gxhdo502bmodel.getMaesyorikaisinichiji().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 前処理開始日時
        day = StringUtil.isEmpty(gxhdo502bmodel.getMaesyorisyuuryounichiji()) ? "" : gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(0, 6);
        time = StringUtil.isEmpty(gxhdo502bmodel.getMaesyorisyuuryounichiji()) ? "" : gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(6, 10);
        params.add(DBUtil.stringToDateObjectDefaultNull(day, "".equals(time) ? "0000" : time)); // 前処理終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getMaesyoritantousya())); // 前処理担当者
        params.add(gxhdo502bmodel.getSokuteigosoujyuuryou()); // 測定後総重量(g)
        params.add(gxhdo502bmodel.getHihyoumensekisokuteiti()); // 比表面積測定値(㎡/g)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getSokuteitantousya())); // 測定担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502bmodel.getBikou())); // 備考
        if (isInsert) {
            params.add(strSystime); // 登録日時
            params.add(strSystime); // 更新日時
        } else {
            params.add(strSystime); // 更新日時
        }

        return params;
    }

    /**
     * 規格外エラーダイアログOK押下時
     */
    public void processKikakuWarnOk() {
        // ﾒｯｾｰｼﾞの表示を行う:登録します。よろしいですか？
        showTorokuKakuninDialog();
    }

    /**
     * ﾒｯｾｰｼﾞの表示を行う:登録します。よろしいですか？(OK/Cancel)
     */
    public void showTorokuKakuninDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "warning");
        this.setWarnMessage("登録します。よろしいですか？");
        this.setWarnProcess("checkTorokuUserAuth");
    }

    /**
     * 桁数チェック
     *
     * @param value 入力値
     * @param itemName 項目名
     * @param length 桁数
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC101(String value, String itemName, int length) {
        // エラー対象をリストに追加
        if (StringUtil.isEmpty(value) || length != StringUtil.getLength(value)) {
            return MessageUtil.getMessage("XHD-000004", itemName, length);
        }
        return null;
    }

    /**
     * 対象のIDの項目データがあるか判定(無ければエラー項目名リストに項目名を追加)
     *
     * @param errorItemNameList エラー項目名リスト
     * @param itemList 項目リスト
     * @param itemId 項目ID
     * @param itemName 項目名
     */
    private void checkExistItem(List<String> errorItemNameList, List<FXHDD01> itemList, String itemId) {
        if (getItemRow(itemList, itemId) == null) {
            errorItemNameList.add(itemId);
        }
    }

    /**
     * 項目データ取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        return listData.stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
    }

    /**
     * 画面項目存在チェック(ﾌｫｰﾑﾊﾟﾗﾒｰﾀに対象の項目が存在していることを確認)
     *
     * @param processData 処理制御データ
     * @return エラーがあるがどうかフラグ
     */
    private boolean checkExistFormItem() {
        List<String> errorItemIdList = new ArrayList<>();
        checkExistItem(errorItemIdList, itemList, GXHDO502BConst.SOKUTEIJYUURYOUKIKAKU);
        checkExistItem(errorItemIdList, itemList, GXHDO502BConst.KANSOUJIKANKIKAKU);
        checkExistItem(errorItemIdList, itemList, GXHDO502BConst.DASSIJIKANKIKAKU);
        checkExistItem(errorItemIdList, itemList, GXHDO502BConst.MAESYORIJIKAN);
        return errorItemIdList.size() > 0;
    }

    /**
     * 【一覧表示部.WIPLotNo】ﾛｽﾄﾌｫｰｶｽ時処理
     *
     * @param wipLotNoStr 入力されたWIPLotNoの値
     * @param rowIndex フォーカスアウトあれる開始時刻の行目
     * @throws SQLException 例外エラー
     */
    public void doWIPLotNoFocusblur(String wipLotNoStr, int rowIndex) throws SQLException {

        GXHDO502BModel gxhdo502bmodelItem = getListData().stream().filter(gxhdo502bmodel -> gxhdo502bmodel.getRowIndx() == rowIndex).findFirst().orElse(null);
        if (gxhdo502bmodelItem == null) {
            return;
        }
        if (StringUtil.isEmpty(gxhdo502bmodelItem.getSokuteibutu()) || !"0".equals(gxhdo502bmodelItem.getSokuteibutu())) {
            return;
        }

        // 背景色をクリア
        clearListDataBgcolor();
        // WIPLotNo
        if (showError(checkC101(wipLotNoStr, "WIPLotNo", 15))) {
            gxhdo502bmodelItem.setWiplotnobgcolor(GXHDO502BConst.ERROR_COLOR);
            return;
        }

        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);

        String kojyo = wipLotNoStr.substring(0, 3); // 工場ｺｰﾄﾞ
        String lotNo9 = wipLotNoStr.substring(3, 12); // ﾛｯﾄNo
        String edaban = wipLotNoStr.substring(12, 15); // 枝番
        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, StringUtil.nullToBlank(getTantoshaCd()), wipLotNoStr);
        if (shikakariData == null || shikakariData.isEmpty()) {
            showError(MessageUtil.getMessage("XHD-000227", "WIPLotNo"));
            return;
        }
        // 品名
        String strHinmei = StringUtil.nullToBlank(shikakariData.get("hinmei"));
        if ("0".equals(StringUtil.nullToBlank(gxhdo502bmodelItem.getJyoutaiFlg()))) {
            gxhdo502bmodelItem.setHinmei(strHinmei);
            gxhdo502bmodelItem.setLot(StringUtil.nullToBlank(shikakariData.get("lotno")));
        }
        boolean setKikakuchiFlg = true;
        String syurui = "比表面積測定記録";
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            setKikakuchiFlg = false;
        }
        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));

        // 規格情報ﾁｪｯｸ処理用規格情報を取得
        if (itemList == null || itemList.isEmpty()) {
            this.loadItemSettings();
        }
        if (itemList == null || itemList.isEmpty() || checkExistFormItem()) {
            showError(MessageUtil.getMessage("XHD-000228"));
            setKikakuchiFlg = false;
        }
        if (setKikakuchiFlg) {
            ArrayList<String> kouteimeiList = new ArrayList<>();
            itemList.forEach((fxhdd01) -> {
                if (!kouteimeiList.contains(StringUtil.nullToBlank(fxhdd01.getJokenKoteiMei()))) {
                    kouteimeiList.add(StringUtil.nullToBlank(fxhdd01.getJokenKoteiMei()));
                }
                fxhdd01.setRender1(false);
            });
            // DaMkJoken情報取得 (6)前工程規格情報より、情報の取得を行う
            getMkJokenInfo(sekkeiNo, kouteimeiList);
            // Mkhyojunjoken情報取得 (8)前工程標準規格情報より、情報の取得を行う
            getMkhyojunjoken(strHinmei, pattern, kouteimeiList);
            if ((listDaMkJoken == null || listDaMkJoken.isEmpty()) && (listDaMkhyojunjoken == null || listDaMkhyojunjoken.isEmpty())) {
                return;
            }
            itemList.stream().map((fxhdd01) -> {
                if (listDaMkJoken != null && !listDaMkJoken.isEmpty()) {
                    listDaMkJoken.stream().filter(damkjoken -> StringUtil.nullToBlank(fxhdd01.getJokenKoteiMei()).equals(StringUtil.nullToBlank(damkjoken.getKouteimei()))
                            && StringUtil.nullToBlank(fxhdd01.getJokenKomokuMei()).equals(StringUtil.nullToBlank(damkjoken.getKoumokumei()))
                            && StringUtil.nullToBlank(fxhdd01.getJokenKanriKomoku()).equals(StringUtil.nullToBlank(damkjoken.getKanrikoumokumei()))
                    ).forEachOrdered(damkjoken -> {
                        fxhdd01.setKikakuChi(damkjoken.getKikakuti());
                        setItemKikakuchi(gxhdo502bmodelItem, fxhdd01.getKikakuChi(), fxhdd01.getItemId());
                        fxhdd01.setRender1(true);
                    });
                }
                return fxhdd01;
            }).filter((fxhdd01) -> (listDaMkhyojunjoken != null && !listDaMkhyojunjoken.isEmpty() && !fxhdd01.isRender1())).forEachOrdered((fxhdd01) -> {
                listDaMkhyojunjoken.stream().filter(damkhyojunjoken -> StringUtil.nullToBlank(fxhdd01.getJokenKoteiMei()).equals(StringUtil.nullToBlank(damkhyojunjoken.getKouteimei()))
                        && StringUtil.nullToBlank(fxhdd01.getJokenKomokuMei()).equals(StringUtil.nullToBlank(damkhyojunjoken.getKoumokumei()))
                        && StringUtil.nullToBlank(fxhdd01.getJokenKanriKomoku()).equals(StringUtil.nullToBlank(damkhyojunjoken.getKanrikoumokumei()))
                ).forEachOrdered(damkhyojunjoken -> {
                    fxhdd01.setKikakuChi(damkhyojunjoken.getKikakuti());
                    setItemKikakuchi(gxhdo502bmodelItem, fxhdd01.getKikakuChi(), fxhdd01.getItemId());
                    fxhdd01.setRender1(true);
                });
            });
        }
    }

    /**
     * 項目規格値を設定
     *
     * @param gxhdo502bmodel モデルデータ
     * @param kikakuChi 項目規格値
     * @param itemId 項目Id
     */
    private void setItemKikakuchi(GXHDO502BModel gxhdo502bmodel, String kikakuChi, String itemId) {
        switch (itemId) {
            case GXHDO502BConst.SOKUTEIJYUURYOUKIKAKU:
                // 測定重量規格
                gxhdo502bmodel.setSokuteijyuuryoukikaku(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502BConst.KANSOUJIKANKIKAKU:
                // 乾燥時間規格
                gxhdo502bmodel.setKansoujikankikaku(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502BConst.DASSIJIKANKIKAKU:
                // 脱脂時間規格
                gxhdo502bmodel.setDassijikankikaku(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502BConst.MAESYORIJIKAN:
                // 前処理時間規格
                gxhdo502bmodel.setMaesyorijikan(StringUtil.nullToBlank(kikakuChi));
                break;
        }
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
     * 前工程WIPから仕掛情報を取得する。
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者コード
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariDataFromWip(QueryRunner queryRunnerDoc, String tantoshaCd, String lotNo) throws SQLException {
        List<SikakariJson> sikakariList = CommonUtil.getMwipResult(queryRunnerDoc, tantoshaCd, lotNo);
        SikakariJson sikakariObj;
        Map shikakariData = new HashMap();
        if (sikakariList != null) {
            sikakariObj = sikakariList.get(0);
            // 前工程WIPから取得した品名
            shikakariData.put("hinmei", sikakariObj.getHinmei());
            shikakariData.put("oyalotedaban", sikakariObj.getOyaLotEdaBan());
            shikakariData.put("lotkubuncode", sikakariObj.getLotKubunCode());
            shikakariData.put("lotkubun", sikakariObj.getLotkubun());
            shikakariData.put("lotno", sikakariObj.getConventionalLot());
        }

        return shikakariData;
    }

    /**
     * [前工程設計]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkSekKeiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String syurui) throws SQLException {
        // 前工程設計データの取得
        String sql = "SELECT sekkeino, pattern FROM da_mksekkei"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND syurui = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(syurui);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 数値→文字列に転換
     *
     * @param intValue 数値
     * @return 文字列
     */
    private String convertIntToString(Integer intValue) {
        if (intValue == null) {
            return "";
        }
        return String.valueOf(intValue);
    }

    /**
     * 【登録】ﾎﾞﾀﾝ押下時のチェック処理
     *
     * @param gxhdo502bmodel モデルデータ
     * @param errorItemList チェックエラーリストデータ
     */
    @SuppressWarnings("empty-statement")
    private void checkGxhdo502bmodelValue(GXHDO502BModel gxhdo502bmodel, ArrayList<String> errorItemList) {
        ValidateUtil validateUtil = new ValidateUtil();
        // 行番号情報
        String rowIndexInfo = gxhdo502bmodel.getRowIndx() + "行目: ";
        // 品名
        if (addError(validateUtil.checkC001(gxhdo502bmodel.getHinmei(), "品名"), errorItemList, rowIndexInfo)) {
            gxhdo502bmodel.setHinmeibgcolor(GXHDO502BConst.ERROR_COLOR);
        };
        // ﾛｯﾄ
        if (addError(validateUtil.checkC001(gxhdo502bmodel.getLot(), "ﾛｯﾄ"), errorItemList, rowIndexInfo)) {
            gxhdo502bmodel.setLotbgcolor(GXHDO502BConst.ERROR_COLOR);
        };
        // ﾊﾟｽ回数(PASS)
        if (addError(validateUtil.checkC001(convertIntToString(gxhdo502bmodel.getPasskaisuu()), "ﾊﾟｽ回数(PASS)"), errorItemList, rowIndexInfo)) {
            gxhdo502bmodel.setPasskaisuubgcolor(GXHDO502BConst.ERROR_COLOR);
        };
        // WIPLotNo
        if (!StringUtil.isEmpty(gxhdo502bmodel.getWiplotno())) {
            if (addError(validateUtil.checkC101(gxhdo502bmodel.getWiplotno(), "WIPLotNo", 15), errorItemList, rowIndexInfo)) {
                gxhdo502bmodel.setWiplotnobgcolor(GXHDO502BConst.ERROR_COLOR);
            };
        }
        // 測定物
        String sokuteibutu = StringUtil.nullToBlank(gxhdo502bmodel.getSokuteibutu());
        // 測定物が製品の場合、WIPLotNoが入力されていること
        if ("0".equals(sokuteibutu)) {
            // WIPLotNo
            if (addError(validateUtil.checkC001(gxhdo502bmodel.getWiplotno(), "WIPLotNo"), errorItemList, rowIndexInfo)) {
                gxhdo502bmodel.setWiplotnobgcolor(GXHDO502BConst.ERROR_COLOR);
            };
        } else {
            // 測定物が製品以外の場合、WIPLotNoが入力されていないこと
            if (!StringUtil.isEmpty(gxhdo502bmodel.getWiplotno())) {
                errorItemList.add(MessageUtil.getMessage("XHD-000229", rowIndexInfo + "WIPLotNo"));
                gxhdo502bmodel.setWiplotnobgcolor(GXHDO502BConst.ERROR_COLOR);
            }
        }
        // 測定回数
        if (addError(validateUtil.checkC001(StringUtil.nullToBlank(gxhdo502bmodel.getSokuteikaisuu()), "測定回数"), errorItemList, rowIndexInfo)) {
            gxhdo502bmodel.setSokuteikaisuubgcolor(GXHDO502BConst.ERROR_COLOR);
        };
        boolean errorFlgKansoukaisinichij = false;
        boolean errorFlgKansousyuuryounichiji = false;
        boolean errorFlgDassikaisinichiji = false;
        boolean errorFlgDassisyuuryounichiji = false;
        boolean errorFlgMaesyorikaisinichiji = false;
        boolean errorFlgMaesyorisyuuryounichiji = false;
        // 乾燥開始日時
        boolean errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getKansoukaisinichij(), "乾燥開始日時", 10), errorItemList, rowIndexInfo);
        boolean errorFlgC201 = false;
        boolean errorFlgC501 = false;
        boolean errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getKansoukaisinichij(), "乾燥開始日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getKansoukaisinichij())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getKansoukaisinichij().substring(0, 6), "乾燥開始日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getKansoukaisinichij().substring(6, 10), "乾燥開始日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setKansoukaisinichijbgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgKansoukaisinichij = true;
        }

        // 乾燥終了日時
        errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getKansousyuuryounichiji(), "乾燥終了日時", 10), errorItemList, rowIndexInfo);
        errorFlgC201 = false;
        errorFlgC501 = false;
        errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getKansousyuuryounichiji(), "乾燥終了日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getKansousyuuryounichiji())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getKansousyuuryounichiji().substring(0, 6), "乾燥終了日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getKansousyuuryounichiji().substring(6, 10), "乾燥終了日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setKansousyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgKansousyuuryounichiji = true;
        }

        // 脱脂開始日時
        errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getDassikaisinichiji(), "脱脂開始日時", 10), errorItemList, rowIndexInfo);
        errorFlgC201 = false;
        errorFlgC501 = false;
        errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getDassikaisinichiji(), "脱脂開始日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getDassikaisinichiji())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getDassikaisinichiji().substring(0, 6), "脱脂開始日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getDassikaisinichiji().substring(6, 10), "脱脂開始日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setDassikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgDassikaisinichiji = true;
        }

        // 脱脂終了日時
        errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getDassisyuuryounichiji(), "脱脂終了日時", 10), errorItemList, rowIndexInfo);
        errorFlgC201 = false;
        errorFlgC501 = false;
        errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getDassisyuuryounichiji(), "脱脂終了日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getDassisyuuryounichiji())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getDassisyuuryounichiji().substring(0, 6), "脱脂終了日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getDassisyuuryounichiji().substring(6, 10), "脱脂終了日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setDassisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgDassisyuuryounichiji = true;
        }

        // 前処理開始日時
        errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getMaesyorikaisinichiji(), "前処理開始日時", 10), errorItemList, rowIndexInfo);
        errorFlgC201 = false;
        errorFlgC501 = false;
        errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getMaesyorikaisinichiji(), "前処理開始日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getMaesyorikaisinichiji())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getMaesyorikaisinichiji().substring(0, 6), "前処理開始日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getMaesyorikaisinichiji().substring(6, 10), "前処理開始日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setMaesyorikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgMaesyorikaisinichiji = true;
        }

        // 前処理終了日時
        errorFlgC101 = addError(validateUtil.checkC101(gxhdo502bmodel.getMaesyorisyuuryounichiji(), "前処理終了日時", 10), errorItemList, rowIndexInfo);
        errorFlgC201 = false;
        errorFlgC501 = false;
        errorFlgC502 = false;
        if (!errorFlgC101) {
            errorFlgC201 = addError(validateUtil.checkC201ForDate(gxhdo502bmodel.getMaesyorisyuuryounichiji(), "前処理終了日時"), errorItemList, rowIndexInfo);
            if (!errorFlgC201 && !StringUtil.isEmpty(gxhdo502bmodel.getMaesyorisyuuryounichiji())) {
                errorFlgC501 = addError(validateUtil.checkC501(gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(0, 6), "前処理終了日時"), errorItemList, rowIndexInfo);
                if (!errorFlgC501) {
                    errorFlgC502 = addError(validateUtil.checkC502(gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(6, 10), "前処理終了日時"), errorItemList, rowIndexInfo);
                }
            }
        }
        if (errorFlgC101 || errorFlgC201 || errorFlgC501 || errorFlgC502) {
            gxhdo502bmodel.setMaesyorisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            errorFlgMaesyorisyuuryounichiji = true;
        }

        // 乾燥開始日時、乾燥終了日時、共に入力されている場合、時刻前後ﾁｪｯｸ(①.R-001参照)
        if (!errorFlgKansoukaisinichij && !errorFlgKansousyuuryounichiji && !StringUtil.isEmpty(gxhdo502bmodel.getKansoukaisinichij())
                && !StringUtil.isEmpty(gxhdo502bmodel.getKansousyuuryounichiji())) {
            Date kaisiDate = DateUtil.convertStringToDate(gxhdo502bmodel.getKansoukaisinichij().substring(0, 6), gxhdo502bmodel.getKansoukaisinichij().substring(6, 10));
            Date syuuryouDate = DateUtil.convertStringToDate(gxhdo502bmodel.getKansousyuuryounichiji().substring(0, 6), gxhdo502bmodel.getKansousyuuryounichiji().substring(6, 10));
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001("乾燥開始日時", kaisiDate, "乾燥終了日時", syuuryouDate);
            if (addError(msgCheckR001, errorItemList, rowIndexInfo)) {
                gxhdo502bmodel.setKansoukaisinichijbgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setKansousyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            };
        }

        // 脱脂開始日時、脱脂終了日時、共に入力されている場合、時刻前後ﾁｪｯｸ(①.R-001参照)
        if (!errorFlgDassikaisinichiji && !errorFlgDassisyuuryounichiji && !StringUtil.isEmpty(gxhdo502bmodel.getDassikaisinichiji())
                && !StringUtil.isEmpty(gxhdo502bmodel.getDassisyuuryounichiji())) {
            Date kaisiDate = DateUtil.convertStringToDate(gxhdo502bmodel.getDassikaisinichiji().substring(0, 6), gxhdo502bmodel.getDassikaisinichiji().substring(6, 10));
            Date syuuryouDate = DateUtil.convertStringToDate(gxhdo502bmodel.getDassisyuuryounichiji().substring(0, 6), gxhdo502bmodel.getDassisyuuryounichiji().substring(6, 10));
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001("脱脂開始日時", kaisiDate, "脱脂終了日時", syuuryouDate);
            if (addError(msgCheckR001, errorItemList, rowIndexInfo)) {
                gxhdo502bmodel.setDassikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setDassisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            };
        }
        // 前処理開始日時、前処理終了日時、共に入力されている場合、時刻前後ﾁｪｯｸ(①.R-001参照)
        if (!errorFlgMaesyorikaisinichiji && !errorFlgMaesyorisyuuryounichiji && !StringUtil.isEmpty(gxhdo502bmodel.getMaesyorikaisinichiji())
                && !StringUtil.isEmpty(gxhdo502bmodel.getMaesyorisyuuryounichiji())) {
            Date kaisiDate = DateUtil.convertStringToDate(gxhdo502bmodel.getMaesyorikaisinichiji().substring(0, 6), gxhdo502bmodel.getMaesyorikaisinichiji().substring(6, 10));
            Date syuuryouDate = DateUtil.convertStringToDate(gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(0, 6), gxhdo502bmodel.getMaesyorisyuuryounichiji().substring(6, 10));
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001("前処理開始日時", kaisiDate, "前処理終了日時", syuuryouDate);
            if (addError(msgCheckR001, errorItemList, rowIndexInfo)) {
                gxhdo502bmodel.setMaesyorikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setMaesyorisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
            };
        }
    }

    /**
     * 項目データ取得
     *
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(String itemId) {
        return getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
    }

    /**
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param hasValueListdata リストデータ
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList, List<GXHDO502BModel> hasValueListdata) throws CloneNotSupportedException {
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> kikakuItemList = new ArrayList<>();
        for (GXHDO502BModel gxhdo502bmodel : hasValueListdata) {
            // 規格情報ﾁｪｯｸ処理:測定重量が入力されている場合、測定重量ﾁｪｯｸ。 測定重量の値が、測定重量規格の範囲内であること。
            // 測定重量
            BigDecimal sokuteijyuuryou = gxhdo502bmodel.getSokuteijyuuryou();
            // 測定重量規格
            String sokuteijyuuryoukikaku = gxhdo502bmodel.getSokuteijyuuryoukikaku();
            FXHDD01 itemSokuteijyuuryoukikaku = getItemRow(GXHDO502BConst.SOKUTEIJYUURYOUKIKAKU);
            if (itemSokuteijyuuryoukikaku != null && sokuteijyuuryou != null) {
                FXHDD01 itemSokuteijyuuryou = itemSokuteijyuuryoukikaku.clone();
                itemSokuteijyuuryou.setValue(sokuteijyuuryou.toPlainString()); // 測定重量
                itemSokuteijyuuryou.setKikakuChi(sokuteijyuuryoukikaku); // 測定重量規格
                itemSokuteijyuuryou.setStandardPattern(itemSokuteijyuuryoukikaku.getStandardPattern());
                itemSokuteijyuuryou.setLabel1(gxhdo502bmodel.getRowIndx() + "行目: 測定重量");
                itemSokuteijyuuryou.setRenderInputText(true);
                kikakuItemList.add(itemSokuteijyuuryou);
            }

            // 乾燥開始日時、乾燥終了日時、共に入力されている場合、乾燥時間ﾁｪｯｸ
            adddiffMinutesKikakuItem(gxhdo502bmodel, kikakuItemList, GXHDO502BConst.KANSOUJIKANKIKAKU);
            // 脱脂開始日時、脱脂終了日時、共に入力されている場合、脱脂時間ﾁｪｯｸ
            adddiffMinutesKikakuItem(gxhdo502bmodel, kikakuItemList, GXHDO502BConst.DASSIJIKANKIKAKU);
            // 前処理開始日時、前処理終了日時、共に入力されている場合、前処理時間ﾁｪｯｸ
            adddiffMinutesKikakuItem(gxhdo502bmodel, kikakuItemList, GXHDO502BConst.MAESYORIJIKAN);
        }

        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(kikakuItemList, kikakuchiInputErrorInfoList);
        // エラー項目の背景色を設定
        if (errorMessageInfo == null) {
            kikakuchiInputErrorInfoList.forEach((kikakuchiinputerrorinfo) -> {
                String itemId = kikakuchiinputerrorinfo.getItemId();
                int rowIndex = Integer.parseInt(kikakuchiinputerrorinfo.getItemLabel().substring(0, kikakuchiinputerrorinfo.getItemLabel().indexOf("行目")));
                GXHDO502BModel gxhdo502bmodelItem = hasValueListdata.stream().filter(gxhdo502bmodel -> gxhdo502bmodel.getRowIndx() == rowIndex).findFirst().orElse(null);
                setErrorMessageInfoKikakuchiBgcolor(itemId, gxhdo502bmodelItem);
            });
        } else {
            errorMessageInfo.setPageChangeItemIndex(-1);
            int rowIndex = Integer.parseInt(errorMessageInfo.getErrorMessage().substring(0, errorMessageInfo.getErrorMessage().indexOf("行目")));
            GXHDO502BModel gxhdo502bmodelItem = hasValueListdata.stream().filter(gxhdo502bmodel -> gxhdo502bmodel.getRowIndx() == rowIndex).findFirst().orElse(null);
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            setErrorMessageInfoKikakuchiBgcolor(itemId, gxhdo502bmodelItem);
        }
        return errorMessageInfo;
    }

    /**
     * 規格チェックのエラー項目の背景色を設定
     *
     * @param itemId 項目ID
     * @param hasValueListdata リストデータ
     * @return 項目データ
     */
    private void setErrorMessageInfoKikakuchiBgcolor(String itemId, GXHDO502BModel gxhdo502bmodel) {
        switch (itemId) {
            // 測定重量規格
            case GXHDO502BConst.SOKUTEIJYUURYOUKIKAKU:
                gxhdo502bmodel.setSokuteijyuuryoubgcolor(GXHDO502BConst.ERROR_COLOR);
                break;
            // 乾燥時間規格
            case GXHDO502BConst.KANSOUJIKANKIKAKU:
                gxhdo502bmodel.setKansoukaisinichijbgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setKansousyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                break;
            // 脱脂時間規格
            case GXHDO502BConst.DASSIJIKANKIKAKU:
                gxhdo502bmodel.setDassikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setDassisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                break;
            // 前処理時間規格
            case GXHDO502BConst.MAESYORIJIKAN:
                gxhdo502bmodel.setMaesyorikaisinichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                gxhdo502bmodel.setMaesyorisyuuryounichijibgcolor(GXHDO502BConst.ERROR_COLOR);
                break;
        }
    }

    /**
     * 終了時間-開始時間で計算して、算出した差分分数を項目の規格値に設定する
     *
     * @param gxhdo502bmodel モデルデータ
     * @param kikakuItemList 規格値の入力値チェック必要の項目リスト
     * @param itemId 項目Id
     */
    private void adddiffMinutesKikakuItem(GXHDO502BModel gxhdo502bmodel, List<FXHDD01> kikakuItemList, String itemId) throws CloneNotSupportedException {
        // 開始日時
        String kaisinichij = "";
        // 終了日時
        String syuuryounichiji = "";
        // 時間規格
        String jikankikaku = "";
        FXHDD01 itemJikankikaku = null;
        String label1 = "";
        switch (itemId) {
            // 乾燥時間規格
            case GXHDO502BConst.KANSOUJIKANKIKAKU:
                kaisinichij = gxhdo502bmodel.getKansoukaisinichij();
                syuuryounichiji = gxhdo502bmodel.getKansousyuuryounichiji();
                jikankikaku = gxhdo502bmodel.getKansoujikankikaku();
                itemJikankikaku = getItemRow(GXHDO502BConst.KANSOUJIKANKIKAKU);
                label1 = gxhdo502bmodel.getRowIndx() + "行目: 乾燥時間";
                break;
            // 脱脂時間規格
            case GXHDO502BConst.DASSIJIKANKIKAKU:
                kaisinichij = gxhdo502bmodel.getDassikaisinichiji();
                syuuryounichiji = gxhdo502bmodel.getDassisyuuryounichiji();
                jikankikaku = gxhdo502bmodel.getDassijikankikaku();
                itemJikankikaku = getItemRow(GXHDO502BConst.DASSIJIKANKIKAKU);
                label1 = gxhdo502bmodel.getRowIndx() + "行目: 脱脂時間";
                break;
            // 前処理時間規格
            case GXHDO502BConst.MAESYORIJIKAN:
                kaisinichij = gxhdo502bmodel.getMaesyorikaisinichiji();
                syuuryounichiji = gxhdo502bmodel.getMaesyorisyuuryounichiji();
                jikankikaku = gxhdo502bmodel.getMaesyorijikan();
                itemJikankikaku = getItemRow(GXHDO502BConst.MAESYORIJIKAN);
                label1 = gxhdo502bmodel.getRowIndx() + "行目: 前処理時間";
                break;
        }

        // 開始日時、終了日時、共に入力されている場合、時間ﾁｪｯｸ
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        if (itemJikankikaku != null && !StringUtil.isEmpty(kaisinichij) && !StringUtil.isEmpty(syuuryounichiji)) {
            if (DateUtil.isValidYYMMDD(kaisinichij.substring(0, 6)) && DateUtil.isValidHHMM(kaisinichij.substring(6, 10))) {
                kaishijikan = DateUtil.convertStringToDate(kaisinichij.substring(0, 6), kaisinichij.substring(6, 10));
            }
            if (DateUtil.isValidYYMMDD(syuuryounichiji.substring(0, 6)) && DateUtil.isValidHHMM(syuuryounichiji.substring(6, 10))) {
                syuuryoujikan = DateUtil.convertStringToDate(syuuryounichiji.substring(0, 6), syuuryounichiji.substring(6, 10));
            }
            if (kaishijikan == null || syuuryoujikan == null) {
                return;
            }
            // 日付の差分分数の数取得処理
            int diffMinutes = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
            FXHDD01 itemjikan = itemJikankikaku.clone();
            itemjikan.setValue(String.valueOf(diffMinutes)); // (終了日時)-(開始日時)の値
            itemjikan.setKikakuChi(jikankikaku); // 乾燥時間規格
            itemjikan.setStandardPattern(itemJikankikaku.getStandardPattern());
            itemjikan.setLabel1(label1);
            itemjikan.setRenderInputText(true);
            kikakuItemList.add(itemjikan);
        }
    }

    /**
     * 項目定義情報取得
     *
     * @param formId 項目定義情報
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    private boolean loadItemSettings() {
        String formId = "GXHDO502B";
        boolean result = false;
        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);

            String sql = "SELECT hdd01.gamen_id, hdd01.item_id, "
                    + "hdd01.joken_kotei_mei,hdd01.joken_komoku_mei,hdd01.joken_kanri_komoku,hdd01.standard_pattern "
                    + "FROM fxhdd01 hdd01 "
                    + "WHERE hdd01.gamen_id = ? AND (hdd01.item_authority IS NULL OR "
                    + DBUtil.getInConditionPreparedStatement("hdd01.item_authority", userGrpList.size()) + ") "
                    + "ORDER BY hdd01.item_no ";

            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("item_id", "itemId");
            mapping.put("joken_kotei_mei", "jokenKoteiMei");
            mapping.put("joken_komoku_mei", "jokenKomokuMei");
            mapping.put("joken_kanri_komoku", "jokenKanriKomoku");
            mapping.put("standard_pattern", "standardPattern");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD01>> beanHandler = new BeanListHandler<>(FXHDD01.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.setItemList(queryRunner.query(sql, beanHandler, params.toArray()));
            if (this.getItemList() != null && !this.itemList.isEmpty()) {
                result = true;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("項目情報取得失敗", ex, LOGGER);
        }
        return result;
    }

    /**
     * 背景色をクリア
     */
    private void clearListDataBgcolor() {
        // 背景色をクリア
        for (int i = 0; i < this.listData.size(); i++) {
            listData.get(i).setHinmeibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setLotbgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setPasskaisuubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteikaisuubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setWiplotnobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteibutubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setNobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKouteibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteisyabgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansouzaranosyuruibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setRutubonobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteijyuuryoukikakubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteijyuuryoubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansouondobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansoujikankikakubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansoukaisinichijbgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansousyuuryounichijibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setDassirogoukibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setDassiondobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setDassijikankikakubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setDassikaisinichijibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setDassisyuuryounichijibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setKansoutantousyabgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setMaesyoriondobgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setMaesyorijikanbgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setMaesyorikaisinichijibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setMaesyorisyuuryounichijibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setMaesyoritantousyabgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteigosoujyuuryoubgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setHihyoumensekisokuteitibgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setSokuteitantousyabgcolor(GXHDO502BConst.NORMAL_COLOR);
            listData.get(i).setBikoubgcolor(GXHDO502BConst.NORMAL_COLOR);
        }
    }

    /**
     * ユーザ認証処理
     */
    public void checkTorokuUserAuth() {

        // ログインユーザーの権限チェックを実施する
        if (!this.userAuthLogin()) {
            // 権限がない場合はユーザー認証画面を表示
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "auth");
        } else {
            try {
                doToroku();
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
        }
    }

    /**
     * 削除件数取得
     *
     * @param paramSekkeino 設計No
     * @return 検索結果件数
     */
    public long selectDelDataCount(String paramSekkeino) {
        long count;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(sekkeino) AS CNT "
                    + "FROM da_mkjoken "
                    + " WHERE ( sekkeino = ?) ";

            // パラメータ設定
            List<Object> params = new ArrayList<>();
            params.addAll(Arrays.asList(paramSekkeino));

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");
        } catch (SQLException ex) {
            count = 0;
            setListData(new ArrayList<>());
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }

    /**
     * エラーチェック： エラーが存在する場合ポップアップ用メッセージをセットする
     *
     * @param errorMessage エラーメッセージ
     * @return エラーが存在する場合true
     */
    private boolean showError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return true;
    }

    /**
     * エラーチェック： エラーが存在する場合ポップアップ用メッセージをセットする
     *
     * @param errorMessage エラーメッセージ
     * @param errorItemList エラーメッセージリスト
     * @param rowIndexInfo 行番号情報
     * @return エラーが存在する場合true
     */
    private boolean addError(String errorMessage, ArrayList<String> errorItemList, String... rowIndexInfo) {
        if (!StringUtil.isEmpty(errorMessage)) {
            if (rowIndexInfo != null && rowIndexInfo.length > 0) {
                errorMessage = rowIndexInfo[0] + errorMessage;
            }
            errorItemList.add(errorMessage);
            return true;
        }
        return false;
    }

    /**
     * ログインユーザー権限チェック
     *
     * @return 権限がある場合true
     */
    private boolean userAuthLogin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String authParam;
        if ("checkTorokuUserAuth".equals(getWarnProcess())) {
            authParam = GXHDO502BConst.USER_AUTH_TOROKU_PARAM;
        } else {
            authParam = GXHDO502BConst.USER_AUTH_DELETE_PARAM;
        }

        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        List<String> loginUserGroup = this.selectFxhbm02(user);

        List<Parameter> paramListLoginUser = parameterEJB.findParameter(user, authParam);
        List<Parameter> paramListLoginUserGroup = new ArrayList<>();
        loginUserGroup.forEach((group) -> {
            paramListLoginUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        });
        return !(paramListLoginUser.isEmpty() && paramListLoginUserGroup.isEmpty());
    }

    /**
     * ユーザーグループマスタ検索
     *
     * @param user 担当者コード
     * @return ユーザーグループ
     */
    private List<String> selectFxhbm02(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT usergroup "
                    + "FROM fxhbm02 WHERE user_name = ? ";

            List<Object> params = new ArrayList<>();
            params.add(user);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List fxhbm02 = queryRunner.query(sql, new MapListHandler(), params.toArray());
            if (null == fxhbm02 || fxhbm02.isEmpty()) {
                return new ArrayList<>();
            }
            return ((List<Map>) fxhbm02).stream()
                    .map(n -> n.get("usergroup").toString())
                    .filter(n -> !StringUtil.isEmpty(n))
                    .collect(Collectors.toList());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
        }
    }

    /**
     * ユーザー認証チェック
     */
    public void userAuth() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String authParam;
        if ("checkTorokuUserAuth".equals(getWarnProcess())) {
            authParam = GXHDO502BConst.USER_AUTH_TOROKU_PARAM;
        } else {
            authParam = GXHDO502BConst.USER_AUTH_DELETE_PARAM;
        }
        String passWord = selectFxhbm01(this.getAuthUser());

        if (StringUtil.isEmpty(passWord)
                || !passWord.toUpperCase().equals(this.getSha256(this.authPassword).toUpperCase())) {
            // ユーザーが存在しない場合またはパスワード不一致の場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }

        List<String> loginUserGroup = this.selectFxhbm02(this.getAuthUser());
        List<Parameter> paramListUser = parameterEJB.findParameter(this.getAuthUser(), authParam);
        List<Parameter> paramListUserGroup = new ArrayList<>();
        loginUserGroup.forEach((group) -> {
            paramListUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        });

        if (paramListUser.isEmpty() && paramListUserGroup.isEmpty()) {
            // 権限がない場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }

        try {
            switch (this.getWarnProcess()) {
                case "checkTorokuUserAuth":
                    doToroku();
                    break;
                case "checkDeleteUserAuth":
                    doDelete();
                    break;
            }
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

    }

    /**
     * ユーザーマスタ検索
     *
     * @param user 担当者コード
     * @return パスワード
     */
    private String selectFxhbm01(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT password "
                    + "FROM fxhbm01 WHERE user_name = ? ";

            List<Object> params = new ArrayList<>();
            params.add(user);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map fxhbm01 = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (null == fxhbm01 || fxhbm01.isEmpty()) {
                return "";
            }
            return StringUtil.nullToBlank(fxhbm01.get("password"));

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return "";
        }
    }

    /**
     * SHA-256変換
     *
     * @param text 文字列
     * @return SHA-256変換後の文字列
     */
    private String getSha256(String text) {
        try {
            if (StringUtil.isEmpty(text)) {
                return "";
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            byte[] cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for (byte b : cipher_byte) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ErrUtil.outputErrorLog("SHA-256変換失敗", ex, LOGGER);
            return "";
        }
    }

    /**
     * da_mkjoken情報取得
     *
     * @param sekkeiNo 設計No
     * @param kouteimeiList 工程名リスト
     */
    private void getMkJokenInfo(String sekkeiNo, ArrayList<String> kouteimeiList) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT kouteimei,koumokumei,kanrikoumokumei,kikakuti "
                    + "  FROM da_mkjoken "
                    + " WHERE sekkeino = ? AND "
                    + DBUtil.getInConditionPreparedStatement("kouteimei", kouteimeiList.size());

            List<Object> params = new ArrayList<>();
            params.add(sekkeiNo);
            params.addAll(kouteimeiList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("kouteimei", "kouteimei");
            mapping.put("koumokumei", "koumokumei");
            mapping.put("kanrikoumokumei", "kanrikoumokumei");
            mapping.put("kikakuti", "kikakuti");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<DaMkJoken>> beanHandler = new BeanListHandler<>(DaMkJoken.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.setListDaMkJoken(queryRunner.query(sql, beanHandler, params.toArray()));

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }

    /**
     * da_mkhyojunjoken情報取得
     *
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param kouteimeiList 工程名リスト
     */
    private void getMkhyojunjoken(String hinmei, String pattern, ArrayList<String> kouteimeiList) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT hinmei,pattern,syurui,kouteimei,koumokumei,kanrikoumokumei, "
                    + "          kikakuti,tyekkupattern,tantousya,tourokunichiji,koushinnichiji "
                    + "     FROM da_mkhyojunjoken "
                    + "    WHERE hinmei = ? "
                    + "      AND pattern = ? AND syurui = ?  AND "
                    + DBUtil.getInConditionPreparedStatement("kouteimei", kouteimeiList.size());

            List<Object> params = new ArrayList<>();
            params.add(hinmei);
            params.add(pattern);
            params.add("比表面積測定記録");
            params.addAll(kouteimeiList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("kouteimei", "kouteimei");
            mapping.put("koumokumei", "koumokumei");
            mapping.put("kanrikoumokumei", "kanrikoumokumei");
            mapping.put("kikakuti", "kikakuti");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<DaMkhyojunjoken>> beanHandler = new BeanListHandler<>(DaMkhyojunjoken.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.setListDaMkhyojunjoken(queryRunner.query(sql, beanHandler, params.toArray()));

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }

    /**
     * 「sr_hihyoumensekisokuteikiroku:比表面積測定記録」から情報取得
     *
     * @param listdata データリスト
     * @return 比表面積測定記録情報
     */
    private List<GXHDO502BModel> getSrHihyoumensekisokuteikirokuListData(List<GXHDO502BModel> listdata) {
        ArrayList<String> hinmeiList = new ArrayList<>();
        ArrayList<String> lotList = new ArrayList<>();
        listdata.forEach((GXHDO502BModel) -> {
            if (!hinmeiList.contains(StringUtil.nullToBlank(GXHDO502BModel.getHinmei()))) {
                hinmeiList.add(StringUtil.nullToBlank(GXHDO502BModel.getHinmei()));
            }
            if (!lotList.contains(StringUtil.nullToBlank(GXHDO502BModel.getLot()))) {
                lotList.add(StringUtil.nullToBlank(GXHDO502BModel.getLot()));
            }
        });
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "select hinmei, lot, passkaisuu, sokuteikaisuu, kosinnichiji "
                    + " FROM sr_hihyoumensekisokuteikiroku "
                    + "WHERE "
                    + DBUtil.getInConditionPreparedStatement("hinmei", hinmeiList.size())
                    + " AND " + DBUtil.getInConditionPreparedStatement("lot", lotList.size())
                    + " ORDER BY kansoukaisinichij ASC";

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("hinmei", "hinmei");                                             // 品名
            mapping.put("lot", "lot");                                                   // ﾛｯﾄ
            mapping.put("passkaisuu", "passkaisuu");                                     // ﾊﾟｽ回数(PASS)
            mapping.put("sokuteikaisuu", "sokuteikaisuu");                               // 測定回数
            mapping.put("kosinnichiji", "kosinnichiji");                                 // 更新日時

            List<Object> params = new ArrayList<>();
            params.addAll(hinmeiList);
            params.addAll(lotList);

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO502BModel>> beanHandler
                    = new BeanListHandler<>(GXHDO502BModel.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

            return queryRunner.query(sql, beanHandler, params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("比表面積測定記録情報取得失敗", ex, LOGGER);
        }
        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="#getter setter">
    /**
     * 一覧表示データ
     *
     * @return the listData
     */
    public List<GXHDO502BModel> getListData() {
        return listData;
    }

    /**
     * 一覧表示データ
     *
     * @param listData the listData to set
     */
    public void setListData(List<GXHDO502BModel> listData) {
        this.listData = listData;
    }

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
     * 警告時処理
     *
     * @return the warnProcess
     */
    public String getWarnProcess() {
        return warnProcess;
    }

    /**
     * 警告時処理
     *
     * @param warnProcess the warnProcess to set
     */
    public void setWarnProcess(String warnProcess) {
        this.warnProcess = warnProcess;
    }

    /**
     * 検索条件：WIPLotNo
     *
     * @return the wipLotNo
     */
    public String getWipLotNo() {
        return wipLotNo;
    }

    /**
     * 検索条件：WIPLotNo
     *
     * @param wipLotNo the wipLotNo to set
     */
    public void setWipLotNo(String wipLotNo) {
        this.wipLotNo = wipLotNo;
    }

    /**
     * 検索条件：品名
     *
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 検索条件：品名
     *
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * 検索条件：乾燥開始日時(from)
     *
     * @return the kansoukaisiDateF
     */
    public String getKansoukaisiDateF() {
        return kansoukaisiDateF;
    }

    /**
     * 検索条件：乾燥開始日時(from)
     *
     * @param kansoukaisiDateF the kansoukaisiDateF to set
     */
    public void setKansoukaisiDateF(String kansoukaisiDateF) {
        this.kansoukaisiDateF = kansoukaisiDateF;
    }

    /**
     * 検索条件：乾燥開始日時(to)
     *
     * @return the kansoukaisiDateT
     */
    public String getKansoukaisiDateT() {
        return kansoukaisiDateT;
    }

    /**
     * 検索条件：乾燥開始日時(to)
     *
     * @param kansoukaisiDateT the kansoukaisiDateT to set
     */
    public void setKansoukaisiDateT(String kansoukaisiDateT) {
        this.kansoukaisiDateT = kansoukaisiDateT;
    }

    /**
     * 検索条件：流れ品
     *
     * @return the nagarehin
     */
    public String getNagarehin() {
        return nagarehin;
    }

    /**
     * 検索条件：流れ品
     *
     * @param nagarehin the nagarehin to set
     */
    public void setNagarehin(String nagarehin) {
        this.nagarehin = nagarehin;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantoshaCd
     */
    public String getTantoshaCd() {
        return tantoshaCd;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantoshaCd the tantoshaCd to set
     */
    public void setTantoshaCd(String tantoshaCd) {
        this.tantoshaCd = tantoshaCd;
    }

    /**
     * 測定物リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbSokuteibutuList
     */
    public List<SelectItem> getCmbSokuteibutuList() {
        List<SelectItem> selectItemList = new ArrayList<>();
        selectItemList.add(new SelectItem("0", "製品"));
        selectItemList.add(new SelectItem("1", "ﾓﾆﾀｰ"));
        return selectItemList;
    }

    /**
     * 測定回数リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbSokuteikaisuuList
     */
    public List<SelectItem> getCmbSokuteikaisuuList() {
        List<SelectItem> selectItemList = new ArrayList<>();
        selectItemList.add(new SelectItem("0", "1回目"));
        selectItemList.add(new SelectItem("1", "再測定1回目"));
        selectItemList.add(new SelectItem("2", "再測定2回目"));
        selectItemList.add(new SelectItem("3", "再測定3回目"));
        selectItemList.add(new SelectItem("4", "再測定4回目"));
        return selectItemList;
    }

    /**
     * 工程リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbKouteiList
     */
    public List<SelectItem> getCmbKouteiList() {
        List<SelectItem> selectItemList = new ArrayList<>();
        selectItemList.add(new SelectItem("0", "ｶﾞﾗｽ"));
        selectItemList.add(new SelectItem("1", "添加材"));
        selectItemList.add(new SelectItem("2", "誘電体"));
        return selectItemList;
    }

    /**
     * 乾燥皿の種類リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbKansouzaranosyuruiList
     */
    public List<SelectItem> getCmbKansouzaranosyuruiList() {
        List<SelectItem> selectItemList = new ArrayList<>();
        selectItemList.add(new SelectItem("0", "ﾙﾂﾎﾞ"));
        selectItemList.add(new SelectItem("1", "ｱﾙﾐ皿"));
        return selectItemList;
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
            ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
        }
    }

    /**
     * Excelダウンロード
     *
     * @throws Throwable
     */
    public void downloadExcel() throws Throwable {
        File excel = null;
        String excelRealPath = JSON_FILE_PATH_502B;
        String excelFileHeadName = GAMEN_NAME_502B;
        List<GXHDO502BModel> outputList = new ArrayList<>();
        for (GXHDO502BModel gxhdo502bmodel : listData) {
            outputList.add(gxhdo502bmodel.clone());
        }
        outputList.stream().map((gxhdo502bmodel) -> {
            // 測定物のコンボボックス(製品,ﾓﾆﾀｰ)テキスト値取得
            gxhdo502bmodel.setSokuteibutu(getComboSokuteibutuText(StringUtil.nullToBlank(gxhdo502bmodel.getSokuteibutu())));
            return gxhdo502bmodel;
        }).map((gxhdo502bmodel) -> {
            // 測定回数のコンボボックス("1回目","再測定1回目","再測定2回目","再測定3回目","再測定4回目")テキスト値取得
            gxhdo502bmodel.setSokuteikaisuu(getComboSokuteikaisuuText(StringUtil.nullToBlank(gxhdo502bmodel.getSokuteikaisuu())));
            return gxhdo502bmodel;
        }).map((gxhdo502bmodel) -> {
            // 工程のコンボボックス("ｶﾞﾗｽ","添加材","誘電体")テキスト値取得
            gxhdo502bmodel.setKoutei(getComboKouteiText(StringUtil.nullToBlank(gxhdo502bmodel.getKoutei())));
            return gxhdo502bmodel;
        }).forEachOrdered((gxhdo502bmodel) -> {
            // 測定回数のコンボボックス("1回目","再測定1回目","再測定2回目","再測定3回目","再測定4回目")テキスト値取得
            gxhdo502bmodel.setKansouzaranosyurui(getComboKansouzaranosyuruiText(StringUtil.nullToBlank(gxhdo502bmodel.getKansouzaranosyurui())));
        });
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
     * 測定物のコンボボックス(製品,ﾓﾆﾀｰ)テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboSokuteibutuText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "製品";
            case "1":
                return "ﾓﾆﾀｰ";
            default:
                return "";
        }
    }

    /**
     * 測定回数のコンボボックス("1回目","再測定1回目","再測定2回目","再測定3回目","再測定4回目")テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboSokuteikaisuuText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "1回目";
            case "1":
                return "再測定1回目";
            case "2":
                return "再測定2回目";
            case "3":
                return "再測定3回目";
            case "4":
                return "再測定4回目";
            default:
                return "";
        }
    }

    /**
     * 工程のコンボボックス("ｶﾞﾗｽ","添加材","誘電体")テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboKouteiText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "ｶﾞﾗｽ";
            case "1":
                return "添加材";
            case "2":
                return "誘電体";
            default:
                return "";
        }
    }

    /**
     * 乾燥皿の種類のコンボボックス("ﾙﾂﾎﾞ","ｱﾙﾐ皿")テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboKansouzaranosyuruiText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "ﾙﾂﾎﾞ";
            case "1":
                return "ｱﾙﾐ皿";
            default:
                return "";
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
     * 件数0の場合「戻る」のみが表示された画面を表示。
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * 件数0の場合「戻る」のみが表示された画面を表示。
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    /**
     * WIPLotNo
     *
     * @return the paramWIPLotNo
     */
    public String getParamWIPLotNo() {
        return paramWIPLotNo;
    }

    /**
     * WIPLotNo
     *
     * @param paramWIPLotNo the paramWIPLotNo to set
     */
    public void setParamWIPLotNo(String paramWIPLotNo) {
        this.paramWIPLotNo = paramWIPLotNo;
    }

    /**
     * 品名
     *
     * @return the paramHinmei
     */
    public String getParamHinmei() {
        return paramHinmei;
    }

    /**
     * 品名
     *
     * @param paramHinmei the paramHinmei to set
     */
    public void setParamHinmei(String paramHinmei) {
        this.paramHinmei = paramHinmei;
    }

    /**
     * ユーザー認証：ユーザー
     *
     * @return the authUser
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * ユーザー認証：ユーザー
     *
     * @param authUser the authUser to set
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * ユーザー認証：パスワード
     *
     * @return the authPassword
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * ユーザー認証：パスワード
     *
     * @param authPassword the authPassword to set
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * Excel保存ボタン使用可、使用不可
     *
     * @return the btnExceldisabled
     */
    public String getBtnExceldisabled() {
        return btnExceldisabled;
    }

    /**
     * Excel保存ボタン使用可、使用不可
     *
     * @param btnExceldisabled the btnExceldisabled to set
     */
    public void setBtnExceldisabled(String btnExceldisabled) {
        this.btnExceldisabled = btnExceldisabled;
    }

    /**
     * 削除使用可、使用不可
     *
     * @return the btnDeletedisabled
     */
    public String getBtnDeletedisabled() {
        return btnDeletedisabled;
    }

    /**
     * 削除使用可、使用不可
     *
     * @param btnDeletedisabled the btnDeletedisabled to set
     */
    public void setBtnDeletedisabled(String btnDeletedisabled) {
        this.btnDeletedisabled = btnDeletedisabled;
    }

    /**
     * 項目データ
     *
     * @return the itemList
     */
    public List<FXHDD01> getItemList() {
        return itemList;
    }

    /**
     * 項目データ
     *
     * @param itemList the itemList to set
     */
    public void setItemList(List<FXHDD01> itemList) {
        this.itemList = itemList;
    }

    /**
     * DaMkJokenリスト
     *
     * @return the listDaMkJoken
     */
    public List<DaMkJoken> getListDaMkJoken() {
        return listDaMkJoken;
    }

    /**
     * DaMkJokenリスト
     *
     * @param listDaMkJoken the listDaMkJoken to set
     */
    public void setListDaMkJoken(List<DaMkJoken> listDaMkJoken) {
        this.listDaMkJoken = listDaMkJoken;
    }

    /**
     * DaMkhyojunjokenリスト
     *
     * @return the listDaMkhyojunjoken
     */
    public List<DaMkhyojunjoken> getListDaMkhyojunjoken() {
        return listDaMkhyojunjoken;
    }

    /**
     * DaMkhyojunjokenリスト
     *
     * @param listDaMkhyojunjoken the listDaMkhyojunjoken to set
     */
    public void setListDaMkhyojunjoken(List<DaMkhyojunjoken> listDaMkhyojunjoken) {
        this.listDaMkhyojunjoken = listDaMkhyojunjoken;
    }
    //</editor-fold>

}
