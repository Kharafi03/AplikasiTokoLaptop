package com.example.aplikasitokolaptop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView namabarang, hargabarang, txtspesifikasi;
    ImageView gambarlaptop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String avatar = getIntent().getStringExtra("avatar");
        String nama = getIntent().getStringExtra("nama");
        String harga = getIntent().getStringExtra("harga");
        String spesifikasi = getIntent().getStringExtra("spesifikasi");

        gambarlaptop = findViewById(R.id.gambar_laptop);
        namabarang = findViewById(R.id.txttNamaBarang);
        hargabarang = findViewById(R.id.txttHargaBarang);
        txtspesifikasi = findViewById(R.id.txttSpesifikasi);

        namabarang.setText(nama);
        hargabarang.setText(harga);
        txtspesifikasi.setText(spesifikasi);
        Glide.with(getApplicationContext()).load(avatar).into(gambarlaptop);
    }

}