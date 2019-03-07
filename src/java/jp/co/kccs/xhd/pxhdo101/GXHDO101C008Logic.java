/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C008Model;

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
 * GXHDO101C008(ﾊﾟﾀｰﾝ間距離)ロジッククラス
 */
public class GXHDO101C008Logic {

    public static GXHDO101C008Model createGXHDO101C008Model(String lotNo) {
        GXHDO101C008Model GXHDO101C008Model = new GXHDO101C008Model();
        List<GXHDO101C008Model.PrintWidthData> printWidthDataList = new ArrayList<>();

        // 膜厚(1行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C008Model, "1", "", "TEXT", "4", ""));
        // 膜厚(2行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C008Model, "2", "", "TEXT", "4", ""));
        // 膜厚(3行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C008Model, "3", "", "TEXT", "4", ""));
        // 膜厚(4行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C008Model, "4", "", "TEXT", "4", ""));
        // 膜厚(5行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C008Model, "5", "", "TEXT", "4", ""));

        GXHDO101C008Model.setPrintWidthDataList(printWidthDataList);
        return GXHDO101C008Model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データ初期化データ取得
     *
     * @param GXHDO101C008Model ﾊﾟﾀｰﾝ間距離画面モデル
     * @param printWidth ﾊﾟﾀｰﾝ間距離
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return ﾊﾟﾀｰﾝ間距離データ
     */
    private static GXHDO101C008Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C008Model GXHDO101C008Model, String printWidth, String startVal, 
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C008Model.PrintWidthData printWidthListData = GXHDO101C008Model.new PrintWidthData();
        // ﾊﾟﾀｰﾝ間距離
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
