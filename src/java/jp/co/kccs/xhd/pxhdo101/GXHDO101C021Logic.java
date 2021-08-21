/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.SrKoteifuryo;
import jp.co.kccs.xhd.model.GXHDO101C021Model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C021(B･Cﾗﾝｸ連絡書一覧画面用)ロジッククラス
 *
 * @author SRC K.Ijuin
 * @since 2021/08/21
 */
public class GXHDO101C021Logic {

    /**
     * GXHDO101C021Modelの新規作成
     *
     * @param dbResultList 工程不良テーブルから取得済のデータ
     * @return
     */
    public static GXHDO101C021Model createGXHDO101C021Model(List<SrKoteifuryo> dbResultList) {
        GXHDO101C021Model model = new GXHDO101C021Model();
        model.setTorokuNoDataList(getInitTorokuNoData(model, dbResultList));
        return model;
    }

    /**
     * DB取得結果より登録No表示用リストを作成する
     *
     * @param GXHDO101C021Model
     * @param dbResultList 工程不良テーブルから取得済のデータ
     * @return
     */
    private static List<GXHDO101C021Model.TorokuNoData> getInitTorokuNoData(GXHDO101C021Model GXHDO101C021Model, List<SrKoteifuryo> dbResultList) {
        List<GXHDO101C021Model.TorokuNoData> initTorokuNoDataList = new ArrayList<>();
        for (SrKoteifuryo data : dbResultList) {
            GXHDO101C021Model.TorokuNoData initTorokuNoData = GXHDO101C021Model.new TorokuNoData();
            initTorokuNoData.setTorokuNo(data.getTorokuno());
            initTorokuNoDataList.add(initTorokuNoData);
        }
        return initTorokuNoDataList;
    }
}
