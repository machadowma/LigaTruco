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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EquipeEditActivity extends AppCompatActivity {
    private Integer id;
    private TextView textViewEquipe;
    private ListView listViewIntegrantes;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incluirIntegrante();
            }
        });

        textViewEquipe = (TextView) findViewById(R.id.textViewEquipe);
        listViewIntegrantes = (ListView) findViewById(R.id.listViewIntegrantes);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        carregarDados();
    }

    protected void onResume() {
        super.onResume();
        carregarDados();
    }

    private void carregarDados(){
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome FROM equipe WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            textViewEquipe.setText(cursor.getString(cursor.getColumnIndex("nome")));

            cursor = bancoDados.rawQuery("SELECT j.nome FROM equipe_jogador ej INNER JOIN jogador j ON j.id=ej.id_jogador WHERE ej.id_equipe = " + id.toString(), null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewIntegrantes.setAdapter(adapter);
            if (cursor.moveToFirst()) {
                while (cursor != null) {
                    linhas.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void incluirIntegrante(){
        Intent intent = new Intent(this, EquipeAddIntegrActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

}
