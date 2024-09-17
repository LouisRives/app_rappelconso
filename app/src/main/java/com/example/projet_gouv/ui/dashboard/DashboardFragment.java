package com.example.projet_gouv.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_gouv.R;
import com.example.projet_gouv.databinding.FragmentDashboardBinding;
import com.example.projet_gouv.data.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Page FAVORIS
/* Cette page correspond à l'affichage de nos rappels favoris. Il utilise la base de donnée "rappel" de type RoomDatabase.
On peut donc y voir une liste de nos rappel favoris, en cliquant sur l'un d'entre eux, on est redirigé vers la page SecondFragment qui affiche plus en détails nos rappels (et permet de les ajouter/retirer en favoris)
On peut également trier les rappels par date d'ajout dans la base de donnée, ordre alphabétique et date du rappel.
 */
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private RecyclerView recyclerView;
    private DashboardRecyclerAdapter adapter;
    private RappelRepository rappelRepository;

    private static final int MENU_SORT_ALPHA = 1;
    private static final int MENU_SORT_DATE = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rappelRepository = new RappelRepository(requireActivity().getApplication());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DashboardRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        viewModel.init(new RappelRepository(requireActivity().getApplication()));

        viewModel.getAllRappels().observe(getViewLifecycleOwner(), new Observer<List<Rappel>>() {
            @Override
            public void onChanged(List<Rappel> rappels) {
                adapter.setRappelList(rappels);
            }
        });

        // OnItemClickListener pour le DashboardRecyclerAdapter
        adapter.setOnItemClickListener(new DashboardRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rappel rappel) {
                // Création d'un SecondFragment
                SecondFragment secondFragment = new SecondFragment();

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
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_secondFragment, bundle);

            }
            @Override
            public void onFavoriteButtonClick(Rappel rappel) {
                rappelRepository.deleteRappelSecond(rappel);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int alphabet_sort = R.id.action_sort_alpha;
        int date_sort = R.id.action_sort_date;
        int id_sort = R.id.action_sort_id;
        Log.d(TAG, "onOptionsItemSelected: test"+R.id.action_sort_alpha);

        if(id==alphabet_sort) {
            // Trier les rappels par ordre alphabétique
            Log.d(TAG, "onOptionsItemSelected: alphab");

            if (adapter != null) {
                List<Rappel> rappelList = adapter.getRappelList();
                if (rappelList != null) {
                    Collections.sort(rappelList, new Comparator<Rappel>() {
                        @Override
                        public int compare(Rappel r1, Rappel r2) {
                            return r1.getNomProduit().compareToIgnoreCase(r2.getNomProduit());
                        }
                    });
                    // Mettre à jour l'adaptateur après le tri
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
        else if (id == date_sort) {
            // Trier les rappels par date
            if (adapter != null) {
                List<Rappel> rappelList = adapter.getRappelList();
                if (rappelList != null) {
                    Collections.sort(rappelList, new Comparator<Rappel>() {
                        @Override
                        public int compare(Rappel r1, Rappel r2) {
                            // Comparer les dates en utilisant la méthode compareTo de String
                            return r1.getDateRappel().compareTo(r2.getDateRappel());
                        }
                    });
                    // Mettre à jour l'adaptateur après le tri
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
        else if (id == id_sort) {
            // Trier les rappels par ID dans l'ordre inverse
            if (adapter != null) {
                List<Rappel> rappelList = adapter.getRappelList();
                if (rappelList != null) {
                    Collections.sort(rappelList, new Comparator<Rappel>() {
                        @Override
                        public int compare(Rappel r1, Rappel r2) {
                            // Comparer les ID dans l'ordre inverse
                            return Long.compare(r2.getId(), r1.getId());
                        }
                    });
                    // Mettre à jour l'adaptateur après le tri
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
        else{
            Log.d(TAG, "onOptionsItemSelected: autre");
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
