/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jp.co.kccs.xhd.pxhdo901.GXHDO901AEX;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 * ===============================================================================<br>
 * <br>
 * システム名    品質DB(コンデンサ)<br>
 * <br>
 * 変更日        2019/12/28<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      新規作成<br>
 * <br>
 * 変更日	2020/10/19<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B041A(電気特性・3端子4端子(製品情報))
 *
 * @author KCSS K.Jo
 * @since  2019/12/28
 */
@Named
@ViewScoped
public class GXHDO101B041A extends GXHDO901AEX {
    private String mainDefaultStyle = "";
    private String mainAutoStyle = "";
    private String mainDivStyle = "";

    /**
     * コンストラクタ
     */
    public GXHDO101B041A() {
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

        this.setFormIds(new String[]{"GXHDO101B041A", "GXHDO101B041B", "GXHDO101B041C", "GXHDO101B041D"});

        //親の初期処理呼び出し
        super.init();

        this.mainDefaultStyle = "width:" + mainWidth + "px;margin-left:auto;margin-right:auto;";
        this.mainAutoStyle = "width:auto;" + "min-width:" + mainWidth + "px;";
        this.mainDivStyle = this.mainDefaultStyle;

    }

    /**
     * タブ変更時処理
     * @param event 
     */
    public void onTabChange(TabChangeEvent event) {
        // 選択したタブよりごとにスタイルを切り替える。
        if ("tab2".equals(event.getTab().getId()) || "tab4".equals(event.getTab().getId())) {
            this.mainDivStyle = this.mainAutoStyle;
        } else {
            this.mainDivStyle = this.mainDefaultStyle;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", this.mainDivStyle);
    }

}
