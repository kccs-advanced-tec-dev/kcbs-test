/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo211;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO211CModel;
import jp.co.kccs.xhd.model.GXHDO211DModel;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
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
 * システム名 焼成温度検索<br>
 * <br>
 * 変更日 2019/11/12<br>
 * 計画書No K1811-DS001<br>
 * 変更者 SYSNAVI K.Hisanaga<br>
 * 変更理由 新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO211C(焼成温度検索)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/11/12
 */
@Named
@ViewScoped
public class GXHDO211C implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO211C.class.getName());

    /**
     * 検索LIMIT値
     */
    private static final int SEARCH_LIMIT = 200;

    /**
     * 条件LIMIT値
     */
    private static final int JOKEN_LIMIT = 500;

    /**
     * 特殊品KCPNO1
     */
    private static final String TOKUSHU_HIN_KCPNO1 = "C0CTN05V5H1000J..........";

    /**
     * 特殊品KCPNO2
     */
    private static final String TOKUSHU_HIN_KCPNO2 = "C0CTN15V5J1000J..........";

    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;

    /**
     * パラメータマスタ操作
     */
    @Inject
    private SelectParam selectParam;

    /**
     * 一覧表示データ
     */
    private List<GXHDO211CModel> listData = null;

    /**
     * 焼成ｺｰﾄﾞﾘｽﾄ
     */
    private List<String[]> syoseiCodeList = null;

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
     * 窒素濃度パラメータ
     */
    private String[] tissonoudoParam = null;

    /**
     * 検索条件：ﾛｯﾄNo
     */
    private String lotNo = "";
    /**
     * 検索条件：KCPNO
     */
    private String kcpNo = "";
    /**
     * 検索条件：工程
     */
    private String koutei = "";
    /**
     * 検索条件：受入日(FROM)
     */
    private String ukeirebiF = "";
    /**
     * 検索条件：受入日(TO)
     */
    private String ukeirebiT = "";
    /**
     * 検索条件：process
     */
    private String process = "";
    /**
     * 検索条件：品種
     */
    private String hinsyu = "";

    /**
     * 焼成工程検索戻り先ID
     */
    private String returnId211DItem = "form:lblKoutei";

    /**
     * 焼成工程(焼成工程検索にて使用)
     */
    private List<GXHDO211DModel> syoseiKoteiItemList = new ArrayList<>();

    /**
     * 選択工程ﾘｽﾄ(焼成工程検索にて使用)
     */
    private List<String> selectedSyoseiKoteiItemList = new ArrayList<>();

    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";

    /**
     * 接続データベース(0:PostgreSQL、1:PSQL)
     */
    private String db_connect_mode = "";

    /**
     * コンストラクタ
     */
    public GXHDO211C() {
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
     * 一覧表示データ取得
     *
     * @return 一覧表示データ
     */
    public List<GXHDO211CModel> getListData() {
        return listData;
    }

    /**
     * 検索条件：ﾛｯﾄNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：ﾛｯﾄNo
     *
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 検索条件：KCPNo
     *
     * @return the kcpNo
     */
    public String getKcpNo() {
        return kcpNo;
    }

    /**
     * 検索条件：KCPNo
     *
     * @param kcpNo the kcpNo to set
     */
    public void setKcpNo(String kcpNo) {
        this.kcpNo = kcpNo;
    }

    /**
     * 検索条件：工程
     *
     * @return the koutei
     */
    public String getKoutei() {
        return koutei;
    }

    /**
     * 検索条件：工程
     *
     * @param koutei the koutei to set
     */
    public void setKoutei(String koutei) {
        this.koutei = koutei;
    }

    /**
     * 検索条件：受入日(FROM)
     *
     * @return the ukeirebiF
     */
    public String getUkeirebiF() {
        return ukeirebiF;
    }

    /**
     * 検索条件：受入日(FROM)
     *
     * @param ukeirebiF the ukeirebiF to set
     */
    public void setUkeirebiF(String ukeirebiF) {
        this.ukeirebiF = ukeirebiF;
    }

    /**
     * 検索条件：受入日(TO)
     *
     * @return the ukeirebiT
     */
    public String getUkeirebiT() {
        return ukeirebiT;
    }

    /**
     * 検索条件：印刷開始日(TO)
     *
     * @param ukeirebiT the ukeirebiT to set
     */
    public void setUkeirebiT(String ukeirebiT) {
        this.ukeirebiT = ukeirebiT;
    }

    /**
     * 検索条件：process
     *
     * @return the process
     */
    public String getProcess() {
        return process;
    }

    /**
     * 検索条件：process
     *
     * @param process the process to set
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * 検索条件：品種
     *
     * @return the hinsyu
     */
    public String getHinsyu() {
        return hinsyu;
    }

    /**
     * 検索条件：品種
     *
     * @param hinsyu the hinsyu to set
     */
    public void setHinsyu(String hinsyu) {
        this.hinsyu = hinsyu;
    }

    /**
     * 焼成工程リスト
     *
     * @return the syoseiKoteiItemList
     */
    public List<GXHDO211DModel> getSyoseiKoteiItemList() {
        return syoseiKoteiItemList;
    }

    /**
     * 焼成工程リスト
     *
     * @param syoseiKoteiItemList the syoseiKoteiItemList to set
     */
    public void setSyoseiKoteiItemList(List<GXHDO211DModel> syoseiKoteiItemList) {
        this.syoseiKoteiItemList = syoseiKoteiItemList;
    }

    /**
     * 選択工程ﾘｽﾄ
     *
     * @return the selectedSyoseiKoteiItemList
     */
    public List<String> getSelectedSyoseiKoteiItemList() {
        return selectedSyoseiKoteiItemList;
    }

    /**
     * 選択工程ﾘｽﾄ
     *
     * @param selectedSyoseiKoteiItemList the selectedSyoseiKoteiItemList to set
     */
    public void setSelectedSyoseiKoteiItemList(List<String> selectedSyoseiKoteiItemList) {
        this.selectedSyoseiKoteiItemList = selectedSyoseiKoteiItemList;

        if (selectedSyoseiKoteiItemList.isEmpty()) {
            this.koutei = "";
            return;
        }

        // 工程名を取得してセット
        this.koutei = this.syoseiKoteiItemList.stream().filter(n -> selectedSyoseiKoteiItemList.get(0).equals(n.getKouteiCode())).findFirst().map(f -> f.getKoutei()).orElse("");
        if (1 < selectedSyoseiKoteiItemList.size()) {
            this.koutei += "...";
        }
    }

    /**
     * メインデータの件数を保持
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * メインデータの件数を保持
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    /**
     * 焼成工程検索戻り先ID
     *
     * @return the returnId211DItem
     */
    public String getReturnId211DItem() {
        return returnId211DItem;
    }

    /**
     * 焼成工程検索戻り先ID
     *
     * @param returnId211DItem the returnId211DItem to set
     */
    public void setReturnId211DItem(String returnId211DItem) {
        this.returnId211DItem = returnId211DItem;
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

        try {
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

            // データベースの接続モードを取得
            FacesContext fc = FacesContext.getCurrentInstance();
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");
            this.db_connect_mode = myParam.getString("db_connect_mode");

            // 画面クリア
            clear();

            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);

            //メッセージリスト
            List<String> messageList = new ArrayList<>();

            // 焼成工程ｺｰﾄﾞﾘｽﾄ作成処理(戻り値はﾒｯｾｰｼﾞﾘｽﾄに格納)
            messageList.addAll(initSyoseiCodeList(queryRunnerDoc, queryRunnerWip));
            if (!messageList.isEmpty()) {
                // エラー発生時はメッセージをﾀﾞｲｱﾛｸﾞにセットしてリターン
                displayStyle = "display:none;";
                openInitMessage(messageList);
                return;
            }

            // 検索件数警告値取得
            this.listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;

            // 検索件数上限値取得
            this.listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;

            // ﾍﾟｰｼﾞﾈｰｼｮﾝ閾値取得(値が無ければデフォルト値(30))
            if (!StringUtil.isEmpty(selectParam.getValue("GXHDO211C_display_page_count", session))) {
                try {
                    this.listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO211C_display_page_count", session));
                } catch (NumberFormatException e) {
                    messageList.add(MessageUtil.getMessage("XHD-000151"));
                }
            }

            // 受入日初期値設定
            messageList.addAll(initUkeirebi(queryRunnerDoc));
            
            // 窒素パラメータ取得保持
            messageList.addAll(initTissonoudoParam(queryRunnerDoc));
            
            //焼成工程検索の初期データを設定
            initSyoseiKoteiKensakuData();
            
            if (!messageList.isEmpty()) {
                // エラー発生時はメッセージをﾀﾞｲｱﾛｸﾞにセットしてリターン
                openInitMessage(messageList);
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

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
        this.lotNo = "";
        this.kcpNo = "";
        this.koutei = "";
        this.ukeirebiF = "";
        this.ukeirebiT = "";
        this.process = "";
        this.hinsyu = "";
        this.listData = new ArrayList<>();
        this.selectedSyoseiKoteiItemList = new ArrayList<>();
    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {

        try {
            // 入力チェック処理
            ValidateUtil validateUtil = new ValidateUtil();

            // ロットNo
            if (!StringUtil.isEmpty(this.lotNo) && (StringUtil.getLength(this.lotNo) != 11 && StringUtil.getLength(this.lotNo) != 14)) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            if (!StringUtil.isEmpty(this.lotNo) && existError(validateUtil.checkValueE001(this.lotNo))) {
                return;
            }

            // KCPNO
            if (existError(validateUtil.checkC103(this.kcpNo, "KCPNO", 25))) {
                return;
            }

            // 受入日(FROM)
            if (existError(validateUtil.checkC101(this.ukeirebiF, "受入日(from)", 6))
                    || existError(validateUtil.checkC201ForDate(this.ukeirebiF, "受入日(from)"))
                    || existError(validateUtil.checkC501(this.ukeirebiF, "受入日(from)"))) {
                return;
            }
            // 受入日(TO)
            if (existError(validateUtil.checkC101(this.ukeirebiT, "受入日(to)", 6))
                    || existError(validateUtil.checkC201ForDate(this.ukeirebiT, "受入日(to)"))
                    || existError(validateUtil.checkC501(this.ukeirebiT, "受入日(to)"))) {
                return;
            }

            // ﾌﾟﾛｾｽ
            if (existError(validateUtil.checkC103(this.process, "ﾌﾟﾛｾｽ", 10))) {
                return;
            }

            // 品種
            if (existError(validateUtil.checkC101(this.hinsyu, "品種", 2))) {
                return;
            }

            // 一覧表示件数を取得
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            long count = loadSearchMainDataCount(queryRunnerWip);
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
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 警告ダイアログで「OK」押下時
     */
    public void processWarnOk() {
        // 検索処理実行
        selectListData();
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        try {
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);

            List<GXHDO211CModel> mainData = loadSearchMainData(queryRunnerWip);

            // 設計データ取得
            List<Map<String, Object>> sekkeiData = getSekkeiData(queryRunnerQcdb, mainData);
            // 設計データが取得出来なかった場合エラー
            if (sekkeiData.isEmpty()) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000002"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            // 条件ﾃｰﾌﾞﾙより水素濃度の規格値を取得
            List<Map<String, Object>> jokenDataSuisonoudo = getJokenData(queryRunnerQcdb, sekkeiData, "焼成", "設定", "水素濃度");

            // 条件ﾃｰﾌﾞﾙよりﾋﾟｰｸ温度の規格値を取得
            List<Map<String, Object>> jokenDataPeakondo = getJokenData(queryRunnerQcdb, sekkeiData, "焼成", "設定", "ﾋﾟｰｸ温度");

            // 条件ﾃｰﾌﾞﾙより窒素濃度の規格値を取得
            List<Map<String, Object>> jokenDataTissonoudo = new ArrayList<>();
            if (tissonoudoParam != null) {
                jokenDataTissonoudo = getJokenData(queryRunnerQcdb, sekkeiData, this.tissonoudoParam[0], this.tissonoudoParam[1], this.tissonoudoParam[2]);
            }
            
            // 指示温度よりデータを取得
            List<Map<String, Object>> fxhdd06Data = getFxhdd06Data(queryRunnerDoc, mainData);

            // 積層SPSよりデータを取得
            List<Map<String, Object>> srSpssekisouData = getSrSpssekisouData(queryRunnerQcdb, mainData);

            // 積層RSUSよりデータを取得
            List<Map<String, Object>> srRsussekData = getSrRsussekData(queryRunnerQcdb, mainData);

            // 印刷積層RHAPSよりデータを取得
            List<Map<String, Object>> srRhapsData = getSrRhapsData(queryRunnerQcdb, mainData);

            // 取得した全データを結合する。
            this.listData = getMergeData(mainData, sekkeiData, jokenDataSuisonoudo, jokenDataPeakondo, jokenDataTissonoudo, fxhdd06Data, srSpssekisouData, srRsussekData, srRhapsData);

        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo211c.json"));
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            List<List<GXHDO211CModel>> datas = new ArrayList<>();

            List<String> hinsyuList = getHinsyuList(this.listData);
            for (String data : hinsyuList) {
                datas.add(this.listData.stream().filter(n -> data.equals(n.getHinsyu())).collect(Collectors.toList()));
            }

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcelMultipleSheet(list, myParam.getString("download_temp"), hinsyuList.toArray(new String[hinsyuList.size()]), datas);

            // ダウンロードファイル名
            String downloadFileName = "焼成温度検索_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
     * 品種を重複を排除した状態で取得
     *
     * @param dataList データリスト
     * @return
     */
    private List<String> getHinsyuList(List<GXHDO211CModel> dataList) {
        List<String> hinsyuList = new ArrayList<>();
        for (GXHDO211CModel model : dataList) {
            if (!hinsyuList.contains(model.getHinsyu())) {
                hinsyuList.add(model.getHinsyu());
            }
        }
        return hinsyuList;
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
        return !(listData == null || listData.isEmpty());
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
     * 初期表示メッセージ表示処理
     *
     * @param messageList メッセージリスト
     * @param paramName パラメータ名称
     * @param paramValue パラメータ値
     */
    private void openInitMessage(List<String> messageList) {
        // メッセージを画面に渡す
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(messageList);

        //スクリプトを実行して画面呼出
        RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param username ﾕｰｻﾞ名
     * @param key ｷｰ
     * @return 取得ﾃﾞｰﾀ
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String username, String key) throws SQLException {

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT data "
                + " FROM fxhbm03 "
                + " WHERE user_name = ? AND key = ? ";

        List<Object> params = new ArrayList<>();
        params.add(username);
        params.add(key);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());

    }

    /**
     * [工程ｺｰﾄﾞﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @param 工程ﾘｽﾄ ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadKoteimasData(QueryRunner queryRunnerWip, List<String> koteiList) throws SQLException {

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT koteicode,kotei "
                + "FROM koteimas "
                + "WHERE right(kotei,1) <> '*' "
                + "AND ";

        sql += DBUtil.getInConditionPreparedStatement("koteicode", koteiList.size());

        List<Object> params = new ArrayList<>();
        params.addAll(koteiList);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * 焼成ｺｰﾄﾞﾘｽﾄに初期設定
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     */
    private List<String> initSyoseiCodeList(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip) throws SQLException {

        this.syoseiCodeList = new ArrayList<>();

        List<String> messageList = new ArrayList<>();
        // 焼成工程ｺｰﾄﾞﾘｽﾄ取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "焼成温度検索ﾊﾟﾗﾒｰﾀ", "焼成工程ｺｰﾄﾞﾘｽﾄ");
        if (fxhbm03Data == null || fxhbm03Data.isEmpty()) {
            // エラーをセットしてリターン
            messageList.add(MessageUtil.getMessage("XHD-000150"));
            return messageList;
        }

        // 焼成工程ｺｰﾄﾞをｶﾝﾏで分割しﾘｽﾄに格納
        List<String> spSyoseiKoteiList = new ArrayList<>(Arrays.asList(StringUtil.nullToBlank(fxhbm03Data.get("data")).split(",", -1)));

        // 焼成工程ｺｰﾄﾞをもとに工程ｺｰﾄﾞﾏｽﾀより工程名を取得
        List<Map<String, Object>> koteimasKoteiList = loadKoteimasData(queryRunnerWip, spSyoseiKoteiList);
        if (koteimasKoteiList == null || koteimasKoteiList.isEmpty()) {
            // エラーをセットしてリターン
            messageList.add(MessageUtil.getMessage("XHD-000150"));
            return messageList;
        }

        // 取得した工程名を焼成工程ｺｰﾄﾞﾘｽﾄの順で【焼成ｺｰﾄﾞﾘｽﾄ】として設定
        spSyoseiKoteiList.forEach((koteiCode) -> {
            Map<String, Object> koteiInfo = koteimasKoteiList.stream().filter(n -> koteiCode.equals(StringUtil.nullToBlank(n.get("koteicode")).trim())).findFirst().orElse(null);
            // 工程ｺｰﾄﾞﾏｽﾀに値が存在する場合、一覧にセット
            if (koteiInfo != null) {
                // 焼成ｺｰﾄﾞﾘｽﾄにｺｰﾄﾞと名称をセット
                this.syoseiCodeList.add(new String[]{koteiCode, StringUtil.nullToBlank(koteiInfo.get("kotei"))});
            }
        });

        return messageList;
    }

    /**
     * 受入日初期設定
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     */
    private List<String> initUkeirebi(QueryRunner queryRunnerDoc) throws SQLException {

        List<String> messageList = new ArrayList<>();
        // 初期日付情報取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "焼成温度検索ﾊﾟﾗﾒｰﾀ", "初期日付");
        if (fxhbm03Data == null || fxhbm03Data.isEmpty()) {
            return messageList;
        }

        int ukeirebiSyoki;
        try {
            ukeirebiSyoki = Integer.parseInt(StringUtil.nullToBlank(fxhbm03Data.get("data")));
        } catch (NumberFormatException e) {
            messageList.add(MessageUtil.getMessage("XHD-000157"));
            return messageList;
        }

        // 現在日から日数を減算
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, ukeirebiSyoki * -1);

        // 減算した日付を受入日に設定
        this.ukeirebiF = new SimpleDateFormat("yyMMdd").format(cal.getTime());

        return messageList;
    }

    /**
     * 窒素濃度に初期設定
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     */
    private List<String> initTissonoudoParam(QueryRunner queryRunnerDoc) throws SQLException {

        List<String> messageList = new ArrayList<>();
        // 焼成工程ｺｰﾄﾞﾘｽﾄ取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "焼成温度検索ﾊﾟﾗﾒｰﾀ", "窒素濃度");
        if (fxhbm03Data == null || fxhbm03Data.isEmpty()) {
            // エラーをセットしてリターン
            messageList.add(MessageUtil.getMessage("XHD-000156"));
            return messageList;
        }

        String[] spNoudo = StringUtil.nullToBlank(fxhbm03Data.get("data")).split(",", -1);
        if (spNoudo.length != 3) {
            // エラーをセットしてリターン
            messageList.add(MessageUtil.getMessage("XHD-000156"));
            return messageList;
        }

        this.tissonoudoParam = spNoudo;

        return messageList;
    }

    /**
     * [仕掛、生産]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<GXHDO211CModel> loadSearchMainData(QueryRunner queryRunnerWip) throws SQLException {

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT S.kojyo ");
        sbSql.append("       ,S.lotno ");
        sbSql.append("       ,S.edaban ");
        sbSql.append("       ,S.kcpno ");
        sbSql.append("       ,S.koteicode ");
        sbSql.append("       ,S.ukeirebi ");
        sbSql.append("       ,S.suuryo ");
        sbSql.append("       ,SE.goukicode ");

        if ("1".equals(this.db_connect_mode)) {
            // PSQLの場合
            sbSql.append("   ,SUBSTRING(S.kcpno, 8, 2) as hinsyu ");
            sbSql.append(getSearchMainDataSqlAfterFromPSQL());
            sbSql.append(" ORDER BY S.koteicode,SUBSTRING(S.kcpno, 8, 2),S.ukeirebi ");

        } else {
            // PostgreSqlの場合
            sbSql.append("   ,SUBSTR(S.kcpno, 8, 2) as hinsyu ");
            sbSql.append(getSearchMainDataSqlAfterFrom());
            sbSql.append(" ORDER BY S.koteicode,SUBSTR(S.kcpno, 8, 2),S.ukeirebi ");
        }

        // 検索条件のパラメータを取得
        List<Object> params = getSearchMainDataParams();

        // モデルクラスとのマッピング定義
        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");
        mapping.put("lotno", "lotno");
        mapping.put("edaban", "edaban");
        mapping.put("koteicode", "koteicode");
        mapping.put("ukeirebi", "ukeirebi");
        mapping.put("suuryo", "kosu");
        mapping.put("goukicode", "gouki");
        mapping.put("hinsyu", "hinsyu");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<GXHDO211CModel>> beanHandler
                = new BeanListHandler<>(GXHDO211CModel.class, rowProcessor);

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerWip.query(sbSql.toString(), beanHandler, params.toArray());

    }

    /**
     * 一覧表示データ件数取得
     *
     * @param queryRunnerWip オブジェクト
     * @return 取得件数
     * @throws SQLException 例外エラー
     */
    public long loadSearchMainDataCount(QueryRunner queryRunnerWip) throws SQLException {

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT COUNT(*) AS CNT ");
        if ("1".equals(this.db_connect_mode)) {
            // PSQLの場合
            sbSql.append(getSearchMainDataSqlAfterFromPSQL());

        } else {
            // PostgreSqlの場合
            sbSql.append(getSearchMainDataSqlAfterFrom());
        }

        // 検索条件のパラメータを取得
        List<Object> params = getSearchMainDataParams();

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        Map result = queryRunnerWip.query(sbSql.toString(), new MapHandler(), params.toArray());
        long count = (long) result.get("CNT");

        return count;
    }

    /**
     * メインデータSQL取得(FROM句以降部分)
     *
     * @return SQL
     */
    private String getSearchMainDataSqlAfterFrom() {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" FROM sikakari S");
        sbSql.append(" LEFT JOIN seisan SE");
        sbSql.append("    ON S.jissekino = SE.jissekino");
        sbSql.append(" WHERE opencloseflag = '1'");

        // ﾛｯﾄNoが入力されている場合
        if (!StringUtil.isEmpty(this.lotNo)) {
            sbSql.append(" AND S.kojyo = ?  AND S.lotno = ?");
            if (StringUtil.length(this.lotNo) == 14) {
                sbSql.append(" AND S.edaban = ?");
            }
        }

        // kcpnoが入力されている場合
        if (!StringUtil.isEmpty(this.kcpNo)) {
            sbSql.append(" AND S.kcpno LIKE ? ");
        }

        // 工程が入力されている場合
        if (!this.selectedSyoseiKoteiItemList.isEmpty()) {
            sbSql.append(" AND ");
            sbSql.append(DBUtil.getInConditionPreparedStatement("S.koteicode", this.selectedSyoseiKoteiItemList.size()));
        } else if(!this.syoseiKoteiItemList.isEmpty()){
            sbSql.append(" AND ");
            sbSql.append(DBUtil.getInConditionPreparedStatement("S.koteicode", this.syoseiKoteiItemList.size()));
        }

        // 受入日(From)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiF)) {
            sbSql.append(" AND S.ukeirebi >= ?");
        }

        // 受入日(To)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiT)) {
            sbSql.append(" AND S.ukeirebi <= ?");
        }

        // 品種が入力されている場合
        if (!StringUtil.isEmpty(this.hinsyu)) {
            sbSql.append(" AND SUBSTR(S.kcpno, 8, 2) = ?");
        }

        return sbSql.toString();
    }

    /**
     * メインデータSQL取得(FROM句以降部分)※PSQL版
     *
     * @return SQL
     */
    private String getSearchMainDataSqlAfterFromPSQL() {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" FROM sikakari S");
        sbSql.append(" LEFT JOIN seisan SE");
        sbSql.append("    ON S.jissekino = SE.jissekino");
        sbSql.append(" WHERE opencloseflag = '1'");

        // ﾛｯﾄNoが入力されている場合
        if (!StringUtil.isEmpty(this.lotNo)) {
            sbSql.append(" AND S.kojyo = ?  AND S.lotno = ?");
            if (StringUtil.length(this.lotNo) == 14) {
                sbSql.append(" AND S.edaban = ?");
            }
        }

        // kcpnoが入力されている場合
        if (!StringUtil.isEmpty(this.kcpNo)) {
            sbSql.append(" AND S.kcpno LIKE ? ");
        }

        // 工程が入力されている場合
        if (!this.selectedSyoseiKoteiItemList.isEmpty()) {
            sbSql.append(" AND ");
            sbSql.append(DBUtil.getInConditionPreparedStatement("S.koteicode", this.selectedSyoseiKoteiItemList.size()));
        } else if(!this.syoseiKoteiItemList.isEmpty()){
            sbSql.append(" AND ");
            sbSql.append(DBUtil.getInConditionPreparedStatement("S.koteicode", this.syoseiKoteiItemList.size()));
        }

        // 受入日(From)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiF)) {
            sbSql.append(" AND S.ukeirebi >= ?");
        }

        // 受入日(To)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiT)) {
            sbSql.append(" AND S.ukeirebi <= ?");
        }

        // 品種が入力されている場合
        if (!StringUtil.isEmpty(this.hinsyu)) {
            sbSql.append(" AND SUBSTRING(S.kcpno, 8, 2) = ?");
        }

        return sbSql.toString();
    }

    /**
     * メインデータ検索用パラメータ取得
     *
     * @return SQL
     */
    private List<Object> getSearchMainDataParams() {

        List<Object> params = new ArrayList<>();

        // ﾛｯﾄNoが入力されている場合
        if (!StringUtil.isEmpty(this.lotNo)) {
            params.add(StringUtils.substring(this.lotNo, 0, 3));
            params.add(StringUtils.substring(this.lotNo, 3, 11));

            if (StringUtil.length(this.lotNo) == 14) {
                params.add(StringUtils.substring(this.lotNo, 11, 14));
            }
        }

        // kcpnoが入力されている場合
        if (!StringUtil.isEmpty(this.kcpNo)) {
            params.add("%" + kcpNo + "%");
        }

        // 工程が入力されている場合
        if (!this.selectedSyoseiKoteiItemList.isEmpty()) {
            params.addAll(this.selectedSyoseiKoteiItemList);
        } else if(!this.syoseiKoteiItemList.isEmpty()){
            params.addAll(this.syoseiKoteiItemList.stream().map(s -> s.getKouteiCode()).collect(Collectors.toList()));
        }

        // 受入日(From)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiF)) {
            params.add(DateUtil.convertDateStringToInteger(this.ukeirebiF));
        }

        // 受入日(To)が入力されている場合
        if (!StringUtil.isEmpty(this.ukeirebiT)) {
            params.add(DateUtil.convertDateStringToInteger(this.ukeirebiT));
        }

        // 品種が入力されている場合
        if (!StringUtil.isEmpty(this.hinsyu)) {
            params.add(this.hinsyu);
        }

        return params;
    }

    /**
     * 焼成工程検索画面の初期データを設定
     */
    private void initSyoseiKoteiKensakuData() {
        for (String[] syoseiCodeInfo : this.syoseiCodeList) {
            GXHDO211DModel model = new GXHDO211DModel();
            model.setKouteiCode(syoseiCodeInfo[0]);
            model.setKoutei(syoseiCodeInfo[0] + ":" + syoseiCodeInfo[1]);

            this.syoseiKoteiItemList.add(model);
        }
    }

    /**
     * 設計データ取得
     *
     * @param queryRunnerQcdb queryRunner(Qcdb)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getSekkeiData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {
        List<Map<String, String>> lotNoList = getLotNoListExcludeEdaban(mainData);

        BigDecimal size = BigDecimal.valueOf(lotNoList.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();

        List<Map<String, Object>> sekkeiData = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = ((i + 1) * SEARCH_LIMIT);
            if (lotNoList.size() < endIdx) {
                endIdx = lotNoList.size();
            }
            sekkeiData.addAll(loadSekkeiData(queryRunnerQcdb, lotNoList.subList(startIdx, endIdx)));
        }

        return sekkeiData;

    }

    /**
     * 枝番以外(工場ｺｰﾄﾞ、ﾛｯﾄNo)のリストを取得
     *
     * @param mainData メインデータ
     * @return ﾛｯﾄNoリスト
     */
    private List<Map<String, String>> getLotNoListExcludeEdaban(List<GXHDO211CModel> mainData) {
        // 工場ｺｰﾄﾞとﾛｯﾄNoだけのリストを作成
        List<Map<String, String>> list = new ArrayList<>();
        for (GXHDO211CModel model : mainData) {
            Map<String, String> map = new HashMap<>();
            map.put("kojyo", model.getKojyo());
            map.put("lotno", model.getLotno());
            list.add(map);
        }
        // 重複を排除してリターン
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * [設計]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param mainData メインデータ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadSekkeiData(QueryRunner queryRunnerQcdb, List<Map<String, String>> lotnoList) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT KOJYO,LOTNO,EDABAN,SEKKEINO");
        sbSql.append("   FROM da_sekkei");
        sbSql.append("  WHERE (");
        boolean notFirst = false;
        for (Map lotnoInfo : lotnoList) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append("KOJYO = '").append(lotnoInfo.get("kojyo")).append("'");
            sbSql.append(" AND ");
            sbSql.append("LOTNO = '").append(lotnoInfo.get("lotno")).append("'");
            sbSql.append(" AND ");
            sbSql.append("EDABAN = '001'");
            sbSql.append(")");
        }
        sbSql.append(")");

        // プロセスが入力されている場合、条件に追加
        if (!StringUtil.isEmpty(this.process)) {
            sbSql.append(" AND PROCESS = ? ");
        }

        //パラメータをセット
        List<Object> params = new ArrayList<>();
        if (!StringUtil.isEmpty(this.process)) {
            params.add(this.process);
        }

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 条件データ取得
     *
     * @param queryRunnerQcdb queryRunner(Qcdb)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getJokenData(QueryRunner queryRunnerQcdb, List<Map<String, Object>> sekkeiData, String kouteimei, String koumokumei, String kanrikoumoku) throws SQLException {
        BigDecimal size = BigDecimal.valueOf(sekkeiData.size());
        BigDecimal limit = BigDecimal.valueOf(JOKEN_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();
        List<Map<String, Object>> jokenData = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * JOKEN_LIMIT;
            int endIdx = ((i + 1) * JOKEN_LIMIT);
            if (sekkeiData.size() < endIdx) {
                endIdx = sekkeiData.size();
            }
            jokenData.addAll(loadJokenData(queryRunnerQcdb, sekkeiData.subList(startIdx, endIdx), kouteimei, koumokumei, kanrikoumoku));
        }

        return jokenData;

    }

    /**
     * [条件]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @param sekkeiData 設計データ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadJokenData(QueryRunner queryRunnerWip, List<Map<String, Object>> sekkeiData, String kouteimei, String koumokumei, String kanrikoumoku) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT SEKKEINO, KIKAKUCHI");
        sbSql.append("   FROM da_joken");
        sbSql.append("  WHERE (");
        boolean notFirst = false;
        for (Map sekkeiDataInfo : sekkeiData) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append(" SEKKEINO = ").append(StringUtil.nullToBlank(sekkeiDataInfo.get("SEKKEINO")));
        }
        sbSql.append(" )");
        sbSql.append(" AND KOUTEIMEI = ? ");
        sbSql.append(" AND KOUMOKUMEI = ? ");
        sbSql.append(" AND KANRIKOUMOKU = ? ");

        List<Object> params = new ArrayList<>();
        params.add(kouteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);

        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerWip.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 指示温度データ取得
     *
     * @param queryRunnerDoc queryRunner(DocumentServer)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getFxhdd06Data(QueryRunner queryRunnerDoc, List<GXHDO211CModel> mainData) throws SQLException {
        BigDecimal size = BigDecimal.valueOf(mainData.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();
        List<Map<String, Object>> fxhdd06Data = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = (i + 1) * SEARCH_LIMIT;
            if (mainData.size() < endIdx) {
                endIdx = mainData.size();
            }
            fxhdd06Data.addAll(loadFxhdd06Data(queryRunnerDoc, mainData.subList(startIdx, endIdx)));
        }

        return fxhdd06Data;

    }

    /**
     * [指示温度]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param mainData メインデータ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadFxhdd06Data(QueryRunner queryRunnerDoc, List<GXHDO211CModel> mainData) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT kojyo,lotno,edaban,goukijyoho,shijiondo,suisonoudo,shijiondogroup");
        sbSql.append("   FROM fxhdd06");
        sbSql.append("  WHERE (");
        boolean notFirst = false;
        for (GXHDO211CModel model : mainData) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append(" kojyo = '").append(model.getKojyo()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" lotno = '").append(model.getLotno()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" edaban = '").append(model.getEdaban()).append("'");
            sbSql.append(")");
        }
        sbSql.append(" )");
        sbSql.append(" AND deleteflag = 0 ");
        sbSql.append(" ORDER BY kojyo,lotno,edaban,shijiondogroup,goukijyoho");
        //パラメータをセット
        List<Object> params = new ArrayList<>();
        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerDoc.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 積層SPSデータ取得
     *
     * @param queryRunnerQcdb queryRunner(Qcdb)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getSrSpssekisouData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {
        BigDecimal size = BigDecimal.valueOf(mainData.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = ((i + 1) * SEARCH_LIMIT);
            if (mainData.size() < endIdx) {
                endIdx = mainData.size();
            }
            dataList.addAll(loadSrSpssekisouData(queryRunnerQcdb, mainData.subList(startIdx, endIdx)));
        }

        return dataList;

    }

    /**
     * [積層SPS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param mainData メインデータ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadSrSpssekisouData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT kojyo,lotno,edaban,tapelotno");
        sbSql.append(" FROM sr_spssekisou");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (GXHDO211CModel model : mainData) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append(" kojyo = '").append(model.getKojyo()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" lotno = '").append(model.getLotno()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" edaban = '").append(model.getEdaban()).append("'");
            sbSql.append(")");
        }
        sbSql.append(" )");

        //パラメータをセット
        List<Object> params = new ArrayList<>();
        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 積層RSUSデータ取得
     *
     * @param queryRunnerQcdb queryRunner(Qcdb)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getSrRsussekData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {
        BigDecimal size = BigDecimal.valueOf(mainData.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = (i + 1) * SEARCH_LIMIT;
            if (mainData.size() < endIdx) {
                endIdx = mainData.size();
            }
            dataList.addAll(loadSrRsussekData(queryRunnerQcdb, mainData.subList(startIdx, endIdx)));
        }

        return dataList;

    }

    /**
     * [積層RSUS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param mainData メインデータ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadSrRsussekData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT KOJYO,LOTNO,EDABAN,tapelotno");
        sbSql.append(" FROM sr_rsussek");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (GXHDO211CModel model : mainData) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append(" KOJYO = '").append(model.getKojyo()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" LOTNO = '").append(model.getLotno()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" EDABAN = '").append(model.getEdaban()).append("'");
            sbSql.append(")");
        }
        sbSql.append(" )");

        //パラメータをセット
        List<Object> params = new ArrayList<>();
        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 印刷積層RHAPSデータ取得
     *
     * @param queryRunnerQcdb queryRunner(Qcdb)オブジェクト
     * @param mainData 面ﾃﾞｰﾀ
     * @return 設計データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getSrRhapsData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {
        BigDecimal size = BigDecimal.valueOf(mainData.size());
        BigDecimal limit = BigDecimal.valueOf(SEARCH_LIMIT);
        long loopCount = size.divide(limit, 0, RoundingMode.UP).longValue();
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            int startIdx = i * SEARCH_LIMIT;
            int endIdx = (i + 1) * SEARCH_LIMIT;
            if (mainData.size() < endIdx) {
                endIdx = mainData.size();
            }
            dataList.addAll(loadSrRhapsData(queryRunnerQcdb, mainData.subList(startIdx, endIdx)));
        }

        return dataList;

    }

    /**
     * [印刷積層RHPS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param mainData メインデータ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadSrRhapsData(QueryRunner queryRunnerQcdb, List<GXHDO211CModel> mainData) throws SQLException {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT KOJYO,LOTNO,EDABAN,ETAPELOT");
        sbSql.append(" FROM sr_rhaps");
        sbSql.append(" WHERE (");
        boolean notFirst = false;
        for (GXHDO211CModel model : mainData) {
            if (notFirst) {
                sbSql.append(" OR ");
            } else {
                notFirst = true;
            }
            sbSql.append("(");
            sbSql.append(" KOJYO = '").append(model.getKojyo()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" LOTNO = '").append(model.getLotno()).append("'");
            sbSql.append(" AND ");
            sbSql.append(" EDABAN = '").append(model.getEdaban()).append("'");
            sbSql.append(")");
        }
        sbSql.append(" )");

        //パラメータをセット
        List<Object> params = new ArrayList<>();
        DBUtil.outputSQLLog(sbSql.toString(), params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sbSql.toString(), new MapListHandler(), params.toArray());
    }

    /**
     * 取得した全データをまとめて表示用データとして取得する。
     *
     * @param mainData メインデータ
     * @param sekkeiData 設計データ
     * @param jokenDataSuisonoudo 条件データ(水素濃度)
     * @param jokenDataPeakondo 条件データPeakOndo
     * @param jokenDataTissonoudo 条件データ窒素温度
     * @param fxhdd06Data 指示温度データ
     * @param srSpssekisouData 積層SPSデータ
     * @param srRsussekData 積層RSUSデータ
     * @param srRhapsData 印刷積層RHAPS
     * @return 表示用データ
     */
    private List<GXHDO211CModel> getMergeData(List<GXHDO211CModel> mainData, List<Map<String, Object>> sekkeiData, List<Map<String, Object>> jokenDataSuisonoudo,
            List<Map<String, Object>> jokenDataPeakondo, List<Map<String, Object>> jokenDataTissonoudo, List<Map<String, Object>> fxhdd06Data,
            List<Map<String, Object>> srSpssekisouData, List<Map<String, Object>> srRsussekData, List<Map<String, Object>> srRhapsData) {

        List<GXHDO211CModel> resultData = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        for (GXHDO211CModel model : mainData) {

            String kojyo = model.getKojyo();
            String lotno = model.getLotno();
            String edaban = model.getEdaban();

            //設計データを取得
            Map<String, Object> sekkeiMap = sekkeiData.stream().filter(n -> kojyo.equals(StringUtil.nullToBlank(n.get("KOJYO")))).
                    filter(n -> lotno.equals(StringUtil.nullToBlank(n.get("LOTNO")))).findFirst().orElse(null);

            if (sekkeiMap == null) {
                continue;
            }

            //ﾛｯﾄNo(表示用)
            model.setLotnoView(kojyo + lotno + edaban);
            
            // 個数(フォーマット処理)
            BigDecimal kosu = new BigDecimal(StringUtil.nullToBlank(model.getKosu()));
            model.setKosu(decimalFormat.format(kosu));

            //受入日(表示用)
            if (model.getUkeirebi() != null) {
                model.setUkeirebiView(DateUtil.getDisplayDate(model.getUkeirebi(), DateUtil.YYYYMMDD_WITH_SLASH));
            }

            // 設計Noを元に条件データを取得しセットする。
            String sekkeino = StringUtil.nullToBlank(sekkeiMap.get("SEKKEINO"));

            // 窒素濃度
            Map<String, Object> jokenTissonoudoMap = jokenDataTissonoudo.stream().filter(n -> sekkeino.equals(StringUtil.nullToBlank(n.get("SEKKEINO")))).findFirst().orElse(null);
            if (jokenTissonoudoMap != null) {
                model.setTissonoudo(StringUtil.nullToBlank(jokenTissonoudoMap.get("KIKAKUCHI")));
            }

            // 指示温度ﾃﾞｰﾀ取得
            List<Map<String, Object>> fxhdd06MapList = fxhdd06Data.stream().filter(n -> kojyo.equals(StringUtil.nullToBlank(n.get("kojyo"))))
                    .filter(n -> lotno.equals(StringUtil.nullToBlank(n.get("lotno"))))
                    .filter(n -> edaban.equals(StringUtil.nullToBlank(n.get("edaban")))).collect(Collectors.toList());

            if (fxhdd06MapList.isEmpty()) {
                //指示温度データが無い場合、規格値をセット
                // 水素濃度1
                Map<String, Object> jokenSuisoondoMap = jokenDataSuisonoudo.stream().filter(n -> sekkeino.equals(StringUtil.nullToBlank(n.get("SEKKEINO")))).findFirst().orElse(null);
                if (jokenSuisoondoMap != null) {
                    model.setSuisonoudo1(StringUtil.nullToBlank(jokenSuisoondoMap.get("KIKAKUCHI")));
                }

                // ﾋﾟｰｸ温度1
                Map<String, Object> jokenPeakondMap = jokenDataPeakondo.stream().filter(n -> sekkeino.equals(StringUtil.nullToBlank(n.get("SEKKEINO")))).findFirst().orElse(null);
                if (jokenPeakondMap != null) {
                    model.setPeakondo1(StringUtil.nullToBlank(jokenPeakondMap.get("KIKAKUCHI")));
                }

            } else {
                //指示温度データがある場合はそのデータをセット
                String[] shijiOndoGropu1 = getShijiOndoGroupData(fxhdd06MapList, 1);
                if (!StringUtil.isEmpty(shijiOndoGropu1[0])) {
                    // 水素濃度1
                    model.setSuisonoudo1(shijiOndoGropu1[2]);
                    // ﾋﾟｰｸ温度1
                    model.setPeakondo1(shijiOndoGropu1[0] + ":" + shijiOndoGropu1[1]);

                }

                String[] shijiOndoGropu2 = getShijiOndoGroupData(fxhdd06MapList, 2);
                if (!StringUtil.isEmpty(shijiOndoGropu2[0])) {
                    // 水素濃度2
                    model.setSuisonoudo2(shijiOndoGropu2[2]);
                    // ﾋﾟｰｸ温度2
                    model.setPeakondo2(shijiOndoGropu2[0] + ":" + shijiOndoGropu2[1]);
                }

                String[] shijiOndoGropu3 = getShijiOndoGroupData(fxhdd06MapList, 3);
                if (!StringUtil.isEmpty(shijiOndoGropu3[0])) {
                    // 水素濃度3
                    model.setSuisonoudo3(shijiOndoGropu3[2]);
                    // ﾋﾟｰｸ温度3
                    model.setPeakondo3(shijiOndoGropu3[0] + ":" + shijiOndoGropu3[1]);
                }

                String[] shijiOndoGropu4 = getShijiOndoGroupData(fxhdd06MapList, 4);
                if (!StringUtil.isEmpty(shijiOndoGropu4[0])) {
                    // 水素濃度4
                    model.setSuisonoudo4(shijiOndoGropu4[2]);
                    // ﾋﾟｰｸ温度4
                    model.setPeakondo4(shijiOndoGropu4[0] + ":" + shijiOndoGropu4[1]);
                }

                String[] shijiOndoGropu5 = getShijiOndoGroupData(fxhdd06MapList, 5);
                if (!StringUtil.isEmpty(shijiOndoGropu5[0])) {
                    // 水素濃度5
                    model.setSuisonoudo5(shijiOndoGropu5[2]);
                    // ﾋﾟｰｸ温度5
                    model.setPeakondo5(shijiOndoGropu5[0] + ":" + shijiOndoGropu5[1]);
                }

            }

            // 積層SPSデータ取得
            Map<String, Object> srSpssekisouMap = srSpssekisouData.stream().filter(n -> kojyo.equals(StringUtil.nullToBlank(n.get("kojyo")))).
                    filter(n -> lotno.equals(StringUtil.nullToBlank(n.get("lotno")))).
                    filter(n -> edaban.equals(StringUtil.nullToBlank(n.get("edaban")))).findFirst().orElse(null);

            // 積層RSUSデータ取得
            Map<String, Object> srRsussekMap = srRsussekData.stream().filter(n -> kojyo.equals(StringUtil.nullToBlank(n.get("KOJYO")))).
                    filter(n -> lotno.equals(StringUtil.nullToBlank(n.get("LOTNO")))).
                    filter(n -> edaban.equals(StringUtil.nullToBlank(n.get("EDABAN")))).findFirst().orElse(null);

            // 印刷積層RHAPSデータ取得
            Map<String, Object> srRhapsMap = srRhapsData.stream().filter(n -> kojyo.equals(StringUtil.nullToBlank(n.get("KOJYO")))).
                    filter(n -> lotno.equals(StringUtil.nullToBlank(n.get("LOTNO")))).
                    filter(n -> edaban.equals(StringUtil.nullToBlank(n.get("EDABAN")))).findFirst().orElse(null);

            // ﾃｰﾌﾟﾛｯﾄNo
            if (srSpssekisouMap != null) {
                model.setTapelotNo(StringUtil.nullToBlank(srSpssekisouMap.get("tapelotno")));
            } else if (srRsussekMap != null) {
                model.setTapelotNo(StringUtil.nullToBlank(srRsussekMap.get("tapelotno")));
            } else if (srRhapsMap != null) {
                model.setTapelotNo(StringUtil.nullToBlank(srRhapsMap.get("ETAPELOT")));
            }

            // 特殊品の場合は品種をV8としてセットする。
            if (model.getKcpno().matches(TOKUSHU_HIN_KCPNO1) || model.getKcpno().matches(TOKUSHU_HIN_KCPNO2)) {
                model.setHinsyu("V8");
            }

            resultData.add(model);
        }

        return resultData;
    }

    /**
     * 指示温度データグループ別データ取得 ※グループ別にまとめて1レコードとしてデータを取得、号機情報については
     * 値がそれぞれ異なるため、結合して値を返す
     *
     * @param fxhdd06MapList 指示温度データリスト
     */
    private String[] getShijiOndoGroupData(List<Map<String, Object>> fxhdd06MapList, int getgroup) {
        String shijiOndo = "";
        String suisoNoudo = "";
        StringBuilder goukiJouhoGroup = new StringBuilder();
        int prevGoukiJouho = 0;
        int renbanCount = 0;
        for (Map<String, Object> mapData : fxhdd06MapList) {
            // 一致しないグループについてはコンティニュー
            if (getgroup != (int) mapData.get("shijiondogroup")) {
                continue;
            }

            int goukiJouho = (int) (mapData.get("goukijyoho"));

            // 初回データセット
            if (0 == goukiJouhoGroup.length()) {
                //指示温度
                if (mapData.get("shijiondo") != null) {
                    BigDecimal decShijiOndo = new BigDecimal(StringUtil.nullToBlank(mapData.get("shijiondo")));
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                    shijiOndo = decimalFormat.format(decShijiOndo);
                }

                //水素濃度
                if (mapData.get("suisonoudo") != null) {
                    BigDecimal decSuisoNoudo = new BigDecimal(StringUtil.nullToBlank(mapData.get("suisonoudo")));
                    DecimalFormat decimalFormat = new DecimalFormat("#0.000");
                    suisoNoudo = decimalFormat.format(decSuisoNoudo);
                }
                //号機情報
                goukiJouhoGroup.append(goukiJouho);
                prevGoukiJouho = goukiJouho;
                continue;
            }

            // 号機情報の前回値の差分を比較
            int sabun = goukiJouho - prevGoukiJouho;

            // 差分が1の場合
            if (sabun == 1) {
                // 連番が続いている場合
                renbanCount++;
            } else if (1 < sabun) {
                if (1 < renbanCount) {
                    // 3つ以上の連番が途切れた場合
                    //前回までの連番分を追加
                    goukiJouhoGroup.append("～");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                } else if (1 == renbanCount) {
                    // 1回の連番が途切れた場合
                    goukiJouhoGroup.append(",");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                }

                //今回の号機情報を追加
                goukiJouhoGroup.append(",");
                goukiJouhoGroup.append(StringUtil.nullToBlank(goukiJouho));
            }
            //今回の号機情報を前回値にセット
            prevGoukiJouho = goukiJouho;
        }

        if (1 < renbanCount) {
            // 3つ以上の連番が途切れた場合
            //前回までの連番分を追加
            goukiJouhoGroup.append("～");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        } else if (1 == renbanCount) {
            // 1回の連番が途切れた場合
            goukiJouhoGroup.append(",");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        }

        return new String[]{goukiJouhoGroup.toString(), shijiOndo, suisoNoudo};
    }

}
