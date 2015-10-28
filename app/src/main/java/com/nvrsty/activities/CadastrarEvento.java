package com.nvrsty.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nvrsty.R;
import com.nvrsty.adapters.ListaDisciplinasAdapter;
import com.nvrsty.adapters.ListaHorariosAdapter;
import com.nvrsty.db.DisciplinasDAO;
import com.nvrsty.extras.Utils;
import com.nvrsty.models.Disciplina;
import com.nvrsty.models.Evento;
import com.nvrsty.models.HorarioEvento;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CadastrarEvento extends AppCompatActivity {
    private DisciplinasDAO dao;
    private Calendar dataAtual, dataEvento;
    private NumberFormat format = NumberFormat.getInstance();
    private EditText cpMateriaRelacionada, cpHorario, cpBloco, cpSala, cpData, cpObservacoes;
    private int tipoEvento;
    private Disciplina disciplinaRelacionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);

        dataAtual = Calendar.getInstance(TimeZone.getDefault());
        dataEvento = Calendar.getInstance(TimeZone.getDefault());
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_navigation_close);

        if (getIntent() != null) {
            tipoEvento = getIntent().getIntExtra("tipo", -1);
            String tituloToolbar = Utils.getTipoStr(tipoEvento);
            getSupportActionBar().setTitle(tituloToolbar);
        }

        format.setMinimumIntegerDigits(2);

        cpMateriaRelacionada = (EditText) findViewById(R.id.cp_materia_relacionada);
        cpBloco = (EditText) findViewById(R.id.cp_bloco);
        cpSala = (EditText) findViewById(R.id.cp_sala);
        cpData = (EditText) findViewById(R.id.cp_data);
        cpHorario = (EditText) findViewById(R.id.cp_horario);
        cpObservacoes = (EditText) findViewById(R.id.cp_observacoes);

        dao = new DisciplinasDAO(this);

        final List<Disciplina> disciplinas = dao.listarDisciplinas();

        cpMateriaRelacionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarEvento.this);

                ListaDisciplinasAdapter adapter = new ListaDisciplinasAdapter(CadastrarEvento.this, dao.getDisciplinasCursor());
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disciplinaRelacionada = disciplinas.get(which);
                        cpMateriaRelacionada.setText(disciplinaRelacionada.getNomeDisciplina());
                        verificarHorarios(disciplinaRelacionada, CadastrarEvento.this);
                    }
                });

                builder.setNeutralButton("Adicionar nova disclipina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CadastrarEvento.this, CadastrarDisciplina.class));
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }
        });


        cpData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mostrarDatePicker();
                }
            }
        });

        cpData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });


        cpHorario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mostrarTimePicker();
                }

            }
        });
        cpHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });
    }

    public void verificarHorarios(final Disciplina disciplina, Context context) {
        if (disciplina.getListaHorarios().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Horários preestabelecidos");

            ListaHorariosAdapter adapter = new ListaHorariosAdapter(context, disciplina.getListaHorarios());

            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cpHorario.setText(disciplina.getListaHorarios().get(which).getHoraInicio().get(Calendar.HOUR_OF_DAY) + ":"
                            + disciplina.getListaHorarios().get(which).getHoraInicio().get(Calendar.MINUTE));
                    cpBloco.setText(disciplina.getListaHorarios().get(which).getBloco());
                    cpSala.setText(disciplina.getListaHorarios().get(which).getSala());
                }
            });

            builder.setNeutralButton("Adicionar horário manualmente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cpHorario.setText("");
                    cpBloco.setText("");
                    cpSala.setText("");
                    dialog.dismiss();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();
        }

        cpHorario.setText("");
        cpBloco.setText("");
        cpSala.setText("");
    }

    public void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CadastrarEvento.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataEvento.set(Calendar.YEAR, year);
                dataEvento.set(Calendar.MONTH, monthOfYear);
                dataEvento.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cpData.setText(format.format(dayOfMonth) + "/" + format.format(++monthOfYear) + "/" + year);
            }
        }, dataAtual.get(Calendar.YEAR), dataAtual.get(Calendar.MONTH), dataAtual.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void mostrarTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(CadastrarEvento.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dataEvento.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dataEvento.set(Calendar.MINUTE, minute);
                cpHorario.setText(format.format(hourOfDay) + ":" + format.format(minute));
            }
        }, dataAtual.get(Calendar.HOUR_OF_DAY), dataAtual.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_confirmar:
                onResult();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onResult() {
        if (verificarCampos()) {
            Evento evento = new Evento();

            evento.setTipo(tipoEvento);
            evento.setObservacoes(cpObservacoes.getText().toString());
            evento.setDisciplinaRelacionada(disciplinaRelacionada);

            HorarioEvento horarioEvento = new HorarioEvento();
            horarioEvento.setBloco(cpBloco.getText().toString());
            horarioEvento.setSala(cpSala.getText().toString());
            horarioEvento.setData(dataEvento);

            evento.setHorarioEvento(horarioEvento);

            Intent intent = new Intent();
            intent.putExtra("evento", evento);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastrar_materia, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarEvento.this);

        builder.setTitle("Confirmar cancelamento");
        builder.setMessage("Você tem certeza que deseja cancelar o cadastro?");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CadastrarEvento.this.finish();
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


    public boolean verificarCampos() {
        boolean passou = true;

        if (cpMateriaRelacionada.getText().toString().trim().isEmpty()) {
            cpMateriaRelacionada.setError("Campo obrigatório");
            passou = false;
        } else {
            cpMateriaRelacionada.setError(null);
        }

        if (cpData.getText().toString().trim().isEmpty()) {
            cpData.setError("Campo obrigatório");
            passou = false;
        } else {
            cpData.setError(null);
        }

        if (cpHorario.getText().toString().trim().isEmpty()) {
            cpHorario.setError("Campo obrigatório");
            passou = false;
        } else {
            cpHorario.setError(null);
        }

        return passou;
    }
}
