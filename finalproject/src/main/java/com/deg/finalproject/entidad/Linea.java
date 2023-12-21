package com.deg.finalproject.entidad;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the linea database table.
 *
 */
@Entity
@Table(name="linea")
@NamedQuery(name="Linea.findAll", query="SELECT l FROM Linea l")
public class Linea implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer lineaid;

    private Integer cantidad;

    private String descripcion;

    private BigDecimal precio;

    //bi-directional many-to-one association to Comprobante
    @ManyToOne
    @JoinColumn(name="comprobanteid")
    private com.deg.finalproject.entidad.Comprobante comprobante;

    //bi-directional many-to-one association to Producto
    @ManyToOne
    @JoinColumn(name="productoid")
    private Producto producto;

    public Linea() {
    }

    public Integer getLineaid() {
        return this.lineaid;
    }

    public void setLineaid(Integer lineaId) {
        this.lineaid = lineaId;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public com.deg.finalproject.entidad.Comprobante getComprobante() {
        return this.comprobante;
    }

    public void setComprobante(com.deg.finalproject.entidad.Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Linea [lineaid=").append(lineaid).append(", cantidad=").append(cantidad).append(", ");
        if (descripcion != null)
            builder.append("descripcion=").append(descripcion).append(", ");
        if (precio != null)
            builder.append("precio=").append(precio);
        builder.append("]");
        return builder.toString();
    }
}