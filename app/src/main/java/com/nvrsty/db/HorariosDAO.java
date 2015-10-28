package com.nvrsty.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nvrsty.models.HorarioAula;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Matheus on 07/10/2015.
 */
public class HorariosDAO {
    private DBCore dbCore;
    private SQLiteDatabase db;

    public HorariosDAO(Context context) {
        dbCore = new DBCore(context);
        db = dbCore.getWritableDatabase();
    }

    public void inserirHorarios(long _idDisciplina, List<HorarioAula> listaHorarios) {
        for (int i = 0; i < listaHorarios.size(); i++) {
            ContentValues contentValues = new ContentValues();

            HorarioAula horarioAula = listaHorarios.get(i);

            contentValues.put(DBCore.COLUNA_HORARIOS_DIA_SEMANA, horarioAula.getDiaSemana());
            contentValues.put(DBCore.COLUNA_HORARIOS_HORARIO_INICIO, horarioAula.getHoraInicio().getTimeInMillis());
            contentValues.put(DBCore.COLUNA_HORARIOS_HORARIO_FIM, horarioAula.getHoraFim().getTimeInMillis());
            contentValues.put(DBCore.COLUNA_HORARIOS_BLOCO, horarioAula.getBloco());
            contentValues.put(DBCore.COLUNA_HORARIOS_SALA, horarioAula.getSala());
            contentValues.put(DBCore.COLUNA_HORARIOS_ID_DISCIPLINA, _idDisciplina);

            db.insert(DBCore.TABELA_HORARIOS, null, contentValues);
        }
    }

    public List<HorarioAula> getListaHorarios(int _id) {
        List<HorarioAula> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBCore.TABELA_HORARIOS + " WHERE " + DBCore.COLUNA_HORARIOS_ID_DISCIPLINA + " =?"
                , new String[]{_id + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                HorarioAula horarioAula = new HorarioAula();

                horarioAula.setBloco(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_BLOCO)));
                horarioAula.setSala(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_SALA)));
                horarioAula.setDiaSemana(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_DIA_SEMANA)));
                horarioAula.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_ID)));

                Calendar calendarInicio = Calendar.getInstance(TimeZone.getDefault());
                calendarInicio.setTimeInMillis((cursor.getLong(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_HORARIO_INICIO))));

                horarioAula.setHoraInicio(calendarInicio);

                Calendar calendarFim = Calendar.getInstance(TimeZone.getDefault());
                calendarFim.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_HORARIO_FIM)));

                horarioAula.setHoraFim(calendarFim);

                list.add(horarioAula);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public boolean remover(int _idDisciplina) {
        int status = db.delete(DBCore.TABELA_HORARIOS, DBCore.COLUNA_HORARIOS_ID_DISCIPLINA + " = ?", new String[] {_idDisciplina+""});

        return status != 0;
    }


    public List<HorarioAula> filtrarHorariosPorData(Calendar dataInicial, Calendar dataFinal) {
        List<HorarioAula> listaHorarios = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Horarios WHERE " + DBCore.COLUNA_HORARIOS_HORARIO_INICIO + " BETWEEN ? AND ? ORDER BY " + DBCore.COLUNA_HORARIOS_HORARIO_INICIO + " DESC", new String[] {dataInicial.getTimeInMillis()+"", dataFinal.getTimeInMillis()+""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                HorarioAula horarioAula = new HorarioAula();

                horarioAula.setBloco(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_BLOCO)));
                horarioAula.setSala(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_SALA)));
                horarioAula.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_ID)));
                horarioAula.setDiaSemana(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_DIA_SEMANA)));
                horarioAula.setIdDisciplinaRelacionada(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_ID_DISCIPLINA)));

                Calendar calendarInicial = Calendar.getInstance(TimeZone.getDefault());
                calendarInicial.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_HORARIO_INICIO)));

                Calendar calendarFinal = Calendar.getInstance(TimeZone.getDefault());
                calendarFinal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_HORARIO_FIM)));

                horarioAula.setHoraInicio(calendarInicial);
                horarioAula.setHoraFim(calendarFinal);

                listaHorarios.add(horarioAula);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listaHorarios;
    }
}
