/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C023Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2022/06/20<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C023(ｽﾞﾚ値サブ画面)
 *
 * @author KCSS wxf
 * @since 2022/06/20
 */
@ManagedBean(name = "beanGXHDO101C023")
@SessionScoped
public class GXHDO101C023 implements Serializable {

    /**
     * ｽﾞﾚ値サブ画面用データ
     */
    private GXHDO101C023Model gxhdO101c023Model;

    /**
     * ｽﾞﾚ値サブ画面用データ(表示制御用)
     */
    private GXHDO101C023Model gxhdO101c023ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C023() {
    }

    /**
     * ｽﾞﾚ値サブ画面用データ
     *
     * @return the gxhdO101c023Model
     */
    public GXHDO101C023Model getGxhdO101c023Model() {
        return gxhdO101c023Model;
    }

    /**
     * ｽﾞﾚ値サブ画面用データ
     *
     * @param gxhdO101c023Model the gxhdO101c023Model to set
     */
    public void setGxhdO101c023Model(GXHDO101C023Model gxhdO101c023Model) {
        this.gxhdO101c023Model = gxhdO101c023Model;
    }

    /**
     * ｽﾞﾚ値サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c023ModelView
     */
    public GXHDO101C023Model getGxhdO101c023ModelView() {
        return gxhdO101c023ModelView;
    }

    /**
     * ｽﾞﾚ値サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c023ModelView the gxhdO101c023ModelView to set
     */
    public void setGxhdO101c023ModelView(GXHDO101C023Model gxhdO101c023ModelView) {
        this.gxhdO101c023ModelView = gxhdO101c023ModelView;
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

        this.gxhdO101c023Model = this.gxhdO101c023ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C023Model.ZurechiData zurechiData : this.gxhdO101c023ModelView.getZurechiInputDataList()) {
            if (!StringUtil.isEmpty(zurechiData.getZurechiVal())) {

                if (!NumberUtil.isIntegerNumeric(zurechiData.getZurechiVal()) || !NumberUtil.isNumeric(zurechiData.getZurechiVal())) {
                    setError(zurechiData, "XHD-000008", "ｽﾞﾚ量" + zurechiData.getZurechiIndex());
                    return false;
                }

                if (9 < StringUtil.length(zurechiData.getZurechiVal())) {
                    setError(zurechiData, "XHD-000006", "ｽﾞﾚ量" + zurechiData.getZurechiIndex(), "9");
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
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C023Model.ZurechiData makuatsuData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        makuatsuData.setZurechiTextBackColor(ErrUtil.ERR_BACK_COLOR);

    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C023Model.ZurechiData zurechiData : this.gxhdO101c023ModelView.getZurechiInputDataList()) {
            zurechiData.setZurechiTextBackColor("");
        }
    }

}
