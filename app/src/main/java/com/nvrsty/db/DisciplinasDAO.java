package com.nvrsty.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nvrsty.models.Disciplina;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matheus on 02/10/2015.
 */
public class DisciplinasDAO {
    private DB dbCore;
    private SQLiteDatabase db;
    private HorariosDAO horariosDAO;

    public DisciplinasDAO(Context context) {
        dbCore = new DB(context);
        db = dbCore.getWritableDatabase();
        horariosDAO = new HorariosDAO(context);
    }

    public boolean inserir(Disciplina disciplina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, disciplina.getNomeDisciplina());
        contentValues.put(DB.COLUNA_DISCIPLINAS_SIGLA, disciplina.getSigla());
        contentValues.put(DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR, disciplina.getNomeProfessor());
        contentValues.put(DB.COLUNA_DISCIPLINAS_COR, disciplina.getCor());
        contentValues.put(DB.COLUNA_DISCIPLINAS_UNIDADES, disciplina.getQtdUnidades());
        contentValues.put(DB.COLUNA_DISCIPLINAS_FREQUENCIA, disciplina.getFrequencia());

        long rowID = db.insert(DB.TABELA_DISCIPLINAS, null, contentValues);


        // Inserir os horários da matéria
        horariosDAO.inserirHorarios(rowID, disciplina.getListaHorarios());

        return rowID != -1;
    }


    public boolean remover(int _id) {
        int status = db.delete(DB.TABELA_DISCIPLINAS, "_id = ?", new String[]{_id + ""});

        horariosDAO.remover(_id);
        return status != 0;
    }

    public boolean atualizar(Disciplina disciplina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, disciplina.getNomeDisciplina());
        contentValues.put(DB.COLUNA_DISCIPLINAS_SIGLA, disciplina.getSigla());
        contentValues.put(DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR, disciplina.getNomeProfessor());
        contentValues.put(DB.COLUNA_DISCIPLINAS_FREQUENCIA, disciplina.getFrequencia());
        contentValues.put(DB.COLUNA_DISCIPLINAS_UNIDADES, disciplina.getQtdUnidades());
        contentValues.put(DB.COLUNA_DISCIPLINAS_COR, disciplina.getCor());

        int status = db.update(DB.TABELA_DISCIPLINAS, contentValues, "_id = ?", new String[]{disciplina.getId() + ""});

        return status > 0;
    }

    public List<Disciplina> listarDisciplinas() {
        List<Disciplina> list = new ArrayList<>();
        String[] colunas = {"_id", DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, DB.COLUNA_DISCIPLINAS_SIGLA, DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR,
                DB.COLUNA_DISCIPLINAS_COR, DB.COLUNA_DISCIPLINAS_FREQUENCIA, DB.COLUNA_DISCIPLINAS_UNIDADES};

        Cursor cursor = db.query(DB.TABELA_DISCIPLINAS, colunas, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Disciplina disciplina = new Disciplina();

                disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA)));
                disciplina.setNomeProfessor(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR)));
                disciplina.setSigla(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_SIGLA)));
                disciplina.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_ID)));
                disciplina.setCor(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_COR)));
                disciplina.setFrequencia(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_FREQUENCIA)));
                disciplina.setQtdUnidades(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_UNIDADES)));
                disciplina.setListaHorarios(horariosDAO.getListaHorarios(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_ID))));

                list.add(disciplina);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public Cursor getDisciplinasCursor() {
        String[] colunas = {"_id", DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, DB.COLUNA_DISCIPLINAS_SIGLA,
                DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR, DB.COLUNA_DISCIPLINAS_FREQUENCIA,
                DB.COLUNA_DISCIPLINAS_COR, DB.COLUNA_DISCIPLINAS_UNIDADES};

        return db.query(DB.TABELA_DISCIPLINAS, colunas, null, null, null, null, null, null);
    }

    public Disciplina getDisciplina(long _id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABELA_DISCIPLINAS + " WHERE " + DB.COLUNA_DISCIPLINAS_ID + " = ?", new String[]{_id + ""});


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Disciplina disciplina = new Disciplina();

            disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA)));
            disciplina.setNomeProfessor(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR)));
            disciplina.setSigla(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_SIGLA)));
            disciplina.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_ID)));
            disciplina.setCor(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_COR)));
            disciplina.setFrequencia(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_FREQUENCIA)));
            disciplina.setQtdUnidades(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_UNIDADES)));
            disciplina.setListaHorarios(horariosDAO.getListaHorarios(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_ID))));

            cursor.close();
            return disciplina;
        }

        cursor.close();
        return null;
    }
}
