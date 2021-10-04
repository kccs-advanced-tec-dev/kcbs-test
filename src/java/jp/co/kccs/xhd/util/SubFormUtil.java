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
 * 変更日	2020/03/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	画面制御の子画面を追加<br>
 * <br>
 * <br>
 * 変更日	2020/10/14<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	前工程WIP取込画面のロジックを追加<br>
 * <br>
 * 変更日	2020/11/23<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	電気特性・熱処理_サブ画面のロジックを追加<br>
 * <br>
 * 変更日	2021/07/09<br>
 * 計画書No	MB2106-DS017<br>
 * 変更者	kcss.gc<br>
 * 変更理由	履歴情報画面サブ画面追加<br>
 * <br>
 * 変更日	2021/07/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	kcss.gc<br>
 * 変更理由	情報表示メッセージ画面追加<br>
 * <br>
 * 変更日	2021/09/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	kcss.wxf<br>
 * 変更理由	ｶﾞﾗｽ作製・秤量入力画面追加<br>
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
     * 剥離・画処NG画面ID
     */
    public static final String FORM_ID_GXHDO101C006 = "GXHDO101C006";    
    /**
     * 電極膜厚画面ID
     */
    public static final String FORM_ID_GXHDO101C007 = "GXHDO101C007";    
    /**
     * ﾊﾟﾀｰﾝ間距離画面ID
     */
    public static final String FORM_ID_GXHDO101C008 = "GXHDO101C008";    
    /**
     * 合わせ(RZ)画面ID
     */
    public static final String FORM_ID_GXHDO101C009 = "GXHDO101C009";
    /**
     * 被り量（μｍ）画面ID
     */
    public static final String FORM_ID_GXHDO101C010 = "GXHDO101C010";
    /**
     * ﾛｯﾄ参照画面ID
     */
    public static final String FORM_ID_GXHDO101C012 = "GXHDO101C012";
    /**
     * 画面追加
     */
    public static final String FORM_ID_GXHDO101C017 = "GXHDO101C017";
    /**
     * 電気特性・熱処理_サブ画面
     */
    public static final String FORM_ID_GXHDO101C018 = "GXHDO101C018";
    /**
     * 前工程WIP取込
     */
    public static final String FORM_ID_GXHDO101C020 = "GXHDO101C020";
            
    /**
     * 初期表示メッセージ
     */
    public static final String FORM_ID_INIT_MESSAGE = "InitMessage";
    /**
     * 規格エラー
     */
    public static final String FORM_ID_KIKAKU_ERROR = "KikakuError";
    /**
     * 警告メッセージ
     */
    public static final String FORM_ID_INFO_MESSAGE = "InfoMessage";

    /**
     * 電気特性・履歴_サブ画面
     */
    public static final String FORM_ID_GXHDO201B040A = "GXHDO201B040A";
    
    /**
     * 情報表示メッセージ
     */
    public static final String FORM_ID_RESULT_MESSAGE = "ResultMessage";
    
    /**
     * ｶﾞﾗｽ作製・秤量入力画面ID
     */
    public static final String FORM_ID_GXHDO102C001 = "GXHDO102C001";
    
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

            // 剥離・画処NG
            case "GXHDO101C006":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C006");
                break;
                
            // 電極膜厚
            case "GXHDO101C007":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C007");
                break;
                
            // ﾊﾟﾀｰﾝ間距離
            case "GXHDO101C008":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C008");
                break;
                
            // 合わせ(RZ)
            case "GXHDO101C009":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C009");
                break;
                
            // 被り量（μｍ）
            case "GXHDO101C010":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C010");
                break;
                
            // ﾛｯﾄ参照
            case "GXHDO101C012":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C012");
                break;

            // 画面追加
            case "GXHDO101C017":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C017");
                break;

            // 電気特性・熱処理_サブ画面
            case "GXHDO101C018":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C018");
                break;
                
            // 前工程WIP取込画面
            case "GXHDO101C020":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO101C020");
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

            // 警告メッセージ
            case "InfoMessage":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanInfoMessage");
                break;
            // 電気特性・履歴_サブ画面
            case "GXHDO201B040A":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO201B040A");
                break;
             // 電気特性・履歴_サブ画面
            case "ResultMessage":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanResultMessage");
                break;
            // ｶﾞﾗｽ作製・秤量入力
            case "GXHDO102C001":
                returnBean = FacesContext.getCurrentInstance().
                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                                getELContext(), null, "beanGXHDO102C001");
                break;
            default:
                break;
        }
        return returnBean;
    }

}
