package com.unab.copaamerica.model;

public class Country {
    private String nombre;
    private String codigo;
    private String bandera;
    private String codigoApi;
    private String id;

    public Country(String nombre, String codigo, String bandera, String codigoApi, String id) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.bandera = bandera;
        this.codigoApi = codigoApi;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getBandera() {
        return bandera;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }

    public String getCodigoApi() {
        return codigoApi;
    }

    public void setCodigoApi(String codigoApi) {
        this.codigoApi = codigoApi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
