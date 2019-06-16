package com.unab.copaamerica.model;

public class Match {
    private Country local;
    private Country visita;
    private String hora;
    private String fecha;

    public Match(Country local, Country visita, String hora, String fecha) {
        this.fecha = fecha;
        this.hora = hora;
        this.local = local;
        this.visita = visita;
    }

    public Country getLocal() {
        return local;
    }

    public void setLocal(Country local) {
        this.local = local;
    }

    public Country getVisita() {
        return visita;
    }

    public void setVisita(Country visita) {
        this.visita = visita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
