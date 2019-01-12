/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;

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

    public static GXHDO101C001Model createGXHDO101C001Model(String[] makuatsuStart, String[] makuatsuEnd) {
        GXHDO101C001Model gxhdo101C001Model = new GXHDO101C001Model();
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = new ArrayList<>();

        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < makuatsuStart.length; i++) {
            makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, String.valueOf(i + 1), makuatsuStart[i], "TEXT", "6", "", makuatsuEnd[i], "TEXT", "6", ""));
        }

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
    
    /**
     * 入力ﾁｪｯｸ
     * @param gXHDO101C001Model 膜厚(SPS)サブ画面用ﾓﾃﾞﾙ
     * @return ｴﾗｰﾘｽﾄ
     */
    public static List<String> checkInput(GXHDO101C001Model gXHDO101C001Model) {
        
        List<String> errorList = new ArrayList<>();
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = gXHDO101C001Model.getMakuatsuDataList();
        for (GXHDO101C001Model.MakuatsuData makuatsuData : makuatsuDataList) {
            if (StringUtil.isEmpty(makuatsuData.getStartVal())) {
                makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("スタートが入力されていません。");
                return errorList;
            }
            
            if (StringUtil.isEmpty(makuatsuData.getEndVal())) {
                makuatsuData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("エンドが入力されていません。");
                return errorList;
            }
        }
        
        return errorList;
    }
}
