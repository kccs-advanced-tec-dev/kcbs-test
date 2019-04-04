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
import jp.co.kccs.xhd.model.GXHDO101C009Model;
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
 * 変更日	2019/01/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C009(合わせ(RZ))
 *
 * @author KCSS K.Jo
 * @since  2019/01/08
 */
@ManagedBean(name = "beanGXHDO101C009")
@SessionScoped
public class GXHDO101C009 implements Serializable {

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
     * 合わせ(RZ)サブ画面用データ
     */
    private GXHDO101C009Model gxhdO101c009Model;

    /**
     * 合わせ(RZ)サブ画面用データ(表示制御用)
     */
    private GXHDO101C009Model gxhdO101c009ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C009() {
    }
    
    
    /**
     * 工場コード
     * 
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場コード
     * 
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ロットNo
     * 
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ロットNo
     * 
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * 
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * 
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
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
     * 合わせ(RZ)サブ画面用データ
     *
     * @return the gxhdO101c009Model
     */
    public GXHDO101C009Model getGxhdO101c009Model() {
        return gxhdO101c009Model;
    }

    /**
     * 合わせ(RZ)サブ画面用データ
     *
     * @param gxhdO101c009Model the gxhdO101c009Model to set
     */
    public void setGxhdO101c009Model(GXHDO101C009Model gxhdO101c009Model) {
        this.gxhdO101c009Model = gxhdO101c009Model;
    }

    /**
     * 合わせ(RZ)サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c009ModelView
     */
    public GXHDO101C009Model getGxhdO101c009ModelView() {
        return gxhdO101c009ModelView;
    }

    /**
     * 合わせ(RZ)サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c009ModelView the gxhdO101c009ModelView to set
     */
    public void setGxhdO101c009ModelView(GXHDO101C009Model gxhdO101c009ModelView) {
        this.gxhdO101c009ModelView = gxhdO101c009ModelView;
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

        this.gxhdO101c009Model = this.gxhdO101c009ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        // 6～9にデータが入力されているかどうか確認
        int index = 0;
        boolean hasInputAfter6 = false;
        for (GXHDO101C009Model.AwaseRzData awaseRzData : this.gxhdO101c009ModelView.getAwaseRzDataList()) {
            index++;
            if (6 <= index && !StringUtil.isEmpty(awaseRzData.getValue())) {
                hasInputAfter6 = true;
                break;
            }
        }

        int indexMain = 0;
        for (GXHDO101C009Model.AwaseRzData awaseRzData : this.gxhdO101c009ModelView.getAwaseRzDataList()) {
            indexMain++;
            if (StringUtil.isEmpty(awaseRzData.getValue())) {
                if (indexMain <= 5) {
                    //1～5は未入力はエラー
                    setError(awaseRzData, "XHD-000027");
                    return false;
                } else if (hasInputAfter6) {
                    //6～9はいずれかが入力されている場合、未入力はエラー
                    setError(awaseRzData, "XHD-000027");
                    return false;
                }
            } else {
                //型ﾁｪｯｸ
                if (!NumberUtil.isNumeric(awaseRzData.getValue())) {
                    setError(awaseRzData, "XHD-000008", "合わせ(RZ)" + awaseRzData.getAwaseRz());
                    return false;
                }
                //桁数ﾁｪｯｸ(小数あり)(スタート)
                if (!NumberUtil.isValidDigits(new BigDecimal(awaseRzData.getValue()), 1, 3)) {
                    setError(awaseRzData, "XHD-000007", "合わせ(RZ)" + awaseRzData.getAwaseRz(), 1, 3);
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
    private void setError(GXHDO101C009Model.AwaseRzData makuatsuData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        makuatsuData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);

    }

    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C009Model.AwaseRzData printWidthData : this.gxhdO101c009ModelView.getAwaseRzDataList()) {
            printWidthData.setTextBackColor("");
        }
    }

}
