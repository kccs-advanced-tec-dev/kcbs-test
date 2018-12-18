/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
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
 * GXHDO101C001(膜厚(SPS))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/13
 */
@ManagedBean(name = "beanGXHDO101C001")
@SessionScoped
public class GXHDO101C001 implements Serializable {

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
        if (!checkOK()) {
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
                setError(makuatsuData, true, false, "XHD-000003", "スタート");
                return false;
            }
            if (StringUtil.isEmpty(makuatsuData.getEndVal())) {
                setError(makuatsuData, false, true, "XHD-000003", "エンド");
                return false;
            }
            makuatsuData.setEndTextBackColor("");
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
