package com.nvrsty.models;

/**
 * Created by Matheus on 29/09/2015.
 */

import java.io.Serializable;

public class Evento implements Serializable {
    public static final int REP_DE_AULA = 0;
    public static final int PROVA = 1;
    public static final int ATIVIDADE = 2;
    public static final int TRABALHO = 3;


    private int tipo;
    private int id;
    private String observacoes;
    private HorarioEvento horarioEvento;
    private Disciplina disciplinaRelacionada;

    public Evento() {
        horarioEvento = new HorarioEvento();
        disciplinaRelacionada = new Disciplina();
    }

    public Evento(int tipo, Disciplina disciplinaRelacionada) {
        this.tipo = tipo;
        this.disciplinaRelacionada = disciplinaRelacionada;
    }

    public HorarioEvento getHorarioEvento() {
        return horarioEvento;
    }

    public void setHorarioEvento(HorarioEvento horarioEvento) {
        this.horarioEvento = horarioEvento;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Disciplina getDisciplinaRelacionada() {
        return disciplinaRelacionada;
    }

    public void setDisciplinaRelacionada(Disciplina disciplinaRelacionada) {
        this.disciplinaRelacionada = disciplinaRelacionada;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
