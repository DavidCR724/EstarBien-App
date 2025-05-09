/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.SeguimientoPacientes;

/**
 *
 * @author carlo
 */
public class SeguimientoPacientesJpaController implements Serializable {

    public SeguimientoPacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SeguimientoPacientes seguimientoPacientes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(seguimientoPacientes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeguimientoPacientes(seguimientoPacientes.getId()) != null) {
                throw new PreexistingEntityException("SeguimientoPacientes " + seguimientoPacientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SeguimientoPacientes seguimientoPacientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            seguimientoPacientes = em.merge(seguimientoPacientes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = seguimientoPacientes.getId();
                if (findSeguimientoPacientes(id) == null) {
                    throw new NonexistentEntityException("The seguimientoPacientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeguimientoPacientes seguimientoPacientes;
            try {
                seguimientoPacientes = em.getReference(SeguimientoPacientes.class, id);
                seguimientoPacientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seguimientoPacientes with id " + id + " no longer exists.", enfe);
            }
            em.remove(seguimientoPacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SeguimientoPacientes> findSeguimientoPacientesEntities() {
        return findSeguimientoPacientesEntities(true, -1, -1);
    }

    public List<SeguimientoPacientes> findSeguimientoPacientesEntities(int maxResults, int firstResult) {
        return findSeguimientoPacientesEntities(false, maxResults, firstResult);
    }

    private List<SeguimientoPacientes> findSeguimientoPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SeguimientoPacientes.class));
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

    public SeguimientoPacientes findSeguimientoPacientes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SeguimientoPacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeguimientoPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SeguimientoPacientes> rt = cq.from(SeguimientoPacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
