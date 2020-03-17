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
import jp.co.kccs.xhd.model.GXHDO201B046Model;
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
 * 変更日	2019/03/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外観検査履歴検索画面
 *
 * @author 863 K.Zhang
 * @since  2020/03/09
 */
@Named
@ViewScoped
public class GXHDO201B046 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B046.class.getName());
    
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
    private List<GXHDO201B046Model> listData = null;
    
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
    /** 検索条件：検査開始日(FROM) */
    private String kensaStartDateF = "";
    /** 検索条件：検査開始日(TO) */
    private String kensaStartDateT = "";
    /** 検索条件：検査開始時刻(FROM) */
    private String kensaStartTimeF = "";
    /** 検索条件：検査開始時刻(TO) */
    private String kensaStartTimeT = "";
    /** 検索条件：検査終了日(FROM) */
    private String kensaEndDateF = "";
    /** 検索条件：検査終了日(TO) */
    private String kensaEndDateT = "";
    /** 検索条件：検査終了時刻(FROM) */
    private String kensaEndTimeF = "";
    /** 検索条件：検査終了時刻(TO) */
    private String kensaEndTimeT = "";
    //表示可能ﾃﾞｰﾀ
    private String possibleData[];
    
    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";
    
    /**
     * コンストラクタ
     */
    public GXHDO201B046() {
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
    public List<GXHDO201B046Model> getListData() {
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
     * 検索条件：検査開始日(from)
     * @return the kensaStartDateF
     */
    public String getKensaStartDateF() {
        return kensaStartDateF;
    }

    /**
     * 検索条件：検査開始日(from)
     * @param kensaStartDateF the kensaStartDateF to set
     */
    public void setKensaStartDateF(String kensaStartDateF) {
        this.kensaStartDateF = kensaStartDateF;
    }

    /**
     * 検索条件：検査開始日(to)
     * @return the kensaStartDateT
     */
    public String getKensaStartDateT() {
        return kensaStartDateT;
    }

    /**
     * 検索条件：検査開始日(to)
     * @param kensaStartDateT the kensaStartDateT to set
     */
    public void setKensaStartDateT(String kensaStartDateT) {
        this.kensaStartDateT = kensaStartDateT;
    }

    /**
     * 検索条件：検査開始時刻(from)
     * @return the kensaStartTimeF
     */
    public String getKensaStartTimeF() {
        return kensaStartTimeF;
    }

    /**
     * 検索条件：検査開始時刻(from)
     * @param kensaStartTimeF the kensaStartTimeF to set
     */
    public void setKensaStartTimeF(String kensaStartTimeF) {
        this.kensaStartTimeF = kensaStartTimeF;
    }

    /**
     * 検索条件：検査開始時刻(to)
     * @return the kensaStartTimeT
     */
    public String getKensaStartTimeT() {
        return kensaStartTimeT;
    }

    /**
     * 検索条件：検査開始時刻(to)
     * @param kensaStartTimeT the kensaStartTimeT to set
     */
    public void setKensaStartTimeT(String kensaStartTimeT) {
        this.kensaStartTimeT = kensaStartTimeT;
    }

    /**
     * 検索条件：検査終了日(from)
     * @return the kensaEndDateF
     */
    public String getKensaEndDateF() {
        return kensaEndDateF;
    }

    /**
     * 検索条件：検査終了日(from)
     * @param kensaEndDateF the kensaEndDateF to set
     */
    public void setKensaEndDateF(String kensaEndDateF) {
        this.kensaEndDateF = kensaEndDateF;
    }

    /**
     * 検索条件：検査終了日(to)
     * @return the kensaEndDateT
     */
    public String getKensaEndDateT() {
        return kensaEndDateT;
    }

    /**
     * 検索条件：検査終了日(to)
     * @param kensaEndDateT the kensaEndDateT to set
     */
    public void setKensaEndDateT(String kensaEndDateT) {
        this.kensaEndDateT = kensaEndDateT;
    }

    /**
     * 検索条件：検査終了時刻(from)
     * @return the kensaEndTimeF
     */
    public String getKensaEndTimeF() {
        return kensaEndTimeF;
    }

    /**
     * 検索条件：検査終了時刻(from)
     * @param kensaEndTimeF the kensaEndTimeF to set
     */
    public void setKensaEndTimeF(String kensaEndTimeF) {
        this.kensaEndTimeF = kensaEndTimeF;
    }

    /**
     * 検索条件：検査終了時刻(to)
     * @return the kensaEndTimeT
     */
    public String getKensaEndTimeT() {
        return kensaEndTimeT;
    }
    
    /**
     * 検索条件：検査終了時刻(to)
     * @param kensaEndTimeT the kensaEndTimeT to set
     */
    public void setKensaEndTimeT(String kensaEndTimeT) {
        this.kensaEndTimeT = kensaEndTimeT;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B046_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B046_display_page_count", session));
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
            // ・ｴﾗｰｺｰﾄﾞ:XHD-000177
            // ・ｴﾗ-ﾒｯｾｰｼﾞ:外観検査履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
            // メッセージを画面に渡す
            settingError();
            return;
        } else {
            //取得したﾃﾞｰﾀが NULL の場合、ｴﾗｰ。以降の処理は実行しない。
            if (getMapData(fxhbm03Data7, "data") == null || "".equals(getMapData(fxhbm03Data7, "data"))) {
                //・ｴﾗｰｺｰﾄﾞ:XHD-000177
                //・ｴﾗ-ﾒｯｾｰｼﾞ:外観検査履歴_表示可能ﾃﾞｰﾀﾊﾟﾗﾒｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。
                settingError();
                return;
            } else {
                //取得したﾃﾞｰﾀが ALL以外の場合
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
        messageList.add(MessageUtil.getMessage("XHD-000177"));
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
        kensaStartDateF = "";
        kensaStartDateT = "";
        kensaStartTimeF = "";
        kensaStartTimeT = "";
        kensaEndDateF = "";
        kensaEndDateT = "";
        kensaEndTimeF = "";
        kensaEndTimeT = "";
        
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
        // 検査開始日(FROM)
        if (existError(validateUtil.checkC101(getKensaStartDateF(), "検査開始日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartDateF(), "検査開始日(from)")) ||
            existError(validateUtil.checkC501(getKensaStartDateF(), "検査開始日(from)"))) {
            return;
        }
        // 検査開始日(TO)
        if (existError(validateUtil.checkC101(getKensaStartDateT(), "検査開始日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartDateT(), "検査開始日(to)")) ||
            existError(validateUtil.checkC501(getKensaStartDateT(), "検査開始日(to)"))) {
            return;
        }
        // 検査終了日(FROM)
        if (existError(validateUtil.checkC101(getKensaEndDateF(), "検査終了日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndDateF(), "検査終了日(from)")) ||
            existError(validateUtil.checkC501(getKensaEndDateF(), "検査終了日(from)"))) {
            return;
        }
        // 検査終了日(TO)
        if (existError(validateUtil.checkC101(getKensaEndDateT(), "検査終了日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndDateT(), "検査終了日(to)")) ||
            existError(validateUtil.checkC501(getKensaEndDateT(), "検査終了日(to)"))) {
            return;
        }
        // 検査開始時刻(FROM)
        if (existError(validateUtil.checkC101(getKensaStartTimeF(), "検査開始時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartTimeF(), "検査開始時刻(from)")) ||
            existError(validateUtil.checkC502(getKensaStartTimeF(), "検査開始時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaStartTimeF) && existError(validateUtil.checkC001(getKensaStartDateF(), "検査開始日(from)"))) {
            return;
        }
        // 検査開始時刻(TO)
        if (existError(validateUtil.checkC101(getKensaStartTimeT(), "検査開始時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaStartTimeT(), "検査開始時刻(to)")) ||
            existError(validateUtil.checkC502(getKensaStartTimeT(), "検査開始時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaStartTimeT) && existError(validateUtil.checkC001(getKensaStartDateT(), "検査開始日(to)"))) {
            return;
        }
        // 検査終了時刻(FROM)
        if (existError(validateUtil.checkC101(getKensaEndTimeF(), "検査終了時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndTimeF(), "検査終了時刻(from)")) ||
            existError(validateUtil.checkC502(getKensaEndTimeF(), "検査終了時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaEndTimeF) && existError(validateUtil.checkC001(getKensaEndDateF(), "検査終了日(from)"))) {
            return;
        }
        // 検査終了時刻(TO)
        if (existError(validateUtil.checkC101(getKensaEndTimeT(), "検査終了時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKensaEndTimeT(), "検査終了時刻(to)")) ||
            existError(validateUtil.checkC502(getKensaEndTimeT(), "検査終了時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(kensaEndTimeT) && existError(validateUtil.checkC001(getKensaEndDateT(), "検査終了日(to)"))) {
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
                    + "FROM sr_gaikankensa "
                    + "WHERE (? IS NULL OR kojyo = ?) "
                    + "AND   (? IS NULL OR lotno = ?) "
                    + "AND   (? IS NULL OR edaban = ?) "
                    + "AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') ";

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

            sql += " AND   (? IS NULL OR kensakaishinichiji >= ?) "
                    + " AND   (? IS NULL OR kensakaishinichiji <= ?) "
                    + " AND   (? IS NULL OR kensasyuuryounichiji >= ?) "
                    + " AND   (? IS NULL OR kensasyuuryounichiji <= ?) ";

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
            String sql = "SELECT CONCAT(IFNULL(kojyo, ''), IFNULL(lotno, ''), IFNULL(edaban, '')) AS LOTNO "
                    + ", kaisuu "
                    + ", kcpno "
                    + ", tokuisaki "
                    + ", ownercode "
                    + ", lotkubuncode "
                    + ", atokouteisijinaiyou "
                    + ", okuriryouhinsuu "
                    + ", ukeiretannijyuryo "
                    + ", ukeiresoujyuryou "
                    + ", gaikankensasyurui "
                    + ", kensabasyo "
                    + ", kensasyurui "
                    + ", kensagouki "
                    + ", kensafileno "
                    + ", kensamen "
                    + ", kensakaishinichiji "
                    + ", kensakaishitantousya "
                    + ", kensakaishininteisya "
                    + ", 10Kcheckmikensa "
                    + ", 10Kcheckcamerasa "
                    + ", 1kaimesyorikosuu "
                    + ", 1kaimeryouhinkosuu "
                    + ", 1kaimeNG1suu "
                    + ", 1kaimeNG2suu "
                    + ", 1kaimebudomari "
                    + ", 1kaimemisyori "
                    + ", 2kaimesyorikosuu "
                    + ", 2kaimeryouhinkosuu "
                    + ", 2kaimeNG1suu "
                    + ", 2kaimeNG2suu "
                    + ", 2kaimebudomari "
                    + ", 2kaimemisyori "
                    + ", 3kaimesyorikosuu "
                    + ", 3kaimeryouhinkosuu "
                    + ", 3kaimeNG1suu "
                    + ", 3kaimeNG2suu "
                    + ", 3kaimebudomari "
                    + ", 3kaimemisyori "
                    + ", goukeisyorikosuu "
                    + ", ryouhinsoujyuuryou "
                    + ", goukeiryouhinkosuu "
                    + ", NGsoujyuuryou "
                    + ", goukeiNGsuu "
                    + ", goukeibudomari "
                    + ", goukeimisyori "
                    + ", mikennsaritu "
                    + ", kensasyuuryounichiji "
                    + ", kensasyuuryoutantousya "
                    + ", kensasyuuryouninteisya "
                    + ", QAgaikannukitorikensa "
                    + ", bikou1 "
                    + ", bikou2 "
                    + "  FROM sr_gaikankensa "
                    + " WHERE (? IS NULL OR kojyo = ?) "
                    + " AND   (? IS NULL OR lotno = ?) "
                    + " AND   (? IS NULL OR edaban = ?) "
                    + " AND   (? IS NULL OR kcpno LIKE ? ESCAPE '\\\\') ";
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
            sql += " AND   (? IS NULL OR kensakaishinichiji >= ?) "
                    + " AND   (? IS NULL OR kensakaishinichiji <= ?) "
                    + " AND   (? IS NULL OR kensasyuuryounichiji >= ?) "
                    + " AND   (? IS NULL OR kensasyuuryounichiji <= ?) "
                    + " ORDER BY kensakaishinichiji ";
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");//ﾛｯﾄNo.
            mapping.put("kcpno", "kcpno");//KCPNO
            mapping.put("tokuisaki", "tokuisaki");//客先
            mapping.put("lotkubuncode", "lotkubuncode");//ﾛｯﾄ区分
            mapping.put("ownercode", "ownercode");//ｵｰﾅｰ
            mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou");//後工程指示内容
            mapping.put("okuriryouhinsuu", "okuriryouhinsuu");//送り良品数
            mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo");//受入れ単位重量
            mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou");//受入れ総重量
            mapping.put("gaikankensasyurui", "gaikankensasyurui");//外観検査種類
            mapping.put("kaisuu", "kaisuu");//検査回数
            mapping.put("kensabasyo", "kensabasyo");//検査場所
            mapping.put("kensasyurui", "kensasyurui");//検査種類
            mapping.put("kensagouki", "kensagouki");//検査号機
            mapping.put("kensafileno", "kensafileno");//検査ﾌｧｲﾙNo.
            mapping.put("kensamen", "kensamen");//検査面
            mapping.put("kensakaishinichiji", "kensakaishinichiji");//検査開始日時
            mapping.put("kensakaishitantousya", "kensakaishitantousya");//検査開始担当者
            mapping.put("kensakaishininteisya", "kensakaishininteisya");//検査開始認定者
            mapping.put("10Kcheckmikensa", "kcheckmikensa10");//10kﾁｪｯｸ未検査 
            mapping.put("10Kcheckcamerasa", "kcheckcamerasa10");//10kﾁｪｯｸｶﾒﾗ差 
            mapping.put("1kaimesyorikosuu", "kaimesyorikosuu1");//1回目処理個数 
            mapping.put("1kaimeryouhinkosuu", "kaimeryouhinkosuu1");//1回目良品個数 
            mapping.put("1kaimeNG1suu", "kaimeng1suu1");//1回目NG1数 
            mapping.put("1kaimeNG2suu", "kaimeng2suu1");//1回目NG2数 
            mapping.put("1kaimebudomari", "kaimebudomari1");//1回目歩留まり 
            mapping.put("1kaimemisyori", "kaimemisyori1");//1回目未処理・ﾘﾃｽﾄ個数 
            mapping.put("2kaimesyorikosuu", "kaimesyorikosuu2");//2回目処理個数 
            mapping.put("2kaimeryouhinkosuu", "kaimeryouhinkosuu2");//2回目良品個数 
            mapping.put("2kaimeNG1suu", "kaimeng1suu2");//2回目NG1数 
            mapping.put("2kaimeNG2suu", "kaimeng2suu2");//2回目NG2数 
            mapping.put("2kaimebudomari", "kaimebudomari2");//2回目歩留まり 
            mapping.put("2kaimemisyori", "kaimemisyori2");//2回目未処理・ﾘﾃｽﾄ個数 
            mapping.put("3kaimesyorikosuu", "kaimesyorikosuu3");//3回目処理個数 
            mapping.put("3kaimeryouhinkosuu", "kaimeryouhinkosuu3");//3回目良品個数 
            mapping.put("3kaimeNG1suu", "kaimeng1suu3");//3回目NG1数 
            mapping.put("3kaimeNG2suu", "kaimeng2suu3");//3回目NG2数 
            mapping.put("3kaimebudomari", "kaimebudomari3");//3回目歩留まり 
            mapping.put("3kaimemisyori", "kaimemisyori3");//3回目未処理・ﾘﾃｽﾄ個数 
            mapping.put("goukeisyorikosuu", "goukeisyorikosuu");//合計処理個数
            mapping.put("ryouhinsoujyuuryou", "ryouhinsoujyuuryou");//良品総重量
            mapping.put("goukeiryouhinkosuu", "goukeiryouhinkosuu");//合計良品個数
            mapping.put("NGsoujyuuryou", "ngsoujyuuryou");//NG総重量 
            mapping.put("goukeiNGsuu", "goukeingsuu");//合計NG数 
            mapping.put("goukeibudomari", "goukeibudomari");//合計歩留まり
            mapping.put("goukeimisyori", "goukeimisyori");//合計未処理・ﾘﾃｽﾄ個数
            mapping.put("mikennsaritu", "mikennsaritu");//未検査率
            mapping.put("kensasyuuryounichiji", "kensasyuuryounichiji");//検査終了日時
            mapping.put("kensasyuuryoutantousya", "kensasyuuryoutantousya");//検査終了担当者
            mapping.put("kensasyuuryouninteisya", "kensasyuuryouninteisya");//検査終了認定者
            mapping.put("QAgaikannukitorikensa", "qagaikannukitorikensa");//QA外観抜き取り検査 
            mapping.put("bikou1", "bikou1");//備考1
            mapping.put("bikou2", "bikou2");//備考2

                               
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B046Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B046Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b046.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "外観検査");

            // ダウンロードファイル名
            String downloadFileName = "外観検査_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        Date paramKensaStartDateF = null;
        if (!StringUtil.isEmpty(kensaStartDateF)) {
            paramKensaStartDateF = DateUtil.convertStringToDate(getKensaStartDateF(), StringUtil.isEmpty(getKensaStartTimeF()) ? "0000" : getKensaStartTimeF());
        }
        Date paramKensaStartDateT = null;
        if (!StringUtil.isEmpty(kensaStartDateT)) {
            paramKensaStartDateT = DateUtil.convertStringToDate(getKensaStartDateT(), StringUtil.isEmpty(getKensaStartTimeT()) ? "2359" : getKensaStartTimeT());
        }
        Date paramKensaEndDateF = null;
        if (!StringUtil.isEmpty(kensaEndDateF)) {
            paramKensaEndDateF = DateUtil.convertStringToDate(getKensaEndDateF(), StringUtil.isEmpty(getKensaEndTimeF()) ? "0000" : getKensaEndTimeF());
        }
        Date paramKensaEndDateT = null;
        if (!StringUtil.isEmpty(kensaEndDateT)) {
            paramKensaEndDateT = DateUtil.convertStringToDate(getKensaEndDateT(), StringUtil.isEmpty(getKensaEndTimeT()) ? "2359" : getKensaEndTimeT());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKcpno, paramKcpno));
        boolean paramsAdd = false;
        for (String data : kensaBasyoDataList) {
            if (!StringUtil.isEmpty(data) && !"ALL".equals(data)) {
                paramsAdd = true;
            }
        }
        if (paramsAdd) {
            params.addAll(kensaBasyoDataList);
        }
        params.addAll(Arrays.asList(paramKensaStartDateF, paramKensaStartDateF));
        params.addAll(Arrays.asList(paramKensaStartDateT, paramKensaStartDateT));
        params.addAll(Arrays.asList(paramKensaEndDateF, paramKensaEndDateF));
        params.addAll(Arrays.asList(paramKensaEndDateT, paramKensaEndDateT));


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
            sql = sql + " AND key = '外観検査履歴_表示可能ﾃﾞｰﾀ'";

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
