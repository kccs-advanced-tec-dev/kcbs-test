/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
import jp.co.kccs.xhd.util.ErrUtil;
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
 * GXHDO101C003(PTN距離Y)ロジッククラス
 */
public class GXHDO101C003Logic {

    public static GXHDO101C003Model createGXHDO101C003Model(String[] ptnKyoriYStart, String[] ptnKyoriYEnd) {
        GXHDO101C003Model gxhdo101c003Model = new GXHDO101C003Model();
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = new ArrayList<>();

        //PTN距離Yのデータをセットする
        for (int i = 0; i < ptnKyoriYStart.length; i++) {
            ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, String.valueOf(i + 1), ptnKyoriYStart[i], "TEXT", "3", "", ptnKyoriYEnd[i], "TEXT", "3", ""));
        }
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
    private static GXHDO101C003Model.PtnKyoriYData getInitPtnKyoriYData(
            GXHDO101C003Model gxhdo101c003Model, String ptnKyoriY,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C003Model.PtnKyoriYData ptnKyoriYData = gxhdo101c003Model.new PtnKyoriYData();
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
    
    /**
     * 入力ﾁｪｯｸ
     * @param gXHDO101C003Model PTN距離Yサブ画面用ﾓﾃﾞﾙ
     * @return ｴﾗｰﾘｽﾄ
     */
    public static List<String> checkInput(GXHDO101C003Model gXHDO101C003Model) {
        
        List<String> errorList = new ArrayList<>();
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = gXHDO101C003Model.getPtnKyoriYDataList();
        for (GXHDO101C003Model.PtnKyoriYData ptnKyoriY : ptnKyoriYDataList) {
            if (StringUtil.isEmpty(ptnKyoriY.getStartVal())) {
                ptnKyoriY.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("スタートが入力されていません。");
                return errorList;
            }
            
            if (StringUtil.isEmpty(ptnKyoriY.getEndVal())) {
                ptnKyoriY.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("エンドが入力されていません。");
                return errorList;
            }
        }
        
        return errorList;
    }
}
