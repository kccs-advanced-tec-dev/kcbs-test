/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.model.GXHDO201B001Model;
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
 * システム名      印刷・SPSｸﾞﾗﾋﾞｱ(コンデンサ)<br>
 * <br>
 * 変更日        2019/1/29<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * GXHDO201B001(印刷・SPSｸﾞﾗﾋﾞｱ)
 * 
 * @author KCSS K.Jo
 * @since  2019/1/29
 */
@Named
@ViewScoped
public class GXHDO201B001 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B001.class.getName());
    
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
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B001Model> listData = null;
    
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 1ページ当たりの表示件数 */
    private int listDisplayPageCount = -1;
    
    /** 検索条件：ロットNo */
    private String lotNo = "";
    /** 検索条件：印刷開始日(FROM) */
    private String startDateF = "";
    /** 検索条件：印刷開始日(TO) */
    private String startDateT = "";
    /** 検索条件：印刷開始時刻(FROM) */
    private String startTimeF = "";
    /** 検索条件：印刷開始時刻(TO) */
    private String startTimeT = "";
    /** 検索条件：印刷終了日(FROM) */
    private String endDateF = "";
    /** 検索条件：印刷終了日(TO) */
    private String endDateT = "";
    /** 検索条件：印刷終了時刻(FROM) */
    private String endTimeF = "";
    /** 検索条件：印刷終了時刻(TO) */
    private String endTimeT = "";
    /** 検索条件：印刷号機 */
    private String gouki = "";
    /** 検索条件：KCPNo */
    private String kcpNo = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B001() {
    }

//<editor-fold defaultstate="collapsed" desc="#getter setter">
    /**
     * 警告メッセージ
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }
    
    /**
     * 一覧表示データ取得
     * @return 一覧表示データ
     */
    public List<GXHDO201B001Model> getListData() {
        return listData;
    }
    
    /**
     * 検索条件：ロットNo
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：ロットNo
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    
    /**
     * 検索条件：印刷開始日(FROM)
     * @return the startDateF
     */
    public String getStartDateF() {
        return startDateF;
    }

    /**
     * 検索条件：印刷開始日(FROM)
     * @param startDateF the startDateF to set
     */
    public void setStartDateF(String startDateF) {
        this.startDateF = startDateF;
    }

    /**
     * 検索条件：印刷開始日(TO)
     * @return the startDateT
     */
    public String getStartDateT() {
        return startDateT;
    }

    /**
     * 検索条件：印刷開始日(TO)
     * @param startDateT the startDateT to set
     */
    public void setStartDateT(String startDateT) {
        this.startDateT = startDateT;
    }

    /**
     * 検索条件：印刷開始時刻(FROM)
     * @return the startTimeF
     */
    public String getStartTimeF() {
        return startTimeF;
    }

    /**
     * 検索条件：印刷開始時刻(FROM)
     * @param startTimeF the startTimeF to set
     */
    public void setStartTimeF(String startTimeF) {
        this.startTimeF = startTimeF;
    }

    /**
     * 検索条件：印刷開始時刻(TO)
     * @return the startTimeT
     */
    public String getStartTimeT() {
        return startTimeT;
    }

    /**
     * 検索条件：印刷開始時刻(TO)
     * @param startTimeT the startTimeT to set
     */
    public void setStartTimeT(String startTimeT) {
        this.startTimeT = startTimeT;
    }

    /**
     * 検索条件：印刷終了日(FROM)
     * @return the endDateF
     */
    public String getEndDateF() {
        return endDateF;
    }

    /**
     * 検索条件：印刷終了日(FROM)
     * @param endDateF the endDateF to set
     */
    public void setEndDateF(String endDateF) {
        this.endDateF = endDateF;
    }

    /**
     * 検索条件：印刷終了日(TO)
     * @return the endDateT
     */
    public String getEndDateT() {
        return endDateT;
    }

    /**
     * 検索条件：印刷終了日(TO)
     * @param endDateT the endDateT to set
     */
    public void setEndDateT(String endDateT) {
        this.endDateT = endDateT;
    }

    /**
     * 検索条件：印刷終了時刻(FROM)
     * @return the endTimeF
     */
    public String getEndTimeF() {
        return endTimeF;
    }

    /**
     * 検索条件：印刷終了時刻(FROM)
     * @param endTimeF the endTimeF to set
     */
    public void setEndTimeF(String endTimeF) {
        this.endTimeF = endTimeF;
    }

    /**
     * 検索条件：印刷終了時刻(TO)
     * @return the endTimeT
     */
    public String getEndTimeT() {
        return endTimeT;
    }

    /**
     * 検索条件：印刷終了時刻(TO)
     * @param endTimeT the endTimeT to set
     */
    public void setEndTimeT(String endTimeT) {
        this.endTimeT = endTimeT;
    }
    
    /**
     * 検索条件：印刷号機
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 検索条件：印刷号機
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 検索条件：KCPNo
     * @return the kcpNo
     */
    public String getKcpNo() {
        return kcpNo;
    }

    /**
     * 検索条件：KCPNo
     * @param kcpNo the kcpNo to set
     */
    public void setKcpNo(String kcpNo) {
        this.kcpNo = kcpNo;
    }
//</editor-fold>

    /**
     * ページング用の件数を返却
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
        
        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
        
        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B001_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B001_display_page_count", session));
        }
        
        // 画面クリア
        clear();
    }
    
    /**
     * Excel保存ボタン非活性制御
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
        startDateF = "";
        startDateT = "";
        startTimeF = "";
        startTimeT = "";
        endDateF = "";
        endDateT = "";
        endTimeF = "";
        endTimeT = "";
        gouki = "";
        kcpNo = "";
        
        listData = new ArrayList<>();
    }
    
    /**
     * 入力値チェック：
     * 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        
        // ロットNo
        if (existError(validateUtil.checkC101(getLotNo(), "ロットNo", 14)) ||
            !StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
            return;
        }
        // 開始日(FROM)
        if (existError(validateUtil.checkC101(getStartDateF(), "開始日(from)", 6)) ||
            existError(validateUtil.checkC201(getStartDateF(), "開始日(from)")) ||
            existError(validateUtil.checkC501(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始日(TO)
        if (existError(validateUtil.checkC101(getStartDateT(), "開始日(to)", 6)) ||
            existError(validateUtil.checkC201(getStartDateT(), "開始日(to)")) ||
            existError(validateUtil.checkC501(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了日(FROM)
        if (existError(validateUtil.checkC101(getEndDateF(), "終了日(from)", 6)) ||
            existError(validateUtil.checkC201(getEndDateF(), "終了日(from)")) ||
            existError(validateUtil.checkC501(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了日(TO)
        if (existError(validateUtil.checkC101(getEndDateT(), "終了日(to)", 6)) ||
            existError(validateUtil.checkC201(getEndDateT(), "終了日(to)")) ||
            existError(validateUtil.checkC501(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201(getStartTimeF(), "開始時刻(from)")) ||
            existError(validateUtil.checkC502(getStartTimeF(), "開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201(getStartTimeT(), "開始時刻(to)")) ||
            existError(validateUtil.checkC502(getStartTimeT(), "開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了時刻(FROM)
        if (existError(validateUtil.checkC101(getEndTimeF(), "終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201(getEndTimeF(), "終了時刻(from)")) ||
            existError(validateUtil.checkC502(getEndTimeF(), "終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeF) && existError(validateUtil.checkC001(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了時刻(TO)
        if (existError(validateUtil.checkC101(getEndTimeT(), "終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201(getEndTimeT(), "終了時刻(to)")) ||
            existError(validateUtil.checkC502(getEndTimeT(), "終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeT) && existError(validateUtil.checkC001(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 号機
        if (existError(validateUtil.checkC101(getGouki(), "印刷号機", 4))) {
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
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "印刷号機"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }
        // KCPNO
        if (existError(validateUtil.checkC103(getKcpNo(), "KCPNO", 25))) {
            return;
        }
        
        // 一覧表示件数を取得
        long count = selectListDataCount();
        
        if (count == 0) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (listCountMax > 0 && count > listCountMax) {
            // 検索結果が上限件数以上の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000046", listCountMax), null);
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
     * @return 検索結果件数
     */
    public long selectListDataCount() {
        long count = 0;
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(*) AS CNT "
                    + "FROM sr_spsprint_gra "
                    + "WHERE (? IS NULL OR kojyo = ?) " 
                    + "AND   (? IS NULL OR lotno = ?) " 
                    + "AND   (? IS NULL OR edaban = ?) "               
                    + "AND   (? IS NULL OR startdatetime >= ?) " 
                    + "AND   (? IS NULL OR startdatetime <= ?) " 
                    + "AND   (? IS NULL OR enddatetime >= ?) " 
                    + "AND   (? IS NULL OR enddatetime <= ?) " 
                    + "AND   (? IS NULL OR gouki = ?) " 
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long)result.get("CNT");
            
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
            String sql ="SELECT CONCAT(T1.kojyo , T1.lotno , T1.edaban) AS lotno"
                    + ", T1.tapelotno"
                    + ", T1.petfilmsyurui"
                    + ", T1.taperollno1"
                    + ", T1.taperollno2"
                    + ", T1.taperollno3"
                    + ", T1.pastelotno"
                    + ", T1.pastenendo"
                    + ", T1.pasteondo"
                    + ", T1.pkokeibun1"
                    + ", T1.pastelotno2"
                    + ", T1.pastenendo2"
                    + ", T1.pasteondo2"
                    + ", T1.pkokeibun2"
                    + ", T1.handoumei"
                    + ", T1.handouno"
                    + ", T1.handoumaisuu"
                    + ", T1.bladeno"
                    + ", (CASE WHEN T1.bladegaikan = 0 THEN 'NG' WHEN T1.bladegaikan = '1' THEN 'OK' ELSE NULL END) AS bladegaikan"
                    + ", T1.BladeATu"
                    + ", T1.AtudoNo"
                    + ", T1.AtudoMaisuu"
                    + ", T1.AtuDoATu"
                    + ", T1.gouki"
                    + ", T1.kansouondo"
                    + ", T1.kansouondo2"
                    + ", T1.kansouondo3"
                    + ", T1.kansouondo4"
                    + ", T1.kansouondo5"
                    + ", T1.hansouspeed"
                    + ", T1.startdatetime"
                    + ", T1.tantousya"
                    + ", T1.makuatuave_start"
                    + ", T1.makuatumax_start"
                    + ", T1.makuatumin_start"
                    + ", T1.makuatucv_start"
                    + ", (CASE WHEN T1.nijimikasure_start = 0 THEN 'NG' WHEN T1.nijimikasure_start = '1' THEN 'OK' ELSE NULL END) AS nijimikasure_start"
                    + ", T1.start_ptn_dist_x"
                    + ", T1.start_ptn_dist_y"
                    + ", T1.TensionS_sum"
                    + ", T1.TensionStemae"
                    + ", T1.TensionSoku"
                    + ", T1.enddatetime"
                    + ", T1.tanto_end"
                    + ", T1.printmaisuu"
                    + ", T1.makuatuave_end"
                    + ", T1.makuatumax_end"
                    + ", T1.makuatumin_end"
                    + ", T1.makuatucv_end"
                    + ", (CASE WHEN T1.nijimikasure_end = 0 THEN 'NG' WHEN T1.nijimikasure_end = '1' THEN 'OK' ELSE NULL END) AS nijimikasure_end"
                    + ", T1.end_ptn_dist_x"
                    + ", T1.end_ptn_dist_y"
                    + ", T1.TensionE_sum"
                    + ", T1.TensionEtemae"
                    + ", T1.TensionEoku"
                    + ", T1.printzure1_surihajime_start"
                    + ", T1.printzure2_center_start"
                    + ", T1.printzure3_suriowari_start"
                    + ", T1.abzure_heikin_start"
                    + ", T1.printzure1_surihajime_end"
                    + ", T1.printzure2_center_end"
                    + ", T1.printzure3_suriowari_end"
                    + ", T1.abzure_heikin_end"
                    + ", T1.genryoukigou"
                    + ", T1.bikou1"
                    + ", T1.bikou2"
                    + ", T1.torokunichiji"
                    + ", T1.kosinnichiji"
                    + ", T1.revision"
                    + ", T1.kcpno"
                    + ", T2.makuatsu_start1"
                    + ", T2.makuatsu_start2"
                    + ", T2.makuatsu_start3"
                    + ", T2.makuatsu_start4"
                    + ", T2.makuatsu_start5"
                    + ", T2.makuatsu_start6"
                    + ", T2.makuatsu_start7"
                    + ", T2.makuatsu_start8"
                    + ", T2.makuatsu_start9"
                    + ", T2.start_ptn_dist_x1"
                    + ", T2.start_ptn_dist_x2"
                    + ", T2.start_ptn_dist_x3"
                    + ", T2.start_ptn_dist_x4"
                    + ", T2.start_ptn_dist_x5"
                    + ", T2.start_ptn_dist_y1"
                    + ", T2.start_ptn_dist_y2"
                    + ", T2.start_ptn_dist_y3"
                    + ", T2.start_ptn_dist_y4"
                    + ", T2.start_ptn_dist_y5"
                    + ", T2.makuatsu_end1"
                    + ", T2.makuatsu_end2"
                    + ", T2.makuatsu_end3"
                    + ", T2.makuatsu_end4"
                    + ", T2.makuatsu_end5"
                    + ", T2.makuatsu_end6"
                    + ", T2.makuatsu_end7"
                    + ", T2.makuatsu_end8"
                    + ", T2.makuatsu_end9"
                    + ", T2.end_ptn_dist_x1"
                    + ", T2.end_ptn_dist_x2"
                    + ", T2.end_ptn_dist_x3"
                    + ", T2.end_ptn_dist_x4"
                    + ", T2.end_ptn_dist_x5"
                    + ", T2.end_ptn_dist_y1"
                    + ", T2.end_ptn_dist_y2"
                    + ", T2.end_ptn_dist_y3"
                    + ", T2.end_ptn_dist_y4"
                    + ", T2.end_ptn_dist_y5 "
                    + "FROM sr_spsprint_gra T1 "
                    + "LEFT JOIN sub_sr_spsprint_gra T2 ON (T1.kojyo = T2.kojyo AND T1.lotno = T2.lotno AND T1.edaban = T2.edaban) "
                    + "WHERE (? IS NULL OR T1.kojyo = ?) " 
                    + "AND   (? IS NULL OR T1.lotno = ?) " 
                    + "AND   (? IS NULL OR T1.edaban = ?) "               
                    + "AND   (? IS NULL OR T1.startdatetime >= ?) " 
                    + "AND   (? IS NULL OR T1.startdatetime <= ?) " 
                    + "AND   (? IS NULL OR T1.enddatetime >= ?) " 
                    + "AND   (? IS NULL OR T1.enddatetime <= ?) " 
                    + "AND   (? IS NULL OR T1.gouki = ?) " 
                    + "AND   (? IS NULL OR T1.kcpno LIKE ? ESCAPE '\\\\') "
                    + "ORDER BY T1.startdatetime ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");
            mapping.put("kcpno", "kcpno");
            mapping.put("tapelotno", "tapelotno");
            mapping.put("petfilmsyurui", "petfilmsyurui");
            mapping.put("taperollno1", "taperollno1");
            mapping.put("taperollno2", "taperollno2");
            mapping.put("taperollno3", "taperollno3");
            mapping.put("pastelotno", "pastelotno");
            mapping.put("pastenendo", "pastenendo");
            mapping.put("pasteondo", "pasteondo");
            mapping.put("pkokeibun1", "pkokeibun1");
            mapping.put("pastelotno2", "pastelotno2");
            mapping.put("pastenendo2", "pastenendo2");
            mapping.put("pasteondo2", "pasteondo2");
            mapping.put("pkokeibun2", "pkokeibun2");
            mapping.put("handoumei", "handoumei");
            mapping.put("handouno", "handouno");
            mapping.put("handoumaisuu", "handoumaisuu");
            mapping.put("bladeno", "bladeno");
            mapping.put("bladegaikan", "bladegaikan");
            mapping.put("BladeATu", "bladeatu");
            mapping.put("AtudoNo", "atudono");
            mapping.put("AtudoMaisuu", "atudomaisuu");
            mapping.put("AtuDoATu", "atudoatu");
            mapping.put("gouki", "gouki");
            mapping.put("kansouondo", "kansouondo");
            mapping.put("kansouondo2", "kansouondo2");
            mapping.put("kansouondo3", "kansouondo3");
            mapping.put("kansouondo4", "kansouondo4");
            mapping.put("kansouondo5", "kansouondo5");
            mapping.put("hansouspeed", "hansouspeed");
            mapping.put("startdatetime", "startdatetime");
            mapping.put("tantousya", "tantousya");
            mapping.put("makuatuave_start", "makuatuaveStart");
            mapping.put("makuatumax_start", "makuatumaxStart");
            mapping.put("makuatumin_start", "makuatuminStart");
            mapping.put("makuatucv_start", "makuatucvStart");
            mapping.put("nijimikasure_start", "nijimikasureStart");
            mapping.put("start_ptn_dist_x", "startPtnDistX");
            mapping.put("start_ptn_dist_y", "startPtnDistY");
            mapping.put("TensionS_sum", "tensionsSum");
            mapping.put("TensionStemae", "tensionstemae");
            mapping.put("TensionSoku", "tensionsoku");
            mapping.put("enddatetime", "enddatetime");
            mapping.put("tanto_end", "tantoEnd");
            mapping.put("printmaisuu", "printmaisuu");
            mapping.put("makuatuave_end", "makuatuaveEnd");
            mapping.put("makuatumax_end", "makuatumaxEnd");
            mapping.put("makuatumin_end", "makuatuminEnd");
            mapping.put("makuatucv_end", "makuatucvEnd");
            mapping.put("nijimikasure_end", "nijimikasureEnd");
            mapping.put("end_ptn_dist_x", "endPtnDistX");
            mapping.put("end_ptn_dist_y", "endPtnDistY");
            mapping.put("TensionE_sum", "tensioneSum");
            mapping.put("TensionEtemae", "tensionetemae");
            mapping.put("TensionEoku", "tensioneoku");
            mapping.put("printzure1_surihajime_start", "printzure1SurihajimeStart");
            mapping.put("printzure2_center_start", "printzure2CenterStart");
            mapping.put("printzure3_suriowari_start", "printzure3SuriowariStart");
            mapping.put("abzure_heikin_start", "abzureHeikinStart");
            mapping.put("printzure1_surihajime_end", "printzure1SurihajimeEnd");
            mapping.put("printzure2_center_end", "printzure2CenterEnd");
            mapping.put("printzure3_suriowari_end", "printzure3SuriowariEnd");
            mapping.put("abzure_heikin_end", "abzureHeikinEnd");
            mapping.put("genryoukigou", "genryoukigou");
            mapping.put("bikou1", "bikou1");
            mapping.put("bikou2", "bikou2");
            mapping.put("makuatsu_start1", "makuatsuStart1");
            mapping.put("makuatsu_start2", "makuatsuStart2");
            mapping.put("makuatsu_start3", "makuatsuStart3");
            mapping.put("makuatsu_start4", "makuatsuStart4");
            mapping.put("makuatsu_start5", "makuatsuStart5");
            mapping.put("makuatsu_start6", "makuatsuStart6");
            mapping.put("makuatsu_start7", "makuatsuStart7");
            mapping.put("makuatsu_start8", "makuatsuStart8");
            mapping.put("makuatsu_start9", "makuatsuStart9");
            mapping.put("start_ptn_dist_x1", "startPtnDistX1");
            mapping.put("start_ptn_dist_x2", "startPtnDistX2");
            mapping.put("start_ptn_dist_x3", "startPtnDistX3");
            mapping.put("start_ptn_dist_x4", "startPtnDistX4");
            mapping.put("start_ptn_dist_x5", "startPtnDistX5");
            mapping.put("start_ptn_dist_y1", "startPtnDistY1");
            mapping.put("start_ptn_dist_y2", "startPtnDistY2");
            mapping.put("start_ptn_dist_y3", "startPtnDistY3");
            mapping.put("start_ptn_dist_y4", "startPtnDistY4");
            mapping.put("start_ptn_dist_y5", "startPtnDistY5");
            mapping.put("makuatsu_end1", "makuatsuEnd1");
            mapping.put("makuatsu_end2", "makuatsuEnd2");
            mapping.put("makuatsu_end3", "makuatsuEnd3");
            mapping.put("makuatsu_end4", "makuatsuEnd4");
            mapping.put("makuatsu_end5", "makuatsuEnd5");
            mapping.put("makuatsu_end6", "makuatsuEnd6");
            mapping.put("makuatsu_end7", "makuatsuEnd7");
            mapping.put("makuatsu_end8", "makuatsuEnd8");
            mapping.put("makuatsu_end9", "makuatsuEnd9");
            mapping.put("end_ptn_dist_x1", "endPtnDistX1");
            mapping.put("end_ptn_dist_x2", "endPtnDistX2");
            mapping.put("end_ptn_dist_x3", "endPtnDistX3");
            mapping.put("end_ptn_dist_x4", "endPtnDistX4");
            mapping.put("end_ptn_dist_x5", "endPtnDistX5");
            mapping.put("end_ptn_dist_y1", "endPtnDistY1");
            mapping.put("end_ptn_dist_y2", "endPtnDistY2");
            mapping.put("end_ptn_dist_y3", "endPtnDistY3");
            mapping.put("end_ptn_dist_y4", "endPtnDistY4");
            mapping.put("end_ptn_dist_y5", "endPtnDistY5");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B001Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B001Model.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
            
        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }
    
    /**
     * 文字列をバイトでカットします。
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
     * @throws Throwable 
     */
    public void downloadExcel() throws Throwable {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b001.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            File excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"));

            // ダウンロードファイル名
            String downloadFileName = "印刷・SPSｸﾞﾗﾋﾞｱ_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        }
    }
    
    /**
     * 検索パラメータ生成
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
            paramEdaban = StringUtils.substring(getLotNo(), 11, 14);
        }
        Date paramStartDateF = null;
        if (!StringUtil.isEmpty(startDateF)) {
            paramStartDateF = DateUtil.convertStringToDate(startDateF, StringUtil.isEmpty(startTimeF) ? "0000" : startTimeF);
        }
        Date paramStartDateT = null;
        if (!StringUtil.isEmpty(startDateT)) {
            paramStartDateT = DateUtil.convertStringToDate(startDateT, StringUtil.isEmpty(startTimeT) ? "0000" : startTimeT);
        }
        Date paramEndDateF = null;
        if (!StringUtil.isEmpty(endDateF)) {
            paramEndDateF = DateUtil.convertStringToDate(endDateF, StringUtil.isEmpty(endTimeF) ? "0000" : endTimeF);
        }
        Date paramEndDateT = null;
        if (!StringUtil.isEmpty(endDateT)) {
            paramEndDateT = DateUtil.convertStringToDate(endDateT, StringUtil.isEmpty(endTimeT) ? "0000" : endTimeT);
        }
        String paramGouki = StringUtil.blankToNull(getGouki());
        String paramKcpno = null;
        if (!StringUtil.isEmpty(kcpNo)) {
            paramKcpno = "%" + DBUtil.escapeString(kcpNo) + "%";
        }
        
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramStartDateF, paramStartDateF));
        params.addAll(Arrays.asList(paramStartDateT, paramStartDateT));
        params.addAll(Arrays.asList(paramEndDateF, paramEndDateF));
        params.addAll(Arrays.asList(paramEndDateT, paramEndDateT));
        params.addAll(Arrays.asList(paramGouki, paramGouki));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));

        return params;
    }
    
    /**
     * エラーチェック：
     * エラーが存在する場合ポップアップ用メッセージをセットする
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