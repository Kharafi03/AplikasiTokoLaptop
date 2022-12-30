package com.example.aplikasitokolaptop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aplikasitokolaptop.adapter.LaptopAdapter;
import com.example.aplikasitokolaptop.model.Laptop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private FloatingActionButton btn_add;
    private SearchView searchView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Laptop> list = new ArrayList<>();
    private LaptopAdapter laptopAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        recycler_view = findViewById(R.id.recycle_view);
        btn_add = findViewById(R.id.btn_add);
        searchView = findViewById(R.id.searchView);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchData(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchData(newText);
//                return false;
//            }
//        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");
        laptopAdapter = new LaptopAdapter(getApplicationContext(), list);
        laptopAdapter.setDialog(new LaptopAdapter.Dialog() {
            @Override
            public void onLongClick(int pos) {

                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Masukan aksi");
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("nama", list.get(pos).getNama());
                                intent.putExtra("harga", list.get(pos).getHarga());
                                intent.putExtra("avatar", list.get(pos).getAvatar());
                                intent.putExtra("spesifikasi", list.get(pos).getSpesifikasi());
                                startActivity(intent);
                                break;
                            case 1:
                                deleteData(list.get(pos).getId(), list.get(pos).getAvatar());
                                break;
                        }
                    }
                });
                dialog.show();
            }

            public void onItemClick(int pos) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                intent.putExtra("avatar", list.get(pos).getAvatar());
                intent.putExtra("nama", list.get(pos).getNama());
                intent.putExtra("harga", list.get(pos).getHarga());

                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);

        recycler_view.setLayoutManager(layoutManager);
        recycler_view.addItemDecoration(decoration);
        recycler_view.setAdapter(laptopAdapter);


        btn_add.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), EditorActivity.class));
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        getData();
    }

    private void getData(){
        progressDialog.show();
        db.collection("laptop")
                .orderBy("nama")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Laptop laptop = new Laptop(document.getString("nama"), document.getString("harga"), document.getString("avatar"), document.getString("spesifikasi"));
                                laptop.setId(document.getId());
                                list.add(laptop);
                            }
                            laptopAdapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Data gagal diambil", Toast.LENGTH_SHORT).show();
                        }
                        laptopAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
    }

    private void deleteData(String id, String avatar){
        progressDialog.show();
        db.collection("laptop").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseStorage.getInstance().getReferenceFromUrl(avatar).delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    getData();
                                                }
                                            });
                            Toast.makeText(getApplicationContext(), "Berhasil menghapus data", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}