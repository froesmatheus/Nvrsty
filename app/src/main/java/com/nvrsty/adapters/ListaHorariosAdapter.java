package com.nvrsty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nvrsty.extras.Utils;
import com.nvrsty.models.HorarioAula;
import com.nvrsty.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Matheus on 28/09/2015.
 */
public class ListaHorariosAdapter extends BaseAdapter {
    private List<HorarioAula> list = new ArrayList<>();
    private Context context;

    public ListaHorariosAdapter(Context context, List<HorarioAula> listaHorarios) {
        this.list = listaHorarios;
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.lista_horarios_view, null);

        ImageView localizacao = (ImageView) view.findViewById(R.id.iv_posicao);

        TextView horarioInicioFim, diaSemana, bloco, sala;

        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(2);

        horarioInicioFim = (TextView) view.findViewById(R.id.tv_hora_inicio_fim);
        bloco = (TextView) view.findViewById(R.id.tv_bloco);
        sala = (TextView) view.findViewById(R.id.tv_sala);
        diaSemana = (TextView) view.findViewById(R.id.tv_dia_semana);

        HorarioAula horarioAula;

        horarioAula = list.get(position);

        String horaInicio = format.format(horarioAula.getHoraInicio().get(Calendar.HOUR_OF_DAY)) + ":" +
                format.format(horarioAula.getHoraInicio().get(Calendar.MINUTE));

        String horaFim = format.format(horarioAula.getHoraFim().get(Calendar.HOUR_OF_DAY)) + ":" +
                format.format(horarioAula.getHoraFim().get(Calendar.MINUTE));
        horarioInicioFim.setText(horaInicio + " - " + horaFim);

        if (horarioAula.getBloco().isEmpty() & horarioAula.getSala().isEmpty())
            localizacao.setImageDrawable(null);

        bloco.setText(horarioAula.getBloco());
        sala.setText(horarioAula.getSala());
        diaSemana.setText(Utils.getDiaSemana(horarioAula.getDiaSemana()));
        return view;
    }

    static class ViewHolder {
        public TextView horarioInicioFim, bloco, sala, diaSemana;
    }
}
