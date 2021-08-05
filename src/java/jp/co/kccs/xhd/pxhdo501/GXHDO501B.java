/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo501;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.common.ColumnInfoParser;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.common.excel.ExcelExporter;
import jp.co.kccs.xhd.model.GXHDO501BModel;
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
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS GC<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 原材料規格取込結果確認機能画面
 *
 * @author KCSS GC
 * @since  2021/07/14
 */
@Named
@ViewScoped
public class GXHDO501B implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(GXHDO501B.class.getName());
    
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
        
    /** 一覧表示データ */
    private List<GXHDO501BModel> listData = null;
    /** 一覧表示最大件数 */
    private int listCountMax = -1;
    /** 一覧表示警告件数 */
    private int listCountWarn = -1;
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 検索条件：規格 */
    private String rKikaku="";
    /** 検索条件：取込日(From) */
    private String torikomiDateF="";
    /** 検索条件：取込日(To) */
    private String torikomiDateT="";
    /** 検索条件：開始時刻(FROM) */
    private String tourokunStartTimeF = "";
    /** 検索条件：開始時刻(TO) */
    private String tourokunStartTimeT = "";
    /** 検索条件：種類 */
    private String cmbSyurui="";
    /** 検索条件：担当者 */
    private String txtTantousya="";
    /** 検査場所リスト:表示可能ﾃﾞｰﾀ */
    private String cmbSyuruiData[];
    /** * ｴﾗｰ発生項目の背景色 */
    private static final String ERROR_COLOR = "#FFB6C1";
    /** * ｴﾗｰがない項目の背景色 */
    private static final String NORMAL_COLOR ="#FFFFFF";
    /** 取込日fromの背景色  */
    private String torikomiDateFbgcolor = null;
    /** 取込日toの背景色  */
    private String torikomiDateTbgcolor = null;
    /** 担当者の背景色  */
    private String txtTantousyabgcolor = null;
    /** 警告時処理 */
    private String warnProcess = "";

    /**
     * コンストラクタ
     */
    public GXHDO501B() {
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
    public List<GXHDO501BModel> getListData() {
        return listData;
    }

    /**
     * 規格
     * @return the rKikaku
     */
    public String getrKikaku() {
        return rKikaku;
    }
    /**
     * 規格
     * @param rKikaku the rKikaku to set
     */
    public void setrKikaku(String rKikaku) {
        this.rKikaku = rKikaku;
    }
    
     /**
     * 取込日(From)
     * @return the torikomiDateF
     */
    public String getTorikomiDateF() {
        return torikomiDateF;
    }
    /**
     * 取込日(From)
     * @param torikomiDateF the torikomiDateF to set
     */
    public void setTorikomiDateF(String torikomiDateF) {
        this.torikomiDateF = torikomiDateF;
    }
    
     /**
     * 取込日(To)
     * @return the torikomiDateT
     */
    public String getTorikomiDateT() {
        return torikomiDateT;
    }
    /**
     * 取込日(To)
     * @param torikomiDateT the torikomiDateT to set
     */
    public void setTorikomiDateT(String torikomiDateT) {
        this.torikomiDateT = torikomiDateT;
    }
    
     /**
     * 種類
     * @return the cmbSyurui
     */
    public String getCmbSyurui() {
        return cmbSyurui;
    }
    /**
     * 種類
     * @param cmbSyurui the cmbSyurui to set
     */
    public void setCmbSyurui(String cmbSyurui) {
        this.cmbSyurui = cmbSyurui;
    }
    
     /**
     * 担当者
     * @return the txtTantousya
     */
    public String getTxtTantousya() {
        return txtTantousya;
    }
    /**
     * 担当者
     * @param txtTantousya the txtTantousya to set
     */
    public void setTxtTantousya(String txtTantousya) {
        this.txtTantousya = txtTantousya;
    }
    
    /**
     * 種類リスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbSyuruiData
     */
    public String[] getCmbSyuruiData() {
        return cmbSyuruiData;
    }
    /**
     * 種類リスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbSyuruiData the cmbSyuruiData to set
     */
    public void setCmbSyuruiData(String[] cmbSyuruiData) {
        this.cmbSyuruiData = cmbSyuruiData;
    }

    /**
     * 検索条件：取込日開始時刻(from)
     * @return the tourokunStartTimeF
     */
    public String getTourokunStartTimeF() {
        return tourokunStartTimeF;
    }
    /**
     * 検索条件：取込日開始時刻(from)
     * @param tourokunStartTimeF the tourokunStartTimeF to set
     */
    public void setTourokunStartTimeF(String tourokunStartTimeF) {
        this.tourokunStartTimeF = tourokunStartTimeF;
    }
    /**
     * 検索条件：取込日終了時刻(from)
     * @return the tourokunStartTimeT
     */
    public String getTourokunStartTimeT() {
        return tourokunStartTimeT;
    }
    /**
     * 検索条件：取込日終了時刻(from)
     * @param tourokunStartTimeT the tourokunStartTimeT to set
     */
    public void setTourokunStartTimeT(String tourokunStartTimeT) {
        this.tourokunStartTimeT = tourokunStartTimeT;
    }
     /**
     * 取込日fromの背景色
     * @return the torikomiDateFbgcolor
     */
    public String getTorikomiDateFbgcolor() {
        return torikomiDateFbgcolor;
    }
    /**
     * 取込日fromの背景色
     * @param torikomiDateFbgcolor the torikomiDateFbgcolor to set
     */
    public void setTorikomiDateFbgcolor(String torikomiDateFbgcolor) {
        this.torikomiDateFbgcolor = torikomiDateFbgcolor;
    }
    /**
     * 取込日toの背景色
     * @return the torikomiDateTbgcolor
     */
    public String getTorikomiDateTbgcolor() {
        return torikomiDateTbgcolor;
    }
    /**
     * 取込日toの背景色
     * @param torikomiDateTbgcolor the torikomiDateTbgcolor to set
     */
    public void setTorikomiDateTbgcolor(String torikomiDateTbgcolor) {
        this.torikomiDateTbgcolor = torikomiDateTbgcolor;
    }
    /**
     * 担当者の背景色
     * @return the txtTantousyabgcolor
     */
    public String getTxtTantousyabgcolor() {
        return txtTantousyabgcolor;
    }
    /**
     * 担当者の背景色
     * @param txtTantousyabgcolor the txtTantousyabgcolor to set
     */
    public void setTxtTantousyabgcolor(String txtTantousyabgcolor) {
        this.txtTantousyabgcolor = txtTantousyabgcolor;
    }
    
    
    //</editor-fold>
    
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
        
        // 規格ラジオボタン設定
        rKikaku="標準規格";
        // 種類コンボボックス設定
        cmbSyuruiData = new String[]{"ｶﾞﾗｽ作製","ｶﾞﾗｽｽﾗﾘｰ作製","添加剤ｽﾗﾘｰ作製","誘電体ｽﾗﾘｰ作製","ﾊﾞｲﾝﾀﾞｰ溶液作製","ｽﾘｯﾌﾟ作製"};
     
        listCountMax = session.getAttribute("menuParam") != null ? Integer.parseInt(session.getAttribute("menuParam").toString()) : -1;
        listCountWarn = session.getAttribute("hyojiKensu") != null ? Integer.parseInt(session.getAttribute("hyojiKensu").toString()) : -1;

        // 画面クリア
        clear();
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
     * 警告OK選択時処理
     */
    public void processWarnOk() {
        switch (this.warnProcess) {
            case "clear":
                clear();
                break;
            case "OK":
                selectListData();
                break;
        }
    }
    
     /**
     * 画面クリア
     */
    public void clear() {
        
        rKikaku="標準規格";
        torikomiDateF="";
        torikomiDateT="";
        cmbSyurui="";
        txtTantousya="";  
        torikomiDateFbgcolor = NORMAL_COLOR;
        torikomiDateTbgcolor = NORMAL_COLOR;
        txtTantousyabgcolor = NORMAL_COLOR;  
        listData = new ArrayList<>();
    }
    
     /**
     * 入力値チェック：
     * 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        
        torikomiDateFbgcolor = NORMAL_COLOR;
        torikomiDateTbgcolor = NORMAL_COLOR;
        txtTantousyabgcolor = NORMAL_COLOR;  
        
        // 取込日(FROM)
        if (existError(validateUtil.checkC101(getTorikomiDateF(), "取込日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getTorikomiDateF(), "取込日(from)")) ||
            existError(validateUtil.checkC501(getTorikomiDateF(), "取込日(from)"))) {
            torikomiDateFbgcolor=ERROR_COLOR;
            return;
        }
        // 取込日(TO)
        if (existError(validateUtil.checkC101(getTorikomiDateT(), "取込日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getTorikomiDateT(), "取込日(to)")) ||
            existError(validateUtil.checkC501(getTorikomiDateT(), "取込日(to)"))) {
            torikomiDateTbgcolor = ERROR_COLOR;
            return;
        }
        
        // 担当者
        if (existError(validateUtil.checkC103(getTxtTantousya(), "担当者", 6))) {
            txtTantousyabgcolor = ERROR_COLOR;  
            return;
        }
        
        // 担当者存在するかの判断
        if(!StringUtil.isEmpty(getTxtTantousya())){
            boolean existTantousya = false;
            try {
                QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
                if(validateUtil.existTantomas(getTxtTantousya(), queryRunnerWip)){
                    existTantousya = true;
                }
            } catch (SQLException ex){
                ErrUtil.outputErrorLog("担当者マスタ存在チェックに失敗", ex, LOGGER);
                existTantousya = false;
            }
            if (!existTantousya) {
                // 入力された担当者が[tantomas]に存在しない場合ｴﾗｰ。
                FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "担当者"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                txtTantousyabgcolor = ERROR_COLOR;  
                return;  
            }
        }
        

        // 一覧表示件数を取得
        long count = selectListDataCount();
        
        if (count == 0) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000212","規格取込履歴"), null);
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
     * 一覧表示データ件数取得
     * @return 検索結果件数
     */
    public long selectListDataCount() {
         long count = 0;
          try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            
            // パラメータ設定
            List<Object> params = createSearchParam();
            List<Object> tempParams = new ArrayList<>();
            String sql = "SELECT COUNT(torikomino) AS CNT "
                    + " FROM fxhdd10 WHERE 1=1" ;
                     if(params.get(0)!=null){
                        sql+= " AND (kikaku = ? ) ";
                        tempParams.add(params.get(0));
                    }
                    if(params.get(1)!=null){
                        sql+="AND (syurui = ?)";
                        tempParams.add(params.get(1));
                    }
                    if(params.get(2)!=null){
                        sql+="AND (tantousya = ? )";        
                        tempParams.add(params.get(2));
                    }
                    if(params.get(3)!=null){
                        sql+="AND (tourokunichiji >= CAST( ? AS TIMESTAMP))";
                        tempParams.add(params.get(3));
                    }
                    if(params.get(4)!=null){
                        sql+="AND (tourokunichiji <= CAST( ? AS TIMESTAMP))";
                        tempParams.add(params.get(4));
                    }
            DBUtil.outputSQLLog(sql, tempParams.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), tempParams.toArray());
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
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);

            // パラメータ設定
            List<Object> params = createSearchParam();
            List<Object> tempParams = new ArrayList<>();
            
            String sql = "SELECT torikomino"
                    + ", tourokunichiji "
                    + ", kikaku "
                    + ", syurui "
                    + ", tantousya "
                    + ", ngsuu "
                    + " FROM fxhdd10 WHERE 1=1";
                    if(params.get(0)!=null){
                        sql+= " AND (kikaku = ? ) ";
                        tempParams.add(params.get(0));
                    }
                    if(params.get(1)!=null){
                        sql+="AND (syurui = ?)";
                        tempParams.add(params.get(1));
                    }
                    if(params.get(2)!=null){
                        sql+="AND (tantousya = ? )";        
                        tempParams.add(params.get(2));
                    }
                    if(params.get(3)!=null){
                        sql+="AND (tourokunichiji >= CAST( ? AS TIMESTAMP))";
                        tempParams.add(params.get(3));
                    }
                    if(params.get(4)!=null){
                        sql+="AND (tourokunichiji <= CAST( ? AS TIMESTAMP))";
                        tempParams.add(params.get(4));
                    }
                    sql+= "ORDER BY tourokunichiji DESC,kikaku,syurui,tantousya ASC";   
                    
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("torikomino", "torikomino");// 取込No
            mapping.put("tourokunichiji", "tourokunichiji");// 登録日
            mapping.put("kikaku", "kikaku");// 規格
            mapping.put("syurui", "syurui");// 種類
            mapping.put("tantousya", "tantousya");// 担当者
            mapping.put("ngsuu", "ngsuu");// NG数
            
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO501BModel>> beanHandler = 
                    new BeanListHandler<>(GXHDO501BModel.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, tempParams.toArray(), LOGGER);
       
            listData = queryRunner.query(sql, beanHandler, tempParams.toArray());
            
            for(int i=0;i<listData.size();i++){
                listData.get(i).setTourokunichiji(stringToDate(listData.get(i).getTourokunichiji()));
            }

        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }
    
     /**
     * 日付文字列⇒String Date変換<br>
     *
     * @param dateValue 年月日時分
     * @return 変換後のデータ
     */
    public static String stringToDate(String dateValue) {
        
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(dateValue);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        String outputText = outputFormat.format(date);
          
        return outputText;
    }
   /**
     * 検索パラメータ生成
     * @return パラメータ
     */
    private List<Object> createSearchParam() {
        // パラメータ設定
        String paramrKikaku = null;
        if (!StringUtil.isEmpty(getrKikaku())) {
            paramrKikaku = StringUtil.blankToNull(getrKikaku());
        }
        
        String paramcmbSyurui = null;
        if (!StringUtil.isEmpty(getCmbSyurui())) {
            paramcmbSyurui = StringUtil.blankToNull(getCmbSyurui());
        }
        
        String paramTxtTantousya = null;
        if (!StringUtil.isEmpty(getTxtTantousya())) {
            paramTxtTantousya = StringUtil.blankToNull(getTxtTantousya());
        }
        
        Date paramTorikomiDateF = null;
        if (!StringUtil.isEmpty(torikomiDateF)) {
            paramTorikomiDateF = DateUtil.convertStringToDate(getTorikomiDateF(), StringUtil.isEmpty(getTourokunStartTimeF()) ? "0000" : getTourokunStartTimeF());  
        }
        
        Date paramTorikomiDateT = null;
        if (!StringUtil.isEmpty(torikomiDateT)) {
            paramTorikomiDateT = DateUtil.convertStringToDate(getTorikomiDateT(), StringUtil.isEmpty(getTourokunStartTimeT()) ? "2359" : getTourokunStartTimeT());
            
        }

        List<Object> params = new ArrayList<>();
        
        params.add(paramrKikaku);
        params.add(paramcmbSyurui);
        params.add(paramTxtTantousya);

        if (!StringUtil.isEmpty(torikomiDateF)) {
             params.add("'"+paramTorikomiDateF+"'");
        }else{
             params.add(paramTorikomiDateF);
        }
       
        if (!StringUtil.isEmpty(torikomiDateT)) {
            params.add("'"+paramTorikomiDateT+"'");
        }else{
            params.add(paramTorikomiDateT);
        }
        return params;
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
     * Excelダウンロード
     * @param torikomino
     * @throws Throwable 
     */
    public void downloadExcel(int torikomino) throws Throwable {

        File excel = null;
        try {
             //ﾃﾞｰﾀの取得

            List<GXHDO501BModel> listExcelData = loadExcelData(torikomino);
            // ﾃﾞｰﾀ有無
            if (listExcelData.isEmpty()) {
                FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000215"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            
            ResourceBundle myParam = fc.getApplication().getResourceBundle(fc, "myParam");

            // Excel出力定義を取得
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo501b.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);
            
            for(int i=0;i<listExcelData.size();i++){
                listExcelData.get(i).setTourokunichiji(stringToDate(listExcelData.get(i).getTourokunichiji()));
            }
            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listExcelData, list, myParam.getString("download_temp"), "原材料規格取込結果確認機能");

            // ダウンロードファイル名
            String downloadFileName = "原材料規格取込結果確認機能_" +torikomino+"_"+ ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
     * 一覧から、ﾃﾞｰﾀを取得
     * @param torikomino 取込No
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<GXHDO501BModel> loadExcelData(int torikomino) {
        QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
        try {
             String sql = "SELECT torikomino"
                    + ", tourokunichiji "
                    + ", kikaku "
                    + ", syurui "
                    + ", tantousya "
                    + ", ngsuu "
                    + "  FROM fxhdd10 "
                    + " WHERE (torikomino = ? )";
             
            List<Object> param = new ArrayList<>();
            param.add(torikomino);
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("torikomino", "torikomino");// 取込No
            mapping.put("tourokunichiji", "tourokunichiji");// 登録日
            mapping.put("kikaku", "kikaku");// 規格
            mapping.put("syurui", "syurui");// 種類
            mapping.put("tantousya", "tantousya");// 担当者
            mapping.put("ngsuu", "ngsuu");// NG数

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO501BModel>> beanHandler = 
                    new BeanListHandler<>(GXHDO501BModel.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, param.toArray(), LOGGER);
       
            return queryRunner.query(sql, beanHandler, param.toArray());
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;       
    }
}
