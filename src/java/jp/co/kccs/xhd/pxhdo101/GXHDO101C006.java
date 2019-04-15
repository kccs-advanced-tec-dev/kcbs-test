/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C006Model;
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
 * 変更日	2019/03/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C006(剥離内容入力)
 *
 * @author KCSS K.Jo
 * @since  2019/03/05
 */
@ManagedBean(name = "beanGXHDO101C006")
@SessionScoped
public class GXHDO101C006 implements Serializable {

    /**
     * 剥離内容入力サブ画面用データ
     */
    private GXHDO101C006Model gxhdO101c006Model;

    /**
     * 剥離内容入力サブ画面用データ(表示制御用)
     */
    private GXHDO101C006Model gxhdO101c006ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C006() {
    }

    /**
     * 剥離内容入力サブ画面用データ
     *
     * @return the gxhdO101c006Model
     */
    public GXHDO101C006Model getGxhdO101c006Model() {
        return gxhdO101c006Model;
    }

    /**
     * 剥離内容入力サブ画面用データ
     *
     * @param gxhdO101c006Model the gxhdO101c006Model to set
     */
    public void setGxhdO101c006Model(GXHDO101C006Model gxhdO101c006Model) {
        this.gxhdO101c006Model = gxhdO101c006Model;
    }

    /**
     * 剥離内容入力サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c006ModelView
     */
    public GXHDO101C006Model getGxhdO101c006ModelView() {
        return gxhdO101c006ModelView;
    }

    /**
     ** 剥離内容入力サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c006ModelView the gxhdO101c006ModelView to set
     */
    public void setGxhdO101c006ModelView(GXHDO101C006Model gxhdO101c006ModelView) {
        this.gxhdO101c006ModelView = gxhdO101c006ModelView;
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

        this.gxhdO101c006Model = this.gxhdO101c006ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();
        int lineNo = 0;
        for (GXHDO101C006Model.HakuriInputData hakuriInputData : this.gxhdO101c006ModelView.getHakuriInputDataList()) {
            lineNo++;
            if (!StringUtil.isEmpty(hakuriInputData.getSetsuuVal())) {
                if (!NumberUtil.isIntegerNumeric(hakuriInputData.getSetsuuVal())) {
                    setError(hakuriInputData, true, false, "XHD-000008", "ｾｯﾄ数入力"+lineNo);
                    return false;
                }

                if (4 < StringUtil.length(hakuriInputData.getSetsuuVal())) {
                    setError(hakuriInputData, true, false, "XHD-000006", "ｾｯﾄ数入力"+lineNo, "4");
                    return false;
                }
            }

            if (!StringUtil.isEmpty(hakuriInputData.getBikouVal())) {
                if (20 < StringUtil.length(hakuriInputData.getBikouVal())) {
                    setError(hakuriInputData, false, true, "XHD-000006", "備考"+lineNo, "20");
                    return false;
                }
            }
            
            if (!StringUtil.isEmpty(hakuriInputData.getSetsuuVal()) && StringUtil.isEmpty(hakuriInputData.getBikouVal())) {
                    setError(hakuriInputData, false, true, "XHD-000003", "備考"+lineNo);
                    return false;
            }
            
            if (StringUtil.isEmpty(hakuriInputData.getSetsuuVal()) && !StringUtil.isEmpty(hakuriInputData.getBikouVal())) {
                    setError(hakuriInputData,  true, false, "XHD-000003", "ｾｯﾄ数入力"+lineNo);
                    return false;
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param hakuriInputData 剥離内容入力データ
     * @param isSetsuuErr ｾｯﾄ数入力エラー
     * @param isBikouErr 備考エラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C006Model.HakuriInputData hakuriInputData, boolean isSetsuuErr, boolean isBikouErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // ｾｯﾄ数入力エラー
        if (isSetsuuErr) {
            hakuriInputData.setSetsuuTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // 備考エラー
        if (isBikouErr) {
            hakuriInputData.setBikouTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C006Model.HakuriInputData hakuriInputData : this.gxhdO101c006ModelView.getHakuriInputDataList()) {
            hakuriInputData.setSetsuuTextBackColor("");
            hakuriInputData.setBikouTextBackColor("");
        }
    }
}
