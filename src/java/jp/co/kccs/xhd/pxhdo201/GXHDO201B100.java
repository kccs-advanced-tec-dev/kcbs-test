package jp.co.kccs.xhd.pxhdo201;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO201B100Model;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * 外観検査・熱処理履歴検索画面
 */
@Named
@ViewScoped
public class GXHDO201B100 implements Serializable {

    /**
     * @return the searchItem
     */
    public GXHDO201B100Model getSearchItem() {
        return searchItem;
    }

    /**
     * @param searchItem the searchItem to set
     */
    public void setSearchItem(GXHDO201B100Model searchItem) {
        this.searchItem = searchItem;
    }
    private static final Logger LOGGER = Logger.getLogger(GXHDO201B100.class.getName());
    
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    
    /** タイムゾーン(convertDateTimeに渡すために使う */
    private final TimeZone timeZone;
 
    /** 一覧表示データ */
    private GXHDO201B100Model searchItem = null;
    
    /** 検索条件：ロットNo */
    private String lotNo = "";

    /**
     * コンストラクタ
     */
    public GXHDO201B100() {
       timeZone = TimeZone.getDefault();
    }

//<editor-fold defaultstate="collapsed" desc="#getter setter">
    
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

   public TimeZone getTimeZome() {
       return timeZone;
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

        // 画面クリア
        clear();
    }
    
    /**
     * 画面クリア
     */
    public void clear() {
        lotNo = "";
        setSearchItem(new GXHDO201B100Model());
    }
           
    /**
     * 入力値チェック：
     * 正常な場合検索処理を実行する
     */
    public void checkInputAndSearch() {
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        
        // ロットNo
        String lotcode = getLotNo();
        if (StringUtil.isEmpty(lotcode)) {
            lotcode = "";
        }
        if (existError(validateUtil.checkValueE001(lotcode))) {
            return;
        }

        String kojyo = StringUtils.substring(lotcode, 0, 3);
        String lotno = StringUtils.substring(lotcode, 3, 11);
        String edaban = StringUtils.substring(lotcode, 11, 14);
        
        // データベース検索処理
        GXHDO201B100Model item = new GXHDO201B100Model();
        setSearchItem(item);
        try {
            // 仕掛データ検索(見つからなかったらエラー)
            if (!retrieveSikakariData(kojyo, lotno, edaban, item))
            {
                existError(MessageUtil.getMessage("XHD-000039"));
                return;
            }

            // 親ロットリストを作成
            List<String> targetEdabanList = makeParentEdabanList(kojyo, lotno, edaban);

            // 品質DB検索
            retrieveQcdbData(kojyo, lotno, targetEdabanList, item);

            // 外観工程への送り数量検索
            retrieveGaikanOkuriData(kojyo, lotno, targetEdabanList, item);

            // 担当者コードに対する担当者名を検索
            retrieveTantousyaName(item);
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            existError("SQLException Error");
            return;
        }
    }
    
    /**
     * 指定したロットの親ロット枝番のリストを作成して返す
     *   指定した枝番が最初の要素となる。その後、子ロット→親ロットの順のリストとなる。
     *   統合親ロットは検索しない。
     */
    private List<String> makeParentEdabanList(String kojyo, String lotno, String edaban) throws SQLException
    {
        Map<String, String> oyaLotMap = new HashMap<>(); 
        QueryRunner queryRunner = new QueryRunner(dataSourceWip);
        String sql = "SELECT EDABAN, OYALOTEDABAN FROM SIKAKARI " +
                     "WHERE KOJYO = ? " +
                     "AND LOTNO = ? ";
        // パラメータ設定
        Object[] params = {kojyo, lotno};
        DBUtil.outputSQLLog(sql, params, LOGGER);
        List<Map<String, Object>> result = queryRunner.query(sql, new MapListHandler(), params);
        for (Map m: result) {
            oyaLotMap.put((String)m.get("EDABAN"), (String)m.get("OYALOTEDABAN"));
        }
        
        List<String> parentLotList = new ArrayList();
        String targetEdaban = edaban;
        int i = 0;
        while ((targetEdaban != null) && (i < 100))
        {
            parentLotList.add(targetEdaban);
            targetEdaban = oyaLotMap.get(targetEdaban);
            if (targetEdaban.equals("000") || targetEdaban.equals("999"))
            {
                targetEdaban = null;
            }
            i++;
        }
        
        return parentLotList;
    }
    
    /**
     * 仕掛データを検索し、itemにセットする
     *   見つかった場合、trueを返す。
     */
    private boolean retrieveSikakariData(String kojyo, String lotno, String edaban, GXHDO201B100Model item) throws SQLException
    {
        boolean found = false;
        QueryRunner queryRunner = new QueryRunner(dataSourceWip);
        String sql = "SELECT S.KCPNO, S.TOKUISAKI, S.BIKOU1, S.BIKOU3, S.KOTEICODE, " +
                            "S.SUURYO, S.OPENCLOSEFLAG, K.KOTEI " +
                     "FROM SIKAKARI S " +
                     "LEFT JOIN KOTEIMAS K " +
                     "ON S.KOTEICODE = K.KOTEICODE " +
                     "WHERE S.KOJYO = ? " +
                     "AND S.LOTNO = ? " +
                     "AND S.EDABAN = ? ";
        // パラメータ設定
        Object[] params = {kojyo, lotno, edaban};
        DBUtil.outputSQLLog(sql, params, LOGGER);
        Map<String, Object> m = queryRunner.query(sql, new MapHandler(), params);
        if (m != null) {
            found = true;
            item.setKcpno((String)m.get("KCPNO"));
            item.setTokuisaki((String)m.get("TOKUISAKI"));
            item.setBikou1((String)m.get("BIKOU1"));
            item.setBikou3((String)m.get("BIKOU3"));
            item.setKotei_code((String)m.get("KOTEICODE"));
            item.setSuuryo((Integer)m.get("SUURYO"));
            item.setOpencloseflag((String)m.get("OPENCLOSEFLAG"));
            item.setKotei_name(((String)m.get("KOTEI")).trim());
        }
        return found;
    }
    
    /**
     * QA外観、4次外観、熱処理の履歴を検索し、itemに追記する。
     */
    private void retrieveQcdbData(String kojyo, String lotno, List<String> targetEdabanList, GXHDO201B100Model item) throws SQLException
    {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);

        // QA外観データ取得
        String sql = "SELECT jissekino, kensahinsyurui, kensamen, hantei, kensabi, skensasya FROM sr_qagaikan " +
                     "WHERE kojyo = ? " +
                     "AND lotno = ? " +
                     "AND edaban = ? " +
                     "ORDER BY jissekino DESC ";
        for (String edaban: targetEdabanList) {
            Object[] params = {kojyo, lotno, edaban};
            DBUtil.outputSQLLog(sql, params, LOGGER);
            Map<String, Object> m = queryRunner.query(sql, new MapHandler(), params);
            if (m != null) {
                item.setQagaikan_nichiji((Timestamp)m.get("kensabi"));
                item.setQagaikan_kensahinsyurui((String)m.get("kensahinsyurui"));
                item.setQagaikan_hantei((String)m.get("hantei"));
                item.setQagaikan_kensasya_code((String)m.get("skensasya"));
                break;
            }
        }

        // (全数)4次外観データ取得
        sql = "SELECT kaisuu, kensagouki, kensamen, kensakaishinichiji, " +
                     "kensasyuuryounichiji, okuriryouhinsuu, goukeiryouhinkosuu " +
              "FROM sr_gaikankensa " +
              "WHERE kojyo = ? " +
              "AND lotno = ? " +
              "AND edaban = ? " +
              "AND gaikankensasyurui = ? " +
              "ORDER BY kaisuu DESC ";
        for (String edaban: targetEdabanList) {
            Object[] params = {kojyo, lotno, edaban, 4};   // 4次外観検査の"4"
            DBUtil.outputSQLLog(sql, params, LOGGER);
            Map<String, Object> m = queryRunner.query(sql, new MapHandler(), params);
            if (m != null) {
                item.setGaikan_kaisuu((Integer)m.get("kaisuu"));
                item.setGaikan_kensagouki((String)m.get("kensagouki"));
                item.setGaikan_kensamen((String)m.get("kensamen"));
                item.setGaikan_kaishinichiji((Timestamp)m.get("kensakaishinichiji"));
                item.setGaikan_syuuryounichiji((Timestamp)m.get("kensasyuuryounichiji"));
                item.setGaikan_okuriryouhinsuu((Integer)m.get("okuriryouhinsuu"));
                item.setGaikan_goukeiryouhinkosuu((Integer)m.get("goukeiryouhinkosuu"));
                break;
            }
        }

        // 熱処理データ取得
        sql = "SELECT syoribi, syuuryoujikan FROM sr_shinkuukansou " +
              "WHERE kojyo = ? " +
              "AND lotno = ? " +
              "AND edaban = ? " +
              "ORDER BY syoribi DESC, syuuryoujikan DESC ";
        for (String edaban: targetEdabanList) {
            Object[] params = {kojyo, lotno, edaban};
            DBUtil.outputSQLLog(sql, params, LOGGER);
            Map<String, Object> m = queryRunner.query(sql, new MapHandler(), params);
            if (m != null) {
                item.setNetsusyori_syoribi((Timestamp)m.get("syoribi"));
                item.setNetsusyori_syuuryoujikan((String)m.get("syuuryoujikan"));
                break;
            }
        }
    }

    /**
     * 4次外観検査工程への送り数量をitemにセットする。
     */
    private void retrieveGaikanOkuriData(String kojyo, String lotno, List<String> targetEdabanList, GXHDO201B100Model item) throws SQLException
    {
        QueryRunner queryRunner = new QueryRunner(dataSourceWip);

        // 4次外観への送り数量を取得(L→Pへの送り)
        // 複数回送り実績がある場合は最新のデータを使う
        String sql = "SELECT J.JissekiNo, SUM(O.SuuRyo) AS OkuriSuu " +
                     "FROM JISSEKI J INNER JOIN OKUBUN O " +
                     "ON J.JissekiNo = O.JissekiNo " +
                     "WHERE J.Kojyo = ? " +
                     "AND J.LotNo = ? " +
                     "AND J.EdaBan = ? " +
                     "AND J.SyoriKubunCode IN ('01', '03', '05', '06') " +
                     "AND J.KoteiCode LIKE 'L%' " +
                     "AND O.NextKotei LIKE 'P%' " +
                     "GROUP BY J.JissekiNo " +
                     "ORDER BY J.JissekiNo DESC";
        for (String edaban: targetEdabanList) {
            Object[] params = {kojyo, lotno, edaban};
            DBUtil.outputSQLLog(sql, params, LOGGER);
            Map<String, Object> m = queryRunner.query(sql, new MapHandler(), params);
            if (m != null) {
                item.setTo_gaikan_okurisuu(Math.round((double)m.get("OkuriSuu")));
                break;
            }
        }
    }

    /**
     * itemの中の担当者コードに対応する担当者名を検索してセットする
     */
    private void retrieveTantousyaName(GXHDO201B100Model item) throws SQLException
    {
        QueryRunner queryRunner = new QueryRunner(dataSourceWip);
        String sql = "SELECT TANTOUSYA FROM TANTOMAS " +
                     "WHERE TANTOUSYACODE = ? ";
        // パラメータ設定
        Object[] params = {item.getQagaikan_kensasya_code()};
        DBUtil.outputSQLLog(sql, params, LOGGER);
        Map<String, Object> map = queryRunner.query(sql, new MapHandler(), params);
        if (map != null) {
            item.setQagaikan_kensasya_name(((String)map.get("TANTOUSYA")).trim());
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
}
