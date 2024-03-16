package com.example.proyectointegradorlaolla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Anadir extends AppCompatActivity {

    private EditText nombre;
    private EditText tiempo;
    private EditText descripcion;
    private EditText ingredientes;
    private EditText pasos;
    private Switch vegetariano;
    private Switch celiaco;

    private Bitmap imageBit;
    private ImageView img;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private long cont;

    public Anadir() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir);
        nombre = findViewById(R.id.editTextnombre);
        tiempo = findViewById(R.id.editTexttiempo);
        descripcion = findViewById(R.id.editTextdescripcion);
        ingredientes = findViewById(R.id.editTextingredientes);
        pasos = findViewById(R.id.editTextpreparacion);
        vegetariano = findViewById(R.id.swveg);
        celiaco = findViewById(R.id.swcel);
        img = findViewById(R.id.imgviewfoto);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        DatabaseReference ref = database.getReference().child("recetas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtener el número de nodes hijos
                long numHijos = dataSnapshot.getChildrenCount();
                cont  = numHijos;
                Log.d("Firebase", "El nodo tiene " + numHijos + " hijos.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    ActivityResultLauncher<Intent> arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                imageBit = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBit);


            }
        }
    });

    public void guardarReceta(View view){
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("celiaco").setValue(celiaco.isChecked());
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("descripcion").setValue(descripcion.getText().toString());
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("id").setValue(String.valueOf(this.cont + 1));
        String[] u = nombre.getText().toString().split("");
        String v = "";
        for (int i = 0; i < u.length; i++){
            v = v + u[i];
        }
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("imagen").setValue(v);
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("ingredientes").setValue(ingredientes.getText().toString());
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("nombre").setValue(nombre.getText().toString());
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("pasos").setValue(pasos.getText().toString());
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("tiempo").setValue(Integer.parseInt(tiempo.getText().toString()));
        database.getReference().child("recetas").child(String.valueOf(this.cont)).child("vegetariano").setValue(vegetariano.isChecked());


        subirFoto(v);
        nombre.setText("");
        tiempo.setText("");
        descripcion.setText("");
        ingredientes.setText("");
        pasos.setText("");
        vegetariano.setChecked(false);
        celiaco.setChecked(false);
        cont++;
        finish();
    }

    public void camara(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        arl.launch(intent);
    }

    public void subirFoto(String ruta){
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference ref = storage.getReference().child(ruta + ".jpeg");
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(e -> Toast.makeText(Anadir.this, "Error al subir la foto", Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> Toast.makeText(Anadir.this, "Foto subida correctamente", Toast.LENGTH_SHORT).show());

    }
}
