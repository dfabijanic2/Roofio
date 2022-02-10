package com.example.roofio.models;

public class Status {
    String Key;
    String Naziv;

    public Status() {
    }

    public Status(String key, String naziv) {
        Key = key;
        Naziv = naziv;
    }

    public String getKey() {
        return Key;
    }


    public String getNaziv() {
        return Naziv;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public Status setKey(String key){
        Key = key;
        return  this;
    }

    @Override
    public String toString() {
        return Naziv;
    }
}
