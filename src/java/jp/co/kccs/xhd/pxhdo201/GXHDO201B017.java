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
import jp.co.kccs.xhd.model.GXHDO201B017Model;
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
 * 変更日	2019/08/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・焼成履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2019/08/05
 */
@Named
@ViewScoped
public class GXHDO201B017 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B017.class.getName());
    
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
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B017Model> listData = null;
    
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
    /** 検索条件：開始日(FROM) */
    private String startDateF = "";
    /** 検索条件：開始日(TO) */
    private String startDateT = "";
    /** 検索条件：開始時刻(FROM) */
    private String startTimeF = "";
    /** 検索条件：開始時刻(TO) */
    private String startTimeT = "";
    /** 検索条件：終了日(FROM) */
    private String endDateF = "";
    /** 検索条件：終了日(TO) */
    private String endDateT = "";
    /** 検索条件：終了時刻(FROM) */
    private String endTimeF = "";
    /** 検索条件：終了時刻(TO) */
    private String endTimeT = "";
    /** 検索条件：焼成号機 */
    private String goki = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B017() {
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
    public List<GXHDO201B017Model> getListData() {
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
     * 検索条件：開始日(FROM)
     * @return the startDateF
     */
    public String getStartDateF() {
        return startDateF;
    }

    /**
     * 検索条件：開始日(FROM)
     * @param startDateF the startDateF to set
     */
    public void setStartDateF(String startDateF) {
        this.startDateF = startDateF;
    }

    /**
     * 検索条件：開始日(TO)
     * @return the startDateT
     */
    public String getStartDateT() {
        return startDateT;
    }

    /**
     * 検索条件：開始日(TO)
     * @param startDateT the startDateT to set
     */
    public void setStartDateT(String startDateT) {
        this.startDateT = startDateT;
    }

    /**
     * 検索条件：開始時刻(FROM)
     * @return the startTimeF
     */
    public String getStartTimeF() {
        return startTimeF;
    }

    /**
     * 検索条件：開始時刻(FROM)
     * @param startTimeF the startTimeF to set
     */
    public void setStartTimeF(String startTimeF) {
        this.startTimeF = startTimeF;
    }

    /**
     * 検索条件：開始時刻(TO)
     * @return the startTimeT
     */
    public String getStartTimeT() {
        return startTimeT;
    }

    /**
     * 検索条件：開始時刻(TO)
     * @param startTimeT the startTimeT to set
     */
    public void setStartTimeT(String startTimeT) {
        this.startTimeT = startTimeT;
    }

    /**
     * 検索条件：終了日(FROM)
     * @return the endDateF
     */
    public String getEndDateF() {
        return endDateF;
    }

    /**
     * 検索条件：終了日(FROM)
     * @param endDateF the endDateF to set
     */
    public void setEndDateF(String endDateF) {
        this.endDateF = endDateF;
    }

    /**
     * 検索条件：終了日(TO)
     * @return the endDateT
     */
    public String getEndDateT() {
        return endDateT;
    }

    /**
     * 検索条件：終了日(TO)
     * @param endDateT the endDateT to set
     */
    public void setEndDateT(String endDateT) {
        this.endDateT = endDateT;
    }

    /**
     * 検索条件：終了時刻(FROM)
     * @return the endTimeF
     */
    public String getEndTimeF() {
        return endTimeF;
    }

    /**
     * 検索条件：終了時刻(FROM)
     * @param endTimeF the endTimeF to set
     */
    public void setEndTimeF(String endTimeF) {
        this.endTimeF = endTimeF;
    }

    /**
     * 検索条件：終了時刻(TO)
     * @return the endTimeT
     */
    public String getEndTimeT() {
        return endTimeT;
    }

    /**
     * 検索条件：終了時刻(TO)
     * @param endTimeT the endTimeT to set
     */
    public void setEndTimeT(String endTimeT) {
        this.endTimeT = endTimeT;
    }

    /**
     * 検索条件：焼成号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 検索条件：焼成号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B017_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B017_display_page_count", session));
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
        goki = "";
        
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
        // 開始日(FROM)
        if (existError(validateUtil.checkC101(getStartDateF(), "開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateF(), "開始日(from)")) ||
            existError(validateUtil.checkC501(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始日(TO)
        if (existError(validateUtil.checkC101(getStartDateT(), "開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getStartDateT(), "開始日(to)")) ||
            existError(validateUtil.checkC501(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了日(FROM)
        if (existError(validateUtil.checkC101(getEndDateF(), "終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateF(), "終了日(from)")) ||
            existError(validateUtil.checkC501(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了日(TO)
        if (existError(validateUtil.checkC101(getEndDateT(), "終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getEndDateT(), "終了日(to)")) ||
            existError(validateUtil.checkC501(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 開始時刻(FROM)
        if (existError(validateUtil.checkC101(getStartTimeF(), "開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeF(), "開始時刻(from)")) ||
            existError(validateUtil.checkC502(getStartTimeF(), "開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeF) && existError(validateUtil.checkC001(getStartDateF(), "開始日(from)"))) {
            return;
        }
        // 開始時刻(TO)
        if (existError(validateUtil.checkC101(getStartTimeT(), "開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getStartTimeT(), "開始時刻(to)")) ||
            existError(validateUtil.checkC502(getStartTimeT(), "開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(startTimeT) && existError(validateUtil.checkC001(getStartDateT(), "開始日(to)"))) {
            return;
        }
        // 終了時刻(FROM)
        if (existError(validateUtil.checkC101(getEndTimeF(), "終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeF(), "終了時刻(from)")) ||
            existError(validateUtil.checkC502(getEndTimeF(), "終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeF) && existError(validateUtil.checkC001(getEndDateF(), "終了日(from)"))) {
            return;
        }
        // 終了時刻(TO)
        if (existError(validateUtil.checkC101(getEndTimeT(), "終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getEndTimeT(), "終了時刻(to)")) ||
            existError(validateUtil.checkC502(getEndTimeT(), "終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(endTimeT) && existError(validateUtil.checkC001(getEndDateT(), "終了日(to)"))) {
            return;
        }
        // 焼成号機
        if (!StringUtil.isEmpty(goki)) {
            // 号機が入力されている場合のみマスタチェックを実施
            boolean existGoukiMaster = false;
            try {
                QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
                if (validateUtil.existGokukimas(getGoki(), queryRunnerWip)) {
                    existGoukiMaster = true;
                }
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("号機マスタ存在チェックに失敗", ex, LOGGER);
                existGoukiMaster = false;
            }
            if (!existGoukiMaster) {
                FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "焼成号機"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
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
                    + "FROM sr_syosei "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 6, 2) = ?) "
                    + "AND   (? IS NULL OR SUBSTR(KCPNO, 10, 4) = ?) "
                    + "AND   (? IS NULL OR SKAISINICHIJI >= ?) "
                    + "AND   (? IS NULL OR SKAISINICHIJI <= ?) "
                    + "AND   (? IS NULL OR SSYURYONICHIJI >= ?) "
                    + "AND   (? IS NULL OR SSYURYONICHIJI <= ?) "
                    + "AND   (? IS NULL OR GOKI = ?)"
                    + "AND   (SYOSEISYURUI = '焼成')";
            
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
                    + ", kcpno"
                    + ", jissekino"
                    + ", kosuu"
                    + ", genryohinsyumei"
                    + ", genryogroup"
                    + ", skaisinichiji"
                    + ", ssyuryonichiji"
                    + ", bprogramno"
                    + ", syoseiondo"
                    + ", goki"
                    + ", ssettermaisuu"
                    + ", nyurodaibanmaisuu"
                    + ", skaisitantosya"
                    + ", ssyuryotantosya"
                    + ", biko1"
                    + ", biko2"
                    + ", biko3"
                    + ", biko4"
                    + ", biko5"
                    + ", bkaisinichiji"
                    + ", bsyuryonichiji"
                    + ", bsettermaisuu"
                    + ", potsuu"
                    + ", potno"
                    + ", btantosya"
                    + ", biko6"
                    + ", biko7"
                    + ", SankaGoki"
                    + ", SankaOndo"
                    + ", SankaSyuryoNichiJi"
                    + ", tounyusettasuu"
                    + ", (CASE WHEN setteipattern = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS setteipattern "
                    + ", dansuu"
                    + ", (CASE WHEN gaikancheck = 0 THEN 'NG' WHEN gaikancheck = 1 THEN 'OK' ELSE NULL END) AS gaikancheck "
                    + ", kaishusettasuu"
                    + ", StartKakuninsyacode"
                    + ", nijidasshigouki"
                    + ", (CASE WHEN NijidasshisetteiPT = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS NijidasshisetteiPT "
                    + ", Nijidasshikeepondo"
                    + ", Nijidasshispeed"
                    + ", peakondo"
                    + ", syoseispeed"
                    + ", syoseipurge"
                    + ", saisankagouki1"
                    + ", saisankagouki2"
                    + ", (CASE WHEN saisankasetteiPT = 1 THEN 'ﾁｪｯｸ' ELSE '未ﾁｪｯｸ' END) AS saisankasetteiPT "
                    + ", saisankakeepondo"
                    + ", saisankaCsokudo"
                    + ", saisankagogaikan"
                    + ", syoseisyurui "
                    + " FROM sr_syosei "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "  AND (? IS NULL OR LOTNO = ?) "
                    + "  AND (? IS NULL OR EDABAN = ?) "
                    + "  AND (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + "  AND (? IS NULL OR SUBSTR(KCPNO, 6, 2) = ?) "
                    + "  AND (? IS NULL OR SUBSTR(KCPNO, 10, 4) = ?) "
                    + "  AND (? IS NULL OR SKAISINICHIJI >= ?) "
                    + "  AND (? IS NULL OR SKAISINICHIJI <= ?) "
                    + "  AND (? IS NULL OR SSYURYONICHIJI >= ?) "
                    + "  AND (? IS NULL OR SSYURYONICHIJI <= ?) "
                    + "  AND (? IS NULL OR GOKI = ?) "
                    + "  AND (SYOSEISYURUI = '焼成') "
                    + "ORDER BY SKAISINICHIJI ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");                                 // ﾛｯﾄNo.
            mapping.put("kcpno", "kcpno");                                 // KCPNo
            mapping.put("jissekino", "jissekino");                         // 実績No
            mapping.put("kosuu", "kosuu");                                 // 個数
            mapping.put("genryohinsyumei", "genryohinsyumei");             // 原料品種名
            mapping.put("genryogroup", "genryogroup");                     // 原料ｸﾞﾙｰﾌﾟ
            mapping.put("skaisinichiji", "skaisinichiji");                 // 焼成開始日時
            mapping.put("ssyuryonichiji", "ssyuryonichiji");               // 焼成終了日時
            mapping.put("bprogramno", "bprogramno");                       // ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
            mapping.put("syoseiondo", "syoseiondo");                       // 焼成温度
            mapping.put("goki", "goki");                                   // ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
            mapping.put("ssettermaisuu", "ssettermaisuu");                 // 受入ｾｯﾀ枚数
            mapping.put("nyurodaibanmaisuu", "nyurodaibanmaisuu");         // 入炉台板枚数
            mapping.put("skaisitantosya", "skaisitantosya");               // 焼成開始担当者
            mapping.put("ssyuryotantosya", "ssyuryotantosya");             // 終了担当者
            mapping.put("biko1", "biko1");                                 // 備考1
            mapping.put("biko2", "biko2");                                 // 備考2
            mapping.put("biko3", "biko3");                                 // 備考3
            mapping.put("biko4", "biko4");                                 // 備考4
            mapping.put("biko5", "biko5");                                 // 備考5
            mapping.put("bkaisinichiji", "bkaisinichiji");                 // ﾊﾞﾗｼ開始日時
            mapping.put("bsyuryonichiji", "bsyuryonichiji");               // ﾊﾞﾗｼ終了日時
            mapping.put("bsettermaisuu", "bsettermaisuu");                 // ﾊﾞﾗｼｾｯﾀｰ枚数
            mapping.put("potsuu", "potsuu");                               // ﾎﾟｯﾄ数
            mapping.put("potno", "potno");                                 // ﾎﾟｯﾄNo
            mapping.put("btantosya", "btantosya");                         // ﾊﾞﾗｼ担当者
            mapping.put("biko6", "biko6");                                 // 備考6
            mapping.put("biko7", "biko7");                                 // 備考7
            mapping.put("SankaGoki", "sankagoki");                         // 再酸化号機
            mapping.put("SankaOndo", "sankaondo");                         // 再酸化温度
            mapping.put("SankaSyuryoNichiJi", "sankasyuryonichiJi");       // 再酸化終了日時
            mapping.put("tounyusettasuu", "tounyusettasuu");               // 投入ｾｯﾀ枚数
            mapping.put("setteipattern", "setteipattern");                 // 焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
            mapping.put("dansuu", "dansuu");                               // 段数
            mapping.put("gaikancheck", "gaikancheck");                     // 外観確認
            mapping.put("kaishusettasuu", "kaishusettasuu");               // 回収ｾｯﾀ枚数
            mapping.put("StartKakuninsyacode", "startkakuninsyacode");     // 開始確認者
            mapping.put("nijidasshigouki", "nijidasshigouki");             // 2次脱脂号機
            mapping.put("NijidasshisetteiPT", "nijidasshisetteipt");       // 2次脱脂設定ﾊﾟﾀｰﾝ
            mapping.put("Nijidasshikeepondo", "nijidasshikeepondo");       // 2次脱脂ｷｰﾌﾟ温度
            mapping.put("Nijidasshispeed", "nijidasshispeed");             // 2次脱脂ｺﾝﾍﾞｱ速度
            mapping.put("peakondo", "peakondo");                           // 焼成ﾋﾟｰｸ温度指示
            mapping.put("syoseispeed", "syoseispeed");                     // 焼成ﾛｰﾗｰ速度
            mapping.put("syoseipurge", "syoseipurge");                     // 焼成ﾊﾟｰｼﾞ
            mapping.put("saisankagouki1", "saisankagouki1");               // 1回目再酸化号機1
            mapping.put("saisankagouki2", "saisankagouki2");               // 1回目再酸化号機2
            mapping.put("saisankasetteiPT", "saisankasetteipt");           // 1回目再酸化設定ﾊﾟﾀｰﾝ
            mapping.put("saisankakeepondo", "saisankakeepondo");           // 1回目再酸化ｷｰﾌﾟ温度
            mapping.put("saisankaCsokudo", "saisankacsokudo");             // 1回目再酸化ｺﾝﾍﾞｱ速度
            mapping.put("saisankagogaikan", "saisankagogaikan");           // 1回目再酸化後外観
            mapping.put("syoseisyurui", "syoseisyurui");                   // 焼成種類

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B017Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B017Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b017.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "焼成・焼成");

            // ダウンロードファイル名
            String downloadFileName = "焼成・焼成_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        String paramGoki = StringUtil.blankToNull(getGoki());

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
        params.addAll(Arrays.asList(paramGoki, paramGoki));

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
