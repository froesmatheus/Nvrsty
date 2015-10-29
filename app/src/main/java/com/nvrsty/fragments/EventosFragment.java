package com.nvrsty.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nvrsty.R;
import com.nvrsty.adapters.ListaEventosAdapter;
import com.nvrsty.db.EventosDAO;

/**
 * Created by matheus on 27/10/2015.
 */
public class EventosFragment extends Fragment {
    public ListaEventosAdapter adapter;
    private EventosDAO eventosDAO;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position", -1);

        eventosDAO = new EventosDAO(getActivity());
        adapter = new ListaEventosAdapter(getActivity(), eventosDAO.getEventosCursor(position));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_eventos_lv, null);

        final ListView lvEventos = (ListView) view.findViewById(R.id.lv_eventos);

        registerForContextMenu(lvEventos);

        lvEventos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_eventos) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            String[] menuItems = new String[]{"Editar", "Excluir"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();


        switch (menuItemIndex) {
            case 1:
                eventosDAO.remover(info.id);
                adapter.changeCursor(eventosDAO.getEventosCursor(position));
                adapter.notifyDataSetChanged();
        }
        return true;
    }
}
