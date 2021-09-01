/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo501;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	原材料規格管理機能(コンデンサ)<br>
 * <br>
 * 変更日	2021/07/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * GXHD501(原材料規格管理機能)
 * 
 * @author KCSS K.Jo
 * @since  2021/07/14
 */
@Named
@ViewScoped
public class GXHDO501 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO501.class.getName());
    
    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSource;
        
    /**
     * メニュー項目
     */
    private List<FXHDM01> menuList;
    /**
     * コンストラクタ
     */
    public GXHDO501() {
    }
    
    /**
     * メニュー項目取得
     * @return メニュー項目リスト
     */
    public List<FXHDM01> getMenuList() {
        return menuList;
    }
    
    /**
     * 一覧取得処理
     */
    public void selectMenuList() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group");
        
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }
        
        List<FXHDM01> menuListData = new ArrayList<>();
                
        if (0 == userGrpList.size()) {
            // ユーザーグループ未登録の場合、ブランクメニューを表示する
        } else {
            // ユーザーグループでメニューマスタを検索
            try {
                QueryRunner queryRunner = new QueryRunner(dataSource);
                String sql = "SELECT gamen_id, gamen_title, title_setting, link_char, menu_name, menu_comment, menu_parameter, gamen_classname, hyouji_kensu FROM fxhdm01 "
                           + "WHERE menu_group_id = 'mkqcdb_sekkei' AND " 
                        + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());
                
                sql += " ORDER BY menu_no ";

                Map<String, String> mapping = new HashMap<>();
                mapping.put("gamen_id", "formId");
                mapping.put("gamen_title", "formTitle");
                mapping.put("title_setting", "titleSetting");
                mapping.put("menu_name", "menuName");
                mapping.put("link_char", "linkChar");
                mapping.put("menu_parameter", "menuParam");
                mapping.put("menu_comment", "menuComment");
                mapping.put("gamen_classname", "formClassName");
                mapping.put("hyouji_kensu", "hyojiKensu");

                BeanProcessor beanProcessor = new BeanProcessor(mapping);
                RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);
                
                DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
                menuListData = queryRunner.query(sql, beanHandler, userGrpList.toArray());    
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
                menuListData = new ArrayList<>();
            }
        }
        
        menuList = menuListData;
    }
    
    /**
     * 品質DBメンテナンス画面open
     * @param rowData 選択行のデータ
     * @return 遷移先画面
     */
    public String openXhdForm(FXHDM01 rowData) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        session.setAttribute("menuParam", rowData.getMenuParam());
        session.setAttribute("hyojiKensu", rowData.getHyojiKensu());
        
        return rowData.getLinkChar() + "?faces-redirect=true";
    }
    
    /**
     * ログアウトします。
     * 
     * @return ログイン用URL文字列
     */
    public String logOut() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch(IllegalStateException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        // goto login
        return "/login.xhtml?faces-redirect=true";
    }
}
