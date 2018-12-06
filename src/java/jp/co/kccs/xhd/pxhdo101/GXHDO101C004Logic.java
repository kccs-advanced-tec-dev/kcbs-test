/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.GXHDO101C004Model;

/**
 *
 * @author koksk-desk2
 */
public class GXHDO101C004Logic {

    public static GXHDO101C004Model createGXHDO101C004Model(String lotNo) {
        List<GXHDO101C004Model.makuatsuData> makuatsuDataList = new ArrayList<>();
        GXHDO101C004Model model = new GXHDO101C004Model();

        // 膜厚(1行目)
        makuatsuDataList.add(getInitModel(model, "1", "", "TEXT", "14", "", "TEXT", "2"));
        // 膜厚(2行目)
        makuatsuDataList.add(getInitModel(model, "2", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(3行目)
        makuatsuDataList.add(getInitModel(model, "3", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(4行目)
        makuatsuDataList.add(getInitModel(model, "4", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(5行目)
        makuatsuDataList.add(getInitModel(model, "5", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(6行目)
        makuatsuDataList.add(getInitModel(model, "6", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(7行目)
        makuatsuDataList.add(getInitModel(model, "7", "", "LABLE", "0", "", "TEXT", "20"));
        // 膜厚(8行目)
        makuatsuDataList.add(getInitModel(model, "8", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(9行目)
        makuatsuDataList.add(getInitModel(model, "9", "", "TEXT", "4", "", "LABEL", "0"));
        model.setMakuatsuDataList(makuatsuDataList);

        return model;
    }

    private static GXHDO101C004Model.makuatsuData getInitModel(GXHDO101C004Model model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength,
            String endVal, String endInputType, String endTextMaxLength) {
        GXHDO101C004Model.makuatsuData makuatsuData = model.new makuatsuData();
        makuatsuData.setMakuatsu(makuatsu); //
        makuatsuData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuData.setStartTextRendered(true);
            makuatsuData.setStartLabelRendered(false);

        } else {
            makuatsuData.setStartTextRendered(false);
            makuatsuData.setStartLabelRendered(true);
        }
        makuatsuData.setStartTextMaxLength(startTextMaxLength);
        makuatsuData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            makuatsuData.setEndTextRendered(true);
            makuatsuData.setEndLabelRendered(false);

        } else {
            makuatsuData.setEndTextRendered(false);
            makuatsuData.setEndLabelRendered(true);
        }
        makuatsuData.setEndTextMaxLength(endTextMaxLength);
        return makuatsuData;
    }

}
