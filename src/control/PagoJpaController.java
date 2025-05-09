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
import modelo.CitaMedica;
import modelo.Paciente;
import modelo.Factura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Pago;

/**
 *
 * @author carlo
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CitaMedica idCita = pago.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                pago.setIdCita(idCita);
            }
            Paciente idPaciente = pago.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                pago.setIdPaciente(idPaciente);
            }
            Factura factura = pago.getFactura();
            if (factura != null) {
                factura = em.getReference(factura.getClass(), factura.getIdFactura());
                pago.setFactura(factura);
            }
            em.persist(pago);
            if (idCita != null) {
                Pago oldPagoOfIdCita = idCita.getPago();
                if (oldPagoOfIdCita != null) {
                    oldPagoOfIdCita.setIdCita(null);
                    oldPagoOfIdCita = em.merge(oldPagoOfIdCita);
                }
                idCita.setPago(pago);
                idCita = em.merge(idCita);
            }
            if (idPaciente != null) {
                idPaciente.getPagoList().add(pago);
                idPaciente = em.merge(idPaciente);
            }
            if (factura != null) {
                Pago oldIdPagoOfFactura = factura.getIdPago();
                if (oldIdPagoOfFactura != null) {
                    oldIdPagoOfFactura.setFactura(null);
                    oldIdPagoOfFactura = em.merge(oldIdPagoOfFactura);
                }
                factura.setIdPago(pago);
                factura = em.merge(factura);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getIdPago());
            CitaMedica idCitaOld = persistentPago.getIdCita();
            CitaMedica idCitaNew = pago.getIdCita();
            Paciente idPacienteOld = persistentPago.getIdPaciente();
            Paciente idPacienteNew = pago.getIdPaciente();
            Factura facturaOld = persistentPago.getFactura();
            Factura facturaNew = pago.getFactura();
            List<String> illegalOrphanMessages = null;
            if (facturaOld != null && !facturaOld.equals(facturaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Factura " + facturaOld + " since its idPago field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                pago.setIdCita(idCitaNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                pago.setIdPaciente(idPacienteNew);
            }
            if (facturaNew != null) {
                facturaNew = em.getReference(facturaNew.getClass(), facturaNew.getIdFactura());
                pago.setFactura(facturaNew);
            }
            pago = em.merge(pago);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.setPago(null);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                Pago oldPagoOfIdCita = idCitaNew.getPago();
                if (oldPagoOfIdCita != null) {
                    oldPagoOfIdCita.setIdCita(null);
                    oldPagoOfIdCita = em.merge(oldPagoOfIdCita);
                }
                idCitaNew.setPago(pago);
                idCitaNew = em.merge(idCitaNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getPagoList().remove(pago);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getPagoList().add(pago);
                idPacienteNew = em.merge(idPacienteNew);
            }
            if (facturaNew != null && !facturaNew.equals(facturaOld)) {
                Pago oldIdPagoOfFactura = facturaNew.getIdPago();
                if (oldIdPagoOfFactura != null) {
                    oldIdPagoOfFactura.setFactura(null);
                    oldIdPagoOfFactura = em.merge(oldIdPagoOfFactura);
                }
                facturaNew.setIdPago(pago);
                facturaNew = em.merge(facturaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getIdPago();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Factura facturaOrphanCheck = pago.getFactura();
            if (facturaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Factura " + facturaOrphanCheck + " in its factura field has a non-nullable idPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CitaMedica idCita = pago.getIdCita();
            if (idCita != null) {
                idCita.setPago(null);
                idCita = em.merge(idCita);
            }
            Paciente idPaciente = pago.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getPagoList().remove(pago);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
