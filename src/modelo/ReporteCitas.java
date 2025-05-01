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
@Table(name = "reporte_citas")
@NamedQueries({
    @NamedQuery(name = "ReporteCitas.findAll", query = "SELECT r FROM ReporteCitas r"),
    @NamedQuery(name = "ReporteCitas.findByIdCita", query = "SELECT r FROM ReporteCitas r WHERE r.idCita = :idCita"),
    @NamedQuery(name = "ReporteCitas.findByPaciente", query = "SELECT r FROM ReporteCitas r WHERE r.paciente = :paciente"),
    @NamedQuery(name = "ReporteCitas.findByMedico", query = "SELECT r FROM ReporteCitas r WHERE r.medico = :medico"),
    @NamedQuery(name = "ReporteCitas.findByFecha", query = "SELECT r FROM ReporteCitas r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "ReporteCitas.findByHora", query = "SELECT r FROM ReporteCitas r WHERE r.hora = :hora"),
    @NamedQuery(name = "ReporteCitas.findByMotivo", query = "SELECT r FROM ReporteCitas r WHERE r.motivo = :motivo"),
    @NamedQuery(name = "ReporteCitas.findByEstatus", query = "SELECT r FROM ReporteCitas r WHERE r.estatus = :estatus"),
    @NamedQuery(name = "ReporteCitas.findByMontoPagado", query = "SELECT r FROM ReporteCitas r WHERE r.montoPagado = :montoPagado"),
    @NamedQuery(name = "ReporteCitas.findByCreatedAt", query = "SELECT r FROM ReporteCitas r WHERE r.createdAt = :createdAt"),
    @NamedQuery(name = "ReporteCitas.findByTieneTratamiento", query = "SELECT r FROM ReporteCitas r WHERE r.tieneTratamiento = :tieneTratamiento")})
public class ReporteCitas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_cita")
    private Integer idCita;
    @Column(name = "paciente")
    private String paciente;
    @Column(name = "medico")
    private String medico;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "estatus")
    private String estatus;
    @Column(name = "monto_pagado")
    private BigInteger montoPagado;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "tiene_tratamiento")
    private String tieneTratamiento;

    public ReporteCitas() {
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
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

    public BigInteger getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigInteger montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTieneTratamiento() {
        return tieneTratamiento;
    }

    public void setTieneTratamiento(String tieneTratamiento) {
        this.tieneTratamiento = tieneTratamiento;
    }
    
}
