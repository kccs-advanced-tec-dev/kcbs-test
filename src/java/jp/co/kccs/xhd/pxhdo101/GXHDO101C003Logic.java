/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.GXHDO101C002Model;
import jp.co.kccs.xhd.db.model.GXHDO101C003Model;

/**
 *
 * @author koksk-desk2
 */
public class GXHDO101C003Logic {

    public static GXHDO101C003Model createGXHDO101C003Model(String lotNo) {
        GXHDO101C003Model gxhdo101c003Model = new GXHDO101C003Model();
        List<GXHDO101C003Model.ptnKyoriYData> ptnKyoriYDataList = new ArrayList<>();

        // PTN距離X(1行目)
        ptnKyoriYDataList.add(getInitModel(gxhdo101c003Model, "1", "", "TEXT", "14", "", "TEXT", "2"));
        // PTN距離X(2行目)
        ptnKyoriYDataList.add(getInitModel(gxhdo101c003Model, "2", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(3行目)
        ptnKyoriYDataList.add(getInitModel(gxhdo101c003Model, "3", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(4行目)
        ptnKyoriYDataList.add(getInitModel(gxhdo101c003Model, "4", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(5行目)
        ptnKyoriYDataList.add(getInitModel(gxhdo101c003Model, "5", "", "TEXT", "11", "", "TEXT", "2"));

        gxhdo101c003Model.setPtnKyoriYDataList(ptnKyoriYDataList);

        return gxhdo101c003Model;
    }

    private static GXHDO101C003Model.ptnKyoriYData getInitModel(
            GXHDO101C003Model gxhdo101c003Model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength,
            String endVal, String endInputType, String endTextMaxLength) {
        GXHDO101C003Model.ptnKyoriYData ptnKyoriYData = gxhdo101c003Model.new ptnKyoriYData();
        ptnKyoriYData.setPtnKyoriY(makuatsu);
        ptnKyoriYData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriYData.setStartTextRendered(true);
            ptnKyoriYData.setStartLabelRendered(false);

        } else {
            ptnKyoriYData.setStartTextRendered(false);
            ptnKyoriYData.setStartLabelRendered(true);
        }
        ptnKyoriYData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriYData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriYData.setEndTextRendered(true);
            ptnKyoriYData.setEndLabelRendered(false);

        } else {
            ptnKyoriYData.setEndTextRendered(false);
            ptnKyoriYData.setEndLabelRendered(true);
        }
        ptnKyoriYData.setEndTextMaxLength(endTextMaxLength);
        return ptnKyoriYData;
    }
}
