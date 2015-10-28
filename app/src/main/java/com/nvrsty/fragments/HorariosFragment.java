package com.nvrsty.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nvrsty.R;
import com.nvrsty.activities.CadastrarHorario;
import com.nvrsty.adapters.ListaHorariosAdapter;
import com.nvrsty.models.Disciplina;
import com.nvrsty.models.HorarioAula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matheus on 28/09/2015.
 */
public class HorariosFragment extends Fragment {
    public List<HorarioAula> listaHorarios;
    private ListaHorariosAdapter adapter;
    public FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.horarios_fragment, container, false);

        ListView lvHorarios = (ListView) view.findViewById(R.id.lv_horarios);

        registerForContextMenu(lvHorarios);
        lvHorarios.setEmptyView(view.findViewById(R.id.empty_view));

        listaHorarios = new ArrayList<>();

        adapter = new ListaHorariosAdapter(getActivity(), listaHorarios);

        lvHorarios.setAdapter(adapter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CadastrarHorario.class);
                startActivityForResult(intent, 1);
            }
        });


        if (getActivity().getIntent().getSerializableExtra("disciplina") != null) {
            Disciplina disciplina = (Disciplina) getActivity().getIntent().getSerializableExtra("disciplina");

            listaHorarios = disciplina.getListaHorarios();
            fab.setVisibility(View.GONE);
            adapter = new ListaHorariosAdapter(getActivity(), disciplina.getListaHorarios());
            lvHorarios.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_horarios) {
            menu.add("Editar");
            menu.add("Excluir");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final HorarioAula horario = listaHorarios.get(acmi.position);

        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.snack_back_location), "Horário removido", Snackbar.LENGTH_SHORT).setAction("Desfazer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaHorarios.add(acmi.position, horario);
                adapter.notifyDataSetChanged();
            }
        });

        if (item.getTitle().equals("Excluir")) {
            listaHorarios.remove(acmi.position);
            adapter.notifyDataSetChanged();
            snackbar.show();
        }
        return true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && requestCode == 1) {

            HorarioAula horario = (HorarioAula) data.getSerializableExtra("HORARIO");

            listaHorarios.add(horario);
            adapter.notifyDataSetChanged();

        }
    }
}
