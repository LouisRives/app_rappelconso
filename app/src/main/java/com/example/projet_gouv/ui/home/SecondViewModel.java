package com.example.projet_gouv.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet_gouv.data.Rappel;
import com.example.projet_gouv.data.RappelRepository;

public class SecondViewModel extends ViewModel {

    private RappelRepository rappelRepository;
    private MutableLiveData<Rappel> selectedRappel;

    public SecondViewModel(RappelRepository rappelRepository) {
        this.rappelRepository = rappelRepository;
        this.selectedRappel = new MutableLiveData<>();
    }

    public SecondViewModel() {
        selectedRappel = new MutableLiveData<>();
        // Initialize your repository here if needed
    }

    public void init(Application application) {
        rappelRepository = new RappelRepository(application);
    }

    public LiveData<Rappel> getSelectedRappel() {
        return selectedRappel;
    }

    public void setSelectedRappel(long rappelId) {
        Log.d("SecondViewModel", "setSelectedRappel: rappelId = " + rappelId);
        selectedRappel.setValue(rappelRepository.getRappel(rappelId));
    }

    public void insertSelectedRappel(Rappel rappel) {
        if (selectedRappel.getValue() == null) {
            selectedRappel.setValue(new Rappel("Default Title", "Default Date", "Default Image", "Default Categorie", "Default Sous-Categorie", "Default Marque", "Default Risque", "Default Motif", "Default Compensation"));
        }

        // Mise à jour les champs du rappel avec les nouvelles valeurs
        selectedRappel.getValue().setId(rappel.getId());
        selectedRappel.getValue().setNomProduit(rappel.getNomProduit());
        selectedRappel.getValue().setDateRappel(rappel.getDateRappel());
        selectedRappel.getValue().setLienImage(rappel.getLienImage());
        selectedRappel.getValue().setCategorie(rappel.getCategorie());
        selectedRappel.getValue().setSousCategorie(rappel.getSousCategorie());
        selectedRappel.getValue().setMarque(rappel.getMarque());
        selectedRappel.getValue().setRisque(rappel.getRisque());
        selectedRappel.getValue().setMotif(rappel.getMotif());
        selectedRappel.getValue().setCompensation(rappel.getCompensation());

        rappelRepository.insertRappel(selectedRappel.getValue());
    }

    public LiveData<Rappel> getRappelById(long rappelId) {
        return rappelRepository.getRappelById(rappelId);
    }
}
