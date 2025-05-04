/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.IllegalOrphanException;
import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Administrador;
import modelo.Paciente;
import modelo.Medico;
import modelo.Notificacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Usuario;

/**
 *
 * @author carlo
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getNotificacionList() == null) {
            usuario.setNotificacionList(new ArrayList<Notificacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrador administrador = usuario.getAdministrador();
            if (administrador != null) {
                administrador = em.getReference(administrador.getClass(), administrador.getIdAdmin());
                usuario.setAdministrador(administrador);
            }
            Paciente paciente = usuario.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getIdPaciente());
                usuario.setPaciente(paciente);
            }
            Medico medico = usuario.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getIdMedico());
                usuario.setMedico(medico);
            }
            List<Notificacion> attachedNotificacionList = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNotificacionToAttach : usuario.getNotificacionList()) {
                notificacionListNotificacionToAttach = em.getReference(notificacionListNotificacionToAttach.getClass(), notificacionListNotificacionToAttach.getIdNotificacion());
                attachedNotificacionList.add(notificacionListNotificacionToAttach);
            }
            usuario.setNotificacionList(attachedNotificacionList);
            em.persist(usuario);
            if (administrador != null) {
                Usuario oldIdUsuarioOfAdministrador = administrador.getIdUsuario();
                if (oldIdUsuarioOfAdministrador != null) {
                    oldIdUsuarioOfAdministrador.setAdministrador(null);
                    oldIdUsuarioOfAdministrador = em.merge(oldIdUsuarioOfAdministrador);
                }
                administrador.setIdUsuario(usuario);
                administrador = em.merge(administrador);
            }
            if (paciente != null) {
                Usuario oldIdUsuarioOfPaciente = paciente.getIdUsuario();
                if (oldIdUsuarioOfPaciente != null) {
                    oldIdUsuarioOfPaciente.setPaciente(null);
                    oldIdUsuarioOfPaciente = em.merge(oldIdUsuarioOfPaciente);
                }
                paciente.setIdUsuario(usuario);
                paciente = em.merge(paciente);
            }
            if (medico != null) {
                Usuario oldIdUsuarioOfMedico = medico.getIdUsuario();
                if (oldIdUsuarioOfMedico != null) {
                    oldIdUsuarioOfMedico.setMedico(null);
                    oldIdUsuarioOfMedico = em.merge(oldIdUsuarioOfMedico);
                }
                medico.setIdUsuario(usuario);
                medico = em.merge(medico);
            }
            for (Notificacion notificacionListNotificacion : usuario.getNotificacionList()) {
                Usuario oldIdUsuarioOfNotificacionListNotificacion = notificacionListNotificacion.getIdUsuario();
                notificacionListNotificacion.setIdUsuario(usuario);
                notificacionListNotificacion = em.merge(notificacionListNotificacion);
                if (oldIdUsuarioOfNotificacionListNotificacion != null) {
                    oldIdUsuarioOfNotificacionListNotificacion.getNotificacionList().remove(notificacionListNotificacion);
                    oldIdUsuarioOfNotificacionListNotificacion = em.merge(oldIdUsuarioOfNotificacionListNotificacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Administrador administradorOld = persistentUsuario.getAdministrador();
            Administrador administradorNew = usuario.getAdministrador();
            Paciente pacienteOld = persistentUsuario.getPaciente();
            Paciente pacienteNew = usuario.getPaciente();
            Medico medicoOld = persistentUsuario.getMedico();
            Medico medicoNew = usuario.getMedico();
            List<Notificacion> notificacionListOld = persistentUsuario.getNotificacionList();
            List<Notificacion> notificacionListNew = usuario.getNotificacionList();
            List<String> illegalOrphanMessages = null;
            for (Notificacion notificacionListOldNotificacion : notificacionListOld) {
                if (!notificacionListNew.contains(notificacionListOldNotificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notificacion " + notificacionListOldNotificacion + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (administradorNew != null) {
                administradorNew = em.getReference(administradorNew.getClass(), administradorNew.getIdAdmin());
                usuario.setAdministrador(administradorNew);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getIdPaciente());
                usuario.setPaciente(pacienteNew);
            }
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getIdMedico());
                usuario.setMedico(medicoNew);
            }
            List<Notificacion> attachedNotificacionListNew = new ArrayList<Notificacion>();
            for (Notificacion notificacionListNewNotificacionToAttach : notificacionListNew) {
                notificacionListNewNotificacionToAttach = em.getReference(notificacionListNewNotificacionToAttach.getClass(), notificacionListNewNotificacionToAttach.getIdNotificacion());
                attachedNotificacionListNew.add(notificacionListNewNotificacionToAttach);
            }
            notificacionListNew = attachedNotificacionListNew;
            usuario.setNotificacionList(notificacionListNew);
            usuario = em.merge(usuario);
            if (administradorOld != null && !administradorOld.equals(administradorNew)) {
                administradorOld.setIdUsuario(null);
                administradorOld = em.merge(administradorOld);
            }
            if (administradorNew != null && !administradorNew.equals(administradorOld)) {
                Usuario oldIdUsuarioOfAdministrador = administradorNew.getIdUsuario();
                if (oldIdUsuarioOfAdministrador != null) {
                    oldIdUsuarioOfAdministrador.setAdministrador(null);
                    oldIdUsuarioOfAdministrador = em.merge(oldIdUsuarioOfAdministrador);
                }
                administradorNew.setIdUsuario(usuario);
                administradorNew = em.merge(administradorNew);
            }
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.setIdUsuario(null);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                Usuario oldIdUsuarioOfPaciente = pacienteNew.getIdUsuario();
                if (oldIdUsuarioOfPaciente != null) {
                    oldIdUsuarioOfPaciente.setPaciente(null);
                    oldIdUsuarioOfPaciente = em.merge(oldIdUsuarioOfPaciente);
                }
                pacienteNew.setIdUsuario(usuario);
                pacienteNew = em.merge(pacienteNew);
            }
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.setIdUsuario(null);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                Usuario oldIdUsuarioOfMedico = medicoNew.getIdUsuario();
                if (oldIdUsuarioOfMedico != null) {
                    oldIdUsuarioOfMedico.setMedico(null);
                    oldIdUsuarioOfMedico = em.merge(oldIdUsuarioOfMedico);
                }
                medicoNew.setIdUsuario(usuario);
                medicoNew = em.merge(medicoNew);
            }
            for (Notificacion notificacionListNewNotificacion : notificacionListNew) {
                if (!notificacionListOld.contains(notificacionListNewNotificacion)) {
                    Usuario oldIdUsuarioOfNotificacionListNewNotificacion = notificacionListNewNotificacion.getIdUsuario();
                    notificacionListNewNotificacion.setIdUsuario(usuario);
                    notificacionListNewNotificacion = em.merge(notificacionListNewNotificacion);
                    if (oldIdUsuarioOfNotificacionListNewNotificacion != null && !oldIdUsuarioOfNotificacionListNewNotificacion.equals(usuario)) {
                        oldIdUsuarioOfNotificacionListNewNotificacion.getNotificacionList().remove(notificacionListNewNotificacion);
                        oldIdUsuarioOfNotificacionListNewNotificacion = em.merge(oldIdUsuarioOfNotificacionListNewNotificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Notificacion> notificacionListOrphanCheck = usuario.getNotificacionList();
            for (Notificacion notificacionListOrphanCheckNotificacion : notificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Notificacion " + notificacionListOrphanCheckNotificacion + " in its notificacionList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Administrador administrador = usuario.getAdministrador();
            if (administrador != null) {
                administrador.setIdUsuario(null);
                administrador = em.merge(administrador);
            }
            Paciente paciente = usuario.getPaciente();
            if (paciente != null) {
                paciente.setIdUsuario(null);
                paciente = em.merge(paciente);
            }
            Medico medico = usuario.getMedico();
            if (medico != null) {
                medico.setIdUsuario(null);
                medico = em.merge(medico);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
