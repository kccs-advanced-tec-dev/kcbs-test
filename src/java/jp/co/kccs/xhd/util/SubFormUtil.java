/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import javax.faces.context.FacesContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/19<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 子画面操作用に使用する関数群です。
 *
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
public class SubFormUtil {

    /**
     * 膜厚(SPS)画面ID
     */
    public static final String FORM_ID_GXHDO101C001 = "GXHDO101C001";
    /**
     * PTN距離X画面ID
     */
    public static final String FORM_ID_GXHDO101C002 = "GXHDO101C002";
    /**
     * PTN距離Y画面ID
     */
    public static final String FORM_ID_GXHDO101C003 = "GXHDO101C003";
    /**
     * 膜厚(RSUS)画面ID
     */
    public static final String FORM_ID_GXHDO101C004 = "GXHDO101C004";
    /**
     * 印刷幅画面ID
     */
    public static final String FORM_ID_GXHDO101C005 = "GXHDO101C005";
    /**
     * 初期表示メッセージ
     */
    public static final String FORM_ID_INIT_MESSAGE = "InitMessage";
    /**
     * 規格エラー
     */
    public static final String FORM_ID_KIKAKU_ERROR = "KikakuError";

    /**
     * コンストラクタ
     *
     */
    private SubFormUtil() {
    }

    /**
     * サブ画面のBean情報を取得
     *
     * @param formId フォームID
     * @return サブ画面情報
     */
    public static Object getSubFormBean(String formId) {

        Object returnBean = null;

        switch (formId) {
            // 膜厚(SPS)
            case "GXHDO101C001":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C001");
                break;

            // PTN距離X
            case "GXHDO101C002":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C002");
                break;

            // PTN距離Y
            case "GXHDO101C003":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C003");
                break;

            // 膜厚(RSUS)
            case "GXHDO101C004":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C004");
                break;

            // 印刷幅
            case "GXHDO101C005":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C005");

                break;
            // 初期表示メッセージ
            case "InitMessage":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanInitMessage");
                break;
            // 規格エラーダイアログ
            case "KikakuError":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanKikakuError");
                break;

            default:
                break;
        }
        return returnBean;
    }

}
