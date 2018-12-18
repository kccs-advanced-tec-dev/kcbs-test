/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C001Model;

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
 * GXHDO101C001(膜厚(SPS))ロジッククラス
 */
public class GXHDO101C001Logic implements Serializable {

    public static GXHDO101C001Model createGXHDO101C001Model(String lotNo) {
        GXHDO101C001Model gxhdo101C001Model = new GXHDO101C001Model();
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = new ArrayList<>();

        // 膜厚(1行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "1", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(2行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "2", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(3行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "3", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(4行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "4", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(5行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "5", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(6行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "6", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(7行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "7", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(8行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "8", "", "TEXT", "2", "", "", "TEXT", "2", ""));
        // 膜厚(9行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "9", "", "TEXT", "2", "", "", "TEXT", "2", ""));

        gxhdo101C001Model.setMakuatsuDataList(makuatsuDataList);
        return gxhdo101C001Model;
    }
    
  

    /**
     * 膜厚データ初期化データ取得
     *
     * @param gxhdo101C001Model 膜厚(SPS)画面モデル
     * @param makuatsu 膜厚
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param endVal エンド項目(値)
     * @param endInputType エンド項目(入力タイプ)
     * @param endTextMaxLength　エンド項目(テキストMaxLength)
     * @param endTextBackColor　スタート項目(BackGround)
     * @return 膜厚データ
     */
    private static GXHDO101C001Model.MakuatsuData getInitMakuatsuData(
            GXHDO101C001Model gxhdo101C001Model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C001Model.MakuatsuData makuatsuListData = gxhdo101C001Model.new MakuatsuData();
        // 膜厚
        makuatsuListData.setMakuatsu(makuatsu);

        // スタート
        makuatsuListData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuListData.setStartTextRendered(true);
            makuatsuListData.setStartLabelRendered(false);
        } else {
            makuatsuListData.setStartTextRendered(false);
            makuatsuListData.setStartLabelRendered(true);
        }
        makuatsuListData.setStartTextMaxLength(startTextMaxLength);
        makuatsuListData.setStartTextBackColor(startTextBackColor);

        // エンド
        makuatsuListData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            makuatsuListData.setEndTextRendered(true);
            makuatsuListData.setEndLabelRendered(false);
        } else {
            makuatsuListData.setEndTextRendered(false);
            makuatsuListData.setEndLabelRendered(true);
        }
        makuatsuListData.setEndTextMaxLength(endTextMaxLength);
        makuatsuListData.setEndTextBackColor(endTextBackColor);
        return makuatsuListData;
    }
}
