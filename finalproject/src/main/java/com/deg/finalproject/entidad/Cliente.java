package com.deg.finalproject.entidad;

import jakarta.persistence.*;

import java.io.Serializable;


/**
 * The persistent class for the cliente database table.
 *
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer clienteid;

    private String apellido;

    private Integer dni;

    private String nombre;

    public Cliente() {
    }

    public Integer getClienteid() {
        return this.clienteid;
    }

    public void setClienteid(Integer clienteid) {
        this.clienteid = clienteid;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return this.dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cliente [");
        if (clienteid != null)
            builder.append("clienteid=").append(clienteid).append(", ");
        if (dni != null)
            builder.append("dni=").append(dni);
        builder.append("]");
        return builder.toString();
    }

}