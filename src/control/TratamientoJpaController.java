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
import modelo.RecetaMedica;
import modelo.CitaMedica;
import modelo.Medico;
import modelo.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Tratamiento;

/**
 *
 * @author carlo
 */
public class TratamientoJpaController implements Serializable {

    public TratamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tratamiento tratamiento) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        CitaMedica idCitaOrphanCheck = tratamiento.getIdCita();
        if (idCitaOrphanCheck != null) {
            Tratamiento oldTratamientoOfIdCita = idCitaOrphanCheck.getTratamiento();
            if (oldTratamientoOfIdCita != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The CitaMedica " + idCitaOrphanCheck + " already has an item of type Tratamiento whose idCita column cannot be null. Please make another selection for the idCita field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecetaMedica recetaMedica = tratamiento.getRecetaMedica();
            if (recetaMedica != null) {
                recetaMedica = em.getReference(recetaMedica.getClass(), recetaMedica.getIdReceta());
                tratamiento.setRecetaMedica(recetaMedica);
            }
            CitaMedica idCita = tratamiento.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                tratamiento.setIdCita(idCita);
            }
            Medico idMedico = tratamiento.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                tratamiento.setIdMedico(idMedico);
            }
            Paciente idPaciente = tratamiento.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                tratamiento.setIdPaciente(idPaciente);
            }
            em.persist(tratamiento);
            if (recetaMedica != null) {
                Tratamiento oldIdTratamientoOfRecetaMedica = recetaMedica.getIdTratamiento();
                if (oldIdTratamientoOfRecetaMedica != null) {
                    oldIdTratamientoOfRecetaMedica.setRecetaMedica(null);
                    oldIdTratamientoOfRecetaMedica = em.merge(oldIdTratamientoOfRecetaMedica);
                }
                recetaMedica.setIdTratamiento(tratamiento);
                recetaMedica = em.merge(recetaMedica);
            }
            if (idCita != null) {
                idCita.setTratamiento(tratamiento);
                idCita = em.merge(idCita);
            }
            if (idMedico != null) {
                idMedico.getTratamientoList().add(tratamiento);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getTratamientoList().add(tratamiento);
                idPaciente = em.merge(idPaciente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tratamiento tratamiento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tratamiento persistentTratamiento = em.find(Tratamiento.class, tratamiento.getIdTratamiento());
            RecetaMedica recetaMedicaOld = persistentTratamiento.getRecetaMedica();
            RecetaMedica recetaMedicaNew = tratamiento.getRecetaMedica();
            CitaMedica idCitaOld = persistentTratamiento.getIdCita();
            CitaMedica idCitaNew = tratamiento.getIdCita();
            Medico idMedicoOld = persistentTratamiento.getIdMedico();
            Medico idMedicoNew = tratamiento.getIdMedico();
            Paciente idPacienteOld = persistentTratamiento.getIdPaciente();
            Paciente idPacienteNew = tratamiento.getIdPaciente();
            List<String> illegalOrphanMessages = null;
            if (recetaMedicaOld != null && !recetaMedicaOld.equals(recetaMedicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain RecetaMedica " + recetaMedicaOld + " since its idTratamiento field is not nullable.");
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                Tratamiento oldTratamientoOfIdCita = idCitaNew.getTratamiento();
                if (oldTratamientoOfIdCita != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The CitaMedica " + idCitaNew + " already has an item of type Tratamiento whose idCita column cannot be null. Please make another selection for the idCita field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (recetaMedicaNew != null) {
                recetaMedicaNew = em.getReference(recetaMedicaNew.getClass(), recetaMedicaNew.getIdReceta());
                tratamiento.setRecetaMedica(recetaMedicaNew);
            }
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                tratamiento.setIdCita(idCitaNew);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                tratamiento.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                tratamiento.setIdPaciente(idPacienteNew);
            }
            tratamiento = em.merge(tratamiento);
            if (recetaMedicaNew != null && !recetaMedicaNew.equals(recetaMedicaOld)) {
                Tratamiento oldIdTratamientoOfRecetaMedica = recetaMedicaNew.getIdTratamiento();
                if (oldIdTratamientoOfRecetaMedica != null) {
                    oldIdTratamientoOfRecetaMedica.setRecetaMedica(null);
                    oldIdTratamientoOfRecetaMedica = em.merge(oldIdTratamientoOfRecetaMedica);
                }
                recetaMedicaNew.setIdTratamiento(tratamiento);
                recetaMedicaNew = em.merge(recetaMedicaNew);
            }
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.setTratamiento(null);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.setTratamiento(tratamiento);
                idCitaNew = em.merge(idCitaNew);
            }
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getTratamientoList().remove(tratamiento);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getTratamientoList().add(tratamiento);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getTratamientoList().remove(tratamiento);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getTratamientoList().add(tratamiento);
                idPacienteNew = em.merge(idPacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tratamiento.getIdTratamiento();
                if (findTratamiento(id) == null) {
                    throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.");
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
            Tratamiento tratamiento;
            try {
                tratamiento = em.getReference(Tratamiento.class, id);
                tratamiento.getIdTratamiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            RecetaMedica recetaMedicaOrphanCheck = tratamiento.getRecetaMedica();
            if (recetaMedicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tratamiento (" + tratamiento + ") cannot be destroyed since the RecetaMedica " + recetaMedicaOrphanCheck + " in its recetaMedica field has a non-nullable idTratamiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CitaMedica idCita = tratamiento.getIdCita();
            if (idCita != null) {
                idCita.setTratamiento(null);
                idCita = em.merge(idCita);
            }
            Medico idMedico = tratamiento.getIdMedico();
            if (idMedico != null) {
                idMedico.getTratamientoList().remove(tratamiento);
                idMedico = em.merge(idMedico);
            }
            Paciente idPaciente = tratamiento.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getTratamientoList().remove(tratamiento);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(tratamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tratamiento> findTratamientoEntities() {
        return findTratamientoEntities(true, -1, -1);
    }

    public List<Tratamiento> findTratamientoEntities(int maxResults, int firstResult) {
        return findTratamientoEntities(false, maxResults, firstResult);
    }

    private List<Tratamiento> findTratamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tratamiento.class));
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

    public Tratamiento findTratamiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tratamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tratamiento> rt = cq.from(Tratamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
