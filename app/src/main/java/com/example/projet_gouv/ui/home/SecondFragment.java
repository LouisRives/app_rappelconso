package com.example.projet_gouv.ui.home;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.projet_gouv.data.Rappel;
import com.example.projet_gouv.data.RappelRepository;
import com.example.projet_gouv.databinding.FragmentSecondBinding;

/* Cette classe permet d'afficher un rappel plus en détails lorsque l'on clique dessus.
On peut sur cette page grâce au "buttonFavorite" ajouter le rappel en favoris (ou le retirer)
elle est accessible depuis la page de recherche (home) et la page des favoris(dashboard) */
public class SecondFragment extends Fragment {

    private Rappel rappel;
    private FragmentSecondBinding binding;
    private RappelRepository rappelRepository;
    private SecondViewModel secondViewModel;
    private boolean isRappelInDatabase = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rappelRepository = new RappelRepository(requireActivity().getApplication());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        secondViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SecondViewModel.class);
        secondViewModel.init(requireActivity().getApplication());
        rappelRepository = new RappelRepository((Application) requireContext().getApplicationContext());

        //Lecture du bundle transmis (permet de récupèrer les informations sur notre rappel
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUrl = bundle.getString("image_url");
            String title = bundle.getString("title");
            String dateRappel = bundle.getString("dateRappel");
            String categorie = bundle.getString("categorie");
            String sousCategorie = bundle.getString("sousCategorie");
            String marque = bundle.getString("marque");
            String risque = bundle.getString("risque");
            String motif = bundle.getString("motif");
            String compensation = bundle.getString("compensation");

            rappel = new Rappel(title, dateRappel, imageUrl, categorie, sousCategorie, marque, risque, motif, compensation);

            // Afficher les données du rappel dans les TextView correspondants
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.itemImage);

            binding.itemTitle.setText(title);
            binding.itemDateRappel.setText("Date du rappel : "+dateRappel);
            binding.itemCategorie.setText("Categorie : "+categorie);
            binding.itemSousCategorie.setText("Sous-catégorie : "+sousCategorie);
            binding.itemMarque.setText("Marque : "+marque);
            binding.itemRisque.setText("Risque : "+risque);
            binding.itemMotif.setText("Motif : "+motif);
            binding.itemCompensation.setText("Compensation : "+compensation);
        }

        // Bouton retour
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SecondFragment", "Bouton retour cliqué");
                requireActivity().onBackPressed();
            }
        });

        //Vérification si le rappel existe déjà dans notre base de donnée
        rappelRepository.isRappelExists(rappel).observe(getViewLifecycleOwner(), exists -> {
            isRappelInDatabase = exists;
            if (exists) {
                // Si le rappel est déjà présent, changer le texte du bouton
                binding.buttonFavorite.setText("Retirer des Favoris");
            } else {
                // Sinon, garder le texte par défaut
                binding.buttonFavorite.setText("Ajouter aux Favoris");
            }
        });

        // Définir le click listener pour le bouton ajouter/retirer des favoris
        binding.buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRappelInDatabase) {
                    // Si le rappel est déjà présent, le retirer des favoris
                    removeRappelFromFavorites();
                } else {
                    // Sinon, l'ajouter aux favoris
                    addRappelToFavorites();
                }
            }
        });

        return root;
    }

    //Permet l'utilisation de la flèche retour dans SecondFragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Activer la flèche de retour
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Méthode pour retirer le rappel des favoris
    private void removeRappelFromFavorites() {
        Log.d("SecondFragment", "Rappel déjà présent dans les favoris : " + rappel.getNomProduit());
        rappelRepository.deleteRappelSecond(rappel);
    }

    // Méthode pour ajouter le rappel aux favoris
    private void addRappelToFavorites() {
        Log.d("SecondFragment", "Rappel ajouté aux favoris : " + rappel.getNomProduit());
        rappelRepository.insertRappel(rappel);
    }

}
