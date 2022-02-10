package com.example.roofio.models;

public class Category {
    String Key;
    String Naziv;
    String ImageUrl;

    public Category() {
    }

    public Category(String key, String naziv, String imageUrl) {
        Key = key;
        Naziv = naziv;
        ImageUrl = imageUrl;
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

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Category setKey(String key){
        Key = key;
        return  this;
    }

    @Override
    public String toString() {
        return Naziv;
    }
}
