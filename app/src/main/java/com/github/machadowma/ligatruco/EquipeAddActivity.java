package com.github.machadowma.ligatruco;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EquipeAddActivity extends AppCompatActivity {
    private Button button;
    private SQLiteDatabase bancoDados;
    private EditText editTextNomeEquipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_add);

        button = (Button) findViewById(R.id.button);
        editTextNomeEquipe = (EditText) findViewById(R.id.editTextDataPartida);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    private void cadastrar(){
        String valueNome = null;
        String valuePreparo = null;
        if(TextUtils.isEmpty(editTextNomeEquipe.getText().toString())){
            editTextNomeEquipe.setError("Este campo é obrigatório");
            return;
        } else {
            valueNome = editTextNomeEquipe.getText().toString();
        }

        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            String sql = "INSERT INTO equipe (nome) VALUES (?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueNome);
            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
