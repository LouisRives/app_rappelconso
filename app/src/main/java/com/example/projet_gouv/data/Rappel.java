package com.example.projet_gouv.data;

import com.google.gson.annotations.SerializedName;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="rappels")
public class Rappel {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("noms_des_modeles_ou_references")
    private String nomProduit;
    @SerializedName("date_de_publication")
    private String dateRappel;
    @SerializedName("liens_vers_les_images")
    private String lienImage;

    @SerializedName("categorie_de_produit")
    private String categorie;

    @SerializedName("sous_categorie_de_produit")
    private String sousCategorie;

    @SerializedName("nom_de_la_marque_du_produit")
    private String marque;

    @SerializedName("risques_encourus_par_le_consommateur")
    private String risque;

    @SerializedName("motif_du_rappel")
    private String motif;

    @SerializedName("modalites_de_compensation")
    private String compensation;

    @Ignore
    public Rappel(String nomProduit, String dateRappel, String lienImage, String categorie, String sousCategorie, String marque, String risque, String motif, String compensation) {
        this.nomProduit = nomProduit;
        this.dateRappel = dateRappel;
        this.lienImage = lienImage;
        this.categorie = categorie;
        this.sousCategorie = sousCategorie;
        this.marque = marque;
        this.risque = risque;
        this.motif = motif;
        this.compensation = compensation;
    }

    public Rappel(long id, String nomProduit, String dateRappel, String lienImage, String categorie, String sousCategorie, String marque, String risque, String motif, String compensation) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.dateRappel = dateRappel;
        this.lienImage = lienImage;
        this.categorie = categorie;
        this.sousCategorie = sousCategorie;
        this.marque = marque;
        this.risque = risque;
        this.motif = motif;
        this.compensation = compensation;
    }

    public long getId() {
        return id;
    }

    public String getNomProduit() {
        return nomProduit;
    }
    public String getDateRappel() {
        return dateRappel;
    }
    public String getLienImage() {
        return lienImage;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getMarque() {
        return marque;
    }

    public String getSousCategorie() {
        return sousCategorie;
    }

    public String getRisque() {
        return risque;
    }

    public String getMotif() {
        return motif;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    public void setLienImage(String lienImage) {
        this.lienImage = lienImage;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setSousCategorie(String sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setRisque(String risque) {
        this.risque = risque;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }
    public static Rappel[] rappels = {};
}

