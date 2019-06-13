/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B014・GXHDO101B015(焼成・Air脱脂|窒素脱脂)ロジック定数
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/06
 */
public class GXHDO101B014Const {

    public GXHDO101B014Const(String formId) {
        // 画面IDがGXHDO101B015(窒素脱脂)の場合、IDを切り替え
        if ("GXHDO101B015".equals(formId)) {
            this.lotno = "sr_yobikan_tisso_lotno";
            this.kcpno = "sr_yobikan_tisso_kcpno";
            this.kyakusaki = "sr_yobikan_tisso_kyakusaki";
            this.lotKubun = "sr_yobikan_tisso_lot_kubun";
            this.owner = "sr_yobikan_tisso_owner";
            this.siji = "sr_yobikan_tisso_siji";
            this.ukeireSettaMaisu = "sr_yobikan_tisso_ukeire_setta_maisu";
            this.goki = "sr_yobikan_tisso_goki";
            this.programNo = "sr_yobikan_tisso_program_no";
            this.maxOndo = "sr_yobikan_tisso_max_ondo";
            this.keepTime = "sr_yobikan_tisso_keep_time";
            this.totalTime = "sr_yobikan_tisso_total_time";
            this.tounyuSettaMaisu = "sr_yobikan_tisso_tounyu_setta_maisu";
            this.kaishiDay = "sr_yobikan_tisso_kaishi_day";
            this.kaishiTime = "sr_yobikan_tisso_kaishi_time";
            this.kaishiTantousya = "sr_yobikan_tisso_kaishi_tantousya";
            this.kaishiKakuninsya = "sr_yobikan_tisso_kaishi_kakuninsya";
            this.shuryouDay = "sr_yobikan_tisso_shuryou_day";
            this.shuryouTime = "sr_yobikan_tisso_shuryou_time";
            this.shuryouTantousya = "sr_yobikan_tisso_shuryou_tantousya";
            this.kaishuSettaMaisu = "sr_yobikan_tisso_kaishu_setta_maisu";
            this.bikou1 = "sr_yobikan_tisso_bikou1";
            this.bikou2 = "sr_yobikan_tisso_bikou2";
            this.btnCopyEdabanTop = "sr_yobikan_tisso_copy_edaban_Top";
            this.btnStartdatetimeTop = "sr_yobikan_tisso_startdatetime_Top";
            this.btnEnddatetimeTop = "sr_yobikan_tisso_enddatetime_Top";
            this.btnKariTourokuTop = "sr_yobikan_tisso_kari_touroku_Top";
            this.btnInsertTop = "sr_yobikan_tisso_insert_Top";
            this.btnUpdateTop = "sr_yobikan_tisso_update_Top";
            this.btnDeleteTop = "sr_yobikan_tisso_delete_Top";
            this.btnCopyEdabanBottom = "sr_yobikan_tisso_copy_edaban_Bottom";
            this.btnStartdatetimeBottom = "sr_yobikan_tisso_startdatetime_Bottom";
            this.btnEnddatetimeBottom = "sr_yobikan_tisso_enddatetime_Bottom";
            this.btnKariTourokuBottom = "sr_yobikan_tisso_kari_touroku_Bottom";
            this.btnInsertBottom = "sr_yobikan_tisso_insert_Bottom";
            this.btnUpdateBottom = "sr_yobikan_tisso_update_Bottom";
            this.btnDeleteBottom = "sr_yobikan_tisso_delete_Bottom";
            this.userAuthUpdateParam = "sr_yobikan_tisso_update_button";
            this.userAuthDeleteParam = "sr_yobikan_tisso_delete_button";
        }
    }

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "sr_yobikan_air_lotno";

    /**
     * kcpno
     */
    private String kcpno = "sr_yobikan_air_kcpno";

    /**
     * 客先
     */
    private String kyakusaki = "sr_yobikan_air_kyakusaki";

    /**
     * ﾛｯﾄ区分
     */
    private String lotKubun = "sr_yobikan_air_lot_kubun";

    /**
     * ｵｰﾅｰ
     */
    private String owner = "sr_yobikan_air_owner";

    /**
     * 指示
     */
    private String siji = "sr_yobikan_air_siji";

    /**
     * 受入ｾｯﾀ枚数
     */
    private String ukeireSettaMaisu = "sr_yobikan_air_ukeire_setta_maisu";

    /**
     * 号機
     */
    private String goki = "sr_yobikan_air_goki";

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo．
     */
    private String programNo = "sr_yobikan_air_program_no";

    /**
     * 最高温度
     */
    private String maxOndo = "sr_yobikan_air_max_ondo";

    /**
     * ｷｰﾌﾟ時間
     */
    private String keepTime = "sr_yobikan_air_keep_time";

    /**
     * 総時間
     */
    private String totalTime = "sr_yobikan_air_total_time";

    /**
     * 投入ｾｯﾀ枚数
     */
    private String tounyuSettaMaisu = "sr_yobikan_air_tounyu_setta_maisu";

    /**
     * 開始日
     */
    private String kaishiDay = "sr_yobikan_air_kaishi_day";

    /**
     * 開始時間
     */
    private String kaishiTime = "sr_yobikan_air_kaishi_time";

    /**
     * 開始担当者
     */
    private String kaishiTantousya = "sr_yobikan_air_kaishi_tantousya";

    /**
     * 開始確認者
     */
    private String kaishiKakuninsya = "sr_yobikan_air_kaishi_kakuninsya";

    /**
     * 終了日
     */
    private String shuryouDay = "sr_yobikan_air_shuryou_day";

    /**
     * 終了時間
     */
    private String shuryouTime = "sr_yobikan_air_shuryou_time";

    /**
     * 終了担当者
     */
    private String shuryouTantousya = "sr_yobikan_air_shuryou_tantousya";

    /**
     * 回収ｾｯﾀ枚数
     */
    private String kaishuSettaMaisu = "sr_yobikan_air_kaishu_setta_maisu";

    /**
     * 備考1
     */
    private String bikou1 = "sr_yobikan_air_bikou1";

    /**
     * 備考2
     */
    private String bikou2 = "sr_yobikan_air_bikou2";

    /**
     * 枝番ｺﾋﾟｰ(画面上ボタン)
     */
    private String btnCopyEdabanTop = "sr_yobikan_air_copy_edaban_Top";

    /**
     * 開始日時(画面上ボタン)
     */
    private String btnStartdatetimeTop = "sr_yobikan_air_startdatetime_Top";

    /**
     * 終了日時(画面上ボタン)
     */
    private String btnEnddatetimeTop = "sr_yobikan_air_enddatetime_Top";

    /**
     * 仮登録(画面上ボタン)
     */
    private String btnKariTourokuTop = "sr_yobikan_air_kari_touroku_Top";

    /**
     * 登録(画面上ボタン)
     */
    private String btnInsertTop = "sr_yobikan_air_insert_Top";

    /**
     * 修正(画面上ボタン)
     */
    private String btnUpdateTop = "sr_yobikan_air_update_Top";

    /**
     * 削除(画面上ボタン)
     */
    private String btnDeleteTop = "sr_yobikan_air_delete_Top";

    /**
     * 枝番ｺﾋﾟｰ(画面下ボタン)
     */
    private String btnCopyEdabanBottom = "sr_yobikan_air_copy_edaban_Bottom";

    /**
     * 開始日時(画面下ボタン)
     */
    private String btnStartdatetimeBottom = "sr_yobikan_air_startdatetime_Bottom";

    /**
     * 終了日時(画面下ボタン)
     */
    private String btnEnddatetimeBottom = "sr_yobikan_air_enddatetime_Bottom";

    /**
     * 仮登録(画面下ボタン)
     */
    private String btnKariTourokuBottom = "sr_yobikan_air_kari_touroku_Bottom";

    /**
     * 登録(画面下ボタン)
     */
    private String btnInsertBottom = "sr_yobikan_air_insert_Bottom";

    /**
     * 修正(画面下ボタン)
     */
    private String btnUpdateBottom = "sr_yobikan_air_update_Bottom";

    /**
     * 削除(画面下ボタン)
     */
    private String btnDeleteBottom = "sr_yobikan_air_delete_Bottom";

    /**
     * ユーザー認証パラメータ(修正)
     */
    private String userAuthUpdateParam = "sr_yobikan_air_update_button";

    /**
     * ユーザー認証パラメータ(削除)
     */
    private String userAuthDeleteParam = "sr_yobikan_air_delete_button";
    
        /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * kcpno
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * 客先
     * @return the kyakusaki
     */
    public String getKyakusaki() {
        return kyakusaki;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotKubun
     */
    public String getLotKubun() {
        return lotKubun;
    }

    /**
     * ｵｰﾅｰ
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 指示
     * @return the siji
     */
    public String getSiji() {
        return siji;
    }

    /**
     * 受入ｾｯﾀ枚数
     * @return the ukeireSettaMaisu
     */
    public String getUkeireSettaMaisu() {
        return ukeireSettaMaisu;
    }

    /**
     * 号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo．
     * @return the programNo
     */
    public String getProgramNo() {
        return programNo;
    }

    /**
     * 最高温度
     * @return the maxOndo
     */
    public String getMaxOndo() {
        return maxOndo;
    }

    /**
     * ｷｰﾌﾟ時間
     * @return the keepTime
     */
    public String getKeepTime() {
        return keepTime;
    }

    /**
     * 総時間
     * @return the totalTime
     */
    public String getTotalTime() {
        return totalTime;
    }

    /**
     * 投入ｾｯﾀ枚数
     * @return the tounyuSettaMaisu
     */
    public String getTounyuSettaMaisu() {
        return tounyuSettaMaisu;
    }

    /**
     * 開始日
     * @return the kaishiDay
     */
    public String getKaishiDay() {
        return kaishiDay;
    }

    /**
     * 開始時間
     * @return the kaishiTime
     */
    public String getKaishiTime() {
        return kaishiTime;
    }

    /**
     * 開始担当者
     * @return the kaishiTantousya
     */
    public String getKaishiTantousya() {
        return kaishiTantousya;
    }

    /**
     * 開始確認者
     * @return the kaishiKakuninsya
     */
    public String getKaishiKakuninsya() {
        return kaishiKakuninsya;
    }

    /**
     * 終了日
     * @return the shuryouDay
     */
    public String getShuryouDay() {
        return shuryouDay;
    }

    /**
     * 終了時間
     * @return the shuryouTime
     */
    public String getShuryouTime() {
        return shuryouTime;
    }

    /**
     * 終了担当者
     * @return the shuryouTantousya
     */
    public String getShuryouTantousya() {
        return shuryouTantousya;
    }

    /**
     * 回収ｾｯﾀ枚数
     * @return the kaishuSettaMaisu
     */
    public String getKaishuSettaMaisu() {
        return kaishuSettaMaisu;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 枝番ｺﾋﾟｰ(画面上ボタン)
     * @return the btnCopyEdabanTop
     */
    public String getBtnCopyEdabanTop() {
        return btnCopyEdabanTop;
    }

    /**
     * 開始日時(画面上ボタン)
     * @return the btnStartdatetimeTop
     */
    public String getBtnStartdatetimeTop() {
        return btnStartdatetimeTop;
    }

    /**
     * 終了日時(画面上ボタン)
     * @return the btnEnddatetimeTop
     */
    public String getBtnEnddatetimeTop() {
        return btnEnddatetimeTop;
    }

    /**
     * 仮登録(画面上ボタン)
     * @return the btnKariTourokuTop
     */
    public String getBtnKariTourokuTop() {
        return btnKariTourokuTop;
    }

    /**
     * 登録(画面上ボタン)
     * @return the btnInsertTop
     */
    public String getBtnInsertTop() {
        return btnInsertTop;
    }

    /**
     * 修正(画面上ボタン)
     * @return the btnUpdateTop
     */
    public String getBtnUpdateTop() {
        return btnUpdateTop;
    }

    /**
     * 削除(画面上ボタン)
     * @return the btnDeleteTop
     */
    public String getBtnDeleteTop() {
        return btnDeleteTop;
    }

    /**
     * 枝番ｺﾋﾟｰ(画面下ボタン)
     * @return the btnCopyEdabanBottom
     */
    public String getBtnCopyEdabanBottom() {
        return btnCopyEdabanBottom;
    }

    /**
     * 開始日時(画面下ボタン)
     * @return the btnStartdatetimeBottom
     */
    public String getBtnStartdatetimeBottom() {
        return btnStartdatetimeBottom;
    }

    /**
     * 終了日時(画面下ボタン)
     * @return the btnEnddatetimeBottom
     */
    public String getBtnEnddatetimeBottom() {
        return btnEnddatetimeBottom;
    }

    /**
     * 仮登録(画面下ボタン)
     * @return the btnKariTourokuBottom
     */
    public String getBtnKariTourokuBottom() {
        return btnKariTourokuBottom;
    }

    /**
     * 登録(画面下ボタン)
     * @return the btnInsertBottom
     */
    public String getBtnInsertBottom() {
        return btnInsertBottom;
    }

    /**
     * 修正(画面下ボタン)
     * @return the btnUpdateBottom
     */
    public String getBtnUpdateBottom() {
        return btnUpdateBottom;
    }

    /**
     * 削除(画面下ボタン)
     * @return the btnDeleteBottom
     */
    public String getBtnDeleteBottom() {
        return btnDeleteBottom;
    }

    /**
     * ユーザー認証パラメータ(修正)
     * @return the userAuthUpdateParam
     */
    public String getUserAuthUpdateParam() {
        return userAuthUpdateParam;
    }
    
    /**
     * ユーザー認証パラメータ(削除)
     * @return the userAuthDeleteParam
     */
    public String getUserAuthDeleteParam() {
        return userAuthDeleteParam;
    }


}
