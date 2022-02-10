package com.example.roofio.models;

import java.util.List;

public class PropertyDetails {
    String Key;
    String Naziv;
    String Kategorija;
    String Status;
    String Oglasivac;
    String OpisOglasa;
    List<String> Slike;
    Double Cijena;
    String Lokacija;
    Double BrojSoba;
    Integer StambenaPovrsina;
    Integer BrojEtaza;
    Integer GodinaIzgradnje;
    Integer GodinaZadnjeRenovacije;
    String EnergetskiRazred;
    String BakonLodaTerasa;
    String Namjestenost;
    String Email;


    public PropertyDetails() {
    }

    public PropertyDetails(String key, String naziv, String kategorija, String status, String oglasivac, String opisOglasa, List<String> slike, Double cijena, String lokacija, Double brojSoba, Integer stambenaPovrsina, Integer brojEtaza, Integer godinaIzgradnje, Integer godinaZadnjeRenovacije, String energetskiRazred, String bakonLodaTerasa, String namjestenost, String email) {
        Key = key;
        Naziv = naziv;
        Kategorija = kategorija;
        Status = status;
        Oglasivac = oglasivac;
        OpisOglasa = opisOglasa;
        Slike = slike;
        Cijena = cijena;
        Lokacija = lokacija;
        BrojSoba = brojSoba;
        StambenaPovrsina = stambenaPovrsina;
        BrojEtaza = brojEtaza;
        GodinaIzgradnje = godinaIzgradnje;
        GodinaZadnjeRenovacije = godinaZadnjeRenovacije;
        EnergetskiRazred = energetskiRazred;
        BakonLodaTerasa = bakonLodaTerasa;
        Namjestenost = namjestenost;
        Email = email;
    }

    public String getKey() {
        return Key;
    }

    public PropertyDetails setKey(String key) {
        Key = key;
        return this;
    }

    public String getNaziv() {
        return Naziv;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public String getKategorija() {
        return Kategorija;
    }

    public void setKategorija(String kategorija) {
        Kategorija = kategorija;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOglasivac() {
        return Oglasivac;
    }

    public void setOglasivac(String oglasivac) {
        Oglasivac = oglasivac;
    }

    public String getOpisOglasa() {
        return OpisOglasa;
    }

    public void setOpisOglasa(String opisOglasa) {
        OpisOglasa = opisOglasa;
    }

    public List<String> getSlike() {
        return Slike;
    }

    public void setSlike(List<String> slike) {
        Slike = slike;
    }

    public Double getCijena() {
        return Cijena;
    }

    public void setCijena(Double cijena) {
        Cijena = cijena;
    }

    public String getLokacija() {
        return Lokacija;
    }

    public void setLokacija(String lokacija) {
        Lokacija = lokacija;
    }

    public Double getBrojSoba() {
        return BrojSoba;
    }

    public void setBrojSoba(Double brojSoba) {
        BrojSoba = brojSoba;
    }

    public Integer getStambenaPovrsina() {
        return StambenaPovrsina;
    }

    public void setStambenaPovrsina(Integer stambenaPovrsina) {
        StambenaPovrsina = stambenaPovrsina;
    }

    public Integer getBrojEtaza() {
        return BrojEtaza;
    }

    public void setBrojEtaza(Integer brojEtaza) {
        BrojEtaza = brojEtaza;
    }

    public Integer getGodinaIzgradnje() {
        return GodinaIzgradnje;
    }

    public void setGodinaIzgradnje(Integer godinaIzgradnje) {
        GodinaIzgradnje = godinaIzgradnje;
    }

    public Integer getGodinaZadnjeRenovacije() {
        return GodinaZadnjeRenovacije;
    }

    public void setGodinaZadnjeRenovacije(Integer godinaZadnjeRenovacije) {
        GodinaZadnjeRenovacije = godinaZadnjeRenovacije;
    }

    public String getEnergetskiRazred() {
        return EnergetskiRazred;
    }

    public void setEnergetskiRazred(String energetskiRazred) {
        EnergetskiRazred = energetskiRazred;
    }

    public String getBakonLodaTerasa() {
        return BakonLodaTerasa;
    }

    public void setBakonLodaTerasa(String bakonLodaTerasa) {
        BakonLodaTerasa = bakonLodaTerasa;
    }

    public String getNamjestenost() {
        return Namjestenost;
    }

    public void setNamjestenost(String namjestenost) {
        Namjestenost = namjestenost;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
