package com.example.sockettestclient;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sockettestclient.databinding.ActivityDialogBinding;
import com.example.sockettestclient.objects.Message;
import com.google.gson.Gson;

public class DialogActivity extends AppCompatActivity {
    ActivityDialogBinding binding;
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog);

        //내 이름
        name = getIntent().getStringExtra("name");
    }

    public void btnSendMessage(View view){
        System.out.println("etMessage : " + String.valueOf(binding.etMessage.getText()));

        String data = new Gson().toJson(new Message(String.valueOf(binding.etMessage.getText())));
        TestApplication.getInstance().connectionThread.sendMessage(data);
//        TestApplication.getInstance().connectionThread.checkThread();
    }
}