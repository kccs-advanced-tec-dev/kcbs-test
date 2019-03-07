/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C010Model;

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
 * GXHDO101C010(被り量（μｍ）)ロジッククラス
 */
public class GXHDO101C010Logic {

    public static GXHDO101C010Model createGXHDO101C010Model(String lotNo) {
        GXHDO101C010Model GXHDO101C010Model = new GXHDO101C010Model();
        List<GXHDO101C010Model.PrintWidthData> printWidthDataList = new ArrayList<>();

        // 被り量（μｍ）(1行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "1", "", "TEXT", "6", ""));
        // 被り量（μｍ）(2行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "2", "", "TEXT", "6", ""));
        // 被り量（μｍ）(3行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "3", "", "TEXT", "6", ""));
        // 被り量（μｍ）(4行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "4", "", "TEXT", "6", ""));
        // 被り量（μｍ）(5行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "5", "", "TEXT", "6", ""));
        // 被り量（μｍ）(6行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "6", "", "TEXT", "6", ""));
        // 被り量（μｍ）(7行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "7", "", "TEXT", "6", ""));
        // 被り量（μｍ）(8行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "8", "", "TEXT", "6", ""));
        // 被り量（μｍ）(9行目)
        printWidthDataList.add(getInitPrintWidhData(GXHDO101C010Model, "9", "", "TEXT", "6", ""));

        GXHDO101C010Model.setPrintWidthDataList(printWidthDataList);
        return GXHDO101C010Model;
    }

    /**
     * 被り量（μｍ）データ初期化データ取得
     *
     * @param GXHDO101C010Model 被り量（μｍ）画面モデル
     * @param printWidth 被り量（μｍ）
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 被り量（μｍ）データ
     */
    private static GXHDO101C010Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C010Model GXHDO101C010Model, String printWidth, String startVal, 
            String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C010Model.PrintWidthData printWidthListData = GXHDO101C010Model.new PrintWidthData();
        // 被り量（μｍ）
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
