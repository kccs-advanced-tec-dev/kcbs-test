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
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO101C007Model;
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
 * 変更日	2019/01/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C007(電極膜厚)
 *
 * @author KCSS K.Jo
 * @since 2019/01/08
 */
@ManagedBean(name = "beanGXHDO101C007")
@SessionScoped
public class GXHDO101C007 implements Serializable {

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * ロガー
     */
    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());

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
     * 電極膜厚サブ画面用データ
     */
    private GXHDO101C007Model gxhdO101c007Model;

    /**
     * 電極膜厚サブ画面用データ(表示制御用)
     */
    private GXHDO101C007Model gxhdO101c007ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C007() {
    }

    /**
     * 工場コード
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場コード
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ロットNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ロットNo
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
     * 電極膜厚サブ画面用データ
     *
     * @return the gxhdO101c007Model
     */
    public GXHDO101C007Model getGxhdO101c007Model() {
        return gxhdO101c007Model;
    }

    /**
     * 電極膜厚サブ画面用データ
     *
     * @param gxhdO101c007Model the gxhdO101c007Model to set
     */
    public void setGxhdO101c007Model(GXHDO101C007Model gxhdO101c007Model) {
        this.gxhdO101c007Model = gxhdO101c007Model;
    }

    /**
     * 電極膜厚サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c007ModelView
     */
    public GXHDO101C007Model getGxhdO101c007ModelView() {
        return gxhdO101c007ModelView;
    }

    /**
     * 電極膜厚サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c007ModelView the gxhdO101c007ModelView to set
     */
    public void setGxhdO101c007ModelView(GXHDO101C007Model gxhdO101c007ModelView) {
        this.gxhdO101c007ModelView = gxhdO101c007ModelView;
    }

    /**
     * 電極膜厚読込処理
     */
    public void doMakuatsuDataImport() {
        try {
            // 背景色をクリア
            clearBackColor();

            // 測定Noを取得
            String sokuteino = getSokuteino();
            if (StringUtil.isEmpty(sokuteino)) {
                // 膜厚測定データをが読込データをセット
                setMakuatsuSokuteiData();
            } else {
                // 膜厚測定データをが読込データをセット
                setSokuteiData(sokuteino);
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実行エラー", ex, LOGGER);
        }

    }

    /**
     * 測定データ設定処理
     *
     * @throws SQLException 例外エラー
     */
    private void setSokuteiData(String sokuteino) throws SQLException {
        List<Map<String, Object>> sokuteiDataList = getSokuteiData(sokuteino);

        if (sokuteiDataList.isEmpty()) {
            // メッセージをセット
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000047"), null);
            facesContext.addMessage(null, message);
            return;
        }

        // データをクリア
        for (GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData : this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList()) {
            denkyokuMakuatsuData.setValue("");
        }

        for (Map<String, Object> map : sokuteiDataList) {
            switch (StringUtil.nullToBlank(map.get("number"))) {
                case "1":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(0).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "2":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(1).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "3":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(2).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "4":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(3).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "5":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(4).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "6":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(5).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "7":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(6).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "8":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(7).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
                case "9":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(7).setValue(StringUtil.nullToBlank(map.get("suiheiphanteih1")));
                    break;
            }
        }
    }

    /**
     * 膜厚測定データ設定処理
     *
     * @throws SQLException 例外エラー
     */
    private void setMakuatsuSokuteiData() throws SQLException {
        List<Map<String, Object>> sokuteiDataList = getMakuatsuSokuteiData();

        if (sokuteiDataList.isEmpty()) {
            // メッセージをセット
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000048"), null);
            facesContext.addMessage(null, message);
            return;
        }

        // データをクリア
        for (GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData : this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList()) {
            denkyokuMakuatsuData.setValue("");
        }

        for (Map<String, Object> map : sokuteiDataList) {
            switch (StringUtil.nullToBlank(map.get("Position"))) {
                case "1":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(0).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "2":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(1).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "3":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(2).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "4":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(3).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "5":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(4).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "6":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(4).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "7":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(6).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "8":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(7).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
                case "9":
                    this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList().get(8).setValue(StringUtil.nullToBlank(map.get("Mean")));
                    break;
            }
        }
    }

    /**
     * ロット情報テーブルから測定Noを取得
     *
     * @throws SQLException 例外エラー
     */
    private String getSokuteino() throws SQLException {
        String sokuteino = "";
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT sokuteino "
                + " FROM sr_rhapsmakuatsu1 "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(this.kojyo);
        params.add(this.lotno);
        params.add(this.edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List<Map<String, Object>> dataList = queryRunner.query(sql, new MapListHandler(), params.toArray());
        if (!dataList.isEmpty()) {
            sokuteino = StringUtil.nullToBlank(dataList.get(0).get("sokuteino"));
        }
        return sokuteino;
    }

    /**
     * 測定データテーブルからデータを取得
     *
     * @param sokuteino 測定No
     * @return 測定データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getSokuteiData(String sokuteino) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT suiheiphanteih1,number "
                + " FROM sr_rhapsmakuatsu2 "
                + " WHERE sokuteino = ? "
                + " ORDER BY number ";

        List<Object> params = new ArrayList<>();
        params.add(sokuteino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunner.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * 膜厚測定データテーブルからデータを取得
     *
     * @return 膜厚測定データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> getMakuatsuSokuteiData() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        String sql = "SELECT Mean,Position "
                + " FROM sr_zygo_e "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + " ORDER BY Position ";

        List<Object> params = new ArrayList<>();
        params.add(this.kojyo);
        params.add(this.lotno);
        params.add(this.edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunner.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        if (!checkOK()) {
            this.isFormError = true;

            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }

        this.gxhdO101c007Model = this.gxhdO101c007ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        // 6～9にデータが入力されているかどうか確認
        int index = 0;
        boolean hasInputAfter6 = false;
        for (GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData : this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList()) {
            index++;
            if (6 <= index && !StringUtil.isEmpty(denkyokuMakuatsuData.getValue())) {
                hasInputAfter6 = true;
                break;
            }
        }

        int indexMain = 0;
        for (GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData : this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList()) {
            indexMain++;
            if (StringUtil.isEmpty(denkyokuMakuatsuData.getValue())) {
                if (indexMain <= 5) {
                    //1～5は未入力はエラー
                    setError(denkyokuMakuatsuData, "XHD-000027");
                    return false;
                } else if (hasInputAfter6) {
                    //6～9はいずれかが入力されている場合、未入力はエラー
                    setError(denkyokuMakuatsuData, "XHD-000027");
                    return false;
                }
            } else {
                //型ﾁｪｯｸ
                if (!NumberUtil.isNumeric(denkyokuMakuatsuData.getValue())) {
                    setError(denkyokuMakuatsuData, "XHD-000008", "電極膜厚" + denkyokuMakuatsuData.getDenkyokuMakuatsu());
                    return false;
                }
                //桁数ﾁｪｯｸ(小数あり)(スタート)
                if (!NumberUtil.isValidDigits(new BigDecimal(denkyokuMakuatsuData.getValue()), 1, 3)) {
                    setError(denkyokuMakuatsuData, "XHD-000007", "電極膜厚" + denkyokuMakuatsuData.getDenkyokuMakuatsu(), 1, 3);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param denkyokuMakuatsuData 電極膜厚データ
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        denkyokuMakuatsuData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);

    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C007Model.DenkyokuMakuatsuData denkyokuMakuatsuData : this.gxhdO101c007ModelView.getDenkyokuMakuatsuDataList()) {
            denkyokuMakuatsuData.setTextBackColor("");
        }
    }

}
