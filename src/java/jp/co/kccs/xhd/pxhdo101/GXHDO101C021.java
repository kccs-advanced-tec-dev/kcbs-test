/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.SrKoteifuryo;
import jp.co.kccs.xhd.model.GXHDO101C021Model;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;

/**
 *
 * @author kokskgp10_nw
 */
@SessionScoped
@Named("beanGXHDO101C021")
public class GXHDO101C021 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());

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


}
