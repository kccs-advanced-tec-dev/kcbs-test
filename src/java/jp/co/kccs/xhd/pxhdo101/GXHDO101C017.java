/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/03/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C017_画面制御
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/03/01
 */
@SessionScoped
@Named("beanGXHDO101C017")
public class GXHDO101C017 implements Serializable {

    /**
     * @return the LOGGER
     */
    public static Logger getLOGGER() {
        return LOGGER;
    }

    
    private static final Logger LOGGER = Logger.getLogger(GXHDO101C017.class.getName());

    /**
     * 通常カラーコード(テキスト)
     */
    private static final String DEFAULT_BACK_COLOR = "#ffffffff";
    
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceXHD;
    
    /**
     * メニューリスト
     */
     private List<FXHDM01> menuListGXHDO101;
    
     /**
     * メニューリスト表示用
     */
     private List<String> menuList;
    
     /**
     * 選択メニュー
     */
     private String selectMenu;
    
     /**
     * 選択メニュー
     */
     private FXHDM01 atoKoteiMenu;
     
    /**
     * 追加位置ﾃﾞｰﾀ
     */
    private FXHDM01 insPositionInfoMenu;
   
    /**
     * ﾛｯﾄNo
     */
    private String lotno;
    
  /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantoushaCd;
    

    /**
     * フォームエラー判定
     */
    private boolean isFormError;
    
    /**
     * コンストラクタ
     */
    public GXHDO101C017() {
    }
    
    

     /**
     * メニューリスト
     * @return the menuListGXHDO101
     */
    public List<FXHDM01> getMenuListGXHDO101() {
        return menuListGXHDO101;
    }

    /**
     * メニューリスト
     * @param menuListGXHDO101 the menuListGXHDO101 to set
     */
    public void setMenuListGXHDO101(List<FXHDM01> menuListGXHDO101) {
        this.menuListGXHDO101 = menuListGXHDO101;
    }

    /**
     * メニューリスト表示用
     * @return the menuList
     */
    public List<String> getMenuList() {
        return menuList;
    }

    /**
     * メニューリスト表示用
     * @param menuList the menuList to set
     */
    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }

    /**
     * 選択メニュー
     * @return the selectMenu
     */
    public String getSelectMenu() {
        return selectMenu;
    }

    /**
     * 選択メニュー
     * @param selectMenu the selectMenu to set
     */
    public void setSelectMenu(String selectMenu) {
        this.selectMenu = selectMenu;
    }

    /**
     * 後工程メニュー
     * @return the atoKoteiMenu
     */
    public FXHDM01 getAtoKoteiMenu() {
        return atoKoteiMenu;
    }

    /**
     * 後工程メニュー
     * @param atoKoteiMenu the atoKoteiMenu to set
     */
    public void setAtoKoteiMenu(FXHDM01 atoKoteiMenu) {
        this.atoKoteiMenu = atoKoteiMenu;
    }

    /**
     * 追加位置ﾃﾞｰﾀ
     * @return the insPositionInfoMenu
     */
    public FXHDM01 getInsPositionInfoMenu() {
        return insPositionInfoMenu;
    }

    /**
     * 追加位置ﾃﾞｰﾀ
     * @param insPositionInfoMenu the insPositionInfoMenu to set
     */
    public void setInsPositionInfoMenu(FXHDM01 insPositionInfoMenu) {
        this.insPositionInfoMenu = insPositionInfoMenu;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @return the tantoushaCd
     */
    public String getTantoushaCd() {
        return tantoushaCd;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @param tantoushaCd the tantoushaCd to set
     */
    public void setTantoushaCd(String tantoushaCd) {
        this.tantoushaCd = tantoushaCd;
    }
    
    

    /**
     * フォームエラー判定
     *
     * @return the isFormError
     */
    public boolean isIsFormError() {
        return isFormError;
    }

    /**
     * フォームエラー判定
     *
     * @param isFormError the isFormError to set
     */
    public void setIsFormError(boolean isFormError) {
        this.isFormError = isFormError;
    }
    
    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        String selectMenuId = this.menuListGXHDO101.stream().filter(n -> n.getFormTitle().equals(this.selectMenu)).findFirst().map(f -> f.getFormId()).orElse("");
        if (!checkOK(selectMenuId, this.selectMenu)) {
            
             setIsFormError(true);
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }
        
        updateFormData(selectMenuId);
        
        setIsFormError(false);
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK(String selectMenuId, String selectMenu) {

        if(this.atoKoteiMenu != null && selectMenuId.equals(this.atoKoteiMenu.getFormId())){
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000193",selectMenu), null);
            facesContext.addMessage(null, message);
            return false;
        }
      
        return true;
    }
    
    private void updateFormData(String selectMenuId){
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);

        Connection conDoc = null;

        String strKojyo = this.lotno.substring(0, 3);
        String strLotNo = this.lotno.substring(3, 11);
        String strEdaban = this.lotno.substring(11, 14);

        try {
            // トランザクション開始
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            int jissekino = getMaxJissekiNo(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban, selectMenuId)+1;
            
            Map<String, Object> fxhdd08InfoAto = loadFxhdd08Info(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban, this.insPositionInfoMenu.getFormId(), this.insPositionInfoMenu.getJissekiNo(), 0);
            if (fxhdd08InfoAto != null && !fxhdd08InfoAto.isEmpty()) {

                if(this.atoKoteiMenu.getKoshinDateFxhdd08() == null || this.atoKoteiMenu.getKoshinDateFxhdd08().before((Timestamp)fxhdd08InfoAto.get("koshin_date"))){
                    FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000195"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                
                // ロールバックしてリターン
                DBUtil.rollbackConnection(conDoc, LOGGER);
                return;
                }
                
                updateFxhdd08AtoKotei(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban,
                        selectMenuId, jissekino,
                        this.insPositionInfoMenu.getFormId(), this.insPositionInfoMenu.getJissekiNo(), this.tantoushaCd, systemTime);

            }
           
            
            insertFxhdd08(queryRunnerDoc, conDoc, this.tantoushaCd, strKojyo, strLotNo, strEdaban, selectMenuId, jissekino, this.insPositionInfoMenu.getFormId(), this.insPositionInfoMenu.getJissekiNo(), systemTime);
            
            
            DbUtils.commitAndCloseQuietly(conDoc);
            
           
        } catch (SQLException e) {

            DBUtil.rollbackConnection(conDoc, LOGGER);
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

        } catch (Exception e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
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
    
    private int getMaxJissekiNo(QueryRunner queryRunnerDoc,Connection conDoc, String kojyo, String lotno, String edaban, String formId) throws SQLException{
        int maxJissekiNo;
        int jissekiNo08 = getMaxJisekiNoFxhdd08(queryRunnerDoc,conDoc, kojyo, lotno, edaban, formId);
        int jissekiNo03 = getMaxJisekiNoFxhdd03(queryRunnerDoc,conDoc, kojyo, lotno, edaban, formId);
        if(jissekiNo08 < jissekiNo03){
            maxJissekiNo = jissekiNo03;
        }else{
            maxJissekiNo = jissekiNo08;
        }
        return maxJissekiNo;
    }
    
   
    /**
     * 画面制御更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocmentServer)
     * @param conDoc コネクション(DocmentServer)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param newMaekouteiGamenId 画面ID
     * @param newMaekouteiJissekino 実績No
     * @param maekouteiGamenId 画面ID(前工程)
     * @param maekouteiJissekino 実績No(前工程)
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void updateFxhdd08AtoKotei(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo, String edaban,
            String newMaekouteiGamenId, int newMaekouteiJissekino, String maekouteiGamenId, int maekouteiJissekino,
            String tantoshaCd, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd08 SET "
                + "maekoutei_gamen_id = ?, maekoutei_jissekino = ?, "
                + "koshin_id = ?, koshin_date = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "  AND maekoutei_gamen_id = ? AND  maekoutei_jissekino = ? "
                + "  AND deleteflag = 0 ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(newMaekouteiGamenId); //前工程画面ID(NEW)
        params.add(newMaekouteiJissekino); //前工程実績No(NEW)
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(maekouteiGamenId); //前工程画面ID
        params.add(maekouteiJissekino); //前工程実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }
    
     /**
     * [画面制御]から実績No(最大)を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private int getMaxJisekiNoFxhdd08(QueryRunner queryRunnerDoc, Connection conDoc,String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND gamen_id = ?  ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());

        int jissekino = 0;
        if (map != null && !map.isEmpty() && map.get("jissekino") != null) {
            jissekino = (int) map.get("jissekino");
        }

        return jissekino;

    }

    /**
     * [画面制御]から実績No(最大)を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private int getMaxJisekiNoFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT MAX(jissekino) AS jissekino "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND gamen_id = ?  ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());

        int jissekino = 0;
        if (map != null && !map.isEmpty() && map.get("jissekino") != null) {
            jissekino = (int) map.get("jissekino");
        }

        return jissekino;

    }
    
     /**
    *  [画面制御]から情報を取得
    * @param queryRunnerDoc queryRunnerオブジェクト
    * @param conDoc コネクション
    * @param kojyo 工場ｺｰﾄﾞ
    * @param lotNo ﾛｯﾄNo
    * @param edaban 枝番
    * @param formId 画面ID
    * @param jissekino 実績No
    * @param deleteflag 削除ﾌﾗｸﾞ
    * @return 画面制御情報
    * @throws SQLException 例外エラー 
    */
    private Map<String, Object> loadFxhdd08Info(QueryRunner queryRunnerDoc,Connection conDoc,  String kojyo, String lotNo,
            String edaban, String formId, int jissekino, int deleteflag) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT koshin_date "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND maekoutei_gamen_id = ?  AND maekoutei_jissekino = ? AND deleteflag = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        params.add(deleteflag);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc,sql, new MapHandler(), params.toArray());

    }


    /**
     * 画面制御(fxhdd08)登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void insertFxhdd08(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, 
            String kojyo, String lotNo, String edaban,String gamenId, int jissekino, String maekoteiGamenId, int maekoteiJissekino,  Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd08 ("
                + "kojyo,lotno,edaban,gamen_id,jissekino,maekoutei_gamen_id,maekoutei_jissekino,toroku_id,toroku_date,koshin_id,koshin_date,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(gamenId); //画面ID
        params.add(jissekino); //実績No
        params.add(maekoteiGamenId); //画面ID(前工程)
        params.add(maekoteiJissekino); //実績No(前工程)
        params.add(tantoshaCd); //登録者
        params.add(systemTime); //登録日
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(0); //削除ﾌﾗｸﾞ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }
    
    
   

}
