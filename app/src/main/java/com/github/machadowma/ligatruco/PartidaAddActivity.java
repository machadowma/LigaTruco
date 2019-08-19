package com.github.machadowma.ligatruco;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PartidaAddActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ArrayList<Integer> arrayIdsEquipes;
    EditText editTextDataPartida;
    Button button;
    private Spinner spinnerEq1,spinnerEq1Pts,spinnerEq2,spinnerEq2Pts;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_add);

        editTextDataPartida = (EditText) findViewById(R.id.editTextDataPartida);
        button = (Button) findViewById(R.id.button);
        spinnerEq1Pts = (Spinner) findViewById(R.id.spinnerEq1Pts);
        spinnerEq1 = (Spinner) findViewById(R.id.spinnerEq1);
        spinnerEq2Pts = (Spinner) findViewById(R.id.spinnerEq2Pts);
        spinnerEq2 = (Spinner) findViewById(R.id.spinnerEq2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

        editTextDataPartida.setFocusable(false);
        editTextDataPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(PartidaAddActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calander2 = Calendar.getInstance();
                        calander2.setTimeInMillis(0);
                        calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date SelectedDate = calander2.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calander2.getTime());
                        editTextDataPartida.setText(strDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        carregarDados();
    }

    private void carregarDados(){

        ArrayAdapter<CharSequence> adapterEqPts = ArrayAdapter.createFromResource(
                this,
                R.array.pontos,
                android.R.layout.simple_spinner_item
        );
        adapterEqPts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEq1Pts.setAdapter(adapterEqPts);
        spinnerEq2Pts.setAdapter(adapterEqPts);

        ArrayList<String> linhas = new ArrayList<String>();
        ArrayAdapter<String> adapterEqNome = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, linhas);
        adapterEqNome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            String sql = " SELECT id,nome from equipe ";
            Cursor cursor = bancoDados.rawQuery(sql, null);
            if (cursor.moveToFirst() ) {
                arrayIdsEquipes = new ArrayList<Integer>();
                while (cursor != null) {
                    arrayIdsEquipes.add(cursor.getInt(cursor.getColumnIndex("id")));
                    linhas.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinnerEq1.setAdapter(adapterEqNome);
        spinnerEq2.setAdapter(adapterEqNome);
    }

    private void cadastrar(){

        if(TextUtils.isEmpty(editTextDataPartida.getText().toString())){
            editTextDataPartida.setError("Este campo é obrigatório");
            return;
        }

        try {

            bancoDados = openOrCreateDatabase("ligatruco", MODE_PRIVATE, null);
            String sql = "INSERT INTO partida (data,id_equipe_1,pontos_equipe_1,id_equipe_2,pontos_equipe_2) VALUES (?,?,?,?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextDataPartida.getText().toString());
            stmt.bindLong(2, arrayIdsEquipes.get(spinnerEq1.getSelectedItemPosition()));
            stmt.bindLong(3, Integer.parseInt(spinnerEq1Pts.getSelectedItem().toString()));
            stmt.bindLong(4, arrayIdsEquipes.get(spinnerEq2.getSelectedItemPosition()));
            stmt.bindLong(5, Integer.parseInt(spinnerEq2Pts.getSelectedItem().toString()));
            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
