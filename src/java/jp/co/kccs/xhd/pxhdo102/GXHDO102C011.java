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
import jp.co.kccs.xhd.model.GXHDO102C011Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.primefaces.context.RequestContext;
import jp.co.kccs.xhd.util.DBUtil;
import org.apache.commons.dbutils.handlers.MapHandler;
import java.math.BigDecimal;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/05/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	材料品名ﾘﾝｸ押下時、調合量規格チェックの追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C011(誘電体ｽﾗﾘｰ作製・主原料秤量入力)
 *
 * @author KCSS wxf
 * @since 2021/11/09
 */
@ManagedBean(name = "beanGXHDO102C011")
@SessionScoped
public class GXHDO102C011 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102C011.class.getName());

    /**
     * DataSource(MLAServer)
     */
    @Resource(mappedName = "jdbc/MLA")
    private transient DataSource dataSourceMLAServer;

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ
     */
    private GXHDO102C011Model gxhdO102c011Model;

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ(表示制御用)
     */
    private GXHDO102C011Model gxhdO102c011ModelView;

    /**
     * コンストラクタ
     */
    public GXHDO102C011() {
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
        GXHDO102C011Logic.setSubgamenDataFromView(this.getGxhdO102c011ModelView(), this.getGxhdO102c011Model());
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        GXHDO102C011Model.SubGamenData showsubgamendata = this.getGxhdO102c011ModelView().getShowsubgamendata();
        // 背景色をクリア
        GXHDO102C011Logic.clearBackColor(this.getGxhdO102c011ModelView());
        setErrorTabIndex(0);
        // サブ画面の部材①タブデータリスト
        List<FXHDD01> subDataBuzaitab1List = showsubgamendata.getSubDataBuzaitab1();
        // 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
        if (!buzainoCheck(subDataBuzaitab1List.get(1), "1")) {
            setErrorTabIndex(1);
            return false;
        }
        // サブ画面の部材①の調合量のチェック処理
        if (!checkBuzaitab(subDataBuzaitab1List)) {
            setErrorTabIndex(1);
            return false;
        }

        // 調合量の合計と調合規格の規格チェック
        //【材料品名】ﾘﾝｸ押下時、サブ画面の調合規格データ
        FXHDD01 subDataTyogouryoukikaku = this.getGxhdO102c011ModelView().getShowsubgamendata().getSubDataTyogouryoukikaku();
        if(!checkTyogouryouKikaku(subDataBuzaitab1List, subDataTyogouryoukikaku)){
            return false;
        }
        return true;
    }

    /**
     * 調合量規格チェック処理
     *
     * @param subDataBuzaitabList タブにの調合量データリスト
     * @return 正常:true、異常:fasle
     */ 
    private boolean checkTyogouryouKikaku(List<FXHDD01> subDataBuzaitab1List,FXHDD01 subDataTyogouryoukikaku) {

        String tyogouryou1Tab1 = subDataBuzaitab1List.get(3).getValue();
        String tyogouryou2Tab1 = subDataBuzaitab1List.get(4).getValue();
        String tyogouryou3Tab1 = subDataBuzaitab1List.get(5).getValue();
        String tyogouryou4Tab1 = subDataBuzaitab1List.get(6).getValue();
        String tyogouryou5Tab1 = subDataBuzaitab1List.get(7).getValue();
        String tyogouryou6Tab1 = subDataBuzaitab1List.get(8).getValue();
        String tyogouryou7Tab1 = subDataBuzaitab1List.get(9).getValue();
        String tyogouryou8Tab1 = subDataBuzaitab1List.get(10).getValue();
        String tyogouryou9Tab1 = subDataBuzaitab1List.get(11).getValue();
        String tyogouryou10Tab1 = subDataBuzaitab1List.get(12).getValue();
        String tyogouryou11Tab1 = subDataBuzaitab1List.get(13).getValue();
        String tyogouryou12Tab1 = subDataBuzaitab1List.get(14).getValue();
        String tyogouryou13Tab1 = subDataBuzaitab1List.get(15).getValue();
        String tyogouryou14Tab1 = subDataBuzaitab1List.get(16).getValue();

        BigDecimal totalValue = BigDecimal.ZERO;

        if(!StringUtil.isEmpty(tyogouryou1Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou1Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou2Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou2Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou3Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou3Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou4Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou4Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou5Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou5Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou6Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou6Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou7Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou7Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou8Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou8Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou9Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou9Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou10Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou10Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou11Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou11Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou12Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou12Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou13Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou13Tab1));
        }

        if(!StringUtil.isEmpty(tyogouryou14Tab1)){
            totalValue = totalValue.add(new BigDecimal(tyogouryou14Tab1));
        }

        try {
            FXHDD01 tyogouryoukikakuCheck = subDataTyogouryoukikaku.clone();
            String kikakuValue = subDataTyogouryoukikaku.getValue();
            tyogouryoukikakuCheck.setKikakuChi(kikakuValue);
            tyogouryoukikakuCheck.setValue(totalValue.toString());
            tyogouryoukikakuCheck.setStandardPattern(subDataTyogouryoukikaku.getStandardPattern());
             
            List<FXHDD01> itemCheckObj = new ArrayList<>();
            itemCheckObj.add(tyogouryoukikakuCheck);
            List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
            ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemCheckObj, kikakuchiInputErrorInfoList);

            // 規格値エラーがある場合は規格値エラーをセット
            if (!kikakuchiInputErrorInfoList.isEmpty()) {
                setError(subDataTyogouryoukikaku, "XHD-000011","調合量");
                return false;
            }

            // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
            if (errorMessageInfo != null) {
                if (!StringUtil.isEmpty(errorMessageInfo.getErrorMessage())) {
                    setError(subDataTyogouryoukikaku, "XHD-000011","調合量");
                    return false;
                }
            }
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
        }
        return true;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の調合残量計算処理
     *
     */
    public void doTyogouzanryouKeisan() {
        //サブ画面の調合残量の計算処理
        GXHDO102C011Logic.calcTyogouzanryou(this.getGxhdO102c011ModelView());
        GXHDO102C011Logic.setShowsubgamendataFromView(this.getGxhdO102c011ModelView(), this.getGxhdO102c011Model());
    }

    /**
     * 調合量リストのチェック処理
     *
     * @param subDataBuzaitabList タブにの調合量データリスト
     * @return 正常:true、異常:fasle
     */
    private boolean checkBuzaitab(List<FXHDD01> subDataBuzaitabList) {

        GXHDO102C011Model.SubGamenData showsubgamendata = this.getGxhdO102c011ModelView().getShowsubgamendata();
        // サブ画面の材料区分
        int zairyokubun = showsubgamendata.getSubDataZairyokubun();
        // 調合量1_2に調合量が入力されている場合、調合量1_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(3), subDataBuzaitabList.get(4))) {
            return false;
        }
        if(zairyokubun == 6){
            return true;
        }
        // 調合量2_2に調合量が入力されている場合、調合量2_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(5), subDataBuzaitabList.get(6))) {
            return false;
        }
        if(zairyokubun == 4 || zairyokubun == 5){
            return true;
        }
        // 調合量3_2に調合量が入力されている場合、調合量3_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(7), subDataBuzaitabList.get(8))) {
            return false;
        }
        if(zairyokubun == 2 || zairyokubun == 3){
            return true;
        }
        // 調合量4_2に調合量が入力されている場合、調合量4_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(9), subDataBuzaitabList.get(10))) {
            return false;
        }
        // 調合量5_2に調合量が入力されている場合、調合量5_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(11), subDataBuzaitabList.get(12))) {
            return false;
        }
        // 調合量6_2に調合量が入力されている場合、調合量6_1に0が入力されていること。
        if (!tyogouryouCheck(subDataBuzaitabList.get(13), subDataBuzaitabList.get(14))) {
            return false;
        }
        // 調合量7_2に調合量が入力されている場合、調合量7_1に0が入力されていること。
        return tyogouryouCheck(subDataBuzaitabList.get(15), subDataBuzaitabList.get(16));
    }

    /**
     * 調合量のチェック処理
     *
     * @param item1 調合量項目1
     * @param item2 調合量項目2
     * @return 正常:true、異常:fasle
     */
    private boolean tyogouryouCheck(FXHDD01 item1, FXHDD01 item2) {
        // 奇数行に入力されるとき、0以外が入力されていないこと。
        if (!StringUtil.isEmpty(item1.getValue()) && !NumberUtil.isZero(item1.getValue())) {
            setError(item1, "XHD-000222");
            return false;
        }

        // 偶数行に調合量が入力されている場合、該当行の前の行に0が入力されていること。
        if (!NumberUtil.isZero(item1.getValue()) && !StringUtil.isEmpty(item2.getValue())) {
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
        GXHDO102C011Logic.clearBackColor(this.getGxhdO102c011ModelView());
        if (!buzainoCheck(itemBuzaino, subTabNo)) {
            this.setIsFormError(true);
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }
        GXHDO102C011Logic.setShowsubgamendataFromView(this.getGxhdO102c011ModelView(), this.getGxhdO102c011Model());
    }

    /**
     * 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時のチェック処理
     *
     * @param itemBuzaino 部材在庫No項目
     * @param subTabNo タブインデックス
     * @return 正常:true、異常:fasle
     */
    private boolean buzainoCheck(FXHDD01 itemBuzaino, String subTabNo) {
        // 部材在庫品名の値をクリア
        setBuzaihinmeiVal(this.getGxhdO102c011ModelView(), "");
        if (StringUtil.isEmpty(itemBuzaino.getValue())) {
            return true;
        }
        // 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時、部材在庫Noの型ﾁｪｯｸ
        if (StringUtil.getLength(itemBuzaino.getValue()) != 9) {
            setError(itemBuzaino, "XHD-000004", itemBuzaino.getLabel1(), "9");
            return false;
        }
        // 【部材在庫No】ﾛｽﾄﾌｫｰｶｽ時、部材在庫ﾃﾞｰﾀ取得後のチェック
        Map<String, Object> fmlad01Data = getFmlad01Data(itemBuzaino.getValue());
        if (fmlad01Data == null) {
            setError(itemBuzaino, "XHD-000219");
            return false;
        }
        String hinmei = StringUtil.nullToBlank(fmlad01Data.get("hinmei"));
        Timestamp yukokigen = (Timestamp) fmlad01Data.get("yukokigen");
        GXHDO102C011Model.SubGamenData showsubgamendata = this.getGxhdO102c011ModelView().getShowsubgamendata();
        FXHDD01 itemZairyohinmei;
        // 品名のチェック: 取得した品名と材料品名が一致していること
        itemZairyohinmei = showsubgamendata.getSubDataBuzaitab1().get(0); // 材料品名
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
        setBuzaihinmeiVal(this.getGxhdO102c011ModelView(), hinmei);

        return true;
    }

    /**
     * 部材在庫品名の値を設定
     *
     * @param gxhdo102c011model モデルデータ
     * @param hinmei 在庫品名
     */
    private void setBuzaihinmeiVal(GXHDO102C011Model gxhdo102c011model, String hinmei) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011model.getShowsubgamendata();
        // 部材在庫品名の値を設定
        showsubgamendata.getSubDataBuzaitab1().get(2).setValue(hinmei);
    }

    /**
     * 部材在庫ﾃﾞｰﾀの取得
     *
     * @param zaikono 在庫No
     * @return 部材在庫ﾃﾞｰﾀ情報取得
     */
    private Map<String, Object> getFmlad01Data(String zaikono) {
        try {
            QueryRunner queryRunner = new QueryRunner(getDataSourceMLAServer());
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
     * @param itemData 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面
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
     * DataSource(MLAServer)
     *
     * @return the dataSourceMLAServer
     */
    public DataSource getDataSourceMLAServer() {
        return dataSourceMLAServer;
    }

    /**
     * DataSource(MLAServer)
     *
     * @param dataSourceMLAServer the dataSourceMLAServer to set
     */
    public void setDataSourceMLAServer(DataSource dataSourceMLAServer) {
        this.dataSourceMLAServer = dataSourceMLAServer;
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ
     *
     * @return the gxhdO102c011Model
     */
    public GXHDO102C011Model getGxhdO102c011Model() {
        return gxhdO102c011Model;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ
     *
     * @param gxhdO102c011Model the gxhdO102c011Model to set
     */
    public void setGxhdO102c011Model(GXHDO102C011Model gxhdO102c011Model) {
        this.gxhdO102c011Model = gxhdO102c011Model;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ(表示制御用)
     *
     * @return the gxhdO102c011ModelView
     */
    public GXHDO102C011Model getGxhdO102c011ModelView() {
        return gxhdO102c011ModelView;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用データ(表示制御用)
     *
     * @param gxhdO102c011ModelView the gxhdO102c011ModelView to set
     */
    public void setGxhdO102c011ModelView(GXHDO102C011Model gxhdO102c011ModelView) {
        this.gxhdO102c011ModelView = gxhdO102c011ModelView;
    }
}
