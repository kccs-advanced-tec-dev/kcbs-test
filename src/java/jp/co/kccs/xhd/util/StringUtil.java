/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * 文字列操作用に使用する関数群です。
 *
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
public class StringUtil {

    private static final Logger LOGGER = Logger.getLogger(StringUtil.class.getName());

    /**
     * コンストラクタ
     *
     */
    private StringUtil() {
    }

    /**
     * Charのマップを取得します。
     *
     * @param str str
     * @param charMap マップ
     * @return マップ
     */
    public static String getCharMapStr(String str, HashMap<String, String> charMap) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String s = String.valueOf(str.charAt(i));
            try {
                if (charMap.containsKey(s)) {
                    s = charMap.get(s);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
            buf.append(s);
        }
        return buf.toString();
    }

    /**
     * 半角Charのマップを取得します。
     *
     * @return 半角Charのマップ
     */
    public static HashMap<String, String> toHalfCharMap() {
        HashMap<String, String> charMap = new HashMap<>();
        charMap.put("Ａ", "A");
        charMap.put("Ｂ", "B");
        charMap.put("Ｃ", "C");
        charMap.put("Ｄ", "D");
        charMap.put("Ｅ", "E");
        charMap.put("Ｆ", "F");
        charMap.put("Ｇ", "G");
        charMap.put("Ｈ", "H");
        charMap.put("Ｉ", "I");
        charMap.put("Ｊ", "J");
        charMap.put("Ｋ", "K");
        charMap.put("Ｌ", "L");
        charMap.put("Ｍ", "M");
        charMap.put("Ｎ", "N");
        charMap.put("Ｏ", "O");
        charMap.put("Ｐ", "P");
        charMap.put("Ｑ", "Q");
        charMap.put("Ｒ", "R");
        charMap.put("Ｓ", "S");
        charMap.put("Ｔ", "T");
        charMap.put("∪", "U");
        charMap.put("∨", "V");
        charMap.put("Ｗ", "W");
        charMap.put("Ｘ", "X");
        charMap.put("Ｙ", "Y");
        charMap.put("Ｚ", "Z");
        // -------------------------
        charMap.put("ａ", "a");
        charMap.put("ｂ", "b");
        charMap.put("ｃ", "c");
        charMap.put("ｄ", "d");
        charMap.put("ｅ", "e");
        charMap.put("ｆ", "f");
        charMap.put("ｇ", "g");
        charMap.put("ｈ", "h");
        charMap.put("ｉ", "i");
        charMap.put("ｊ", "j");
        charMap.put("ｋ", "k");
        charMap.put("ｌ", "l");
        charMap.put("ｍ", "m");
        charMap.put("ｎ", "n");
        charMap.put("ｏ", "o");
        charMap.put("ｐ", "p");
        charMap.put("ｑ", "q");
        charMap.put("ｒ", "r");
        charMap.put("ｓ", "s");
        charMap.put("ｔ", "t");
        charMap.put("ｕ", "u");
        charMap.put("ｖ", "v");
        charMap.put("ｗ", "w");
        charMap.put("ｘ", "x");
        charMap.put("ｙ", "y");
        charMap.put("ｚ", "z");
        // -------------------------
        charMap.put("０", "0");
        charMap.put("１", "1");
        charMap.put("２", "2");
        charMap.put("３", "3");
        charMap.put("４", "4");
        charMap.put("５", "5");
        charMap.put("６", "6");
        charMap.put("７", "7");
        charMap.put("８", "8");
        charMap.put("９", "9");
        // -------------------------
        charMap.put("！", "!");
        charMap.put("”", "\"");
        charMap.put("＃", "#");
        charMap.put("＄", "$");
        charMap.put("％", "%");
        charMap.put("＆", "&");
        charMap.put("’", "\"");
        charMap.put("（", "(");
        charMap.put("）", ")");
        charMap.put("＝", "=");
        charMap.put("〜", "~");
        charMap.put("ー", "-");
        charMap.put("｜", "|");
        charMap.put("￥", "\\");
        charMap.put("＠", "@");
        charMap.put("「", "[");
        charMap.put("｛", "{");
        charMap.put("」", "]");
        charMap.put("［", "[");
        charMap.put("］", "]");
        charMap.put("＋", "+");
        charMap.put("＊", "*");
        charMap.put("；", ";");
        charMap.put("：", ":");
        charMap.put("＜", "<");
        charMap.put("＞", ">");
        charMap.put("、", ",");
        charMap.put("。", ".");
        charMap.put("．", ".");
        charMap.put("／", "/");
        charMap.put("？", "?");
        charMap.put("＿", "_");
        // -------------------------
        charMap.put("あ", "ｱ");
        charMap.put("い", "ｲ");
        charMap.put("う", "ｳ");
        charMap.put("え", "ｴ");
        charMap.put("お", "ｵ");
        charMap.put("か", "ｶ");
        charMap.put("き", "ｷ");
        charMap.put("く", "ｸ");
        charMap.put("け", "ｹ");
        charMap.put("こ", "ｺ");
        charMap.put("さ", "ｻ");
        charMap.put("し", "ｼ");
        charMap.put("す", "ｽ");
        charMap.put("せ", "ｾ");
        charMap.put("そ", "ｿ");
        charMap.put("た", "ﾀ");
        charMap.put("ち", "ﾁ");
        charMap.put("つ", "ﾂ");
        charMap.put("て", "ﾃ");
        charMap.put("と", "ﾄ");
        charMap.put("な", "ﾅ");
        charMap.put("に", "ﾆ");
        charMap.put("ぬ", "ﾇ");
        charMap.put("ね", "ﾈ");
        charMap.put("の", "ﾉ");
        charMap.put("は", "ﾊ");
        charMap.put("ひ", "ﾋ");
        charMap.put("ふ", "ﾌ");
        charMap.put("へ", "ﾍ");
        charMap.put("ほ", "ﾎ");
        charMap.put("ま", "ﾏ");
        charMap.put("み", "ﾐ");
        charMap.put("む", "ﾑ");
        charMap.put("め", "ﾒ");
        charMap.put("も", "ﾓ");
        charMap.put("や", "ﾔ");
        charMap.put("ゆ", "ﾕ");
        charMap.put("よ", "ﾖ");
        charMap.put("ら", "ﾗ");
        charMap.put("り", "ﾘ");
        charMap.put("る", "ﾙ");
        charMap.put("れ", "ﾚ");
        charMap.put("ろ", "ﾛ");
        charMap.put("わ", "ﾜ");
        charMap.put("を", "ｦ");
        charMap.put("ん", "ﾝ");
        charMap.put("が", "ｶﾞ");
        charMap.put("ぎ", "ｷﾞ");
        charMap.put("ぐ", "ｸﾞ");
        charMap.put("げ", "ｹﾞ");
        charMap.put("ご", "ｺﾞ");
        charMap.put("ざ", "ｻﾞ");
        charMap.put("じ", "ｼﾞ");
        charMap.put("ず", "ｽﾞ");
        charMap.put("ぜ", "ｾﾞ");
        charMap.put("ぞ", "ｿﾞ");
        charMap.put("だ", "ﾀﾞ");
        charMap.put("ぢ", "ﾁﾞ");
        charMap.put("づ", "ﾂﾞ");
        charMap.put("で", "ﾃﾞ");
        charMap.put("ど", "ﾄﾞ");
        charMap.put("ば", "ﾊﾞ");
        charMap.put("び", "ﾋﾞ");
        charMap.put("ぶ", "ﾌﾞ");
        charMap.put("べ", "ﾍﾞ");
        charMap.put("ぼ", "ﾎﾞ");
        charMap.put("ぱ", "ﾊﾟ");
        charMap.put("ぴ", "ﾋﾟ");
        charMap.put("ぷ", "ﾌﾟ");
        charMap.put("ぺ", "ﾍﾟ");
        charMap.put("ぽ", "ﾎﾟ");
        charMap.put("ぁ", "ｧ");
        charMap.put("ぃ", "ｨ");
        charMap.put("ぅ", "ｩ");
        charMap.put("ぇ", "ｪ");
        charMap.put("ぉ", "ｫ");
        charMap.put("ゃ", "ｬ");
        charMap.put("ゅ", "ｭ");
        charMap.put("ょ", "ｮ");
        charMap.put("っ", "ｯ");
        // -------------------------
        charMap.put("ア", "ｱ");
        charMap.put("イ", "ｲ");
        charMap.put("ウ", "ｳ");
        charMap.put("エ", "ｴ");
        charMap.put("オ", "ｵ");
        charMap.put("カ", "ｶ");
        charMap.put("キ", "ｷ");
        charMap.put("ク", "ｸ");
        charMap.put("ケ", "ｹ");
        charMap.put("コ", "ｺ");
        charMap.put("サ", "ｻ");
        charMap.put("シ", "ｼ");
        charMap.put("ス", "ｽ");
        charMap.put("セ", "ｾ");
        charMap.put("ソ", "ｿ");
        charMap.put("タ", "ﾀ");
        charMap.put("チ", "ﾁ");
        charMap.put("ツ", "ﾂ");
        charMap.put("テ", "ﾃ");
        charMap.put("ト", "ﾄ");
        charMap.put("ナ", "ﾅ");
        charMap.put("ニ", "ﾆ");
        charMap.put("ヌ", "ﾇ");
        charMap.put("ネ", "ﾈ");
        charMap.put("ノ", "ﾉ");
        charMap.put("ハ", "ﾊ");
        charMap.put("ヒ", "ﾋ");
        charMap.put("フ", "ﾌ");
        charMap.put("ヘ", "ﾍ");
        charMap.put("ホ", "ﾎ");
        charMap.put("マ", "ﾏ");
        charMap.put("ミ", "ﾐ");
        charMap.put("ム", "ﾑ");
        charMap.put("メ", "ﾒ");
        charMap.put("モ", "ﾓ");
        charMap.put("ヤ", "ﾔ");
        charMap.put("ユ", "ﾕ");
        charMap.put("ヨ", "ﾖ");
        charMap.put("ラ", "ﾗ");
        charMap.put("リ", "ﾘ");
        charMap.put("ル", "ﾙ");
        charMap.put("レ", "ﾚ");
        charMap.put("ロ", "ﾛ");
        charMap.put("ワ", "ﾜ");
        charMap.put("ヲ", "ｦ");
        charMap.put("ン", "ﾝ");
        charMap.put("ガ", "ｶﾞ");
        charMap.put("ギ", "ｷﾞ");
        charMap.put("グ", "ｸﾞ");
        charMap.put("ゲ", "ｹﾞ");
        charMap.put("ゴ", "ｺﾞ");
        charMap.put("ザ", "ｻﾞ");
        charMap.put("ジ", "ｼﾞ");
        charMap.put("ズ", "ｽﾞ");
        charMap.put("ゼ", "ｾﾞ");
        charMap.put("ゾ", "ｿﾞ");
        charMap.put("ダ", "ﾀﾞ");
        charMap.put("ヂ", "ﾁﾞ");
        charMap.put("ヅ", "ﾂﾞ");
        charMap.put("デ", "ﾃﾞ");
        charMap.put("ド", "ﾄﾞ");
        charMap.put("バ", "ﾊﾞ");
        charMap.put("ビ", "ﾋﾞ");
        charMap.put("ブ", "ﾌﾞ");
        charMap.put("ベ", "ﾍﾞ");
        charMap.put("ボ", "ﾎﾞ");
        charMap.put("パ", "ﾊﾟ");
        charMap.put("ピ", "ﾋﾟ");
        charMap.put("プ", "ﾌﾟ");
        charMap.put("ペ", "ﾍﾟ");
        charMap.put("ポ", "ﾎﾟ");
        charMap.put("ァ", "ｧ");
        charMap.put("ィ", "ｨ");
        charMap.put("ゥ", "ｩ");
        charMap.put("ェ", "ｪ");
        charMap.put("ォ", "ｫ");
        charMap.put("ャ", "ｬ");
        charMap.put("ュ", "ｭ");
        charMap.put("ョ", "ｮ");
        charMap.put("ッ", "ｯ");
        charMap.put("ヴ", "ｳﾞ");

        // -------------------------
        return charMap;
    }

    /**
     * 母音変換用のマップを取得します。
     *
     * @return 母音変換用のマップ
     */
    public static HashMap<String, String> toVowelCharMap() {
        HashMap<String, String> charMap = new HashMap<>();
        charMap.put("あ", "A");
        charMap.put("い", "I");
        charMap.put("う", "U");
        charMap.put("え", "E");
        charMap.put("お", "O");
        // -------------------------
        charMap.put("ア", "A");
        charMap.put("イ", "I");
        charMap.put("ウ", "U");
        charMap.put("エ", "E");
        charMap.put("オ", "O");
        // -------------------------
        charMap.put("ｱ", "A");
        charMap.put("ｲ", "I");
        charMap.put("ｳ", "U");
        charMap.put("ｴ", "E");
        charMap.put("ｵ", "O");
        // -------------------------
        return charMap;
    }

    /**
     * 文字列のﾊﾞｲﾄ数を返します。
     *
     * @param strValue 変換対象文字列
     * @param strEncode 変換時文字ｺｰﾄﾞ
     * @param logger ﾛｸﾞｸﾗｽ
     * @return ﾊﾞｲﾄ数
     */
    public static int getByte(String strValue, String strEncode, Logger logger) {
        if (null == strValue || "".equals(strValue)) {
            return 0;
        }
        int intValue = 0;
        try {
            intValue = strValue.getBytes(strEncode).length;
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new InvalidParameterException(ex.toString());
        }
        return intValue;
    }

    /**
     * 文字列が空白またはNULLの場合、trueを返します。
     *
     * @param src 文字列
     * @return 判定値
     */
    public static boolean isEmpty(String src) {
        return src == null || "".equals(src);
    }

    /**
     * 文字列がNULL値の場合、空の値を返します。NULL以外の場合、そのままの値を返します。
     *
     * @param src 文字列
     * @return 変換値
     */
    public static String nullToBlank(String src) {
        String output = src;
        if (output == null) {
            output = "";
        }
        return output;
    }
    
    /**
     * 引数がNULL値の場合、空の値を返します。NULL以外の場合、文字列変換した値(toString())を返します。
     *
     * @param src 文字列
     * @return 変換値
     */
    public static String nullToBlank(Object src) {
        if (null == src) {
            return "";
        }
        return src.toString();
    }

    /**
     * 文字数を取得
     *
     * @param src 対象文字列
     * @return 文字数
     */
    public static int length(String src) {

        if (src != null) {
            return src.length();
        } else {
            return 0;
        }
    }

    /**
     * 文字列の左側から指定文字数取得
     *
     * @param src 対象文字列
     * @param len 対象文字数
     * @return 取得した文字列
     */
    public static String left(String src, int len) {

        if (src == null) {
            return "";
        }

        if (length(src) <= len) {
            return src;
        }

        return subString(src, 0, len);
    }

    /**
     * 文字列の右側から指定文字数取得
     *
     * @param src 対象文字列
     * @param len 取得文字数
     * @return 取得文字列
     */
    public static String right(String src, int len) {

        if (src == null) {
            return "";
        }

        if (length(src) <= len) {
            return src;
        }

        return src.substring(src.length() - len, src.length());
    }

    /**
     * FROM～TOを文字数指定で文字部分抜き出し
     *
     * @param src 対象文字列
     * @param startIndex FROM
     * @param endIndex TO
     * @return 切り出した文字列
     */
    public static String mid(String src, int startIndex, int endIndex) {

        if (src == null) {
            return "";
        }

        int len = length(src);

        if (endIndex < startIndex) {
            return "";
        }

        if (len < startIndex) {
            return "";
        }

        String tmpRight = right(src, len - startIndex + 1);
        return left(tmpRight, endIndex - startIndex + 1);

    }

    /**
     * 右トリムを行います.
     *
     * @param str 処理対象文字列
     * @return トリムされた文字列
     */
    public static String trimRight(String str) {

        if (str == null || "".equals(str)) {
            return "";
        }

        String s = str;
        int idx = s.length() - 1;
        char[] c = s.toCharArray();
        for (int i = idx; 0 <= i; i--) {
            if (c[i] != ' ' && c[i] != '　') {
                idx = i;
                break;
            }
        }
        return s.substring(0, idx + 1);
    }
    
    /**
     * 左トリムを行います.
     *
     * @param str 処理対象文字列
     * @return トリムされた文字列
     */
    public static String trimLeft(String str) {

        if (str == null || "".equals(str)) {
            return "";
        }

        boolean bSpaceAll = true;
        String s = str;
        int idx = 0;
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (c[i] != ' ' && c[i] != '　') {
                idx = i;
                bSpaceAll = false;
                break;
            }
        }

        if (bSpaceAll) {
            return "";
        } else {
            return s.substring(idx);
        }
    }
    
    /**
     * 文字列がNULL値または空白の場合、半角スペースを返します。
     *
     * @param src 文字列
     * @return 変換値
     */
    public static String emptyToSpace(String src) {
        String output = src;
        if(isEmpty(src)){
            output = " ";
        }
        return output;
    }
    
    /**
     * 指定された文字列に含まれる特殊文字を、HTML用にエスケープします。
     *
     * @param value エスケープ処理前の文字列
     * @param emptyReplaceValue value値がnullまたは空文字の場合の代替文字列
     * @return エスケープ処理後の文字列
     */
    public static String escapeForXhtml(String value, String emptyReplaceValue) {
        if (isEmpty(value)) {
            return emptyReplaceValue;
        }
        
        return value.replaceAll("\\&", "&amp;")
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("\"", "&quot;")
                        .replaceAll("'", "&#39;");
    }
    
    /**
     * 文字列の左側から指定バイト数取得。
     *
     * @param src 対象文字列
     * @param len 取得文字数
     * @param setCharSet キャラセット
     * @return 取得した文字列
     */
    public static String leftB(String src, int len, String setCharSet) {

        String ret = "";
        if (StringUtil.isEmpty(src)) {
            return ret;
        }
        String charSet = "MS932";
        if (!StringUtil.isEmpty(setCharSet)) {
            charSet = setCharSet;
        }
        
        int cnt = 0;
        String wk;
        char[] srcChars = src.toCharArray();
        for (int i = 0, codePoint; i < srcChars.length; i += Character.charCount(codePoint)) {
            codePoint = Character.codePointAt(srcChars, i);
            wk = String.valueOf(Character.toChars(codePoint));
            cnt += getByte(wk, charSet, LOGGER);
            if (cnt <= len) {
                ret += wk;
            }
            if (cnt >= len) {
                break;
            }
        }
        return ret;

    }
	
    /**
     * サロゲートペアのsubString文字の取得
     *
     * @param target 対象文字列
     * @param startIndex 開始位置（0から）
     * @param endIndex 終了位置
     * @return
     */
    private static String subString(String target, int startIndex, int endIndex) {
        // 入力値チェック
        if (startIndex > endIndex) {
            int tmp = startIndex;
            startIndex = endIndex;
            endIndex = tmp;
        }
        // 文字数取得
        int len = getLength(target);
        if (0 > startIndex) {
            startIndex = 0;
        } else if (startIndex > len) {
            startIndex = len;
        }
        if (0 > endIndex) {
            endIndex = 0;
        } else if (endIndex > len) {
            endIndex = len;
        }

        // char配列の取得
        char[] charArray = target.toCharArray();

        // コードポイント分だけの開始位置インデックスの取得（繰り返し用）
        int offsetStart = target.offsetByCodePoints(0, startIndex);
        // コードポイント分だけの終了位置インデックスの取得（繰り返し用）
        int offsetEnd = target.offsetByCodePoints(0, endIndex);
        // コードポイント初期値
        int codePoint = 0;

        StringBuilder sb = new StringBuilder();

        for (int i = offsetStart; i < offsetEnd; i += Character.charCount(codePoint)) {

            // カレント文字のコードポイントの取得
            codePoint = Character.codePointAt(charArray, i);
            sb.append(String.valueOf(Character.toChars(codePoint)));
        }

        return sb.toString();
    }

    /**
     * 与えられた文字列の文字数を返却します。<br>
     * サロゲートペア文字対応
     *
     * @param target　字列
     * @return 桁数
     */
    public static int getLength(String target) {
        return target.codePointCount(0, target.length());
    }
}
