/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C007Model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C007(電極膜厚)ロジッククラス
 */
public class GXHDO101C007Logic {

    public static GXHDO101C007Model createGXHDO101C007Model(String lotNo) {
        GXHDO101C007Model GXHDO101C007Model = new GXHDO101C007Model();
        List<GXHDO101C007Model.PrintWidthData> printWidthDataList = new ArrayList<>();

        // 電極膜厚(1行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "1", "", "TEXT", "6", ""));
        // 電極膜厚(2行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "2", "", "TEXT", "6", ""));
        // 電極膜厚(3行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "3", "", "TEXT", "6", ""));
        // 電極膜厚(4行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "4", "", "TEXT", "6", ""));
        // 電極膜厚(5行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "5", "", "TEXT", "6", ""));
        // 電極膜厚(6行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "6", "", "TEXT", "6", ""));
        // 電極膜厚(7行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "7", "", "TEXT", "6", ""));
        // 電極膜厚(8行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "8", "", "TEXT", "6", ""));
        // 電極膜厚(9行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C007Model, "9", "", "TEXT", "6", ""));

        GXHDO101C007Model.setPrintWidthDataList(printWidthDataList);
        return GXHDO101C007Model;
    }

    /**
     * 電極膜厚データ初期化データ取得
     *
     * @param GXHDO101C007Model 電極膜厚画面モデル
     * @param printWidth 電極膜厚
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 電極膜厚データ
     */
    private static GXHDO101C007Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C007Model GXHDO101C007Model, String printWidth, String startVal, 
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C007Model.PrintWidthData printWidthListData = GXHDO101C007Model.new PrintWidthData();
        // 電極膜厚
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
}
