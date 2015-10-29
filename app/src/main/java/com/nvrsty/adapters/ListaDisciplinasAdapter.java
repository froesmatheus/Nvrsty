package com.nvrsty.adapters;

/**
 * Created by Matheus on 29/09/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nvrsty.R;
import com.nvrsty.db.DB;
import com.nvrsty.models.Disciplina;


public class ListaDisciplinasAdapter extends CursorAdapter {
    Context context;

    public ListaDisciplinasAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lista_materias_view, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Disciplina disciplina = new Disciplina();

        disciplina.setNomeDisciplina(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_DISCIPLINA)));
        disciplina.setNomeProfessor(cursor.getString(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_NOME_PROFESSOR)));
        disciplina.setCor(cursor.getInt(cursor.getColumnIndex(DB.COLUNA_DISCIPLINAS_COR)));

        TextView nomeMateria, nomeProfessor;
        View corHex;
        nomeMateria = (TextView) view.findViewById(R.id.tv_nome_materia);
        nomeProfessor = (TextView) view.findViewById(R.id.tv_nome_professor);
        corHex = view.findViewById(R.id.v_cor_hex);

        nomeMateria.setText(disciplina.getNomeDisciplina());
        nomeProfessor.setText(disciplina.getNomeProfessor());
        corHex.setBackgroundColor(disciplina.getCor());
    }
}
