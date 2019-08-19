package com.github.machadowma.ligatruco;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private Button buttonJogador, buttonEquipe, buttonPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonJogador = (Button) findViewById(R.id.buttonJogador);
        buttonEquipe = (Button) findViewById(R.id.buttonEquipe);
        buttonPartida = (Button) findViewById(R.id.buttonPartida);

        buttonJogador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTela("Jogador");
            }
        });

        buttonEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTela("Equipe");
            }
        });

        buttonPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTela("Partida");
            }
        });

        criarBancoDados();
    }

    public void criarBancoDados(){
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS jogador(" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    " ) " );
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS equipe(" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    " )" );
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS equipe_jogador(" +
                    "   id_equipe INTEGER" +
                    " , id_jogador INTEGER" +
                    " , FOREIGN KEY(id_jogador) REFERENCES jogador(id)" +
                    " , FOREIGN KEY(id_equipe) REFERENCES equipe(id)" +
                    " )");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS partida(" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , data DATE" +
                    " , id_equipe_1 INTEGER" +
                    " , pontos_equipe_1 INTEGER" +
                    " , id_equipe_2 INTEGER" +
                    " , pontos_equipe_2 INTEGER" +
                    " , FOREIGN KEY(id_equipe_1) REFERENCES equipe(id)" +
                    " , FOREIGN KEY(id_equipe_2) REFERENCES equipe(id)" +
                    " )" );


            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirTela(String tela){
        Intent intent;
        switch (tela){
            case "Jogador":
                intent = new Intent(this,JogadorActivity.class);
                startActivity(intent);
                break;
            case "Equipe":
                intent = new Intent(this,EquipeActivity.class);
                startActivity(intent);
                break;
            case "Partida":
                intent = new Intent(this,PartidaActivity
                        .class);
                startActivity(intent);
                break;
        }
    }
}
