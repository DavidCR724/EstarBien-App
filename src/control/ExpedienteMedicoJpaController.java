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
import modelo.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.ExpedienteMedico;

/**
 *
 * @author carlo
 */
public class ExpedienteMedicoJpaController implements Serializable {

    public ExpedienteMedicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExpedienteMedico expedienteMedico) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Paciente idPacienteOrphanCheck = expedienteMedico.getIdPaciente();
        if (idPacienteOrphanCheck != null) {
            ExpedienteMedico oldExpedienteMedicoOfIdPaciente = idPacienteOrphanCheck.getExpedienteMedico();
            if (oldExpedienteMedicoOfIdPaciente != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paciente " + idPacienteOrphanCheck + " already has an item of type ExpedienteMedico whose idPaciente column cannot be null. Please make another selection for the idPaciente field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente idPaciente = expedienteMedico.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                expedienteMedico.setIdPaciente(idPaciente);
            }
            em.persist(expedienteMedico);
            if (idPaciente != null) {
                idPaciente.setExpedienteMedico(expedienteMedico);
                idPaciente = em.merge(idPaciente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExpedienteMedico expedienteMedico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExpedienteMedico persistentExpedienteMedico = em.find(ExpedienteMedico.class, expedienteMedico.getIdExpediente());
            Paciente idPacienteOld = persistentExpedienteMedico.getIdPaciente();
            Paciente idPacienteNew = expedienteMedico.getIdPaciente();
            List<String> illegalOrphanMessages = null;
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                ExpedienteMedico oldExpedienteMedicoOfIdPaciente = idPacienteNew.getExpedienteMedico();
                if (oldExpedienteMedicoOfIdPaciente != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paciente " + idPacienteNew + " already has an item of type ExpedienteMedico whose idPaciente column cannot be null. Please make another selection for the idPaciente field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                expedienteMedico.setIdPaciente(idPacienteNew);
            }
            expedienteMedico = em.merge(expedienteMedico);
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.setExpedienteMedico(null);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.setExpedienteMedico(expedienteMedico);
                idPacienteNew = em.merge(idPacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = expedienteMedico.getIdExpediente();
                if (findExpedienteMedico(id) == null) {
                    throw new NonexistentEntityException("The expedienteMedico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExpedienteMedico expedienteMedico;
            try {
                expedienteMedico = em.getReference(ExpedienteMedico.class, id);
                expedienteMedico.getIdExpediente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The expedienteMedico with id " + id + " no longer exists.", enfe);
            }
            Paciente idPaciente = expedienteMedico.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.setExpedienteMedico(null);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(expedienteMedico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExpedienteMedico> findExpedienteMedicoEntities() {
        return findExpedienteMedicoEntities(true, -1, -1);
    }

    public List<ExpedienteMedico> findExpedienteMedicoEntities(int maxResults, int firstResult) {
        return findExpedienteMedicoEntities(false, maxResults, firstResult);
    }

    private List<ExpedienteMedico> findExpedienteMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExpedienteMedico.class));
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

    public ExpedienteMedico findExpedienteMedico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExpedienteMedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getExpedienteMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExpedienteMedico> rt = cq.from(ExpedienteMedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
