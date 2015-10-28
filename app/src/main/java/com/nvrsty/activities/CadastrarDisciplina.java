package com.nvrsty.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nvrsty.R;
import com.nvrsty.adapters.TabsAdapter;
import com.nvrsty.extras.SlidingTabLayout;
import com.nvrsty.fragments.HorariosFragment;
import com.nvrsty.fragments.InformacoesGeraisFragment;
import com.nvrsty.models.Disciplina;
import com.nvrsty.models.HorarioAula;

import java.util.List;

public class CadastrarDisciplina extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private List<HorarioAula> listaHorarios;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_materia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastrar matéria");
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.ic_action_navigation_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(tabsAdapter);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        slidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

        slidingTabLayout.setCustomTabView(R.layout.layout_tab_view, R.id.txt_tab);
        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastrar_materia, menu);
        return true;
    }

    // Recebe o evento de clique e repassa para o fragment
    public void onColorSelected(View v) {
        InformacoesGeraisFragment fragment = (InformacoesGeraisFragment) tabsAdapter.getItem(0);
        fragment.onColorSelected(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_confirmar:
                Disciplina disciplina = montarMateria();
                if (disciplina != null) {
                    Intent intent = new Intent();
                    intent.putExtra("disciplina", disciplina);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            case android.R.id.home:
                this.onBackPressed();
                break;
        }

        return true;
    }

    // Junta as informações dos dois fragments para formar uma matéria
    private Disciplina montarMateria() {
        InformacoesGeraisFragment informacoesGeraisFragment = (InformacoesGeraisFragment) tabsAdapter.getItem(0);
        HorariosFragment horariosFragment = (HorariosFragment) tabsAdapter.getItem(1);


        if (validarCampos(informacoesGeraisFragment, horariosFragment)) {
            String nomeMateria = informacoesGeraisFragment.cpNomeDisciplina.getText().toString();
            String nomeProfessor = informacoesGeraisFragment.cpNomeProfessor.getText().toString();
            String sigla = informacoesGeraisFragment.cpSigla.getText().toString();
            int frequencia = informacoesGeraisFragment.spFrequencia.getSelectedItemPosition();
            int cor = informacoesGeraisFragment.cor;
            int qtUnidades = informacoesGeraisFragment.qtUnidades;
            List<HorarioAula> listaHorarios = horariosFragment.listaHorarios;


            return new Disciplina(nomeMateria, nomeProfessor, sigla, frequencia, cor, qtUnidades, listaHorarios);
        }
        return null;
    }

    private boolean validarCampos(InformacoesGeraisFragment informacoesGeraisFragment, HorariosFragment horariosFragment) {
        boolean valido = true;

        if (informacoesGeraisFragment.cpNomeDisciplina.getText().toString().trim().isEmpty()) {
            informacoesGeraisFragment.cpNomeDisciplina.setError("Campo obrigatório");
            valido = false;
        } else {
            informacoesGeraisFragment.cpNomeDisciplina.setError(null);
        }

        if (informacoesGeraisFragment.cpSigla.getText().toString().trim().isEmpty()) {
            informacoesGeraisFragment.cpSigla.setError("Campo obrigatório");
            valido = false;
        } else {
            informacoesGeraisFragment.cpSigla.setError(null);
        }

        if (valido && horariosFragment.listaHorarios.size() == 0) {
            Toast.makeText(this, "Você precisa adicionar pelo menos um horário", Toast.LENGTH_LONG).show();
            viewPager.setCurrentItem(1, true);
            valido = false;
        }

        return valido;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarDisciplina.this);

        builder.setTitle("Confirmar cancelamento");
        builder.setMessage("Você tem certeza que deseja cancelar o cadastro?");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CadastrarDisciplina.this.finish();
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
