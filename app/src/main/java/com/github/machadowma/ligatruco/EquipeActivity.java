package com.github.machadowma.ligatruco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EquipeActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ListView listViewEquipe;
    private ArrayList<Integer> arrayIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });

        listViewEquipe = (ListView) findViewById(R.id.listViewEquipe);
        listViewEquipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirEquipe(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    private void abrirCadastro(){
        Intent intent = new Intent(this, EquipeAddActivity.class);
        startActivity(intent);
    }

    private void listarDados() {
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome FROM equipe", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewEquipe.setAdapter(adapter);
            cursor.moveToFirst();
            arrayIds = new ArrayList<Integer>();
            while (cursor != null) {
                arrayIds.add(cursor.getInt(cursor.getColumnIndex("id")));
                linhas.add(cursor.getString(1));
                cursor.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEquipe(Integer i){
        Intent intent = new Intent(this, EquipeEditActivity.class);
        intent.putExtra("id",arrayIds.get(i));
        startActivity(intent);
    }

}
