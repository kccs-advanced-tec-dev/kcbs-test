/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO101C020Model;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/10/14<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C020(前工程WIP取込)
 *
 * @author 863 zhangjy
 * @since 2020/10/14
 */
@SessionScoped
@Named("beanGXHDO101C020")
public class GXHDO101C020 implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());
    
    /**
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    
    /**
     * フォームエラー判定
     */
    private boolean isFormError;
    
    /**
     * 前工程WIP取込サブ画面用データ
     */
    private GXHDO101C020Model gxhdO101c020Model;

    /**
     * 前工程WIP取込サブ画面用データ(表示制御用)
     */
    private GXHDO101C020Model gxhdO101c020ModelView;
    
    /**
     * コンストラクタ
     */
    public GXHDO101C020() {
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
     * 前工程WIP取込サブ画面用データ
     *
     * @return the gxhdO101c020Model
     */
    public GXHDO101C020Model getGxhdO101c020Model() {
        return gxhdO101c020Model;
    }

    /**
     * 前工程WIP取込サブ画面用データ
     *
     * @param gxhdO101c020Model the gxhdO101c020Model to set
     */
    public void setGxhdO101c020Model(GXHDO101C020Model gxhdO101c020Model) {
        this.gxhdO101c020Model = gxhdO101c020Model;
    }

    /**
     * 前工程WIP取込サブ画面用データ(表示制御用)
     *
     * @return the gxhdO101c020ModelView
     */
    public GXHDO101C020Model getGxhdO101c020ModelView() {
        return gxhdO101c020ModelView;
    }

    /**
     * 前工程WIP取込サブ画面用データ(表示制御用)
     *
     * @param gxhdO101c020ModelView the gxhdO101c020ModelView to set
     */
    public void setGxhdO101c020ModelView(GXHDO101C020Model gxhdO101c020ModelView) {
        this.gxhdO101c020ModelView = gxhdO101c020ModelView;
    }
    
    /**
     * 前工程WIP取込処理
     */
    public void doTorikomi() {
        this.isFormError = false;
        if (!commenCheck()) {
            this.isFormError = true;
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
            return;
        }
        
        try {
            String url = getUrl(); // リクエストurlを取得
            String[] user = getUser(); // ユーザー情報の配列を取得
            String[] lotnoList = getLotnoList(); // ﾛｯﾄNoの配列を取得
            
            String data = doRequest(url, user, lotnoList); // HTTPリクエストを送信する
            
            if ("error".equals(StringUtil.nullToBlank(data))) {
                this.isFormError = true;
                // エラーの場合はコールバック変数に"error"をセット
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "error");
                return;
            }
            
            Map<String, Map<String, String>> resultMaps = new HashMap<>();
            if (!StringUtil.isEmpty(data)) {
                
                JSONArray jsonArray = new JSONArray(data);
                String status = StringUtil.nullToBlank(jsonArray.get(0));
                if ("ERR".equals(status)) {
                    // レスポンスデータのstatusが「ERR」の場合、レスポンスのエラーメッセージをセット
                    String errormessage = StringUtil.nullToBlank(jsonArray.get(2));
                    setErrorMessage(null, false, errormessage);
                    this.isFormError = true;
                    // エラーの場合はコールバック変数に"error"をセット
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("firstParam", "error");
                    return;
                }
                
                JSONArray jsonResultArray = jsonArray.getJSONArray(1);
                if (jsonResultArray != null) {
                    Map<String, String> resultMap;
                    for (int i = 0; i < jsonResultArray.length(); i++) {
                        resultMap = new HashMap<>();
                        JSONObject jsonData = jsonResultArray.getJSONObject(i);
                        String lotcode = StringUtil.nullToBlank(jsonData.get("lotcode"));
                        String category = StringUtil.nullToBlank(jsonData.get("category"));
                        String hinmei = StringUtil.nullToBlank(jsonData.get("hinmei"));
                        String conventionallot = StringUtil.nullToBlank(jsonData.get("conventionallot"));
                        String lotkigo = StringUtil.nullToBlank(jsonData.get("lotkigo"));
                        String rollno = StringUtil.nullToBlank(jsonData.get("rollno"));
                        String tapelength_m = StringUtil.nullToBlank(jsonData.get("tapelength_m"));
                        String thickness_um = StringUtil.nullToBlank(jsonData.get("thickness_um"));
                        String kokeibun_pct = StringUtil.nullToBlank(jsonData.get("kokeibun_pct"));
                        String petname = StringUtil.nullToBlank(jsonData.get("petname"));
                        
                        resultMap.put("category", category);
                        resultMap.put("hinmei", hinmei);
                        resultMap.put("conventionallot", conventionallot);
                        resultMap.put("lotkigo", lotkigo);
                        resultMap.put("rollno", rollno);
                        resultMap.put("tapelength_m", tapelength_m);
                        resultMap.put("thickness_um", thickness_um);
                        resultMap.put("kokeibun_pct", kokeibun_pct);
                        resultMap.put("petname", petname);
                        
                        resultMaps.put(lotcode, resultMap);
                    }
                    
                    // 取得ﾃﾞｰﾀのﾁｪｯｸ
                    if (!checkResult(resultMaps)) {
                        this.isFormError = true;
                        // エラーの場合はコールバック変数に"error"をセット
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.addCallbackParam("firstParam", "error");
                        return;
                    }
                }
            }
            gxhdO101c020ModelView.setResultMap(resultMaps);
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実行エラー", ex, LOGGER);
        }
        
        this.gxhdO101c020Model = this.gxhdO101c020ModelView;
    }
    
    /**
     * 【前工程WIP取込】ボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean commenCheck() {
        
        // 背景色をクリア
        clearBackColor();
        
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = this.gxhdO101c020ModelView.getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            // 型ﾁｪｯｸ
            if (!NumberUtils.isNumber(genryouLotData.getValue())) {
                setError(genryouLotData, true, "XHD-000008", genryouLotData.getTypeName());
                return false;
            }
            // 型ﾁｪｯｸ
            if (StringUtil.getLength(genryouLotData.getValue()) != 15) {
                setError(genryouLotData, true, "XHD-000004", genryouLotData.getTypeName(), "15");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        for (GXHDO101C020Model.GenryouLotData genryouLotData : this.gxhdO101c020ModelView.getGenryouLotDataList()) {
            genryouLotData.setTextBackColor("");
        }
    }
    
    /**
     * エラーセット
     *
     * @param genryouLotData データ
     * @param isErr エラー
     * @param errorId エラーID
     * @param errParams エラーパラメータ
     */
    private void setError(GXHDO101C020Model.GenryouLotData genryouLotData, boolean isErr, String errorId, Object... errParams) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage(errorId, errParams), null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isErr) {
            genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }
    
    /**
     * エラーセット
     *
     * @param genryouLotData データ
     * @param isErr エラー
     * @param errormessage エラーメッセージ
     */
    private void setErrorMessage(GXHDO101C020Model.GenryouLotData genryouLotData, boolean isErr, String errormessage) {

        // メッセージをセット
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, errormessage, null);
        facesContext.addMessage(null, message);

        //エラー項目に背景色をセット
        // スタートエラー
        if (isErr) {
            genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
        }
    }

    /**
     * リクエストurlを取得
     * 
     * @return url リクエストurl
     * @throws SQLException 
     */
    private String getUrl() throws SQLException {
        String url = "http://mwip.mlcc.in.kycera.co.jp/";
        QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
        
        String sql = "select data FROM fxhbm03 where user_name = ? and key = ?";
        
        List<Object> params = new ArrayList<>();
        params.add("common_user");
        params.add("前工程WIP取込API_ドメイン");
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List<Map<String, Object>> dataList = queryRunner.query(sql, new MapListHandler(), params.toArray());
        
        if (!dataList.isEmpty()) {
            url = StringUtil.nullToBlank(dataList.get(0).get("data"));
        }
        return url + "mwip/search/sklistxhd/";
    }

    /**
     * usernameとpasswordを取得
     * 
     * @return user ユーザー情報の配列
     * @throws SQLException 
     */
    private String[] getUser() throws SQLException {
        String[] user = {"kccs", "kccs0000"};
        QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
        
        String sql = "select data FROM fxhbm03 where user_name = ? and key = ?";
        
        List<Object> params = new ArrayList<>();
        params.add("common_user");
        params.add("前工程WIP取込API_ユーザー情報");
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List<Map<String, Object>> dataList = queryRunner.query(sql, new MapListHandler(), params.toArray());
        
        if (!dataList.isEmpty()) {
            String userData = StringUtil.nullToBlank(dataList.get(0).get("data"));
            user = userData.split(",");
        }
        return user;
    }

    /**
     * HTTPリクエストを送信する
     * 
     * @param appUrl リクエストURL
     * @param user ユーザー情報の配列
     * @param lotnoList ﾛｯﾄNoの配列
     * @return レスポンスデータ
     */
    private String doRequest(String appUrl, String[] user, String[] lotnoList) {
        StringBuilder sb=new StringBuilder();
        try {
            URL url = new URL(appUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.connect();
            
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("username", user[0]);
            obj.put("password", user[1]);
            obj.put("lotlist", lotnoList);
            
            out.writeBytes(obj.toString());
            out.flush();
            out.close();
            
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                InputStream in1 = connection.getInputStream();
                String readLine;
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                while((readLine = responseReader.readLine()) != null){
                    sb.append(readLine);
                }
                   responseReader.close();
            }
            
            connection.disconnect();
            
        } catch (MalformedURLException ex) { 
            // HTTPリクエストが異常の場合、エラーメッセージを表示
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000198", ""), null);
            facesContext.addMessage(null, message);
            return "error";
        } catch (IOException ex) {
            // HTTPリクエストが異常の場合、エラーメッセージを表示
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000198", ""), null);
            facesContext.addMessage(null, message);
            return "error";
        }
        return sb.toString();
    }

    /**
     * レスポンスデータのチェック
     * 
     * @param resultMaps レスポンスデータmap
     * @return チェック結果
     */
    private boolean checkResult(Map<String, Map<String, String>> resultMaps) {
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gxhdO101c020ModelView.getGenryouLotDataList();
        
        String tapelot1hinmei = "";
        String tapelot2hinmei = "";
        String tapelot3hinmei = "";
        String pastelot1hinmei = "";
        String pastelot2hinmei = "";
        boolean hasTapelot1 = false;
        boolean hasTapelot2 = false;
        boolean hasTapelot3 = false;
        boolean hasPastelot1 = false;
        boolean hasPastelot2 = false;
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            Map<String, String> result = resultMaps.get(value);
            if (result == null) {
                continue;
            }
            String category = StringUtil.nullToBlank(result.get("category"));
            switch (typeName) {
                case GXHDO101C020Model.TAPE_LOT_1:
                case GXHDO101C020Model.TAPE_LOT_2:
                case GXHDO101C020Model.TAPE_LOT_3:
                case GXHDO101C020Model.UWA_TANSHI:
                case GXHDO101C020Model.SHITA_TANSHI:
                    if (!"S".equals(category)) {
                        setError(genryouLotData, true, "XHD-000200", "");
                        return false;
                    }
                    break;
                case GXHDO101C020Model.PASTE_LOT_1:
                case GXHDO101C020Model.PASTE_LOT_2:
                    if (!"P".equals(category)) {
                        setError(genryouLotData, true, "XHD-000201", "");
                        return false;
                    }
                    break;
            }
            
            String hinmei = StringUtil.nullToBlank(result.get("hinmei"));
            switch (typeName) {
                case GXHDO101C020Model.TAPE_LOT_1:
                    hasTapelot1 = true;
                    tapelot1hinmei = hinmei;
                    break;
                case GXHDO101C020Model.TAPE_LOT_2:
                    hasTapelot2 = true;
                    tapelot2hinmei = hinmei;
                    break;
                case GXHDO101C020Model.TAPE_LOT_3:
                    hasTapelot3 = true;
                    tapelot3hinmei = hinmei;
                    break;
                case GXHDO101C020Model.PASTE_LOT_1:
                    hasPastelot1 = true;
                    pastelot1hinmei = hinmei;
                    break;
                case GXHDO101C020Model.PASTE_LOT_2:
                    hasPastelot2 = true;
                    pastelot2hinmei = hinmei;
                    break;
            }
        }
        
        if (hasTapelot1 && hasTapelot2 && !tapelot1hinmei.equals(tapelot2hinmei)) {
            setError(null, false, "XHD-000202", "");
            return false;
        }
        if (hasTapelot2 && hasTapelot3 && !tapelot2hinmei.equals(tapelot3hinmei)) {
            setError(null, false, "XHD-000202", "");
            return false;
        }
        if (hasTapelot1 && hasTapelot3 && !tapelot1hinmei.equals(tapelot3hinmei)) {
            setError(null, false, "XHD-000202", "");
            return false;
        }
        
        if (hasPastelot1 && hasPastelot2 && !pastelot1hinmei.equals(pastelot2hinmei)) {
            setError(null, false, "XHD-000203", "");
            return false;
        }
        
        return true;
    }

    /**
     * ﾛｯﾄNoの配列を取得
     * 
     * @return 
     */
    private String[] getLotnoList() {
        List<String> arraylist = new ArrayList<>();
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gxhdO101c020ModelView.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            arraylist.add(genryouLotData.getValue());
        }
        return arraylist.toArray(new String[arraylist.size()]);
    }
    
    /**
     * 電極ﾃｰﾌﾟ警告メッセージ 
     * 
     * @return tapeWarnMessage the tapeWarnMessage
     */
    public String getTapeWarnMessage() {
        return MessageUtil.getMessage("XHD-000204", "");
    }

    /**
     * 電極ペースト警告メッセージ 
     * 
     * @return pasteWarnMessage the pasteWarnMessage
     */
    public String getPasteWarnMessage() {
        return MessageUtil.getMessage("XHD-000205", "");
    }

    /**
     * 上端子警告メッセージ 
     * 
     * @return uwaTWarnMessage the uwaTWarnMessage
     */
    public String getUwaTWarnMessage() {
        return MessageUtil.getMessage("XHD-000206", "");
    }

    /**
     * 下端子警告メッセージ 
     * 
     * @return shitaTWarnMessage the shitaTWarnMessage
     */
    public String getShitaTWarnMessage() {
        return MessageUtil.getMessage("XHD-000207", "");
    }

    /**
     * 電極ﾍﾟｰｽﾄﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public void checkTapeSameHinmeiOK() {
        GXHDO101C020Logic.notCheckTapeSameHinmei();
    }
    
    /**
     * 電極ﾃｰﾌﾟﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public void checkPasteSameHinmeiOK() {
        GXHDO101C020Logic.notCheckPasteSameHinmei();
    }

    /**
     * 上端子ﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public void checkUwaTanshiSameHinmeiOK() {
        GXHDO101C020Logic.notCheckUwaTanshiSameHinmei();
    }

    /**
     * 下端子ﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public void checkShitaTanshiSameHinmeiOK() {
        GXHDO101C020Logic.notCheckShitaTanshiSameHinmei();
    }
}
