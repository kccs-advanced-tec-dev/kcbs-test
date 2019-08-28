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
import jp.co.kccs.xhd.model.GXHDO201B012Model;
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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/08/07<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶｯﾄ・生品質検査履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2019/08/07
 */
@Named
@ViewScoped
public class GXHDO201B012 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B012.class.getName());
    
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B012Model> listData = null;
    
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 1ページ当たりの表示件数 */
    private int listDisplayPageCount = 30;
    
    /** 検索条件：ロットNo */
    private String lotNo = "";
    /** 検索条件：KCPNo */
    private String kcpNo = "";
    /** 検索条件：型式 */
    private String katashiki = "";
    /** 検索条件：容量 */
    private String yoryo = "";
    /** 検索条件：外観検査開始日(FROM) */
    private String startDateF = "";
    /** 検索条件：外観検査開始日(TO) */
    private String startDateT = "";
    /** 検索条件：外観検査開始時刻(FROM) */
    private String startTimeF = "";
    /** 検索条件：外観検査開始時刻(TO) */
    private String startTimeT = "";
    /** 検索条件：外観検査終了日(FROM) */
    private String endDateF = "";
    /** 検索条件：外観検査終了日(TO) */
    private String endDateT = "";
    /** 検索条件：外観検査終了時刻(FROM) */
    private String endTimeF = "";
    /** 検索条件：外観検査終了時刻(TO) */
    private String endTimeT = "";
    /** 検索条件：ばらし開始日(FROM) */
    private String barashiStartDateF = "";
    /** 検索条件：ばらし開始日(TO) */
    private String barashiStartDateT = "";
    /** 検索条件：ばらし開始時刻(FROM) */
    private String barashiStartTimeF = "";
    /** 検索条件：ばらし開始時刻(TO) */
    private String barashiStartTimeT = "";
    /** 検索条件：ばらし終了日(FROM) */
    private String barashiEndDateF = "";
    /** 検索条件：ばらし終了日(TO) */
    private String barashiEndDateT = "";
    /** 検索条件：ばらし終了時刻(FROM) */
    private String barashiEndTimeF = "";
    /** 検索条件：ばらし終了時刻(TO) */
    private String barashiEndTimeT = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B012() {
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
    public List<GXHDO201B012Model> getListData() {
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

    /**
     * 検索条件：型式
     * @return the katashiki
     */
    public String getKatashiki() {
        return katashiki;
    }

    /**
     * 検索条件：型式
     * @param katashiki the katashiki to set
     */
    public void setKatashiki(String katashiki) {
        this.katashiki = katashiki;
    }

    /**
     * 検索条件：容量
     * @return the yoryo
     */
    public String getYoryo() {
        return yoryo;
    }

    /**
     * 検索条件：容量
     * @param yoryo the yoryo to set
     */
    public void setYoryo(String yoryo) {
        this.yoryo = yoryo;
    }

    /**
     * 検索条件：外観検査開始日(FROM)
     * @return the startDateF
     */
    public String getStartDateF() {
        return startDateF;
    }

    /**
     * 検索条件：外観検査開始日(FROM)
     * @param startDateF the startDateF to set
     */
    public void setStartDateF(String startDateF) {
        this.startDateF = startDateF;
    }

    /**
     * 検索条件：外観検査開始日(TO)
     * @return the startDateT
     */
    public String getStartDateT() {
        return startDateT;
    }

    /**
     * 検索条件：外観検査開始日(TO)
     * @param startDateT the startDateT to set
     */
    public void setStartDateT(String startDateT) {
        this.startDateT = startDateT;
    }

    /**
     * 検索条件：外観検査開始時刻(FROM)
     * @return the startTimeF
     */
    public String getStartTimeF() {
        return startTimeF;
    }

    /**
     * 検索条件：外観検査開始時刻(FROM)
     * @param startTimeF the startTimeF to set
     */
    public void setStartTimeF(String startTimeF) {
        this.startTimeF = startTimeF;
    }

    /**
     * 検索条件：外観検査開始時刻(TO)
     * @return the startTimeT
     */
    public String getStartTimeT() {
        return startTimeT;
    }

    /**
     * 検索条件：外観検査開始時刻(TO)
     * @param startTimeT the startTimeT to set
     */
    public void setStartTimeT(String startTimeT) {
        this.startTimeT = startTimeT;
    }

    /**
     * 検索条件：外観検査終了日(FROM)
     * @return the endDateF
     */
    public String getEndDateF() {
        return endDateF;
    }

    /**
     * 検索条件：外観検査終了日(FROM)
     * @param endDateF the endDateF to set
     */
    public void setEndDateF(String endDateF) {
        this.endDateF = endDateF;
    }

    /**
     * 検索条件：外観検査終了日(TO)
     * @return the endDateT
     */
    public String getEndDateT() {
        return endDateT;
    }

    /**
     * 検索条件：外観検査終了日(TO)
     * @param endDateT the endDateT to set
     */
    public void setEndDateT(String endDateT) {
        this.endDateT = endDateT;
    }

    /**
     * 検索条件：外観検査終了時刻(FROM)
     * @return the endTimeF
     */
    public String getEndTimeF() {
        return endTimeF;
    }

    /**
     * 検索条件：外観検査終了時刻(FROM)
     * @param endTimeF the endTimeF to set
     */
    public void setEndTimeF(String endTimeF) {
        this.endTimeF = endTimeF;
    }

    /**
     * 検索条件：外観検査終了時刻(TO)
     * @return the endTimeT
     */
    public String getEndTimeT() {
        return endTimeT;
    }

    /**
     * 検索条件：外観検査終了時刻(TO)
     * @param endTimeT the endTimeT to set
     */
    public void setEndTimeT(String endTimeT) {
        this.endTimeT = endTimeT;
    }

    /**
     * 検索条件：ばらし開始日(FROM)
     * @return the barashiStartDateF
     */
    public String getBarashiStartDateF() {
        return barashiStartDateF;
    }

    /**
     * 検索条件：ばらし開始日(FROM)
     * @param barashiStartDateF the barashiStartDateF to set
     */
    public void setBarashiStartDateF(String barashiStartDateF) {
        this.barashiStartDateF = barashiStartDateF;
    }

    /**
     * 検索条件：ばらし開始日(TO)
     * @return the barashiStartDateT
     */
    public String getBarashiStartDateT() {
        return barashiStartDateT;
    }

    /**
     * 検索条件：ばらし開始日(TO)
     * @param barashiStartDateT the barashiStartDateT to set
     */
    public void setBarashiStartDateT(String barashiStartDateT) {
        this.barashiStartDateT = barashiStartDateT;
    }

    /**
     * 検索条件：ばらし開始時刻(FROM)
     * @return the startTimeF
     */
    public String getBarashiStartTimeF() {
        return barashiStartTimeF;
    }

    /**
     * 検索条件：ばらし開始時刻(FROM)
     * @param barashiStartTimeF the barashiStartTimeF to set
     */
    public void setBarashiStartTimeF(String barashiStartTimeF) {
        this.barashiStartTimeF = barashiStartTimeF;
    }

    /**
     * 検索条件：ばらし開始時刻(TO)
     * @return the barashiStartTimeT
     */
    public String getBarashiStartTimeT() {
        return barashiStartTimeT;
    }

    /**
     * 検索条件：ばらし開始時刻(TO)
     * @param barashiStartTimeT the barashiStartTimeT to set
     */
    public void setBarashiStartTimeT(String barashiStartTimeT) {
        this.barashiStartTimeT = barashiStartTimeT;
    }

    /**
     * 検索条件：ばらし終了日(FROM)
     * @return the barashiEndDateF
     */
    public String getBarashiEndDateF() {
        return barashiEndDateF;
    }

    /**
     * 検索条件：ばらし終了日(FROM)
     * @param barashiEndDateF the barashiEndDateF to set
     */
    public void setBarashiEndDateF(String barashiEndDateF) {
        this.barashiEndDateF = barashiEndDateF;
    }

    /**
     * 検索条件：ばらし終了日(TO)
     * @return the barashiEndDateT
     */
    public String getBarashiEndDateT() {
        return barashiEndDateT;
    }

    /**
     * 検索条件：ばらし終了日(TO)
     * @param barashiEndDateT the barashiEndDateT to set
     */
    public void setBarashiEndDateT(String barashiEndDateT) {
        this.barashiEndDateT = barashiEndDateT;
    }

    /**
     * 検索条件：ばらし終了時刻(FROM)
     * @return the barashiEndTimeF
     */
    public String getBarashiEndTimeF() {
        return barashiEndTimeF;
    }

    /**
     * 検索条件：ばらし終了時刻(FROM)
     * @param barashiEndTimeF the barashiEndTimeF to set
     */
    public void setBarashiEndTimeF(String barashiEndTimeF) {
        this.barashiEndTimeF = barashiEndTimeF;
    }

    /**
     * 検索条件：ばらし終了時刻(TO)
     * @return the barashiEndTimeT
     */
    public String getBarashiEndTimeT() {
        return barashiEndTimeT;
    }

    /**
     * 検索条件：ばらし終了時刻(TO)
     * @param barashiEndTimeT the barashiEndTimeT to set
     */
    public void setBarashiEndTimeT(String barashiEndTimeT) {
        this.barashiEndTimeT = barashiEndTimeT;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B012_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B012_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
        
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
        kcpNo = "";
        katashiki = "";
        yoryo = "";
        startDateF = "";
        startDateT = "";
        startTimeF = "";
        startTimeT = "";
        endDateF = "";
        endDateT = "";
        endTimeF = "";
        endTimeT = "";
        barashiStartDateF = "";
        barashiStartDateT = "";
        barashiStartTimeF = "";
        barashiStartTimeT = "";
        barashiEndDateF = "";
        barashiEndDateT = "";
        barashiEndTimeF = "";
        barashiEndTimeT = "";
        
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
        if (!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)) {
            // エラー対象をリストに追加
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
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
        // 外観検査開始日(FROM)
        if (existError(validateUtil.checkC101(getStartDateF(), "外観検査開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateF(), "外観検査開始日(from)")) ||
            existError(validateUtil.checkC501(getStartDateF(), "外観検査開始日(from)"))) {
            return;
        }
        // 外観検査開始日(TO)
        if (existError(validateUtil.checkC101(getStartDateT(), "外観検査開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateT(), "外観検査開始日(to)")) ||
            existError(validateUtil.checkC501(getStartDateT(), "外観検査開始日(to)"))) {
            return;
        }
        // 外観検査終了日(FROM)
        if (existError(validateUtil.checkC101(getEndDateF(), "外観検査終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateF(), "外観検査終了日(from)")) ||
            existError(validateUtil.checkC501(getEndDateF(), "外観検査終了日(from)"))) {
            return;
        }
        // 外観検査終了日(TO)
        if (existError(validateUtil.checkC101(getEndDateT(), "外観検査終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateT(), "外観検査終了日(to)")) ||
            existError(validateUtil.checkC501(getEndDateT(), "外観検査終了日(to)"))) {
            return;
        }
        // 外観検査開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "外観検査開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeF(), "外観検査開始時刻(from)")) ||
            existError(validateUtil.checkC502(getStartTimeF(), "外観検査開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "外観検査開始日(from)"))) {
            return;
        }
        // 外観検査開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "外観検査開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeT(), "外観検査開始時刻(to)")) ||
            existError(validateUtil.checkC502(getStartTimeT(), "外観検査開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "外観検査開始日(to)"))) {
            return;
        }
        // 外観検査終了時刻(FROM)
        if (existError(validateUtil.checkC101(getEndTimeF(), "外観検査終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeF(), "外観検査終了時刻(from)")) ||
            existError(validateUtil.checkC502(getEndTimeF(), "外観検査終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeF) && existError(validateUtil.checkC001(getEndDateF(), "外観検査終了日(from)"))) {
            return;
        }
        // 外観検査終了時刻(TO)
        if (existError(validateUtil.checkC101(getEndTimeT(), "外観検査終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeT(), "外観検査終了時刻(to)")) ||
            existError(validateUtil.checkC502(getEndTimeT(), "外観検査終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeT) && existError(validateUtil.checkC001(getEndDateT(), "外観検査終了日(to)"))) {
            return;
        }
        
        // ばらし開始日(FROM)
        if (existError(validateUtil.checkC101(getBarashiStartDateF(), "ばらし開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBarashiStartDateF(), "ばらし開始日(from)")) ||
            existError(validateUtil.checkC501(getBarashiStartDateF(), "ばらし開始日(from)"))) {
            return;
        }
        // ばらし開始日(TO)
        if (existError(validateUtil.checkC101(getBarashiStartDateT(), "ばらし開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBarashiStartDateT(), "ばらし開始日(to)")) ||
            existError(validateUtil.checkC501(getBarashiStartDateT(), "ばらし開始日(to)"))) {
            return;
        }
        // ばらし終了日(FROM)
        if (existError(validateUtil.checkC101(getBarashiEndDateF(), "ばらし終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBarashiEndDateF(), "ばらし終了日(from)")) ||
            existError(validateUtil.checkC501(getBarashiEndDateF(), "ばらし終了日(from)"))) {
            return;
        }
        // ばらし終了日(TO)
        if (existError(validateUtil.checkC101(getBarashiEndDateT(), "ばらし終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getBarashiEndDateT(), "ばらし終了日(to)")) ||
            existError(validateUtil.checkC501(getBarashiEndDateT(), "ばらし終了日(to)"))) {
            return;
        }
        // ばらし開始時刻(FROM)
        if (existError(validateUtil.checkC101(getBarashiStartTimeF(), "ばらし開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getBarashiStartTimeF(), "ばらし開始時刻(from)")) ||
            existError(validateUtil.checkC502(getBarashiStartTimeF(), "ばらし開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(barashiStartTimeF) && existError(validateUtil.checkC001(getBarashiStartDateF(), "ばらし開始日(from)"))) {
            return;
        }
        // ばらし開始時刻(TO)
        if (existError(validateUtil.checkC101(getBarashiStartTimeT(), "ばらし開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getBarashiStartTimeT(), "ばらし開始時刻(to)")) ||
            existError(validateUtil.checkC502(getBarashiStartTimeT(), "ばらし開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(barashiStartTimeT) && existError(validateUtil.checkC001(getBarashiStartDateT(), "ばらし開始日(to)"))) {
            return;
        }
        // ばらし終了時刻(FROM)
        if (existError(validateUtil.checkC101(getBarashiEndTimeF(), "ばらし終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getBarashiEndTimeF(), "ばらし終了時刻(from)")) ||
            existError(validateUtil.checkC502(getBarashiEndTimeF(), "ばらし終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(barashiEndTimeF) && existError(validateUtil.checkC001(getBarashiEndDateF(), "ばらし終了日(from)"))) {
            return;
        }
        // ばらし終了時刻(TO)
        if (existError(validateUtil.checkC101(getBarashiEndTimeT(), "ばらし終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getBarashiEndTimeT(), "ばらし終了時刻(to)")) ||
            existError(validateUtil.checkC502(getBarashiEndTimeT(), "ばらし終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(barashiEndTimeT) && existError(validateUtil.checkC001(getBarashiEndDateT(), "ばらし終了日(to)"))) {
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
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM sr_cutcheck "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 10, 4) = ?) "
                    + "AND   (? IS NULL OR STARTDATETIME >= ?) "
                    + "AND   (? IS NULL OR STARTDATETIME <= ?) "
                    + "AND   (? IS NULL OR ENDDATETIME >= ?) "
                    + "AND   (? IS NULL OR ENDDATETIME <= ?) "
                    + "AND   (? IS NULL OR BARASHISTARTNICHIJI >= ?) "
                    + "AND   (? IS NULL OR BARASHISTARTNICHIJI <= ?) "
                    + "AND   (? IS NULL OR BARASHIENDNICHIJI >= ?) "
                    + "AND   (? IS NULL OR BARASHIENDNICHIJI <= ?) ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
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
            String sql = "SELECT CONCAT(IFNULL(KOJYO, ''), IFNULL(LOTNO, ''), IFNULL(EDABAN, '')) AS LOTNO "
                    + ", KCPNO "
                    + ", startdatetime "
                    + ", enddatetime "
                    + ", tantousya "
                    + ", kakuninsya "
                    + ", AtumiMin "
                    + ", AtumiMax "
                    + ", Atumi01 "
                    + ", Atumi02 "
                    + ", Atumi03 "
                    + ", Atumi04 "
                    + ", Atumi05 "
                    + ", Atumi06 "
                    + ", Atumi07 "
                    + ", Atumi08 "
                    + ", Atumi09 "
                    + ", Atumi10 "
                    + ", bikou1 "
                    + ", bikou2 "
                    + ", bikou3 "
                    + ", bikou4 "
                    + ", bikou5 "
                    + ", Soujyuryo "
                    + ", Tanijyuryo "
                    + ", gaikankensatantousya "
                    + ", barasshi "
                    + ", joken "
                    + ", barashistartnichiji "
                    + ", batashistarttantousya "
                    + ", barashiendnichiji "
                    + ", barashiendtantousya "
                    + ", (CASE WHEN konamabushi = 0 THEN 'なし' WHEN konamabushi = 1 THEN 'あり' ELSE NULL END) AS konamabushi "
                    + ", syorisetsuu "
                    + ", RyouhinSetsuu "
                    + ", budomari "
                    + "FROM sr_cutcheck "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 10, 4) = ?) "
                    + "AND   (? IS NULL OR STARTDATETIME >= ?) "
                    + "AND   (? IS NULL OR STARTDATETIME <= ?) "
                    + "AND   (? IS NULL OR ENDDATETIME >= ?) "
                    + "AND   (? IS NULL OR ENDDATETIME <= ?) "
                    + "AND   (? IS NULL OR BARASHISTARTNICHIJI >= ?) "
                    + "AND   (? IS NULL OR BARASHISTARTNICHIJI <= ?) "
                    + "AND   (? IS NULL OR BARASHIENDNICHIJI >= ?) "
                    + "AND   (? IS NULL OR BARASHIENDNICHIJI <= ?) "
                    + "ORDER BY STARTDATETIME ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");                                  // ﾛｯﾄNo.
            mapping.put("KCPNO", "kcpno");                                  // KCPNO
            mapping.put("startdatetime", "startdatetime");                  // 開始日時
            mapping.put("enddatetime", "enddatetime");                      // 終了日時
            mapping.put("tantousya", "tantousya");                          // 担当者
            mapping.put("kakuninsya", "kakuninsya");                        // 確認者
            mapping.put("AtumiMin", "atumimin");                            // 厚みMIN
            mapping.put("AtumiMax", "atumimax");                            // 厚みMAX
            mapping.put("Atumi01", "atumi01");                              // 厚み01
            mapping.put("Atumi02", "atumi02");                              // 厚み02
            mapping.put("Atumi03", "atumi03");                              // 厚み03
            mapping.put("Atumi04", "atumi04");                              // 厚み04
            mapping.put("Atumi05", "atumi05");                              // 厚み05
            mapping.put("Atumi06", "atumi06");                              // 厚み06
            mapping.put("Atumi07", "atumi07");                              // 厚み07
            mapping.put("Atumi08", "atumi08");                              // 厚み08
            mapping.put("Atumi09", "atumi09");                              // 厚み09
            mapping.put("Atumi10", "atumi10");                              // 厚み10
            mapping.put("bikou1", "bikou1");                                // 備考1
            mapping.put("bikou2", "bikou2");                                // 備考2
            mapping.put("bikou3", "bikou3");                                // 備考3
            mapping.put("bikou4", "bikou4");                                // 備考4
            mapping.put("bikou5", "bikou5");                                // 備考5
            mapping.put("Soujyuryo", "soujyuryo");                          // 総重量
            mapping.put("Tanijyuryo", "tanijyuryo");                        // 単位重量
            mapping.put("gaikankensatantousya", "gaikankensatantousya");    // 外観検査終了担当者
            mapping.put("barasshi", "barasshi");                            // ばらし方法
            mapping.put("joken", "joken");                                  // 条件
            mapping.put("barashistartnichiji", "barashistartnichiji");      // ばらし開始日時
            mapping.put("batashistarttantousya", "batashistarttantousya");  // ばらし開始担当者
            mapping.put("barashiendnichiji", "barashiendnichiji");          // ばらし終了日時
            mapping.put("barashiendtantousya", "barashiendtantousya");      // ばらし終了担当者
            mapping.put("konamabushi", "konamabushi");                      // 粉まぶし
            mapping.put("syorisetsuu", "syorisetsuu");                      // 処理個数
            mapping.put("RyouhinSetsuu", "ryouhinsetsuu");                  // 良品個数
            mapping.put("budomari", "budomari");                            // 歩留まり

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B012Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B012Model.class, rowProcessor);
            
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
        File excel = null;
        
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b012.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "カット・生品質検査");

            // ダウンロードファイル名
            String downloadFileName = "カット・生品質検査_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        
        Date paramBarashiStartDateF = null;
        if (!StringUtil.isEmpty(barashiStartDateF)) {
            paramBarashiStartDateF = DateUtil.convertStringToDate(getBarashiStartDateF(), StringUtil.isEmpty(getBarashiStartTimeF()) ? "0000" : getBarashiStartTimeF());
        }
        Date paramBarashiStartDateT = null;
        if (!StringUtil.isEmpty(barashiStartDateT)) {
            paramBarashiStartDateT = DateUtil.convertStringToDate(getBarashiStartDateT(), StringUtil.isEmpty(getBarashiStartTimeT()) ? "2359" : getBarashiStartTimeT());
        }
        Date paramBarashiEndDateF = null;
        if (!StringUtil.isEmpty(barashiEndDateF)) {
            paramBarashiEndDateF = DateUtil.convertStringToDate(getBarashiEndDateF(), StringUtil.isEmpty(getBarashiEndTimeF()) ? "0000" : getBarashiEndTimeF());
        }
        Date paramBarashiEndDateT = null;
        if (!StringUtil.isEmpty(barashiEndDateT)) {
            paramBarashiEndDateT = DateUtil.convertStringToDate(getBarashiEndDateT(), StringUtil.isEmpty(getBarashiEndTimeT()) ? "2359" : getBarashiEndTimeT());
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
        params.addAll(Arrays.asList(paramBarashiStartDateF, paramBarashiStartDateF));
        params.addAll(Arrays.asList(paramBarashiStartDateT, paramBarashiStartDateT));
        params.addAll(Arrays.asList(paramBarashiEndDateF, paramBarashiEndDateF));
        params.addAll(Arrays.asList(paramBarashiEndDateT, paramBarashiEndDateT));

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
