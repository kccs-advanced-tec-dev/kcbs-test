/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO102C001Model;
import jp.co.kccs.xhd.pxhdo901.GXHDO901B;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.primefaces.context.RequestContext;
import jp.co.kccs.xhd.util.DBUtil;
import org.apache.commons.dbutils.handlers.MapHandler;

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
 * GXHDO102C001(ｶﾞﾗｽ作製・秤量入力)
 *
 * @author KCSS wxf
 * @since 2021/09/10
 */
@ManagedBean(name = "beanGXHDO102C001")
@SessionScoped
public class GXHDO102C001 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901B.class.getName());

    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo;
    /**
     * ﾛｯﾄNo
     */
    private String lotno;
    /**
     * 枝番
     */
    private String edaban;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * エラーを発生されるタブインデックス
     */
    private Integer errorTabIndex;

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ
     */
    private GXHDO102C001Model gxhdO102c001Model;

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ(表示制御用)
     */
    private GXHDO102C001Model gxhdO102c001ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO102C001() {
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        this.setIsFormError(false);
        if (!checkOK()) {
            this.setIsFormError(true);
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error" + getErrorTabIndex());
            return;
        }

        this.setGxhdO102c001Model(this.getGxhdO102c001ModelView());
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        GXHDO102C001Logic.clearBackColor(this.getGxhdO102c001ModelView());
        setErrorTabIndex(0);
        if (this.getGxhdO102c001ModelView().isSub1DataRendered()) {
            // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
            List<FXHDD01> subDataBuzaitab1List = this.getGxhdO102c001ModelView().getSub1DataBuzaitab1();
            // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
            List<FXHDD01> subDataBuzaitab2List = this.getGxhdO102c001ModelView().getSub1DataBuzaitab2();
            // 【部材在庫No1】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
            if (!buzainoCheck(subDataBuzaitab1List.get(1), "1")) {
                setErrorTabIndex(1);
                return false;
            }
            // サブ画面の部材①の調合量のチェック処理
            if (!checkBuzaitab(subDataBuzaitab1List)) {
                setErrorTabIndex(1);
                return false;
            }

            // 【部材在庫No2】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
            if (!buzainoCheck(subDataBuzaitab2List.get(1), "2")) {
                setErrorTabIndex(2);
                return false;
            }
            // サブ画面の部材②の調合量のチェック処理
            if (!checkBuzaitab(subDataBuzaitab2List)) {
                setErrorTabIndex(2);
                return false;
            }
        } else if (this.getGxhdO102c001ModelView().isSub2DataRendered()) {
            // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト
            List<FXHDD01> subDataBuzaitab1List = this.getGxhdO102c001ModelView().getSub2DataBuzaitab1();
            // 【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト
            List<FXHDD01> subDataBuzaitab2List = this.getGxhdO102c001ModelView().getSub2DataBuzaitab2();
            // 【部材在庫No1】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
            if (!buzainoCheck(subDataBuzaitab1List.get(1), "1")) {
                setErrorTabIndex(1);
                return false;
            }
            // サブ画面の部材①の調合量のチェック処理
            if (!checkBuzaitab(subDataBuzaitab1List)) {
                setErrorTabIndex(1);
                return false;
            }

            // 【部材在庫No2】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
            if (!buzainoCheck(subDataBuzaitab2List.get(1), "2")) {
                setErrorTabIndex(2);
                return false;
            }
            // サブ画面の部材②の調合量のチェック処理
            if (!checkBuzaitab(subDataBuzaitab2List)) {
                setErrorTabIndex(2);
                return false;
            }
        }

        return true;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力_サブ画面の調合残量計算処理
     *
     */
    public void doTyogouzanryouKeisan() {
        //サブ画面の調合残量の計算処理
        GXHDO102C001Logic.calcTyogouzanryou(this.getGxhdO102c001ModelView());
        this.setGxhdO102c001Model(this.getGxhdO102c001ModelView());
    }

    /**
     * 調合量リストのチェック処理
     *
     * @param subDataBuzaitabList タブにの調合量データリスト
     * @return 正常:true、異常:fasle
     */
    private boolean checkBuzaitab(List<FXHDD01> subDataBuzaitabList) {
        // 調合量X_2に調合量が入力されている場合、調合量X_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(3), subDataBuzaitabList.get(4))) {
            return false;
        }
        // 調合量X_4に調合量が入力されている場合、調合量X_3に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(5), subDataBuzaitabList.get(6))) {
            return false;
        }
        // 調合量X_6に調合量が入力されている場合、調合量X_5に0が入力されていること。
        return tyogouryouCheck(subDataBuzaitabList.get(7), subDataBuzaitabList.get(8));
    }

    /**
     * 調合量のチェック処理
     *
     * @param item1 調合量項目1
     * @param item2 調合量項目2
     * @return 正常:true、異常:fasle
     */
    private boolean tyogouryouCheck(FXHDD01 item1, FXHDD01 item2) {
        if (NumberUtil.isZeroOrEmpty(item1.getValue()) && !StringUtil.isEmpty(item2.getValue())) {
            setError(item1, "XHD-000222");
            return false;
        }
        return true;
    }

    /**
     * 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時の処理
     *
     * @param itemBuzaino 部材在庫No項目
     * @param subTabNo タブインデックス
     */
    public void dobuzainoOnblur(FXHDD01 itemBuzaino, String subTabNo) {
        this.setIsFormError(false);
        // 背景色をクリア
        GXHDO102C001Logic.clearBackColor(this.getGxhdO102c001ModelView());
        if (!buzainoCheck(itemBuzaino, subTabNo)) {
            this.setIsFormError(true);
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }
        this.setGxhdO102c001Model(this.getGxhdO102c001ModelView());
    }

    /**
     * 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
     *
     * @param itemBuzaino 部材在庫No項目
     * @param subTabNo タブインデックス
     * @return 正常:true、異常:fasle
     */
    private boolean buzainoCheck(FXHDD01 itemBuzaino, String subTabNo) {
        if (StringUtil.isEmpty(itemBuzaino.getValue())) {
            return true;
        }
        // 部材在庫品名の値をクリア
        setBuzaihinmeiVal(this.getGxhdO102c001ModelView(), subTabNo, "");
        // 【部材在庫No1】ﾛｽﾄﾌｫｰｶｽ時、部材在庫No1の型ﾁｪｯｸ
        if (StringUtil.getLength(itemBuzaino.getValue()) != 9) {
            setError(itemBuzaino, "XHD-000004", itemBuzaino.getLabel1(), "9");
            return false;
        }
        // 【部材在庫No1】ﾛｽﾄﾌｫｰｶｽ時、部材在庫ﾃﾞｰﾀ取得後のチェック
        Map<String, Object> fmlad01Data = getFmlad01Data(itemBuzaino.getValue());
        if (fmlad01Data == null) {
            setError(itemBuzaino, "XHD-000219");
            return false;
        }
        String hinmei = StringUtil.nullToBlank(fmlad01Data.get("hinmei"));
        Timestamp yukokigen = (Timestamp) fmlad01Data.get("yukokigen");
        FXHDD01 itemZairyohinmei;
        // 品名のチェック: 取得した品名と材料品名が一致していること
        if (this.getGxhdO102c001ModelView().isSub1DataRendered()) {
            if ("1".equals(subTabNo)) {
                // 材料品名
                itemZairyohinmei = this.getGxhdO102c001ModelView().getSub1DataBuzaitab1().get(0);
            } else {
                itemZairyohinmei = this.getGxhdO102c001ModelView().getSub1DataBuzaitab2().get(0);
            }
        } else {
            if ("1".equals(subTabNo)) {
                // 材料品名
                itemZairyohinmei = this.getGxhdO102c001ModelView().getSub2DataBuzaitab1().get(0);
            } else {
                itemZairyohinmei = this.getGxhdO102c001ModelView().getSub2DataBuzaitab2().get(0);
            }
        }
        String zairyohinmei = StringUtil.nullToBlank(itemZairyohinmei.getValue());
        if (!zairyohinmei.equals(hinmei)) {
            setError(itemZairyohinmei, "XHD-000220");
            return false;
        }

        // 有効期限のチェック: 現在日付が取得した有効期限を過ぎていないこと
        Timestamp systemTime = new Timestamp(System.currentTimeMillis());

        if (yukokigen != null && systemTime.after(yukokigen)) {
            setError(itemBuzaino, "XHD-000221");
            return false;
        }
        // 部材在庫ﾃﾞｰﾀから取得した品名は部材在庫品名に設定
        setBuzaihinmeiVal(this.getGxhdO102c001ModelView(), subTabNo, hinmei);

        return true;
    }

    /**
     * 部材在庫品名の値を設定
     *
     * @param gxhdo102c001model モデルデータ
     * @param subTabNo タブインデックス
     */
    private void setBuzaihinmeiVal(GXHDO102C001Model gxhdo102c001model, String subTabNo, String hinmei) {
        // 部材在庫品名の値を設定
        if (gxhdo102c001model.isSub1DataRendered()) {
            if ("1".equals(subTabNo)) {
                gxhdo102c001model.getSub1DataBuzaitab1().get(2).setValue(hinmei);
            } else if ("2".equals(subTabNo)) {
                gxhdo102c001model.getSub1DataBuzaitab2().get(2).setValue(hinmei);
            }
        } else if (gxhdo102c001model.isSub2DataRendered()) {
            if ("1".equals(subTabNo)) {
                gxhdo102c001model.getSub2DataBuzaitab1().get(2).setValue(hinmei);
            } else if ("2".equals(subTabNo)) {
                gxhdo102c001model.getSub2DataBuzaitab2().get(2).setValue(hinmei);
            }
        }
    }
    
    /**
     * 部材在庫ﾃﾞｰﾀの取得
     *
     * @param zaikono 在庫No
     * @return 部材在庫ﾃﾞｰﾀ情報取得
     */
    private Map<String, Object> getFmlad01Data(String zaikono) {
        try {
            QueryRunner queryRunner = new QueryRunner(getDataSourceDocServer());
            String sql = "SELECT hinmei, yuko_kigen as yukokigen"
                    + " FROM fmlad01 "
                    + " WHERE zaiko_no = ? ";

            List<Object> params = new ArrayList<>();
            params.add(zaikono);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunner.query(sql, new MapHandler(), params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実行エラー", ex, LOGGER);
        }
        return null;
    }

    /**
     * エラーセット
     *
     * @param itemData ｶﾞﾗｽ作製・秤量入力サブ画面
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(FXHDD01 itemData, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        itemData.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
    }

    /**
     * DataSource(DocumentServer)
     *
     * @return the dataSourceDocServer
     */
    public DataSource getDataSourceDocServer() {
        return dataSourceDocServer;
    }

    /**
     * DataSource(DocumentServer)
     *
     * @param dataSourceDocServer the dataSourceDocServer to set
     */
    public void setDataSourceDocServer(DataSource dataSourceDocServer) {
        this.dataSourceDocServer = dataSourceDocServer;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * フォームエラー判定
     *
     * @return the isFormError
     */
    public boolean isIsFormError() {
        return isFormError;
    }

    /**
     * フォームエラー判定
     *
     * @param isFormError the isFormError to set
     */
    public void setIsFormError(boolean isFormError) {
        this.isFormError = isFormError;
    }

    /**
     * エラーを発生されるタブインデックス
     *
     * @return the errorTabIndex
     */
    public Integer getErrorTabIndex() {
        return errorTabIndex;
    }

    /**
     * エラーを発生されるタブインデックス
     *
     * @param errorTabIndex the errorTabIndex to set
     */
    public void setErrorTabIndex(Integer errorTabIndex) {
        this.errorTabIndex = errorTabIndex;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ
     *
     * @return the gxhdO102c001Model
     */
    public GXHDO102C001Model getGxhdO102c001Model() {
        return gxhdO102c001Model;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ
     *
     * @param gxhdO102c001Model the gxhdO102c001Model to set
     */
    public void setGxhdO102c001Model(GXHDO102C001Model gxhdO102c001Model) {
        this.gxhdO102c001Model = gxhdO102c001Model;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ(表示制御用)
     *
     * @return the gxhdO102c001ModelView
     */
    public GXHDO102C001Model getGxhdO102c001ModelView() {
        return gxhdO102c001ModelView;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力サブ画面用データ(表示制御用)
     *
     * @param gxhdO102c001ModelView the gxhdO102c001ModelView to set
     */
    public void setGxhdO102c001ModelView(GXHDO102C001Model gxhdO102c001ModelView) {
        this.gxhdO102c001ModelView = gxhdO102c001ModelView;
    }
}
