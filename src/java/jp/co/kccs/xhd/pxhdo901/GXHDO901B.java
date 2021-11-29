/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.common.CompMessage;
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.common.InfoMessage;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.ParameterEJB;
import jp.co.kccs.xhd.db.model.DaMkJoken;
import jp.co.kccs.xhd.db.model.DaMkhyojunjoken;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDD02;
import jp.co.kccs.xhd.db.model.FXHDM02;
import jp.co.kccs.xhd.db.model.FXHDM05;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C001;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C001Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C002;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C002Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C004;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C004Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C005;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C005Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C006;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C006Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C007;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C007Logic;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C008;
import jp.co.kccs.xhd.pxhdo102.GXHDO102C008Logic;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C009;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C009Logic;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C010;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C010Logic;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C011;
//import jp.co.kccs.xhd.pxhdo102.GXHDO102C011Logic;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datalist.DataList;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/08/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO901B(品質DB原料入力機能画面共通)
 *
 * @author KCSS K.Jo
 * @since  2021/08/16
 */
@Named
@ViewScoped
public class GXHDO901B implements Serializable {

    protected static final Logger LOGGER = Logger.getLogger(GXHDO901B.class.getName());
    private static final int LOTNO_ITEMNO = 100;
    
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    protected transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    protected transient DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    protected transient DataSource dataSourceWip;
    /**
     * DataSource(Spskadoritu)
     */
    @Resource(mappedName = "jdbc/spskadoritu")
    protected transient DataSource dataSourceSpskadoritu;
    /**
     * DataSource(Ttpkadoritu)
     */
    @Resource(mappedName = "jdbc/ttpkadoritu")
    protected transient DataSource dataSourceTtpkadoritu;
    /**
     * DataSource(equipment)
     */
    @Resource(mappedName = "jdbc/equipment")
    protected transient DataSource dataSourceEquipment;
    /**
     * パラメータ操作(DB)
     */
    @Inject
    protected ParameterEJB parameterEJB;

    /**
     * 起動時処理
     */
    protected String onLoadProcess = "";

    /**
     * タイトル情報
     */
    protected FXHDM02 titleInfo;
    /**
     * 画面上部ボタン
     */
    protected List<FXHDD02> buttonListTop;
    /**
     * 画面下部ボタン
     */
    protected List<FXHDD02> buttonListBottom;
    /**
     * チェックリスト
     */
    protected List<FXHDM05> checkListHDM05;
    /**
     * DaMkJokenリスト
     */
    protected List<DaMkJoken> listDaMkJoken;
    /**
     * DaMkhyojunjokenリスト
     */
    protected List<DaMkhyojunjoken> listDaMkhyojunjoken;
    /**
     *
     * 項目データ
     */
    protected List<FXHDD01> itemList;
    /**
     *
     * 項目データ(拡張)※複数画面IDの情報保持
     */
    protected List<FXHDD01> itemListEx;
    /**
     * 処理データ
     */
    protected ProcessData processData = null;
    /**
     * 警告ダイアログ
     */
    protected boolean warnDialogRendered = false;
    /**
     * ユーザー認証：ユーザー
     */
    private String authUser;
    /**
     * ユーザー認証：パスワード
     */
    private String authPassword;

    /**
     * チェック無しボタンIDリスト(設定しているIDについてはエラー処理の背景色をクリアしない)
     */
    protected List<String> noCheckButtonId;

    /**
     * 一覧の表示件数
     */
    protected String hyojiKensu;

    /**
     * displayNoneを設定する(致命的な画面表示エラー時)
     */
    private String styleDisplayNone = "";

    /**
     * リビジョン(起動時)
     */
    private String initRev = "";

    /**
     * 状態フラグ(起動時)
     */
    private String initJotaiFlg = "";

    /**
     * 状態表示
     */
    private String jotaiDisplay = "";

    /**
     * リビジョンチェック対象ボタンID
     */
    private List<String> checkRevisionButtonId;

    /**
     * 完了メッセージ
     */
    private String compMessage;

    /**
     * 入力項目最大幅
     */
    private String maxWidthInputCol;
    
    /**
     * 隠し項目MAP
     */
    private Map<String, Object> hiddenDataMap = new HashMap<>();


    /**
     * コンストラクタ
     */
    public GXHDO901B() {
    }

    /**
     * フォームID取得
     *
     * @return the formId
     */
    public String getFormId() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        return StringUtil.nullToBlank(session.getAttribute("formId"));
    }

    /**
     * 起動時処理
     *
     * @return the onLoadProcess
     */
    public String getOnLoadProcess() {
        return onLoadProcess;
    }

    /**
     * 起動時処理
     *
     * @param onLoadProcess the onLoadProcess to set
     */
    public void setOnLoadProcess(String onLoadProcess) {
        this.onLoadProcess = onLoadProcess;
    }

    /**
     * タイトル情報
     *
     * @return タイトル情報
     */
    public FXHDM02 getTitleInfo() {
        return titleInfo;
    }

    /**
     * 画面上部ボタン
     *
     * @return the buttonListTop
     */
    public List<FXHDD02> getButtonListTop() {
        return buttonListTop;
    }

    /**
     * 画面下部ボタン
     *
     * @return the buttonListBottom
     */
    public List<FXHDD02> getButtonListBottom() {
        return buttonListBottom;
    }

    /**
     * 項目データ
     *
     * @return the itemList
     */
    public List<FXHDD01> getItemList() {
        return itemList;
    }
    /**
     * 項目データ(拡張)※複数画面IDの情報保持
     * @return the itemListEx
     */
    public List<FXHDD01> getItemListEx() {
        return itemListEx;
    }
    
    /**
     * 項目データ(拡張)※複数画面IDの情報保持
     * @param itemListEx
     */
    public void setItemListEx(List<FXHDD01> itemListEx) {
        this.itemListEx = itemListEx;
    }

    /**
     * 処理データ
     *
     * @return the processData
     */
    public ProcessData getProcessData() {
        return processData;
    }

    /**
     * 警告ダイアログ
     *
     * @return the warnDialogRendered
     */
    public boolean getWarnDialogRendered() {
        return warnDialogRendered;
    }

    /**
     * 入力項目表示有無
     *
     * @return true or false
     */
    public boolean getItemRendered() {
        if (null == this.itemList || 0 == this.itemList.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ユーザー認証：ユーザー
     *
     * @return the authUser
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * ユーザー認証：ユーザー
     *
     * @param authUser the authUser to set
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * ユーザー認証：パスワード
     *
     * @return the authPassword
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * ユーザー認証：パスワード
     *
     * @param authPassword the authPassword to set
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * チェック無しボタンIDリスト
     *
     * @return the noCheckButtonId
     */
    public List<String> getNoCheckButtonId() {
        return noCheckButtonId;
    }

    /**
     * チェック無しボタンIDリスト
     *
     * @param noCheckButtonId the noCheckButtonId to set
     */
    public void setNoCheckButtonId(List<String> noCheckButtonId) {
        this.noCheckButtonId = noCheckButtonId;
    }

    /**
     * 一覧の表示件数
     *
     * @return the hyojiKensu
     */
    public String getHyojiKensu() {
        return hyojiKensu;
    }

    /**
     * 一覧の表示件数
     *
     * @param hyojiKensu the hyojiKensu to set
     */
    public void setHyojiKensu(String hyojiKensu) {
        this.hyojiKensu = hyojiKensu;
    }

    /**
     * displayNone設定
     *
     * @return the styleDisplayNone
     */
    public String getStyleDisplayNone() {
        return styleDisplayNone;
    }

    /**
     * displayNone設定
     *
     * @param styleDisplayNone the styleDisplayNone to set
     */
    public void setStyleDisplayNone(String styleDisplayNone) {
        this.styleDisplayNone = styleDisplayNone;
    }

    /**
     * 状態表示
     *
     * @return the jotaiDisplay
     */
    public String getJotaiDisplay() {
        return jotaiDisplay;
    }

    /**
     * 状態表示
     *
     * @param jotaiDisplay the jotaiDisplay to set
     */
    public void setJotaiDisplay(String jotaiDisplay) {
        this.jotaiDisplay = jotaiDisplay;
    }

    /**
     * 完了メッセージ
     *
     * @return the compMessage
     */
    public String getCompMessage() {
        return compMessage;
    }

    /**
     * 完了メッセージ
     *
     * @param compMessage the compMessage to set
     */
    public void setCompMessage(String compMessage) {
        this.compMessage = compMessage;
    }

    /**
     * 入力項目最大幅
     *
     * @return the maxWidthInputCol
     */
    public String getMaxWidthInputCol() {
        return maxWidthInputCol;
    }

    /**
     * 入力項目最大幅
     *
     * @param maxWidthInputCol the maxWidthInputCol to set
     */
    public void setMaxWidthInputCol(String maxWidthInputCol) {
        this.maxWidthInputCol = maxWidthInputCol;
    }

    /**
     * 起動時処理
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (IOException e) {
                ErrUtil.outputErrorLog("IOException発生", e, LOGGER);
            }
            return;
        }

        this.setOnLoadProcess("");

        // 画面遷移パラメータ取得
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String callerFormId = StringUtil.nullToBlank(session.getAttribute("callerFormId"));
        String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
        String titleSetting = StringUtil.nullToBlank(session.getAttribute("titleSetting"));
        String lotNo = StringUtil.nullToBlank(session.getAttribute("lotNo"));

        // 一覧の表示件数を設定
        this.hyojiKensu = StringUtil.nullToBlank(session.getAttribute("hyojiKensu"));
        // 入力項目の最大幅を設定する。
        this.maxWidthInputCol = "475";

        // タイトル設定情報取得
        if (!this.loadTitleSettings(titleSetting, formTitle)) {
            settingError();
            return;
        }

        // ボタン定義情報取得
        if (!this.loadButtonSettings(formId)) {
            settingError();
            return;
        }

        // 項目定義情報取得
        if (!this.loadItemSettings(formId, callerFormId)) {
            settingError();
            return;
        }

        // チェック処理情報取得
        this.loadCheckList(formId);

        // SEKKEINO取得 (5)前工程設計ﾃﾞｰﾀより、情報を取得する
        Map mapSekkei = this.getSekkeiNo(lotNo);

        if (null != mapSekkei && !mapSekkei.isEmpty()) {
            String strSekkeiNo = StringUtil.nullToBlank(NumberUtil.convertIntData(mapSekkei.get("sekkeino")));
            String strHinmei = StringUtil.nullToBlank(mapSekkei.get("hinmei"));
            String strPattern = StringUtil.nullToBlank(NumberUtil.convertIntData(mapSekkei.get("pattern")));
            // DaMkJoken情報取得 (6)前工程規格情報より、情報の取得を行う
            getMkJokenInfo(strSekkeiNo);
            // Mkhyojunjoken情報取得 (8)前工程標準規格情報より、情報の取得を行う
            getMkhyojunjoken(strHinmei,strPattern);
        }
        List<String> initMessageList = new ArrayList<>();
        // 前工程規格情報、前工程標準規格情報から規格値を取得して、項目データに設定処理
        setItemListKikakuChi(itemList, initMessageList);
        setItemListKikakuChi(itemListEx, initMessageList);

        // 初期表示処理を実行する
        // 画面クラス名を取得
        String formClassName = StringUtil.nullToBlank(session.getAttribute("formClassName"));

        // 画面クラスをロード
        IFormLogic formLogic;
        try {
            Class<?> formClass = Class.forName(formClassName);
            formLogic = (IFormLogic) formClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
            return;
        }
        //ロットNo初期表示設定
        for (int i = 0; i <= this.itemList.size() - 1; i++) {
            if (this.itemList.get(i).getItemNo() == LOTNO_ITEMNO) {
                this.itemList.get(i).setInputDefault(lotNo);
                break;
            }
        }
        setTempCheckBoxItem(lotNo, formId);

        // 処理データ生成
        ProcessData data = new ProcessData();
        data.setFormClassName(formClassName);
        data.setFormLogic(formLogic);
        data.setMethod("initial");
        data.setItemList(this.itemList);
        data.setItemListEx(this.itemListEx);
        data.setDataSourceDocServer(this.dataSourceDocServer);
        data.setDataSourceQcdb(this.dataSourceQcdb);
        data.setDataSourceSpskadoritu(this.dataSourceSpskadoritu);
        data.setDataSourceTtpkadoritu(this.dataSourceTtpkadoritu);
        data.setDataSourceEquipment(this.dataSourceEquipment);
        data.setDataSourceWip(this.dataSourceWip);
        data.setInitMessageList(initMessageList);
        data.setInitRev(this.initRev);
        data.setInitJotaiFlg(this.initJotaiFlg);
        data.setHiddenDataMap(this.hiddenDataMap);

        this.processData = data;

        // 処理開始
        this.processMain();
        
        setTempRegistButton();
    }

    /**
     * 前工程規格情報、前工程標準規格情報から規格値を取得して、項目データに設定処理
     *
     * @param itemDataList 項目データ
     * @param initMessageList エラーリスト
     */
    private void setItemListKikakuChi(List<FXHDD01> itemDataList, List<String> initMessageList) {
        if (itemDataList == null) {
            return;
        }
        boolean isExist;
        //(8)前工程標準規格情報より、情報がある場合
        for (int i = 0; i <= itemDataList.size() - 1; i++) {

            // 条件ﾃｰﾌﾞﾙ工程名が空の場合は規格値は設定しない
            if (StringUtil.isEmpty(itemDataList.get(i).getJokenKoteiMei())) {
                continue;
            }

            isExist = false;
            //(6)前工程規格情報より、情報がある場合
            if(null != listDaMkJoken && !listDaMkJoken.isEmpty()){
                for (int j = 0; j <= listDaMkJoken.size() - 1; j++) {
                    if (StringUtil.nullToBlank(itemDataList.get(i).getJokenKoteiMei()).equals(StringUtil.nullToBlank(listDaMkJoken.get(j).getKouteimei()))
                            && StringUtil.nullToBlank(itemDataList.get(i).getJokenKomokuMei()).equals(StringUtil.nullToBlank(listDaMkJoken.get(j).getKoumokumei()))
                            && StringUtil.nullToBlank(itemDataList.get(i).getJokenKanriKomoku()).equals(StringUtil.nullToBlank(listDaMkJoken.get(j).getKanrikoumokumei()))) {

                        // 規格値が空またはNULLだった場合
                        if (StringUtil.isEmpty(listDaMkJoken.get(j).getKikakuti())) {
                            itemDataList.get(i).setKikakuChi("【】");
                            break;
                        }
                        itemDataList.get(i).setKikakuChi("【" + listDaMkJoken.get(j).getKikakuti() + "】");
                        isExist = true;
                        break;
                    }
                }
            }

            if(!isExist){
                //(8)前工程標準規格情報より、情報がある場合
                if(null != listDaMkhyojunjoken && !listDaMkhyojunjoken.isEmpty()){
                    for (int k = 0; k <= listDaMkhyojunjoken.size() - 1; k++) {
                        if (StringUtil.nullToBlank(itemDataList.get(i).getJokenKoteiMei()).equals(StringUtil.nullToBlank(listDaMkhyojunjoken.get(k).getKouteimei()))
                                && StringUtil.nullToBlank(itemDataList.get(i).getJokenKomokuMei()).equals(StringUtil.nullToBlank(listDaMkhyojunjoken.get(k).getKoumokumei()))
                                && StringUtil.nullToBlank(itemDataList.get(i).getJokenKanriKomoku()).equals(StringUtil.nullToBlank(listDaMkhyojunjoken.get(k).getKanrikoumokumei()))) {

                            // 規格値が空またはNULLだった場合
                            if (StringUtil.isEmpty(listDaMkhyojunjoken.get(k).getKikakuti())) {
                                itemDataList.get(i).setKikakuChi("【】");
                                break;    
                            }

                            itemDataList.get(i).setKikakuChi("【" + listDaMkhyojunjoken.get(k).getKikakuti() + "】");
                            isExist = true;
                            break;
                        }
                    }
                }
            }                

            if (!isExist) {
                initMessageList.add(MessageUtil.getMessage("XHD-000019", "【" + itemDataList.get(i).getLabel1() + "】"));
            }
        }
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
            } catch (IllegalStateException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        // goto login
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * メニュー画面遷移
     *
     * @return メニュー遷移用URL文字列
     */
    public String returnMenu() {
        return "/pxhdo012/gxhdo012a.xhtml?faces-redirect=true";
    }

    /**
     * ユーザーマスタ検索
     *
     * @param user 担当者コード
     * @return パスワード
     */
    private String selectFxhbm01(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT password "
                    + "FROM fxhbm01 WHERE user_name = ? ";

            List<Object> params = new ArrayList<>();
            params.add(user);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map fxhbm01 = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (null == fxhbm01 || fxhbm01.isEmpty()) {
                return "";
            }
            return StringUtil.nullToBlank(fxhbm01.get("password"));

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return "";
        }
    }

    /**
     * ユーザーグループマスタ検索
     *
     * @param user 担当者コード
     * @return ユーザーグループ
     */
    private List<String> selectFxhbm02(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT usergroup "
                    + "FROM fxhbm02 WHERE user_name = ? ";

            List<Object> params = new ArrayList<>();
            params.add(user);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List fxhbm02 = queryRunner.query(sql, new MapListHandler(), params.toArray());
            if (null == fxhbm02 || fxhbm02.isEmpty()) {
                return new ArrayList<>();
            }
            return ((List<Map>) fxhbm02).stream()
                    .map(n -> n.get("usergroup").toString())
                    .filter(n -> !StringUtil.isEmpty(n))
                    .collect(Collectors.toList());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
        }
    }

    /**
     * ログインユーザー権限チェック
     *
     * @return 権限がある場合true
     */
    private boolean userAuthLogin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        String authParam = this.processData.getUserAuthParam();

        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        List<String> loginUserGroup = this.selectFxhbm02(user);

        List<Parameter> paramListLoginUser = parameterEJB.findParameter(user, authParam);
        List<Parameter> paramListLoginUserGroup = new ArrayList<>();
        for (String group : loginUserGroup) {
            paramListLoginUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        }
        if (paramListLoginUser.isEmpty() && paramListLoginUserGroup.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * ユーザー認証チェック
     */
    public void userAuth() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        String authParam = this.processData.getUserAuthParam();
        String passWord = this.selectFxhbm01(this.authUser);

        if (StringUtil.isEmpty(passWord)
                || !passWord.toUpperCase().equals(this.getSha256(this.authPassword).toUpperCase())) {
            // ユーザーが存在しない場合またはパスワード不一致の場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }

        List<String> loginUserGroup = this.selectFxhbm02(this.authUser);
        List<Parameter> paramListUser = parameterEJB.findParameter(this.authUser, authParam);
        List<Parameter> paramListUserGroup = new ArrayList<>();
        for (String group : loginUserGroup) {
            paramListUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        }

        if (paramListUser.isEmpty() && paramListUserGroup.isEmpty()) {
            // 権限がない場合エラー終了
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }

        // 後続処理を実行
        this.processMain();
    }

    /**
     * SHA-256変換
     *
     * @param text 文字列
     * @return SHA-256変換後の文字列
     */
    private String getSha256(String text) {
        try {
            if (StringUtil.isEmpty(text)) {
                return "";
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            byte[] cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for (byte b : cipher_byte) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ErrUtil.outputErrorLog("SHA-256変換失敗", ex, LOGGER);
            return "";
        }
    }

    /**
     * リンククリック時処理
     * 
     * @param linkButtonId ボタンID 
     */
    public void cmdLinkAction(String linkButtonId) {
        btnClick(linkButtonId);
    }
    
    /**
     * ボタン押下時処理
     *
     * @param buttonId ボタンID
     */
    public void btnClick(String buttonId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        this.warnDialogRendered = false;

        // 画面ID、画面クラス名を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String formClassName = StringUtil.nullToBlank(session.getAttribute("formClassName"));
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はログイン画面に遷移
            this.setOnLoadProcess("window.location.href = '" + externalContext.getRequestContextPath() + "/faces/login.xhtml?faces-redirect=true';");
            return;
        }

        // 画面クラスをロード
        IFormLogic formLogic;
        try {
            Class<?> formClass = Class.forName(formClassName);
            formLogic = (IFormLogic) formClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
            return;
        }

        boolean isCheckMethod = true;
        boolean isFlowButtonClik = false;
        if ("flow_temp_regist_Top".equals(buttonId) || "flow_temp_regist_Bottom".equals(buttonId)) {
            isCheckMethod = false;
            isFlowButtonClik = true;
        }
        
        // ボタンに対応した処理メソッド名を取得
        String method = formLogic.convertButtonIdToMethod(buttonId);
        if ("error".equals(method) && isCheckMethod) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "処理が登録されていません", null);
            facesContext.addMessage(null, message);
        }

        boolean isCheck = false;
        try {
            FXHDD02 fxhdd02 = (FXHDD02) externalContext.getRequestMap().get("item");
            if (null != fxhdd02) {
                String buttonName = fxhdd02.getButtonName();
                if ("登録".equals(buttonName) || "修正".equals(buttonName)) {
                    isCheck = true;
                }
            }
        } catch (ClassCastException e) {
            ErrUtil.outputErrorLog("ClassCastException発生", e, LOGGER);
        }

        if ("flow_temp_regist_Top".equals(buttonId) || "flow_temp_regist_Bottom".equals(buttonId)) {
            isCheck = true;
            buttonId = getTempRegistButtonId();
            method = formLogic.convertButtonIdToMethod(buttonId);
        }

        // 背景色を元に戻す
        this.clearItemListBackColor(buttonId);

        //共通ﾁｪｯｸ
        ErrorMessageInfo errorMessageInfo = getCheckResult(buttonId);

        // エラーメッセージが設定されている場合、エラー出力のみ
        if (errorMessageInfo != null && !StringUtil.isEmpty(errorMessageInfo.getErrorMessage())) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessageInfo.getErrorMessage(), null);
            facesContext.addMessage(null, message);

            // 項目の背景色を変更する場合
            if (errorMessageInfo.getIsChangeBackColor()) {
                // エラー項目の背景色を設定
                ErrUtil.setErrorItemBackColor(this.itemList, errorMessageInfo);
                if(this.itemListEx != null){
                    ErrUtil.setErrorItemBackColor(this.itemListEx, errorMessageInfo);
                }
            }

            // エラー項目を表示するためページを遷移する。
            // 表示したい項目のIndexを指定(0以下のIndexは内部的に無視)
            setPageItemDataList(errorMessageInfo.getPageChangeItemIndex());

            return;
        }

        if (isCheck && !checkFlowTempItemCheckBox()) {
            return;
        }

        // 処理データ生成
        ProcessData data = new ProcessData();
        data.setFormClassName(formClassName);
        data.setFormLogic(formLogic);
        data.setMethod(method);
        data.setItemList(this.itemList);
        data.setItemListEx(this.itemListEx);
        data.setDataSourceDocServer(this.dataSourceDocServer);
        data.setDataSourceQcdb(this.dataSourceQcdb);
        data.setDataSourceSpskadoritu(this.dataSourceSpskadoritu);
        data.setDataSourceTtpkadoritu(this.dataSourceTtpkadoritu);
        data.setDataSourceEquipment(this.dataSourceEquipment);
        data.setDataSourceWip(this.dataSourceWip);
        data.setInitRev(this.initRev);
        data.setInitJotaiFlg(this.initJotaiFlg);
        data.setHiddenDataMap(this.hiddenDataMap);

        this.processData = data;

        if (isFlowButtonClik) {
            session.setAttribute("isFlowButtonClik", isFlowButtonClik);
        }
        
        // 処理開始
        this.processMain();
        session.removeAttribute("isFlowButtonClik");
    }

    /**
     * 警告ダイアログで「はい」が選択された場合の処理
     */
    public void processWarnOk() {
        // 警告メッセージを削除して処理再開
        this.processData.setWarnMessage("");
        this.warnDialogRendered = false;
        this.processMain();
    }

    /**
     * メイン処理
     */
    private void processMain() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // エラーメッセージが設定されている場合、エラー出力のみ
        if (!this.processData.getErrorMessageInfoList().isEmpty()) {

            for (ErrorMessageInfo errorMessageInfo : this.processData.getErrorMessageInfoList()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessageInfo.getErrorMessage(), null));

                // 項目の背景色を変更する場合
                if (errorMessageInfo.getIsChangeBackColor()) {
                    // エラー項目の背景色を設定
                    ErrUtil.setErrorItemBackColor(this.itemList, errorMessageInfo);
                    if(this.itemListEx != null && !this.itemListEx.isEmpty()){
                        ErrUtil.setErrorItemBackColor(this.itemListEx, errorMessageInfo);
                    }
                }
            }

            // エラー項目を表示するためページを遷移する。
            // 表示したい項目のIndexを指定(0以下のIndexは内部的に無視)
            setPageItemDataList(this.processData.getErrorMessageInfoList().get(0).getPageChangeItemIndex());

            return;
        }

        try {

            // 注意メッセージが設定されている場合、ダイアログを表示する
            if (!this.processData.getInfoMessageList().isEmpty()) {

                // メッセージを画面に渡す
                InfoMessage infoMessageError = (InfoMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INFO_MESSAGE);

                infoMessageError.setInfoMessageList(this.processData.getInfoMessageList());

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "infoMessage");

                return;
            }
            
            // エラーリストメッセージが設定されている場合、ダイアログを表示する
            List<String> resultMessageList = this.processData.getErrorListMessage().getResultMessageList();
            if ((resultMessageList != null && !resultMessageList.isEmpty()) || !StringUtil.isEmpty(this.processData.getErrorListMessage().getResultMessage())) {

                // メッセージを画面に渡す
                ErrorListMessage errorListMessageError = (ErrorListMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_ERRORLIST_MESSAGE);

                errorListMessageError.setResultMessageList(this.processData.getErrorListMessage().getResultMessageList());
                errorListMessageError.setResultMessage(this.processData.getErrorListMessage().getResultMessage());
                errorListMessageError.setTitleMessage(this.processData.getErrorListMessage().getTitleMessage());

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "errorListMessage");

                return;
            }

            // 規格エラーメッセージが設定されている場合、ダイアログを表示する
            if (!this.processData.getKikakuchiInputErrorInfoList().isEmpty()) {

                // メッセージを画面に渡す
                KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
                List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
                for (KikakuchiInputErrorInfo kikakuchiInputErrorInfo : this.processData.getKikakuchiInputErrorInfoList()) {
                    kikakuchiInputErrorInfoList.add(kikakuchiInputErrorInfo.clone());
                }

                kikakuError.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "kikakuError");

                // エラー項目の背景色を設定
                boolean hasItemFlag = ErrUtil.setErrorItemBackColor(this.itemList, this.processData.getKikakuchiInputErrorInfoList());
                ErrUtil.setErrorItemBackColor(this.itemListEx, this.processData.getKikakuchiInputErrorInfoList());

                // エラー項目を表示するためページを遷移する。
                // 表示したい項目のIndexを指定(0以下のIndexは内部的に無視)
                if (hasItemFlag) {
                    setPageItemDataList(this.processData.getKikakuchiInputErrorInfoList().get(0).getItemIndex());
                }
                return;
            }

            // 警告メッセージが設定されている場合、ダイアログを表示する
            if (!StringUtil.isEmpty(this.processData.getWarnMessage())) {
                this.warnDialogRendered = true;

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "warning");

                return;
            }

            // ユーザー認証が必要な場合
            if (this.processData.isRquireAuth()) {
                this.processData.setRquireAuth(false);

                // ログインユーザーの権限チェックを実施する
                if (!this.userAuthLogin()) {
                    // 権限がない場合はユーザー認証画面を表示
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("firstParam", "auth");
                    return;
                }
            }

            // 警告もエラーも存在しない場合、後続処理を実行して結果を反映する
            IFormLogic formLogic = this.processData.getFormLogic();
            String methodName = this.processData.getMethod();

            Class<?> formClass = Class.forName(this.processData.getFormClassName());
            Method method = formClass.getMethod(methodName, ProcessData.class);

            // 処理実行
            ProcessData resultData = (ProcessData) method.invoke(formLogic, this.processData);

            // 致命的ｴﾗｰの場合
            if (this.processData.isFatalError()) {
                // 画面の戻るボタン以外を非表示とする。
                this.itemList = new ArrayList<>();
                this.itemListEx = new ArrayList<>();
                this.buttonListTop = new ArrayList<>();
                this.buttonListBottom = new ArrayList<>();
                this.styleDisplayNone = "display: none;";
            }

            if (!StringUtil.isEmpty(resultData.getMethod())) {
                // 後続処理が定義されている場合は再起的に実行する
                this.processData = resultData;
                this.processMain();
            } else {
                // 後続処理が定義されていない場合は結果を反映して処理を終了する
                this.itemList = resultData.getItemList();
                this.itemListEx = resultData.getItemListEx();
                // 情報メッセージが設定されていれば出力する
                if (!StringUtils.isEmpty(this.processData.getInfoMessage())) {
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_INFO, this.processData.getInfoMessage(), null);
                    facesContext.addMessage(null, message);
                }

                //***************************************************************************************************
                // サブ画面初期表示用のメッセージが存在する場合はメッセージを設定
                for (String message : this.processData.getSubInitDispMsgList()) {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
                }

                // コールバックパラメータが設定されている場合はセットする。
                if (!StringUtil.isEmpty(resultData.getCollBackParam())) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("firstParam", resultData.getCollBackParam());
                }

                // 完了時メッセージの設定
                if (!StringUtil.isEmpty(resultData.getCompMessage())) {
                    this.compMessage = resultData.getCompMessage();
                }

                // オンロード処理設定
                if (!StringUtil.isEmpty(resultData.getExecuteScript())) {
                    RequestContext.getCurrentInstance().execute(resultData.getExecuteScript());
                }

                // リビジョンを保持
                this.initRev = resultData.getInitRev();

                // 状態フラグを保持
                this.initJotaiFlg = resultData.getInitJotaiFlg();

                // 状態ﾌﾗｸﾞ表示を設定
                this.jotaiDisplay = getJotaiDisplayValue(this.initJotaiFlg);
                
                // 隠し項目
                this.hiddenDataMap = resultData.getHiddenDataMap();
           

                //リビジョンチェック対象ボタンIDを設定
                if (this.processData.getCheckRevisionButtonId() != null && !this.processData.getCheckRevisionButtonId().isEmpty()) {
                    this.checkRevisionButtonId = this.processData.getCheckRevisionButtonId();
                }

                // 対象の処理にボタン押下時の処理についてはテーブルチェックは行わない
                if (this.processData.getNoCheckButtonId() != null && !this.processData.getNoCheckButtonId().isEmpty()) {
                    this.noCheckButtonId = this.processData.getNoCheckButtonId();
                }

                // ボタンの活性・非活性制御
                this.setButtonEnabled(this.processData.getActiveButtonId(), this.processData.getInactiveButtonId());
                // 処理制御データクリア
                this.processData = new ProcessData();
            }

        } catch (ReflectiveOperationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("クローン処理エラー", ex, LOGGER);
        }
    }

    /**
     * 状態表示の表示内容を取得
     *
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @return 状態表示表示内容
     */
    private String getJotaiDisplayValue(String jotaiFlg) {
        switch (jotaiFlg) {
            case "0":
                String tempRegistName = "仮登録";
                Map tempRegistNameMap = getTempRegistName();
                if (tempRegistNameMap != null && !tempRegistNameMap.isEmpty() 
                        && !"".equals(StringUtil.nullToBlank(tempRegistNameMap.get("tmp_name")))) {
                    tempRegistName = StringUtil.nullToBlank(tempRegistNameMap.get("tmp_name"));
                }
                return "現在「" + tempRegistName + "」です";
            case "1":
                return "現在「登録済」です";
            default:
                return "現在「未登録」です";
        }
    }

    /**
     * ボタンの活性・非活性制御
     *
     * @param activeId 活性にするボタンのID
     * @param inactiveId 非活性にするボタンのID
     */
    private void setButtonEnabled(List<String> activeId, List<String> inactiveId) {
        if (null != activeId) {
            for (String buttonId : activeId) {
                for (FXHDD02 button : this.buttonListTop) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(true);
                    }
                }
                for (FXHDD02 button : this.buttonListBottom) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(true);
                    }
                }
            }
        }

        if (null != inactiveId) {
            for (String buttonId : inactiveId) {
                for (FXHDD02 button : this.buttonListTop) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(false);
                    }
                }
                for (FXHDD02 button : this.buttonListBottom) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     * タイトル設定情報取得
     *
     * @param settingId 設定ID
     * @param formTitle 画面タイトル
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    private boolean loadTitleSettings(String settingId, String formTitle) {
        boolean result = false;
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT setting_id, font_size, font_color, bg_color "
                    + "FROM fxhdm02 WHERE setting_id = ? ";

            List<Object> params = new ArrayList<>();
            params.add(settingId);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("setting_id", "settingId");
            mapping.put("font_size", "fontSize");
            mapping.put("font_color", "fontColor");
            mapping.put("bg_color", "bgColor");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM02>> beanHandler = new BeanListHandler(FXHDM02.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List<FXHDM02> fXHDM02List = queryRunner.query(sql, beanHandler, params.toArray());

            if (fXHDM02List.isEmpty()) {
                this.titleInfo = new FXHDM02();
                this.titleInfo.setFormName(formTitle);
            } else {
                this.titleInfo = fXHDM02List.get(0);
                this.titleInfo.setFormName(formTitle);
                result = true;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("タイトル設定情報取得失敗", ex, LOGGER);
        }
        return result;
    }

    /**
     * 項目定義情報取得
     *
     * @param formId 項目定義情報
     * @param callerFormId 画面ID(呼出し元)
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    protected boolean loadItemSettings(String formId, String callerFormId) {
        boolean result = false;
        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);

            String inputItemInfo;
            inputItemInfo = "hdm02_3.font_color AS font_color_input, hdm02_3.bg_color AS bg_color_input, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '1' THEN 'false' ELSE 'true' END AS render_iput_text, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '2' THEN 'false' ELSE 'true' END AS render_iput_number, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '3' THEN 'false' ELSE 'true' END AS render_iput_date, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '4' THEN 'false' ELSE 'true' END AS render_iput_select, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '5' THEN 'false' ELSE 'true' END AS render_iput_radio, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '6' THEN 'false' ELSE 'true' END AS render_iput_time, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '8' THEN 'false' ELSE 'true' END AS render_iput_checkbox, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '7' THEN 'false' ELSE 'true' END AS render_output_label, "
                    + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '9' THEN 'false' ELSE 'true' END AS render_iput_select_available, ";

            String sql = "SELECT A.*, row_number() over() AS item_index FROM ("
                    + "SELECT hdd01.gamen_id, hdd01.item_id, hdd01.item_no, hdd01.input_item_mold, hdd01.label1, hdd01.label2, "
                    + "hdd01.input_list, hdd01.input_default, hdd01.input_length, hdd01.input_length_dec, "
                    + "hdm02_1.font_size AS font_size_1, hdm02_1.font_color AS font_color_1, hdm02_1.bg_color AS bg_color_1, "
                    + "hcm02_4.font_size AS font_size_3, hcm02_4.font_color AS font_color_3, hcm02_4.bg_color AS bg_color_3, "
                    + "CASE WHEN hdd01.label1_setting IS NULL OR hdd01.link_button_id IS NOT NULL THEN 'false' ELSE 'true' END AS render_1, "
                    + "hdm02_2.font_size AS font_size_2, hdm02_2.font_color AS font_color_2, hdm02_2.bg_color AS bg_color_2, "
                    + "CASE WHEN hdd01.label2_setting IS NULL THEN 'false' ELSE 'true' END AS render_2, "
                    + "CASE WHEN hdd01.link_button_id IS NULL THEN 'false' ELSE 'true' END AS render_link_button, "
                    + "hdm02_3.font_size AS font_size_input, "
                    + inputItemInfo
                    + "hdd01.joken_kotei_mei,hdd01.joken_komoku_mei,hdd01.joken_kanri_komoku,hdd01.standard_pattern, "
                    + "hdd01.link_button_id, "
                    + "hdm02_3.bg_color AS bg_color_input_default "
                    + "FROM fxhdd01 hdd01 "
                    + "LEFT JOIN fxhdm02 hdm02_1 ON (hdd01.label1_setting = hdm02_1.setting_id) "
                    + "LEFT JOIN fxhdm02 hdm02_2 ON (hdd01.label2_setting = hdm02_2.setting_id) "
                    + "LEFT JOIN fxhdm02 hdm02_3 ON (hdd01.input_setting = hdm02_3.setting_id) "
                    + "LEFT JOIN fxhdm02 hcm02_4 ON (hdd01.standard_setting = hcm02_4.setting_id) "
                    + "WHERE hdd01.gamen_id = ? AND (hdd01.item_authority IS NULL OR "
                    + DBUtil.getInConditionPreparedStatement("hdd01.item_authority", userGrpList.size()) + ") "
                    + "ORDER BY hdd01.item_no "
                    + ") A ";

            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("item_id", "itemId");
            mapping.put("item_no", "itemNo");
            mapping.put("input_item_mold", "inputItemType");
            mapping.put("label1", "label1");
            mapping.put("label2", "label2");
            mapping.put("input_list", "inputList");
            mapping.put("input_default", "inputDefault");
            mapping.put("input_length", "inputLength");
            mapping.put("input_length_dec", "inputLengthDec");
            mapping.put("font_size_1", "fontSize1");
            mapping.put("font_color_1", "fontColor1");
            mapping.put("bg_color_1", "backColor1");
            mapping.put("render_1", "render1");
            mapping.put("font_size_2", "fontSize2");
            mapping.put("font_color_2", "fontColor2");
            mapping.put("bg_color_2", "backColor2");
            mapping.put("render_2", "render2");
            mapping.put("render_link_button", "renderLinkButton");
            mapping.put("font_size_input", "fontSizeInput");
            mapping.put("font_color_input", "fontColorInput");
            mapping.put("bg_color_input", "backColorInput");
            mapping.put("render_iput_text", "renderInputText");
            mapping.put("render_iput_number", "renderInputNumber");
            mapping.put("render_iput_date", "renderInputDate");
            mapping.put("render_iput_select", "renderInputSelect");
            mapping.put("render_iput_radio", "renderInputRadio");
            mapping.put("render_iput_time", "renderInputTime");
            mapping.put("render_iput_checkbox", "renderInputCheckBox");
            mapping.put("render_output_label", "renderOutputLabel");
            mapping.put("render_iput_select_available", "renderInputSelectAvailable");
            mapping.put("font_size_3", "fontSize3");
            mapping.put("font_color_3", "fontColor3");
            mapping.put("bg_color_3", "backColor3");
            mapping.put("joken_kotei_mei", "jokenKoteiMei");
            mapping.put("joken_komoku_mei", "jokenKomokuMei");
            mapping.put("joken_kanri_komoku", "jokenKanriKomoku");
            mapping.put("standard_pattern", "standardPattern");
            mapping.put("link_button_id", "linkButtonId");
            mapping.put("bg_color_input_default", "backColorInputDefault");
            mapping.put("item_index", "itemIndex");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD01>> beanHandler = new BeanListHandler<>(FXHDD01.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.itemList = queryRunner.query(sql, beanHandler, params.toArray());
            if (this.itemList != null && !this.itemList.isEmpty()) {
                result = true;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("項目情報取得失敗", ex, LOGGER);
        }
        return result;
    }

    /**
     * ボタンパラメータ情報取得
     *
     * @param formId 画面ID
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    protected boolean loadButtonSettings(String formId) {
        boolean result = false;

        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT hdd02.button_id, hdd02.button_no, hdd02.button_location, hdd02.button_name, hdm02.font_size, hdm02.font_color, hdm02.bg_color "
                    + "FROM fxhdd02 hdd02 INNER JOIN fxhdm02 hdm02 ON (hdd02.button_setting = hdm02.setting_id) "
                    + "WHERE hdd02.gamen_id = ? AND  (hdd02.button_authority IS NULL OR"
                    + DBUtil.getInConditionPreparedStatement("hdd02.button_authority", userGrpList.size()) + ") "
                    + "ORDER BY hdd02.button_location, hdd02.button_no";

            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("button_id", "buttonId");
            mapping.put("button_no", "buttonNo");
            mapping.put("button_location", "buttonLocation");
            mapping.put("button_name", "buttonName");
            mapping.put("font_size", "fontSize");
            mapping.put("font_color", "fontColor");
            mapping.put("bg_color", "backColor");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD02>> beanHandler = new BeanListHandler<>(FXHDD02.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List<FXHDD02> buttonList = queryRunner.query(sql, beanHandler, params.toArray());

            if (buttonList != null && !buttonList.isEmpty()) {
                // 上部ボタン
                this.buttonListTop
                        = buttonList.stream().filter(n -> "1".equals(n.getButtonLocation())).collect(Collectors.toList());
                // 下部ボタン
                this.buttonListBottom
                        = buttonList.stream().filter(n -> "2".equals(n.getButtonLocation())).collect(Collectors.toList());
                result = true;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("ボタン項目取得失敗", ex, LOGGER);
        }
        return result;
    }

    /**
     * 設定エラー
     */
    private void settingError() {
        this.itemList = new ArrayList<>();
        this.buttonListTop = new ArrayList<>();
        this.buttonListBottom = new ArrayList<>();
        this.styleDisplayNone = "display: none;";

        // メッセージを画面に渡す
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000018", "")));

        // 実行スクリプトを設定
        RequestContext.getCurrentInstance().execute("PF('W_dlg_initMessage').show();");
    }

    /**
     * チェック処理情報取得
     *
     * @param formId 画面ID
     */
    protected void loadCheckList(String formId) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT gamen_id,button_id,item_id,check_pattern,check_no "
                    + "  FROM FXHDM05 "
                    + " WHERE gamen_id = ? "
                    + " ORDER BY button_id,item_id,check_no ";

            List<Object> params = new ArrayList<>();
            params.add(formId);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("button_id", "buttonId");
            mapping.put("item_id", "itemId");
            mapping.put("check_pattern", "checkPattern");
            mapping.put("check_no", "checkNo");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM05>> beanHandler = new BeanListHandler<>(FXHDM05.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.checkListHDM05 = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }

    /**
     * SekkeiNo取得
     *
     * @param lotNo 画面ロットNo
     */
    private Map getSekkeiNo(String lotNo) {
        String strKojyo = lotNo.substring(0, 3);
        String strLotNo = lotNo.substring(3, 12);
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            
            Map mapSekkeiNo = CommonUtil.getMkSekkeiInfo(queryRunnerQcdb, queryRunnerWip, strKojyo, strLotNo, "001");
            
            if (null != mapSekkeiNo && !mapSekkeiNo.isEmpty()) {
                return mapSekkeiNo;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SekkeiNo取得失敗", ex, LOGGER);
        }
        return null;
    }

    /**
     * da_mkjoken情報取得
     *
     * @param sekkeiNo 設計No
     */
    private void getMkJokenInfo(String sekkeiNo) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT kouteimei,koumokumei,kanrikoumokumei,kikakuti "
                    + "  FROM da_mkjoken "
                    + " WHERE sekkeino = ? ";

            List<Object> params = new ArrayList<>();
            params.add(sekkeiNo);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("kouteimei", "kouteimei");
            mapping.put("koumokumei", "koumokumei");
            mapping.put("kanrikoumokumei", "kanrikoumokumei");
            mapping.put("kikakuti", "kikakuti");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<DaMkJoken>> beanHandler = new BeanListHandler<>(DaMkJoken.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.listDaMkJoken = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }

    /**
     * da_mkhyojunjoken情報取得
     *
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     */
    private void getMkhyojunjoken(String hinmei,String pattern) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT hinmei,pattern,syurui,kouteimei,koumokumei,kanrikoumokumei, "
                    + "          kikakuti,tyekkupattern,tantousya,tourokunichiji,koushinnichiji "
                    + "     FROM da_mkhyojunjoken "
                    + "    WHERE hinmei = ? "
                    + "      AND pattern = ? ";

            List<Object> params = new ArrayList<>();
            params.add(hinmei);
            params.add(pattern);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("kouteimei", "kouteimei");
            mapping.put("koumokumei", "koumokumei");
            mapping.put("kanrikoumokumei", "kanrikoumokumei");
            mapping.put("kikakuti", "kikakuti");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<DaMkhyojunjoken>> beanHandler = new BeanListHandler<>(DaMkhyojunjoken.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.listDaMkhyojunjoken = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }
    
    /**
     * 共通チェック
     *
     * @param buttonId ボタンID
     * @return エラーメッセージ
     */
    protected ErrorMessageInfo getCheckResult(String buttonId) {

        // リビジョンチェック
        ErrorMessageInfo checkRevErrorMessage = checkRevision(buttonId);
        if (checkRevErrorMessage != null) {
            return checkRevErrorMessage;
        }

        //共通ﾁｪｯｸ
        List<FXHDM05> itemRowCheckList
                = this.checkListHDM05.stream().filter(n -> buttonId.equals(n.getButtonId())).collect(Collectors.toList());

        ValidateUtil validateUtil = new ValidateUtil();
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
        ErrorMessageInfo requireCheckErrorMessage = validateUtil.executeValidation(itemRowCheckList, this.itemList, queryRunnerWip);

        return requireCheckErrorMessage;
    }

    /**
     * 背景色をデフォルトの背景色に戻す
     *
     * @param buttonId ボタンID
     */
    protected void clearItemListBackColor(String buttonId) {
        // 背景色を戻さない特定の処理を除き背景色をデフォルトの背景色に戻す。
        if (this.noCheckButtonId == null || !this.noCheckButtonId.contains(buttonId)) {
            for (FXHDD01 fxhdd01 : this.itemList) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }
        }
    }

    /**
     * ページ選択処理
     *
     * @param itemIndex 表示項目のインデックス
     */
    protected void setPageItemDataList(int itemIndex) {

        // Indexが0未満の場合はリターン
        if (itemIndex <= 0) {
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        DataList itemDataList
                = (DataList) facesContext.getViewRoot().findComponent(":form:itemDataList");
        if (null == itemDataList) {
            return;
        }
        // 項目インデックス
        BigDecimal decItemIndex = BigDecimal.valueOf(itemIndex);
        // 一覧の表示件数
        BigDecimal decHyojiKensu = new BigDecimal(this.hyojiKensu);
        // ページ数(インデックス / 表示件数)の切り上げ
        BigDecimal decPage = decItemIndex.divide(decHyojiKensu, RoundingMode.UP);
        // 開始インデックス = 表示件数 * (ページ数 - 1)
        BigDecimal startIdx = decHyojiKensu.multiply(decPage.subtract(BigDecimal.ONE));
        itemDataList.setFirst(startIdx.intValue());

    }

    /**
     * 規格外エラーダイアログOK押下時
     */
    public void processKikakuWarnOk() {

        // 規格外エラーをを削除して処理再開
        this.processData.getKikakuchiInputErrorInfoList().clear();
        this.processMain();
    }

    /**
     * 警告メッセージダイアログOK押下時
     */
    public void processInfoWarnOk() {

        // エラーをを削除して処理再開
        this.processData.getInfoMessageList().clear();
        this.processMain();
    }

    /**
     * リビジョンチェック
     *
     * @param buttonId ボタンID
     * @return エラーメッセージ情報(エラーなし無しの場合リターン)
     */
    protected ErrorMessageInfo checkRevision(String buttonId) {
        try {

            //リビジョンチェック対象のボタンの場合、チェックを行う。
            if (!this.checkRevisionButtonId.contains(buttonId)) {
                return null;
            }

            QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 12); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(12, 15); //枝番
            Integer jissekino = (Integer) session.getAttribute("jissekino"); //実績No

            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId, jissekino);
            if (StringUtil.isEmpty(this.initJotaiFlg)) {
                // 新規の場合、データが存在する場合
                if (fxhdd11RevInfo != null && !fxhdd11RevInfo.isEmpty()) {
                    return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
                }
            } else {
                // 品質DB登録実績データが取得出来ていない場合エラー
                if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                    return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
                }

                // revisionが更新されていた場合エラー
                if (!this.initRev.equals(StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev")))) {
                    return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
                }
            }
            return null;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ErrorMessageInfo("実行時エラー");
        }
    }

    /**
     * [原材料品質DB登録実績]から、リビジョン,状態フラグを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd11RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo, String edaban, String formId, Integer jissekino) throws SQLException {
        // 原材料品質DB登録実績の取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd11 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? ";
        if (jissekino != null) {
            sql += "AND jissekino = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        if (jissekino != null) {
            params.add(jissekino);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
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
     * 戻る時処理
     *
     * @return 戻り先ページ
     */
    public String returnPage() {
        //セッションのロットNoをクリア
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.setAttribute("lotNo", "");

        return "/secure/pxhdo102/gxhdo102a.xhtml?faces-redirect=true";
    }

    /**
     * サブ画面からの戻り時処理
     *
     * @param subFormId サブ画面ID
     * @param isError エラー判定(true:エラー、false:エラーなし)
     */
    public void returnSubForm(String subFormId, boolean isError) {
        if (isError) {
            // 処理なし
            return;
        }

        switch (subFormId) {
            // ｶﾞﾗｽ作製・秤量入力
            case SubFormUtil.FORM_ID_GXHDO102C001:
                GXHDO102C001 beanGXHDO102C001 = (GXHDO102C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C001);
                GXHDO102C001Logic.setReturnData(beanGXHDO102C001.getGxhdO102c001Model(), this.itemList);
                break;
            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力
            case SubFormUtil.FORM_ID_GXHDO102C002:
                GXHDO102C002 beanGXHDO102C002 = (GXHDO102C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C002);
                GXHDO102C002Logic.setReturnData(beanGXHDO102C002.getGxhdO102c002Model(), this.itemListEx);
                break;
            // 添加材ｽﾗﾘｰ作製・添加材調合入力
            case SubFormUtil.FORM_ID_GXHDO102C004:
                GXHDO102C004 beanGXHDO102C004 = (GXHDO102C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C004);
                GXHDO102C004Logic.setReturnData(beanGXHDO102C004.getGxhdO102c004Model(), this.itemList);
                break;
            // 添加材ｽﾗﾘｰ作製・溶剤調合入力
            case SubFormUtil.FORM_ID_GXHDO102C005:
                GXHDO102C005 beanGXHDO102C005 = (GXHDO102C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C005);
                GXHDO102C005Logic.setReturnData(beanGXHDO102C005.getGxhdO102c005Model(), this.itemList);
                break;
            // 添加材ｽﾗﾘｰ作製・粉砕入力
            case SubFormUtil.FORM_ID_GXHDO102C006:
                GXHDO102C006 beanGXHDO102C006 = (GXHDO102C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C006);
                GXHDO102C006Logic.setReturnData(beanGXHDO102C006.getGxhdO102c006Model(), this.itemList);
                break;
            // ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量入力
            case SubFormUtil.FORM_ID_GXHDO102C007:
                GXHDO102C007 beanGXHDO102C007 = (GXHDO102C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C007);
                GXHDO102C007Logic.setReturnData(beanGXHDO102C007.getGxhdO102c007Model(), this.itemList);
                break;
            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量入力
            case SubFormUtil.FORM_ID_GXHDO102C008:
                GXHDO102C008 beanGXHDO102C008 = (GXHDO102C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C008);
                GXHDO102C008Logic.setReturnData(beanGXHDO102C008.getGxhdO102c008Model(), this.itemList);
                break;
//            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力
//            case SubFormUtil.FORM_ID_GXHDO102C009:
//                GXHDO102C009 beanGXHDO102C009 = (GXHDO102C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C009);
//                GXHDO102C009Logic.setReturnData(beanGXHDO102C009.getGxhdO102c009Model(), this.itemList);
//                break;
//             // 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量入力
//            case SubFormUtil.FORM_ID_GXHDO102C010:
//                GXHDO102C010 beanGXHDO102C010 = (GXHDO102C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C010);
//                GXHDO102C010Logic.setReturnData(beanGXHDO102C010.getGxhdO102c010Model(), this.itemList);
//                break;
//             // 誘電体ｽﾗﾘｰ作製・主原料秤量入力
//            case SubFormUtil.FORM_ID_GXHDO102C011:
//                GXHDO102C011 beanGXHDO102C011 = (GXHDO102C011) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C011);
//                GXHDO102C011Logic.setReturnData(beanGXHDO102C011.getGxhdO102c011Model(), this.itemList);
//                break;
            default:
                break;
        }
    }

    /**
     * 状態が仮登録時、仮登録名を取得。
     * 
     * @return 仮登録名
     */
    private Map getTempRegistName() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String lotNo = StringUtil.nullToBlank(session.getAttribute("lotNo"));
            
            Map fxhdd11Map = loadFxhdd11TmpKbnInfo(lotNo, getFormId());
            if (fxhdd11Map == null || fxhdd11Map.isEmpty()) {
                return null;
            }
            if ("".equals(StringUtil.nullToBlank(fxhdd11Map.get("tmp_kbn")))) {
                return null;
            }
            
            int tmp_kbn = Integer.valueOf(StringUtil.nullToBlank(fxhdd11Map.get("tmp_kbn")));
            
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT tmp_name FROM fxhdm08 WHERE gamen_id = ? AND tmp_kbn = ?";

            List<Object> params = new ArrayList<>();
            params.add(getFormId());
            params.add(tmp_kbn);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunner.query(sql, new MapHandler(), params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return new HashMap();
    }

    /**
     * [原材料品質DB登録実績]から、仮登録区分,状態フラグを取得
     * 
     * @param lotNo ロットNo
     * @param formId 画面ID
     * @return 
     */
    private Map loadFxhdd11TmpKbnInfo(String lotNo, String formId) {
        
        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
        String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
        String lotNo8 = lotNo.substring(3, 12); //ﾛｯﾄNo
        String edaban = lotNo.substring(12, 15); //枝番
        try {
            // 品質DB登録実績の取得
            String sql = "SELECT tmp_kbn, jotai_flg "
                    + "FROM fxhdd11 "
                    + "WHERE kojyo = ? AND lotno = ? "
                    + "AND edaban = ? AND gamen_id = ? ";

            List<Object> params = new ArrayList<>();
            params.add(kojyo);
            params.add(lotNo8);
            params.add(edaban);
            params.add(formId);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new HashMap();
        }
    }

    /**
     * フロー用仮登録項目のチェックボックスを作成
     * 
     * @param lotNo ロットNo
     * @param formId 画面ID
     */
    private void setTempCheckBoxItem(String lotNo, String formId) {
        // [品質DB登録実績]から、仮登録区分,状態フラグを取得
        Map fxhdd11TmpKbnInfo = loadFxhdd11TmpKbnInfo(lotNo, formId);
        
        if (fxhdd11TmpKbnInfo != null && !fxhdd11TmpKbnInfo.isEmpty()) {
            
            String jotai_flg = StringUtil.nullToBlank(fxhdd11TmpKbnInfo.get("jotai_flg"));
            if (!"1".equals(jotai_flg)) {
                int tmp_kbn = 0;
                
                if ("0".equals(jotai_flg)) {
                    if ("".equals(StringUtil.nullToBlank(fxhdd11TmpKbnInfo.get("tmp_kbn")))) {
                        return;
                    }
                    tmp_kbn = Integer.valueOf(StringUtil.nullToBlank(fxhdd11TmpKbnInfo.get("tmp_kbn")));
                }
                
                // ﾌﾛｰ項目ﾏｽﾀからﾁｪｯｸﾎﾞｯｸｽに関する情報を取得する
                List<Map<String, Object>> checkboxItemList = loadFxhdm09(formId, tmp_kbn);
                // 取得できない場合、何も処理を行わない。
                if (checkboxItemList == null || checkboxItemList.isEmpty()) {
                    return;
                }
                
                for (Map<String, Object> checkboxItem : checkboxItemList) {
                    String item_id = StringUtil.nullToBlank(checkboxItem.get("item_id"));
                    List<FXHDD01> selectData
                        = this.itemList.stream().filter(n -> item_id.equals(n.getItemId())).collect(Collectors.toList());
                    if (null != selectData && 0 < selectData.size()) {
                        FXHDD01 item = selectData.get(0);
                        // フロー用仮登録項目のチェックボックスを表示
                        item.setRenderTempItemCheckBox(true);
                    }
                }
            }
        }
    }

    /**
     * ﾌﾛｰ項目ﾏｽﾀからﾁｪｯｸﾎﾞｯｸｽに関する情報を取得する
     * 
     * @param formId 画面ID
     * @param tmp_kbn 仮登録区分
     * @return ﾁｪｯｸﾎﾞｯｸｽ項目ID
     */
    private List<Map<String, Object>> loadFxhdm09(String formId, int tmp_kbn) {
        QueryRunner queryRunnerDoc = new QueryRunner(this.dataSourceDocServer);
        try {
            // フロー項目マスタの取得
            String sql = "SELECT item_id "
                    + "FROM fxhdm09 "
                    + "WHERE gamen_id = ? AND tmp_kbn = ? ";

            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.add(tmp_kbn);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
        }
    }

    /**
     * フロー用仮登録ボタンを作成
     */
    private void setTempRegistButton() {
        
        Map tempRegistNameMap = getTempRegistName();
        // フロー用仮登録名を取得できない場合、何も処理を行わない。
        if (tempRegistNameMap == null || tempRegistNameMap.isEmpty() 
                || "".equals(StringUtil.nullToBlank(tempRegistNameMap.get("tmp_name")))) {
            return;
        }
        
        boolean isEnabled = true;
        // 状態が"登録済"場合、フロー用仮登録ボタンをdisabledとする
        if ("1".equals(initJotaiFlg)) {
            isEnabled = false;
        }
        String tempRegistName = StringUtil.nullToBlank(tempRegistNameMap.get("tmp_name"));
        
        // 画面上部ボタン
        FXHDD02 fxhdd02Top = new FXHDD02();
        fxhdd02Top.setButtonId("flow_temp_regist_Top");
        fxhdd02Top.setButtonName(tempRegistName);
        fxhdd02Top.setFontColor("black");
        fxhdd02Top.setEnabled(isEnabled);
        fxhdd02Top.setFontSize(17);
        fxhdd02Top.setRender(true);
        this.buttonListTop.add(fxhdd02Top);
        
        // 画面下部ボタン
        FXHDD02 fxhdd02Bottom = new FXHDD02();
        fxhdd02Bottom.setButtonId("flow_temp_regist_Bottom");
        fxhdd02Bottom.setButtonName(tempRegistName);
        fxhdd02Bottom.setFontColor("black");
        fxhdd02Bottom.setEnabled(isEnabled);
        fxhdd02Bottom.setFontSize(17);
        fxhdd02Bottom.setRender(true);
        this.buttonListBottom.add(fxhdd02Bottom);
    }

    /**
     * フロー用仮登録ボタン押下時のチェック処理
     * 
     * @return チェック処理結果
     */
    private boolean checkFlowTempItemCheckBox() {
        for (FXHDD01 fxhdd01 : itemList) {
            if (fxhdd01.isRenderTempItemCheckBox() && !"true".equals(StringUtil.nullToBlank(fxhdd01.getTempItemCheckBoxValue()).toLowerCase())) {
                FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000199", fxhdd01.getLabel1()), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return false;
            }
        }
        return true;
    }

    /**
     * フロー用仮登録ボタン押下時、仮登録処理ができるように仮登録ボタンのIDを取得
     * 
     * @return 仮登録ボタンのID
     */
    private String getTempRegistButtonId() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT button_id FROM fxhdd02 WHERE gamen_id = ? AND button_name ='仮登録'";

            List<Object> params = new ArrayList<>();
            params.add(getFormId());

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map<String, Object> result = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (result != null && !result.isEmpty()) {
                return StringUtil.nullToBlank(result.get("button_id"));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return "";
    }
    
    /**
     * 完了メッセージ表示処理
     */
    public void showCompMessage() {
        CompMessage compMessageBean;
        compMessageBean = (CompMessage) FacesContext.getCurrentInstance().
                getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                        getELContext(), null, "beanCompMessage");
        compMessageBean.setCompMessage("登録しました。");
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "complete");
    }
}
