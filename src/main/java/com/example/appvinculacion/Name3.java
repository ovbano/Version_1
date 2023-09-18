package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Name3 {

    private String codigo;
    private String codigo_persona;
    private String nombre;
    private String dir;
    private String comu;
    private String barr;
    private String junta;
    private String sexo;
    private String edad;
    private String lon;
    private String lat;
    private int estado;

    public Name3() {
    }

    public Name3(String codigo, String nombre, String dir, String comu, String barr, String junta, String sexo, String edad, String lon, String lat, int estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dir = dir;
        this.comu = comu;
        this.barr = barr;
        this.junta = junta;
        this.sexo = sexo;
        this.edad = edad;
        this.lon = lon;
        this.lat = lat;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getComu() {
        return comu;
    }

    public void setComu(String comu) {
        this.comu = comu;
    }

    public String getBarr() {
        return barr;
    }

    public void setBarr(String barr) {
        this.barr = barr;
    }

    public String getJunta() {
        return junta;
    }

    public void setJunta(String junta) {
        this.junta = junta;
    }
}

