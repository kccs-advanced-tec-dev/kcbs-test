/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C005Model;
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
 * GXHDO101C005(印刷幅)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C005")
@SessionScoped
public class GXHDO101C005 implements Serializable {

    /**
     * 印刷幅サブ画面用データ
     */
    private GXHDO101C005Model gxhdO101c005Model;

    /**
     * 印刷幅サブ画面用データ(表示制御用)
     */
    private GXHDO101C005Model gxhdO101c005ModelView;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C005() {
    }

    /**
     * 印刷幅サブ画面用データ
     *
     * @return the gxhdO101c005Model
     */
    public GXHDO101C005Model getGxhdO101c005Model() {
        return gxhdO101c005Model;
    }

    /**
     * 印刷幅サブ画面用データ
     *
     * @param gxhdO101c005Model the gxhdO101c005Model to set
     */
    public void setGxhdO101c005Model(GXHDO101C005Model gxhdO101c005Model) {
        this.gxhdO101c005Model = gxhdO101c005Model;
    }

    /**
     * 印刷幅サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c005ModelView
     */
    public GXHDO101C005Model getGxhdO101c005ModelView() {
        return gxhdO101c005ModelView;
    }

    /**
     * 印刷幅サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c005ModelView the gxhdO101c005ModelView to set
     */
    public void setGxhdO101c005ModelView(GXHDO101C005Model gxhdO101c005ModelView) {
        this.gxhdO101c005ModelView = gxhdO101c005ModelView;
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

        this.gxhdO101c005Model = this.gxhdO101c005ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C005Model.PrintWidthData printWidthData : this.gxhdO101c005ModelView.getPrintWidthDataList()) {
            if (!StringUtil.isEmpty(printWidthData.getStartVal())) {

                if (!NumberUtil.isIntegerNumeric(printWidthData.getStartVal())) {
                    setError(printWidthData, "XHD-000008", "スタート");
                    return false;
                }

                if (4 < StringUtil.length(printWidthData.getStartVal())) {
                    setError(printWidthData, "XHD-000006", "スタート", "4");
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
    private void setError(GXHDO101C005Model.PrintWidthData makuatsuData, String errorId, Object... errParams) {

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
        for (GXHDO101C005Model.PrintWidthData printWidthData : this.gxhdO101c005ModelView.getPrintWidthDataList()) {
            printWidthData.setStartTextBackColor("");
        }
    }

}
