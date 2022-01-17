package com.example.myloyaltycards.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class CardsManager {
    private List<Card> cardList;
    private static CardsManager instance = null;
    private CardDao cardDao;

    private CardsManager(Context context) {
        this.cardList = new ArrayList<>();
        this.cardDao = CardsDatabase.getInstance(context).cardDao();
    }

    public static CardsManager getInstance(Context context) {
        if(instance == null){
            instance = new CardsManager(context);
        }
        return instance;
    }

    public void addCard(Card card) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cardDao.insert(card);
            }
        }).start();
    }

    public void removeCard(Card card) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cardDao.delete(card);
            }
        }).start();
    }

    public LiveData<List<Card>> getCardsLiveData() {
        return cardDao.getAll();
    }

    public LiveData<Card> getCard(int id){
        return cardDao.getCard(id);
    }

}
