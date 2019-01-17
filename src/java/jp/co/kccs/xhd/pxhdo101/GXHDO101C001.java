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
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
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
 * GXHDO101C001(膜厚(SPS))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/13
 */
@ManagedBean(name = "beanGXHDO101C001")
@SessionScoped
public class GXHDO101C001 implements Serializable {

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
     * 膜厚(SPS)サブ画面用データ
     */
    private GXHDO101C001Model gxhdO101c001Model;

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     */
    private GXHDO101C001Model gxhdO101c001ModelView;


    /**
     * コンストラクタ
     */
    public GXHDO101C001() {
    }

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * フォームエラー判定
     * @return the isFormError
     */
    public boolean isIsFormError() {
        return isFormError;
    }

    /**
     * フォームエラー判定
     * @param isFormError the isFormError to set
     */
    public void setIsFormError(boolean isFormError) {
        this.isFormError = isFormError;
    }

    /**
     * 膜厚(SPS)サブ画面用データ
     *
     * @return the gxhdO101c001Model
     */
    public GXHDO101C001Model getGxhdO101c001Model() {
        return gxhdO101c001Model;
    }

    /**
     * 膜厚(SPS)サブ画面用データ
     *
     * @param gxhdO101c001Model the gxhdO101c001Model to set
     */
    public void setGxhdO101c001Model(GXHDO101C001Model gxhdO101c001Model) {
        this.gxhdO101c001Model = gxhdO101c001Model;
    }

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c001ModelView
     */
    public GXHDO101C001Model getGxhdO101c001ModelView() {
        return gxhdO101c001ModelView;
    }

    /**
     * 膜厚(SPS)サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c001ModelView the gxhdO101c001ModelView to set
     */
    public void setGxhdO101c001ModelView(GXHDO101C001Model gxhdO101c001ModelView) {
        this.gxhdO101c001ModelView = gxhdO101c001ModelView;
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

        this.gxhdO101c001Model = this.gxhdO101c001ModelView;
    }
    
  
    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C001Model.MakuatsuData makuatsuData : this.gxhdO101c001ModelView.getMakuatsuDataList()) {
            if (StringUtil.isEmpty(makuatsuData.getStartVal())) {
                setError(makuatsuData, true, false, "XHD-000027", "");
                return false;
            } else {
                if (!NumberUtil.isNumeric(makuatsuData.getStartVal())) {
                    setError(makuatsuData, true, false, "XHD-000008", "スタート");
                    return false;
                }

                BigDecimal decStart = new BigDecimal(makuatsuData.getStartVal());
                if (!NumberUtil.isValidDigits(decStart, 2, 3)) {
                    setError(makuatsuData, true, false, "XHD-000007", "スタート", "2", "3");
                    return false;
                }
            }

            if (StringUtil.isEmpty(makuatsuData.getEndVal())) {
                setError(makuatsuData, false, true, "XHD-000027", "");
                return false;
            } else {
                if (!NumberUtil.isNumeric(makuatsuData.getEndVal())) {
                    setError(makuatsuData, false, true, "XHD-000008", "エンド");
                    return false;
                }

                BigDecimal decEnd = new BigDecimal(makuatsuData.getEndVal());
                if (!NumberUtil.isValidDigits(decEnd, 2, 3)) {
                    setError(makuatsuData, false, true, "XHD-000007", "エンド", "2", "3");
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
    private void setError(GXHDO101C001Model.MakuatsuData makuatsuData, boolean isStartErr, boolean isEndErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isStartErr) {
            makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }

        // エンドエラー
        if (isEndErr) {
            makuatsuData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C001Model.MakuatsuData makuatsuData : this.gxhdO101c001ModelView.getMakuatsuDataList()) {
            makuatsuData.setStartTextBackColor("");
            makuatsuData.setEndTextBackColor("");
        }
    }

}
