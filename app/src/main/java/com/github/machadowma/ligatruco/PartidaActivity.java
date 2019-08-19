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

import java.util.ArrayList;

public class PartidaActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ListView listViewPartida;
    private ArrayList<Integer> arrayIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });

        listViewPartida = (ListView) findViewById(R.id.listViewPartida);
        listarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    private void listarDados() {
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            String sql = "" +
                    " SELECT " +
                    "   p.id " +
                    "  ,p.data " +
                    "  ,e1.nome as nome_equipe_1 " +
                    "  ,e2.nome as nome_equipe_2 " +
                    "  ,p.pontos_equipe_1 " +
                    "  ,p.pontos_equipe_2 " +
                    " FROM partida p " +
                    " LEFT JOIN equipe e1 ON e1.id = p.id_equipe_1 " +
                    " LEFT JOIN equipe e2 ON e2.id = p.id_equipe_2 " +
                    " ORDER BY p.data ";
            Cursor cursor = bancoDados.rawQuery(sql, null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewPartida.setAdapter(adapter);
            cursor.moveToFirst();
            arrayIds = new ArrayList<Integer>();
            while (cursor != null) {
                arrayIds.add(cursor.getInt(cursor.getColumnIndex("id")));

                linhas.add(
                        cursor.getString(
                                cursor.getColumnIndex("data"))
                                + ": "
                                + cursor.getString(cursor.getColumnIndex("nome_equipe_1"))
                                + " "
                                + cursor.getString(cursor.getColumnIndex("pontos_equipe_1"))
                                + " X "
                                + cursor.getString(cursor.getColumnIndex("pontos_equipe_2"))
                                + " "
                                + cursor.getString(cursor.getColumnIndex("nome_equipe_2"))
                );
                cursor.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirCadastro() {
        Intent intent = new Intent(this, PartidaAddActivity.class);
        startActivity(intent);
    }

}
