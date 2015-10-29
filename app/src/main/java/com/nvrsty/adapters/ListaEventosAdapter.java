package com.nvrsty.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nvrsty.R;
import com.nvrsty.db.DB;
import com.nvrsty.db.DisciplinasDAO;
import com.nvrsty.db.HorariosEventosDAO;
import com.nvrsty.extras.Utils;
import com.nvrsty.models.Evento;

/**
 * Created by matheus on 27/10/2015.
 */
public class ListaEventosAdapter extends CursorAdapter {
    private DisciplinasDAO disciplinasDAO;
    private HorariosEventosDAO horariosEventosDAO;

    public ListaEventosAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        disciplinasDAO = new DisciplinasDAO(context);
        horariosEventosDAO = new HorariosEventosDAO(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lista_eventos_view, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Evento evento = new Evento();

        evento.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID)));
        evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_DISCIPLINA))));
        evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DB.COLUNA_EVENTOS_OBSERVACOES)));
        evento.setTipo(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_TIPO)));
        evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));

        TextView disciplinaRelacionada = (TextView) view.findViewById(R.id.disciplina_relacionada);
        TextView horarioData = (TextView) view.findViewById(R.id.data_horario);
        TextView blocoSala = (TextView) view.findViewById(R.id.bloco_sala);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_local);

        disciplinaRelacionada.setText(evento.getDisciplinaRelacionada().getNomeDisciplina());
        String dataFormatada = Utils.formatarDataDiaMesAno(evento.getHorarioEvento().getData());
        String horaFormatada = Utils.formatarDataHoraMinutos(evento.getHorarioEvento().getData());

        String dataHora = dataFormatada + " - " + horaFormatada;

        horarioData.setText(dataHora);

        if (evento.getHorarioEvento().getBloco().isEmpty() && evento.getHorarioEvento().getSala().isEmpty()) {
            imageView.setVisibility(View.GONE);
        } else {
            String blocoSalaStr = evento.getHorarioEvento().getBloco() + " " + evento.getHorarioEvento().getSala();
            blocoSala.setText(blocoSalaStr);
        }
    }
}
