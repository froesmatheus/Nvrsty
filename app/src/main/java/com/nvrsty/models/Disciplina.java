package com.nvrsty.models;

/**
 * Created by Matheus on 29/09/2015.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Disciplina implements Serializable {
    private int id;
    private String nomeDisciplina;
    private String nomeProfessor;
    private String sigla;
    private int frequencia;
    private int cor;
    private int qtUnidades;
    private List<HorarioAula> listaHorarios;
    private List<Evento> listaEventos;

    public Disciplina(String nomeDisciplina, String nomeProfessor, String sigla, int frequencia, int cor, int qtUnidades, List<HorarioAula> listaHorarios) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.sigla = sigla;
        this.frequencia = frequencia;
        this.cor = cor;
        this.qtUnidades = qtUnidades;
        this.listaHorarios = listaHorarios;
    }

    public Disciplina() {
        listaHorarios = new ArrayList<>();
    }

    public Disciplina(String nomeDisciplina, String nomeProfessor, int cor,
                      List<HorarioAula> listaHorarios) {
        this();
        this.listaHorarios = listaHorarios;
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtUnidades() {
        return qtUnidades;
    }

    public void setQtUnidades(int qtUnidades) {
        this.qtUnidades = qtUnidades;
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

    public Disciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Disciplina(String nomeDisciplina, String nomeProfessor, int cor) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.cor = cor;
    }

    public Disciplina(String nomeDisciplina, String nomeProfessor) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
    }

    // Métodos

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
