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
        DB core = new DB(context);
        db = core.getWritableDatabase();
    }

    public long inserir(HorarioEvento horarioEvento) {
        ContentValues cv = new ContentValues();

        cv.put(DB.COLUNA_HORARIOS_EVENTOS_BLOCO, horarioEvento.getBloco());
        cv.put(DB.COLUNA_HORARIOS_EVENTOS_SALA, horarioEvento.getSala());
        cv.put(DB.COLUNA_HORARIOS_EVENTOS_DATA, horarioEvento.getData().getTimeInMillis());

        long id = db.insert(DB.TABELA_HORARIOS_EVENTO, null, cv);

        return id;
    }

    public boolean remover(long _id) {
        int status = db.delete(DB.TABELA_HORARIOS_EVENTO, "_id = ?", new String[]{_id + ""});

        return status != 0;
    }

    public HorarioEvento getHorarioEvento(long _id) {
        HorarioEvento horarioEvento = new HorarioEvento();
        String[] colunas = {DB.COLUNA_HORARIOS_EVENTOS_ID, DB.COLUNA_HORARIOS_EVENTOS_BLOCO, DB.COLUNA_HORARIOS_EVENTOS_SALA, DB.COLUNA_HORARIOS_EVENTOS_DATA};

        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABELA_HORARIOS_EVENTO + " WHERE " + DB.COLUNA_HORARIOS_EVENTOS_ID + " = ?",
                new String[]{_id+""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            horarioEvento.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_HORARIOS_EVENTOS_ID)));
            horarioEvento.setBloco(cursor.getString(cursor.getColumnIndex(DB.COLUNA_HORARIOS_EVENTOS_BLOCO)));
            horarioEvento.setSala(cursor.getString(cursor.getColumnIndex(DB.COLUNA_HORARIOS_EVENTOS_SALA)));

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTime(new Date(Long.valueOf(cursor.getString(cursor.getColumnIndex(DB.COLUNA_HORARIOS_EVENTOS_DATA)))));

            horarioEvento.setData(calendar);
        }
        cursor.close();
        return horarioEvento;
    }
}
