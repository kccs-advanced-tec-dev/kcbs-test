/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C001Model(ｶﾞﾗｽ作製・秤量入力サブ画面用)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/09/10
 */
public class GXHDO102C001Model implements Cloneable {


    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO102C001Model clone() throws CloneNotSupportedException {
        GXHDO102C001Model cloneModel = (GXHDO102C001Model) super.clone();
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
        List<FXHDD01> sub1DataBuzaitab1List = new ArrayList();
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
        List<FXHDD01> sub1DataBuzaitab2List = new ArrayList();
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
        List<FXHDD01> sub2DataBuzaitab1List = new ArrayList();
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
        List<FXHDD01> sub2DataBuzaitab2List = new ArrayList();
        
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリストのClone処理
        for (FXHDD01 data : this.getSub1DataBuzaitab1()) {
            sub1DataBuzaitab1List.add(data.clone());
        }
        cloneModel.setSub1DataBuzaitab1(sub1DataBuzaitab1List);
        
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリストのClone処理
        for (FXHDD01 data : this.getSub1DataBuzaitab2()) {
            sub1DataBuzaitab2List.add(data.clone());
        }
        cloneModel.setSub1DataBuzaitab2(sub1DataBuzaitab2List);
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格データのClone処理
        cloneModel.setSub1DataTyogouryoukikaku(this.getSub1DataTyogouryoukikaku().clone());
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量データのClone処理
        cloneModel.setSub1DataTyogouzanryou(this.getSub1DataTyogouzanryou().clone());
        
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリストのClone処理
        for (FXHDD01 data : this.getSub2DataBuzaitab1()) {
            sub2DataBuzaitab1List.add(data.clone());
        }
        cloneModel.setSub2DataBuzaitab1(sub2DataBuzaitab1List);
        
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリストのClone処理
        for (FXHDD01 data : this.getSub2DataBuzaitab2()) {
            sub2DataBuzaitab2List.add(data.clone());
        }
        cloneModel.setSub2DataBuzaitab2(sub2DataBuzaitab2List);
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合規格データのClone処理
        cloneModel.setSub2DataTyogouryoukikaku(this.getSub2DataTyogouryoukikaku().clone());
        // 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合残量データのClone処理
        cloneModel.setSub2DataTyogouzanryou(this.getSub2DataTyogouzanryou().clone());
        return cloneModel;
    }
    
    /**
     * 部材在庫No1_1設定項目ID
     */
    private String returnItemIdBuzailotno1_1 ="";
    
    /**
     * 調合量1_1設定項目ID
     */
    private String returnItemIdTyougouryou1_1 ="";
    
    /**
     * 部材在庫No1_2設定項目ID
     */
    private String returnItemIdBuzailotno1_2 ="";
    
    /**
     * 調合量1_2設定項目ID
     */
    private String returnItemIdTyougouryou1_2 ="";
    
    /**
     * 部材在庫No2_1設定項目ID
     */
    private String returnItemIdBuzailotno2_1 ="";
    
    /**
     * 調合量2_1設定項目ID
     */
    private String returnItemIdTyougouryou2_1 ="";
    
    /**
     * 部材在庫No2_2設定項目ID
     */
    private String returnItemIdBuzailotno2_2 ="";
    
    /**
     * 調合量2_2設定項目ID
     */
    private String returnItemIdTyougouryou2_2 ="";
    
    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の表示フラグ
     */
    private boolean  sub1DataRendered;
    
    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格データ
     */
    private FXHDD01 sub1DataTyogouryoukikaku;
    
    /**
     * サブ画面の固定値「g」
     */
    private String subDataTani;
    
    /**
     * 【材料品名1】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     */
    private String sub1DataGoki;
    
    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量データ
     */
    private FXHDD01 sub1DataTyogouzanryou;
    
    /**
     *【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     */
    private List<FXHDD01> sub1DataBuzaitab1;
    
    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     */
    private List<FXHDD01> sub1DataBuzaitab2;
    
    /**
     * 【材料品名2】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     */
    private String sub2DataGoki;
    
    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の表示フラグ
     */
    private boolean  sub2DataRendered;
    
    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合規格データ
     */
    private FXHDD01 sub2DataTyogouryoukikaku;

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合残量データ
     */
    private FXHDD01 sub2DataTyogouzanryou;
    
    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     */
    private List<FXHDD01> sub2DataBuzaitab1;
    
    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     */
    private List<FXHDD01> sub2DataBuzaitab2;

    /**
     * コンストラクタ
     */
    public GXHDO102C001Model() {
        
    }

    /**
     * 部材在庫No1_1設定項目ID
     * @return the returnItemIdBuzailotno1_1
     */
    public String getReturnItemIdBuzailotno1_1() {
        return returnItemIdBuzailotno1_1;
    }

    /**
     * 部材在庫No1_1設定項目ID
     * @param returnItemIdBuzailotno1_1 the returnItemIdBuzailotno1_1 to set
     */
    public void setReturnItemIdBuzailotno1_1(String returnItemIdBuzailotno1_1) {
        this.returnItemIdBuzailotno1_1 = returnItemIdBuzailotno1_1;
    }

    /**
     * 調合量1_1設定項目ID
     * @return the returnItemIdTyougouryou1_1
     */
    public String getReturnItemIdTyougouryou1_1() {
        return returnItemIdTyougouryou1_1;
    }

    /**
     * 調合量1_1設定項目ID
     * @param returnItemIdTyougouryou1_1 the returnItemIdTyougouryou1_1 to set
     */
    public void setReturnItemIdTyougouryou1_1(String returnItemIdTyougouryou1_1) {
        this.returnItemIdTyougouryou1_1 = returnItemIdTyougouryou1_1;
    }

    /**
     * 部材在庫No1_2設定項目ID
     * @return the returnItemIdBuzailotno1_2
     */
    public String getReturnItemIdBuzailotno1_2() {
        return returnItemIdBuzailotno1_2;
    }

    /**
     * 部材在庫No1_2設定項目ID
     * @param returnItemIdBuzailotno1_2 the returnItemIdBuzailotno1_2 to set
     */
    public void setReturnItemIdBuzailotno1_2(String returnItemIdBuzailotno1_2) {
        this.returnItemIdBuzailotno1_2 = returnItemIdBuzailotno1_2;
    }

    /**
     * 調合量1_2設定項目ID
     * @return the returnItemIdTyougouryou1_2
     */
    public String getReturnItemIdTyougouryou1_2() {
        return returnItemIdTyougouryou1_2;
    }

    /**
     * 調合量1_2設定項目ID
     * @param returnItemIdTyougouryou1_2 the returnItemIdTyougouryou1_2 to set
     */
    public void setReturnItemIdTyougouryou1_2(String returnItemIdTyougouryou1_2) {
        this.returnItemIdTyougouryou1_2 = returnItemIdTyougouryou1_2;
    }

    /**
     * 部材在庫No2_1設定項目ID
     * @return the returnItemIdBuzailotno2_1
     */
    public String getReturnItemIdBuzailotno2_1() {
        return returnItemIdBuzailotno2_1;
    }

    /**
     * 部材在庫No2_1設定項目ID
     * @param returnItemIdBuzailotno2_1 the returnItemIdBuzailotno2_1 to set
     */
    public void setReturnItemIdBuzailotno2_1(String returnItemIdBuzailotno2_1) {
        this.returnItemIdBuzailotno2_1 = returnItemIdBuzailotno2_1;
    }

    /**
     * 調合量2_1設定項目ID
     * @return the returnItemIdTyougouryou2_1
     */
    public String getReturnItemIdTyougouryou2_1() {
        return returnItemIdTyougouryou2_1;
    }

    /**
     * 調合量2_1設定項目ID
     * @param returnItemIdTyougouryou2_1 the returnItemIdTyougouryou2_1 to set
     */
    public void setReturnItemIdTyougouryou2_1(String returnItemIdTyougouryou2_1) {
        this.returnItemIdTyougouryou2_1 = returnItemIdTyougouryou2_1;
    }

    /**
     * 部材在庫No2_2設定項目ID
     * @return the returnItemIdBuzailotno2_2
     */
    public String getReturnItemIdBuzailotno2_2() {
        return returnItemIdBuzailotno2_2;
    }

    /**
     * 部材在庫No2_2設定項目ID
     * @param returnItemIdBuzailotno2_2 the returnItemIdBuzailotno2_2 to set
     */
    public void setReturnItemIdBuzailotno2_2(String returnItemIdBuzailotno2_2) {
        this.returnItemIdBuzailotno2_2 = returnItemIdBuzailotno2_2;
    }

    /**
     * 調合量2_2設定項目ID
     * @return the returnItemIdTyougouryou2_2
     */
    public String getReturnItemIdTyougouryou2_2() {
        return returnItemIdTyougouryou2_2;
    }

    /**
     * 調合量2_2設定項目ID
     * @param returnItemIdTyougouryou2_2 the returnItemIdTyougouryou2_2 to set
     */
    public void setReturnItemIdTyougouryou2_2(String returnItemIdTyougouryou2_2) {
        this.returnItemIdTyougouryou2_2 = returnItemIdTyougouryou2_2;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の表示フラグ
     * @return the sub1DataRendered
     */
    public boolean isSub1DataRendered() {
        return sub1DataRendered;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の表示フラグ
     * @param sub1DataRendered the sub1DataRendered to set
     */
    public void setSub1DataRendered(boolean sub1DataRendered) {
        this.sub1DataRendered = sub1DataRendered;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格データ
     * @return the sub1DataTyogouryoukikaku
     */
    public FXHDD01 getSub1DataTyogouryoukikaku() {
        return sub1DataTyogouryoukikaku;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格データ
     * @param sub1DataTyogouryoukikaku the sub1DataTyogouryoukikaku to set
     */
    public void setSub1DataTyogouryoukikaku(FXHDD01 sub1DataTyogouryoukikaku) {
        this.sub1DataTyogouryoukikaku = sub1DataTyogouryoukikaku;
    }

    /**
     * サブ画面の固定値「g」
     * @return the subDataTani
     */
    public String getSubDataTani() {
        return "g";
    }

    /**
     * サブ画面の固定値「g」
     * @param subDataTani the subDataTani to set
     */
    public void setSubDataTani(String subDataTani) {
        this.subDataTani = subDataTani;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     * @return the sub1DataGoki
     */
    public String getSub1DataGoki() {
        return sub1DataGoki;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     * @param sub1DataGoki the sub1DataGoki to set
     */
    public void setSub1DataGoki(String sub1DataGoki) {
        this.sub1DataGoki = sub1DataGoki;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量データ
     * @return the sub1DataTyogouzanryou
     */
    public FXHDD01 getSub1DataTyogouzanryou() {
        return sub1DataTyogouzanryou;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量データ
     * @param sub1DataTyogouzanryou the sub1DataTyogouzanryou to set
     */
    public void setSub1DataTyogouzanryou(FXHDD01 sub1DataTyogouzanryou) {
        this.sub1DataTyogouzanryou = sub1DataTyogouzanryou;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     * @return the sub1DataBuzaitab1
     */
    public List<FXHDD01> getSub1DataBuzaitab1() {
        return sub1DataBuzaitab1;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     * @param sub1DataBuzaitab1 the sub1DataBuzaitab1 to set
     */
    public void setSub1DataBuzaitab1(List<FXHDD01> sub1DataBuzaitab1) {
        this.sub1DataBuzaitab1 = sub1DataBuzaitab1;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     * @return the sub1DataBuzaitab2
     */
    public List<FXHDD01> getSub1DataBuzaitab2() {
        return sub1DataBuzaitab2;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     * @param sub1DataBuzaitab2 the sub1DataBuzaitab2 to set
     */
    public void setSub1DataBuzaitab2(List<FXHDD01> sub1DataBuzaitab2) {
        this.sub1DataBuzaitab2 = sub1DataBuzaitab2;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     * @return the sub2DataGoki
     */
    public String getSub2DataGoki() {
        return sub2DataGoki;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、主画面からサブ画面に渡された秤量号機データ
     * @param sub2DataGoki the sub2DataGoki to set
     */
    public void setSub2DataGoki(String sub2DataGoki) {
        this.sub2DataGoki = sub2DataGoki;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の表示フラグ
     * @return the sub2DataRendered
     */
    public boolean isSub2DataRendered() {
        return sub2DataRendered;
    }

    /**
     * 【材料品名1】ﾘﾝｸ押下時、サブ画面の表示フラグ
     * @param sub2DataRendered the sub2DataRendered to set
     */
    public void setSub2DataRendered(boolean sub2DataRendered) {
        this.sub2DataRendered = sub2DataRendered;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合規格データ
     * @return the sub2DataTyogouryoukikaku
     */
    public FXHDD01 getSub2DataTyogouryoukikaku() {
        return sub2DataTyogouryoukikaku;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合規格データ
     * @param sub2DataTyogouryoukikaku the sub2DataTyogouryoukikaku to set
     */
    public void setSub2DataTyogouryoukikaku(FXHDD01 sub2DataTyogouryoukikaku) {
        this.sub2DataTyogouryoukikaku = sub2DataTyogouryoukikaku;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合残量データ
     * @return the sub2DataTyogouzanryou
     */
    public FXHDD01 getSub2DataTyogouzanryou() {
        return sub2DataTyogouzanryou;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の調合残量データ
     * @param sub2DataTyogouzanryou the sub2DataTyogouzanryou to set
     */
    public void setSub2DataTyogouzanryou(FXHDD01 sub2DataTyogouzanryou) {
        this.sub2DataTyogouzanryou = sub2DataTyogouzanryou;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     * @return the sub2DataBuzaitab1
     */
    public List<FXHDD01> getSub2DataBuzaitab1() {
        return sub2DataBuzaitab1;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
     * @param sub2DataBuzaitab1 the sub2DataBuzaitab1 to set
     */
    public void setSub2DataBuzaitab1(List<FXHDD01> sub2DataBuzaitab1) {
        this.sub2DataBuzaitab1 = sub2DataBuzaitab1;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     * @return the sub2DataBuzaitab2
     */
    public List<FXHDD01> getSub2DataBuzaitab2() {
        return sub2DataBuzaitab2;
    }

    /**
     * 【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
     * @param sub2DataBuzaitab2 the sub2DataBuzaitab2 to set
     */
    public void setSub2DataBuzaitab2(List<FXHDD01> sub2DataBuzaitab2) {
        this.sub2DataBuzaitab2 = sub2DataBuzaitab2;
    }

}
