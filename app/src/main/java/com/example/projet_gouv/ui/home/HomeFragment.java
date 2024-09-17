package com.example.projet_gouv.ui.home;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projet_gouv.R;
import com.example.projet_gouv.data.Rappel;
import com.example.projet_gouv.data.RappelRepository;
import com.example.projet_gouv.databinding.FragmentHomeBinding;

// Page : RECHERCHE
/* Cette page correspond à l'affichage d'une liste de rappels récupérés grâce à l'API sur le site data.economie.gouv.fr
On peut donc y voir une liste de rappel, en cliquant sur l'un d'entre eux, on est redirigé vers la page SecondFragment qui affiche plus en détails nos rappels (et permet de les ajouter/retirer en favoris)
En cliquant le coeur d'un rappel (dans le card_layout), il est automatiquement rajouté dans la base de donnée de nos rappels favoris. En cliquant une seconde fois il est retiré des favoris.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter adapter;
    private FragmentHomeBinding binding;
    private RappelRepository rappelRepository;
    private AsyncTask<Void, Void, Void> toggleFavoriteTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rappelRepository = new RappelRepository(requireActivity().getApplication());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllRappels().observe(getViewLifecycleOwner(), new Observer<List<Rappel>>() {
            @Override
            public void onChanged(List<Rappel> rappels) {
                if (rappels != null) {
                    // Mettez à jour la liste des favoris en utilisant les données de votre DAO
                    List<Boolean> favorisStateList = new ArrayList<>();
                    for (Rappel rappel : rappels) {
                        rappelRepository.isRappelExists(rappel).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean isFavorite) {
                                favorisStateList.add(isFavorite);
                                // Vérifie si le nombre d'états de favoris correspond au nombre total de rappels
                                if (favorisStateList.size() == rappels.size()) {
                                    adapter.updateFavorites(favorisStateList);
                                }
                            }
                        });
                    }
                }
            }
        });
    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.init();
        viewModel.getAllRappels().observe(getViewLifecycleOwner(), new Observer<List<Rappel>>() {
            @Override
            public void onChanged(List<Rappel> rappels) {
                adapter.setRappelList(rappels);
            }
        });

        adapter = new HomeRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        // OnItemClickListener pour le RecyclerAdapter
        adapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rappel rappel) {
                // Création d'un SecondFragment
                com.example.projet_gouv.ui.home.SecondFragment secondFragment = new SecondFragment();

                // Création d'un bundle pour transférer les données à SecondFragment
                Bundle bundle = new Bundle();
                bundle.putString("image_url", rappel.getLienImage());
                bundle.putString("title", rappel.getNomProduit());
                bundle.putString("dateRappel", rappel.getDateRappel());
                bundle.putString("categorie", rappel.getCategorie());
                bundle.putString("sousCategorie", rappel.getSousCategorie());
                bundle.putString("marque", rappel.getMarque());
                bundle.putString("risque", rappel.getRisque());
                bundle.putString("motif", rappel.getMotif());
                bundle.putString("compensation", rappel.getCompensation());
                secondFragment.setArguments(bundle);

                // Navigation vers SecondFragment avec Nav
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_home_to_secondFragment, bundle);

            }
            @Override
            public void onFavoriteButtonClick(Rappel rappel) {
                toggleFavorite(rappel);
            }
        });

        return root;
    }

    private void toggleFavorite(Rappel rappel) {
        // Annule la dernière task si en cours, évite de faire tout bugger avec les tâches synchrones/asynchrones
        if (toggleFavoriteTask != null && toggleFavoriteTask.getStatus() == AsyncTask.Status.RUNNING) {
            toggleFavoriteTask.cancel(true);
        }

        toggleFavoriteTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!isCancelled()) {
                    if (rappelRepository.isRappelExistsSync(rappel)) {
                        // Le rappel existe, vous pouvez le supprimer
                        Log.d(TAG, "onChanged: Rappel retiré des favoris");
                        rappelRepository.deleteRappelSecond(rappel);
                    } else {
                        // Le rappel n'existe pas, vous pouvez l'insérer
                        Log.d(TAG, "onChanged: Rappel ajouté aux favoris");
                        rappelRepository.insertRappel(rappel);
                    }
                }
                return null;
            }
        };

        toggleFavoriteTask.execute();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Rappel> rappels = viewModel.getAllRappels().getValue();
        if (rappels == null) {
            return false;
        }

        List<Rappel> filteredList = new ArrayList<>();
        for (Rappel rappel : rappels) {
            if (rappel.getNomProduit().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(rappel);
            }
        }
        adapter.setRappelList(filteredList);
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}