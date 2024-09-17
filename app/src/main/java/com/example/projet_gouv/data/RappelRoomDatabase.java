package com.example.projet_gouv.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Rappel.class}, version = 1, exportSchema = false)
public abstract class RappelRoomDatabase extends RoomDatabase {

    public abstract RappelDao rappelDao();

    private static volatile RappelRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                RappelDao dao = INSTANCE.rappelDao();
                dao.deleteAllRappels();

                for (Rappel newRappel : Rappel.rappels) {
                    dao.insertRappel(newRappel);
                }
            });
        }
    };

    static RappelRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RappelRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RappelRoomDatabase.class, "rappel_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
