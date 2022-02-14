package com.example.roofio.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    String VrijemeKreiranjaOglasa;

    public PropertyInfo() {
    }

    public PropertyInfo(String key, String naziv, String kategorija, String status, Double cijena, String lokacija, Double brojSoba, String slika, Integer statusId, String vrijemeKreiranjaOglasa) {
        Key = key;
        Naziv = naziv;
        Kategorija = kategorija;
        Status = status;
        Cijena = cijena;
        Lokacija = lokacija;
        BrojSoba = brojSoba;
        Slika = slika;
        StatusId = statusId;
        VrijemeKreiranjaOglasa = vrijemeKreiranjaOglasa;
    }

    public String getVrijemeKreiranjaOglasa() {
        return VrijemeKreiranjaOglasa;
    }

    public void setVrijemeKreiranjaOglasa(String vrijemeKreiranjaOglasa) {
        VrijemeKreiranjaOglasa = vrijemeKreiranjaOglasa;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyInfo that = (PropertyInfo) o;
        return Key.equals(that.Key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Key);
    }
}
