/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日       2020/9/22<br>
 * 計画書No     MB2008-DK001<br>
 * 変更者       863 zhangjinyan<br>
 * 変更理由     仕様変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C001(膜厚(SPS))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/13
 */
@ManagedBean(name = "beanGXHDO101C001")
@SessionScoped
public class GXHDO101C001 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo;
    /**
     * ﾛｯﾄNo
     */
    private String lotno;
    /**
     * 枝番
     */
    private String edaban;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * 膜厚(SPS)サブ画面用データ
     */
    private GXHDO101C001Model gxhdO101c001Model;

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     */
    private GXHDO101C001Model gxhdO101c001ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C001() {
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo.
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
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
     * 膜厚(SPS)サブ画面用データ
     *
     * @return the gxhdO101c001Model
     */
    public GXHDO101C001Model getGxhdO101c001Model() {
        return gxhdO101c001Model;
    }

    /**
     * 膜厚(SPS)サブ画面用データ
     *
     * @param gxhdO101c001Model the gxhdO101c001Model to set
     */
    public void setGxhdO101c001Model(GXHDO101C001Model gxhdO101c001Model) {
        this.gxhdO101c001Model = gxhdO101c001Model;
    }

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c001ModelView
     */
    public GXHDO101C001Model getGxhdO101c001ModelView() {
        return gxhdO101c001ModelView;
    }

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c001ModelView the gxhdO101c001ModelView to set
     */
    public void setGxhdO101c001ModelView(GXHDO101C001Model gxhdO101c001ModelView) {
        this.gxhdO101c001ModelView = gxhdO101c001ModelView;
    }

    /**
     * 膜厚読込処理
     */
    public void doMakuatsuDataImport() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String jissekino = StringUtil.nullToBlank(session.getAttribute("jissekino"));
            List<Map<String, Object>> makuatsuKanriDataList = getMakuatuKanri(jissekino);
            if (makuatsuKanriDataList.isEmpty()) {
                // メッセージをセット
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000033"), null);
                facesContext.addMessage(null, message);
                return;
            }
            
            String startKaisuu = ""; // 区分がスタートの最大回数
            String endKaisuu = ""; // 区分がエンドの最大回数
            for (Map<String, Object> makuatsuKanriData : makuatsuKanriDataList) {
                String kubun = StringUtil.nullToBlank(makuatsuKanriData.get("kubun"));
                if (kubun.endsWith("スタート")) {
                    startKaisuu = StringUtil.nullToBlank(makuatsuKanriData.get("kaiSuu"));
                } else if (kubun.endsWith("エンド")) {
                    endKaisuu = StringUtil.nullToBlank(makuatsuKanriData.get("kaiSuu"));
                }
            }
        
            List<Map<String, Object>> makuatsuDataList = getMakuatu(startKaisuu, endKaisuu);
           if(makuatsuDataList.isEmpty()){
                // メッセージをセット
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000033"), null);
                facesContext.addMessage(null, message);
                return;
           }
           // 膜厚1データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(0), "1");
           // 膜厚2データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(1), "2");
           // 膜厚3データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(2), "3");
           // 膜厚4データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(3), "4");
           // 膜厚5データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(4), "5");
           // 膜厚6データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(5), "6");
           // 膜厚7データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(6), "7");
           // 膜厚8データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(7), "8");
           // 膜厚9データセット
           setMakuatsuData(makuatsuDataList,this.gxhdO101c001ModelView.getMakuatsuDataList().get(8), "9");
           
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実行エラー", ex, LOGGER);
        }

    }
    
    /**
     * 膜厚管理情報取得
     * @param jissekino 実績No
     *
     * @return 膜厚管理情報取得
     */
    private List<Map<String, Object>> getMakuatuKanri(String jissekino) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        
        String startKubun = "";
        String endKubun = "";
        String mark = "=";
        if ("1".equals(jissekino)) {
            startKubun = "スタート";
            endKubun = "エンド";
        } else {
            startKubun = "% " + jissekino + "層目スタート"; 
            endKubun = "% " + jissekino + "層目エンド"; 
            mark = "like";
        }
        
        String sql = "SELECT MAX(KaiSuu) kaiSuu,kubun "
                + " FROM sr_makuatu_kanri "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + " AND (Kubun " + mark + " ? OR Kubun " + mark + " ? )"
                + " GROUP BY Kubun ORDER BY KaiSuu ";

        List<Object> params = new ArrayList<>();
        params.add(this.kojyo);
        params.add(this.lotno);
        params.add(this.edaban);
        params.add(startKubun);
        params.add(endKubun);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return  queryRunner.query(sql, new MapListHandler(), params.toArray());
    }
    
    /**
     * 膜厚データ設定処理
     * @param makuatsuDataList 膜厚データリスト(DB取得ﾃﾞｰﾀ)
     * @param makuatsuData 膜厚データ(画面表示データ)
     * @param sokuteiten 測定点
     */
    private void setMakuatsuData(List<Map<String, Object>> makuatsuDataList, GXHDO101C001Model.MakuatsuData makuatsuData, String sokuteiten){
        String makuatsuStart = "";
        String makuatsuEnd = "";
        boolean isFirst = true;
        for (Map<String, Object> map : makuatsuDataList) {
            // 測定点が引数の測定点でない場合コンティニュー
            if(!sokuteiten.equals(StringUtil.nullToBlank(map.get("sokuteiten")))){
                // 対象の測定点のデータがすでに取得出来ている場合はブレイク
                if(!isFirst){
                    break;
                }
                continue;
            }
            
            if(isFirst){
                isFirst = false;
                // 回数が最小のデータ
                makuatsuStart = StringUtil.nullToBlank(map.get("makuatu"));
            }
            
            // 回数が最大のデータ
            makuatsuEnd = StringUtil.nullToBlank(map.get("makuatu"));
        }
        
        makuatsuData.setStartVal(makuatsuStart);
        makuatsuData.setEndVal(makuatsuEnd);
        
    }

    /**
     * 膜厚情報取得
     *
     * @return 膜厚情報取得
     */
    private List<Map<String, Object>> getMakuatu(String startKaisuu, String endKaisuu) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT kaisuu,makuatu,sokuteiten "
                + " FROM sr_makuatu "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND (kaisuu = ? OR kaisuu = ?)"
                + " ORDER BY sokuteiten, kaisuu";

        List<Object> params = new ArrayList<>();
        params.add(this.kojyo);
        params.add(this.lotno);
        params.add(this.edaban);
        params.add(startKaisuu);
        params.add(endKaisuu);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return  queryRunner.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        this.isFormError = false;
        if (!checkOK()) {
            this.isFormError = true;
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }

        this.gxhdO101c001Model = this.gxhdO101c001ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C001Model.MakuatsuData makuatsuData : this.gxhdO101c001ModelView.getMakuatsuDataList()) {
            if (StringUtil.isEmpty(makuatsuData.getStartVal())) {
                setError(makuatsuData, true, false, "XHD-000027", "");
                return false;
            } else {
                if (!NumberUtil.isNumeric(makuatsuData.getStartVal())) {
                    setError(makuatsuData, true, false, "XHD-000008", "膜厚ｽﾀｰﾄ" + makuatsuData.getMakuatsu());
                    return false;
                }

                BigDecimal decStart = new BigDecimal(makuatsuData.getStartVal());
                if (!NumberUtil.isValidDigits(decStart, 2, 3)) {
                    setError(makuatsuData, true, false, "XHD-000007", "膜厚ｽﾀｰﾄ" + makuatsuData.getMakuatsu(), "2", "3");
                    return false;
                }
            }

            if (StringUtil.isEmpty(makuatsuData.getEndVal())) {
                setError(makuatsuData, false, true, "XHD-000027", "");
                return false;
            } else {
                if (!NumberUtil.isNumeric(makuatsuData.getEndVal())) {
                    setError(makuatsuData, false, true, "XHD-000008", "膜厚ｴﾝﾄﾞ" + makuatsuData.getMakuatsu());
                    return false;
                }

                BigDecimal decEnd = new BigDecimal(makuatsuData.getEndVal());
                if (!NumberUtil.isValidDigits(decEnd, 2, 3)) {
                    setError(makuatsuData, false, true, "XHD-000007", "膜厚ｴﾝﾄﾞ" + makuatsuData.getMakuatsu(), "2", "3");
                    return false;
                }

            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param makuatsuData 膜厚データ
     * @param isStartErr スタートエラー
     * @param isEndErr エンドエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C001Model.MakuatsuData makuatsuData, boolean isStartErr, boolean isEndErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isStartErr) {
            makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // エンドエラー
        if (isEndErr) {
            makuatsuData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C001Model.MakuatsuData makuatsuData : this.gxhdO101c001ModelView.getMakuatsuDataList()) {
            makuatsuData.setStartTextBackColor("");
            makuatsuData.setEndTextBackColor("");
        }
    }

}
