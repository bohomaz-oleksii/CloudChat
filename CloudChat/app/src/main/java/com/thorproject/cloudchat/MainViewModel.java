package com.thorproject.cloudchat;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<ArrayList<MyMessage>> data;

    public MainViewModel(){
        repository = new Repository();
    }

    public void addMessage(String msg, String nick, int id){
        repository.addMsg(msg, nick, id);
    }


    public MutableLiveData<ArrayList<MyMessage>> getData(){
        if(data == null){
            data = new MutableLiveData<>();
        }
        data.setValue(repository.getMsg());
        return data;
    }
}
