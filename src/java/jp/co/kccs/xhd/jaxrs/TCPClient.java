package jp.co.kccs.xhd.jaxrs;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    //テスト用ソースコードスタート
    public static final String SERVICE_IP = "10.150.86.19";

    public static final int SERVICE_PORT = 8501;
    
    public static final String COMMAND_CONTEXT = "WRS DM1100.H 8 ";
    
    public static final String SEND_LOTNO = "001460000061001";
    
    protected String COMMAND_TERMINATOR = "\r";
    // レスポンス終端文字
    protected String  RESPONSE_TERMINATOR = "\r\n";
    // 書き込みタイムアウト[ms]
    private static final int WRITE_TIMEOUT_DEFULT = 10000;

    public static void main(String[] args) {
        TCPClient client = new TCPClient();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\r【コマンド内容を入力して、エンターキーで送信】");
        while(true){
            String inputMsg = scanner.nextLine();
            if("#".equals(inputMsg))
                break;
            System.out.println("[Socket受信内容] " + client.executeCommand(SERVICE_IP, SERVICE_PORT, inputMsg + COMMAND_CONTEXT, SEND_LOTNO));
            System.out.println();
        }
    }
    //テスト用ソースコードエンド

    /**
     * コマンドの実行処理
     *
     * @param ipAddress
     * @param port
     * @param inputCommand
     * @param lotNo
     * @return レスポンス文字列
     */
    public String executeCommand(String ipAddress, int port, String inputCommand, String lotNo){
        
        String sendLotno = "";
        String [] lotNoArr= lotNo.split("");
        for(int i = 0; i < lotNoArr.length ; i++){
            sendLotno = sendLotno + hexString(lotNoArr[i]);
            if(i%2 == 1){
                sendLotno = sendLotno + " ";
            }
        }
        sendLotno = sendLotno + "00";
        System.out.println("[sendLotno]: " + sendLotno);
        
        String command = inputCommand + sendLotno + COMMAND_TERMINATOR;
        System.out.println("[Socket送信 コマンド内容]: " + command);

        //配列定義
        //char[] chArray = command.toCharArray();
        StringBuilder receiveMsg = new StringBuilder();

        Socket socket = new Socket();
        try{
            socket.connect(new InetSocketAddress(ipAddress, port),WRITE_TIMEOUT_DEFULT);

            OutputStream out = socket.getOutputStream();
            
            out.write(command.getBytes());

/*
            //コマンド文字列をバイト配列に変換
            for (int iCount = 0; iCount < chArray.length; iCount++)
            {
                //コマンド送信
                out.write((byte)(chArray[iCount]));
                System.out.println("[Socket送信 アスキー内容][" + iCount +"]: " + (byte)(chArray[iCount]));
            }
            System.out.println("[Socket送信終了]");
*/
            InputStream inputStream = socket.getInputStream();
            for (int charVal = inputStream.read(); (charVal != 10); charVal = inputStream.read()) {
                // 終端文字が含まれていれば受信完了とする
                if (RESPONSE_TERMINATOR.equals((byte)charVal)) {
                    break;
                }
                if(charVal == -1)
                    break;
                receiveMsg.append((char)charVal);
            }
        }catch (IOException e){
            System.out.println("[Socket通信] Exception発生:" + e);
        }
        return receiveMsg.toString();
    }
    
    // 16進数に変換する関数
    private static String hexString(String strVal) {
        try {
            byte[] buf = strVal.getBytes("Windows-31J");
            String result = "";

            for (byte b : buf) {
                result += String.format("%02x", b);
            }

            return result;

        } catch (UnsupportedEncodingException e) {
            return "[Socket通信] Exception発生:" + e;
        }
    }
}