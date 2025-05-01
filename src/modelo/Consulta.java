/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "consulta")
@NamedQueries({
    @NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c"),
    @NamedQuery(name = "Consulta.findByIdConsulta", query = "SELECT c FROM Consulta c WHERE c.idConsulta = :idConsulta"),
    @NamedQuery(name = "Consulta.findBySintomas", query = "SELECT c FROM Consulta c WHERE c.sintomas = :sintomas"),
    @NamedQuery(name = "Consulta.findByObservaciones", query = "SELECT c FROM Consulta c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "Consulta.findByPresionArterial", query = "SELECT c FROM Consulta c WHERE c.presionArterial = :presionArterial"),
    @NamedQuery(name = "Consulta.findByTemperatura", query = "SELECT c FROM Consulta c WHERE c.temperatura = :temperatura"),
    @NamedQuery(name = "Consulta.findByFrecuenciaCardiaca", query = "SELECT c FROM Consulta c WHERE c.frecuenciaCardiaca = :frecuenciaCardiaca"),
    @NamedQuery(name = "Consulta.findByPeso", query = "SELECT c FROM Consulta c WHERE c.peso = :peso"),
    @NamedQuery(name = "Consulta.findByEstatura", query = "SELECT c FROM Consulta c WHERE c.estatura = :estatura"),
    @NamedQuery(name = "Consulta.findByCreatedAt", query = "SELECT c FROM Consulta c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "Consulta.findByUpdatedAt", query = "SELECT c FROM Consulta c WHERE c.updatedAt = :updatedAt")})
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consulta")
    private Integer idConsulta;
    @Column(name = "sintomas")
    private String sintomas;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "presion_arterial")
    private String presionArterial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "temperatura")
    private BigDecimal temperatura;
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "estatura")
    private BigDecimal estatura;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConsulta")
    private List<ConsultaDiagnostico> consultaDiagnosticoList;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @OneToOne(optional = false)
    private CitaMedica idCita;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    @ManyToOne(optional = false)
    private Medico idMedico;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;

    public Consulta() {
    }

    public Consulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
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

    public List<ConsultaDiagnostico> getConsultaDiagnosticoList() {
        return consultaDiagnosticoList;
    }

    public void setConsultaDiagnosticoList(List<ConsultaDiagnostico> consultaDiagnosticoList) {
        this.consultaDiagnosticoList = consultaDiagnosticoList;
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
        hash += (idConsulta != null ? idConsulta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consulta)) {
            return false;
        }
        Consulta other = (Consulta) object;
        if ((this.idConsulta == null && other.idConsulta != null) || (this.idConsulta != null && !this.idConsulta.equals(other.idConsulta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Consulta[ idConsulta=" + idConsulta + " ]";
    }
    
}
