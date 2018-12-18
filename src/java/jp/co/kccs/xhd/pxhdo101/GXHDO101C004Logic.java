/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C004Model;

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
 * GXHDO101C004(膜厚(RSUS))ロジッククラス
 */
public class GXHDO101C004Logic {

    public static GXHDO101C004Model createGXHDO101C004Model(String lotNo) {
        List<GXHDO101C004Model.MakuatsuData> makuatsuDataList = new ArrayList<>();
        GXHDO101C004Model model = new GXHDO101C004Model();

        // 膜厚(1行目)
        makuatsuDataList.add(getInitMakuatsuData(model, "1", "", "TEXT", "5", ""));
        // 膜厚(2行目)
        makuatsuDataList.add(getInitMakuatsuData(model, "2", "", "TEXT", "5", ""));
        // 膜厚(3行目)
        makuatsuDataList.add(getInitMakuatsuData(model, "3", "", "TEXT", "5", ""));
        // 膜厚(4行目)
        makuatsuDataList.add(getInitMakuatsuData(model, "4", "", "TEXT", "5", ""));
        // 膜厚(5行目)
        makuatsuDataList.add(getInitMakuatsuData(model, "5", "", "TEXT", "5", ""));
        model.setMakuatsuDataList(makuatsuDataList);

        return model;
    }

    /**
     * 膜厚(RSUS)データ初期化データ取得
     *
     * @param gxhdo101C004Model 膜厚(RSUS)画面モデル
     * @param makuatsu 膜厚
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 膜厚データ
     */
    private static GXHDO101C004Model.MakuatsuData getInitMakuatsuData(GXHDO101C004Model model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C004Model.MakuatsuData makuatsuData = model.new MakuatsuData();
        // 膜厚
        makuatsuData.setMakuatsu(makuatsu);

        // スタート
        makuatsuData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuData.setStartTextRendered(true);
            makuatsuData.setStartLabelRendered(false);
        } else {
            makuatsuData.setStartTextRendered(false);
            makuatsuData.setStartLabelRendered(true);
        }
        makuatsuData.setStartTextMaxLength(startTextMaxLength);
        makuatsuData.setStartTextBackColor(startTextBackColor);
        return makuatsuData;
    }

}
