/*
 * Copyright 2017 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	電子配信システム(コンデンサ)<br>
 * <br>
 * 変更日	2017/02/15<br>
 * 計画書No	K1604-DS003<br>
 * 変更者	KCCS R.Fujimura<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * アプリケーションの設定クラスです。
 *
 * @author KCCS R.Fujimura
 * @since 2017/02/18
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(jp.co.kccs.xhd.jaxrs.PublicResource.class);
    }
    
}
