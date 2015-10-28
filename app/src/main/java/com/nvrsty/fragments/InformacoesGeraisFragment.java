package com.nvrsty.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nvrsty.R;
import com.nvrsty.models.Disciplina;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matheus on 28/09/2015.
 */
public class InformacoesGeraisFragment extends Fragment implements View.OnClickListener {
    public int qtUnidades = 1, cor;
    public EditText cpUnidades, cpNomeDisciplina, cpNomeProfessor, cpSigla;
    public TextView tvNomeMateria, tvSigla;
    public CircleImageView seletorCor,  circleImageView;
    public Spinner spFrequencia;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.informacoes_gerais_fragment, null);

        cpUnidades = (EditText) view.findViewById(R.id.cp_unidades);
        cpUnidades.setText(String.valueOf(qtUnidades));

        tvNomeMateria = (TextView) view.findViewById(R.id.tv_nome_materia);
        tvSigla = (TextView) view.findViewById(R.id.tv_sigla);

        cpNomeDisciplina = (EditText) view.findViewById(R.id.cp_nome_materia);
        cpNomeProfessor = (EditText) view.findViewById(R.id.cp_nome_professor);
        cpSigla = (EditText) view.findViewById(R.id.cp_sigla);
        cpSigla.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(4) {
        }});

        seletorCor = (CircleImageView) view.findViewById(R.id.seletor_cor);

        circleImageView = new CircleImageView(getActivity());

        circleImageView.setBorderColor(Color.parseColor("#2196F3"));
        circleImageView.setBackgroundColor(Color.parseColor("#2196F3"));
        cor = circleImageView.getBorderColor();
        seletorCor.setImageDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
        seletorCor.setBorderColor(Color.parseColor("#2196F3"));

        seletorCor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                View view = getActivity().getLayoutInflater().inflate(R.layout.paleta_cores, null);
                builder.setTitle("Escolha uma cor");
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });

        // Quantidade de unidades na matéria
        ImageButton btnIncremento = (ImageButton) view.findViewById(R.id.btn_incremento);
        ImageButton btnDecremento = (ImageButton) view.findViewById(R.id.btn_decremento);
        btnIncremento.setOnClickListener(this);
        btnDecremento.setOnClickListener(this);
        // ----------------------------------

        // Spinner que controla a frequência da matéria
        spFrequencia = (Spinner) view.findViewById(R.id.sp_frequencia);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        adapter.addAll("Toda semana", "A cada 15 dias", "Mensalmente");

        spFrequencia.setAdapter(adapter);
        // ----------------------------------


        if (getActivity().getIntent().getSerializableExtra("disciplina") != null) {
            Disciplina disciplina = (Disciplina) getActivity().getIntent().getSerializableExtra("disciplina");

            cpNomeDisciplina.setText(disciplina.getNomeDisciplina());
            cpNomeDisciplina.setEnabled(false);
            cpNomeProfessor.setText(disciplina.getNomeProfessor());
            cpNomeProfessor.setEnabled(false);
            cpSigla.setText(disciplina.getSigla());
            cpSigla.setEnabled(false);
            qtUnidades = disciplina.getQtUnidades();
            cpUnidades.setText(String.valueOf(disciplina.getQtUnidades()));
            cpUnidades.setEnabled(false);
            seletorCor.setImageDrawable(new ColorDrawable(disciplina.getCor()));
            seletorCor.setEnabled(false);
            circleImageView.setImageDrawable(new ColorDrawable(disciplina.getCor()));
            spFrequencia.setSelection(disciplina.getFrequencia(), true);
            spFrequencia.setEnabled(false);
        }

        return view;
    }

    public void onColorSelected(View v) {
        circleImageView = (CircleImageView) v;
        seletorCor.setImageDrawable(new ColorDrawable(circleImageView.getBorderColor()));
        cor = circleImageView.getBorderColor();
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btn_incremento:
                if (qtUnidades < 9) {
                    qtUnidades++;
                    cpUnidades.setText(String.valueOf(qtUnidades));
                }
                break;
            case R.id.btn_decremento:
                if (qtUnidades > 1) {
                    qtUnidades--;
                    cpUnidades.setText(String.valueOf(qtUnidades));
                }
        }
    }
}
