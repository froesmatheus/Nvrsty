package com.nvrsty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nvrsty.R;
import com.nvrsty.models.HorarioLembrete;

import java.util.List;

/**
 * Created by mathe on 06/11/2015.
 */
public class SpinnerAdapterDias extends BaseAdapter {
    private Context context;
    private List<HorarioLembrete> list;

    public SpinnerAdapterDias(Context context, List<HorarioLembrete> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_dia_view, null);

        TextView txtDia = (TextView) view.findViewById(R.id.dia_txt);
        TextView txtHora = (TextView) view.findViewById(R.id.hora_txt);

        HorarioLembrete horarioLembrete = list.get(position);

        txtDia.setText(horarioLembrete.getDia());
        txtHora.setText(horarioLembrete.getHora());
        return view;
    }
}
