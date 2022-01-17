package com.example.myloyaltycards.model;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {
    @Query("SELECT * FROM cards")
    LiveData<List<Card>> getAll();

    @Query("SELECT * FROM cards WHERE uid = :id")
    LiveData<Card> getCard(int id);

    @Insert
    void insert(Card card);

    @Delete
    void delete(Card card);
}
