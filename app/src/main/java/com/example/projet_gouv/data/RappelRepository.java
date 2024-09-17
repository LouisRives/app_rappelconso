package com.example.projet_gouv.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RappelRepository {

    private RappelDao rappelDao;
    private LiveData<List<Rappel>> allRappels;

    public RappelRepository(Application application) {
        RappelRoomDatabase database = RappelRoomDatabase.getDatabase(application);
        rappelDao = database.rappelDao();
        allRappels = rappelDao.getAllRappels();
    }

    public LiveData<List<Rappel>> getAllRappels() {
        return allRappels;
    }

    public void updateRappel(Rappel rappel) {
        RappelRoomDatabase.databaseWriteExecutor.execute(() -> rappelDao.updateRappel(rappel));
    }

    public void insertRappel(Rappel rappel) {
        RappelRoomDatabase.databaseWriteExecutor.execute(() -> {
            long maxId = rappelDao.getMaxRappelId(); // Permet au livre d'avoir l'id+1 du dernier livre présent dans la BDD
            rappel.setId(maxId + 1);
            rappelDao.insertRappel(rappel);
        });
    }


    public Rappel getRappel(long id) {
        return rappelDao.getRappel(id);
    }

    public void deleteRappel(Rappel rappel) {
        RappelRoomDatabase.databaseWriteExecutor.execute(() -> rappelDao.deleteRappel(rappel));
    }

    public void deleteRappelSecond(Rappel rappel) {
        RappelRoomDatabase.databaseWriteExecutor.execute(() -> rappelDao.deleteRappelSecond(rappel.getNomProduit(), rappel.getDateRappel(), rappel.getLienImage()));
    }

    public LiveData<Rappel> getRappelById(long rappelId) {
        return rappelDao.getRappelById(rappelId);
    }

    public LiveData<Boolean> isRappelExists(Rappel rappel) {
        return rappelDao.isRappelExists(rappel.getNomProduit(), rappel.getDateRappel(), rappel.getLienImage());
    }

    public Boolean isRappelExistsSync(Rappel rappel) {
        return rappelDao.isRappelExistsSync(rappel.getNomProduit(), rappel.getDateRappel(), rappel.getLienImage());
    }
}
