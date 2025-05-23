/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "tratamiento")
@NamedQueries({
    @NamedQuery(name = "Tratamiento.findAll", query = "SELECT t FROM Tratamiento t"),
    @NamedQuery(name = "Tratamiento.findByIdTratamiento", query = "SELECT t FROM Tratamiento t WHERE t.idTratamiento = :idTratamiento"),
    @NamedQuery(name = "Tratamiento.findByDiagnostico", query = "SELECT t FROM Tratamiento t WHERE t.diagnostico = :diagnostico"),
    @NamedQuery(name = "Tratamiento.findByIndicaciones", query = "SELECT t FROM Tratamiento t WHERE t.indicaciones = :indicaciones"),
    @NamedQuery(name = "Tratamiento.findByDuracion", query = "SELECT t FROM Tratamiento t WHERE t.duracion = :duracion"),
    @NamedQuery(name = "Tratamiento.findByCreatedAt", query = "SELECT t FROM Tratamiento t WHERE t.createdAt = :createdAt"),
    @NamedQuery(name = "Tratamiento.findByUpdatedAt", query = "SELECT t FROM Tratamiento t WHERE t.updatedAt = :updatedAt")})
public class Tratamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tratamiento")
    private Integer idTratamiento;
    @Basic(optional = false)
    @Column(name = "diagnostico")
    private String diagnostico;
    @Column(name = "indicaciones")
    private String indicaciones;
    @Column(name = "duracion")
    private String duracion;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idTratamiento")
    private RecetaMedica recetaMedica;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @OneToOne(optional = false)
    private CitaMedica idCita;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    @ManyToOne(optional = false)
    private Medico idMedico;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;

    public Tratamiento() {
    }

    public Tratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public Tratamiento(Integer idTratamiento, String diagnostico) {
        this.idTratamiento = idTratamiento;
        this.diagnostico = diagnostico;
    }

    public Integer getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
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

    public RecetaMedica getRecetaMedica() {
        return recetaMedica;
    }

    public void setRecetaMedica(RecetaMedica recetaMedica) {
        this.recetaMedica = recetaMedica;
    }

    public CitaMedica getIdCita() {
        return idCita;
    }

    public void setIdCita(CitaMedica idCita) {
        this.idCita = idCita;
    }

    public Medico getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Medico idMedico) {
        this.idMedico = idMedico;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTratamiento != null ? idTratamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tratamiento)) {
            return false;
        }
        Tratamiento other = (Tratamiento) object;
        if ((this.idTratamiento == null && other.idTratamiento != null) || (this.idTratamiento != null && !this.idTratamiento.equals(other.idTratamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idTratamiento + " > " + duracion;
    }
    
}
