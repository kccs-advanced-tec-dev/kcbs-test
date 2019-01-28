/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C004Model;
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
 * GXHDO101C004(膜厚(RSUS))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C004")
@SessionScoped
public class GXHDO101C004 implements Serializable {

    /**
     * 膜厚(RSUS)サブ画面用データ
     */
    private GXHDO101C004Model gxhdO101c004Model;

    /**
     * 膜厚(RSUS)サブ画面用データ(表示制御用)
     */
    private GXHDO101C004Model gxhdO101c004ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C004() {
    }

    /**
     * 膜厚(RSUS)サブ画面用データ
     *
     * @return the gxhdO101c004Model
     */
    public GXHDO101C004Model getGxhdO101c004Model() {
        return gxhdO101c004Model;
    }

    /**
     * 膜厚(RSUS)サブ画面用データ
     *
     * @param gxhdO101c004Model the gxhdO101c004Model to set
     */
    public void setGxhdO101c004Model(GXHDO101C004Model gxhdO101c004Model) {
        this.gxhdO101c004Model = gxhdO101c004Model;
    }

    /**
     * 膜厚(RSUS)サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c004ModelView
     */
    public GXHDO101C004Model getGxhdO101c004ModelView() {
        return gxhdO101c004ModelView;
    }

    /**
     * 膜厚(RSUS)サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c004ModelView the gxhdO101c004ModelView to set
     */
    public void setGxhdO101c004ModelView(GXHDO101C004Model gxhdO101c004ModelView) {
        this.gxhdO101c004ModelView = gxhdO101c004ModelView;
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

        this.gxhdO101c004Model = this.gxhdO101c004ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C004Model.MakuatsuData makuatsuData : this.gxhdO101c004ModelView.getMakuatsuDataList()) {
            if (!StringUtil.isEmpty(makuatsuData.getStartVal())) {

                if (!NumberUtil.isNumeric(makuatsuData.getStartVal())) {
                    setError(makuatsuData, "XHD-000008", "スタート");
                    return false;
                }

                BigDecimal decStart = new BigDecimal(makuatsuData.getStartVal());
                if (!NumberUtil.isValidDigits(decStart, 2, 3)) {
                    setError(makuatsuData, "XHD-000007", "スタート", "2", "3");
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
    private void setError(GXHDO101C004Model.MakuatsuData makuatsuData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C004Model.MakuatsuData makuatsuData : this.gxhdO101c004ModelView.getMakuatsuDataList()) {
            makuatsuData.setStartTextBackColor("");
        }
    }
}
