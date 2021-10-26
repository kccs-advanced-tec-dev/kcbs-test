/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.db.model.FXHDD12;
import jp.co.kccs.xhd.model.GXHDO101C022Model;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import jp.co.kccs.xhd.util.SubFormUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/10/08<br>
 * 計画書No	MB2109-DK002<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C022(ﾃｰﾋﾟﾝｸﾞ号機選択画面)ロジッククラス
 *
 * @author SRC T.Ushiyama
 * @since 2021/10/08
 */
public class GXHDO101C022 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101C022.class.getName());
    
    /**
     * 初期化処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    @Override
    public ProcessData initial(ProcessData processData) {

        try {

            // 処理名を登録
            processData.setProcessName("initial");

            // 初期表示データ設定処理
            processData = setInitData(processData);
            // 中断エラー発生時
            if (processData.isFatalError()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                }
                return processData;
            }

            // エラーが発生していない場合
            if (processData.getErrorMessageInfoList().isEmpty()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                } else {
                    // 後続処理なし
                    processData.setMethod("");
                }
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }

    /**
     * 初期表示データ設定
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    private ProcessData setInitData(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String jissekiNo = StringUtil.nullToBlank(session.getAttribute("jissekino"));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        String strKojyo = "";
        String strLotNo = "";
        String strEdaban = "";

        if (!"".equals(lotNo) && lotNo != null) {
            strKojyo = lotNo.substring(0, 3);
            strLotNo = lotNo.substring(3, 11);
            strEdaban = lotNo.substring(11, 14);
        }

        // ﾃｰﾋﾟﾝｸﾞ号機選択からﾃﾞｰﾀを取得する
        List<FXHDD12> listFXHDD12 = getFxhdd12List(queryRunnerDoc, strKojyo, strLotNo, strEdaban,Integer.parseInt(jissekiNo));

        // GXHDO101C022 bean初期化
        GXHDO101C022A beanGXHDO101C022 = (GXHDO101C022A) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C022);

        // GXHDO101C022 初回表示用Model作成
        GXHDO101C022Model newModel = createGXHDO101C022Model(listFXHDD12);
        beanGXHDO101C022.setGxhdO101c022ModelView(newModel);

        beanGXHDO101C022.setTableRender(true);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機選択取得
     *
     * @param queryRunnerXHD データオブジェクト
     * @param kojyo
     * @param lotNo
     * @param edaban
     * @param jissekino
     * @return
     * @throws SQLException
     */
    private List<FXHDD12> getFxhdd12List(QueryRunner queryRunnerXHD, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT rev,bango,goki,jotai_flg "
                + "FROM fxhdd12 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? "
                + "ORDER BY bango ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("rev", "rev");
        mapping.put("bango", "bango");
        mapping.put("goki", "goki");
        mapping.put("jotai_flg", "jotai_flg");
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDD12>> beanHandler = new BeanListHandler<>(FXHDD12.class, rowProcessor);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerXHD.query(sql, beanHandler, params.toArray());

    }

    /**
     * GXHDO101C022Modelの新規作成
     *
     * @param dbResultList ﾃｰﾋﾟﾝｸﾞ号機選択から取得済のデータ
     * @return
     */
    public static GXHDO101C022Model createGXHDO101C022Model(List<FXHDD12> dbResultList) {
        GXHDO101C022Model model = new GXHDO101C022Model();
        model.setGokiSentakuDataList(getInitGokiSentakuData(model, dbResultList));
        return model;
    }

    /**
     * DB取得結果より表示用リストを作成する
     *
     * @param GXHDO101C022Model
     * @param dbResultList ﾃｰﾋﾟﾝｸﾞ号機選択から取得済のデータ
     * @return
     */
    private static List<GXHDO101C022Model.GokiSentakuData> getInitGokiSentakuData(GXHDO101C022Model GXHDO101C022Model, List<FXHDD12> dbResultList) {
        List<GXHDO101C022Model.GokiSentakuData> initGokiSentakuDataList = new ArrayList<>();
        for (int i = 1; i < 11; i++){
            GXHDO101C022Model.GokiSentakuData initGokiSentakuData = GXHDO101C022Model.new GokiSentakuData();
            String dispGoki = "";
            switch(i){
                case 1:
                    dispGoki = "号機①";
                    break;
                case 2:
                    dispGoki = "号機②";
                    break;
                case 3:
                    dispGoki = "号機③";
                    break;
                case 4:
                    dispGoki = "号機④";
                    break;
                case 5:
                    dispGoki = "号機⑤";
                    break;
                case 6:
                    dispGoki = "号機⑥";
                    break;
                case 7:
                    dispGoki = "号機⑦";
                    break;
                case 8:
                    dispGoki = "号機⑧";
                    break;
                case 9:
                    dispGoki = "号機⑨";
                    break;
                case 10:
                    dispGoki = "号機⑩";
                    break;
            }
            initGokiSentakuData.setDispgoki(dispGoki);
            for (FXHDD12 data : dbResultList) {
                if (data.getBango() == i){
                    initGokiSentakuData.setBango(data.getBango());
                    initGokiSentakuData.setGoki(data.getGoki());
                    switch(data.getJotai_flg()){
                        case "0":
                            initGokiSentakuData.setJotai("仮登録");
                            break;
                        case "1":
                            initGokiSentakuData.setJotai("登録済");
                            break;
                        case "9":
                            initGokiSentakuData.setJotai("未登録");
                            break;
                    }
                                
                    initGokiSentakuData.setRev(data.getRev());
                    initGokiSentakuDataList.add(initGokiSentakuData);
                    break;
                }
            }
            if (initGokiSentakuDataList.size() != i){
                    initGokiSentakuData.setBango(i);
                    initGokiSentakuData.setGoki("");
                    initGokiSentakuData.setJotai("未登録");
                    initGokiSentakuData.setRev(0);
                    initGokiSentakuDataList.add(initGokiSentakuData);
            }
        }
        return initGokiSentakuDataList;
    }

    /**
     * ボタンID⇒メソッド名変換
     *
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method = "";

        return method;
    }
}
