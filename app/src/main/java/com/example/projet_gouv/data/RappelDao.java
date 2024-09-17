package com.example.projet_gouv.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RappelDao {
    @Update
    void updateRappel(Rappel rappel);

    @Query("SELECT MAX(id) FROM rappels")
    long getMaxRappelId();

    @Insert
    void insertRappel(Rappel rappel);

    @Query("SELECT * FROM rappels WHERE id = :id")
    Rappel getRappel(long id);

    @Delete
    void deleteRappel(Rappel rappel);

    @Query("DELETE FROM rappels WHERE nomProduit = :nomProduit AND dateRappel = :dateRappel AND lienImage = :lienImage")
    void deleteRappelSecond(String nomProduit, String dateRappel, String lienImage);


    @Query("DELETE FROM rappels")
    void deleteAllRappels();

    @Query("SELECT * FROM rappels")
    LiveData<List<Rappel>> getAllRappels();

    @Query("SELECT * FROM rappels WHERE id = :rappelId")
    LiveData<Rappel> getRappelById(long rappelId);

    @Query("SELECT COUNT(*) > 0 FROM rappels WHERE nomProduit = :nomProduit AND dateRappel = :dateRappel AND lienImage = :lienImage")
    LiveData<Boolean> isRappelExists(String nomProduit, String dateRappel, String lienImage);

    @Query("SELECT COUNT(*) > 0 FROM rappels WHERE nomProduit = :nomProduit AND dateRappel = :dateRappel AND lienImage = :lienImage")
    Boolean isRappelExistsSync(String nomProduit, String dateRappel, String lienImage);

}
