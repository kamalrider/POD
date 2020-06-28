package com.kamalrider.pod.core.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kamalrider.pod.core.database.converter.DataConverter;
import com.kamalrider.pod.scan.DAO.ScanCNDao;
import com.kamalrider.pod.scan.model.ScanCN;

@Database(entities = {ScanCN.class},version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ScanCNDao scanCNDao();

    public static AppDatabase getDatabase(final Context context){

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_database")
                                    .fallbackToDestructiveMigration()
                                    .addCallback(roomCallback)
                                    .build();
                }
            }
        }
        return INSTANCE;

    }

    public static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);

        }
    };

}
