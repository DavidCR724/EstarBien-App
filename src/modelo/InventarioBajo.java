/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "inventario_bajo")
@NamedQueries({
    @NamedQuery(name = "InventarioBajo.findAll", query = "SELECT i FROM InventarioBajo i"),
    @NamedQuery(name = "InventarioBajo.findByIdMedicamento", query = "SELECT i FROM InventarioBajo i WHERE i.idMedicamento = :idMedicamento"),
    @NamedQuery(name = "InventarioBajo.findByNombre", query = "SELECT i FROM InventarioBajo i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "InventarioBajo.findByCantidad", query = "SELECT i FROM InventarioBajo i WHERE i.cantidad = :cantidad"),
    @NamedQuery(name = "InventarioBajo.findByDescripcion", query = "SELECT i FROM InventarioBajo i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "InventarioBajo.findByFechaCaducidad", query = "SELECT i FROM InventarioBajo i WHERE i.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "InventarioBajo.findByEstadoCaducidad", query = "SELECT i FROM InventarioBajo i WHERE i.estadoCaducidad = :estadoCaducidad"),
    @NamedQuery(name = "InventarioBajo.findByEstadoInventario", query = "SELECT i FROM InventarioBajo i WHERE i.estadoInventario = :estadoInventario"),
    @NamedQuery(name = "InventarioBajo.findByVecesRecetado", query = "SELECT i FROM InventarioBajo i WHERE i.vecesRecetado = :vecesRecetado")})
public class InventarioBajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_medicamento")
    private Integer idMedicamento;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Column(name = "estado_caducidad")
    private String estadoCaducidad;
    @Column(name = "estado_inventario")
    private String estadoInventario;
    @Column(name = "veces_recetado")
    private BigInteger vecesRecetado;

    public InventarioBajo() {
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getEstadoCaducidad() {
        return estadoCaducidad;
    }

    public void setEstadoCaducidad(String estadoCaducidad) {
        this.estadoCaducidad = estadoCaducidad;
    }

    public String getEstadoInventario() {
        return estadoInventario;
    }

    public void setEstadoInventario(String estadoInventario) {
        this.estadoInventario = estadoInventario;
    }

    public BigInteger getVecesRecetado() {
        return vecesRecetado;
    }

    public void setVecesRecetado(BigInteger vecesRecetado) {
        this.vecesRecetado = vecesRecetado;
    }
    
}
