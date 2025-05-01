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
@Table(name = "consulta_diagnostico")
@NamedQueries({
    @NamedQuery(name = "ConsultaDiagnostico.findAll", query = "SELECT c FROM ConsultaDiagnostico c"),
    @NamedQuery(name = "ConsultaDiagnostico.findByIdConsultaDiagnostico", query = "SELECT c FROM ConsultaDiagnostico c WHERE c.idConsultaDiagnostico = :idConsultaDiagnostico"),
    @NamedQuery(name = "ConsultaDiagnostico.findByCodigoCie", query = "SELECT c FROM ConsultaDiagnostico c WHERE c.codigoCie = :codigoCie"),
    @NamedQuery(name = "ConsultaDiagnostico.findByDiagnostico", query = "SELECT c FROM ConsultaDiagnostico c WHERE c.diagnostico = :diagnostico"),
    @NamedQuery(name = "ConsultaDiagnostico.findByTipo", query = "SELECT c FROM ConsultaDiagnostico c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "ConsultaDiagnostico.findByCreatedAt", query = "SELECT c FROM ConsultaDiagnostico c WHERE c.createdAt = :createdAt")})
public class ConsultaDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consulta_diagnostico")
    private Integer idConsultaDiagnostico;
    @Column(name = "codigo_cie")
    private String codigoCie;
    @Basic(optional = false)
    @Column(name = "diagnostico")
    private String diagnostico;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "id_consulta", referencedColumnName = "id_consulta")
    @ManyToOne(optional = false)
    private Consulta idConsulta;

    public ConsultaDiagnostico() {
    }

    public ConsultaDiagnostico(Integer idConsultaDiagnostico) {
        this.idConsultaDiagnostico = idConsultaDiagnostico;
    }

    public ConsultaDiagnostico(Integer idConsultaDiagnostico, String diagnostico) {
        this.idConsultaDiagnostico = idConsultaDiagnostico;
        this.diagnostico = diagnostico;
    }

    public Integer getIdConsultaDiagnostico() {
        return idConsultaDiagnostico;
    }

    public void setIdConsultaDiagnostico(Integer idConsultaDiagnostico) {
        this.idConsultaDiagnostico = idConsultaDiagnostico;
    }

    public String getCodigoCie() {
        return codigoCie;
    }

    public void setCodigoCie(String codigoCie) {
        this.codigoCie = codigoCie;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Consulta getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Consulta idConsulta) {
        this.idConsulta = idConsulta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsultaDiagnostico != null ? idConsultaDiagnostico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultaDiagnostico)) {
            return false;
        }
        ConsultaDiagnostico other = (ConsultaDiagnostico) object;
        if ((this.idConsultaDiagnostico == null && other.idConsultaDiagnostico != null) || (this.idConsultaDiagnostico != null && !this.idConsultaDiagnostico.equals(other.idConsultaDiagnostico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.ConsultaDiagnostico[ idConsultaDiagnostico=" + idConsultaDiagnostico + " ]";
    }
    
}
