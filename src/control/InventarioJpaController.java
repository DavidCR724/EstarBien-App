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
import modelo.Medicamento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Inventario;

/**
 *
 * @author carlo
 */
public class InventarioJpaController implements Serializable {

    public InventarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inventario inventario) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Medicamento idMedicamentoOrphanCheck = inventario.getIdMedicamento();
        if (idMedicamentoOrphanCheck != null) {
            Inventario oldInventarioOfIdMedicamento = idMedicamentoOrphanCheck.getInventario();
            if (oldInventarioOfIdMedicamento != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Medicamento " + idMedicamentoOrphanCheck + " already has an item of type Inventario whose idMedicamento column cannot be null. Please make another selection for the idMedicamento field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamento idMedicamento = inventario.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento = em.getReference(idMedicamento.getClass(), idMedicamento.getIdMedicamento());
                inventario.setIdMedicamento(idMedicamento);
            }
            em.persist(inventario);
            if (idMedicamento != null) {
                idMedicamento.setInventario(inventario);
                idMedicamento = em.merge(idMedicamento);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inventario inventario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventario persistentInventario = em.find(Inventario.class, inventario.getIdInventario());
            Medicamento idMedicamentoOld = persistentInventario.getIdMedicamento();
            Medicamento idMedicamentoNew = inventario.getIdMedicamento();
            List<String> illegalOrphanMessages = null;
            if (idMedicamentoNew != null && !idMedicamentoNew.equals(idMedicamentoOld)) {
                Inventario oldInventarioOfIdMedicamento = idMedicamentoNew.getInventario();
                if (oldInventarioOfIdMedicamento != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Medicamento " + idMedicamentoNew + " already has an item of type Inventario whose idMedicamento column cannot be null. Please make another selection for the idMedicamento field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMedicamentoNew != null) {
                idMedicamentoNew = em.getReference(idMedicamentoNew.getClass(), idMedicamentoNew.getIdMedicamento());
                inventario.setIdMedicamento(idMedicamentoNew);
            }
            inventario = em.merge(inventario);
            if (idMedicamentoOld != null && !idMedicamentoOld.equals(idMedicamentoNew)) {
                idMedicamentoOld.setInventario(null);
                idMedicamentoOld = em.merge(idMedicamentoOld);
            }
            if (idMedicamentoNew != null && !idMedicamentoNew.equals(idMedicamentoOld)) {
                idMedicamentoNew.setInventario(inventario);
                idMedicamentoNew = em.merge(idMedicamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventario.getIdInventario();
                if (findInventario(id) == null) {
                    throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.");
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
            Inventario inventario;
            try {
                inventario = em.getReference(Inventario.class, id);
                inventario.getIdInventario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.", enfe);
            }
            Medicamento idMedicamento = inventario.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento.setInventario(null);
                idMedicamento = em.merge(idMedicamento);
            }
            em.remove(inventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inventario> findInventarioEntities() {
        return findInventarioEntities(true, -1, -1);
    }

    public List<Inventario> findInventarioEntities(int maxResults, int firstResult) {
        return findInventarioEntities(false, maxResults, firstResult);
    }

    private List<Inventario> findInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventario.class));
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

    public Inventario findInventario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventario> rt = cq.from(Inventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
