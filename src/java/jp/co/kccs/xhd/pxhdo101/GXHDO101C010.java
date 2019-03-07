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
import jp.co.kccs.xhd.model.GXHDO101C010Model;
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
 * 変更日	2019/01/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C010(被り量（μｍ）)
 *
 * @author KCSS K.Jo
 * @since  2019/01/09
 */
@ManagedBean(name = "beanGXHDO101C010")
@SessionScoped
public class GXHDO101C010 implements Serializable {

    /**
     * 被り量（μｍ）サブ画面用データ
     */
    private GXHDO101C010Model gxhdO101c010Model;

    /**
     * 被り量（μｍ）サブ画面用データ(表示制御用)
     */
    private GXHDO101C010Model gxhdO101c010ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C010() {
    }

    /**
     * 被り量（μｍ）サブ画面用データ
     *
     * @return the gxhdO101c010Model
     */
    public GXHDO101C010Model getGxhdO101c010Model() {
        return gxhdO101c010Model;
    }

    /**
     * 被り量（μｍ）サブ画面用データ
     *
     * @param gxhdO101c010Model the gxhdO101c010Model to set
     */
    public void setGxhdO101c010Model(GXHDO101C010Model gxhdO101c010Model) {
        this.gxhdO101c010Model = gxhdO101c010Model;
    }

    /**
     * 被り量（μｍ）サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c010ModelView
     */
    public GXHDO101C010Model getGxhdO101c010ModelView() {
        return gxhdO101c010ModelView;
    }

    /**
     * 被り量（μｍ）サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c010ModelView the gxhdO101c010ModelView to set
     */
    public void setGxhdO101c010ModelView(GXHDO101C010Model gxhdO101c010ModelView) {
        this.gxhdO101c010ModelView = gxhdO101c010ModelView;
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

        this.gxhdO101c010Model = this.gxhdO101c010ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C010Model.PrintWidthData printWidthData : this.gxhdO101c010ModelView.getPrintWidthDataList()) {
            
            if (!StringUtil.isEmpty(printWidthData.getStartVal())) {
                //型ﾁｪｯｸ(スタート)
                if (!NumberUtil.isNumeric(printWidthData.getStartVal())) {
                    setError(printWidthData, "XHD-000008", "スタート");
                    return false;
                }
                //桁数ﾁｪｯｸ(小数なし)(スタート)
                if (!NumberUtil.isValidDigits(new BigDecimal(printWidthData.getStartVal()), 2, 3)){
                    setError(printWidthData, "XHD-000007", "スタート", 2,3);
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
    private void setError(GXHDO101C010Model.PrintWidthData makuatsuData, String errorId, Object... errParams) {

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
        for (GXHDO101C010Model.PrintWidthData printWidthData : this.gxhdO101c010ModelView.getPrintWidthDataList()) {
            printWidthData.setStartTextBackColor("");
        }
    }

}
