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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "receta_medica")
@NamedQueries({
    @NamedQuery(name = "RecetaMedica.findAll", query = "SELECT r FROM RecetaMedica r"),
    @NamedQuery(name = "RecetaMedica.findByIdReceta", query = "SELECT r FROM RecetaMedica r WHERE r.idReceta = :idReceta"),
    @NamedQuery(name = "RecetaMedica.findByFechaEmision", query = "SELECT r FROM RecetaMedica r WHERE r.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "RecetaMedica.findByInstrucciones", query = "SELECT r FROM RecetaMedica r WHERE r.instrucciones = :instrucciones"),
    @NamedQuery(name = "RecetaMedica.findByFirmaDigital", query = "SELECT r FROM RecetaMedica r WHERE r.firmaDigital = :firmaDigital"),
    @NamedQuery(name = "RecetaMedica.findByCreatedAt", query = "SELECT r FROM RecetaMedica r WHERE r.createdAt = :createdAt")})
public class RecetaMedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_receta")
    private Integer idReceta;
    @Basic(optional = false)
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Basic(optional = false)
    @Column(name = "instrucciones")
    private String instrucciones;
    @Column(name = "firma_digital")
    private String firmaDigital;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne(optional = false)
    private CitaMedica idCita;
    @JoinColumn(name = "id_tratamiento", referencedColumnName = "id_tratamiento")
    @OneToOne(optional = false)
    private Tratamiento idTratamiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReceta")
    private List<RecetaMedicamento> recetaMedicamentoList;

    public RecetaMedica() {
    }

    public RecetaMedica(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public RecetaMedica(Integer idReceta, Date fechaEmision, String instrucciones) {
        this.idReceta = idReceta;
        this.fechaEmision = fechaEmision;
        this.instrucciones = instrucciones;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public CitaMedica getIdCita() {
        return idCita;
    }

    public void setIdCita(CitaMedica idCita) {
        this.idCita = idCita;
    }

    public Tratamiento getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Tratamiento idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public List<RecetaMedicamento> getRecetaMedicamentoList() {
        return recetaMedicamentoList;
    }

    public void setRecetaMedicamentoList(List<RecetaMedicamento> recetaMedicamentoList) {
        this.recetaMedicamentoList = recetaMedicamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReceta != null ? idReceta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecetaMedica)) {
            return false;
        }
        RecetaMedica other = (RecetaMedica) object;
        if ((this.idReceta == null && other.idReceta != null) || (this.idReceta != null && !this.idReceta.equals(other.idReceta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.RecetaMedica[ idReceta=" + idReceta + " ]";
    }
    
}
