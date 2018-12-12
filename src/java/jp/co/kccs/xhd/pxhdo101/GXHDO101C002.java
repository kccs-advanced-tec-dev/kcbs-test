/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
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
 * GXHDO101C002(PTN距離X)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanGXHDO101C002")
@SessionScoped
public class GXHDO101C002 implements Serializable {

    /**
     * PTN距離Xサブ画面用データ
     */
    private GXHDO101C002Model gxhdO101c002Model;

    /**
     * PTN距離Xサブ画面用データ(表示制御用)
     */
    private GXHDO101C002Model gxhdO101c002ModelView;

    /**
     * 初期表示時メッセージリスト
     */
    private List<String> initDispMsgList;

    /**
     * コンストラクタ
     */
    public GXHDO101C002() {
    }

    /**
     * PTN距離Xサブ画面用データ
     *
     * @return the gxhdO101c002Model
     */
    public GXHDO101C002Model getGxhdO101c002Model() {
        return gxhdO101c002Model;
    }

    /**
     * PTN距離Xサブ画面用データ
     *
     * @param gxhdO101c002Model the gxhdO101c002Model to set
     */
    public void setGxhdO101c002Model(GXHDO101C002Model gxhdO101c002Model) {
        this.gxhdO101c002Model = gxhdO101c002Model;
    }

    /**
     * PTN距離Xサブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c002ModelView
     */
    public GXHDO101C002Model getGxhdO101c002ModelView() {
        return gxhdO101c002ModelView;
    }

    /**
     ** PTN距離Xサブ画面用データ(表示制御用)
     *
     * @param gxhdO101c002ModelView the gxhdO101c002ModelView to set
     */
    public void setGxhdO101c002ModelView(GXHDO101C002Model gxhdO101c002ModelView) {
        this.gxhdO101c002ModelView = gxhdO101c002ModelView;
    }

    /**
     * 初期表示時メッセージリスト
     *
     * @return the initDispMsgList
     */
    public List<String> getInitDispMsgList() {
        return initDispMsgList;
    }

    /**
     * 初期表示時メッセージリスト
     *
     * @param initDispMsgList the initDispMsgList to set
     */
    public void setInitDispMsgList(List<String> initDispMsgList) {
        this.initDispMsgList = initDispMsgList;
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

        this.gxhdO101c002Model = this.gxhdO101c002ModelView;
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();

        for (GXHDO101C002Model.ptnKyoriXData ptnKyoriXData : this.gxhdO101c002ModelView.getPtnKyoriXDataList()) {
            if (StringUtil.isEmpty(ptnKyoriXData.getStartVal())) {
                setError(ptnKyoriXData, true, false, "XHD-000003", "スタート");
                return false;
            }
            if (StringUtil.isEmpty(ptnKyoriXData.getEndVal())) {
                setError(ptnKyoriXData, false, true, "XHD-000003", "エンド");
                return false;
            }
            ptnKyoriXData.setEndTextBackColor("");
        }

        return true;
    }

    /**
     * エラーセット
     *
     * @param ptnKyoriXData PTN距離Xデータ
     * @param isStartErr スタートエラー
     * @param isEndErr エンドエラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C002Model.ptnKyoriXData ptnKyoriXData, boolean isStartErr, boolean isEndErr, String errorId, Object... errParams) {

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
        for (GXHDO101C002Model.ptnKyoriXData ptnKyoriXData : this.gxhdO101c002ModelView.getPtnKyoriXDataList()) {
            ptnKyoriXData.setStartTextBackColor("");
            ptnKyoriXData.setEndTextBackColor("");
        }
    }

    /**
     * 初期表示メッセージ表示
     */
    public void showInitDispMessage() {
        if (this.initDispMsgList == null || this.initDispMsgList.isEmpty()) {
            return;
        }

        // 初期表示メッセージの設定        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        for (String dispMsg : this.initDispMsgList) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, dispMsg, null));
        }
    }
}
