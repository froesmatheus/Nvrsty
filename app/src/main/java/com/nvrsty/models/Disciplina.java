package com.nvrsty.models;

/**
 * Created by Matheus on 29/09/2015.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Disciplina implements Serializable {
    private int id, frequencia, cor, qtdUnidades;
    private String nomeDisciplina, nomeProfessor, sigla;
    private List<HorarioAula> listaHorarios;
    private List<Evento> listaEventos;

    public Disciplina(String nomeDisciplina, String nomeProfessor, String sigla, int frequencia,
                      int cor, int qtdUnidades, List<HorarioAula> listaHorarios) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.sigla = sigla;
        this.frequencia = frequencia;
        this.cor = cor;
        this.qtdUnidades = qtdUnidades;
        this.listaHorarios = listaHorarios;
    }

    public Disciplina() {
        listaHorarios = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtdUnidades() {
        return qtdUnidades;
    }

    public void setQtdUnidades(int qtdUnidades) {
        this.qtdUnidades = qtdUnidades;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }


    public List<HorarioAula> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<HorarioAula> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public List<Evento> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    @Override
    public String toString() {
        return this.getNomeDisciplina();
    }
}
