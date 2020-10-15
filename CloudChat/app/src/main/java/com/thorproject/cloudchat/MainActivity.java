package com.thorproject.cloudchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;



import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int MAX_MESSAGE_LENGTH = 100;


    EditText mEditTextMessage;
    Button  mSendButton;
    RecyclerView mMessagesRecycler;
    Button dialogButtonOk, dialogButtonCancel;
    SharedPreferences sPref;
    String nickname;
    int personalId;
    final String NICKNAME = "nickname";
    final String PERSONALID = "personalId";


    //---
    private MainViewModel viewModel;
    //---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sPref = getPreferences(MODE_PRIVATE);
        nickname = sPref.getString(NICKNAME, "");
        personalId = sPref.getInt(PERSONALID, 0);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);;

        if(personalId == 0){
            personalId = new Random().nextInt();
        }
        if (nickname == "") {
            showDialog();
        }

        mEditTextMessage = findViewById(R.id.message_input);
        mSendButton = findViewById(R.id.send_msg_btn);
        mMessagesRecycler = findViewById(R.id.msg_recycler);

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(getApplicationContext(), viewModel.getData().getValue(), personalId);
        mMessagesRecycler.setAdapter(dataAdapter);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = mEditTextMessage.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(getApplicationContext(), "Введите сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length() > MAX_MESSAGE_LENGTH){
                    Toast.makeText(getApplicationContext(), "Слишком длинное сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nickname != "") {
                    viewModel.addMessage(msg, nickname, personalId);
                    mEditTextMessage.setText("");
                }else{
                    showDialog();
                }
            }
        });

        final Observer<ArrayList<MyMessage>> nameObserver = new Observer<ArrayList<MyMessage>>() {
            @Override
            public void onChanged(ArrayList<MyMessage> myMessages) {
                dataAdapter.notifyDataSetChanged();
            }
        };
        viewModel.getData().observe(this, nameObserver);
    }


    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        final EditText editText = dialog.findViewById(R.id.editTextDialog);

        dialogButtonOk = dialog.findViewById(R.id.btnOk);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() != "")
                {
                    nickname = editText.getText().toString();
                }
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(NICKNAME, nickname);
                ed.putInt(PERSONALID, personalId);
                ed.commit();
                dialog.dismiss();
            }
        });

        dialogButtonCancel = dialog.findViewById(R.id.btnCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(NICKNAME, nickname);
        ed.putInt(PERSONALID, personalId);
        ed.commit();
    }




}



