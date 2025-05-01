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
import modelo.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Factura;

/**
 *
 * @author carlo
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Pago idPagoOrphanCheck = factura.getIdPago();
        if (idPagoOrphanCheck != null) {
            Factura oldFacturaOfIdPago = idPagoOrphanCheck.getFactura();
            if (oldFacturaOfIdPago != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pago " + idPagoOrphanCheck + " already has an item of type Factura whose idPago column cannot be null. Please make another selection for the idPago field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago idPago = factura.getIdPago();
            if (idPago != null) {
                idPago = em.getReference(idPago.getClass(), idPago.getIdPago());
                factura.setIdPago(idPago);
            }
            em.persist(factura);
            if (idPago != null) {
                idPago.setFactura(factura);
                idPago = em.merge(idPago);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactura());
            Pago idPagoOld = persistentFactura.getIdPago();
            Pago idPagoNew = factura.getIdPago();
            List<String> illegalOrphanMessages = null;
            if (idPagoNew != null && !idPagoNew.equals(idPagoOld)) {
                Factura oldFacturaOfIdPago = idPagoNew.getFactura();
                if (oldFacturaOfIdPago != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pago " + idPagoNew + " already has an item of type Factura whose idPago column cannot be null. Please make another selection for the idPago field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPagoNew != null) {
                idPagoNew = em.getReference(idPagoNew.getClass(), idPagoNew.getIdPago());
                factura.setIdPago(idPagoNew);
            }
            factura = em.merge(factura);
            if (idPagoOld != null && !idPagoOld.equals(idPagoNew)) {
                idPagoOld.setFactura(null);
                idPagoOld = em.merge(idPagoOld);
            }
            if (idPagoNew != null && !idPagoNew.equals(idPagoOld)) {
                idPagoNew.setFactura(factura);
                idPagoNew = em.merge(idPagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Pago idPago = factura.getIdPago();
            if (idPago != null) {
                idPago.setFactura(null);
                idPago = em.merge(idPago);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
