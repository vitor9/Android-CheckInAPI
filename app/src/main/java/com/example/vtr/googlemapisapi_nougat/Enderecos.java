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

    List<String> lstEnderecos = new ArrayList<>();
    ListView listView;
    String[] listItems = {"Endereco 1", "Endereco 2 ", "Endereco 3", "Endereco 4" };
    ArrayAdapter<String> adapter;
    ArrayList<String> listEnderecos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);


        // Lista
        listView = (ListView) findViewById(R.id.lstEnderecos);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    public void adicionarEndereco(String endereco) {
        listEnderecos.add(endereco);
        adapter.notifyDataSetChanged();
    }

    public void voltarCheckIn(View view) {
        Intent intent = new Intent(this, LocalizacaoAtual.class);
        startActivity(intent);
    }
}
