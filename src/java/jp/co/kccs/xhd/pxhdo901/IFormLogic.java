/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/05/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * 品質DB画面ロジックインターフェース
 * 
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
public interface IFormLogic {
    /**
     * 画面初期表示時に起動されるメソッド
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    ProcessData initial(ProcessData processData);
    /**
     * 画面のボタン押下時に起動されるメソッド
     * @param buttonId ボタンID
     * @return ボタン押下時に起動する画面クラスのメソッド名
     */
    String convertButtonIdToMethod(String buttonId);
}
