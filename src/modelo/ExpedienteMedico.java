/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "expediente_medico")
@NamedQueries({
    @NamedQuery(name = "ExpedienteMedico.findAll", query = "SELECT e FROM ExpedienteMedico e"),
    @NamedQuery(name = "ExpedienteMedico.findByIdExpediente", query = "SELECT e FROM ExpedienteMedico e WHERE e.idExpediente = :idExpediente"),
    @NamedQuery(name = "ExpedienteMedico.findByTipoSangre", query = "SELECT e FROM ExpedienteMedico e WHERE e.tipoSangre = :tipoSangre"),
    @NamedQuery(name = "ExpedienteMedico.findByPeso", query = "SELECT e FROM ExpedienteMedico e WHERE e.peso = :peso"),
    @NamedQuery(name = "ExpedienteMedico.findByEstatura", query = "SELECT e FROM ExpedienteMedico e WHERE e.estatura = :estatura"),
    @NamedQuery(name = "ExpedienteMedico.findByAntecedentes", query = "SELECT e FROM ExpedienteMedico e WHERE e.antecedentes = :antecedentes"),
    @NamedQuery(name = "ExpedienteMedico.findByCreatedAt", query = "SELECT e FROM ExpedienteMedico e WHERE e.createdAt = :createdAt"),
    @NamedQuery(name = "ExpedienteMedico.findByUpdatedAt", query = "SELECT e FROM ExpedienteMedico e WHERE e.updatedAt = :updatedAt")})
public class ExpedienteMedico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_expediente")
    private Integer idExpediente;
    @Column(name = "tipo_sangre")
    private String tipoSangre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "estatura")
    private BigDecimal estatura;
    @Column(name = "antecedentes")
    private String antecedentes;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @OneToOne(optional = false)
    private Paciente idPaciente;

    public ExpedienteMedico() {
    }

    public ExpedienteMedico(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
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

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
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

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExpediente != null ? idExpediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedienteMedico)) {
            return false;
        }
        ExpedienteMedico other = (ExpedienteMedico) object;
        if ((this.idExpediente == null && other.idExpediente != null) || (this.idExpediente != null && !this.idExpediente.equals(other.idExpediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.ExpedienteMedico[ idExpediente=" + idExpediente + " ]";
    }
    
}
