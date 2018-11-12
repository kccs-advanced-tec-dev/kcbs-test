/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/04/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * タブレット画面に関する情報を纏めたクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Named
@SessionScoped
//@Stateful
public class AdjustModels implements Serializable {
    private String uAgent;
    private String model;
    private String submodel;
    private Double fontSize = 20.0;
    private Double fontSize_5S = 10.5;
    private Double fontSize_4S = 11.0;
    private Double fontSize_SSS = 12.0;
    private Double fontSize_SSX = 13.5;
    private Double fontSize_SS = 16.0;
    private Double fontSize_S = 18.0;
    private Double fontSize_L = 24.0;
    private Double fontSize_Z = 28.0;
    private Double docSelectListfontSizeM = 22.8;
    private Double docSelectListfontSizeS = 18.87;
    private int docSelectListCount = 20;
    private int menuWidth = 720;
    private Double menuCaptionSize = 22.0;
    private int menuCaptionLeft = 260;
    private int docListWidth = 720;
    private int zoomHeight = 500;
    private int zoomWidth = 720;
    private int custompanel_1_height = 66;
    private int custompanel_2_height = 136;
    private int custompanel_3_height = 232;
    private int custompanel_2_width = 510;
    private String external = "external";
    private int custompanel_2b_width = 128;
    private int custompanel_2c_width = 222;
    //Insert Start[MB1703-DS019] D.Yanagida 2017.07.13
    private int meisaiBunshoNo_width = 18;//パーセント指定
    private int meisaiBunshoMei_width = 46;//パーセント指定
    private int meisaiFileMei_width = 14;//パーセント指定
    private int meisaiTorokuDate_width = 13;//パーセント指定
    private int meisaiKiansha_width = 9;//パーセント指定
    //Insert End[MB1703-DS019] D.Yanagida 2017.07.13
    private String docSelectCss;//文書閲覧CSS
    private Double menuFontSize = 32.0;
    
    /**
     * コンストラクタ
     * 
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public AdjustModels() {
        // user-agent
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        model = getModel.getModel();
        submodel = getModel.getSubmodel();
        
        if (model.equals("ipad") || model.equals("android")) {
            fontSize = 32.0;
            fontSize_5S = 11.5;
            fontSize_4S = 12.5;
            fontSize_SSS = 13.5;
            fontSize_SSX = 15.5;
            fontSize_SS = 16.0;
            fontSize_S = 23.0;
            fontSize_L = 36.0;
            fontSize_Z = 34.0;
            menuWidth = 740;
            menuCaptionSize = 20.0;
            menuCaptionLeft = 270;
            docListWidth = 740;
            //Delete Start[MB1703-DS019] D.Yanagida 2017.07.13
            //custompanel_1_height = 64;
            //custompanel_2_height = 150;
            //Delete End[MB1703-DS019] D.Yanagida 2017.07.13
            custompanel_2_width = 600;
            //Insert Start[MB1703-DS019] D.Yanagida 2017.07.13
            custompanel_1_height = 80;
            custompanel_2_height = 104;
            meisaiBunshoNo_width = 18;
            meisaiBunshoMei_width = 46;
            meisaiFileMei_width = 14;
            meisaiTorokuDate_width = 13;
            meisaiKiansha_width = 9;
            //Insert End[MB1703-DS019] D.Yanagida 2017.07.13
            
            if ("android".equals(model)) {
                custompanel_3_height = 240;
                docSelectCss = "/DocumentServer/secure/resources/css/docselect.css";
            } else if ("ipad".equals(model)) {
                //Delete Start[MB1703-DS019] D.Yanagida 2017.07.13
                //custompanel_1_height = 74;
                //custompanel_2_height = 210;
                //Delete End[MB1703-DS019] D.Yanagida 2017.07.13
                custompanel_3_height = 300;
                //Insert Start[MB1703-DS019] D.Yanagida 2017.07.13
                custompanel_1_height = 100;
                custompanel_2_height = 108;
                meisaiBunshoNo_width = 18;
                meisaiBunshoMei_width = 46;
                meisaiFileMei_width = 14;
                meisaiTorokuDate_width = 13;
                meisaiKiansha_width = 9;
                //Insert End[MB1703-DS019] D.Yanagida 2017.07.13
                docSelectCss = "/DocumentServer/secure/resources/css/docselectIpad.css";
            }
            
            if (submodel != null) {
                if (submodel.equals("PC-TE508BA")) {
                    fontSize = 20.0;
                    fontSize_S = 14.0;
                    menuWidth = 540;
                    menuCaptionSize = 18.0;
                    menuCaptionLeft = 165;
                    docListWidth = 540;
                } else if (submodel.equals("Nexus 7")) {
                    fontSize = 20.0;
                    fontSize_S = 14.0;
                    menuWidth = 540;
                    menuCaptionSize = 18.0;
                    menuCaptionLeft = 165;
                    docListWidth = 540;
                } else if ("HuaweiMediaPad".equals(submodel)) {
                    // 文書閲覧
                    docSelectListfontSizeM = 18.7;
                    docSelectListfontSizeS = 11.5;
                    docSelectCss = "/DocumentServer/secure/resources/css/docselectHuaweiMediaPad.css";
                    fontSize_Z = 24.0;
                    zoomWidth = 550;
                    zoomHeight = 400;
                    docSelectListCount = 15;                      
                    menuFontSize = 23.0;
                    menuWidth = 590;
                }
            }
            //external = "webView";
        } else if (model.equals("linux_chrome")) {
            menuCaptionSize = 20.0;
            zoomHeight = 440;
        } else if (model.equals("linux_firefox")) {
            menuCaptionSize = 20.0;
            zoomHeight = 440;
        } else if (model.equals("ie11")) {
            custompanel_2_height = 126;
        } else if (model.equals("chrome")) {
            custompanel_2_height = 116;
            custompanel_2_width = 480;
            custompanel_2c_width = 252;
        } else if (model.equals("firefox")) {
            custompanel_2_height = 130;
            custompanel_2_width = 480;
            custompanel_2c_width = 252;
        } 
        
}

    /**
     * モデルを取得します。
     * 
     * @return モデル
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getModel() {
        return model;
    }
    
    /**
     * メニュー画面の幅を取得します。
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMenuWidth() {
        return menuWidth;
    }

    /**
     * メニューキャプションのサイズを取得します。
     * 
     * @return サイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getMenuCaptionSize() {
        return menuCaptionSize;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize() {
        return fontSize;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_S() {
        return fontSize_S;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_SS() {
        return fontSize_SS;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_SSX() {
        return fontSize_SSX;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @param fontSize_SSX サイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setFontSize_SSX(Double fontSize_SSX) {
        this.fontSize_SSX = fontSize_SSX;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return サイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_SSS() {
        return fontSize_SSS;
    }

    /**
     * フォントサイズを設定します。
     * 
     * @param fontSize_SSS フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setFontSize_SSS(Double fontSize_SSS) {
        this.fontSize_SSS = fontSize_SSS;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_4S() {
        return fontSize_4S;
    }

    /**
     * フォントサイズを設定します。
     * 
     * @param fontSize_4S フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setFontSize_4S(Double fontSize_4S) {
        this.fontSize_4S = fontSize_4S;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_5S() {
        return fontSize_5S;
    }

    /**
     * フォントサイズを設定します。
     * 
     * @param fontSize_5S フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setFontSize_5S(Double fontSize_5S) {
        this.fontSize_5S = fontSize_5S;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_L() {
        return fontSize_L;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize_Z() {
        return fontSize_Z;
    }

    /**
     * メニューキャプションを取得します。
     * 
     * @return メニューキャプション
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMenuCaptionLeft() {
        return menuCaptionLeft;
    }
    
    /**
     * 文書明細リストの幅を取得します。
     * 
     * @return メニューキャプション
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getDocListWidth() {
        return docListWidth;
    }

    /**
     * Android操作で使用する値を取得します。
     * 
     * @return external
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getExternal() {
        return external;
    }

    /**
     * パネル幅を取得します。
     * 
     * @return パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_2b_width() {
        return custompanel_2b_width;
    }

    /**
     * パネル幅を設定します。
     * 
     * @param custompanel_2b_width パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_2b_width(int custompanel_2b_width) {
        this.custompanel_2b_width = custompanel_2b_width;
    }

    /**
     * パネル幅を取得します。
     * 
     * @return パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_2c_width() {
        return custompanel_2c_width;
    }

    /**
     * パネル幅を設定します。
     * 
     * @param custompanel_2c_width パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_2c_width(int custompanel_2c_width) {
        this.custompanel_2c_width = custompanel_2c_width;
    }

    /**
     * サブモデルを取得します。
     * 
     * @return パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getSubmodel() {
        return submodel;
    }

    /**
     * ズーム画面の高さを取得します。
     * 
     * @return 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getZoomHeight() {
        return zoomHeight;
    }

    /**
     * ズーム画面の幅を取得します。
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getZoomWidth() {
        return zoomWidth;
    }

    /**
     * パネルの高さを取得します。
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_1_height() {
        return custompanel_1_height;
    }

    /**
     * パネルの高さを設定します。
     * 
     * @param custompanel_1_height 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_1_height(int custompanel_1_height) {
        this.custompanel_1_height = custompanel_1_height;
    }

    /**
     * パネルの高さを取得します。
     * 
     * @return 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_2_height() {
        return custompanel_2_height;
    }

    /**
     * パネルの高さを設定します。
     * 
     * @param custompanel_2_height 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_2_height(int custompanel_2_height) {
        this.custompanel_2_height = custompanel_2_height;
    }

    /**
     * パネルの高さを取得します。
     * 
     * @return 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_3_height() {
        return custompanel_3_height;
    }

    /**
     * パネルの高さを設定します。
     * 
     * @param custompanel_3_height 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_3_height(int custompanel_3_height) {
        this.custompanel_3_height = custompanel_3_height;
    }

    /**
     * パネルの高さを取得します。
     * 
     * @return 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getCustompanel_2_width() {
        return custompanel_2_width;
    }

    /**
     * パネルの高さを設定します。
     * 
     * @param custompanel_2_width 高さ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setCustompanel_2_width(int custompanel_2_width) {
        this.custompanel_2_width = custompanel_2_width;
    }
    
    //Insert Start[MB1703-DS019] D.Yanagida 2017.07.13
    /**
     * 文書閲覧明細(文書No)の幅を取得します。(パーセント指定)
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMeisaiBunshoNo_width() {
        return meisaiBunshoNo_width;
    }

    /**
     * 文書閲覧明細(文書No)の幅を設定します。(パーセント指定)
     * 
     * @param meisaiBunshoNo_width 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMeisaiBunshoNo_width(int meisaiBunshoNo_width) {
        this.meisaiBunshoNo_width = meisaiBunshoNo_width;
    }
    
    /**
     * 文書閲覧明細(文書名)の幅を取得します。(パーセント指定)
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMeisaiBunshoMei_width() {
        return meisaiBunshoMei_width;
    }

    /**
     * 文書閲覧明細(文書名)の幅を設定します。(パーセント指定)
     * 
     * @param meisaiBunshoMei_width 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMeisaiBunshoMei_width(int meisaiBunshoMei_width) {
        this.meisaiBunshoMei_width = meisaiBunshoMei_width;
    }
    
    /**
     * 文書閲覧明細(ファイル名)の幅を取得します。(パーセント指定)
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMeisaiFileMei_width() {
        return meisaiFileMei_width;
    }

    /**
     * 文書閲覧明細(ファイル名)の幅を設定します。(パーセント指定)
     * 
     * @param meisaiFileMei_width 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMeisaiFileMei_width(int meisaiFileMei_width) {
        this.meisaiFileMei_width = meisaiFileMei_width;
    }
    
    /**
     * 文書閲覧明細(登録日)の幅を取得します。(パーセント指定)
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMeisaiTorokuDate_width() {
        return meisaiTorokuDate_width;
    }

    /**
     * 文書閲覧明細(登録日)の幅を設定します。(パーセント指定)
     * 
     * @param meisaiTorokuDate_width 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMeisaiTorokuDate_width(int meisaiTorokuDate_width) {
        this.meisaiTorokuDate_width = meisaiTorokuDate_width;
    }

    /**
     * 文書閲覧明細(起案者)の幅を取得します。(パーセント指定)
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMeisaiKiansha_width() {
        return meisaiKiansha_width;
    }

    /**
     * 文書閲覧明細(起案者)の幅を設定します。(パーセント指定)
     * 
     * @param meisaiKiansha_width 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMeisaiKiansha_width(int meisaiKiansha_width) {
        this.meisaiKiansha_width = meisaiKiansha_width;
    }
    //Insert End[MB1703-DS019] D.Yanagida 2017.07.13
    
    
    /**
     * 文書閲覧明細のフォントサイズMを取得します。
     * 
     * @return フォントサイズM
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getDocSelectListfontSizeM() {
        return docSelectListfontSizeM;
    }
    
    /**
     * 文書閲覧明細のフォントサイズSを取得します。
     * 
     * @return フォントサイズS
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getDocSelectListfontSizeS() {
        return docSelectListfontSizeS;
    }
    
    /**
     * 文書閲覧明細の件数を取得します。
     * 
     * @return 件数
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getDocSelectListCount() {
        return docSelectListCount;
    }
    
    /**
     * 文書閲覧で参照するCSSを取得します。
     * 
     * @return CSS
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getDocSelectCss() {
        return this.docSelectCss;
    }
    
    /**
     * メニューフォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getMenuFontSize() {
        return menuFontSize;
    }
}
