package com.nvrsty.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.nvrsty.models.Evento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 27/10/2015.
 */
public class EventosDAO {
    private SQLiteDatabase db;
    private DisciplinasDAO disciplinasDAO;
    private HorariosEventosDAO horariosEventosDAO;
    private Context context;

    public EventosDAO(Context context) {
        this.context = context;
        DB core = new DB(context);
        db = core.getWritableDatabase();
        disciplinasDAO = new DisciplinasDAO(context);
        horariosEventosDAO = new HorariosEventosDAO(context);
    }

    public boolean inserir(Evento evento) {
        ContentValues cv = new ContentValues();

        cv.put(DB.COLUNA_EVENTOS_OBSERVACOES, evento.getObservacoes());
        cv.put(DB.COLUNA_EVENTOS_TIPO, evento.getTipo());
        cv.put(DB.COLUNA_EVENTOS_ID_DISCIPLINA, evento.getDisciplinaRelacionada().getId());
        cv.put(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO, evento.getHorarioEvento().getId());

        // Inserir o horário do evento em outra tabela
        long idHorario = horariosEventosDAO.inserir(evento.getHorarioEvento());

        cv.put(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO, idHorario);

        long id = db.insert(DB.TABELA_EVENTOS, null, cv);

        return id != -1 && idHorario != -1;
    }

    public boolean remover(long _id) {
        Evento evento = getEvento(_id);

        int status = db.delete(DB.TABELA_EVENTOS, " _id = ?", new String[]{_id + ""});

        boolean status2 = horariosEventosDAO.remover(evento.getHorarioEvento().getId());
        return status != 0 && status2;
    }

    public List<Evento> getListaEventos() {
        List<Evento> lista = new ArrayList<>();
        String[] colunas = {DB.COLUNA_EVENTOS_ID, DB.COLUNA_EVENTOS_TIPO, DB.COLUNA_EVENTOS_OBSERVACOES, DB.COLUNA_EVENTOS_ID_DISCIPLINA,
                DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO};

        Cursor cursor = db.query(DB.TABELA_EVENTOS, colunas, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Evento evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID)));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_TIPO)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DB.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));

                lista.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    public List<Evento> getListaEventos(int tipo) {
        List<Evento> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABELA_EVENTOS + "JOIN " + DB.TABELA_HORARIOS_EVENTO + " ON " +
                DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO + " = " + DB.TABELA_HORARIOS_EVENTO+"."+DB.COLUNA_HORARIOS_EVENTOS_ID + " WHERE " +
                DB.COLUNA_EVENTOS_TIPO + " = ? ORDER BY " + DB.COLUNA_HORARIOS_EVENTOS_DATA, new String[]{tipo + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Evento evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID)));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_TIPO)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DB.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));

                lista.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    public Evento getEvento(long _id) {
        Evento evento = new Evento();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABELA_EVENTOS + " WHERE " + DB.COLUNA_EVENTOS_ID + " = ?", new String[]{_id + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DB.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_TIPO)));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return evento;
    }

    public Cursor getEventosCursor(int tipo) {
        return db.rawQuery("SELECT * FROM " + DB.TABELA_EVENTOS + " JOIN " + DB.TABELA_HORARIOS_EVENTO + " ON " +
                DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO + " = " + DB.TABELA_HORARIOS_EVENTO+"."+ DB.COLUNA_HORARIOS_EVENTOS_ID + " WHERE " +
                DB.COLUNA_EVENTOS_TIPO + " = ? ORDER BY " + DB.COLUNA_HORARIOS_EVENTOS_DATA, new String[]{tipo + ""});
    }

    public Cursor getEventosCursor(int tipo, int disciplinaId) {
        return db.rawQuery("SELECT * FROM " + DB.TABELA_EVENTOS + " JOIN " + DB.TABELA_HORARIOS_EVENTO + " ON " +
                DB.COLUNA_EVENTOS_ID_HORARIO_EVENTO + " = " + DB.TABELA_HORARIOS_EVENTO +"."+ DB.COLUNA_HORARIOS_EVENTOS_ID + " WHERE " +
                DB.COLUNA_EVENTOS_TIPO + " = ? AND " + DB.COLUNA_EVENTOS_ID_DISCIPLINA
                + " = ? ORDER BY " + DB.COLUNA_HORARIOS_EVENTOS_DATA, new String[]{tipo + "", disciplinaId + ""});
    }

}
