package com.github.machadowma.ligatruco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EquipeAddIntegrActivity extends AppCompatActivity {
    private ListView listViewJogadores;
    private SQLiteDatabase bancoDados;
    private ArrayList<Integer> arrayIds;
    private Integer id_equipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_add_integr);

        Intent intent = getIntent();
        id_equipe = intent.getIntExtra("id",0);

        listViewJogadores = (ListView) findViewById(R.id.listViewJogadores);

        listViewJogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                incluirIntegrante(i);
            }
        });

        listarDados();
    }

    private void listarDados() {
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome FROM jogador where id not in (select id_jogador from equipe_jogador WHERE id_equipe = " + id_equipe.toString() + ")", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewJogadores.setAdapter(adapter);
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

    private void incluirIntegrante(Integer i) {
        Integer id_jogador = arrayIds.get(i);
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            String sql = "INSERT INTO equipe_jogador (id_equipe, id_jogador) VALUES (?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, id_equipe);
            stmt.bindLong(2, id_jogador);
            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
