/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "medicamento")
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m"),
    @NamedQuery(name = "Medicamento.findByIdMedicamento", query = "SELECT m FROM Medicamento m WHERE m.idMedicamento = :idMedicamento"),
    @NamedQuery(name = "Medicamento.findByNombre", query = "SELECT m FROM Medicamento m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Medicamento.findByDescripcion", query = "SELECT m FROM Medicamento m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Medicamento.findByPresentacion", query = "SELECT m FROM Medicamento m WHERE m.presentacion = :presentacion"),
    @NamedQuery(name = "Medicamento.findByDosisRecomendada", query = "SELECT m FROM Medicamento m WHERE m.dosisRecomendada = :dosisRecomendada"),
    @NamedQuery(name = "Medicamento.findByContraindicaciones", query = "SELECT m FROM Medicamento m WHERE m.contraindicaciones = :contraindicaciones"),
    @NamedQuery(name = "Medicamento.findByCreatedAt", query = "SELECT m FROM Medicamento m WHERE m.createdAt = :createdAt"),
    @NamedQuery(name = "Medicamento.findByUpdatedAt", query = "SELECT m FROM Medicamento m WHERE m.updatedAt = :updatedAt")})
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medicamento")
    private Integer idMedicamento;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "presentacion")
    private String presentacion;
    @Column(name = "dosis_recomendada")
    private String dosisRecomendada;
    @Column(name = "contraindicaciones")
    private String contraindicaciones;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMedicamento")
    private List<RecetaMedicamento> recetaMedicamentoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idMedicamento")
    private Inventario inventario;

    public Medicamento() {
    }

    public Medicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Medicamento(Integer idMedicamento, String nombre) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getDosisRecomendada() {
        return dosisRecomendada;
    }

    public void setDosisRecomendada(String dosisRecomendada) {
        this.dosisRecomendada = dosisRecomendada;
    }

    public String getContraindicaciones() {
        return contraindicaciones;
    }

    public void setContraindicaciones(String contraindicaciones) {
        this.contraindicaciones = contraindicaciones;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RecetaMedicamento> getRecetaMedicamentoList() {
        return recetaMedicamentoList;
    }

    public void setRecetaMedicamentoList(List<RecetaMedicamento> recetaMedicamentoList) {
        this.recetaMedicamentoList = recetaMedicamentoList;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedicamento != null ? idMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.idMedicamento == null && other.idMedicamento != null) || (this.idMedicamento != null && !this.idMedicamento.equals(other.idMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Medicamento[ idMedicamento=" + idMedicamento + " ]";
    }
    
}
