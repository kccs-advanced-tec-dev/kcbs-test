/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C009Model;

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
 * GXHDO101C009(合わせ(RZ))ロジッククラス
 */
public class GXHDO101C009Logic {

    public static GXHDO101C009Model createGXHDO101C009Model(String lotNo) {
        GXHDO101C009Model GXHDO101C009Model = new GXHDO101C009Model();
        List<GXHDO101C009Model.PrintWidthData> printWidthDataList = new ArrayList<>();

        // 合わせ(RZ)(1行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "1", "", "TEXT", "6", ""));
        // 合わせ(RZ)(2行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "2", "", "TEXT", "6", ""));
        // 合わせ(RZ)(3行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "3", "", "TEXT", "6", ""));
        // 合わせ(RZ)(4行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "4", "", "TEXT", "6", ""));
        // 合わせ(RZ)(5行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "5", "", "TEXT", "6", ""));
        // 合わせ(RZ)(6行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "6", "", "TEXT", "6", ""));
        // 合わせ(RZ)(7行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "7", "", "TEXT", "6", ""));
        // 合わせ(RZ)(8行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "8", "", "TEXT", "6", ""));
        // 合わせ(RZ)(9行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C009Model, "9", "", "TEXT", "6", ""));

        GXHDO101C009Model.setPrintWidthDataList(printWidthDataList);
        return GXHDO101C009Model;
    }

    /**
     * 合わせ(RZ)データ初期化データ取得
     *
     * @param GXHDO101C009Model 合わせ(RZ)画面モデル
     * @param printWidth 合わせ(RZ)
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 合わせ(RZ)データ
     */
    private static GXHDO101C009Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C009Model GXHDO101C009Model, String printWidth, String startVal, 
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C009Model.PrintWidthData printWidthListData = GXHDO101C009Model.new PrintWidthData();
        // 合わせ(RZ)
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
