/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C005Model;

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

    public static GXHDO101C005Model createGXHDO101C005Model(String lotNo) {
        GXHDO101C005Model GXHDO101C005Model = new GXHDO101C005Model();
        List<GXHDO101C005Model.printWidthData> printWidthDataList = new ArrayList<>();

        // 膜厚(1行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C005Model, "1", "", "TEXT", "5", ""));
        // 膜厚(2行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C005Model, "2", "", "TEXT", "5", ""));
        // 膜厚(3行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C005Model, "3", "", "TEXT", "5", ""));
        // 膜厚(4行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C005Model, "4", "", "TEXT", "5", ""));
        // 膜厚(5行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C005Model, "5", "", "TEXT", "5", ""));

        GXHDO101C005Model.setPrintWidthDataList(printWidthDataList);
        return GXHDO101C005Model;
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
    private static GXHDO101C005Model.printWidthData getInitPrintWidhData(
            GXHDO101C005Model gxhdo101C005Model, String printWidth, String startVal, 
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C005Model.printWidthData printWidthListData = gxhdo101C005Model.new printWidthData();
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
}
