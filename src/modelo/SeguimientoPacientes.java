/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "seguimiento_pacientes")
@NamedQueries({
    @NamedQuery(name = "SeguimientoPacientes.findAll", query = "SELECT s FROM SeguimientoPacientes s"),
    @NamedQuery(name = "SeguimientoPacientes.findByIdPaciente", query = "SELECT s FROM SeguimientoPacientes s WHERE s.idPaciente = :idPaciente"),
    @NamedQuery(name = "SeguimientoPacientes.findByPaciente", query = "SELECT s FROM SeguimientoPacientes s WHERE s.paciente = :paciente"),
    @NamedQuery(name = "SeguimientoPacientes.findByTotalCitas", query = "SELECT s FROM SeguimientoPacientes s WHERE s.totalCitas = :totalCitas"),
    @NamedQuery(name = "SeguimientoPacientes.findByCitasCompletadas", query = "SELECT s FROM SeguimientoPacientes s WHERE s.citasCompletadas = :citasCompletadas"),
    @NamedQuery(name = "SeguimientoPacientes.findByUltimaCita", query = "SELECT s FROM SeguimientoPacientes s WHERE s.ultimaCita = :ultimaCita"),
    @NamedQuery(name = "SeguimientoPacientes.findByTratamientos", query = "SELECT s FROM SeguimientoPacientes s WHERE s.tratamientos = :tratamientos"),
    @NamedQuery(name = "SeguimientoPacientes.findByMedicamentosRecetados", query = "SELECT s FROM SeguimientoPacientes s WHERE s.medicamentosRecetados = :medicamentosRecetados"),
    @NamedQuery(name = "SeguimientoPacientes.findByTipoSangre", query = "SELECT s FROM SeguimientoPacientes s WHERE s.tipoSangre = :tipoSangre"),
    @NamedQuery(name = "SeguimientoPacientes.findByPeso", query = "SELECT s FROM SeguimientoPacientes s WHERE s.peso = :peso"),
    @NamedQuery(name = "SeguimientoPacientes.findByEstatura", query = "SELECT s FROM SeguimientoPacientes s WHERE s.estatura = :estatura")})
public class SeguimientoPacientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_paciente")
    private Integer idPaciente;
    @Column(name = "paciente")
    private String paciente;
    @Column(name = "total_citas")
    private BigInteger totalCitas;
    @Column(name = "citas_completadas")
    private BigInteger citasCompletadas;
    @Column(name = "ultima_cita")
    @Temporal(TemporalType.DATE)
    private Date ultimaCita;
    @Column(name = "tratamientos")
    private BigInteger tratamientos;
    @Column(name = "medicamentos_recetados")
    private BigInteger medicamentosRecetados;
    @Column(name = "tipo_sangre")
    private String tipoSangre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "estatura")
    private BigDecimal estatura;
    @Id
    private Long id;

    public SeguimientoPacientes() {
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public BigInteger getTotalCitas() {
        return totalCitas;
    }

    public void setTotalCitas(BigInteger totalCitas) {
        this.totalCitas = totalCitas;
    }

    public BigInteger getCitasCompletadas() {
        return citasCompletadas;
    }

    public void setCitasCompletadas(BigInteger citasCompletadas) {
        this.citasCompletadas = citasCompletadas;
    }

    public Date getUltimaCita() {
        return ultimaCita;
    }

    public void setUltimaCita(Date ultimaCita) {
        this.ultimaCita = ultimaCita;
    }

    public BigInteger getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(BigInteger tratamientos) {
        this.tratamientos = tratamientos;
    }

    public BigInteger getMedicamentosRecetados() {
        return medicamentosRecetados;
    }

    public void setMedicamentosRecetados(BigInteger medicamentosRecetados) {
        this.medicamentosRecetados = medicamentosRecetados;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
