/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo501;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO501AModel;
import jp.co.kccs.xhd.util.DBUtil;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.primefaces.model.UploadedFile;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.primefaces.context.RequestContext;
import java.sql.Connection;
import java.sql.Timestamp;
import jp.co.kccs.xhd.common.ResultMessage;
import jp.co.kccs.xhd.db.model.DaMkhyojunjoken;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	原材料規格管理機能(コンデンサ)<br>
 * <br>
 * 変更日	2021/07/27<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS gc<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD501A(原材料規格ｱｯﾌﾟﾛｰﾄﾞ機能)
 *
 * @author KCSS gc
 * @since 2021/07/27
 */
@Named
@SessionScoped
@ViewScoped
public class GXHDO501A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO501A.class.getName());

    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * 検索条件：規格
     */
    private String rKikaku = "";
    /**
     * 検索条件：種類
     */
    private String cmbSyurui = "";
    /**
     * 種類リスト:表示可能ﾃﾞｰﾀ
     */
    private String cmbSyuruiData[];
    /**
     * 検索条件：担当者
     */
    private String txtTantousya = "";
    /**
     * 検索条件：参照Excelパース
     */
    private String excelPath = "";
    /**
     * 取込ファイル
     */
    private UploadedFile file;

    /**
     * 警告メッセージ
     */
    private String warnMessage = "";
    /**
     * * ｴﾗｰ発生項目の背景色
     */
    private static final String ERROR_COLOR = "#FFB6C1";
    /**
     * * ｴﾗｰがない項目の背景色
     */
    private static final String NORMAL_COLOR = "#FFFFFF";
    /**
     * 検索条件：種類の背景色
     */
    private String cmbSyuruibgcolor = "";
    /**
     * 検索条件：担当者の背景色
     */
    private String txtTantousyabgcolor = "";
    /**
     * ﾌｧｲﾙ拡張子
     */
    private static final String EXCEL_XLSX = "xlsx";
    /**
     * ﾌｧｲﾙ拡張子
     */
    private static final String EXCEL_XLSM = "xlsm";
    /**
     * 格納できる値の上限
     */
    private static final int MAX_VALUE_99999 = 99999;
    /**
     * 取込ファイル名称 *
     */
    private String downLoadFileName = "";

    /**
     * コンストラクタ
     */
    public GXHDO501A() {
    }

    /**
     * 規格
     *
     * @return the rKikaku
     */
    public String getrKikaku() {
        return rKikaku;
    }

    /**
     * 規格
     *
     * @param rKikaku the rKikaku to set
     */
    public void setrKikaku(String rKikaku) {
        this.rKikaku = rKikaku;
    }

    /**
     * 種類
     *
     * @return the cmbSyurui
     */
    public String getCmbSyurui() {
        return cmbSyurui;
    }

    /**
     * 種類
     *
     * @param cmbSyurui the cmbSyurui to set
     */
    public void setCmbSyurui(String cmbSyurui) {
        this.cmbSyurui = cmbSyurui;
    }

    /**
     * 種類リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbSyuruiData
     */
    public String[] getCmbSyuruiData() {
        return cmbSyuruiData;
    }

    /**
     * 種類リスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbSyuruiData the cmbSyuruiData to set
     */
    public void setCmbSyuruiData(String[] cmbSyuruiData) {
        this.cmbSyuruiData = cmbSyuruiData;
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
     * 担当者
     *
     * @return the txtTantousya
     */
    public String getTxtTantousya() {
        return txtTantousya;
    }

    /**
     * 担当者
     *
     * @param txtTantousya the txtTantousya to set
     */
    public void setTxtTantousya(String txtTantousya) {
        this.txtTantousya = txtTantousya;
    }

    /**
     * 参照Excelﾊﾟｰｽ
     *
     * @return the excelPath
     */
    public String getExcelPath() {
        return excelPath;
    }

    /**
     * 参照Excelﾊﾟｰｽ
     *
     * @param excelPath the excelPath to set
     */
    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    /**
     * 検索条件：種類の背景色
     *
     * @return the cmbSyuruibgcolor
     */
    public String getCmbSyuruibgcolor() {
        return cmbSyuruibgcolor;
    }

    /**
     * 検索条件：種類の背景色
     *
     * @param cmbSyuruibgcolor the cmbSyuruibgcolor to set
     */
    public void setCmbSyuruibgcolor(String cmbSyuruibgcolor) {
        this.cmbSyuruibgcolor = cmbSyuruibgcolor;
    }
    
     /**
      * 検索条件：担当者の背景色
      * 
     * @return the txtTantousyabgcolor
     */
    public String getTxtTantousyabgcolor() {
        return txtTantousyabgcolor;
    }

    /**
     * 検索条件：担当者の背景色
     * 
     * @param txtTantousyabgcolor the txtTantousyabgcolor to set
     */
    public void setTxtTantousyabgcolor(String txtTantousyabgcolor) {
        this.txtTantousyabgcolor = txtTantousyabgcolor;
    }

    /**
     * 取込ファイル名称
     *
     * @return the downLoadFileName
     */
    public String getDownLoadFileName() {
        return downLoadFileName;
    }

    /**
     * 取込ファイル名称
     *
     * @param downLoadFileName the downLoadFileName to set
     */
    public void setDownLoadFileName(String downLoadFileName) {
        this.downLoadFileName = downLoadFileName;
    }

    /**
     * 取込ファイル
     *
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * 取込ファイル
     *
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
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
            } catch (IOException e) {
                ErrUtil.outputErrorLog("IOException発生", e, LOGGER);
            }
            return;
        }

        // 規格ラジオボタン設定
        rKikaku = "標準規格";
        // 種類コンボボックス設定
        cmbSyuruiData = new String[]{"ｶﾞﾗｽ作製", "ｶﾞﾗｽｽﾗﾘｰ作製", "添加剤ｽﾗﾘｰ作製", "誘電体ｽﾗﾘｰ作製", "ﾊﾞｲﾝﾀﾞｰ溶液作製", "ｽﾘｯﾌﾟ作製"};
    }

    /**
     * Excel取込前、ファイル存在チェック
     */
    public void showMsg() {
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000217"), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Excel取込
     */
    public void inputExcel() {
        // ファイルチェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        String fileName = getFile().getFileName();
        // 指定されたｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙが存在しません。
        if (StringUtil.isEmpty(fileName)) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000003", "Excel"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } else {
            // ﾌｧｲﾙ拡張子が正しくありません。
            if (!fileName.endsWith(EXCEL_XLSX) && !fileName.endsWith(EXCEL_XLSM)) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000208"), null);
                facesContext.addMessage(null, message);
                return;
            }
        }

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(getFile().getInputstream());
        } catch (IOException e) {
            ErrUtil.outputErrorLog("IOException発生", e, LOGGER);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excelエラー発生。", null);
            facesContext.addMessage(null, message);
            return;
        }
        // Excel対象を取得 
        Sheet sheet = workbook.getSheetAt(0);
        setTxtTantousyabgcolor(NORMAL_COLOR) ;
        // 担当者桁数チェック
        if (existError(validateUtil.checkC103(getTxtTantousya(), "担当者", 6))) {
            setTxtTantousyabgcolor(ERROR_COLOR);
            return;
        }
        String paramTxtTantousya = StringUtil.nullToBlank(getTxtTantousya());
        //担当者ﾃﾞｰﾀ存在チェック
        if (!"".equals(paramTxtTantousya) && selectTxtTantousyaData() == 0) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "担当者"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            setTxtTantousyabgcolor(ERROR_COLOR);
            return;
        }

        // 種類
        String fvsyurui = getCellValue(sheet.getRow(1), 1);
        // 種類チェック
        if (!inputSyuruiCheck(fvsyurui, "種類")) {
            return;
        }
        HashMap<String, Integer> resultMap = new HashMap<>();
        List<String> resultMessageList = new ArrayList<>();
        setDownLoadFileName(fileName);
        // A.NG数(変数)を初期化する
        resultMap.put("ngCount", 0);
        List<GXHDO501AModel> loadDataList;
        if ("標準規格".equals(getrKikaku())) {
            // 標準規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙのﾌｫｰﾏｯﾄﾁｪｯｸ
            if (!loadHyoJunFileFormat(sheet)) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000216", "規格"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            loadDataList = doHyojunfiletorikomisyori(sheet, resultMap, resultMessageList, fvsyurui);
            saveTorikomiFile(loadDataList, getFile(), "0");
        } else {
            // ﾛｯﾄ規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙのﾌｫｰﾏｯﾄﾁｪｯｸ
            if (!loadLotFileFormat(sheet)) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000216", "規格"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            loadDataList = doLotfiletorikomisyori(sheet, resultMap, resultMessageList, fvsyurui);
            saveTorikomiFile(loadDataList, getFile(), "1");
        }
        
        showResultMsg(resultMessageList);
    }

    /**
     * R.標準規格情報登録処理
     *
     * @param sheet 取込データ
     * @param resultMap チェック結果の格納
     * @param resultMessageList メッセージリスト
     * @param fvsyurui 種類
     * @return 以降処理のステップ
     */
    private List<GXHDO501AModel> doHyojunfiletorikomisyori(Sheet sheet, HashMap<String, Integer> resultMap, List<String> resultMessageList, String fvsyurui) {
        // B選択された規格が「標準規格」の場合、ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙより登録ﾃﾞｰﾀを取得
        List<GXHDO501AModel> loadHyoJunDataList = loadHyoJunDataFromFile(sheet);
        resultMap.put("kikakuCount", 0);
        loadHyoJunDataList.forEach((gxhdo501aModel) -> {
            // D.取込件数(変数)を初期化する
            resultMap.put("rowDataCount", 0);
            // 毎行のチェック処理
            String rtnStep = checkHyoJunRowData(gxhdo501aModel, resultMap);
            if ("L".equals(rtnStep)) {
                int rowDataCount = resultMap.get("rowDataCount");
                if (rowDataCount == 0) {
                    gxhdo501aModel.setResulta("NG");
                    gxhdo501aModel.setResultb("取込件数が0件です。");
                    // NG数+1
                    resultMap.put("ngCount", resultMap.get("ngCount") + 1);
                } else {
                    // M.結果欄の入力
                    gxhdo501aModel.setResulta("OK");
                }
            }
        });
        Connection conDoc = null;
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        Connection conQcdb = null;
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
        try {
            // トランザクション開始
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            // N.規格取込履歴登録処理
            String stepFlg = doTorikomiRirekiSyori(resultMap, resultMessageList, fvsyurui, conDoc, queryRunnerDoc);

            if ("U".equals(stepFlg)) {
                return loadHyoJunDataList;
            } else if ("T".equals(stepFlg)) {
                // T.画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
                resultMessageList.clear();
                resultMessageList.add("取込NGは" + resultMap.get("ngCount") + "件です。");
                DbUtils.commitAndCloseQuietly(conDoc);
                DbUtils.commitAndCloseQuietly(conQcdb);
                return loadHyoJunDataList;
            }

            // L.標準規格情報登録処理
            updDaMkhyojunjokenData(resultMap, conQcdb, queryRunnerQcdb, loadHyoJunDataList);

            // M.登録件数ﾁｪｯｸ
            int addCount = resultMap.get("addCount");
            resultMessageList.clear();
            if (addCount == 0) {
                resultMessageList.add(MessageUtil.getMessage("XHD-000214", "登録件数"));
            } else {
                resultMessageList.add("標準規格情報を" + resultMap.get("delCount") + "件削除しました。");
                resultMessageList.add("標準規格情報を" + resultMap.get("addCount") + "件登録しました。");
            }

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);
        } catch (SQLException e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
        } catch (IOException e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            ErrUtil.outputErrorLog("IOException発生", e, LOGGER);
        }
        return loadHyoJunDataList;
    }

    /**
     * R.ﾛｯﾄ規格情報登録処理
     *
     * @param sheet 取込データ
     * @param resultMap チェック結果の格納
     * @param resultMessageList メッセージリスト
     * @param fvsyurui 種類
     * @return 以降処理のステップ
     */
    private List<GXHDO501AModel> doLotfiletorikomisyori(Sheet sheet, HashMap<String, Integer> resultMap, List<String> resultMessageList, String fvsyurui) {
        // 選択された規格が「ﾛｯﾄ規格」の場合、ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙより登録ﾃﾞｰﾀを取得
        List<GXHDO501AModel> loadLotDataList = loadLotDataFromFile(sheet);
        resultMap.put("kikakuCount", 0);
        loadLotDataList.forEach((gxhdo501aModel) -> {
            // C.取込件数(変数)を初期化する
            resultMap.put("rowDataCount", 0);
            // 毎行のチェック処理
            String rtnStep = checkRowData(gxhdo501aModel, resultMap);
            if ("L".equals(rtnStep)) {
                int rowDataCount = resultMap.get("rowDataCount");
                if (rowDataCount == 0) {
                    gxhdo501aModel.setResulta("NG");
                    gxhdo501aModel.setResultb("取込件数が0件です。");
                    // NG数+1
                    resultMap.put("ngCount", resultMap.get("ngCount") + 1);
                } else {
                    // M.結果欄の入力
                    gxhdo501aModel.setResulta("OK");
                }
            }
        });

        Connection conDoc = null;
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        Connection conQcdb = null;
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
        try {
            // トランザクション開始
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            // N.規格取込履歴登録処理
            String stepFlg = doTorikomiRirekiSyori(resultMap, resultMessageList, fvsyurui, conDoc, queryRunnerDoc);
            if ("U".equals(stepFlg)) {
                return loadLotDataList;
            } else if ("T".equals(stepFlg)) {
                // T.画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
                resultMessageList.clear();
                resultMessageList.add("取込NGは" + resultMap.get("ngCount") + "件です。");
                DbUtils.commitAndCloseQuietly(conDoc);
                DbUtils.commitAndCloseQuietly(conQcdb);
                return loadLotDataList;
            }
            // O.品名採番
            doHinnmeiSaibann(conDoc, queryRunnerDoc, loadLotDataList);
            // P.前工程WIPへの引数作成
            doStepP(loadLotDataList);
            // Q.[製造LotNo]を作成:API[前工程WIP]を呼び出す TODO QA24

            // R.ﾛｯﾄ規格情報登録処理
            stepFlg = doLotKikakuInfoTorokuSyori(resultMap, resultMessageList, fvsyurui, conQcdb, queryRunnerQcdb, loadLotDataList);
            if ("U".equals(stepFlg)) {
                return loadLotDataList;
            }
            // S.登録件数ﾁｪｯｸ
            int kikakuCount = resultMap.get("kikakuCount");
            resultMessageList.clear();
            if (kikakuCount == 0) {
                resultMessageList.add(MessageUtil.getMessage("XHD-000214", "登録件数"));
            } else {
                resultMessageList.add("設計ﾃﾞｰﾀを" + resultMap.get("sekkeiCount") + "件登録しました。");
                resultMessageList.add("規格情報を" + resultMap.get("kikakuCount") + "件登録しました。");
            }
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);
        } catch (SQLException e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
        } catch (IOException e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            ErrUtil.outputErrorLog("IOException発生", e, LOGGER);
        }
        return loadLotDataList;
    }

    /**
     * T.画面に以下のﾎﾟｯﾌﾟｱｯﾌﾟﾒｯｾｰｼﾞを表示する。
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
        RequestContext.getCurrentInstance().execute("cmdButtonOncomplete('resultMessage', '" + getDownLoadFileName() + "');");
    }

    /**
     * R.ﾛｯﾄ規格情報登録処理
     *
     * @param resultMap チェック結果の格納
     * @param resultMessageList メッセージリスト
     * @param fvsyurui 種類
     * @param conDoc コネクション
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param loadLotDataList 取込データ
     * @return 以降処理のステップ
     */
    private String doLotKikakuInfoTorokuSyori(HashMap<String, Integer> resultMap, List<String> resultMessageList, String fvsyurui,
            Connection conQcdb, QueryRunner queryRunnerQcdb, List<GXHDO501AModel> loadLotDataList) throws SQLException, IOException {
        String stepFlg = "";
        // ｱ.製造LotNo(変数)を初期化する
        String lotNoP = "";
        // 設計ﾃﾞｰﾀへ登録件数
        int sekkeiCount = 0;
        // 規格ﾃﾞｰﾀへ登録件数
        int kikakuCount = 0;
        // (ｱ).設計Noを取得
        long maxSekkeino = getMaxSekkeino();
        // 規格値
        String dkikakuti;
        for (GXHDO501AModel gxhdo501aModel : loadLotDataList) {
            String lotNo = StringUtil.blankToNull(gxhdo501aModel.getLotno());
            List<DaMkhyojunjoken> rowDataList = gxhdo501aModel.getRowdata();
            for (DaMkhyojunjoken data : rowDataList) {
                // 規格値
                dkikakuti = StringUtil.nullToBlank(data.getKikakuti());
                if ("-".equals(dkikakuti)) {
                    continue;
                }
                // ｸ.[製造LotNo]を製造LotNo(変数)と比較する。
                if (!lotNoP.equals(lotNo)) {
                    lotNoP = lotNo;
                    // (A).異なる場合
                    if (maxSekkeino >= Integer.MAX_VALUE) {
                        resultMessageList.clear();
                        resultMessageList.add(MessageUtil.getMessage("XHD-000211", "設計No"));
                        stepFlg = "U";
                        break;
                    }
                    maxSekkeino++;
                    // (ｲ).設計ﾃﾞｰﾀにﾃﾞｰﾀ登録を行う
                    sekkeiCount++;
                    insertDaMksekkei(conQcdb, queryRunnerQcdb, fvsyurui, maxSekkeino, gxhdo501aModel);
                    // (ｳ).規格情報にﾃﾞｰﾀ登録を行う
                    insertDaMkjoken(conQcdb, queryRunnerQcdb, maxSekkeino, data);
                    kikakuCount++;
                } else {
                    // (ｱ).規格情報にﾃﾞｰﾀ登録を行う
                    insertDaMkjoken(conQcdb, queryRunnerQcdb, maxSekkeino, data);
                    kikakuCount++;
                }
            }
            if ("U".equals(stepFlg)) {
                break;
            }
            resultMap.put("sekkeiCount", sekkeiCount);
            resultMap.put("kikakuCount", kikakuCount);
        }

        return stepFlg;
    }

    /**
     * P.前工程WIPへの引数作成
     *
     * @param loadLotDataList 取込データ
     * @return 前工程WIPへのJSON引数
     */
    private String doStepP(List<GXHDO501AModel> loadLotDataList) throws SQLException, IOException {
        String jsonStr = "[";
        int index = 0;
        for (GXHDO501AModel gxhdo501aModel : loadLotDataList) {
            jsonStr += "{";
            String kouteimei = StringUtil.blankToNull(gxhdo501aModel.getKouteimei());
            jsonStr += "\"kouteimei\":" + "\"" + kouteimei + "\", ";
            String hinmei = StringUtil.blankToNull(gxhdo501aModel.getHinmei());
            jsonStr += "\"hinmei\":" + "\"" + hinmei + "\", ";
            String jyuuryou = StringUtil.blankToNull(gxhdo501aModel.getJyuuryou());
            jsonStr += "\"jyuuryou\":" + jyuuryou+ ", ";
            String lotNo = StringUtil.blankToNull(gxhdo501aModel.getLotno());
            jsonStr += "\"lotNo\":" + "\"" + lotNo + "\", ";
            String owner = StringUtil.blankToNull(gxhdo501aModel.getOwner());
            jsonStr += "\"owner\":" + "\"" + owner + "\"";
            jsonStr += "}";
            if (index != loadLotDataList.size() - 1) {
                jsonStr += ",";
            }
            index++;
        }
        jsonStr += "]";
        return jsonStr;
    }

    /**
     * O.品名採番
     *
     * @param conDoc コネクション
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param loadLotDataList 取込データ
     */
    private void doHinnmeiSaibann(Connection conDoc, QueryRunner queryRunnerDoc, List<GXHDO501AModel> loadLotDataList) throws SQLException, IOException {
        String hinmei;
        HashMap<String, Integer> resultMap;
        int saibanno;
        int hasRecordFlg;
        HashMap<String, Integer> hinmeiSaibannoMap = new HashMap<>();
        for (GXHDO501AModel gxhdo501aModel : loadLotDataList) {
            hinmei = StringUtil.blankToNull(gxhdo501aModel.getHinmei());
            // (3)[採番ﾏｽﾀ]から、ﾃﾞｰﾀを取得
            if (!hinmeiSaibannoMap.containsKey(hinmei)) {
                resultMap = getSaibanno(hinmei);
                saibanno = resultMap.get("saibanno");
                if (saibanno >= MAX_VALUE_99999) {
                    saibanno = 0;
                }
                hasRecordFlg = resultMap.get("hasRecordFlg");
                hinmeiSaibannoMap.put(hinmei, saibanno + 1);
            }else{
                saibanno = hinmeiSaibannoMap.get(hinmei);
                if (saibanno >= MAX_VALUE_99999) {
                    saibanno = 0;
                }
                hinmeiSaibannoMap.put(hinmei, saibanno + 1);
                hasRecordFlg = 1;
            }

            saibanno++;
            hinmei = hinmei + String.format("%05d", saibanno);
            // (ｴ).(ｳ)で採番した[品名]でExcelの[品名]を更新する
            gxhdo501aModel.setHinmeisaiban(hinmei);
            // (ｵ).採番マスタにﾃﾞｰﾀ登録を行う
            if (hasRecordFlg == 0) {
                insertfxhdm10(conDoc, queryRunnerDoc, saibanno, gxhdo501aModel);
            } else {
                updatefxhdm10(conDoc, queryRunnerDoc, saibanno, gxhdo501aModel);
            }
        }
    }

    /**
     * N.規格取込履歴登録処理
     *
     * @param resultMap チェック結果の格納
     * @param resultMessageList メッセージリスト
     * @param fvsyurui 種類
     * @param conDoc コネクション
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param loadLotDataList 取込データ
     * @return 以降処理のステップ
     */
    private String doTorikomiRirekiSyori(HashMap<String, Integer> resultMap, List<String> resultMessageList, String fvsyurui,
            Connection conDoc, QueryRunner queryRunnerDoc) throws SQLException, IOException {
        String stepFlg = "";
        int ngCount = resultMap.get("ngCount");
        // N.規格取込履歴登録処理
        if (ngCount >= 1) {
            //ｱ.NG数(変数)が、1以上の場合
            // (A).取込Noを取得
            int maxTorikomiNo = getMaxTorikomiNo();
            // 取得したﾃﾞｰﾀが、格納できる値の上限の場合、ｴﾗｰ。Oの処理を実行する
            if (maxTorikomiNo >= Integer.MAX_VALUE) {
                resultMessageList.clear();
                resultMessageList.add(MessageUtil.getMessage("XHD-000211", "取込No"));
                return "U";
            }
            maxTorikomiNo += 1;
            // (B).規格取込履歴にﾃﾞｰﾀ登録を行う
            insertFxhdd10(queryRunnerDoc, conDoc, maxTorikomiNo, ngCount, fvsyurui);

            // (C).Excel格納
            setDownLoadFileName(String.valueOf(maxTorikomiNo) + getDownLoadFileName().substring(getDownLoadFileName().indexOf(".")));
            return "T";
        }
        return stepFlg;
    }

    /**
     * 取込ファイルをサーバに格納して、ダウンロードする
     *
     * @param loadLotDataList 取込データ
     * @param file 取込ファイル
     * @param fileType 取込ファイル種類
     */
    private void saveTorikomiFile(List<GXHDO501AModel> loadLotDataList, UploadedFile file, String fileType) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");
            String filePath = myParam.getString("download_temp");
            if (!filePath.endsWith(File.separator)) {
                filePath += File.separator;
            }
            doMkdir(filePath);
            filePath += getDownLoadFileName();
            File outputFile = new File(filePath);
            Workbook workbookCp = WorkbookFactory.create(file.getInputstream());

            // Excel対象を取得 
            Sheet sheet = workbookCp.getSheetAt(0);
            // 総行数
            int endRowCount = sheet.getLastRowNum();
            GXHDO501AModel gxhdo501aModel;
            for (int i = 8; i <= endRowCount; i++) {
                Row row = sheet.getRow(i);
                gxhdo501aModel = loadLotDataList.get(i - 8);
                row.getCell(0).setCellValue(gxhdo501aModel.getResulta());
                row.getCell(1).setCellValue(gxhdo501aModel.getResultb());
                if ("1".equals(fileType)) {
                    row.getCell(4).setCellValue(gxhdo501aModel.getHinmeisaiban());
                }
            }
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbookCp.write(fos);
                fos.flush();
            }

        } catch (IOException ex) {
            ErrUtil.outputErrorLog("取込ﾌｧｲﾙをサーバに格納失敗", ex, LOGGER);
        }
    }

    /**
     * 取込ファイルをサーバに格納して、ダウンロードする
     *
     */
    public void downloadTorikomiFile() {
        File outputFile = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");
            String filePath = myParam.getString("download_temp");
            if (!filePath.endsWith(File.separator)) {
                filePath += File.separator;
            }
            String downLoadFileNamePreffix = downLoadFileName.substring(0, downLoadFileName.indexOf("."));
            // 取り込んだExcelファイルの拡張子を取得
            String downLoadFileNameSuffix = downLoadFileName.substring(downLoadFileName.indexOf("."));
            outputFile = new File(filePath + downLoadFileName);
            // ダウンロードファイル名
            String downloadFileName = downLoadFileNamePreffix + "_" + ((new java.text.SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + downLoadFileNameSuffix;

            // outputstreamにファイルを転送
            ec.responseReset();
            ec.setResponseContentType("application/octet-stream");
            ec.setResponseHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(downloadFileName, "UTF-8") + "\"");
            try (OutputStream os = ec.getResponseOutputStream()) {
                output(outputFile, os);
                os.flush();
                ec.responseFlushBuffer();
            }

            // サーバの物理ファイルを削除
            // ※削除失敗時も処理継続
            try {
                outputFile.delete();
            } catch (Exception e) {
                ErrUtil.outputErrorLog("物理ファイルはサーバから削除失敗", e, LOGGER);
            }

            fc.responseComplete();
        } catch (IOException ex) {
            ErrUtil.outputErrorLog("取込ﾌｧｲﾙをﾀﾞｳﾝﾛｰﾄﾞに失敗", ex, LOGGER);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "取込ﾌｧｲﾙﾀﾞｳﾝﾛｰﾄﾞに失敗しました。", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            // 物理ファイルが削除されていない場合削除する
            if (outputFile != null && outputFile.exists()) {
                try {
                    outputFile.delete();
                } catch (Exception e) {
                    ErrUtil.outputErrorLog("物理ファイルはサーバから削除失敗", e, LOGGER);
                }
            }
        }
    }

    /**
     * サーバに格納ファイルパス作成
     *
     * @param path 格納ファイルパス
     */
    private void doMkdir(String path) {
        File fileBackup = new File(path);
        if (!fileBackup.exists()) {
            fileBackup.mkdirs();
        }
    }

    /**
     * ﾛｯﾄ規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙの毎行のチェック処理
     *
     * @param gxhdo501aModel 取込データ
     * @param resultMap チェック結果の格納
     */
    private String checkRowData(GXHDO501AModel gxhdo501aModel, HashMap<String, Integer> resultMap) {
        String rtnStep = "L";
        // D.[工程]ﾁｪｯｸ処理
        String kouteimei = StringUtil.nullToBlank(gxhdo501aModel.getKouteimei());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, kouteimei, "工程", 6, resultMap)) {
            rtnStep = "C";
            return rtnStep;
        }
        // E.[品名]ﾁｪｯｸ処理
        String hinmei = StringUtil.nullToBlank(gxhdo501aModel.getHinmei());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, hinmei, "品名", 8, resultMap)) {
            rtnStep = "C";
            return rtnStep;
        }

        // F.[重量]ﾁｪｯｸ処理
        String jyuuryou = StringUtil.nullToBlank(gxhdo501aModel.getJyuuryou());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, jyuuryou, "重量", 10, resultMap, "1")) {
            rtnStep = "C";
            return rtnStep;
        }

        // G.[ﾛｯﾄ区分]ﾁｪｯｸ処理
        String lotkubunn = StringUtil.nullToBlank(gxhdo501aModel.getLotkubunn());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, lotkubunn, "ﾛｯﾄ区分", 4, resultMap)) {
            rtnStep = "C";
            return rtnStep;
        }

        // H.[ｵｰﾅｰ]ﾁｪｯｸ処理
        String owner = StringUtil.nullToBlank(gxhdo501aModel.getOwner());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, owner, "ｵｰﾅｰ", 4, resultMap)) {
            rtnStep = "C";
            return rtnStep;
        }

        // I.[ﾊﾟﾀｰﾝ]ﾁｪｯｸ処理
        String pattern = StringUtil.nullToBlank(gxhdo501aModel.getPatterns());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, pattern, "ﾊﾟﾀｰﾝ", 11, resultMap, "1")) {
            rtnStep = "C";
            return rtnStep;
        }

        List<DaMkhyojunjoken> rowDataList = gxhdo501aModel.getRowdata();
        // 工程名
        String dkouteimei;
        // 項目名
        String dkoumokumei;
        // 管理項目名
        String dkanrikoumokumei;
        // ﾁｪｯｸﾊﾟﾀｰﾝ
        String dtyekkupattern;
        // 規格値
        String dkikakuti;
        for (DaMkhyojunjoken data : rowDataList) {
            // 工程名
            dkouteimei = StringUtil.nullToBlank(data.getKouteimei());
            // 項目名
            dkoumokumei = StringUtil.nullToBlank(data.getKoumokumei());
            // 管理項目名
            dkanrikoumokumei = StringUtil.nullToBlank(data.getKanrikoumokumei());
            // ﾁｪｯｸﾊﾟﾀｰﾝ
            dtyekkupattern = StringUtil.nullToBlank(data.getTyekkupattern());
            // 規格値
            dkikakuti = StringUtil.nullToBlank(data.getKikakuti());
            // [工程名]、[項目名]、[管理項目名]、[ﾁｪｯｸﾊﾟﾀｰﾝ]、[規格値]のどれか1項目でも取得できた場合　ﾁｪｯｸする。
            if (StringUtil.isEmpty(dkouteimei) && StringUtil.isEmpty(dkoumokumei) && StringUtil.isEmpty(dkanrikoumokumei)
                    && StringUtil.isEmpty(dtyekkupattern) && StringUtil.isEmpty(dkikakuti)) {
                rtnStep = "C";
                continue;
            }
            // ﾁｪｯｸ処理
            if (check003And006(gxhdo501aModel, dkouteimei, "工程名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dkoumokumei, "項目名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dkanrikoumokumei, "管理項目名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dtyekkupattern, "ﾁｪｯｸﾊﾟﾀｰﾝ", 3, resultMap)) {
                rtnStep = "C";
                break;
            }
            // ﾁｪｯｸﾊﾟﾀｰﾝが「±、～、≧、≦、MAX、MIN、＝、-」の内容以外の場合ｴﾗｰ
            if (checkChkPattern(gxhdo501aModel, dtyekkupattern, "ﾁｪｯｸﾊﾟﾀｰﾝ", resultMap)) {
                rtnStep = "C";
                break;
            }
            if (!"-".equals(dkikakuti)) {
                // (A).取込件数(変数) + 1　を行う
                resultMap.put("rowDataCount", resultMap.get("rowDataCount") + 1);
                if (check003And006(gxhdo501aModel, dkikakuti, "規格値", 20, resultMap)) {
                    rtnStep = "C";
                    break;
                }

                // 規格値 チェック：ﾌｫｰﾏｯﾄがﾁｪｯｸﾊﾟﾀｰﾝと一致しない場合ｴﾗｰ。
                if ("-1".equals(chkTyekkupatternError(dkikakuti, dtyekkupattern))) {
                    // NG数+1
                    resultMap.put("ngCount", resultMap.get("ngCount") + 1);
                    gxhdo501aModel.setResulta("NG");
                    gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000213", "規格値", dtyekkupattern));
                    rtnStep = "C";
                    break;
                }
            }
        }
        return rtnStep;
    }

    /**
     * ﾛｯﾄ規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙの毎行のチェック処理
     *
     * @param gxhdo501aModel 取込データ
     * @param checkVal 項目値
     * @param itemName 項目名称
     * @param maxLen 最大サイズ
     * @param resultMap チェック結果
     * @param numberCheckFlg 数値項目の型チェックフラグ
     * @return エラーが存在する場合true
     */
    private boolean check003And006(GXHDO501AModel gxhdo501aModel, String checkVal, String itemName, int maxLen,
            HashMap<String, Integer> resultMap, String... numberCheckFlg) {
        // 半角・全角ｽﾍﾟｰｽを削除し、1文字以上の入力がない場合ｴﾗｰ
        if (!inputCheck003(gxhdo501aModel, checkVal, itemName)) {
            resultMap.put("ngCount", resultMap.get("ngCount") + 1);
            return true;
        }
        // 文字数及び、数値(整数部のみ)の桁数が指定桁数以内ではない場合ｴﾗｰ。
        if (!inputCheck006(gxhdo501aModel, checkVal, itemName, maxLen, numberCheckFlg)) {
            resultMap.put("ngCount", resultMap.get("ngCount") + 1);
            return true;
        }
        return false;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝが「±、～、≧、≦、MAX、MIN、＝、-」の内容以外の場合ｴﾗｰ
     *
     * @param gxhdo501aModel 取込データ
     * @param checkVal 項目値
     * @param itemName 項目名称
     * @param resultMap チェック結果
     * @return エラーが存在する場合true
     */
    private boolean checkChkPattern(GXHDO501AModel gxhdo501aModel, String checkVal, String itemName, HashMap<String, Integer> resultMap) {
        // ﾁｪｯｸﾊﾟﾀｰﾝが「±、～、≧、≦、MAX、MIN、＝、-」の内容以外の場合ｴﾗｰ
        List<String> chkPatternList = Arrays.asList("±", "～", "≧", "≦", "MAX", "MIN", "=", "-");
        if (!chkPatternList.contains(checkVal)) {
            gxhdo501aModel.setResulta("NG");
            gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000011", itemName));
            resultMap.put("ngCount", resultMap.get("ngCount") + 1);
            return true;
        }
        
        return false;
    }
    
    /**
     * 取込ﾌｧｲﾙから項目を取得
     *
     * @param row 項目がある行
     * @param cellIndex 項目があるセールIndex
     * @return 項目値
     */
    private String getCellValue(Row row, int cellIndex) {
        if (row == null || row.getCell(cellIndex) == null) {
            return "";
        }
        return row.getCell(cellIndex).getStringCellValue();
    }

    /**
     * ﾛｯﾄ規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙのﾌｫｰﾏｯﾄﾁｪｯｸ
     *
     * @param sheet 取込ファイル
     * @return エラーが存在する場合false
     */
    private boolean loadLotFileFormat(Sheet sheet) {
        // 種類
        String fhsyurui = getCellValue(sheet.getRow(1), 0);
        // 結果
        String fhresultA = getCellValue(sheet.getRow(4), 0);
        // 結果
        String fhresultB = getCellValue(sheet.getRow(4), 1);
        // 製造LotNo
        String fhlotNo = getCellValue(sheet.getRow(4), 2);
        // 工程
        String fhkoutei = getCellValue(sheet.getRow(4), 3);
        // 品名
        String fhhinmei = getCellValue(sheet.getRow(4), 4);
        // 重量
        String fhjyuuryou = getCellValue(sheet.getRow(4), 5);
        // ﾛｯﾄ区分
        String fhlotkubunn = getCellValue(sheet.getRow(4), 6);
        // ｵｰﾅｰ
        String fhowner = getCellValue(sheet.getRow(4), 7);
        // ﾊﾟﾀｰﾝ
        String fhtyekkupattern = getCellValue(sheet.getRow(4), 8);

        if (!("種類".equals(fhsyurui) && "結果".equals(fhresultA) && "結果".equals(fhresultB) && "製造LotNo".equals(fhlotNo) && "工程".equals(fhkoutei)
                && "品名".equals(fhhinmei) && "重量".equals(fhjyuuryou) && "ﾛｯﾄ区分".equals(fhlotkubunn)
                && "ｵｰﾅｰ".equals(fhowner) && "ﾊﾟﾀｰﾝ".equals(fhtyekkupattern))) {
            return false;
        }

        // 取込ファイル行目のサーズチェック
        return checkRowLength(sheet);
    }

    /**
     * 取込ファイル行目のサーズを取得
     *
     * @param rowLengthList 行目のサーズリスト
     * @param len 行目のサーズ
     */
    private void addList(List<Integer> rowLengthList, Integer len) {
        if (!rowLengthList.contains(len)) {
            rowLengthList.add(len);
        }
    }

    /**
     * 取込ファイル行目のサーズチェック
     *
     * @param sheet 取込ファイル
     * @return エラーが存在する場合false
     */
    private boolean checkRowLength(Sheet sheet) {
        Row row4 = sheet.getRow(4);
        Row row5 = sheet.getRow(5);
        Row row6 = sheet.getRow(6);
        Row row7 = sheet.getRow(7);
        if (row4 == null || row5 == null || row6 == null || row7 == null) {
            return false;
        }
        List<Integer> rowLengthList = new ArrayList<>();
        addList(rowLengthList, ((Short) row4.getLastCellNum()).intValue());
        addList(rowLengthList, ((Short) row5.getLastCellNum()).intValue());
        addList(rowLengthList, ((Short) row6.getLastCellNum()).intValue());
        addList(rowLengthList, ((Short) row7.getLastCellNum()).intValue());
        // ヘッダーlength
        int headRowLen = 7;
        // 総行数
        int detailRowCount = sheet.getLastRowNum() - headRowLen;
        Row row;
        // 毎行総列数
        int colMaxNum;
        // データを取込開始
        for (int i = 1; i <= detailRowCount; i++) {
            row = sheet.getRow(headRowLen + i);
            colMaxNum = row.getLastCellNum();
            addList(rowLengthList, colMaxNum);
        }
        return rowLengthList.size() == 1;
    }

    /**
     * 選択された規格が「ﾛｯﾄ規格」の場合、ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙより登録ﾃﾞｰﾀを取得
     *
     * @param sheet 取込ファイル
     * @return 取込ﾃﾞｰﾀ
     */
    private List<GXHDO501AModel> loadLotDataFromFile(Sheet sheet) {
        List<GXHDO501AModel> loadDataList = new ArrayList<>();
        // 種類
        String fvsyurui = getCellValue(sheet.getRow(1), 1);
        // ヘッダーlength
        int headRowLen = 7;
        // 総行数
        int detailRowCount = sheet.getLastRowNum() - headRowLen;
        Row row4 = sheet.getRow(4);
        Row row5 = sheet.getRow(5);
        Row row6 = sheet.getRow(6);
        Row row7 = sheet.getRow(7);
        List<Row> headerRowList = Arrays.asList(row4, row5, row6, row7);
        int cellIndex;
        
        // データを取込開始
        for (int i = 1; i <= detailRowCount; i++) {
            Row row = sheet.getRow(headRowLen + i);
            // 毎行総列数 
            int colMaxNum = row.getLastCellNum();
            for (int j = 0; j < colMaxNum; j++) {
                if (row.getCell(j) != null && !CellType.STRING.equals(row.getCell(j).getCellType())) {
                    row.getCell(j).setCellType(CellType.STRING);
                }
            }
            GXHDO501AModel gxhdO501AModel = new GXHDO501AModel();
            // 種類
            gxhdO501AModel.setSyurui(fvsyurui);
            // 結果
            gxhdO501AModel.setResulta(getCellValue(row, 0));
            cellIndex = 2;
            // 製造LotNo
            gxhdO501AModel.setLotno(getCellValue(row, cellIndex));
            // 工程
            gxhdO501AModel.setKouteimei(getCellValue(row, cellIndex + 1));
            // 品名
            gxhdO501AModel.setHinmei(getCellValue(row, cellIndex + 2));
            gxhdO501AModel.setHinmeisaiban(getCellValue(row, cellIndex + 2));
            // 重量
            gxhdO501AModel.setJyuuryou(getCellValue(row, cellIndex + 3));
            // ﾛｯﾄ区分
            gxhdO501AModel.setLotkubunn(getCellValue(row, cellIndex + 4));
            // ｵｰﾅｰ
            gxhdO501AModel.setOwner(getCellValue(row, cellIndex + 5));
            // ﾊﾟﾀｰﾝ
            gxhdO501AModel.setPatterns(getCellValue(row, cellIndex + 6));

            List<DaMkhyojunjoken> rowDataList = new ArrayList<>();
            for (int j = 9; j < colMaxNum; j++) {
                DaMkhyojunjoken daMkhyojunjoken = new DaMkhyojunjoken();
                for (Row rowItem : headerRowList) {
                    if (rowItem.getCell(j) != null && !CellType.STRING.equals(rowItem.getCell(j).getCellType())) {
                        rowItem.getCell(j).setCellType(CellType.STRING);
                    }
                }
                // 工程名
                daMkhyojunjoken.setKouteimei(getCellValue(row4, j));
                // 項目名
                daMkhyojunjoken.setKoumokumei(getCellValue(row5, j));
                // 管理項目名
                daMkhyojunjoken.setKanrikoumokumei(getCellValue(row6, j));
                // ﾁｪｯｸﾊﾟﾀｰﾝ
                daMkhyojunjoken.setTyekkupattern(getCellValue(row7, j));
                // 規格値
                daMkhyojunjoken.setKikakuti(getCellValue(row, j));
                rowDataList.add(daMkhyojunjoken);
            }
            // 毎行の取込データ
            gxhdO501AModel.setRowdata(rowDataList);
            loadDataList.add(gxhdO501AModel);
        }
        return loadDataList;
    }

    /**
     * 規格値 チェック詳細
     *
     * @param kikakuti 規格値
     * @param tyekkupattern ﾁｪｯｸﾊﾟﾀｰﾝ
     * @return エラーが存在する場合false
     */
    private String chkTyekkupatternError(String strKikakuti, String strTyekkupattern) {
        //半角・全角ｽﾍﾟｰｽを削除
        strKikakuti =strKikakuti.replace((char)12288, ' ').trim();
        FXHDD01 fxhdd01 = new FXHDD01();
        fxhdd01.setKikakuChi(strKikakuti);
        String resultFlg = "0";
        switch (strTyekkupattern) {
            case "±":
                resultFlg = ValidateUtil.checkKikakuST001(fxhdd01, "0");
                break;
            case "～":
                resultFlg = ValidateUtil.checkKikakuST002(fxhdd01, "0");
                break;
            case "≦":
                resultFlg = ValidateUtil.checkKikakuST005(fxhdd01, "0");
                break;
            case "≧":
                resultFlg = ValidateUtil.checkKikakuST006(fxhdd01, "0");
                break;
            case "MAX":
                resultFlg = ValidateUtil.checkKikakuST009(fxhdd01, "0");
                break;
            case "MIN":
                resultFlg = ValidateUtil.checkKikakuST010(fxhdd01, "0");
                break;
            case "=":
                resultFlg = ValidateUtil.checkKikakuST011(fxhdd01, "0");
                break;
        }
        return resultFlg;
    }

    /**
     * 規格取込履歴]からMAX(取込No)取得
     *
     * @return MAX(取込No)
     */
    private int getMaxTorikomiNo() {
        int count = 0;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT MAX(torikomino) AS MAXNo from fxhdd10 ";
            Map result = queryRunner.query(sql, new MapHandler());

            if (result != null && !result.isEmpty() && result.get("MAXNo") != null) {
                count = (int) result.get("MAXNo");
            }
            DBUtil.outputSQLLog(sql, new ArrayList<>().toArray(), LOGGER);
        } catch (SQLException ex) {
            count = 0;
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }

    /**
     * (3)[採番ﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param hinmei 品名
     * @return 採番No
     */
    private HashMap<String, Integer> getSaibanno(String hinmei) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        int saibanno = 0;
        int hasRecordFlg = 0;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT saibanno from fxhdm10 where hinmei = ? ";
            List<Object> params = new ArrayList<>();
            params.add(hinmei);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map<String, Object> result = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (result != null && !result.isEmpty() && result.get("saibanno") != null) {
                saibanno = (int) result.get("saibanno");
                hasRecordFlg = 1;
            }
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        resultMap.put("saibanno", saibanno);
        resultMap.put("hasRecordFlg", hasRecordFlg);
        return resultMap;
    }

    /**
     * (4)[設計ﾃﾞｰﾀ]から、ﾃﾞｰﾀを取得
     *
     * @return MAX(設計No)
     */
    private long getMaxSekkeino() {
        long count = 0;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT MAX(sekkeino) AS sekkeino from da_mksekkei ";
            Map result = queryRunner.query(sql, new MapHandler());

            if (result != null && !result.isEmpty() && result.get("sekkeino") != null) {
                count = (long) result.get("sekkeino");
            }
            DBUtil.outputSQLLog(sql, new ArrayList<>().toArray(), LOGGER);
        } catch (SQLException ex) {
            count = 0;
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
    private boolean existError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return true;
    }

    /**
     * 担当者データ件数取得
     *
     * @return 検索結果件数
     */
    public Long selectTxtTantousyaData() {

        long count;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            // パラメータ設定
            String paramTxtTantousya = StringUtil.nullToBlank(getTxtTantousya());
            List<Object> params = new ArrayList<>();
            String sql = " SELECT COUNT(tantousya) AS COUNT "
                    + " FROM TANTOMAS "
                    + " WHERE ( TANTOUSYACODE = ? ) "
                    + " AND ZAISEKI = '1' ";

            params.addAll(Arrays.asList(paramTxtTantousya));
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("COUNT");

        } catch (SQLException ex) {
            count = 0;
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }

    /**
     * 【取込】ﾎﾞﾀﾝ押下時、種類チェック： 正常な場合処理を実行する
     *
     * @param item チェック項目
     * @param itemName チェック項目名
     * @return エラーが存在する場合false
     */
    private boolean inputSyuruiCheck(String item, String itemName) {
        //ﾁｪｯｸ詳細
        if (StringUtil.isEmpty(item.trim())) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000209", itemName), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 入力必須チェック： 正常な場合処理を実行する
     *
     * @param gxhdo501aModel 取込データ
     * @param item チェック項目
     * @param itemName チェック項目名
     * @return エラーが存在する場合false
     */
    private boolean inputCheck003(GXHDO501AModel gxhdo501aModel, String item, String itemName) {

        //半角・全角ｽﾍﾟｰｽを削除
        String str = item.replace((char) 12288, ' ');

        // ﾁｪｯｸ詳細
        if (StringUtil.isEmpty(str.trim())) {
            gxhdo501aModel.setResulta("NG");
            gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000003", itemName));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 指定桁数チェック： 正常な場合処理を実行する
     *
     * @param gxhdo501aModel 取込データ
     * @param item チェック項目
     * @param itemName チェック項目名
     * @param maxLength 指定桁数
     * @param numberCheckFlg 数値項目の型チェックフラグ
     * @return エラーが存在する場合false
     */
    private boolean inputCheck006(GXHDO501AModel gxhdo501aModel, String item, String itemName, int maxLength, String... numberCheckFlg) {
        boolean chkFlg = true;
        Pattern patternChk = Pattern.compile("[0-9]*\\.?[0-9]+");

        Matcher isNum0 = patternChk.matcher(item);
        boolean matches = isNum0.matches();
        if (numberCheckFlg != null && numberCheckFlg.length > 0 && "1".equals(numberCheckFlg[0]) && !matches) {
            // 数字の場合型チェック
            gxhdo501aModel.setResulta("NG");
            gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000008", itemName));
            chkFlg = false;
            return chkFlg;
        }
        // 数値(整数部のみ)
        if (matches) {
            int indexPattern = item.lastIndexOf(".");
            String checkStr = item;
            if (indexPattern != -1) {
                checkStr = item.substring(0, indexPattern);
            }
            //ﾁｪｯｸ詳細
            if (maxLength < StringUtil.length(checkStr)) {
                gxhdo501aModel.setResulta("NG");
                gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000006", itemName, String.valueOf(maxLength)));
                chkFlg = false;
            }
        } else {
            if (maxLength < item.length()) {
                gxhdo501aModel.setResulta("NG");
                gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000006", itemName, String.valueOf(maxLength)));
                chkFlg = false;
            }
        }
        return chkFlg;
    }

    /**
     * ダウンロードExcelの明細
     *
     * @return ダウンロードデータ
     */
    public List<GXHDO501AModel> downloadExcelDetail() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = " SELECT hinmei "
                    + " , pattern "
                    + " , syurui "
                    + " , kouteimei "
                    + " , koumokumei "
                    + " , kanrikoumokumei "
                    + " , tyekkupattern "
                    + " , kikakuti "
                    + " FROM "
                    + "	da_mkhyojunjoken "
                    + " WHERE ? IS NULL OR syurui = ? "
                    + " ORDER BY hinmei, pattern, kouteimei, koumokumei, kanrikoumokumei ";

            // パラメータ設定
            List<Object> params = new ArrayList<>();
            String paramCmbSyurui = StringUtil.blankToNull(getCmbSyurui());
            params.addAll(Arrays.asList(paramCmbSyurui, paramCmbSyurui));

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("hinmei", "hinmei");                       // 品名
            mapping.put("pattern", "pattern");                     // ﾊﾟﾀｰﾝ
            mapping.put("syurui", "syurui");                       // 種類
            mapping.put("kouteimei", "kouteimei");                 // 工程名
            mapping.put("koumokumei", "koumokumei");               // 項目名
            mapping.put("kanrikoumokumei", "kanrikoumokumei");     // 管理項目名
            mapping.put("tyekkupattern", "tyekkupattern");         // ﾁｪｯｸﾊﾟﾀｰﾝ
            mapping.put("kikakuti", "kikakuti");                   // 規格値

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO501AModel>> beanHandler
                    = new BeanListHandler<>(GXHDO501AModel.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunner.query(sql, beanHandler, params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
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
     * Excelダウンロード
     *
     * @throws Throwable
     */
    public void downloadExcel() throws Throwable {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        setCmbSyuruibgcolor(NORMAL_COLOR);
        setTxtTantousyabgcolor(NORMAL_COLOR);
        RequestContext.getCurrentInstance().execute("downloadOncomplete('" + NORMAL_COLOR + "')");
        String paramSyurui = StringUtil.nullToBlank(getCmbSyurui());
        // ﾁｪｯｸﾊﾟﾀｰﾝ入力チェック
        if (existError(validateUtil.checkC001(paramSyurui, "種類"))) {
            RequestContext.getCurrentInstance().execute("downloadOncomplete('" + ERROR_COLOR + "')");
            setCmbSyuruibgcolor(ERROR_COLOR);
            return;
        }
        List<GXHDO501AModel> listDataDetail = downloadExcelDetail();
        if (listDataDetail.isEmpty()) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000210", "標準規格情報"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        List<GXHDO501AModel> listDataHead = listDataDetail.parallelStream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(o -> o.getKouteimei() + "#" + o.getKoumokumei() + "#" + o.getKanrikoumokumei() + "#" + o.getTyekkupattern()))),
                ArrayList::new));
        File excel = null;

        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // 物理ファイルを生成
            excel = outputExcel(listDataHead, listDataDetail, myParam.getString("download_temp"), paramSyurui);
            // ダウンロードファイル名
            String downloadFileName = paramSyurui + "_" + ((new java.text.SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
                ErrUtil.outputErrorLog("物理ファイルはサーバから削除失敗", e, LOGGER);
            }

            fc.responseComplete();
        } catch (Exception ex) {
            ErrUtil.outputErrorLog("ﾀﾞｳﾝﾛｰﾄﾞに失敗", ex, LOGGER);

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ﾀﾞｳﾝﾛｰﾄﾞに失敗しました。", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            // 物理ファイルが削除されていない場合削除する
            if (excel != null && excel.exists()) {
                try {
                    excel.delete();
                } catch (Exception e) {
                    ErrUtil.outputErrorLog("物理ファイルはサーバから削除失敗", e, LOGGER);
                }
            }
        }
    }

    /**
     * Excelファイル生成
     *
     * @param listDataHead ヘッダーデータ
     * @param data 明細データ
     * @param sheetName シート名
     * @return 出力ファイル
     * @throws Throwable
     */
    private File outputExcel(List<GXHDO501AModel> listDataHead, List<GXHDO501AModel> data, String tempDir, String sheetName) throws Throwable {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
        workbook.setSheetName(0, sheetName);
        int rowIdx = 1;
        // スタイル
        CellStyle cellStyleWt = createHeaderCellStyle(workbook, IndexedColors.WHITE.getIndex());
        // ヘッダの背景色が水色を設置
        CellStyle cellStyleYl = createHeaderCellStyle(workbook, IndexedColors.PALE_BLUE.getIndex());
        CellStyle cellStyleNm = createNumericCellStyle(workbook, 0);

        // ヘッダーの「種類」列
        SXSSFRow headerRow1 = (SXSSFRow) sheet.createRow(rowIdx);
        SXSSFCell cell_0 = (SXSSFCell) headerRow1.createCell(0);
        cell_0.setCellStyle(cellStyleYl);
        cell_0.setCellValue("種類");
        SXSSFCell cell_1 = (SXSSFCell) headerRow1.createCell(1);
        cell_1.setCellStyle(cellStyleWt);
        cell_1.setCellValue(sheetName);

        rowIdx = 4;
        SXSSFRow headerRow4 = (SXSSFRow) sheet.createRow(rowIdx++);
        SXSSFRow headerRow5 = (SXSSFRow) sheet.createRow(rowIdx++);
        SXSSFRow headerRow6 = (SXSSFRow) sheet.createRow(rowIdx++);
        SXSSFRow headerRow7 = (SXSSFRow) sheet.createRow(rowIdx++);
        List<String> cellValueHeader = Arrays.asList("結果", "結果", "品名", "ﾊﾟﾀｰﾝ");
        // ヘッダーの前4列を設置
        for (int i = 0; i < 4; i++) {
            SXSSFCell createCell = headerRow4.createCell(i);

            CellRangeAddress cellRangeAddress = new CellRangeAddress(4, 7, i, i);
            sheet.addMergedRegion(cellRangeAddress);
            if (i == 0 || i == 1) {
                // ヘッダーの「結果」列
                createCell.setCellStyle(cellStyleWt);
            } else {
                // ヘッダーの「品名」||「ﾊﾟﾀｰﾝ」列
                createCell.setCellStyle(cellStyleYl);
            }
            // ボーダースタイルを設置
            RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet);
            RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet);
            RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet);
            RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
            createCell.setCellValue(cellValueHeader.get(i));
        }
        // ヘッダーの前4列以外を設置
        for (int i = 0; i < listDataHead.size(); i++) {
            GXHDO501AModel gxhdo501aModel = listDataHead.get(i);
            for (int j = 0; j < 4; j++) {
                // ヘッダーの「工程名」
                SXSSFCell createCell4 = headerRow4.createCell(i + 4);
                createCell4.setCellStyle(cellStyleYl);
                createCell4.setCellValue(StringUtil.nullToBlank(gxhdo501aModel.getKouteimei()));
                // ヘッダーの「項目名」
                SXSSFCell createCell5 = headerRow5.createCell(i + 4);
                createCell5.setCellStyle(cellStyleYl);
                createCell5.setCellValue(StringUtil.nullToBlank(gxhdo501aModel.getKoumokumei()));
                // ヘッダーの「管理項目名」
                SXSSFCell createCell6 = headerRow6.createCell(i + 4);
                createCell6.setCellStyle(cellStyleYl);
                createCell6.setCellValue(StringUtil.nullToBlank(gxhdo501aModel.getKanrikoumokumei()));
                // ヘッダーの「ﾁｪｯｸﾊﾟﾀｰﾝ」
                SXSSFCell createCell7 = headerRow7.createCell(i + 4);
                createCell7.setCellStyle(cellStyleYl);
                createCell7.setCellValue(StringUtil.nullToBlank(gxhdo501aModel.getTyekkupattern()));
            }
        }

        rowIdx = 8;
        CellStyle stringCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        stringCellStyle.setFont(font);
        Map<String, List<GXHDO501AModel>> collect = data.parallelStream().collect(Collectors.groupingBy(o -> o.getPattern() + "_" + o.getHinmei()));

        // 明細データを取得
        List<GXHDO501AModel> excelDetail = collect.keySet().stream().map((String key) -> {
            String[] temp = key.split("_");
            Integer pattern = (Integer) Integer.parseInt(temp[0]);
            String hinmei = temp[1];
            GXHDO501AModel gxhdO501AModel = new GXHDO501AModel();
            gxhdO501AModel.setPattern(pattern);
            gxhdO501AModel.setHinmei(hinmei);
            List<GXHDO501AModel> detailList = collect.get(key);
            ArrayList<String> kikakutiList = new ArrayList<>();
            listDataHead.forEach((item) -> kikakutiList.add("-"));
            for (int j = 0; j < detailList.size(); j++) {
                for (int i = 0; i < listDataHead.size(); i++) {
                    GXHDO501AModel gxhdo501aModelHead = listDataHead.get(i);
                    GXHDO501AModel gxhdo501aModelDetail = detailList.get(j);
                    if (StringUtil.nullToBlank(gxhdo501aModelHead.getKouteimei()).equals(StringUtil.nullToBlank(gxhdo501aModelDetail.getKouteimei()))
                            && StringUtil.nullToBlank(gxhdo501aModelHead.getKoumokumei()).equals(StringUtil.nullToBlank(gxhdo501aModelDetail.getKoumokumei()))
                            && StringUtil.nullToBlank(gxhdo501aModelHead.getKanrikoumokumei()).equals(StringUtil.nullToBlank(gxhdo501aModelDetail.getKanrikoumokumei()))
                            && StringUtil.nullToBlank(gxhdo501aModelHead.getTyekkupattern()).equals(StringUtil.nullToBlank(gxhdo501aModelDetail.getTyekkupattern()))) {
                        kikakutiList.set(i, gxhdo501aModelDetail.getKikakuti());
                        break;
                    }
                }
            }
            gxhdO501AModel.setKikakutilist(kikakutiList);
            return gxhdO501AModel;
        }).collect(Collectors.toList());
        excelDetail.sort(Comparator.comparing(GXHDO501AModel::getHinmei).thenComparing(GXHDO501AModel::getPattern));
        Iterator<GXHDO501AModel> iterator = excelDetail.iterator();
        SXSSFRow row;
        GXHDO501AModel rowData;
        SXSSFCell createCell;
        // 明細データはExcelの明細部に設置
        while (iterator.hasNext()) {
            row = (SXSSFRow) sheet.createRow(rowIdx++);
            rowData = iterator.next();
            int colIdx = 0;
            for (int i = 0; i < 4 + listDataHead.size(); i++) {
                createCell = row.createCell(colIdx++);
                createCell.setCellStyle(cellStyleWt);
                if (i == 2) {
                    createCell.setCellValue(StringUtil.nullToBlank(rowData.getHinmei()));
                } else if (i == 3) {
                    createCell.setCellStyle(cellStyleNm);
                    Integer patternVal = rowData.getPattern() != null ? rowData.getPattern() : null;
                    createCell.setCellValue(patternVal);
                } else if (i >= 4) {
                    createCell.setCellValue(StringUtil.nullToBlank(rowData.getKikakutilist().get(i - 4)));
                }
            }
        }

        sheet.trackAllColumnsForAutoSizing();
        // ランダムなファイル名を生成
        String tempFileName = RandomStringUtils.randomAlphanumeric(15);

        File outputFile = new File(tempDir, tempFileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
            fos.flush();
        }

        return outputFile;
    }

    /**
     * ヘッダセルスタイル取得
     *
     * @param style セルスタイル
     * @param bcolor ボーダースタイル
     */
    private void setBorder(CellStyle style, BorderStyle border) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBottomBorderColor(IndexedColors.BLACK.index);
    }

    /**
     * ヘッダセルスタイル取得
     *
     * @param workbook ワークブック
     * @param bcolor bcolor
     * @return ヘッダセルスタイル
     */
    private CellStyle createHeaderCellStyle(SXSSFWorkbook workbook, short bcolor) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(bcolor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        cellStyle.setFont(font);
        setBorder(cellStyle, BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * 数値セルスタイル取得
     *
     * @param workbook ワークブック
     * @param decimalLength 小数部桁数
     * @return 数値セルスタイル
     */
    private CellStyle createNumericCellStyle(SXSSFWorkbook workbook, int decimalLength) {

        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat fmt = workbook.createDataFormat();

        StringBuilder fmtStr = new StringBuilder("#,##0");

        // 小数部のフォーマット設定
        if (decimalLength > 0) {
            fmtStr.append(".");
            for (int i = 0; i < decimalLength; i++) {
                fmtStr.append("0");
            }
        }
        cellStyle.setDataFormat(fmt.getFormat(fmtStr.toString()));
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        cellStyle.setFont(font);
        setBorder(cellStyle, BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * fxhdm10:採番ﾏｽﾀ登録処理
     *
     * @param conDoc コネクション
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param saibanno 採番No
     * @param gxhdo501aModel 取込データ
     * @throws SQLException 例外エラー
     */
    private void insertfxhdm10(Connection conDoc, QueryRunner queryRunnerDoc, int saibanno,
            GXHDO501AModel gxhdo501aModel) throws SQLException {

        String sql = " INSERT INTO fxhdm10 ( "
                + " hinmei,saibanno,tourokunichiji "
                + " ) VALUES ( "
                + " ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(gxhdo501aModel.getHinmei()); //Excel.品名
        params.add(saibanno); //採番No
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * fxhdm10:採番ﾏｽﾀ修正処理
     *
     * @param conDoc コネクション
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param saibanno 採番No
     * @param gxhdo501aModel 取込データ
     * @throws SQLException 例外エラー
     */
    private void updatefxhdm10(Connection conDoc, QueryRunner queryRunnerDoc, int saibanno,
            GXHDO501AModel gxhdo501aModel) throws SQLException {

        String sql = " UPDATE fxhdm10 SET "
                + " hinmei = ?, saibanno = ?, "
                + " koushinnichiji = ? "
                + " WHERE hinmei= ? ";

        List<Object> params = new ArrayList<>();

        // 更新内容
        params.add(gxhdo501aModel.getHinmei()); //Excel.品名
        params.add(saibanno); //採番No
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        // 検索条件
        params.add(gxhdo501aModel.getHinmei()); //採番する前の品名

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * da_mkhyojunjoken:標準規格情報登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param gxhdo501aModel 取込データ
     * @param data 取込データ
     * @return 規格値='-'場合false
     * @throws SQLException 例外エラー
     */
    private boolean insertDaMkhyojunjoken(QueryRunner queryRunnerDoc, Connection conDoc,
            GXHDO501AModel gxhdo501aModel, DaMkhyojunjoken data) throws SQLException {
        if ("-".equals(data.getKikakuti())) {
            return false;
        }
        String sql = " INSERT INTO da_mkhyojunjoken ( "
                + " hinmei, pattern, syurui, kouteimei, koumokumei, kikakuti, "
                + " kanrikoumokumei, tyekkupattern, tantousya, tourokunichiji "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        List<Object> params = new ArrayList<>();

        params.add(gxhdo501aModel.getHinmei()); //Excel.品名
        params.add(gxhdo501aModel.getPatterns()); //Excel.ﾊﾟﾀｰﾝ
        params.add(gxhdo501aModel.getSyurui()); //Excel.種類
        params.add(data.getKouteimei()); //Excel.工程名
        params.add(data.getKoumokumei()); //Excel.項目名
        params.add(data.getKikakuti()); //Excel.規格値
        params.add(data.getKanrikoumokumei()); //Excel.管理項目名
        params.add(data.getTyekkupattern()); //Excel.ﾁｪｯｸﾊﾟﾀｰﾝ
        params.add(getTxtTantousya()); //画面.担当者       
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
        return true;
    }

    /**
     * da_mkhyojunjoken:標準規格情報削除処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param gxhdo501aModel 取込データ
     * @throws SQLException 例外エラー
     */
    private int deleteDaMkhyojunjoken(QueryRunner queryRunnerDoc, Connection conDoc,
            GXHDO501AModel data) throws SQLException {

        String sql = " DELETE FROM da_mkhyojunjoken "
                + " WHERE hinmei = ? AND pattern = ? AND syurui = ? ";

        List<Object> params = new ArrayList<>();
        params.add(data.getHinmei()); //Excel.品名
        params.add(data.getPatterns()); //Excel.ﾊﾟﾀｰﾝ
        params.add(data.getSyurui()); //Excel.種類

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * da_mksekkei:設計ﾃﾞｰﾀ登録処理
     *
     * @param conQcdb コネクション
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param fvsyurui 種類
     * @param paramSekkeino 設計No
     * @param jokenLotNo 製造LotNo
     * @param gxhdo501aModel 取込データ
     * @throws SQLException 例外エラー
     */
    private void insertDaMksekkei(Connection conQcdb, QueryRunner queryRunnerQcdb, String fvsyurui, long paramSekkeino, GXHDO501AModel gxhdo501aModel) throws SQLException {

        String sql = " INSERT INTO da_mksekkei ( "
                + " sekkeino, kojyo, lotno, edaban, syurui, hinmei, pattern, tantousya, tourokunichiji "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        String lotNo = StringUtil.blankToNull(gxhdo501aModel.getLotno());
        List<Object> params = new ArrayList<>();

        params.add(paramSekkeino); //設計No
        params.add(StringUtils.substring(lotNo, 0, 3)); //工場ｺｰﾄﾞ
        params.add(StringUtils.substring(lotNo, 3, 11)); //LotNo
        params.add(StringUtil.blankToNull(StringUtils.substring(lotNo, 11, 14))); //枝番
        params.add(fvsyurui); //Excel.種類
        params.add(StringUtil.blankToNull(gxhdo501aModel.getHinmei())); //Excel.品名
        params.add(StringUtil.blankToNull(gxhdo501aModel.getPatterns())); //Excel.ﾊﾟﾀｰﾝ
        params.add(getTxtTantousya()); //画面.担当者
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * da_mkjoken:規格情報登録処理
     *
     * @param conQcdb コネクション
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param paramSekkeino 設計No
     * @param data 取込データ
     * @throws SQLException 例外エラー
     */
    private void insertDaMkjoken(Connection conQcdb, QueryRunner queryRunnerQcdb, long paramSekkeino,
            DaMkhyojunjoken data) throws SQLException {

        String sql = " INSERT INTO da_mkjoken ( "
                + " sekkeino, kouteimei, koumokumei, kanrikoumokumei, kikakuti, tyekkupattern, tourokunichiji "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?, ?) ";
        List<Object> params = new ArrayList<>();
        params.add(paramSekkeino); //設計No
        params.add(StringUtil.nullToBlank(data.getKouteimei())); //Excel.工程名
        params.add(StringUtil.nullToBlank(data.getKoumokumei())); //Excel.項目名
        params.add(StringUtil.nullToBlank(data.getKanrikoumokumei())); //Excel.管理項目名
        params.add(StringUtil.nullToBlank(data.getKikakuti())); //Excel.規格値
        params.add(StringUtil.nullToBlank(data.getTyekkupattern())); //Excel.ﾁｪｯｸﾊﾟﾀｰﾝ
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * fxhdd10:規格取込履歴登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param torikomiNo 取込No
     * @param ngsuu NG数
     * @param syurui 種類
     * @throws SQLException 例外エラー
     */
    private void insertFxhdd10(QueryRunner queryRunnerDoc, Connection conDoc, int torikomiNo, int ngsuu,
            String syurui) throws SQLException {

        String sql = " INSERT INTO fxhdd10 ( "
                + " torikomino, kikaku, syurui, tantousya, ngsuu, tourokunichiji "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(torikomiNo); //取込No
        params.add(getrKikaku()); //画面.規格選択
        params.add(syurui); //Excel.種類
        params.add(getTxtTantousya()); //画面.担当者
        params.add(ngsuu); //NG数
        params.add(new Timestamp(System.currentTimeMillis())); //登録日

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 標準規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙのﾌｫｰﾏｯﾄﾁｪｯｸ
     *
     * @param sheet 取込ファイル
     * @return エラーが存在する場合false
     */
    private boolean loadHyoJunFileFormat(Sheet sheet) {
        // 種類
        String fhsyurui = getCellValue(sheet.getRow(1), 0);
        // 結果
        String fhresultA = getCellValue(sheet.getRow(4), 0);
        // 結果
        String fhresultB = getCellValue(sheet.getRow(4), 1);
        // 品名
        String fhhinmei = getCellValue(sheet.getRow(4), 2);
        // ﾊﾟﾀｰﾝ
        String fhtyekkupattern = getCellValue(sheet.getRow(4), 3);

        if (!("種類".equals(fhsyurui) && "結果".equals(fhresultA) && "結果".equals(fhresultB)
                && "品名".equals(fhhinmei) && "ﾊﾟﾀｰﾝ".equals(fhtyekkupattern))) {
            return false;
        }

        // 取込ファイル行目のサーズチェック
        return checkRowLength(sheet);
    }

    /**
     * 選択された規格が「標準規格」の場合、ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙより登録ﾃﾞｰﾀを取得
     *
     * @param sheet 取込ファイル
     * @return 登録ﾃﾞｰﾀ
     */
    private List<GXHDO501AModel> loadHyoJunDataFromFile(Sheet sheet) {
        List<GXHDO501AModel> loadDataList = new ArrayList<>();
        // 種類
        String fvsyurui = getCellValue(sheet.getRow(1), 1);
        // ヘッダーlength
        int headRowLen = 7;
        // 総行数
        int detailRowCount = sheet.getLastRowNum() - headRowLen;
        Row row4 = sheet.getRow(4);
        Row row5 = sheet.getRow(5);
        Row row6 = sheet.getRow(6);
        Row row7 = sheet.getRow(7);
        List<Row> headerRowList = Arrays.asList(row4, row5, row6, row7);

        // データを取込開始
        for (int i = 1; i <= detailRowCount; i++) {
            int rowNum = headRowLen + i;
            Row row = sheet.getRow(rowNum);
            // 毎行総列数 
            int colMaxNum = row.getLastCellNum();
            for (int j = 0; j < colMaxNum; j++) {
                if (row.getCell(j) != null && !CellType.STRING.equals(row.getCell(j).getCellType())) {
                    row.getCell(j).setCellType(CellType.STRING);
                }
            }
            GXHDO501AModel gxhdO501AModel = new GXHDO501AModel();
            // 種類
            gxhdO501AModel.setSyurui(fvsyurui);
            // 結果
            gxhdO501AModel.setResulta(getCellValue(row, 0));
            // 結果
            gxhdO501AModel.setResultb(getCellValue(row, 1));
            // 品名
            gxhdO501AModel.setHinmei(getCellValue(row, 2));
            // ﾊﾟﾀｰﾝ
            gxhdO501AModel.setPatterns(getCellValue(row, 3));

            List<DaMkhyojunjoken> rowDataList = new ArrayList<>();
            for (int j = 4; j < colMaxNum; j++) {
                DaMkhyojunjoken daMkhyojunjoken = new DaMkhyojunjoken();
                for (Row rowItem : headerRowList) {
                    if (rowItem.getCell(j) != null && !CellType.STRING.equals(rowItem.getCell(j).getCellType())) {
                        rowItem.getCell(j).setCellType(CellType.STRING);
                    }
                }
                // 工程名
                daMkhyojunjoken.setKouteimei(getCellValue(row4, j));
                // 項目名
                daMkhyojunjoken.setKoumokumei(getCellValue(row5, j));
                // 管理項目名
                daMkhyojunjoken.setKanrikoumokumei(getCellValue(row6, j));
                // ﾁｪｯｸﾊﾟﾀｰﾝ
                daMkhyojunjoken.setTyekkupattern(getCellValue(row7, j));
                // 規格値
                daMkhyojunjoken.setKikakuti(getCellValue(row, j));
                rowDataList.add(daMkhyojunjoken);
            }
            // 毎行の取込データ
            gxhdO501AModel.setRowdata(rowDataList);
            loadDataList.add(gxhdO501AModel);
        }
        return loadDataList;
    }

    /**
     * 標準規格ｱｯﾌﾟﾛｰﾄﾞﾌｧｲﾙの毎行のチェック処理
     *
     * @param gxhdo501aModel 取込データ
     * @param resultMap チェック結果の格納
     */
    private String checkHyoJunRowData(GXHDO501AModel gxhdo501aModel, HashMap<String, Integer> resultMap) {
        String rtnStep = "L";
        // E.[品名]ﾁｪｯｸ処理
        String hinmei = StringUtil.nullToBlank(gxhdo501aModel.getHinmei());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, hinmei, "品名", 8, resultMap)) {
            rtnStep = "C";
            return rtnStep;
        }
        // F.[ﾊﾟﾀｰﾝ]ﾁｪｯｸ処理
        String pattern = StringUtil.nullToBlank(gxhdo501aModel.getPatterns());
        // ﾁｪｯｸ処理
        if (check003And006(gxhdo501aModel, pattern, "ﾊﾟﾀｰﾝ", 11, resultMap, "1")) {
            rtnStep = "C";
            return rtnStep;
        }

        List<DaMkhyojunjoken> rowDataList = gxhdo501aModel.getRowdata();
        // 工程名
        String dkouteimei;
        // 項目名
        String dkoumokumei;
        // 管理項目名
        String dkanrikoumokumei;
        // ﾁｪｯｸﾊﾟﾀｰﾝ
        String dtyekkupattern;
        // 規格値
        String dkikakuti;
        for (DaMkhyojunjoken data : rowDataList) {
            // 工程名
            dkouteimei = StringUtil.nullToBlank(data.getKouteimei());
            // 項目名
            dkoumokumei = StringUtil.nullToBlank(data.getKoumokumei());
            // 管理項目名
            dkanrikoumokumei = StringUtil.nullToBlank(data.getKanrikoumokumei());
            // ﾁｪｯｸﾊﾟﾀｰﾝ
            dtyekkupattern = StringUtil.nullToBlank(data.getTyekkupattern());
            // 規格値
            dkikakuti = StringUtil.nullToBlank(data.getKikakuti());
            // [工程名]、[項目名]、[管理項目名]、[ﾁｪｯｸﾊﾟﾀｰﾝ]、[規格値]のどれか1項目でも取得できた場合　ﾁｪｯｸする。
            if (StringUtil.isEmpty(dkouteimei) && StringUtil.isEmpty(dkoumokumei) && StringUtil.isEmpty(dkanrikoumokumei)
                    && StringUtil.isEmpty(dtyekkupattern) && StringUtil.isEmpty(dkikakuti)) {
                rtnStep = "C";
                continue;
            }
            // ﾁｪｯｸ処理
            if (check003And006(gxhdo501aModel, dkouteimei, "工程名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dkoumokumei, "項目名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dkanrikoumokumei, "管理項目名", 20, resultMap)) {
                rtnStep = "C";
                break;
            }
            if (check003And006(gxhdo501aModel, dtyekkupattern, "ﾁｪｯｸﾊﾟﾀｰﾝ", 3, resultMap)) {
                rtnStep = "C";
                break;
            }
            // ﾁｪｯｸﾊﾟﾀｰﾝが「±、～、≧、≦、MAX、MIN、＝、-」の内容以外の場合ｴﾗｰ
            if (checkChkPattern(gxhdo501aModel, dtyekkupattern, "ﾁｪｯｸﾊﾟﾀｰﾝ", resultMap)) {
                rtnStep = "C";
                break;
            }
            if (!"-".equals(dkikakuti)) {
                // (A).取込件数(変数) + 1　を行う
                resultMap.put("rowDataCount", resultMap.get("rowDataCount") + 1);
                if (check003And006(gxhdo501aModel, dkikakuti, "規格値", 20, resultMap)) {
                    rtnStep = "C";
                    break;
                }

                // 規格値 チェック：ﾌｫｰﾏｯﾄがﾁｪｯｸﾊﾟﾀｰﾝと一致しない場合ｴﾗｰ。
                if ("-1".equals(chkTyekkupatternError(dkikakuti, dtyekkupattern))) {
                    // NG数+1
                    resultMap.put("ngCount", resultMap.get("ngCount") + 1);
                    gxhdo501aModel.setResulta("NG");
                    gxhdo501aModel.setResultb(MessageUtil.getMessage("XHD-000213", "規格値", dtyekkupattern));
                    rtnStep = "C";
                    break;
                }
            }
        }
        return rtnStep;
    }

    /**
     * L.標準規格情報登録処理
     *
     * @param resultMap
     * @param conQcdb コネクション
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param loadHyoJunDataList データリスト
     *
     */
    private String updDaMkhyojunjokenData(HashMap<String, Integer> resultMap, Connection conQcdb, QueryRunner queryRunnerQcdb,
            List<GXHDO501AModel> loadHyoJunDataList) throws SQLException {
        // 品名(変数)を初期化する
        String tmpHinme = "";
        // ﾊﾟﾀｰﾝ(変数)を初期化する
        String tmpPattern = "";
        // 標準規格情報ﾃﾞｰﾀへ削除件数
        int delCount = 0;
        // 標準規格情報ﾃﾞｰﾀへ登録件数
        int addCount = 0;

        for (GXHDO501AModel gxhdo501aModel : loadHyoJunDataList) {

            String hinmei = StringUtil.blankToNull(gxhdo501aModel.getHinmei());
            String pattern = StringUtil.blankToNull(gxhdo501aModel.getPatterns());

            List<DaMkhyojunjoken> rowDataList = gxhdo501aModel.getRowdata();
            for (DaMkhyojunjoken data : rowDataList) {
                // 規格情報(配列変数)の1ﾚｺｰﾄﾞ目の[品名]、[ﾊﾟﾀｰﾝ]を品名(変数)、ﾊﾟﾀｰﾝ(変数)と比較する。
                if (!tmpHinme.equals(hinmei) && !tmpPattern.equals(pattern)) {
                    // (B).異なる場合
                    tmpHinme = hinmei;
                    tmpPattern = pattern;

                    try {
                        // 標準規格情報のﾃﾞｰﾀ削除
                        int delNum = deleteDaMkhyojunjoken(queryRunnerQcdb, conQcdb, gxhdo501aModel);
                        delCount += delNum;
                        // 標準規格情報にﾃﾞｰﾀ登録
                        if (insertDaMkhyojunjoken(queryRunnerQcdb, conQcdb, gxhdo501aModel, data)) {
                            addCount++;
                        }
                    } catch (SQLException ex) {
                        ErrUtil.outputErrorLog("IOException発生", ex, LOGGER);
                    }
                } else {
                    // 標準規格情報にﾃﾞｰﾀ登録
                    if (insertDaMkhyojunjoken(queryRunnerQcdb, conQcdb, gxhdo501aModel, data)) {
                        addCount++;
                    }
                }
            }
        }
        resultMap.put("delCount", delCount);
        resultMap.put("addCount", addCount);
        return "M";
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
            ErrUtil.outputErrorLog("checkByte方法にException発生", e, LOGGER);
        }
    }
}
