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
import modelo.Medicamento;
import modelo.RecetaMedica;
import modelo.RecetaMedicamento;

/**
 *
 * @author carlo
 */
public class RecetaMedicamentoJpaController implements Serializable {

    public RecetaMedicamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecetaMedicamento recetaMedicamento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamento idMedicamento = recetaMedicamento.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento = em.getReference(idMedicamento.getClass(), idMedicamento.getIdMedicamento());
                recetaMedicamento.setIdMedicamento(idMedicamento);
            }
            RecetaMedica idReceta = recetaMedicamento.getIdReceta();
            if (idReceta != null) {
                idReceta = em.getReference(idReceta.getClass(), idReceta.getIdReceta());
                recetaMedicamento.setIdReceta(idReceta);
            }
            em.persist(recetaMedicamento);
            if (idMedicamento != null) {
                idMedicamento.getRecetaMedicamentoList().add(recetaMedicamento);
                idMedicamento = em.merge(idMedicamento);
            }
            if (idReceta != null) {
                idReceta.getRecetaMedicamentoList().add(recetaMedicamento);
                idReceta = em.merge(idReceta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecetaMedicamento recetaMedicamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecetaMedicamento persistentRecetaMedicamento = em.find(RecetaMedicamento.class, recetaMedicamento.getIdRecetaMedicamento());
            Medicamento idMedicamentoOld = persistentRecetaMedicamento.getIdMedicamento();
            Medicamento idMedicamentoNew = recetaMedicamento.getIdMedicamento();
            RecetaMedica idRecetaOld = persistentRecetaMedicamento.getIdReceta();
            RecetaMedica idRecetaNew = recetaMedicamento.getIdReceta();
            if (idMedicamentoNew != null) {
                idMedicamentoNew = em.getReference(idMedicamentoNew.getClass(), idMedicamentoNew.getIdMedicamento());
                recetaMedicamento.setIdMedicamento(idMedicamentoNew);
            }
            if (idRecetaNew != null) {
                idRecetaNew = em.getReference(idRecetaNew.getClass(), idRecetaNew.getIdReceta());
                recetaMedicamento.setIdReceta(idRecetaNew);
            }
            recetaMedicamento = em.merge(recetaMedicamento);
            if (idMedicamentoOld != null && !idMedicamentoOld.equals(idMedicamentoNew)) {
                idMedicamentoOld.getRecetaMedicamentoList().remove(recetaMedicamento);
                idMedicamentoOld = em.merge(idMedicamentoOld);
            }
            if (idMedicamentoNew != null && !idMedicamentoNew.equals(idMedicamentoOld)) {
                idMedicamentoNew.getRecetaMedicamentoList().add(recetaMedicamento);
                idMedicamentoNew = em.merge(idMedicamentoNew);
            }
            if (idRecetaOld != null && !idRecetaOld.equals(idRecetaNew)) {
                idRecetaOld.getRecetaMedicamentoList().remove(recetaMedicamento);
                idRecetaOld = em.merge(idRecetaOld);
            }
            if (idRecetaNew != null && !idRecetaNew.equals(idRecetaOld)) {
                idRecetaNew.getRecetaMedicamentoList().add(recetaMedicamento);
                idRecetaNew = em.merge(idRecetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recetaMedicamento.getIdRecetaMedicamento();
                if (findRecetaMedicamento(id) == null) {
                    throw new NonexistentEntityException("The recetaMedicamento with id " + id + " no longer exists.");
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
            RecetaMedicamento recetaMedicamento;
            try {
                recetaMedicamento = em.getReference(RecetaMedicamento.class, id);
                recetaMedicamento.getIdRecetaMedicamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recetaMedicamento with id " + id + " no longer exists.", enfe);
            }
            Medicamento idMedicamento = recetaMedicamento.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento.getRecetaMedicamentoList().remove(recetaMedicamento);
                idMedicamento = em.merge(idMedicamento);
            }
            RecetaMedica idReceta = recetaMedicamento.getIdReceta();
            if (idReceta != null) {
                idReceta.getRecetaMedicamentoList().remove(recetaMedicamento);
                idReceta = em.merge(idReceta);
            }
            em.remove(recetaMedicamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecetaMedicamento> findRecetaMedicamentoEntities() {
        return findRecetaMedicamentoEntities(true, -1, -1);
    }

    public List<RecetaMedicamento> findRecetaMedicamentoEntities(int maxResults, int firstResult) {
        return findRecetaMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<RecetaMedicamento> findRecetaMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecetaMedicamento.class));
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

    public RecetaMedicamento findRecetaMedicamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecetaMedicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecetaMedicamento> rt = cq.from(RecetaMedicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
