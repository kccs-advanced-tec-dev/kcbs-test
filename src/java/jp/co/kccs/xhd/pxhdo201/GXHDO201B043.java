/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo201;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO201B043Model;
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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2020/03/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/11/26<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 電気特性・熱処理履歴検索画面
 *
 * @author 863 F.Zhang
 * @since 2020/03/09
 */
@Named
@ViewScoped
public class GXHDO201B043 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO201B043.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

    /**
     * パラメータマスタ操作
     */
    @Inject
    private SelectParam selectParam;

    /**
     * 一覧表示データ
     */
    private List<GXHDO201B043Model> listData = null;

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
     * 検索条件：KCPNo
     */
    private String kcpNo = "";

    /**
     * 検索条件：型式
     */
    private String katashiki = "";
    /**
     * 検索条件：容量
     */
    private String yoryo = "";

    /**
     * 検索条件：開始日(FROM)
     */
    private String startDateF = "";
    /**
     * 検索条件：開始日(TO)
     */
    private String startDateT = "";
    /**
     * 検索条件：開始時刻(FROM)
     */
    private String startTimeF = "";
    /**
     * 検索条件：開始時刻(TO)
     */
    private String startTimeT = "";
    /**
     * 検索条件：終了日(FROM)
     */
    private String endDateF = "";
    /**
     * 検索条件：終了日(TO)
     */
    private String endDateT = "";
    /**
     * 検索条件：終了時刻(FROM)
     */
    private String endTimeF = "";
    /**
     * 検索条件：終了時刻(TO)
     */
    private String endTimeT = "";

    /**
     * 表示可能ﾃﾞｰﾀ
     */
    private String possibleData[];

    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B043() {
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
    public List<GXHDO201B043Model> getListData() {
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
     * 検索条件：型式
     *
     * @return the katashiki
     */
    public String getKatashiki() {
        return katashiki;
    }

    /**
     * 検索条件：型式
     *
     * @param katashiki the katashiki to set
     */
    public void setKatashiki(String katashiki) {
        this.katashiki = katashiki;
    }

    /**
     * 検索条件：容量
     *
     * @return the yoryo
     */
    public String getYoryo() {
        return yoryo;
    }

    /**
     * 検索条件：容量
     *
     * @param yoryo the yoryo to set
     */
    public void setYoryo(String yoryo) {
        this.yoryo = yoryo;
    }

    /**
     * 検索条件：開始日(FROM)
     *
     * @return the startDateF
     */
    public String getStartDateF() {
        return startDateF;
    }

    /**
     * 検索条件：開始日(FROM)
     *
     * @param startDateF the startDateF to set
     */
    public void setStartDateF(String startDateF) {
        this.startDateF = startDateF;
    }

    /**
     * 検索条件：開始日(TO)
     *
     * @return the startDateT
     */
    public String getStartDateT() {
        return startDateT;
    }

    /**
     * 検索条件：開始日(TO)
     *
     * @param startDateT the startDateT to set
     */
    public void setStartDateT(String startDateT) {
        this.startDateT = startDateT;
    }

    /**
     * 検索条件：開始時刻(FROM)
     *
     * @return the startTimeF
     */
    public String getStartTimeF() {
        return startTimeF;
    }

    /**
     * 検索条件：開始時刻(FROM)
     *
     * @param startTimeF the startTimeF to set
     */
    public void setStartTimeF(String startTimeF) {
        this.startTimeF = startTimeF;
    }

    /**
     * 検索条件：開始時刻(TO)
     *
     * @return the startTimeT
     */
    public String getStartTimeT() {
        return startTimeT;
    }

    /**
     * 検索条件：開始時刻(TO)
     *
     * @param startTimeT the startTimeT to set
     */
    public void setStartTimeT(String startTimeT) {
        this.startTimeT = startTimeT;
    }

    /**
     * 検索条件：終了日(FROM)
     *
     * @return the endDateF
     */
    public String getEndDateF() {
        return endDateF;
    }

    /**
     * 検索条件：終了日(FROM)
     *
     * @param endDateF the endDateF to set
     */
    public void setEndDateF(String endDateF) {
        this.endDateF = endDateF;
    }

    /**
     * 検索条件：終了日(TO)
     *
     * @return the endDateT
     */
    public String getEndDateT() {
        return endDateT;
    }

    /**
     * 検索条件：終了日(TO)
     *
     * @param endDateT the endDateT to set
     */
    public void setEndDateT(String endDateT) {
        this.endDateT = endDateT;
    }

    /**
     * 検索条件：終了時刻(FROM)
     *
     * @return the endTimeF
     */
    public String getEndTimeF() {
        return endTimeF;
    }

    /**
     * 検索条件：終了時刻(FROM)
     *
     * @param endTimeF the endTimeF to set
     */
    public void setEndTimeF(String endTimeF) {
        this.endTimeF = endTimeF;
    }

    /**
     * 検索条件：終了時刻(TO)
     *
     * @return the endTimeT
     */
    public String getEndTimeT() {
        return endTimeT;
    }

    /**
     * 検索条件：終了時刻(TO)
     *
     * @param endTimeT the endTimeT to set
     */
    public void setEndTimeT(String endTimeT) {
        this.endTimeT = endTimeT;
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
     * 画面起動時処理
     */
    public void init() {
        // セッション情報から画面パラメータを取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        String login_user_name = (String) session.getAttribute("login_user_name");
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");
        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B043_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B043_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;

        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List7 = "";
        //■表示可能ﾃﾞｰﾀ取得処理
        //①Ⅲ.画面表示仕様(6)を発行する。
        Map fxhbm03Data7 = loadFxhbm03Data(userGrpList);
        //  1.取得できなかった場合、ｴﾗｰ。以降の処理は実行しない。
        if (fxhbm03Data7 == null || fxhbm03Data7.isEmpty()) {
            // ・ｴﾗｰｺｰﾄﾞ:XHD-000188
            // ・ｴﾗ-ﾒｯｾｰｼﾞ:電気特性熱処理履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
            // メッセージを画面に渡す
            settingError();
            return;
        } else {
            //取得したﾃﾞｰﾀが NULL の場合、ｴﾗｰ。以降の処理は実行しない。
            if (getMapData(fxhbm03Data7, "data") == null || "".equals(getMapData(fxhbm03Data7, "data"))) {
                //・ｴﾗｰｺｰﾄﾞ:XHD-000185
                //・ｴﾗ-ﾒｯｾｰｼﾞ:ﾃｰﾋﾟﾝｸﾞ作業履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
                settingError();
                return;
            } else {
                strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                possibleData = strfxhbm03List7.split(",");
            }
        }

        // 画面クリア
        clear();
    }

    /**
     * 設定エラー
     */
    private void settingError() {
        List<String> messageList = new ArrayList<>();
        messageList.add(MessageUtil.getMessage("XHD-000188"));
        displayStyle = "display:none;";
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(messageList);
        RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
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
        kcpNo = "";
        startDateF = "";
        startDateT = "";
        startTimeF = "";
        startTimeT = "";
        endDateF = "";
        endDateT = "";
        endTimeF = "";
        endTimeT = "";
        katashiki = "";
        yoryo = "";

        listData = new ArrayList<>();
    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
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

        // KCPNO
        if (existError(validateUtil.checkC103(getKcpNo(), "KCPNO", 25))) {
            return;
        }

        // 型式
        if (existError(validateUtil.checkC101(getKatashiki(), "型式", 2))) {
            return;
        }
        
        // 容量
        if (existError(validateUtil.checkC101(getYoryo(), "容量", 4))) {
            return;
        }

        // 開始日(FROM)
        if (existError(validateUtil.checkC101(getStartDateF(), "開始日(from)", 6))
                || existError(validateUtil.checkC201ForDate(getStartDateF(), "開始日(from)"))
                || existError(validateUtil.checkC501(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始日(TO)
        if (existError(validateUtil.checkC101(getStartDateT(), "開始日(to)", 6))
                || existError(validateUtil.checkC201ForDate(getStartDateT(), "開始日(to)"))
                || existError(validateUtil.checkC501(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "開始時刻(from)", 4))
                || existError(validateUtil.checkC201ForDate(getStartTimeF(), "開始時刻(from)"))
                || existError(validateUtil.checkC502(getStartTimeF(), "開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "開始時刻(to)", 4))
                || existError(validateUtil.checkC201ForDate(getStartTimeT(), "開始時刻(to)"))
                || existError(validateUtil.checkC502(getStartTimeT(), "開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "開始日(to)"))) {
            return;
        }

        // 終了日(FROM)
        if (existError(validateUtil.checkC101(getEndDateF(), "終了日(from)", 6))
                || existError(validateUtil.checkC201ForDate(getEndDateF(), "終了日(from)"))
                || existError(validateUtil.checkC501(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了日(TO)
        if (existError(validateUtil.checkC101(getEndDateT(), "終了日(to)", 6))
                || existError(validateUtil.checkC201ForDate(getEndDateT(), "終了日(to)"))
                || existError(validateUtil.checkC501(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "開始時刻(from)", 4))
                || existError(validateUtil.checkC201ForDate(getStartTimeF(), "開始時刻(from)"))
                || existError(validateUtil.checkC502(getStartTimeF(), "開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "開始時刻(to)", 4))
                || existError(validateUtil.checkC201ForDate(getStartTimeT(), "開始時刻(to)"))
                || existError(validateUtil.checkC502(getStartTimeT(), "開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了時刻(FROM)
        if (existError(validateUtil.checkC101(getEndTimeF(), "終了時刻(from)", 4))
                || existError(validateUtil.checkC201ForDate(getEndTimeF(), "終了時刻(from)"))
                || existError(validateUtil.checkC502(getEndTimeF(), "終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeF) && existError(validateUtil.checkC001(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了時刻(TO)
        if (existError(validateUtil.checkC101(getEndTimeT(), "終了時刻(to)", 4))
                || existError(validateUtil.checkC201ForDate(getEndTimeT(), "終了時刻(to)"))
                || existError(validateUtil.checkC502(getEndTimeT(), "終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeT) && existError(validateUtil.checkC001(getEndDateT(), "終了日(to)"))) {
            return;
        }

        if (possibleData == null) {
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
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param1", "warning");

            warnMessage = String.format("検索結果が%d件を超えています。<br/>継続しますか?<br/>%d件", listCountWarn, count);
            return;
        }

        // 入力チェックでエラーが存在しない場合検索処理を実行する
        selectListData();
    }

    /**
     * 警告ダイアログで「OK」押下時
     */
    public void processWarnOk() {
        // 検索処理実行
        selectListData();
    }

    /**
     * 一覧表示データ件数取得
     *
     * @return 検索結果件数
     */
    public long selectListDataCount() {
        long count = 0;
        //検査場所データリスト
        List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(lotno) AS CNT "
                    + "FROM sr_shinkuukansou "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(kcpno, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(kcpno, 10, 4) = ?) "
                    + "AND   (? IS NULL OR syoribi >= ?) "
                    + "AND   (? IS NULL OR syoribi <= ?) "
                    + "AND   (? IS NULL OR syuryonichiji >= ?) "
                    + "AND   (? IS NULL OR syuryonichiji <= ?) ";

            boolean jyokenAdd = false;
            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    jyokenAdd = true;
                }
            }
            if (jyokenAdd) {
                sql += " AND ";
                sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
            }

            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");

        } catch (SQLException ex) {
            count = 0;
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return count;
    }

    /**
     * 一覧表示データ検索
     */
    public void selectListData() {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            //検査場所データリスト
            List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));

            String sql = "SELECT "
                    + "  CONCAT(IFNULL(T1.kojyo, ''), IFNULL(T1.lotno, ''), IFNULL(T1.edaban, '')) AS LOTNO "
                    + ", T1.lotpre"
                    + ", T1.kcpno"
                    + ", T1.syoribi"
                    + ", T1.kaishijikan"
                    + ", T1.syuuryoujikan"
                    + ", T1.sagyosya"
                    + ", T1.koutei"
                    + ", T1.gouki"
                    + ", T1.setteiondo"
                    + ", T1.setteijikan"
                    + ", T1.kaisuu"
                    + ", T1.suuryo"
                    + ", T1.bikou1"
                    + ", T1.bikou2"
                    + ", T1.bikou3"
                    + ", T1.tokuisaki"
                    + ", T1.lotkubuncode"
                    + ", T1.ownercode"
                    + ", T1.ukeiretannijyuryo"
                    + ", T1.ukeiresoujyuryou"
                    + ", T1.syurui"
                    + ", T1.startkakunin"
                    + ", T1.syuryonichiji"
                    + ", T1.endtantou"
                    + ", T1.kensabasyo"
                    + ", T2.gouki1saya1"
                    + ", T2.gouki1saya2"
                    + ", T2.gouki1saya3"
                    + ", T2.gouki1saya4"
                    + ", T2.gouki1saya5"
                    + ", T2.gouki1saya6"
                    + ", T2.gouki1saya7"
                    + ", T2.gouki1saya8"
                    + ", T2.gouki1saya9"
                    + ", T2.gouki1saya10"
                    + ", T2.gouki1check1"
                    + ", T2.gouki1check2"
                    + ", T2.gouki1check3"
                    + ", T2.gouki1check4"
                    + ", T2.gouki1check5"
                    + ", T2.gouki1check6"
                    + ", T2.gouki1check7"
                    + ", T2.gouki1check8"
                    + ", T2.gouki1check9"
                    + ", T2.gouki1check10"
                    + ", T2.gouki2saya1"
                    + ", T2.gouki2saya2"
                    + ", T2.gouki2saya3"
                    + ", T2.gouki2saya4"
                    + ", T2.gouki2saya5"
                    + ", T2.gouki2saya6"
                    + ", T2.gouki2saya7"
                    + ", T2.gouki2saya8"
                    + ", T2.gouki2saya9"
                    + ", T2.gouki2saya10"
                    + ", T2.gouki2check1"
                    + ", T2.gouki2check2"
                    + ", T2.gouki2check3"
                    + ", T2.gouki2check4"
                    + ", T2.gouki2check5"
                    + ", T2.gouki2check6"
                    + ", T2.gouki2check7"
                    + ", T2.gouki2check8"
                    + ", T2.gouki2check9"
                    + ", T2.gouki2check10"
                    + ", T2.gouki3saya1"
                    + ", T2.gouki3saya2"
                    + ", T2.gouki3saya3"
                    + ", T2.gouki3saya4"
                    + ", T2.gouki3saya5"
                    + ", T2.gouki3saya6"
                    + ", T2.gouki3saya7"
                    + ", T2.gouki3saya8"
                    + ", T2.gouki3saya9"
                    + ", T2.gouki3saya10"
                    + ", T2.gouki3check1"
                    + ", T2.gouki3check2"
                    + ", T2.gouki3check3"
                    + ", T2.gouki3check4"
                    + ", T2.gouki3check5"
                    + ", T2.gouki3check6"
                    + ", T2.gouki3check7"
                    + ", T2.gouki3check8"
                    + ", T2.gouki3check9"
                    + ", T2.gouki3check10"
                    + ", T2.gouki4saya1"
                    + ", T2.gouki4saya2"
                    + ", T2.gouki4saya3"
                    + ", T2.gouki4saya4"
                    + ", T2.gouki4saya5"
                    + ", T2.gouki4saya6"
                    + ", T2.gouki4saya7"
                    + ", T2.gouki4saya8"
                    + ", T2.gouki4saya9"
                    + ", T2.gouki4saya10"
                    + ", T2.gouki4check1"
                    + ", T2.gouki4check2"
                    + ", T2.gouki4check3"
                    + ", T2.gouki4check4"
                    + ", T2.gouki4check5"
                    + ", T2.gouki4check6"
                    + ", T2.gouki4check7"
                    + ", T2.gouki4check8"
                    + ", T2.gouki4check9"
                    + ", T2.gouki4check10"
                    + ", T1.torokunichiji"
                    + ", T1.kosinnichiji"
                    + ", T1.revision "
                    + "FROM sr_shinkuukansou T1 "
                    + "LEFT JOIN sub_sr_shinkuukansou T2 "
                    + "ON (T1.kojyo = T2.kojyo "
                    + "AND T1.lotno = T2.lotno "
                    + "AND T1.edaban = T2.edaban "
                    + "AND T1.kaisuu = T2.kaisuu) "
                    + "WHERE (? IS NULL OR T1.kojyo = ?) "
                    + "AND   (? IS NULL OR T1.lotno = ?) "
                    + "AND   (? IS NULL OR T1.edaban = ?) "
                    + "AND   (? IS NULL OR T1.kcpno LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(T1.kcpno, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(T1.kcpno, 10, 4) = ?) "
                    + "AND   (? IS NULL OR T1.syoribi >= ?) "
                    + "AND   (? IS NULL OR T1.syoribi <= ?) "
                    + "AND   (? IS NULL OR T1.syuryonichiji >= ?) "
                    + "AND   (? IS NULL OR T1.syuryonichiji <= ?) ";

            boolean jyokenAdd = false;
            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    jyokenAdd = true;
                }
            }
            if (jyokenAdd) {
                sql += " AND ";
                sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
            }

            sql += "ORDER BY syoribi";

            // パラメータ設定
            List<Object> params = createSearchParam();

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("LOTNO", "lotno"); // ﾛｯﾄNo.
            mapping.put("kcpno", "kcpno"); // KCPNO
            mapping.put("tokuisaki", "tokuisaki"); // 客先
            mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
            mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
            mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); // 送り良品数
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量
            mapping.put("kaisuu", "kaisuu"); // 検査回数
            mapping.put("kensabasyo", "kensabasyo"); // 検査場所
            mapping.put("gouki", "gouki"); // 号機
            mapping.put("tpsiyou", "tpsiyou"); // TP仕様
            mapping.put("kakuhosu", "kakuhosu"); // 確保数
            mapping.put("kakuhoreelmaki1", "kakuhoreelmaki1"); // 確保ﾘｰﾙ巻数①
            mapping.put("kakuhoreelhonsu1", "kakuhoreelhonsu1"); // 確保ﾘｰﾙ本数①
            mapping.put("kakuhoreelmaki2", "kakuhoreelmaki2"); // 確保ﾘｰﾙ巻数②
            mapping.put("kakuhoreelhonsu2", "kakuhoreelhonsu2"); // 確保ﾘｰﾙ本数②
            mapping.put("tapelotno1", "tapelotno1"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.①
            mapping.put("tapelotno2", "tapelotno2"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.②
            mapping.put("tapelotno3", "tapelotno3"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.③
            mapping.put("tapelotno4", "tapelotno4"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.④
            mapping.put("tapelotno5", "tapelotno5"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑤
            mapping.put("tapelotno6", "tapelotno6"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑥
            mapping.put("tapelotno7", "tapelotno7"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑦
            mapping.put("tapelotno8", "tapelotno8"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑧
            mapping.put("tapelotno9", "tapelotno9"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑨
            mapping.put("tapelotno10", "tapelotno10"); // ｷｬﾘｱﾃｰﾌﾟLOTNO.⑩
            mapping.put("toptapelotno1", "toptapelotno1"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.①
            mapping.put("toptapelotno2", "toptapelotno2"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.②
            mapping.put("toptapelotno3", "toptapelotno3"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.③
            mapping.put("toptapelotno4", "toptapelotno4"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.④
            mapping.put("toptapelotno5", "toptapelotno5"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑤
            mapping.put("toptapelotno6", "toptapelotno6"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑥
            mapping.put("toptapelotno7", "toptapelotno7"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑦
            mapping.put("toptapelotno8", "toptapelotno8"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑧
            mapping.put("toptapelotno9", "toptapelotno9"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑨
            mapping.put("toptapelotno10", "toptapelotno10"); // ﾄｯﾌﾟﾃｰﾌﾟLOTNO.⑩
            mapping.put("bottomtapelot1", "bottomtapelot1"); // ﾎﾞﾄﾑﾃｰﾌﾟLOTNO.①
            mapping.put("bottomtapelot2", "bottomtapelot2"); // ﾎﾞﾄﾑﾃｰﾌﾟLOTNO.②
            mapping.put("bottomtapelot3", "bottomtapelot3"); // ﾎﾞﾄﾑﾃｰﾌﾟLOTNO.③
            mapping.put("yoryohannihi", "yoryohannihi"); // 容量範囲ｾｯﾄ値　Hi
            mapping.put("yoryohannilo", "yoryohannilo"); // 容量範囲ｾｯﾄ値　Lo
            mapping.put("yoryohannitanni", "yoryohannitanni"); // 容量範囲ｾｯﾄ値単位
            mapping.put("rangehyoji", "rangehyoji"); // ﾚﾝｼﾞ表示
            mapping.put("rangehyojitanni", "rangehyojitanni"); // ﾚﾝｼﾞ表示単位
            mapping.put("youryou", "youryou"); // 容量値
            mapping.put("youryoutanni", "youryoutanni"); // 容量値単位
            mapping.put("dfsethi", "dfsethi"); // DFｾｯﾄ値　Hi
            mapping.put("dfsetlo", "dfsetlo"); // DFｾｯﾄ値　Lo
            mapping.put("dfatai", "dfatai"); // DF値
            mapping.put("gazousetteijyoken", "gazousetteijyoken"); // 画像設定条件
            mapping.put("hoperneji", "hoperneji"); // ﾎｯﾊﾟｰﾈｼﾞ確認
            mapping.put("koteseisou", "koteseisou"); // ｺﾃ清掃
            mapping.put("kaisimaesehinzannasi", "kaisimaesehinzannasi"); // 開始前に製品残なき事
            mapping.put("setsya", "setsya"); // SET者
            mapping.put("kakuninsya", "kakuninsya"); // Wﾁｪｯｸ者
            mapping.put("kaisinichiji", "kaisinichiji"); // 開始日時
            mapping.put("sikentantou", "sikentantou"); // 試験担当者
            mapping.put("jijyurakkasiken", "jijyurakkasiken"); // 自重落下試験
            mapping.put("barasicheck", "barasicheck"); // ﾊﾞﾗｼﾁｪｯｸ
            mapping.put("toptapekakunin", "toptapekakunin"); // ﾄｯﾌﾟﾃｰﾌﾟ確認
            mapping.put("sounyuusu", "sounyuusu"); // 挿入数
            mapping.put("tounyuusu", "tounyuusu"); // 投入数
            mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1"); // 良品TPﾘｰﾙ巻数①
            mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1"); // 良品TPﾘｰﾙ本数①
            mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2"); // 良品TPﾘｰﾙ巻数②
            mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2"); // 良品TPﾘｰﾙ本数②
            mapping.put("ryouhinsu", "ryouhinsu"); // 良品数
            mapping.put("youryong1", "youryong1"); // 容量NG(NG1)
            mapping.put("gazoungue2", "gazoungue2"); // 画像NG(上画像数):NG2
            mapping.put("gazoungsita2", "gazoungsita2"); // 画像NG(下画像数):NG2
            mapping.put("budomari", "budomari"); // 歩留まり
            mapping.put("shuryonichiji", "shuryonichiji"); // 終了日時
            mapping.put("mentekaisu", "mentekaisu"); // ﾒﾝﾃﾅﾝｽ回数
            mapping.put("mentegotpgaikan", "mentegotpgaikan"); // ﾒﾝﾃ後TP外観
            mapping.put("barasitaisyoreelsu", "barasitaisyoreelsu"); // ﾊﾞﾗｼ対象ﾘｰﾙ数
            mapping.put("akireelsu", "akireelsu"); // 空ﾘｰﾙ数
            mapping.put("ryouhinreelsu", "ryouhinreelsu"); // 良品ﾘｰﾙ数
            mapping.put("qakakuniniraireelsu", "qakakuniniraireelsu"); // QA確認依頼ﾘｰﾙ数
            mapping.put("reelsucheck", "reelsucheck"); // ﾘｰﾙ数ﾁｪｯｸ
            mapping.put("tpatohopper", "tpatohopper"); // TP後清掃：ﾎｯﾊﾟｰ部
            mapping.put("tpatofeeder", "tpatofeeder"); // TP後清掃：ﾌｨｰﾀﾞ部
            mapping.put("tpatoindex", "tpatoindex"); // TP後清掃：INDEX内
            mapping.put("tpatongbox", "tpatongbox"); // TP後清掃：NGBOX内
            mapping.put("seisoutantou", "seisoutantou"); // 清掃担当者
            mapping.put("seisoukakunin", "seisoukakunin"); // 清掃確認者
            mapping.put("barasiiraireelsu", "barasiiraireelsu"); // ﾊﾞﾗｼ依頼ﾘｰﾙ数
            mapping.put("barasikaisinichiji", "barasikaisinichiji"); // ﾊﾞﾗｼ開始日時
            mapping.put("barasiksyuryonichiji", "barasiksyuryonichiji"); // ﾊﾞﾗｼ終了日時
            mapping.put("barasitantou", "barasitantou"); // ﾊﾞﾗｼ担当者
            mapping.put("datujiiraireelsu", "datujiiraireelsu"); // 脱磁依頼ﾘｰﾙ数
            mapping.put("datujikaisinichiji", "datujikaisinichiji"); // 脱磁開始日時
            mapping.put("datujitantou", "datujitantou"); // 脱磁担当者
            mapping.put("kakuhoreelsu", "kakuhoreelsu"); // 確保ﾘｰﾙ数
            mapping.put("akireelsu2", "akireelsu2"); // 空ﾘｰﾙ数2
            mapping.put("ryouhinreelsu2", "ryouhinreelsu2"); // 良品ﾘｰﾙ数2
            mapping.put("qakakuniniraireelsu2", "qakakuniniraireelsu2"); // QA確認依頼ﾘｰﾙ数2
            mapping.put("saisyukakuninn", "saisyukakuninn"); // 最終確認担当者
            mapping.put("bikou1", "bikou1"); // 備考1
            mapping.put("bikou2", "bikou2"); // 備考2
            mapping.put("gouki1saya1", "gouki1saya1"); // 号機①ｻﾔNo1
            mapping.put("gouki1saya2", "gouki1saya2"); // 号機①ｻﾔNo2
            mapping.put("gouki1saya3", "gouki1saya3"); // 号機①ｻﾔNo3
            mapping.put("gouki1saya4", "gouki1saya4"); // 号機①ｻﾔNo4
            mapping.put("gouki1saya5", "gouki1saya5"); // 号機①ｻﾔNo5
            mapping.put("gouki1saya6", "gouki1saya6"); // 号機①ｻﾔNo6
            mapping.put("gouki1saya7", "gouki1saya7"); // 号機①ｻﾔNo7
            mapping.put("gouki1saya8", "gouki1saya8"); // 号機①ｻﾔNo8
            mapping.put("gouki1saya9", "gouki1saya9"); // 号機①ｻﾔNo9
            mapping.put("gouki1saya10", "gouki1saya10"); // 号機①ｻﾔNo10
            mapping.put("gouki1check1", "gouki1check1"); // 号機①ﾁｪｯｸ1
            mapping.put("gouki1check2", "gouki1check2"); // 号機①ﾁｪｯｸ2
            mapping.put("gouki1check3", "gouki1check3"); // 号機①ﾁｪｯｸ3
            mapping.put("gouki1check4", "gouki1check4"); // 号機①ﾁｪｯｸ4
            mapping.put("gouki1check5", "gouki1check5"); // 号機①ﾁｪｯｸ5
            mapping.put("gouki1check6", "gouki1check6"); // 号機①ﾁｪｯｸ6
            mapping.put("gouki1check7", "gouki1check7"); // 号機①ﾁｪｯｸ7
            mapping.put("gouki1check8", "gouki1check8"); // 号機①ﾁｪｯｸ8
            mapping.put("gouki1check9", "gouki1check9"); // 号機①ﾁｪｯｸ9
            mapping.put("gouki1check10", "gouki1check10"); // 号機①ﾁｪｯｸ10
            mapping.put("gouki2saya1", "gouki2saya1"); // 号機②ｻﾔNo1
            mapping.put("gouki2saya2", "gouki2saya2"); // 号機②ｻﾔNo2
            mapping.put("gouki2saya3", "gouki2saya3"); // 号機②ｻﾔNo3
            mapping.put("gouki2saya4", "gouki2saya4"); // 号機②ｻﾔNo4
            mapping.put("gouki2saya5", "gouki2saya5"); // 号機②ｻﾔNo5
            mapping.put("gouki2saya6", "gouki2saya6"); // 号機②ｻﾔNo6
            mapping.put("gouki2saya7", "gouki2saya7"); // 号機②ｻﾔNo7
            mapping.put("gouki2saya8", "gouki2saya8"); // 号機②ｻﾔNo8
            mapping.put("gouki2saya9", "gouki2saya9"); // 号機②ｻﾔNo9
            mapping.put("gouki2saya10", "gouki2saya10"); // 号機②ｻﾔNo10
            mapping.put("gouki2check1", "gouki2check1"); // 号機②ﾁｪｯｸ1
            mapping.put("gouki2check2", "gouki2check2"); // 号機②ﾁｪｯｸ2
            mapping.put("gouki2check3", "gouki2check3"); // 号機②ﾁｪｯｸ3
            mapping.put("gouki2check4", "gouki2check4"); // 号機②ﾁｪｯｸ4
            mapping.put("gouki2check5", "gouki2check5"); // 号機②ﾁｪｯｸ5
            mapping.put("gouki2check6", "gouki2check6"); // 号機②ﾁｪｯｸ6
            mapping.put("gouki2check7", "gouki2check7"); // 号機②ﾁｪｯｸ7
            mapping.put("gouki2check8", "gouki2check8"); // 号機②ﾁｪｯｸ8
            mapping.put("gouki2check9", "gouki2check9"); // 号機②ﾁｪｯｸ9
            mapping.put("gouki2check10", "gouki2check10"); // 号機②ﾁｪｯｸ10
            mapping.put("gouki3saya1", "gouki3saya1"); // 号機③ｻﾔNo1
            mapping.put("gouki3saya2", "gouki3saya2"); // 号機③ｻﾔNo2
            mapping.put("gouki3saya3", "gouki3saya3"); // 号機③ｻﾔNo3
            mapping.put("gouki3saya4", "gouki3saya4"); // 号機③ｻﾔNo4
            mapping.put("gouki3saya5", "gouki3saya5"); // 号機③ｻﾔNo5
            mapping.put("gouki3saya6", "gouki3saya6"); // 号機③ｻﾔNo6
            mapping.put("gouki3saya7", "gouki3saya7"); // 号機③ｻﾔNo7
            mapping.put("gouki3saya8", "gouki3saya8"); // 号機③ｻﾔNo8
            mapping.put("gouki3saya9", "gouki3saya9"); // 号機③ｻﾔNo9
            mapping.put("gouki3saya10", "gouki3saya10"); // 号機③ｻﾔNo10
            mapping.put("gouki3check1", "gouki3check1"); // 号機③ﾁｪｯｸ1
            mapping.put("gouki3check2", "gouki3check2"); // 号機③ﾁｪｯｸ2
            mapping.put("gouki3check3", "gouki3check3"); // 号機③ﾁｪｯｸ3
            mapping.put("gouki3check4", "gouki3check4"); // 号機③ﾁｪｯｸ4
            mapping.put("gouki3check5", "gouki3check5"); // 号機③ﾁｪｯｸ5
            mapping.put("gouki3check6", "gouki3check6"); // 号機③ﾁｪｯｸ6
            mapping.put("gouki3check7", "gouki3check7"); // 号機③ﾁｪｯｸ7
            mapping.put("gouki3check8", "gouki3check8"); // 号機③ﾁｪｯｸ8
            mapping.put("gouki3check9", "gouki3check9"); // 号機③ﾁｪｯｸ9
            mapping.put("gouki3check10", "gouki3check10"); // 号機③ﾁｪｯｸ10
            mapping.put("gouki4saya1", "gouki4saya1"); // 号機④ｻﾔNo1
            mapping.put("gouki4saya2", "gouki4saya2"); // 号機④ｻﾔNo2
            mapping.put("gouki4saya3", "gouki4saya3"); // 号機④ｻﾔNo3
            mapping.put("gouki4saya4", "gouki4saya4"); // 号機④ｻﾔNo4
            mapping.put("gouki4saya5", "gouki4saya5"); // 号機④ｻﾔNo5
            mapping.put("gouki4saya6", "gouki4saya6"); // 号機④ｻﾔNo6
            mapping.put("gouki4saya7", "gouki4saya7"); // 号機④ｻﾔNo7
            mapping.put("gouki4saya8", "gouki4saya8"); // 号機④ｻﾔNo8
            mapping.put("gouki4saya9", "gouki4saya9"); // 号機④ｻﾔNo9
            mapping.put("gouki4saya10", "gouki4saya10"); // 号機④ｻﾔNo10
            mapping.put("gouki4check1", "gouki4check1"); // 号機④ﾁｪｯｸ1
            mapping.put("gouki4check2", "gouki4check2"); // 号機④ﾁｪｯｸ2
            mapping.put("gouki4check3", "gouki4check3"); // 号機④ﾁｪｯｸ3
            mapping.put("gouki4check4", "gouki4check4"); // 号機④ﾁｪｯｸ4
            mapping.put("gouki4check5", "gouki4check5"); // 号機④ﾁｪｯｸ5
            mapping.put("gouki4check6", "gouki4check6"); // 号機④ﾁｪｯｸ6
            mapping.put("gouki4check7", "gouki4check7"); // 号機④ﾁｪｯｸ7
            mapping.put("gouki4check8", "gouki4check8"); // 号機④ﾁｪｯｸ8
            mapping.put("gouki4check9", "gouki4check9"); // 号機④ﾁｪｯｸ9
            mapping.put("gouki4check10", "gouki4check10"); // 号機④ﾁｪｯｸ10

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B043Model>> beanHandler
                    = new BeanListHandler<>(GXHDO201B043Model.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b043.json"));
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "電気特性・熱処理");

            // ダウンロードファイル名
            String downloadFileName = "電気特性・熱処理_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
    private List<Object> createSearchParam() {
        // パラメータ設定
        //検査場所データリスト
        List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));
        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 11);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 11, 14));
        }
        String paramKcpno = null;
        if (!StringUtil.isEmpty(kcpNo)) {
            paramKcpno = "%" + DBUtil.escapeString(getKcpNo()) + "%";
        }
        String paramKatashiki = StringUtil.blankToNull(getKatashiki());
        String paramYoryo = StringUtil.blankToNull(getYoryo());
        Date paramStartDateF = null;
        if (!StringUtil.isEmpty(startDateF)) {
            paramStartDateF = DateUtil.convertStringToDate(getStartDateF(), StringUtil.isEmpty(getStartTimeF()) ? "0000" : getStartTimeF());
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startDateT)) {
            paramStartDateT = DateUtil.convertStringToDate(getStartDateT(), StringUtil.isEmpty(getStartTimeT()) ? "2359" : getStartTimeT());
        }
        Date paramEndDateF = null;
        if (!StringUtil.isEmpty(endDateF)) {
            paramEndDateF = DateUtil.convertStringToDate(getEndDateF(), StringUtil.isEmpty(getEndTimeF()) ? "0000" : getEndTimeF());
        }
        Date paramEndDateT = null;
        if (!StringUtil.isEmpty(endDateT)) {
            paramEndDateT = DateUtil.convertStringToDate(getEndDateT(), StringUtil.isEmpty(getEndTimeT()) ? "2359" : getEndTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        params.addAll(Arrays.asList(paramKatashiki, paramKatashiki));
        params.addAll(Arrays.asList(paramYoryo, paramYoryo));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));
        params.addAll(Arrays.asList(paramEndDateF, paramEndDateF));
        params.addAll(Arrays.asList(paramEndDateT, paramEndDateT));
        boolean paramsAdd = false;
        for (String data : kensaBasyoDataList) {
            if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                paramsAdd = true;
            }
        }
        if (paramsAdd) {
            params.addAll(kensaBasyoDataList);
        }

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
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @return 取得データ
     */
    private Map loadFxhbm03Data(List<String> userGrpList) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 WHERE ";
            sql = sql + DBUtil.getInConditionPreparedStatement("user_name", userGrpList.size());
            sql = sql + " AND key = '電気特性熱処理履歴_表示可能ﾃﾞｰﾀ'";

            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            return queryRunner.query(sql, new MapHandler(), userGrpList.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
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
}
