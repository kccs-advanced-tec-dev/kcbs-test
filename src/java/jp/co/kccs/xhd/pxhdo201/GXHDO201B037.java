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
import jp.co.kccs.xhd.model.GXHDO201B037Model;
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
 * 変更日	2019/11/12<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・計数履歴検索画面
 *
 * @author KCSS K.Jo
 * @since  2019/11/12
 */
@Named
@ViewScoped
public class GXHDO201B037 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B037.class.getName());
    
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
    private List<GXHDO201B037Model> listData = null;
    
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
    /** 検索条件：計数担当者 */
    private String keitantosya = "";
    /** 検索条件：計数日(FROM) */
    private String keisuuDateF = "";
    /** 検索条件：計数日(TO) */
    private String keisuuDateT = "";
    /** 検索条件：計数時刻(FROM) */
    private String keisuuTimeF = "";
    /** 検索条件：計数時刻(TO) */
    private String keisuuTimeT = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B037() {
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
    public List<GXHDO201B037Model> getListData() {
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
     * 検索条件：計数担当者
     * @return the keitantosya
     */
    public String getKeitantosya() {
        return keitantosya;
    }

    /**
     * 検索条件：計数担当者
     * @param keitantosya the keitantosya to set
     */
    public void setKeitantosya(String keitantosya) {
        this.keitantosya = keitantosya;
    }

    /**
     * 検索条件：計数日(FROM)
     * @return the keisuuDateF
     */
    public String getKeisuuDateF() {
        return keisuuDateF;
    }

    /**
     * 検索条件：計数日(FROM)
     * @param keisuuDateF the keisuuDateF to set
     */
    public void setKeisuuDateF(String keisuuDateF) {
        this.keisuuDateF = keisuuDateF;
    }

    /**
     * 検索条件：計数日(TO)
     * @return the keisuuDateT
     */
    public String getKeisuuDateT() {
        return keisuuDateT;
    }

    /**
     * 検索条件：計数日(TO)
     * @param keisuuDateT the keisuuDateT to set
     */
    public void setKeisuuDateT(String keisuuDateT) {
        this.keisuuDateT = keisuuDateT;
    }

    /**
     * 検索条件：計数時刻(FROM)
     * @return the keisuuTimeF
     */
    public String getKeisuuTimeF() {
        return keisuuTimeF;
    }

    /**
     * 検索条件：計数時刻(FROM)
     * @param keisuuTimeF the keisuuTimeF to set
     */
    public void setKeisuuTimeF(String keisuuTimeF) {
        this.keisuuTimeF = keisuuTimeF;
    }

    /**
     * 検索条件：計数時刻(TO)
     * @return the keisuuTimeT
     */
    public String getKeisuuTimeT() {
        return keisuuTimeT;
    }

    /**
     * 検索条件：計数時刻(TO)
     * @param keisuuTimeT the keisuuTimeT to set
     */
    public void setKeisuuTimeT(String keisuuTimeT) {
        this.keisuuTimeT = keisuuTimeT;
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

        if (!StringUtil.isEmpty(selectParam.getValue("GXHDO201B037_display_page_count", session))) {
            listDisplayPageCount = Integer.parseInt(selectParam.getValue("GXHDO201B037_display_page_count", session));
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
        keitantosya = "";
        keisuuDateF = "";
        keisuuDateT = "";
        keisuuTimeF = "";
        keisuuTimeT = "";
        
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
        if(!StringUtil.isEmpty(getLotNo()) && (StringUtil.getLength(getLotNo()) != 11 && StringUtil.getLength(getLotNo()) != 14)){
         FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000064"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        if (!StringUtil.isEmpty(getLotNo()) && existError(validateUtil.checkValueE001(getLotNo()))) {
            return;
        }
        // 計数日(FROM)
        if (existError(validateUtil.checkC101(getKeisuuDateF(), "計数日(from)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKeisuuDateF(), "計数日(from)")) ||
            existError(validateUtil.checkC501(getKeisuuDateF(), "計数日(from)"))) {
            return;
        }
        // 計数日(TO)
        if (existError(validateUtil.checkC101(getKeisuuDateT(), "計数日(to)", 6)) ||
            existError(validateUtil.checkC201ForDate(getKeisuuDateT(), "計数日(to)")) ||
            existError(validateUtil.checkC501(getKeisuuDateT(), "計数日(to)"))) {
            return;
        }
        // 計数時刻(FROM)
        if (existError(validateUtil.checkC101(getKeisuuTimeF(), "計数時刻(from)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKeisuuTimeF(), "計数時刻(from)")) ||
            existError(validateUtil.checkC502(getKeisuuTimeF(), "計数時刻(from)"))) {
            return;
        }
        if (!StringUtil.isEmpty(keisuuTimeF) && existError(validateUtil.checkC001(getKeisuuDateF(), "計数日(from)"))) {
            return;
        }
        // 計数時刻(TO)
        if (existError(validateUtil.checkC101(getKeisuuTimeT(), "計数時刻(to)", 4)) ||
            existError(validateUtil.checkC201ForDate(getKeisuuTimeT(), "計数時刻(to)")) ||
            existError(validateUtil.checkC502(getKeisuuTimeT(), "計数時刻(to)"))) {
            return;
        }
        if (!StringUtil.isEmpty(keisuuTimeT) && existError(validateUtil.checkC001(getKeisuuDateT(), "計数日(to)"))) {
            return;
        }
        // KCPNO
        if (existError(validateUtil.checkC103(getKcpNo(), "KCPNO", 25))) {
            return;
        }
        // 計数担当者
        if (existError(validateUtil.checkC101(getKeitantosya(), "計数担当者", 6))) {
            return;
        }
        if(!StringUtil.isEmpty(getKeitantosya())){
            boolean existGoki = false;
            try {
                QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
                if(validateUtil.existTantomas(getKeitantosya(), queryRunnerWip)){
                    existGoki = true;
                }
            } catch (SQLException ex){
                ErrUtil.outputErrorLog("担当者マスタ存在チェックに失敗", ex, LOGGER);
                existGoki = false;
            }
            if (!existGoki) {
              // 入力された担当者が[tantomas]に存在しない場合ｴﾗｰ。
                FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000011", "計数担当者"), null);
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
                    + "FROM sr_gdkeisuu "
                    + "WHERE (? IS NULL OR KOJYO = ?) "
                    + "AND   (? IS NULL OR LOTNO = ?) "
                    + "AND   (? IS NULL OR EDABAN = ?) "
                    + "AND   (? IS NULL OR KEINICHIJI >= ?) "
                    + "AND   (? IS NULL OR KEINICHIJI <= ?) "
                    + "AND   (? IS NULL OR KEITANTOSYA = ?)"
                    + "AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') ";

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
                    + ", kaisuu "
                    + ", kcpno "
                    + ", tokuisaki "
                    + ", lotkubuncode "
                    + ", ownercode "
                    + ", lotpre "
                    + ", syorisuu "
                    + ", tanijyuryo "
                    + ", soujuryou "
                    + ", ryohinkosuu "
                    + ", keinichiji "
                    + ", keitantosya "
                    + ", budomari "
                    + ", biko1 "
                    + ", biko2 "
                    + "  FROM sr_gdkeisuu "
                    + " WHERE (? IS NULL OR KOJYO = ?) "
                    + " AND   (? IS NULL OR LOTNO = ?) "
                    + " AND   (? IS NULL OR EDABAN = ?) "
                    + " AND   (? IS NULL OR KEINICHIJI >= ?) "
                    + " AND   (? IS NULL OR KEINICHIJI <= ?) "
                    + " AND   (? IS NULL OR KEITANTOSYA = ?)"
                    + " AND   (? IS NULL OR KCPNO LIKE ? ESCAPE '\\\\') "
                    + " ORDER BY KEINICHIJI ";

            // パラメータ設定
            List<Object> params = createSearchParam();

            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("LOTNO", "lotno");                  // ﾛｯﾄNo
            mapping.put("kaisuu", "kaisuu");                // 回数
            mapping.put("kcpno", "kcpno");                  // KCPNO
            mapping.put("tokuisaki", "tokuisaki");          // 客先
            mapping.put("lotkubuncode", "lotkubuncode");    // ﾛｯﾄ区分
            mapping.put("ownercode", "ownercode");          // ｵｰﾅｰ
            mapping.put("lotpre", "lotpre");                // ﾛｯﾄﾌﾟﾚ
            mapping.put("syorisuu", "syorisuu");            // 処理数
            mapping.put("tanijyuryo", "tanijyuryo");        // 単位重量
            mapping.put("soujuryou", "soujuryou");          // 総重量
            mapping.put("ryohinkosuu", "ryohinkosuu");      // 送り良品数
            mapping.put("keinichiji", "keinichiji");        // 計数日時
            mapping.put("keitantosya", "keitantosya");      // 担当者
            mapping.put("budomari", "budomari");            // 歩留まり
            mapping.put("biko1", "biko1");                  // 備考1
            mapping.put("biko2", "biko2");                  // 備考2

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO201B037Model>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B037Model.class, rowProcessor);
            
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
            File file = new File(servletContext.getRealPath("/WEB-INF/classes/resources/json/gxhdo201b037.json")); 
            List<ColumnInformation> list = (new ColumnInfoParser()).parseColumnJson(file);

            // 物理ファイルを生成
            excel = ExcelExporter.outputExcel(listData, list, myParam.getString("download_temp"), "外部電極・計数");

            // ダウンロードファイル名
            String downloadFileName = "外部電極・計数_" + ((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())) + ".xlsx";
            
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
        Date paramKeisuuDateF = null;
        if (!StringUtil.isEmpty(keisuuDateF)) {
            paramKeisuuDateF = DateUtil.convertStringToDate(getKeisuuDateF(), StringUtil.isEmpty(getKeisuuTimeF()) ? "0000" : getKeisuuTimeF());
        }
        Date paramKeisuuDateT = null;
        if (!StringUtil.isEmpty(keisuuDateT)) {
            paramKeisuuDateT = DateUtil.convertStringToDate(getKeisuuDateT(), StringUtil.isEmpty(getKeisuuTimeT()) ? "2359" : getKeisuuTimeT());
        }
        String paramKeisuutantosya = StringUtil.blankToNull(getKeitantosya());
        String paramKcpno = null;
        if (!StringUtil.isEmpty(kcpNo)) {
            paramKcpno = "%" + DBUtil.escapeString(getKcpNo()) + "%";
        }

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(paramKojo, paramKojo));
        params.addAll(Arrays.asList(paramLotNo, paramLotNo));
        params.addAll(Arrays.asList(paramEdaban, paramEdaban));
        params.addAll(Arrays.asList(paramKeisuuDateF, paramKeisuuDateF));
        params.addAll(Arrays.asList(paramKeisuuDateT, paramKeisuuDateT));
        params.addAll(Arrays.asList(paramKeisuutantosya, paramKeisuutantosya));
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
