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
import jp.co.kccs.xhd.model.GXHDO201B026Model;
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
 * 変更日	2019/12/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/03<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒ外部電極塗布履歴検索画面
 *
 * @author 863 F.Zhang
 * @since 2019/12/09
 */
@Named
@ViewScoped
public class GXHDO201B026 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO201B026.class.getName());

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
    private List<GXHDO201B026Model> listData = null;

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
     * 検索条件：開始担当者
     */
    private String tantousya = "";
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
     * 検索条件：号機
     */
    private String gouki = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B026() {
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
    public List<GXHDO201B026Model> getListData() {
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
     * 検索条件：開始担当者
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 検索条件：開始担当者
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B026_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B026_display_page_count", session));
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
        kcpNo = "";
        tantousya = "";
        startDateF = "";
        startDateT = "";
        startTimeF = "";
        startTimeT = "";
        endDateF = "";
        endDateT = "";
        endTimeF = "";
        endTimeT = "";
        gouki = "";

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
        // 開始担当者
        if (existError(validateUtil.checkC101(getTantousya(), "開始担当者", 6))) {
            return;
        }
        if (!StringUtil.isEmpty(getTantousya())) {
            boolean existTantousyaMaster = false;
            try {
                QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
                if (validateUtil.existTantomasEx(getTantousya(), queryRunnerWip)) {
                    existTantousyaMaster = true;
                }
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("担当者マスタ存在チェックに失敗", ex, LOGGER);
                existTantousyaMaster = false;
            }
            if (!existTantousyaMaster) {
                // 入力された担当者が[tantomas]に存在しない場合ｴﾗｰ。
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "開始担当者"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }

        // KCPNO
        if (existError(validateUtil.checkC103(getKcpNo(), "KCPNO", 25))) {
            return;
        }

        // 号機
        if (!StringUtil.isEmpty(gouki)) {
            // 号機が入力されている場合のみマスタチェックを実施
            boolean existGoukiMaster = false;
            try {
                QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
                if (validateUtil.existGokukimas(getGouki(), queryRunnerWip)) {
                    existGoukiMaster = true;
                }
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("号機マスタ存在チェックに失敗", ex, LOGGER);
                existGoukiMaster = false;
            }
            if (!existGoukiMaster) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "号機"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
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

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(lotno) AS CNT "
                    + "FROM sr_term "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR startdatetime >= ?) "
                    + "AND   (? IS NULL OR startdatetime <= ?) "
                    + "AND   (? IS NULL OR enddatetime >= ?) "
                    + "AND   (? IS NULL OR enddatetime <= ?) "
                    + "AND   (? IS NULL OR StartTantosyacode = ?)"
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR gouki1 = ?) ";

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
            String sql = "SELECT CONCAT(IFNULL(kojyo, ''), IFNULL(lotno, ''), IFNULL(edaban, '')) AS LOTNO"
                    + ", lotpre"
                    + ", kcpno"
                    + ", suuryou"
                    + ", ukeiretannijyuryo"
                    + ", ukeiresoujyuryou"
                    + ", jikilsunpoumax"
                    + ", jikilsunpoumin"
                    + ", jikiwsunpoumax"
                    + ", jikiwsunpoumin"
                    + ", jikitsunpoumax"
                    + ", jikitsunpoumin"
                    + ", kyakusaki"
                    + ", sagyobasyo"
                    + ", gouki1"
                    + ", setteisya1"
                    + ", gouki2"
                    + ", setteisya2"
                    + ", pastehinmei"
                    + ", pastelotno"
                    + ", pastesaiseikaisuu"
                    + ", pastekoukanjikan"
                    + ", pastenendo"
                    + ", pasteondo"
                    + ", pastekansannendo"
                    + ", pastekigen"
                    + ", startdatetime"
                    + ", enddatetime"
                    + ", senkougaikan"
                    + ", jikilsunpou"
                    + ", jikitsunpou"
                    + ", psunpou1a"
                    + ", psunpou1b"
                    + ", psunpou1c"
                    + ", psunpou1d"
                    + ", psunpou1e"
                    + ", psunpouave1"
                    + ", psunpourange1"
                    + ", lsunpou1a"
                    + ", lsunpou1b"
                    + ", lsunpou1c"
                    + ", lsunpou1d"
                    + ", lsunpou1e"
                    + ", tanmenatsumi1"
                    + ", sunpouhantei1"
                    + ", psunpou2a"
                    + ", psunpou2b"
                    + ", psunpou2c"
                    + ", psunpou2d"
                    + ", psunpou2e"
                    + ", psunpouave2"
                    + ", psunpourange2"
                    + ", lsunpou2a"
                    + ", lsunpou2b"
                    + ", lsunpou2c"
                    + ", lsunpou2d"
                    + ", lsunpou2e"
                    + ", tanmenatsumi2"
                    + ", sunpouhantei2"
                    + ", pasteatsumi1"
                    + ", pasteatsumi2"
                    + ", dipjigusize"
                    + ", dipjigumaisuu"
                    + ", dipgogaikankekka"
                    + ", syochinaiyou"
                    + ", inkuatsumia"
                    + ", inkuatsumib"
                    + ", setteijyouken"
                    + ", bikou1"
                    + ", bikou2"
                    + ", bikou3"
                    + ", psunpou1f"
                    + ", psunpou1g"
                    + ", psunpou1h"
                    + ", psunpou1i"
                    + ", psunpou1j"
                    + ", lsunpou1f"
                    + ", lsunpou1g"
                    + ", lsunpou1h"
                    + ", lsunpou1i"
                    + ", lsunpou1j"
                    + ", psunpou2f"
                    + ", psunpou2g"
                    + ", psunpou2h"
                    + ", psunpou2i"
                    + ", psunpou2j"
                    + ", lsunpou2f"
                    + ", lsunpou2g"
                    + ", lsunpou2h"
                    + ", lsunpou2i"
                    + ", lsunpou2j"
                    + ", lsunpouave1"
                    + ", lsunpouave2"
                    + ", pastekokeibun"
                    + ", Dip1AtamaDashiSettei"
                    + ", Dip1Clearance"
                    + ", Dip1SkeegeSettei"
                    + ", Dip1BlotClearance"
                    + ", Dip1Reveler"
                    + ", Dip2AtamaDashiSettei"
                    + ", Dip2Clearance"
                    + ", Dip2SkeegeSettei"
                    + ", Dip2BlotClearance"
                    + ", Dip2Reveler"
                    + ", psunpou2k"
                    + ", psunpou2l"
                    + ", psunpou2m"
                    + ", psunpou2n"
                    + ", psunpou2o"
                    + ", psunpou2p"
                    + ", psunpou2q"
                    + ", psunpou2r"
                    + ", psunpou2s"
                    + ", psunpou2t"
                    + ", psunpou2u"
                    + ", psunpou2v"
                    + ", psunpou2w"
                    + ", psunpou2x"
                    + ", psunpou2y"
                    + ", psunpou2z"
                    + ", psunpou2aa"
                    + ", psunpou2ab"
                    + ", psunpou2ac"
                    + ", psunpou2ad"
                    + ", psunpou2ae"
                    + ", psunpou2af"
                    + ", psunpou2ag"
                    + ", psunpou2ah"
                    + ", psunpou2ai"
                    + ", psunpou2aj"
                    + ", psunpou2ak"
                    + ", psunpou2al"
                    + ", psunpou2am"
                    + ", psunpou2an"
                    + ", lsunpou2k"
                    + ", lsunpou2l"
                    + ", lsunpou2m"
                    + ", lsunpou2n"
                    + ", lsunpou2o"
                    + ", lsunpou2p"
                    + ", lsunpou2q"
                    + ", lsunpou2r"
                    + ", lsunpou2s"
                    + ", lsunpou2t"
                    + ", lsunpou2u"
                    + ", lsunpou2v"
                    + ", lsunpou2w"
                    + ", lsunpou2x"
                    + ", lsunpou2y"
                    + ", lsunpou2z"
                    + ", lsunpou2aa"
                    + ", lsunpou2ab"
                    + ", lsunpou2ac"
                    + ", lsunpou2ad"
                    + ", lsunpou2ae"
                    + ", lsunpou2af"
                    + ", lsunpou2ag"
                    + ", lsunpou2ah"
                    + ", lsunpou2ai"
                    + ", lsunpou2aj"
                    + ", lsunpou2ak"
                    + ", lsunpou2al"
                    + ", lsunpou2am"
                    + ", lsunpou2an"
                    + ", lsunpourange2"
                    + ", lsunpoumin2"
                    + ", lsunpoumax2"
                    + ", wtsunpou2a"
                    + ", wtsunpou2b"
                    + ", wtsunpou2c"
                    + ", wtsunpou2d"
                    + ", wtsunpou2e"
                    + ", wtsunpou2f"
                    + ", wtsunpou2g"
                    + ", wtsunpou2h"
                    + ", wtsunpou2i"
                    + ", wtsunpou2j"
                    + ", wtsunpou2k"
                    + ", wtsunpou2l"
                    + ", wtsunpou2m"
                    + ", wtsunpou2n"
                    + ", wtsunpou2o"
                    + ", wtsunpou2p"
                    + ", wtsunpou2q"
                    + ", wtsunpou2r"
                    + ", wtsunpou2s"
                    + ", wtsunpou2t"
                    + ", wtsunpou2u"
                    + ", wtsunpou2v"
                    + ", wtsunpou2w"
                    + ", wtsunpou2x"
                    + ", wtsunpou2y"
                    + ", wtsunpou2z"
                    + ", wtsunpou2aa"
                    + ", wtsunpou2ab"
                    + ", wtsunpou2ac"
                    + ", wtsunpou2ad"
                    + ", wtsunpou2ae"
                    + ", wtsunpou2af"
                    + ", wtsunpou2ag"
                    + ", wtsunpou2ah"
                    + ", wtsunpou2ai"
                    + ", wtsunpou2aj"
                    + ", wtsunpou2ak"
                    + ", wtsunpou2al"
                    + ", wtsunpou2am"
                    + ", wtsunpou2an"
                    + ", wtsunpouave2"
                    + ", wtsunpourange2"
                    + ", wtsunpoumin2"
                    + ", wtsunpoumax2"
                    + ", psunpoumin2"
                    + ", psunpoumax2"
                    + ", setubisyurui"
                    + ", tofukaisuu"
                    + ", hojijigu"
                    + ", nentyakusheetlot1"
                    + ", nentyakusheetlot2"
                    + ", tofujigutorikosuu"
                    + ", StartTantosyacode"
                    + ", StartKakuninsyacode"
                    + ", juryou"
                    + ", syorikosuu"
                    + ", EndTantosyacode"
                    + ", pasteatsumi1ji"
                    + ", pasteatsumi2ji"
                    + ", atsumiinkua"
                    + ", atsumiinkub"
                    + ", kaisuu "
                    + "FROM sr_term "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR startdatetime >= ?) "
                    + "AND   (? IS NULL OR startdatetime <= ?) "
                    + "AND   (? IS NULL OR enddatetime >= ?) "
                    + "AND   (? IS NULL OR enddatetime <= ?) "
                    + "AND   (? IS NULL OR StartTantosyacode = ?)"
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR gouki1 = ?) "
                    + "ORDER BY startdatetime ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("LOTNO", "lotno"); // ﾛｯﾄNo
            mapping.put("lotpre", "lotpre"); // ﾛｯﾄﾌﾟﾚ
            mapping.put("kcpno", "kcpno"); // KCPNO
            mapping.put("suuryou", "suuryou"); // 受入れ良品数
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量
            mapping.put("jikilsunpoumax", "jikilsunpoumax"); // 磁器L寸法(MAX)
            mapping.put("jikilsunpoumin", "jikilsunpoumin"); // 磁器L寸法(MIN)
            mapping.put("jikiwsunpoumax", "jikiwsunpoumax"); // 磁器W寸法(MAX)
            mapping.put("jikiwsunpoumin", "jikiwsunpoumin"); // 磁器W寸法(MIN)
            mapping.put("jikitsunpoumax", "jikitsunpoumax"); // 磁器T寸法(MAX)
            mapping.put("jikitsunpoumin", "jikitsunpoumin"); // 磁器T寸法(MIN)
            mapping.put("kyakusaki", "kyakusaki"); // 客先
            mapping.put("sagyobasyo", "sagyobasyo"); // 作業場所
            mapping.put("gouki1", "gouki1"); // 号機1
            mapping.put("setteisya1", "setteisya1"); // 条件設定者1
            mapping.put("gouki2", "gouki2"); // 号機2
            mapping.put("setteisya2", "setteisya2"); // 条件設定者2
            mapping.put("pastehinmei", "pastehinmei"); // ﾍﾟｰｽﾄ品名
            mapping.put("pastelotno", "pastelotno"); // ﾍﾟｰｽﾄﾛｯﾄNo
            mapping.put("pastesaiseikaisuu", "pastesaiseikaisuu"); // ﾍﾟｰｽﾄ再生回数
            mapping.put("pastekoukanjikan", "pastekoukanjikan"); // ﾍﾟｰｽﾄ交換時間
            mapping.put("pastenendo", "pastenendo"); // ﾍﾟｰｽﾄ粘度
            mapping.put("pasteondo", "pasteondo"); // ﾍﾟｰｽﾄ温度
            mapping.put("pastekansannendo", "pastekansannendo"); // ﾍﾟｰｽﾄ換算粘度
            mapping.put("pastekigen", "pastekigen"); // ﾍﾟｰｽﾄ期限
            mapping.put("startdatetime", "startdatetime"); // 開始日時
            mapping.put("enddatetime", "enddatetime"); // 終了日時
            mapping.put("senkougaikan", "senkougaikan"); // 先行外観
            mapping.put("jikilsunpou", "jikilsunpou"); // 磁器L寸法
            mapping.put("jikitsunpou", "jikitsunpou"); // 磁器T寸法
            mapping.put("psunpou1a", "psunpou1a"); // P寸法1OLD
            mapping.put("psunpou1b", "psunpou1b"); // P寸法2OLD
            mapping.put("psunpou1c", "psunpou1c"); // P寸法3OLD
            mapping.put("psunpou1d", "psunpou1d"); // P寸法4OLD
            mapping.put("psunpou1e", "psunpou1e"); // P寸法5OLD
            mapping.put("psunpouave1", "psunpouave1"); // P寸法AVEOLD
            mapping.put("psunpourange1", "psunpourange1"); // P寸法RANGEOLD
            mapping.put("lsunpou1a", "lsunpou1a"); // L寸法1OLD
            mapping.put("lsunpou1b", "lsunpou1b"); // L寸法2OLD
            mapping.put("lsunpou1c", "lsunpou1c"); // L寸法3OLD
            mapping.put("lsunpou1d", "lsunpou1d"); // L寸法4OLD
            mapping.put("lsunpou1e", "lsunpou1e"); // L寸法5OLD
            mapping.put("tanmenatsumi1", "tanmenatsumi1"); // 端面厚みOLD
            mapping.put("sunpouhantei1", "sunpouhantei1"); // 判定OLD
            mapping.put("psunpou2a", "psunpou2a"); // P寸法1
            mapping.put("psunpou2b", "psunpou2b"); // P寸法2
            mapping.put("psunpou2c", "psunpou2c"); // P寸法3
            mapping.put("psunpou2d", "psunpou2d"); // P寸法4
            mapping.put("psunpou2e", "psunpou2e"); // P寸法5
            mapping.put("psunpouave2", "psunpouave2"); // P寸法AVE
            mapping.put("psunpourange2", "psunpourange2"); // P寸法RANGE
            mapping.put("lsunpou2a", "lsunpou2a"); // L寸法1
            mapping.put("lsunpou2b", "lsunpou2b"); // L寸法2
            mapping.put("lsunpou2c", "lsunpou2c"); // L寸法3
            mapping.put("lsunpou2d", "lsunpou2d"); // L寸法4
            mapping.put("lsunpou2e", "lsunpou2e"); // L寸法5
            mapping.put("tanmenatsumi2", "tanmenatsumi2"); // 端面厚み
            mapping.put("sunpouhantei2", "sunpouhantei2"); // 判定
            mapping.put("pasteatsumi1", "pasteatsumi1"); // ﾍﾟｰｽﾄ厚み設定値1次
            mapping.put("pasteatsumi2", "pasteatsumi2"); // ﾍﾟｰｽﾄ厚み設定値2次
            mapping.put("dipjigusize", "dipjigusize"); // DIP治具ｻｲｽﾞ
            mapping.put("dipjigumaisuu", "dipjigumaisuu"); // DIP治具枚数
            mapping.put("dipgogaikankekka", "dipgogaikankekka"); // DIP後外観結果
            mapping.put("syochinaiyou", "syochinaiyou"); // 処置内容
            mapping.put("inkuatsumia", "inkuatsumia"); // ｲﾝｸ厚みa
            mapping.put("inkuatsumib", "inkuatsumib"); // ｲﾝｸ厚みb
            mapping.put("setteijyouken", "setteijyouken"); // 設定条件
            mapping.put("bikou1", "bikou1"); // 備考1
            mapping.put("bikou2", "bikou2"); // 備考2
            mapping.put("bikou3", "bikou3"); // 備考3
            mapping.put("psunpou1f", "psunpou1f"); // P寸法6OLD
            mapping.put("psunpou1g", "psunpou1g"); // P寸法7OLD
            mapping.put("psunpou1h", "psunpou1h"); // P寸法8OLD
            mapping.put("psunpou1i", "psunpou1i"); // P寸法9OLD
            mapping.put("psunpou1j", "psunpou1j"); // P寸法10OLD
            mapping.put("lsunpou1f", "lsunpou1f"); // L寸法6OLD
            mapping.put("lsunpou1g", "lsunpou1g"); // L寸法7OLD
            mapping.put("lsunpou1h", "lsunpou1h"); // L寸法8OLD
            mapping.put("lsunpou1i", "lsunpou1i"); // L寸法9OLD
            mapping.put("lsunpou1j", "lsunpou1j"); // L寸法10OLD
            mapping.put("psunpou2f", "psunpou2f"); // P寸法6
            mapping.put("psunpou2g", "psunpou2g"); // P寸法7
            mapping.put("psunpou2h", "psunpou2h"); // P寸法8
            mapping.put("psunpou2i", "psunpou2i"); // P寸法9
            mapping.put("psunpou2j", "psunpou2j"); // P寸法10
            mapping.put("lsunpou2f", "lsunpou2f"); // L寸法6
            mapping.put("lsunpou2g", "lsunpou2g"); // L寸法7
            mapping.put("lsunpou2h", "lsunpou2h"); // L寸法8
            mapping.put("lsunpou2i", "lsunpou2i"); // L寸法9
            mapping.put("lsunpou2j", "lsunpou2j"); // L寸法10
            mapping.put("lsunpouave1", "lsunpouave1"); // L寸法AVEOLD
            mapping.put("lsunpouave2", "lsunpouave2"); // L寸法AVE
            mapping.put("pastekokeibun", "pastekokeibun"); // ﾍﾟｰｽﾄ固形分
            mapping.put("Dip1AtamaDashiSettei", "dip1atamadashisettei"); // 1次頭出し設定
            mapping.put("Dip1Clearance", "dip1clearance"); // 1次DIPｸﾘｱﾗﾝｽ設定
            mapping.put("Dip1SkeegeSettei", "dip1skeegesettei"); // 1次DIPｽｷｰｼﾞ設定
            mapping.put("Dip1BlotClearance", "dip1blotclearance"); // 1次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
            mapping.put("Dip1Reveler", "dip1reveler"); // 1次ﾚﾍﾞﾗｰ設定
            mapping.put("Dip2AtamaDashiSettei", "dip2atamadashisettei"); // 2次頭出し設定
            mapping.put("Dip2Clearance", "dip2clearance"); // 2次DIPｸﾘｱﾗﾝｽ設定
            mapping.put("Dip2SkeegeSettei", "dip2skeegesettei"); // 2次DIPｽｷｰｼﾞ設定
            mapping.put("Dip2BlotClearance", "dip2blotclearance"); // 2次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
            mapping.put("Dip2Reveler", "dip2reveler"); // 2次ﾚﾍﾞﾗｰ設定
            mapping.put("psunpou2k", "psunpou2k"); // P寸法11
            mapping.put("psunpou2l", "psunpou2l"); // P寸法12
            mapping.put("psunpou2m", "psunpou2m"); // P寸法13
            mapping.put("psunpou2n", "psunpou2n"); // P寸法14
            mapping.put("psunpou2o", "psunpou2o"); // P寸法15
            mapping.put("psunpou2p", "psunpou2p"); // P寸法16
            mapping.put("psunpou2q", "psunpou2q"); // P寸法17
            mapping.put("psunpou2r", "psunpou2r"); // P寸法18
            mapping.put("psunpou2s", "psunpou2s"); // P寸法19
            mapping.put("psunpou2t", "psunpou2t"); // P寸法20
            mapping.put("psunpou2u", "psunpou2u"); // P寸法21
            mapping.put("psunpou2v", "psunpou2v"); // P寸法22
            mapping.put("psunpou2w", "psunpou2w"); // P寸法23
            mapping.put("psunpou2x", "psunpou2x"); // P寸法24
            mapping.put("psunpou2y", "psunpou2y"); // P寸法25
            mapping.put("psunpou2z", "psunpou2z"); // P寸法26
            mapping.put("psunpou2aa", "psunpou2aa"); // P寸法27
            mapping.put("psunpou2ab", "psunpou2ab"); // P寸法28
            mapping.put("psunpou2ac", "psunpou2ac"); // P寸法29
            mapping.put("psunpou2ad", "psunpou2ad"); // P寸法30
            mapping.put("psunpou2ae", "psunpou2ae"); // P寸法31
            mapping.put("psunpou2af", "psunpou2af"); // P寸法32
            mapping.put("psunpou2ag", "psunpou2ag"); // P寸法33
            mapping.put("psunpou2ah", "psunpou2ah"); // P寸法34
            mapping.put("psunpou2ai", "psunpou2ai"); // P寸法35
            mapping.put("psunpou2aj", "psunpou2aj"); // P寸法36
            mapping.put("psunpou2ak", "psunpou2ak"); // P寸法37
            mapping.put("psunpou2al", "psunpou2al"); // P寸法38
            mapping.put("psunpou2am", "psunpou2am"); // P寸法39
            mapping.put("psunpou2an", "psunpou2an"); // P寸法40
            mapping.put("lsunpou2k", "lsunpou2k"); // L寸法11
            mapping.put("lsunpou2l", "lsunpou2l"); // L寸法12
            mapping.put("lsunpou2m", "lsunpou2m"); // L寸法13
            mapping.put("lsunpou2n", "lsunpou2n"); // L寸法14
            mapping.put("lsunpou2o", "lsunpou2o"); // L寸法15
            mapping.put("lsunpou2p", "lsunpou2p"); // L寸法16
            mapping.put("lsunpou2q", "lsunpou2q"); // L寸法17
            mapping.put("lsunpou2r", "lsunpou2r"); // L寸法18
            mapping.put("lsunpou2s", "lsunpou2s"); // L寸法19
            mapping.put("lsunpou2t", "lsunpou2t"); // L寸法20
            mapping.put("lsunpou2u", "lsunpou2u"); // L寸法21
            mapping.put("lsunpou2v", "lsunpou2v"); // L寸法22
            mapping.put("lsunpou2w", "lsunpou2w"); // L寸法23
            mapping.put("lsunpou2x", "lsunpou2x"); // L寸法24
            mapping.put("lsunpou2y", "lsunpou2y"); // L寸法25
            mapping.put("lsunpou2z", "lsunpou2z"); // L寸法26
            mapping.put("lsunpou2aa", "lsunpou2aa"); // L寸法27
            mapping.put("lsunpou2ab", "lsunpou2ab"); // L寸法28
            mapping.put("lsunpou2ac", "lsunpou2ac"); // L寸法29
            mapping.put("lsunpou2ad", "lsunpou2ad"); // L寸法30
            mapping.put("lsunpou2ae", "lsunpou2ae"); // L寸法31
            mapping.put("lsunpou2af", "lsunpou2af"); // L寸法32
            mapping.put("lsunpou2ag", "lsunpou2ag"); // L寸法33
            mapping.put("lsunpou2ah", "lsunpou2ah"); // L寸法34
            mapping.put("lsunpou2ai", "lsunpou2ai"); // L寸法35
            mapping.put("lsunpou2aj", "lsunpou2aj"); // L寸法36
            mapping.put("lsunpou2ak", "lsunpou2ak"); // L寸法37
            mapping.put("lsunpou2al", "lsunpou2al"); // L寸法38
            mapping.put("lsunpou2am", "lsunpou2am"); // L寸法39
            mapping.put("lsunpou2an", "lsunpou2an"); // L寸法40
            mapping.put("lsunpourange2", "lsunpourange2"); // L寸法RANGE
            mapping.put("lsunpoumin2", "lsunpoumin2"); // L寸法MIN
            mapping.put("lsunpoumax2", "lsunpoumax2"); // L寸法MAX
            mapping.put("wtsunpou2a", "wtsunpou2a"); // WT寸法1
            mapping.put("wtsunpou2b", "wtsunpou2b"); // WT寸法2
            mapping.put("wtsunpou2c", "wtsunpou2c"); // WT寸法3
            mapping.put("wtsunpou2d", "wtsunpou2d"); // WT寸法4
            mapping.put("wtsunpou2e", "wtsunpou2e"); // WT寸法5
            mapping.put("wtsunpou2f", "wtsunpou2f"); // WT寸法6
            mapping.put("wtsunpou2g", "wtsunpou2g"); // WT寸法7
            mapping.put("wtsunpou2h", "wtsunpou2h"); // WT寸法8
            mapping.put("wtsunpou2i", "wtsunpou2i"); // WT寸法9
            mapping.put("wtsunpou2j", "wtsunpou2j"); // WT寸法10
            mapping.put("wtsunpou2k", "wtsunpou2k"); // WT寸法11
            mapping.put("wtsunpou2l", "wtsunpou2l"); // WT寸法12
            mapping.put("wtsunpou2m", "wtsunpou2m"); // WT寸法13
            mapping.put("wtsunpou2n", "wtsunpou2n"); // WT寸法14
            mapping.put("wtsunpou2o", "wtsunpou2o"); // WT寸法15
            mapping.put("wtsunpou2p", "wtsunpou2p"); // WT寸法16
            mapping.put("wtsunpou2q", "wtsunpou2q"); // WT寸法17
            mapping.put("wtsunpou2r", "wtsunpou2r"); // WT寸法18
            mapping.put("wtsunpou2s", "wtsunpou2s"); // WT寸法19
            mapping.put("wtsunpou2t", "wtsunpou2t"); // WT寸法20
            mapping.put("wtsunpou2u", "wtsunpou2u"); // WT寸法21
            mapping.put("wtsunpou2v", "wtsunpou2v"); // WT寸法22
            mapping.put("wtsunpou2w", "wtsunpou2w"); // WT寸法23
            mapping.put("wtsunpou2x", "wtsunpou2x"); // WT寸法24
            mapping.put("wtsunpou2y", "wtsunpou2y"); // WT寸法25
            mapping.put("wtsunpou2z", "wtsunpou2z"); // WT寸法26
            mapping.put("wtsunpou2aa", "wtsunpou2aa"); // WT寸法27
            mapping.put("wtsunpou2ab", "wtsunpou2ab"); // WT寸法28
            mapping.put("wtsunpou2ac", "wtsunpou2ac"); // WT寸法29
            mapping.put("wtsunpou2ad", "wtsunpou2ad"); // WT寸法30
            mapping.put("wtsunpou2ae", "wtsunpou2ae"); // WT寸法31
            mapping.put("wtsunpou2af", "wtsunpou2af"); // WT寸法32
            mapping.put("wtsunpou2ag", "wtsunpou2ag"); // WT寸法33
            mapping.put("wtsunpou2ah", "wtsunpou2ah"); // WT寸法34
            mapping.put("wtsunpou2ai", "wtsunpou2ai"); // WT寸法35
            mapping.put("wtsunpou2aj", "wtsunpou2aj"); // WT寸法36
            mapping.put("wtsunpou2ak", "wtsunpou2ak"); // WT寸法37
            mapping.put("wtsunpou2al", "wtsunpou2al"); // WT寸法38
            mapping.put("wtsunpou2am", "wtsunpou2am"); // WT寸法39
            mapping.put("wtsunpou2an", "wtsunpou2an"); // WT寸法40
            mapping.put("wtsunpouave2", "wtsunpouave2"); // WT寸法AVE
            mapping.put("wtsunpourange2", "wtsunpourange2"); // WT寸法RANGE
            mapping.put("wtsunpoumin2", "wtsunpoumin2"); // WT寸法MIN
            mapping.put("wtsunpoumax2", "wtsunpoumax2"); // WT寸法MAX
            mapping.put("psunpoumin2", "psunpoumin2"); // P寸法MIN
            mapping.put("psunpoumax2", "psunpoumax2"); // P寸法MAX
            mapping.put("setubisyurui", "setubisyurui"); // 設備種類
            mapping.put("tofukaisuu", "tofukaisuu"); // 塗布回数
            mapping.put("hojijigu", "hojijigu"); // 保持ｼﾞｸﾞ
            mapping.put("nentyakusheetlot1", "nentyakusheetlot1"); // 粘着ｼｰﾄﾛｯﾄ1次側
            mapping.put("nentyakusheetlot2", "nentyakusheetlot2"); // 粘着ｼｰﾄﾛｯﾄ2次側
            mapping.put("tofujigutorikosuu", "tofujigutorikosuu"); // 塗布ｼﾞｸﾞ取り個数
            mapping.put("StartTantosyacode", "starttantosyacode"); // 開始担当者
            mapping.put("StartKakuninsyacode", "startkakuninsyacode"); // 開始確認者
            mapping.put("juryou", "juryou"); // 重量
            mapping.put("syorikosuu", "syorikosuu"); // 処理個数
            mapping.put("EndTantosyacode", "endtantosyacode"); // 終了担当者
            mapping.put("pasteatsumi1ji", "pasteatsumi1ji"); // 1次ﾍﾟｰｽﾄ厚み設定値
            mapping.put("pasteatsumi2ji", "pasteatsumi2ji"); // 2次ﾍﾟｰｽﾄ厚み設定値
            mapping.put("atsumiinkua", "atsumiinkua"); // ｲﾝｸ厚みA
            mapping.put("atsumiinkub", "atsumiinkub"); // ｲﾝｸ厚みB
            mapping.put("kaisuu", "kaisuu"); // 回数

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B026Model>> beanHandler
                    = new BeanListHandler<>(GXHDO201B026Model.class, rowProcessor);

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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b026.json"));
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "外部電極・外部電極塗布");

            // ダウンロードファイル名
            String downloadFileName = "外部電極・外部電極塗布_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 11);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 11, 14));
        }
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
        String paramTantousya = StringUtil.blankToNull(getTantousya());
        String paramKcpno = null;
        if (!StringUtil.isEmpty(kcpNo)) {
            paramKcpno = "%" + DBUtil.escapeString(getKcpNo()) + "%";
        }
        String paramGouki = StringUtil.blankToNull(getGouki());

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));
        params.addAll(Arrays.asList(paramEndDateF, paramEndDateF));
        params.addAll(Arrays.asList(paramEndDateT, paramEndDateT));
        params.addAll(Arrays.asList(paramTantousya, paramTantousya));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
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
}
