package com.nvrsty.models;

/**
 * Created by Matheus on 29/09/2015.
 */

import java.io.Serializable;
import java.util.Calendar;

public class HorarioEvento implements Serializable {
    private int id;
    private Calendar data;
    private String bloco;
    private String sala;

    public HorarioEvento() {
        data = Calendar.getInstance();
    }

    public Calendar getData() {
        return this.data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
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
