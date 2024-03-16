package com.example.proyectointegradorlaolla;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class ElAdaptador extends RecyclerView.Adapter<ElAdaptador.ElViewHolder> implements View.OnClickListener, Filterable {

    private ArrayList<ItemLista> datos;
    private View.OnClickListener listener;
    private ArrayList<ItemLista> datosFiltrados;
    private ItemFilter mFilter = new ItemFilter();

    public void actualizarDatos(ArrayList<ItemLista> nuevosDatos) {
        this.datos.clear();
        this.datos.addAll(nuevosDatos);
        this.datosFiltrados = (ArrayList<ItemLista>) datos.clone();
        notifyDataSetChanged();
    }

    public ItemLista getItemFromPosition(int position) {
        return datosFiltrados.get(position);
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<ItemLista> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No hay restricción de búsqueda, retornar la lista original
                results.count = datos.size();
                results.values = datos;
            } else {
                // Aplicar la restricción de búsqueda
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemLista item : datos) {
                    if (item.getTextoup().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datosFiltrados = (ArrayList<ItemLista>) results.values;
            notifyDataSetChanged();
        }
    }

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
        this.datosFiltrados = (ArrayList<ItemLista>) datos.clone();
    }

    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_1, parent, false);
        v.setOnClickListener(this);
        ElViewHolder evh = new ElViewHolder(v);
        return evh;
    }

    public void onBindViewHolder(ElViewHolder holder, int pos){
        holder.bindItemLista(datosFiltrados.get(pos), holder.itemView.getContext());
    }
    @Override
    public int getItemCount() {
        return datosFiltrados.size();
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
