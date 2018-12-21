/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/19<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * KikakuError(規格外エラーダイアログ)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/19
 */
@ManagedBean(name = "beanKikakuError")
@SessionScoped
public class KikakuError implements Serializable {


    /**
     * 規格値エラー情報リスト
     */
    private List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList;


    /**
     * コンストラクタ
     */
    public KikakuError() {
    }

   
    
    /**
     * 規格値エラー情報リスト
     * @return the kikakuchiInputErrorInfoList
     */
    public List<KikakuchiInputErrorInfo> getKikakuchiInputErrorInfoList() {
        return kikakuchiInputErrorInfoList;
    }

    /**
     * 規格値エラー情報リスト
     * @param kikakuchiInputErrorInfoList the kikakuchiInputErrorInfoList to set
     */
    public void setKikakuchiInputErrorInfoList(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        this.kikakuchiInputErrorInfoList = kikakuchiInputErrorInfoList;
    }

}
