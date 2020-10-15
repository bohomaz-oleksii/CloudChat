package com.thorproject.cloudchat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Repository {

    DatabaseReference database;
    ArrayList<MyMessage> messages = new ArrayList<>();


    public void addMsg(String msg, String nickname, int personalId){
        Integer num = new Random().nextInt();
        database = FirebaseDatabase.getInstance().getReference().child("Message").child("Message" + num);
            DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            MyMessage m = new MyMessage(msg, nickname, personalId, date);
            database.getRef().setValue(m);


    }

    public ArrayList<MyMessage> getMsg() {
        database = FirebaseDatabase.getInstance().getReference().child("Message");


        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MyMessage p = dataSnapshot.getValue(MyMessage.class);
                messages.add(p);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return messages;
    }
}
