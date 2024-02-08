package com.example.myagora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MeetActivity extends AppCompatActivity {
    
    private EditText editText, edtChannel;
    private Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        
        editText = findViewById(R.id.tvUserId);
        btnJoin = findViewById(R.id.btnJoin);

        edtChannel = findViewById(R.id.tvChannelId);
        
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String uid = editText.getText().toString().trim();

//                if(TextUtils.isEmpty(uid)){
//                    Toast.makeText(MeetActivity.this, "User id can't be empty", Toast.LENGTH_SHORT).show();
//                }else{
////                    Intent intent = new Intent(MeetActivity.this,MainActivity.class);
//                    Intent intent = new Intent(MeetActivity.this,CallActivity.class);
//                    intent.putExtra("uid",uid);
//
//                    startActivity(intent);
//                }

                String id = edtChannel.getText().toString().trim();
                if(TextUtils.isEmpty(id)){
                    Toast.makeText(MeetActivity.this, "User id can't be empty", Toast.LENGTH_SHORT).show();
                }else{
//                    Intent intent = new Intent(MeetActivity.this,MainActivity.class);
                    Intent intent = new Intent(MeetActivity.this,CallActivity.class);
                    intent.putExtra("id",id);

                    startActivity(intent);
                }
            }
        });
    }
}