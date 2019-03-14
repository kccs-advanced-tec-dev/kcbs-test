/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
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
 * GXHDO101C002(PTN距離ｽﾀｰﾄ)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C002")
@SessionScoped
public class GXHDO101C002 implements Serializable {

    /**
     * PTN距離ｽﾀｰﾄサブ画面用データ
     */
    private GXHDO101C002Model gxhdO101c002Model;

    /**
     * PTN距離ｽﾀｰﾄサブ画面用データ(表示制御用)
     */
    private GXHDO101C002Model gxhdO101c002ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C002() {
    }

    /**
     * PTN距離ｽﾀｰﾄサブ画面用データ
     *
     * @return the gxhdO101c002Model
     */
    public GXHDO101C002Model getGxhdO101c002Model() {
        return gxhdO101c002Model;
    }

    /**
     * PTN距離ｽﾀｰﾄサブ画面用データ
     *
     * @param gxhdO101c002Model the gxhdO101c002Model to set
     */
    public void setGxhdO101c002Model(GXHDO101C002Model gxhdO101c002Model) {
        this.gxhdO101c002Model = gxhdO101c002Model;
    }

    /**
     * PTN距離ｽﾀｰﾄサブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c002ModelView
     */
    public GXHDO101C002Model getGxhdO101c002ModelView() {
        return gxhdO101c002ModelView;
    }

    /**
     ** PTN距離ｽﾀｰﾄサブ画面用データ(表示制御用)
     *
     * @param gxhdO101c002ModelView the gxhdO101c002ModelView to set
     */
    public void setGxhdO101c002ModelView(GXHDO101C002Model gxhdO101c002ModelView) {
        this.gxhdO101c002ModelView = gxhdO101c002ModelView;
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

        this.gxhdO101c002Model = this.gxhdO101c002ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C002Model.PtnKyoriStartData ptnKyoriStartData : this.gxhdO101c002ModelView.getPtnKyoriStartDataList()) {

            if (!StringUtil.isEmpty(ptnKyoriStartData.getPtnKyoriXVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriStartData.getPtnKyoriXVal())) {
                    setError(ptnKyoriStartData, true, false, "XHD-000008", "PTN距離Xｽﾀｰﾄ" + ptnKyoriStartData.getPtnKyoriStart());
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriStartData.getPtnKyoriXVal())) {
                    setError(ptnKyoriStartData, true, false, "XHD-000006", "PTN距離Xｽﾀｰﾄ" + ptnKyoriStartData.getPtnKyoriStart(), "3");
                    return false;
                }
            }

            if (!StringUtil.isEmpty(ptnKyoriStartData.getPtnKyoriYVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriStartData.getPtnKyoriYVal())) {
                    setError(ptnKyoriStartData, false, true, "XHD-000008", "PTN距離Yｽﾀｰﾄ" + ptnKyoriStartData.getPtnKyoriStart());
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriStartData.getPtnKyoriYVal())) {
                    setError(ptnKyoriStartData, false, true, "XHD-000006", "PTN距離Yｽﾀｰﾄ" + ptnKyoriStartData.getPtnKyoriStart(), "3");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKyoriStartData PTN距離ｽﾀｰﾄデータ
     * @param isItemXErr PTN距離Xエラー
     * @param isItemYErr PTN距離Yエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C002Model.PtnKyoriStartData ptnKyoriStartData, boolean isItemXErr, boolean isItemYErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // PTN距離Xエラー
        if (isItemXErr) {
            ptnKyoriStartData.setPtnKyoriXTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // PTN距離Yエラー
        if (isItemYErr) {
            ptnKyoriStartData.setPtnKyoriYTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C002Model.PtnKyoriStartData ptnKyoriXData : this.gxhdO101c002ModelView.getPtnKyoriStartDataList()) {
            ptnKyoriXData.setPtnKyoriXTextBackColor("");
            ptnKyoriXData.setPtnKyoriYTextBackColor("");
        }
    }
}
