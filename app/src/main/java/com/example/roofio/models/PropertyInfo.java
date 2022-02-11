package com.example.roofio.models;

import java.util.List;

public class PropertyInfo {
    String Key;
    String Naziv;
    String Kategorija;
    String Status;
    Integer StatusId;
    Double Cijena;
    String Lokacija;
    Double BrojSoba;
    String Slika;

    public PropertyInfo() {
    }

    public PropertyInfo(String key, String naziv, String kategorija, String status, Double cijena, String lokacija, Double brojSoba, String slika, Integer statusId) {
        Key = key;
        Naziv = naziv;
        Kategorija = kategorija;
        Status = status;
        Cijena = cijena;
        Lokacija = lokacija;
        BrojSoba = brojSoba;
        Slika = slika;
        StatusId = statusId;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }

    public String getKey() {
        return Key;
    }

    public String getNaziv() {
        return Naziv;
    }

    public String getKategorija() {
        return Kategorija;
    }

    public String getStatus() {
        return Status;
    }

    public Double getCijena() {
        return Cijena;
    }

    public String getLokacija() {
        return Lokacija;
    }

    public Double getBrojSoba() {
        return BrojSoba;
    }

    public String getSlika() {
        return Slika;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public void setKategorija(String kategorija) {
        Kategorija = kategorija;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setCijena(Double cijena) {
        Cijena = cijena;
    }

    public void setLokacija(String lokacija) {
        Lokacija = lokacija;
    }

    public void setBrojSoba(Double brojSoba) {
        BrojSoba = brojSoba;
    }

    public void setSlike(String slika) {
        Slika = slika;
    }
}
