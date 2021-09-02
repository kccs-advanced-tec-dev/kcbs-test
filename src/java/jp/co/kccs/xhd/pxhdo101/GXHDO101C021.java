/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO101C021Model;
import jp.co.kccs.xhd.model.GXHDO101C021Model.TorokuNoData;
import jp.co.kccs.xhd.util.StringUtil;

/**
 *
 * @author kokskgp10_nw
 */
/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/10<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C021(B･Cﾗﾝｸ連絡書一覧画面)
 *
 * @author SRC K.Ijuin
 * @since 2021/08/10
 */
@SessionScoped
@Named("beanGXHDO101C021")
public class GXHDO101C021 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101C021.class.getName());

    /**
     * 参照元ﾛｯﾄNo
     */
    private String sanshouMotoLotNo;

    /**
     * 登録No表示用データ
     */
    private GXHDO101C021Model gxhdO101c021Model;

    /**
     * 登録No表示用データ(表示制御用)
     */
    private GXHDO101C021Model gxhdO101c021ModelView;

    /**
     * ロットNo(検索値)
     */
    private String searchLotNo;

    /**
     * 担当者ｺｰﾄﾞ(検索値)
     */
    private String searchTantoshaCd;

    /**
     * 表示render有無
     */
    private boolean tourokuNoTableRender;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;

    /**
     * コンストラクタ
     */
    public GXHDO101C021() {
    }

    /**
     * フォームエラー判定
     *
     * @return isFormError
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
     * @return the sanshouMotoLotNo
     */
    public String getSanshouMotoLotNo() {
        return sanshouMotoLotNo;
    }

    /**
     * @param sanshouMotoLotNo the sanshouMotoLotNo to set
     */
    public void setSanshouMotoLotNo(String sanshouMotoLotNo) {
        this.sanshouMotoLotNo = sanshouMotoLotNo;
    }

    /**
     * @return the gxhdO101c021Model
     */
    public GXHDO101C021Model getGxhdO101c021Model() {
        return gxhdO101c021Model;
    }

    /**
     * @param gxhdO101c021Model the gxhdO101c021Model to set
     */
    public void setGxhdO101c021Model(GXHDO101C021Model gxhdO101c021Model) {
        this.gxhdO101c021Model = gxhdO101c021Model;
    }

    /**
     * @return the gxhdO101c021ModelView
     */
    public GXHDO101C021Model getGxhdO101c021ModelView() {
        return gxhdO101c021ModelView;
    }

    /**
     * @param gxhdO101c021ModelView the gxhdO101c021ModelView to set
     */
    public void setGxhdO101c021ModelView(GXHDO101C021Model gxhdO101c021ModelView) {
        this.gxhdO101c021ModelView = gxhdO101c021ModelView;
    }

    /**
     * @return the tourokuNoTableRender
     */
    public boolean isTourokuNoTableRender() {
        return tourokuNoTableRender;
    }

    /**
     * @param tourokuNoTableRender the tourokuNoTableRender to set
     */
    public void setTourokuNoTableRender(boolean tourokuNoTableRender) {
        this.tourokuNoTableRender = tourokuNoTableRender;
    }

    /**
     * 品質確認連絡書の画面を表示する
     *
     * @return
     */
    public String openXhdFormHinsitsu(String torokuNo) {

        // セッション情報
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        // ①登録Noに表示している一覧表を配列形式で変数化する
        String[] torokuNoArray = createTorokuNoListToArray(this.gxhdO101c021ModelView.getTorokuNoDataList());

        // ②ｸﾘｯｸした行数を変数化する。
        // 選択した登録Noの配列Indexを取得
        String torokuNoIndex = Integer.valueOf(Arrays.asList(torokuNoArray).indexOf(torokuNo)).toString();

        // 品質確認連絡書への値の受渡し
        // 登録No配列Index
        session.setAttribute("torokuNoIndex", StringUtil.blankToNull(torokuNoIndex));
        // 登録No配列: session送受信時はObject型として処理されるので受取時に元の型への復元処理を行うこと
        session.setAttribute("torokuNoArray", torokuNoArray);
        // ロットNo(検索値)を保持
        String sLotNo = this.searchLotNo;
        session.setAttribute("searchLotNo", sLotNo);
        // 担当者ｺｰﾄﾞ(検索値)を保持
        String sTantoshaCd = this.searchTantoshaCd;
        session.setAttribute("searchTantoshaCd", sTantoshaCd);
        
        // ③変数化した上記二項目を引数にして品質確認連絡書の画面を表示する。
        return "/secure/pxhdo101/gxhdo101d001.xhtml?faces-redirect=true";
    }

    /**
     * 登録Noに表示している一覧表を配列形式で変数化する
     *
     * @param torokuNoList
     * @return
     */
    private String[] createTorokuNoListToArray(List<TorokuNoData> torokuNoList) {

        // 登録NoのみのArrayListを作成する
        List<String> torokuList = new ArrayList<>();
        for (TorokuNoData data : torokuNoList) {
            torokuList.add(data.getTorokuNo());
        }
        String[] torokuNoArray = torokuList.toArray(new String[torokuList.size()]);
        return torokuNoArray;
    }

    /**
     * @return the searchLotNo
     */
    public String getSearchLotNo() {
        return searchLotNo;
    }

    /**
     * @param searchLotNo the searchLotNo to set
     */
    public void setSearchLotNo(String searchLotNo) {
        this.searchLotNo = searchLotNo;
    }

    /**
     * @return the searchTantoshaCd
     */
    public String getSearchTantoshaCd() {
        return searchTantoshaCd;
    }

    /**
     * @param searchTantoshaCd the searchTantoshaCd to set
     */
    public void setSearchTantoshaCd(String searchTantoshaCd) {
        this.searchTantoshaCd = searchTantoshaCd;
    }

}
