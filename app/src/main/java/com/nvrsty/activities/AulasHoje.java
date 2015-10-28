package com.nvrsty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.nvrsty.R;
import com.nvrsty.adapters.ListaHorariosAdapter;
import com.nvrsty.db.HorariosDAO;
import com.nvrsty.models.HorarioAula;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AulasHoje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas_hoje);

        ListView lvAulas = (ListView) findViewById(R.id.lv_aulas);

        HorariosDAO dao = new HorariosDAO(this);

        Calendar dataInicial = Calendar.getInstance(TimeZone.getDefault());
        dataInicial.set(0, 0, dataInicial.get(Calendar.DAY_OF_WEEK), 0, 0);

        Calendar dataFinal = Calendar.getInstance(TimeZone.getDefault());
        dataInicial.set(0, 0, dataFinal.get(Calendar.DAY_OF_WEEK), 23, 59);

        List<HorarioAula> listaHorarios = dao.filtrarHorariosPorData(dataInicial, dataFinal);

        Log.d("NVRSTY", listaHorarios.toString());
        ListaHorariosAdapter adapter = new ListaHorariosAdapter(this, listaHorarios);

        lvAulas.setAdapter(adapter);
    }
}
