package com.example.proyectointegradorlaolla;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemLista {
    private String img;
    private String texto;

    private String id;


    public ItemLista(String id, String img, String textoup) {
        this.id = id;
        this.img = img;
        this.texto = textoup;
    }

    public String getTextoup() {
        return texto;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTextoup(String textoup) {
        this.texto = textoup;
    }





}
