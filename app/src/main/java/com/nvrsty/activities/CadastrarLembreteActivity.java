package com.nvrsty.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nvrsty.R;
import com.nvrsty.adapters.SpinnerAdapterDias;
import com.nvrsty.extras.Utils;
import com.nvrsty.models.HorarioLembrete;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastrarLembreteActivity extends AppCompatActivity {
    private static final int OPCAO_ESCOLHER_DIA = 2, OPCAO_ESCOLHER_HORA = 4;
    private Calendar dataLembrete, dataAtual;
    private Spinner spinnerDia, spinnerHora;
    private ArrayAdapter<String> adapterDia;
    private SpinnerAdapterDias adapterHora;
    private List<String> opcoesDia;
    private List<HorarioLembrete> opcoesHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lembrete);

        dataLembrete = Calendar.getInstance();
        dataAtual = Calendar.getInstance();

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_navigation_close);


        spinnerDia = (Spinner) findViewById(R.id.spinner_dia);

        opcoesDia = new ArrayList<>();

        opcoesDia.add("Hoje");
        opcoesDia.add("Amanhã");
        opcoesDia.add("Escolher dia");
        adapterDia = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoesDia);

        spinnerDia.setAdapter(adapterDia);


        spinnerDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == OPCAO_ESCOLHER_DIA) {
                    mostrarDatePicker();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerHora = (Spinner) findViewById(R.id.spinner_hora);

        opcoesHora = new ArrayList<>();

        opcoesHora.add(new HorarioLembrete("Manhã", "09:00"));
        opcoesHora.add(new HorarioLembrete("Tarde", "15:00"));
        opcoesHora.add(new HorarioLembrete("Fim da tarde", "17:00"));
        opcoesHora.add(new HorarioLembrete("Noite", "20:00"));
        opcoesHora.add(new HorarioLembrete("Escolher hora...", ""));

        adapterHora = new SpinnerAdapterDias(this, opcoesHora);

        spinnerHora.setAdapter(adapterHora);

        spinnerHora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == OPCAO_ESCOLHER_HORA) {
                    mostrarTimePicker();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void mostrarTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(CadastrarLembreteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dataLembrete.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dataLembrete.set(Calendar.MINUTE, minute);

                String horarioStr = Utils.formatarDataHoraMinutos(dataLembrete);

                opcoesHora.remove(OPCAO_ESCOLHER_HORA);
                opcoesHora.add(OPCAO_ESCOLHER_HORA, new HorarioLembrete(horarioStr, ""));
                adapterHora.notifyDataSetChanged();
            }
        }, dataAtual.get(Calendar.HOUR_OF_DAY), dataAtual.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CadastrarLembreteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataLembrete.set(Calendar.YEAR, year);
                dataLembrete.set(Calendar.MONTH, monthOfYear);
                dataLembrete.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dataStr = Utils.formatarDataDiaMesAno(dataLembrete);

                opcoesDia.set(OPCAO_ESCOLHER_DIA, dataStr);
                adapterDia.notifyDataSetChanged();
            }
        }, dataAtual.get(Calendar.YEAR), dataAtual.get(Calendar.MONTH), dataAtual.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarLembreteActivity.this);

        builder.setTitle(R.string.confirmar_cancelamento);
        builder.setMessage(R.string.confirmar_cancelar_cadastro_lembrete);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CadastrarLembreteActivity.this.finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
