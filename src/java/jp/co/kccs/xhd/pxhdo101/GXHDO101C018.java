/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jp.co.kccs.xhd.model.GXHDO101C018Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/11/23<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C018(電気特性・熱処理_サブ画面)
 *
 * @author 863 zhangjy
 * @since 2020/11/23
 */
@SessionScoped
@Named("beanGXHDO101C018")
public class GXHDO101C018 implements Serializable {
    
    /**
     * フォームエラー判定
     */
    private boolean isFormError;
    
    /**
     * 前工程WIP取込サブ画面用データ
     */
    private GXHDO101C018Model gxhdO101c018Model;

    /**
     * 前工程WIP取込サブ画面用データ(表示制御用)
     */
    private GXHDO101C018Model gxhdO101c018ModelView;
    
    /**
     * コンストラクタ
     */
    public GXHDO101C018() {
    }

    /**
     * フォームエラー判定
     * 
     * @return isFormError
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
     * 電気特性・熱処理_サブ画面用データ
     *
     * @return the gxhdO101c018Model
     */
    public GXHDO101C018Model getGxhdO101c018Model() {
        return gxhdO101c018Model;
    }

    /**
     * 電気特性・熱処理_サブ画面用データ
     *
     * @param gxhdO101c018Model the gxhdO101c018Model to set
     */
    public void setGxhdO101c018Model(GXHDO101C018Model gxhdO101c018Model) {
        this.gxhdO101c018Model = gxhdO101c018Model;
    }

    /**
     * 電気特性・熱処理_サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c018ModelView
     */
    public GXHDO101C018Model getGxhdO101c018ModelView() {
        return gxhdO101c018ModelView;
    }

    /**
     * 電気特性・熱処理_サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c018ModelView the gxhdO101c018ModelView to set
     */
    public void setGxhdO101c018ModelView(GXHDO101C018Model gxhdO101c018ModelView) {
        this.gxhdO101c018ModelView = gxhdO101c018ModelView;
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

        this.gxhdO101c018Model = this.gxhdO101c018ModelView;
    }
    
    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {
        
        // 背景色をクリア
        clearBackColor();
        
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki1DataList()) {
            if (!StringUtil.isEmpty(goukiData.getItemValue()) && !NumberUtils.isNumber(goukiData.getItemValue())) {
                setError(goukiData, "XHD-000008", "号機①" + goukiData.getItemName());
                return false;
            }
        }
        
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki2DataList()) {
            if (!StringUtil.isEmpty(goukiData.getItemValue()) && !NumberUtils.isNumber(goukiData.getItemValue())) {
                setError(goukiData, "XHD-000008", "号機②" + goukiData.getItemName());
                return false;
            }
        }
        
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki3DataList()) {
            if (!StringUtil.isEmpty(goukiData.getItemValue()) && !NumberUtils.isNumber(goukiData.getItemValue())) {
                setError(goukiData, "XHD-000008", "号機③" + goukiData.getItemName());
                return false;
            }
        }
        
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki4DataList()) {
            if (!StringUtil.isEmpty(goukiData.getItemValue()) && !NumberUtils.isNumber(goukiData.getItemValue())) {
                setError(goukiData, "XHD-000008", "号機④" + goukiData.getItemName());
                return false;
            }
        }
        return true;
    }
    
    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki1DataList()) {
            goukiData.setTextBackColor("");
        }
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki2DataList()) {
            goukiData.setTextBackColor("");
        }
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki3DataList()) {
            goukiData.setTextBackColor("");
        }
        for (GXHDO101C018Model.GoukiData goukiData : this.gxhdO101c018ModelView.getGouki4DataList()) {
            goukiData.setTextBackColor("");
        }
    }
    
    /**
     * エラーセット
     *
     * @param goukiData 号機データ
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C018Model.GoukiData goukiData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        goukiData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
    }
}
