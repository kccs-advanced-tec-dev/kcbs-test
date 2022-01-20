/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDM05;
import jp.co.kccs.xhd.model.GXHDO102B024Model;
import static jp.co.kccs.xhd.pxhdo102.GXHDO102B024.getFormBean;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901BEX;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.QueryRunner;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B024A(誘電体ｽﾗﾘｰ作製・粉砕)
 *
 * @author KCSS K.Jo
 * @since 2021/12/26
 */
@Named
@ViewScoped
public class GXHDO102B024A extends GXHDO901BEX {

    private String mainDefaultStyle = "";
    private String mainAutoStyle = "";
    private String mainDivStyle = "";
    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕テーブル表示スタイル
     */
    private String displayStyle;

    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * 誘電体ｽﾗﾘｰ品名
     */
    private FXHDD01 yuudentaihinmei;

    /**
     * 誘電体ｽﾗﾘｰLotNo
     */
    private FXHDD01 yuudentailotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 原料LotNo
     */
    private FXHDD01 genryoulotno;

    /**
     * 原料記号
     */
    private FXHDD01 genryoukigou;

    /**
     * 粉砕機
     */
    private FXHDD01 funsaiki;

    /**
     * 粉砕機洗浄①
     */
    private FXHDD01 funsaikisenjyou1;

    /**
     * 粉砕機洗浄②
     */
    private FXHDD01 funsaikisenjyou2;

    /**
     * 連続運転回数①
     */
    private FXHDD01 renzokuunten1;

    /**
     * 連続運転回数②
     */
    private FXHDD01 renzokuunten2;

    /**
     * 玉石_重量
     */
    private FXHDD01 gyokusekijyuryou;

    /**
     * 玉石_ﾛｯﾄ
     */
    private FXHDD01 gyokusekilot;

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     */
    private FXHDD01 gyokusekimediakei;

    /**
     * 投入量
     */
    private FXHDD01 tounyuuryou;

    /**
     * 時間/ﾊﾟｽ回数
     */
    private FXHDD01 zikanpass;

    /**
     * ｽｸﾘｰﾝ
     */
    private FXHDD01 screen;

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     */
    private FXHDD01 kaitensuudisp;

    /**
     * 回転数_主軸
     */
    private FXHDD01 kaitensuusyujiku;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private FXHDD01 pompsyutsuryoku;

    /**
     * 流量
     */
    private FXHDD01 ryuuryou;

    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu;

    /**
     * コンストラクタ
     */
    public GXHDO102B024A() {
    }

//<editor-fold defaultstate="collapsed" desc="#setter getter">
    /**
     * ﾒｲﾝDivのスタイル
     *
     * @return the mainDivStyle
     */
    public String getMainDivStyle() {
        return mainDivStyle;
    }

    /**
     * ﾒｲﾝDivのスタイル
     *
     * @param mainDivStyle the mainDivStyle to set
     */
    public void setMainDivStyle(String mainDivStyle) {
        this.mainDivStyle = mainDivStyle;
    }

    /**
     * @return the mainDefaultStyle
     */
    public String getMainDefaultStyle() {
        return mainDefaultStyle;
    }

    /**
     * @param mainDefaultStyle the mainDefaultStyle to set
     */
    public void setMainDefaultStyle(String mainDefaultStyle) {
        this.mainDefaultStyle = mainDefaultStyle;
    }

    /**
     * @return the mainAutoStyle
     */
    public String getMainAutoStyle() {
        return mainAutoStyle;
    }

    /**
     * @param mainAutoStyle the mainAutoStyle to set
     */
    public void setMainAutoStyle(String mainAutoStyle) {
        this.mainAutoStyle = mainAutoStyle;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕テーブル表示スタイル
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕テーブル表示スタイル
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    /**
     * WIPﾛｯﾄNo
     *
     * @return the wiplotno
     */
    public FXHDD01 getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPﾛｯﾄNo
     *
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(FXHDD01 wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     *
     * @return the yuudentaihinmei
     */
    public FXHDD01 getYuudentaihinmei() {
        return yuudentaihinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     *
     * @param yuudentaihinmei the yuudentaihinmei to set
     */
    public void setYuudentaihinmei(FXHDD01 yuudentaihinmei) {
        this.yuudentaihinmei = yuudentaihinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     *
     * @return the yuudentailotno
     */
    public FXHDD01 getYuudentailotno() {
        return yuudentailotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     *
     * @param yuudentailotno the yuudentailotno to set
     */
    public void setYuudentailotno(FXHDD01 yuudentailotno) {
        this.yuudentailotno = yuudentailotno;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubun
     */
    public FXHDD01 getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(FXHDD01 lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料LotNo
     *
     * @return the genryoulotno
     */
    public FXHDD01 getGenryoulotno() {
        return genryoulotno;
    }

    /**
     * 原料LotNo
     *
     * @param genryoulotno the genryoulotno to set
     */
    public void setGenryoulotno(FXHDD01 genryoulotno) {
        this.genryoulotno = genryoulotno;
    }

    /**
     * 原料記号
     *
     * @return the genryoukigou
     */
    public FXHDD01 getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     *
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(FXHDD01 genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 粉砕機
     *
     * @return the funsaiki
     */
    public FXHDD01 getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     *
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(FXHDD01 funsaiki) {
        this.funsaiki = funsaiki;
    }

    /**
     * 粉砕機洗浄①
     *
     * @return the funsaikisenjyou1
     */
    public FXHDD01 getFunsaikisenjyou1() {
        return funsaikisenjyou1;
    }

    /**
     * 粉砕機洗浄①
     *
     * @param funsaikisenjyou1 the funsaikisenjyou1 to set
     */
    public void setFunsaikisenjyou1(FXHDD01 funsaikisenjyou1) {
        this.funsaikisenjyou1 = funsaikisenjyou1;
    }

    /**
     * 粉砕機洗浄②
     *
     * @return the funsaikisenjyou2
     */
    public FXHDD01 getFunsaikisenjyou2() {
        return funsaikisenjyou2;
    }

    /**
     * 粉砕機洗浄②
     *
     * @param funsaikisenjyou2 the funsaikisenjyou2 to set
     */
    public void setFunsaikisenjyou2(FXHDD01 funsaikisenjyou2) {
        this.funsaikisenjyou2 = funsaikisenjyou2;
    }

    /**
     * 連続運転回数①
     *
     * @return the renzokuunten1
     */
    public FXHDD01 getRenzokuunten1() {
        return renzokuunten1;
    }

    /**
     * 連続運転回数①
     *
     * @param renzokuunten1 the renzokuunten1 to set
     */
    public void setRenzokuunten1(FXHDD01 renzokuunten1) {
        this.renzokuunten1 = renzokuunten1;
    }

    /**
     * 連続運転回数②
     *
     * @return the renzokuunten2
     */
    public FXHDD01 getRenzokuunten2() {
        return renzokuunten2;
    }

    /**
     * 連続運転回数②
     *
     * @param renzokuunten2 the renzokuunten2 to set
     */
    public void setRenzokuunten2(FXHDD01 renzokuunten2) {
        this.renzokuunten2 = renzokuunten2;
    }

    /**
     * 玉石_重量
     *
     * @return the gyokusekijyuryou
     */
    public FXHDD01 getGyokusekijyuryou() {
        return gyokusekijyuryou;
    }

    /**
     * 玉石_重量
     *
     * @param gyokusekijyuryou the gyokusekijyuryou to set
     */
    public void setGyokusekijyuryou(FXHDD01 gyokusekijyuryou) {
        this.gyokusekijyuryou = gyokusekijyuryou;
    }

    /**
     * 玉石_ﾛｯﾄ
     *
     * @return the gyokusekilot
     */
    public FXHDD01 getGyokusekilot() {
        return gyokusekilot;
    }

    /**
     * 玉石_ﾛｯﾄ
     *
     * @param gyokusekilot the gyokusekilot to set
     */
    public void setGyokusekilot(FXHDD01 gyokusekilot) {
        this.gyokusekilot = gyokusekilot;
    }

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     *
     * @return the gyokusekimediakei
     */
    public FXHDD01 getGyokusekimediakei() {
        return gyokusekimediakei;
    }

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     *
     * @param gyokusekimediakei the gyokusekimediakei to set
     */
    public void setGyokusekimediakei(FXHDD01 gyokusekimediakei) {
        this.gyokusekimediakei = gyokusekimediakei;
    }

    /**
     * 投入量
     *
     * @return the tounyuuryou
     */
    public FXHDD01 getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     *
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(FXHDD01 tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
    }

    /**
     * 時間/ﾊﾟｽ回数
     *
     * @return the zikanpass
     */
    public FXHDD01 getZikanpass() {
        return zikanpass;
    }

    /**
     * 時間/ﾊﾟｽ回数
     *
     * @param zikanpass the zikanpass to set
     */
    public void setZikanpass(FXHDD01 zikanpass) {
        this.zikanpass = zikanpass;
    }

    /**
     * ｽｸﾘｰﾝ
     *
     * @return the screen
     */
    public FXHDD01 getScreen() {
        return screen;
    }

    /**
     * ｽｸﾘｰﾝ
     *
     * @param screen the screen to set
     */
    public void setScreen(FXHDD01 screen) {
        this.screen = screen;
    }

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     *
     * @return the kaitensuudisp
     */
    public FXHDD01 getKaitensuudisp() {
        return kaitensuudisp;
    }

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     *
     * @param kaitensuudisp the kaitensuudisp to set
     */
    public void setKaitensuudisp(FXHDD01 kaitensuudisp) {
        this.kaitensuudisp = kaitensuudisp;
    }

    /**
     * 回転数_主軸
     *
     * @return the kaitensuusyujiku
     */
    public FXHDD01 getKaitensuusyujiku() {
        return kaitensuusyujiku;
    }

    /**
     * 回転数_主軸
     *
     * @param kaitensuusyujiku the kaitensuusyujiku to set
     */
    public void setKaitensuusyujiku(FXHDD01 kaitensuusyujiku) {
        this.kaitensuusyujiku = kaitensuusyujiku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     *
     * @return the pompsyutsuryoku
     */
    public FXHDD01 getPompsyutsuryoku() {
        return pompsyutsuryoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     *
     * @param pompsyutsuryoku the pompsyutsuryoku to set
     */
    public void setPompsyutsuryoku(FXHDD01 pompsyutsuryoku) {
        this.pompsyutsuryoku = pompsyutsuryoku;
    }

    /**
     * 流量
     *
     * @return the ryuuryou
     */
    public FXHDD01 getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     *
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(FXHDD01 ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ回数
     *
     * @return the passkaisuu
     */
    public FXHDD01 getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数
     *
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(FXHDD01 passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

//</editor-fold>  
    /**
     * 画面起動時処理
     *
     * @param mainWidth 画面サイズ
     */
    public void init(String mainWidth) {

        this.setFormIds(new String[]{"GXHDO102B024A", "GXHDO102B024B"});

        //親の初期処理呼び出し
        super.init();

        this.setMainDefaultStyle("width:" + mainWidth + "px;margin-left:auto;margin-right:auto;");
        this.setMainAutoStyle("width:auto;" + "min-width:" + mainWidth + "px;");
        this.setMainDivStyle(this.getMainDefaultStyle());
    }

    /**
     * 画面の「↓情報」を押下する処理
     *
     */
    public void setKouatsubunsanTableDisplayStyle() {
        GXHDO102B024A bean = (GXHDO102B024A) getFormBean("gXHDO102B024A");
        if ("none".equals(bean.getDisplayStyle())) {
            bean.setDisplayStyle("block");
        } else {
            bean.setDisplayStyle("none");
        }
        processData.setMethod("");
    }

    /**
     * 画面のBean情報を取得
     *
     * @param beanId フォームID
     * @return サブ画面情報
     */
    public static Object getFormBean(String beanId) {
        return FacesContext.getCurrentInstance().
                getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                        getELContext(), null, beanId);
    }

    /**
     * 背景色のクリア処理
     *
     * @param listdata 一覧表示データ
     */
    private void clearBackColor(List<GXHDO102B024Model> listdata) {
        listdata.stream().map((gxhdo102b024model) -> gxhdo102b024model.getKaishi_time()).forEachOrdered((kaishi_time) -> {
            kaishi_time.setBackColorInput(kaishi_time.getBackColorInputDefault());
        });
    }

    /**
     * エラーセット
     *
     * @param kaishi_timeItem 開始時刻
     */
    private void setError(FXHDD01 kaishi_timeItem) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000013", kaishi_timeItem.getLabel1()), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        kaishi_timeItem.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
    }

    /**
     * 【停止予定時刻】自動入力処理の規格値取得
     *
     * @param kaishitimeValue 入力された開始時刻の値
     * @param rowNo フォーカスアウトあれる開始時刻の行目
     * @throws SQLException 例外エラー
     */
    public void doTeishiyoteitimejidounyuuryouku(String kaishitimeValue, int rowNo) throws SQLException {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        // 背景色のクリア処理
        clearBackColor(listdata);
        if (StringUtil.isEmpty(kaishitimeValue)) {
            return;
        }
        HashMap<String, String> kaisuuMap = new HashMap<>();
        // 規格値取得
        boolean errorFlg = getJidounyuuryoukuKikakuchi(kaisuuMap);
        if (errorFlg) {
            return;
        }
        String kikakuti = kaisuuMap.get("kikakuti");
        if (!NumberUtil.isIntegerNumeric(kikakuti)) {
            return;
        }
        FXHDD01 kaishi_timeItem = null;
        if (rowNo == 0) {
            errorFlg = false;

            for (int i = 1; i < listdata.size(); i++) {
                GXHDO102B024Model gxhdo102b024model = listdata.get(i);
                // 開始時刻(2行目～最終行)まで時刻が入力されている場合
                if (!StringUtil.isEmpty(gxhdo102b024model.getKaishi_time().getValue())) {
                    errorFlg = true;
                    kaishi_timeItem = gxhdo102b024model.getKaishi_time();
                    break;
                }
            }
            // 開始時刻(2行目～最終行)まで時刻が入力されている場合エラー
            if (errorFlg && kaishi_timeItem != null) {
                //エラー発生
                setError(kaishi_timeItem);
                return;
            }

            // 開始時刻(2行目～最終行)まで時刻が入力されていない場合、【停止予定時刻】自動入力処理の計算処理を実行
            doJidounyuuryoukuKeisan(listdata, kikakuti, rowNo);
        } else {
            for (int i = rowNo - 1; i >= 0; i--) {
                GXHDO102B024Model gxhdo102b024model = listdata.get(i);
                // 開始時刻(n-1行目)から開始時刻(1行目)まで空白がある場合
                if (StringUtil.isEmpty(gxhdo102b024model.getKaishi_time().getValue())) {
                    errorFlg = true;
                    kaishi_timeItem = gxhdo102b024model.getKaishi_time();
                    break;
                }
            }
            // 開始時刻(n-1行目)から開始時刻(1行目)まで空白がある場合エラー
            if (errorFlg && kaishi_timeItem != null) {
                //エラー発生
                setError(kaishi_timeItem);
                return;
            }

            // 開始時刻(n-1行目)から開始時刻(1行目)まで空白がない場合、【停止予定時刻】自動入力処理の計算処理を実行
            doJidounyuuryoukuKeisan(listdata, kikakuti, rowNo);
        }

        processData.setMethod("");
    }

    /**
     * 【停止予定時刻】自動入力処理の計算処理
     *
     * @param listdata 一覧表示データ
     * @param kikakuti 規格値
     * @param rowNo フォーカスアウトあれる開始時刻の行目
     */
    private void doJidounyuuryoukuKeisan(List<GXHDO102B024Model> listdata, String kikakuti, int rowNo) {
        boolean setDayFlg = true;
        // フォーカスアウトされる行に開始日が入力されるかフラグ
        if (StringUtil.isEmpty(listdata.get(rowNo).getKaishi_day().getValue())) {
            setDayFlg = false;
        }
        for (int i = rowNo; i < listdata.size(); i++) {
            GXHDO102B024Model gxhdo102b024model = listdata.get(i);
            GXHDO102B024Model gxhdo102b024modelTugi = null;
            if (i < (listdata.size() - 1)) {
                gxhdo102b024modelTugi = listdata.get(i + 1);
            }
            // 開始日
            String kaishi_dayValue = gxhdo102b024model.getKaishi_day().getValue();
            if (StringUtil.isEmpty(kaishi_dayValue)) {
                kaishi_dayValue = "700101";
            }
            // 開始時刻
            String kaishi_timeValue = gxhdo102b024model.getKaishi_time().getValue();
            // 開始ﾊﾟｽ
            String kaishipassValue = gxhdo102b024model.getKaishipass().getValue();
            // 停止ﾊﾟｽ
            String teishipassValue = gxhdo102b024model.getTeishipass().getValue();
            if (!NumberUtil.isIntegerNumeric(kaishipassValue) || !NumberUtil.isIntegerNumeric(teishipassValue)
                    || !NumberUtil.isIntegerNumeric(kaishi_timeValue)) {
                return;
            }
            if (!DateUtil.isValidYYMMDD(kaishi_dayValue) || !DateUtil.isValidHHMM(kaishi_timeValue)) {
                return;
            }
            // ﾊﾟｽ間隔 = ﾊﾟｽ停止 ー ﾊﾟｽ開始
            BigDecimal passIntervalDec = new BigDecimal(teishipassValue).subtract(new BigDecimal(kaishipassValue));
            // 開始時刻 + ﾊﾟｽ間隔 × 【①規格値取得.規格値】
            BigDecimal jikan = passIntervalDec.multiply(new BigDecimal(kikakuti));
            // 停止予定時刻 = 開始時刻 + ﾊﾟｽ間隔 × 【①規格値取得.規格値】
            Date dateTime = DateUtil.addJikan(kaishi_dayValue, kaishi_timeValue, jikan.intValue(), Calendar.MINUTE);
            if (dateTime == null) {
                return;
            }
            String teishiyotei_dayValue = new SimpleDateFormat("yyMMdd").format(dateTime);
            String teishiyotei_timeValue = new SimpleDateFormat("HHmm").format(dateTime);
            gxhdo102b024model.getTeishiyotei_time().setValue(teishiyotei_timeValue);
            if (gxhdo102b024modelTugi != null) {
                // 開始時刻(次の行)に値がある場合、処理を終了する
                String kaishi_timeTugiValue = gxhdo102b024modelTugi.getKaishi_time().getValue();
                if (!StringUtil.isEmpty(kaishi_timeTugiValue)) {
                    return;
                }
                // 開始時刻(次の行) = 停止予定時刻 + 10分
                dateTime = DateUtil.addJikan(teishiyotei_dayValue, teishiyotei_timeValue, 10, Calendar.MINUTE);
                if (dateTime == null) {
                    return;
                }
                if (setDayFlg) {
                    gxhdo102b024modelTugi.getKaishi_day().setValue(new SimpleDateFormat("yyMMdd").format(dateTime));
                }
                gxhdo102b024modelTugi.getKaishi_time().setValue(new SimpleDateFormat("HHmm").format(dateTime));
            }
        }
    }

    /**
     * 【停止予定時刻】自動入力処理の規格値取得
     *
     * @param kikakuchiMap 規格値マップ
     * @return エラーフラグ
     * @throws SQLException 例外エラー
     */
    private boolean getJidounyuuryoukuKikakuchi(HashMap<String, String> kikakuchiMap) throws SQLException {
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "誘電体ｽﾗﾘｰ作製";
        String key = "誘電体ｽﾗﾘｰ作製・粉砕・ﾊﾟｽ間隔";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = GXHDO102B024.loadFxhbm03Data(queryRunnerDoc, key);
        if (fxhbm03Data == null || fxhbm03Data.isEmpty()) {
            // 取得できなかった場合、処理を終了する。
            return true;
        }

        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = GXHDO102B024.loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // 取得できなかった場合、処理を終了する。
            return true;
        }
        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(GXHDO102B024.getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(GXHDO102B024.getMapData(daMkSekKeiData, "pattern"));
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
        String data = StringUtil.nullToBlank(GXHDO102B024.getMapData(fxhbm03Data, "data"));
        String[] dataSplitList = data.split(",");
        // [前工程規格情報]から、ﾃﾞｰﾀを取得
        Map daMkJokenData = GXHDO102B024.loadJidounyuuryoukuDaMkJokenData(queryRunnerQcdb, sekkeiNo, dataSplitList);
        String kikakuti;
        if (daMkJokenData == null || daMkJokenData.isEmpty()) {
            // [前工程標準規格情報]から、ﾃﾞｰﾀを取得
            String hinmei = (String) session.getAttribute("hinmei");
            Map daMkhYoJunJokenData = GXHDO102B024.loadJidounyuuryoukuDaMkhYoJunJokenData(queryRunnerQcdb, hinmei, pattern, key);
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // 取得できなかった場合、処理を終了する。
                return true;
            }
            // 前工程標準規格情報の規格値
            kikakuti = StringUtil.nullToBlank(GXHDO102B024.getMapData(daMkhYoJunJokenData, "kikakuti"));
        } else {
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(GXHDO102B024.getMapData(daMkJokenData, "kikakuti"));
        }
        kikakuchiMap.put("kikakuti", kikakuti);
        return false;
    }

    /**
     * 共通ﾁｪｯｸ必要の項目リストを取得
     *
     * @param gxhdo102b024model モデルデータ
     * @param itemsList 項目リスト
     */
    private void addItemListFromGXHDO102B024Model(GXHDO102B024Model gxhdo102b024model, List<FXHDD01> itemsList) {
        itemsList.add(gxhdo102b024model.getKaishi_day());
        itemsList.add(gxhdo102b024model.getKaishi_time());
        itemsList.add(gxhdo102b024model.getTeishiyotei_time());
        itemsList.add(gxhdo102b024model.getTeishi_time());
        itemsList.add(gxhdo102b024model.getSyujikudenryuu());
        itemsList.add(gxhdo102b024model.getDeguchiondo());
        itemsList.add(gxhdo102b024model.getSealondo());
        itemsList.add(gxhdo102b024model.getPumpmemori());
        itemsList.add(gxhdo102b024model.getPumpatsu());
        itemsList.add(gxhdo102b024model.getD50());
        itemsList.add(gxhdo102b024model.getBet());
        itemsList.add(gxhdo102b024model.getRyuuryoukikaku());
        itemsList.add(gxhdo102b024model.getRyuuryou());
        itemsList.add(gxhdo102b024model.getKaishipass());
        itemsList.add(gxhdo102b024model.getTeishipass());
        itemsList.add(gxhdo102b024model.getBikou1());
        itemsList.add(gxhdo102b024model.getBikou2());
    }

    /**
     * モデルデータの項目のラベルを設定
     *
     * @param gxhdo102b024model モデルデータ
     * @param rowIndx 行番号
     */
    private void setGXHDO102B024ModelItemLabel1(GXHDO102B024Model gxhdo102b024model, int rowIndx) {

        String Kaishi_dayLabel1 = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_day().getLabel1());
        if (!Kaishi_dayLabel1.contains("行目: ")) {
            gxhdo102b024model.getKaishi_day().setLabel1(rowIndx + "行目: " + Kaishi_dayLabel1);
            gxhdo102b024model.getKaishi_time().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getKaishi_time().getLabel1());
            gxhdo102b024model.getTeishiyotei_time().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getTeishiyotei_time().getLabel1());
            gxhdo102b024model.getTeishi_time().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getTeishi_time().getLabel1());
            gxhdo102b024model.getSyujikudenryuu().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getSyujikudenryuu().getLabel1());
            gxhdo102b024model.getDeguchiondo().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getDeguchiondo().getLabel1());
            gxhdo102b024model.getSealondo().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getSealondo().getLabel1());
            gxhdo102b024model.getPumpmemori().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getPumpmemori().getLabel1());
            gxhdo102b024model.getPumpatsu().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getPumpatsu().getLabel1());
            gxhdo102b024model.getD50().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getD50().getLabel1());
            gxhdo102b024model.getBet().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getBet().getLabel1());
            gxhdo102b024model.getRyuuryoukikaku().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getRyuuryoukikaku().getLabel1());
            gxhdo102b024model.getRyuuryou().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getRyuuryou().getLabel1());
            gxhdo102b024model.getKaishipass().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getKaishipass().getLabel1());
            gxhdo102b024model.getTeishipass().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getTeishipass().getLabel1());
            gxhdo102b024model.getBikou1().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getBikou1().getLabel1());
            gxhdo102b024model.getBikou2().setLabel1(rowIndx + "行目: " + gxhdo102b024model.getBikou2().getLabel1());
        }
    }

    /**
     * モデルから項目データを取得する。
     *
     * @param itemId 項目ID
     * @param gxhdo102b024model モデルデータ
     * @return 項目データ
     */
    private FXHDD01 getGXHDO102B024ModelItem(String itemId, GXHDO102B024Model gxhdo102b024model) {
        switch (itemId) {
            // 日付
            case GXHDO102B024Const.KAISHI_DAY:
                return gxhdo102b024model.getKaishi_day();
            // 開始時刻
            case GXHDO102B024Const.KAISHI_TIME:
                return gxhdo102b024model.getKaishi_time();
            // 停止予定時刻
            case GXHDO102B024Const.TEISHIYOTEI_TIME:
                return gxhdo102b024model.getTeishiyotei_time();
            // 停止時刻
            case GXHDO102B024Const.TEISHI_TIME:
                return gxhdo102b024model.getTeishi_time();
            // 主軸電流（A）
            case GXHDO102B024Const.SYUJIKUDENRYUU:
                return gxhdo102b024model.getSyujikudenryuu();
            // 出口温度（℃）
            case GXHDO102B024Const.DEGUCHIONDO:
                return gxhdo102b024model.getDeguchiondo();
            // ｼｰﾙ温度（℃）
            case GXHDO102B024Const.SEALONDO:
                return gxhdo102b024model.getSealondo();
            // ﾎﾟﾝﾌﾟ目盛（rpm）
            case GXHDO102B024Const.PUMPMEMORI:
                return gxhdo102b024model.getPumpmemori();
            // ﾎﾟﾝﾌﾟ圧（Mpa）
            case GXHDO102B024Const.PUMPATSU:
                return gxhdo102b024model.getPumpatsu();
            // D50（μm）
            case GXHDO102B024Const.D50:
                return gxhdo102b024model.getD50();
            // BET（㎡/g・%）
            case GXHDO102B024Const.BET:
                return gxhdo102b024model.getBet();
            // 流量規格
            case GXHDO102B024Const.RYUURYOUKIKAKU:
                return gxhdo102b024model.getRyuuryoukikaku();
            // 流量 kg/min or L
            case GXHDO102B024Const.SUB_RYUURYOU:
                return gxhdo102b024model.getRyuuryou();
            // 開始ﾊﾟｽ
            case GXHDO102B024Const.KAISHIPASS:
                return gxhdo102b024model.getKaishipass();
            // 停止ﾊﾟｽ
            case GXHDO102B024Const.TEISHIPASS:
                return gxhdo102b024model.getTeishipass();
            // 備考1
            case GXHDO102B024Const.BIKOU1:
                return gxhdo102b024model.getBikou1();
            // 備考2
            case GXHDO102B024Const.BIKOU2:
                return gxhdo102b024model.getBikou2();
        }
        return null;
    }

    /**
     * 共通チェック
     *
     * @param buttonId ボタンID
     * @return エラーメッセージ
     */
    protected ErrorMessageInfo getCheckResult(String buttonId) {
        // リビジョンチェック
        ErrorMessageInfo checkRevErrorMessage = checkRevision(buttonId);
        if (checkRevErrorMessage != null) {
            return checkRevErrorMessage;
        }

        //共通ﾁｪｯｸ
        List<FXHDM05> itemRowCheckList
                = this.checkListHDM05.stream().filter(n -> buttonId.equals(n.getButtonId())).collect(Collectors.toList());

        ValidateUtil validateUtil = new ValidateUtil();
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
        // 共通ﾁｪｯｸ必要の項目リスト
        List<FXHDD01> itemsList = new ArrayList<>();
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (int i = 0; i < listdata.size(); i++) {
            GXHDO102B024Model gxhdo102b024model = listdata.get(i);
            setGXHDO102B024ModelItemLabel1(gxhdo102b024model, i + 1);
            addItemListFromGXHDO102B024Model(gxhdo102b024model, itemsList);
        }
        ErrorMessageInfo requireCheckErrorMessage = validateUtil.executeValidation(itemRowCheckList, itemsList, queryRunnerWip);
        if (requireCheckErrorMessage != null && !StringUtil.isEmpty(requireCheckErrorMessage.getErrorMessage())) {
            // エラー項目の背景色を設定
            requireCheckErrorMessage.setPageChangeItemIndex(-1);
            int kaisuu = Integer.parseInt(requireCheckErrorMessage.getErrorMessage().substring(0, requireCheckErrorMessage.getErrorMessage().indexOf("行目"))) - 1;
            GXHDO102B024Model gxhdo102b024model = listdata.get(kaisuu);
            String itemId = requireCheckErrorMessage.getErrorItemInfoList().get(0).getItemId();
            FXHDD01 item = getGXHDO102B024ModelItem(itemId, gxhdo102b024model);
            if (item != null) {
                item.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            }
        }

        return requireCheckErrorMessage;
    }
    
    /**
     * 背景色をデフォルトの背景色に戻す
     *
     * @param buttonId ボタンID
     */
    public void clearItemListBackColor(String buttonId) {
        // 背景色を戻さない特定の処理を除き背景色をデフォルトの背景色に戻す。
        if (this.noCheckButtonId == null || !this.noCheckButtonId.contains(buttonId)) {
            for (FXHDD01 fxhdd01 : this.itemList) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }
        }
        clearListDataBackColor();
    }
    
    /**
     * 背景色をデフォルトの背景色に戻す
     *
     */
    public static void clearListDataBackColor() {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        // 背景色を戻さない特定の処理を除き背景色をデフォルトの背景色に戻す。
        listdata.stream().forEach(gxhdo102b024model -> {
            gxhdo102b024model.getKaishi_day().setBackColorInput(gxhdo102b024model.getKaishi_day().getBackColorInputDefault());
            gxhdo102b024model.getKaishi_time().setBackColorInput(gxhdo102b024model.getKaishi_time().getBackColorInputDefault());
            gxhdo102b024model.getTeishiyotei_time().setBackColorInput(gxhdo102b024model.getTeishiyotei_time().getBackColorInputDefault());
            gxhdo102b024model.getTeishi_time().setBackColorInput(gxhdo102b024model.getTeishi_time().getBackColorInputDefault());
            gxhdo102b024model.getSyujikudenryuu().setBackColorInput(gxhdo102b024model.getSyujikudenryuu().getBackColorInputDefault());
            gxhdo102b024model.getDeguchiondo().setBackColorInput(gxhdo102b024model.getDeguchiondo().getBackColorInputDefault());
            gxhdo102b024model.getSealondo().setBackColorInput(gxhdo102b024model.getSealondo().getBackColorInputDefault());
            gxhdo102b024model.getPumpmemori().setBackColorInput(gxhdo102b024model.getPumpmemori().getBackColorInputDefault());
            gxhdo102b024model.getPumpatsu().setBackColorInput(gxhdo102b024model.getPumpatsu().getBackColorInputDefault());
            gxhdo102b024model.getD50().setBackColorInput(gxhdo102b024model.getD50().getBackColorInputDefault());
            gxhdo102b024model.getBet().setBackColorInput(gxhdo102b024model.getBet().getBackColorInputDefault());
            gxhdo102b024model.getRyuuryoukikaku().setBackColorInput(gxhdo102b024model.getRyuuryoukikaku().getBackColorInputDefault());
            gxhdo102b024model.getRyuuryou().setBackColorInput(gxhdo102b024model.getRyuuryou().getBackColorInputDefault());
            gxhdo102b024model.getKaishipass().setBackColorInput(gxhdo102b024model.getKaishipass().getBackColorInputDefault());
            gxhdo102b024model.getTeishipass().setBackColorInput(gxhdo102b024model.getTeishipass().getBackColorInputDefault());
            gxhdo102b024model.getBikou1().setBackColorInput(gxhdo102b024model.getBikou1().getBackColorInputDefault());
            gxhdo102b024model.getBikou2().setBackColorInput(gxhdo102b024model.getBikou2().getBackColorInputDefault());
        });
    }
}
