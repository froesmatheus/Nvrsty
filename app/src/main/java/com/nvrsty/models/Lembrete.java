package com.nvrsty.models;

import java.util.Calendar;

/**
 * Created by mathe on 06/11/2015.
 */
public class Lembrete {
    private String titulo;
    private String corpo;
    private Calendar horario;

    public Lembrete(String titulo, String corpo, Calendar horario) {
        this.titulo = titulo;
        this.corpo = corpo;
        this.horario = horario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Calendar getHorario() {
        return horario;
    }

    public void setHorario(Calendar horario) {
        this.horario = horario;
    }
}
