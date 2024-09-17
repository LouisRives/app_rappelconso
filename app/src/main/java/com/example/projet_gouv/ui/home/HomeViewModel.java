package com.example.projet_gouv.ui.home;
import static android.content.ContentValues.TAG;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;
import com.example.projet_gouv.MyApplication;
import com.example.projet_gouv.api.api;
import com.example.projet_gouv.api.apiImplement;
import com.example.projet_gouv.data.Rappel;
import com.example.projet_gouv.data.RappelResponse;
import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Rappel>> rappelList;
    private final MutableLiveData<String> mText;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Rappel>> getAllRappels() {
        if (rappelList == null) {
            rappelList = new MutableLiveData<>();
        }
        return rappelList;
    }

    public void init() {
        api apiService = new apiImplement();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());

        int maxResults = Integer.parseInt(sharedPreferences.getString("max_results", "6"));
        String compensation = "modalites_de_compensation=";
        String tmpCompensation = sharedPreferences.getString("compensation", "modalites_de_compensation");
        if (tmpCompensation.equals("all"))
            compensation += "modalites_de_compensation";
        else
            compensation += "'" + sharedPreferences.getString("compensation", "modalites_de_compensation")+"'";

        String categorie = "categorie_de_produit=";
        String tmpCategorie = sharedPreferences.getString("categorie", "Alimentation");
        if (tmpCategorie.equals("all"))
            categorie += "categorie_de_produit";
        else
            categorie += "'" + sharedPreferences.getString("categorie", "Alimentation")+"'";

        String risque = "risques_encourus_par_le_consommateur=";
        String tmpRisque = sharedPreferences.getString("risque", "Dommages internes Intoxication");
        if (tmpRisque.equals("all"))
            risque += "risques_encourus_par_le_consommateur";
        else
            risque += "'" + sharedPreferences.getString("risque", "Dommages internes Intoxication")+"'";

        String distributeur = "distributeurs=";
        String tmpDistributeur = sharedPreferences.getString("distributeur", "distributeurs");
        if (tmpDistributeur.equals("all"))
            distributeur += "distributeurs";
        else
            distributeur += "'" + sharedPreferences.getString("distributeur", "distributeurs")+"'";

        String where = compensation+" AND "+categorie+" AND "+risque+" AND "+distributeur;
        Log.d(TAG, "WHERE : " + where);

        Call<RappelResponse> call = apiService.getRappels(maxResults,where);
        callAPI(call);
    }

    public void callAPI(Call call) {
        call.enqueue(new Callback<RappelResponse>() {
            @Override
            public void onResponse(Call<RappelResponse> call, Response<RappelResponse> response) {
                if (response.isSuccessful()) {
                    RappelResponse rappelConsoResponse = response.body();
                    List<RappelResponse.Record> records = rappelConsoResponse.getRecords();
                    List<Rappel> rappels = new ArrayList<>();
                    for (RappelResponse.Record record : records) {
                        RappelResponse.Record.Fields fields = record.getFields();
                        Rappel rappel = fields.getRappel();
                        rappels.add(rappel);
                        String nomMarque = rappel.getNomProduit();
                        Log.d(TAG, "Nom du produit : " + nomMarque);
                        String dateRappel = rappel.getDateRappel();
                        Log.d(TAG, "Date du rappel : " + dateRappel);
                        String categorie = rappel.getCategorie();
                        Log.d(TAG, "categorie produit : " + categorie);
                        String compensation = rappel.getCompensation();
                        Log.d(TAG, "compensation : " + compensation);
                        String risque = rappel.getRisque();
                        Log.d(TAG, "risque : " + risque);
                    }
                    if (rappels.isEmpty()) {
                        // Afficher un toast si aucun rappel n'a été trouvé
                        Toast.makeText(MyApplication.getAppContext(), "Aucun rappel trouvé", Toast.LENGTH_SHORT).show();
                        rappelList.setValue(null);
                    } else {
                        rappelList.setValue(rappels);
                    }                }
            }
            @Override
            public void onFailure(Call<RappelResponse> call, Throwable t) {
                Log.e(TAG, "Erreur lors de la récupération des rappels : " + t.getMessage());
            }
        });
    }
}

