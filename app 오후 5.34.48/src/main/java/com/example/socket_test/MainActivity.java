package com.example.socket_test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socket_test.objects.User;
import com.example.socket_test.room.RoomListActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    ByteBuffer byteBuffer;
    EditText editTextInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_setting);
        editTextInput = findViewById(R.id.editTextInput);

    }

    public void btnConnect(View view){
        String name = editTextInput.getText().toString();

        Log.d(TAG, "btnConnect Clicked");
        Log.d(TAG, name);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", name);
        editor.commit();

        TestApplication testApplication = (TestApplication) getApplication();

        User user = new User();
        user.name = name;
        testApplication.executeThread(user, this);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("랜덤 대화방에 연결 중입니다")
                .setMessage("잠시만 기다려 주세요...")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //연결 종료
                            dialog.dismiss();
                            TestApplication.getInstance().connectionThread.mustDisconnect = true;
                        }
        }).create();

        alertDialog.show();

//                String data = new Gson().toJson(user);
//                Log.d(TAG, "data :" + data);
//                Log.d(TAG, "data getBytes() :" + data.getBytes());
    }

}