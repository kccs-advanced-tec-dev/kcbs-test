/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C005Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C005(印刷幅)ロジッククラス
 */
public class GXHDO101C005Logic {

    public static GXHDO101C005Model createGXHDO101C005Model(String[] startValues) {

        GXHDO101C005Model model = new GXHDO101C005Model();
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = new ArrayList<>();

        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < startValues.length; i++) {
            printWidthDataList.add(getInitPrintWidhData(model, String.valueOf(i + 1), startValues[i], "TEXT", "4", ""));
        }

        model.setPrintWidthDataList(printWidthDataList);
        return model;

    }

    /**
     * 印刷幅データ初期化データ取得
     *
     * @param gxhdo101C005Model 印刷幅画面モデル
     * @param printWidth 印刷幅
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 印刷幅データ
     */
    private static GXHDO101C005Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C005Model gxhdo101C005Model, String printWidth, String startVal,
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C005Model.PrintWidthData printWidthListData = gxhdo101C005Model.new PrintWidthData();
        // 印刷幅
        printWidthListData.setPrintWidth(printWidth);
        // スタート
        printWidthListData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            printWidthListData.setStartTextRendered(true);
            printWidthListData.setStartLabelRendered(false);
        } else {
            printWidthListData.setStartTextRendered(false);
            printWidthListData.setStartLabelRendered(true);
        }
        printWidthListData.setStartTextMaxLength(startTextMaxLength);
        printWidthListData.setStartTextBackColor(startTextBackColor);

        return printWidthListData;
    }

    /**
     * 入力ﾁｪｯｸ
     *
     * @param gXHDO101C005Model 印刷幅サブ画面用ﾓﾃﾞﾙ
     * @return ｴﾗｰﾘｽﾄ
     */
    public static List<String> checkInput(GXHDO101C005Model gXHDO101C005Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = gXHDO101C005Model.getPrintWidthDataList();
        for (GXHDO101C005Model.PrintWidthData printWidthData : printWidthDataList) {
            if (StringUtil.isEmpty(printWidthData.getStartVal())) {
                printWidthData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "スタート"));
                return errorList;
            }
        }
        return errorList;
    }

}
