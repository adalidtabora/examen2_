package com.example.tlenguajes2023.configuracion;

import static com.example.tlenguajes2023.configuracion.ConfigDB.descripcion;

public class personas
{
    private Integer id;
    private String nombres;

    private String descripcion;
    private String foto;


    public personas()
    {
    }

    public personas(Integer id, String nombres, String descripcion, String foto) {
        this.id = id;
        this.nombres = nombres;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDescripcion() { return descripcion;}

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
