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
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C006(剥離・画処NG)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C006")
@SessionScoped
public class GXHDO101C006 implements Serializable {

    /**
     * 剥離・画処NGサブ画面用データ
     */
    private GXHDO101C006Model gxhdO101c006Model;

    /**
     * 剥離・画処NGサブ画面用データ(表示制御用)
     */
    private GXHDO101C006Model gxhdO101c006ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C006() {
    }

    /**
     * 剥離・画処NGサブ画面用データ
     *
     * @return the gxhdO101c006Model
     */
    public GXHDO101C006Model getGxhdO101c006Model() {
        return gxhdO101c006Model;
    }

    /**
     * 剥離・画処NGサブ画面用データ
     *
     * @param gxhdO101c006Model the gxhdO101c006Model to set
     */
    public void setGxhdO101c006Model(GXHDO101C006Model gxhdO101c006Model) {
        this.gxhdO101c006Model = gxhdO101c006Model;
    }

    /**
     * 剥離・画処NGサブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c006ModelView
     */
    public GXHDO101C006Model getGxhdO101c006ModelView() {
        return gxhdO101c006ModelView;
    }

    /**
     ** 剥離・画処NGサブ画面用データ(表示制御用)
     *
     * @param gxhdO101c006ModelView the gxhdO101c006ModelView to set
     */
    public void setGxhdO101c006ModelView(GXHDO101C006Model gxhdO101c006ModelView) {
        this.gxhdO101c006ModelView = gxhdO101c006ModelView;
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        if (!checkOK()) {
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

        for (GXHDO101C006Model.PtnKyoriXData ptnKyoriXData : this.gxhdO101c006ModelView.getPtnKyoriXDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriXData.getStartVal())) {
                //型ﾁｪｯｸ(スタート)
                if (!NumberUtil.isNumeric(ptnKyoriXData.getStartVal())) {
                    setError(ptnKyoriXData, true, false, "XHD-000008", "スタート");
                    return false;
                }
                //桁数ﾁｪｯｸ(小数なし)(スタート)
                if (!NumberUtil.isValidDigits(new BigDecimal(ptnKyoriXData.getStartVal()), 3, 0)){
                    setError(ptnKyoriXData, true, false, "XHD-000006", "スタート", 3);
                    return false;
                }
            }
            
            if (!StringUtil.isEmpty(ptnKyoriXData.getEndVal())) {
                //型ﾁｪｯｸ(エンド)
                if (!NumberUtil.isNumeric(ptnKyoriXData.getEndVal())) {
                    setError(ptnKyoriXData, false, true, "XHD-000008", "エンド");
                    return false;
                }
                //桁数ﾁｪｯｸ(小数なし)(エンド)
                if (!NumberUtil.isValidDigits(new BigDecimal(ptnKyoriXData.getEndVal()), 3, 0)){
                    setError(ptnKyoriXData, false, true, "XHD-000006", "エンド", 3);
                    return false;
                }
                ptnKyoriXData.setEndTextBackColor("");
            }
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKyoriXData 剥離・画処NGデータ
     * @param isStartErr スタートエラー
     * @param isEndErr エンドエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C006Model.PtnKyoriXData ptnKyoriXData, boolean isStartErr, boolean isEndErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isStartErr) {
            ptnKyoriXData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // エンドエラー
        if (isEndErr) {
            ptnKyoriXData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C006Model.PtnKyoriXData ptnKyoriXData : this.gxhdO101c006ModelView.getPtnKyoriXDataList()) {
            ptnKyoriXData.setStartTextBackColor("");
            ptnKyoriXData.setEndTextBackColor("");
        }
    }
}
