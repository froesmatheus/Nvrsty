package com.nvrsty.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nvrsty.R;
import com.nvrsty.adapters.ListaEventosAdapter;
import com.nvrsty.adapters.TabsAdapterEventos;
import com.nvrsty.db.DisciplinasDAO;
import com.nvrsty.db.EventosDAO;
import com.nvrsty.extras.SlidingTabLayout;
import com.nvrsty.fragments.EventosFragment;
import com.nvrsty.models.Disciplina;
import com.nvrsty.models.Evento;

import java.util.ArrayList;
import java.util.List;

public class ListaEventos extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabsAdapterEventos tabsAdapter;
    private FloatingActionsMenu menu;
    private EventosDAO dao;
    private SlidingTabLayout slidingTabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setTitleTextColor(Color.WHITE);

        dao = new EventosDAO(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabsAdapter = new TabsAdapterEventos(getSupportFragmentManager(), this);

        viewPager.setAdapter(tabsAdapter);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        slidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

        slidingTabLayout.setCustomTabView(R.layout.layout_tab_view, R.id.txt_tab);
        slidingTabLayout.setViewPager(viewPager);

        findViewById(R.id.fab_prova).setOnClickListener(this);
        findViewById(R.id.fab_rep_aula).setOnClickListener(this);
        findViewById(R.id.fab_trabalho).setOnClickListener(this);
        findViewById(R.id.fab_atividade).setOnClickListener(this);

        menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        findViewById(R.id.view_pager).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (menu.isExpanded()) {
                    menu.collapse();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (menu.isExpanded())
            menu.collapse();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int tipo = -1;
        switch (v.getId()) {
            case R.id.fab_prova:
                tipo = Evento.PROVA;
                break;
            case R.id.fab_rep_aula:
                tipo = Evento.REP_DE_AULA;
                break;
            case R.id.fab_trabalho:
                tipo = Evento.TRABALHO;
                break;
            case R.id.fab_atividade:
                tipo = Evento.ATIVIDADE;
                break;
        }

        intent = new Intent(ListaEventos.this, CadastrarEvento.class).putExtra("tipo", tipo);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && requestCode == 1) {
            Evento evento = (Evento) data.getSerializableExtra("evento");
            dao.inserir(evento);
            EventosFragment fragment = (EventosFragment) tabsAdapter.getItem(evento.getTipo());

            if (fragment.adapter == null) {
                fragment.adapter = new ListaEventosAdapter(ListaEventos.this, dao.getEventosCursor(evento.getTipo()));
            }
            fragment.adapter.changeCursor(dao.getEventosCursor(evento.getTipo()));
            fragment.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:
                mostrarDialogFiltros();
        }


        return super.onOptionsItemSelected(item);
    }

    public void mostrarDialogFiltros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.filtro_titulo);

        View view = getLayoutInflater().inflate(R.layout.popup_filtros, null);

        final Spinner disciplinaSpinner = (Spinner) view.findViewById(R.id.disciplina_spinner);
        final Spinner diaSpinner = (Spinner) view.findViewById(R.id.dia_spinner);

        diaSpinner.setEnabled(false);
        disciplinaSpinner.setEnabled(false);

        RadioButton rb01 = (RadioButton) view.findViewById(R.id.rb01);
        RadioButton rb02 = (RadioButton) view.findViewById(R.id.rb02);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        rb01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                disciplinaSpinner.setEnabled(isChecked);
            }
        });

        rb02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                diaSpinner.setEnabled(isChecked);
            }
        });

        final DisciplinasDAO disciplinasDAO = new DisciplinasDAO(this);
        List<Disciplina> list = disciplinasDAO.listarDisciplinas();
        ArrayAdapter<Disciplina> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);

        disciplinaSpinner.setAdapter(adapter);
        builder.setView(view);

        List<String> stringList = new ArrayList<>();
        stringList.add("Hoje");
        stringList.add("Esta semana");
        stringList.add("Este mês");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        diaSpinner.setAdapter(arrayAdapter);

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = radioGroup.getCheckedRadioButtonId();

                Disciplina disciplina = (Disciplina) disciplinaSpinner.getSelectedItem();

                switch (id) {
                    case R.id.rb01:
                        toolbar.setSubtitle("Filtrando por disciplina (" + disciplina.getSigla() + ")");
                        for (int i = 0; i < 4; i++) {
                            EventosFragment fragment = (EventosFragment) tabsAdapter.getItem(i);

                            if (fragment.adapter == null) {
                                fragment.adapter = new ListaEventosAdapter(ListaEventos.this, dao.getEventosCursor(i, disciplina.getId()));
                            }
                            fragment.adapter.changeCursor(dao.getEventosCursor(i, disciplina.getId()));
                            fragment.adapter.notifyDataSetChanged();
                        }
                        break;
                    case R.id.rb02:


                        toolbar.setSubtitle("Filtrando por dia (" + diaSpinner.getSelectedItem().toString() + ")");
                        break;
                }
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
