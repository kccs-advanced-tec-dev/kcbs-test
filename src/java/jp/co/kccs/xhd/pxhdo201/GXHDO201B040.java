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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO201B040Model;
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
 * 変更日	2019/12/28<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 電気特性履歴検索画面
 *
 * @author 863 K.Zhang
 * @since  2019/12/28
 */
@Named
@ViewScoped
public class GXHDO201B040 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B040.class.getName());
    
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
    
    /** パラメータマスタ操作 */
    @Inject
    private SelectParam selectParam;
    
    /** 一覧表示データ */
    private List<GXHDO201B040Model> listData = null;
    
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
    /** 検索条件：検査場所 */
    private String kensaBasyo = "";
    /** 検索条件：開始日(FROM) */
    private String senbetuStartDateF = "";
    /** 検索条件：開始日(TO) */
    private String senbetuStartDateT = "";
    /** 検索条件：開始時刻(FROM) */
    private String senbetuStartTimeF = "";
    /** 検索条件：開始時刻(TO) */
    private String senbetuStartTimeT = "";
    /** 検索条件：終了日(FROM) */
    private String senbetuEndDateF = "";
    /** 検索条件：終了日(TO) */
    private String senbetuEndDateT = "";
    /** 検索条件：終了時刻(FROM) */
    private String senbetuEndTimeF = "";
    /** 検索条件：終了時刻(TO) */
    private String senbetuEndTimeT = "";
    //表示可能ﾃﾞｰﾀ
    private String possibleData[];
    
    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B040() {
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
    public List<GXHDO201B040Model> getListData() {
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
     * 検索条件：検査場所
     * @return the kensaBasyo
     */
    public String getKensaBasyo() {
        return kensaBasyo;
    }

    /**
     * 検索条件：検査場所
     * @param kensaBasyo the kensaBasyo to set
     */
    public void setKensaBasyo(String kensaBasyo) {
        this.kensaBasyo = kensaBasyo;
    }

    /**
     * 検索条件：選別開始日(from)
     * @return the senbetuStartDateF
     */
    public String getSenbetuStartDateF() {
        return senbetuStartDateF;
    }

    /**
     * 検索条件：選別開始日(from)
     * @param senbetuStartDateF the senbetuStartDateF to set
     */
    public void setSenbetuStartDateF(String senbetuStartDateF) {
        this.senbetuStartDateF = senbetuStartDateF;
    }

    /**
     * 検索条件：選別開始日(to)
     * @return the senbetuStartDateT
     */
    public String getSenbetuStartDateT() {
        return senbetuStartDateT;
    }

    /**
     * 検索条件：選別開始日(to)
     * @param senbetuStartDateT the senbetuStartDateT to set
     */
    public void setSenbetuStartDateT(String senbetuStartDateT) {
        this.senbetuStartDateT = senbetuStartDateT;
    }

    /**
     * 検索条件：選別開始時刻(from)
     * @return the senbetuStartTimeF
     */
    public String getSenbetuStartTimeF() {
        return senbetuStartTimeF;
    }

    /**
     * 検索条件：選別開始時刻(from)
     * @param senbetuStartTimeF the senbetuStartTimeF to set
     */
    public void setSenbetuStartTimeF(String senbetuStartTimeF) {
        this.senbetuStartTimeF = senbetuStartTimeF;
    }

    /**
     * 検索条件：選別開始時刻(to)
     * @return the senbetuStartTimeT
     */
    public String getSenbetuStartTimeT() {
        return senbetuStartTimeT;
    }

    /**
     * 検索条件：選別開始時刻(to)
     * @param senbetuStartTimeT the senbetuStartTimeT to set
     */
    public void setSenbetuStartTimeT(String senbetuStartTimeT) {
        this.senbetuStartTimeT = senbetuStartTimeT;
    }

    /**
     * 検索条件：選別終了日(from)
     * @return the senbetuEndDateF
     */
    public String getSenbetuEndDateF() {
        return senbetuEndDateF;
    }

    /**
     * 検索条件：選別終了日(from)
     * @param senbetuEndDateF the senbetuEndDateF to set
     */
    public void setSenbetuEndDateF(String senbetuEndDateF) {
        this.senbetuEndDateF = senbetuEndDateF;
    }

    /**
     * 検索条件：選別終了日(to)
     * @return the senbetuEndDateT
     */
    public String getSenbetuEndDateT() {
        return senbetuEndDateT;
    }

    /**
     * 検索条件：選別終了日(to)
     * @param senbetuEndDateT the senbetuEndDateT to set
     */
    public void setSenbetuEndDateT(String senbetuEndDateT) {
        this.senbetuEndDateT = senbetuEndDateT;
    }

    /**
     * 検索条件：選別終了時刻(from)
     * @return the senbetuEndTimeF
     */
    public String getSenbetuEndTimeF() {
        return senbetuEndTimeF;
    }

    /**
     * 検索条件：選別終了時刻(from)
     * @param senbetuEndTimeF the senbetuEndTimeF to set
     */
    public void setSenbetuEndTimeF(String senbetuEndTimeF) {
        this.senbetuEndTimeF = senbetuEndTimeF;
    }

    /**
     * 検索条件：選別終了時刻(to)
     * @return the senbetuEndTimeT
     */
    public String getSenbetuEndTimeT() {
        return senbetuEndTimeT;
    }
    
    /**
     * 検索条件：選別終了時刻(to)
     * @param senbetuEndTimeT the senbetuEndTimeT to set
     */
    public void setSenbetuEndTimeT(String senbetuEndTimeT) {
        this.senbetuEndTimeT = senbetuEndTimeT;
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
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group"); 

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B040_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B040_display_page_count", session));
        }

        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
            
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List7 = "";
        //■表示可能ﾃﾞｰﾀ取得処理
        //①Ⅲ.画面表示仕様(7)を発行する。
        Map fxhbm03Data7 = loadFxhbm03Data(userGrpList);
        //  1.取得できなかった場合、ｴﾗｰ。以降の処理は実行しない。
        if (fxhbm03Data7 == null || fxhbm03Data7.isEmpty()) {
            // ・ｴﾗｰｺｰﾄﾞ:XHD-000172
            // ・ｴﾗ-ﾒｯｾｰｼﾞ:電気特性履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
            // メッセージを画面に渡す
            settingError();
            return;
        } else {
            for (Object data : fxhbm03Data7.values()) {
                //取得したﾃﾞｰﾀが ALL の場合
                if ("ALL".equals(fxhbm03Data7.get(data))) {
                    strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                    possibleData = strfxhbm03List7.split(",");
                    //取得したﾃﾞｰﾀが NULL の場合、ｴﾗｰ。以降の処理は実行しない。
                } else if (data == null || "".equals(data)) {
                    //・ｴﾗｰｺｰﾄﾞ:XHD-000172
                    //・ｴﾗ-ﾒｯｾｰｼﾞ:電気特性履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
                    settingError();
                    return;
                } else {
                    //取得したﾃﾞｰﾀが ALL以外の場合
                    strfxhbm03List7 = StringUtil.nullToBlank(getMapData(fxhbm03Data7, "data"));
                    possibleData = strfxhbm03List7.split(",");
                }
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
        messageList.add(MessageUtil.getMessage("XHD-000172"));
        displayStyle = "display:none;";
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(messageList);
        RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
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
        senbetuStartDateF = "";
        senbetuStartDateT = "";
        senbetuStartTimeF = "";
        senbetuStartTimeT = "";
        senbetuEndDateF = "";
        senbetuEndDateT = "";
        senbetuEndTimeF = "";
        senbetuEndTimeT = "";
        
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
        // 選別開始日(FROM)
        if (existError(validateUtil.checkC101(getSenbetuStartDateF(), "選別開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartDateF(), "選別開始日(from)")) ||
            existError(validateUtil.checkC501(getSenbetuStartDateF(), "選別開始日(from)"))) {
            return;
        }
        // 選別開始日(TO)
        if (existError(validateUtil.checkC101(getSenbetuStartDateT(), "選別開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartDateT(), "選別開始日(to)")) ||
            existError(validateUtil.checkC501(getSenbetuStartDateT(), "選別開始日(to)"))) {
            return;
        }
        // 選別終了日(FROM)
        if (existError(validateUtil.checkC101(getSenbetuEndDateF(), "選別終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndDateF(), "選別終了日(from)")) ||
            existError(validateUtil.checkC501(getSenbetuEndDateF(), "選別終了日(from)"))) {
            return;
        }
        // 選別終了日(TO)
        if (existError(validateUtil.checkC101(getSenbetuEndDateT(), "選別終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndDateT(), "選別終了日(to)")) ||
            existError(validateUtil.checkC501(getSenbetuEndDateT(), "選別終了日(to)"))) {
            return;
        }
        // 選別開始時刻(FROM)
        if (existError(validateUtil.checkC101(getSenbetuStartTimeF(), "選別開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartTimeF(), "選別開始時刻(from)")) ||
            existError(validateUtil.checkC502(getSenbetuStartTimeF(), "選別開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuStartTimeF) && existError(validateUtil.checkC001(getSenbetuStartDateF(), "選別開始日(from)"))) {
            return;
        }
        // 選別開始時刻(TO)
        if (existError(validateUtil.checkC101(getSenbetuStartTimeT(), "選別開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuStartTimeT(), "選別開始時刻(to)")) ||
            existError(validateUtil.checkC502(getSenbetuStartTimeT(), "選別開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuStartTimeT) && existError(validateUtil.checkC001(getSenbetuStartDateT(), "選別開始日(to)"))) {
            return;
        }
        // 選別終了時刻(FROM)
        if (existError(validateUtil.checkC101(getSenbetuEndTimeF(), "選別終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndTimeF(), "選別終了時刻(from)")) ||
            existError(validateUtil.checkC502(getSenbetuEndTimeF(), "選別終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuEndTimeF) && existError(validateUtil.checkC001(getSenbetuEndDateF(), "選別終了日(from)"))) {
            return;
        }
        // 選別終了時刻(TO)
        if (existError(validateUtil.checkC101(getSenbetuEndTimeT(), "選別終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getSenbetuEndTimeT(), "選別終了時刻(to)")) ||
            existError(validateUtil.checkC502(getSenbetuEndTimeT(), "選別終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(senbetuEndTimeT) && existError(validateUtil.checkC001(getSenbetuEndDateT(), "選別終了日(to)"))) {
            return;
        }       
        
        if (possibleData == null) {
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
        //検査場所データリスト
        List<String> kensaBasyoDataList = new ArrayList<>(Arrays.asList(possibleData));
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM sr_denkitokuseiesi "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') ";

            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    sql += " AND ";
                    sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
                }
            }

            sql += " AND   (? IS NULL OR SENBETUKAISINITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUKAISINITIJI <= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI <= ?) ";

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
            String sql = "SELECT CONCAT(IFNULL(KOJYO, ''), IFNULL(LOTNO, ''), IFNULL(EDABAN, '')) AS LOTNO"
                    + ", kaisuu "
                    + ", kcpno "
                    + ", tokuisaki "
                    + ", ownercode "
                    + ", lotkubuncode "
                    + ", siteikousa "
                    + ", atokouteisijinaiyou "
                    + ", okuriryouhinsuu "
                    + ", ukeiretannijyuryo "
                    + ", ukeiresoujyuryou "
                    + ", gdyakitukenitiji "
                    + ", mekkinitiji "
                    + ", kensabasyo "
                    + ", senbetukaisinitiji "
                    + ", senbetusyuryounitiji "
                    + ", kensagouki "
                    + ", bunruiairatu "
                    + ", cdcontactatu "
                    + ", ircontactatu "
                    + ", stationcd1 "
                    + ", stationpc1 "
                    + ", stationpc2 "
                    + ", stationpc3 "
                    + ", stationpc4 "
                    + ", stationir1 "
                    + ", stationir2 "
                    + ", stationir3 "
                    + ", stationir4 "
                    + ", stationir5 "
                    + ", stationir6 "
                    + ", stationir7 "
                    + ", stationir8 "
                    + ", koteidenkyoku "
                    + ", torakkugaido "
                    + ", testplatekeijo "
                    + ", bunruifukidasi "
                    + ", testplatekakunin "
                    + ", denkyokuseisou "
                    + ", senbetujunjo "
                    + ", setteikakunin "
                    + ", haisenkakunin "
                    + ", seihintounyuujotai "
                    + ", binboxseisoucheck "
                    + ", setsya "
                    + ", kakuninsya "
                    + ", siteikousabudomari1 "
                    + ", siteikousabudomari2 "
                    + ", testplatekanrino "
                    + ", tan "
                    + ", sokuteisyuhasuu "
                    + ", sokuteidenatu "
                    + ", hoseiyoutippuyoryou "
                    + ", hoseiyoutipputan "
                    + ", hoseimae "
                    + ", hoseigo "
                    + ", hoseiritu "
                    + ", standard "
                    + ", bunruikakunin "
                    + ", gaikankakunin "
                    + ", netsusyorinitiji "
                    + ", agingjikan "
                    + ", jutenritu "
                    + ", mc "
                    + ", kyoseihaisyutu "
                    + ", rakka "
                    + ", syoninsha "
                    + ", furimukesya "
                    + ", bikou1 "
                    + ", bikou2 "
                    + ", pcdenatu1 "
                    + ", pcjudenjikan1 "
                    + ", pcdenatu2 "
                    + ", pcjudenjikan2 "
                    + ", pcdenatu3 "
                    + ", pcjudenjikan3 "
                    + ", pcdenatu4 "
                    + ", pcjudenjikan4 "
                    + ", irdenatu1 "
                    + ", irhanteiti1 "
                    + ", irjudenjikan1 "
                    + ", irdenatu2 "
                    + ", irhanteiti2 "
                    + ", irjudenjikan2 "
                    + ", irdenatu3 "
                    + ", irhanteiti3 "
                    + ", irjudenjikan3 "
                    + ", irdenatu4 "
                    + ", irhanteiti4 "
                    + ", irjudenjikan4 "
                    + ", irdenatu5 "
                    + ", irhanteiti5 "
                    + ", irjudenjikan5 "
                    + ", irdenatu6 "
                    + ", irhanteiti6 "
                    + ", irjudenjikan6 "
                    + ", irdenatu7 "
                    + ", irhanteiti7 "
                    + ", irjudenjikan7 "
                    + ", irdenatu8 "
                    + ", irhanteiti8 "
                    + ", irjudenjikan8 "
                    + ", rdcrange1 "
                    + ", rdchantei1 "
                    + ", rdcrange2 "
                    + ", rdchantei2 "
                    + ", drop13pc "
                    + ", drop13ps "
                    + ", drop13msdc "
                    + ", drop24pc "
                    + ", drop24ps "
                    + ", drop24msdc "
                    + ", bin1setteiti "
                    + ", bin1senbetukubun "
                    + ", bin1keiryougosuryou "
                    + ", bin1countersuu "
                    + ", bin1gosaritu "
                    + ", bin1masinfuryouritu "
                    + ", bin1nukitorikekkabosuu "
                    + ", bin1nukitorikekka "
                    + ", bin1sinnofuryouritu "
                    + ", bin1kekkacheck "
                    + ", bin2setteiti "
                    + ", bin2senbetukubun "
                    + ", bin2keiryougosuryou "
                    + ", bin2countersuu "
                    + ", bin2gosaritu "
                    + ", bin2masinfuryouritu "
                    + ", bin2nukitorikekkabosuu "
                    + ", bin2nukitorikekka "
                    + ", bin2sinnofuryouritu "
                    + ", bin2kekkacheck "
                    + ", bin3setteiti "
                    + ", bin3senbetukubun "
                    + ", bin3keiryougosuryou "
                    + ", bin3countersuu "
                    + ", bin3gosaritu "
                    + ", bin3masinfuryouritu "
                    + ", bin3nukitorikekkabosuu "
                    + ", bin3nukitorikekka "
                    + ", bin3sinnofuryouritu "
                    + ", bin3kekkacheck "
                    + ", bin4setteiti "
                    + ", bin4senbetukubun "
                    + ", bin4keiryougosuryou "
                    + ", bin4countersuu "
                    + ", bin4gosaritu "
                    + ", bin4masinfuryouritu "
                    + ", bin4nukitorikekkabosuu "
                    + ", bin4nukitorikekka "
                    + ", bin4sinnofuryouritu "
                    + ", bin4kekkacheck "
                    + ", bin5setteiti "
                    + ", bin5senbetukubun "
                    + ", bin5keiryougosuryou "
                    + ", bin5countersuu "
                    + ", bin5gosaritu "
                    + ", bin5masinfuryouritu "
                    + ", bin5nukitorikekkabosuu "
                    + ", bin5nukitorikekka "
                    + ", bin5sinnofuryouritu "
                    + ", bin5kekkacheck "
                    + ", bin5fukurocheck "
                    + ", bin6setteiti "
                    + ", bin6senbetukubun "
                    + ", bin6keiryougosuryou "
                    + ", bin6countersuu "
                    + ", bin6gosaritu "
                    + ", bin6masinfuryouritu "
                    + ", bin6nukitorikekkabosuu "
                    + ", bin6nukitorikekka "
                    + ", bin6sinnofuryouritu "
                    + ", bin6kekkacheck "
                    + ", bin6fukurocheck "
                    + ", bin7setteiti "
                    + ", bin7senbetukubun "
                    + ", bin7keiryougosuryou "
                    + ", bin7countersuu "
                    + ", bin7gosaritu "
                    + ", bin7masinfuryouritu "
                    + ", bin7fukurocheck "
                    + ", bin8setteiti "
                    + ", bin8senbetukubun "
                    + ", bin8keiryougosuryou "
                    + ", bin8countersuu "
                    + ", bin8gosaritu "
                    + ", bin8masinfuryouritu "
                    + ", bin8fukurocheck "
                    + ", bin9keiryougosuryou "
                    + ", bin9masinfuryouritu "
                    + ", rakkakeiryougosuryou "
                    + ", rakkamasinfuryouritu "
                    + ", handasample "
                    + ", sinraiseisample "
                    + ", sinfuryouhanteisya "
                    + ", hanteinyuuryokusya "
                    + ", toridasisya "
                    + ", kousa1 "
                    + ", juryou1 "
                    + ", kosuu1 "
                    + ", kousa2 "
                    + ", juryou2 "
                    + ", kosuu2 "
                    + ", kousa3 "
                    + ", juryou3 "
                    + ", kosuu3 "
                    + ", kousa4 "
                    + ", juryou4 "
                    + ", kosuu4 "
                    + ", countersousuu "
                    + ", ryohinjuryou "
                    + ", ryohinkosuu "
                    + ", budomari "
                    + ", binkakuninsya "
                    + ", saiken "
                    + ", setubikubun "
                    + "  FROM sr_denkitokuseiesi "
                    + " WHERE (? IS NULL OR KOJYO = ?) "
                    + " AND   (? IS NULL OR LOTNO = ?) "
                    + " AND   (? IS NULL OR EDABAN = ?) "
                    + " AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') ";
            for (String data : kensaBasyoDataList) {
                if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                    sql += " AND ";
                    sql += DBUtil.getInConditionPreparedStatement("kensabasyo", kensaBasyoDataList.size());
                }
            }
            sql += " AND   (? IS NULL OR SENBETUKAISINITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUKAISINITIJI <= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI >= ?) "
                    + " AND   (? IS NULL OR SENBETUSYURYOUNITIJI <= ?) "
                    + " ORDER BY SENBETUKAISINITIJI ";            
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");// ﾛｯﾄNo
            mapping.put("kaisuu", "kaisuu");// 回数
            mapping.put("kcpno", "kcpno");// KCPNO
            mapping.put("tokuisaki", "tokuisaki");// 客先
            mapping.put("ownercode", "ownercode");// ｵｰﾅｰ
            mapping.put("lotkubuncode", "lotkubuncode");// ﾛｯﾄ区分
            mapping.put("siteikousa", "siteikousa");// 指定公差
            mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou");// 後工程指示内容
            mapping.put("okuriryouhinsuu", "okuriryouhinsuu");// 送り良品数
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo");// 受入れ単位重量
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou");// 受入れ総重量
            mapping.put("gdyakitukenitiji", "gdyakitukenitiji");// 外部電極焼付日時
            mapping.put("mekkinitiji", "mekkinitiji");// ﾒｯｷ日時
            mapping.put("kensabasyo", "kensabasyo");// 検査場所
            mapping.put("senbetukaisinitiji", "senbetukaisinitiji");// 選別開始日時
            mapping.put("senbetusyuryounitiji", "senbetusyuryounitiji");// 選別終了日時
            mapping.put("kensagouki", "kensagouki");// 検査号機
            mapping.put("bunruiairatu", "bunruiairatu");// 分類ｴｱｰ圧
            mapping.put("cdcontactatu", "cdcontactatu");// CDｺﾝﾀｸﾄ圧
            mapping.put("ircontactatu", "ircontactatu");// IRｺﾝﾀｸﾄ圧
            mapping.put("stationcd1", "stationcd1");// 使用後ｽﾃｰｼｮﾝ確認CD1
            mapping.put("stationpc1", "stationpc1");// 使用後ｽﾃｰｼｮﾝ確認PC1
            mapping.put("stationpc2", "stationpc2");// 使用後ｽﾃｰｼｮﾝ確認PC2
            mapping.put("stationpc3", "stationpc3");// 使用後ｽﾃｰｼｮﾝ確認PC3
            mapping.put("stationpc4", "stationpc4");// 使用後ｽﾃｰｼｮﾝ確認PC4
            mapping.put("stationir1", "stationir1");// 使用後ｽﾃｰｼｮﾝ確認IR1
            mapping.put("stationir2", "stationir2");// 使用後ｽﾃｰｼｮﾝ確認IR2
            mapping.put("stationir3", "stationir3");// 使用後ｽﾃｰｼｮﾝ確認IR3
            mapping.put("stationir4", "stationir4");// 使用後ｽﾃｰｼｮﾝ確認IR4
            mapping.put("stationir5", "stationir5");// 使用後ｽﾃｰｼｮﾝ確認IR5
            mapping.put("stationir6", "stationir6");// 使用後ｽﾃｰｼｮﾝ確認IR6
            mapping.put("stationir7", "stationir7");// 使用後ｽﾃｰｼｮﾝ確認IR7
            mapping.put("stationir8", "stationir8");// 使用後ｽﾃｰｼｮﾝ確認IR8
            mapping.put("koteidenkyoku", "koteidenkyoku");// 固定電極外観･段差
            mapping.put("torakkugaido", "torakkugaido");// ﾄﾗｯｸｶﾞｲﾄﾞ隙間
            mapping.put("testplatekeijo", "testplatekeijo");// ﾃｽﾄﾌﾟﾚｰﾄ形状･清掃
            mapping.put("bunruifukidasi", "bunruifukidasi");// 分類吹き出し穴
            mapping.put("testplatekakunin", "testplatekakunin");// ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
            mapping.put("denkyokuseisou", "denkyokuseisou");// 電極清掃･動作
            mapping.put("senbetujunjo", "senbetujunjo");// 選別順序変更
            mapping.put("setteikakunin", "setteikakunin");// 設定ﾓｰﾄﾞ確認
            mapping.put("haisenkakunin", "haisenkakunin");// 配線確認
            mapping.put("seihintounyuujotai", "seihintounyuujotai");// 製品投入状態
            mapping.put("binboxseisoucheck", "binboxseisoucheck");// BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
            mapping.put("setsya", "setsya");// ｾｯﾄ者
            mapping.put("kakuninsya", "kakuninsya");// 確認者
            mapping.put("siteikousabudomari1", "siteikousabudomari1");// 指定公差歩留まり1
            mapping.put("siteikousabudomari2", "siteikousabudomari2");// 指定公差歩留まり2
            mapping.put("testplatekanrino", "testplatekanrino");// ﾃｽﾄﾌﾟﾚｰﾄ管理No
            mapping.put("tan", "tan");// Tanδ
            mapping.put("sokuteisyuhasuu", "sokuteisyuhasuu");// 測定周波数
            mapping.put("sokuteidenatu", "sokuteidenatu");// 測定電圧
            mapping.put("hoseiyoutippuyoryou", "hoseiyoutippuyoryou");// 補正用ﾁｯﾌﾟ容量
            mapping.put("hoseiyoutipputan", "hoseiyoutipputan");// 補正用ﾁｯﾌﾟTanδ
            mapping.put("hoseimae", "hoseimae");// 補正前
            mapping.put("hoseigo", "hoseigo");// 補正後
            mapping.put("hoseiritu", "hoseiritu");// 補正率
            mapping.put("standard", "standard");// ｽﾀﾝﾀﾞｰﾄﾞ補正
            mapping.put("bunruikakunin", "bunruikakunin");// 分類確認
            mapping.put("gaikankakunin", "gaikankakunin");// 外観確認
            mapping.put("netsusyorinitiji", "netsusyorinitiji");// 熱処理日時
            mapping.put("agingjikan", "agingjikan");// ｴｰｼﾞﾝｸﾞ時間
            mapping.put("jutenritu", "jutenritu");// 充填率
            mapping.put("mc", "mc");// MC
            mapping.put("kyoseihaisyutu", "kyoseihaisyutu");// 強制排出
            mapping.put("rakka", "rakka");// 落下
            mapping.put("syoninsha", "syoninsha");// 承認者
            mapping.put("furimukesya", "furimukesya");// 振向者
            mapping.put("bikou1", "bikou1");// 備考1
            mapping.put("bikou2", "bikou2");// 備考2
            mapping.put("pcdenatu1", "pcdenatu1");// ﾌﾟﾘﾁｬｰｼﾞ条件PC①電圧
            mapping.put("pcjudenjikan1", "pcjudenjikan1");// ﾌﾟﾘﾁｬｰｼﾞ条件PC①充電時間
            mapping.put("pcdenatu2", "pcdenatu2");// ﾌﾟﾘﾁｬｰｼﾞ条件PC②電圧
            mapping.put("pcjudenjikan2", "pcjudenjikan2");// ﾌﾟﾘﾁｬｰｼﾞ条件PC②充電時間
            mapping.put("pcdenatu3", "pcdenatu3");// ﾌﾟﾘﾁｬｰｼﾞ条件PC③電圧
            mapping.put("pcjudenjikan3", "pcjudenjikan3");// ﾌﾟﾘﾁｬｰｼﾞ条件PC③充電時間
            mapping.put("pcdenatu4", "pcdenatu4");// ﾌﾟﾘﾁｬｰｼﾞ条件PC④電圧
            mapping.put("pcjudenjikan4", "pcjudenjikan4");// ﾌﾟﾘﾁｬｰｼﾞ条件PC④充電時間
            mapping.put("irdenatu1", "irdenatu1");// 耐電圧設定条件IR①電圧
            mapping.put("irhanteiti1", "irhanteiti1");// 耐電圧設定条件IR①判定値
            mapping.put("irjudenjikan1", "irjudenjikan1");// 耐電圧設定条件IR①充電時間
            mapping.put("irdenatu2", "irdenatu2");// 耐電圧設定条件IR②電圧
            mapping.put("irhanteiti2", "irhanteiti2");// 耐電圧設定条件IR②判定値
            mapping.put("irjudenjikan2", "irjudenjikan2");// 耐電圧設定条件IR②充電時間
            mapping.put("irdenatu3", "irdenatu3");// 耐電圧設定条件IR③電圧
            mapping.put("irhanteiti3", "irhanteiti3");// 耐電圧設定条件IR③判定値
            mapping.put("irjudenjikan3", "irjudenjikan3");// 耐電圧設定条件IR③充電時間
            mapping.put("irdenatu4", "irdenatu4");// 耐電圧設定条件IR④電圧
            mapping.put("irhanteiti4", "irhanteiti4");// 耐電圧設定条件IR④判定値
            mapping.put("irjudenjikan4", "irjudenjikan4");// 耐電圧設定条件IR④充電時間
            mapping.put("irdenatu5", "irdenatu5");// 耐電圧設定条件IR⑤電圧
            mapping.put("irhanteiti5", "irhanteiti5");// 耐電圧設定条件IR⑤判定値
            mapping.put("irjudenjikan5", "irjudenjikan5");// 耐電圧設定条件IR⑤充電時間
            mapping.put("irdenatu6", "irdenatu6");// 耐電圧設定条件IR⑥電圧
            mapping.put("irhanteiti6", "irhanteiti6");// 耐電圧設定条件IR⑥判定値
            mapping.put("irjudenjikan6", "irjudenjikan6");// 耐電圧設定条件IR⑥充電時間
            mapping.put("irdenatu7", "irdenatu7");// 耐電圧設定条件IR⑦電圧
            mapping.put("irhanteiti7", "irhanteiti7");// 耐電圧設定条件IR⑦判定値
            mapping.put("irjudenjikan7", "irjudenjikan7");// 耐電圧設定条件IR⑦充電時間
            mapping.put("irdenatu8", "irdenatu8");// 耐電圧設定条件IR⑧電圧
            mapping.put("irhanteiti8", "irhanteiti8");// 耐電圧設定条件IR⑧判定値
            mapping.put("irjudenjikan8", "irjudenjikan8");// 耐電圧設定条件IR⑧充電時間
            mapping.put("rdcrange1", "rdcrange1");// RDC1ﾚﾝｼﾞ
            mapping.put("rdchantei1", "rdchantei1");// RDC1判定値
            mapping.put("rdcrange2", "rdcrange2");// RDC2ﾚﾝｼﾞ
            mapping.put("rdchantei2", "rdchantei2");// RDC2判定値
            mapping.put("drop13pc", "drop13pc");// DROP1,3PC
            mapping.put("drop13ps", "drop13ps");// DROP1,3PS
            mapping.put("drop13msdc", "drop13msdc");// DROP1,3MS･DC
            mapping.put("drop24pc", "drop24pc");// DROP2,4PC
            mapping.put("drop24ps", "drop24ps");// DROP2,4PS
            mapping.put("drop24msdc", "drop24msdc");// DROP2,4MS･DC
            mapping.put("bin1setteiti", "bin1setteiti");// BIN1%区分(設定値)
            mapping.put("bin1senbetukubun", "bin1senbetukubun");// BIN1選別区分
            mapping.put("bin1keiryougosuryou", "bin1keiryougosuryou");// BIN1計量後数量
            mapping.put("bin1countersuu", "bin1countersuu");// BIN1ｶｳﾝﾀｰ数
            mapping.put("bin1gosaritu", "bin1gosaritu");// BIN1誤差率(%)
            mapping.put("bin1masinfuryouritu", "bin1masinfuryouritu");// BIN1ﾏｼﾝ不良率(%)
            mapping.put("bin1nukitorikekkabosuu", "bin1nukitorikekkabosuu");// BIN1抜き取り結果
            mapping.put("bin1nukitorikekka", "bin1nukitorikekka");// BIN1抜き取り結果
            mapping.put("bin1sinnofuryouritu", "bin1sinnofuryouritu");// BIN1真の不良率(%)
            mapping.put("bin1kekkacheck", "bin1kekkacheck");// BIN1結果ﾁｪｯｸ
            mapping.put("bin2setteiti", "bin2setteiti");// BIN2%区分(設定値)
            mapping.put("bin2senbetukubun", "bin2senbetukubun");// BIN2選別区分
            mapping.put("bin2keiryougosuryou", "bin2keiryougosuryou");// BIN2計量後数量
            mapping.put("bin2countersuu", "bin2countersuu");// BIN2ｶｳﾝﾀｰ数
            mapping.put("bin2gosaritu", "bin2gosaritu");// BIN2誤差率(%)
            mapping.put("bin2masinfuryouritu", "bin2masinfuryouritu");// BIN2ﾏｼﾝ不良率(%)
            mapping.put("bin2nukitorikekkabosuu", "bin2nukitorikekkabosuu");// BIN2抜き取り結果
            mapping.put("bin2nukitorikekka", "bin2nukitorikekka");// BIN2抜き取り結果
            mapping.put("bin2sinnofuryouritu", "bin2sinnofuryouritu");// BIN2真の不良率(%)
            mapping.put("bin2kekkacheck", "bin2kekkacheck");// BIN2結果ﾁｪｯｸ
            mapping.put("bin3setteiti", "bin3setteiti");// BIN3%区分(設定値)
            mapping.put("bin3senbetukubun", "bin3senbetukubun");// BIN3選別区分
            mapping.put("bin3keiryougosuryou", "bin3keiryougosuryou");// BIN3計量後数量
            mapping.put("bin3countersuu", "bin3countersuu");// BIN3ｶｳﾝﾀｰ数
            mapping.put("bin3gosaritu", "bin3gosaritu");// BIN3誤差率(%)
            mapping.put("bin3masinfuryouritu", "bin3masinfuryouritu");// BIN3ﾏｼﾝ不良率(%)
            mapping.put("bin3nukitorikekkabosuu", "bin3nukitorikekkabosuu");// BIN3抜き取り結果
            mapping.put("bin3nukitorikekka", "bin3nukitorikekka");// BIN3抜き取り結果
            mapping.put("bin3sinnofuryouritu", "bin3sinnofuryouritu");// BIN3真の不良率(%)
            mapping.put("bin3kekkacheck", "bin3kekkacheck");// BIN3結果ﾁｪｯｸ
            mapping.put("bin4setteiti", "bin4setteiti");// BIN4%区分(設定値)
            mapping.put("bin4senbetukubun", "bin4senbetukubun");// BIN4選別区分
            mapping.put("bin4keiryougosuryou", "bin4keiryougosuryou");// BIN4計量後数量
            mapping.put("bin4countersuu", "bin4countersuu");// BIN4ｶｳﾝﾀｰ数
            mapping.put("bin4gosaritu", "bin4gosaritu");// BIN4誤差率(%)
            mapping.put("bin4masinfuryouritu", "bin4masinfuryouritu");// BIN4ﾏｼﾝ不良率(%)
            mapping.put("bin4nukitorikekkabosuu", "bin4nukitorikekkabosuu");// BIN4抜き取り結果
            mapping.put("bin4nukitorikekka", "bin4nukitorikekka");// BIN4抜き取り結果
            mapping.put("bin4sinnofuryouritu", "bin4sinnofuryouritu");// BIN4真の不良率(%)
            mapping.put("bin4kekkacheck", "bin4kekkacheck");// BIN4結果ﾁｪｯｸ
            mapping.put("bin5setteiti", "bin5setteiti");// BIN5%区分(設定値)
            mapping.put("bin5senbetukubun", "bin5senbetukubun");// BIN5選別区分
            mapping.put("bin5keiryougosuryou", "bin5keiryougosuryou");// BIN5計量後数量
            mapping.put("bin5countersuu", "bin5countersuu");// BIN5ｶｳﾝﾀｰ数
            mapping.put("bin5gosaritu", "bin5gosaritu");// BIN5誤差率(%)
            mapping.put("bin5masinfuryouritu", "bin5masinfuryouritu");// BIN5ﾏｼﾝ不良率(%)
            mapping.put("bin5nukitorikekkabosuu", "bin5nukitorikekkabosuu");// BIN5抜き取り結果
            mapping.put("bin5nukitorikekka", "bin5nukitorikekka");// BIN5抜き取り結果
            mapping.put("bin5sinnofuryouritu", "bin5sinnofuryouritu");// BIN5真の不良率(%)
            mapping.put("bin5kekkacheck", "bin5kekkacheck");// BIN5結果ﾁｪｯｸ
            mapping.put("bin5fukurocheck", "bin5fukurocheck");// BIN5袋ﾁｪｯｸ
            mapping.put("bin6setteiti", "bin6setteiti");// BIN6%区分(設定値)
            mapping.put("bin6senbetukubun", "bin6senbetukubun");// BIN6選別区分
            mapping.put("bin6keiryougosuryou", "bin6keiryougosuryou");// BIN6計量後数量
            mapping.put("bin6countersuu", "bin6countersuu");// BIN6ｶｳﾝﾀｰ数
            mapping.put("bin6gosaritu", "bin6gosaritu");// BIN6誤差率(%)
            mapping.put("bin6masinfuryouritu", "bin6masinfuryouritu");// BIN6ﾏｼﾝ不良率(%)
            mapping.put("bin6nukitorikekkabosuu", "bin6nukitorikekkabosuu");// BIN6抜き取り結果
            mapping.put("bin6nukitorikekka", "bin6nukitorikekka");// BIN6抜き取り結果
            mapping.put("bin6sinnofuryouritu", "bin6sinnofuryouritu");// BIN6真の不良率(%)
            mapping.put("bin6kekkacheck", "bin6kekkacheck");// BIN6結果ﾁｪｯｸ
            mapping.put("bin6fukurocheck", "bin6fukurocheck");// BIN6袋ﾁｪｯｸ
            mapping.put("bin7setteiti", "bin7setteiti");// BIN7%区分(設定値)
            mapping.put("bin7senbetukubun", "bin7senbetukubun");// BIN7選別区分
            mapping.put("bin7keiryougosuryou", "bin7keiryougosuryou");// BIN7計量後数量
            mapping.put("bin7countersuu", "bin7countersuu");// BIN7ｶｳﾝﾀｰ数
            mapping.put("bin7gosaritu", "bin7gosaritu");// BIN7誤差率(%)
            mapping.put("bin7masinfuryouritu", "bin7masinfuryouritu");// BIN7ﾏｼﾝ不良率(%)
            mapping.put("bin7fukurocheck", "bin7fukurocheck");// BIN7袋ﾁｪｯｸ
            mapping.put("bin8setteiti", "bin8setteiti");// BIN8%区分(設定値)
            mapping.put("bin8senbetukubun", "bin8senbetukubun");// BIN8選別区分
            mapping.put("bin8keiryougosuryou", "bin8keiryougosuryou");// BIN8計量後数量
            mapping.put("bin8countersuu", "bin8countersuu");// BIN8ｶｳﾝﾀｰ数
            mapping.put("bin8gosaritu", "bin8gosaritu");// BIN8誤差率(%)
            mapping.put("bin8masinfuryouritu", "bin8masinfuryouritu");// BIN8ﾏｼﾝ不良率(%)
            mapping.put("bin8fukurocheck", "bin8fukurocheck");// BIN8袋ﾁｪｯｸ
            mapping.put("bin9keiryougosuryou", "bin9keiryougosuryou");// BIN9強制排出計量後数量
            mapping.put("bin9masinfuryouritu", "bin9masinfuryouritu");// BIN9強制排出ﾏｼﾝ不良率
            mapping.put("rakkakeiryougosuryou", "rakkakeiryougosuryou");// 落下計量後数量
            mapping.put("rakkamasinfuryouritu", "rakkamasinfuryouritu");// 落下ﾏｼﾝ不良率
            mapping.put("handasample", "handasample");// 半田ｻﾝﾌﾟﾙ
            mapping.put("sinraiseisample", "sinraiseisample");// 信頼性ｻﾝﾌﾟﾙ
            mapping.put("sinfuryouhanteisya", "sinfuryouhanteisya");// 真不良判定者
            mapping.put("hanteinyuuryokusya", "hanteinyuuryokusya");// 判定入力者
            mapping.put("toridasisya", "toridasisya");// 取出者
            mapping.put("kousa1", "kousa1");// 公差①
            mapping.put("juryou1", "juryou1");// 重量①
            mapping.put("kosuu1", "kosuu1");// 個数①
            mapping.put("kousa2", "kousa2");// 公差②
            mapping.put("juryou2", "juryou2");// 重量②
            mapping.put("kosuu2", "kosuu2");// 個数②
            mapping.put("kousa3", "kousa3");// 公差③
            mapping.put("juryou3", "juryou3");// 重量③
            mapping.put("kosuu3", "kosuu3");// 個数③
            mapping.put("kousa4", "kousa4");// 公差④
            mapping.put("juryou4", "juryou4");// 重量④
            mapping.put("kosuu4", "kosuu4");// 個数④
            mapping.put("countersousuu", "countersousuu");// ｶｳﾝﾀｰ総数
            mapping.put("ryohinjuryou", "ryohinjuryou");// 良品重量
            mapping.put("ryohinkosuu", "ryohinkosuu");// 良品個数
            mapping.put("budomari", "budomari");// 歩留まり
            mapping.put("binkakuninsya", "binkakuninsya");// BIN確認者
            mapping.put("saiken", "saiken");// 電気特性再検
            mapping.put("setubikubun", "setubikubun");// 設備区分

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B040Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B040Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b040.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "電気特性");

            // ダウンロードファイル名
            String downloadFileName = "電気特性_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        //検査場所データリスト
        List<String> kensaBasyoDataList= new ArrayList<>(Arrays.asList(possibleData));
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
        Date paramSenbetuStartDateF = null;
        if (!StringUtil.isEmpty(senbetuStartDateF)) {
            paramSenbetuStartDateF = DateUtil.convertStringToDate(getSenbetuStartDateF(), StringUtil.isEmpty(getSenbetuStartTimeF()) ? "0000" : getSenbetuStartTimeF());
        }
        Date paramSenbetuStartDateT = null;
        if (!StringUtil.isEmpty(senbetuStartDateT)) {
            paramSenbetuStartDateT = DateUtil.convertStringToDate(getSenbetuStartDateT(), StringUtil.isEmpty(getSenbetuStartTimeT()) ? "2359" : getSenbetuStartTimeT());
        }
        Date paramSenbetuEndDateF = null;
        if (!StringUtil.isEmpty(senbetuEndDateF)) {
            paramSenbetuEndDateF = DateUtil.convertStringToDate(getSenbetuEndDateF(), StringUtil.isEmpty(getSenbetuEndTimeF()) ? "0000" : getSenbetuEndTimeF());
        }
        Date paramSenbetuEndDateT = null;
        if (!StringUtil.isEmpty(senbetuEndDateT)) {
            paramSenbetuEndDateT = DateUtil.convertStringToDate(getSenbetuEndDateT(), StringUtil.isEmpty(getSenbetuEndTimeT()) ? "2359" : getSenbetuEndTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        for (String data : kensaBasyoDataList) {
            if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                params.addAll(kensaBasyoDataList);
            }
        }
        params.addAll(Arrays.asList(paramSenbetuStartDateF, paramSenbetuStartDateF));
        params.addAll(Arrays.asList(paramSenbetuStartDateT, paramSenbetuStartDateT));
        params.addAll(Arrays.asList(paramSenbetuEndDateF, paramSenbetuEndDateF));
        params.addAll(Arrays.asList(paramSenbetuEndDateT, paramSenbetuEndDateT));


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
            sql = sql + " AND key = '電気特性履歴_表示可能ﾃﾞｰﾀ'";

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
