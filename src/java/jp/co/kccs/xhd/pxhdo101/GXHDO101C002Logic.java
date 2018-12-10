/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C002Model;

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
 * GXHDO101C002(PTN距離X)ロジッククラス
 */
public class GXHDO101C002Logic {

    public static GXHDO101C002Model createGXHDO101C002Model(String lotNo) {
        GXHDO101C002Model gxhdo101c002Model = new GXHDO101C002Model();
        List<GXHDO101C002Model.ptnKyoriXData> ptnKyoriXDataList = new ArrayList<>();

        // PTN距離X(1行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, "1", "", "TEXT", "14", "", "", "TEXT", "2", ""));
        // PTN距離X(2行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, "2", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(3行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, "3", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(4行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, "4", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(5行目)
        ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, "5", "", "TEXT", "11", "", "", "TEXT", "2", ""));

        gxhdo101c002Model.setPtnKyoriXDataList(ptnKyoriXDataList);

        return gxhdo101c002Model;
    }

    /**
     * PTN距離Xデータ初期データ取得処理
     *
     * @param gxhdo101c002Model PTN距離X画面モデル
     * @param ptnKyoriX PTN距離X
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
    private static GXHDO101C002Model.ptnKyoriXData getInitPtnKyoriXData(
            GXHDO101C002Model gxhdo101c002Model, String ptnKyoriX,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C002Model.ptnKyoriXData ptnKyoriXData = gxhdo101c002Model.new ptnKyoriXData();
        //PTN距離X
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
