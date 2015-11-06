package com.nvrsty.models;

/**
 * Created by mathe on 06/11/2015.
 */
public class HorarioLembrete {
    private String dia, hora;

    public HorarioLembrete(String dia, String hora) {
        this.dia = dia;
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
