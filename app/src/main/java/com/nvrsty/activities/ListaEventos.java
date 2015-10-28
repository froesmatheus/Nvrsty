package com.nvrsty.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nvrsty.R;
import com.nvrsty.adapters.TabsAdapterEventos;
import com.nvrsty.db.EventosDAO;
import com.nvrsty.extras.SlidingTabLayout;
import com.nvrsty.fragments.EventosFragment;
import com.nvrsty.models.Evento;

public class ListaEventos extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabsAdapterEventos tabsAdapter;
    private FloatingActionsMenu menu;
    private EventosDAO dao;
    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

            fragment.adapter.changeCursor(dao.getEventosCursor(evento.getTipo()));
            fragment.adapter.notifyDataSetChanged();
        }

    }
}
