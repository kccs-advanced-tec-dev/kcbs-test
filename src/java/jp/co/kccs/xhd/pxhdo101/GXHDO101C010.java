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
 * @since 2019/01/09
 */
@ManagedBean(name = "beanGXHDO101C010")
@SessionScoped
public class GXHDO101C010 implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    private static final String DEFAULT_BACK_COLOR = "#E0FFFF";
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
            this.isFormError = true;

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

        // 被り量(μｍ)1
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData1(), "被り量(µm)1")) {
            return false;
        }

        // 被り量(μｍ)2
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData2(), "被り量(µm)2")) {
            return false;
        }

        // 被り量(μｍ)3
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData3(), "被り量(µm)3")) {
            return false;
        }

        // 被り量(μｍ)4
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData4(), "被り量(µm)4")) {
            return false;
        }

        // 被り量(μｍ)5
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData5(), "被り量(µm)5")) {
            return false;
        }

        // 被り量(μｍ)6
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData6(), "被り量(µm)6")) {
            return false;
        }

        // 被り量(μｍ)7
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData7(), "被り量(µm)7")) {
            return false;
        }

        // 被り量(μｍ)8
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData8(), "被り量(µm)8")) {
            return false;
        }

        // 被り量(μｍ)9
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData9(), "被り量(µm)9")) {
            return false;
        }

        // 被り量(μｍ)10
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData10(), "被り量(µm)10")) {
            return false;
        }

        // 被り量(μｍ)11
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData11(), "被り量(µm)11")) {
            return false;
        }

        // 被り量(μｍ)12
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData12(), "被り量(µm)12")) {
            return false;
        }

        // 被り量(μｍ)13
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData13(), "被り量(µm)13")) {
            return false;
        }

        // 被り量(μｍ)14
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData14(), "被り量(µm)14")) {
            return false;
        }

        // 被り量(μｍ)15
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData15(), "被り量(µm)15")) {
            return false;
        }

        // 被り量(μｍ)16
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData16(), "被り量(µm)16")) {
            return false;
        }

        // 被り量(μｍ)17
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData17(), "被り量(µm)17")) {
            return false;
        }

        // 被り量(μｍ)18
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData18(), "被り量(µm)18")) {
            return false;
        }

        // 被り量(μｍ)19
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData19(), "被り量(µm)19")) {
            return false;
        }

        // 被り量(μｍ)20
        if (!checkKaburiryou(this.gxhdO101c010ModelView.getKaburiryouData20(), "被り量(µm)20")) {
            return false;
        }

        return true;
    }

    /**
     * 被り量(µm)チェック処理
     *
     * @param kaburiryouData 被り量(µm)データ
     * @param itemName 項目名
     * @return チェック結果
     */
    private boolean checkKaburiryou(GXHDO101C010Model.KaburiryouData kaburiryouData, String itemName) {

        // 未入力チェック
        if (StringUtil.isEmpty(kaburiryouData.getValue())) {

            setError(kaburiryouData, "XHD-000027");
            return false;

        } else {

            // 型チェック
            if (!NumberUtil.isIntegerNumeric(kaburiryouData.getValue()) || !NumberUtil.isNumeric(kaburiryouData.getValue())) {
                setError(kaburiryouData, "XHD-000008", itemName);
                return false;
            }

            // 桁チェック
            if (3 < StringUtil.length(kaburiryouData.getValue())) {
                setError(kaburiryouData, "XHD-000006", itemName, "3");
                return false;
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
    private void setError(GXHDO101C010Model.KaburiryouData makuatsuData, String errorId, Object... errParams) {

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
        //各項目の色をリセット
        this.gxhdO101c010ModelView.getKaburiryouData1().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)1
        this.gxhdO101c010ModelView.getKaburiryouData2().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)2
        this.gxhdO101c010ModelView.getKaburiryouData3().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)3
        this.gxhdO101c010ModelView.getKaburiryouData4().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)4
        this.gxhdO101c010ModelView.getKaburiryouData5().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)5
        this.gxhdO101c010ModelView.getKaburiryouData6().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)6
        this.gxhdO101c010ModelView.getKaburiryouData7().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)7
        this.gxhdO101c010ModelView.getKaburiryouData8().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)8
        this.gxhdO101c010ModelView.getKaburiryouData9().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)9
        this.gxhdO101c010ModelView.getKaburiryouData10().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)10
        this.gxhdO101c010ModelView.getKaburiryouData11().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)11
        this.gxhdO101c010ModelView.getKaburiryouData12().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)12
        this.gxhdO101c010ModelView.getKaburiryouData13().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)13
        this.gxhdO101c010ModelView.getKaburiryouData14().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)14
        this.gxhdO101c010ModelView.getKaburiryouData15().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)15
        this.gxhdO101c010ModelView.getKaburiryouData16().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)16
        this.gxhdO101c010ModelView.getKaburiryouData17().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)17
        this.gxhdO101c010ModelView.getKaburiryouData18().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)18
        this.gxhdO101c010ModelView.getKaburiryouData19().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)19
        this.gxhdO101c010ModelView.getKaburiryouData20().setTextBackColor(DEFAULT_BACK_COLOR); //被り量(µm)20

    }

}
