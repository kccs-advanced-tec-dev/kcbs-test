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
import jp.co.kccs.xhd.model.GXHDO101C008Model;
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
 * GXHDO101C008(ﾊﾟﾀｰﾝ間距離)
 *
 * @author KCSS K.Jo
 * @since  2019/01/08
 */
@ManagedBean(name = "beanGXHDO101C008")
@SessionScoped
public class GXHDO101C008 implements Serializable {

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     */
    private GXHDO101C008Model gxhdO101c008Model;

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     */
    private GXHDO101C008Model gxhdO101c008ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO101C008() {
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     *
     * @return the gxhdO101c008Model
     */
    public GXHDO101C008Model getGxhdO101c008Model() {
        return gxhdO101c008Model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ
     *
     * @param gxhdO101c008Model the gxhdO101c008Model to set
     */
    public void setGxhdO101c008Model(GXHDO101C008Model gxhdO101c008Model) {
        this.gxhdO101c008Model = gxhdO101c008Model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c008ModelView
     */
    public GXHDO101C008Model getGxhdO101c008ModelView() {
        return gxhdO101c008ModelView;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c008ModelView the gxhdO101c008ModelView to set
     */
    public void setGxhdO101c008ModelView(GXHDO101C008Model gxhdO101c008ModelView) {
        this.gxhdO101c008ModelView = gxhdO101c008ModelView;
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

        this.gxhdO101c008Model = this.gxhdO101c008ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C008Model.PrintWidthData printWidthData : this.gxhdO101c008ModelView.getPrintWidthDataList()) {
            
            if (!StringUtil.isEmpty(printWidthData.getStartVal())) {
                //型ﾁｪｯｸ(スタート)
                if (!NumberUtil.isNumeric(printWidthData.getStartVal())) {
                    setError(printWidthData, "XHD-000008", "スタート");
                    return false;
                }
                //桁数ﾁｪｯｸ(小数なし)(スタート)
                if (!NumberUtil.isValidDigits(new BigDecimal(printWidthData.getStartVal()), 4, 0)){
                    setError(printWidthData, "XHD-000006", "スタート", 4);
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
    private void setError(GXHDO101C008Model.PrintWidthData makuatsuData, String errorId, Object... errParams) {

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
        for (GXHDO101C008Model.PrintWidthData printWidthData : this.gxhdO101c008ModelView.getPrintWidthDataList()) {
            printWidthData.setStartTextBackColor("");
        }
    }

}
