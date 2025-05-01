/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "receta_medicamento")
@NamedQueries({
    @NamedQuery(name = "RecetaMedicamento.findAll", query = "SELECT r FROM RecetaMedicamento r"),
    @NamedQuery(name = "RecetaMedicamento.findByIdRecetaMedicamento", query = "SELECT r FROM RecetaMedicamento r WHERE r.idRecetaMedicamento = :idRecetaMedicamento"),
    @NamedQuery(name = "RecetaMedicamento.findByDosis", query = "SELECT r FROM RecetaMedicamento r WHERE r.dosis = :dosis"),
    @NamedQuery(name = "RecetaMedicamento.findByFrecuencia", query = "SELECT r FROM RecetaMedicamento r WHERE r.frecuencia = :frecuencia"),
    @NamedQuery(name = "RecetaMedicamento.findByDuracion", query = "SELECT r FROM RecetaMedicamento r WHERE r.duracion = :duracion"),
    @NamedQuery(name = "RecetaMedicamento.findByCantidad", query = "SELECT r FROM RecetaMedicamento r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "RecetaMedicamento.findByCreatedAt", query = "SELECT r FROM RecetaMedicamento r WHERE r.createdAt = :createdAt")})
public class RecetaMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_receta_medicamento")
    private Integer idRecetaMedicamento;
    @Basic(optional = false)
    @Column(name = "dosis")
    private String dosis;
    @Basic(optional = false)
    @Column(name = "frecuencia")
    private String frecuencia;
    @Basic(optional = false)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "id_medicamento", referencedColumnName = "id_medicamento")
    @ManyToOne(optional = false)
    private Medicamento idMedicamento;
    @JoinColumn(name = "id_receta", referencedColumnName = "id_receta")
    @ManyToOne(optional = false)
    private RecetaMedica idReceta;

    public RecetaMedicamento() {
    }

    public RecetaMedicamento(Integer idRecetaMedicamento) {
        this.idRecetaMedicamento = idRecetaMedicamento;
    }

    public RecetaMedicamento(Integer idRecetaMedicamento, String dosis, String frecuencia, String duracion, int cantidad) {
        this.idRecetaMedicamento = idRecetaMedicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.cantidad = cantidad;
    }

    public Integer getIdRecetaMedicamento() {
        return idRecetaMedicamento;
    }

    public void setIdRecetaMedicamento(Integer idRecetaMedicamento) {
        this.idRecetaMedicamento = idRecetaMedicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Medicamento getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Medicamento idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public RecetaMedica getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(RecetaMedica idReceta) {
        this.idReceta = idReceta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecetaMedicamento != null ? idRecetaMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecetaMedicamento)) {
            return false;
        }
        RecetaMedicamento other = (RecetaMedicamento) object;
        if ((this.idRecetaMedicamento == null && other.idRecetaMedicamento != null) || (this.idRecetaMedicamento != null && !this.idRecetaMedicamento.equals(other.idRecetaMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.RecetaMedicamento[ idRecetaMedicamento=" + idRecetaMedicamento + " ]";
    }
    
}
