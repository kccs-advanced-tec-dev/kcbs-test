/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo501;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.SelectParam;
import jp.co.kccs.xhd.model.GXHDO501CModel;
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
 * 変更日	2021/07/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS DengH<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 原材料規格一覧表示機能検索画面
 *
 * @author KCSS DengH
 * @since 2021/07/15
 */
@Named
@ViewScoped
public class GXHDO501C implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO501C.class.getName());

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
    private List<GXHDO501CModel> listData = null;
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    /**
     * 原材料規格ﾒﾝﾃﾅﾝｽ機能画面URL
     */
    private final String GXHDO501D_PGAE = "/secure/pxhdo501/gxhdo501d.xhtml?faces-redirect=true";

    /**
     * 警告メッセージ
     */
    private String warnMessage = "";

    /**
     * 検索条件：設計日(FROM)
     */
    private String sekkeiDateF = "";

    /**
     * 検索条件：設計日(TO)
     */
    private String sekkeiDateT = "";

    /**
     * 検索条件：設計日時刻(FROM)
     */
    private String sekkeTimeF = "";
    /**
     * 検索条件：設計日時刻(TO)
     */
    private String sekkeTimeT = "";

    /**
     * 検索条件：LotNo
     */
    private String lotNo = "";

    /**
     * 検索条件：品名
     */
    private String hinmei = "";

    /**
     * 検索条件：種類
     */
    private String cmbSyurui = "";

    /**
     * 検査場所リスト:表示可能ﾃﾞｰﾀ
     */
    private String cmbSyuruiData[];

    /**
     * メインデータの件数を保持
     */
    private String displayStyle = "";

    /**
     * 警告時処理
     */
    private String warnProcess = "";

    /** 
     * 設計日fromの背景色
     */
    private String sekkeiDateFbgcolor = null;
    
    /**
     * 設計日toの背景色
     */
    private String sekkeTimeTbgcolor = null;
    
    /**
     * lotNoの背景色 
     */
    private String lotNobgcolor = null;
    
    /** 
     * ｴﾗｰ発生項目の背景色
     */
    private static final String ERROR_COLOR = "#FFB6C1";
    
    /** 
     * ｴﾗｰがない項目の背景色
     */
    private static final String NORMAL_COLOR ="#FFFFFF";
    
    /**
     * コンストラクタ
     */
    public GXHDO501C() {
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
     * 一覧表示データ取得
     *
     * @return 一覧表示データ
     */
    public List<GXHDO501CModel> getListData() {
        return listData;
    }

    /**
     * 検索条件：設計日(FROM)
     *
     * @return the touokunDateF
     */
    public String getSekkeiDateF() {
        return sekkeiDateF;
    }

    /**
     * 検索条件：設計日(FROM)
     *
     * @param sekkeiDateF the touokunDateF to set
     */
    public void setSekkeiDateF(String sekkeiDateF) {
        this.sekkeiDateF = sekkeiDateF;
    }

    /**
     * 検索条件：設計日(TO)
     *
     * @return the touokunDateT
     */
    public String getSekkeiDateT() {
        return sekkeiDateT;
    }

    /**
     * 検索条件：設計日(TO)
     *
     * @param sekkeiDateT the touokunDateT to set
     */
    public void setSekkeiDateT(String sekkeiDateT) {
        this.sekkeiDateT = sekkeiDateT;
    }

    /**
     * 検索条件：設計日時刻(FROM)
     *
     * @return the sekkeTimeF
     */
    public String getSekkeTimeF() {
        return sekkeTimeF;
    }

    /**
     *
     * 検索条件：設計日時刻(FROM)
     *
     * @param sekkeTimeF the sekkeTimeF to set
     */
    public void setSekkeTimeF(String sekkeTimeF) {
        this.sekkeTimeF = sekkeTimeF;
    }

    /**
     * 検索条件：設計日時刻(TO)
     *
     * @return the sekkeTimeT
     */
    public String getSekkeTimeT() {
        return sekkeTimeT;
    }

    /**
     * 検索条件：設計日時刻(TO)
     *
     * @param sekkeTimeT the sekkeTimeT to set
     */
    public void setSekkeTimeT(String sekkeTimeT) {
        this.sekkeTimeT = sekkeTimeT;
    }

    /**
     * 検索条件：LotNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * 検索条件：LotNo
     *
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 検索条件：品名
     *
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 検索条件：品名
     *
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * 検索条件：種類
     *
     * @return the cmbSyurui
     */
    public String getCmbSyurui() {
        return cmbSyurui;
    }

    /**
     * 検索条件：種類
     *
     * @param cmbSyurui the cmbSyurui to set
     */
    public void setCmbSyurui(String cmbSyurui) {
        this.cmbSyurui = cmbSyurui;
    }

    /**
     * @return the cmbSyuruiData
     */
    public String[] getCmbSyuruiData() {
        return cmbSyuruiData;
    }

    /**
     * @param cmbSyuruiData the cmbSyuruiData to set
     */
    public void setCmbSyuruiData(String[] cmbSyuruiData) {
        this.cmbSyuruiData = cmbSyuruiData;
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
     * 設計日fromの背景色
     *
     * @return the sekkeiDateFbgcolor
     */
    public String getSekkeiDateFbgcolor() {
        return sekkeiDateFbgcolor;
    }
    
    /**
     * 設計日fromの背景色
     *
     * @param sekkeiDateFbgcolor the sekkeiDateFbgcolor to set
     */
    public void setSekkeiDateFbgcolor(String sekkeiDateFbgcolor) {
        this.sekkeiDateFbgcolor = sekkeiDateFbgcolor;
    }

    /**
     * 設計日toの背景色
     *
     * @return the sekkeTimeTbgcolor
     */
    public String getSekkeTimeTbgcolor() {
        return sekkeTimeTbgcolor;
    }
    
    /**
     * 設計日toの背景色
     *
     * @param sekkeTimeTbgcolor the sekkeTimeTbgcolor to set
     */
    public void setSekkeTimeTbgcolor(String sekkeTimeTbgcolor) {
        this.sekkeTimeTbgcolor = sekkeTimeTbgcolor;
    }

    /**
     * 担当者の背景色
     *
     * @return the lotNobgcolor
     */
    public String getLotNobgcolor() {
        return lotNobgcolor;
    }
    
    /**
     * 担当者の背景色
     *
     * @param lotNobgcolor the lotNobgcolor to set
     */
    public void setLotNobgcolor(String lotNobgcolor) {
        this.lotNobgcolor = lotNobgcolor;
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

        // 種類コンボボックス設定
        setCmbSyuruiData(new String[]{"ｶﾞﾗｽ作製", "ｶﾞﾗｽｽﾗﾘｰ作製", "添加剤ｽﾗﾘｰ作製", "誘電体ｽﾗﾘｰ作製", "ﾊﾞｲﾝﾀﾞｰ溶液作製", "ｽﾘｯﾌﾟ作製"});
        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;
        // 画面クリア
        doClear();
    }

    /**
     * クリア処理確認
     */
    public void confirmClear() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("param1", "warning");
        this.warnMessage = "ｸﾘｱします。よろしいですか？";
        this.warnProcess = "clear";
    }

    /**
     * 画面クリア
     */
    public void doClear() {

        sekkeiDateF = "";
        sekkeiDateT = "";
        lotNo = "";
        hinmei = "";
        cmbSyurui = "";
        sekkeiDateFbgcolor = NORMAL_COLOR;
        sekkeTimeTbgcolor = NORMAL_COLOR;
        lotNobgcolor = NORMAL_COLOR;  
        listData = new ArrayList<>();
    }

    /**
     * 警告OK選択時処理
     */
    public void processWarnOk() {
        switch (this.warnProcess) {
            case "clear":
                doClear();
                break;
            case "OK":
                selectListData();
                break;
        }

    }

    /**
     * 入力値チェック： 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        
        sekkeiDateFbgcolor = NORMAL_COLOR;
        sekkeTimeTbgcolor = NORMAL_COLOR;
        lotNobgcolor = NORMAL_COLOR;  
        
        // LotNo
        if (!StringUtil.isEmpty(getLotNo()) && StringUtil.getLength(getLotNo()) != 15) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000004", "LotNo", "15"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            lotNobgcolor = ERROR_COLOR;
            return;
        }
        // 設計日(FROM)
        if (existError(validateUtil.checkC101(getSekkeiDateF(), "設計日(from)", 6))
                || existError(validateUtil.checkC201ForDate(getSekkeiDateF(), "設計日(from)"))
                || existError(validateUtil.checkC501(getSekkeiDateF(), "設計日(from)"))) {
            sekkeiDateFbgcolor = ERROR_COLOR;
            return;
        }
        // 設計日(TO)
        if (existError(validateUtil.checkC101(getSekkeiDateT(), "設計日(to)", 6))
                || existError(validateUtil.checkC201ForDate(getSekkeiDateT(), "設計日(to)"))
                || existError(validateUtil.checkC501(getSekkeiDateT(), "設計日(to)"))) {
            sekkeTimeTbgcolor = ERROR_COLOR;
            return;
        }

        // 一覧表示件数を取得
        long count = selectListDataCount();

        if (count == 0) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000212", "設計ﾃﾞｰﾀ"), null);
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
            
            this.warnMessage = String.format("検索結果が%d件を超えています。<br/>継続しますか?<br/>%d件", listCountWarn, count);
            this.warnProcess = "OK";
            return;
        }
        
        // 入力チェックでエラーが存在しない場合検索処理を実行する
        selectListData();
    }

    /**
     * 一覧表示データ件数取得
     *
     * @return 検索結果件数
     */
    public Long selectListDataCount() {

        long count;

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(sekkeino) AS COUNT "
                    + "FROM da_mksekkei "
                    + " WHERE  (? IS NULL OR  TOUROKUNICHIJI >= ?) "
                    + " AND (? IS NULL OR  TOUROKUNICHIJI <= ?) "
                    + " AND (? IS NULL OR KOJYO = ?) "
                    + " AND (? IS NULL OR LOTNO = ?) "
                    + " AND (? IS NULL OR EDABAN = ?) "
                    + " AND (? IS NULL OR SYURUI = ?) "
                    + " AND (? IS NULL OR HINMEI = ?) ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("COUNT");

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
            String sql = "SELECT CONCAT(IFNULL(KOJYO, ''), IFNULL(LOTNO, ''), IFNULL(EDABAN, '')) AS LOTNO"
                    + ", syurui "
                    + ", hinmei "
                    + ", tourokunichiji "
                    + ", tantousya "
                    + ", sekkeino "
                    + ", pattern "
                    + "FROM da_mksekkei"
                    + " WHERE  (? IS NULL OR  TOUROKUNICHIJI >= ?) "
                    + " AND (? IS NULL OR  TOUROKUNICHIJI <= ?) "
                    + " AND (? IS NULL OR KOJYO = ?) "
                    + " AND (? IS NULL OR LOTNO = ?) "
                    + " AND (? IS NULL OR EDABAN = ?) "
                    + " AND (? IS NULL OR SYURUI = ?) "
                    + " AND (? IS NULL OR HINMEI = ?) ";
            sql += " ORDER BY TOUROKUNICHIJI DESC"
                    + ",HINMEI";

            // パラメータ設定
            List<Object> params = createSearchParam();

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("lotno", "lotno");//LotN          
            mapping.put("syurui", "syurui");//種類
            mapping.put("hinmei", "hinmei");//品名
            mapping.put("tourokunichiji", "sekkeidate");//設計日
            mapping.put("tantousya", "tantousya");//担当者
            mapping.put("sekkeino", "sekkeino");//設計No
            mapping.put("pattern", "pattern");//ﾊﾟﾀｰﾝ

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO501CModel>> beanHandler
                    = new BeanListHandler<>(GXHDO501CModel.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

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
     * 検索パラメータ生成
     *
     * @return パラメータ
     */
    private List<Object> createSearchParam() {

        // パラメータ設定
        Date paramSekkeiDateF = null;
        if (!StringUtil.isEmpty(sekkeiDateF)) {
            paramSekkeiDateF = DateUtil.convertStringToDate(getSekkeiDateF(), StringUtil.isEmpty(getSekkeTimeF()) ? "0000" : getSekkeTimeF());

        }
        Date paramSekkeiDateT = null;
        if (!StringUtil.isEmpty(sekkeiDateT)) {
            paramSekkeiDateT = DateUtil.convertStringToDate(getSekkeiDateT(), StringUtil.isEmpty(getSekkeTimeT()) ? "2359" : getSekkeTimeT());
        }

        String paramKojo = null;
        String paramLotNo = null;
        String paramEdaban = null;
        if (!StringUtil.isEmpty(lotNo)) {
            paramKojo = StringUtils.substring(getLotNo(), 0, 3);
            paramLotNo = StringUtils.substring(getLotNo(), 3, 12);
            paramEdaban = StringUtil.blankToNull(StringUtils.substring(getLotNo(), 12, 15));
        }

        String paramHinmei = null;
        if (!StringUtil.isEmpty(hinmei)) {
            paramHinmei = StringUtil.blankToNull(getHinmei());
        }

        String paramSyurui = null;
        if (!StringUtil.isEmpty(cmbSyurui)) {
            paramSyurui = StringUtil.blankToNull(getCmbSyurui());
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramSekkeiDateF, paramSekkeiDateF));
        params.addAll(Arrays.asList(paramSekkeiDateT, paramSekkeiDateT));
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramSyurui, paramSyurui));
        params.addAll(Arrays.asList(paramHinmei, paramHinmei));

        return params;
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
     * 原材料規格ﾒﾝﾃﾅﾝｽ機能画面open
     *
     * @param rowData 遷移データ
     * @return 遷移先画面
     */
    public String openXhdForm(GXHDO501CModel rowData) {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        //設計No
        session.setAttribute("sekkeino", rowData.getSekkeino());
        //LotNo
        session.setAttribute("lotNo", rowData.getLotno());
        //種類
        session.setAttribute("syurui", rowData.getSyurui());
        //品名
        session.setAttribute("hinmei", rowData.getHinmei());
        //ﾊﾟﾀｰﾝ
        session.setAttribute("pattern", rowData.getPattern());

        return GXHDO501D_PGAE;

    }

}
