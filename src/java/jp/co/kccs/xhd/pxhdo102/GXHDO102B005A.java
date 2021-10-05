/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jp.co.kccs.xhd.pxhdo901.GXHDO901BEX;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/08<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B005A(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/09/08
 */
@Named
@ViewScoped
public class GXHDO102B005A extends GXHDO901BEX {
    private String mainDefaultStyle = "";
    private String mainAutoStyle = "";
    private String mainDivStyle = "";
    /**
     * コンストラクタ
     */
    public GXHDO102B005A() {
    }

//<editor-fold defaultstate="collapsed" desc="#setter getter">
    /**
     * ﾒｲﾝDivのスタイル
     * @return the mainDivStyle
     */
    public String getMainDivStyle() {
        return mainDivStyle;
    }

    /**
     *  ﾒｲﾝDivのスタイル
     * @param mainDivStyle the mainDivStyle to set
     */
    public void setMainDivStyle(String mainDivStyle) {
        this.mainDivStyle = mainDivStyle;
    }
//</editor-fold>  
    /**
     * 画面起動時処理
     *
     * @param mainWidth 画面サイズ
     */
    public void init(String mainWidth) {

        this.setFormIds(new String[]{"GXHDO102B005A", "GXHDO102B005B", "GXHDO102B005C", "GXHDO102B005D","GXHDO102B005E", "GXHDO102B005F", "GXHDO102B005G", "GXHDO102B005H", "GXHDO102B005I"});

        //親の初期処理呼び出し
        super.init();

        this.mainDefaultStyle = "width:" + mainWidth + "px;margin-left:auto;margin-right:auto;";
        this.mainAutoStyle = "width:auto;" + "min-width:" + mainWidth + "px;";
        this.mainDivStyle = this.mainDefaultStyle;

    }
}