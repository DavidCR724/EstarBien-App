/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "horario_medico")
@NamedQueries({
    @NamedQuery(name = "HorarioMedico.findAll", query = "SELECT h FROM HorarioMedico h"),
    @NamedQuery(name = "HorarioMedico.findByIdHorario", query = "SELECT h FROM HorarioMedico h WHERE h.idHorario = :idHorario"),
    @NamedQuery(name = "HorarioMedico.findByDiaSemana", query = "SELECT h FROM HorarioMedico h WHERE h.diaSemana = :diaSemana"),
    @NamedQuery(name = "HorarioMedico.findByHoraInicio", query = "SELECT h FROM HorarioMedico h WHERE h.horaInicio = :horaInicio"),
    @NamedQuery(name = "HorarioMedico.findByHoraFin", query = "SELECT h FROM HorarioMedico h WHERE h.horaFin = :horaFin"),
    @NamedQuery(name = "HorarioMedico.findByDisponible", query = "SELECT h FROM HorarioMedico h WHERE h.disponible = :disponible"),
    @NamedQuery(name = "HorarioMedico.findByCreatedAt", query = "SELECT h FROM HorarioMedico h WHERE h.createdAt = :createdAt"),
    @NamedQuery(name = "HorarioMedico.findByUpdatedAt", query = "SELECT h FROM HorarioMedico h WHERE h.updatedAt = :updatedAt")})
public class HorarioMedico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_horario")
    private Integer idHorario;
    @Basic(optional = false)
    @Column(name = "dia_semana")
    private String diaSemana;
    @Basic(optional = false)
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Basic(optional = false)
    @Column(name = "hora_fin")
    @Temporal(TemporalType.TIME)
    private Date horaFin;
    @Column(name = "disponible")
    private Boolean disponible;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    @ManyToOne(optional = false)
    private Medico idMedico;
    @OneToMany(mappedBy = "idHorario")
    private List<CitaMedica> citaMedicaList;

    public HorarioMedico() {
    }

    public HorarioMedico(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public HorarioMedico(Integer idHorario, String diaSemana, Date horaInicio, Date horaFin) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
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

    public Medico getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Medico idMedico) {
        this.idMedico = idMedico;
    }

    public List<CitaMedica> getCitaMedicaList() {
        return citaMedicaList;
    }

    public void setCitaMedicaList(List<CitaMedica> citaMedicaList) {
        this.citaMedicaList = citaMedicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorario != null ? idHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioMedico)) {
            return false;
        }
        HorarioMedico other = (HorarioMedico) object;
        if ((this.idHorario == null && other.idHorario != null) || (this.idHorario != null && !this.idHorario.equals(other.idHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.HorarioMedico[ idHorario=" + idHorario + " ]";
    }
    
}
