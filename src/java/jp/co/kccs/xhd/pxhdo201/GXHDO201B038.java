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
import jp.co.kccs.xhd.db.model.SrMkmakuatsu;
import jp.co.kccs.xhd.model.GXHDO201B038Model;
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
 * 外部電極・ﾒｯｷ品質検査履歴検索画面
 *
 * @author 863 F.Zhang
 * @since 2019/12/02
 */
@Named
@ViewScoped
public class GXHDO201B038 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO201B038.class.getName());

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
    private List<GXHDO201B038Model> listData = null;

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
     * 検索条件：ﾒｯｷ開始時刻(TO)
     */
    private String gouki = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B038() {
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
    public List<GXHDO201B038Model> getListData() {
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B038_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B038_display_page_count", session));
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
        // 号機
        if (existError(validateUtil.checkC101(getGouki(), "号機", 4))) {
            return;
        }
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
                FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "号機"), null);
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
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM sr_mekki "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR mekkikaishinichiji >= ?) "
                    + "AND   (? IS NULL OR mekkikaishinichiji <= ?) "
                    + "AND   (? IS NULL OR gouki = ?) ";

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
            String sql = "SELECT "
                    + "  CONCAT(IFNULL(KOJYO, ''), IFNULL(LOTNO, ''), IFNULL(EDABAN, '')) AS LOTNO "
                    + "  , kcpno "
                    + "  , ukeiresuu "
                    + "  , domekosuu "
                    + "  , gouki "
                    + "  , tantousya "
                    + "  , mekkikaishinichiji "
                    + "  , mekkijyoukennia "
                    + "  , mekkijyoukenniam "
                    + "  , mekkijyoukensna "
                    + "  , mekkijyoukensnam "
                    + "  , shukkakosuu "
                    + "  , budomari "
                    + "  , makuatsunimin "
                    + "  , makuatsunimax "
                    + "  , makuatsuniave "
                    + "  , makuatsunistd "
                    + "  , makuatsusnmin "
                    + "  , makuatsusnmax "
                    + "  , makuatsusnave "
                    + "  , makuatsusnstd "
                    + "  , nurekensakekka "
                    + "  , tainetsukensakekka "
                    + "  , gaikankensakekka "
                    + "  , bikou1 "
                    + "  , bikou2 "
                    + "  , bikou3 "
                    + "  , jissekino "
                    + "  , domemeisai "
                    + "  , tyoseimaeph1 "
                    + "  , tyoseigoph1 "
                    + "  , tyoseijikan1 "
                    + "  , tyoseimaeph2 "
                    + "  , tyoseigoph2 "
                    + "  , tyoseijikan2 "
                    + "  , tsunpou "
                    + "  , barrelno "
                    + "  , makuatsunicpl "
                    + "  , makuatsusncpl "
                    + "  , sokuteinichiji "
                    + "  , makuatsunicv "
                    + "  , makuatsusncv "
                    + "  , kensanichiji "
                    + "  , kensatantousya "
                    + "  , makuatsutantosya "
                    + "  , kaishinichiji_sn "
                    + "  , tokuisaki "
                    + "  , lotkubuncode "
                    + "  , ownercode "
                    + "  , ukeiretannijyuryo "
                    + "  , ukeiresoujyuryou "
                    + "  , mekkibasyo "
                    + "  , mekkibasyosetubi "
                    + "  , mekkisyuryounichiji "
                    + "  , syuryousya "
                    + "  , kensatannijyuryo "
                    + "  , kensasoujyuryou "
                    + "  , netusyorijyouken "
                    + "  , netusyorikaisinichiji "
                    + "  , netusyoritantousya "
                    + "  , jisyakusenbetukaisinichiji "
                    + "  , jisyakusenbetutantousya "
                    + "  , ijouhakkou "
                    + "  , ijourank "
                    + "  , makuatsukakunin "
                    + "  , testhin "
                    + "  , tsunpouave "
                    + "FROM sr_mekki "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR mekkikaishinichiji >= ?) "
                    + "AND   (? IS NULL OR mekkikaishinichiji <= ?) "
                    + "AND   (? IS NULL OR gouki = ?) "
                    + "ORDER BY "
                    + "  kojyo "
                    + "  , lotno "
                    + "  , edaban ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("LOTNO", "lotno"); // ﾛｯﾄNo.
            mapping.put("kcpno", "kcpno"); // KCPNO
            mapping.put("ukeiresuu", "ukeiresuu"); // 処理数(個)
            mapping.put("domekosuu", "domekosuu"); // ﾄﾞｰﾑ個数
            mapping.put("gouki", "gouki"); // 号機
            mapping.put("tantousya", "starttantosyacode"); // 開始担当者
            mapping.put("mekkikaishinichiji", "mekkikaishinichiji"); // ﾒｯｷ開始日時
            mapping.put("mekkijyoukennia", "mekkijyoukennia"); // 条件NI(A)
            mapping.put("mekkijyoukenniam", "mekkijyoukenniam"); // 条件NI(AM)
            mapping.put("mekkijyoukensna", "mekkijyoukensna"); // 条件SN(A)
            mapping.put("mekkijyoukensnam", "mekkijyoukensnam"); // 条件SN(AM)
            mapping.put("shukkakosuu", "shukkakosuu"); // 良品数
            mapping.put("budomari", "budomari"); // 歩留まり
            mapping.put("makuatsunimin", "makuatsunimin"); // Ni膜厚(MIN)
            mapping.put("makuatsunimax", "makuatsunimax"); // Ni膜厚(MAX)
            mapping.put("makuatsuniave", "makuatsuniave"); // Ni膜厚(AVE)
            mapping.put("makuatsunistd", "makuatsunistd"); // Ni膜厚(STD)
            mapping.put("makuatsusnmin", "makuatsusnmin"); // Sn膜厚(MIN)
            mapping.put("makuatsusnmax", "makuatsusnmax"); // Sn膜厚(MAX)
            mapping.put("makuatsusnave", "makuatsusnave"); // Sn膜厚(AVE)
            mapping.put("makuatsusnstd", "makuatsusnstd"); // Sn膜厚(STD)
            mapping.put("nurekensakekka", "nurekensakekka"); // 半田ﾇﾚ性
            mapping.put("tainetsukensakekka", "tainetsukensakekka"); // 半田耐熱性
            mapping.put("gaikankensakekka", "gaikankensakekka"); // 外観
            mapping.put("bikou1", "biko1"); // 備考1
            mapping.put("bikou2", "biko2"); // 備考2
            mapping.put("bikou3", "biko3"); // 備考3
            mapping.put("jissekino", "jissekino"); // 回数
            mapping.put("domemeisai", "domemeisai"); // 使用ﾄﾞｰﾑ明細
            mapping.put("tyoseimaeph1", "tyoseimaeph1"); // 1回目調整前PH値
            mapping.put("tyoseigoph1", "tyoseigoph1"); // 1回目調整後PH値
            mapping.put("tyoseijikan1", "tyoseijikan1"); // 1回目調整時間
            mapping.put("tyoseimaeph2", "tyoseimaeph2"); // 2回目調整前PH値
            mapping.put("tyoseigoph2", "tyoseigoph2"); // 2回目調整後PH値
            mapping.put("tyoseijikan2", "tyoseijikan2"); // 2回目調整時間
            mapping.put("tsunpou", "tsunpou"); // Ｔ寸法
            mapping.put("barrelno", "barrelno"); // ﾊﾞﾚﾙNo
            mapping.put("makuatsunicpl", "makuatsunicpl"); // Ni膜厚(CPL)
            mapping.put("makuatsusncpl", "makuatsusncpl"); // Sn膜厚(CPL)
            mapping.put("sokuteinichiji", "sokuteinichiji"); // 測定日時
            mapping.put("makuatsunicv", "makuatsunicv"); // Ni膜厚(CV)
            mapping.put("makuatsusncv", "makuatsusncv"); // Sn膜厚(CV)
            mapping.put("kensanichiji", "kensanichiji"); // 検査日時
            mapping.put("kensatantousya", "kensatantousya"); // 検査・外観担当者
            mapping.put("makuatsutantosya", "makuatsutantosya"); // 膜厚担当者
            mapping.put("kaishinichiji_sn", "kaishinichiji_sn"); // Sn開始日時
            mapping.put("tokuisaki", "tokuisaki"); // 客先
            mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
            mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量(g/100個)
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量(g)
            mapping.put("mekkibasyo", "mekkibasyo"); // ﾒｯｷ場所
            mapping.put("mekkibasyosetubi", "mekkibasyosetubi"); // ﾒｯｷ場所設備
            mapping.put("mekkisyuryounichiji", "mekkisyuryounichiji"); // ﾒｯｷ終了日時
            mapping.put("syuryousya", "syuryousya"); // 終了担当者
            mapping.put("kensatannijyuryo", "kensatannijyuryo"); // 検査単位重量
            mapping.put("kensasoujyuryou", "kensasoujyuryou"); // 検査総重量
            mapping.put("netusyorijyouken", "netusyorijyouken"); // 熱処理条件
            mapping.put("netusyorikaisinichiji", "netusyorikaisinichiji"); // 熱処理開始日時
            mapping.put("netusyoritantousya", "netusyoritantousya"); // 熱処理担当者
            mapping.put("jisyakusenbetukaisinichiji", "jisyakusenbetukaisinichiji"); // 磁石選別開始日時
            mapping.put("jisyakusenbetutantousya", "jisyakusenbetutantousya"); // 磁石選別担当者
            mapping.put("ijouhakkou", "ijouhakkou"); // 異常発行
            mapping.put("ijourank", "ijourank"); // 異常品ﾗﾝｸ
            mapping.put("makuatsukakunin", "makuatsukakunin"); // 膜厚確認
            mapping.put("testhin", "testhin"); // ﾃｽﾄ品
            mapping.put("tsunpouave", "tsunpouave"); // T寸法AVE(mm)
            mapping.put("mekkisyurui", "mekkisyurui"); // ﾒｯｷ種類
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

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B038Model>> beanHandler
                    = new BeanListHandler<>(GXHDO201B038Model.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
            
            // 取得したLotNo分の膜厚データを取得する
            for (GXHDO201B038Model data : listData) {
                
                // パラメータ設定
                List<Object> paramsMk = createMakuatsuSearchParam(data.getLotno());

                // 膜厚データの取得
                String sqlMk = "SELECT "
                        + "  SRMK_BMIN.SokuteiNo"
                        + " ,SRMK_BMIN.NiMakuatsu"
                        + " ,SRMK_BMIN.SnMakuatsu"
                        + " FROM  sr_mkmakuatsu SRMK_BMIN"
                        + " WHERE SRMK_BMIN.Kojyo = ?"
                        + " AND   SRMK_BMIN.LotNo = ?"
                        + " AND   SRMK_BMIN.Edaban = ?"
                        + " AND   SRMK_BMIN.Barelno = 1"
                        + " AND   SRMK_BMIN.SokuteiKaisuu = ( SELECT MAX(SRMK_SMAX.sokuteikaisuu) "
                        + "                  FROM   sr_mkmakuatsu SRMK_SMAX "
                        + "                  WHERE  SRMK_SMAX.kojyo = SRMK_BMIN.kojyo "
                        + "                  AND    SRMK_SMAX.lotno = SRMK_BMIN.lotno "
                        + "                  AND    SRMK_SMAX.edaban = SRMK_BMIN.edaban "
                        + "                  GROUP BY "
                        + "                         SRMK_SMAX.kojyo "
                        + "                         , SRMK_SMAX.lotno "
                        + "                         , SRMK_SMAX.edaban )"
                        + " ORDER BY SokuteiNo";

                DBUtil.outputSQLLog(sqlMk, paramsMk.toArray(), LOGGER);
                List<Map<String, Object>> result = queryRunner.query(sqlMk, new MapListHandler(), paramsMk.toArray());
                
                // 膜厚データが取得された場合にmodelにセットする
                for (Map mkData : result) {
                    
                    int sokuteiNo = (int) mkData.get("SokuteiNo");
                    
                    switch (sokuteiNo) {
                        case 1:
                            data.setMakuatsuni01((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn01((BigDecimal)mkData.get("SnMakuatsu"));
                        case 2:
                            data.setMakuatsuni02((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn02((BigDecimal)mkData.get("SnMakuatsu"));
                        case 3:
                            data.setMakuatsuni03((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn03((BigDecimal)mkData.get("SnMakuatsu"));
                        case 4:
                            data.setMakuatsuni04((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn04((BigDecimal)mkData.get("SnMakuatsu"));
                        case 5:
                            data.setMakuatsuni05((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn05((BigDecimal)mkData.get("SnMakuatsu"));
                        case 6:
                            data.setMakuatsuni06((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn06((BigDecimal)mkData.get("SnMakuatsu"));
                        case 7:
                            data.setMakuatsuni07((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn07((BigDecimal)mkData.get("SnMakuatsu"));
                        case 8:
                            data.setMakuatsuni08((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn08((BigDecimal)mkData.get("SnMakuatsu"));
                        case 9:
                            data.setMakuatsuni09((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn09((BigDecimal)mkData.get("SnMakuatsu"));
                        case 10:
                            data.setMakuatsuni10((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn10((BigDecimal)mkData.get("SnMakuatsu"));
                        case 11:
                            data.setMakuatsuni11((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn11((BigDecimal)mkData.get("SnMakuatsu"));
                        case 12:
                            data.setMakuatsuni12((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn12((BigDecimal)mkData.get("SnMakuatsu"));
                        case 13:
                            data.setMakuatsuni13((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn13((BigDecimal)mkData.get("SnMakuatsu"));
                        case 14:
                            data.setMakuatsuni14((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn14((BigDecimal)mkData.get("SnMakuatsu"));
                        case 15:
                            data.setMakuatsuni15((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn15((BigDecimal)mkData.get("SnMakuatsu"));
                        case 16:
                            data.setMakuatsuni16((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn16((BigDecimal)mkData.get("SnMakuatsu"));
                        case 17:
                            data.setMakuatsuni17((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn17((BigDecimal)mkData.get("SnMakuatsu"));
                        case 18:
                            data.setMakuatsuni18((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn18((BigDecimal)mkData.get("SnMakuatsu"));
                        case 19:
                            data.setMakuatsuni19((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn19((BigDecimal)mkData.get("SnMakuatsu"));
                        case 20:
                            data.setMakuatsuni20((BigDecimal)mkData.get("NiMakuatsu"));
                            data.setMakuatsusn20((BigDecimal)mkData.get("SnMakuatsu"));
                    }
                }
            }
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b038.json"));
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "外部電極・メッキ品質検査");

            // ダウンロードファイル名
            String downloadFileName = "外部電極・メッキ品質検査_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";

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
        if (!StringUtil.isEmpty(startMekkiDateF)) {
            paramStartDateF = DateUtil.convertStringToDate(getStartMekkiDateF(), StringUtil.isEmpty(getStartMekkiTimeF()) ? "0000" : getStartMekkiTimeF());
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startMekkiDateT)) {
            paramStartDateT = DateUtil.convertStringToDate(getStartMekkiDateT(), StringUtil.isEmpty(getStartMekkiTimeT()) ? "2359" : getStartMekkiTimeT());
        }
        String paramGouki = StringUtil.blankToNull(getGouki());
        
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
     * 膜厚用検索パラメータ生成
     *
     * @return パラメータ
     */
    private List<Object> createMakuatsuSearchParam(String mklotNo) {
        // パラメータ設定
        String paramKojo = StringUtils.substring(mklotNo, 0, 3);
        String paramLotNo = StringUtils.substring(mklotNo, 3, 11);
        String paramEdaban = StringUtil.blankToNull(StringUtils.substring(mklotNo, 11, 14));

        List<Object> params = new ArrayList<>();
        params.add(paramKojo);
        params.add(paramLotNo);
        params.add(paramEdaban);

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
