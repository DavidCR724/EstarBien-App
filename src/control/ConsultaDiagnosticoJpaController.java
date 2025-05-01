/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Consulta;
import modelo.ConsultaDiagnostico;

/**
 *
 * @author carlo
 */
public class ConsultaDiagnosticoJpaController implements Serializable {

    public ConsultaDiagnosticoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsultaDiagnostico consultaDiagnostico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consulta idConsulta = consultaDiagnostico.getIdConsulta();
            if (idConsulta != null) {
                idConsulta = em.getReference(idConsulta.getClass(), idConsulta.getIdConsulta());
                consultaDiagnostico.setIdConsulta(idConsulta);
            }
            em.persist(consultaDiagnostico);
            if (idConsulta != null) {
                idConsulta.getConsultaDiagnosticoList().add(consultaDiagnostico);
                idConsulta = em.merge(idConsulta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsultaDiagnostico consultaDiagnostico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConsultaDiagnostico persistentConsultaDiagnostico = em.find(ConsultaDiagnostico.class, consultaDiagnostico.getIdConsultaDiagnostico());
            Consulta idConsultaOld = persistentConsultaDiagnostico.getIdConsulta();
            Consulta idConsultaNew = consultaDiagnostico.getIdConsulta();
            if (idConsultaNew != null) {
                idConsultaNew = em.getReference(idConsultaNew.getClass(), idConsultaNew.getIdConsulta());
                consultaDiagnostico.setIdConsulta(idConsultaNew);
            }
            consultaDiagnostico = em.merge(consultaDiagnostico);
            if (idConsultaOld != null && !idConsultaOld.equals(idConsultaNew)) {
                idConsultaOld.getConsultaDiagnosticoList().remove(consultaDiagnostico);
                idConsultaOld = em.merge(idConsultaOld);
            }
            if (idConsultaNew != null && !idConsultaNew.equals(idConsultaOld)) {
                idConsultaNew.getConsultaDiagnosticoList().add(consultaDiagnostico);
                idConsultaNew = em.merge(idConsultaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consultaDiagnostico.getIdConsultaDiagnostico();
                if (findConsultaDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The consultaDiagnostico with id " + id + " no longer exists.");
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
            ConsultaDiagnostico consultaDiagnostico;
            try {
                consultaDiagnostico = em.getReference(ConsultaDiagnostico.class, id);
                consultaDiagnostico.getIdConsultaDiagnostico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultaDiagnostico with id " + id + " no longer exists.", enfe);
            }
            Consulta idConsulta = consultaDiagnostico.getIdConsulta();
            if (idConsulta != null) {
                idConsulta.getConsultaDiagnosticoList().remove(consultaDiagnostico);
                idConsulta = em.merge(idConsulta);
            }
            em.remove(consultaDiagnostico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsultaDiagnostico> findConsultaDiagnosticoEntities() {
        return findConsultaDiagnosticoEntities(true, -1, -1);
    }

    public List<ConsultaDiagnostico> findConsultaDiagnosticoEntities(int maxResults, int firstResult) {
        return findConsultaDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<ConsultaDiagnostico> findConsultaDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsultaDiagnostico.class));
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

    public ConsultaDiagnostico findConsultaDiagnostico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsultaDiagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsultaDiagnostico> rt = cq.from(ConsultaDiagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
