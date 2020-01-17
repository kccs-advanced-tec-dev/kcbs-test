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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/28<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B042A(電気特性・一般品(製品情報))
 *
 * @author 863 F.Zhang
 * @since 2019/12/28
 */
@Named
@ViewScoped
public class GXHDO101B042A extends GXHDO901AEX {
    private String mainDefaultStyle = "";
    private String mainAutoStyle = "";
    private String mainDivStyle = "";

    /**
     * コンストラクタ
     */
    public GXHDO101B042A() {
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

        this.setFormIds(new String[]{"GXHDO101B042A", "GXHDO101B042B", "GXHDO101B042C", "GXHDO101B042D"});

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
        if ("tab4".equals(event.getTab().getId())) {
            this.mainDivStyle = this.mainAutoStyle;
        } else {
            this.mainDivStyle = this.mainDefaultStyle;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", this.mainDivStyle);
    }

}
