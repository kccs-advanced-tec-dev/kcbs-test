/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C008Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
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
 * GXHDO101C008(ﾊﾟﾀｰﾝ間距離)
 *
 * @author KCSS K.Jo
 * @since 2019/01/08
 */
@ManagedBean(name = "beanGXHDO101C008")
@SessionScoped
public class GXHDO101C008 implements Serializable {

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
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     */
    private GXHDO101C008Model gxhdO101c008Model;

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     */
    private GXHDO101C008Model gxhdO101c008ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C008() {
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
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     *
     * @return the gxhdO101c008Model
     */
    public GXHDO101C008Model getGxhdO101c008Model() {
        return gxhdO101c008Model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     *
     * @param gxhdO101c008Model the gxhdO101c008Model to set
     */
    public void setGxhdO101c008Model(GXHDO101C008Model gxhdO101c008Model) {
        this.gxhdO101c008Model = gxhdO101c008Model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c008ModelView
     */
    public GXHDO101C008Model getGxhdO101c008ModelView() {
        return gxhdO101c008ModelView;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c008ModelView the gxhdO101c008ModelView to set
     */
    public void setGxhdO101c008ModelView(GXHDO101C008Model gxhdO101c008ModelView) {
        this.gxhdO101c008ModelView = gxhdO101c008ModelView;
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

        this.gxhdO101c008Model = this.gxhdO101c008ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C008Model.PtnKanKyoriData ptnKanKyoriData : this.gxhdO101c008ModelView.getPtnKanKyoriDataList()) {

            // 未入力チェック
            if (StringUtil.isEmpty(ptnKanKyoriData.getValue())) {

                setError(ptnKanKyoriData, "XHD-000027");
                return false;

            } else {

                // 型チェック
                if (!NumberUtil.isIntegerNumeric(ptnKanKyoriData.getValue()) || !NumberUtil.isNumeric(ptnKanKyoriData.getValue())) {
                    setError(ptnKanKyoriData, "XHD-000008", "ﾊﾟﾀｰﾝ間距離" + ptnKanKyoriData.getPtnKanKyori());
                    return false;
                }

                // 桁チェック
                if (3 < StringUtil.length(ptnKanKyoriData.getValue())) {
                    setError(ptnKanKyoriData, "XHD-000006", "ﾊﾟﾀｰﾝ間距離" + ptnKanKyoriData.getPtnKanKyori(), "3");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKanKyoriData ﾊﾟﾀｰﾝ間距離データ
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C008Model.PtnKanKyoriData ptnKanKyoriData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        ptnKanKyoriData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);

    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C008Model.PtnKanKyoriData ptnKanKyoriData : this.gxhdO101c008ModelView.getPtnKanKyoriDataList()) {
            ptnKanKyoriData.setTextBackColor("");
        }
    }

}
