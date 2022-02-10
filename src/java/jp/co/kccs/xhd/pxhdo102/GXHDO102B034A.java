/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDM05;
import jp.co.kccs.xhd.model.GXHDO102B034Model;
import static jp.co.kccs.xhd.pxhdo102.GXHDO102B024A.getFormBean;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901BEX;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.QueryRunner;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/21<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B034A(ｽﾘｯﾌﾟ作製・高圧分散)
 *
 * @author KCSS K.Jo
 * @since 2021/12/21
 */
@Named
@ViewScoped
public class GXHDO102B034A extends GXHDO901BEX {

    private String mainDefaultStyle = "";
    private String mainAutoStyle = "";
    private String mainDivStyle = "";

    /**
     * コンストラクタ
     */
    public GXHDO102B034A() {
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
//</editor-fold>  

    /**
     * 画面起動時処理
     *
     * @param mainWidth 画面サイズ
     */
    public void init(String mainWidth) {

        this.setFormIds(new String[]{"GXHDO102B034A", "GXHDO102B034B"});

        //親の初期処理呼び出し
        super.init();

        this.mainDefaultStyle = "width:" + mainWidth + "px;margin-left:auto;margin-right:auto;";
        this.mainAutoStyle = "width:auto;" + "min-width:" + mainWidth + "px;";
        this.mainDivStyle = this.mainDefaultStyle;
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
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> listdata = bean.getPasslistdata();
        itemsList.addAll(this.itemList);
        for (int i = 0; i < listdata.size(); i++) {
            GXHDO102B034Model gxhdo102b034model = listdata.get(i);
            setGXHDO102B034ModelItemLabel1(gxhdo102b034model, i + 1);
            addItemListFromGXHDO102B034Model(gxhdo102b034model, itemsList);
        }
        ErrorMessageInfo requireCheckErrorMessage = validateUtil.executeValidation(itemRowCheckList, itemsList, queryRunnerWip);
        if (requireCheckErrorMessage != null && !StringUtil.isEmpty(requireCheckErrorMessage.getErrorMessage())) {
            if(requireCheckErrorMessage.getErrorMessage().contains("行目") ){
                // エラー項目の背景色を設定
                requireCheckErrorMessage.setPageChangeItemIndex(-1);
                int kaisuu = Integer.parseInt(requireCheckErrorMessage.getErrorMessage().substring(0, requireCheckErrorMessage.getErrorMessage().indexOf("行目"))) - 1;
                GXHDO102B034Model gxhdo102b034model = listdata.get(kaisuu);
                String itemId = requireCheckErrorMessage.getErrorItemInfoList().get(0).getItemId();
                FXHDD01 item = getGXHDO102B034ModelItem(itemId, gxhdo102b034model);
                if (item != null) {
                    item.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                }
            }
        }

        return requireCheckErrorMessage;
    }

    /**
     * モデルデータの項目のラベルを設定
     *
     * @param gxhdo102b034model モデルデータ
     * @param rowIndx 行番号
     */
    private void setGXHDO102B034ModelItemLabel1(GXHDO102B034Model gxhdo102b034model, int rowIndx) {

        String passLabel1 = StringUtil.nullToBlank(gxhdo102b034model.getPass().getLabel1());
        if (!passLabel1.contains("行目: ")) {
            gxhdo102b034model.getPass().setLabel1(rowIndx + "行目: " + passLabel1);
            gxhdo102b034model.getSouekigawatankno().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getSouekigawatankno().getLabel1());
            gxhdo102b034model.getHaisyutsugawatankno().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getHaisyutsugawatankno().getLabel1());
            gxhdo102b034model.getSetteiatsuryoku().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getSetteiatsuryoku().getLabel1());
            gxhdo102b034model.getKaishityokugohaikiryou().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKaishityokugohaikiryou().getLabel1());
            gxhdo102b034model.getKouatsubunsankaishi_day().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsankaishi_day().getLabel1());
            gxhdo102b034model.getKouatsubunsankaishi_time().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsankaishi_time().getLabel1());
            gxhdo102b034model.getHaikikakunin().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getHaikikakunin().getLabel1());
            gxhdo102b034model.getJitsuatsuryoku().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getJitsuatsuryoku().getLabel1());
            gxhdo102b034model.getSlipryuuryou().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getSlipryuuryou().getLabel1());
            gxhdo102b034model.getSlipondoin().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getSlipondoin().getLabel1());
            gxhdo102b034model.getSlipondoout().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getSlipondoout().getLabel1());
            gxhdo102b034model.getKouatsubunsankaishitantousya().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsankaishitantousya().getLabel1());
            gxhdo102b034model.getKouatsubunsansyuuryou_day().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsansyuuryou_day().getLabel1());
            gxhdo102b034model.getKouatsubunsansyuuryou_time().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsansyuuryou_time().getLabel1());
            gxhdo102b034model.getKouatsubunsanteishitantousya().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getKouatsubunsanteishitantousya().getLabel1());
            gxhdo102b034model.getBikou1().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getBikou1().getLabel1());
            gxhdo102b034model.getBikou2().setLabel1(rowIndx + "行目: " + gxhdo102b034model.getBikou2().getLabel1());
        }
    }

    /**
     * 共通ﾁｪｯｸ必要の項目リストを取得
     *
     * @param gxhdo102b034model モデルデータ
     * @param itemsList 項目リスト
     */
    private void addItemListFromGXHDO102B034Model(GXHDO102B034Model gxhdo102b034model, List<FXHDD01> itemsList) {
        itemsList.add(gxhdo102b034model.getPass());
        itemsList.add(gxhdo102b034model.getSouekigawatankno());
        itemsList.add(gxhdo102b034model.getHaisyutsugawatankno());
        itemsList.add(gxhdo102b034model.getSetteiatsuryoku());
        itemsList.add(gxhdo102b034model.getKaishityokugohaikiryou());
        itemsList.add(gxhdo102b034model.getKouatsubunsankaishi_day());
        itemsList.add(gxhdo102b034model.getKouatsubunsankaishi_time());
        itemsList.add(gxhdo102b034model.getHaikikakunin());
        itemsList.add(gxhdo102b034model.getJitsuatsuryoku());
        itemsList.add(gxhdo102b034model.getSlipryuuryou());
        itemsList.add(gxhdo102b034model.getSlipondoin());
        itemsList.add(gxhdo102b034model.getSlipondoout());
        itemsList.add(gxhdo102b034model.getKouatsubunsankaishitantousya());
        itemsList.add(gxhdo102b034model.getKouatsubunsansyuuryou_day());
        itemsList.add(gxhdo102b034model.getKouatsubunsansyuuryou_time());
        itemsList.add(gxhdo102b034model.getKouatsubunsanteishitantousya());
        itemsList.add(gxhdo102b034model.getBikou1());
        itemsList.add(gxhdo102b034model.getBikou2());
    }

    /**
     * モデルから項目データを取得する。
     *
     * @param itemId 項目ID
     * @param gxhdo102b034model モデルデータ
     * @return 項目データ
     */
    private FXHDD01 getGXHDO102B034ModelItem(String itemId, GXHDO102B034Model gxhdo102b034model) {
        switch (itemId) {
            // ﾊﾟｽ回数
            case GXHDO102B034Const.PASS:
                return gxhdo102b034model.getPass();
            // 送液側ﾀﾝｸNo.
            case GXHDO102B034Const.SOUEKIGAWATANKNO:
                return gxhdo102b034model.getSouekigawatankno();
            // 排出側ﾀﾝｸNo.
            case GXHDO102B034Const.HAISYUTSUGAWATANKNO:
                return gxhdo102b034model.getHaisyutsugawatankno();
            // 設定圧力
            case GXHDO102B034Const.SETTEIATSURYOKU:
                return gxhdo102b034model.getSetteiatsuryoku();
            // 開始直後排気量
            case GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU:
                return gxhdo102b034model.getKaishityokugohaikiryou();
            // 高圧分散開始日
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY:
                return gxhdo102b034model.getKouatsubunsankaishi_day();
            // 高圧分散開始時間
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME:
                return gxhdo102b034model.getKouatsubunsankaishi_time();
            // 廃棄確認
            case GXHDO102B034Const.HAIKIKAKUNIN:
                return gxhdo102b034model.getHaikikakunin();
            // 実圧力(最大値)
            case GXHDO102B034Const.JITSUATSURYOKU:
                return gxhdo102b034model.getJitsuatsuryoku();
            // ｽﾘｯﾌﾟ流量
            case GXHDO102B034Const.SLIPRYUURYOU:
                return gxhdo102b034model.getSlipryuuryou();
            // ｽﾘｯﾌﾟ温度(IN)
            case GXHDO102B034Const.SLIPONDOIN:
                return gxhdo102b034model.getSlipondoin();
            // ｽﾘｯﾌﾟ温度(OUT)
            case GXHDO102B034Const.SLIPONDOOUT:
                return gxhdo102b034model.getSlipondoout();
            // 高圧分散開始担当者
            case GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA:
                return gxhdo102b034model.getKouatsubunsankaishitantousya();
            // 高圧分散終了日
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY:
                return gxhdo102b034model.getKouatsubunsansyuuryou_day();
            // 高圧分散終了時間
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME:
                return gxhdo102b034model.getKouatsubunsansyuuryou_time();
            // 高圧分散停止担当者
            case GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA:
                return gxhdo102b034model.getKouatsubunsanteishitantousya();
            // 備考1
            case GXHDO102B034Const.BIKOU1:
                return gxhdo102b034model.getBikou1();
            // 備考2
            case GXHDO102B034Const.BIKOU2:
                return gxhdo102b034model.getBikou2();
        }
        return null;
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
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> listdata = bean.getPasslistdata();
        // 背景色を戻さない特定の処理を除き背景色をデフォルトの背景色に戻す。
        listdata.stream().forEach(gxhdo102b034model -> {
            gxhdo102b034model.getPass().setBackColorInput(gxhdo102b034model.getPass().getBackColorInputDefault());
            gxhdo102b034model.getSouekigawatankno().setBackColorInput(gxhdo102b034model.getSouekigawatankno().getBackColorInputDefault());
            gxhdo102b034model.getHaisyutsugawatankno().setBackColorInput(gxhdo102b034model.getHaisyutsugawatankno().getBackColorInputDefault());
            gxhdo102b034model.getSetteiatsuryoku().setBackColorInput(gxhdo102b034model.getSetteiatsuryoku().getBackColorInputDefault());
            gxhdo102b034model.getKaishityokugohaikiryou().setBackColorInput(gxhdo102b034model.getKaishityokugohaikiryou().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsankaishi_day().setBackColorInput(gxhdo102b034model.getKouatsubunsankaishi_day().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsankaishi_time().setBackColorInput(gxhdo102b034model.getKouatsubunsankaishi_time().getBackColorInputDefault());
            gxhdo102b034model.getHaikikakunin().setBackColorInput(gxhdo102b034model.getHaikikakunin().getBackColorInputDefault());
            gxhdo102b034model.getJitsuatsuryoku().setBackColorInput(gxhdo102b034model.getJitsuatsuryoku().getBackColorInputDefault());
            gxhdo102b034model.getSlipryuuryou().setBackColorInput(gxhdo102b034model.getSlipryuuryou().getBackColorInputDefault());
            gxhdo102b034model.getSlipondoin().setBackColorInput(gxhdo102b034model.getSlipondoin().getBackColorInputDefault());
            gxhdo102b034model.getSlipondoout().setBackColorInput(gxhdo102b034model.getSlipondoout().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsankaishitantousya().setBackColorInput(gxhdo102b034model.getKouatsubunsankaishitantousya().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsansyuuryou_day().setBackColorInput(gxhdo102b034model.getKouatsubunsansyuuryou_day().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsansyuuryou_time().setBackColorInput(gxhdo102b034model.getKouatsubunsansyuuryou_time().getBackColorInputDefault());
            gxhdo102b034model.getKouatsubunsanteishitantousya().setBackColorInput(gxhdo102b034model.getKouatsubunsanteishitantousya().getBackColorInputDefault());
            gxhdo102b034model.getBikou1().setBackColorInput(gxhdo102b034model.getBikou1().getBackColorInputDefault());
            gxhdo102b034model.getBikou2().setBackColorInput(gxhdo102b034model.getBikou2().getBackColorInputDefault());
        });
    }
}
