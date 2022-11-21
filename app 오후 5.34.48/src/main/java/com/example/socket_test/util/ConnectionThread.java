package com.example.socket_test.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.socket_test.DialogActivity;
import com.example.socket_test.TestApplication;
import com.example.socket_test.objects.Header;
import com.example.socket_test.objects.Response;
import com.example.socket_test.objects.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ConnectionThread extends Thread{
    public final String TAG = getClass().getSimpleName();
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Context context;
    public boolean mustConnect = false;
    public boolean mustDisconnect = false;
    public User user;

    public ConnectionThread(User user, Context context){
        super();
        this.user = user;
        this.context = context;
    }

    public void connectServer(User user){
        try {
            //Socket IP와 Port 설정.
            if(socket == null){
                socket = new Socket("192.168.219.104", 3333);
//                socket = new Socket("172.20.10.2", 3333);
                //InputStream으로 데이터를 받아온다.
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                Log.d(TAG, "connectServer: ");
                Header header = new Header();
                String data = new Gson().toJson(user);
                header.code = 'C';
                header.length = data.getBytes().length;
                header.hash = "hash";

                ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH + data.getBytes().length);
                byteBuffer.put(header.getHeader());
                byteBuffer.put(data.getBytes());
                outputStream.write(byteBuffer.array());

                Header h = new Header();
                h.code = 'M';
                h.length = 0;
                h.hash = "hash";

                outputStream.write(h.getHeader());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void disconnectServer() throws IOException {
        Header header = new Header();
        header.code = 'Q';
        header.length = 0;
        header.hash = "hash";

        outputStream.write(header.getHeader());

        if(socket != null){
            socket.close(); //소켓을 닫고
            socket = null; //null로 만든다
        }
    }

    public void checkThread(){
        Log.d(TAG, "checkThread, socket : " + socket + ", inputstream : " + inputStream + ", outputstream : " + outputStream + ", mustConnect : " + mustConnect + ", user : " + user +", mustDisconnect" + mustDisconnect);
    }

    public void sendMessage(String data){
        try {
//            Header header = new Header();
//            header.code = 'M';
//            header.length = data.getBytes().length;
//            header.hash = "hash";
//
//            Log.d(TAG, Header.HEADER_LENGTH + data.getBytes().length + "");
//            Log.d(TAG, data.getBytes().length + "");
//
////            ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH + data.getBytes().length);
//            ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH);
//            byteBuffer.put(header.getHeader());
////            byteBuffer.put(data.getBytes());
//
//            Log.d(TAG, "byteBuffer.array().length :" + byteBuffer.array().length);
//
//            outputStream.write(byteBuffer.array());
//
//
//            byte[] headerByte = new byte[Header.HEADER_LENGTH];
//            inputStream.read(headerByte);
//            Header resHeader = new Header(headerByte);
//
//            byte[] dataByte = new byte[resHeader.length];
//            System.out.println("available num : " + inputStream.available());
//            inputStream.read(dataByte);
//
//            Log.d(TAG, "response : " + new String(dataByte));
//
////                outputStream.write(dataByte);

            checkThread();
            Header header = new Header();
            String d = new Gson().toJson(user);
            header.code = 'C';
            header.length = d.getBytes().length;
            header.hash = "hash";

            ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH + d.getBytes().length);
            byteBuffer.put(header.getHeader());
            byteBuffer.put(d.getBytes());
            outputStream.write(byteBuffer.array());




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();



        while (true){
            //3가지 체크 : 어떤것?
            if(socket != null && socket.isConnected()){ //소켓이 연결된 상태일때
                try {
                    if(mustDisconnect){ //연결을 끊어야 할때
                        disconnectServer();
                        mustDisconnect = false;
                        continue;
                    }
                    InputStream inputStream = socket.getInputStream();
                    if(inputStream.available() > 0){ //읽을 데이터가 있을때


                        byte[] headerByte = new byte[Header.HEADER_LENGTH];
                        inputStream.read(headerByte);
                        Header header = new Header(headerByte);

                        if(header.code == 'R'){ //서버 응답 데이터일 경우
                            System.out.println("Received Server Response");

                            TestApplication.getInstance().connectionThread.checkThread();
                            Intent intent = new Intent(context, DialogActivity.class);
                            intent.putExtra("name", user.name);
                            context.startActivity(intent);

//                            Header h = new Header();
//                            String d = new Gson().toJson(user);
//                            h.code = 'M';
//                            h.length = d.getBytes().length;
//                            h.hash = "hash";
//
//                            ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH + d.getBytes().length);
//                            byteBuffer.put(h.getHeader());
//                            byteBuffer.put(d.getBytes());
//                            outputStream.write(byteBuffer.array());
                        }

                        byte[] dataByte = new byte[header.length];
                        System.out.println("available num : " + inputStream.available());
                        inputStream.read(dataByte);

                        ByteBuffer byteBuffer = ByteBuffer.allocate(dataByte.length);
                        byteBuffer.put(dataByte);
                        byteBuffer.position(0);
                        System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));

                        String responseString = new String(byteBuffer.array(), StandardCharsets.UTF_8);
                        Log.d(TAG, "responseString : " + responseString);
                        Response response = new Gson().fromJson(responseString, Response.class);

                    }else{

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{ //소켓이 연결된 상태가 아닐때
                if(mustConnect){ //연결이 필요한 상태일 경우
                    connectServer(user); //서버에 연결을 하고
                    mustConnect = false; //연결이 필요하지 않다는 상태로 변경
                }
            }
        }
    }
}
