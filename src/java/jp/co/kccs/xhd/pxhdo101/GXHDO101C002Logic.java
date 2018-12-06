/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.GXHDO101C002Model;

/**
 *
 * @author koksk-desk2
 */
public class GXHDO101C002Logic {
    
    public static GXHDO101C002Model createGXHDO101C002Model(String lotNo) {
        GXHDO101C002Model gxhdo101c002Model = new GXHDO101C002Model();
        List<GXHDO101C002Model.ptnKyoriXData> ptnKyoriXDataList = new ArrayList<>();

        // PTN距離X(1行目)
        ptnKyoriXDataList.add(getInitModel(gxhdo101c002Model, "1", "", "TEXT", "14", "", "TEXT", "2"));
        // PTN距離X(2行目)
        ptnKyoriXDataList.add(getInitModel(gxhdo101c002Model, "2", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(3行目)
        ptnKyoriXDataList.add(getInitModel(gxhdo101c002Model, "3", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(4行目)
        ptnKyoriXDataList.add(getInitModel(gxhdo101c002Model, "4", "", "TEXT", "11", "", "TEXT", "2"));
        // PTN距離X(5行目)
        ptnKyoriXDataList.add(getInitModel(gxhdo101c002Model, "5", "", "TEXT", "11", "", "TEXT", "2"));
        
        gxhdo101c002Model.setPtnKyoriXDataList(ptnKyoriXDataList);
        
        return gxhdo101c002Model;
    }
    
    private static GXHDO101C002Model.ptnKyoriXData getInitModel(
            GXHDO101C002Model gxhdo101c002Model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength,
            String endVal, String endInputType, String endTextMaxLength) {
        GXHDO101C002Model.ptnKyoriXData ptnKyoriXData = gxhdo101c002Model.new ptnKyoriXData();
        ptnKyoriXData.setPtnKyoriX(makuatsu); //
        ptnKyoriXData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriXData.setStartTextRendered(true);
            ptnKyoriXData.setStartLabelRendered(false);
            
        } else {
            ptnKyoriXData.setStartTextRendered(false);
            ptnKyoriXData.setStartLabelRendered(true);
        }
        ptnKyoriXData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriXData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriXData.setEndTextRendered(true);
            ptnKyoriXData.setEndLabelRendered(false);
            
        } else {
            ptnKyoriXData.setEndTextRendered(false);
            ptnKyoriXData.setEndLabelRendered(true);
        }
        ptnKyoriXData.setEndTextMaxLength(endTextMaxLength);
        return ptnKyoriXData;
    }
}
