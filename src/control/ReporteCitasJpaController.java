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
import modelo.ReporteCitas;

/**
 *
 * @author carlo
 */
public class ReporteCitasJpaController implements Serializable {

    public ReporteCitasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReporteCitas reporteCitas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reporteCitas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReporteCitas(reporteCitas.getIdCita()) != null) {
                throw new PreexistingEntityException("ReporteCitas " + reporteCitas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReporteCitas reporteCitas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reporteCitas = em.merge(reporteCitas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reporteCitas.getIdCita();
                if (findReporteCitas(id) == null) {
                    throw new NonexistentEntityException("The reporteCitas with id " + id + " no longer exists.");
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
            ReporteCitas reporteCitas;
            try {
                reporteCitas = em.getReference(ReporteCitas.class, id);
                reporteCitas.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reporteCitas with id " + id + " no longer exists.", enfe);
            }
            em.remove(reporteCitas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReporteCitas> findReporteCitasEntities() {
        return findReporteCitasEntities(true, -1, -1);
    }

    public List<ReporteCitas> findReporteCitasEntities(int maxResults, int firstResult) {
        return findReporteCitasEntities(false, maxResults, firstResult);
    }

    private List<ReporteCitas> findReporteCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReporteCitas.class));
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

    public ReporteCitas findReporteCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReporteCitas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReporteCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReporteCitas> rt = cq.from(ReporteCitas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
