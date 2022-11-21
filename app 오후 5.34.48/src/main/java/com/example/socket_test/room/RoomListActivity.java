package com.example.socket_test.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.socket_test.R;
import com.example.socket_test.databinding.ActivityRoomListBinding;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRoomListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_room_list);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        List<String> list = new ArrayList<>();
        list.add(name);
        binding.recyclerview.setAdapter(new RoomListAdapter(list));
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}