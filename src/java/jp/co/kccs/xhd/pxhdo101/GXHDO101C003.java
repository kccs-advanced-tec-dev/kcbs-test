/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
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
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C003(PTN距離ｴﾝﾄﾞ)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C003")
@SessionScoped
public class GXHDO101C003 implements Serializable {

    /**
     * PTN距離Yサブ画面用データ
     */
    private GXHDO101C003Model gxhdO101c003Model;

    /**
     * PTN距離Yサブ画面用データ(表示制御用)
     */
    private GXHDO101C003Model gxhdO101c003ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C003() {
    }

    /**
     * PTN距離ｴﾝﾄﾞサブ画面用データ
     *
     * @return the gxhdO101c003Model
     */
    public GXHDO101C003Model getGxhdO101c003Model() {
        return gxhdO101c003Model;
    }

    /**
     * PTN距離ｴﾝﾄﾞサブ画面用データ
     *
     * @param gxhdO101c003Model the gxhdO101c003Model to set
     */
    public void setGxhdO101c003Model(GXHDO101C003Model gxhdO101c003Model) {
        this.gxhdO101c003Model = gxhdO101c003Model;
    }

    /**
     * PTN距離ｴﾝﾄﾞサブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c003ModelView
     */
    public GXHDO101C003Model getGxhdO101c003ModelView() {
        return gxhdO101c003ModelView;
    }

    /**
     * PTN距離ｴﾝﾄﾞサブ画面用データ(表示制御用)
     *
     * @param gxhdO101c003ModelView the gxhdO101c003ModelView to set
     */
    public void setGxhdO101c003ModelView(GXHDO101C003Model gxhdO101c003ModelView) {
        this.gxhdO101c003ModelView = gxhdO101c003ModelView;
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
        this.isFormError = false;
        if (!checkOK()) {
            this.isFormError = true;
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }

        this.gxhdO101c003Model = this.gxhdO101c003ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C003Model.PtnKyoriEndData ptnKyoriEndData : this.gxhdO101c003ModelView.getPtnKyoriEndDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriEndData.getPtnKyoriXVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriEndData.getPtnKyoriXVal())) {
                    setError(ptnKyoriEndData, true, false, "XHD-000008", "PTN距離Xｴﾝﾄﾞ" + ptnKyoriEndData.getPtnKyoriEnd());
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriEndData.getPtnKyoriXVal())) {
                    setError(ptnKyoriEndData, true, false, "XHD-000006", "PTN距離Xｴﾝﾄﾞ" + ptnKyoriEndData.getPtnKyoriEnd(), "3");
                    return false;
                }
            }

            if (!StringUtil.isEmpty(ptnKyoriEndData.getPtnKyoriYVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriEndData.getPtnKyoriYVal())) {
                    setError(ptnKyoriEndData, false, true, "XHD-000008", "PTN距離Yｴﾝﾄﾞ" + ptnKyoriEndData.getPtnKyoriEnd());
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriEndData.getPtnKyoriYVal())) {
                    setError(ptnKyoriEndData, false, true, "XHD-000006",  "PTN距離Yｴﾝﾄﾞ" + ptnKyoriEndData.getPtnKyoriEnd(), "3");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKyoriEndData PTN距離ｴﾝﾄﾞデータ
     * @param isItemXErr PTN距離Xエラー
     * @param isItemYErr PTN距離Yエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C003Model.PtnKyoriEndData ptnKyoriEndData, boolean isItemXErr, boolean isItemYErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // PTN距離Xエラー
        if (isItemXErr) {
            ptnKyoriEndData.setPtnKyoriXTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // PTN距離Yエラー
        if (isItemYErr) {
            ptnKyoriEndData.setPtnKyoriYTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C003Model.PtnKyoriEndData ptnKyoriEndData : this.gxhdO101c003ModelView.getPtnKyoriEndDataList()) {
            ptnKyoriEndData.setPtnKyoriXTextBackColor("");
            ptnKyoriEndData.setPtnKyoriYTextBackColor("");
        }
    }
}
