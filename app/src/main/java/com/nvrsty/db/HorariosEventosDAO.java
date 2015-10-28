package com.nvrsty.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nvrsty.models.HorarioEvento;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by matheus on 27/10/2015.
 */
public class HorariosEventosDAO {
    private SQLiteDatabase db;

    public HorariosEventosDAO(Context context) {
        DBCore core = new DBCore(context);
        db = core.getWritableDatabase();
    }

    public long inserir(HorarioEvento horarioEvento) {
        ContentValues cv = new ContentValues();

        cv.put(DBCore.COLUNA_HORARIOS_EVENTOS_BLOCO, horarioEvento.getBloco());
        cv.put(DBCore.COLUNA_HORARIOS_EVENTOS_SALA, horarioEvento.getSala());
        cv.put(DBCore.COLUNA_HORARIOS_EVENTOS_DATA, horarioEvento.getData().getTimeInMillis());

        long id = db.insert(DBCore.TABELA_HORARIOS_EVENTO, null, cv);

        return id;
    }

    public HorarioEvento getHorarioEvento(long _id) {
        HorarioEvento horarioEvento = new HorarioEvento();
        String[] colunas = {DBCore.COLUNA_HORARIOS_EVENTOS_ID, DBCore.COLUNA_HORARIOS_EVENTOS_BLOCO, DBCore.COLUNA_HORARIOS_EVENTOS_SALA, DBCore.COLUNA_HORARIOS_EVENTOS_DATA};

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBCore.TABELA_HORARIOS_EVENTO + " WHERE " + DBCore.COLUNA_HORARIOS_EVENTOS_ID + " = ?",
                new String[]{_id+""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            horarioEvento.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_EVENTOS_ID)));
            horarioEvento.setBloco(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_EVENTOS_BLOCO)));
            horarioEvento.setSala(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_EVENTOS_SALA)));

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTime(new Date(Long.valueOf(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_HORARIOS_EVENTOS_DATA)))));

            horarioEvento.setData(calendar);
        }
        cursor.close();
        return horarioEvento;
    }
}
