package com.example.myloyaltycards.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//requisiti di implementazione di RoomDatabase
@Database(entities = {Card.class}, version = 1) //annotazione @Database e lista entità associate al DB
public abstract class CardsDatabase extends RoomDatabase { //classe astratta
    private static CardsDatabase instance;
    private static final String DATABASE_NAME = "cards_database";

    public static CardsDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, CardsDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    //metodo astratto senza argomenti che ritorna la classe annotata con @Dao
    public abstract CardDao cardDao();
}
