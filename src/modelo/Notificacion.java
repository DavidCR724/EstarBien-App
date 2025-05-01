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
@Table(name = "notificacion")
@NamedQueries({
    @NamedQuery(name = "Notificacion.findAll", query = "SELECT n FROM Notificacion n"),
    @NamedQuery(name = "Notificacion.findByIdNotificacion", query = "SELECT n FROM Notificacion n WHERE n.idNotificacion = :idNotificacion"),
    @NamedQuery(name = "Notificacion.findByTipo", query = "SELECT n FROM Notificacion n WHERE n.tipo = :tipo"),
    @NamedQuery(name = "Notificacion.findByMensaje", query = "SELECT n FROM Notificacion n WHERE n.mensaje = :mensaje"),
    @NamedQuery(name = "Notificacion.findByFechaEnvio", query = "SELECT n FROM Notificacion n WHERE n.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "Notificacion.findByFechaLectura", query = "SELECT n FROM Notificacion n WHERE n.fechaLectura = :fechaLectura"),
    @NamedQuery(name = "Notificacion.findByEstatus", query = "SELECT n FROM Notificacion n WHERE n.estatus = :estatus"),
    @NamedQuery(name = "Notificacion.findByIdReferencia", query = "SELECT n FROM Notificacion n WHERE n.idReferencia = :idReferencia"),
    @NamedQuery(name = "Notificacion.findByCreatedAt", query = "SELECT n FROM Notificacion n WHERE n.createdAt = :createdAt")})
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "mensaje")
    private String mensaje;
    @Basic(optional = false)
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Column(name = "fecha_lectura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLectura;
    @Basic(optional = false)
    @Column(name = "estatus")
    private String estatus;
    @Column(name = "id_referencia")
    private Integer idReferencia;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Notificacion() {
    }

    public Notificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Notificacion(Integer idNotificacion, String tipo, String mensaje, Date fechaEnvio, String estatus) {
        this.idNotificacion = idNotificacion;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.estatus = estatus;
    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(Date fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNotificacion != null ? idNotificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.idNotificacion == null && other.idNotificacion != null) || (this.idNotificacion != null && !this.idNotificacion.equals(other.idNotificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Notificacion[ idNotificacion=" + idNotificacion + " ]";
    }
    
}
