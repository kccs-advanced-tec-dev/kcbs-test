/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C006Model;

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
 * GXHDO101C006(剥離・画処NG)ロジッククラス
 */
public class GXHDO101C006Logic {

    public static GXHDO101C006Model createGXHDO101C006Model(String lotNo) {
        GXHDO101C006Model gxhdo101c006Model = new GXHDO101C006Model();
        List<GXHDO101C006Model.PtnKyoriXData> ptnKyoriXDataList = new ArrayList<>();

        // 剥離・画処NG(1行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "1", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(2行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "2", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(3行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "3", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(4行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "4", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(5行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "5", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(6行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "6", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(7行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "7", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(8行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "8", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(9行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "9", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(10行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "10", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(11行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "11", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(12行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "12", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(13行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "13", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "14", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "15", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "16", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "17", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "18", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "19", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "20", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "21", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "22", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "23", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "24", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "25", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "26", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "27", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "28", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "29", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "30", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "31", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "32", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "33", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "34", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "35", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "36", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "37", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "38", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "39", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "40", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "41", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "42", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "43", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "44", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "45", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "46", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "47", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "48", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "49", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "50", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "51", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "52", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "53", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "54", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "55", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "56", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "57", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "58", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "59", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "60", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "61", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "62", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "63", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "64", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "65", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "66", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "67", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "68", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "69", "", "TEXT", "3", "", "", "TEXT", "3", ""));
        // 剥離・画処NG(14行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c006Model, "70", "", "TEXT", "3", "", "", "TEXT", "3", ""));

        gxhdo101c006Model.setPtnKyoriXDataList(ptnKyoriXDataList);

        return gxhdo101c006Model;
    }

    /**
     * 剥離・画処NGデータ初期データ取得処理
     *
     * @param gxhdo101c006Model 剥離・画処NG画面モデル
     * @param ptnKyoriX 剥離・画処NG
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param endVal エンド項目(値)
     * @param endInputType エンド項目(入力タイプ)
     * @param endTextMaxLength　エンド項目(テキストMaxLength)
     * @param endTextBackColor　エンド項目(BackGround)
     * @return PTN距離データ
     */
    private static GXHDO101C006Model.PtnKyoriXData getInitPtnKyoriXData(
            GXHDO101C006Model gxhdo101c006Model, String ptnKyoriX,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C006Model.PtnKyoriXData ptnKyoriXData = gxhdo101c006Model.new PtnKyoriXData();
        //剥離・画処NG
        ptnKyoriXData.setPtnKyoriX(ptnKyoriX);
        // スタート
        ptnKyoriXData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriXData.setStartTextRendered(true);
            ptnKyoriXData.setStartLabelRendered(false);

        } else {
            ptnKyoriXData.setStartTextRendered(false);
            ptnKyoriXData.setStartLabelRendered(true);
        }
        ptnKyoriXData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriXData.setStartTextBackColor(startTextBackColor);

        // エンド
        ptnKyoriXData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriXData.setEndTextRendered(true);
            ptnKyoriXData.setEndLabelRendered(false);
        } else {
            ptnKyoriXData.setEndTextRendered(false);
            ptnKyoriXData.setEndLabelRendered(true);
        }
        ptnKyoriXData.setEndTextMaxLength(endTextMaxLength);
        ptnKyoriXData.setEndTextBackColor(endTextBackColor);

        return ptnKyoriXData;
    }
}
