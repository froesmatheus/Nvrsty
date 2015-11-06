package com.nvrsty.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nvrsty.R;
import com.nvrsty.adapters.ListaDisciplinasAdapter;
import com.nvrsty.db.DisciplinasDAO;
import com.nvrsty.models.Disciplina;

import java.util.List;

public class ListaDisciplinasActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Disciplina> listaDisciplinas;
    private ListaDisciplinasAdapter adapter;
    private boolean actionModeAberto;
    private DisciplinasDAO disciplinasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materias);

        disciplinasDAO = new DisciplinasDAO(this);

        final ListView lvMaterias = (ListView) findViewById(R.id.lv_materias);
        lvMaterias.setEmptyView(findViewById(R.id.empty_view));
        listaDisciplinas = disciplinasDAO.listarDisciplinas();

        adapter = new ListaDisciplinasAdapter(this, disciplinasDAO.getDisciplinasCursor());
        lvMaterias.setAdapter(adapter);

        registerForContextMenu(lvMaterias);

        lvMaterias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Disciplina disciplina = listaDisciplinas.get(position);

                final TextView tvNomeMateria = (TextView) view.findViewById(R.id.tv_nome_materia);
                final int corPadrao = tvNomeMateria.getTextColors().getDefaultColor();
                final TextView tvNomeProfessor = (TextView) view.findViewById(R.id.tv_nome_professor);

                actionModeAberto = false;

                lvMaterias.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                lvMaterias.startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        if (!actionModeAberto) {
                            actionModeAberto = true;
                            view.setBackgroundColor(disciplina.getCor());
                            tvNomeMateria.setTextColor(Color.WHITE);
                            tvNomeProfessor.setTextColor(Color.WHITE);
                            mode.getMenuInflater().inflate(R.menu.menu_actionmode_lista_materias, menu);
                        }
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.action_excluir) {
                            disciplinasDAO.remover(disciplina.getId());
                            final Disciplina disciplina = listaDisciplinas.remove(position);
                            adapter.changeCursor(disciplinasDAO.getDisciplinasCursor());
                            adapter.notifyDataSetChanged();

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.snack_back_location), "Matéria removida", Snackbar.LENGTH_SHORT).setAction("Desfazer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listaDisciplinas.add(position, disciplina);
                                    disciplinasDAO.inserir(disciplina);
                                    adapter.changeCursor(disciplinasDAO.getDisciplinasCursor());
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            snackbar.show();
                            actionModeAberto = false;
                        }
                        mode.finish();
                        view.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        if (actionModeAberto) {
                            view.setBackgroundColor(Color.TRANSPARENT);
                            tvNomeMateria.setTextColor(corPadrao);
                            tvNomeProfessor.setTextColor(corPadrao);
                            actionModeAberto = false;
                        }
                    }
                });
                return true;
            }
        });


        lvMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Disciplina disciplina = disciplinasDAO.getDisciplina(id);
                Intent intent = new Intent(getApplicationContext(), InformacoesMateriaActivity.class);
                intent.putExtra("disciplina", disciplina);
                startActivityForResult(intent, 2);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(getApplicationContext(), CadastrarDisciplinaActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == 1) {
                Disciplina disciplina = (Disciplina) data.getSerializableExtra("disciplina");
                disciplinasDAO.inserir(disciplina);
                listaDisciplinas.add(disciplina);
                adapter.changeCursor(disciplinasDAO.getDisciplinasCursor());
                adapter.notifyDataSetChanged();
            } else if (requestCode == 2) {
                String situacao = data.getStringExtra("status");
                final Disciplina disciplina = (Disciplina) data.getSerializableExtra("disciplina");

                if (disciplina != null) {

                    if (situacao.equals("deletar")) {
                        boolean removeu = disciplinasDAO.remover(disciplina.getId());

                        if (removeu) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.snack_back_location), "Disciplina removida", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        listaDisciplinas.remove(disciplina);
                        adapter.changeCursor(disciplinasDAO.getDisciplinasCursor());
                        adapter.notifyDataSetChanged();
                    } else if (situacao.equals("atualizar")) {
                        boolean atualizou = disciplinasDAO.atualizar(disciplina);
                        if (atualizou) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.snack_back_location), "Disciplina atualizada", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        adapter.changeCursor(disciplinasDAO.getDisciplinasCursor());
                        listaDisciplinas = disciplinasDAO.listarDisciplinas();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
