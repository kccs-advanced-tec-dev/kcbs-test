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
                    + "AND   (? IS NULL OR mekkikaishinichiji <= ?) ";

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
            String sql = "SELECT CONCAT(IFNULL(M.KOJYO, ''), IFNULL(M.LOTNO, ''), IFNULL(M.EDABAN, '')) AS LOTNO"
                    + ", M.kcpno "
                    + ", M.ukeiresuu "
                    + ", M.domekosuu "
                    + ", M.gouki "
                    + ", M.tantousya "
                    + ", M.mekkikaishinichiji "
                    + ", M.mekkijyoukennia "
                    + ", M.mekkijyoukenniam "
                    + ", M.mekkijyoukensna "
                    + ", M.mekkijyoukensnam "
                    + ", M.shukkakosuu "
                    + ", M.budomari "
                    + ", M.makuatsunimin "
                    + ", M.makuatsunimax "
                    + ", M.makuatsuniave "
                    + ", M.makuatsunistd "
                    + ", M.makuatsusnmin "
                    + ", M.makuatsusnmax "
                    + ", M.makuatsusnave "
                    + ", M.makuatsusnstd "
                    + ", M.nurekensakekka "
                    + ", M.tainetsukensakekka "
                    + ", M.gaikankensakekka "
                    + ", M.bikou1 "
                    + ", M.bikou2 "
                    + ", M.bikou3 "
                    + ", M.jissekino "
                    + ", M.domemeisai "
                    + ", M.tyoseimaeph1 "
                    + ", M.tyoseigoph1 "
                    + ", M.tyoseijikan1 "
                    + ", M.tyoseimaeph2 "
                    + ", M.tyoseigoph2 "
                    + ", M.tyoseijikan2 "
                    + ", M.tsunpou "
                    + ", M.barrelno "
                    + ", M.makuatsunicpl "
                    + ", M.makuatsusncpl "
                    + ", M.sokuteinichiji "
                    + ", M.makuatsunicv "
                    + ", M.makuatsusncv "
                    + ", M.kensanichiji "
                    + ", M.kensatantousya "
                    + ", M.makuatsutantosya "
                    + ", M.kaishinichiji_sn "
                    + ", M.tokuisaki "
                    + ", M.lotkubuncode "
                    + ", M.ownercode "
                    + ", M.ukeiretannijyuryo "
                    + ", M.ukeiresoujyuryou "
                    + ", M.mekkibasyo "
                    + ", M.mekkibasyosetubi "
                    + ", M.mekkisyuryounichiji "
                    + ", M.syuryousya "
                    + ", M.kensatannijyuryo "
                    + ", M.kensasoujyuryou "
                    + ", M.netusyorijyouken "
                    + ", M.netusyorikaisinichiji "
                    + ", M.netusyoritantousya "
                    + ", M.jisyakusenbetukaisinichiji "
                    + ", M.jisyakusenbetutantousya "
                    + ", M.ijouhakkou "
                    + ", M.ijourank "
                    + ", M.makuatsukakunin "
                    + ", M.testhin "
                    + ", M.tsunpouave "
                    + ", M.mekkisyurui  "
                    + ", (CASE WHEN T1.sokuteino = '1' THEN T1.nimakuatsu ELSE NULL END) makuatsuni01 "
                    + ", (CASE WHEN T2.sokuteino = '2' THEN T2.nimakuatsu ELSE NULL END) makuatsuni02 "
                    + ", (CASE WHEN T3.sokuteino = '3' THEN T3.nimakuatsu ELSE NULL END) makuatsuni03 "
                    + ", (CASE WHEN T4.sokuteino = '4' THEN T4.nimakuatsu ELSE NULL END) makuatsuni04 "
                    + ", (CASE WHEN T5.sokuteino = '5' THEN T5.nimakuatsu ELSE NULL END) makuatsuni05 "
                    + ", (CASE WHEN T6.sokuteino = '6' THEN T6.nimakuatsu ELSE NULL END) makuatsuni06 "
                    + ", (CASE WHEN T7.sokuteino = '7' THEN T7.nimakuatsu ELSE NULL END) makuatsuni07 "
                    + ", (CASE WHEN T8.sokuteino = '8' THEN T8.nimakuatsu ELSE NULL END) makuatsuni08 "
                    + ", (CASE WHEN T9.sokuteino = '9' THEN T9.nimakuatsu ELSE NULL END) makuatsuni09 "
                    + ", (CASE WHEN T10.sokuteino = '10' THEN T10.nimakuatsu ELSE NULL END) makuatsuni10 "
                    + ", (CASE WHEN T11.sokuteino = '11' THEN T11.nimakuatsu ELSE NULL END) makuatsuni11 "
                    + ", (CASE WHEN T12.sokuteino = '12' THEN T12.nimakuatsu ELSE NULL END) makuatsuni12 "
                    + ", (CASE WHEN T13.sokuteino = '13' THEN T13.nimakuatsu ELSE NULL END) makuatsuni13 "
                    + ", (CASE WHEN T14.sokuteino = '14' THEN T14.nimakuatsu ELSE NULL END) makuatsuni14 "
                    + ", (CASE WHEN T15.sokuteino = '15' THEN T15.nimakuatsu ELSE NULL END) makuatsuni15 "
                    + ", (CASE WHEN T16.sokuteino = '16' THEN T16.nimakuatsu ELSE NULL END) makuatsuni16 "
                    + ", (CASE WHEN T17.sokuteino = '17' THEN T17.nimakuatsu ELSE NULL END) makuatsuni17 "
                    + ", (CASE WHEN T18.sokuteino = '18' THEN T18.nimakuatsu ELSE NULL END) makuatsuni18 "
                    + ", (CASE WHEN T19.sokuteino = '19' THEN T19.nimakuatsu ELSE NULL END) makuatsuni19 "
                    + ", (CASE WHEN T20.sokuteino = '20' THEN T20.nimakuatsu ELSE NULL END) makuatsuni20 "
                    + ", (CASE WHEN T1.sokuteino = '1' THEN T1.snmakuatsu ELSE NULL END) makuatsusn01 "
                    + ", (CASE WHEN T2.sokuteino = '2' THEN T2.snmakuatsu ELSE NULL END) makuatsusn02 "
                    + ", (CASE WHEN T3.sokuteino = '3' THEN T3.snmakuatsu ELSE NULL END) makuatsusn03 "
                    + ", (CASE WHEN T4.sokuteino = '4' THEN T4.snmakuatsu ELSE NULL END) makuatsusn04 "
                    + ", (CASE WHEN T5.sokuteino = '5' THEN T5.snmakuatsu ELSE NULL END) makuatsusn05 "
                    + ", (CASE WHEN T6.sokuteino = '6' THEN T6.snmakuatsu ELSE NULL END) makuatsusn06 "
                    + ", (CASE WHEN T7.sokuteino = '7' THEN T7.snmakuatsu ELSE NULL END) makuatsusn07 "
                    + ", (CASE WHEN T8.sokuteino = '8' THEN T8.snmakuatsu ELSE NULL END) makuatsusn08 "
                    + ", (CASE WHEN T9.sokuteino = '9' THEN T9.snmakuatsu ELSE NULL END) makuatsusn09 "
                    + ", (CASE WHEN T10.sokuteino = '10' THEN T10.snmakuatsu ELSE NULL END) makuatsusn10 "
                    + ", (CASE WHEN T11.sokuteino = '11' THEN T11.snmakuatsu ELSE NULL END) makuatsusn11 "
                    + ", (CASE WHEN T12.sokuteino = '12' THEN T12.snmakuatsu ELSE NULL END) makuatsusn12 "
                    + ", (CASE WHEN T13.sokuteino = '13' THEN T13.snmakuatsu ELSE NULL END) makuatsusn13 "
                    + ", (CASE WHEN T14.sokuteino = '14' THEN T14.snmakuatsu ELSE NULL END) makuatsusn14 "
                    + ", (CASE WHEN T15.sokuteino = '15' THEN T15.snmakuatsu ELSE NULL END) makuatsusn15 "
                    + ", (CASE WHEN T16.sokuteino = '16' THEN T16.snmakuatsu ELSE NULL END) makuatsusn16 "
                    + ", (CASE WHEN T17.sokuteino = '17' THEN T17.snmakuatsu ELSE NULL END) makuatsusn17 "
                    + ", (CASE WHEN T18.sokuteino = '18' THEN T18.snmakuatsu ELSE NULL END) makuatsusn18 "
                    + ", (CASE WHEN T19.sokuteino = '19' THEN T19.snmakuatsu ELSE NULL END) makuatsusn19 "
                    + ", (CASE WHEN T20.sokuteino = '20' THEN T20.snmakuatsu ELSE NULL END) makuatsusn20 "
                    + " FROM sr_mekki M LEFT JOIN  "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 1 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(mk1.sokuteikaisuu) from sr_mkmakuatsu mk1  "
                    + "  WHERE srmk.kojyo = mk1.kojyo "
                    + "  AND srmk.lotno = mk1.lotno "
                    + "  AND srmk.edaban = mk1.edaban)  "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T1  "
                    + "  ON M.kojyo = T1.kojyo "
                    + "  AND M.lotno = T1.lotno "
                    + "  AND M.edaban = T1.edaban "
                    + "  LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 2 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu mk2 "
                    + "  WHERE srmk.kojyo = mk2.kojyo "
                    + "  AND srmk.lotno = mk2.lotno "
                    + "  AND srmk.edaban = mk2.edaban)   "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T2  "
                    + "  ON M.kojyo = T2.kojyo "
                    + "  AND M.lotno = T2.lotno "
                    + "  AND M.edaban = T2.edaban "
                    + "  LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 3 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu mk3 "
                    + "  WHERE srmk.kojyo = mk3.kojyo "
                    + "  AND srmk.lotno = mk3.lotno "
                    + "  AND srmk.edaban = mk3.edaban)   "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T3  "
                    + "  ON M.kojyo = T3.kojyo "
                    + "  AND M.lotno = T3.lotno "
                    + "  AND M.edaban = T3.edaban "
                    + "  LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 4 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu mk4 "
                    + "  WHERE srmk.kojyo = mk4.kojyo "
                    + "  AND srmk.lotno = mk4.lotno "
                    + "  AND srmk.edaban = mk4.edaban)   "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T4  "
                    + "  ON M.kojyo = T4.kojyo "
                    + "  AND M.lotno = T4.lotno "
                    + "  AND M.edaban = T4.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 5 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk5 "
                    + "  WHERE srmk.kojyo = mk5.kojyo "
                    + "  AND srmk.lotno = mk5.lotno "
                    + "  AND srmk.edaban = mk5.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T5 "
                    + "  ON M.kojyo = T5.kojyo "
                    + "  AND M.lotno = T5.lotno "
                    + "  AND M.edaban = T5.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 6 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk6 "
                    + "  WHERE srmk.kojyo = mk6.kojyo "
                    + "  AND srmk.lotno = mk6.lotno "
                    + "  AND srmk.edaban = mk6.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T6 "
                    + "  ON M.kojyo = T6.kojyo "
                    + "  AND M.lotno = T6.lotno "
                    + "  AND M.edaban = T6.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 7 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk7 "
                    + "  WHERE srmk.kojyo = mk7.kojyo "
                    + "  AND srmk.lotno = mk7.lotno "
                    + "  AND srmk.edaban = mk7.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T7 "
                    + "  ON M.kojyo = T7.kojyo "
                    + "  AND M.lotno = T7.lotno "
                    + "  AND M.edaban = T7.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 8 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk8 "
                    + "  WHERE srmk.kojyo = mk8.kojyo "
                    + "  AND srmk.lotno = mk8.lotno "
                    + "  AND srmk.edaban = mk8.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T8 "
                    + "  ON M.kojyo = T8.kojyo "
                    + "  AND M.lotno = T8.lotno "
                    + "  AND M.edaban = T8.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 9 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk9 "
                    + "  WHERE srmk.kojyo = mk9.kojyo "
                    + "  AND srmk.lotno = mk9.lotno "
                    + "  AND srmk.edaban = mk9.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T9 "
                    + "  ON M.kojyo = T9.kojyo "
                    + "  AND M.lotno = T9.lotno "
                    + "  AND M.edaban = T9.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 10 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk10 "
                    + "  WHERE srmk.kojyo = mk10.kojyo "
                    + "  AND srmk.lotno = mk10.lotno "
                    + "  AND srmk.edaban = mk10.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T10 "
                    + "  ON M.kojyo = T10.kojyo "
                    + "  AND M.lotno = T10.lotno "
                    + "  AND M.edaban = T10.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 11 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk11 "
                    + "  WHERE srmk.kojyo = mk11.kojyo "
                    + "  AND srmk.lotno = mk11.lotno "
                    + "  AND srmk.edaban = mk11.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T11 "
                    + "  ON M.kojyo = T11.kojyo "
                    + "  AND M.lotno = T11.lotno "
                    + "  AND M.edaban = T11.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 12 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk12 "
                    + "  WHERE srmk.kojyo = mk12.kojyo "
                    + "  AND srmk.lotno = mk12.lotno "
                    + "  AND srmk.edaban = mk12.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T12 "
                    + "  ON M.kojyo = T12.kojyo "
                    + "  AND M.lotno = T12.lotno "
                    + "  AND M.edaban = T12.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 13 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk13 "
                    + "  WHERE srmk.kojyo = mk13.kojyo "
                    + "  AND srmk.lotno = mk13.lotno "
                    + "  AND srmk.edaban = mk13.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T13 "
                    + "  ON M.kojyo = T13.kojyo "
                    + "  AND M.lotno = T13.lotno "
                    + "  AND M.edaban = T13.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 14 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk14 "
                    + "  WHERE srmk.kojyo = mk14.kojyo "
                    + "  AND srmk.lotno = mk14.lotno "
                    + "  AND srmk.edaban = mk14.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T14 "
                    + "  ON M.kojyo = T14.kojyo "
                    + "  AND M.lotno = T14.lotno "
                    + "  AND M.edaban = T14.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 15 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk15 "
                    + "  WHERE srmk.kojyo = mk15.kojyo "
                    + "  AND srmk.lotno = mk15.lotno "
                    + "  AND srmk.edaban = mk15.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T15 "
                    + "  ON M.kojyo = T15.kojyo "
                    + "  AND M.lotno = T15.lotno "
                    + "  AND M.edaban = T15.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 16 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk16 "
                    + "  WHERE srmk.kojyo = mk16.kojyo "
                    + "  AND srmk.lotno = mk16.lotno "
                    + "  AND srmk.edaban = mk16.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T16 "
                    + "  ON M.kojyo = T16.kojyo "
                    + "  AND M.lotno = T16.lotno "
                    + "  AND M.edaban = T16.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 17 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk17 "
                    + "  WHERE srmk.kojyo = mk17.kojyo "
                    + "  AND srmk.lotno = mk17.lotno "
                    + "  AND srmk.edaban = mk17.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T17 "
                    + "  ON M.kojyo = T17.kojyo "
                    + "  AND M.lotno = T17.lotno "
                    + "  AND M.edaban = T17.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 18 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk18 "
                    + "  WHERE srmk.kojyo = mk18.kojyo "
                    + "  AND srmk.lotno = mk18.lotno "
                    + "  AND srmk.edaban = mk18.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T18 "
                    + "  ON M.kojyo = T18.kojyo "
                    + "  AND M.lotno = T18.lotno "
                    + "  AND M.edaban = T18.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 19 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu   mk19 "
                    + "  WHERE srmk.kojyo = mk19.kojyo "
                    + "  AND srmk.lotno = mk19.lotno "
                    + "  AND srmk.edaban = mk19.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T19 "
                    + "  ON M.kojyo = T19.kojyo "
                    + "  AND M.lotno = T19.lotno "
                    + "  AND M.edaban = T19.edaban "
                    + " LEFT JOIN    "
                    + "( "
                    + "select srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteikaisuu, srmk.sokuteino,  "
                    + "       min(srmk.barelno), srmk.nimakuatsu, srmk.snmakuatsu  "
                    + " from sr_mkmakuatsu srmk  "
                    + " WHERE "
                    + "   srmk.sokuteino = 20 "
                    + "  AND srmk.sokuteikaisuu =  "
                    + "  (select MAX(sokuteikaisuu) from sr_mkmakuatsu mk20 "
                    + "  WHERE srmk.kojyo = mk20.kojyo "
                    + "  AND srmk.lotno = mk20.lotno "
                    + "  AND srmk.edaban = mk20.edaban) "
                    + "  group by srmk.kojyo, srmk.lotno, srmk.edaban, srmk.sokuteino "
                    + "  ) T20 "
                    + "  ON M.kojyo = T20.kojyo "
                    + "  AND M.lotno = T20.lotno "
                    + "  AND M.edaban = T20.edaban "
                    + " WHERE (? IS NULL OR M.kojyo = ?) "
                    + " AND   (? IS NULL OR M.lotno = ?) "
                    + " AND   (? IS NULL OR M.edaban = ?) "
                    + " AND   (? IS NULL OR M.mekkikaishinichiji >= ?) "
                    + " AND   (? IS NULL OR M.mekkikaishinichiji <= ?) "
                    + " ORDER BY M.kojyo, M.lotno, M.edaban";

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
            ResultSetHandler<List<GXHDO201B038Model>> beanHandler
                    = new BeanListHandler<>(GXHDO201B038Model.class, rowProcessor);

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
