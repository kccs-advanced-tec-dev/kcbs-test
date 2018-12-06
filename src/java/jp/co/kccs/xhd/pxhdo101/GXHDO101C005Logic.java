/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.GXHDO101C001Model;

/**
 *
 * @author koksk-desk2
 */
public class GXHDO101C005Logic {

    public static GXHDO101C001Model createGXHDO101C001Model(String lotNo) {
        GXHDO101C001Model gxhdo101C001Model = new GXHDO101C001Model();
        List<GXHDO101C001Model.makuatsuData> makuatsuDataList = new ArrayList<>();

        // 膜厚(1行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "1", "", "TEXT", "14", "", "TEXT", "2"));
        // 膜厚(2行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "2", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(3行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "3", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(4行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "4", "", "LABLE", "0", "", "TEXT", "2"));
        // 膜厚(5行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "5", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(6行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "6", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(7行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "7", "", "LABLE", "0", "", "TEXT", "20"));
        // 膜厚(8行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "8", "", "LABLE", "0", "", "LABEL", "0"));
        // 膜厚(9行目)
        makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, "9", "", "TEXT", "4", "", "LABEL", "0"));

        gxhdo101C001Model.setMakuatsuDataList(makuatsuDataList);
        return gxhdo101C001Model;
    }

    private static GXHDO101C001Model.makuatsuData getInitMakuatsuData(
            GXHDO101C001Model gxhdo101C001Model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength,
            String endVal, String endInputType, String endTextMaxLength) {
        GXHDO101C001Model.makuatsuData makuatsuListData = gxhdo101C001Model.new makuatsuData();
        makuatsuListData.setMakuatsu(makuatsu); //
        makuatsuListData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuListData.setStartTextRendered(true);
            makuatsuListData.setStartLabelRendered(false);

        } else {
            makuatsuListData.setStartTextRendered(false);
            makuatsuListData.setStartLabelRendered(true);
        }
        makuatsuListData.setStartTextMaxLength(startTextMaxLength);
        makuatsuListData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            makuatsuListData.setEndTextRendered(true);
            makuatsuListData.setEndLabelRendered(false);

        } else {
            makuatsuListData.setEndTextRendered(false);
            makuatsuListData.setEndLabelRendered(true);
        }
        makuatsuListData.setEndTextMaxLength(endTextMaxLength);
        return makuatsuListData;
    }

}
