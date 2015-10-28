package com.nvrsty.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public EventosDAO(Context context) {
        DBCore core = new DBCore(context);
        db = core.getWritableDatabase();
        disciplinasDAO = new DisciplinasDAO(context);
        horariosEventosDAO = new HorariosEventosDAO(context);
    }

    public boolean inserir(Evento evento) {
        ContentValues cv = new ContentValues();

        cv.put(DBCore.COLUNA_EVENTOS_OBSERVACOES, evento.getObservacoes());
        cv.put(DBCore.COLUNA_EVENTOS_TIPO, evento.getTipo());
        cv.put(DBCore.COLUNA_EVENTOS_ID_DISCIPLINA, evento.getDisciplinaRelacionada().getId());
        cv.put(DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO, evento.getHorarioEvento().getId());

        // Inserir o horário do evento em outra tabela
        long idHorario = horariosEventosDAO.inserir(evento.getHorarioEvento());

        cv.put(DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO, idHorario);

        long id = db.insert(DBCore.TABELA_EVENTOS, null, cv);

        return id != -1 && idHorario != -1;
    }

    public List<Evento> getListaEventos() {
        List<Evento> lista = new ArrayList<>();
        String[] colunas = {DBCore.COLUNA_EVENTOS_ID, DBCore.COLUNA_EVENTOS_TIPO, DBCore.COLUNA_EVENTOS_OBSERVACOES, DBCore.COLUNA_EVENTOS_ID_DISCIPLINA,
                DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO};

        Cursor cursor = db.query(DBCore.TABELA_EVENTOS, colunas, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Evento evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID)));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_TIPO)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));

                lista.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    public List<Evento> getListaEventos(int tipo) {
        List<Evento> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBCore.TABELA_EVENTOS + " WHERE " + DBCore.COLUNA_EVENTOS_TIPO + " =?", new String[]{tipo + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Evento evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID)));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_TIPO)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));

                lista.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    public Evento getEvento(long _id) {
        Evento evento = new Evento();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBCore.TABELA_EVENTOS + " WHERE " + DBCore.COLUNA_EVENTOS_ID + " = ?", new String[]{_id + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                evento = new Evento();

                evento.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID)));
                evento.setObservacoes(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_OBSERVACOES)));
                evento.setDisciplinaRelacionada(disciplinasDAO.getDisciplina(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_DISCIPLINA))));
                evento.setTipo(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_TIPO)));
                evento.setHorarioEvento(horariosEventosDAO.getHorarioEvento(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_EVENTOS_ID_HORARIO_EVENTO))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return evento;
    }

    public Cursor getEventosCursor(int tipo) {
        return db.rawQuery("SELECT * FROM " + DBCore.TABELA_EVENTOS + " WHERE " + DBCore.COLUNA_EVENTOS_TIPO + " = ?", new String[]{tipo+""});
    }

}
