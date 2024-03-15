package com.example.proyectointegradorlaolla;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ElAdaptador extends RecyclerView.Adapter<ElAdaptador.ElViewHolder> implements View.OnClickListener {

    private ArrayList<ItemLista> datos;
    private View.OnClickListener listener;

    public static class ElViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen;
        private TextView nombrereceta;
        public ElViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgtar);
            nombrereceta = itemView.findViewById(R.id.txttar);
        }

        public void bindItemLista(ItemLista r, Context context){
            nombrereceta.setText(r.getTextoup());
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(r.getImg() + ".jpeg");
            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .into(imagen);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Ocurrió un error al obtener la URL de descarga de la imagen
                    Log.e("Firebase", "Error al obtener la URL de descarga de la imagen: " + e.getMessage());
                }
            });

        }
    }

    public ElAdaptador(ArrayList<ItemLista> datos){
        this.datos = datos;
    }

    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_1, parent, false);
        v.setOnClickListener(this);
        ElViewHolder evh = new ElViewHolder(v);
        return evh;
    }

    public void onBindViewHolder(ElViewHolder holder, int pos){
        holder.bindItemLista(datos.get(pos), holder.itemView.getContext());
    }
    @Override
    public int getItemCount() {
        return datos.size();
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
