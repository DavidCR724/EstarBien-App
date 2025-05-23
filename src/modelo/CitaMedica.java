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
@Table(name = "cita_medica")
@NamedQueries({
    @NamedQuery(name = "CitaMedica.findAll", query = "SELECT c FROM CitaMedica c"),
    @NamedQuery(name = "CitaMedica.findByIdCita", query = "SELECT c FROM CitaMedica c WHERE c.idCita = :idCita"),
    @NamedQuery(name = "CitaMedica.findByFecha", query = "SELECT c FROM CitaMedica c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CitaMedica.findByHora", query = "SELECT c FROM CitaMedica c WHERE c.hora = :hora"),
    @NamedQuery(name = "CitaMedica.findByMotivo", query = "SELECT c FROM CitaMedica c WHERE c.motivo = :motivo"),
    @NamedQuery(name = "CitaMedica.findByEstatus", query = "SELECT c FROM CitaMedica c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CitaMedica.findByNotas", query = "SELECT c FROM CitaMedica c WHERE c.notas = :notas"),
    @NamedQuery(name = "CitaMedica.findByCreatedAt", query = "SELECT c FROM CitaMedica c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "CitaMedica.findByUpdatedAt", query = "SELECT c FROM CitaMedica c WHERE c.updatedAt = :updatedAt")})
public class CitaMedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cita")
    private Integer idCita;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @Column(name = "motivo")
    private String motivo;
    @Basic(optional = false)
    @Column(name = "estatus")
    private String estatus;
    @Column(name = "notas")
    private String notas;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCita")
    private List<RecetaMedica> recetaMedicaList;
    @OneToOne(mappedBy = "idCita")
    private Pago pago;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idCita")
    private Tratamiento tratamiento;
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario")
    @ManyToOne
    private HorarioMedico idHorario;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    @ManyToOne(optional = false)
    private Medico idMedico;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne(optional = false)
    private Paciente idPaciente;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idCita")
    private Consulta consulta;

    public CitaMedica() {
    }

    public CitaMedica(Integer idCita) {
        this.idCita = idCita;
    }

    public CitaMedica(Integer idCita, Date fecha, Date hora, String motivo, String estatus) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estatus = estatus;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
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

    public List<RecetaMedica> getRecetaMedicaList() {
        return recetaMedicaList;
    }

    public void setRecetaMedicaList(List<RecetaMedica> recetaMedicaList) {
        this.recetaMedicaList = recetaMedicaList;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public HorarioMedico getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(HorarioMedico idHorario) {
        this.idHorario = idHorario;
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

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaMedica)) {
            return false;
        }
        CitaMedica other = (CitaMedica) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " paciente " + idPaciente;
    }
    
}
