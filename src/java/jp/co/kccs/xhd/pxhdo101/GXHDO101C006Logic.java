/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO101C006Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/03/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C006Logic(剥離内容入力)ロジッククラス
 */
public class GXHDO101C006Logic {

    /**
     * 剥離内容入力画面のモデルデータを作成する
     *
     * @param setsuuInput ｾｯﾄ数入力
     * @param bikou 備考
     * @return モデルデータ
     */
    public static GXHDO101C006Model createGXHDO101C006Model(String[] setsuuInput, String[] bikou) {
        GXHDO101C006Model gxhdo101c006Model = new GXHDO101C006Model();
        List<GXHDO101C006Model.HakuriInputData> hakuriInputDataList = new ArrayList<>();

        int setsuuIndex = 1;
        int bikouIndex = 2;
        
        //剥離内容入力のデータをセットする
        for (int i = 0; i < setsuuInput.length; i++) {
            hakuriInputDataList.add(getInitHakuriInputData(gxhdo101c006Model, String.valueOf(i + 1), setsuuInput[i], "TEXT", "4", "", String.valueOf(setsuuIndex), bikou[i], "TEXT", "20", "", String.valueOf(bikouIndex)));
            setsuuIndex += 2;
            bikouIndex += 2;
        }

        gxhdo101c006Model.setHakuriInputDataList(hakuriInputDataList);

        return gxhdo101c006Model;
    }

    /**
     * 剥離内容入力データ初期データ取得処理
     *
     * @param gxhdo101c006Model 剥離内容入力画面モデル
     * @param hakuriInput 剥離内容入力
     * @param setsuuVal ｾｯﾄ数入力項目(値)
     * @param setsuuInputType　ｾｯﾄ数入力項目(入力タイプ)
     * @param setsuuTextMaxLength　ｾｯﾄ数入力項目(テキストMaxLength)
     * @param setsuuTextBackColor　ｾｯﾄ数入力項目(BackGround)
     * @param setsuuTabIndex　ｾｯﾄ数入力項目(TabIndex)
     * @param bikouVal 備考項目(値)
     * @param bikouInputType 備考項目(入力タイプ)
     * @param bikouTextMaxLength　備考項目(テキストMaxLength)
     * @param bikouTextBackColor　備考項目(BackGround)
     * @param bikouTabIndex　備考項目(TabIndex)
     * @return 剥離内容入力データ
     */
    private static GXHDO101C006Model.HakuriInputData getInitHakuriInputData(
            GXHDO101C006Model gxhdo101c006Model, String hakuriInput,
            String setsuuVal, String setsuuInputType, String setsuuTextMaxLength, String setsuuTextBackColor, String setsuuTabIndex,
            String bikouVal, String bikouInputType, String bikouTextMaxLength, String bikouTextBackColor, String bikouTabIndex) {
        GXHDO101C006Model.HakuriInputData hakuriInputData = gxhdo101c006Model.new HakuriInputData();
        //剥離内容入力
        hakuriInputData.setHakuriInput(hakuriInput);
        // ｾｯﾄ数入力
        hakuriInputData.setSetsuuVal(setsuuVal);
        if ("TEXT".equals(setsuuInputType)) {
            hakuriInputData.setSetsuuTextRendered(true);
            hakuriInputData.setSetsuuLabelRendered(false);

        } else {
            hakuriInputData.setSetsuuTextRendered(false);
            hakuriInputData.setSetsuuLabelRendered(true);
        }
        hakuriInputData.setSetsuuTextMaxLength(setsuuTextMaxLength);
        hakuriInputData.setSetsuuTextBackColor(setsuuTextBackColor);
        hakuriInputData.setSetsuuTabIndex(setsuuTabIndex);

        // 備考
        hakuriInputData.setBikouVal(bikouVal);
        if ("TEXT".equals(bikouInputType)) {
            hakuriInputData.setBikouTextRendered(true);
            hakuriInputData.setBikouLabelRendered(false);
        } else {
            hakuriInputData.setBikouTextRendered(false);
            hakuriInputData.setBikouLabelRendered(true);
        }
        hakuriInputData.setBikouTextMaxLength(bikouTextMaxLength);
        hakuriInputData.setBikouTextBackColor(bikouTextBackColor);
        hakuriInputData.setBikouTabIndex(bikouTabIndex);

        return hakuriInputData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C006Model 剥離内容入力サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C006Model gXHDO101C006Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C006Model.HakuriInputData> hakuriInputDataList = gXHDO101C006Model.getHakuriInputDataList();
        for (GXHDO101C006Model.HakuriInputData hakuriInput : hakuriInputDataList) {
            if (StringUtil.isEmpty(hakuriInput.getSetsuuVal())) {
                hakuriInput.setSetsuuTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "ｾｯﾄ数入力"));
                return errorList;
            }

            if (StringUtil.isEmpty(hakuriInput.getBikouVal())) {
                hakuriInput.setBikouTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "備考"));
                return errorList;
            }
        }

        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C006Model 剥離内容入力サブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C006Model gXHDO101C006Model, List<FXHDD01> itemList) {

        List<String> setsuuDataList = new ArrayList<>();
        List<String> bikouDataList = new ArrayList<>();
        for (GXHDO101C006Model.HakuriInputData hakuriInputdata : gXHDO101C006Model.getHakuriInputDataList()) {
            if (!StringUtil.isEmpty(hakuriInputdata.getSetsuuVal())) {
                setsuuDataList.add(hakuriInputdata.getSetsuuVal());
            }

            if (!StringUtil.isEmpty(hakuriInputdata.getBikouVal())) {
                bikouDataList.add(hakuriInputdata.getBikouVal());
            }
        }

    }

    /**
     * 項目データ取得
     *
     * @param listData 項目データリスト
     * @param itemId 項目ID
     * @return 項目データ
     */
    private static FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

}
