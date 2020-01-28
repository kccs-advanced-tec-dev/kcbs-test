/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jp.co.kccs.xhd.db.model.FXHDD01;

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
 * GXHDO101B042D(電気特性・一般品(設定条件及び処理結果))
 *
 * @author 863 F.Zhang
 * @since 2019/12/28
 */
@ViewScoped
@Named("beanGXHDO101C042D")
public class GXHDO101B042D implements Serializable {

    /**
     * BIN1 %区分(設定値)
     */
    private FXHDD01 bin1PercentKbn;

    /**
     * BIN1 選別区分
     */
    private FXHDD01 bin1SenbetsuKbn;

    /**
     * BIN1 計量後数量
     */
    private FXHDD01 bin1KeiryogoSuryo;

    /**
     * BIN1 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin1CounterSu;

    /**
     * BIN1 誤差率(%)
     */
    private FXHDD01 bin1Gosaritsu;

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin1MachineFuryoritsu;

    /**
     * BIN1 抜き取り結果
     */
    private FXHDD01 bin1NukitorikekkaS;

    /**
     * BIN1 抜き取り結果
     */
    private FXHDD01 bin1NukitorikekkaT;

    /**
     * BIN1 真の不良率(%)
     */
    private FXHDD01 bin1ShinFuryoritsu;

    /**
     * BIN1 結果ﾁｪｯｸ
     */
    private FXHDD01 bin1KekkaCheck;

    /**
     * BIN2 %区分(設定値)
     */
    private FXHDD01 bin2PercentKbn;

    /**
     * BIN2 選別区分
     */
    private FXHDD01 bin2SenbetsuKbn;

    /**
     * BIN2 計量後数量
     */
    private FXHDD01 bin2KeiryogoSuryo;

    /**
     * BIN2 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin2CounterSu;

    /**
     * BIN2 誤差率(%)
     */
    private FXHDD01 bin2Gosaritsu;

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin2MachineFuryoritsu;

    /**
     * BIN2 抜き取り結果
     */
    private FXHDD01 bin2NukitorikekkaS;

    /**
     * BIN2 抜き取り結果
     */
    private FXHDD01 bin2NukitorikekkaT;

    /**
     * BIN2 真の不良率(%)
     */
    private FXHDD01 bin2ShinFuryoritsu;

    /**
     * BIN2 結果ﾁｪｯｸ
     */
    private FXHDD01 bin2KekkaCheck;

    /**
     * BIN3 %区分(設定値)
     */
    private FXHDD01 bin3PercentKbn;

    /**
     * BIN3 選別区分
     */
    private FXHDD01 bin3SenbetsuKbn;

    /**
     * BIN3 計量後数量
     */
    private FXHDD01 bin3KeiryogoSuryo;

    /**
     * BIN3 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin3CounterSu;

    /**
     * BIN3 誤差率(%)
     */
    private FXHDD01 bin3Gosaritsu;

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin3MachineFuryoritsu;

    /**
     * BIN3 抜き取り結果
     */
    private FXHDD01 bin3NukitorikekkaS;

    /**
     * BIN3 抜き取り結果
     */
    private FXHDD01 bin3NukitorikekkaT;

    /**
     * BIN3 真の不良率(%)
     */
    private FXHDD01 bin3ShinFuryoritsu;

    /**
     * BIN3 結果ﾁｪｯｸ
     */
    private FXHDD01 bin3KekkaCheck;

    /**
     * BIN4 %区分(設定値)
     */
    private FXHDD01 bin4PercentKbn;

    /**
     * BIN4 選別区分
     */
    private FXHDD01 bin4SenbetsuKbn;

    /**
     * BIN4 計量後数量
     */
    private FXHDD01 bin4KeiryogoSuryo;

    /**
     * BIN4 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin4CounterSu;

    /**
     * BIN4 誤差率(%)
     */
    private FXHDD01 bin4Gosaritsu;

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin4MachineFuryoritsu;

    /**
     * BIN4 抜き取り結果
     */
    private FXHDD01 bin4NukitorikekkaS;

    /**
     * BIN4 抜き取り結果
     */
    private FXHDD01 bin4NukitorikekkaT;

    /**
     * BIN4 真の不良率(%)
     */
    private FXHDD01 bin4ShinFuryoritsu;

    /**
     * BIN4 結果ﾁｪｯｸ
     */
    private FXHDD01 bin4KekkaCheck;

    /**
     * BIN5 %区分(設定値)
     */
    private FXHDD01 bin5PercentKbn;

    /**
     * BIN5 選別区分
     */
    private FXHDD01 bin5SenbetsuKbn;

    /**
     * BIN5 計量後数量
     */
    private FXHDD01 bin5KeiryogoSuryo;

    /**
     * BIN5 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin5CounterSu;

    /**
     * BIN5 誤差率(%)
     */
    private FXHDD01 bin5Gosaritsu;

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin5MachineFuryoritsu;

    /**
     * BIN5 抜き取り結果
     */
    private FXHDD01 bin5NukitorikekkaS;

    /**
     * BIN5 抜き取り結果
     */
    private FXHDD01 bin5NukitorikekkaT;

    /**
     * BIN5 真の不良率(%)
     */
    private FXHDD01 bin5ShinFuryoritsu;

    /**
     * BIN5 結果ﾁｪｯｸ
     */
    private FXHDD01 bin5KekkaCheck;

    /**
     * BIN5 袋ﾁｪｯｸ
     */
    private FXHDD01 bin5FukuroCheck;

    /**
     * BIN6 %区分(設定値)
     */
    private FXHDD01 bin6PercentKbn;

    /**
     * BIN6 選別区分
     */
    private FXHDD01 bin6SenbetsuKbn;

    /**
     * BIN6 計量後数量
     */
    private FXHDD01 bin6KeiryogoSuryo;

    /**
     * BIN6 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin6CounterSu;

    /**
     * BIN6 誤差率(%)
     */
    private FXHDD01 bin6Gosaritsu;

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin6MachineFuryoritsu;

    /**
     * BIN6 抜き取り結果
     */
    private FXHDD01 bin6NukitorikekkaS;

    /**
     * BIN6 抜き取り結果
     */
    private FXHDD01 bin6NukitorikekkaT;

    /**
     * BIN6 真の不良率(%)
     */
    private FXHDD01 bin6ShinFuryoritsu;

    /**
     * BIN6 結果ﾁｪｯｸ
     */
    private FXHDD01 bin6KekkaCheck;

    /**
     * BIN6 袋ﾁｪｯｸ
     */
    private FXHDD01 bin6FukuroCheck;

    /**
     * BIN7 %区分(設定値)
     */
    private FXHDD01 bin7PercentKbn;

    /**
     * BIN7 選別区分
     */
    private FXHDD01 bin7SenbetsuKbn;

    /**
     * BIN7 計量後数量
     */
    private FXHDD01 bin7KeiryogoSuryo;

    /**
     * BIN7 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin7CounterSu;

    /**
     * BIN7 誤差率(%)
     */
    private FXHDD01 bin7Gosaritsu;

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin7MachineFuryoritsu;

    /**
     * BIN7 袋ﾁｪｯｸ
     */
    private FXHDD01 bin7FukuroCheck;

    /**
     * BIN8 %区分(設定値)
     */
    private FXHDD01 bin8PercentKbn;

    /**
     * BIN8 選別区分
     */
    private FXHDD01 bin8SenbetsuKbn;

    /**
     * BIN8 計量後数量
     */
    private FXHDD01 bin8KeiryogoSuryo;

    /**
     * BIN8 ｶｳﾝﾀｰ数
     */
    private FXHDD01 bin8CounterSu;

    /**
     * BIN8 誤差率(%)
     */
    private FXHDD01 bin8Gosaritsu;

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     */
    private FXHDD01 bin8MachineFuryoritsu;

    /**
     * BIN8 袋ﾁｪｯｸ
     */
    private FXHDD01 bin8FukuroCheck;

    /**
     * BIN9 強制排出 計量後数量
     */
    private FXHDD01 bin9KKeiryogoSuryo;

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     */
    private FXHDD01 bin9KMachineFuryoritsu;

    /**
     * 落下 計量後数量
     */
    private FXHDD01 rakkaKeiryogoSuryo;

    /**
     * 落下 ﾏｼﾝ不良率
     */
    private FXHDD01 rakkaMachineFuryoritsu;

    /**
     * 半田ｻﾝﾌﾟﾙ
     */
    private FXHDD01 handaSample;

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     */
    private FXHDD01 shinraiseiSample;

    /**
     * 真不良判定者
     */
    private FXHDD01 shinFuryoHanteisha;

    /**
     * 判定入力者
     */
    private FXHDD01 hanteiNyuryokusha;

    /**
     * 取出者
     */
    private FXHDD01 toridashisha;

    /**
     * 公差①
     */
    private FXHDD01 kousa1;

    /**
     * 重量①
     */
    private FXHDD01 juryo1;

    /**
     * 個数①
     */
    private FXHDD01 kosu1;

    /**
     * 公差②
     */
    private FXHDD01 kousa2;

    /**
     * 重量②
     */
    private FXHDD01 juryo2;

    /**
     * 個数②
     */
    private FXHDD01 kosu2;

    /**
     * 公差③
     */
    private FXHDD01 kousa3;

    /**
     * 重量③
     */
    private FXHDD01 juryo3;

    /**
     * 個数③
     */
    private FXHDD01 kosu3;

    /**
     * 公差④
     */
    private FXHDD01 kousa4;

    /**
     * 重量④
     */
    private FXHDD01 juryo4;

    /**
     * 個数④
     */
    private FXHDD01 kosu4;

    /**
     * ｶｳﾝﾀｰ総数
     */
    private FXHDD01 counterSosu;

    /**
     * 良品重量
     */
    private FXHDD01 ryouhinJuryo;

    /**
     * 良品個数
     */
    private FXHDD01 ryouhinKosu;

    /**
     * 歩留まり
     */
    private FXHDD01 budomari;

    /**
     * 確認者
     */
    private FXHDD01 kakuninsha;

    /**
     * コンストラクタ
     */
    public GXHDO101B042D() {
    }

    /**
     * BIN1 %区分(設定値)
     * @return the bin1PercentKbn
     */
    public FXHDD01 getBin1PercentKbn() {
        return bin1PercentKbn;
    }

    /**
     * BIN1 %区分(設定値)
     * @param bin1PercentKbn the bin1PercentKbn to set
     */
    public void setBin1PercentKbn(FXHDD01 bin1PercentKbn) {
        this.bin1PercentKbn = bin1PercentKbn;
    }

    /**
     * BIN1 選別区分
     * @return the bin1SenbetsuKbn
     */
    public FXHDD01 getBin1SenbetsuKbn() {
        return bin1SenbetsuKbn;
    }

    /**
     * BIN1 選別区分
     * @param bin1SenbetsuKbn the bin1SenbetsuKbn to set
     */
    public void setBin1SenbetsuKbn(FXHDD01 bin1SenbetsuKbn) {
        this.bin1SenbetsuKbn = bin1SenbetsuKbn;
    }

    /**
     * BIN1 計量後数量
     * @return the bin1KeiryogoSuryo
     */
    public FXHDD01 getBin1KeiryogoSuryo() {
        return bin1KeiryogoSuryo;
    }

    /**
     * BIN1 計量後数量
     * @param bin1KeiryogoSuryo the bin1KeiryogoSuryo to set
     */
    public void setBin1KeiryogoSuryo(FXHDD01 bin1KeiryogoSuryo) {
        this.bin1KeiryogoSuryo = bin1KeiryogoSuryo;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @return the bin1CounterSu
     */
    public FXHDD01 getBin1CounterSu() {
        return bin1CounterSu;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @param bin1CounterSu the bin1CounterSu to set
     */
    public void setBin1CounterSu(FXHDD01 bin1CounterSu) {
        this.bin1CounterSu = bin1CounterSu;
    }

    /**
     * BIN1 誤差率(%)
     * @return the bin1Gosaritsu
     */
    public FXHDD01 getBin1Gosaritsu() {
        return bin1Gosaritsu;
    }

    /**
     * BIN1 誤差率(%)
     * @param bin1Gosaritsu the bin1Gosaritsu to set
     */
    public void setBin1Gosaritsu(FXHDD01 bin1Gosaritsu) {
        this.bin1Gosaritsu = bin1Gosaritsu;
    }

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     * @return the bin1MachineFuryoritsu
     */
    public FXHDD01 getBin1MachineFuryoritsu() {
        return bin1MachineFuryoritsu;
    }

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     * @param bin1MachineFuryoritsu the bin1MachineFuryoritsu to set
     */
    public void setBin1MachineFuryoritsu(FXHDD01 bin1MachineFuryoritsu) {
        this.bin1MachineFuryoritsu = bin1MachineFuryoritsu;
    }

    /**
     * BIN1 抜き取り結果
     * @return the bin1NukitorikekkaS
     */
    public FXHDD01 getBin1NukitorikekkaS() {
        return bin1NukitorikekkaS;
    }

    /**
     * BIN1 抜き取り結果
     * @param bin1NukitorikekkaS the bin1NukitorikekkaS to set
     */
    public void setBin1NukitorikekkaS(FXHDD01 bin1NukitorikekkaS) {
        this.bin1NukitorikekkaS = bin1NukitorikekkaS;
    }

    /**
     * BIN1 抜き取り結果
     * @return the bin1NukitorikekkaT
     */
    public FXHDD01 getBin1NukitorikekkaT() {
        return bin1NukitorikekkaT;
    }

    /**
     * BIN1 抜き取り結果
     * @param bin1NukitorikekkaT the bin1NukitorikekkaT to set
     */
    public void setBin1NukitorikekkaT(FXHDD01 bin1NukitorikekkaT) {
        this.bin1NukitorikekkaT = bin1NukitorikekkaT;
    }

    /**
     * BIN1 真の不良率(%)
     * @return the bin1ShinFuryoritsu
     */
    public FXHDD01 getBin1ShinFuryoritsu() {
        return bin1ShinFuryoritsu;
    }

    /**
     * BIN1 真の不良率(%)
     * @param bin1ShinFuryoritsu the bin1ShinFuryoritsu to set
     */
    public void setBin1ShinFuryoritsu(FXHDD01 bin1ShinFuryoritsu) {
        this.bin1ShinFuryoritsu = bin1ShinFuryoritsu;
    }

    /**
     * BIN1 結果ﾁｪｯｸ
     * @return the bin1KekkaCheck
     */
    public FXHDD01 getBin1KekkaCheck() {
        return bin1KekkaCheck;
    }

    /**
     * BIN1 結果ﾁｪｯｸ
     * @param bin1KekkaCheck the bin1KekkaCheck to set
     */
    public void setBin1KekkaCheck(FXHDD01 bin1KekkaCheck) {
        this.bin1KekkaCheck = bin1KekkaCheck;
    }

    /**
     * BIN2 %区分(設定値)
     * @return the bin2PercentKbn
     */
    public FXHDD01 getBin2PercentKbn() {
        return bin2PercentKbn;
    }

    /**
     * BIN2 %区分(設定値)
     * @param bin2PercentKbn the bin2PercentKbn to set
     */
    public void setBin2PercentKbn(FXHDD01 bin2PercentKbn) {
        this.bin2PercentKbn = bin2PercentKbn;
    }

    /**
     * BIN2 選別区分
     * @return the bin2SenbetsuKbn
     */
    public FXHDD01 getBin2SenbetsuKbn() {
        return bin2SenbetsuKbn;
    }

    /**
     * BIN2 選別区分
     * @param bin2SenbetsuKbn the bin2SenbetsuKbn to set
     */
    public void setBin2SenbetsuKbn(FXHDD01 bin2SenbetsuKbn) {
        this.bin2SenbetsuKbn = bin2SenbetsuKbn;
    }

    /**
     * BIN2 計量後数量
     * @return the bin2KeiryogoSuryo
     */
    public FXHDD01 getBin2KeiryogoSuryo() {
        return bin2KeiryogoSuryo;
    }

    /**
     * BIN2 計量後数量
     * @param bin2KeiryogoSuryo the bin2KeiryogoSuryo to set
     */
    public void setBin2KeiryogoSuryo(FXHDD01 bin2KeiryogoSuryo) {
        this.bin2KeiryogoSuryo = bin2KeiryogoSuryo;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @return the bin2CounterSu
     */
    public FXHDD01 getBin2CounterSu() {
        return bin2CounterSu;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @param bin2CounterSu the bin2CounterSu to set
     */
    public void setBin2CounterSu(FXHDD01 bin2CounterSu) {
        this.bin2CounterSu = bin2CounterSu;
    }

    /**
     * BIN2 誤差率(%)
     * @return the bin2Gosaritsu
     */
    public FXHDD01 getBin2Gosaritsu() {
        return bin2Gosaritsu;
    }

    /**
     * BIN2 誤差率(%)
     * @param bin2Gosaritsu the bin2Gosaritsu to set
     */
    public void setBin2Gosaritsu(FXHDD01 bin2Gosaritsu) {
        this.bin2Gosaritsu = bin2Gosaritsu;
    }

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     * @return the bin2MachineFuryoritsu
     */
    public FXHDD01 getBin2MachineFuryoritsu() {
        return bin2MachineFuryoritsu;
    }

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     * @param bin2MachineFuryoritsu the bin2MachineFuryoritsu to set
     */
    public void setBin2MachineFuryoritsu(FXHDD01 bin2MachineFuryoritsu) {
        this.bin2MachineFuryoritsu = bin2MachineFuryoritsu;
    }

    /**
     * BIN2 抜き取り結果
     * @return the bin2NukitorikekkaS
     */
    public FXHDD01 getBin2NukitorikekkaS() {
        return bin2NukitorikekkaS;
    }

    /**
     * BIN2 抜き取り結果
     * @param bin2NukitorikekkaS the bin2NukitorikekkaS to set
     */
    public void setBin2NukitorikekkaS(FXHDD01 bin2NukitorikekkaS) {
        this.bin2NukitorikekkaS = bin2NukitorikekkaS;
    }

    /**
     * BIN2 抜き取り結果
     * @return the bin2NukitorikekkaT
     */
    public FXHDD01 getBin2NukitorikekkaT() {
        return bin2NukitorikekkaT;
    }

    /**
     * BIN2 抜き取り結果
     * @param bin2NukitorikekkaT the bin2NukitorikekkaT to set
     */
    public void setBin2NukitorikekkaT(FXHDD01 bin2NukitorikekkaT) {
        this.bin2NukitorikekkaT = bin2NukitorikekkaT;
    }

    /**
     * BIN2 真の不良率(%)
     * @return the bin2ShinFuryoritsu
     */
    public FXHDD01 getBin2ShinFuryoritsu() {
        return bin2ShinFuryoritsu;
    }

    /**
     * BIN2 真の不良率(%)
     * @param bin2ShinFuryoritsu the bin2ShinFuryoritsu to set
     */
    public void setBin2ShinFuryoritsu(FXHDD01 bin2ShinFuryoritsu) {
        this.bin2ShinFuryoritsu = bin2ShinFuryoritsu;
    }

    /**
     * BIN2 結果ﾁｪｯｸ
     * @return the bin2KekkaCheck
     */
    public FXHDD01 getBin2KekkaCheck() {
        return bin2KekkaCheck;
    }

    /**
     * BIN2 結果ﾁｪｯｸ
     * @param bin2KekkaCheck the bin2KekkaCheck to set
     */
    public void setBin2KekkaCheck(FXHDD01 bin2KekkaCheck) {
        this.bin2KekkaCheck = bin2KekkaCheck;
    }

    /**
     * BIN3 %区分(設定値)
     * @return the bin3PercentKbn
     */
    public FXHDD01 getBin3PercentKbn() {
        return bin3PercentKbn;
    }

    /**
     * BIN3 %区分(設定値)
     * @param bin3PercentKbn the bin3PercentKbn to set
     */
    public void setBin3PercentKbn(FXHDD01 bin3PercentKbn) {
        this.bin3PercentKbn = bin3PercentKbn;
    }

    /**
     * BIN3 選別区分
     * @return the bin3SenbetsuKbn
     */
    public FXHDD01 getBin3SenbetsuKbn() {
        return bin3SenbetsuKbn;
    }

    /**
     * BIN3 選別区分
     * @param bin3SenbetsuKbn the bin3SenbetsuKbn to set
     */
    public void setBin3SenbetsuKbn(FXHDD01 bin3SenbetsuKbn) {
        this.bin3SenbetsuKbn = bin3SenbetsuKbn;
    }

    /**
     * BIN3 計量後数量
     * @return the bin3KeiryogoSuryo
     */
    public FXHDD01 getBin3KeiryogoSuryo() {
        return bin3KeiryogoSuryo;
    }

    /**
     * BIN3 計量後数量
     * @param bin3KeiryogoSuryo the bin3KeiryogoSuryo to set
     */
    public void setBin3KeiryogoSuryo(FXHDD01 bin3KeiryogoSuryo) {
        this.bin3KeiryogoSuryo = bin3KeiryogoSuryo;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @return the bin3CounterSu
     */
    public FXHDD01 getBin3CounterSu() {
        return bin3CounterSu;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @param bin3CounterSu the bin3CounterSu to set
     */
    public void setBin3CounterSu(FXHDD01 bin3CounterSu) {
        this.bin3CounterSu = bin3CounterSu;
    }

    /**
     * BIN3 誤差率(%)
     * @return the bin3Gosaritsu
     */
    public FXHDD01 getBin3Gosaritsu() {
        return bin3Gosaritsu;
    }

    /**
     * BIN3 誤差率(%)
     * @param bin3Gosaritsu the bin3Gosaritsu to set
     */
    public void setBin3Gosaritsu(FXHDD01 bin3Gosaritsu) {
        this.bin3Gosaritsu = bin3Gosaritsu;
    }

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     * @return the bin3MachineFuryoritsu
     */
    public FXHDD01 getBin3MachineFuryoritsu() {
        return bin3MachineFuryoritsu;
    }

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     * @param bin3MachineFuryoritsu the bin3MachineFuryoritsu to set
     */
    public void setBin3MachineFuryoritsu(FXHDD01 bin3MachineFuryoritsu) {
        this.bin3MachineFuryoritsu = bin3MachineFuryoritsu;
    }

    /**
     * BIN3 抜き取り結果
     * @return the bin3NukitorikekkaS
     */
    public FXHDD01 getBin3NukitorikekkaS() {
        return bin3NukitorikekkaS;
    }

    /**
     * BIN3 抜き取り結果
     * @param bin3NukitorikekkaS the bin3NukitorikekkaS to set
     */
    public void setBin3NukitorikekkaS(FXHDD01 bin3NukitorikekkaS) {
        this.bin3NukitorikekkaS = bin3NukitorikekkaS;
    }

    /**
     * BIN3 抜き取り結果
     * @return the bin3NukitorikekkaT
     */
    public FXHDD01 getBin3NukitorikekkaT() {
        return bin3NukitorikekkaT;
    }

    /**
     * BIN3 抜き取り結果
     * @param bin3NukitorikekkaT the bin3NukitorikekkaT to set
     */
    public void setBin3NukitorikekkaT(FXHDD01 bin3NukitorikekkaT) {
        this.bin3NukitorikekkaT = bin3NukitorikekkaT;
    }

    /**
     * BIN3 真の不良率(%)
     * @return the bin3ShinFuryoritsu
     */
    public FXHDD01 getBin3ShinFuryoritsu() {
        return bin3ShinFuryoritsu;
    }

    /**
     * BIN3 真の不良率(%)
     * @param bin3ShinFuryoritsu the bin3ShinFuryoritsu to set
     */
    public void setBin3ShinFuryoritsu(FXHDD01 bin3ShinFuryoritsu) {
        this.bin3ShinFuryoritsu = bin3ShinFuryoritsu;
    }

    /**
     * BIN3 結果ﾁｪｯｸ
     * @return the bin3KekkaCheck
     */
    public FXHDD01 getBin3KekkaCheck() {
        return bin3KekkaCheck;
    }

    /**
     * BIN3 結果ﾁｪｯｸ
     * @param bin3KekkaCheck the bin3KekkaCheck to set
     */
    public void setBin3KekkaCheck(FXHDD01 bin3KekkaCheck) {
        this.bin3KekkaCheck = bin3KekkaCheck;
    }

    /**
     * BIN4 %区分(設定値)
     * @return the bin4PercentKbn
     */
    public FXHDD01 getBin4PercentKbn() {
        return bin4PercentKbn;
    }

    /**
     * BIN4 %区分(設定値)
     * @param bin4PercentKbn the bin4PercentKbn to set
     */
    public void setBin4PercentKbn(FXHDD01 bin4PercentKbn) {
        this.bin4PercentKbn = bin4PercentKbn;
    }

    /**
     * BIN4 選別区分
     * @return the bin4SenbetsuKbn
     */
    public FXHDD01 getBin4SenbetsuKbn() {
        return bin4SenbetsuKbn;
    }

    /**
     * BIN4 選別区分
     * @param bin4SenbetsuKbn the bin4SenbetsuKbn to set
     */
    public void setBin4SenbetsuKbn(FXHDD01 bin4SenbetsuKbn) {
        this.bin4SenbetsuKbn = bin4SenbetsuKbn;
    }

    /**
     * BIN4 計量後数量
     * @return the bin4KeiryogoSuryo
     */
    public FXHDD01 getBin4KeiryogoSuryo() {
        return bin4KeiryogoSuryo;
    }

    /**
     * BIN4 計量後数量
     * @param bin4KeiryogoSuryo the bin4KeiryogoSuryo to set
     */
    public void setBin4KeiryogoSuryo(FXHDD01 bin4KeiryogoSuryo) {
        this.bin4KeiryogoSuryo = bin4KeiryogoSuryo;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @return the bin4CounterSu
     */
    public FXHDD01 getBin4CounterSu() {
        return bin4CounterSu;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @param bin4CounterSu the bin4CounterSu to set
     */
    public void setBin4CounterSu(FXHDD01 bin4CounterSu) {
        this.bin4CounterSu = bin4CounterSu;
    }

    /**
     * BIN4 誤差率(%)
     * @return the bin4Gosaritsu
     */
    public FXHDD01 getBin4Gosaritsu() {
        return bin4Gosaritsu;
    }

    /**
     * BIN4 誤差率(%)
     * @param bin4Gosaritsu the bin4Gosaritsu to set
     */
    public void setBin4Gosaritsu(FXHDD01 bin4Gosaritsu) {
        this.bin4Gosaritsu = bin4Gosaritsu;
    }

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     * @return the bin4MachineFuryoritsu
     */
    public FXHDD01 getBin4MachineFuryoritsu() {
        return bin4MachineFuryoritsu;
    }

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     * @param bin4MachineFuryoritsu the bin4MachineFuryoritsu to set
     */
    public void setBin4MachineFuryoritsu(FXHDD01 bin4MachineFuryoritsu) {
        this.bin4MachineFuryoritsu = bin4MachineFuryoritsu;
    }

    /**
     * BIN4 抜き取り結果
     * @return the bin4NukitorikekkaS
     */
    public FXHDD01 getBin4NukitorikekkaS() {
        return bin4NukitorikekkaS;
    }

    /**
     * BIN4 抜き取り結果
     * @param bin4NukitorikekkaS the bin4NukitorikekkaS to set
     */
    public void setBin4NukitorikekkaS(FXHDD01 bin4NukitorikekkaS) {
        this.bin4NukitorikekkaS = bin4NukitorikekkaS;
    }

    /**
     * BIN4 抜き取り結果
     * @return the bin4NukitorikekkaT
     */
    public FXHDD01 getBin4NukitorikekkaT() {
        return bin4NukitorikekkaT;
    }

    /**
     * BIN4 抜き取り結果
     * @param bin4NukitorikekkaT the bin4NukitorikekkaT to set
     */
    public void setBin4NukitorikekkaT(FXHDD01 bin4NukitorikekkaT) {
        this.bin4NukitorikekkaT = bin4NukitorikekkaT;
    }

    /**
     * BIN4 真の不良率(%)
     * @return the bin4ShinFuryoritsu
     */
    public FXHDD01 getBin4ShinFuryoritsu() {
        return bin4ShinFuryoritsu;
    }

    /**
     * BIN4 真の不良率(%)
     * @param bin4ShinFuryoritsu the bin4ShinFuryoritsu to set
     */
    public void setBin4ShinFuryoritsu(FXHDD01 bin4ShinFuryoritsu) {
        this.bin4ShinFuryoritsu = bin4ShinFuryoritsu;
    }

    /**
     * BIN4 結果ﾁｪｯｸ
     * @return the bin4KekkaCheck
     */
    public FXHDD01 getBin4KekkaCheck() {
        return bin4KekkaCheck;
    }

    /**
     * BIN4 結果ﾁｪｯｸ
     * @param bin4KekkaCheck the bin4KekkaCheck to set
     */
    public void setBin4KekkaCheck(FXHDD01 bin4KekkaCheck) {
        this.bin4KekkaCheck = bin4KekkaCheck;
    }

    /**
     * BIN5 %区分(設定値)
     * @return the bin5PercentKbn
     */
    public FXHDD01 getBin5PercentKbn() {
        return bin5PercentKbn;
    }

    /**
     * BIN5 %区分(設定値)
     * @param bin5PercentKbn the bin5PercentKbn to set
     */
    public void setBin5PercentKbn(FXHDD01 bin5PercentKbn) {
        this.bin5PercentKbn = bin5PercentKbn;
    }

    /**
     * BIN5 選別区分
     * @return the bin5SenbetsuKbn
     */
    public FXHDD01 getBin5SenbetsuKbn() {
        return bin5SenbetsuKbn;
    }

    /**
     * BIN5 選別区分
     * @param bin5SenbetsuKbn the bin5SenbetsuKbn to set
     */
    public void setBin5SenbetsuKbn(FXHDD01 bin5SenbetsuKbn) {
        this.bin5SenbetsuKbn = bin5SenbetsuKbn;
    }

    /**
     * BIN5 計量後数量
     * @return the bin5KeiryogoSuryo
     */
    public FXHDD01 getBin5KeiryogoSuryo() {
        return bin5KeiryogoSuryo;
    }

    /**
     * BIN5 計量後数量
     * @param bin5KeiryogoSuryo the bin5KeiryogoSuryo to set
     */
    public void setBin5KeiryogoSuryo(FXHDD01 bin5KeiryogoSuryo) {
        this.bin5KeiryogoSuryo = bin5KeiryogoSuryo;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @return the bin5CounterSu
     */
    public FXHDD01 getBin5CounterSu() {
        return bin5CounterSu;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @param bin5CounterSu the bin5CounterSu to set
     */
    public void setBin5CounterSu(FXHDD01 bin5CounterSu) {
        this.bin5CounterSu = bin5CounterSu;
    }

    /**
     * BIN5 誤差率(%)
     * @return the bin5Gosaritsu
     */
    public FXHDD01 getBin5Gosaritsu() {
        return bin5Gosaritsu;
    }

    /**
     * BIN5 誤差率(%)
     * @param bin5Gosaritsu the bin5Gosaritsu to set
     */
    public void setBin5Gosaritsu(FXHDD01 bin5Gosaritsu) {
        this.bin5Gosaritsu = bin5Gosaritsu;
    }

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     * @return the bin5MachineFuryoritsu
     */
    public FXHDD01 getBin5MachineFuryoritsu() {
        return bin5MachineFuryoritsu;
    }

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     * @param bin5MachineFuryoritsu the bin5MachineFuryoritsu to set
     */
    public void setBin5MachineFuryoritsu(FXHDD01 bin5MachineFuryoritsu) {
        this.bin5MachineFuryoritsu = bin5MachineFuryoritsu;
    }

    /**
     * BIN5 抜き取り結果
     * @return the bin5NukitorikekkaS
     */
    public FXHDD01 getBin5NukitorikekkaS() {
        return bin5NukitorikekkaS;
    }

    /**
     * BIN5 抜き取り結果
     * @param bin5NukitorikekkaS the bin5NukitorikekkaS to set
     */
    public void setBin5NukitorikekkaS(FXHDD01 bin5NukitorikekkaS) {
        this.bin5NukitorikekkaS = bin5NukitorikekkaS;
    }

    /**
     * BIN5 抜き取り結果
     * @return the bin5NukitorikekkaT
     */
    public FXHDD01 getBin5NukitorikekkaT() {
        return bin5NukitorikekkaT;
    }

    /**
     * BIN5 抜き取り結果
     * @param bin5NukitorikekkaT the bin5NukitorikekkaT to set
     */
    public void setBin5NukitorikekkaT(FXHDD01 bin5NukitorikekkaT) {
        this.bin5NukitorikekkaT = bin5NukitorikekkaT;
    }

    /**
     * BIN5 真の不良率(%)
     * @return the bin5ShinFuryoritsu
     */
    public FXHDD01 getBin5ShinFuryoritsu() {
        return bin5ShinFuryoritsu;
    }

    /**
     * BIN5 真の不良率(%)
     * @param bin5ShinFuryoritsu the bin5ShinFuryoritsu to set
     */
    public void setBin5ShinFuryoritsu(FXHDD01 bin5ShinFuryoritsu) {
        this.bin5ShinFuryoritsu = bin5ShinFuryoritsu;
    }

    /**
     * BIN5 結果ﾁｪｯｸ
     * @return the bin5KekkaCheck
     */
    public FXHDD01 getBin5KekkaCheck() {
        return bin5KekkaCheck;
    }

    /**
     * BIN5 結果ﾁｪｯｸ
     * @param bin5KekkaCheck the bin5KekkaCheck to set
     */
    public void setBin5KekkaCheck(FXHDD01 bin5KekkaCheck) {
        this.bin5KekkaCheck = bin5KekkaCheck;
    }

    /**
     * BIN5 袋ﾁｪｯｸ
     * @return the bin5FukuroCheck
     */
    public FXHDD01 getBin5FukuroCheck() {
        return bin5FukuroCheck;
    }

    /**
     * BIN5 袋ﾁｪｯｸ
     * @param bin5FukuroCheck the bin5FukuroCheck to set
     */
    public void setBin5FukuroCheck(FXHDD01 bin5FukuroCheck) {
        this.bin5FukuroCheck = bin5FukuroCheck;
    }

    /**
     * BIN6 %区分(設定値)
     * @return the bin6PercentKbn
     */
    public FXHDD01 getBin6PercentKbn() {
        return bin6PercentKbn;
    }

    /**
     * BIN6 %区分(設定値)
     * @param bin6PercentKbn the bin6PercentKbn to set
     */
    public void setBin6PercentKbn(FXHDD01 bin6PercentKbn) {
        this.bin6PercentKbn = bin6PercentKbn;
    }

    /**
     * BIN6 選別区分
     * @return the bin6SenbetsuKbn
     */
    public FXHDD01 getBin6SenbetsuKbn() {
        return bin6SenbetsuKbn;
    }

    /**
     * BIN6 選別区分
     * @param bin6SenbetsuKbn the bin6SenbetsuKbn to set
     */
    public void setBin6SenbetsuKbn(FXHDD01 bin6SenbetsuKbn) {
        this.bin6SenbetsuKbn = bin6SenbetsuKbn;
    }

    /**
     * BIN6 計量後数量
     * @return the bin6KeiryogoSuryo
     */
    public FXHDD01 getBin6KeiryogoSuryo() {
        return bin6KeiryogoSuryo;
    }

    /**
     * BIN6 計量後数量
     * @param bin6KeiryogoSuryo the bin6KeiryogoSuryo to set
     */
    public void setBin6KeiryogoSuryo(FXHDD01 bin6KeiryogoSuryo) {
        this.bin6KeiryogoSuryo = bin6KeiryogoSuryo;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @return the bin6CounterSu
     */
    public FXHDD01 getBin6CounterSu() {
        return bin6CounterSu;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @param bin6CounterSu the bin6CounterSu to set
     */
    public void setBin6CounterSu(FXHDD01 bin6CounterSu) {
        this.bin6CounterSu = bin6CounterSu;
    }

    /**
     * BIN6 誤差率(%)
     * @return the bin6Gosaritsu
     */
    public FXHDD01 getBin6Gosaritsu() {
        return bin6Gosaritsu;
    }

    /**
     * BIN6 誤差率(%)
     * @param bin6Gosaritsu the bin6Gosaritsu to set
     */
    public void setBin6Gosaritsu(FXHDD01 bin6Gosaritsu) {
        this.bin6Gosaritsu = bin6Gosaritsu;
    }

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     * @return the bin6MachineFuryoritsu
     */
    public FXHDD01 getBin6MachineFuryoritsu() {
        return bin6MachineFuryoritsu;
    }

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     * @param bin6MachineFuryoritsu the bin6MachineFuryoritsu to set
     */
    public void setBin6MachineFuryoritsu(FXHDD01 bin6MachineFuryoritsu) {
        this.bin6MachineFuryoritsu = bin6MachineFuryoritsu;
    }

    /**
     * BIN6 抜き取り結果
     * @return the bin6NukitorikekkaS
     */
    public FXHDD01 getBin6NukitorikekkaS() {
        return bin6NukitorikekkaS;
    }

    /**
     * BIN6 抜き取り結果
     * @param bin6NukitorikekkaS the bin6NukitorikekkaS to set
     */
    public void setBin6NukitorikekkaS(FXHDD01 bin6NukitorikekkaS) {
        this.bin6NukitorikekkaS = bin6NukitorikekkaS;
    }

    /**
     * BIN6 抜き取り結果
     * @return the bin6NukitorikekkaT
     */
    public FXHDD01 getBin6NukitorikekkaT() {
        return bin6NukitorikekkaT;
    }

    /**
     * BIN6 抜き取り結果
     * @param bin6NukitorikekkaT the bin6NukitorikekkaT to set
     */
    public void setBin6NukitorikekkaT(FXHDD01 bin6NukitorikekkaT) {
        this.bin6NukitorikekkaT = bin6NukitorikekkaT;
    }

    /**
     * BIN6 真の不良率(%)
     * @return the bin6ShinFuryoritsu
     */
    public FXHDD01 getBin6ShinFuryoritsu() {
        return bin6ShinFuryoritsu;
    }

    /**
     * BIN6 真の不良率(%)
     * @param bin6ShinFuryoritsu the bin6ShinFuryoritsu to set
     */
    public void setBin6ShinFuryoritsu(FXHDD01 bin6ShinFuryoritsu) {
        this.bin6ShinFuryoritsu = bin6ShinFuryoritsu;
    }

    /**
     * BIN6 結果ﾁｪｯｸ
     * @return the bin6KekkaCheck
     */
    public FXHDD01 getBin6KekkaCheck() {
        return bin6KekkaCheck;
    }

    /**
     * BIN6 結果ﾁｪｯｸ
     * @param bin6KekkaCheck the bin6KekkaCheck to set
     */
    public void setBin6KekkaCheck(FXHDD01 bin6KekkaCheck) {
        this.bin6KekkaCheck = bin6KekkaCheck;
    }

    /**
     * BIN6 袋ﾁｪｯｸ
     * @return the bin6FukuroCheck
     */
    public FXHDD01 getBin6FukuroCheck() {
        return bin6FukuroCheck;
    }

    /**
     * BIN6 袋ﾁｪｯｸ
     * @param bin6FukuroCheck the bin6FukuroCheck to set
     */
    public void setBin6FukuroCheck(FXHDD01 bin6FukuroCheck) {
        this.bin6FukuroCheck = bin6FukuroCheck;
    }

    /**
     * BIN7 %区分(設定値)
     * @return the bin7PercentKbn
     */
    public FXHDD01 getBin7PercentKbn() {
        return bin7PercentKbn;
    }

    /**
     * BIN7 %区分(設定値)
     * @param bin7PercentKbn the bin7PercentKbn to set
     */
    public void setBin7PercentKbn(FXHDD01 bin7PercentKbn) {
        this.bin7PercentKbn = bin7PercentKbn;
    }

    /**
     * BIN7 選別区分
     * @return the bin7SenbetsuKbn
     */
    public FXHDD01 getBin7SenbetsuKbn() {
        return bin7SenbetsuKbn;
    }

    /**
     * BIN7 選別区分
     * @param bin7SenbetsuKbn the bin7SenbetsuKbn to set
     */
    public void setBin7SenbetsuKbn(FXHDD01 bin7SenbetsuKbn) {
        this.bin7SenbetsuKbn = bin7SenbetsuKbn;
    }

    /**
     * BIN7 計量後数量
     * @return the bin7KeiryogoSuryo
     */
    public FXHDD01 getBin7KeiryogoSuryo() {
        return bin7KeiryogoSuryo;
    }

    /**
     * BIN7 計量後数量
     * @param bin7KeiryogoSuryo the bin7KeiryogoSuryo to set
     */
    public void setBin7KeiryogoSuryo(FXHDD01 bin7KeiryogoSuryo) {
        this.bin7KeiryogoSuryo = bin7KeiryogoSuryo;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @return the bin7CounterSu
     */
    public FXHDD01 getBin7CounterSu() {
        return bin7CounterSu;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @param bin7CounterSu the bin7CounterSu to set
     */
    public void setBin7CounterSu(FXHDD01 bin7CounterSu) {
        this.bin7CounterSu = bin7CounterSu;
    }

    /**
     * BIN7 誤差率(%)
     * @return the bin7Gosaritsu
     */
    public FXHDD01 getBin7Gosaritsu() {
        return bin7Gosaritsu;
    }

    /**
     * BIN7 誤差率(%)
     * @param bin7Gosaritsu the bin7Gosaritsu to set
     */
    public void setBin7Gosaritsu(FXHDD01 bin7Gosaritsu) {
        this.bin7Gosaritsu = bin7Gosaritsu;
    }

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     * @return the bin7MachineFuryoritsu
     */
    public FXHDD01 getBin7MachineFuryoritsu() {
        return bin7MachineFuryoritsu;
    }

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     * @param bin7MachineFuryoritsu the bin7MachineFuryoritsu to set
     */
    public void setBin7MachineFuryoritsu(FXHDD01 bin7MachineFuryoritsu) {
        this.bin7MachineFuryoritsu = bin7MachineFuryoritsu;
    }

    /**
     * BIN7 袋ﾁｪｯｸ
     * @return the bin7FukuroCheck
     */
    public FXHDD01 getBin7FukuroCheck() {
        return bin7FukuroCheck;
    }

    /**
     * BIN7 袋ﾁｪｯｸ
     * @param bin7FukuroCheck the bin7FukuroCheck to set
     */
    public void setBin7FukuroCheck(FXHDD01 bin7FukuroCheck) {
        this.bin7FukuroCheck = bin7FukuroCheck;
    }

    /**
     * BIN8 %区分(設定値)
     * @return the bin8PercentKbn
     */
    public FXHDD01 getBin8PercentKbn() {
        return bin8PercentKbn;
    }

    /**
     * BIN8 %区分(設定値)
     * @param bin8PercentKbn the bin8PercentKbn to set
     */
    public void setBin8PercentKbn(FXHDD01 bin8PercentKbn) {
        this.bin8PercentKbn = bin8PercentKbn;
    }

    /**
     * BIN8 選別区分
     * @return the bin8SenbetsuKbn
     */
    public FXHDD01 getBin8SenbetsuKbn() {
        return bin8SenbetsuKbn;
    }

    /**
     * BIN8 選別区分
     * @param bin8SenbetsuKbn the bin8SenbetsuKbn to set
     */
    public void setBin8SenbetsuKbn(FXHDD01 bin8SenbetsuKbn) {
        this.bin8SenbetsuKbn = bin8SenbetsuKbn;
    }

    /**
     * BIN8 計量後数量
     * @return the bin8KeiryogoSuryo
     */
    public FXHDD01 getBin8KeiryogoSuryo() {
        return bin8KeiryogoSuryo;
    }

    /**
     * BIN8 計量後数量
     * @param bin8KeiryogoSuryo the bin8KeiryogoSuryo to set
     */
    public void setBin8KeiryogoSuryo(FXHDD01 bin8KeiryogoSuryo) {
        this.bin8KeiryogoSuryo = bin8KeiryogoSuryo;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @return the bin8CounterSu
     */
    public FXHDD01 getBin8CounterSu() {
        return bin8CounterSu;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @param bin8CounterSu the bin8CounterSu to set
     */
    public void setBin8CounterSu(FXHDD01 bin8CounterSu) {
        this.bin8CounterSu = bin8CounterSu;
    }

    /**
     * BIN8 誤差率(%)
     * @return the bin8Gosaritsu
     */
    public FXHDD01 getBin8Gosaritsu() {
        return bin8Gosaritsu;
    }

    /**
     * BIN8 誤差率(%)
     * @param bin8Gosaritsu the bin8Gosaritsu to set
     */
    public void setBin8Gosaritsu(FXHDD01 bin8Gosaritsu) {
        this.bin8Gosaritsu = bin8Gosaritsu;
    }

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     * @return the bin8MachineFuryoritsu
     */
    public FXHDD01 getBin8MachineFuryoritsu() {
        return bin8MachineFuryoritsu;
    }

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     * @param bin8MachineFuryoritsu the bin8MachineFuryoritsu to set
     */
    public void setBin8MachineFuryoritsu(FXHDD01 bin8MachineFuryoritsu) {
        this.bin8MachineFuryoritsu = bin8MachineFuryoritsu;
    }

    /**
     * BIN8 袋ﾁｪｯｸ
     * @return the bin8FukuroCheck
     */
    public FXHDD01 getBin8FukuroCheck() {
        return bin8FukuroCheck;
    }

    /**
     * BIN8 袋ﾁｪｯｸ
     * @param bin8FukuroCheck the bin8FukuroCheck to set
     */
    public void setBin8FukuroCheck(FXHDD01 bin8FukuroCheck) {
        this.bin8FukuroCheck = bin8FukuroCheck;
    }

    /**
     * BIN9 強制排出 計量後数量
     * @return the bin9KKeiryogoSuryo
     */
    public FXHDD01 getBin9KKeiryogoSuryo() {
        return bin9KKeiryogoSuryo;
    }

    /**
     * BIN9 強制排出 計量後数量
     * @param bin9KKeiryogoSuryo the bin9KKeiryogoSuryo to set
     */
    public void setBin9KKeiryogoSuryo(FXHDD01 bin9KKeiryogoSuryo) {
        this.bin9KKeiryogoSuryo = bin9KKeiryogoSuryo;
    }

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     * @return the bin9KMachineFuryoritsu
     */
    public FXHDD01 getBin9KMachineFuryoritsu() {
        return bin9KMachineFuryoritsu;
    }

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     * @param bin9KMachineFuryoritsu the bin9KMachineFuryoritsu to set
     */
    public void setBin9KMachineFuryoritsu(FXHDD01 bin9KMachineFuryoritsu) {
        this.bin9KMachineFuryoritsu = bin9KMachineFuryoritsu;
    }

    /**
     * 落下 計量後数量
     * @return the rakkaKeiryogoSuryo
     */
    public FXHDD01 getRakkaKeiryogoSuryo() {
        return rakkaKeiryogoSuryo;
    }

    /**
     * 落下 計量後数量
     * @param rakkaKeiryogoSuryo the rakkaKeiryogoSuryo to set
     */
    public void setRakkaKeiryogoSuryo(FXHDD01 rakkaKeiryogoSuryo) {
        this.rakkaKeiryogoSuryo = rakkaKeiryogoSuryo;
    }

    /**
     * 落下 ﾏｼﾝ不良率
     * @return the rakkaMachineFuryoritsu
     */
    public FXHDD01 getRakkaMachineFuryoritsu() {
        return rakkaMachineFuryoritsu;
    }

    /**
     * 落下 ﾏｼﾝ不良率
     * @param rakkaMachineFuryoritsu the rakkaMachineFuryoritsu to set
     */
    public void setRakkaMachineFuryoritsu(FXHDD01 rakkaMachineFuryoritsu) {
        this.rakkaMachineFuryoritsu = rakkaMachineFuryoritsu;
    }

    /**
     * 半田ｻﾝﾌﾟﾙ
     * @return the handaSample
     */
    public FXHDD01 getHandaSample() {
        return handaSample;
    }

    /**
     * 半田ｻﾝﾌﾟﾙ
     * @param handaSample the handaSample to set
     */
    public void setHandaSample(FXHDD01 handaSample) {
        this.handaSample = handaSample;
    }

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     * @return the shinraiseiSample
     */
    public FXHDD01 getShinraiseiSample() {
        return shinraiseiSample;
    }

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     * @param shinraiseiSample the shinraiseiSample to set
     */
    public void setShinraiseiSample(FXHDD01 shinraiseiSample) {
        this.shinraiseiSample = shinraiseiSample;
    }

    /**
     * 真不良判定者
     * @return the shinFuryoHanteisha
     */
    public FXHDD01 getShinFuryoHanteisha() {
        return shinFuryoHanteisha;
    }

    /**
     * 真不良判定者
     * @param shinFuryoHanteisha the shinFuryoHanteisha to set
     */
    public void setShinFuryoHanteisha(FXHDD01 shinFuryoHanteisha) {
        this.shinFuryoHanteisha = shinFuryoHanteisha;
    }

    /**
     * 判定入力者
     * @return the hanteiNyuryokusha
     */
    public FXHDD01 getHanteiNyuryokusha() {
        return hanteiNyuryokusha;
    }

    /**
     * 判定入力者
     * @param hanteiNyuryokusha the hanteiNyuryokusha to set
     */
    public void setHanteiNyuryokusha(FXHDD01 hanteiNyuryokusha) {
        this.hanteiNyuryokusha = hanteiNyuryokusha;
    }

    /**
     * 取出者
     * @return the toridashisha
     */
    public FXHDD01 getToridashisha() {
        return toridashisha;
    }

    /**
     * 取出者
     * @param toridashisha the toridashisha to set
     */
    public void setToridashisha(FXHDD01 toridashisha) {
        this.toridashisha = toridashisha;
    }

    /**
     * 公差①
     * @return the kousa1
     */
    public FXHDD01 getKousa1() {
        return kousa1;
    }

    /**
     * 公差①
     * @param kousa1 the kousa1 to set
     */
    public void setKousa1(FXHDD01 kousa1) {
        this.kousa1 = kousa1;
    }

    /**
     * 重量①
     * @return the juryo1
     */
    public FXHDD01 getJuryo1() {
        return juryo1;
    }

    /**
     * 重量①
     * @param juryo1 the juryo1 to set
     */
    public void setJuryo1(FXHDD01 juryo1) {
        this.juryo1 = juryo1;
    }

    /**
     * 個数①
     * @return the kosu1
     */
    public FXHDD01 getKosu1() {
        return kosu1;
    }

    /**
     * 個数①
     * @param kosu1 the kosu1 to set
     */
    public void setKosu1(FXHDD01 kosu1) {
        this.kosu1 = kosu1;
    }

    /**
     * 公差②
     * @return the kousa2
     */
    public FXHDD01 getKousa2() {
        return kousa2;
    }

    /**
     * 公差②
     * @param kousa2 the kousa2 to set
     */
    public void setKousa2(FXHDD01 kousa2) {
        this.kousa2 = kousa2;
    }

    /**
     * 重量②
     * @return the juryo2
     */
    public FXHDD01 getJuryo2() {
        return juryo2;
    }

    /**
     * 重量②
     * @param juryo2 the juryo2 to set
     */
    public void setJuryo2(FXHDD01 juryo2) {
        this.juryo2 = juryo2;
    }

    /**
     * 個数②
     * @return the kosu2
     */
    public FXHDD01 getKosu2() {
        return kosu2;
    }

    /**
     * 個数②
     * @param kosu2 the kosu2 to set
     */
    public void setKosu2(FXHDD01 kosu2) {
        this.kosu2 = kosu2;
    }

    /**
     * 公差③
     * @return the kousa3
     */
    public FXHDD01 getKousa3() {
        return kousa3;
    }

    /**
     * 公差③
     * @param kousa3 the kousa3 to set
     */
    public void setKousa3(FXHDD01 kousa3) {
        this.kousa3 = kousa3;
    }

    /**
     * 重量③
     * @return the juryo3
     */
    public FXHDD01 getJuryo3() {
        return juryo3;
    }

    /**
     * 重量③
     * @param juryo3 the juryo3 to set
     */
    public void setJuryo3(FXHDD01 juryo3) {
        this.juryo3 = juryo3;
    }

    /**
     * 個数③
     * @return the kosu3
     */
    public FXHDD01 getKosu3() {
        return kosu3;
    }

    /**
     * 個数③
     * @param kosu3 the kosu3 to set
     */
    public void setKosu3(FXHDD01 kosu3) {
        this.kosu3 = kosu3;
    }

    /**
     * 公差④
     * @return the kousa4
     */
    public FXHDD01 getKousa4() {
        return kousa4;
    }

    /**
     * 公差④
     * @param kousa4 the kousa4 to set
     */
    public void setKousa4(FXHDD01 kousa4) {
        this.kousa4 = kousa4;
    }

    /**
     * 重量④
     * @return the juryo4
     */
    public FXHDD01 getJuryo4() {
        return juryo4;
    }

    /**
     * 重量④
     * @param juryo4 the juryo4 to set
     */
    public void setJuryo4(FXHDD01 juryo4) {
        this.juryo4 = juryo4;
    }

    /**
     * 個数④
     * @return the kosu4
     */
    public FXHDD01 getKosu4() {
        return kosu4;
    }

    /**
     * 個数④
     * @param kosu4 the kosu4 to set
     */
    public void setKosu4(FXHDD01 kosu4) {
        this.kosu4 = kosu4;
    }

    /**
     * ｶｳﾝﾀｰ総数
     * @return the counterSosu
     */
    public FXHDD01 getCounterSosu() {
        return counterSosu;
    }

    /**
     * ｶｳﾝﾀｰ総数
     * @param counterSosu the counterSosu to set
     */
    public void setCounterSosu(FXHDD01 counterSosu) {
        this.counterSosu = counterSosu;
    }

    /**
     * 良品重量
     * @return the ryouhinJuryo
     */
    public FXHDD01 getRyouhinJuryo() {
        return ryouhinJuryo;
    }

    /**
     * 良品重量
     * @param ryouhinJuryo the ryouhinJuryo to set
     */
    public void setRyouhinJuryo(FXHDD01 ryouhinJuryo) {
        this.ryouhinJuryo = ryouhinJuryo;
    }

    /**
     * 良品個数
     * @return the ryouhinKosu
     */
    public FXHDD01 getRyouhinKosu() {
        return ryouhinKosu;
    }

    /**
     * 良品個数
     * @param ryouhinKosu the ryouhinKosu to set
     */
    public void setRyouhinKosu(FXHDD01 ryouhinKosu) {
        this.ryouhinKosu = ryouhinKosu;
    }

    /**
     * 歩留まり
     * @return the budomari
     */
    public FXHDD01 getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     * @param budomari the budomari to set
     */
    public void setBudomari(FXHDD01 budomari) {
        this.budomari = budomari;
    }

    /**
     * 確認者
     * @return the kakuninsha
     */
    public FXHDD01 getKakuninsha() {
        return kakuninsha;
    }

    /**
     * 確認者
     * @param kakuninsha the kakuninsha to set
     */
    public void setKakuninsha(FXHDD01 kakuninsha) {
        this.kakuninsha = kakuninsha;
    }

}
