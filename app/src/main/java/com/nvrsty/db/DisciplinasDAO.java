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
    private DBCore dbCore;
    private SQLiteDatabase db;
    private HorariosDAO horariosDAO;

    public DisciplinasDAO(Context context) {
        dbCore = new DBCore(context);
        db = dbCore.getWritableDatabase();
        horariosDAO = new HorariosDAO(context);
    }

    public boolean inserir(Disciplina disciplina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, disciplina.getNomeDisciplina());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_SIGLA, disciplina.getSigla());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR, disciplina.getNomeProfessor());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_COR, disciplina.getCor());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_UNIDADES, disciplina.getQtUnidades());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_FREQUENCIA, disciplina.getFrequencia());

        long rowID = db.insert(DBCore.TABELA_DISCIPLINAS, null, contentValues);


        // Inserir os horários da matéria
        horariosDAO.inserirHorarios(rowID, disciplina.getListaHorarios());

        return rowID != -1;
    }


    public boolean remover(int _id) {
        int status = db.delete(DBCore.TABELA_DISCIPLINAS, "_id = ?", new String[]{_id + ""});

        horariosDAO.remover(_id);
        return status != 0;
    }

    public boolean atualizar(Disciplina disciplina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, disciplina.getNomeDisciplina());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_SIGLA, disciplina.getSigla());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR, disciplina.getNomeProfessor());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_FREQUENCIA, disciplina.getFrequencia());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_UNIDADES, disciplina.getQtUnidades());
        contentValues.put(DBCore.COLUNA_DISCIPLINAS_COR, disciplina.getCor());

        int status = db.update(DBCore.TABELA_DISCIPLINAS, contentValues, "_id = ?", new String[]{disciplina.getId() + ""});

        return status > 0;
    }

    public List<Disciplina> listarDisciplinas() {
        List<Disciplina> list = new ArrayList<>();
        String[] colunas = {"_id", DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, DBCore.COLUNA_DISCIPLINAS_SIGLA, DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR,
                DBCore.COLUNA_DISCIPLINAS_COR, DBCore.COLUNA_DISCIPLINAS_FREQUENCIA, DBCore.COLUNA_DISCIPLINAS_UNIDADES};

        Cursor cursor = db.query(DBCore.TABELA_DISCIPLINAS, colunas, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Disciplina disciplina = new Disciplina();

                disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA)));
                disciplina.setNomeProfessor(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR)));
                disciplina.setSigla(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_SIGLA)));
                disciplina.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_ID)));
                disciplina.setCor(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_COR)));
                disciplina.setFrequencia(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_FREQUENCIA)));
                disciplina.setQtUnidades(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_UNIDADES)));
                disciplina.setListaHorarios(horariosDAO.getListaHorarios(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_ID))));

                list.add(disciplina);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public Cursor getDisciplinasCursor() {
        String[] colunas = {"_id", DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA, DBCore.COLUNA_DISCIPLINAS_SIGLA,
                DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR, DBCore.COLUNA_DISCIPLINAS_FREQUENCIA,
                DBCore.COLUNA_DISCIPLINAS_COR, DBCore.COLUNA_DISCIPLINAS_UNIDADES};

        return db.query(DBCore.TABELA_DISCIPLINAS, colunas, null, null, null, null, null, null);
    }

    public Disciplina getDisciplina(long _id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBCore.TABELA_DISCIPLINAS + " WHERE " + DBCore.COLUNA_DISCIPLINAS_ID + " = ?", new String[]{_id + ""});


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Disciplina disciplina = new Disciplina();

            disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_NOME_DISCIPLINA)));
            disciplina.setNomeProfessor(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_NOME_PROFESSOR)));
            disciplina.setSigla(cursor.getString(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_SIGLA)));
            disciplina.setId(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_ID)));
            disciplina.setCor(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_COR)));
            disciplina.setFrequencia(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_FREQUENCIA)));
            disciplina.setQtUnidades(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_UNIDADES)));
            disciplina.setListaHorarios(horariosDAO.getListaHorarios(cursor.getInt(cursor.getColumnIndex(DBCore.COLUNA_DISCIPLINAS_ID))));

            cursor.close();
            return disciplina;
        }

        cursor.close();
        return null;
    }
}
