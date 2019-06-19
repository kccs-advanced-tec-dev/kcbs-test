/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/11/14<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD101A(品質DB入力機能) 入力画面選択
 *
 * @author KCSS K.Jo
 * @since 2018/11/14
 */
@Named
@ViewScoped
public class GXHDO101A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101A.class.getName());
    private static final String CHARSET = "MS932";
    private static final int LOTNO_BYTE = 14;
    private static final int TANTOSHA_CODE_BYTE = 6;

    /**
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceXHD;
    /**
     * メニュー項目
     */
    private List<FXHDM01> menuListGXHDO101;
    /**
     * ロットNo
     */
    private String lotNo;
    /**
     * 画面ID
     */
    private String gamenID;
    /**
     * 表示render有無
     */
    private boolean menuTableRender;
    /**
     * 情報メッセージ
     */
    private String infoMessage;
    /**
     * 警告メッセージ
     */
    private String warnMessage;
    /**
     * エラーメッセージ
     */
    private String errorMessage;
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantoshaCd;

    /**
     * ロットNo(検索値)
     */
    private String searchLotNo;

    /**
     * 担当者ｺｰﾄﾞ(検索値)
     */
    private String searchTantoshaCd;

    /**
     * リンクページ
     */
    private String linkPage;

    /**
     * コンストラクタ
     */
    public GXHDO101A() {
    }

    /**
     * 起動時処理
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String sLotNo = StringUtil.nullToBlank(session.getAttribute("lotNo"));
        String sTantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
        this.tantoshaCd = sTantoshaCd;

        session.setAttribute("lotNo", "");
        session.setAttribute("tantoshaCd", "");
        if (!StringUtil.isEmpty(sLotNo)) {
            this.lotNo = sLotNo;
            getMenuListGXHDO101();
        }
    }

    /**
     * 一覧取得処理
     *
     * @return 一覧データ
     */
    public List<FXHDM01> getMenuListGXHDO101() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        menuListGXHDO101 = new ArrayList<>();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // user-agent
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        String model = getModel.getModel();

        // userAgentでPC or タブレットを判定
        boolean isPC = false;
        if (model.equals("ie11")) {
            isPC = true;
        }

        // ユーザーグループでメニューマスタを検索
        try {

            // ロットNoを取得する
            String strGamenLotNo = getLotNo();

            String strProcess = "";

            // 担当者ｺｰﾄﾞ桁数チェック
            if (TANTOSHA_CODE_BYTE != StringUtil.getByte(this.tantoshaCd, CHARSET, LOGGER)) {
                setErrorMessage(MessageUtil.getMessage("XHD-000004", "担当者ｺｰﾄﾞ", TANTOSHA_CODE_BYTE));
                setMenuTableRender(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // ロットNo桁数チェック
            if (LOTNO_BYTE != StringUtil.getByte(strGamenLotNo, CHARSET, LOGGER)) {
                setErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                setMenuTableRender(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // ﾛｯﾄNoのﾁｪｯｸﾃﾞｼﾞｯﾄﾁｪｯｸ
            ValidateUtil validateUtil = new ValidateUtil();
            String retMsg = validateUtil.checkValueE001(strGamenLotNo);

            if (!StringUtil.isEmpty(retMsg)) {
                setMenuTableRender(false);
                setErrorMessage(retMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            retMsg = validateUtil.checkT002("担当者ｺｰﾄﾞ", this.tantoshaCd, queryRunnerWip);
            if (!StringUtil.isEmpty(retMsg)) {
                setMenuTableRender(false);
                setErrorMessage(retMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            String strKojyo = strGamenLotNo.substring(0, 3);
            String strLotNo = strGamenLotNo.substring(3, 11);

            QueryRunner queryRunnerXHD = new QueryRunner(dataSourceXHD);
            String sqlsearchProcess = "SELECT PrintFmt FROM da_sekkei WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
            List<Object> params = new ArrayList<>();
            params.add(strKojyo);
            params.add(strLotNo);
            params.add("001");
            List processResult = (List) queryRunnerXHD.query(sqlsearchProcess, new MapListHandler(), params.toArray());
            for (Iterator i = processResult.iterator(); i.hasNext();) {
                HashMap m = (HashMap) i.next();
                strProcess = m.get("PrintFmt").toString();
            }

            if (processResult.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            String sqlsearchGamenID = "SELECT gamen_id, kotei_jun FROM fxhdm03 WHERE kotei_process_kubun = ? order by kotei_jun ";
            List<Object> params2 = new ArrayList<>();
            params2.add(strProcess);

            List sqlsearchResult = (List) queryRunnerDoc.query(sqlsearchGamenID, new MapListHandler(), params2.toArray());

            List<Object> listGamenID = new ArrayList<>();
            for (Iterator i = sqlsearchResult.iterator(); i.hasNext();) {
                HashMap m = (HashMap) i.next();
                listGamenID.add(m.get("gamen_id").toString());
            }

            if (sqlsearchResult.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            String sql = "SELECT gamen_id,gamen_title,title_setting,menu_no,menu_name,link_char,menu_parameter,menu_comment,gamen_classname,hyouji_kensu "
                    + "FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";

            if (!listGamenID.isEmpty()) {
                sql += " AND ";
                sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
            }
            sql += " AND " + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

            if (!isPC) {
                sql += " AND pc_flg = '0'";
            }

            sql += " ORDER BY menu_no ASC ";

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "formId");
            mapping.put("gamen_title", "formTitle");
            mapping.put("title_setting", "titleSetting");
            mapping.put("menu_no", "menuNo");
            mapping.put("menu_name", "menuName");
            mapping.put("link_char", "linkChar");
            mapping.put("menu_parameter", "menuParam");
            mapping.put("menu_comment", "menuComment");
            mapping.put("gamen_classname", "formClassName");
            mapping.put("hyouji_kensu", "hyojiKensu");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);

            List<Object> params3 = new ArrayList<>();
            params3.addAll(listGamenID);
            params3.addAll(userGrpList);

            if (listGamenID.isEmpty()) {
                DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
                menuListGXHDO101 = queryRunnerDoc.query(sql, beanHandler, userGrpList.toArray());
            } else {
                DBUtil.outputSQLLog(sql, params3.toArray(), LOGGER);
                menuListGXHDO101 = queryRunnerDoc.query(sql, beanHandler, params3.toArray());
            }

            if (menuListGXHDO101.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
            } else {
                setMenuTableRender(true);

                // 検索した時点のﾛｯﾄNoと担当者ｺｰﾄﾞを保持する。
                this.searchLotNo = this.lotNo;
                this.searchTantoshaCd = this.tantoshaCd;

            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
            menuListGXHDO101 = new ArrayList<>();
        }

        return menuListGXHDO101;
    }

    /**
     * 品質DBメンテナンス画面open
     *
     * @param rowData 選択行のデータ
     * @return 遷移先画面
     */
    public String openXhdForm(FXHDM01 rowData) {
        try {
            // 権限なしでメニューの画面IDリストを取得
            List<String[]> gamenInfoList = getGamenInfoList();
            if (gamenInfoList.isEmpty()) {
                //データが存在しない場合、エラー
                FacesContext facesContext = FacesContext.getCurrentInstance();
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return "";
            }
            // 前画面の画面ID、メニュー名を取得
            String[] maeKoteiGamenInfo = getMaeKoteiGamenInfo(gamenInfoList, rowData.getFormId());

            // 実績Noを取得
            int jissekino = getJissekino(gamenInfoList, rowData.getFormId());

            // 前工程画面IDを取得している場合、前工程情報を取得
            Map maekoteiInfo = null;
            if (maeKoteiGamenInfo != null) {
                maekoteiInfo = getMaeKoteiInfo(gamenInfoList, maeKoteiGamenInfo[0]);
            }

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);

            // 画面遷移パラメータをセッション変数に保持
            session.setAttribute("formId", rowData.getFormId());
            session.setAttribute("callerFormId", "GXHDO101A");
            session.setAttribute("formTitle", rowData.getFormTitle());
            session.setAttribute("menuName", rowData.getMenuName());
            session.setAttribute("menuComment", rowData.getMenuComment());
            session.setAttribute("titleSetting", rowData.getTitleSetting());
            session.setAttribute("formClassName", rowData.getFormClassName());
            session.setAttribute("hyojiKensu", rowData.getHyojiKensu());
            session.setAttribute("lotNo", this.searchLotNo);
            session.setAttribute("tantoshaCd", this.searchTantoshaCd);
            session.setAttribute("jissekino", jissekino);
            session.setAttribute("maekoteiInfo", maekoteiInfo);
            if (maeKoteiGamenInfo != null) {
                session.setAttribute("maekoteiFormId", maeKoteiGamenInfo[0]);
            }
           
            // 前工程が存在するかつ前工程のデータが取得できなかった場合
            if (maeKoteiGamenInfo != null && maekoteiInfo == null) {
                this.warnMessage = MessageUtil.getMessage("XHD-000035", maeKoteiGamenInfo[1]);
                this.linkPage = rowData.getLinkChar() + "?faces-redirect=true";
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "warning");
                return "";
            }

            return rowData.getLinkChar() + "?faces-redirect=true";
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
        }
        return "";

    }

    /**
     * 品質DB画面データ検索
     */
    public void searchHinshitsuData() {
        menuListGXHDO101 = getMenuListGXHDO101();
    }

    /**
     * 一覧表示データを返却します。
     *
     * @return 一覧データ
     */
    public List<FXHDM01> getListData() {
        return this.menuListGXHDO101;
    }

    /**
     * 前画面に戻ります。
     *
     * @return ログイン用URL文字列
     */
    public String btnReturn() {
        return "/pxhdo012/gxhdo012a.xhtml?faces-redirect=true";
    }

    /**
     * 画面ID
     *
     * @return the GamenID
     */
    public String getGamenID() {
        return gamenID;
    }

    /**
     * 画面ID
     *
     * @param gamenID
     */
    public void setGamenID(String gamenID) {
        this.gamenID = gamenID;
    }

    /**
     * ロットNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * ロットNo
     *
     * @param lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 表示render有無
     *
     * @param menuTableRender
     */
    public void setMenuTableRender(boolean menuTableRender) {
        this.menuTableRender = menuTableRender;
    }

    /**
     * 表示render有無
     *
     * @return menuTableRender
     */
    public boolean getMenuTableRender() {
        return menuTableRender;
    }

    /**
     * エラーメッセージ
     *
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * エラーメッセージ
     *
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 情報メッセージ
     *
     * @return the infoMessage
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * 情報メッセージ
     *
     * @param infoMessage the infoMessage to set
     */
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * 警告メッセージ
     *
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     *
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    /**
     * リンクページ
     *
     * @return the linkPage
     */
    public String getLinkPage() {
        return linkPage;
    }

    /**
     * リンクページ
     *
     * @param linkPage the linkPage to set
     */
    public void setLinkPage(String linkPage) {
        this.linkPage = linkPage;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantoshaCd
     */
    public String getTantoshaCd() {
        return tantoshaCd;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantoshaCd the tantoshaCd to set
     */
    public void setTantoshaCd(String tantoshaCd) {
        this.tantoshaCd = tantoshaCd;
    }

    /**
     * 権限の絞りこみなしで対象機能の画面IDのリストを取得
     *
     * @return メニューリスト
     * @throws SQLException
     */
    private List<String[]> getGamenInfoList() throws SQLException {
        List<String[]> gamenIdList = new ArrayList<>();

        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);

        // (1)設計ﾃｰﾌﾞﾙより、ﾌﾟﾛｾｽを取得する
        String strProcess = "";
        QueryRunner queryRunnerXHD = new QueryRunner(dataSourceXHD);
        String sqlsearchProcess = "SELECT PrintFmt FROM da_sekkei WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        List<Object> params = new ArrayList<>();
        params.add(strKojyo);
        params.add(strLotNo);
        params.add("001");

        DBUtil.outputSQLLog(sqlsearchProcess, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerXHD.query(sqlsearchProcess, new MapListHandler(), params.toArray());
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            strProcess = m.get("PrintFmt").toString();
        }

        // データが取得できなかった場合
        if (processResult.isEmpty()) {
            return gamenIdList;
        }

        //(2)ﾌﾟﾛｾｽﾏｽﾀより、画面IDを取得する
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        String sqlsearchGamenID = "SELECT gamen_id FROM fxhdm03 WHERE kotei_process_kubun = ? ";
        List<Object> params2 = new ArrayList<>();
        params2.add(strProcess);

        DBUtil.outputSQLLog(sqlsearchGamenID, params2.toArray(), LOGGER);
        List sqlsearchGamenId = (List) queryRunnerDoc.query(sqlsearchGamenID, new MapListHandler(), params2.toArray());

        List<String> listGamenID = new ArrayList<>();
        for (Iterator i = sqlsearchGamenId.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            listGamenID.add(m.get("gamen_id").toString());
        }

        if (sqlsearchGamenId.isEmpty()) {
            return gamenIdList;
        }

        //(9)ﾒﾆｭｰﾏｽﾀから、ﾃﾞｰﾀを取得
        String sql = "SELECT gamen_id,menu_name "
                + "FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";
        if (!listGamenID.isEmpty()) {
            sql += " AND ";
            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
        }
        sql += " ORDER BY menu_no ASC ";

        List<Object> params3 = new ArrayList<>();
        params3.addAll(listGamenID);
        DBUtil.outputSQLLog(sql, params3.toArray(), LOGGER);
        List sqlsearchMenuData = (List) queryRunnerDoc.query(sql, new MapListHandler(), params3.toArray());
        for (Iterator i = sqlsearchMenuData.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            gamenIdList.add(new String[]{StringUtil.nullToBlank(m.get("gamen_id")), StringUtil.nullToBlank(m.get("menu_name"))});
        }

        return gamenIdList;
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT rev, jotai_flg, edaban "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 前工程画面ID、ﾒﾆｭｰ名取得
     *
     * @param gamenIdList 画面IDリスト
     * @param gamenId 画面ID
     * @return 前工程画面ID
     */
    private String[] getMaeKoteiGamenInfo(List<String[]> gamenIdList, String gamenId) {
        // 画面IDリストから指定の画面IDの一つ前の画面IDを取得
        String[] maeKoteiInfo = null;
        for (String[] compGamenInfo : gamenIdList) {
            if (gamenId.equals(compGamenInfo[0])) {
                return maeKoteiInfo;
            }
            maeKoteiInfo = compGamenInfo;
        }
        return maeKoteiInfo;
    }

    /**
     * 画面ID別の実績No取得
     *
     * @param gamenId 画面ID
     * @param gamenIdList 画面IDリスト
     * @return 実績No
     */
    private int getJissekino(List<String[]> gamenInfoList, String gamenId) {
        switch (gamenId) {
            case "GXHDO101B001": //印刷･SPSｸﾞﾗﾋﾞｱ
                return 1;
            case "GXHDO101B002": //印刷･SPSｽｸﾘｰﾝ
                return 1;
            case "GXHDO101B003": //印刷･RSUS
                return 1;
            case "GXHDO101B004": //積層･SPS
                return 1;
            case "GXHDO101B005": //積層･RSUS
                return 1;
            case "GXHDO101B006": //印刷積層･RHAPS
                return 1;
            case "GXHDO101B007": //ﾌﾚｽ･仮ﾌﾟﾚｽ
                return 1;
            case "GXHDO101B008": //ﾌﾚｽ･真空脱気
                return 1;
            case "GXHDO101B009": //ﾌﾚｽ･ﾒｶﾌﾟﾚｽ
                return 1;
            case "GXHDO101B010": //ｶｯﾄ･押切ｶｯﾄ
                return 1;
            case "GXHDO101B011": //ｶｯﾄ･ﾀﾞｲｼﾝｸﾞｶｯﾄ
                return 1;
            case "GXHDO101B012": //生品質検査
                return 1;
            case "GXHDO101B013": //焼成･ｾｯﾀ詰め
                return 1;
            case "GXHDO101B014": //焼成･Air脱脂
                return 1;
            case "GXHDO101B015": //焼成･窒素脱脂
                // リストに焼成･Air脱脂の画面IDが存在する場合、2をリターン
                for (String[] gamenInfo : gamenInfoList) {
                    if ("GXHDO101B014".equals(gamenInfo[0])) {
                        return 2;
                    }
                }
                return 1;
            case "GXHDO101B016": //焼成･2次脱脂(ﾍﾞﾙﾄ)
                return 1;
            case "GXHDO101B017": //焼成･焼成
                return 1;
            case "GXHDO101B018": //焼成･RHK焼成
                return 1;
            case "GXHDO101B019": //焼成･再酸化
                return 1;
            case "GXHDO101B020": //ﾞﾚﾙ･研磨
                return 1;
            case "GXHDO101B021": //磁器QC
                return 1;
            default:
                return 1;
        }
    }

    /**
     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
     *
     * @param map マップ
     * @param mapId ID
     * @return マップから取得した値
     */
    private Object getMapData(Map map, String mapId) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.get(mapId);
    }

    /**
     * 前工程情報の取得
     *
     * @param gamenInfoList 画面IDリスト
     * @param maeKoteiGamenId 前工程画面ID
     * @return 前工程情報
     * @throws SQLException 例外エラー
     */
    private Map getMaeKoteiInfo(List<String[]> gamenInfoList, String maeKoteiGamenId) throws SQLException {
        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

        // 前工程の実績Noを取得
        int maeKoteijissekino = getJissekino(gamenInfoList, maeKoteiGamenId);

        // 前工程の実績情報を取得
        Map fxhdd03Info = getJissekiInfo(queryRunnerDoc, queryRunnerWip, strKojyo, strLotNo, strEdaban, maeKoteiGamenId, maeKoteijissekino);
        String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03Info, "jotai_flg")); //状態フラグ

        // 登録済み実績情報があった場合
        if ("1".equals(jotaiFlg)) {
            return CommonUtil.getMaeKoteiData(queryRunnerQcdb, maeKoteiGamenId, strKojyo, strLotNo,
                    StringUtil.nullToBlank(getMapData(fxhdd03Info, "edaban")), StringUtil.nullToBlank(getMapData(fxhdd03Info, "rev")), maeKoteijissekino);
        }
        return null;
    }

    /**
     * 実績情報の取得
     *
     * @param queryRunnerDoc
     * @param queryRunnerWip
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param gamenId 画面ID
     * @param jissekino 実績No
     * @return
     * @throws SQLException
     */
    private Map getJissekiInfo(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip, String kojyo, String lotNo, String edaban, String gamenId, int jissekino) throws SQLException {

        // 実績を検索
        Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo, edaban, gamenId, jissekino);
        // 状態フラグが空(実績がない)の場合、自身の親の実績を探しに行く
        if (StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg")))) {

            // 親ﾛｯﾄの枝番
            String oyalotEdaban = getOyaLotEdaban(queryRunnerWip, kojyo, lotNo, edaban);
            if (StringUtil.isEmpty(oyalotEdaban)) {
                // 該当データがない場合はリターン
                return null;
            }

            // 親ﾛｯﾄの実績を検索
            return getJissekiInfo(queryRunnerDoc, queryRunnerWip, kojyo, lotNo, oyalotEdaban, gamenId, jissekino);
        }

        return fxhdd03RevInfo;
    }

    /**
     * 親ﾛｯﾄ枝番の取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private String getOyaLotEdaban(QueryRunner queryRunnerWip, String kojyo, String lotNo, String edaban) throws SQLException {

        // 仕掛情報データの取得
        String sql = "SELECT oyalotedaban "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sikakariInfo = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        return StringUtil.nullToBlank(getMapData(sikakariInfo, "oyalotedaban"));
    }
}
