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
import modelo.Inventario;
import modelo.RecetaMedicamento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Medicamento;

/**
 *
 * @author carlo
 */
public class MedicamentoJpaController implements Serializable {

    public MedicamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamento medicamento) {
        if (medicamento.getRecetaMedicamentoList() == null) {
            medicamento.setRecetaMedicamentoList(new ArrayList<RecetaMedicamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventario inventario = medicamento.getInventario();
            if (inventario != null) {
                inventario = em.getReference(inventario.getClass(), inventario.getIdInventario());
                medicamento.setInventario(inventario);
            }
            List<RecetaMedicamento> attachedRecetaMedicamentoList = new ArrayList<RecetaMedicamento>();
            for (RecetaMedicamento recetaMedicamentoListRecetaMedicamentoToAttach : medicamento.getRecetaMedicamentoList()) {
                recetaMedicamentoListRecetaMedicamentoToAttach = em.getReference(recetaMedicamentoListRecetaMedicamentoToAttach.getClass(), recetaMedicamentoListRecetaMedicamentoToAttach.getIdRecetaMedicamento());
                attachedRecetaMedicamentoList.add(recetaMedicamentoListRecetaMedicamentoToAttach);
            }
            medicamento.setRecetaMedicamentoList(attachedRecetaMedicamentoList);
            em.persist(medicamento);
            if (inventario != null) {
                Medicamento oldIdMedicamentoOfInventario = inventario.getIdMedicamento();
                if (oldIdMedicamentoOfInventario != null) {
                    oldIdMedicamentoOfInventario.setInventario(null);
                    oldIdMedicamentoOfInventario = em.merge(oldIdMedicamentoOfInventario);
                }
                inventario.setIdMedicamento(medicamento);
                inventario = em.merge(inventario);
            }
            for (RecetaMedicamento recetaMedicamentoListRecetaMedicamento : medicamento.getRecetaMedicamentoList()) {
                Medicamento oldIdMedicamentoOfRecetaMedicamentoListRecetaMedicamento = recetaMedicamentoListRecetaMedicamento.getIdMedicamento();
                recetaMedicamentoListRecetaMedicamento.setIdMedicamento(medicamento);
                recetaMedicamentoListRecetaMedicamento = em.merge(recetaMedicamentoListRecetaMedicamento);
                if (oldIdMedicamentoOfRecetaMedicamentoListRecetaMedicamento != null) {
                    oldIdMedicamentoOfRecetaMedicamentoListRecetaMedicamento.getRecetaMedicamentoList().remove(recetaMedicamentoListRecetaMedicamento);
                    oldIdMedicamentoOfRecetaMedicamentoListRecetaMedicamento = em.merge(oldIdMedicamentoOfRecetaMedicamentoListRecetaMedicamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicamento medicamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getIdMedicamento());
            Inventario inventarioOld = persistentMedicamento.getInventario();
            Inventario inventarioNew = medicamento.getInventario();
            List<RecetaMedicamento> recetaMedicamentoListOld = persistentMedicamento.getRecetaMedicamentoList();
            List<RecetaMedicamento> recetaMedicamentoListNew = medicamento.getRecetaMedicamentoList();
            List<String> illegalOrphanMessages = null;
            if (inventarioOld != null && !inventarioOld.equals(inventarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Inventario " + inventarioOld + " since its idMedicamento field is not nullable.");
            }
            for (RecetaMedicamento recetaMedicamentoListOldRecetaMedicamento : recetaMedicamentoListOld) {
                if (!recetaMedicamentoListNew.contains(recetaMedicamentoListOldRecetaMedicamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecetaMedicamento " + recetaMedicamentoListOldRecetaMedicamento + " since its idMedicamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (inventarioNew != null) {
                inventarioNew = em.getReference(inventarioNew.getClass(), inventarioNew.getIdInventario());
                medicamento.setInventario(inventarioNew);
            }
            List<RecetaMedicamento> attachedRecetaMedicamentoListNew = new ArrayList<RecetaMedicamento>();
            for (RecetaMedicamento recetaMedicamentoListNewRecetaMedicamentoToAttach : recetaMedicamentoListNew) {
                recetaMedicamentoListNewRecetaMedicamentoToAttach = em.getReference(recetaMedicamentoListNewRecetaMedicamentoToAttach.getClass(), recetaMedicamentoListNewRecetaMedicamentoToAttach.getIdRecetaMedicamento());
                attachedRecetaMedicamentoListNew.add(recetaMedicamentoListNewRecetaMedicamentoToAttach);
            }
            recetaMedicamentoListNew = attachedRecetaMedicamentoListNew;
            medicamento.setRecetaMedicamentoList(recetaMedicamentoListNew);
            medicamento = em.merge(medicamento);
            if (inventarioNew != null && !inventarioNew.equals(inventarioOld)) {
                Medicamento oldIdMedicamentoOfInventario = inventarioNew.getIdMedicamento();
                if (oldIdMedicamentoOfInventario != null) {
                    oldIdMedicamentoOfInventario.setInventario(null);
                    oldIdMedicamentoOfInventario = em.merge(oldIdMedicamentoOfInventario);
                }
                inventarioNew.setIdMedicamento(medicamento);
                inventarioNew = em.merge(inventarioNew);
            }
            for (RecetaMedicamento recetaMedicamentoListNewRecetaMedicamento : recetaMedicamentoListNew) {
                if (!recetaMedicamentoListOld.contains(recetaMedicamentoListNewRecetaMedicamento)) {
                    Medicamento oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento = recetaMedicamentoListNewRecetaMedicamento.getIdMedicamento();
                    recetaMedicamentoListNewRecetaMedicamento.setIdMedicamento(medicamento);
                    recetaMedicamentoListNewRecetaMedicamento = em.merge(recetaMedicamentoListNewRecetaMedicamento);
                    if (oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento != null && !oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento.equals(medicamento)) {
                        oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento.getRecetaMedicamentoList().remove(recetaMedicamentoListNewRecetaMedicamento);
                        oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento = em.merge(oldIdMedicamentoOfRecetaMedicamentoListNewRecetaMedicamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicamento.getIdMedicamento();
                if (findMedicamento(id) == null) {
                    throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.");
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
            Medicamento medicamento;
            try {
                medicamento = em.getReference(Medicamento.class, id);
                medicamento.getIdMedicamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Inventario inventarioOrphanCheck = medicamento.getInventario();
            if (inventarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the Inventario " + inventarioOrphanCheck + " in its inventario field has a non-nullable idMedicamento field.");
            }
            List<RecetaMedicamento> recetaMedicamentoListOrphanCheck = medicamento.getRecetaMedicamentoList();
            for (RecetaMedicamento recetaMedicamentoListOrphanCheckRecetaMedicamento : recetaMedicamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the RecetaMedicamento " + recetaMedicamentoListOrphanCheckRecetaMedicamento + " in its recetaMedicamentoList field has a non-nullable idMedicamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicamento> findMedicamentoEntities() {
        return findMedicamentoEntities(true, -1, -1);
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return findMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<Medicamento> findMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamento.class));
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

    public Medicamento findMedicamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamento> rt = cq.from(Medicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    

    

    
}
