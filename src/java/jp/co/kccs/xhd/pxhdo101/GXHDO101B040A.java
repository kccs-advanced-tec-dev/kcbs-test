/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.SelectParam;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SrDenkitokuseiesi;
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

//         // 「プリチャージ」初期設定
//            //initPrecharge();
//
//            //「耐電圧」初期設定
//            initTaidenatsu();
//
//            //「設定条件及び処理結果」初期設定
//            initSetteiJoken();
        // 画面クリア
        //formClear();
    }

    private void initGXHDO101B040B(ProcessData processData) {

        GXHDO101B040B bean = (GXHDO101B040B) getFormBean("beanGXHDO101C040B");

        bean.setDenatsu1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU1));
        bean.setJudenTime1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME1));
        bean.setDenatsu2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU2));
        bean.setJudenTime2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME2));
        bean.setDenatsu3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU3));
        bean.setJudenTime3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME3));
        bean.setDenatsu4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_DENATSU4));
        bean.setJudenTime4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.PRECHARGE_JUDEN_TIME4));
    }

    private void initGXHDO101B040C(ProcessData processData) {
        GXHDO101B040C bean = (GXHDO101B040C) getFormBean("beanGXHDO101C040C");

        bean.setDenatsu1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU1));
        bean.setHanteichi1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI1));
        bean.setJudenTime1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME1));
        bean.setDenatsu2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU2));
        bean.setHanteichi2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI2));
        bean.setJudenTime2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME2));
        bean.setDenatsu3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU3));
        bean.setHanteichi3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI3));
        bean.setJudenTime3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME3));
        bean.setDenatsu4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU4));
        bean.setHanteichi4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI4));
        bean.setJudenTime4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME4));
        bean.setDenatsu5(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU5));
        bean.setHanteichi5(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI5));
        bean.setJudenTime5(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME5));
        bean.setDenatsu6(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU6));
        bean.setHanteichi6(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI6));
        bean.setJudenTime6(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME6));
        bean.setDenatsu7(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU7));
        bean.setHanteichi7(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI7));
        bean.setJudenTime7(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME7));
        bean.setDenatsu8(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_DENATSU8));
        bean.setHanteichi8(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_HANTEICHI8));
        bean.setJudenTime8(getItemRow(processData.getItemListEx(), GXHDO101B040Const.TAIDEN_JUDEN_TIME8));
    }

    private void initGXHDO101B040D(ProcessData processData) {
        GXHDO101B040D bean = (GXHDO101B040D) getFormBean("beanGXHDO101C040D");

        bean.setBin1PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_PERCENT_KBN));
        bean.setBin1SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_SENBETSU_KBN));
        bean.setBin1KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO));
        bean.setBin1CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_COUNTER_SU));
        bean.setBin1Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_GOSARITSU));
        bean.setBin1MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU));
        bean.setBin1NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S));
        bean.setBin1NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T));
        bean.setBin1ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU));
        bean.setBin1KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN1_KEKKA_CHECK));
        bean.setBin2PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_PERCENT_KBN));
        bean.setBin2SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_SENBETSU_KBN));
        bean.setBin2KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO));
        bean.setBin2CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_COUNTER_SU));
        bean.setBin2Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_GOSARITSU));
        bean.setBin2MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU));
        bean.setBin2NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S));
        bean.setBin2NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T));
        bean.setBin2ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU));
        bean.setBin2KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN2_KEKKA_CHECK));
        bean.setBin3PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_PERCENT_KBN));
        bean.setBin3SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_SENBETSU_KBN));
        bean.setBin3KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO));
        bean.setBin3CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_COUNTER_SU));
        bean.setBin3Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_GOSARITSU));
        bean.setBin3MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU));
        bean.setBin3NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S));
        bean.setBin3NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T));
        bean.setBin3ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU));
        bean.setBin3KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN3_KEKKA_CHECK));
        bean.setBin4PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_PERCENT_KBN));
        bean.setBin4SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_SENBETSU_KBN));
        bean.setBin4KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO));
        bean.setBin4CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_COUNTER_SU));
        bean.setBin4Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_GOSARITSU));
        bean.setBin4MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU));
        bean.setBin4NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S));
        bean.setBin4NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T));
        bean.setBin4ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU));
        bean.setBin4KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN4_KEKKA_CHECK));
        bean.setBin5PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_PERCENT_KBN));
        bean.setBin5SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_SENBETSU_KBN));
        bean.setBin5KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO));
        bean.setBin5CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_COUNTER_SU));
        bean.setBin5Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_GOSARITSU));
        bean.setBin5MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU));
        bean.setBin5NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S));
        bean.setBin5NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T));
        bean.setBin5ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU));
        bean.setBin5KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_KEKKA_CHECK));
        bean.setBin5FukuroCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN5_FUKURO_CHECK));
        bean.setBin6PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_PERCENT_KBN));
        bean.setBin6SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_SENBETSU_KBN));
        bean.setBin6KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO));
        bean.setBin6CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_COUNTER_SU));
        bean.setBin6Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_GOSARITSU));
        bean.setBin6MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU));
        bean.setBin6NukitorikekkaS(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S));
        bean.setBin6NukitorikekkaT(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T));
        bean.setBin6ShinFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU));
        bean.setBin6KekkaCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_KEKKA_CHECK));
        bean.setBin6FukuroCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN6_FUKURO_CHECK));
        bean.setBin7PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_PERCENT_KBN));
        bean.setBin7SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_SENBETSU_KBN));
        bean.setBin7KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO));
        bean.setBin7CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_COUNTER_SU));
        bean.setBin7Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_GOSARITSU));
        bean.setBin7MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU));
        bean.setBin7FukuroCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN7_FUKURO_CHECK));
        bean.setBin8PercentKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_PERCENT_KBN));
        bean.setBin8SenbetsuKbn(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_SENBETSU_KBN));
        bean.setBin8KeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO));
        bean.setBin8CounterSu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_COUNTER_SU));
        bean.setBin8Gosaritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_GOSARITSU));
        bean.setBin8MachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU));
        bean.setBin8FukuroCheck(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN8_FUKURO_CHECK));
        bean.setBin9KKeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO));
        bean.setBin9KMachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU));
        bean.setRakkaKeiryogoSuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO));
        bean.setRakkaMachineFuryoritsu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU));
        bean.setHandaSample(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_HANDA_SAMPLE));
        bean.setShinraiseiSample(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_SHINRAISEI_SAMPLE));
        bean.setShinFuryoHanteisha(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA));
        bean.setHanteiNyuryokusha(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA));
        bean.setToridashisha(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_TORIDASHISHA));
        bean.setKousa1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOUSA1));
        bean.setJuryo1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_JURYO1));
        bean.setKosu1(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOSU1));
        bean.setKousa2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOUSA2));
        bean.setJuryo2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_JURYO2));
        bean.setKosu2(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOSU2));
        bean.setKousa3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOUSA3));
        bean.setJuryo3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_JURYO3));
        bean.setKosu3(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOSU3));
        bean.setKousa4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOUSA4));
        bean.setJuryo4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_JURYO4));
        bean.setKosu4(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KOSU4));
        bean.setCounterSosu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_COUNTER_SOSU));
        bean.setRyouhinJuryo(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_RYOUHIN_JURYO));
        bean.setRyouhinKosu(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_RYOUHIN_KOSU));
        bean.setBudomari(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_BUDOMARI));
        bean.setKakuninsha(getItemRow(processData.getItemListEx(), GXHDO101B040Const.SET_KAKUNINSHA));

    }

    public void onTabChange(TabChangeEvent event) {
        System.out.println("tab id = " + event.getTab().getId());
        if ("tab4".equals(event.getTab().getId())) {
            this.mainDivStyle = "width:auto;";

        } else {
            this.mainDivStyle = this.mainDefaultStyle;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", this.mainDivStyle);

    }

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

            // 「プリチャージ」初期設定
            initGXHDO101B040B(processData);

            //「耐電圧」初期設定
            initGXHDO101B040C(processData);

            //「設定条件及び処理結果」初期設定
            initGXHDO101B040D(processData);

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

            //処理時にエラーの背景色を戻さない機能として登録
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B040Const.BTN_SENBETSU_STARTDATETIME_TOP,
                    GXHDO101B040Const.BTN_SENBETSU_ENDDATETIME_TOP,
                    GXHDO101B040Const.BTN_BIN_KEISAN_TOP
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B040Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B040Const.BTN_INSERT_TOP,
                    GXHDO101B040Const.BTN_DELETE_TOP,
                    GXHDO101B040Const.BTN_UPDATE_TOP));

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

                // 電気特性_仮登録登録処理
                insertTmpSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData, formId);

            } else {

                // 電気特性_仮登録更新処理
                updateTmpSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData,formId);

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

        //TODO
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List = "";
        //KCPNO
//        FXHDD01 itemKcpno = getItemRow(processData.getItemList(), GXHDO101B040Const.KCPNO);
//        if (!StringUtil.isEmpty(itemKcpno.getValue()) && itemKcpno.getValue().length() > 9) {
//            String kcpno9 = itemKcpno.getValue().substring(8, 9);
//            // ﾊﾞﾚﾙ洗浄必須ﾁｪｯｸ処理
//            Map fxhbm03Data21 = loadFxhbm03Data(queryRunnerDoc, 21);
//            if (fxhbm03Data21 != null && !fxhbm03Data21.isEmpty()) {
//                strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data21, "data"));
//                String fxhbm03Data[] = strfxhbm03List.split(",");
//                for (int i = 0; i < fxhbm03Data.length; i++) {
//                    if (!StringUtil.isEmpty(kcpno9) && kcpno9.equals(fxhbm03Data[i])) {
//                        // ﾊﾞﾚﾙ洗浄開始日
//                        FXHDD01 itemBarrelkaishiday = getItemRow(processData.getItemList(), GXHDO101B040Const.BARRELKAISHI_DAY);
//                        // ﾊﾞﾚﾙ洗浄開始時刻
//                        FXHDD01 itemBarrelkaishitime = getItemRow(processData.getItemList(), GXHDO101B040Const.BARRELKAISHI_TIME);
//                        // ﾊﾞﾚﾙ洗浄担当者
//                        FXHDD01 itemBarrelkaishitantosya = getItemRow(processData.getItemList(), GXHDO101B040Const.BARRELKAISHI_TANTOSYA);
//                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishiday.getValue()))) {
//                            //エラー発生時
//                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishiday);
//                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishiday.getLabel1());
//                        }
//                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitime.getValue()))) {
//                            //エラー発生時
//                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitime);
//                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitime.getLabel1());
//                        }
//                        if (StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitantosya.getValue()))) {
//                            //エラー発生時
//                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitantosya);
//                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitantosya.getLabel1());
//                        }
//                    }
//                }
//            }
//        }

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
            SrDenkitokuseiesi tmpSrDenkitokuseiesi = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrDenkitokuseiesi> srMksinkuukansouList = getSrDenkitokuseiesiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srMksinkuukansouList.isEmpty()) {
                    tmpSrDenkitokuseiesi = srMksinkuukansouList.get(0);
                }

                deleteTmpSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // 電気特性_登録処理
            insertSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData, tmpSrDenkitokuseiesi, formId);

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
        processData.setUserAuthParam(GXHDO101B040Const.USER_AUTH_UPDATE_PARAM);

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

            // 電気特性_更新処理
            updateSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData, formId);

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
        processData.setUserAuthParam(GXHDO101B040Const.USER_AUTH_DELETE_PARAM);

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

            // 電気特性_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // 電気特性_削除処理
            deleteSrDenkitokuseiesi(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B040Const.BTN_UPDATE_TOP,
                        GXHDO101B040Const.BTN_DELETE_TOP,
                        GXHDO101B040Const.BTN_SENBETSU_STARTDATETIME_TOP,
                        GXHDO101B040Const.BTN_SENBETSU_ENDDATETIME_TOP,
                        GXHDO101B040Const.BTN_BIN_KEISAN_TOP,
                        GXHDO101B040Const.BTN_RYOHIN_KEISAN_TOP,
                        GXHDO101B040Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B040Const.BTN_NETSUSYORI_KEISAN_TOP,
                        GXHDO101B040Const.BTN_HOSEIRITSU_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B040Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B040Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B040Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B040Const.BTN_INSERT_TOP,
                        GXHDO101B040Const.BTN_SENBETSU_STARTDATETIME_TOP,
                        GXHDO101B040Const.BTN_SENBETSU_ENDDATETIME_TOP,
                        GXHDO101B040Const.BTN_BIN_KEISAN_TOP,
                        GXHDO101B040Const.BTN_RYOHIN_KEISAN_TOP,
                        GXHDO101B040Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B040Const.BTN_NETSUSYORI_KEISAN_TOP,
                        GXHDO101B040Const.BTN_HOSEIRITSU_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B040Const.BTN_UPDATE_TOP,
                        GXHDO101B040Const.BTN_DELETE_TOP
                ));

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
            case GXHDO101B040Const.BTN_KARI_TOUROKU_TOP:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B040Const.BTN_INSERT_TOP:
                method = "checkDataResist";
                break;
            // 修正
            case GXHDO101B040Const.BTN_UPDATE_TOP:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B040Const.BTN_DELETE_TOP:
                method = "checkDataDelete";
                break;
            // 選別開始日時
            case GXHDO101B040Const.BTN_SENBETSU_STARTDATETIME_TOP:
                method = "setSenbetsuKaishiDateTime";
                break;
            // 選別終了日時
            case GXHDO101B040Const.BTN_SENBETSU_ENDDATETIME_TOP:
                method = "setSenbetsuShuryoDateTime";
                break;

            // BIN計算
            case GXHDO101B040Const.BTN_BIN_KEISAN_TOP:
                method = "doBinKeisan"; //TODO
                break;
            // 良品計算
            case GXHDO101B040Const.BTN_RYOHIN_KEISAN_TOP:
                method = "setSenbetsuShuryoDateTime"; //TODO
                break;

            // 歩留まり計算
            case GXHDO101B040Const.BTN_BUDOMARI_KEISAN_TOP:
                method = "setSenbetsuKaishiDateTime"; //TODO
                break;
            // 熱処理ｴｰｼﾞﾝｸﾞ
            case GXHDO101B040Const.BTN_NETSUSYORI_KEISAN_TOP:
                method = "setSenbetsuShuryoDateTime"; //TODO
                break;

            // 補正率計算
            case GXHDO101B040Const.BTN_HOSEIRITSU_KEISAN_TOP:
                method = "setSenbetsuShuryoDateTime"; //TODO
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

        // TODO QA履歴
        Map qarireki = new HashMap<String, Object>();

//        //ﾃﾞｰﾀの取得
//        String strfxhbm03List = "";
//
//        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, 20);
//        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
//            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
//            String fxhbm03DataArr[] = strfxhbm03List.split(",");
//
//            // 実績情報の取得
//            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
//            if (jissekiData != null && jissekiData.size() > 0) {
//                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
//                if (dbShorisu > 0) {
//                    syorisuu = String.valueOf(dbShorisu);
//
//                }
//            }
//        }

        // 検査場所選択値初期設定
        if(!initKensabasho(session, processData, errorMessageList)){
            return processData;
        }


        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }
    
    
    /**
     * 検査場所初期設定
     * @param session セッション情報
     * @param processData 処理制御データ
     * @param errorMessageList エラーメッセージリスト
     * @return 処理成功:true、処理失敗:false
     */
    private boolean initKensabasho(HttpSession session, ProcessData processData, List<String> errorMessageList){
        String kensaBasho =  getParamData(session, "電気特性_検査場所ﾄﾞﾛｯﾌﾟﾀﾞｳﾝ");
        FXHDD01 itemKensaBasho = getItemRow(processData.getItemList(), GXHDO101B040Const.SEIHIN_KENSA_BASHO);
        if(StringUtil.isEmpty(kensaBasho)){
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000169", itemKensaBasho.getLabel1()));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return false;
        }
        itemKensaBasho.setInputList(kensaBasho);
        
        return true;
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B040Const.SEIHIN_LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B040Const.SEIHIN_LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B040Const.SEIHIN_OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B040Const.SEIHIN_OWNER, ownercode + ":" + owner);
        }

        // 指定公差
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA, StringUtil.nullToBlank(getMapData(sekkeiData, "KOUSA")));

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

        List<SrDenkitokuseiesi> srDenkitokuseiesiList = new ArrayList<>();
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

                // 画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }
                for (FXHDD01 fxhdd001 : processData.getItemListEx()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }
                return true;
            }

            // 電気特性データ取得
            srDenkitokuseiesiList = getSrDenkitokuseiesiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srDenkitokuseiesiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srDenkitokuseiesiList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemData(processData, srDenkitokuseiesiList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     */
    private void setInputItemData(ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi) {
        //製品情報
        setInputItemDataFormA(processData, srDenkitokuseiesi);
        //ﾌﾟﾘﾁｬｰｼﾞ条件
        setInputItemDataFormB(processData, srDenkitokuseiesi);
        //耐電圧設定条件
        setInputItemDataFormC(processData, srDenkitokuseiesi);
        //設定条件及び処理結果
        setInputItemDataFormD(processData, srDenkitokuseiesi);
    }

    /**
     * 製品情報データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     */
    private void setInputItemDataFormA(ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi) {

        // 製品情報:送り良品数
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_OKURI_RYOHINSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_OKURI_RYOHINSU, srDenkitokuseiesi));

        // 製品情報:受入れ単位重量
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_UKEIRE_TANNIJURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_UKEIRE_TANNIJURYO, srDenkitokuseiesi));

        // 製品情報:受入れ総重量
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_UKEIRE_SOUJURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_UKEIRE_SOUJURYO, srDenkitokuseiesi));

        // 製品情報:外部電極焼付日
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_DAY, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_G_YAKITSUKE_DAY, srDenkitokuseiesi));

        // 製品情報:外部電極焼付時間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_G_YAKITSUKE_TIME, srDenkitokuseiesi));

        // 製品情報:ﾒｯｷ日
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_MEKKI_DAY, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_MEKKI_DAY, srDenkitokuseiesi));

        // 製品情報:ﾒｯｷ時間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_MEKKI_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_MEKKI_TIME, srDenkitokuseiesi));

        // 製品情報:検査場所
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_KENSA_BASHO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_KENSA_BASHO, srDenkitokuseiesi));

        // 製品情報:選別開始日
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY, srDenkitokuseiesi));

        // 製品情報:選別開始時間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME, srDenkitokuseiesi));

        // 製品情報:選別終了日
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY, srDenkitokuseiesi));

        // 製品情報:選別終了時間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME, srDenkitokuseiesi));

        // 製品情報:検査号機
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_KENSA_GOKI, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_KENSA_GOKI, srDenkitokuseiesi));

        // 製品情報:分類ｴｱｰ圧
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BUNRUI_AIR_ATSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BUNRUI_AIR_ATSU, srDenkitokuseiesi));

        // 製品情報:CDｺﾝﾀｸﾄ圧
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_CD_CONTACT_ATSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_CD_CONTACT_ATSU, srDenkitokuseiesi));

        // 製品情報:IRｺﾝﾀｸﾄ圧
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_IR_CONTACT_ATSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_IR_CONTACT_ATSU, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認CD1
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_CD1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_CD1, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認PC1
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC1, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認PC2
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC2, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認PC3
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC3, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認PC4
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC4, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR1
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR1, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR2
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR2, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR3
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR3, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR4
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR4, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR5
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR5, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR5, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR6
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR6, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR6, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR7
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR7, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR7, srDenkitokuseiesi));

        // 製品情報:使用後ｽﾃｰｼｮﾝ確認IR8
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR8, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR8, srDenkitokuseiesi));

        // 製品情報:固定電極 外観･段差
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_K_GAIKAN_DANSA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_K_GAIKAN_DANSA, srDenkitokuseiesi));

        // 製品情報:ﾄﾗｯｸｶﾞｲﾄﾞ隙間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TRACK_GUIDE_SUKIMA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_TRACK_GUIDE_SUKIMA, srDenkitokuseiesi));

        // 製品情報:ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TEST_PLATE_KEIJO_SEISOU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_TEST_PLATE_KEIJO_SEISOU, srDenkitokuseiesi));

        // 製品情報:分類吹き出し穴
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BUNRUI_FUKIDASHIANA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BUNRUI_FUKIDASHIANA, srDenkitokuseiesi));

        // 製品情報:ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TEST_PLATE_ICHI_KAKUNIN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_TEST_PLATE_ICHI_KAKUNIN, srDenkitokuseiesi));

        // 製品情報:電極清掃・動作
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_DENKYOKU_SEISOU_DOUSA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_DENKYOKU_SEISOU_DOUSA, srDenkitokuseiesi));

        // 製品情報:製品投入状態
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SEIHIN_TOUNYU_JOTAI, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SEIHIN_TOUNYU_JOTAI, srDenkitokuseiesi));

        // 製品情報:BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BIN_BOX_SEISOU_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BIN_BOX_SEISOU_CHECK, srDenkitokuseiesi));

        // 製品情報:ｾｯﾄ者
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SETSHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SETSHA, srDenkitokuseiesi));

        // 製品情報:確認者
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_KAKUNINSHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_KAKUNINSHA, srDenkitokuseiesi));

        // 製品情報:指定公差歩留まり1
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI1, srDenkitokuseiesi));

        // 製品情報:指定公差歩留まり2
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI2, srDenkitokuseiesi));

        // 製品情報:ﾃｽﾄﾌﾟﾚｰﾄ管理No
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TEST_PLATE_KANRINO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_TEST_PLATE_KANRINO, srDenkitokuseiesi));

        // 製品情報:Tanδ
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_TAN_DELTA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_TAN_DELTA, srDenkitokuseiesi));

        // 製品情報:測定周波数
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SOKUTEI_SHUHASU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SOKUTEI_SHUHASU, srDenkitokuseiesi));

        // 製品情報:測定電圧
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU, srDenkitokuseiesi));

        // 製品情報:補正用ﾁｯﾌﾟ容量
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_YORYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU, srDenkitokuseiesi));

        // 製品情報:補正用ﾁｯﾌﾟTanδ
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_TAN_DELTA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_TAN_DELTA, srDenkitokuseiesi));

        // 製品情報:補正前
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_HOSEIMAE, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_HOSEIMAE, srDenkitokuseiesi));

        // 製品情報:補正後
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_HOSEIATO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_HOSEIATO, srDenkitokuseiesi));

        // 製品情報:補正率
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_HOSEIRITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_HOSEIRITSU, srDenkitokuseiesi));

        // 製品情報:ｽﾀﾝﾀﾞｰﾄﾞ補正
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_STANDARD_HOSEI, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_STANDARD_HOSEI, srDenkitokuseiesi));

        // 製品情報:分類確認
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BUNRUI_KAKUNIN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BUNRUI_KAKUNIN, srDenkitokuseiesi));

        // 製品情報:外観確認
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_GAIKAN_KAKUNIN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_GAIKAN_KAKUNIN, srDenkitokuseiesi));

        // 製品情報:熱処理日
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_NETSUSYORI_DAY, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_NETSUSYORI_DAY, srDenkitokuseiesi));

        // 製品情報:熱処理時刻
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_NETSUSYORI_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_NETSUSYORI_TIME, srDenkitokuseiesi));

        // 製品情報:ｴｰｼﾞﾝｸﾞ時間
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_AGING_TIME, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_AGING_TIME, srDenkitokuseiesi));

        // 製品情報:充填率
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_JUTENRITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_JUTENRITSU, srDenkitokuseiesi));

        // 製品情報:MC
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_MC, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_MC, srDenkitokuseiesi));

        // 製品情報:強制排出
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_KYOSEI_HAISHUTSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_KYOSEI_HAISHUTSU, srDenkitokuseiesi));

        // 製品情報:落下
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_RAKKA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_RAKKA, srDenkitokuseiesi));

        // 製品情報:承認者
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_SHONINSHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_SHONINSHA, srDenkitokuseiesi));

        // 製品情報:振向者
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_FURIMUKESHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_FURIMUKESHA, srDenkitokuseiesi));

        // 製品情報:電気特性再検
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_DENKITOKUSEI_SAIKEN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_DENKITOKUSEI_SAIKEN, srDenkitokuseiesi));

        // 製品情報:備考1
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BIKOU1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BIKOU1, srDenkitokuseiesi));

        // 製品情報:備考2
        this.setItemData(processData, GXHDO101B040Const.SEIHIN_BIKOU2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SEIHIN_BIKOU2, srDenkitokuseiesi));

    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     */
    private void setInputItemDataFormB(ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi) {

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC① 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_DENATSU1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_DENATSU1, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC① 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_JUDEN_TIME1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_JUDEN_TIME1, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC② 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_DENATSU2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_DENATSU2, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC② 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_JUDEN_TIME2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_JUDEN_TIME2, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC③ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_DENATSU3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_DENATSU3, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC③ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_JUDEN_TIME3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_JUDEN_TIME3, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC④ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_DENATSU4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_DENATSU4, srDenkitokuseiesi));

        // ﾌﾟﾘﾁｬｰｼﾞ条件:PC④ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.PRECHARGE_JUDEN_TIME4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.PRECHARGE_JUDEN_TIME4, srDenkitokuseiesi));

    }

    /**
     * 耐電圧設定条件データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     */
    private void setInputItemDataFormC(ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi) {

        // 耐電圧設定条件:IR① 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU1, srDenkitokuseiesi));

        // 耐電圧設定条件:IR① 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI1, srDenkitokuseiesi));

        // 耐電圧設定条件:IR① 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME1, srDenkitokuseiesi));

        // 耐電圧設定条件:IR② 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU2, srDenkitokuseiesi));

        // 耐電圧設定条件:IR② 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI2, srDenkitokuseiesi));

        // 耐電圧設定条件:IR② 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME2, srDenkitokuseiesi));

        // 耐電圧設定条件:IR③ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU3, srDenkitokuseiesi));

        // 耐電圧設定条件:IR③ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI3, srDenkitokuseiesi));

        // 耐電圧設定条件:IR③ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME3, srDenkitokuseiesi));

        // 耐電圧設定条件:IR④ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU4, srDenkitokuseiesi));

        // 耐電圧設定条件:IR④ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI4, srDenkitokuseiesi));

        // 耐電圧設定条件:IR④ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME4, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑤ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU5, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU5, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑤ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI5, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI5, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑤ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME5, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME5, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑥ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU6, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU6, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑥ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI6, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI6, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑥ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME6, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME6, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑦ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU7, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU7, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑦ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI7, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI7, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑦ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME7, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME7, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑧ 電圧
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_DENATSU8, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_DENATSU8, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑧ 判定値
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_HANTEICHI8, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_HANTEICHI8, srDenkitokuseiesi));

        // 耐電圧設定条件:IR⑧ 充電時間
        this.setItemDataEx(processData, GXHDO101B040Const.TAIDEN_JUDEN_TIME8, getSrDenkitokuseiesiItemData(GXHDO101B040Const.TAIDEN_JUDEN_TIME8, srDenkitokuseiesi));

    }

    /**
     * 設定条件及び処理結果データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     */
    private void setInputItemDataFormD(ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi) {

        // 設定条件及び処理結果:BIN1 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN1 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN1_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN1_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN2 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN2_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN2_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN3 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN3_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN3_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN4 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN4_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN4_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN5 袋ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN5_FUKURO_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN5_FUKURO_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 抜き取り結果
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 真の不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 結果ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_KEKKA_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_KEKKA_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN6 袋ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN6_FUKURO_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN6_FUKURO_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN7 袋ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN7_FUKURO_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN7_FUKURO_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 %区分(設定値)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_PERCENT_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_PERCENT_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 選別区分
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_SENBETSU_KBN, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_SENBETSU_KBN, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 ｶｳﾝﾀｰ数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_COUNTER_SU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_COUNTER_SU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 誤差率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_GOSARITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_GOSARITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 ﾏｼﾝ不良率(%)
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN8 袋ﾁｪｯｸ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN8_FUKURO_CHECK, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN8_FUKURO_CHECK, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN9 強制排出 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:BIN9 強制排出 ﾏｼﾝ不良率
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:落下 計量後数量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:落下 ﾏｼﾝ不良率
        this.setItemDataEx(processData, GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:半田ｻﾝﾌﾟﾙ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_HANDA_SAMPLE, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_HANDA_SAMPLE, srDenkitokuseiesi));

        // 設定条件及び処理結果:信頼性ｻﾝﾌﾟﾙ
        this.setItemDataEx(processData, GXHDO101B040Const.SET_SHINRAISEI_SAMPLE, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_SHINRAISEI_SAMPLE, srDenkitokuseiesi));

        // 設定条件及び処理結果:真不良判定者
        this.setItemDataEx(processData, GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA, srDenkitokuseiesi));

        // 設定条件及び処理結果:判定入力者
        this.setItemDataEx(processData, GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA, srDenkitokuseiesi));

        // 設定条件及び処理結果:取出者
        this.setItemDataEx(processData, GXHDO101B040Const.SET_TORIDASHISHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_TORIDASHISHA, srDenkitokuseiesi));

        // 設定条件及び処理結果:公差①
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOUSA1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOUSA1, srDenkitokuseiesi));

        // 設定条件及び処理結果:重量①
        this.setItemDataEx(processData, GXHDO101B040Const.SET_JURYO1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_JURYO1, srDenkitokuseiesi));

        // 設定条件及び処理結果:個数①
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOSU1, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOSU1, srDenkitokuseiesi));

        // 設定条件及び処理結果:公差②
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOUSA2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOUSA2, srDenkitokuseiesi));

        // 設定条件及び処理結果:重量②
        this.setItemDataEx(processData, GXHDO101B040Const.SET_JURYO2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_JURYO2, srDenkitokuseiesi));

        // 設定条件及び処理結果:個数②
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOSU2, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOSU2, srDenkitokuseiesi));

        // 設定条件及び処理結果:公差③
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOUSA3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOUSA3, srDenkitokuseiesi));

        // 設定条件及び処理結果:重量③
        this.setItemDataEx(processData, GXHDO101B040Const.SET_JURYO3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_JURYO3, srDenkitokuseiesi));

        // 設定条件及び処理結果:個数③
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOSU3, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOSU3, srDenkitokuseiesi));

        // 設定条件及び処理結果:公差④
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOUSA4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOUSA4, srDenkitokuseiesi));

        // 設定条件及び処理結果:重量④
        this.setItemDataEx(processData, GXHDO101B040Const.SET_JURYO4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_JURYO4, srDenkitokuseiesi));

        // 設定条件及び処理結果:個数④
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KOSU4, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KOSU4, srDenkitokuseiesi));

        // 設定条件及び処理結果:ｶｳﾝﾀｰ総数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_COUNTER_SOSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_COUNTER_SOSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:良品重量
        this.setItemDataEx(processData, GXHDO101B040Const.SET_RYOUHIN_JURYO, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_RYOUHIN_JURYO, srDenkitokuseiesi));

        // 設定条件及び処理結果:良品個数
        this.setItemDataEx(processData, GXHDO101B040Const.SET_RYOUHIN_KOSU, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_RYOUHIN_KOSU, srDenkitokuseiesi));

        // 設定条件及び処理結果:歩留まり
        this.setItemDataEx(processData, GXHDO101B040Const.SET_BUDOMARI, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_BUDOMARI, srDenkitokuseiesi));

        // 設定条件及び処理結果:確認者
        this.setItemDataEx(processData, GXHDO101B040Const.SET_KAKUNINSHA, getSrDenkitokuseiesiItemData(GXHDO101B040Const.SET_KAKUNINSHA, srDenkitokuseiesi));
    }

    /**
     * 電気特性の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 電気特性登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrDenkitokuseiesi> getSrDenkitokuseiesiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrDenkitokuseiesi(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrDenkitokuseiesi(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
        String sql = "SELECT SEKKEINO,KOUSA "
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
        map.put("KOUSA", "指定公差");
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

//    /**
//     * [実績]から、ﾃﾞｰﾀを取得
//     *
//     * @param queryRunnerWip オブジェクト
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
//     * @return 取得データ
//     * @throws SQLException
//     */
//    private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {
//
//        String lotNo1 = lotNo.substring(0, 3);
//        String lotNo2 = lotNo.substring(3, 11);
//        String lotNo3 = lotNo.substring(11, 14);
//
//        List<String> dataList = new ArrayList<>(Arrays.asList(data));
//
//        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
//        String sql = "SELECT syorisuu "
//                + "FROM jisseki "
//                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";
//
//        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());
//
//        sql += " ORDER BY syoribi DESC, syorijikoku DESC";
//
//        Map mapping = new HashMap<>();
//        mapping.put("syorisuu", "syorisuu");
//
//        BeanProcessor beanProcessor = new BeanProcessor(mapping);
//        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
//        ResultSetHandler<List<Jisseki>> beanHandler = new BeanListHandler<>(Jisseki.class, rowProcessor);
//
//        List<Object> params = new ArrayList<>();
//        params.add(lotNo1);
//        params.add(lotNo2);
//        params.add(lotNo3);
//        params.addAll(dataList);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerWip.query(sql, beanHandler, params.toArray());
//    }
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
                sql = sql + " key = 'xhd_外部電極電気特性_ﾊﾞﾚﾙ洗浄_必須判定ｺｰﾄﾞ' ";
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
        String sql = "SELECT kcpno, tokuisaki, lotkubuncode, ownercode "
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
     * [電気特性]から、ﾃﾞｰﾀを取得
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
    private List<SrDenkitokuseiesi> loadSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("siteikousa", "siteikousa"); //指定公差
        mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou"); //後工程指示内容
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("gdyakitukenitiji", "gdyakitukenitiji"); //外部電極焼付日時
        mapping.put("mekkinitiji", "mekkinitiji"); //ﾒｯｷ日時
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("senbetukaisinitiji", "senbetukaisinitiji"); //選別開始日時
        mapping.put("senbetusyuryounitiji", "senbetusyuryounitiji"); //選別終了日時
        mapping.put("kensagouki", "kensagouki"); //検査号機
        mapping.put("bunruiairatu", "bunruiairatu"); //分類ｴｱｰ圧
        mapping.put("cdcontactatu", "cdcontactatu"); //CDｺﾝﾀｸﾄ圧
        mapping.put("ircontactatu", "ircontactatu"); //IRｺﾝﾀｸﾄ圧
        mapping.put("stationcd1", "stationcd1"); //使用後ｽﾃｰｼｮﾝ確認CD1
        mapping.put("stationpc1", "stationpc1"); //使用後ｽﾃｰｼｮﾝ確認PC1
        mapping.put("stationpc2", "stationpc2"); //使用後ｽﾃｰｼｮﾝ確認PC2
        mapping.put("stationpc3", "stationpc3"); //使用後ｽﾃｰｼｮﾝ確認PC3
        mapping.put("stationpc4", "stationpc4"); //使用後ｽﾃｰｼｮﾝ確認PC4
        mapping.put("stationir1", "stationir1"); //使用後ｽﾃｰｼｮﾝ確認IR1
        mapping.put("stationir2", "stationir2"); //使用後ｽﾃｰｼｮﾝ確認IR2
        mapping.put("stationir3", "stationir3"); //使用後ｽﾃｰｼｮﾝ確認IR3
        mapping.put("stationir4", "stationir4"); //使用後ｽﾃｰｼｮﾝ確認IR4
        mapping.put("stationir5", "stationir5"); //使用後ｽﾃｰｼｮﾝ確認IR5
        mapping.put("stationir6", "stationir6"); //使用後ｽﾃｰｼｮﾝ確認IR6
        mapping.put("stationir7", "stationir7"); //使用後ｽﾃｰｼｮﾝ確認IR7
        mapping.put("stationir8", "stationir8"); //使用後ｽﾃｰｼｮﾝ確認IR8
        mapping.put("koteidenkyoku", "koteidenkyoku"); //固定電極 外観･段差
        mapping.put("torakkugaido", "torakkugaido"); //ﾄﾗｯｸｶﾞｲﾄﾞ隙間
        mapping.put("testplatekeijo", "testplatekeijo"); //ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
        mapping.put("bunruifukidasi", "bunruifukidasi"); //分類吹き出し穴
        mapping.put("testplatekakunin", "testplatekakunin"); //ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
        mapping.put("denkyokuseisou", "denkyokuseisou"); //電極清掃･動作
        mapping.put("seihintounyuujotai", "seihintounyuujotai"); //製品投入状態
        mapping.put("binboxseisoucheck", "binboxseisoucheck"); //BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
        mapping.put("setsya", "setsya"); //ｾｯﾄ者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("siteikousabudomari1", "siteikousabudomari1"); //指定公差歩留まり1
        mapping.put("siteikousabudomari2", "siteikousabudomari2"); //指定公差歩留まり2
        mapping.put("testplatekanrino", "testplatekanrino"); //ﾃｽﾄﾌﾟﾚｰﾄ管理No
        mapping.put("tan", "tan"); //Tanδ
        mapping.put("sokuteisyuhasuu", "sokuteisyuhasuu"); //測定周波数
        mapping.put("sokuteidenatu", "sokuteidenatu"); //測定電圧
        mapping.put("hoseiyoutippuyoryou", "hoseiyoutippuyoryou"); //補正用ﾁｯﾌﾟ容量
        mapping.put("hoseiyoutipputan", "hoseiyoutipputan"); //補正用ﾁｯﾌﾟTanδ
        mapping.put("hoseimae", "hoseimae"); //補正前
        mapping.put("hoseigo", "hoseigo"); //補正後
        mapping.put("hoseiritu", "hoseiritu"); //補正率
        mapping.put("Standard", "standard"); //ｽﾀﾝﾀﾞｰﾄﾞ補正
        mapping.put("bunruikakunin", "bunruikakunin"); //分類確認
        mapping.put("gaikankakunin", "gaikankakunin"); //外観確認
        mapping.put("netsusyorinitiji", "netsusyorinitiji"); //熱処理日時
        mapping.put("agingjikan", "agingjikan"); //ｴｰｼﾞﾝｸﾞ時間
        mapping.put("jutenritu", "jutenritu"); //充填率
        mapping.put("mc", "mc"); //MC
        mapping.put("kyoseihaisyutu", "kyoseihaisyutu"); //強制排出
        mapping.put("rakka", "rakka"); //落下
        mapping.put("syoninsha", "syoninsha"); //承認者
        mapping.put("furimukesya", "furimukesya"); //振向者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("pcdenatu1", "pcdenatu1"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
        mapping.put("pcjudenjikan1", "pcjudenjikan1"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
        mapping.put("pcdenatu2", "pcdenatu2"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
        mapping.put("pcjudenjikan2", "pcjudenjikan2"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
        mapping.put("pcdenatu3", "pcdenatu3"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
        mapping.put("pcjudenjikan3", "pcjudenjikan3"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
        mapping.put("pcdenatu4", "pcdenatu4"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
        mapping.put("pcjudenjikan4", "pcjudenjikan4"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
        mapping.put("irdenatu1", "irdenatu1"); //耐電圧設定条件 IR① 電圧
        mapping.put("irhanteiti1", "irhanteiti1"); //耐電圧設定条件 IR① 判定値
        mapping.put("irjudenjikan1", "irjudenjikan1"); //耐電圧設定条件 IR① 充電時間
        mapping.put("irdenatu2", "irdenatu2"); //耐電圧設定条件 IR② 電圧
        mapping.put("irhanteiti2", "irhanteiti2"); //耐電圧設定条件 IR② 判定値
        mapping.put("irjudenjikan2", "irjudenjikan2"); //耐電圧設定条件 IR② 充電時間
        mapping.put("irdenatu3", "irdenatu3"); //耐電圧設定条件 IR③ 電圧
        mapping.put("irhanteiti3", "irhanteiti3"); //耐電圧設定条件 IR③ 判定値
        mapping.put("irjudenjikan3", "irjudenjikan3"); //耐電圧設定条件 IR③ 充電時間
        mapping.put("irdenatu4", "irdenatu4"); //耐電圧設定条件 IR④ 電圧
        mapping.put("irhanteiti4", "irhanteiti4"); //耐電圧設定条件 IR④ 判定値
        mapping.put("irjudenjikan4", "irjudenjikan4"); //耐電圧設定条件 IR④ 充電時間
        mapping.put("irdenatu5", "irdenatu5"); //耐電圧設定条件 IR⑤ 電圧
        mapping.put("irhanteiti5", "irhanteiti5"); //耐電圧設定条件 IR⑤ 判定値
        mapping.put("irjudenjikan5", "irjudenjikan5"); //耐電圧設定条件 IR⑤ 充電時間
        mapping.put("irdenatu6", "irdenatu6"); //耐電圧設定条件 IR⑥ 電圧
        mapping.put("irhanteiti6", "irhanteiti6"); //耐電圧設定条件 IR⑥ 判定値
        mapping.put("irjudenjikan6", "irjudenjikan6"); //耐電圧設定条件 IR⑥ 充電時間
        mapping.put("irdenatu7", "irdenatu7"); //耐電圧設定条件 IR⑦ 電圧
        mapping.put("irhanteiti7", "irhanteiti7"); //耐電圧設定条件 IR⑦ 判定値
        mapping.put("irjudenjikan7", "irjudenjikan7"); //耐電圧設定条件 IR⑦ 充電時間
        mapping.put("irdenatu8", "irdenatu8"); //耐電圧設定条件 IR⑧ 電圧
        mapping.put("irhanteiti8", "irhanteiti8"); //耐電圧設定条件 IR⑧ 判定値
        mapping.put("irjudenjikan8", "irjudenjikan8"); //耐電圧設定条件 IR⑧ 充電時間
        mapping.put("bin1setteiti", "bin1setteiti"); //BIN1 %区分(設定値)
        mapping.put("bin1senbetukubun", "bin1senbetukubun"); //BIN1 選別区分
        mapping.put("bin1keiryougosuryou", "bin1keiryougosuryou"); //BIN1 計量後数量
        mapping.put("bin1countersuu", "bin1countersuu"); //BIN1 ｶｳﾝﾀｰ数
        mapping.put("bin1gosaritu", "bin1gosaritu"); //BIN1 誤差率(%)
        mapping.put("bin1masinfuryouritu", "bin1masinfuryouritu"); //BIN1 ﾏｼﾝ不良率(%)
        mapping.put("bin1nukitorikekkabosuu", "bin1nukitorikekkabosuu"); //BIN1 抜き取り結果
        mapping.put("bin1nukitorikekka", "bin1nukitorikekka"); //BIN1 抜き取り結果 
        mapping.put("bin1sinnofuryouritu", "bin1sinnofuryouritu"); //BIN1 真の不良率(%)
        mapping.put("bin1kekkacheck", "bin1kekkacheck"); //BIN1 結果ﾁｪｯｸ
        mapping.put("bin2setteiti", "bin2setteiti"); //BIN2 %区分(設定値)
        mapping.put("bin2senbetukubun", "bin2senbetukubun"); //BIN2 選別区分
        mapping.put("bin2keiryougosuryou", "bin2keiryougosuryou"); //BIN2 計量後数量
        mapping.put("bin2countersuu", "bin2countersuu"); //BIN2 ｶｳﾝﾀｰ数
        mapping.put("bin2gosaritu", "bin2gosaritu"); //BIN2 誤差率(%)
        mapping.put("bin2masinfuryouritu", "bin2masinfuryouritu"); //BIN2 ﾏｼﾝ不良率(%)
        mapping.put("bin2nukitorikekkabosuu", "bin2nukitorikekkabosuu"); //BIN2 抜き取り結果
        mapping.put("bin2nukitorikekka", "bin2nukitorikekka"); //BIN2 抜き取り結果
        mapping.put("bin2sinnofuryouritu", "bin2sinnofuryouritu"); //BIN2 真の不良率(%)
        mapping.put("bin2kekkacheck", "bin2kekkacheck"); //BIN2 結果ﾁｪｯｸ
        mapping.put("bin3setteiti", "bin3setteiti"); //BIN3 %区分(設定値)
        mapping.put("bin3senbetukubun", "bin3senbetukubun"); //BIN3 選別区分
        mapping.put("bin3keiryougosuryou", "bin3keiryougosuryou"); //BIN3 計量後数量
        mapping.put("bin3countersuu", "bin3countersuu"); //BIN3 ｶｳﾝﾀｰ数
        mapping.put("bin3gosaritu", "bin3gosaritu"); //BIN3 誤差率(%)
        mapping.put("bin3masinfuryouritu", "bin3masinfuryouritu"); //BIN3 ﾏｼﾝ不良率(%)
        mapping.put("bin3nukitorikekkabosuu", "bin3nukitorikekkabosuu"); //BIN3 抜き取り結果
        mapping.put("bin3nukitorikekka", "bin3nukitorikekka"); //BIN3 抜き取り結果
        mapping.put("bin3sinnofuryouritu", "bin3sinnofuryouritu"); //BIN3 真の不良率(%)
        mapping.put("bin3kekkacheck", "bin3kekkacheck"); //BIN3 結果ﾁｪｯｸ
        mapping.put("bin4setteiti", "bin4setteiti"); //BIN4 %区分(設定値)
        mapping.put("bin4senbetukubun", "bin4senbetukubun"); //BIN4 選別区分
        mapping.put("bin4keiryougosuryou", "bin4keiryougosuryou"); //BIN4 計量後数量
        mapping.put("bin4countersuu", "bin4countersuu"); //BIN4 ｶｳﾝﾀｰ数
        mapping.put("bin4gosaritu", "bin4gosaritu"); //BIN4 誤差率(%)
        mapping.put("bin4masinfuryouritu", "bin4masinfuryouritu"); //BIN4 ﾏｼﾝ不良率(%)
        mapping.put("bin4nukitorikekkabosuu", "bin4nukitorikekkabosuu"); //BIN4 抜き取り結果
        mapping.put("bin4nukitorikekka", "bin4nukitorikekka"); //BIN4 抜き取り結果
        mapping.put("bin4sinnofuryouritu", "bin4sinnofuryouritu"); //BIN4 真の不良率(%)
        mapping.put("bin4kekkacheck", "bin4kekkacheck"); //BIN4 結果ﾁｪｯｸ
        mapping.put("bin5setteiti", "bin5setteiti"); //BIN5 %区分(設定値)
        mapping.put("bin5senbetukubun", "bin5senbetukubun"); //BIN5 選別区分
        mapping.put("bin5keiryougosuryou", "bin5keiryougosuryou"); //BIN5 計量後数量
        mapping.put("bin5countersuu", "bin5countersuu"); //BIN5 ｶｳﾝﾀｰ数
        mapping.put("bin5gosaritu", "bin5gosaritu"); //BIN5 誤差率(%)
        mapping.put("bin5masinfuryouritu", "bin5masinfuryouritu"); //BIN5 ﾏｼﾝ不良率(%)
        mapping.put("bin5nukitorikekkabosuu", "bin5nukitorikekkabosuu"); //BIN5 抜き取り結果
        mapping.put("bin5nukitorikekka", "bin5nukitorikekka"); //BIN5 抜き取り結果
        mapping.put("bin5sinnofuryouritu", "bin5sinnofuryouritu"); //BIN5 真の不良率(%)
        mapping.put("bin5kekkacheck", "bin5kekkacheck"); //BIN5 結果ﾁｪｯｸ
        mapping.put("bin5fukurocheck", "bin5fukurocheck"); //BIN5 袋ﾁｪｯｸ
        mapping.put("bin6setteiti", "bin6setteiti"); //BIN6 %区分(設定値)
        mapping.put("bin6senbetukubun", "bin6senbetukubun"); //BIN6 選別区分
        mapping.put("bin6keiryougosuryou", "bin6keiryougosuryou"); //BIN6 計量後数量
        mapping.put("bin6countersuu", "bin6countersuu"); //BIN6 ｶｳﾝﾀｰ数
        mapping.put("bin6gosaritu", "bin6gosaritu"); //BIN6 誤差率(%)
        mapping.put("bin6masinfuryouritu", "bin6masinfuryouritu"); //BIN6 ﾏｼﾝ不良率(%)
        mapping.put("bin6nukitorikekkabosuu", "bin6nukitorikekkabosuu"); //BIN6 抜き取り結果
        mapping.put("bin6nukitorikekka", "bin6nukitorikekka"); //BIN6 抜き取り結果
        mapping.put("bin6sinnofuryouritu", "bin6sinnofuryouritu"); //BIN6 真の不良率(%)
        mapping.put("bin6kekkacheck", "bin6kekkacheck"); //BIN6 結果ﾁｪｯｸ
        mapping.put("bin6fukurocheck", "bin6fukurocheck"); //BIN6 袋ﾁｪｯｸ
        mapping.put("bin7setteiti", "bin7setteiti"); //BIN7 %区分(設定値)
        mapping.put("bin7senbetukubun", "bin7senbetukubun"); //BIN7 選別区分
        mapping.put("bin7keiryougosuryou", "bin7keiryougosuryou"); //BIN7 計量後数量
        mapping.put("bin7countersuu", "bin7countersuu"); //BIN7 ｶｳﾝﾀｰ数
        mapping.put("bin7gosaritu", "bin7gosaritu"); //BIN7 誤差率(%)
        mapping.put("bin7masinfuryouritu", "bin7masinfuryouritu"); //BIN7 ﾏｼﾝ不良率(%)
        mapping.put("bin7fukurocheck", "bin7fukurocheck"); //BIN7 袋ﾁｪｯｸ
        mapping.put("bin8setteiti", "bin8setteiti"); //BIN8 %区分(設定値)
        mapping.put("bin8senbetukubun", "bin8senbetukubun"); //BIN8 選別区分
        mapping.put("bin8keiryougosuryou", "bin8keiryougosuryou"); //BIN8 計量後数量
        mapping.put("bin8countersuu", "bin8countersuu"); //BIN8 ｶｳﾝﾀｰ数
        mapping.put("bin8gosaritu", "bin8gosaritu"); //BIN8 誤差率(%)
        mapping.put("bin8masinfuryouritu", "bin8masinfuryouritu"); //BIN8 ﾏｼﾝ不良率(%)
        mapping.put("bin8fukurocheck", "bin8fukurocheck"); //BIN8 袋ﾁｪｯｸ
        mapping.put("bin9keiryougosuryou", "bin9keiryougosuryou"); //BIN9 強制排出 計量後数量
        mapping.put("bin9masinfuryouritu", "bin9masinfuryouritu"); //BIN9 強制排出 ﾏｼﾝ不良率
        mapping.put("rakkakeiryougosuryou", "rakkakeiryougosuryou"); //落下 計量後数量 
        mapping.put("rakkamasinfuryouritu", "rakkamasinfuryouritu"); //落下 ﾏｼﾝ不良率
        mapping.put("handasample", "handasample"); //半田ｻﾝﾌﾟﾙ
        mapping.put("sinraiseisample", "sinraiseisample"); //信頼性ｻﾝﾌﾟﾙ
        mapping.put("sinfuryouhanteisya", "sinfuryouhanteisya"); //真不良判定者
        mapping.put("hanteinyuuryokusya", "hanteinyuuryokusya"); //判定入力者
        mapping.put("toridasisya", "toridasisya"); //取出者
        mapping.put("kousa1", "kousa1"); //公差①
        mapping.put("juryou1", "juryou1"); //重量①
        mapping.put("kosuu1", "kosuu1"); //個数①
        mapping.put("kousa2", "kousa2"); //公差②
        mapping.put("juryou2", "juryou2"); //重量②
        mapping.put("kosuu2", "kosuu2"); //個数②
        mapping.put("kousa3", "kousa3"); //公差③
        mapping.put("juryou3", "juryou3"); //重量③
        mapping.put("kosuu3", "kosuu3"); //個数③
        mapping.put("kousa4", "kousa4"); //公差④
        mapping.put("juryou4", "juryou4"); //重量④
        mapping.put("kosuu4", "kosuu4"); //個数④
        mapping.put("countersousuu", "countersousuu"); //ｶｳﾝﾀｰ総数
        mapping.put("ryohinjuryou", "ryohinjuryou"); //良品重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); //良品個数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("binkakuninsya", "binkakuninsya"); //BIN確認者
        mapping.put("saiken", "saiken"); //電気特性再検
        mapping.put("setubikubun", "setubikubun"); //設備区分
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDenkitokuseiesi>> beanHandler = new BeanListHandler<>(SrDenkitokuseiesi.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [電気特性_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrDenkitokuseiesi> loadTmpSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND deleteflag = ? ";

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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("siteikousa", "siteikousa"); //指定公差
        mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou"); //後工程指示内容
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("gdyakitukenitiji", "gdyakitukenitiji"); //外部電極焼付日時
        mapping.put("mekkinitiji", "mekkinitiji"); //ﾒｯｷ日時
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("senbetukaisinitiji", "senbetukaisinitiji"); //選別開始日時
        mapping.put("senbetusyuryounitiji", "senbetusyuryounitiji"); //選別終了日時
        mapping.put("kensagouki", "kensagouki"); //検査号機
        mapping.put("bunruiairatu", "bunruiairatu"); //分類ｴｱｰ圧
        mapping.put("cdcontactatu", "cdcontactatu"); //CDｺﾝﾀｸﾄ圧
        mapping.put("ircontactatu", "ircontactatu"); //IRｺﾝﾀｸﾄ圧
        mapping.put("stationcd1", "stationcd1"); //使用後ｽﾃｰｼｮﾝ確認CD1
        mapping.put("stationpc1", "stationpc1"); //使用後ｽﾃｰｼｮﾝ確認PC1
        mapping.put("stationpc2", "stationpc2"); //使用後ｽﾃｰｼｮﾝ確認PC2
        mapping.put("stationpc3", "stationpc3"); //使用後ｽﾃｰｼｮﾝ確認PC3
        mapping.put("stationpc4", "stationpc4"); //使用後ｽﾃｰｼｮﾝ確認PC4
        mapping.put("stationir1", "stationir1"); //使用後ｽﾃｰｼｮﾝ確認IR1
        mapping.put("stationir2", "stationir2"); //使用後ｽﾃｰｼｮﾝ確認IR2
        mapping.put("stationir3", "stationir3"); //使用後ｽﾃｰｼｮﾝ確認IR3
        mapping.put("stationir4", "stationir4"); //使用後ｽﾃｰｼｮﾝ確認IR4
        mapping.put("stationir5", "stationir5"); //使用後ｽﾃｰｼｮﾝ確認IR5
        mapping.put("stationir6", "stationir6"); //使用後ｽﾃｰｼｮﾝ確認IR6
        mapping.put("stationir7", "stationir7"); //使用後ｽﾃｰｼｮﾝ確認IR7
        mapping.put("stationir8", "stationir8"); //使用後ｽﾃｰｼｮﾝ確認IR8
        mapping.put("koteidenkyoku", "koteidenkyoku"); //固定電極 外観･段差
        mapping.put("torakkugaido", "torakkugaido"); //ﾄﾗｯｸｶﾞｲﾄﾞ隙間
        mapping.put("testplatekeijo", "testplatekeijo"); //ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
        mapping.put("bunruifukidasi", "bunruifukidasi"); //分類吹き出し穴
        mapping.put("testplatekakunin", "testplatekakunin"); //ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
        mapping.put("denkyokuseisou", "denkyokuseisou"); //電極清掃･動作
        mapping.put("seihintounyuujotai", "seihintounyuujotai"); //製品投入状態
        mapping.put("binboxseisoucheck", "binboxseisoucheck"); //BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
        mapping.put("setsya", "setsya"); //ｾｯﾄ者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("siteikousabudomari1", "siteikousabudomari1"); //指定公差歩留まり1
        mapping.put("siteikousabudomari2", "siteikousabudomari2"); //指定公差歩留まり2
        mapping.put("testplatekanrino", "testplatekanrino"); //ﾃｽﾄﾌﾟﾚｰﾄ管理No
        mapping.put("tan", "tan"); //Tanδ
        mapping.put("sokuteisyuhasuu", "sokuteisyuhasuu"); //測定周波数
        mapping.put("sokuteidenatu", "sokuteidenatu"); //測定電圧
        mapping.put("hoseiyoutippuyoryou", "hoseiyoutippuyoryou"); //補正用ﾁｯﾌﾟ容量
        mapping.put("hoseiyoutipputan", "hoseiyoutipputan"); //補正用ﾁｯﾌﾟTanδ
        mapping.put("hoseimae", "hoseimae"); //補正前
        mapping.put("hoseigo", "hoseigo"); //補正後
        mapping.put("hoseiritu", "hoseiritu"); //補正率
        mapping.put("Standard", "standard"); //ｽﾀﾝﾀﾞｰﾄﾞ補正
        mapping.put("bunruikakunin", "bunruikakunin"); //分類確認
        mapping.put("gaikankakunin", "gaikankakunin"); //外観確認
        mapping.put("netsusyorinitiji", "netsusyorinitiji"); //熱処理日時
        mapping.put("agingjikan", "agingjikan"); //ｴｰｼﾞﾝｸﾞ時間
        mapping.put("jutenritu", "jutenritu"); //充填率
        mapping.put("mc", "mc"); //MC
        mapping.put("kyoseihaisyutu", "kyoseihaisyutu"); //強制排出
        mapping.put("rakka", "rakka"); //落下
        mapping.put("syoninsha", "syoninsha"); //承認者
        mapping.put("furimukesya", "furimukesya"); //振向者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("pcdenatu1", "pcdenatu1"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
        mapping.put("pcjudenjikan1", "pcjudenjikan1"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
        mapping.put("pcdenatu2", "pcdenatu2"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
        mapping.put("pcjudenjikan2", "pcjudenjikan2"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
        mapping.put("pcdenatu3", "pcdenatu3"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
        mapping.put("pcjudenjikan3", "pcjudenjikan3"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
        mapping.put("pcdenatu4", "pcdenatu4"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
        mapping.put("pcjudenjikan4", "pcjudenjikan4"); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
        mapping.put("irdenatu1", "irdenatu1"); //耐電圧設定条件 IR① 電圧
        mapping.put("irhanteiti1", "irhanteiti1"); //耐電圧設定条件 IR① 判定値
        mapping.put("irjudenjikan1", "irjudenjikan1"); //耐電圧設定条件 IR① 充電時間
        mapping.put("irdenatu2", "irdenatu2"); //耐電圧設定条件 IR② 電圧
        mapping.put("irhanteiti2", "irhanteiti2"); //耐電圧設定条件 IR② 判定値
        mapping.put("irjudenjikan2", "irjudenjikan2"); //耐電圧設定条件 IR② 充電時間
        mapping.put("irdenatu3", "irdenatu3"); //耐電圧設定条件 IR③ 電圧
        mapping.put("irhanteiti3", "irhanteiti3"); //耐電圧設定条件 IR③ 判定値
        mapping.put("irjudenjikan3", "irjudenjikan3"); //耐電圧設定条件 IR③ 充電時間
        mapping.put("irdenatu4", "irdenatu4"); //耐電圧設定条件 IR④ 電圧
        mapping.put("irhanteiti4", "irhanteiti4"); //耐電圧設定条件 IR④ 判定値
        mapping.put("irjudenjikan4", "irjudenjikan4"); //耐電圧設定条件 IR④ 充電時間
        mapping.put("irdenatu5", "irdenatu5"); //耐電圧設定条件 IR⑤ 電圧
        mapping.put("irhanteiti5", "irhanteiti5"); //耐電圧設定条件 IR⑤ 判定値
        mapping.put("irjudenjikan5", "irjudenjikan5"); //耐電圧設定条件 IR⑤ 充電時間
        mapping.put("irdenatu6", "irdenatu6"); //耐電圧設定条件 IR⑥ 電圧
        mapping.put("irhanteiti6", "irhanteiti6"); //耐電圧設定条件 IR⑥ 判定値
        mapping.put("irjudenjikan6", "irjudenjikan6"); //耐電圧設定条件 IR⑥ 充電時間
        mapping.put("irdenatu7", "irdenatu7"); //耐電圧設定条件 IR⑦ 電圧
        mapping.put("irhanteiti7", "irhanteiti7"); //耐電圧設定条件 IR⑦ 判定値
        mapping.put("irjudenjikan7", "irjudenjikan7"); //耐電圧設定条件 IR⑦ 充電時間
        mapping.put("irdenatu8", "irdenatu8"); //耐電圧設定条件 IR⑧ 電圧
        mapping.put("irhanteiti8", "irhanteiti8"); //耐電圧設定条件 IR⑧ 判定値
        mapping.put("irjudenjikan8", "irjudenjikan8"); //耐電圧設定条件 IR⑧ 充電時間
        mapping.put("bin1setteiti", "bin1setteiti"); //BIN1 %区分(設定値)
        mapping.put("bin1senbetukubun", "bin1senbetukubun"); //BIN1 選別区分
        mapping.put("bin1keiryougosuryou", "bin1keiryougosuryou"); //BIN1 計量後数量
        mapping.put("bin1countersuu", "bin1countersuu"); //BIN1 ｶｳﾝﾀｰ数
        mapping.put("bin1gosaritu", "bin1gosaritu"); //BIN1 誤差率(%)
        mapping.put("bin1masinfuryouritu", "bin1masinfuryouritu"); //BIN1 ﾏｼﾝ不良率(%)
        mapping.put("bin1nukitorikekkabosuu", "bin1nukitorikekkabosuu"); //BIN1 抜き取り結果
        mapping.put("bin1nukitorikekka", "bin1nukitorikekka"); //BIN1 抜き取り結果 
        mapping.put("bin1sinnofuryouritu", "bin1sinnofuryouritu"); //BIN1 真の不良率(%)
        mapping.put("bin1kekkacheck", "bin1kekkacheck"); //BIN1 結果ﾁｪｯｸ
        mapping.put("bin2setteiti", "bin2setteiti"); //BIN2 %区分(設定値)
        mapping.put("bin2senbetukubun", "bin2senbetukubun"); //BIN2 選別区分
        mapping.put("bin2keiryougosuryou", "bin2keiryougosuryou"); //BIN2 計量後数量
        mapping.put("bin2countersuu", "bin2countersuu"); //BIN2 ｶｳﾝﾀｰ数
        mapping.put("bin2gosaritu", "bin2gosaritu"); //BIN2 誤差率(%)
        mapping.put("bin2masinfuryouritu", "bin2masinfuryouritu"); //BIN2 ﾏｼﾝ不良率(%)
        mapping.put("bin2nukitorikekkabosuu", "bin2nukitorikekkabosuu"); //BIN2 抜き取り結果
        mapping.put("bin2nukitorikekka", "bin2nukitorikekka"); //BIN2 抜き取り結果
        mapping.put("bin2sinnofuryouritu", "bin2sinnofuryouritu"); //BIN2 真の不良率(%)
        mapping.put("bin2kekkacheck", "bin2kekkacheck"); //BIN2 結果ﾁｪｯｸ
        mapping.put("bin3setteiti", "bin3setteiti"); //BIN3 %区分(設定値)
        mapping.put("bin3senbetukubun", "bin3senbetukubun"); //BIN3 選別区分
        mapping.put("bin3keiryougosuryou", "bin3keiryougosuryou"); //BIN3 計量後数量
        mapping.put("bin3countersuu", "bin3countersuu"); //BIN3 ｶｳﾝﾀｰ数
        mapping.put("bin3gosaritu", "bin3gosaritu"); //BIN3 誤差率(%)
        mapping.put("bin3masinfuryouritu", "bin3masinfuryouritu"); //BIN3 ﾏｼﾝ不良率(%)
        mapping.put("bin3nukitorikekkabosuu", "bin3nukitorikekkabosuu"); //BIN3 抜き取り結果
        mapping.put("bin3nukitorikekka", "bin3nukitorikekka"); //BIN3 抜き取り結果
        mapping.put("bin3sinnofuryouritu", "bin3sinnofuryouritu"); //BIN3 真の不良率(%)
        mapping.put("bin3kekkacheck", "bin3kekkacheck"); //BIN3 結果ﾁｪｯｸ
        mapping.put("bin4setteiti", "bin4setteiti"); //BIN4 %区分(設定値)
        mapping.put("bin4senbetukubun", "bin4senbetukubun"); //BIN4 選別区分
        mapping.put("bin4keiryougosuryou", "bin4keiryougosuryou"); //BIN4 計量後数量
        mapping.put("bin4countersuu", "bin4countersuu"); //BIN4 ｶｳﾝﾀｰ数
        mapping.put("bin4gosaritu", "bin4gosaritu"); //BIN4 誤差率(%)
        mapping.put("bin4masinfuryouritu", "bin4masinfuryouritu"); //BIN4 ﾏｼﾝ不良率(%)
        mapping.put("bin4nukitorikekkabosuu", "bin4nukitorikekkabosuu"); //BIN4 抜き取り結果
        mapping.put("bin4nukitorikekka", "bin4nukitorikekka"); //BIN4 抜き取り結果
        mapping.put("bin4sinnofuryouritu", "bin4sinnofuryouritu"); //BIN4 真の不良率(%)
        mapping.put("bin4kekkacheck", "bin4kekkacheck"); //BIN4 結果ﾁｪｯｸ
        mapping.put("bin5setteiti", "bin5setteiti"); //BIN5 %区分(設定値)
        mapping.put("bin5senbetukubun", "bin5senbetukubun"); //BIN5 選別区分
        mapping.put("bin5keiryougosuryou", "bin5keiryougosuryou"); //BIN5 計量後数量
        mapping.put("bin5countersuu", "bin5countersuu"); //BIN5 ｶｳﾝﾀｰ数
        mapping.put("bin5gosaritu", "bin5gosaritu"); //BIN5 誤差率(%)
        mapping.put("bin5masinfuryouritu", "bin5masinfuryouritu"); //BIN5 ﾏｼﾝ不良率(%)
        mapping.put("bin5nukitorikekkabosuu", "bin5nukitorikekkabosuu"); //BIN5 抜き取り結果
        mapping.put("bin5nukitorikekka", "bin5nukitorikekka"); //BIN5 抜き取り結果
        mapping.put("bin5sinnofuryouritu", "bin5sinnofuryouritu"); //BIN5 真の不良率(%)
        mapping.put("bin5kekkacheck", "bin5kekkacheck"); //BIN5 結果ﾁｪｯｸ
        mapping.put("bin5fukurocheck", "bin5fukurocheck"); //BIN5 袋ﾁｪｯｸ
        mapping.put("bin6setteiti", "bin6setteiti"); //BIN6 %区分(設定値)
        mapping.put("bin6senbetukubun", "bin6senbetukubun"); //BIN6 選別区分
        mapping.put("bin6keiryougosuryou", "bin6keiryougosuryou"); //BIN6 計量後数量
        mapping.put("bin6countersuu", "bin6countersuu"); //BIN6 ｶｳﾝﾀｰ数
        mapping.put("bin6gosaritu", "bin6gosaritu"); //BIN6 誤差率(%)
        mapping.put("bin6masinfuryouritu", "bin6masinfuryouritu"); //BIN6 ﾏｼﾝ不良率(%)
        mapping.put("bin6nukitorikekkabosuu", "bin6nukitorikekkabosuu"); //BIN6 抜き取り結果
        mapping.put("bin6nukitorikekka", "bin6nukitorikekka"); //BIN6 抜き取り結果
        mapping.put("bin6sinnofuryouritu", "bin6sinnofuryouritu"); //BIN6 真の不良率(%)
        mapping.put("bin6kekkacheck", "bin6kekkacheck"); //BIN6 結果ﾁｪｯｸ
        mapping.put("bin6fukurocheck", "bin6fukurocheck"); //BIN6 袋ﾁｪｯｸ
        mapping.put("bin7setteiti", "bin7setteiti"); //BIN7 %区分(設定値)
        mapping.put("bin7senbetukubun", "bin7senbetukubun"); //BIN7 選別区分
        mapping.put("bin7keiryougosuryou", "bin7keiryougosuryou"); //BIN7 計量後数量
        mapping.put("bin7countersuu", "bin7countersuu"); //BIN7 ｶｳﾝﾀｰ数
        mapping.put("bin7gosaritu", "bin7gosaritu"); //BIN7 誤差率(%)
        mapping.put("bin7masinfuryouritu", "bin7masinfuryouritu"); //BIN7 ﾏｼﾝ不良率(%)
        mapping.put("bin7fukurocheck", "bin7fukurocheck"); //BIN7 袋ﾁｪｯｸ
        mapping.put("bin8setteiti", "bin8setteiti"); //BIN8 %区分(設定値)
        mapping.put("bin8senbetukubun", "bin8senbetukubun"); //BIN8 選別区分
        mapping.put("bin8keiryougosuryou", "bin8keiryougosuryou"); //BIN8 計量後数量
        mapping.put("bin8countersuu", "bin8countersuu"); //BIN8 ｶｳﾝﾀｰ数
        mapping.put("bin8gosaritu", "bin8gosaritu"); //BIN8 誤差率(%)
        mapping.put("bin8masinfuryouritu", "bin8masinfuryouritu"); //BIN8 ﾏｼﾝ不良率(%)
        mapping.put("bin8fukurocheck", "bin8fukurocheck"); //BIN8 袋ﾁｪｯｸ
        mapping.put("bin9keiryougosuryou", "bin9keiryougosuryou"); //BIN9 強制排出 計量後数量
        mapping.put("bin9masinfuryouritu", "bin9masinfuryouritu"); //BIN9 強制排出 ﾏｼﾝ不良率
        mapping.put("rakkakeiryougosuryou", "rakkakeiryougosuryou"); //落下 計量後数量 
        mapping.put("rakkamasinfuryouritu", "rakkamasinfuryouritu"); //落下 ﾏｼﾝ不良率
        mapping.put("handasample", "handasample"); //半田ｻﾝﾌﾟﾙ
        mapping.put("sinraiseisample", "sinraiseisample"); //信頼性ｻﾝﾌﾟﾙ
        mapping.put("sinfuryouhanteisya", "sinfuryouhanteisya"); //真不良判定者
        mapping.put("hanteinyuuryokusya", "hanteinyuuryokusya"); //判定入力者
        mapping.put("toridasisya", "toridasisya"); //取出者
        mapping.put("kousa1", "kousa1"); //公差①
        mapping.put("juryou1", "juryou1"); //重量①
        mapping.put("kosuu1", "kosuu1"); //個数①
        mapping.put("kousa2", "kousa2"); //公差②
        mapping.put("juryou2", "juryou2"); //重量②
        mapping.put("kosuu2", "kosuu2"); //個数②
        mapping.put("kousa3", "kousa3"); //公差③
        mapping.put("juryou3", "juryou3"); //重量③
        mapping.put("kosuu3", "kosuu3"); //個数③
        mapping.put("kousa4", "kousa4"); //公差④
        mapping.put("juryou4", "juryou4"); //重量④
        mapping.put("kosuu4", "kosuu4"); //個数④
        mapping.put("countersousuu", "countersousuu"); //ｶｳﾝﾀｰ総数
        mapping.put("ryohinjuryou", "ryohinjuryou"); //良品重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); //良品個数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("binkakuninsya", "binkakuninsya"); //BIN確認者
        mapping.put("saiken", "saiken"); //電気特性再検
        mapping.put("setubikubun", "setubikubun"); //設備区分
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDenkitokuseiesi>> beanHandler = new BeanListHandler<>(SrDenkitokuseiesi.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
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
     * 項目データ設定
     *
     * @param processData 処理制御データ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private ProcessData setItemDataEx(ProcessData processData, String itemId, String value) {
        List<FXHDD01> selectData
                = processData.getItemListEx().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
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
     * @param srDenkitokuseiesi 電気特性データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrDenkitokuseiesi srDenkitokuseiesi) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srDenkitokuseiesi != null) {
            // 元データが存在する場合元データより取得
            return getSrDenkitokuseiesiItemData(itemId, srDenkitokuseiesi);
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
     * 電気特性_仮登録(tmp_sr_denkitokuseiesi)登録処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_denkitokuseiesi ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrDenkitokuseiesi(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime,processData, null, jissekino, formId);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 電気特性_仮登録(tmp_sr_denkitokuseiesi)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "UPDATE tmp_sr_denkitokuseiesi SET "
                + "kcpno = ?,tokuisaki = ?,ownercode = ?,lotkubuncode = ?,siteikousa = ?,atokouteisijinaiyou = ?,okuriryouhinsuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,gdyakitukenitiji = ?,"
                + "mekkinitiji = ?,kensabasyo = ?,senbetukaisinitiji = ?,senbetusyuryounitiji = ?,kensagouki = ?,bunruiairatu = ?,cdcontactatu = ?,ircontactatu = ?,stationcd1 = ?,stationpc1 = ?,"
                + "stationpc2 = ?,stationpc3 = ?,stationpc4 = ?,stationir1 = ?,stationir2 = ?,stationir3 = ?,stationir4 = ?,stationir5 = ?,stationir6 = ?,stationir7 = ?,stationir8 = ?,koteidenkyoku = ?,"
                + "torakkugaido = ?,testplatekeijo = ?,bunruifukidasi = ?,testplatekakunin = ?,denkyokuseisou = ?,seihintounyuujotai = ?,binboxseisoucheck = ?,setsya = ?,kakuninsya = ?,"
                + "siteikousabudomari1 = ?,siteikousabudomari2 = ?,testplatekanrino = ?,tan = ?,sokuteisyuhasuu = ?,sokuteidenatu = ?,hoseiyoutippuyoryou = ?,hoseiyoutipputan = ?,hoseimae = ?,"
                + "hoseigo = ?,hoseiritu = ?,Standard = ?,bunruikakunin = ?,gaikankakunin = ?,netsusyorinitiji = ?,agingjikan = ?,jutenritu = ?,mc = ?,kyoseihaisyutu = ?,rakka = ?,syoninsha = ?,"
                + "furimukesya = ?,bikou1 = ?,bikou2 = ?,pcdenatu1 = ?,pcjudenjikan1 = ?,pcdenatu2 = ?,pcjudenjikan2 = ?,pcdenatu3 = ?,pcjudenjikan3 = ?,pcdenatu4 = ?,pcjudenjikan4 = ?,irdenatu1 = ?,"
                + "irhanteiti1 = ?,irjudenjikan1 = ?,irdenatu2 = ?,irhanteiti2 = ?,irjudenjikan2 = ?,irdenatu3 = ?,irhanteiti3 = ?,irjudenjikan3 = ?,irdenatu4 = ?,irhanteiti4 = ?,irjudenjikan4 = ?,"
                + "irdenatu5 = ?,irhanteiti5 = ?,irjudenjikan5 = ?,irdenatu6 = ?,irhanteiti6 = ?,irjudenjikan6 = ?,irdenatu7 = ?,irhanteiti7 = ?,irjudenjikan7 = ?,irdenatu8 = ?,irhanteiti8 = ?,"
                + "irjudenjikan8 = ?,bin1setteiti = ?,bin1senbetukubun = ?,bin1keiryougosuryou = ?,bin1countersuu = ?,bin1gosaritu = ?,bin1masinfuryouritu = ?,bin1nukitorikekkabosuu = ?,"
                + "bin1nukitorikekka = ?,bin1sinnofuryouritu = ?,bin1kekkacheck = ?,bin2setteiti = ?,bin2senbetukubun = ?,bin2keiryougosuryou = ?,bin2countersuu = ?,bin2gosaritu = ?,"
                + "bin2masinfuryouritu = ?,bin2nukitorikekkabosuu = ?,bin2nukitorikekka = ?,bin2sinnofuryouritu = ?,bin2kekkacheck = ?,bin3setteiti = ?,bin3senbetukubun = ?,bin3keiryougosuryou = ?,"
                + "bin3countersuu = ?,bin3gosaritu = ?,bin3masinfuryouritu = ?,bin3nukitorikekkabosuu = ?,bin3nukitorikekka = ?,bin3sinnofuryouritu = ?,bin3kekkacheck = ?,bin4setteiti = ?,"
                + "bin4senbetukubun = ?,bin4keiryougosuryou = ?,bin4countersuu = ?,bin4gosaritu = ?,bin4masinfuryouritu = ?,bin4nukitorikekkabosuu = ?,bin4nukitorikekka = ?,bin4sinnofuryouritu = ?,"
                + "bin4kekkacheck = ?,bin5setteiti = ?,bin5senbetukubun = ?,bin5keiryougosuryou = ?,bin5countersuu = ?,bin5gosaritu = ?,bin5masinfuryouritu = ?,bin5nukitorikekkabosuu = ?,"
                + "bin5nukitorikekka = ?,bin5sinnofuryouritu = ?,bin5kekkacheck = ?,bin5fukurocheck = ?,bin6setteiti = ?,bin6senbetukubun = ?,bin6keiryougosuryou = ?,bin6countersuu = ?,bin6gosaritu = ?,"
                + "bin6masinfuryouritu = ?,bin6nukitorikekkabosuu = ?,bin6nukitorikekka = ?,bin6sinnofuryouritu = ?,bin6kekkacheck = ?,bin6fukurocheck = ?,bin7setteiti = ?,bin7senbetukubun = ?,"
                + "bin7keiryougosuryou = ?,bin7countersuu = ?,bin7gosaritu = ?,bin7masinfuryouritu = ?,bin7fukurocheck = ?,bin8setteiti = ?,bin8senbetukubun = ?,bin8keiryougosuryou = ?,"
                + "bin8countersuu = ?,bin8gosaritu = ?,bin8masinfuryouritu = ?,bin8fukurocheck = ?,bin9keiryougosuryou = ?,bin9masinfuryouritu = ?,rakkakeiryougosuryou = ?,rakkamasinfuryouritu = ?,"
                + "handasample = ?,sinraiseisample = ?,sinfuryouhanteisya = ?,hanteinyuuryokusya = ?,toridasisya = ?,kousa1 = ?,juryou1 = ?,kosuu1 = ?,kousa2 = ?,juryou2 = ?,kosuu2 = ?,kousa3 = ?,"
                + "juryou3 = ?,kosuu3 = ?,kousa4 = ?,juryou4 = ?,kosuu4 = ?,countersousuu = ?,ryohinjuryou = ?,ryohinkosuu = ?,budomari = ?,binkakuninsya = ?,saiken = ?,setubikubun = ?,kosinnichiji = ?,"
                + "revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrDenkitokuseiesi> srSrDenkitokuseiesiList = getSrDenkitokuseiesiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrDenkitokuseiesi srMksinkuukansou = null;
        if (!srSrDenkitokuseiesiList.isEmpty()) {
            srMksinkuukansou = srSrDenkitokuseiesiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrDenkitokuseiesi(false, newRev, 0, "", "", "", systemTime, processData, srMksinkuukansou, jissekino, formId);

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
     * 電気特性_仮登録(tmp_sr_denkitokuseiesi)削除処理
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
    private void deleteTmpSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

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
     * 電気特性_仮登録(tmp_sr_denkitokuseiesi)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srDenkitokuseiesi 電気特性データ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrDenkitokuseiesi(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi, int jissekino, String formId) {
        
        List<FXHDD01> pItemList = processData.getItemList();
        List<FXHDD01> pItemListEx = processData.getItemListEx();
        
        List<Object> params = new ArrayList<>();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_KCPNO, srDenkitokuseiesi))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TOKUISAKI, srDenkitokuseiesi))); //客先
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA, srDenkitokuseiesi))); //指定公差
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_ATOKOUTEI_SHIJI_NAIYO, srDenkitokuseiesi))); //後工程指示内容
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_OKURI_RYOHINSU, srDenkitokuseiesi))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_UKEIRE_TANNIJURYO, srDenkitokuseiesi))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_UKEIRE_SOUJURYO, srDenkitokuseiesi))); //受入れ総重量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_TIME, srDenkitokuseiesi))); //外部電極焼付日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_MEKKI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_MEKKI_TIME, srDenkitokuseiesi))); //ﾒｯｷ日
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_KENSA_BASHO, srDenkitokuseiesi))); //検査場所
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME, srDenkitokuseiesi))); //選別開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME, srDenkitokuseiesi))); //選別終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_KENSA_GOKI, srDenkitokuseiesi))); //検査号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_AIR_ATSU, srDenkitokuseiesi))); //分類ｴｱｰ圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_CD_CONTACT_ATSU, srDenkitokuseiesi))); //CDｺﾝﾀｸﾄ圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_IR_CONTACT_ATSU, srDenkitokuseiesi))); //IRｺﾝﾀｸﾄ圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_CD1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認CD1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC2, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC3, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC4, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR2, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR3, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR4, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR5, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR6, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR7, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR7
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR8, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR8
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_K_GAIKAN_DANSA, srDenkitokuseiesi))); //固定電極 外観･段差
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TRACK_GUIDE_SUKIMA, srDenkitokuseiesi))); //ﾄﾗｯｸｶﾞｲﾄﾞ隙間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_KEIJO_SEISOU, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_FUKIDASHIANA, srDenkitokuseiesi))); //分類吹き出し穴
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_ICHI_KAKUNIN, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_DENKYOKU_SEISOU_DOUSA, srDenkitokuseiesi))); //電極清掃・動作
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SEIHIN_TOUNYU_JOTAI, srDenkitokuseiesi))); //製品投入状態
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIN_BOX_SEISOU_CHECK, srDenkitokuseiesi))); //BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SETSHA, srDenkitokuseiesi))); //ｾｯﾄ者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_KAKUNINSHA, srDenkitokuseiesi))); //確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI1, srDenkitokuseiesi))); //指定公差歩留まり1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI2, srDenkitokuseiesi))); //指定公差歩留まり2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_KANRINO, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ管理No
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_TAN_DELTA, srDenkitokuseiesi))); //Tanδ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SOKUTEI_SHUHASU, srDenkitokuseiesi))); //測定周波数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU, srDenkitokuseiesi))); //測定電圧
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_YORYO, srDenkitokuseiesi))); //補正用ﾁｯﾌﾟ容量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_TAN_DELTA, srDenkitokuseiesi))); //補正用ﾁｯﾌﾟTanδ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIMAE, srDenkitokuseiesi))); //補正前
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIATO, srDenkitokuseiesi))); //補正後
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIRITSU, srDenkitokuseiesi))); //補正率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_STANDARD_HOSEI, srDenkitokuseiesi))); //ｽﾀﾝﾀﾞｰﾄﾞ補正
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_KAKUNIN, srDenkitokuseiesi))); //分類確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_GAIKAN_KAKUNIN, srDenkitokuseiesi))); //外観確認
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_NETSUSYORI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_NETSUSYORI_TIME, srDenkitokuseiesi))); //熱処理日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_AGING_TIME, srDenkitokuseiesi))); //ｴｰｼﾞﾝｸﾞ時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_JUTENRITSU, srDenkitokuseiesi))); //充填率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_MC, srDenkitokuseiesi))); //MC
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_KYOSEI_HAISHUTSU, srDenkitokuseiesi))); //強制排出
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_RAKKA, srDenkitokuseiesi))); //落下
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHONINSHA, srDenkitokuseiesi))); //承認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_FURIMUKESHA, srDenkitokuseiesi))); //振向者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIKOU1, srDenkitokuseiesi))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIKOU2, srDenkitokuseiesi))); //備考2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU1, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME1, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU2, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME2, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU3, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME3, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU4, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME4, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 充電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 電圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 判定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 充電時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_PERCENT_KBN, srDenkitokuseiesi))); //BIN1 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_SENBETSU_KBN, srDenkitokuseiesi))); //BIN1 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN1 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_COUNTER_SU, srDenkitokuseiesi))); //BIN1 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_GOSARITSU, srDenkitokuseiesi))); //BIN1 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN1 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN1 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN1 抜き取り結果 
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN1 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_KEKKA_CHECK, srDenkitokuseiesi))); //BIN1 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_PERCENT_KBN, srDenkitokuseiesi))); //BIN2 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_SENBETSU_KBN, srDenkitokuseiesi))); //BIN2 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN2 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_COUNTER_SU, srDenkitokuseiesi))); //BIN2 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_GOSARITSU, srDenkitokuseiesi))); //BIN2 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN2 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN2 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN2 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN2 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_KEKKA_CHECK, srDenkitokuseiesi))); //BIN2 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_PERCENT_KBN, srDenkitokuseiesi))); //BIN3 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_SENBETSU_KBN, srDenkitokuseiesi))); //BIN3 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN3 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_COUNTER_SU, srDenkitokuseiesi))); //BIN3 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_GOSARITSU, srDenkitokuseiesi))); //BIN3 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN3 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN3 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN3 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN3 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_KEKKA_CHECK, srDenkitokuseiesi))); //BIN3 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_PERCENT_KBN, srDenkitokuseiesi))); //BIN4 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_SENBETSU_KBN, srDenkitokuseiesi))); //BIN4 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN4 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_COUNTER_SU, srDenkitokuseiesi))); //BIN4 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_GOSARITSU, srDenkitokuseiesi))); //BIN4 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN4 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN4 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN4 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN4 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_KEKKA_CHECK, srDenkitokuseiesi))); //BIN4 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_PERCENT_KBN, srDenkitokuseiesi))); //BIN5 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_SENBETSU_KBN, srDenkitokuseiesi))); //BIN5 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN5 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_COUNTER_SU, srDenkitokuseiesi))); //BIN5 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_GOSARITSU, srDenkitokuseiesi))); //BIN5 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN5 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN5 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN5 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN5 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_KEKKA_CHECK, srDenkitokuseiesi))); //BIN5 結果ﾁｪｯｸ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_FUKURO_CHECK, srDenkitokuseiesi))); //BIN5 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_PERCENT_KBN, srDenkitokuseiesi))); //BIN6 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_SENBETSU_KBN, srDenkitokuseiesi))); //BIN6 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN6 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_COUNTER_SU, srDenkitokuseiesi))); //BIN6 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_GOSARITSU, srDenkitokuseiesi))); //BIN6 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN6 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN6 抜き取り結果
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN6 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN6 真の不良率(%)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_KEKKA_CHECK, srDenkitokuseiesi))); //BIN6 結果ﾁｪｯｸ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_FUKURO_CHECK, srDenkitokuseiesi))); //BIN6 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_PERCENT_KBN, srDenkitokuseiesi))); //BIN7 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_SENBETSU_KBN, srDenkitokuseiesi))); //BIN7 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN7 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_COUNTER_SU, srDenkitokuseiesi))); //BIN7 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_GOSARITSU, srDenkitokuseiesi))); //BIN7 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN7 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_FUKURO_CHECK, srDenkitokuseiesi))); //BIN7 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_PERCENT_KBN, srDenkitokuseiesi))); //BIN8 %区分(設定値)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_SENBETSU_KBN, srDenkitokuseiesi))); //BIN8 選別区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN8 計量後数量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_COUNTER_SU, srDenkitokuseiesi))); //BIN8 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_GOSARITSU, srDenkitokuseiesi))); //BIN8 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN8 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_FUKURO_CHECK, srDenkitokuseiesi))); //BIN8 袋ﾁｪｯｸ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN9 強制排出 計量後数量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN9 強制排出 ﾏｼﾝ不良率
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO, srDenkitokuseiesi))); //落下 計量後数量 
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU, srDenkitokuseiesi))); //落下 ﾏｼﾝ不良率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_HANDA_SAMPLE, srDenkitokuseiesi))); //半田ｻﾝﾌﾟﾙ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_SHINRAISEI_SAMPLE, srDenkitokuseiesi))); //信頼性ｻﾝﾌﾟﾙ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA, srDenkitokuseiesi))); //真不良判定者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA, srDenkitokuseiesi))); //判定入力者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_TORIDASHISHA, srDenkitokuseiesi))); //取出者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA1, srDenkitokuseiesi))); //公差①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO1, srDenkitokuseiesi))); //重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU1, srDenkitokuseiesi))); //個数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA2, srDenkitokuseiesi))); //公差②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO2, srDenkitokuseiesi))); //重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU2, srDenkitokuseiesi))); //個数②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA3, srDenkitokuseiesi))); //公差③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO3, srDenkitokuseiesi))); //重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU3, srDenkitokuseiesi))); //個数③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA4, srDenkitokuseiesi))); //公差④
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO4, srDenkitokuseiesi))); //重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU4, srDenkitokuseiesi))); //個数④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_COUNTER_SOSU, srDenkitokuseiesi))); //ｶｳﾝﾀｰ総数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_RYOUHIN_JURYO, srDenkitokuseiesi))); //良品重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_RYOUHIN_KOSU, srDenkitokuseiesi))); //良品個数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_BUDOMARI, srDenkitokuseiesi))); //歩留まり
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO101B040Const.SET_KAKUNINSHA, srDenkitokuseiesi))); //BIN確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO101B040Const.SEIHIN_DENKITOKUSEI_SAIKEN, srDenkitokuseiesi))); //電気特性再検
        params.add(formId); //設備区分
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 電気特性(sr_denkitokuseiesi)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrDenkitokuseiesi 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, ProcessData processData, SrDenkitokuseiesi tmpSrDenkitokuseiesi, String formId) throws SQLException {

        String sql = "INSERT INTO sr_denkitokuseiesi ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,torokunichiji,kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrDenkitokuseiesi(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, processData, tmpSrDenkitokuseiesi, formId);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 電気特性(sr_denkitokuseiesi)更新処理
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
    private void updateSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, ProcessData processData,String formId) throws SQLException {
        String sql = "UPDATE sr_denkitokuseiesi SET "
                + "kcpno = ?,tokuisaki = ?,ownercode = ?,lotkubuncode = ?,siteikousa = ?,atokouteisijinaiyou = ?,okuriryouhinsuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,gdyakitukenitiji = ?,"
                + "mekkinitiji = ?,kensabasyo = ?,senbetukaisinitiji = ?,senbetusyuryounitiji = ?,kensagouki = ?,bunruiairatu = ?,cdcontactatu = ?,ircontactatu = ?,stationcd1 = ?,stationpc1 = ?,"
                + "stationpc2 = ?,stationpc3 = ?,stationpc4 = ?,stationir1 = ?,stationir2 = ?,stationir3 = ?,stationir4 = ?,stationir5 = ?,stationir6 = ?,stationir7 = ?,stationir8 = ?,koteidenkyoku = ?,"
                + "torakkugaido = ?,testplatekeijo = ?,bunruifukidasi = ?,testplatekakunin = ?,denkyokuseisou = ?,seihintounyuujotai = ?,binboxseisoucheck = ?,setsya = ?,kakuninsya = ?,"
                + "siteikousabudomari1 = ?,siteikousabudomari2 = ?,testplatekanrino = ?,tan = ?,sokuteisyuhasuu = ?,sokuteidenatu = ?,hoseiyoutippuyoryou = ?,hoseiyoutipputan = ?,hoseimae = ?,"
                + "hoseigo = ?,hoseiritu = ?,Standard = ?,bunruikakunin = ?,gaikankakunin = ?,netsusyorinitiji = ?,agingjikan = ?,jutenritu = ?,mc = ?,kyoseihaisyutu = ?,rakka = ?,syoninsha = ?,"
                + "furimukesya = ?,bikou1 = ?,bikou2 = ?,pcdenatu1 = ?,pcjudenjikan1 = ?,pcdenatu2 = ?,pcjudenjikan2 = ?,pcdenatu3 = ?,pcjudenjikan3 = ?,pcdenatu4 = ?,pcjudenjikan4 = ?,irdenatu1 = ?,"
                + "irhanteiti1 = ?,irjudenjikan1 = ?,irdenatu2 = ?,irhanteiti2 = ?,irjudenjikan2 = ?,irdenatu3 = ?,irhanteiti3 = ?,irjudenjikan3 = ?,irdenatu4 = ?,irhanteiti4 = ?,irjudenjikan4 = ?,"
                + "irdenatu5 = ?,irhanteiti5 = ?,irjudenjikan5 = ?,irdenatu6 = ?,irhanteiti6 = ?,irjudenjikan6 = ?,irdenatu7 = ?,irhanteiti7 = ?,irjudenjikan7 = ?,irdenatu8 = ?,irhanteiti8 = ?,"
                + "irjudenjikan8 = ?,bin1setteiti = ?,bin1senbetukubun = ?,bin1keiryougosuryou = ?,bin1countersuu = ?,bin1gosaritu = ?,bin1masinfuryouritu = ?,bin1nukitorikekkabosuu = ?,"
                + "bin1nukitorikekka = ?,bin1sinnofuryouritu = ?,bin1kekkacheck = ?,bin2setteiti = ?,bin2senbetukubun = ?,bin2keiryougosuryou = ?,bin2countersuu = ?,bin2gosaritu = ?,"
                + "bin2masinfuryouritu = ?,bin2nukitorikekkabosuu = ?,bin2nukitorikekka = ?,bin2sinnofuryouritu = ?,bin2kekkacheck = ?,bin3setteiti = ?,bin3senbetukubun = ?,bin3keiryougosuryou = ?,"
                + "bin3countersuu = ?,bin3gosaritu = ?,bin3masinfuryouritu = ?,bin3nukitorikekkabosuu = ?,bin3nukitorikekka = ?,bin3sinnofuryouritu = ?,bin3kekkacheck = ?,bin4setteiti = ?,"
                + "bin4senbetukubun = ?,bin4keiryougosuryou = ?,bin4countersuu = ?,bin4gosaritu = ?,bin4masinfuryouritu = ?,bin4nukitorikekkabosuu = ?,bin4nukitorikekka = ?,bin4sinnofuryouritu = ?,"
                + "bin4kekkacheck = ?,bin5setteiti = ?,bin5senbetukubun = ?,bin5keiryougosuryou = ?,bin5countersuu = ?,bin5gosaritu = ?,bin5masinfuryouritu = ?,bin5nukitorikekkabosuu = ?,"
                + "bin5nukitorikekka = ?,bin5sinnofuryouritu = ?,bin5kekkacheck = ?,bin5fukurocheck = ?,bin6setteiti = ?,bin6senbetukubun = ?,bin6keiryougosuryou = ?,bin6countersuu = ?,bin6gosaritu = ?,"
                + "bin6masinfuryouritu = ?,bin6nukitorikekkabosuu = ?,bin6nukitorikekka = ?,bin6sinnofuryouritu = ?,bin6kekkacheck = ?,bin6fukurocheck = ?,bin7setteiti = ?,bin7senbetukubun = ?,"
                + "bin7keiryougosuryou = ?,bin7countersuu = ?,bin7gosaritu = ?,bin7masinfuryouritu = ?,bin7fukurocheck = ?,bin8setteiti = ?,bin8senbetukubun = ?,bin8keiryougosuryou = ?,"
                + "bin8countersuu = ?,bin8gosaritu = ?,bin8masinfuryouritu = ?,bin8fukurocheck = ?,bin9keiryougosuryou = ?,bin9masinfuryouritu = ?,rakkakeiryougosuryou = ?,rakkamasinfuryouritu = ?,"
                + "handasample = ?,sinraiseisample = ?,sinfuryouhanteisya = ?,hanteinyuuryokusya = ?,toridasisya = ?,kousa1 = ?,juryou1 = ?,kosuu1 = ?,kousa2 = ?,juryou2 = ?,kosuu2 = ?,kousa3 = ?,"
                + "juryou3 = ?,kosuu3 = ?,kousa4 = ?,juryou4 = ?,kosuu4 = ?,countersousuu = ?,ryohinjuryou = ?,ryohinkosuu = ?,budomari = ?,binkakuninsya = ?,saiken = ?,setubikubun = ?,kosinnichiji = ?,"
                + "revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrDenkitokuseiesi> srMksinkuukansouList = getSrDenkitokuseiesiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrDenkitokuseiesi srMksinkuukansou = null;
        if (!srMksinkuukansouList.isEmpty()) {
            srMksinkuukansou = srMksinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrDenkitokuseiesi(false, newRev, "", "", "", jissekino, systemTime, processData, srMksinkuukansou, formId);

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
     * 電気特性(sr_denkitokuseiesi)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srDenkitokuseiesi 電気特性データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrDenkitokuseiesi(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, ProcessData processData, SrDenkitokuseiesi srDenkitokuseiesi, String formId) {
        
        List<FXHDD01> pItemList = processData.getItemList();
                List<FXHDD01> pItemListEx = processData.getItemListEx();
        
        List<Object> params = new ArrayList<>();

        
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_KCPNO, srDenkitokuseiesi))); //KCPNO
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TOKUISAKI, srDenkitokuseiesi))); //客先
        
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分

        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA, srDenkitokuseiesi))); //指定公差
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_ATOKOUTEI_SHIJI_NAIYO, srDenkitokuseiesi))); //後工程指示内容
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_OKURI_RYOHINSU, srDenkitokuseiesi))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_UKEIRE_TANNIJURYO, srDenkitokuseiesi))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_UKEIRE_SOUJURYO, srDenkitokuseiesi))); //受入れ総重量
        params.add(DBUtil.stringToDateObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_G_YAKITSUKE_TIME, srDenkitokuseiesi))); //外部電極焼付日時
        params.add(DBUtil.stringToDateObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_MEKKI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_MEKKI_TIME, srDenkitokuseiesi))); //ﾒｯｷ日
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_KENSA_BASHO, srDenkitokuseiesi))); //検査場所
        params.add(DBUtil.stringToDateObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME, srDenkitokuseiesi))); //選別開始日時
        params.add(DBUtil.stringToDateObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME, srDenkitokuseiesi))); //選別終了日時
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_KENSA_GOKI, srDenkitokuseiesi))); //検査号機
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_AIR_ATSU, srDenkitokuseiesi))); //分類ｴｱｰ圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_CD_CONTACT_ATSU, srDenkitokuseiesi))); //CDｺﾝﾀｸﾄ圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_IR_CONTACT_ATSU, srDenkitokuseiesi))); //IRｺﾝﾀｸﾄ圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_CD1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認CD1
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC1
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC2, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC2
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC3, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC3
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC4, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認PC4
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR1, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR1
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR2, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR2
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR3, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR3
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR4, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR4
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR5, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR5
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR6, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR6
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR7, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR7
        params.add(DBUtil.stringToIntObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR8, srDenkitokuseiesi))); //使用後ｽﾃｰｼｮﾝ確認IR8
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_K_GAIKAN_DANSA, srDenkitokuseiesi))); //固定電極 外観･段差
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TRACK_GUIDE_SUKIMA, srDenkitokuseiesi))); //ﾄﾗｯｸｶﾞｲﾄﾞ隙間
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_KEIJO_SEISOU, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_FUKIDASHIANA, srDenkitokuseiesi))); //分類吹き出し穴
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_ICHI_KAKUNIN, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_DENKYOKU_SEISOU_DOUSA, srDenkitokuseiesi))); //電極清掃・動作
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SEIHIN_TOUNYU_JOTAI, srDenkitokuseiesi))); //製品投入状態
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIN_BOX_SEISOU_CHECK, srDenkitokuseiesi))); //BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SETSHA, srDenkitokuseiesi))); //ｾｯﾄ者
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_KAKUNINSHA, srDenkitokuseiesi))); //確認者
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI1, srDenkitokuseiesi))); //指定公差歩留まり1
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI2, srDenkitokuseiesi))); //指定公差歩留まり2
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TEST_PLATE_KANRINO, srDenkitokuseiesi))); //ﾃｽﾄﾌﾟﾚｰﾄ管理No
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_TAN_DELTA, srDenkitokuseiesi))); //Tanδ
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SOKUTEI_SHUHASU, srDenkitokuseiesi))); //測定周波数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU, srDenkitokuseiesi))); //測定電圧
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_YORYO, srDenkitokuseiesi))); //補正用ﾁｯﾌﾟ容量
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_TAN_DELTA, srDenkitokuseiesi))); //補正用ﾁｯﾌﾟTanδ
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIMAE, srDenkitokuseiesi))); //補正前
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIATO, srDenkitokuseiesi))); //補正後
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_HOSEIRITSU, srDenkitokuseiesi))); //補正率
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_STANDARD_HOSEI, srDenkitokuseiesi))); //ｽﾀﾝﾀﾞｰﾄﾞ補正
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BUNRUI_KAKUNIN, srDenkitokuseiesi))); //分類確認
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_GAIKAN_KAKUNIN, srDenkitokuseiesi))); //外観確認
        params.add(DBUtil.stringToDateObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_NETSUSYORI_DAY, srDenkitokuseiesi),
                getItemData(pItemList, GXHDO101B040Const.SEIHIN_NETSUSYORI_TIME, srDenkitokuseiesi))); //熱処理日時
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_AGING_TIME, srDenkitokuseiesi))); //ｴｰｼﾞﾝｸﾞ時間
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_JUTENRITSU, srDenkitokuseiesi))); //充填率
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_MC, srDenkitokuseiesi))); //MC
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_KYOSEI_HAISHUTSU, srDenkitokuseiesi))); //強制排出
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_RAKKA, srDenkitokuseiesi))); //落下
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_SHONINSHA, srDenkitokuseiesi))); //承認者
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_FURIMUKESHA, srDenkitokuseiesi))); //振向者
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIKOU1, srDenkitokuseiesi))); //備考1
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_BIKOU2, srDenkitokuseiesi))); //備考2
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU1, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME1, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU2, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME2, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU3, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME3, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_DENATSU4, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.PRECHARGE_JUDEN_TIME4, srDenkitokuseiesi))); //ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME1, srDenkitokuseiesi))); //耐電圧設定条件 IR① 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME2, srDenkitokuseiesi))); //耐電圧設定条件 IR② 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME3, srDenkitokuseiesi))); //耐電圧設定条件 IR③ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME4, srDenkitokuseiesi))); //耐電圧設定条件 IR④ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME5, srDenkitokuseiesi))); //耐電圧設定条件 IR⑤ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME6, srDenkitokuseiesi))); //耐電圧設定条件 IR⑥ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME7, srDenkitokuseiesi))); //耐電圧設定条件 IR⑦ 充電時間
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_DENATSU8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 電圧
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_HANTEICHI8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 判定値
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.TAIDEN_JUDEN_TIME8, srDenkitokuseiesi))); //耐電圧設定条件 IR⑧ 充電時間
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_PERCENT_KBN, srDenkitokuseiesi))); //BIN1 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_SENBETSU_KBN, srDenkitokuseiesi))); //BIN1 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN1 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_COUNTER_SU, srDenkitokuseiesi))); //BIN1 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_GOSARITSU, srDenkitokuseiesi))); //BIN1 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN1 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN1 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN1 抜き取り結果 
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN1 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN1_KEKKA_CHECK, srDenkitokuseiesi))); //BIN1 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_PERCENT_KBN, srDenkitokuseiesi))); //BIN2 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_SENBETSU_KBN, srDenkitokuseiesi))); //BIN2 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN2 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_COUNTER_SU, srDenkitokuseiesi))); //BIN2 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_GOSARITSU, srDenkitokuseiesi))); //BIN2 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN2 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN2 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN2 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN2 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN2_KEKKA_CHECK, srDenkitokuseiesi))); //BIN2 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_PERCENT_KBN, srDenkitokuseiesi))); //BIN3 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_SENBETSU_KBN, srDenkitokuseiesi))); //BIN3 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN3 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_COUNTER_SU, srDenkitokuseiesi))); //BIN3 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_GOSARITSU, srDenkitokuseiesi))); //BIN3 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN3 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN3 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN3 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN3 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN3_KEKKA_CHECK, srDenkitokuseiesi))); //BIN3 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_PERCENT_KBN, srDenkitokuseiesi))); //BIN4 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_SENBETSU_KBN, srDenkitokuseiesi))); //BIN4 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN4 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_COUNTER_SU, srDenkitokuseiesi))); //BIN4 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_GOSARITSU, srDenkitokuseiesi))); //BIN4 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN4 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN4 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN4 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN4 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN4_KEKKA_CHECK, srDenkitokuseiesi))); //BIN4 結果ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_PERCENT_KBN, srDenkitokuseiesi))); //BIN5 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_SENBETSU_KBN, srDenkitokuseiesi))); //BIN5 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN5 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_COUNTER_SU, srDenkitokuseiesi))); //BIN5 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_GOSARITSU, srDenkitokuseiesi))); //BIN5 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN5 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN5 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN5 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN5 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_KEKKA_CHECK, srDenkitokuseiesi))); //BIN5 結果ﾁｪｯｸ
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN5_FUKURO_CHECK, srDenkitokuseiesi))); //BIN5 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_PERCENT_KBN, srDenkitokuseiesi))); //BIN6 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_SENBETSU_KBN, srDenkitokuseiesi))); //BIN6 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN6 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_COUNTER_SU, srDenkitokuseiesi))); //BIN6 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_GOSARITSU, srDenkitokuseiesi))); //BIN6 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN6 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S, srDenkitokuseiesi))); //BIN6 抜き取り結果
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T, srDenkitokuseiesi))); //BIN6 抜き取り結果
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU, srDenkitokuseiesi))); //BIN6 真の不良率(%)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_KEKKA_CHECK, srDenkitokuseiesi))); //BIN6 結果ﾁｪｯｸ
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN6_FUKURO_CHECK, srDenkitokuseiesi))); //BIN6 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_PERCENT_KBN, srDenkitokuseiesi))); //BIN7 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_SENBETSU_KBN, srDenkitokuseiesi))); //BIN7 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN7 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_COUNTER_SU, srDenkitokuseiesi))); //BIN7 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_GOSARITSU, srDenkitokuseiesi))); //BIN7 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN7 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN7_FUKURO_CHECK, srDenkitokuseiesi))); //BIN7 袋ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_PERCENT_KBN, srDenkitokuseiesi))); //BIN8 %区分(設定値)
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_SENBETSU_KBN, srDenkitokuseiesi))); //BIN8 選別区分
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN8 計量後数量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_COUNTER_SU, srDenkitokuseiesi))); //BIN8 ｶｳﾝﾀｰ数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_GOSARITSU, srDenkitokuseiesi))); //BIN8 誤差率(%)
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN8 ﾏｼﾝ不良率(%)
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN8_FUKURO_CHECK, srDenkitokuseiesi))); //BIN8 袋ﾁｪｯｸ
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO, srDenkitokuseiesi))); //BIN9 強制排出 計量後数量
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU, srDenkitokuseiesi))); //BIN9 強制排出 ﾏｼﾝ不良率
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO, srDenkitokuseiesi))); //落下 計量後数量 
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU, srDenkitokuseiesi))); //落下 ﾏｼﾝ不良率
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_HANDA_SAMPLE, srDenkitokuseiesi))); //半田ｻﾝﾌﾟﾙ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_SHINRAISEI_SAMPLE, srDenkitokuseiesi))); //信頼性ｻﾝﾌﾟﾙ
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA, srDenkitokuseiesi))); //真不良判定者
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA, srDenkitokuseiesi))); //判定入力者
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_TORIDASHISHA, srDenkitokuseiesi))); //取出者
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA1, srDenkitokuseiesi))); //公差①
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO1, srDenkitokuseiesi))); //重量①
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU1, srDenkitokuseiesi))); //個数①
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA2, srDenkitokuseiesi))); //公差②
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO2, srDenkitokuseiesi))); //重量②
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU2, srDenkitokuseiesi))); //個数②
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA3, srDenkitokuseiesi))); //公差③
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO3, srDenkitokuseiesi))); //重量③
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU3, srDenkitokuseiesi))); //個数③
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOUSA4, srDenkitokuseiesi))); //公差④
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_JURYO4, srDenkitokuseiesi))); //重量④
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KOSU4, srDenkitokuseiesi))); //個数④
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_COUNTER_SOSU, srDenkitokuseiesi))); //ｶｳﾝﾀｰ総数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_RYOUHIN_JURYO, srDenkitokuseiesi))); //良品重量
        params.add(DBUtil.stringToIntObject (getItemData(pItemListEx, GXHDO101B040Const.SET_RYOUHIN_KOSU, srDenkitokuseiesi))); //良品個数
        params.add(DBUtil.stringToBigDecimalObject (getItemData(pItemListEx, GXHDO101B040Const.SET_BUDOMARI, srDenkitokuseiesi))); //歩留まり
        params.add(DBUtil.stringToStringObject (getItemData(pItemListEx, GXHDO101B040Const.SET_KAKUNINSHA, srDenkitokuseiesi))); //BIN確認者
        params.add(DBUtil.stringToStringObject (getItemData(pItemList, GXHDO101B040Const.SEIHIN_DENKITOKUSEI_SAIKEN, srDenkitokuseiesi))); //電気特性再検
        params.add(formId); //設備区分
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }

        params.add(newRev); //revision
        return params;
    }

    /**
     * 電気特性(sr_denkitokuseiesi)削除処理
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
    private void deleteSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

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
     * [電気特性_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
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
     * 選別開始日時設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSenbetsuKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 選別終了日時設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSenbetsuShuryoDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }
    
    
    /**
     * BIN計算処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBinKeisan(ProcessData processDate) {
       
        processDate.setMethod("");
        
        //BIN1 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN1_GOSARITSU,GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN1_COUNTER_SU);
        //BIN2 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN2_GOSARITSU,GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN2_COUNTER_SU);
        //BIN3 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN3_GOSARITSU,GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN3_COUNTER_SU);
        //BIN4 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN4_GOSARITSU,GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN4_COUNTER_SU);
        //BIN5 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN5_GOSARITSU,GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN5_COUNTER_SU);
        //BIN6 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN6_GOSARITSU,GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN6_COUNTER_SU);
        //BIN7 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN7_GOSARITSU,GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN7_COUNTER_SU);
        //BIN8 誤差率計算
        calcGosaritsuMain(processDate.getItemListEx(),GXHDO101B040Const.SET_BIN8_GOSARITSU,GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO,GXHDO101B040Const.SET_BIN8_COUNTER_SU);   
        
        return processDate;
    }
    
    
    /**
     * 誤差率計算(ﾒｲﾝ処理)
     * @param itemListEx 項目リスト
     * @param gosaritsuId 誤差率ID
     * @param keiryogoSuryoId 計量後数量ID
     * @param counterSuId  カウンター数ID
     */
    private void calcGosaritsuMain(List<FXHDD01> itemListEx, String gosaritsuId, String keiryogoSuryoId, String counterSuId){
        try{
            FXHDD01 itemGosaritsu = getItemRow(itemListEx,gosaritsuId); //誤差率
            FXHDD01 itemKeiryogoSuryo = getItemRow(itemListEx,keiryogoSuryoId); //計量後数量
            FXHDD01 itemCounterSu = getItemRow(itemListEx,counterSuId); //カウンター数
            
            // 誤差率に値が入力されている場合、リターン
            if(!StringUtil.isEmpty(itemGosaritsu.getValue())){
                return;
            }
            
            BigDecimal keiryogoSuryo = new BigDecimal(itemKeiryogoSuryo.getValue());
            BigDecimal counterSu = new BigDecimal(itemCounterSu.getValue());
            
            // 計量後数量、カウンター数の値のいずれかがZEROの場合"0"をセットしてリターン
            if(BigDecimal.ZERO.compareTo(keiryogoSuryo) == 0  || BigDecimal.ZERO.compareTo(counterSu) == 0){
                itemGosaritsu.setValue("0");
            }
       
            //計量後数量 / ｶｳﾝﾀｰ数 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal gosaritsu = keiryogoSuryo.multiply(BigDecimal.valueOf(100)).divide(counterSu, 2, RoundingMode.HALF_UP);
                   
            //計算結果を誤差率にセット
            itemGosaritsu.setValue(gosaritsu.toPlainString());
        
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
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
     * @param srDenkitokuseiesi 電気特性データ
     * @return DB値
     */
    private String getSrDenkitokuseiesiItemData(String itemId, SrDenkitokuseiesi srDenkitokuseiesi) {
        //TODO
        switch (itemId) {
            //製品情報:KCPNO
            case GXHDO101B040Const.SEIHIN_KCPNO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKcpno());

            //製品情報:客先
            case GXHDO101B040Const.SEIHIN_TOKUISAKI:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTokuisaki());

            //製品情報:ﾛｯﾄ区分
            case GXHDO101B040Const.SEIHIN_LOT_KUBUN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getLotkubuncode());

            //製品情報:ｵｰﾅｰ
            case GXHDO101B040Const.SEIHIN_OWNER:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getOwnercode());

            //製品情報:指定公差
            case GXHDO101B040Const.SEIHIN_SHITEI_KOUSA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSiteikousa());

            //製品情報:後工程指示内容
            case GXHDO101B040Const.SEIHIN_ATOKOUTEI_SHIJI_NAIYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getAtokouteisijinaiyou());

            //製品情報:送り良品数
            case GXHDO101B040Const.SEIHIN_OKURI_RYOHINSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getOkuriryouhinsuu());

            //製品情報:受入れ単位重量
            case GXHDO101B040Const.SEIHIN_UKEIRE_TANNIJURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getUkeiretannijyuryo());

            //製品情報:受入れ総重量
            case GXHDO101B040Const.SEIHIN_UKEIRE_SOUJURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getUkeiresoujyuryou());

            //製品情報:外部電極焼付日
            case GXHDO101B040Const.SEIHIN_G_YAKITSUKE_DAY:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getGdyakitukenitiji(), "yyMMdd");

            //製品情報:外部電極焼付時間
            case GXHDO101B040Const.SEIHIN_G_YAKITSUKE_TIME:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getGdyakitukenitiji(), "HHmm");

            //製品情報:ﾒｯｷ日
            case GXHDO101B040Const.SEIHIN_MEKKI_DAY:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getMekkinitiji(), "yyMMdd");

            //製品情報:ﾒｯｷ時間
            case GXHDO101B040Const.SEIHIN_MEKKI_TIME:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getMekkinitiji(), "HHmm");

            //製品情報:検査場所
            case GXHDO101B040Const.SEIHIN_KENSA_BASHO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKensabasyo());

            //製品情報:選別開始日
            case GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getSenbetukaisinitiji(), "yyMMdd");

            //製品情報:選別開始時間
            case GXHDO101B040Const.SEIHIN_SENBETSU_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getSenbetukaisinitiji(), "HHmm");

            //製品情報:選別終了日
            case GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_DAY:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getSenbetusyuryounitiji(), "yyMMdd");

            //製品情報:選別終了時間
            case GXHDO101B040Const.SEIHIN_SENBETSU_SHURYO_TIME:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getSenbetusyuryounitiji(), "HHmm");

            //製品情報:検査号機
            case GXHDO101B040Const.SEIHIN_KENSA_GOKI:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKensagouki());

            //製品情報:分類ｴｱｰ圧
            case GXHDO101B040Const.SEIHIN_BUNRUI_AIR_ATSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBunruiairatu());

            //製品情報:CDｺﾝﾀｸﾄ圧
            case GXHDO101B040Const.SEIHIN_CD_CONTACT_ATSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getCdcontactatu());

            //製品情報:IRｺﾝﾀｸﾄ圧
            case GXHDO101B040Const.SEIHIN_IR_CONTACT_ATSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrcontactatu());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認CD1
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_CD1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationcd1());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認PC1
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationpc1());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認PC2
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationpc2());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認PC3
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationpc3());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認PC4
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_PC4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationpc4());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR1
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir1());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR2
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir2());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR3
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir3());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR4
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir4());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR5
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR5:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir5());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR6
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR6:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir6());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR7
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR7:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir7());

            //製品情報:使用後ｽﾃｰｼｮﾝ確認IR8
            case GXHDO101B040Const.SEIHIN_SIYOATO_STATION_IR8:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStationir8());

            //製品情報:固定電極 外観･段差
            case GXHDO101B040Const.SEIHIN_K_GAIKAN_DANSA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKoteidenkyoku());

            //製品情報:ﾄﾗｯｸｶﾞｲﾄﾞ隙間
            case GXHDO101B040Const.SEIHIN_TRACK_GUIDE_SUKIMA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTorakkugaido());

            //製品情報:ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
            case GXHDO101B040Const.SEIHIN_TEST_PLATE_KEIJO_SEISOU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTestplatekeijo());

            //製品情報:分類吹き出し穴
            case GXHDO101B040Const.SEIHIN_BUNRUI_FUKIDASHIANA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBunruifukidasi());

            //製品情報:ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
            case GXHDO101B040Const.SEIHIN_TEST_PLATE_ICHI_KAKUNIN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTestplatekakunin());

            //製品情報:電極清掃・動作
            case GXHDO101B040Const.SEIHIN_DENKYOKU_SEISOU_DOUSA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getDenkyokuseisou());

            //製品情報:製品投入状態
            case GXHDO101B040Const.SEIHIN_SEIHIN_TOUNYU_JOTAI:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSeihintounyuujotai());

            //製品情報:BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
            case GXHDO101B040Const.SEIHIN_BIN_BOX_SEISOU_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBinboxseisoucheck());

            //製品情報:ｾｯﾄ者
            case GXHDO101B040Const.SEIHIN_SETSHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSetsya());

            //製品情報:確認者
            case GXHDO101B040Const.SEIHIN_KAKUNINSHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKakuninsya());

            //製品情報:指定公差歩留まり1
            case GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSiteikousabudomari1());

            //製品情報:指定公差歩留まり2
            case GXHDO101B040Const.SEIHIN_SHITEI_KOUSA_BUDOMARI2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSiteikousabudomari2());

            //製品情報:ﾃｽﾄﾌﾟﾚｰﾄ管理No
            case GXHDO101B040Const.SEIHIN_TEST_PLATE_KANRINO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTestplatekanrino());

            //製品情報:Tanδ
            case GXHDO101B040Const.SEIHIN_TAN_DELTA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getTan());

            //製品情報:測定周波数
            case GXHDO101B040Const.SEIHIN_SOKUTEI_SHUHASU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSokuteisyuhasuu());

            //製品情報:測定電圧
            case GXHDO101B040Const.SEIHIN_SOKUTEI_DENATSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSokuteidenatu());

            //製品情報:補正用ﾁｯﾌﾟ容量
            case GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_YORYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHoseiyoutippuyoryou());

            //製品情報:補正用ﾁｯﾌﾟTanδ
            case GXHDO101B040Const.SEIHIN_HOSEIYOU_CHIP_TAN_DELTA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHoseiyoutipputan());

            //製品情報:補正前
            case GXHDO101B040Const.SEIHIN_HOSEIMAE:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHoseimae());

            //製品情報:補正後
            case GXHDO101B040Const.SEIHIN_HOSEIATO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHoseigo());

            //製品情報:補正率
            case GXHDO101B040Const.SEIHIN_HOSEIRITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHoseiritu());

            //製品情報:ｽﾀﾝﾀﾞｰﾄﾞ補正
            case GXHDO101B040Const.SEIHIN_STANDARD_HOSEI:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getStandard());

            //製品情報:分類確認
            case GXHDO101B040Const.SEIHIN_BUNRUI_KAKUNIN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBunruikakunin());

            //製品情報:外観確認
            case GXHDO101B040Const.SEIHIN_GAIKAN_KAKUNIN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getGaikankakunin());

            //製品情報:熱処理日
            case GXHDO101B040Const.SEIHIN_NETSUSYORI_DAY:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getNetsusyorinitiji(), "yyMMdd");

            //製品情報:熱処理時刻
            case GXHDO101B040Const.SEIHIN_NETSUSYORI_TIME:
                return DateUtil.formattedTimestamp(srDenkitokuseiesi.getNetsusyorinitiji(), "HHmm");

            //製品情報:ｴｰｼﾞﾝｸﾞ時間
            case GXHDO101B040Const.SEIHIN_AGING_TIME:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getAgingjikan());

            //製品情報:充填率
            case GXHDO101B040Const.SEIHIN_JUTENRITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getJutenritu());

            //製品情報:MC
            case GXHDO101B040Const.SEIHIN_MC:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getMc());

            //製品情報:強制排出
            case GXHDO101B040Const.SEIHIN_KYOSEI_HAISHUTSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKyoseihaisyutu());

            //製品情報:落下
            case GXHDO101B040Const.SEIHIN_RAKKA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getRakka());

            //製品情報:承認者
            case GXHDO101B040Const.SEIHIN_SHONINSHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSyoninsha());

            //製品情報:振向者
            case GXHDO101B040Const.SEIHIN_FURIMUKESHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getFurimukesya());

            //製品情報:電気特性再検
            case GXHDO101B040Const.SEIHIN_DENKITOKUSEI_SAIKEN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSaiken());

            //製品情報:備考1
            case GXHDO101B040Const.SEIHIN_BIKOU1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBikou1());

            //製品情報:備考2
            case GXHDO101B040Const.SEIHIN_BIKOU2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBikou2());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC① 電圧
            case GXHDO101B040Const.PRECHARGE_DENATSU1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcdenatu1());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC① 充電時間
            case GXHDO101B040Const.PRECHARGE_JUDEN_TIME1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcjudenjikan1());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC② 電圧
            case GXHDO101B040Const.PRECHARGE_DENATSU2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcdenatu2());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC② 充電時間
            case GXHDO101B040Const.PRECHARGE_JUDEN_TIME2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcjudenjikan2());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC③ 電圧
            case GXHDO101B040Const.PRECHARGE_DENATSU3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcdenatu3());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC③ 充電時間
            case GXHDO101B040Const.PRECHARGE_JUDEN_TIME3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcjudenjikan3());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC④ 電圧
            case GXHDO101B040Const.PRECHARGE_DENATSU4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcdenatu4());

            //ﾌﾟﾘﾁｬｰｼﾞ条件:PC④ 充電時間
            case GXHDO101B040Const.PRECHARGE_JUDEN_TIME4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getPcjudenjikan4());

            //耐電圧設定条件:IR① 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu1());

            //耐電圧設定条件:IR① 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti1());

            //耐電圧設定条件:IR① 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan1());

            //耐電圧設定条件:IR② 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu2());

            //耐電圧設定条件:IR② 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti2());

            //耐電圧設定条件:IR② 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan2());

            //耐電圧設定条件:IR③ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu3());

            //耐電圧設定条件:IR③ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti3());

            //耐電圧設定条件:IR③ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan3());

            //耐電圧設定条件:IR④ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu4());

            //耐電圧設定条件:IR④ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti4());

            //耐電圧設定条件:IR④ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan4());

            //耐電圧設定条件:IR⑤ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU5:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu5());

            //耐電圧設定条件:IR⑤ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI5:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti5());

            //耐電圧設定条件:IR⑤ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME5:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan5());

            //耐電圧設定条件:IR⑥ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU6:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu6());

            //耐電圧設定条件:IR⑥ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI6:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti6());

            //耐電圧設定条件:IR⑥ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME6:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan6());

            //耐電圧設定条件:IR⑦ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU7:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu7());

            //耐電圧設定条件:IR⑦ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI7:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti7());

            //耐電圧設定条件:IR⑦ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME7:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan7());

            //耐電圧設定条件:IR⑧ 電圧
            case GXHDO101B040Const.TAIDEN_DENATSU8:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrdenatu8());

            //耐電圧設定条件:IR⑧ 判定値
            case GXHDO101B040Const.TAIDEN_HANTEICHI8:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrhanteiti8());

            //耐電圧設定条件:IR⑧ 充電時間
            case GXHDO101B040Const.TAIDEN_JUDEN_TIME8:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getIrjudenjikan8());

            //設定条件及び処理結果:BIN1 %区分(設定値)
            case GXHDO101B040Const.SET_BIN1_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1setteiti());

            //設定条件及び処理結果:BIN1 選別区分
            case GXHDO101B040Const.SET_BIN1_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1senbetukubun());

            //設定条件及び処理結果:BIN1 計量後数量
            case GXHDO101B040Const.SET_BIN1_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1keiryougosuryou());

            //設定条件及び処理結果:BIN1 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN1_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1countersuu());

            //設定条件及び処理結果:BIN1 誤差率(%)
            case GXHDO101B040Const.SET_BIN1_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1gosaritu());

            //設定条件及び処理結果:BIN1 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN1_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1masinfuryouritu());

            //設定条件及び処理結果:BIN1 抜き取り結果
            case GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1nukitorikekkabosuu());

            //設定条件及び処理結果:BIN1 抜き取り結果
            case GXHDO101B040Const.SET_BIN1_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1nukitorikekka());

            //設定条件及び処理結果:BIN1 真の不良率(%)
            case GXHDO101B040Const.SET_BIN1_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1sinnofuryouritu());

            //設定条件及び処理結果:BIN1 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN1_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin1kekkacheck());

            //設定条件及び処理結果:BIN2 %区分(設定値)
            case GXHDO101B040Const.SET_BIN2_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2setteiti());

            //設定条件及び処理結果:BIN2 選別区分
            case GXHDO101B040Const.SET_BIN2_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2senbetukubun());

            //設定条件及び処理結果:BIN2 計量後数量
            case GXHDO101B040Const.SET_BIN2_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2keiryougosuryou());

            //設定条件及び処理結果:BIN2 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN2_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2countersuu());

            //設定条件及び処理結果:BIN2 誤差率(%)
            case GXHDO101B040Const.SET_BIN2_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2gosaritu());

            //設定条件及び処理結果:BIN2 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN2_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2masinfuryouritu());

            //設定条件及び処理結果:BIN2 抜き取り結果
            case GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2nukitorikekkabosuu());

            //設定条件及び処理結果:BIN2 抜き取り結果
            case GXHDO101B040Const.SET_BIN2_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2nukitorikekka());

            //設定条件及び処理結果:BIN2 真の不良率(%)
            case GXHDO101B040Const.SET_BIN2_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2sinnofuryouritu());

            //設定条件及び処理結果:BIN2 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN2_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin2kekkacheck());

            //設定条件及び処理結果:BIN3 %区分(設定値)
            case GXHDO101B040Const.SET_BIN3_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3setteiti());

            //設定条件及び処理結果:BIN3 選別区分
            case GXHDO101B040Const.SET_BIN3_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3senbetukubun());

            //設定条件及び処理結果:BIN3 計量後数量
            case GXHDO101B040Const.SET_BIN3_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3keiryougosuryou());

            //設定条件及び処理結果:BIN3 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN3_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3countersuu());

            //設定条件及び処理結果:BIN3 誤差率(%)
            case GXHDO101B040Const.SET_BIN3_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3gosaritu());

            //設定条件及び処理結果:BIN3 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN3_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3masinfuryouritu());

            //設定条件及び処理結果:BIN3 抜き取り結果
            case GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3nukitorikekkabosuu());

            //設定条件及び処理結果:BIN3 抜き取り結果
            case GXHDO101B040Const.SET_BIN3_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3nukitorikekka());

            //設定条件及び処理結果:BIN3 真の不良率(%)
            case GXHDO101B040Const.SET_BIN3_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3sinnofuryouritu());

            //設定条件及び処理結果:BIN3 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN3_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin3kekkacheck());

            //設定条件及び処理結果:BIN4 %区分(設定値)
            case GXHDO101B040Const.SET_BIN4_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4setteiti());

            //設定条件及び処理結果:BIN4 選別区分
            case GXHDO101B040Const.SET_BIN4_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4senbetukubun());

            //設定条件及び処理結果:BIN4 計量後数量
            case GXHDO101B040Const.SET_BIN4_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4keiryougosuryou());

            //設定条件及び処理結果:BIN4 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN4_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4countersuu());

            //設定条件及び処理結果:BIN4 誤差率(%)
            case GXHDO101B040Const.SET_BIN4_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4gosaritu());

            //設定条件及び処理結果:BIN4 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN4_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4masinfuryouritu());

            //設定条件及び処理結果:BIN4 抜き取り結果
            case GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4nukitorikekkabosuu());

            //設定条件及び処理結果:BIN4 抜き取り結果
            case GXHDO101B040Const.SET_BIN4_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4nukitorikekka());

            //設定条件及び処理結果:BIN4 真の不良率(%)
            case GXHDO101B040Const.SET_BIN4_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4sinnofuryouritu());

            //設定条件及び処理結果:BIN4 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN4_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin4kekkacheck());

            //設定条件及び処理結果:BIN5 %区分(設定値)
            case GXHDO101B040Const.SET_BIN5_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5setteiti());

            //設定条件及び処理結果:BIN5 選別区分
            case GXHDO101B040Const.SET_BIN5_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5senbetukubun());

            //設定条件及び処理結果:BIN5 計量後数量
            case GXHDO101B040Const.SET_BIN5_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5keiryougosuryou());

            //設定条件及び処理結果:BIN5 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN5_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5countersuu());

            //設定条件及び処理結果:BIN5 誤差率(%)
            case GXHDO101B040Const.SET_BIN5_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5gosaritu());

            //設定条件及び処理結果:BIN5 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN5_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5masinfuryouritu());

            //設定条件及び処理結果:BIN5 抜き取り結果
            case GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5nukitorikekkabosuu());

            //設定条件及び処理結果:BIN5 抜き取り結果
            case GXHDO101B040Const.SET_BIN5_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5nukitorikekka());

            //設定条件及び処理結果:BIN5 真の不良率(%)
            case GXHDO101B040Const.SET_BIN5_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5sinnofuryouritu());

            //設定条件及び処理結果:BIN5 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN5_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5kekkacheck());

            //設定条件及び処理結果:BIN5 袋ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN5_FUKURO_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin5fukurocheck());

            //設定条件及び処理結果:BIN6 %区分(設定値)
            case GXHDO101B040Const.SET_BIN6_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6setteiti());

            //設定条件及び処理結果:BIN6 選別区分
            case GXHDO101B040Const.SET_BIN6_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6senbetukubun());

            //設定条件及び処理結果:BIN6 計量後数量
            case GXHDO101B040Const.SET_BIN6_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6keiryougosuryou());

            //設定条件及び処理結果:BIN6 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN6_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6countersuu());

            //設定条件及び処理結果:BIN6 誤差率(%)
            case GXHDO101B040Const.SET_BIN6_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6gosaritu());

            //設定条件及び処理結果:BIN6 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN6_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6masinfuryouritu());

            //設定条件及び処理結果:BIN6 抜き取り結果
            case GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_S:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6nukitorikekkabosuu());

            //設定条件及び処理結果:BIN6 抜き取り結果
            case GXHDO101B040Const.SET_BIN6_NUKITORIKEKKA_T:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6nukitorikekka());

            //設定条件及び処理結果:BIN6 真の不良率(%)
            case GXHDO101B040Const.SET_BIN6_SHIN_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6sinnofuryouritu());

            //設定条件及び処理結果:BIN6 結果ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN6_KEKKA_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6kekkacheck());

            //設定条件及び処理結果:BIN6 袋ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN6_FUKURO_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin6fukurocheck());

            //設定条件及び処理結果:BIN7 %区分(設定値)
            case GXHDO101B040Const.SET_BIN7_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7setteiti());

            //設定条件及び処理結果:BIN7 選別区分
            case GXHDO101B040Const.SET_BIN7_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7senbetukubun());

            //設定条件及び処理結果:BIN7 計量後数量
            case GXHDO101B040Const.SET_BIN7_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7keiryougosuryou());

            //設定条件及び処理結果:BIN7 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN7_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7countersuu());

            //設定条件及び処理結果:BIN7 誤差率(%)
            case GXHDO101B040Const.SET_BIN7_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7gosaritu());

            //設定条件及び処理結果:BIN7 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN7_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7masinfuryouritu());

            //設定条件及び処理結果:BIN7 袋ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN7_FUKURO_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin7fukurocheck());

            //設定条件及び処理結果:BIN8 %区分(設定値)
            case GXHDO101B040Const.SET_BIN8_PERCENT_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8setteiti());

            //設定条件及び処理結果:BIN8 選別区分
            case GXHDO101B040Const.SET_BIN8_SENBETSU_KBN:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8senbetukubun());

            //設定条件及び処理結果:BIN8 計量後数量
            case GXHDO101B040Const.SET_BIN8_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8keiryougosuryou());

            //設定条件及び処理結果:BIN8 ｶｳﾝﾀｰ数
            case GXHDO101B040Const.SET_BIN8_COUNTER_SU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8countersuu());

            //設定条件及び処理結果:BIN8 誤差率(%)
            case GXHDO101B040Const.SET_BIN8_GOSARITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8gosaritu());

            //設定条件及び処理結果:BIN8 ﾏｼﾝ不良率(%)
            case GXHDO101B040Const.SET_BIN8_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8masinfuryouritu());

            //設定条件及び処理結果:BIN8 袋ﾁｪｯｸ
            case GXHDO101B040Const.SET_BIN8_FUKURO_CHECK:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin8fukurocheck());

            //設定条件及び処理結果:BIN9 強制排出 計量後数量
            case GXHDO101B040Const.SET_BIN9_K_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin9keiryougosuryou());

            //設定条件及び処理結果:BIN9 強制排出 ﾏｼﾝ不良率
            case GXHDO101B040Const.SET_BIN9_K_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBin9masinfuryouritu());

            //設定条件及び処理結果:落下 計量後数量
            case GXHDO101B040Const.SET_RAKKA_KEIRYOGO_SURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getRakkakeiryougosuryou());

            //設定条件及び処理結果:落下 ﾏｼﾝ不良率
            case GXHDO101B040Const.SET_RAKKA_MACHINE_FURYORITSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getRakkamasinfuryouritu());

            //設定条件及び処理結果:半田ｻﾝﾌﾟﾙ
            case GXHDO101B040Const.SET_HANDA_SAMPLE:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHandasample());

            //設定条件及び処理結果:信頼性ｻﾝﾌﾟﾙ
            case GXHDO101B040Const.SET_SHINRAISEI_SAMPLE:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSinraiseisample());

            //設定条件及び処理結果:真不良判定者
            case GXHDO101B040Const.SET_SHIN_FURYO_HANTEISHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getSinfuryouhanteisya());

            //設定条件及び処理結果:判定入力者
            case GXHDO101B040Const.SET_HANTEI_NYURYOKUSHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getHanteinyuuryokusya());

            //設定条件及び処理結果:取出者
            case GXHDO101B040Const.SET_TORIDASHISHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getToridasisya());

            //設定条件及び処理結果:公差①
            case GXHDO101B040Const.SET_KOUSA1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKousa1());

            //設定条件及び処理結果:重量①
            case GXHDO101B040Const.SET_JURYO1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getJuryou1());

            //設定条件及び処理結果:個数①
            case GXHDO101B040Const.SET_KOSU1:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKosuu1());

            //設定条件及び処理結果:公差②
            case GXHDO101B040Const.SET_KOUSA2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKousa2());

            //設定条件及び処理結果:重量②
            case GXHDO101B040Const.SET_JURYO2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getJuryou2());

            //設定条件及び処理結果:個数②
            case GXHDO101B040Const.SET_KOSU2:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKosuu2());

            //設定条件及び処理結果:公差③
            case GXHDO101B040Const.SET_KOUSA3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKousa3());

            //設定条件及び処理結果:重量③
            case GXHDO101B040Const.SET_JURYO3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getJuryou3());

            //設定条件及び処理結果:個数③
            case GXHDO101B040Const.SET_KOSU3:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKosuu3());

            //設定条件及び処理結果:公差④
            case GXHDO101B040Const.SET_KOUSA4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKousa4());

            //設定条件及び処理結果:重量④
            case GXHDO101B040Const.SET_JURYO4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getJuryou4());

            //設定条件及び処理結果:個数④
            case GXHDO101B040Const.SET_KOSU4:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKosuu4());

            //設定条件及び処理結果:ｶｳﾝﾀｰ総数
            case GXHDO101B040Const.SET_COUNTER_SOSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getCountersousuu());

            //設定条件及び処理結果:良品重量
            case GXHDO101B040Const.SET_RYOUHIN_JURYO:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getRyohinjuryou());

            //設定条件及び処理結果:良品個数
            case GXHDO101B040Const.SET_RYOUHIN_KOSU:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getRyohinkosuu());

            //設定条件及び処理結果:歩留まり
            case GXHDO101B040Const.SET_BUDOMARI:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getBudomari());

            //設定条件及び処理結果:確認者
            case GXHDO101B040Const.SET_KAKUNINSHA:
                return StringUtil.nullToBlank(srDenkitokuseiesi.getKakuninsya());

            default:
                return null;
        }
    }

    /**
     * 電気特性_仮登録(tmp_sr_denkitokuseiesi)登録処理(削除時)
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
    private void insertDeleteDataTmpSrDenkitokuseiesi(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_denkitokuseiesi ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,siteikousa,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gdyakitukenitiji,mekkinitiji,kensabasyo,senbetukaisinitiji,senbetusyuryounitiji,kensagouki,bunruiairatu,cdcontactatu,ircontactatu,stationcd1,stationpc1,stationpc2,"
                + "stationpc3,stationpc4,stationir1,stationir2,stationir3,stationir4,stationir5,stationir6,stationir7,stationir8,koteidenkyoku,torakkugaido,testplatekeijo,bunruifukidasi,"
                + "testplatekakunin,denkyokuseisou,seihintounyuujotai,binboxseisoucheck,setsya,kakuninsya,siteikousabudomari1,siteikousabudomari2,testplatekanrino,tan,sokuteisyuhasuu,"
                + "sokuteidenatu,hoseiyoutippuyoryou,hoseiyoutipputan,hoseimae,hoseigo,hoseiritu,Standard,bunruikakunin,gaikankakunin,netsusyorinitiji,agingjikan,jutenritu,mc,"
                + "kyoseihaisyutu,rakka,syoninsha,furimukesya,bikou1,bikou2,pcdenatu1,pcjudenjikan1,pcdenatu2,pcjudenjikan2,pcdenatu3,pcjudenjikan3,pcdenatu4,pcjudenjikan4,irdenatu1,"
                + "irhanteiti1,irjudenjikan1,irdenatu2,irhanteiti2,irjudenjikan2,irdenatu3,irhanteiti3,irjudenjikan3,irdenatu4,irhanteiti4,irjudenjikan4,irdenatu5,irhanteiti5,irjudenjikan5,"
                + "irdenatu6,irhanteiti6,irjudenjikan6,irdenatu7,irhanteiti7,irjudenjikan7,irdenatu8,irhanteiti8,irjudenjikan8,bin1setteiti,bin1senbetukubun,bin1keiryougosuryou,"
                + "bin1countersuu,bin1gosaritu,bin1masinfuryouritu,bin1nukitorikekkabosuu,bin1nukitorikekka,bin1sinnofuryouritu,bin1kekkacheck,bin2setteiti,bin2senbetukubun,"
                + "bin2keiryougosuryou,bin2countersuu,bin2gosaritu,bin2masinfuryouritu,bin2nukitorikekkabosuu,bin2nukitorikekka,bin2sinnofuryouritu,bin2kekkacheck,bin3setteiti,"
                + "bin3senbetukubun,bin3keiryougosuryou,bin3countersuu,bin3gosaritu,bin3masinfuryouritu,bin3nukitorikekkabosuu,bin3nukitorikekka,bin3sinnofuryouritu,bin3kekkacheck,"
                + "bin4setteiti,bin4senbetukubun,bin4keiryougosuryou,bin4countersuu,bin4gosaritu,bin4masinfuryouritu,bin4nukitorikekkabosuu,bin4nukitorikekka,bin4sinnofuryouritu,"
                + "bin4kekkacheck,bin5setteiti,bin5senbetukubun,bin5keiryougosuryou,bin5countersuu,bin5gosaritu,bin5masinfuryouritu,bin5nukitorikekkabosuu,bin5nukitorikekka,"
                + "bin5sinnofuryouritu,bin5kekkacheck,bin5fukurocheck,bin6setteiti,bin6senbetukubun,bin6keiryougosuryou,bin6countersuu,bin6gosaritu,bin6masinfuryouritu,bin6nukitorikekkabosuu,"
                + "bin6nukitorikekka,bin6sinnofuryouritu,bin6kekkacheck,bin6fukurocheck,bin7setteiti,bin7senbetukubun,bin7keiryougosuryou,bin7countersuu,bin7gosaritu,bin7masinfuryouritu,"
                + "bin7fukurocheck,bin8setteiti,bin8senbetukubun,bin8keiryougosuryou,bin8countersuu,bin8gosaritu,bin8masinfuryouritu,bin8fukurocheck,bin9keiryougosuryou,bin9masinfuryouritu,"
                + "rakkakeiryougosuryou,rakkamasinfuryouritu,handasample,sinraiseisample,sinfuryouhanteisya,hanteinyuuryokusya,toridasisya,kousa1,juryou1,kosuu1,kousa2,juryou2,kosuu2,kousa3,"
                + "juryou3,kosuu3,kousa4,juryou4,kosuu4,countersousuu,ryohinjuryou,ryohinkosuu,budomari,binkakuninsya,saiken,setubikubun,?,?,?,?"
                + " FROM sr_denkitokuseiesi "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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

    
    private String getParamData(HttpSession session, String key){
        key = key.toUpperCase();
        HashMap<String, String>  parameterMap = (HashMap<String, String>) session.getAttribute("parameter");
        //parameterMapがなければ作成
        if (parameterMap == null) {
            return null;
        }
        
        return parameterMap.get(key);
    }
    
//    /**
//     * ユーザー認証チェック
//     */
//    @Override
//    public void userAuth() {
//        super.userAuth();
// 
//    }
}
