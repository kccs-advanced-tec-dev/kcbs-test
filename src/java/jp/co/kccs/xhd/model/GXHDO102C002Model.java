/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C002Model(ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面用)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/09/22
 */
public class GXHDO102C002Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO102C002Model clone() throws CloneNotSupportedException {
        return (GXHDO102C002Model) super.clone();
    }
    
    /**
     * 表示されるサブ画面名
     */
    private C002SubGamenData showsubgamendata;
    
    /**
     * ﾎﾟｯﾄ①タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto1subgamen1;
    
    /**
     * ﾎﾟｯﾄ①タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto1subgamen2;
    
    /**
     * ﾎﾟｯﾄ①タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto1subgamen3;
    
    /**
     * ﾎﾟｯﾄ①タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto1subgamen4;
    
    /**
     * ﾎﾟｯﾄ②タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto2subgamen1;
    
    /**
     * ﾎﾟｯﾄ②タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto2subgamen2;
    
    /**
     * ﾎﾟｯﾄ②タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto2subgamen3;
    
    /**
     * ﾎﾟｯﾄ②タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto2subgamen4;
    
    /**
     * ﾎﾟｯﾄ③タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto3subgamen1;
    
    /**
     * ﾎﾟｯﾄ③タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto3subgamen2;
    
    /**
     * ﾎﾟｯﾄ③タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto3subgamen3;
    
    /**
     * ﾎﾟｯﾄ③タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto3subgamen4;
    
    /**
     * ﾎﾟｯﾄ④タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto4subgamen1;
    
    /**
     * ﾎﾟｯﾄ④タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto4subgamen2;
    
    /**
     * ﾎﾟｯﾄ④タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto4subgamen3;
    
    /**
     * ﾎﾟｯﾄ④タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto4subgamen4;
    
    /**
     * ﾎﾟｯﾄ⑤タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto5subgamen1;
    
    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto5subgamen2;
    
    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto5subgamen3;
    
    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto5subgamen4;
    
    /**
     * ﾎﾟｯﾄ⑥タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto6subgamen1;
    
    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto6subgamen2;
    
    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto6subgamen3;
    
    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto6subgamen4;
    
    /**
     * ﾎﾟｯﾄ⑦タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto7subgamen1;
    
    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto7subgamen2;
    
    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto7subgamen3;
    
    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto7subgamen4;
    
    /**
     * ﾎﾟｯﾄ⑧タブの表示されるサブ画面用データ
     */
    private C002SubGamenData potto8subgamen1;
    
    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto8subgamen2;
    
    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto8subgamen3;
    
    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     */
    private C002SubGamenData potto8subgamen4;
    
    /**
     * コンストラクタ
     */
    public GXHDO102C002Model() {

    }

    /**
     * 表示されるサブ画面名
     * @return the showsubgamendata
     */
    public C002SubGamenData getShowsubgamendata() {
        return showsubgamendata;
    }

    /**
     * 表示されるサブ画面名
     * @param showsubgamendata the showsubgamendata to set
     */
    public void setShowsubgamendata(C002SubGamenData showsubgamendata) {
        this.showsubgamendata = showsubgamendata;
    }

    /**
     * ﾎﾟｯﾄ①タブの表示されるサブ画面用データ
     * @return the potto1subgamen1
     */
    public C002SubGamenData getPotto1subgamen1() {
        return potto1subgamen1;
    }

    /**
     * ﾎﾟｯﾄ①タブの表示されるサブ画面用データ
     * @param potto1subgamen1 the potto1subgamen1 to set
     */
    public void setPotto1subgamen1(C002SubGamenData potto1subgamen1) {
        this.potto1subgamen1 = potto1subgamen1;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto1subgamen2
     */
    public C002SubGamenData getPotto1subgamen2() {
        return potto1subgamen2;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto1subgamen2 the potto1subgamen2 to set
     */
    public void setPotto1subgamen2(C002SubGamenData potto1subgamen2) {
        this.potto1subgamen2 = potto1subgamen2;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto1subgamen3
     */
    public C002SubGamenData getPotto1subgamen3() {
        return potto1subgamen3;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto1subgamen3 the potto1subgamen3 to set
     */
    public void setPotto1subgamen3(C002SubGamenData potto1subgamen3) {
        this.potto1subgamen3 = potto1subgamen3;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto1subgamen4
     */
    public C002SubGamenData getPotto1subgamen4() {
        return potto1subgamen4;
    }

    /**
     * ﾎﾟｯﾄ①タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto1subgamen4 the potto1subgamen4 to set
     */
    public void setPotto1subgamen4(C002SubGamenData potto1subgamen4) {
        this.potto1subgamen4 = potto1subgamen4;
    }

    /**
     * ﾎﾟｯﾄ②タブの表示されるサブ画面用データ
     * @return the potto2subgamen1
     */
    public C002SubGamenData getPotto2subgamen1() {
        return potto2subgamen1;
    }

    /**
     * ﾎﾟｯﾄ②タブの表示されるサブ画面用データ
     * @param potto2subgamen1 the potto2subgamen1 to set
     */
    public void setPotto2subgamen1(C002SubGamenData potto2subgamen1) {
        this.potto2subgamen1 = potto2subgamen1;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto2subgamen2
     */
    public C002SubGamenData getPotto2subgamen2() {
        return potto2subgamen2;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto2subgamen2 the potto2subgamen2 to set
     */
    public void setPotto2subgamen2(C002SubGamenData potto2subgamen2) {
        this.potto2subgamen2 = potto2subgamen2;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto2subgamen3
     */
    public C002SubGamenData getPotto2subgamen3() {
        return potto2subgamen3;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto2subgamen3 the potto2subgamen3 to set
     */
    public void setPotto2subgamen3(C002SubGamenData potto2subgamen3) {
        this.potto2subgamen3 = potto2subgamen3;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto2subgamen4
     */
    public C002SubGamenData getPotto2subgamen4() {
        return potto2subgamen4;
    }

    /**
     * ﾎﾟｯﾄ②タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto2subgamen4 the potto2subgamen4 to set
     */
    public void setPotto2subgamen4(C002SubGamenData potto2subgamen4) {
        this.potto2subgamen4 = potto2subgamen4;
    }

    /**
     * ﾎﾟｯﾄ③タブの表示されるサブ画面用データ
     * @return the potto3subgamen1
     */
    public C002SubGamenData getPotto3subgamen1() {
        return potto3subgamen1;
    }

    /**
     * ﾎﾟｯﾄ③タブの表示されるサブ画面用データ
     * @param potto3subgamen1 the potto3subgamen1 to set
     */
    public void setPotto3subgamen1(C002SubGamenData potto3subgamen1) {
        this.potto3subgamen1 = potto3subgamen1;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto3subgamen2
     */
    public C002SubGamenData getPotto3subgamen2() {
        return potto3subgamen2;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto3subgamen2 the potto3subgamen2 to set
     */
    public void setPotto3subgamen2(C002SubGamenData potto3subgamen2) {
        this.potto3subgamen2 = potto3subgamen2;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto3subgamen3
     */
    public C002SubGamenData getPotto3subgamen3() {
        return potto3subgamen3;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto3subgamen3 the potto3subgamen3 to set
     */
    public void setPotto3subgamen3(C002SubGamenData potto3subgamen3) {
        this.potto3subgamen3 = potto3subgamen3;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto3subgamen4
     */
    public C002SubGamenData getPotto3subgamen4() {
        return potto3subgamen4;
    }

    /**
     * ﾎﾟｯﾄ③タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto3subgamen4 the potto3subgamen4 to set
     */
    public void setPotto3subgamen4(C002SubGamenData potto3subgamen4) {
        this.potto3subgamen4 = potto3subgamen4;
    }

    /**
     * ﾎﾟｯﾄ④タブの表示されるサブ画面用データ
     * @return the potto4subgamen1
     */
    public C002SubGamenData getPotto4subgamen1() {
        return potto4subgamen1;
    }

    /**
     * ﾎﾟｯﾄ④タブの表示されるサブ画面用データ
     * @param potto4subgamen1 the potto4subgamen1 to set
     */
    public void setPotto4subgamen1(C002SubGamenData potto4subgamen1) {
        this.potto4subgamen1 = potto4subgamen1;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto4subgamen2
     */
    public C002SubGamenData getPotto4subgamen2() {
        return potto4subgamen2;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto4subgamen2 the potto4subgamen2 to set
     */
    public void setPotto4subgamen2(C002SubGamenData potto4subgamen2) {
        this.potto4subgamen2 = potto4subgamen2;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto4subgamen3
     */
    public C002SubGamenData getPotto4subgamen3() {
        return potto4subgamen3;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto4subgamen3 the potto4subgamen3 to set
     */
    public void setPotto4subgamen3(C002SubGamenData potto4subgamen3) {
        this.potto4subgamen3 = potto4subgamen3;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto4subgamen4
     */
    public C002SubGamenData getPotto4subgamen4() {
        return potto4subgamen4;
    }

    /**
     * ﾎﾟｯﾄ④タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto4subgamen4 the potto4subgamen4 to set
     */
    public void setPotto4subgamen4(C002SubGamenData potto4subgamen4) {
        this.potto4subgamen4 = potto4subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの表示されるサブ画面用データ
     * @return the potto5subgamen1
     */
    public C002SubGamenData getPotto5subgamen1() {
        return potto5subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの表示されるサブ画面用データ
     * @param potto5subgamen1 the potto5subgamen1 to set
     */
    public void setPotto5subgamen1(C002SubGamenData potto5subgamen1) {
        this.potto5subgamen1 = potto5subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto5subgamen2
     */
    public C002SubGamenData getPotto5subgamen2() {
        return potto5subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto5subgamen2 the potto5subgamen2 to set
     */
    public void setPotto5subgamen2(C002SubGamenData potto5subgamen2) {
        this.potto5subgamen2 = potto5subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto5subgamen3
     */
    public C002SubGamenData getPotto5subgamen3() {
        return potto5subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto5subgamen3 the potto5subgamen3 to set
     */
    public void setPotto5subgamen3(C002SubGamenData potto5subgamen3) {
        this.potto5subgamen3 = potto5subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto5subgamen4
     */
    public C002SubGamenData getPotto5subgamen4() {
        return potto5subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑤タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto5subgamen4 the potto5subgamen4 to set
     */
    public void setPotto5subgamen4(C002SubGamenData potto5subgamen4) {
        this.potto5subgamen4 = potto5subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの表示されるサブ画面用データ
     * @return the potto6subgamen1
     */
    public C002SubGamenData getPotto6subgamen1() {
        return potto6subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの表示されるサブ画面用データ
     * @param potto6subgamen1 the potto6subgamen1 to set
     */
    public void setPotto6subgamen1(C002SubGamenData potto6subgamen1) {
        this.potto6subgamen1 = potto6subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto6subgamen2
     */
    public C002SubGamenData getPotto6subgamen2() {
        return potto6subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto6subgamen2 the potto6subgamen2 to set
     */
    public void setPotto6subgamen2(C002SubGamenData potto6subgamen2) {
        this.potto6subgamen2 = potto6subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto6subgamen3
     */
    public C002SubGamenData getPotto6subgamen3() {
        return potto6subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto6subgamen3 the potto6subgamen3 to set
     */
    public void setPotto6subgamen3(C002SubGamenData potto6subgamen3) {
        this.potto6subgamen3 = potto6subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto6subgamen4
     */
    public C002SubGamenData getPotto6subgamen4() {
        return potto6subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑥タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto6subgamen4 the potto6subgamen4 to set
     */
    public void setPotto6subgamen4(C002SubGamenData potto6subgamen4) {
        this.potto6subgamen4 = potto6subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの表示されるサブ画面用データ
     * @return the potto7subgamen1
     */
    public C002SubGamenData getPotto7subgamen1() {
        return potto7subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの表示されるサブ画面用データ
     * @param potto7subgamen1 the potto7subgamen1 to set
     */
    public void setPotto7subgamen1(C002SubGamenData potto7subgamen1) {
        this.potto7subgamen1 = potto7subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto7subgamen2
     */
    public C002SubGamenData getPotto7subgamen2() {
        return potto7subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto7subgamen2 the potto7subgamen2 to set
     */
    public void setPotto7subgamen2(C002SubGamenData potto7subgamen2) {
        this.potto7subgamen2 = potto7subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto7subgamen3
     */
    public C002SubGamenData getPotto7subgamen3() {
        return potto7subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto7subgamen3 the potto7subgamen3 to set
     */
    public void setPotto7subgamen3(C002SubGamenData potto7subgamen3) {
        this.potto7subgamen3 = potto7subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto7subgamen4
     */
    public C002SubGamenData getPotto7subgamen4() {
        return potto7subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑦タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto7subgamen4 the potto7subgamen4 to set
     */
    public void setPotto7subgamen4(C002SubGamenData potto7subgamen4) {
        this.potto7subgamen4 = potto7subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの表示されるサブ画面用データ
     * @return the potto8subgamen1
     */
    public C002SubGamenData getPotto8subgamen1() {
        return potto8subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの表示されるサブ画面用データ
     * @param potto8subgamen1 the potto8subgamen1 to set
     */
    public void setPotto8subgamen1(C002SubGamenData potto8subgamen1) {
        this.potto8subgamen1 = potto8subgamen1;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto8subgamen2
     */
    public C002SubGamenData getPotto8subgamen2() {
        return potto8subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名2】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto8subgamen2 the potto8subgamen2 to set
     */
    public void setPotto8subgamen2(C002SubGamenData potto8subgamen2) {
        this.potto8subgamen2 = potto8subgamen2;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto8subgamen3
     */
    public C002SubGamenData getPotto8subgamen3() {
        return potto8subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名3】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto8subgamen3 the potto8subgamen3 to set
     */
    public void setPotto8subgamen3(C002SubGamenData potto8subgamen3) {
        this.potto8subgamen3 = potto8subgamen3;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @return the potto8subgamen4
     */
    public C002SubGamenData getPotto8subgamen4() {
        return potto8subgamen4;
    }

    /**
     * ﾎﾟｯﾄ⑧タブの【材料品名4】ﾘﾝｸ押下時、表示されるサブ画面用データ
     * @param potto8subgamen4 the potto8subgamen4 to set
     */
    public void setPotto8subgamen4(C002SubGamenData potto8subgamen4) {
        this.potto8subgamen4 = potto8subgamen4;
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量サブ画面データ
     */
    public class C002SubGamenData implements Cloneable {

        /**
         * クローン実装
         *
         * @return クローン
         * @throws java.lang.CloneNotSupportedException
         */
        @Override
        public C002SubGamenData clone() throws CloneNotSupportedException {
            C002SubGamenData cloneModel = (C002SubGamenData) super.clone();
            // サブ画面の部材①タブデータリスト
            List<FXHDD01> subDataBuzaitab1List = new ArrayList();
            // サブ画面の部材②タブデータリスト
            List<FXHDD01> subDataBuzaitab2List = new ArrayList();

            // サブ画面の部材①タブデータリストのClone処理
            for (FXHDD01 data : this.getSubDataBuzaitab1()) {
                subDataBuzaitab1List.add(data.clone());
            }
            cloneModel.setSubDataBuzaitab1(subDataBuzaitab1List);

            // サブ画面の部材②タブデータリストのClone処理
            for (FXHDD01 data : this.getSubDataBuzaitab2()) {
                subDataBuzaitab2List.add(data.clone());
            }
            cloneModel.setSubDataBuzaitab2(subDataBuzaitab2List);
            // サブ画面の調合規格データのClone処理
            cloneModel.setSubDataTyogouryoukikaku(this.getSubDataTyogouryoukikaku().clone());
            // サブ画面の調合残量データのClone処理
            cloneModel.setSubDataTyogouzanryou(this.getSubDataTyogouzanryou().clone());

            return cloneModel;
        }

        /**
         * 部材在庫No1設定項目ID
         */
        private String returnItemIdBuzailotno1 = "";

        /**
         * 調合量1設定項目ID
         */
        private String returnItemIdTyougouryou1 = "";

        /**
         * 部材在庫No2設定項目ID
         */
        private String returnItemIdBuzailotno2 = "";

        /**
         * 調合量2設定項目ID
         */
        private String returnItemIdTyougouryou2 = "";

        /**
         * サブ画面のﾎﾟｯﾄ
         */
        private Integer subDataPot;

        /**
         * サブ画面の材料区分
         */
        private Integer subDataZairyokubun;

        /**
         * サブ画面の固定値「g」
         */
        private String subDataTani;

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         */
        private String subDataGoki;

        /**
         * サブ画面の調合規格データ
         */
        private FXHDD01 subDataTyogouryoukikaku;
        
        /**
         * サブ画面の調合残量データ
         */
        private FXHDD01 subDataTyogouzanryou;

        /**
         * サブ画面の部材①タブデータリスト
         */
        private List<FXHDD01> subDataBuzaitab1;

        /**
         * サブ画面の部材②タブデータリスト
         */
        private List<FXHDD01> subDataBuzaitab2;

        /**
         * 部材在庫No1設定項目ID
         * @return the returnItemIdBuzailotno1
         */
        public String getReturnItemIdBuzailotno1() {
            return returnItemIdBuzailotno1;
        }

        /**
         * 部材在庫No1設定項目ID
         * @param returnItemIdBuzailotno1 the returnItemIdBuzailotno1 to set
         */
        public void setReturnItemIdBuzailotno1(String returnItemIdBuzailotno1) {
            this.returnItemIdBuzailotno1 = returnItemIdBuzailotno1;
        }

        /**
         * 調合量1設定項目ID
         * @return the returnItemIdTyougouryou1
         */
        public String getReturnItemIdTyougouryou1() {
            return returnItemIdTyougouryou1;
        }

        /**
         * 調合量1設定項目ID
         * @param returnItemIdTyougouryou1 the returnItemIdTyougouryou1 to set
         */
        public void setReturnItemIdTyougouryou1(String returnItemIdTyougouryou1) {
            this.returnItemIdTyougouryou1 = returnItemIdTyougouryou1;
        }

        /**
         * 部材在庫No2設定項目ID
         * @return the returnItemIdBuzailotno2
         */
        public String getReturnItemIdBuzailotno2() {
            return returnItemIdBuzailotno2;
        }

        /**
         * 部材在庫No2設定項目ID
         * @param returnItemIdBuzailotno2 the returnItemIdBuzailotno2 to set
         */
        public void setReturnItemIdBuzailotno2(String returnItemIdBuzailotno2) {
            this.returnItemIdBuzailotno2 = returnItemIdBuzailotno2;
        }

        /**
         * 調合量2設定項目ID
         * @return the returnItemIdTyougouryou2
         */
        public String getReturnItemIdTyougouryou2() {
            return returnItemIdTyougouryou2;
        }

        /**
         * 調合量2設定項目ID
         * @param returnItemIdTyougouryou2 the returnItemIdTyougouryou2 to set
         */
        public void setReturnItemIdTyougouryou2(String returnItemIdTyougouryou2) {
            this.returnItemIdTyougouryou2 = returnItemIdTyougouryou2;
        }

        /**
         * サブ画面のﾎﾟｯﾄ
         * @return the subDataPot
         */
        public Integer getSubDataPot() {
            return subDataPot;
        }

        /**
         * サブ画面のﾎﾟｯﾄ
         * @param subDataPot the subDataPot to set
         */
        public void setSubDataPot(Integer subDataPot) {
            this.subDataPot = subDataPot;
        }

        /**
         * サブ画面の材料区分
         * @return the subDataZairyokubun
         */
        public Integer getSubDataZairyokubun() {
            return subDataZairyokubun;
        }

        /**
         * サブ画面の材料区分
         * @param subDataZairyokubun the subDataZairyokubun to set
         */
        public void setSubDataZairyokubun(Integer subDataZairyokubun) {
            this.subDataZairyokubun = subDataZairyokubun;
        }

        /**
         * サブ画面の固定値「g」
         * @return the subDataTani
         */
        public String getSubDataTani() {
            return "g";
        }

        /**
         * サブ画面の固定値「g」
         * @param subDataTani the subDataTani to set
         */
        public void setSubDataTani(String subDataTani) {
            this.subDataTani = subDataTani;
        }

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         * @return the subDataGoki
         */
        public String getSubDataGoki() {
            return subDataGoki;
        }

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         * @param subDataGoki the subDataGoki to set
         */
        public void setSubDataGoki(String subDataGoki) {
            this.subDataGoki = subDataGoki;
        }

        /**
         * サブ画面の調合規格データ
         * @return the subDataTyogouryoukikaku
         */
        public FXHDD01 getSubDataTyogouryoukikaku() {
            return subDataTyogouryoukikaku;
        }

        /**
         * サブ画面の調合規格データ
         * @param subDataTyogouryoukikaku the subDataTyogouryoukikaku to set
         */
        public void setSubDataTyogouryoukikaku(FXHDD01 subDataTyogouryoukikaku) {
            this.subDataTyogouryoukikaku = subDataTyogouryoukikaku;
        }

        /**
         * サブ画面の調合残量データ
         * @return the subDataTyogouzanryou
         */
        public FXHDD01 getSubDataTyogouzanryou() {
            return subDataTyogouzanryou;
        }

        /**
         * サブ画面の調合残量データ
         * @param subDataTyogouzanryou the subDataTyogouzanryou to set
         */
        public void setSubDataTyogouzanryou(FXHDD01 subDataTyogouzanryou) {
            this.subDataTyogouzanryou = subDataTyogouzanryou;
        }

        /**
         * サブ画面の部材①タブデータリスト
         * @return the subDataBuzaitab1
         */
        public List<FXHDD01> getSubDataBuzaitab1() {
            return subDataBuzaitab1;
        }

        /**
         * サブ画面の部材①タブデータリスト
         * @param subDataBuzaitab1 the subDataBuzaitab1 to set
         */
        public void setSubDataBuzaitab1(List<FXHDD01> subDataBuzaitab1) {
            this.subDataBuzaitab1 = subDataBuzaitab1;
        }

        /**
         * サブ画面の部材②タブデータリスト
         * @return the subDataBuzaitab2
         */
        public List<FXHDD01> getSubDataBuzaitab2() {
            return subDataBuzaitab2;
        }

        /**
         * サブ画面の部材②タブデータリスト
         * @param subDataBuzaitab2 the subDataBuzaitab2 to set
         */
        public void setSubDataBuzaitab2(List<FXHDD01> subDataBuzaitab2) {
            this.subDataBuzaitab2 = subDataBuzaitab2;
        }
    }
}
