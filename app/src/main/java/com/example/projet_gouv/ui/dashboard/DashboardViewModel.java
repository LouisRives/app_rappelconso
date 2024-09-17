package com.example.projet_gouv.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.projet_gouv.data.*;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private RappelRepository repository;
    private LiveData<List<Rappel>> allRappels;

    public DashboardViewModel() {
    }

    public DashboardViewModel(RappelRepository repository) {
        this.repository = repository;
        allRappels = repository.getAllRappels();
    }

    public LiveData<List<Rappel>> getAllRappels() {
        return allRappels;
    }

    public void init(RappelRepository repository) {
        this.repository = repository;
        allRappels = repository.getAllRappels();
    }
}
