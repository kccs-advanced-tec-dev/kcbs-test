/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SrMksinkuukansou;
import jp.co.kccs.xhd.model.gxhdo101b040.GXHDO101B040BModel;
import jp.co.kccs.xhd.model.gxhdo101b040.GXHDO101B040CModel;
import jp.co.kccs.xhd.model.gxhdo101b040.GXHDO101B040DModel;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901AEX;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	電気特性画面選択(コンデンサ)<br>
 * <br>
 * 変更日	2019/XX/XX<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD111B(電気特性)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/09/06
 */
@Named
@ViewScoped
public class GXHDO101B040 extends GXHDO901AEX implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101B040.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

//<editor-fold defaultstate="collapsed" desc="#変数(画面項目)">
    private String mainDefaultStyle = "720px";
    private String mainDivStyle = "";

    /**
     * プリチャージタブモデル
     */
    private GXHDO101B040BModel precharge;

    /**
     * 耐電圧設定条件タブモデル
     */
    private GXHDO101B040CModel taidenatsu;

    /**
     * 設定条件及び処理結果
     */
    private GXHDO101B040DModel setteiJouken;

//</editor-fold>  
    /**
     * コンストラクタ
     */
    public GXHDO101B040() {
    }

//<editor-fold defaultstate="collapsed" desc="#setter getter">
    /**
     * @return the mainDivStyle
     */
    public String getMainDivStyle() {
        return mainDivStyle;
    }

    /**
     * @param mainDivStyle the mainDivStyle to set
     */
    public void setMainDivStyle(String mainDivStyle) {
        this.mainDivStyle = mainDivStyle;
    }

    /**
     * プリチャージ画面モデル
     *
     * @return the precharge
     */
    public GXHDO101B040BModel getPrecharge() {
        return precharge;
    }

    /**
     * プリチャージ画面モデル
     *
     * @param precharge the precharge to set
     */
    public void setPrecharge(GXHDO101B040BModel precharge) {
        this.precharge = precharge;
    }

    /**
     * 耐電圧設定条件タブモデル
     *
     * @return the taidenatsu
     */
    public GXHDO101B040CModel getTaidenatsu() {
        return taidenatsu;
    }

    /**
     * 耐電圧設定条件タブモデル
     *
     * @param taidenatsu the taidenatsu to set
     */
    public void setTaidenatsu(GXHDO101B040CModel taidenatsu) {
        this.taidenatsu = taidenatsu;
    }

    /**
     * 設定条件及び処理結果
     *
     * @return the setteiJouken
     */
    public GXHDO101B040DModel getSetteiJouken() {
        return setteiJouken;
    }

    /**
     * 設定条件及び処理結果
     *
     * @param setteiJouken the setteiJouken to set
     */
    public void setSetteiJouken(GXHDO101B040DModel setteiJouken) {
        this.setteiJouken = setteiJouken;
    }

    //</editor-fold>  
    /**
     * 画面起動時処理
     *
     * @param mainWidth 画面サイズ
     */
    public void init(String mainWidth) {

        this.setFormIds(new String[]{"GXHDO101B040A", "GXHDO101B040B", "GXHDO101B040C", "GXHDO101B040D"});

        //親の初期処理呼び出し
        this.init();

        this.mainDefaultStyle = "width:" + mainWidth + "px;margin-left:auto;margin-right:auto;";
        this.mainDivStyle = this.mainDefaultStyle;

        // 「プリチャージ」初期設定
        initPrecharge();

        //「耐電圧」初期設定
        initTaidenatsu();
        
        //「設定条件及び処理結果」初期設定
        initSetteiJoken();
        // 画面クリア
        //formClear();
    }

    private void initPrecharge() {
        this.precharge = new GXHDO101B040BModel();
        this.precharge.setDenatsu1(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU1));
        this.precharge.setJudenTime1(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME1));
        this.precharge.setDenatsu2(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU2));
        this.precharge.setJudenTime2(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME2));
        this.precharge.setDenatsu3(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU3));
        this.precharge.setJudenTime3(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME3));
        this.precharge.setDenatsu4(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU4));
        this.precharge.setJudenTime4(getItemRow(this.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME4));
    }

    private void initTaidenatsu() {
        this.taidenatsu = new GXHDO101B040CModel();
        this.taidenatsu.setDenatsu1(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU1));
        this.taidenatsu.setHanteichi1(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI1));
        this.taidenatsu.setJudenTime1(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME1));
        this.taidenatsu.setDenatsu2(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU2));
        this.taidenatsu.setHanteichi2(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI2));
        this.taidenatsu.setJudenTime2(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME2));
        this.taidenatsu.setDenatsu3(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU3));
        this.taidenatsu.setHanteichi3(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI3));
        this.taidenatsu.setJudenTime3(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME3));
        this.taidenatsu.setDenatsu4(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU4));
        this.taidenatsu.setHanteichi4(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI4));
        this.taidenatsu.setJudenTime4(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME4));
        this.taidenatsu.setDenatsu5(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU5));
        this.taidenatsu.setHanteichi5(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI5));
        this.taidenatsu.setJudenTime5(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME5));
        this.taidenatsu.setDenatsu6(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU6));
        this.taidenatsu.setHanteichi6(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI6));
        this.taidenatsu.setJudenTime6(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME6));
        this.taidenatsu.setDenatsu7(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU7));
        this.taidenatsu.setHanteichi7(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI7));
        this.taidenatsu.setJudenTime7(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME7));
        this.taidenatsu.setDenatsu8(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU8));
        this.taidenatsu.setHanteichi8(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI8));
        this.taidenatsu.setJudenTime8(getItemRow(this.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME8));
    }

    private void initSetteiJoken() {
        this.setteiJouken = new GXHDO101B040DModel();
        this.setteiJouken.setBin1PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_PERCENT_KBN));
        this.setteiJouken.setBin1SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_SENBETSU_KBN));
        this.setteiJouken.setBin1KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO));
        this.setteiJouken.setBin1CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_COUNTER_SU));
        this.setteiJouken.setBin1Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_GOSARITSU));
        this.setteiJouken.setBin1MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU));
        this.setteiJouken.setBin1NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S));
        this.setteiJouken.setBin1NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T));
        this.setteiJouken.setBin1ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU));
        this.setteiJouken.setBin1KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN1_KEKKA_CHECK));
        this.setteiJouken.setBin2PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_PERCENT_KBN));
        this.setteiJouken.setBin2SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_SENBETSU_KBN));
        this.setteiJouken.setBin2KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO));
        this.setteiJouken.setBin2CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_COUNTER_SU));
        this.setteiJouken.setBin2Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_GOSARITSU));
        this.setteiJouken.setBin2MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU));
        this.setteiJouken.setBin2NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S));
        this.setteiJouken.setBin2NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T));
        this.setteiJouken.setBin2ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU));
        this.setteiJouken.setBin2KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN2_KEKKA_CHECK));
        this.setteiJouken.setBin3PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_PERCENT_KBN));
        this.setteiJouken.setBin3SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_SENBETSU_KBN));
        this.setteiJouken.setBin3KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO));
        this.setteiJouken.setBin3CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_COUNTER_SU));
        this.setteiJouken.setBin3Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_GOSARITSU));
        this.setteiJouken.setBin3MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU));
        this.setteiJouken.setBin3NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S));
        this.setteiJouken.setBin3NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T));
        this.setteiJouken.setBin3ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU));
        this.setteiJouken.setBin3KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN3_KEKKA_CHECK));
        this.setteiJouken.setBin4PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_PERCENT_KBN));
        this.setteiJouken.setBin4SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_SENBETSU_KBN));
        this.setteiJouken.setBin4KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO));
        this.setteiJouken.setBin4CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_COUNTER_SU));
        this.setteiJouken.setBin4Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_GOSARITSU));
        this.setteiJouken.setBin4MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU));
        this.setteiJouken.setBin4NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S));
        this.setteiJouken.setBin4NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T));
        this.setteiJouken.setBin4ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU));
        this.setteiJouken.setBin4KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN4_KEKKA_CHECK));
        this.setteiJouken.setBin5PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_PERCENT_KBN));
        this.setteiJouken.setBin5SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_SENBETSU_KBN));
        this.setteiJouken.setBin5KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO));
        this.setteiJouken.setBin5CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_COUNTER_SU));
        this.setteiJouken.setBin5Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_GOSARITSU));
        this.setteiJouken.setBin5MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU));
        this.setteiJouken.setBin5NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S));
        this.setteiJouken.setBin5NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T));
        this.setteiJouken.setBin5ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU));
        this.setteiJouken.setBin5KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_KEKKA_CHECK));
        this.setteiJouken.setBin5FukuroCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN5_FUKURO_CHECK));
        this.setteiJouken.setBin6PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_PERCENT_KBN));
        this.setteiJouken.setBin6SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_SENBETSU_KBN));
        this.setteiJouken.setBin6KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO));
        this.setteiJouken.setBin6CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_COUNTER_SU));
        this.setteiJouken.setBin6Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_GOSARITSU));
        this.setteiJouken.setBin6MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU));
        this.setteiJouken.setBin6NukitorikekkaS(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S));
        this.setteiJouken.setBin6NukitorikekkaT(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T));
        this.setteiJouken.setBin6ShinFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU));
        this.setteiJouken.setBin6KekkaCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_KEKKA_CHECK));
        this.setteiJouken.setBin6FukuroCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN6_FUKURO_CHECK));
        this.setteiJouken.setBin7PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_PERCENT_KBN));
        this.setteiJouken.setBin7SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_SENBETSU_KBN));
        this.setteiJouken.setBin7KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO));
        this.setteiJouken.setBin7CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_COUNTER_SU));
        this.setteiJouken.setBin7Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_GOSARITSU));
        this.setteiJouken.setBin7MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU));
        this.setteiJouken.setBin7FukuroCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN7_FUKURO_CHECK));
        this.setteiJouken.setBin8PercentKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_PERCENT_KBN));
        this.setteiJouken.setBin8SenbetsuKbn(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_SENBETSU_KBN));
        this.setteiJouken.setBin8KeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO));
        this.setteiJouken.setBin8CounterSu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_COUNTER_SU));
        this.setteiJouken.setBin8Gosaritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_GOSARITSU));
        this.setteiJouken.setBin8MachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU));
        this.setteiJouken.setBin8FukuroCheck(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN8_FUKURO_CHECK));
        this.setteiJouken.setBin9KKeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO));
        this.setteiJouken.setBin9KMachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU));
        this.setteiJouken.setRakkaKeiryogoSuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO));
        this.setteiJouken.setRakkaMachineFuryoritsu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU));
        this.setteiJouken.setHandaSample(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_HANDA_SAMPLE));
        this.setteiJouken.setShinraiseiSample(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_SHINRAISEI_SAMPLE));
        this.setteiJouken.setShinFuryoHanteisha(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA));
        this.setteiJouken.setHanteiNyuryokusha(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA));
        this.setteiJouken.setToridashisha(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_TORIDASHISHA));
        this.setteiJouken.setKousa1(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOUSA1));
        this.setteiJouken.setJuryo1(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_JURYO1));
        this.setteiJouken.setKosu1(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOSU1));
        this.setteiJouken.setKousa2(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOUSA2));
        this.setteiJouken.setJuryo2(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_JURYO2));
        this.setteiJouken.setKosu2(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOSU2));
        this.setteiJouken.setKousa3(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOUSA3));
        this.setteiJouken.setJuryo3(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_JURYO3));
        this.setteiJouken.setKosu3(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOSU3));
        this.setteiJouken.setKousa4(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOUSA4));
        this.setteiJouken.setJuryo4(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_JURYO4));
        this.setteiJouken.setKosu4(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KOSU4));
        this.setteiJouken.setCounterSosu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_COUNTER_SOSU));
        this.setteiJouken.setRyouhinJuryo(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_RYOUHIN_JURYO));
        this.setteiJouken.setRyouhinKosu(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_RYOUHIN_KOSU));
        this.setteiJouken.setBudomari(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_BUDOMARI));
        this.setteiJouken.setKakuninsha(getItemRow(this.getItemListEx(), GXHDO101B040Const.SET_KAKUNINSHA));

    }

    public void onTabChange(TabChangeEvent event) {
        System.out.println("tab id = " + event.getTab().getId());
        if ("tab4".equals(event.getTab().getId())) {
            this.mainDivStyle = "width:auto;";

        } else {
            this.mainDivStyle = this.mainDefaultStyle;
        }

    }

    /**
     * // * 検索処理 //
     */
//    public void doSearch() {
//
//        try {
//            // 処理前に各項目の背景色をクリア
//            clearInputBackGround();
//
//            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
//            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
//            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
//            // 検索時の入力チェックを行う。
//            if (!inputCheckSearch(queryRunnerWip)) {
//                //エラー発生時はリターン
//                return;
//            }
//
//            // 仕掛データ取得
//            Map sikakariData = loadShikakariData(queryRunnerWip, this.lotno);
//            // 仕掛データのチェック処理を行う。
//            if (!checkSikakariData(sikakariData)) {
//                //エラー発生時はリターン
//                return;
//            }
//            String koteicode = StringUtil.nullToBlank(sikakariData.get("koteicode"));
//            // 仕掛の工程が温度登録可能な工程かチェックする。
//            String permitCode = checkPermitProcess(queryRunnerDoc, koteicode);
//            // 許可工程か判定できなかった場合
//            if ("9".equals(permitCode)) {
//                return;
//            }
//
//            // 工程マスタデータ取得
//            Map koteimasData = loadKoteimas(queryRunnerWip, koteicode);
//            if (koteimasData == null || koteimasData.isEmpty()) {
//                // 工程情報が取得できない場合エラー
//                addErrorMessage(MessageUtil.getMessage("XHD-000097", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000001"));
//            }
//
//            // 設計情報の取得
//            Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, this.lotno);
//            if (sekkeiData == null || sekkeiData.isEmpty()) {
//                // 設計情報が取得できない場合エラー
//                addErrorMessage(MessageUtil.getMessage("XHD-000002"));
//                return;
//            }
//
//            // 規格値(号機/温度)を取得
//            String kikakuchiOndo;
//            Map daJokenDataOndo = getDaJokenData(queryRunnerQcdb, StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")), "焼成", "設定", "ﾋﾟｰｸ温度");
//            if (daJokenDataOndo == null || daJokenDataOndo.isEmpty()) {
//                // データが取得できなかった場合はエラーメッセージをセット
//                kikakuchiOndo = MessageUtil.getMessage("XHD-000071");
//                addErrorMessage(kikakuchiOndo);
//            } else {
//                kikakuchiOndo = StringUtil.nullToBlank(daJokenDataOndo.get("KIKAKUCHI"));
//            }
//
//            // 規格値(水素濃度)を取得
//            String kikakuchiSuisoNoudo;
//            Map daJokenDataSuisoNoudo = getDaJokenData(queryRunnerQcdb, StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")), "焼成", "設定", "水素濃度");
//            if (daJokenDataSuisoNoudo == null || daJokenDataSuisoNoudo.isEmpty()) {
//                // データが取得できなかった場合はエラーメッセージをセット
//                kikakuchiSuisoNoudo = MessageUtil.getMessage("XHD-000072");
//                addErrorMessage(kikakuchiSuisoNoudo);
//            } else {
//                kikakuchiSuisoNoudo = StringUtil.nullToBlank(daJokenDataSuisoNoudo.get("KIKAKUCHI"));
//            }
//
//            List<FXHDD06> fxhdd06List = loadFxhdd06(queryRunnerDoc, this.lotno);
//            if (!checkFxhdd06Data(fxhdd06List)) {
//                return;
//            }
//
//            // 仕掛情報、条件情報エリア表示
//            setJouhoAreaData(sikakariData, koteimasData, kikakuchiOndo, kikakuchiSuisoNoudo);
//
//            // 入力エリア設定処理
//            setInputAreaData(fxhdd06List);
//
//            // 画面の使用可否制御
//            setSearchedDisabled(!fxhdd06List.isEmpty(), permitCode);
//
//            // 検索時のrevisionを設定
//            if (fxhdd06List.isEmpty()) {
//                this.searchedRev = getMaxRev(queryRunnerDoc, this.lotno);
//            } else {
//                this.searchedRev = fxhdd06List.get(0).getRev();
//            }
//
//        } catch (SQLException e) {
//            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
//            addErrorMessage("実行時エラー");
//
//        }
//    }
//
//    /**
//     * クリア処理確認
//     */
//    public void confirmClear() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("param1", "warning");
//        this.warnMessage = "ｸﾘｱします。よろしいですか？";
//        this.warnProcess = "clear";
//    }
//
//    /**
//     * クリア処理
//     */
//    public void doClear() {
//        formClear();
//    }
//
//    /**
//     * 削除処理確認
//     */
//    public void confirmDelete() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("param1", "warning");
//        this.warnMessage = "ﾃﾞｰﾀを削除します。よろしいですか？";
//        this.warnProcess = "delete";
//    }
//
//    /**
//     * 削除処理
//     */
//    public void doDelete() {
//
//        try {
//            // 処理前に各項目の背景色をクリア
//            clearInputBackGround();
//
//            // 削除処理
//            if (!deleteData()) {
//                return;
//            }
//            // 削除後コントロール制御
//            controlAfterDeletion();
//            
//            FacesMessage message
//                    = new FacesMessage(FacesMessage.SEVERITY_INFO, "削除しました。", null);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//
//        } catch (SQLException e) {
//            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
//            addErrorMessage("実行時エラー");
//        }
//    }
//
//    /**
//     * 登録処理確認
//     */
//    public void confirmRegist() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("param1", "warning");
//        this.warnMessage = "登録します。よろしいですか？";
//        this.warnProcess = "regist";
//    }
//
//    /**
//     * 登録処理確認
//     */
//    public void doRegist() {
//        try {
//            // 処理開始前にエラーの背景色を解除
//            clearInputBackGround();
//
//            // 入力チェック
//            if (!inputCheckRegist()) {
//                return;
//            }
//
//            List<FXHDD06> fxhdd06List = getRegistData();
//            // 重複チェック
//            if (!uniqueCheckGouki(fxhdd06List)) {
//                addErrorMessage(MessageUtil.getMessage("XHD-000094"));
//                return;
//            }
//
//            // 登録処理
//            if (!registData(fxhdd06List)) {
//                return;
//            }
//
//            // 実行後ボタン制御
//            this.btnDeleteDisabled = false;
//            this.btnRegistDisabled = false;
//
//            // 完了メッセージを表示
//            FacesMessage message
//                    = new FacesMessage(FacesMessage.SEVERITY_INFO, "登録しました。", null);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//
//        } catch (SQLException e) {
//            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
//            addErrorMessage("実行時エラー");
//
//        } catch (NumberFormatException e) {
//            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);
//            addErrorMessage("実行時エラー");
//
//        }
//
//    }
//
//    /**
//     * 警告OK選択時処理
//     */
//    public void processWarnOk() {
//        switch (this.warnProcess) {
//            case "clear":
//                doClear();
//                break;
//            case "delete":
//                doDelete();
//                break;
//            case "regist":
//                doRegist();
//                break;
//            case "research":
//                formClear();
//                doSearch();
//                break;
//        }
//
//    }
//
//    /**
//     * 文字列を桁数でカットします。
//     *
//     * @param fieldName フィールド
//     * @param length 桁数
//     */
//    public void checkByte(String fieldName, int length) {
//        try {
//            if (length < 1) {
//                return;
//            }
//
//            // 指定フィールドの値を取得する
//            Field f = this.getClass().getDeclaredField(fieldName);
//            f.setAccessible(true);
//            Object value = f.get(this);
//
//            // 切り捨て処理
//            String cutValue = StringUtil.left(value.toString(), length);
//
//            // 値をセット
//            f.set(this, cutValue);
//        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
//            // 処理無し
//        }
//    }
//
//    /**
//     * クリア処理
//     */
//    private void formClear() {
//        // 検索エリア使用可否
//        this.searchAreaDisabled = false;
//        // 検索ボタン使用可否
//        this.btnSearchDisabled = false;
//        // 条件情報(温度)
//        this.jokenOndo = "";
//        // 条件情報(水素濃度)
//        this.jokenSuisoNoudo = "";
//        // 仕掛情報(KCPNO)
//        this.sikakariKcpno = "";
//        // 仕掛情報(工程)
//        this.sikakariKotei = "";
//        // 仕掛情報(数量)
//        this.sikakariSuryo = "";
//        // 入力エリア使用不可
//        this.inputAreaReadOnly = true;
//        // 入力エリア使用不可
//        this.inputAreaDisabled = true;
//        // 号機1
//        this.gouki1 = "";
//        // ピーク温度1
//        this.peakOndo1 = "";
//        // 水素濃度1
//        this.suisoNoudo1 = "";
//        // 号機2
//        this.gouki2 = "";
//        // ピーク温度2
//        this.peakOndo2 = "";
//        // 水素濃度2
//        this.suisoNoudo2 = "";
//        // 号機3
//        this.gouki3 = "";
//        // ピーク温度3
//        this.peakOndo3 = "";
//        // 水素濃度3
//        this.suisoNoudo3 = "";
//        // 号機4
//        this.gouki4 = "";
//        // ピーク温度4
//        this.peakOndo4 = "";
//        // 水素濃度4
//        this.suisoNoudo4 = "";
//        // 号機5
//        this.gouki5 = "";
//        // ピーク温度5
//        this.peakOndo5 = "";
//        // 水素濃度5
//        this.suisoNoudo5 = "";
//        // クリアボタン使用可否
//        this.btnClearDisabled = false;
//        // 削除ボタン使用可否
//        this.btnDeleteDisabled = true;
//        // 登録ボタン使用可否
//        this.btnRegistDisabled = true;
//
//        // 各項目の背景色をクリア
//        clearInputBackGround();
//    }
//
//    /**
//     * 入力チェック(検索時)
//     *
//     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
//     * @return true:チェックOK、false:チェックNG
//     * @throws SQLException 例外エラー
//     */
//    private boolean inputCheckSearch(QueryRunner queryRunnerWip) throws SQLException {
//
//        // 入力チェック処理
//        ValidateUtil validateUtil = new ValidateUtil();
//
//        // ロットNo
//        if (StringUtil.isEmpty(this.lotno)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000003", "ﾛｯﾄNo"));
//            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
//            return false;
//        } else if (existError(validateUtil.checkC101(this.lotno, "ﾛｯﾄNo", 14))) {
//            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
//            return false;
//        } else if (existError(validateUtil.checkValueE001(this.lotno))) {
//            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
//            return false;
//        }
//
//        // 指示者
//        if (StringUtil.isEmpty(this.shijisya)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000003", "指示者"));
//            this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
//            return false;
//        } else if (existError(validateUtil.checkT002("指示者", this.shijisya, queryRunnerWip))) {
//            this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * エラーチェック： エラーが存在する場合ポップアップ用メッセージをセットする
//     *
//     * @param errorMessage エラーメッセージ
//     * @return エラーが存在する場合true
//     */
//    private boolean existError(String errorMessage) {
//        if (StringUtil.isEmpty(errorMessage)) {
//            return false;
//        }
//
//        addErrorMessage(errorMessage);
//        return true;
//    }
//
//    /**
//     * 仕掛データ検索
//     *
//     * @param queryRunnerWip QueryRunnerオブジェクト
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
//        String lotNo1 = lotNo.substring(0, 3);
//        String lotNo2 = lotNo.substring(3, 11);
//        String lotNo3 = lotNo.substring(11, 14);
//
//        // 仕掛情報データの取得
//        String sql = "SELECT kcpno, koteicode, suuryo, opencloseflag"
//                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
//
//        List<Object> params = new ArrayList<>();
//        params.add(lotNo1);
//        params.add(lotNo2);
//        params.add(lotNo3);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
//    }
//
//    /**
//     * 工程コードマスタデータ検索
//     *
//     * @param queryRunnerWip QueryRunnerオブジェクト
//     * @param koteiCode 工程コード(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private Map loadKoteimas(QueryRunner queryRunnerWip, String koteiCode) throws SQLException {
//        // 工程マスタ情報データの取得
//        String sql = "SELECT kotei"
//                + " FROM koteimas WHERE koteicode = ? ";
//
//        List<Object> params = new ArrayList<>();
//        params.add(koteiCode);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
//    }
//
//    /**
//     * パラメータマスタデータ検索
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private List<Map<String, Object>> loadFxhbm03DataList(QueryRunner queryRunnerDoc) throws SQLException {
//
//        // パラメータデータの取得
//        String sql = "SELECT key, data"
//                + " FROM fxhbm03 WHERE user_name = ? AND key IN(?,?) ";
//        List<Object> params = new ArrayList<>();
//        params.add("syosei_user");
//        params.add("xhd_syosei_ondotouroku_kyoka_kokteicode1");
//        params.add("xhd_syosei_ondotouroku_kyoka_kokteicode2");
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//
//        return queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
//
//    }
//
//    /**
//     * [設計]から情報を取得
//     *
//     * @param queryRunnerQcdb QueryRunnerオブジェクト
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
//        String lotNo1 = lotNo.substring(0, 3);
//        String lotNo2 = lotNo.substring(3, 11);
//        // 設計データの取得
//        String sql = "SELECT SEKKEINO "
//                + "FROM da_sekkei "
//                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";
//
//        List<Object> params = new ArrayList<>();
//        params.add(lotNo1);
//        params.add(lotNo2);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
//    }
//
//    /**
//     * 規格値取得
//     *
//     * @param queryRunnerQcdb QueryRunnerオブジェクト
//     * @param sekkeino 設計No
//     * @param koteimei 工程名
//     * @param koumokumei 項目名
//     * @param kanrikoumoku 管理項目
//     * @return 規格値
//     * @throws SQLException 例外エラー
//     */
//    private Map getDaJokenData(QueryRunner queryRunnerQcdb, String sekkeino, String koteimei, String koumokumei, String kanrikoumoku) throws SQLException {
//        String sql = "SELECT KIKAKUCHI "
//                + "  FROM da_joken "
//                + " WHERE SEKKEINO = ? AND KOUTEIMEI = ? AND KOUMOKUMEI = ? AND KANRIKOUMOKU = ? ";
//
//        List<Object> params = new ArrayList<>();
//        params.add(sekkeino);
//        params.add(koteimei);
//        params.add(koumokumei);
//        params.add(kanrikoumoku);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
//    }
//
//    /**
//     * [指示温度]から、ﾃﾞｰﾀを取得
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private List<FXHDD06> loadFxhdd06(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
//        String lotNo1 = lotNo.substring(0, 3);
//        String lotNo2 = lotNo.substring(3, 11);
//        String lotNo3 = lotNo.substring(11, 14);
//
//        String sql = "SELECT goukijyoho,shijiondo,suisonoudo,shijiondogroup,rev "
//                + "FROM fxhdd06 "
//                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? "
//                + "ORDER BY shijiondogroup, goukijyoho";
//
//        List<Object> params = new ArrayList<>();
//        params.add(lotNo1);
//        params.add(lotNo2);
//        params.add(lotNo3);
//        params.add(0);
//
//        Map<String, String> mapping = new HashMap<>();
//        mapping.put("goukijyoho", "goukijyoho"); //号機情報
//        mapping.put("shijiondo", "shijiondo"); //指示温度
//        mapping.put("suisonoudo", "suisonoudo"); //水素濃度
//        mapping.put("shijiondogroup", "shijiondogroup"); //指示温度ｸﾞﾙｰﾌﾟ
//        mapping.put("rev", "rev"); //REV
//
//        BeanProcessor beanProcessor = new BeanProcessor(mapping);
//        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
//        ResultSetHandler<List<FXHDD06>> beanHandler = new BeanListHandler<>(FXHDD06.class, rowProcessor);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerDoc.query(sql, beanHandler, params.toArray());
//    }
//
//    /**
//     * 指示温度から削除ﾌﾗｸﾞのMAX値を取得
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @param edaban 枝番(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private int getMaxDeleteFlag(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
//            String edaban) throws SQLException {
//        int deleteflag = -1;
//        // 設計データの取得
//        String sql = "SELECT MAX(deleteflag) AS deleteflag "
//                + "FROM fxhdd06 "
//                + "WHERE kojyo = ? AND lotno = ? "
//                + "AND edaban = ? ";
//
//        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban));
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        Map map = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
//        if (map != null && !map.isEmpty() && map.get("deleteflag") != null) {
//            deleteflag = Integer.parseInt(map.get("deleteflag").toString());
//        }
//
//        return deleteflag;
//    }
//
//    /**
//     * 指示温度からRevのMAX値を取得
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @param conDoc コネクション
//     * @param lotNo ﾛｯﾄNo(フル桁)(検索キー)
//     * @return 取得データ
//     * @throws SQLException 例外エラー
//     */
//    private int getMaxRev(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
//        String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
//        String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
//        String edaban = lotNo.substring(11, 14); //枝番
//
//        int rev = 0;
//        // 設計データの取得
//        String sql = "SELECT MAX(rev) AS rev "
//                + "FROM fxhdd06 "
//                + "WHERE kojyo = ? AND lotno = ? "
//                + "AND edaban = ? ";
//
//        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo8, edaban));
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        Map map = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
//        if (map != null && !map.isEmpty() && map.get("rev") != null) {
//            rev = Integer.parseInt(map.get("rev").toString());
//        }
//
//        return rev;
//    }
//
//    /**
//     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
//     *
//     * @param map マップ
//     * @param mapId ID
//     * @return マップから取得した値
//     */
//    private Object getMapData(Map map, String mapId) {
//        if (map == null || map.isEmpty()) {
//            return null;
//        }
//        return map.get(mapId);
//    }
//
//    /**
//     * 仕掛データチェック
//     *
//     * @param sikakariData 仕掛データ
//     * @return true:チェックOK、false:チェックNG
//     */
//    private boolean checkSikakariData(Map sikakariData) {
//
//        if (sikakariData == null) {
//            // 仕掛データが取得できない場合はエラー
//            addErrorMessage(MessageUtil.getMessage("XHD-000029"));
//            return false;
//        } else if ("0".equals(StringUtil.nullToBlank(getMapData(sikakariData, "opencloseflag")))) {
//            // オープンクローズフラグが"0"クローズの場合エラー
//            addErrorMessage(MessageUtil.getMessage("XHD-000082"));
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 温度登録許可工程チェック
//     *
//     * @param queryRunnerオブジェクト
//     * @param 工程コード
//     * @return 0:許可工程以外、1：許可工程、9：エラー
//     */
//    private String checkPermitProcess(QueryRunner queryRunnerDoc, String koteicode) throws SQLException {
//
//        // パラメータマスタデータ取得
//        List<Map<String, Object>> fxhbm03DataList = loadFxhbm03DataList(queryRunnerDoc);
//        // 該当データが存在しない場合
//        if (fxhbm03DataList.isEmpty()) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000001"));
//            return "9";
//        }
//
//        // パラメータマスタより取得したパラメータデータをカンマ区切りで分割し保持する。
//        List<String> permitProcessList = new ArrayList<>();
//        for (Map<String, Object> fxhdm03Data : fxhbm03DataList) {
//            String data = StringUtil.nullToBlank(getMapData(fxhdm03Data, "data"));
//
//            // データに"ALL"が入っていた場合、許可工程
//            if ("ALL".equals(data)) {
//                return "1";
//            }
//            // データをカンマ区切りに分解
//            String[] spData = data.split(",");
//            for (String pKotei : spData) {
//                permitProcessList.add(StringUtil.trimAll(pKotei));
//            }
//        }
//
//        // 分割したパラメータデータに対象の工程コードが存在しない場合
//        if (!permitProcessList.contains(koteicode)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000084"));
//            return "0";
//        }
//
//        return "1";
//    }
//
//    /**
//     * 指示温度データチェック
//     *
//     * @param fxhdd06List 指示温度データリスト
//     * @return true:チェックOK、false:チェックNG
//     */
//    private boolean checkFxhdd06Data(List<FXHDD06> fxhdd06List) {
//
//        int group = 0;
//        int sijiOndo = 0;
//        BigDecimal suisoNoudo = null;
//        for (FXHDD06 fxhdd06 : fxhdd06List) {
//            if (group == 0 || group != fxhdd06.getShijiondogroup().intValue()) {
//                group = fxhdd06.getShijiondogroup().intValue();
//                sijiOndo = fxhdd06.getShijiondo().intValue();
//                suisoNoudo = fxhdd06.getSuisonoudo();
//                continue;
//            }
//
//            if (fxhdd06.getShijiondo().intValue() != sijiOndo) {
//                addErrorMessage(MessageUtil.getMessage("XHD-000079"));
//                return false;
//            }
//
//            if (fxhdd06.getSuisonoudo().compareTo(suisoNoudo) != 0) {
//                addErrorMessage(MessageUtil.getMessage("XHD-000080"));
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 指示温度データチェック
//     *
//     * @param fxhdd06List 指示温度データリスト
//     */
//    private void setInputAreaData(List<FXHDD06> fxhdd06List) {
//
//        // 1行目データセット
//        String[] groupData1 = getGroupData(fxhdd06List, 1);
//        this.gouki1 = groupData1[0];
//        this.peakOndo1 = groupData1[1];
//        this.suisoNoudo1 = groupData1[2];
//
//        // 2行目データセット
//        String[] groupData2 = getGroupData(fxhdd06List, 2);
//        this.gouki2 = groupData2[0];
//        this.peakOndo2 = groupData2[1];
//        this.suisoNoudo2 = groupData2[2];
//
//        // 3行目データセット
//        String[] groupData3 = getGroupData(fxhdd06List, 3);
//        this.gouki3 = groupData3[0];
//        this.peakOndo3 = groupData3[1];
//        this.suisoNoudo3 = groupData3[2];
//
//        // 4行目データセット
//        String[] groupData4 = getGroupData(fxhdd06List, 4);
//        this.gouki4 = groupData4[0];
//        this.peakOndo4 = groupData4[1];
//        this.suisoNoudo4 = groupData4[2];
//
//        // 5行目データセット
//        String[] groupData5 = getGroupData(fxhdd06List, 5);
//        this.gouki5 = groupData5[0];
//        this.peakOndo5 = groupData5[1];
//        this.suisoNoudo5 = groupData5[2];
//
//    }
//
//    /**
//     * 指示温度データグループ別データ取得 ※グループ別にまとめて1レコードとしてデータを取得、号機情報については
//     * 値がそれぞれ異なるため、結合して値を返す
//     *
//     * @param fxhdd06List 指示温度データリスト
//     */
//    private String[] getGroupData(List<FXHDD06> fxhdd06List, Integer getgroup) {
//        String shijiOndo = "";
//        String suisoNoudo = "";
//        StringBuilder goukiJouhoGroup = new StringBuilder();
//        int goukiJouho;
//        int sabun;
//        int prevGoukiJouho = 0;
//        int renbanCount = 0;
//
//        for (FXHDD06 fxhdd06 : fxhdd06List) {
//            // 一致しないグループについてはコンティニュー
//            if (!getgroup.equals(fxhdd06.getShijiondogroup())) {
//                continue;
//            }
//
//            // 号機情報を数値化
//            try {
//                goukiJouho = Integer.parseInt(StringUtil.nullToBlank(fxhdd06.getGoukijyoho()));
//            } catch (NumberFormatException e) {
//                // ※エラーになることはありえないはずだが念の為、例外対応
//                continue;
//            }
//
//            // 初回データセット
//            if (0 == goukiJouhoGroup.length()) {
//                //指示温度
//                shijiOndo = StringUtil.nullToBlank(fxhdd06.getShijiondo());
//                //水素濃度
//                suisoNoudo = StringUtil.nullToBlank(fxhdd06.getSuisonoudo());
//                // 号機情報
//                goukiJouhoGroup.append(goukiJouho);
//                prevGoukiJouho = goukiJouho;
//                continue;
//            }
//
//            // 号機情報の前回値の差分を比較
//            sabun = goukiJouho - prevGoukiJouho;
//
//            // 差分が1の場合
//            if (sabun == 1) {
//                // 連番が続いている場合
//                renbanCount++;
//            } else if (1 < sabun) {
//                if (1 < renbanCount) {
//                    // 3つ以上の連番が途切れた場合
//                    //前回までの連番分を追加
//                    goukiJouhoGroup.append("～");
//                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
//                    renbanCount = 0;
//                } else if (1 == renbanCount) {
//                    // 1回の連番が途切れた場合
//                    goukiJouhoGroup.append(",");
//                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
//                    renbanCount = 0;
//                }
//
//                //今回の号機情報を追加
//                goukiJouhoGroup.append(",");
//                goukiJouhoGroup.append(StringUtil.nullToBlank(goukiJouho));
//            }
//            //今回の号機情報を前回値にセット
//            prevGoukiJouho = goukiJouho;
//        }
//
//        if (1 < renbanCount) {
//            // 3つ以上の連番が途切れた場合
//            //前回までの連番分を追加
//            goukiJouhoGroup.append("～");
//            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
//        } else if (1 == renbanCount) {
//            // 1回の連番が途切れた場合
//            goukiJouhoGroup.append(",");
//            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
//        }
//
//        return new String[]{goukiJouhoGroup.toString(), shijiOndo, suisoNoudo};
//    }
//
//    /**
//     * 情報エリア値表示処理
//     *
//     * @param sikakariData 仕掛データ
//     * @param kikakuchiOndo 規格値(温度)
//     * @param kikakuchiSuisoNoudo 規格値(水素濃度)
//     */
//    private void setJouhoAreaData(Map sikakariData, Map koteimasData, String kikakuchiOndo, String kikakuchiSuisoNoudo) {
//        this.sikakariKcpno = StringUtil.nullToBlank(sikakariData.get("kcpno"));
//
//        String kotei = StringUtil.nullToBlank(getMapData(koteimasData, "kotei"));
//
//        this.sikakariKotei = StringUtil.nullToBlank(sikakariData.get("koteicode") + "(" + kotei + ")");
//
//        if (StringUtil.isEmpty(StringUtil.nullToBlank(sikakariData.get("suuryo")))) {
//            this.sikakariSuryo = "";
//        } else {
//            int suryo = Integer.parseInt(StringUtil.nullToBlank(sikakariData.get("suuryo")));
//            NumberFormat nfNum = NumberFormat.getNumberInstance();
//            this.sikakariSuryo = nfNum.format(suryo);
//        }
//        this.jokenOndo = kikakuchiOndo;
//        this.jokenSuisoNoudo = kikakuchiSuisoNoudo;
//    }
//
//    /**
//     * 検索後使用可否制御
//     *
//     * @param existShijiData データ有無
//     * @param permitCode 許可ｺｰﾄﾞ"1：登録許可"
//     */
//    private void setSearchedDisabled(boolean existShijiData, String permitCode) {
//        // 検索エリア使用可否
//        this.searchAreaDisabled = true;
//        // 検索ボタン使用可否
//        this.btnSearchDisabled = true;
//        // クリアボタン使用可否
//        this.btnClearDisabled = false;
//        // 入力エリア使用不可
//        this.inputAreaDisabled = false;
//
//        if ("1".equals(permitCode)) {
//            // 入力エリア使用不可
//            this.inputAreaReadOnly = false;
//
//            // 削除ボタン使用可否
//            if (existShijiData) {
//                this.btnDeleteDisabled = false;
//            } else {
//                this.btnDeleteDisabled = true;
//            }
//            // 登録ボタン使用可否
//            this.btnRegistDisabled = false;
//        } else {
//            // 入力エリア使用不可
//            this.inputAreaReadOnly = true;
//            // 削除ボタン入力不可
//            this.btnDeleteDisabled = true;
//            // 登録ボタン使用可否
//            this.btnRegistDisabled = true;
//        }
//    }
//
//    /**
//     * 削除後制御
//     *
//     */
//    private void controlAfterDeletion() {
//        // 入力項目をクリア
//        // 号機1
//        this.gouki1 = "";
//        // ピーク温度1
//        this.peakOndo1 = "";
//        // 水素濃度1
//        this.suisoNoudo1 = "";
//        // 号機2
//        this.gouki2 = "";
//        // ピーク温度2
//        this.peakOndo2 = "";
//        // 水素濃度2
//        this.suisoNoudo2 = "";
//        // 号機3
//        this.gouki3 = "";
//        // ピーク温度3
//        this.peakOndo3 = "";
//        // 水素濃度3
//        this.suisoNoudo3 = "";
//        // 号機4
//        this.gouki4 = "";
//        // ピーク温度4
//        this.peakOndo4 = "";
//        // 水素濃度4
//        this.suisoNoudo4 = "";
//        // 号機5
//        this.gouki5 = "";
//        // ピーク温度5
//        this.peakOndo5 = "";
//        // 水素濃度5
//        this.suisoNoudo5 = "";
//        // 登録ボタン使用可否
//        this.btnRegistDisabled = false;
//        // 削除ボタン使用可否
//        this.btnDeleteDisabled = true;
//
//    }
//
//    /**
//     * 入力チェック(登録時)
//     *
//     * @return true:チェックOK、false:チェックNG
//     * @throws SQLException 例外エラー
//     */
//    private boolean inputCheckRegist() {
//
//        // 入力項目が全項目未入力の場合エラー
//        if (!hasEntry()) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000093"));
//            return false;
//        }
//
//        // グループ1入力チェック
//        if (!checkGroupEntry(this.gouki1, this.peakOndo1, this.suisoNoudo1)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
//            setErrorBackGround(new String[]{"gouki1", "peakOndo1", "suisoNoudo1"});
//            return false;
//        }
//
//        // グループ2入力チェック
//        if (!checkGroupEntry(this.gouki2, this.peakOndo2, this.suisoNoudo2)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
//            setErrorBackGround(new String[]{"gouki2", "peakOndo2", "suisoNoudo2"});
//            return false;
//        }
//
//        // グループ3入力チェック
//        if (!checkGroupEntry(this.gouki3, this.peakOndo3, this.suisoNoudo3)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
//            setErrorBackGround(new String[]{"gouki3", "peakOndo3", "suisoNoudo3"});
//            return false;
//        }
//
//        // グループ4入力チェック
//        if (!checkGroupEntry(this.gouki4, this.peakOndo4, this.suisoNoudo4)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
//            setErrorBackGround(new String[]{"gouki4", "peakOndo4", "suisoNoudo4"});
//            return false;
//        }
//
//        // グループ5入力チェック
//        if (!checkGroupEntry(this.gouki5, this.peakOndo5, this.suisoNoudo5)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
//            setErrorBackGround(new String[]{"gouki5", "peakOndo5", "suisoNoudo5"});
//            return false;
//        }
//
//        // グループ1入力値チェック
//        if (!checkGroupValue(this.gouki1, this.peakOndo1, this.suisoNoudo1, "1", "①")) {
//            //エラー時はリターン(エラーは関数内部でセット)
//            return false;
//        }
//
//        // グループ2入力値チェック
//        if (!checkGroupValue(this.gouki2, this.peakOndo2, this.suisoNoudo2, "2", "②")) {
//            //エラー時はリターン(エラーは関数内部でセット)
//            return false;
//        }
//
//        // グループ3入力値チェック
//        if (!checkGroupValue(this.gouki3, this.peakOndo3, this.suisoNoudo3, "3", "③")) {
//            //エラー時はリターン(エラーは関数内部でセット)
//            return false;
//        }
//
//        // グループ4入力値チェック
//        if (!checkGroupValue(this.gouki4, this.peakOndo4, this.suisoNoudo4, "4", "④")) {
//            //エラー時はリターン(エラーは関数内部でセット)
//            return false;
//        }
//
//        // グループ5入力値チェック
//        if (!checkGroupValue(this.gouki5, this.peakOndo5, this.suisoNoudo5, "5", "⑤")) {
//            //エラー時はリターン(エラーは関数内部でセット)
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 入力項目の入力チェック
//     *
//     * @return true:チェックOK、false:チェック:NG
//     */
//    private boolean hasEntry() {
//
//        if (!StringUtil.isEmpty(this.gouki1) || !StringUtil.isEmpty(this.gouki2)
//                || !StringUtil.isEmpty(this.gouki3) || !StringUtil.isEmpty(this.gouki4)
//                || !StringUtil.isEmpty(this.gouki5)
//                || !StringUtil.isEmpty(this.peakOndo1) || !StringUtil.isEmpty(this.peakOndo2)
//                || !StringUtil.isEmpty(this.peakOndo3) || !StringUtil.isEmpty(this.peakOndo4)
//                || !StringUtil.isEmpty(this.peakOndo5)
//                || !StringUtil.isEmpty(this.suisoNoudo1) || !StringUtil.isEmpty(this.suisoNoudo2)
//                || !StringUtil.isEmpty(this.suisoNoudo3) || !StringUtil.isEmpty(this.suisoNoudo4)
//                || !StringUtil.isEmpty(this.suisoNoudo5)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * グループ単位で全て入力されているかチェック
//     *
//     * @return true:チェックOK、false:チェック:NG
//     */
//    private boolean checkGroupEntry(String gouki, String peakOndo, String suisoNoudo) {
//
//        // いずれも入力されていない場合はリターン
//        if (StringUtil.isEmpty(gouki) && StringUtil.isEmpty(peakOndo) && StringUtil.isEmpty(suisoNoudo)) {
//            return true;
//        }
//
//        //一つでも空があればエラー
//        if (StringUtil.isEmpty(gouki) || StringUtil.isEmpty(peakOndo) || StringUtil.isEmpty(suisoNoudo)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * //エラーメッセージ追加処理
//     *
//     * @param errorMessage エラーメッセージ
//     */
//    private void addErrorMessage(String errorMessage) {
//        FacesMessage message
//                = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    }
//
//    /**
//     * 号機文字チェック
//     *
//     * @param gouki 号機
//     * @return 号機形式チェック
//     */
//    private String checkGouki(String gouki) {
//
//        // 号機未入力時はチェックしない
//        if (StringUtil.isEmpty(gouki)) {
//            return "";
//        }
//        try {
//
//            // カンマで分割
//            String spGouki[] = gouki.split(",");
//            for (String goukiVal : spGouki) {
//
//                if (goukiVal.contains("～")) {
//                    // 分割が2分割になってなければエラー
//                    String chkValues[] = goukiVal.split("～");
//                    if (chkValues.length != 2) {
//                        // 形式エラー
//                        return "XHD-000090";
//                    }
//
//                    // 前方後方の値を数値化
//                    int val1 = Integer.parseInt(chkValues[0]);
//                    int val2 = Integer.parseInt(chkValues[1]);
//
//                    if (!checkRange(val1, 1, 99) || !checkRange(val2, 1, 99)) {
//                        // 数値範囲チェック
//                        return "XHD-000090";
//                    } else if ((val2 - val1) < 2) {
//                        // 範囲指定エラー
//                        return "XHD-000095";
//                    }
//
//                } else {
//                    if (!checkRange(Integer.parseInt(goukiVal), 1, 99)) {
//                        // 数値範囲エラー
//                        return "XHD-000090";
//                    }
//                }
//
//            }
//        } catch (NumberFormatException e) {
//            // 数値変換する変換できなかった場合形式エラー
//            return "XHD-000090";
//        }
//
//        return "";
//    }
//
//    /**
//     * グループ単位で入力されている値をチェック
//     *
//     * @param gouki 号機
//     * @param peakOndo ピーク温度
//     * @param suisoNoudo 水素濃度
//     * @param rowNoLable 行No
//     * @return true:
//     */
//    private boolean checkGroupValue(String gouki, String peakOndo, String suisoNoudo, String rowNo, String rowNoLable) {
//        String errorCode;
//        // 号機チェック
//        errorCode = checkGouki(gouki);
//        if (!StringUtil.isEmpty(errorCode)) {
//            addErrorMessage(MessageUtil.getMessage(errorCode));
//            setErrorBackGround(new String[]{"gouki" + rowNo});
//            return false;
//        }
//
//        // ピーク温度整数チェック
//        if (!checkIntegral(peakOndo)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000091", "ﾋﾟｰｸ温度" + rowNoLable));
//            setErrorBackGround(new String[]{"peakOndo" + rowNo});
//            return false;
//        }
//
//        // 水素濃度整数チェック
//        if (!checkIntegral(suisoNoudo)) {
//            addErrorMessage(MessageUtil.getMessage("XHD-000091", "水素濃度" + rowNoLable));
//            setErrorBackGround(new String[]{"suisoNoudo" + rowNo});
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 指定の項目の背景色を変更
//     *
//     * @param itemList 項目リスト
//     */
//    private void setErrorBackGround(String[] itemList) {
//        for (String item : itemList) {
//            switch (item) {
//                case "lotno":
//                    this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "shijisya":
//                    this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "gouki1":
//                    this.gouki1BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "peakOndo1":
//                    this.peakOndo1BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "suisoNoudo1":
//                    this.suisoNoudo1BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "gouki2":
//                    this.gouki2BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "peakOndo2":
//                    this.peakOndo2BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "suisoNoudo2":
//                    this.suisoNoudo2BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "gouki3":
//                    this.gouki3BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "peakOndo3":
//                    this.peakOndo3BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "suisoNoudo3":
//                    this.suisoNoudo3BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "gouki4":
//                    this.gouki4BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "peakOndo4":
//                    this.peakOndo4BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "suisoNoudo4":
//                    this.suisoNoudo4BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "gouki5":
//                    this.gouki5BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "peakOndo5":
//                    this.peakOndo5BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//                case "suisoNoudo5":
//                    this.suisoNoudo5BackGround = ErrUtil.ERR_BACK_COLOR;
//                    break;
//            }
//
//        }
//
//    }
//
//    /**
//     * 整数チェック
//     *
//     * @param value 値
//     * @return ture:整数(1以上)、false:それ以外
//     */
//    private boolean checkIntegral(String value) {
//        //値が空の場合は
//        if (StringUtil.isEmpty(value)) {
//            return true;
//        }
//        BigDecimal decValue;
//        try {
//            decValue = new BigDecimal(value);
//        } catch (NumberFormatException e) {
//            return false;
//        }
//
//        // 値が0以下の場合エラー
//        if (0 <= BigDecimal.ZERO.compareTo(decValue)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 値が指定範囲内か確認する。
//     *
//     * @param value 値
//     * @param minValue 最小値
//     * @param maxValue 最大値
//     * @return true：範囲内、false：範囲外
//     */
//    private boolean checkRange(int value, int minValue, int maxValue) {
//
//        if (value < minValue || maxValue < value) {
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 削除処理
//     *
//     * @param processData 処理制御データ
//     * @return 処理制御データ
//     */
//    private boolean deleteData() throws SQLException {
//
//        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
//        Connection conDoc = null;
//
//        try {
//            // トランザクション開始
//            //DocServer 
//            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
//
//            String kojyo = this.lotno.substring(0, 3); //工場ｺｰﾄﾞ
//            String lotNo8 = this.lotno.substring(3, 11); //ﾛｯﾄNo(8桁)
//            String edaban = this.lotno.substring(11, 14); //枝番
//
//            String checkId = checkRev(queryRunnerDoc);
//            // 正常値以外の場合
//            if (!"0".equals(checkId)) {
//                // コネクションロールバック処理
//                DBUtil.rollbackConnection(conDoc, LOGGER);
//
//                // 警告エラーの場合
//                if ("1".equals(checkId)) {
//                    // 警告メッセージをセットしてリターン
//                    RequestContext context = RequestContext.getCurrentInstance();
//                    context.addCallbackParam("param1", "warning");
//                    this.warnMessage = "ﾃﾞｰﾀが更新されています。画面を更新してもよろしいですか？";
//                    this.warnProcess = "research";
//                } else {
//                    // システムエラーをセットしてリターン
//                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000002"));
//                }
//                return false;
//            }
//            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
//
//            // 削除フラグの最大値を取得
//            int deleteflag = getMaxDeleteFlag(queryRunnerDoc, kojyo, lotNo8, edaban);
//            // 削除フラグが取得できなかった場合は想定外としてシステムエラー
//            if (-1 == deleteflag) {
//                // コネクションロールバック処理
//                DBUtil.rollbackConnection(conDoc, LOGGER);
//
//                // システムエラーをセットしてリターン
//                addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000003"));
//                return false;
//            }
//
//            // データ論理削除
//            logicalDeleteFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev, deleteflag + 1, systemTime);
//
//            DbUtils.commitAndCloseQuietly(conDoc);
//            
//            // リビジョンを更新
//            this.searchedRev += 1;
//
//        } catch (SQLException e) {
//
//            // コネクションロールバック処理
//            DBUtil.rollbackConnection(conDoc, LOGGER);
//            throw e;
//        }
//        return true;
//
//    }
//
//    /**
//     * 登録処理
//     *
//     * @param processData 処理制御データ
//     * @return 処理制御データ
//     */
//    private boolean registData(List<FXHDD06> fxhdd06List) throws SQLException {
//
//        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
//        Connection conDoc = null;
//
//        try {
//            // トランザクション開始
//            //DocServer 
//            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
//
//            String kojyo = this.lotno.substring(0, 3); //工場ｺｰﾄﾞ
//            String lotNo8 = this.lotno.substring(3, 11); //ﾛｯﾄNo(8桁)
//            String edaban = this.lotno.substring(11, 14); //枝番
//
//            String checkId = checkRev(queryRunnerDoc);
//            // 正常値以外の場合
//            if (!"0".equals(checkId)) {
//                // コネクションロールバック処理
//                DBUtil.rollbackConnection(conDoc, LOGGER);
//
//                // 警告エラーの場合
//                if ("1".equals(checkId)) {
//                    // 警告メッセージをセットしてリターン
//                    RequestContext context = RequestContext.getCurrentInstance();
//                    context.addCallbackParam("param1", "warning");
//                    this.warnMessage = "ﾃﾞｰﾀが更新されています。画面を更新してもよろしいですか？";
//                    this.warnProcess = "research";
//                } else {
//                    // システムエラーをセットしてリターン
//                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000002"));
//                }
//                return false;
//            }
//
//            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
//
//            // 既存データが存在する場合は論理削除
//            if (!this.btnDeleteDisabled) {
//
//                // 削除フラグの最大値を取得
//                int deleteflag = getMaxDeleteFlag(queryRunnerDoc, kojyo, lotNo8, edaban);
//                // 削除フラグが取得できなかった場合は想定外としてシステムエラー
//                if (-1 == deleteflag) {
//                    // コネクションロールバック処理
//                    DBUtil.rollbackConnection(conDoc, LOGGER);
//
//                    // システムエラーをセットしてリターン
//                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000003"));
//                    return false;
//                }
//
//                // データ論理削除
//                logicalDeleteFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev, deleteflag + 1, systemTime);
//                
//            }
//
//            // データ登録処理
//            insertFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev + 1, systemTime, fxhdd06List);
//
//            DbUtils.commitAndCloseQuietly(conDoc);
//
//            // リビジョンを更新
//            this.searchedRev += 1;
//
//        } catch (SQLException e) {
//
//            // コネクションロールバック処理
//            DBUtil.rollbackConnection(conDoc, LOGGER);
//            throw e;
//        }
//        return true;
//
//    }
//
//    /**
//     * 登録データ取得
//     *
//     * @return
//     */
//    private List<FXHDD06> getRegistData() {
//
//        List<FXHDD06> listAllGroup = new ArrayList<>();
//        listAllGroup.addAll(getGroupBetsuList(this.gouki1, this.peakOndo1, this.suisoNoudo1, "1"));
//        listAllGroup.addAll(getGroupBetsuList(this.gouki2, this.peakOndo2, this.suisoNoudo2, "2"));
//        listAllGroup.addAll(getGroupBetsuList(this.gouki3, this.peakOndo3, this.suisoNoudo3, "3"));
//        listAllGroup.addAll(getGroupBetsuList(this.gouki4, this.peakOndo4, this.suisoNoudo4, "4"));
//        listAllGroup.addAll(getGroupBetsuList(this.gouki5, this.peakOndo5, this.suisoNoudo5, "5"));
//        return listAllGroup;
//    }
//
//    /**
//     * グループ別の集約データを取得(号機"1","2","3","5","6","8","10","12"のデータを"1～3,5,6,8,10,12"のような形に集約
//     *
//     * @param gouki 号機
//     * @param peakOndo ピーク温度
//     * @param suisoNoudo 水素濃度
//     * @param group グループ
//     * @return 集約レコード
//     * @throws NumberFormatException 例外エラー
//     */
//    private List<FXHDD06> getGroupBetsuList(String gouki, String peakOndo, String suisoNoudo, String group) throws NumberFormatException {
//
//        List<FXHDD06> listGroup = new ArrayList<>();
//        // 入力が無ければ空のリストを返す。
//        if (StringUtil.isEmpty(gouki)) {
//            return listGroup;
//        }
//
//        String[] spGouki = gouki.split(",");
//        for (String goukiVal : spGouki) {
//
//            if (goukiVal.contains("～")) {
//                String chkValues[] = goukiVal.split("～");
//                // 前方後方の値を数値化
//                int startVal = Integer.parseInt(chkValues[0]);
//                int endVal = Integer.parseInt(chkValues[1]);
//                for (int i = startVal; i <= endVal; i++) {
//                    // リストにデータを追加
//                    listGroup.add(createFXHDD06(String.valueOf(i), peakOndo, suisoNoudo, group));
//                }
//
//            } else {
//                // リストにデータを追加
//                listGroup.add(createFXHDD06(goukiVal, peakOndo, suisoNoudo, group));
//            }
//        }
//
//        return listGroup;
//
//    }
//
//    /**
//     * 指定温度データを作成
//     *
//     * @param gouki 号機
//     * @param peakOndo ﾋﾟｰｸ温度
//     * @param suisoNoudo 水素濃度
//     * @param group グループ
//     * @return 指定温度データ
//     */
//    private FXHDD06 createFXHDD06(String gouki, String peakOndo, String suisoNoudo, String group) throws NumberFormatException {
//        FXHDD06 fxhdd06 = new FXHDD06();
//        fxhdd06.setGoukijyoho(Integer.parseInt(gouki));
//        fxhdd06.setShijiondo(Integer.parseInt(peakOndo));
//        fxhdd06.setSuisonoudo(new BigDecimal(suisoNoudo));
//        fxhdd06.setShijiondogroup(Integer.parseInt(group));
//        return fxhdd06;
//    }
//
//    /**
//     * 号機の重複チェック
//     *
//     * @param fxhdd06List 指示温度データ
//     * @return true:チェックOK、false:チェックNG
//     */
//    private boolean uniqueCheckGouki(List<FXHDD06> fxhdd06List) {
//        List<Integer> goukiList = new ArrayList<>();
//
//        for (FXHDD06 fxhdd06 : fxhdd06List) {
//            if (goukiList.contains(fxhdd06.getGoukijyoho())) {
//                return false;
//            }
//            goukiList.add(fxhdd06.getGoukijyoho());
//        }
//
//        return true;
//    }
//
//    /**
//     * リビジョンチェック
//     *
//     * @return　"0":正常、"1":警告エラー、"9":システムエラー
//     * @throws SQLException
//     */
//    private String checkRev(QueryRunner queryRunnerDoc) throws SQLException {
//
//        int maxRev = getMaxRev(queryRunnerDoc, this.lotno);
//        if (maxRev == 0) {
//            if (0 < this.searchedRev) {
//                // 想定外システムエラー
//                return "9";
//            }
//        } else if (this.searchedRev != maxRev) {
//            // 警告エラー
//            return "1";
//        }
//
//        // 正常
//        return "0";
//    }
//
//    /**
//     * 指示温度(fxhdd06)論理削除処理
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @param conDoc コネクション
//     * @param shijisya 指示者
//     * @param kojyo 工場
//     * @param lotNo ロットNo
//     * @param edaban 枝番
//     * @param rev rev
//     * @param deleteflag 削除フラグ
//     * @param systemTime システム日付
//     * @throws SQLException 例外エラー
//     *
//     */
//    private void logicalDeleteFxhdd06(QueryRunner queryRunnerDoc, Connection conDoc, String shijisya,
//            String kojyo, String lotNo, String edaban, int rev, int deleteflag, Timestamp systemTime) throws SQLException {
//        String sql = "UPDATE fxhdd06 SET "
//                + "koshin_tantousyacode = ?, "
//                + "koshin_date = ?,"
//                + "rev = ?, "
//                + "deleteflag = ? "
//                + "WHERE kojyo = ? "
//                + "  AND lotno = ? AND edaban = ? "
//                + "  AND rev = ? AND deleteflag = 0 ";
//
//        List<Object> params = new ArrayList<>();
//        // 更新内容
//        params.add(shijisya); //更新担当者ｺｰﾄﾞ
//        params.add(systemTime); //更新日
//        params.add(rev + 1); //rev
//        params.add(deleteflag); //削除フラグ
//
//        // 検索条件
//        params.add(kojyo); //工場ｺｰﾄﾞ
//        params.add(lotNo); //ﾛｯﾄNo
//        params.add(edaban); //枝番
//        params.add(rev); //rev
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        queryRunnerDoc.update(conDoc, sql, params.toArray());
//    }
//
//    /**
//     * 品質DB登録実績(fxhdd03)登録処理
//     *
//     * @param queryRunnerDoc QueryRunnerオブジェクト
//     * @param conDoc コネクション
//     * @param tantoshaCd 担当者ｺｰﾄﾞ
//     * @param formId 画面ID
//     * @param rev revision
//     * @param kojyo 工場ｺｰﾄﾞ
//     * @param lotNo ﾛｯﾄNo
//     * @param edaban 枝番
//     * @param jissekino 実績No
//     * @param jotaiFlg 状態ﾌﾗｸﾞ
//     * @param systemTime システム日付
//     * @throws SQLException 例外エラー
//     */
//    private void insertFxhdd06(QueryRunner queryRunnerDoc, Connection conDoc, String shijisya,
//            String kojyo, String lotNo, String edaban, int rev, Timestamp systemTime, List<FXHDD06> fxhdd06List) throws SQLException {
//        String sql = "INSERT INTO fxhdd06 ("
//                + "kojyo,lotno,edaban,tantousyacode,goukijyoho,shijiondo,suisonoudo,shijiondogroup,"
//                + "koshin_tantousyacode,toroku_date,koshin_date,rev,deleteflag"
//                + ") VALUES ("
//                + "?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//
//        for (FXHDD06 fxhdd06 : fxhdd06List) {
//            List<Object> params = new ArrayList<>();
//            params.add(kojyo); //工場ｺｰﾄﾞ
//            params.add(lotNo); //ﾛｯﾄNo
//            params.add(edaban); //枝番
//            params.add(shijisya); //担当者ｺｰﾄﾞ
//            params.add(fxhdd06.getGoukijyoho()); //号機情報
//            params.add(fxhdd06.getShijiondo()); //指示温度
//            params.add(fxhdd06.getSuisonoudo()); //水素濃度
//            params.add(fxhdd06.getShijiondogroup()); //指示温度ｸﾞﾙｰﾌﾟ
//            params.add(null); //更新担当者ｺｰﾄﾞ
//            params.add(systemTime); //登録日
//            params.add(null); //更新日
//            params.add(rev); //rev
//            params.add(0); //削除ﾌﾗｸﾞ
//
//            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//            queryRunnerDoc.update(conDoc, sql, params.toArray());
//        }
//
//    }
//
//    /**
//     * 入力項目のバックグランド値をクリア
//     */
//    private void clearInputBackGround() {
//        this.lotnoBackGround = "";
//        this.shijisyaBackGround = "";
//        this.gouki1BackGround = "";
//        this.peakOndo1BackGround = "";
//        this.suisoNoudo1BackGround = "";
//        this.gouki2BackGround = "";
//        this.peakOndo2BackGround = "";
//        this.suisoNoudo2BackGround = "";
//        this.gouki3BackGround = "";
//        this.peakOndo3BackGround = "";
//        this.suisoNoudo3BackGround = "";
//        this.gouki4BackGround = "";
//        this.peakOndo4BackGround = "";
//        this.suisoNoudo4BackGround = "";
//        this.gouki5BackGround = "";
//        this.peakOndo5BackGround = "";
//        this.suisoNoudo5BackGround = "";
//    }

    /**
     * 初期化処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    @Override
    public ProcessData initial(ProcessData processData) {
        try {

            // 処理名を登録
            processData.setProcessName("initial");

            // 初期表示データ設定処理
            processData = setInitData(processData);
            // 中断エラー発生時
            if (processData.isFatalError()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                }
                return processData;
            }

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, processData.getInitJotaiFlg());

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B039Const.BTN_START_DATETIME_TOP,
                    GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP,
                    GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B039Const.BTN_INSERT_TOP,
                    GXHDO101B039Const.BTN_DELETE_TOP,
                    GXHDO101B039Const.BTN_UPDATE_TOP,
                    GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B039Const.BTN_INSERT_BOTTOM,
                    GXHDO101B039Const.BTN_DELETE_BOTTOM,
                    GXHDO101B039Const.BTN_UPDATE_BOTTOM));

            // エラーが発生していない場合
            if (processData.getErrorMessageInfoList().isEmpty()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                } else {
                    // 後続処理なし
                    processData.setMethod("");
                }
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }

        // 後続処理メソッド設定
        processData.setMethod("doTempResist");

        return processData;

    }

    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempResist(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, paramJissekino);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ﾒｯｷ真空乾燥_仮登録登録処理
                insertTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            } else {

                // ﾒｯｷ真空乾燥_仮登録更新処理
                updateTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");
            //完了メッセージ設定
            processData.setInfoMessage("仮登録完了");

            // 背景色をクリア
            for (FXHDD01 fxhdd01 : processData.getItemList()) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }

            // 状態ﾌﾗｸﾞ、revisionを設定する。
            processData.setInitJotaiFlg(JOTAI_FLG_KARI_TOROKU);
            processData.setInitRev(newRev.toPlainString());
            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * 登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataResist(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }

        // 後続処理メソッド設定
        processData.setMethod("doResist");

        return processData;
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List = "";
        //KCPNO
        FXHDD01 itemKcpno = getItemRow(processData.getItemList(), GXHDO101B039Const.KCPNO);
        if (!StringUtil.isEmpty(itemKcpno.getValue()) && itemKcpno.getValue().length() > 9) {
            String kcpno9 = itemKcpno.getValue().substring(8, 9);
            // ﾊﾞﾚﾙ洗浄必須ﾁｪｯｸ処理
            Map fxhbm03Data21 = loadFxhbm03Data(queryRunnerDoc, 21);
            if (fxhbm03Data21 != null && !fxhbm03Data21.isEmpty()) {
                strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data21, "data"));
                String fxhbm03Data[] = strfxhbm03List.split(",");
                for (int i = 0; i < fxhbm03Data.length; i++) {
                    if (!StringUtil.isEmpty(kcpno9) && kcpno9.equals(fxhbm03Data[i])) {
                        // ﾊﾞﾚﾙ洗浄開始日
                        FXHDD01 itemBarrelkaishiday = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_DAY);
                        // ﾊﾞﾚﾙ洗浄開始時刻
                        FXHDD01 itemBarrelkaishitime = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_TIME);
                        // ﾊﾞﾚﾙ洗浄担当者
                        FXHDD01 itemBarrelkaishitantosya = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_TANTOSYA);
                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishiday.getValue()))) {
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishiday);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishiday.getLabel1());
                        }
                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitime.getValue()))) {
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitime);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitime.getLabel1());
                        }
                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitantosya.getValue()))) {
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitantosya);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitantosya.getLabel1());
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * 登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doResist(ProcessData processData) {
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = BigDecimal.ZERO;
            BigDecimal newRev = BigDecimal.ONE;

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrMksinkuukansou tmpSrMksinkuukansou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrMksinkuukansou> srMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srMksinkuukansouList.isEmpty()) {
                    tmpSrMksinkuukansou = srMksinkuukansouList.get(0);
                }

                deleteTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // ﾒｯｷ真空乾燥_登録処理
            insertSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrMksinkuukansou, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("登録しました。");
            processData.setCollBackParam("complete");
            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);

            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }

        }

        return processData;

    }

    /**
     * 修正処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataCorrect(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B039Const.USER_AUTH_UPDATE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");

        return processData;

    }

    /**
     * 修正処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCorrect(ProcessData processData) {
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 最新のリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);

            // ﾒｯｷ真空乾燥_更新処理
            updateSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("修正しました。");
            processData.setCollBackParam("complete");
            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * 削除処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataDelete(ProcessData processData) {

        // 警告メッセージの設定
        processData.setWarnMessage("削除します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B039Const.USER_AUTH_DELETE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doDelete");
        return processData;
    }

    /**
     * 削除処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doDelete(ProcessData processData) {
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 最新のリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ﾒｯｷ真空乾燥_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // ﾒｯｷ真空乾燥_削除処理
            deleteSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");
            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * ボタン活性・非活性設定
     *
     * @param processData 処理制御データ
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @return 処理制御データ
     */
    private ProcessData setButtonEnable(ProcessData processData, String jotaiFlg) {

        List<String> activeIdList = new ArrayList<>();
        List<String> inactiveIdList = new ArrayList<>();
        switch (jotaiFlg) {
            case JOTAI_FLG_TOROKUZUMI:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B039Const.BTN_DELETE_BOTTOM,
                        GXHDO101B039Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B039Const.BTN_DELETE_TOP,
                        GXHDO101B039Const.BTN_UPDATE_TOP,
                        GXHDO101B039Const.BTN_START_DATETIME_TOP,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B039Const.BTN_INSERT_BOTTOM,
                        GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B039Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B039Const.BTN_INSERT_BOTTOM,
                        GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B039Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B039Const.BTN_INSERT_TOP,
                        GXHDO101B039Const.BTN_START_DATETIME_TOP,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_DELETE_BOTTOM,
                        GXHDO101B039Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B039Const.BTN_DELETE_TOP,
                        GXHDO101B039Const.BTN_UPDATE_TOP));

                break;

        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);
        return processData;
    }

    /**
     * ボタンID⇒メソッド名変換
     *
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method;
        switch (buttonId) {
            // 仮登録
            case GXHDO101B039Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B039Const.BTN_INSERT_TOP:
            case GXHDO101B039Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B039Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B039Const.BTN_UPDATE_TOP:
            case GXHDO101B039Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B039Const.BTN_DELETE_TOP:
            case GXHDO101B039Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B039Const.BTN_START_DATETIME_TOP:
            case GXHDO101B039Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // ﾊﾞﾚﾙ洗浄開始日時
            case GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP:
            case GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM:
                method = "setBarrelstartDateTime";
                break;
            default:
                method = "error";
                break;
        }

        return method;
    }

    /**
     * 初期表示データ設定
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    private ProcessData setInitData(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        int paramJissekino = (Integer) session.getAttribute("jissekino");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        Map hiddenMap = processData.getHiddenDataMap();
        hiddenMap.put("lotkubuncode", lotkubuncode);
        hiddenMap.put("ownercode", ownercode);

        // ﾛｯﾄ区分ﾏｽﾀ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }

        // ｵｰﾅｰﾏｽﾀ情報の取得
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016"));
        }

        // 処理数の取得
        String syorisuu = null;

        //ﾃﾞｰﾀの取得
        String strfxhbm03List = "";

        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, 20);
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if (jissekiData != null && jissekiData.size() > 0) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
                if (dbShorisu > 0) {
                    syorisuu = String.valueOf(dbShorisu);

                }
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, syorisuu);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo, String syorisuu) {

        // ロットNo
        this.setItemData(processData, GXHDO101B039Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B039Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B039Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B039Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B039Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B039Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B039Const.OWNER, ownercode + ":" + owner);
        }

        //処理数
        this.setItemData(processData, GXHDO101B039Const.SYORISUU, syorisuu);

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @param jissekino 実績No
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino) throws SQLException {

        List<SrMksinkuukansou> srMksinkuukansouDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // メイン画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }
                return true;
            }

            // ﾒｯｷ真空乾燥データ取得
            srMksinkuukansouDataList = getSrMksinkuukansouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srMksinkuukansouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srMksinkuukansouDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srMksinkuukansouDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrMksinkuukansou srMksinkuukansouData) {

        // 号機
        this.setItemData(processData, GXHDO101B039Const.GOUKI, getSrMksinkuukansouItemData(GXHDO101B039Const.GOUKI, srMksinkuukansouData));
        // 乾燥時間
        this.setItemData(processData, GXHDO101B039Const.KANSOUJIKAN, getSrMksinkuukansouItemData(GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData));
        // 温度
        this.setItemData(processData, GXHDO101B039Const.ONDO, getSrMksinkuukansouItemData(GXHDO101B039Const.ONDO, srMksinkuukansouData));
        // 真空度
        this.setItemData(processData, GXHDO101B039Const.SINKUUDO, getSrMksinkuukansouItemData(GXHDO101B039Const.SINKUUDO, srMksinkuukansouData));
        // 開始日
        this.setItemData(processData, GXHDO101B039Const.KAISHI_DAY, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData));
        // 開始時間
        this.setItemData(processData, GXHDO101B039Const.KAISHI_TIME, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B039Const.KAISHI_TANTOSYA, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData));
        // 作業場所
        this.setItemData(processData, GXHDO101B039Const.SAGYOUBASYO, getSrMksinkuukansouItemData(GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄開始日
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_DAY, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄開始時刻
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_TIME, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄担当者
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData));
        // 備考1
        this.setItemData(processData, GXHDO101B039Const.BIKO1, getSrMksinkuukansouItemData(GXHDO101B039Const.BIKO1, srMksinkuukansouData));
        // 備考2
        this.setItemData(processData, GXHDO101B039Const.BIKO2, getSrMksinkuukansouItemData(GXHDO101B039Const.BIKO2, srMksinkuukansouData));

    }

    /**
     * ﾒｯｷ真空乾燥の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ﾒｯｷ真空乾燥登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrMksinkuukansou> getSrMksinkuukansouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrMksinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrMksinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
    }

    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        String sql = "SELECT SEKKEINO "
                + "FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 設計データ関連付けマップ取得
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiAssociation() {
        Map<String, String> map = new LinkedHashMap<>();
        return map;
    }

    /**
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotKubunCode ﾛｯﾄ区分ｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerDoc, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT lotkubun "
                + "FROM lotkumas "
                + "WHERE lotkubuncode = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param ownerCode ｵｰﾅｰｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadOwnerMas(QueryRunner queryRunnerWip, String ownerCode) throws SQLException {

        // オーナーデータの取得
        String sql = "SELECT \"owner\" AS ownername "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        List<String> dataList = new ArrayList<>(Arrays.asList(data));

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT syorisuu "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";

        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());

        sql += " ORDER BY syoribi DESC, syorijikoku DESC";

        Map mapping = new HashMap<>();
        mapping.put("syorisuu", "syorisuu");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<Jisseki>> beanHandler = new BeanListHandler<>(Jisseki.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.addAll(dataList);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param selectNo 処理区分
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, Integer selectNo) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = 'common_user' AND ";
            if (selectNo == 20) {
                sql = sql + " key = '工程コード_ﾒｯｷ社内' ";
            }
            if (selectNo == 21) {
                sql = sql + " key = 'xhd_外部電極ﾒｯｷ真空乾燥_ﾊﾞﾚﾙ洗浄_必須判定ｺｰﾄﾞ' ";
            }
            return queryRunnerDoc.query(sql, new MapHandler());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;

    }

    /**
     * 仕掛データ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, lotpre, tanijuryo "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
    }

    /**
     * 最大リビジョン+1のデータを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }

    /**
     * [ﾒｯｷ真空乾燥]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrMksinkuukansou> loadSrMksinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,"
                + " KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,tokuisaki ,lotkubuncode ,ownercode ,"
                + " syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya,bikou2 ,revision ,'0' AS deleteflag "
                + "FROM sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("GoukiCode", "goukicode"); //号機
        mapping.put("Kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LotNo", "lotno"); //ﾛｯﾄNo
        mapping.put("EdaBan", "edaban"); //枝番
        mapping.put("Syorikaisuu", "syorikaisuu"); //回数
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TantousyaCode", "tantousyacode"); //開始担当者
        mapping.put("KansouJikan", "kansoujikan"); //乾燥時間
        mapping.put("Ondo", "ondo"); //温度
        mapping.put("Sinkuudo", "sinkuudo"); //真空度
        mapping.put("KaisiNichiji", "kaisinichiji"); //開始日時
        mapping.put("Bikou", "bikou"); //備考1
        mapping.put("TourokuNichiji", "tourokunichiji"); //登録日時
        mapping.put("KousinNichiji", "kousinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syorisuu", "syorisuu"); //処理数
        mapping.put("sagyoubasyo", "sagyoubasyo"); //作業場所
        mapping.put("barrelkaishinichiji", "barrelkaishinichiji"); //ﾊﾞﾚﾙ洗浄開始日時
        mapping.put("barreltantousya", "barreltantousya"); //ﾊﾞﾚﾙ洗浄担当者
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMksinkuukansou>> beanHandler = new BeanListHandler<>(SrMksinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾒｯｷ真空乾燥_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrMksinkuukansou> loadTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                + " Sinkuudo ,KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,"
                + " tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision ,deleteflag "
                + "FROM tmp_sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? AND deleteflag = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(0);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("GoukiCode", "goukicode"); //号機
        mapping.put("Kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LotNo", "lotno"); //ﾛｯﾄNo
        mapping.put("EdaBan", "edaban"); //枝番
        mapping.put("Syorikaisuu", "syorikaisuu"); //回数
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TantousyaCode", "tantousyacode"); //開始担当者
        mapping.put("KansouJikan", "kansoujikan"); //乾燥時間
        mapping.put("Ondo", "ondo"); //温度
        mapping.put("Sinkuudo", "sinkuudo"); //真空度
        mapping.put("KaisiNichiji", "kaisinichiji"); //開始日時
        mapping.put("Bikou", "bikou"); //備考1
        mapping.put("TourokuNichiji", "tourokunichiji"); //登録日時
        mapping.put("KousinNichiji", "kousinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syorisuu", "syorisuu"); //処理数
        mapping.put("sagyoubasyo", "sagyoubasyo"); //作業場所
        mapping.put("barrelkaishinichiji", "barrelkaishinichiji"); //ﾊﾞﾚﾙ洗浄開始日時
        mapping.put("barreltantousya", "barreltantousya"); //ﾊﾞﾚﾙ洗浄担当者
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMksinkuukansou>> beanHandler = new BeanListHandler<>(SrMksinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 枝番コピー確認メッセージ表示
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData confEdabanCopy(ProcessData processData) {
        processData.setWarnMessage("親ﾃﾞｰﾀを取得します。よろしいですか？");

        processData.setMethod("edabanCopy");
        return processData;
    }

    /**
     * 枝番コピー
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData edabanCopy(ProcessData processData) {
        try {

            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, paramJissekino, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ﾒｯｷ真空乾燥データ取得
            List<SrMksinkuukansou> srMksinkuukansouDataList = getSrMksinkuukansouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srMksinkuukansouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srMksinkuukansouDataList.get(0));

            // 次呼出しメソッドをクリア
            processData.setMethod("");

            return processData;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
    }

    /**
     * 項目データ設定
     *
     * @param processData 処理制御データ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private ProcessData setItemData(ProcessData processData, String itemId, String value) {
        List<FXHDD01> selectData
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            selectData.get(0).setValue(value);
        }
        return processData;
    }

    /**
     * 項目データ取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        return listData.stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrMksinkuukansou srMksinkuukansouData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srMksinkuukansouData != null) {
            // 元データが存在する場合元データより取得
            return getSrMksinkuukansouItemData(itemId, srMksinkuukansouData);
        } else {
            return null;
        }
    }

    /**
     * 初期表示メッセージ表示
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openInitMessage(ProcessData processData) {

        processData.setMethod("");

        // メッセージを画面に渡す
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(processData.getInitMessageList());

        // 実行スクリプトを設定
        processData.setExecuteScript("PF('W_dlg_initMessage').show();");

        return processData;
    }

    /**
     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
     *
     * @param map マップ
     * @param mapId ID
     * @return マップから取得した値
     */
    private Object getMapData(Map map, String mapId) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.get(mapId);
    }

    /**
     * 品質DB登録実績(fxhdd03)登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void insertFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd03 ("
                + "torokusha ,toroku_date ,koshinsha ,koshin_date ,gamen_id ,rev ,kojyo ,lotno ,"
                + "edaban ,jissekino ,jotai_flg ,tsuika_kotei_flg "
                + ") VALUES ("
                + "?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(tantoshaCd); //登録者
        params.add(systemTime); //登録日
        params.add(null); //更新者
        params.add(null); //更新日
        params.add(formId); //画面ID
        params.add(rev); //revision
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ
        params.add("0"); //追加工程ﾌﾗｸﾞ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 品質DB登録実績(fxhdd03)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param jissekino 実績No
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime, int jissekino) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ?  ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(rev); //revision
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ

        // 検索条件
        params.add(formId); //画面ID
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mksinkuukansou ("
                + " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,KaisiNichiji ,"
                + " Bikou ,TourokuNichiji ,KousinNichiji, tokuisaki ,lotkubuncode ,ownercode ,"
                + " syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision ,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrMksinkuukansou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_mksinkuukansou SET "
                + " GoukiCode = ?,KCPNO = ?,TantousyaCode = ?,KansouJikan = ?,Ondo = ?, "
                + " Sinkuudo = ?,KaisiNichiji = ?,Bikou = ?,KousinNichiji = ?,tokuisaki = ?,lotkubuncode = ?,"
                + " ownercode = ?,syorisuu = ?,sagyoubasyo = ?,barrelkaishinichiji = ?,barreltantousya = ?,bikou2 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMksinkuukansou> srSrMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMksinkuukansou srMksinkuukansou = null;
        if (!srSrMksinkuukansouList.isEmpty()) {
            srMksinkuukansou = srSrMksinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrMksinkuukansou(false, newRev, 0, "", "", "", systemTime, itemList, srMksinkuukansou, jissekino, processData);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_mksinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrMksinkuukansou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou srMksinkuukansouData, int jissekino, ProcessData processData) {
        List<Object> params = new ArrayList<>();
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.GOUKI, srMksinkuukansouData))); // 号機

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KCPNO, srMksinkuukansouData))); //KCPNO        

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData))); // 開始担当者

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData))); // 乾燥時間

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.ONDO, srMksinkuukansouData))); // 温度

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SINKUUDO, srMksinkuukansouData))); // 真空度

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData),
                getItemData(itemList, GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData))); //開始日時

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BIKO1, srMksinkuukansouData))); // 備考1

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.TOKUISAKI, srMksinkuukansouData))); // 客先

        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分

        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SYORISUU, srMksinkuukansouData))); // 処理数

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData))); //作業場所

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData),
                getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄開始日時

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData))); //ﾊﾞﾚﾙ洗浄担当者

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BIKO2, srMksinkuukansouData))); //備考2

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrMksinkuukansou 仮登録データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou tmpSrMksinkuukansou, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO sr_mksinkuukansou ("
                + "GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                + " Sinkuudo ,KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,"
                + " tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrMksinkuukansou(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrMksinkuukansou, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_mksinkuukansou SET "
                + " GoukiCode = ?,KCPNO = ?,TantousyaCode = ?,KansouJikan = ?,Ondo = ?,"
                + " Sinkuudo = ?,KaisiNichiji = ?,Bikou = ?,KousinNichiji = ?,tokuisaki = ?,lotkubuncode = ?,"
                + " ownercode = ?,syorisuu = ?,sagyoubasyo = ?,barrelkaishinichiji = ?,barreltantousya = ?,bikou2 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMksinkuukansou> srMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMksinkuukansou srMksinkuukansou = null;
        if (!srMksinkuukansouList.isEmpty()) {
            srMksinkuukansou = srMksinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrMksinkuukansou(false, newRev, "", "", "", jissekino, systemTime, itemList, srMksinkuukansou, processData);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrMksinkuukansou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou srMksinkuukansouData, ProcessData processData) {
        List<Object> params = new ArrayList<>();

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.GOUKI, srMksinkuukansouData))); // 号機

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KCPNO, srMksinkuukansouData))); // KCPNO

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData))); // 開始担当者

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData))); // 乾燥時間

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.ONDO, srMksinkuukansouData))); // 温度

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.SINKUUDO, srMksinkuukansouData))); // 真空度

        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData),
                getItemData(itemList, GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData))); //開始日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BIKO1, srMksinkuukansouData))); // 備考1

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.TOKUISAKI, srMksinkuukansouData))); // 客先

        params.add(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode"))); // ﾛｯﾄ区分

        params.add(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode"))); //ｵｰﾅｰ

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B039Const.SYORISUU, srMksinkuukansouData))); // 処理数

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData))); // 作業場所

        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData),
                getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄開始日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄担当者

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BIKO2, srMksinkuukansouData))); // 備考2

        params.add(newRev); //revision

        return params;
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_mksinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [ﾒｯｷ真空乾燥_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        int newDeleteFlg = 0;
        if (!StringUtil.isEmpty(StringUtil.nullToBlank(resultMap.get("deleteflag")))) {
            newDeleteFlg = Integer.parseInt(StringUtil.nullToBlank(resultMap.get("deleteflag")));
        }
        newDeleteFlg++;

        return newDeleteFlg;
    }

    /**
     * リビジョンチェック
     *
     * @param processData 処理制御データ
     * @param fxhdd03RevInfo 品質DB登録実績データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd03RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }
        }

        return null;

    }

    /**
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B039Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B039Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * ﾊﾞﾚﾙ洗浄開始日時設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarrelstartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B039Const.BARRELKAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B039Const.BARRELKAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     * @param setDateTime 設定日付
     */
    private void setDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, Date setDateTime) {
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @return DB値
     */
    private String getSrMksinkuukansouItemData(String itemId, SrMksinkuukansou srMksinkuukansouData) {
        switch (itemId) {
            // 客先
            case GXHDO101B039Const.TOKUISAKI:
                return StringUtil.nullToBlank(srMksinkuukansouData.getTokuisaki());
            // KCPNO
            case GXHDO101B039Const.KCPNO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getKcpno());
            // 処理数
            case GXHDO101B039Const.SYORISUU:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSyorisuu());
            // 号機
            case GXHDO101B039Const.GOUKI:
                return StringUtil.nullToBlank(srMksinkuukansouData.getGoukicode());
            // 乾燥時間    
            case GXHDO101B039Const.KANSOUJIKAN:
                return StringUtil.nullToBlank(srMksinkuukansouData.getKansoujikan());
            // 温度
            case GXHDO101B039Const.ONDO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getOndo());
            // 真空度
            case GXHDO101B039Const.SINKUUDO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSinkuudo());
            // 開始日
            case GXHDO101B039Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getKaisinichiji(), "yyMMdd");
            // 開始時間
            case GXHDO101B039Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B039Const.KAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srMksinkuukansouData.getTantousyacode());
            // 作業場所
            case GXHDO101B039Const.SAGYOUBASYO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSagyoubasyo());
            // ﾊﾞﾚﾙ洗浄開始日
            case GXHDO101B039Const.BARRELKAISHI_DAY:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getBarrelkaishinichiji(), "yyMMdd");
            // ﾊﾞﾚﾙ洗浄開始時刻
            case GXHDO101B039Const.BARRELKAISHI_TIME:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getBarrelkaishinichiji(), "HHmm");
            // ﾊﾞﾚﾙ洗浄担当者
            case GXHDO101B039Const.BARRELKAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBarreltantousya());
            // 備考1
            case GXHDO101B039Const.BIKO1:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBikou());
            // 備考2
            case GXHDO101B039Const.BIKO2:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBikou2());
            default:
                return null;
        }
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mksinkuukansou ("
                + " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,KaisiNichiji ,"
                + " Bikou ,TourokuNichiji ,KousinNichiji ,tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,"
                + " barreltantousya ,bikou2 ,revision ,deleteflag"
                + ") SELECT "
                + " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                + " Sinkuudo ,KaisiNichiji ,Bikou ,? ,? ,tokuisaki ,lotkubuncode ,"
                + " ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,? ,?"
                + " FROM sr_mksinkuukansou "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? ";

        List<Object> params = new ArrayList<>();
        // 更新値
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        // 検索値
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //回数

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

}
