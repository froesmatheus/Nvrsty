package com.nvrsty.models;

/**
 * Created by Matheus on 28/09/2015.
 */

import java.io.Serializable;
import java.util.Calendar;

public class HorarioAula implements Serializable {
    private int diaSemana;
    private int id;
    private int idDisciplinaRelacionada;
    private Calendar horaInicio;
    private Calendar horaFim;
    private String bloco;
    private String sala;

    @Override
    public String toString() {
        return horaInicio.get(Calendar.HOUR_OF_DAY) + ":" + horaInicio.get(Calendar.MINUTE) + " " +
                "- " + horaFim.get(Calendar.HOUR_OF_DAY) + ":" + horaFim.get(Calendar.MINUTE);
    }

    public HorarioAula() {

    }

    public HorarioAula(Calendar horaInicio, Calendar horaFim, int diaSemana, String bloco, String sala) {
        this.horaInicio = horaInicio;
        this.diaSemana = diaSemana;
        this.horaFim = horaFim;
        this.bloco = bloco;
        this.sala = sala;
    }

    public int getIdDisciplinaRelacionada() {
        return idDisciplinaRelacionada;
    }

    public void setIdDisciplinaRelacionada(int idDisciplinaRelacionada) {
        this.idDisciplinaRelacionada = idDisciplinaRelacionada;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public Calendar getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Calendar horaFim) {
        this.horaFim = horaFim;
    }

    public Calendar getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Calendar horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}