package com.example.vtr.googlemapisapi_nougat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Enderecos extends AppCompatActivity {

    private ListView lstEnderecos;
    private List<Endereco> enderecos;
    private MeuDB db;

    String[] listItems = {"Endereco 1", "Endereco 2 ", "Endereco 3", "Endereco 4" };
    ArrayAdapter<String> adapter;
//    ArrayList<String> listEnderecos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);

        db = new MeuDB(this);
        // Lista
        lstEnderecos = findViewById(R.id.lstEnderecos);
        enderecos = db.getAllEnderecos();

        EnderecosAdapter adapter = new EnderecosAdapter(this, enderecos);
//        listView = (ListView) findViewById(R.id.lstEnderecos);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        lstEnderecos.setAdapter(adapter);
//        lstEnderecos.setOnClickListener();
    }

//    public void adicionarEndereco(String endereco) {
//        listEnderecos.add(endereco);
//        adapter.notifyDataSetChanged();
//    }

    public void voltarCheckIn(View view) {
        Intent intent = new Intent(this, CheckInAPI.class);
        startActivity(intent);
    }
}
