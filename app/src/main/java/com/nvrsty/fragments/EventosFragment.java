package com.nvrsty.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nvrsty.R;
import com.nvrsty.adapters.ListaEventosAdapter;
import com.nvrsty.db.EventosDAO;
import com.nvrsty.models.Evento;

/**
 * Created by matheus on 27/10/2015.
 */
public class EventosFragment extends Fragment {
    public ListaEventosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_eventos_lv, null);

        int position = getArguments().getInt("position", -1);

        EventosDAO eventosDAO = new EventosDAO(getActivity());

        ListView lvEventos = (ListView) view.findViewById(R.id.lv_eventos);

        adapter = new ListaEventosAdapter(getActivity(), eventosDAO.getEventosCursor(position));

        lvEventos.setAdapter(adapter);

        return view;
    }
}
