/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/10/14<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C020Model(前工程WIP取込サブ画面用)のモデルクラスです。
 * 
 * @author 863 zhangjy
 */
public class GXHDO101C020Model implements Cloneable {
    
    /** 種類名:ﾃｰﾌﾟﾛｯﾄ① */
    public static final String TAPE_LOT_1 = "ﾃｰﾌﾟﾛｯﾄ①";
    /** 種類名:ﾃｰﾌﾟﾛｯﾄ② */
    public static final String TAPE_LOT_2 = "ﾃｰﾌﾟﾛｯﾄ②";
    /** 種類名:ﾃｰﾌﾟﾛｯﾄ③ */
    public static final String TAPE_LOT_3 = "ﾃｰﾌﾟﾛｯﾄ③";
    /** 種類名:ﾍﾟｰｽﾄﾛｯﾄ① */
    public static final String PASTE_LOT_1 = "ﾍﾟｰｽﾄﾛｯﾄ①";
    /** 種類名:ﾍﾟｰｽﾄﾛｯﾄ② */
    public static final String PASTE_LOT_2 = "ﾍﾟｰｽﾄﾛｯﾄ②";
    /** 種類名:上端子 */
    public static final String UWA_TANSHI = "上端子";
    /** 種類名:下端子 */
    public static final String SHITA_TANSHI = "下端子";

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C020Model clone() throws CloneNotSupportedException {
        GXHDO101C020Model cloneModel = (GXHDO101C020Model) super.clone();
        
        List<GenryouLotData> newList = new ArrayList();
        for (GenryouLotData data : this.getGenryouLotDataList()) {
            GenryouLotData newData = new GenryouLotData();
            newData.setTypeName(data.getTypeName());
            newData.setValue(data.getValue());
            newData.setTextRendered(data.isTextRendered());
            newData.setTextMaxLength(data.getTextMaxLength());
            newData.setTextBackColor(data.getTextBackColor());
            newData.setLabelRendered(data.isLabelRendered());
            newData.setTabIndex(data.getTabIndex());
            newList.add(newData);
        }

        cloneModel.setGenryouLotDataList(newList);
        return cloneModel;
    }
    
    /**
     * 原料ロットデータリスト
     */
    private List<GenryouLotData> genryouLotDataList;
    
    /**
     * レスポンスデータMap
     */
    private Map<String, Map<String, String>> resultMap;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     */
    private String returnItemId_TapeLot1_Hinmei;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)conventionallotの設定項目ID
     */
    private String returnItemId_TapeLot1_Conventionallot;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)Lotkigoの設定項目ID
     */
    private String returnItemId_TapeLot1_Lotkigo;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)rollno設定項目ID
     */
    private String returnItemId_TapeLot1_Rollno;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)rollnoの設定項目ID
     */
    private String returnItemId_TapeLot2_Rollno;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ③)rollnoの設定項目ID
     */
    private String returnItemId_TapeLot3_Rollno;
    
    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)hinmeiの設定項目ID
     */
    private String returnItemId_PasteLot1_Hinmei;
    
    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)conventionallotの設定項目ID
     */
    private String returnItemId_PasteLot1_Conventionallot;
    
    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)kokeibun_pctの設定項目ID
     */
    private String returnItemId_PasteLot1_Kokeibunpct;
    
    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ②)conventionallotの設定項目ID
     */
    private String returnItemId_PasteLot2_Conventionallot;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)kokeibun_pctの設定項目ID
     */
    private String returnItemId_PasteLot2_Kokeibunpct;
    
    /**
     * (上端子)conventionallotの設定項目ID
     */
    private String returnItemId_Uwatanshi_Conventionallot;
    
    /**
     * (上端子)rollnoの設定項目ID
     */
    private String returnItemId_Uwatanshi_Rollno;
    
    /**
     * (下端子)conventionallotの設定項目ID
     */
    private String returnItemId_Shitatanshi_Conventionallot;
    
    /**
     * (下端子)rollnoの設定項目ID
     */
    private String returnItemId_Shitatanshi_Rollno;
    
    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     */
    private String returnItemId_TapeLot1_Tapelength;

    
    /**
     * petnameの設定項目ID
     */
    private String returnItemId_Petname;

    /**
     * 原料ロットデータリスト
     *
     * @return the genryouLotDataList
     */
    public List<GenryouLotData> getGenryouLotDataList() {
        return genryouLotDataList;
    }

    /**
     * 原料ロットデータリスト
     *
     * @param genryouLotDataList the genryouLotDataList to set
     */
    public void setGenryouLotDataList(List<GenryouLotData> genryouLotDataList) {
        this.genryouLotDataList = genryouLotDataList;
    }
    
    /**
     * レスポンスデータMap
     * 
     * @return the resultMap 
     */
    public Map<String, Map<String, String>> getResultMap() {
        return resultMap;
    }

    /**
     * レスポンスデータMap
     * 
     * @param resultMap the resultMap to set
     */
    public void setResultMap(Map<String, Map<String, String>> resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     * 
     * @return the returnItemId_TapeLot1_Hinmei 
     */
    public String getReturnItemId_TapeLot1_Hinmei() {
        return returnItemId_TapeLot1_Hinmei;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     * 
     * @param returnItemId_TapeLot1_Hinmei the returnItemId_TapeLot1_Hinmei to set
     */
    public void setReturnItemId_TapeLot1_Hinmei(String returnItemId_TapeLot1_Hinmei) {
        this.returnItemId_TapeLot1_Hinmei = returnItemId_TapeLot1_Hinmei;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)conventionallotの設定項目ID
     * 
     * @return the returnItemId_TapeLot1_Conventionallot
     */
    public String getReturnItemId_TapeLot1_Conventionallot() {
        return returnItemId_TapeLot1_Conventionallot;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)conventionallotの設定項目ID
     * 
     * @param returnItemId_TapeLot1_Conventionallot the returnItemId_TapeLot1_Conventionallot to set
     */
    public void setReturnItemId_TapeLot1_Conventionallot(String returnItemId_TapeLot1_Conventionallot) {
        this.returnItemId_TapeLot1_Conventionallot = returnItemId_TapeLot1_Conventionallot;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)Lotkigoの設定項目ID
     * 
     * @return the returnItemId_TapeLot1_Lotkigo
     */
    public String getReturnItemId_TapeLot1_Lotkigo() {
        return returnItemId_TapeLot1_Lotkigo;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)Lotkigoの設定項目ID
     * 
     * @param returnItemId_TapeLot1_Lotkigo the returnItemId_TapeLot1_Lotkigo to set
     */
    public void setReturnItemId_TapeLot1_Lotkigo(String returnItemId_TapeLot1_Lotkigo) {
        this.returnItemId_TapeLot1_Lotkigo = returnItemId_TapeLot1_Lotkigo;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)rollno設定項目ID
     * 
     * @return the returnItemId_TapeLot1_Rollno
     */
    public String getReturnItemId_TapeLot1_Rollno() {
        return returnItemId_TapeLot1_Rollno;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)rollno設定項目ID
     * 
     * @param returnItemId_TapeLot1_Rollno the returnItemId_TapeLot1_Rollno to set
     */
    public void setReturnItemId_TapeLot1_Rollno(String returnItemId_TapeLot1_Rollno) {
        this.returnItemId_TapeLot1_Rollno = returnItemId_TapeLot1_Rollno;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)rollnoの設定項目ID
     * 
     * @return the returnItemId_TapeLot2_Rollno
     */
    public String getReturnItemId_TapeLot2_Rollno() {
        return returnItemId_TapeLot2_Rollno;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)rollnoの設定項目ID
     * 
     * @param returnItemId_TapeLot2_Rollno the returnItemId_TapeLot2_Rollno to set
     */
    public void setReturnItemId_TapeLot2_Rollno(String returnItemId_TapeLot2_Rollno) {
        this.returnItemId_TapeLot2_Rollno = returnItemId_TapeLot2_Rollno;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ③)rollnoの設定項目ID
     * 
     * @return the returnItemId_TapeLot3_Rollno
     */
    public String getReturnItemId_TapeLot3_Rollno() {
        return returnItemId_TapeLot3_Rollno;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ③)rollnoの設定項目ID
     * 
     * @param returnItemId_TapeLot3_Rollno the returnItemId_TapeLot3_Rollno to set
     */
    public void setReturnItemId_TapeLot3_Rollno(String returnItemId_TapeLot3_Rollno) {
        this.returnItemId_TapeLot3_Rollno = returnItemId_TapeLot3_Rollno;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)hinmeiの設定項目ID
     * 
     * @return the returnItemId_PasteLot1_Hinmei
     */
    public String getReturnItemId_PasteLot1_Hinmei() {
        return returnItemId_PasteLot1_Hinmei;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)hinmeiの設定項目ID
     * 
     * @param returnItemId_PasteLot1_Hinmei the returnItemId_PasteLot1_Hinmei to set
     */
    public void setReturnItemId_PasteLot1_Hinmei(String returnItemId_PasteLot1_Hinmei) {
        this.returnItemId_PasteLot1_Hinmei = returnItemId_PasteLot1_Hinmei;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)conventionallotの設定項目ID
     * 
     * @return the returnItemId_PasteLot1_Conventionallot
     */
    public String getReturnItemId_PasteLot1_Conventionallot() {
        return returnItemId_PasteLot1_Conventionallot;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)conventionallotの設定項目ID
     * 
     * @param returnItemId_PasteLot1_Conventionallot the returnItemId_PasteLot1_Conventionallot to set
     */
    public void setReturnItemId_PasteLot1_Conventionallot(String returnItemId_PasteLot1_Conventionallot) {
        this.returnItemId_PasteLot1_Conventionallot = returnItemId_PasteLot1_Conventionallot;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)kokeibun_pctの設定項目ID
     * 
     * @return the returnItemId_PasteLot1_Kokeibunpct
     */
    public String getReturnItemId_PasteLot1_Kokeibunpct() {
        return returnItemId_PasteLot1_Kokeibunpct;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ①)kokeibun_pctの設定項目ID
     * 
     * @param returnItemId_PasteLot1_Kokeibunpct the returnItemId_PasteLot1_Kokeibunpct to set
     */
    public void setReturnItemId_PasteLot1_Kokeibunpct(String returnItemId_PasteLot1_Kokeibunpct) {
        this.returnItemId_PasteLot1_Kokeibunpct = returnItemId_PasteLot1_Kokeibunpct;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ②)conventionallotの設定項目ID
     * 
     * @return the returnItemId_PasteLot2_Conventionallot
     */
    public String getReturnItemId_PasteLot2_Conventionallot() {
        return returnItemId_PasteLot2_Conventionallot;
    }

    /**
     * (ﾍﾟｰｽﾄﾛｯﾄ②)conventionallotの設定項目ID
     * 
     * @param returnItemId_PasteLot2_Conventionallot the returnItemId_PasteLot2_Conventionallot to set
     */
    public void setReturnItemId_PasteLot2_Conventionallot(String returnItemId_PasteLot2_Conventionallot) {
        this.returnItemId_PasteLot2_Conventionallot = returnItemId_PasteLot2_Conventionallot;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)kokeibun_pctの設定項目ID
     * 
     * @return the returnItemId_PasteLot2_Kokeibunpct
     */
    public String getReturnItemId_PasteLot2_Kokeibunpct() {
        return returnItemId_PasteLot2_Kokeibunpct;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ②)kokeibun_pctの設定項目ID
     * 
     * @param returnItemId_PasteLot2_Kokeibunpct the returnItemId_PasteLot2_Kokeibunpct to set
     */
    public void setReturnItemId_PasteLot2_Kokeibunpct(String returnItemId_PasteLot2_Kokeibunpct) {
        this.returnItemId_PasteLot2_Kokeibunpct = returnItemId_PasteLot2_Kokeibunpct;
    }

    /**
     * (上端子)conventionallotの設定項目ID
     * 
     * @return the returnItemId_Uwatanshi_Conventionallot
     */
    public String getReturnItemId_Uwatanshi_Conventionallot() {
        return returnItemId_Uwatanshi_Conventionallot;
    }

    /**
     * (上端子)conventionallotの設定項目ID
     * 
     * @param returnItemId_Uwatanshi_Conventionallot the returnItemId_Uwatanshi_Conventionallot to set
     */
    public void setReturnItemId_Uwatanshi_Conventionallot(String returnItemId_Uwatanshi_Conventionallot) {
        this.returnItemId_Uwatanshi_Conventionallot = returnItemId_Uwatanshi_Conventionallot;
    }

    /**
     * (上端子)rollnoの設定項目ID
     * 
     * @return the returnItemId_Uwatanshi_Rollno
     */
    public String getReturnItemId_Uwatanshi_Rollno() {
        return returnItemId_Uwatanshi_Rollno;
    }

    /**
     * (上端子)rollnoの設定項目ID
     * 
     * @param returnItemId_Uwatanshi_Rollno the returnItemId_Uwatanshi_Rollno to set
     */
    public void setReturnItemId_Uwatanshi_Rollno(String returnItemId_Uwatanshi_Rollno) {
        this.returnItemId_Uwatanshi_Rollno = returnItemId_Uwatanshi_Rollno;
    }

    /**
     * (下端子)conventionallotの設定項目ID
     * 
     * @return the returnItemId_Shitatanshi_Conventionallot
     */
    public String getReturnItemId_Shitatanshi_Conventionallot() {
        return returnItemId_Shitatanshi_Conventionallot;
    }

    /**
     * (下端子)conventionallotの設定項目ID
     * 
     * @param returnItemId_Shitatanshi_Conventionallot the returnItemId_Shitatanshi_Conventionallot to set
     */
    public void setReturnItemId_Shitatanshi_Conventionallot(String returnItemId_Shitatanshi_Conventionallot) {
        this.returnItemId_Shitatanshi_Conventionallot = returnItemId_Shitatanshi_Conventionallot;
    }

    /**
     * (下端子)rollnoの設定項目ID
     * 
     * @return the returnItemId_Shitatanshi_Rollno
     */
    public String getReturnItemId_Shitatanshi_Rollno() {
        return returnItemId_Shitatanshi_Rollno;
    }

    /**
     * (下端子)rollnoの設定項目ID
     * 
     * @param returnItemId_Shitatanshi_Rollno the returnItemId_Shitatanshi_Rollno to set
     */
    public void setReturnItemId_Shitatanshi_Rollno(String returnItemId_Shitatanshi_Rollno) {
        this.returnItemId_Shitatanshi_Rollno = returnItemId_Shitatanshi_Rollno;
    }

    /**
     * petnameの設定項目ID
     * 
     * @return the returnItemId_Petname
     */
    public String getReturnItemId_Petname() {
        return returnItemId_Petname;
    }

    /**
     * petnameの設定項目ID
     * 
     * @param returnItemId_Petname the returnItemId_Petname to set
     */
    public void setReturnItemId_Petname(String returnItemId_Petname) {
        this.returnItemId_Petname = returnItemId_Petname;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     * @return the returnItemId_TapeLot1_Tapelength
     */
    public String getReturnItemId_TapeLot1_Tapelength() {
        return returnItemId_TapeLot1_Tapelength;
    }

    /**
     * (ﾃｰﾌﾟﾛｯﾄ①)hinmeiの設定項目ID
     * @param returnItemId_TapeLot1_Tapelength the returnItemId_TapeLot1_Tapelength to set
     */
    public void setReturnItemId_TapeLot1_Tapelength(String returnItemId_TapeLot1_Tapelength) {
        this.returnItemId_TapeLot1_Tapelength = returnItemId_TapeLot1_Tapelength;
    }
    
    /**
     * コンストラクタ
     */
    public GXHDO101C020Model() {

    }
    
    /**
     * 原料ロットデータ
     */
    public class GenryouLotData {

        /**
         * 種類
         */
        private String typeName;
        
        /**
         * 値
         */
        private String value;

        /**
         * テキスト(Rendered)
         */
        private boolean textRendered;

        /**
         * テキスト(MaxLength)
         */
        private String textMaxLength;

        /**
         * テキスト(BackGround)
         */
        private String textBackColor;

        /**
         * ラベル(Rendered)
         */
        private boolean labelRendered;

        /**
         * タブインデックス(TabIndex)
         */
        private String tabIndex;

        /**
         * 種類
         * 
         * @return 
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         * 種類
         * 
         * @param typeName 
         */
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        /**
         * 値
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * 値
         *
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * テキスト(Rendered)
         *
         * @return the textRendered
         */
        public boolean isTextRendered() {
            return textRendered;
        }

        /**
         * テキスト(Rendered)
         *
         * @param textRendered the textRendered to set
         */
        public void setTextRendered(boolean textRendered) {
            this.textRendered = textRendered;
        }

        /**
         * テキスト(MaxLength)
         *
         * @return the textMaxLength
         */
        public String getTextMaxLength() {
            return textMaxLength;
        }

        /**
         * テキスト(MaxLength)
         *
         * @param textMaxLength the textMaxLength to set
         */
        public void setTextMaxLength(String textMaxLength) {
            this.textMaxLength = textMaxLength;
        }
        
        /**
         * テキスト(BackGround)
         *
         * @return the textBackColor
         */
        public String getTextBackColor() {
            return textBackColor;
        }

        /**
         * テキスト(BackGround)
         *
         * @param textBackColor the textBackColor to set
         */
        public void setTextBackColor(String textBackColor) {
            this.textBackColor = textBackColor;
        }

        /**
         * ラベル(Rendered)
         *
         * @return the labelRendered
         */
        public boolean isLabelRendered() {
            return labelRendered;
        }

        /**
         * ラベル(Rendered)
         *
         * @param labelRendered the labelRendered to set
         */
        public void setLabelRendered(boolean labelRendered) {
            this.labelRendered = labelRendered;
        }

        /**
         * タブインデックス(TabIndex)
         *
         * @return the tabIndex
         */
        public String getTabIndex() {
            return tabIndex;
        }

        /**
         * タブインデックス(TabIndex)
         *
         * @param tabIndex the tabIndex to set
         */
        public void setTabIndex(String tabIndex) {
            this.tabIndex = tabIndex;
        }
    }
}
