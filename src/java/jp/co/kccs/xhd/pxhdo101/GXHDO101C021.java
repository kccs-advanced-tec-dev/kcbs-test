/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
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
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

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
    public String openXhdFormHinsitsu() {

        // セッション情報
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.setAttribute("lotNo", "");

        // args: String torokuNo
//        // ①登録Noに表示している一覧表を配列形式で変数化する
//        String[] tourokuNoArray = createTorokuNoListToArray(this.gxhdO101c021ModelView.getTorokuNoDataList());
//        // ②ｸﾘｯｸした行数を変数化する。
//        int getIndex = Arrays.asList(tourokuNoArray).indexOf(torokuNo);
//        //TODO index見つからなかった場合
//        System.out.println(getIndex);
//        LOGGER.log(Level.INFO, "torokuNo index: %d", getIndex);

        // 品質確認連絡書bean初期化。必要な設定を追加する。

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
        String[] torokuNoArray = torokuNoList.toArray(new String[torokuNoList.size()]);
        return torokuNoArray;
    }

}
