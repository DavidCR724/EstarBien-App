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
@Table(name = "reporte_tratamientos")
@NamedQueries({
    @NamedQuery(name = "ReporteTratamientos.findAll", query = "SELECT r FROM ReporteTratamientos r"),
    @NamedQuery(name = "ReporteTratamientos.findByIdTratamiento", query = "SELECT r FROM ReporteTratamientos r WHERE r.idTratamiento = :idTratamiento"),
    @NamedQuery(name = "ReporteTratamientos.findByPaciente", query = "SELECT r FROM ReporteTratamientos r WHERE r.paciente = :paciente"),
    @NamedQuery(name = "ReporteTratamientos.findByMedico", query = "SELECT r FROM ReporteTratamientos r WHERE r.medico = :medico"),
    @NamedQuery(name = "ReporteTratamientos.findByDiagnostico", query = "SELECT r FROM ReporteTratamientos r WHERE r.diagnostico = :diagnostico"),
    @NamedQuery(name = "ReporteTratamientos.findByFechaPrescripcion", query = "SELECT r FROM ReporteTratamientos r WHERE r.fechaPrescripcion = :fechaPrescripcion"),
    @NamedQuery(name = "ReporteTratamientos.findByCantidadMedicamentos", query = "SELECT r FROM ReporteTratamientos r WHERE r.cantidadMedicamentos = :cantidadMedicamentos"),
    @NamedQuery(name = "ReporteTratamientos.findByMedicamentos", query = "SELECT r FROM ReporteTratamientos r WHERE r.medicamentos = :medicamentos"),
    @NamedQuery(name = "ReporteTratamientos.findByTotalMedicamentos", query = "SELECT r FROM ReporteTratamientos r WHERE r.totalMedicamentos = :totalMedicamentos")})
public class ReporteTratamientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_tratamiento")
    private Integer idTratamiento;
    @Column(name = "paciente")
    private String paciente;
    @Column(name = "medico")
    private String medico;
    @Column(name = "diagnostico")
    private String diagnostico;
    @Column(name = "fecha_prescripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrescripcion;
    @Column(name = "cantidad_medicamentos")
    private BigInteger cantidadMedicamentos;
    @Column(name = "medicamentos")
    private String medicamentos;
    @Column(name = "total_medicamentos")
    private BigInteger totalMedicamentos;

    public ReporteTratamientos() {
    }

    public Integer getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Date getFechaPrescripcion() {
        return fechaPrescripcion;
    }

    public void setFechaPrescripcion(Date fechaPrescripcion) {
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public BigInteger getCantidadMedicamentos() {
        return cantidadMedicamentos;
    }

    public void setCantidadMedicamentos(BigInteger cantidadMedicamentos) {
        this.cantidadMedicamentos = cantidadMedicamentos;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public BigInteger getTotalMedicamentos() {
        return totalMedicamentos;
    }

    public void setTotalMedicamentos(BigInteger totalMedicamentos) {
        this.totalMedicamentos = totalMedicamentos;
    }
    
}
