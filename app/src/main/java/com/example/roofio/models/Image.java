package com.example.roofio.models;

import android.net.Uri;

public class Image {
    Uri imageUri;
    boolean isLocal;

    public Image(Uri imageUri, boolean isLocal) {
        this.imageUri = imageUri;
        this.isLocal = isLocal;
    }

    public Image() {
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
