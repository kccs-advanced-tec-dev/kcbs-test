/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C003Model;

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
 * GXHDO101C003(PTN距離Y)ロジッククラス
 */
public class GXHDO101C003Logic {

    public static GXHDO101C003Model createGXHDO101C003Model(String lotNo) {
        GXHDO101C003Model gxhdo101c003Model = new GXHDO101C003Model();
        List<GXHDO101C003Model.ptnKyoriYData> ptnKyoriYDataList = new ArrayList<>();

        // PTN距離X(1行目)
        ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, "1", "", "TEXT", "14", "", "", "TEXT", "2", ""));
        // PTN距離X(2行目)
        ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, "2", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(3行目)
        ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, "3", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(4行目)
        ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, "4", "", "TEXT", "11", "", "", "TEXT", "2", ""));
        // PTN距離X(5行目)
        ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, "5", "", "TEXT", "11", "", "", "TEXT", "2", ""));

        gxhdo101c003Model.setPtnKyoriYDataList(ptnKyoriYDataList);

        return gxhdo101c003Model;
    }

    /**
     * PTN距離Yデータ初期データ取得処理
     *
     * @param gxhdo101c003Model PTN距離X画面モデル
     * @param ptnKyoriY PTN距離Y
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
    private static GXHDO101C003Model.ptnKyoriYData getInitPtnKyoriYData(
            GXHDO101C003Model gxhdo101c003Model, String ptnKyoriY,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C003Model.ptnKyoriYData ptnKyoriYData = gxhdo101c003Model.new ptnKyoriYData();
        // PTN距離Y
        ptnKyoriYData.setPtnKyoriY(ptnKyoriY);
        // スタート
        ptnKyoriYData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriYData.setStartTextRendered(true);
            ptnKyoriYData.setStartLabelRendered(false);
        } else {
            ptnKyoriYData.setStartTextRendered(false);
            ptnKyoriYData.setStartLabelRendered(true);
        }
        ptnKyoriYData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriYData.setStartTextBackColor(startTextBackColor);

        // エンド
        ptnKyoriYData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriYData.setEndTextRendered(true);
            ptnKyoriYData.setEndLabelRendered(false);

        } else {
            ptnKyoriYData.setEndTextRendered(false);
            ptnKyoriYData.setEndLabelRendered(true);
        }
        ptnKyoriYData.setEndTextMaxLength(endTextMaxLength);
        ptnKyoriYData.setEndTextBackColor(endTextBackColor);
        return ptnKyoriYData;
    }
}
