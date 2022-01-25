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
import jp.co.kccs.xhd.common.ResultMessage;
import jp.co.kccs.xhd.db.ParameterEJB;
import jp.co.kccs.xhd.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.model.DaMkJoken;
import jp.co.kccs.xhd.db.model.DaMkhyojunjoken;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.model.GXHDO502AModel;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2022/01/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO502A(粒度記録機能)
 *
 * @author KCSS K.Jo
 * @since 2022/01/10
 */
@Named
@ViewScoped
public class GXHDO502A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO502A.class.getName());
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
    private List<GXHDO502AModel> listData = null;
    /**
     * 警告メッセージ
     */
    private String warnMessage = "";
    /**
     * 警告時処理
     */
    private String warnProcess = "";
    /**
     * 一覧表示最大件数
     */
    private int listCountMax = -1;
    /**
     * 一覧表示警告件数
     */
    private int listCountWarn = -1;
    /**
     * 検索条件：WIPLotNo
     */
    private String lotNo = "";
    /**
     * 検索条件：品名
     */
    private String hinmei = "";
    /**
     * 検索条件：流れ品
     */
    private String nagarehin = "";
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantoshaCd = "";
    /**
     * 一覧工程リスト:表示可能ﾃﾞｰﾀ
     */
    private String cmbKouteiData[];
    /**
     * 一覧測定回数リスト:表示可能ﾃﾞｰﾀ
     */
    private String cmbSokuteikaisuuData[];
    /**
     * * 件数0の場合「戻る」のみが表示された画面を表示。
     */
    private String displayStyle = "";
    /**
     * LotNo
     */
    private String paramLotno = "";
    /**
     *
     * 項目データ
     */
    private List<FXHDD01> itemList;
    /**
     * ユーザー認証：ユーザー
     */
    private String authUser;
    /**
     * ユーザー認証：パスワード
     */
    private String authPassword;
    /**
     * DaMkJokenリスト
     */
    private List<DaMkJoken> listDaMkJoken;
    /**
     * DaMkhyojunjokenリスト
     */
    private List<DaMkhyojunjoken> listDaMkhyojunjoken;
    //エクセル出力ファイルパス 粒度記録
    private static final String JSON_FILE_PATH_502A = "/WEB-INF/classes/resources/json/gxhdo502a.json";
    //画面名称 502A
    private static final String GAMEN_NAME_502A = "粒度記録";
    /**
     * /**
     * * ユーザー認証パラメータ(修正)
     */
    private static String USER_AUTH_TOROKU_PARAM = "sr_ryuudokiroku_update_button";
    /**
     * * ユーザー認証パラメータ(削除)
     */
    private static String USER_AUTH_DELETE_PARAM = "sr_ryuudokiroku_delete_button";
    /**
     *
     * Excel保存ボタン使用可、使用不可
     */
    private String btnExceldisabled;
    /**
     * 削除使用可、使用不可
     */
    private String btnDeletedisabled;

    /**
     * コンストラクタ
     */
    public GXHDO502A() {
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
        nagarehin = "流れ品";
        // ﾁｪｯｸﾊﾟﾀｰﾝコンボボックス設定
        setCmbKouteiData(new String[]{"ｶﾞﾗｽ", "誘電体"});
        setCmbSokuteikaisuuData(new String[]{"1回目", "再測定1回目", "再測定2回目", "再測定3回目", "再測定4回目"});
        // 一覧表示最大件数、警告件数を取得処理
        getListCountMaxAndWarnKensu();
        // 規格情報ﾁｪｯｸ処理用規格情報を取得
        if (itemList == null || itemList.isEmpty()) {
            this.loadItemSettings();
        }
        // 画面クリア
        clear();
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
                    + "WHERE menu_group_id = 'qcdb_mainMenu' AND gamen_id = 'GXHDO502A' AND "
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
     * 一覧表示データ
     *
     */
    public void initDataRowCount() {
        // 一覧表示部に8行(空白行)を表示する
        if (listData != null) {
            listData.clear();
        } else {
            listData = new ArrayList<>();
        }
        for (int i = 1; i <= 8; i++) {
            listData.add(createGXHDO502AModel(i));
        }
    }

    /**
     * 空白行を作成する
     *
     * @return 空白ﾃﾞｰﾀ
     */
    private GXHDO502AModel createGXHDO502AModel(int rowIndx) {
        GXHDO502AModel gxhdo502amodel = new GXHDO502AModel();
        gxhdo502amodel.setRowIndx(rowIndx);
        // 削除ﾁｪｯｸﾎﾞｯｸｽ非表示
        gxhdo502amodel.setChkboxrender("false");
        // 品名使用可
        gxhdo502amodel.setHinmeidisabled("false");
        // ﾛｯﾄ使用可
        gxhdo502amodel.setLotdisabled("false");
        // ﾊﾟｽ回数(PASS)使用可
        gxhdo502amodel.setPasskaisuudisabled("false");
        // 測定回数使用可
        gxhdo502amodel.setSokuteikaisuudisabled("false");
        // 純水量使用可
        gxhdo502amodel.setJyunsuiryoudisabled("false");
        // ｻﾝﾌﾟﾙ使用可
        gxhdo502amodel.setSampleryoudisabled("false");
        // 前超音波時間使用可
        gxhdo502amodel.setMaetyouonpajikandisabled("false");
        // 透過率規格使用可
        gxhdo502amodel.setToukaritukikakudisabled("false");
        // D50粒度規格使用可
        gxhdo502amodel.setD50ryuudokikakudisabled("false");
        // D50狙い値使用可
        gxhdo502amodel.setD50neraitidisabled("false");
        // D90粒度規格使用可
        gxhdo502amodel.setD90ryuudokikakudisabled("false");
        // 状態Flag: 0:登録
        gxhdo502amodel.setJyoutaiFlg("0");
        return gxhdo502amodel;
    }

    /**
     * 追加処理
     */
    public void addDataRow() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "addRow");
        // 一覧表示件数を取得
        int count = listData.size();
        if (listCountMax > 0 && count + 1 > listCountMax) {
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
        listData.add(createGXHDO502AModel(count + 1));
    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "search");
        // WIPLotNo
        if (!StringUtil.isEmpty(getLotNo()) && StringUtil.getLength(getLotNo()) != 15) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000004", "WIPLotNo", "15"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
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

            warnMessage = String.format("検索結果が%d件を超えています。<br/>継続しますか?<br/>%d件", listCountWarn, count);
            this.warnProcess = "OK";
            return;
        }

        // 入力チェックでエラーが存在しない場合検索処理を実行する
        selectListData();
    }

    /**
     * 一覧表示データ検索
     *
     * @return 検索結果件数
     */
    private void selectListData() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT hinmei,lot,passkaisuu,"
                    + "(CASE WHEN sokuteikaisuu = 0 "
                    + "      THEN '1回目'"
                    + "    WHEN sokuteikaisuu = 1"
                    + "      THEN '再測定1回目'"
                    + "    WHEN sokuteikaisuu = 2"
                    + "      THEN '再測定2回目'"
                    + "    WHEN sokuteikaisuu = 3"
                    + "      THEN '再測定3回目'"
                    + "    WHEN sokuteikaisuu = 4"
                    + "      THEN '再測定4回目'"
                    + "    ELSE ''"
                    + "    END) AS sokuteikaisuu,"
                    + "nagarehin,CONCAT(kojyo , lotno , edaban) AS wiplotno,No,"
                    + "(CASE WHEN koutei = 0 THEN 'ｶﾞﾗｽ' WHEN koutei = 1 THEN '誘電体' ELSE '' END) AS koutei,"
                    + "sokuteisya,jyunsuiryou,sampleryou,maetyouonpajikan,sokuteigoki,toukaritukikaku,"
                    + "D50ryuudokikaku,D50neraiti,D50ryuudosokuteiti,D90ryuudokikaku,D90ryuudosokuteiti,toukaritu,bikou, kosinnichiji "
                    + " FROM sr_ryuudokiroku WHERE (? IS NULL OR kojyo = ?) "
                    + " AND (? IS NULL OR lotno = ?) "
                    + " AND (? IS NULL OR edaban = ?) "
                    + " AND (? IS NULL OR nagarehin = ?) "
                    + " AND (? IS NULL OR hinmei LIKE ? ESCAPE '\\\\') "
                    + " ORDER BY hinmei ASC";
            // パラメータ設定
            List<Object> params = createSearchParam();
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("hinmei", "hinmei");                                       // 品名                       		
            mapping.put("lot", "lot");                                             // ﾛｯﾄ                       		
            mapping.put("passkaisuu", "passkaisuu");                               // ﾊﾟｽ回数(PASS)                       		
            mapping.put("sokuteikaisuu", "sokuteikaisuu");                         // 測定回数                       		
            mapping.put("nagarehin", "nagarehin");                                 // 流れ品                       		
            mapping.put("wiplotno", "wiplotno");                                   // WIPLotNo		                  		
            mapping.put("no", "no");                                               // No                       		
            mapping.put("koutei", "koutei");                                       // 工程                       		
            mapping.put("sokuteisya", "sokuteisya");                               // 測定者                       		
            mapping.put("jyunsuiryou", "jyunsuiryou");                             // 純水量                       		
            mapping.put("sampleryou", "sampleryou");                               // ｻﾝﾌﾟﾙ量                       		
            mapping.put("maetyouonpajikan", "maetyouonpajikan");                   // 前超音波時間                       		
            mapping.put("sokuteigoki", "sokuteigoki");                             // 測定号機(号機)                       		
            mapping.put("toukaritukikaku", "toukaritukikaku");                     // 透過率規格                       		
            mapping.put("d50ryuudokikaku", "d50ryuudokikaku");                     // D50粒度規格                       		
            mapping.put("d50neraiti", "d50neraiti");                               // D50狙い値                       		
            mapping.put("d50ryuudosokuteiti", "d50ryuudosokuteiti");               // D50粒度測定値(μm)                       		
            mapping.put("d90ryuudokikaku", "d90ryuudokikaku");                     // D90粒度規格                       		
            mapping.put("d90ryuudosokuteiti", "d90ryuudosokuteiti");               // D90粒度測定値(μm)                        		
            mapping.put("toukaritu", "toukaritu");                                 // 透過率(%)                       		
            mapping.put("bikou", "bikou");                                         // 備考
            mapping.put("kosinnichiji", "kosinnichiji");                           // 更新日時
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO502AModel>> beanHandler
                    = new BeanListHandler<>(GXHDO502AModel.class, rowProcessor);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
            for (int i = 0; i < listData.size(); i++) {
                GXHDO502AModel gxhdo502amodel = listData.get(i);
                // 削除ﾁｪｯｸﾎﾞｯｸｽ表示
                gxhdo502amodel.setChkboxrender("true");
                // 品名使用不可
                gxhdo502amodel.setHinmeidisabled("true");
                // ﾛｯﾄ使用不可
                gxhdo502amodel.setLotdisabled("true");
                // ﾊﾟｽ回数(PASS)使用不可
                gxhdo502amodel.setPasskaisuudisabled("true");
                // 測定回数使用不可
                gxhdo502amodel.setSokuteikaisuudisabled("true");
                // 純水量使用不可
                gxhdo502amodel.setJyunsuiryoudisabled("true");
                // ｻﾝﾌﾟﾙ使用不可
                gxhdo502amodel.setSampleryoudisabled("true");
                // 前超音波時間使用不可
                gxhdo502amodel.setMaetyouonpajikandisabled("true");
                // 透過率規格使用不可
                gxhdo502amodel.setToukaritukikakudisabled("true");
                // D50粒度規格使用不可
                gxhdo502amodel.setD50ryuudokikakudisabled("true");
                // D50狙い値使用不可
                gxhdo502amodel.setD50neraitidisabled("true");
                // D90粒度規格使用不可
                gxhdo502amodel.setD90ryuudokikakudisabled("true");
                // 状態Flag: 1:更新
                gxhdo502amodel.setJyoutaiFlg("1");
                gxhdo502amodel.setRowIndx(i + 1);
            }
            if (listData.size() < 8) {
                for (int i = listData.size() + 1; i <= 8; i++) {
                    listData.add(createGXHDO502AModel(i));
                }
            }
            // 項目制御:Excel保存ﾎﾞﾀﾝ使用可
            setBtnExceldisabled("false");
            // 項目制御:削除ﾎﾞﾀﾝ使用可
            setBtnDeletedisabled("false");
        } catch (SQLException ex) {
            initDataRowCount();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 一覧表示データ件数取得
     *
     * @return 検索結果件数
     */
    private long selectListDataCount() {
        long count;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(1) AS CNT"
                    + " FROM sr_ryuudokiroku WHERE (? IS NULL OR kojyo = ?) "
                    + " AND (? IS NULL OR lotno = ?) "
                    + " AND (? IS NULL OR edaban = ?) "
                    + " AND (? IS NULL OR nagarehin = ?) "
                    + " AND (? IS NULL OR hinmei LIKE ? ESCAPE '\\\\') ";
            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");
        } catch (SQLException ex) {
            count = 8;
            initDataRowCount();
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
        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        String paramNagarehin = null;
        String paramHinmei = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 12);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 12, 15));
        }
        if (!StringUtil.isEmpty(nagarehin)) {
            //流れ品
            paramNagarehin = StringUtil.blankToNull(getNagarehin());
        }
        if (!StringUtil.isEmpty(hinmei)) {
            //品名
            paramHinmei = "%" + DBUtil.escapeString(StringUtil.blankToNull(getHinmei())) + "%";// 品名
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(getRadioDbValue(paramNagarehin, 0), getRadioDbValue(paramNagarehin, 0)));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));

        return params;
    }

    /**
     * 画面クリア
     */
    public void clear() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "clear");
        lotNo = "";
        hinmei = "";
        nagarehin = "流れ品";
        setTantoshaCd("");
        // 一覧表示データ
        listData = new ArrayList<>();
        // 項目制御:Excel保存ﾎﾞﾀﾝ使用不可
        setBtnExceldisabled("true");
        // 項目制御:削除ﾎﾞﾀﾝ使用不可
        setBtnDeletedisabled("true");
        // 一覧表示部の値を画面初期のように設定：一覧表示部に8行(空白行)を表示する。
        initDataRowCount();
    }

    /**
     * 警告OK選択時処理
     *
     * @throws java.sql.SQLException
     */
    public void processWarnOkClick() throws SQLException {
        switch (this.warnProcess) {
            case "clear":
                clear();
                break;
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
        String excelRealPath = JSON_FILE_PATH_502A;
        String excelFileHeadName = GAMEN_NAME_502A;
        List outputList = listData;

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
     * 登録処理確認
     */
    public void confirmToroku() {
        // 背景色をクリア
        clearListDataBgcolor();
        // 入力値があるデータを取得
        List<GXHDO502AModel> hasValueListdata = getHasValueListdata();
        // PKが入力しているデータを取得
        List<GXHDO502AModel> hasKeyValueListdata = hasValueListdata.stream().filter(
                o -> !StringUtil.isEmpty(o.getHinmei()) && !StringUtil.isEmpty(o.getLot()) && o.getPasskaisuu() != null && !StringUtil.isEmpty(o.getSokuteikaisuu())).collect(Collectors.toList());
        // 「sr_ryuudokiroku:粒度記録」から情報取得
        List<GXHDO502AModel> srHihyoumensekisokuteikirokuListData = getSrRyuudokirokuListData(hasKeyValueListdata);
        // 状態が 0:登録 のﾃﾞｰﾀを取得する
        List<GXHDO502AModel> insertListData = getInsertListData(hasKeyValueListdata);
        // 状態が 1:更新 のﾃﾞｰﾀを取得する
        List<GXHDO502AModel> updateListData = getUpdateListData(hasKeyValueListdata);

        boolean errorFlg = false;
        // 登録の場合、キーのデータがすでにある場合、排他エラーとする。
        for (GXHDO502AModel gxhdo502amodel : insertListData) {
            List<GXHDO502AModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502amodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502amodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502amodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && getCmbSokuteikaisuuDbValue(gxhdo502amodel.getSokuteikaisuu()).equals(((Integer) Integer.parseInt(o.getSokuteikaisuu())))
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
        for (GXHDO502AModel gxhdo502amodel : updateListData) {
            List<GXHDO502AModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502amodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502amodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502amodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && getCmbSokuteikaisuuDbValue(gxhdo502amodel.getSokuteikaisuu()).equals(((Integer) Integer.parseInt(o.getSokuteikaisuu())))
            ).collect(Collectors.toList());
            if (collect.isEmpty()) {
                errorFlg = true;
                break;
            }
            GXHDO502AModel gxhdo502amodelTableData = collect.get(0);
            Timestamp gamenKosinnichiji = (Timestamp) gxhdo502amodel.getKosinnichiji();
            Timestamp tableKosinnichiji = (Timestamp) gxhdo502amodelTableData.getKosinnichiji();
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
        hasValueListdata.forEach((gxhdo502amodel) -> {
            // 【登録】ﾎﾞﾀﾝ押下時、値がある行のチェック処理
            checkGxhdo502amodelValue(gxhdo502amodel, errorItemList);
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
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param hasValueListdata リストデータ
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList, List<GXHDO502AModel> hasValueListdata) throws CloneNotSupportedException {
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> kikakuItemList = new ArrayList<>();
        for (GXHDO502AModel gxhdo502amodel : hasValueListdata) {
            // 規格情報ﾁｪｯｸ処理:D50粒度測定値が入力されている場合、D50粒度測定値ﾁｪｯｸ、D50粒度測定値の値が、D50粒度規格の範囲内であること。
            // D50粒度測定値(μm)
            BigDecimal d50ryuudosokuteiti = gxhdo502amodel.getD50ryuudosokuteiti();
            // D50粒度規格
            String d50ryuudokikaku = gxhdo502amodel.getD50ryuudokikaku();
            // D90粒度測定値(μm)
            BigDecimal d90ryuudosokuteiti = gxhdo502amodel.getD90ryuudosokuteiti();
            // D90粒度規格
            String d90ryuudokikaku = gxhdo502amodel.getD90ryuudokikaku();
            // 透過率(%)
            BigDecimal toukaritu = gxhdo502amodel.getToukaritu();
            // 透過率規格
            String toukaritukikaku = gxhdo502amodel.getToukaritukikaku();
            FXHDD01 itemD50ryuudokikaku = getItemRow(GXHDO502AConst.D50RYUUDOKIKAKU);
            FXHDD01 itemD90ryuudokikaku = getItemRow(GXHDO502AConst.D90RYUUDOKIKAKU);
            FXHDD01 itemToukaritukikaku = getItemRow(GXHDO502AConst.TOUKARITUKIKAKU);
            if (itemD50ryuudokikaku != null && d50ryuudosokuteiti != null) {
                FXHDD01 itemmD50ryuudo = itemD50ryuudokikaku.clone();
                itemmD50ryuudo.setValue(d50ryuudosokuteiti.toPlainString()); // D50粒度測定値(μm)
                itemmD50ryuudo.setKikakuChi(d50ryuudokikaku); // D50粒度規格
                itemmD50ryuudo.setStandardPattern(itemD50ryuudokikaku.getStandardPattern());
                itemmD50ryuudo.setLabel1(gxhdo502amodel.getRowIndx() + "行目: D50粒度測定値");
                itemmD50ryuudo.setRenderInputText(true);
                kikakuItemList.add(itemmD50ryuudo);
            }
            if (itemD90ryuudokikaku != null && d90ryuudosokuteiti != null) {
                FXHDD01 itemD90ryuudo = itemD90ryuudokikaku.clone();
                itemD90ryuudo.setValue(d90ryuudosokuteiti.toPlainString()); // D90粒度測定値(μm)
                itemD90ryuudo.setKikakuChi(d90ryuudokikaku); // D50粒度規格
                itemD90ryuudo.setStandardPattern(itemD90ryuudokikaku.getStandardPattern());
                itemD90ryuudo.setLabel1(gxhdo502amodel.getRowIndx() + "行目: D90粒度測定値");
                itemD90ryuudo.setRenderInputText(true);
                kikakuItemList.add(itemD90ryuudo);
            }
            if (itemToukaritukikaku != null && toukaritu != null) {
                FXHDD01 itemToukaritu = itemToukaritukikaku.clone();
                itemToukaritu.setValue(toukaritu.toPlainString()); //透過率
                itemToukaritu.setKikakuChi(toukaritukikaku); // 透過率規格
                itemToukaritu.setStandardPattern(itemToukaritukikaku.getStandardPattern());
                itemToukaritu.setLabel1(gxhdo502amodel.getRowIndx() + "行目: 透過率");
                itemToukaritu.setRenderInputText(true);
                kikakuItemList.add(itemToukaritu);
            }
        }

        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(kikakuItemList, kikakuchiInputErrorInfoList);
        // エラー項目の背景色を設定
        if (errorMessageInfo == null) {
            kikakuchiInputErrorInfoList.forEach((kikakuchiinputerrorinfo) -> {
                String itemId = kikakuchiinputerrorinfo.getItemId();
                int rowIndex = Integer.parseInt(kikakuchiinputerrorinfo.getItemLabel().substring(0, kikakuchiinputerrorinfo.getItemLabel().indexOf("行目")));
                GXHDO502AModel gxhdo502amodelItem = hasValueListdata.stream().filter(gxhdo502amodel -> gxhdo502amodel.getRowIndx() == rowIndex).findFirst().orElse(null);
                setErrorMessageInfoKikakuchiBgcolor(itemId, gxhdo502amodelItem);
            });
        } else {
            errorMessageInfo.setPageChangeItemIndex(-1);
            int rowIndex = Integer.parseInt(errorMessageInfo.getErrorMessage().substring(0, errorMessageInfo.getErrorMessage().indexOf("行目")));
            GXHDO502AModel gxhdo502amodelItem = hasValueListdata.stream().filter(gxhdo502amodel -> gxhdo502amodel.getRowIndx() == rowIndex).findFirst().orElse(null);
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            setErrorMessageInfoKikakuchiBgcolor(itemId, gxhdo502amodelItem);
        }
        return errorMessageInfo;
    }

    /**
     * 規格チェックのエラー項目の背景色を設定
     *
     * @param itemId 項目ID
     * @param gxhdo502amodelItem データ
     * @return 項目データ
     */
    private void setErrorMessageInfoKikakuchiBgcolor(String itemId, GXHDO502AModel gxhdo502amodel) {
        if (gxhdo502amodel == null) {
            return;
        }
        switch (itemId) {
            // D50粒度規格
            case GXHDO502AConst.D50RYUUDOKIKAKU:
                gxhdo502amodel.setD50ryuudosokuteitibgcolor(GXHDO502AConst.ERROR_COLOR);
                break;
            // D90粒度規格
            case GXHDO502AConst.D90RYUUDOKIKAKU:
                gxhdo502amodel.setD90ryuudosokuteitibgcolor(GXHDO502AConst.ERROR_COLOR);
                break;
            // 透過率規格
            case GXHDO502AConst.TOUKARITUKIKAKU:
                gxhdo502amodel.setToukaritubgcolor(GXHDO502AConst.ERROR_COLOR);
                break;
        }
    }

    /**
     * 規格外エラーダイアログOK押下時
     */
    public void processKikakuWarnOk() {
        // ﾒｯｾｰｼﾞの表示を行う:登録します。よろしいですか？
        showTorokuKakuninDialog();
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
     * ﾒｯｾｰｼﾞの表示を行う:登録します。よろしいですか？(OK/Cancel)
     */
    public void showTorokuKakuninDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "warning");
        this.setWarnMessage("登録します。よろしいですか？");
        this.setWarnProcess("checkTorokuUserAuth");
    }

    /**
     * 項目定義情報取得
     *
     * @param formId 項目定義情報
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    private boolean loadItemSettings() {
        String formId = "GXHDO502A";
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
     * 【登録】ﾎﾞﾀﾝ押下時のチェック処理
     *
     * @param gxhdo502amodel モデルデータ
     * @param errorItemList チェックエラーリストデータ
     */
    private void checkGxhdo502amodelValue(GXHDO502AModel gxhdo502amodel, ArrayList<String> errorItemList) {
        // 行番号情報
        String rowIndexInfo = gxhdo502amodel.getRowIndx() + "行目: ";
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();

        // 品名入力チェック
        if (addError(validateUtil.checkC001(gxhdo502amodel.getHinmei(), "品名"), errorItemList, rowIndexInfo)) {
            gxhdo502amodel.setHinmeibgcolor(GXHDO502AConst.ERROR_COLOR);
        }
        // ﾛｯﾄ入力チェック
        if (addError(validateUtil.checkC001(gxhdo502amodel.getLot(), "ﾛｯﾄ"), errorItemList, rowIndexInfo)) {
            gxhdo502amodel.setLotbgcolor(GXHDO502AConst.ERROR_COLOR);
        }
        // ﾊﾟｽ回数(PASS)入力チェック
        if (addError(validateUtil.checkC001(StringUtil.nullToBlank(gxhdo502amodel.getPasskaisuu()), "ﾊﾟｽ回数(PASS)"), errorItemList, rowIndexInfo)) {
            gxhdo502amodel.setPasskaisuubgcolor(GXHDO502AConst.ERROR_COLOR);
        }
        // WIPLotNo桁数チェック
        if (addError(validateUtil.checkC101(gxhdo502amodel.getWiplotno(), "WIPLotNo", 15), errorItemList, rowIndexInfo)) {
            gxhdo502amodel.setWiplotnobgcolor(GXHDO502AConst.ERROR_COLOR);
        }
        // 測定回数
        if (addError(validateUtil.checkC001(StringUtil.nullToBlank(gxhdo502amodel.getSokuteikaisuu()), "測定回数"), errorItemList, rowIndexInfo)) {
            gxhdo502amodel.setSokuteikaisuubgcolor(GXHDO502AConst.ERROR_COLOR);
        }
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
     * 状態が 0:登録のデータを取得
     *
     * @param hasValueListdata 入力値があるデータリスト
     * @return 状態が 0:登録のデータリスト
     */
    public List<GXHDO502AModel> getInsertListData(List<GXHDO502AModel> hasValueListdata) {
        // 状態が 0:登録 の行を取得する
        List<GXHDO502AModel> insertListData = hasValueListdata.stream().filter(o -> "0".equals(o.getJyoutaiFlg())).collect(Collectors.toList());
        insertListData.sort(Comparator.comparing(GXHDO502AModel::getRowIndx));
        return insertListData;
    }

    /**
     * 状態が 1:更新のデータを取得
     *
     * @param hasValueListdata 入力値があるデータリスト
     * @return 状態が 1:更新のデータリスト
     */
    public List<GXHDO502AModel> getUpdateListData(List<GXHDO502AModel> hasValueListdata) {
        // 状態が 1:更新 の行を取得する
        List<GXHDO502AModel> updateListData = hasValueListdata.stream().filter(o -> "1".equals(o.getJyoutaiFlg())).collect(Collectors.toList());
        updateListData.sort(Comparator.comparing(GXHDO502AModel::getRowIndx));
        return updateListData;
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
     * 背景色をクリア
     */
    private void clearListDataBgcolor() {

        //背景色をクリア
        for (int i = 0; i < this.listData.size(); i++) {
            listData.get(i).setHinmeibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setLotbgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setPasskaisuubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setWiplotnobgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setSokuteikaisuubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setNobgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setKouteibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setSokuteisyabgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setJyunsuiryoubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setSampleryoubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setMaetyouonpajikanbgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setSokuteigokibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setToukaritukikakubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setD50ryuudokikakubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setD50neraitibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setD50ryuudosokuteitibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setD90ryuudokikakubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setD90ryuudosokuteitibgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setToukaritubgcolor(GXHDO502AConst.NORMAL_COLOR);
            listData.get(i).setBikoubgcolor(GXHDO502AConst.NORMAL_COLOR);
        }

    }

    /**
     * 入力値があるデータを取得
     *
     * @return 入力値があるデータリスト
     */
    public List<GXHDO502AModel> getHasValueListdata() {
        if (getListData() == null || getListData().isEmpty()) {
            return new ArrayList<>();
        }
        return getListData().stream().filter(o -> !(StringUtil.isEmpty(o.getHinmei()) && StringUtil.isEmpty(o.getLot()) && o.getPasskaisuu() == null
                && StringUtil.isEmpty(o.getWiplotno()) && StringUtil.isEmpty(o.getSokuteikaisuu()) && o.getNo() == null && StringUtil.isEmpty(o.getKoutei()) && StringUtil.isEmpty(o.getSokuteisya())
                && StringUtil.isEmpty(o.getJyunsuiryou()) && StringUtil.isEmpty(o.getSampleryou()) && StringUtil.isEmpty(o.getMaetyouonpajikan()) && o.getSokuteigoki() == null
                && StringUtil.isEmpty(o.getToukaritukikaku()) && o.getD50ryuudosokuteiti() == null && StringUtil.isEmpty(o.getD50ryuudokikaku()) && StringUtil.isEmpty(o.getD50neraiti())
                && StringUtil.isEmpty(o.getD90ryuudokikaku()) && o.getD90ryuudosokuteiti() == null && o.getToukaritu() == null && StringUtil.isEmpty(o.getBikou()))
        ).collect(Collectors.toList());
    }

    /**
     * 粒度記録(sr_ryuudokiroku)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param insertListData 状態が 0:登録 のﾃﾞｰﾀ
     * @throws SQLException 例外エラー
     */
    private void insertSrRyuudoKiroku(QueryRunner queryRunnerQcdb, Connection conDoc, List<GXHDO502AModel> insertListData) throws SQLException {
        for (int i = 0; i < insertListData.size(); i++) {
            GXHDO502AModel gxhdo502amodel = insertListData.get(i);
            String sql = "INSERT INTO sr_ryuudokiroku ("
                    + " hinmei,lot,passkaisuu,sokuteikaisuu,nagarehin,kojyo,lotno,edaban,No,koutei,sokuteisya,jyunsuiryou,sampleryou,"
                    + " maetyouonpajikan,sokuteigoki,toukaritukikaku,D50ryuudokikaku,D50neraiti,D50ryuudosokuteiti,D90ryuudokikaku,D90ryuudosokuteiti,"
                    + " toukaritu,bikou,torokunichiji,kosinnichiji"
                    + " ) VALUES ( "
                    + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            List<Object> params = setUpdateParameterSrRyuudoKiroku(true, gxhdo502amodel);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerQcdb.update(conDoc, sql, params.toArray());
        }
    }

    /**
     * 粒度記録(sr_ryuudokiroku)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param updateListData 状態が 1:更新 のﾃﾞｰﾀ
     * @throws SQLException 例外エラー
     */
    private void updateSrRyuudoKiroku(QueryRunner queryRunnerDoc, Connection conDoc, List<GXHDO502AModel> updateListData) throws SQLException {

        for (int i = 0; i < updateListData.size(); i++) {
            GXHDO502AModel gxhdo502amodel = updateListData.get(i);
            String sql = "UPDATE sr_ryuudokiroku SET "
                    + " nagarehin = ?,kojyo = ?,lotno = ?,edaban = ?,No = ?,koutei = ?,sokuteisya = ?,jyunsuiryou = ?,sampleryou = ?,"
                    + " maetyouonpajikan = ?,sokuteigoki = ?,toukaritukikaku = ?,D50ryuudokikaku = ?,D50neraiti = ?,D50ryuudosokuteiti = ?,D90ryuudokikaku = ?,"
                    + " D90ryuudosokuteiti = ?,toukaritu = ?,bikou = ?,kosinnichiji = ?"
                    + " WHERE hinmei = ? AND lot = ? AND passkaisuu = ? AND sokuteikaisuu = ? ";

            List<Object> params = setUpdateParameterSrRyuudoKiroku(false, gxhdo502amodel);

            //検索条件設定
            params.add(gxhdo502amodel.getHinmei()); // 品名
            params.add(gxhdo502amodel.getLot()); // ﾛｯﾄ
            params.add(gxhdo502amodel.getPasskaisuu()); // ﾊﾟｽ回数(PASS)
            params.add(getCmbSokuteikaisuuDbValue(StringUtil.nullToBlank(gxhdo502amodel.getSokuteikaisuu()))); // 測定回数
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(conDoc, sql, params.toArray());
        }
    }

    /**
     * 粒度記録(sr_ryuudokiroku)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param gxhdo502amodel モデルデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrRyuudoKiroku(boolean isInsert, GXHDO502AModel gxhdo502amodel) {

        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp systemTime = new Timestamp(System.currentTimeMillis());
        String strSystime = sdf.format(systemTime);
        String lotNoStr = gxhdo502amodel.getWiplotno(); // WIPLotNo
        String paramKojyo = ""; // 工場ｺｰﾄﾞ
        String paramLotNo = ""; // ﾛｯﾄNo
        String paramEdaban = ""; // 枝番
        if (!StringUtil.isEmpty(gxhdo502amodel.getWiplotno())) {
            paramKojyo = StringUtils.substring(lotNoStr, 0, 3);
            paramLotNo = StringUtils.substring(lotNoStr, 3, 12);
            paramEdaban = StringUtils.substring(lotNoStr, 12, 15);
        }
        if (isInsert) {
            params.add(gxhdo502amodel.getHinmei()); // 品名
            params.add(gxhdo502amodel.getLot()); // ﾛｯﾄ
            params.add(gxhdo502amodel.getPasskaisuu()); // ﾊﾟｽ回数(PASS)
            params.add(getCmbSokuteikaisuuDbValue(StringUtil.nullToBlank(gxhdo502amodel.getSokuteikaisuu()))); // 測定回数
        }
        // 流れ品ﾗｼﾞｵﾎﾞﾀﾝが、流れ品の場合:1 その他の場合:0
        params.add(getRadioDbValue(nagarehin, 0)); //流れ品
        params.add(DBUtil.stringToStringObjectDefaultNull(paramKojyo)); //工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(paramLotNo)); //ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(paramEdaban)); //枝番
        params.add(gxhdo502amodel.getNo()); //No
        params.add(getCmbKouteiDbValue(StringUtil.nullToBlank(gxhdo502amodel.getKoutei()))); //工程
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getSokuteisya())); //測定者
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getJyunsuiryou())); //純水量
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getSampleryou())); //ｻﾝﾌﾟﾙ量
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getMaetyouonpajikan())); //前超音波時間
        params.add(gxhdo502amodel.getSokuteigoki()); //測定号機(号機)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getToukaritukikaku())); //透過率規格
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getD50ryuudokikaku())); //D50粒度規格
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getD50neraiti())); //D50狙い値
        params.add(gxhdo502amodel.getD50ryuudosokuteiti()); //D50粒度測定値(μm)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getD90ryuudokikaku())); //D90粒度規格
        params.add(gxhdo502amodel.getD90ryuudosokuteiti()); //D90粒度測定値(μm)
        params.add(gxhdo502amodel.getToukaritu()); //透過率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo502amodel.getBikou())); //備考
        if (isInsert) {
            params.add(strSystime); //登録日時
            params.add(strSystime); //更新日時
        } else {
            params.add(strSystime); //更新日時
        }
        return params;
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
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.JYUNSUIRYOU);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.SAMPLERYOU);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.MAETYOUONPAJIKAN);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.TOUKARITUKIKAKU);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.D50RYUUDOKIKAKU);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.D50NERAITI);
        checkExistItem(errorItemIdList, itemList, GXHDO502AConst.D90RYUUDOKIKAKU);
        return errorItemIdList.size() > 0;
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
     * 【一覧表示部.WIPLotNo】ﾛｽﾄﾌｫｰｶｽ時処理
     *
     * @param wipLotNoStr 入力されたWIPLotNoの値
     * @param rowIndex フォーカスアウトあれる開始時刻の行目
     * @throws SQLException 例外エラー
     */
    public void doWIPLotNoFocusblur(String wipLotNoStr, int rowIndex) throws SQLException {

        GXHDO502AModel gxhdo502amodelItem = getListData().stream().filter(gxhdo502amodel -> gxhdo502amodel.getRowIndx() == rowIndex).findFirst().orElse(null);
        if (gxhdo502amodelItem == null) {
            return;
        }
        // 背景色をクリア
        clearListDataBgcolor();
        // WIPLotNo
        if (showError(checkC101(wipLotNoStr, "WIPLotNo", 15))) {
            gxhdo502amodelItem.setWiplotnobgcolor(GXHDO502AConst.ERROR_COLOR);
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
        if ("0".equals(StringUtil.nullToBlank(gxhdo502amodelItem.getJyoutaiFlg()))) {
            gxhdo502amodelItem.setHinmei(strHinmei);
            gxhdo502amodelItem.setLot(StringUtil.nullToBlank(shikakariData.get("lotno")));
        }
        boolean setKikakuchiFlg = true;
        String syurui = "粒度記録";
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
                        setItemKikakuchi(gxhdo502amodelItem, fxhdd01.getKikakuChi(), fxhdd01.getItemId());
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
                    setItemKikakuchi(gxhdo502amodelItem, fxhdd01.getKikakuChi(), fxhdd01.getItemId());
                    fxhdd01.setRender1(true);
                });
            });
        }
    }

    /**
     * 項目規格値を設定
     *
     * @param gxhdo502amodel モデルデータ
     * @param kikakuChi 項目規格値
     * @param itemId 項目Id
     */
    private void setItemKikakuchi(GXHDO502AModel gxhdo502amodel, String kikakuChi, String itemId) {
        switch (itemId) {
            case GXHDO502AConst.JYUNSUIRYOU:
                // 純水量
                gxhdo502amodel.setJyunsuiryou(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.SAMPLERYOU:
                // ｻﾝﾌﾟﾙ量
                gxhdo502amodel.setSampleryou(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.MAETYOUONPAJIKAN:
                // 前超音波時間
                gxhdo502amodel.setMaetyouonpajikan(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.TOUKARITUKIKAKU:
                // 透過率規格
                gxhdo502amodel.setToukaritukikaku(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.D50RYUUDOKIKAKU:
                // D50粒度規格
                gxhdo502amodel.setD50ryuudokikaku(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.D50NERAITI:
                // D50狙い値
                gxhdo502amodel.setD50neraiti(StringUtil.nullToBlank(kikakuChi));
                break;
            case GXHDO502AConst.D90RYUUDOKIKAKU:
                // D90粒度規格
                gxhdo502amodel.setD90ryuudokikaku(StringUtil.nullToBlank(kikakuChi));
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
            params.add("粒度記録");
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
     * ログインユーザー権限チェック
     *
     * @return 権限がある場合true
     */
    private boolean userAuthLogin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String authParam;
        if ("checkTorokuUserAuth".equals(getWarnProcess())) {
            authParam = getUSER_AUTH_TOROKU_PARAM();
        } else {
            authParam = getUSER_AUTH_DELETE_PARAM();
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
            authParam = getUSER_AUTH_TOROKU_PARAM();
        } else {
            authParam = getUSER_AUTH_DELETE_PARAM();
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
     * 正常な場合実行する
     *
     * @throws java.sql.SQLException
     */
    public void doToroku() throws SQLException {
        // 入力値があるデータを取得
        List<GXHDO502AModel> hasValueListdata = getHasValueListdata();
        // 状態が 0:登録 のﾃﾞｰﾀを取得する
        List<GXHDO502AModel> insertListData = getInsertListData(hasValueListdata);
        // 状態が 1:更新 のﾃﾞｰﾀを取得する
        List<GXHDO502AModel> updateListData = getUpdateListData(hasValueListdata);
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        Connection conDoc = null;
        try {
            conDoc = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());

            // 状態が 0:登録 のﾃﾞｰﾀを粒度記録にﾃﾞｰﾀ登録を行う
            insertSrRyuudoKiroku(queryRunner, conDoc, insertListData);
            // 状態が 1:更新 のﾃﾞｰﾀを粒度記録にﾃﾞｰﾀ更新を行う
            updateSrRyuudoKiroku(queryRunner, conDoc, updateListData);

            List<String> messageList = new ArrayList<>();
            messageList.add(String.format("粒度記録を%d件登録しました", insertListData.size()));
            messageList.add(String.format("粒度記録を%d件更新しました", updateListData.size()));
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
     * 削除処理確認
     */
    public void confirmDelete() {
        List<GXHDO502AModel> deleteListData
                = this.getListData().stream().filter(gxhdo502amodel -> "true".equals(gxhdo502amodel.getChkboxvalue())).collect(Collectors.toList());

        // 「sr_ryuudokiroku:粒度記録」から情報取得
        List<GXHDO502AModel> srHihyoumensekisokuteikirokuListData = getSrRyuudokirokuListData(deleteListData);
        boolean errorFlg = false;
        // 削除の場合、キーのデータがない、あるいは更新日時が表示時と変わっている場合、排他エラーとする。
        for (GXHDO502AModel gxhdo502amodel : deleteListData) {
            List<GXHDO502AModel> collect = srHihyoumensekisokuteikirokuListData.stream().filter(o
                    -> StringUtil.nullToBlank(gxhdo502amodel.getHinmei()).equals(StringUtil.nullToBlank(o.getHinmei()))
                    && StringUtil.nullToBlank(gxhdo502amodel.getLot()).equals(StringUtil.nullToBlank(o.getLot()))
                    && gxhdo502amodel.getPasskaisuu().equals(o.getPasskaisuu())
                    && getCmbSokuteikaisuuDbValue(gxhdo502amodel.getSokuteikaisuu()).equals(((Integer) Integer.parseInt(o.getSokuteikaisuu())))
            ).collect(Collectors.toList());
            if (collect.isEmpty()) {
                errorFlg = true;
                break;
            }
            GXHDO502AModel gxhdo502amodelTableData = collect.get(0);
            Timestamp gamenKosinnichiji = (Timestamp) gxhdo502amodel.getKosinnichiji();
            Timestamp tableKosinnichiji = (Timestamp) gxhdo502amodelTableData.getKosinnichiji();
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
        long count = this.getListData().stream().filter(gxhdo502amodel -> "true".equals(gxhdo502amodel.getChkboxvalue())).count();
        if (count == 0) {
            existError(MessageUtil.getErrorMessageInfo("XHD-000199", false, false, null, "削除ﾁｪｯｸﾎﾞｯｸｽ").getErrorMessage());
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
            try {
                doDelete();
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
        }
    }

    /**
     * 削除処理
     *
     * @throws java.sql.SQLException
     */
    public void doDelete() throws SQLException {

        List<String> messageList = new ArrayList<>();
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        Connection conDoc = null;

        List<GXHDO502AModel> deleteListData
                = this.getListData().stream().filter(gxhdo502amodel -> "true".equals(gxhdo502amodel.getChkboxvalue())).collect(Collectors.toList());

        try {
            conDoc = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());

            for (GXHDO502AModel gxhdo502amodel : deleteListData) {
                List<Object> params = new ArrayList<>();
                String sql = "DELETE FROM sr_ryuudokiroku WHERE hinmei = ?  AND lot = ? AND passkaisuu = ? AND sokuteikaisuu = ?";
                //検索条件設定
                params.add(gxhdo502amodel.getHinmei());//品名
                params.add(gxhdo502amodel.getLot());//ﾛｯﾄ
                params.add(gxhdo502amodel.getPasskaisuu());//ﾊﾟｽ回数(PASS)
                params.add(getCmbSokuteikaisuuDbValue(gxhdo502amodel.getSokuteikaisuu()));//測定回数
                DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                queryRunner.update(conDoc, sql, params.toArray());
            }
            ResultMessage beanResultMessage = (ResultMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_RESULT_MESSAGE);
            messageList.add(String.format("粒度記録を%d件削除しました", deleteListData.size()));
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
     * ﾗｼﾞｵﾎﾞﾀﾝ値(DB内のValue値)取得
     *
     * @param checkBoxValue ﾗｼﾞｵﾎﾞﾀﾝValue値
     * @param defaultValue チェックがついていない場合のデフォルト値
     * @return ﾗｼﾞｵﾎﾞﾀﾝテキスト値
     */
    private Integer getRadioDbValue(String radioValue, Integer defaultValue) {
        if ("流れ品".equals(radioValue)) {
            return 1;
        }
        return defaultValue;
    }

    /**
     * チェックボックス値(DB内のValue値)取得
     *
     * @param checkBoxValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private Integer getCmbSokuteikaisuuDbValue(String checkBoxValue) {
        switch (checkBoxValue) {
            case "1回目":
                return 0;
            case "再測定1回目":
                return 1;
            case "再測定2回目":
                return 2;
            case "再測定3回目":
                return 3;
            case "再測定4回目":
                return 4;
            default:
                break;
        }
        return null;
    }

    /**
     * チェックボックス値(DB内のValue値)取得
     *
     * @param checkBoxValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private Integer getCmbKouteiDbValue(String checkBoxValue) {
        switch (checkBoxValue) {
            case "ｶﾞﾗｽ":
                return 0;
            case "誘電体":
                return 1;
            default:
                break;
        }
        return null;
    }

    /**
     * 「sr_ryuudokiroku:粒度記録」から情報取得
     *
     * @param listdata データリスト
     * @return 粒度記録情報
     */
    private List<GXHDO502AModel> getSrRyuudokirokuListData(List<GXHDO502AModel> listdata) {
        ArrayList<String> hinmeiList = new ArrayList<>();
        ArrayList<String> lotList = new ArrayList<>();
        listdata.forEach((GXHDO502AModel) -> {
            if (!hinmeiList.contains(StringUtil.nullToBlank(GXHDO502AModel.getHinmei()))) {
                hinmeiList.add(StringUtil.nullToBlank(GXHDO502AModel.getHinmei()));
            }
            if (!lotList.contains(StringUtil.nullToBlank(GXHDO502AModel.getLot()))) {
                lotList.add(StringUtil.nullToBlank(GXHDO502AModel.getLot()));
            }
        });
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "select hinmei, lot, passkaisuu, sokuteikaisuu, kosinnichiji "
                    + " FROM sr_ryuudokiroku "
                    + "WHERE "
                    + DBUtil.getInConditionPreparedStatement("hinmei", hinmeiList.size())
                    + " AND " + DBUtil.getInConditionPreparedStatement("lot", lotList.size())
                    + " ORDER BY hinmei ASC";

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
            ResultSetHandler<List<GXHDO502AModel>> beanHandler
                    = new BeanListHandler<>(GXHDO502AModel.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

            return queryRunner.query(sql, beanHandler, params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("粒度記録情報取得失敗", ex, LOGGER);
        }
        return null;
    }

    /**
     * 件数0の場合「戻る」のみが表示
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * 件数0の場合「戻る」のみが表示
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
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
     * 一覧表示データ取得
     *
     * @return 一覧表示データ
     */
    public List<GXHDO502AModel> getListData() {
        return listData;
    }

    /**
     * WIPLotNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * WIPLotNo
     *
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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
     * LotNo
     *
     * @return the paramLotno
     */
    public String getParamLotno() {
        return paramLotno;
    }

    /**
     * LotNo
     *
     * @param paramLotno the paramLotno to set
     */
    public void setParamLotno(String paramLotno) {
        this.paramLotno = paramLotno;
    }

    /**
     * 一覧工程リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbKouteiData
     */
    public String[] getCmbKouteiData() {
        return cmbKouteiData;
    }

    /**
     * 一覧工程リスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbKouteiData the cmbKouteiData to set
     */
    public void setCmbKouteiData(String[] cmbKouteiData) {
        this.cmbKouteiData = cmbKouteiData;
    }

    /**
     * 一覧測定回数リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbSokuteikaisuuData
     */
    public String[] getCmbSokuteikaisuuData() {
        return cmbSokuteikaisuuData;
    }

    /**
     * 一覧測定回数リスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbSokuteikaisuuData the cmbSokuteikaisuuData to set
     */
    public void setCmbSokuteikaisuuData(String[] cmbSokuteikaisuuData) {
        this.cmbSokuteikaisuuData = cmbSokuteikaisuuData;
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
     * ユーザー認証パラメータ(修正)
     *
     * @return the USER_AUTH_TOROKU_PARAM
     */
    public static String getUSER_AUTH_TOROKU_PARAM() {
        return USER_AUTH_TOROKU_PARAM;
    }

    /**
     * ユーザー認証パラメータ(修正)
     *
     * @param aUSER_AUTH_TOROKU_PARAM the USER_AUTH_TOROKU_PARAM to set
     */
    public static void setUSER_AUTH_TOROKU_PARAM(String aUSER_AUTH_TOROKU_PARAM) {
        USER_AUTH_TOROKU_PARAM = aUSER_AUTH_TOROKU_PARAM;
    }

    /**
     * ユーザー認証パラメータ(削除)
     *
     * @return the USER_AUTH_DELETE_PARAM
     */
    public static String getUSER_AUTH_DELETE_PARAM() {
        return USER_AUTH_DELETE_PARAM;
    }

    /**
     * ユーザー認証パラメータ(削除)
     *
     * @param aUSER_AUTH_DELETE_PARAM the USER_AUTH_DELETE_PARAM to set
     */
    public static void setUSER_AUTH_DELETE_PARAM(String aUSER_AUTH_DELETE_PARAM) {
        USER_AUTH_DELETE_PARAM = aUSER_AUTH_DELETE_PARAM;
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

}
