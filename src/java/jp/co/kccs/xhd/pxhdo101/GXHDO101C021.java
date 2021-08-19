/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;
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

}
