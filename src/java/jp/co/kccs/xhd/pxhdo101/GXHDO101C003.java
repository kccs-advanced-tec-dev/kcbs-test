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
 * GXHDO101C003(PTN距離Y)
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
     * PTN距離Yサブ画面用データ
     *
     * @return the gxhdO101c003Model
     */
    public GXHDO101C003Model getGxhdO101c003Model() {
        return gxhdO101c003Model;
    }

    /**
     * PTN距離Yサブ画面用データ
     *
     * @param gxhdO101c003Model the gxhdO101c003Model to set
     */
    public void setGxhdO101c003Model(GXHDO101C003Model gxhdO101c003Model) {
        this.gxhdO101c003Model = gxhdO101c003Model;
    }

    /**
     * PTN距離Yサブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c003ModelView
     */
    public GXHDO101C003Model getGxhdO101c003ModelView() {
        return gxhdO101c003ModelView;
    }

    /**
     * PTN距離Yサブ画面用データ(表示制御用)
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

        for (GXHDO101C003Model.PtnKyoriYData ptnKyoriYData : this.gxhdO101c003ModelView.getPtnKyoriYDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriYData.getStartVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriYData.getStartVal()) || !NumberUtil.isNumeric(ptnKyoriYData.getStartVal())) {
                    setError(ptnKyoriYData, true, false, "XHD-000008", "スタート");
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriYData.getStartVal())) {
                    setError(ptnKyoriYData, true, false, "XHD-000006", "スタート", "3");
                    return false;
                }
            }

            if (!StringUtil.isEmpty(ptnKyoriYData.getEndVal())) {
                if (!NumberUtil.isIntegerNumeric(ptnKyoriYData.getEndVal()) || !NumberUtil.isNumeric(ptnKyoriYData.getEndVal())) {
                    setError(ptnKyoriYData, false, true, "XHD-000008", "エンド");
                    return false;
                }

                if (3 < StringUtil.length(ptnKyoriYData.getEndVal())) {
                    setError(ptnKyoriYData, false, true, "XHD-000006", "エンド", "3");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKyoriYData PTN距離Yデータ
     * @param isStartErr スタートエラー
     * @param isEndErr エンドエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C003Model.PtnKyoriYData ptnKyoriYData, boolean isStartErr, boolean isEndErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isStartErr) {
            ptnKyoriYData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // エンドエラー
        if (isEndErr) {
            ptnKyoriYData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C003Model.PtnKyoriYData ptnKyoriYData : this.gxhdO101c003ModelView.getPtnKyoriYDataList()) {
            ptnKyoriYData.setStartTextBackColor("");
            ptnKyoriYData.setEndTextBackColor("");
        }
    }
}
