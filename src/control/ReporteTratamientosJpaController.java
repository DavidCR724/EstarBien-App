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
import modelo.ReporteTratamientos;

/**
 *
 * @author carlo
 */
public class ReporteTratamientosJpaController implements Serializable {

    public ReporteTratamientosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReporteTratamientos reporteTratamientos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reporteTratamientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReporteTratamientos(reporteTratamientos.getIdTratamiento()) != null) {
                throw new PreexistingEntityException("ReporteTratamientos " + reporteTratamientos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReporteTratamientos reporteTratamientos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reporteTratamientos = em.merge(reporteTratamientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reporteTratamientos.getIdTratamiento();
                if (findReporteTratamientos(id) == null) {
                    throw new NonexistentEntityException("The reporteTratamientos with id " + id + " no longer exists.");
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
            ReporteTratamientos reporteTratamientos;
            try {
                reporteTratamientos = em.getReference(ReporteTratamientos.class, id);
                reporteTratamientos.getIdTratamiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reporteTratamientos with id " + id + " no longer exists.", enfe);
            }
            em.remove(reporteTratamientos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReporteTratamientos> findReporteTratamientosEntities() {
        return findReporteTratamientosEntities(true, -1, -1);
    }

    public List<ReporteTratamientos> findReporteTratamientosEntities(int maxResults, int firstResult) {
        return findReporteTratamientosEntities(false, maxResults, firstResult);
    }

    private List<ReporteTratamientos> findReporteTratamientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReporteTratamientos.class));
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

    public ReporteTratamientos findReporteTratamientos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReporteTratamientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getReporteTratamientosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReporteTratamientos> rt = cq.from(ReporteTratamientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
