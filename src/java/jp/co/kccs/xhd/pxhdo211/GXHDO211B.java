/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo211;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDD06;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	焼成温度管理画面選択(コンデンサ)<br>
 * <br>
 * 変更日	2019/09/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/02/28<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	登録実行後は次の入力が可能なように画面をクリアするように修正<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD201B(焼成温度管理登録)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/09/06
 */
@Named
@ViewScoped
public class GXHDO211B implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO211B.class.getName());

    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;

//<editor-fold defaultstate="collapsed" desc="#変数(画面項目)">
    /**
     * Rev
     */
    private int searchedRev = 0;

    /**
     * 警告メッセージ
     */
    private String warnMessage = "";

    /**
     * ロットNo
     */
    private String lotno;

    /**
     * 指示者
     */
    private String shijisya;

    /**
     * 号機1
     */
    private String gouki1;

    /**
     * ピーク温度1
     */
    private String peakOndo1;

    /**
     * 水素濃度1
     */
    private String suisoNoudo1;

    /**
     * 号機2
     */
    private String gouki2;

    /**
     * ピーク温度2
     */
    private String peakOndo2;

    /**
     * 水素濃度2
     */
    private String suisoNoudo2;

    /**
     * 号機3
     */
    private String gouki3;

    /**
     * ピーク温度3
     */
    private String peakOndo3;

    /**
     * 水素濃度3
     */
    private String suisoNoudo3;

    /**
     * 号機4
     */
    private String gouki4;

    /**
     * ピーク温度4
     */
    private String peakOndo4;

    /**
     * 水素濃度4
     */
    private String suisoNoudo4;

    /**
     * 号機5
     */
    private String gouki5;

    /**
     * ピーク温度5
     */
    private String peakOndo5;

    /**
     * 水素濃度5
     */
    private String suisoNoudo5;

    /**
     * ロットNo(BackGround)
     */
    private String lotnoBackGround;

    /**
     * 指示者(BackGround)
     */
    private String shijisyaBackGround;

    /**
     * 号機1(BackGround)
     */
    private String gouki1BackGround;

    /**
     * ピーク温度1(BackGround)
     */
    private String peakOndo1BackGround;

    /**
     * 水素濃度1(BackGround)
     */
    private String suisoNoudo1BackGround;

    /**
     * 号機2(BackGround)
     */
    private String gouki2BackGround;

    /**
     * ピーク温度2(BackGround)
     */
    private String peakOndo2BackGround;

    /**
     * 水素濃度2(BackGround)
     */
    private String suisoNoudo2BackGround;

    /**
     * 号機3(BackGround)
     */
    private String gouki3BackGround;

    /**
     * ピーク温度3(BackGround)
     */
    private String peakOndo3BackGround;

    /**
     * 水素濃度3(BackGround)
     */
    private String suisoNoudo3BackGround;

    /**
     * 号機4(BackGround)
     */
    private String gouki4BackGround;

    /**
     * ピーク温度4(BackGround)
     */
    private String peakOndo4BackGround;

    /**
     * 水素濃度4(BackGround)
     */
    private String suisoNoudo4BackGround;

    /**
     * 号機5(BackGround)
     */
    private String gouki5BackGround;

    /**
     * ピーク温度5(BackGround)
     */
    private String peakOndo5BackGround;

    /**
     * 水素濃度5(BackGround)
     */
    private String suisoNoudo5BackGround;

    /**
     * 【条件情報】温度
     */
    private String jokenOndo;

    /**
     * 【条件情報】水素濃度
     */
    private String jokenSuisoNoudo;

    /**
     * 【仕掛情報】KCPNO
     */
    private String sikakariKcpno;

    /**
     * 【仕掛情報】工程
     */
    private String sikakariKotei;

    /**
     * 【仕掛情報】数量
     */
    private String sikakariSuryo;

    /**
     * 入力エリア使用可否(ReadOnly)
     */
    private Boolean inputAreaReadOnly;

    /**
     * 入力エリア使用可否(Disabled)
     */
    private Boolean inputAreaDisabled;

    /**
     * 検索エリア使用可否
     */
    private Boolean searchAreaDisabled;

    /**
     * 検索ボタン使用可否
     */
    private Boolean btnSearchDisabled;

    /**
     * クリアボタン使用可否
     */
    private Boolean btnClearDisabled;

    /**
     * 削除ボタン使用可否
     */
    private Boolean btnDeleteDisabled;

    /**
     * 登録ボタン使用可否
     */
    private Boolean btnRegistDisabled;

    /**
     * 警告時処理
     */
    private String warnProcess = "";

//</editor-fold>  
    /**
     * コンストラクタ
     */
    public GXHDO211B() {
    }

//<editor-fold defaultstate="collapsed" desc="#setter getter">
    /**
     * 警告メッセージ
     *
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     *
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    /**
     * ロットNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ロットNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = StringUtil.trimAll(lotno);
    }

    /**
     * 指示者
     *
     * @return the shijisya
     */
    public String getShijisya() {
        return shijisya;
    }

    /**
     * 指示者
     *
     * @param shijisya the shijisya to set
     */
    public void setShijisya(String shijisya) {
        this.shijisya = StringUtil.trimAll(shijisya);
    }

    /**
     * 号機1
     *
     * @return the gouki1
     */
    public String getGouki1() {
        return gouki1;
    }

    /**
     * 号機1
     *
     * @param gouki1 the gouki1 to set
     */
    public void setGouki1(String gouki1) {
        this.gouki1 = StringUtil.trimAll(gouki1);
    }

    /**
     * ピーク温度1
     *
     * @return the peakOndo1
     */
    public String getPeakOndo1() {
        return peakOndo1;
    }

    /**
     * ピーク温度1
     *
     * @param peakOndo1 the peakOndo1 to set
     */
    public void setPeakOndo1(String peakOndo1) {
        this.peakOndo1 = StringUtil.trimAll(peakOndo1);
    }

    /**
     * 水素濃度1
     *
     * @return the suisoNoudo1
     */
    public String getSuisoNoudo1() {
        return suisoNoudo1;
    }

    /**
     * 水素濃度1
     *
     * @param suisoNoudo1 the suisoNoudo1 to set
     */
    public void setSuisoNoudo1(String suisoNoudo1) {
        this.suisoNoudo1 = StringUtil.trimAll(suisoNoudo1);
    }

    /**
     * 号機2
     *
     * @return the gouki2
     */
    public String getGouki2() {
        return gouki2;
    }

    /**
     * 号機2
     *
     * @param gouki2 the gouki2 to set
     */
    public void setGouki2(String gouki2) {
        this.gouki2 = StringUtil.trimAll(gouki2);
    }

    /**
     * ピーク温度2
     *
     * @return the peakOndo2
     */
    public String getPeakOndo2() {
        return peakOndo2;
    }

    /**
     * ピーク温度2
     *
     * @param peakOndo2 the peakOndo2 to set
     */
    public void setPeakOndo2(String peakOndo2) {
        this.peakOndo2 = StringUtil.trimAll(peakOndo2);
    }

    /**
     * 水素濃度2
     *
     * @return the suisoNoudo2
     */
    public String getSuisoNoudo2() {
        return suisoNoudo2;
    }

    /**
     * 水素濃度2
     *
     * @param suisoNoudo2 the suisoNoudo2 to set
     */
    public void setSuisoNoudo2(String suisoNoudo2) {
        this.suisoNoudo2 = StringUtil.trimAll(suisoNoudo2);
    }

    /**
     * 号機3
     *
     * @return the gouki3
     */
    public String getGouki3() {
        return gouki3;
    }

    /**
     * 号機3
     *
     * @param gouki3 the gouki3 to set
     */
    public void setGouki3(String gouki3) {
        this.gouki3 = StringUtil.trimAll(gouki3);
    }

    /**
     * ピーク温度3
     *
     * @return the peakOndo3
     */
    public String getPeakOndo3() {
        return peakOndo3;
    }

    /**
     * ピーク温度3
     *
     * @param peakOndo3 the peakOndo3 to set
     */
    public void setPeakOndo3(String peakOndo3) {
        this.peakOndo3 = StringUtil.trimAll(peakOndo3);
    }

    /**
     * 水素濃度3
     *
     * @return the suisoNoudo3
     */
    public String getSuisoNoudo3() {
        return suisoNoudo3;
    }

    /**
     * 水素濃度3
     *
     * @param suisoNoudo3 the suisoNoudo3 to set
     */
    public void setSuisoNoudo3(String suisoNoudo3) {
        this.suisoNoudo3 = StringUtil.trimAll(suisoNoudo3);
    }

    /**
     * 号機4
     *
     * @return the gouki4
     */
    public String getGouki4() {
        return gouki4;
    }

    /**
     * 号機4
     *
     * @param gouki4 the gouki4 to set
     */
    public void setGouki4(String gouki4) {
        this.gouki4 = StringUtil.trimAll(gouki4);
    }

    /**
     * ピーク温度4
     *
     * @return the peakOndo4
     */
    public String getPeakOndo4() {
        return peakOndo4;
    }

    /**
     * ピーク温度4
     *
     * @param peakOndo4 the peakOndo4 to set
     */
    public void setPeakOndo4(String peakOndo4) {
        this.peakOndo4 = StringUtil.trimAll(peakOndo4);
    }

    /**
     * 水素濃度4
     *
     * @return the suisoNoudo4
     */
    public String getSuisoNoudo4() {
        return suisoNoudo4;
    }

    /**
     * 水素濃度4
     *
     * @param suisoNoudo4 the suisoNoudo4 to set
     */
    public void setSuisoNoudo4(String suisoNoudo4) {
        this.suisoNoudo4 = StringUtil.trimAll(suisoNoudo4);
    }

    /**
     * 号機5
     *
     * @return the gouki5
     */
    public String getGouki5() {
        return gouki5;
    }

    /**
     * 号機5
     *
     * @param gouki5 the gouki5 to set
     */
    public void setGouki5(String gouki5) {
        this.gouki5 = StringUtil.trimAll(gouki5);
    }

    /**
     * ピーク温度5
     *
     * @return the peakOndo5
     */
    public String getPeakOndo5() {
        return peakOndo5;
    }

    /**
     * ピーク温度5
     *
     * @param peakOndo5 the peakOndo5 to set
     */
    public void setPeakOndo5(String peakOndo5) {
        this.peakOndo5 = StringUtil.trimAll(peakOndo5);
    }

    /**
     * 水素濃度5
     *
     * @return the suisoNoudo5
     */
    public String getSuisoNoudo5() {
        return suisoNoudo5;
    }

    /**
     * 水素濃度5
     *
     * @param suisoNoudo5 the suisoNoudo5 to set
     */
    public void setSuisoNoudo5(String suisoNoudo5) {
        this.suisoNoudo5 = StringUtil.trimAll(suisoNoudo5);
    }

    /**
     * ロットNo(BackGround)
     *
     * @return the lotnoBackGround
     */
    public String getLotnoBackGround() {
        return lotnoBackGround;
    }

    /**
     * ロットNo(BackGround)
     *
     * @param lotnoBackGround the lotnoBackGround to set
     */
    public void setLotnoBackGround(String lotnoBackGround) {
        this.lotnoBackGround = lotnoBackGround;
    }

    /**
     * 指示者(BackGround)
     *
     * @return the shijisyaBackGround
     */
    public String getShijisyaBackGround() {
        return shijisyaBackGround;
    }

    /**
     * 指示者(BackGround)
     *
     * @param shijisyaBackGround the shijisyaBackGround to set
     */
    public void setShijisyaBackGround(String shijisyaBackGround) {
        this.shijisyaBackGround = shijisyaBackGround;
    }

    /**
     * 号機1(BackGround)
     *
     * @return the gouki1BackGround
     */
    public String getGouki1BackGround() {
        return gouki1BackGround;
    }

    /**
     * 号機1(BackGround)
     *
     * @param gouki1BackGround the gouki1BackGround to set
     */
    public void setGouki1BackGround(String gouki1BackGround) {
        this.gouki1BackGround = gouki1BackGround;
    }

    /**
     * ピーク温度1(BackGround)
     *
     * @return the peakOndo1BackGround
     */
    public String getPeakOndo1BackGround() {
        return peakOndo1BackGround;
    }

    /**
     * ピーク温度1(BackGround)
     *
     * @param peakOndo1BackGround the peakOndo1BackGround to set
     */
    public void setPeakOndo1BackGround(String peakOndo1BackGround) {
        this.peakOndo1BackGround = peakOndo1BackGround;
    }

    /**
     * 水素濃度1(BackGround)
     *
     * @return the suisoNoudo1BackGround
     */
    public String getSuisoNoudo1BackGround() {
        return suisoNoudo1BackGround;
    }

    /**
     * 水素濃度1(BackGround)
     *
     * @param suisoNoudo1BackGround the suisoNoudo1BackGround to set
     */
    public void setSuisoNoudo1BackGround(String suisoNoudo1BackGround) {
        this.suisoNoudo1BackGround = suisoNoudo1BackGround;
    }

    /**
     * 号機2(BackGround)
     *
     * @return the gouki2BackGround
     */
    public String getGouki2BackGround() {
        return gouki2BackGround;
    }

    /**
     * 号機2(BackGround)
     *
     * @param gouki2BackGround the gouki2BackGround to set
     */
    public void setGouki2BackGround(String gouki2BackGround) {
        this.gouki2BackGround = gouki2BackGround;
    }

    /**
     * ピーク温度2(BackGround)
     *
     * @return the peakOndo2BackGround
     */
    public String getPeakOndo2BackGround() {
        return peakOndo2BackGround;
    }

    /**
     * ピーク温度2(BackGround)
     *
     * @param peakOndo2BackGround the peakOndo2BackGround to set
     */
    public void setPeakOndo2BackGround(String peakOndo2BackGround) {
        this.peakOndo2BackGround = peakOndo2BackGround;
    }

    /**
     * 水素濃度2(BackGround)
     *
     * @return the suisoNoudo2BackGround
     */
    public String getSuisoNoudo2BackGround() {
        return suisoNoudo2BackGround;
    }

    /**
     * 水素濃度2(BackGround)
     *
     * @param suisoNoudo2BackGround the suisoNoudo2BackGround to set
     */
    public void setSuisoNoudo2BackGround(String suisoNoudo2BackGround) {
        this.suisoNoudo2BackGround = suisoNoudo2BackGround;
    }

    /**
     * 号機3(BackGround)
     *
     * @return the gouki3BackGround
     */
    public String getGouki3BackGround() {
        return gouki3BackGround;
    }

    /**
     * 号機3(BackGround)
     *
     * @param gouki3BackGround the gouki3BackGround to set
     */
    public void setGouki3BackGround(String gouki3BackGround) {
        this.gouki3BackGround = gouki3BackGround;
    }

    /**
     * ピーク温度3(BackGround)
     *
     * @return the peakOndo3BackGround
     */
    public String getPeakOndo3BackGround() {
        return peakOndo3BackGround;
    }

    /**
     * ピーク温度3(BackGround)
     *
     * @param peakOndo3BackGround the peakOndo3BackGround to set
     */
    public void setPeakOndo3BackGround(String peakOndo3BackGround) {
        this.peakOndo3BackGround = peakOndo3BackGround;
    }

    /**
     * 水素濃度3(BackGround)
     *
     * @return the suisoNoudo3BackGround
     */
    public String getSuisoNoudo3BackGround() {
        return suisoNoudo3BackGround;
    }

    /**
     * 水素濃度3(BackGround)
     *
     * @param suisoNoudo3BackGround the suisoNoudo3BackGround to set
     */
    public void setSuisoNoudo3BackGround(String suisoNoudo3BackGround) {
        this.suisoNoudo3BackGround = suisoNoudo3BackGround;
    }

    /**
     * 号機4(BackGround)
     *
     * @return the gouki4BackGround
     */
    public String getGouki4BackGround() {
        return gouki4BackGround;
    }

    /**
     * 号機4(BackGround)
     *
     * @param gouki4BackGround the gouki4BackGround to set
     */
    public void setGouki4BackGround(String gouki4BackGround) {
        this.gouki4BackGround = gouki4BackGround;
    }

    /**
     * ピーク温度4(BackGround)
     *
     * @return the peakOndo4BackGround
     */
    public String getPeakOndo4BackGround() {
        return peakOndo4BackGround;
    }

    /**
     * ピーク温度4(BackGround)
     *
     * @param peakOndo4BackGround the peakOndo4BackGround to set
     */
    public void setPeakOndo4BackGround(String peakOndo4BackGround) {
        this.peakOndo4BackGround = peakOndo4BackGround;
    }

    /**
     * 水素濃度4(BackGround)
     *
     * @return the suisoNoudo4BackGround
     */
    public String getSuisoNoudo4BackGround() {
        return suisoNoudo4BackGround;
    }

    /**
     * 水素濃度4(BackGround)
     *
     * @param suisoNoudo4BackGround the suisoNoudo4BackGround to set
     */
    public void setSuisoNoudo4BackGround(String suisoNoudo4BackGround) {
        this.suisoNoudo4BackGround = suisoNoudo4BackGround;
    }

    /**
     * 号機5(BackGround)
     *
     * @return the gouki5BackGround
     */
    public String getGouki5BackGround() {
        return gouki5BackGround;
    }

    /**
     * 号機5(BackGround)
     *
     * @param gouki5BackGround the gouki5BackGround to set
     */
    public void setGouki5BackGround(String gouki5BackGround) {
        this.gouki5BackGround = gouki5BackGround;
    }

    /**
     * ピーク温度5(BackGround)
     *
     * @return the peakOndo5BackGround
     */
    public String getPeakOndo5BackGround() {
        return peakOndo5BackGround;
    }

    /**
     * ピーク温度5(BackGround)
     *
     * @param peakOndo5BackGround the peakOndo5BackGround to set
     */
    public void setPeakOndo5BackGround(String peakOndo5BackGround) {
        this.peakOndo5BackGround = peakOndo5BackGround;
    }

    /**
     * 水素濃度5(BackGround)
     *
     * @return the suisoNoudo5BackGround
     */
    public String getSuisoNoudo5BackGround() {
        return suisoNoudo5BackGround;
    }

    /**
     * 水素濃度5(BackGround)
     *
     * @param suisoNoudo5BackGround the suisoNoudo5BackGround to set
     */
    public void setSuisoNoudo5BackGround(String suisoNoudo5BackGround) {
        this.suisoNoudo5BackGround = suisoNoudo5BackGround;
    }

    /**
     * 【条件情報】温度
     *
     * @return the jokenOndo
     */
    public String getJokenOndo() {
        return jokenOndo;
    }

    /**
     * 【条件情報】温度
     *
     * @param jokenOndo the jokenOndo to set
     */
    public void setJokenOndo(String jokenOndo) {
        this.jokenOndo = jokenOndo;
    }

    /**
     * 【条件情報】水素濃度
     *
     * @return the jokenSuisoNoudo
     */
    public String getJokenSuisoNoudo() {
        return jokenSuisoNoudo;
    }

    /**
     * 【条件情報】水素温度
     *
     * @param jokenSuisoNoudo the jokenSuisoNoudo to set
     */
    public void setJokenSuisoNoudo(String jokenSuisoNoudo) {
        this.jokenSuisoNoudo = jokenSuisoNoudo;
    }

    /**
     * 【仕掛情報】KCPNO
     *
     * @return the sikakariKcpno
     */
    public String getSikakariKcpno() {
        return sikakariKcpno;
    }

    /**
     * 【仕掛情報】KCPNO
     *
     * @param sikakariKcpno the sikakariKcpno to set
     */
    public void setSikakariKcpno(String sikakariKcpno) {
        this.sikakariKcpno = sikakariKcpno;
    }

    /**
     * 【仕掛情報】工程
     *
     * @return the sikakariKotei
     */
    public String getSikakariKotei() {
        return sikakariKotei;
    }

    /**
     * 【仕掛情報】工程
     *
     * @param sikakariKotei the sikakariKotei to set
     */
    public void setSikakariKotei(String sikakariKotei) {
        this.sikakariKotei = sikakariKotei;
    }

    /**
     * 【仕掛情報】数量
     *
     * @return the sikakariSuryo
     */
    public String getSikakariSuryo() {
        return sikakariSuryo;
    }

    /**
     * 【仕掛情報】数量
     *
     * @param sikakariSuryo the sikakariSuryo to set
     */
    public void setSikakariSuryo(String sikakariSuryo) {
        this.sikakariSuryo = sikakariSuryo;
    }

    /**
     * 入力エリア使用可否(ReadOnly)
     *
     * @return the inputAreaReadOnly
     */
    public Boolean getInputAreaReadOnly() {
        return inputAreaReadOnly;
    }

    /**
     * 入力エリア使用可否(ReadOnly)
     *
     * @param inputAreaReadOnly the inputAreaReadOnly to set
     */
    public void setInputAreaReadOnly(Boolean inputAreaReadOnly) {
        this.inputAreaReadOnly = inputAreaReadOnly;
    }

    /**
     * 入力エリア使用可否(Disabled)
     *
     * @return the inputAreaDisabled
     */
    public Boolean getInputAreaDisabled() {
        return inputAreaDisabled;
    }

    /**
     * 入力エリア使用可否(Disabled)
     *
     * @param inputAreaDisabled the inputAreaDisabled to set
     */
    public void setInputAreaDisabled(Boolean inputAreaDisabled) {
        this.inputAreaDisabled = inputAreaDisabled;
    }

    /**
     * 入力エリア文字色
     *
     * @return the inputAreaReadOnly
     */
    public String getInputAreaColor() {
        if (inputAreaReadOnly) {
            return "#c8cbcf";
        }
        return "";
    }

    /**
     * 検索エリア使用可否
     *
     * @return the searchAreaDisabled
     */
    public Boolean getSearchAreaDisabled() {
        return searchAreaDisabled;
    }

    /**
     * 検索エリア使用可否
     *
     * @param searchAreaDisabled the searchAreaDisabled to set
     */
    public void setSearchAreaDisabled(Boolean searchAreaDisabled) {
        this.searchAreaDisabled = searchAreaDisabled;
    }

    /**
     * 検索ボタン使用可否
     *
     * @return the btnSearchDisabled
     */
    public Boolean getBtnSearchDisabled() {
        return btnSearchDisabled;
    }

    /**
     * 検索ボタン使用可否
     *
     * @param btnSearchDisabled the btnSearchDisabled to set
     */
    public void setBtnSearchDisabled(Boolean btnSearchDisabled) {
        this.btnSearchDisabled = btnSearchDisabled;
    }

    /**
     * クリアボタン使用可否
     *
     * @return the btnClearDisabled
     */
    public Boolean getBtnClearDisabled() {
        return btnClearDisabled;
    }

    /**
     * クリアボタン使用可否
     *
     * @param btnClearDisabled the btnClearDisabled to set
     */
    public void setBtnClearDisabled(Boolean btnClearDisabled) {
        this.btnClearDisabled = btnClearDisabled;
    }

    /**
     * 削除ボタン使用可否
     *
     * @return the btnDeleteDisabled
     */
    public Boolean getBtnDeleteDisabled() {
        return btnDeleteDisabled;
    }

    /**
     * 削除ボタン使用可否
     *
     * @param btnDeleteDisabled the btnDeleteDisabled to set
     */
    public void setBtnDeleteDisabled(Boolean btnDeleteDisabled) {
        this.btnDeleteDisabled = btnDeleteDisabled;
    }

    /**
     * 登録ボタン使用可否
     *
     * @return the btnRegistDisabled
     */
    public Boolean getBtnRegistDisabled() {
        return btnRegistDisabled;
    }

    /**
     * 登録ボタン使用可否
     *
     * @param btnRegistDisabled the btnRegistDisabled to set
     */
    public void setBtnRegistDisabled(Boolean btnRegistDisabled) {
        this.btnRegistDisabled = btnRegistDisabled;
    }

    //</editor-fold>  
    /**
     * 画面起動時処理
     */
    public void init() {
        // セッション情報から画面パラメータを取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
                // 処理なし
            }
            return;
        }

        // 画面クリア
        formClear();
    }

    /**
     * 検索処理
     */
    public void doSearch() {

        try {
            // 処理前に各項目の背景色をクリア
            clearInputBackGround();

            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
            // 検索時の入力チェックを行う。
            if (!inputCheckSearch(queryRunnerWip)) {
                //エラー発生時はリターン
                return;
            }

            // 仕掛データ取得
            Map sikakariData = loadShikakariData(queryRunnerWip, this.lotno);
            // 仕掛データのチェック処理を行う。
            if (!checkSikakariData(sikakariData)) {
                //エラー発生時はリターン
                return;
            }
            String koteicode = StringUtil.nullToBlank(sikakariData.get("koteicode"));
            // 仕掛の工程が温度登録可能な工程かチェックする。
            String permitCode = checkPermitProcess(queryRunnerDoc, koteicode);
            // 許可工程か判定できなかった場合
            if ("9".equals(permitCode)) {
                return;
            }

            // 工程マスタデータ取得
            Map koteimasData = loadKoteimas(queryRunnerWip, koteicode);
            if (koteimasData == null || koteimasData.isEmpty()) {
                // 工程情報が取得できない場合エラー
                addErrorMessage(MessageUtil.getMessage("XHD-000097", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000001"));
            }

            // 設計情報の取得
            Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, this.lotno);
            if (sekkeiData == null || sekkeiData.isEmpty()) {
                // 設計情報が取得できない場合エラー
                addErrorMessage(MessageUtil.getMessage("XHD-000002"));
                return;
            }

            // 規格値(号機/温度)を取得
            String kikakuchiOndo;
            Map daJokenDataOndo = getDaJokenData(queryRunnerQcdb, StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")), "焼成", "設定", "ﾋﾟｰｸ温度");
            if (daJokenDataOndo == null || daJokenDataOndo.isEmpty()) {
                // データが取得できなかった場合はエラーメッセージをセット
                kikakuchiOndo = MessageUtil.getMessage("XHD-000071");
                addErrorMessage(kikakuchiOndo);
            } else {
                kikakuchiOndo = StringUtil.nullToBlank(daJokenDataOndo.get("KIKAKUCHI"));
            }

            // 規格値(水素濃度)を取得
            String kikakuchiSuisoNoudo;
            Map daJokenDataSuisoNoudo = getDaJokenData(queryRunnerQcdb, StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")), "焼成", "設定", "水素濃度");
            if (daJokenDataSuisoNoudo == null || daJokenDataSuisoNoudo.isEmpty()) {
                // データが取得できなかった場合はエラーメッセージをセット
                kikakuchiSuisoNoudo = MessageUtil.getMessage("XHD-000072");
                addErrorMessage(kikakuchiSuisoNoudo);
            } else {
                kikakuchiSuisoNoudo = StringUtil.nullToBlank(daJokenDataSuisoNoudo.get("KIKAKUCHI"));
            }

            List<FXHDD06> fxhdd06List = loadFxhdd06(queryRunnerDoc, this.lotno);
            if (!checkFxhdd06Data(fxhdd06List)) {
                return;
            }

            // 仕掛情報、条件情報エリア表示
            setJouhoAreaData(sikakariData, koteimasData, kikakuchiOndo, kikakuchiSuisoNoudo);

            // 入力エリア設定処理
            setInputAreaData(fxhdd06List);

            // 画面の使用可否制御
            setSearchedDisabled(!fxhdd06List.isEmpty(), permitCode);

            // 検索時のrevisionを設定
            if (fxhdd06List.isEmpty()) {
                this.searchedRev = getMaxRev(queryRunnerDoc, this.lotno);
            } else {
                this.searchedRev = fxhdd06List.get(0).getRev();
            }

        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            addErrorMessage("実行時エラー");

        }
    }

    /**
     * クリア処理確認
     */
    public void confirmClear() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("param1", "warning");
        this.warnMessage = "ｸﾘｱします。よろしいですか？";
        this.warnProcess = "clear";
    }

    /**
     * クリア処理
     */
    public void doClear() {
        formClear();
    }

    /**
     * 削除処理確認
     */
    public void confirmDelete() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("param1", "warning");
        this.warnMessage = "ﾃﾞｰﾀを削除します。よろしいですか？";
        this.warnProcess = "delete";
    }

    /**
     * 削除処理
     */
    public void doDelete() {

        try {
            // 処理前に各項目の背景色をクリア
            clearInputBackGround();

            // 削除処理
            if (!deleteData()) {
                return;
            }
            // 削除後コントロール制御
            controlAfterDeletion();
            
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_INFO, "削除しました。", null);
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            addErrorMessage("実行時エラー");
        }
    }

    /**
     * 登録処理確認
     */
    public void confirmRegist() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("param1", "warning");
        this.warnMessage = "登録します。よろしいですか？";
        this.warnProcess = "regist";
    }

    /**
     * 登録処理確認
     */
    public void doRegist() {
        try {
            // 処理開始前にエラーの背景色を解除
            clearInputBackGround();

            // 入力チェック
            if (!inputCheckRegist()) {
                return;
            }

            List<FXHDD06> fxhdd06List = getRegistData();
            // 重複チェック
            if (!uniqueCheckGouki(fxhdd06List)) {
                addErrorMessage(MessageUtil.getMessage("XHD-000094"));
                return;
            }

            // 登録処理
            if (!registData(fxhdd06List)) {
                return;
            }

            // クリア処理
            this.lotno = "";
            this.shijisya = "";
            formClear();
            
            // 完了メッセージを表示
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_INFO, "登録しました。", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            addErrorMessage("実行時エラー");

        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);
            addErrorMessage("実行時エラー");

        }

    }

    /**
     * 警告OK選択時処理
     */
    public void processWarnOk() {
        switch (this.warnProcess) {
            case "clear":
                doClear();
                break;
            case "delete":
                doDelete();
                break;
            case "regist":
                doRegist();
                break;
            case "research":
                formClear();
                doSearch();
                break;
        }

    }

    /**
     * 文字列を桁数でカットします。
     *
     * @param fieldName フィールド
     * @param length 桁数
     */
    public void checkByte(String fieldName, int length) {
        try {
            if (length < 1) {
                return;
            }

            // 指定フィールドの値を取得する
            Field f = this.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object value = f.get(this);

            // 切り捨て処理
            String cutValue = StringUtil.left(value.toString(), length);

            // 値をセット
            f.set(this, cutValue);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            // 処理無し
        }
    }

    /**
     * クリア処理
     */
    private void formClear() {
        // 検索エリア使用可否
        this.searchAreaDisabled = false;
        // 検索ボタン使用可否
        this.btnSearchDisabled = false;
        // 条件情報(温度)
        this.jokenOndo = "";
        // 条件情報(水素濃度)
        this.jokenSuisoNoudo = "";
        // 仕掛情報(KCPNO)
        this.sikakariKcpno = "";
        // 仕掛情報(工程)
        this.sikakariKotei = "";
        // 仕掛情報(数量)
        this.sikakariSuryo = "";
        // 入力エリア使用不可
        this.inputAreaReadOnly = true;
        // 入力エリア使用不可
        this.inputAreaDisabled = true;
        // 号機1
        this.gouki1 = "";
        // ピーク温度1
        this.peakOndo1 = "";
        // 水素濃度1
        this.suisoNoudo1 = "";
        // 号機2
        this.gouki2 = "";
        // ピーク温度2
        this.peakOndo2 = "";
        // 水素濃度2
        this.suisoNoudo2 = "";
        // 号機3
        this.gouki3 = "";
        // ピーク温度3
        this.peakOndo3 = "";
        // 水素濃度3
        this.suisoNoudo3 = "";
        // 号機4
        this.gouki4 = "";
        // ピーク温度4
        this.peakOndo4 = "";
        // 水素濃度4
        this.suisoNoudo4 = "";
        // 号機5
        this.gouki5 = "";
        // ピーク温度5
        this.peakOndo5 = "";
        // 水素濃度5
        this.suisoNoudo5 = "";
        // クリアボタン使用可否
        this.btnClearDisabled = false;
        // 削除ボタン使用可否
        this.btnDeleteDisabled = true;
        // 登録ボタン使用可否
        this.btnRegistDisabled = true;

        // 各項目の背景色をクリア
        clearInputBackGround();
    }

    /**
     * 入力チェック(検索時)
     *
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @return true:チェックOK、false:チェックNG
     * @throws SQLException 例外エラー
     */
    private boolean inputCheckSearch(QueryRunner queryRunnerWip) throws SQLException {

        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();

        // ロットNo
        if (StringUtil.isEmpty(this.lotno)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000003", "ﾛｯﾄNo"));
            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
            return false;
        } else if (existError(validateUtil.checkC101(this.lotno, "ﾛｯﾄNo", 14))) {
            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
            return false;
        } else if (existError(validateUtil.checkValueE001(this.lotno))) {
            this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
            return false;
        }

        // 指示者
        if (StringUtil.isEmpty(this.shijisya)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000003", "指示者"));
            this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
            return false;
        } else if (existError(validateUtil.checkT002("指示者", this.shijisya, queryRunnerWip))) {
            this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
            return false;
        }

        return true;
    }

    /**
     * エラーチェック： エラーが存在する場合ポップアップ用メッセージをセットする
     *
     * @param errorMessage エラーメッセージ
     * @return エラーが存在する場合true
     */
    private boolean existError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }

        addErrorMessage(errorMessage);
        return true;
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
        String sql = "SELECT kcpno, koteicode, suuryo, opencloseflag"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 工程コードマスタデータ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param koteiCode 工程コード(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadKoteimas(QueryRunner queryRunnerWip, String koteiCode) throws SQLException {
        // 工程マスタ情報データの取得
        String sql = "SELECT kotei"
                + " FROM koteimas WHERE koteicode = ? ";

        List<Object> params = new ArrayList<>();
        params.add(koteiCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * パラメータマスタデータ検索
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadFxhbm03DataList(QueryRunner queryRunnerDoc) throws SQLException {

        // パラメータデータの取得
        String sql = "SELECT key, data"
                + " FROM fxhbm03 WHERE user_name = ? AND key IN(?,?) ";
        List<Object> params = new ArrayList<>();
        params.add("syosei_user");
        params.add("xhd_syosei_ondotouroku_kyoka_kokteicode1");
        params.add("xhd_syosei_ondotouroku_kyoka_kokteicode2");

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

        return queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

    }

    /**
     * [設計]から情報を取得
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
     * 規格値取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param sekkeino 設計No
     * @param koteimei 工程名
     * @param koumokumei 項目名
     * @param kanrikoumoku 管理項目
     * @return 規格値
     * @throws SQLException 例外エラー
     */
    private Map getDaJokenData(QueryRunner queryRunnerQcdb, String sekkeino, String koteimei, String koumokumei, String kanrikoumoku) throws SQLException {
        String sql = "SELECT KIKAKUCHI "
                + "  FROM da_joken "
                + " WHERE SEKKEINO = ? AND KOUTEIMEI = ? AND KOUMOKUMEI = ? AND KANRIKOUMOKU = ? ";

        List<Object> params = new ArrayList<>();
        params.add(sekkeino);
        params.add(koteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [指示温度]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<FXHDD06> loadFxhdd06(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        String sql = "SELECT goukijyoho,shijiondo,suisonoudo,shijiondogroup,rev "
                + "FROM fxhdd06 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? "
                + "ORDER BY shijiondogroup, goukijyoho";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.add(0);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("goukijyoho", "goukijyoho"); //号機情報
        mapping.put("shijiondo", "shijiondo"); //指示温度
        mapping.put("suisonoudo", "suisonoudo"); //水素濃度
        mapping.put("shijiondogroup", "shijiondogroup"); //指示温度ｸﾞﾙｰﾌﾟ
        mapping.put("rev", "rev"); //REV

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDD06>> beanHandler = new BeanListHandler<>(FXHDD06.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, beanHandler, params.toArray());
    }

    /**
     * 指示温度から削除ﾌﾗｸﾞのMAX値を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private int getMaxDeleteFlag(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban) throws SQLException {
        int deleteflag = -1;
        // 設計データの取得
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM fxhdd06 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? ";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban));
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map map = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty() && map.get("deleteflag") != null) {
            deleteflag = Integer.parseInt(map.get("deleteflag").toString());
        }

        return deleteflag;
    }

    /**
     * 指示温度からRevのMAX値を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param lotNo ﾛｯﾄNo(フル桁)(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private int getMaxRev(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
        String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
        String edaban = lotNo.substring(11, 14); //枝番

        int rev = 0;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd06 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? ";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo8, edaban));
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map map = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty() && map.get("rev") != null) {
            rev = Integer.parseInt(map.get("rev").toString());
        }

        return rev;
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
     * 仕掛データチェック
     *
     * @param sikakariData 仕掛データ
     * @return true:チェックOK、false:チェックNG
     */
    private boolean checkSikakariData(Map sikakariData) {

        if (sikakariData == null) {
            // 仕掛データが取得できない場合はエラー
            addErrorMessage(MessageUtil.getMessage("XHD-000029"));
            return false;
        } else if ("0".equals(StringUtil.nullToBlank(getMapData(sikakariData, "opencloseflag")))) {
            // オープンクローズフラグが"0"クローズの場合エラー
            addErrorMessage(MessageUtil.getMessage("XHD-000082"));
            return false;
        }

        return true;
    }

    /**
     * 温度登録許可工程チェック
     *
     * @param queryRunnerオブジェクト
     * @param 工程コード
     * @return 0:許可工程以外、1：許可工程、9：エラー
     */
    private String checkPermitProcess(QueryRunner queryRunnerDoc, String koteicode) throws SQLException {

        // パラメータマスタデータ取得
        List<Map<String, Object>> fxhbm03DataList = loadFxhbm03DataList(queryRunnerDoc);
        // 該当データが存在しない場合
        if (fxhbm03DataList.isEmpty()) {
            addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000001"));
            return "9";
        }

        // パラメータマスタより取得したパラメータデータをカンマ区切りで分割し保持する。
        List<String> permitProcessList = new ArrayList<>();
        for (Map<String, Object> fxhdm03Data : fxhbm03DataList) {
            String data = StringUtil.nullToBlank(getMapData(fxhdm03Data, "data"));

            // データに"ALL"が入っていた場合、許可工程
            if ("ALL".equals(data)) {
                return "1";
            }
            // データをカンマ区切りに分解
            String[] spData = data.split(",");
            for (String pKotei : spData) {
                permitProcessList.add(StringUtil.trimAll(pKotei));
            }
        }

        // 分割したパラメータデータに対象の工程コードが存在しない場合
        if (!permitProcessList.contains(koteicode)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000084"));
            return "0";
        }

        return "1";
    }

    /**
     * 指示温度データチェック
     *
     * @param fxhdd06List 指示温度データリスト
     * @return true:チェックOK、false:チェックNG
     */
    private boolean checkFxhdd06Data(List<FXHDD06> fxhdd06List) {

        int group = 0;
        int sijiOndo = 0;
        BigDecimal suisoNoudo = null;
        for (FXHDD06 fxhdd06 : fxhdd06List) {
            if (group == 0 || group != fxhdd06.getShijiondogroup().intValue()) {
                group = fxhdd06.getShijiondogroup().intValue();
                sijiOndo = fxhdd06.getShijiondo().intValue();
                suisoNoudo = fxhdd06.getSuisonoudo();
                continue;
            }

            if (fxhdd06.getShijiondo().intValue() != sijiOndo) {
                addErrorMessage(MessageUtil.getMessage("XHD-000079"));
                return false;
            }

            if (fxhdd06.getSuisonoudo().compareTo(suisoNoudo) != 0) {
                addErrorMessage(MessageUtil.getMessage("XHD-000080"));
                return false;
            }
        }

        return true;
    }

    /**
     * 指示温度データチェック
     *
     * @param fxhdd06List 指示温度データリスト
     */
    private void setInputAreaData(List<FXHDD06> fxhdd06List) {

        // 1行目データセット
        String[] groupData1 = getGroupData(fxhdd06List, 1);
        this.gouki1 = groupData1[0];
        this.peakOndo1 = groupData1[1];
        this.suisoNoudo1 = groupData1[2];

        // 2行目データセット
        String[] groupData2 = getGroupData(fxhdd06List, 2);
        this.gouki2 = groupData2[0];
        this.peakOndo2 = groupData2[1];
        this.suisoNoudo2 = groupData2[2];

        // 3行目データセット
        String[] groupData3 = getGroupData(fxhdd06List, 3);
        this.gouki3 = groupData3[0];
        this.peakOndo3 = groupData3[1];
        this.suisoNoudo3 = groupData3[2];

        // 4行目データセット
        String[] groupData4 = getGroupData(fxhdd06List, 4);
        this.gouki4 = groupData4[0];
        this.peakOndo4 = groupData4[1];
        this.suisoNoudo4 = groupData4[2];

        // 5行目データセット
        String[] groupData5 = getGroupData(fxhdd06List, 5);
        this.gouki5 = groupData5[0];
        this.peakOndo5 = groupData5[1];
        this.suisoNoudo5 = groupData5[2];

    }

    /**
     * 指示温度データグループ別データ取得 ※グループ別にまとめて1レコードとしてデータを取得、号機情報については
     * 値がそれぞれ異なるため、結合して値を返す
     *
     * @param fxhdd06List 指示温度データリスト
     */
    private String[] getGroupData(List<FXHDD06> fxhdd06List, Integer getgroup) {
        String shijiOndo = "";
        String suisoNoudo = "";
        StringBuilder goukiJouhoGroup = new StringBuilder();
        int goukiJouho;
        int sabun;
        int prevGoukiJouho = 0;
        int renbanCount = 0;

        for (FXHDD06 fxhdd06 : fxhdd06List) {
            // 一致しないグループについてはコンティニュー
            if (!getgroup.equals(fxhdd06.getShijiondogroup())) {
                continue;
            }

            // 号機情報を数値化
            try {
                goukiJouho = Integer.parseInt(StringUtil.nullToBlank(fxhdd06.getGoukijyoho()));
            } catch (NumberFormatException e) {
                // ※エラーになることはありえないはずだが念の為、例外対応
                continue;
            }

            // 初回データセット
            if (0 == goukiJouhoGroup.length()) {
                //指示温度
                shijiOndo = StringUtil.nullToBlank(fxhdd06.getShijiondo());
                //水素濃度
                suisoNoudo = StringUtil.nullToBlank(fxhdd06.getSuisonoudo());
                // 号機情報
                goukiJouhoGroup.append(goukiJouho);
                prevGoukiJouho = goukiJouho;
                continue;
            }

            // 号機情報の前回値の差分を比較
            sabun = goukiJouho - prevGoukiJouho;

            // 差分が1の場合
            if (sabun == 1) {
                // 連番が続いている場合
                renbanCount++;
            } else if (1 < sabun) {
                if (1 < renbanCount) {
                    // 3つ以上の連番が途切れた場合
                    //前回までの連番分を追加
                    goukiJouhoGroup.append("～");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                } else if (1 == renbanCount) {
                    // 1回の連番が途切れた場合
                    goukiJouhoGroup.append(",");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                }

                //今回の号機情報を追加
                goukiJouhoGroup.append(",");
                goukiJouhoGroup.append(StringUtil.nullToBlank(goukiJouho));
            }
            //今回の号機情報を前回値にセット
            prevGoukiJouho = goukiJouho;
        }

        if (1 < renbanCount) {
            // 3つ以上の連番が途切れた場合
            //前回までの連番分を追加
            goukiJouhoGroup.append("～");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        } else if (1 == renbanCount) {
            // 1回の連番が途切れた場合
            goukiJouhoGroup.append(",");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        }

        return new String[]{goukiJouhoGroup.toString(), shijiOndo, suisoNoudo};
    }

    /**
     * 情報エリア値表示処理
     *
     * @param sikakariData 仕掛データ
     * @param kikakuchiOndo 規格値(温度)
     * @param kikakuchiSuisoNoudo 規格値(水素濃度)
     */
    private void setJouhoAreaData(Map sikakariData, Map koteimasData, String kikakuchiOndo, String kikakuchiSuisoNoudo) {
        this.sikakariKcpno = StringUtil.nullToBlank(sikakariData.get("kcpno"));

        String kotei = StringUtil.nullToBlank(getMapData(koteimasData, "kotei"));

        this.sikakariKotei = StringUtil.nullToBlank(sikakariData.get("koteicode") + "(" + kotei + ")");

        if (StringUtil.isEmpty(StringUtil.nullToBlank(sikakariData.get("suuryo")))) {
            this.sikakariSuryo = "";
        } else {
            int suryo = Integer.parseInt(StringUtil.nullToBlank(sikakariData.get("suuryo")));
            NumberFormat nfNum = NumberFormat.getNumberInstance();
            this.sikakariSuryo = nfNum.format(suryo);
        }
        this.jokenOndo = kikakuchiOndo;
        this.jokenSuisoNoudo = kikakuchiSuisoNoudo;
    }

    /**
     * 検索後使用可否制御
     *
     * @param existShijiData データ有無
     * @param permitCode 許可ｺｰﾄﾞ"1：登録許可"
     */
    private void setSearchedDisabled(boolean existShijiData, String permitCode) {
        // 検索エリア使用可否
        this.searchAreaDisabled = true;
        // 検索ボタン使用可否
        this.btnSearchDisabled = true;
        // クリアボタン使用可否
        this.btnClearDisabled = false;
        // 入力エリア使用不可
        this.inputAreaDisabled = false;

        if ("1".equals(permitCode)) {
            // 入力エリア使用不可
            this.inputAreaReadOnly = false;

            // 削除ボタン使用可否
            if (existShijiData) {
                this.btnDeleteDisabled = false;
            } else {
                this.btnDeleteDisabled = true;
            }
            // 登録ボタン使用可否
            this.btnRegistDisabled = false;
        } else {
            // 入力エリア使用不可
            this.inputAreaReadOnly = true;
            // 削除ボタン入力不可
            this.btnDeleteDisabled = true;
            // 登録ボタン使用可否
            this.btnRegistDisabled = true;
        }
    }

    /**
     * 削除後制御
     *
     */
    private void controlAfterDeletion() {
        // 入力項目をクリア
        // 号機1
        this.gouki1 = "";
        // ピーク温度1
        this.peakOndo1 = "";
        // 水素濃度1
        this.suisoNoudo1 = "";
        // 号機2
        this.gouki2 = "";
        // ピーク温度2
        this.peakOndo2 = "";
        // 水素濃度2
        this.suisoNoudo2 = "";
        // 号機3
        this.gouki3 = "";
        // ピーク温度3
        this.peakOndo3 = "";
        // 水素濃度3
        this.suisoNoudo3 = "";
        // 号機4
        this.gouki4 = "";
        // ピーク温度4
        this.peakOndo4 = "";
        // 水素濃度4
        this.suisoNoudo4 = "";
        // 号機5
        this.gouki5 = "";
        // ピーク温度5
        this.peakOndo5 = "";
        // 水素濃度5
        this.suisoNoudo5 = "";
        // 登録ボタン使用可否
        this.btnRegistDisabled = false;
        // 削除ボタン使用可否
        this.btnDeleteDisabled = true;

    }

    /**
     * 入力チェック(登録時)
     *
     * @return true:チェックOK、false:チェックNG
     * @throws SQLException 例外エラー
     */
    private boolean inputCheckRegist() {

        // 入力項目が全項目未入力の場合エラー
        if (!hasEntry()) {
            addErrorMessage(MessageUtil.getMessage("XHD-000093"));
            return false;
        }

        // グループ1入力チェック
        if (!checkGroupEntry(this.gouki1, this.peakOndo1, this.suisoNoudo1)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
            setErrorBackGround(new String[]{"gouki1", "peakOndo1", "suisoNoudo1"});
            return false;
        }

        // グループ2入力チェック
        if (!checkGroupEntry(this.gouki2, this.peakOndo2, this.suisoNoudo2)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
            setErrorBackGround(new String[]{"gouki2", "peakOndo2", "suisoNoudo2"});
            return false;
        }

        // グループ3入力チェック
        if (!checkGroupEntry(this.gouki3, this.peakOndo3, this.suisoNoudo3)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
            setErrorBackGround(new String[]{"gouki3", "peakOndo3", "suisoNoudo3"});
            return false;
        }

        // グループ4入力チェック
        if (!checkGroupEntry(this.gouki4, this.peakOndo4, this.suisoNoudo4)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
            setErrorBackGround(new String[]{"gouki4", "peakOndo4", "suisoNoudo4"});
            return false;
        }

        // グループ5入力チェック
        if (!checkGroupEntry(this.gouki5, this.peakOndo5, this.suisoNoudo5)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000073"));
            setErrorBackGround(new String[]{"gouki5", "peakOndo5", "suisoNoudo5"});
            return false;
        }

        // グループ1入力値チェック
        if (!checkGroupValue(this.gouki1, this.peakOndo1, this.suisoNoudo1, "1", "①")) {
            //エラー時はリターン(エラーは関数内部でセット)
            return false;
        }

        // グループ2入力値チェック
        if (!checkGroupValue(this.gouki2, this.peakOndo2, this.suisoNoudo2, "2", "②")) {
            //エラー時はリターン(エラーは関数内部でセット)
            return false;
        }

        // グループ3入力値チェック
        if (!checkGroupValue(this.gouki3, this.peakOndo3, this.suisoNoudo3, "3", "③")) {
            //エラー時はリターン(エラーは関数内部でセット)
            return false;
        }

        // グループ4入力値チェック
        if (!checkGroupValue(this.gouki4, this.peakOndo4, this.suisoNoudo4, "4", "④")) {
            //エラー時はリターン(エラーは関数内部でセット)
            return false;
        }

        // グループ5入力値チェック
        if (!checkGroupValue(this.gouki5, this.peakOndo5, this.suisoNoudo5, "5", "⑤")) {
            //エラー時はリターン(エラーは関数内部でセット)
            return false;
        }

        return true;
    }

    /**
     * 入力項目の入力チェック
     *
     * @return true:チェックOK、false:チェック:NG
     */
    private boolean hasEntry() {

        if (!StringUtil.isEmpty(this.gouki1) || !StringUtil.isEmpty(this.gouki2)
                || !StringUtil.isEmpty(this.gouki3) || !StringUtil.isEmpty(this.gouki4)
                || !StringUtil.isEmpty(this.gouki5)
                || !StringUtil.isEmpty(this.peakOndo1) || !StringUtil.isEmpty(this.peakOndo2)
                || !StringUtil.isEmpty(this.peakOndo3) || !StringUtil.isEmpty(this.peakOndo4)
                || !StringUtil.isEmpty(this.peakOndo5)
                || !StringUtil.isEmpty(this.suisoNoudo1) || !StringUtil.isEmpty(this.suisoNoudo2)
                || !StringUtil.isEmpty(this.suisoNoudo3) || !StringUtil.isEmpty(this.suisoNoudo4)
                || !StringUtil.isEmpty(this.suisoNoudo5)) {
            return true;
        }

        return false;
    }

    /**
     * グループ単位で全て入力されているかチェック
     *
     * @return true:チェックOK、false:チェック:NG
     */
    private boolean checkGroupEntry(String gouki, String peakOndo, String suisoNoudo) {

        // いずれも入力されていない場合はリターン
        if (StringUtil.isEmpty(gouki) && StringUtil.isEmpty(peakOndo) && StringUtil.isEmpty(suisoNoudo)) {
            return true;
        }

        //一つでも空があればエラー
        if (StringUtil.isEmpty(gouki) || StringUtil.isEmpty(peakOndo) || StringUtil.isEmpty(suisoNoudo)) {
            return false;
        }

        return true;
    }

    /**
     * //エラーメッセージ追加処理
     *
     * @param errorMessage エラーメッセージ
     */
    private void addErrorMessage(String errorMessage) {
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * 号機文字チェック
     *
     * @param gouki 号機
     * @return 号機形式チェック
     */
    private String checkGouki(String gouki) {

        // 号機未入力時はチェックしない
        if (StringUtil.isEmpty(gouki)) {
            return "";
        }
        try {

            // カンマで分割
            String spGouki[] = gouki.split(",");
            for (String goukiVal : spGouki) {

                if (goukiVal.contains("～")) {
                    // 分割が2分割になってなければエラー
                    String chkValues[] = goukiVal.split("～");
                    if (chkValues.length != 2) {
                        // 形式エラー
                        return "XHD-000090";
                    }

                    // 前方後方の値を数値化
                    int val1 = Integer.parseInt(chkValues[0]);
                    int val2 = Integer.parseInt(chkValues[1]);

                    if (!checkRange(val1, 1, 99) || !checkRange(val2, 1, 99)) {
                        // 数値範囲チェック
                        return "XHD-000090";
                    } else if ((val2 - val1) < 2) {
                        // 範囲指定エラー
                        return "XHD-000095";
                    }

                } else {
                    if (!checkRange(Integer.parseInt(goukiVal), 1, 99)) {
                        // 数値範囲エラー
                        return "XHD-000090";
                    }
                }

            }
        } catch (NumberFormatException e) {
            // 数値変換する変換できなかった場合形式エラー
            return "XHD-000090";
        }

        return "";
    }

    /**
     * グループ単位で入力されている値をチェック
     *
     * @param gouki 号機
     * @param peakOndo ピーク温度
     * @param suisoNoudo 水素濃度
     * @param rowNoLable 行No
     * @return true:
     */
    private boolean checkGroupValue(String gouki, String peakOndo, String suisoNoudo, String rowNo, String rowNoLable) {
        String errorCode;
        // 号機チェック
        errorCode = checkGouki(gouki);
        if (!StringUtil.isEmpty(errorCode)) {
            addErrorMessage(MessageUtil.getMessage(errorCode));
            setErrorBackGround(new String[]{"gouki" + rowNo});
            return false;
        }

        // ピーク温度整数チェック
        if (!checkIntegral(peakOndo)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000091", "ﾋﾟｰｸ温度" + rowNoLable));
            setErrorBackGround(new String[]{"peakOndo" + rowNo});
            return false;
        }

        // 水素濃度整数チェック
        if (!checkIntegral(suisoNoudo)) {
            addErrorMessage(MessageUtil.getMessage("XHD-000091", "水素濃度" + rowNoLable));
            setErrorBackGround(new String[]{"suisoNoudo" + rowNo});
            return false;
        }

        return true;
    }

    /**
     * 指定の項目の背景色を変更
     *
     * @param itemList 項目リスト
     */
    private void setErrorBackGround(String[] itemList) {
        for (String item : itemList) {
            switch (item) {
                case "lotno":
                    this.lotnoBackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "shijisya":
                    this.shijisyaBackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "gouki1":
                    this.gouki1BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "peakOndo1":
                    this.peakOndo1BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "suisoNoudo1":
                    this.suisoNoudo1BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "gouki2":
                    this.gouki2BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "peakOndo2":
                    this.peakOndo2BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "suisoNoudo2":
                    this.suisoNoudo2BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "gouki3":
                    this.gouki3BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "peakOndo3":
                    this.peakOndo3BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "suisoNoudo3":
                    this.suisoNoudo3BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "gouki4":
                    this.gouki4BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "peakOndo4":
                    this.peakOndo4BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "suisoNoudo4":
                    this.suisoNoudo4BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "gouki5":
                    this.gouki5BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "peakOndo5":
                    this.peakOndo5BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
                case "suisoNoudo5":
                    this.suisoNoudo5BackGround = ErrUtil.ERR_BACK_COLOR;
                    break;
            }

        }

    }

    /**
     * 整数チェック
     *
     * @param value 値
     * @return ture:整数(1以上)、false:それ以外
     */
    private boolean checkIntegral(String value) {
        //値が空の場合は
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        BigDecimal decValue;
        try {
            decValue = new BigDecimal(value);
        } catch (NumberFormatException e) {
            return false;
        }

        // 値が0以下の場合エラー
        if (0 <= BigDecimal.ZERO.compareTo(decValue)) {
            return false;
        }

        return true;
    }

    /**
     * 値が指定範囲内か確認する。
     *
     * @param value 値
     * @param minValue 最小値
     * @param maxValue 最大値
     * @return true：範囲内、false：範囲外
     */
    private boolean checkRange(int value, int minValue, int maxValue) {

        if (value < minValue || maxValue < value) {
            return false;
        }

        return true;
    }

    /**
     * 削除処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private boolean deleteData() throws SQLException {

        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
        Connection conDoc = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            String kojyo = this.lotno.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = this.lotno.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = this.lotno.substring(11, 14); //枝番

            String checkId = checkRev(queryRunnerDoc);
            // 正常値以外の場合
            if (!"0".equals(checkId)) {
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);

                // 警告エラーの場合
                if ("1".equals(checkId)) {
                    // 警告メッセージをセットしてリターン
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("param1", "warning");
                    this.warnMessage = "ﾃﾞｰﾀが更新されています。画面を更新してもよろしいですか？";
                    this.warnProcess = "research";
                } else {
                    // システムエラーをセットしてリターン
                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000002"));
                }
                return false;
            }
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            // 削除フラグの最大値を取得
            int deleteflag = getMaxDeleteFlag(queryRunnerDoc, kojyo, lotNo8, edaban);
            // 削除フラグが取得できなかった場合は想定外としてシステムエラー
            if (-1 == deleteflag) {
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);

                // システムエラーをセットしてリターン
                addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000003"));
                return false;
            }

            // データ論理削除
            logicalDeleteFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev, deleteflag + 1, systemTime);

            DbUtils.commitAndCloseQuietly(conDoc);
            
            // リビジョンを更新
            this.searchedRev += 1;

        } catch (SQLException e) {

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            throw e;
        }
        return true;

    }

    /**
     * 登録処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private boolean registData(List<FXHDD06> fxhdd06List) throws SQLException {

        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
        Connection conDoc = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            String kojyo = this.lotno.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = this.lotno.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = this.lotno.substring(11, 14); //枝番

            String checkId = checkRev(queryRunnerDoc);
            // 正常値以外の場合
            if (!"0".equals(checkId)) {
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);

                // 警告エラーの場合
                if ("1".equals(checkId)) {
                    // 警告メッセージをセットしてリターン
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("param1", "warning");
                    this.warnMessage = "ﾃﾞｰﾀが更新されています。画面を更新してもよろしいですか？";
                    this.warnProcess = "research";
                } else {
                    // システムエラーをセットしてリターン
                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000002"));
                }
                return false;
            }

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            // 既存データが存在する場合は論理削除
            if (!this.btnDeleteDisabled) {

                // 削除フラグの最大値を取得
                int deleteflag = getMaxDeleteFlag(queryRunnerDoc, kojyo, lotNo8, edaban);
                // 削除フラグが取得できなかった場合は想定外としてシステムエラー
                if (-1 == deleteflag) {
                    // コネクションロールバック処理
                    DBUtil.rollbackConnection(conDoc, LOGGER);

                    // システムエラーをセットしてリターン
                    addErrorMessage(MessageUtil.getMessage("XHD-000083", "ｴﾗｰ内容ｺｰﾄﾞ:EXHD-000003"));
                    return false;
                }

                // データ論理削除
                logicalDeleteFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev, deleteflag + 1, systemTime);
                
            }

            // データ登録処理
            insertFxhdd06(queryRunnerDoc, conDoc, this.shijisya, kojyo, lotNo8, edaban, this.searchedRev + 1, systemTime, fxhdd06List);

            DbUtils.commitAndCloseQuietly(conDoc);

            // リビジョンを更新
            this.searchedRev += 1;

        } catch (SQLException e) {

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            throw e;
        }
        return true;

    }

    /**
     * 登録データ取得
     *
     * @return
     */
    private List<FXHDD06> getRegistData() {

        List<FXHDD06> listAllGroup = new ArrayList<>();
        listAllGroup.addAll(getGroupBetsuList(this.gouki1, this.peakOndo1, this.suisoNoudo1, "1"));
        listAllGroup.addAll(getGroupBetsuList(this.gouki2, this.peakOndo2, this.suisoNoudo2, "2"));
        listAllGroup.addAll(getGroupBetsuList(this.gouki3, this.peakOndo3, this.suisoNoudo3, "3"));
        listAllGroup.addAll(getGroupBetsuList(this.gouki4, this.peakOndo4, this.suisoNoudo4, "4"));
        listAllGroup.addAll(getGroupBetsuList(this.gouki5, this.peakOndo5, this.suisoNoudo5, "5"));
        return listAllGroup;
    }

    /**
     * グループ別の集約データを取得(号機"1","2","3","5","6","8","10","12"のデータを"1～3,5,6,8,10,12"のような形に集約
     *
     * @param gouki 号機
     * @param peakOndo ピーク温度
     * @param suisoNoudo 水素濃度
     * @param group グループ
     * @return 集約レコード
     * @throws NumberFormatException 例外エラー
     */
    private List<FXHDD06> getGroupBetsuList(String gouki, String peakOndo, String suisoNoudo, String group) throws NumberFormatException {

        List<FXHDD06> listGroup = new ArrayList<>();
        // 入力が無ければ空のリストを返す。
        if (StringUtil.isEmpty(gouki)) {
            return listGroup;
        }

        String[] spGouki = gouki.split(",");
        for (String goukiVal : spGouki) {

            if (goukiVal.contains("～")) {
                String chkValues[] = goukiVal.split("～");
                // 前方後方の値を数値化
                int startVal = Integer.parseInt(chkValues[0]);
                int endVal = Integer.parseInt(chkValues[1]);
                for (int i = startVal; i <= endVal; i++) {
                    // リストにデータを追加
                    listGroup.add(createFXHDD06(String.valueOf(i), peakOndo, suisoNoudo, group));
                }

            } else {
                // リストにデータを追加
                listGroup.add(createFXHDD06(goukiVal, peakOndo, suisoNoudo, group));
            }
        }

        return listGroup;

    }

    /**
     * 指定温度データを作成
     *
     * @param gouki 号機
     * @param peakOndo ﾋﾟｰｸ温度
     * @param suisoNoudo 水素濃度
     * @param group グループ
     * @return 指定温度データ
     */
    private FXHDD06 createFXHDD06(String gouki, String peakOndo, String suisoNoudo, String group) throws NumberFormatException {
        FXHDD06 fxhdd06 = new FXHDD06();
        fxhdd06.setGoukijyoho(Integer.parseInt(gouki));
        fxhdd06.setShijiondo(Integer.parseInt(peakOndo));
        fxhdd06.setSuisonoudo(new BigDecimal(suisoNoudo));
        fxhdd06.setShijiondogroup(Integer.parseInt(group));
        return fxhdd06;
    }

    /**
     * 号機の重複チェック
     *
     * @param fxhdd06List 指示温度データ
     * @return true:チェックOK、false:チェックNG
     */
    private boolean uniqueCheckGouki(List<FXHDD06> fxhdd06List) {
        List<Integer> goukiList = new ArrayList<>();

        for (FXHDD06 fxhdd06 : fxhdd06List) {
            if (goukiList.contains(fxhdd06.getGoukijyoho())) {
                return false;
            }
            goukiList.add(fxhdd06.getGoukijyoho());
        }

        return true;
    }

    /**
     * リビジョンチェック
     *
     * @return　"0":正常、"1":警告エラー、"9":システムエラー
     * @throws SQLException
     */
    private String checkRev(QueryRunner queryRunnerDoc) throws SQLException {

        int maxRev = getMaxRev(queryRunnerDoc, this.lotno);
        if (maxRev == 0) {
            if (0 < this.searchedRev) {
                // 想定外システムエラー
                return "9";
            }
        } else if (this.searchedRev != maxRev) {
            // 警告エラー
            return "1";
        }

        // 正常
        return "0";
    }

    /**
     * 指示温度(fxhdd06)論理削除処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param shijisya 指示者
     * @param kojyo 工場
     * @param lotNo ロットNo
     * @param edaban 枝番
     * @param rev rev
     * @param deleteflag 削除フラグ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     *
     */
    private void logicalDeleteFxhdd06(QueryRunner queryRunnerDoc, Connection conDoc, String shijisya,
            String kojyo, String lotNo, String edaban, int rev, int deleteflag, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd06 SET "
                + "koshin_tantousyacode = ?, "
                + "koshin_date = ?,"
                + "rev = ?, "
                + "deleteflag = ? "
                + "WHERE kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND rev = ? AND deleteflag = 0 ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(shijisya); //更新担当者ｺｰﾄﾞ
        params.add(systemTime); //更新日
        params.add(rev + 1); //rev
        params.add(deleteflag); //削除フラグ

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(rev); //rev

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
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
    private void insertFxhdd06(QueryRunner queryRunnerDoc, Connection conDoc, String shijisya,
            String kojyo, String lotNo, String edaban, int rev, Timestamp systemTime, List<FXHDD06> fxhdd06List) throws SQLException {
        String sql = "INSERT INTO fxhdd06 ("
                + "kojyo,lotno,edaban,tantousyacode,goukijyoho,shijiondo,suisonoudo,shijiondogroup,"
                + "koshin_tantousyacode,toroku_date,koshin_date,rev,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        for (FXHDD06 fxhdd06 : fxhdd06List) {
            List<Object> params = new ArrayList<>();
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(shijisya); //担当者ｺｰﾄﾞ
            params.add(fxhdd06.getGoukijyoho()); //号機情報
            params.add(fxhdd06.getShijiondo()); //指示温度
            params.add(fxhdd06.getSuisonoudo()); //水素濃度
            params.add(fxhdd06.getShijiondogroup()); //指示温度ｸﾞﾙｰﾌﾟ
            params.add(null); //更新担当者ｺｰﾄﾞ
            params.add(systemTime); //登録日
            params.add(null); //更新日
            params.add(rev); //rev
            params.add(0); //削除ﾌﾗｸﾞ

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(conDoc, sql, params.toArray());
        }

    }

    /**
     * 入力項目のバックグランド値をクリア
     */
    private void clearInputBackGround() {
        this.lotnoBackGround = "";
        this.shijisyaBackGround = "";
        this.gouki1BackGround = "";
        this.peakOndo1BackGround = "";
        this.suisoNoudo1BackGround = "";
        this.gouki2BackGround = "";
        this.peakOndo2BackGround = "";
        this.suisoNoudo2BackGround = "";
        this.gouki3BackGround = "";
        this.peakOndo3BackGround = "";
        this.suisoNoudo3BackGround = "";
        this.gouki4BackGround = "";
        this.peakOndo4BackGround = "";
        this.suisoNoudo4BackGround = "";
        this.gouki5BackGround = "";
        this.peakOndo5BackGround = "";
        this.suisoNoudo5BackGround = "";
    }

}
