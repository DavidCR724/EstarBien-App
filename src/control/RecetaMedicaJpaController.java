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
import modelo.Tratamiento;
import modelo.RecetaMedicamento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.RecetaMedica;

/**
 *
 * @author carlo
 */
public class RecetaMedicaJpaController implements Serializable {

    public RecetaMedicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecetaMedica recetaMedica) throws IllegalOrphanException {
        if (recetaMedica.getRecetaMedicamentoList() == null) {
            recetaMedica.setRecetaMedicamentoList(new ArrayList<RecetaMedicamento>());
        }
        List<String> illegalOrphanMessages = null;
        Tratamiento idTratamientoOrphanCheck = recetaMedica.getIdTratamiento();
        if (idTratamientoOrphanCheck != null) {
            RecetaMedica oldRecetaMedicaOfIdTratamiento = idTratamientoOrphanCheck.getRecetaMedica();
            if (oldRecetaMedicaOfIdTratamiento != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Tratamiento " + idTratamientoOrphanCheck + " already has an item of type RecetaMedica whose idTratamiento column cannot be null. Please make another selection for the idTratamiento field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CitaMedica idCita = recetaMedica.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                recetaMedica.setIdCita(idCita);
            }
            Tratamiento idTratamiento = recetaMedica.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento = em.getReference(idTratamiento.getClass(), idTratamiento.getIdTratamiento());
                recetaMedica.setIdTratamiento(idTratamiento);
            }
            List<RecetaMedicamento> attachedRecetaMedicamentoList = new ArrayList<RecetaMedicamento>();
            for (RecetaMedicamento recetaMedicamentoListRecetaMedicamentoToAttach : recetaMedica.getRecetaMedicamentoList()) {
                recetaMedicamentoListRecetaMedicamentoToAttach = em.getReference(recetaMedicamentoListRecetaMedicamentoToAttach.getClass(), recetaMedicamentoListRecetaMedicamentoToAttach.getIdRecetaMedicamento());
                attachedRecetaMedicamentoList.add(recetaMedicamentoListRecetaMedicamentoToAttach);
            }
            recetaMedica.setRecetaMedicamentoList(attachedRecetaMedicamentoList);
            em.persist(recetaMedica);
            if (idCita != null) {
                idCita.getRecetaMedicaList().add(recetaMedica);
                idCita = em.merge(idCita);
            }
            if (idTratamiento != null) {
                idTratamiento.setRecetaMedica(recetaMedica);
                idTratamiento = em.merge(idTratamiento);
            }
            for (RecetaMedicamento recetaMedicamentoListRecetaMedicamento : recetaMedica.getRecetaMedicamentoList()) {
                RecetaMedica oldIdRecetaOfRecetaMedicamentoListRecetaMedicamento = recetaMedicamentoListRecetaMedicamento.getIdReceta();
                recetaMedicamentoListRecetaMedicamento.setIdReceta(recetaMedica);
                recetaMedicamentoListRecetaMedicamento = em.merge(recetaMedicamentoListRecetaMedicamento);
                if (oldIdRecetaOfRecetaMedicamentoListRecetaMedicamento != null) {
                    oldIdRecetaOfRecetaMedicamentoListRecetaMedicamento.getRecetaMedicamentoList().remove(recetaMedicamentoListRecetaMedicamento);
                    oldIdRecetaOfRecetaMedicamentoListRecetaMedicamento = em.merge(oldIdRecetaOfRecetaMedicamentoListRecetaMedicamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecetaMedica recetaMedica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecetaMedica persistentRecetaMedica = em.find(RecetaMedica.class, recetaMedica.getIdReceta());
            CitaMedica idCitaOld = persistentRecetaMedica.getIdCita();
            CitaMedica idCitaNew = recetaMedica.getIdCita();
            Tratamiento idTratamientoOld = persistentRecetaMedica.getIdTratamiento();
            Tratamiento idTratamientoNew = recetaMedica.getIdTratamiento();
            List<RecetaMedicamento> recetaMedicamentoListOld = persistentRecetaMedica.getRecetaMedicamentoList();
            List<RecetaMedicamento> recetaMedicamentoListNew = recetaMedica.getRecetaMedicamentoList();
            List<String> illegalOrphanMessages = null;
            if (idTratamientoNew != null && !idTratamientoNew.equals(idTratamientoOld)) {
                RecetaMedica oldRecetaMedicaOfIdTratamiento = idTratamientoNew.getRecetaMedica();
                if (oldRecetaMedicaOfIdTratamiento != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Tratamiento " + idTratamientoNew + " already has an item of type RecetaMedica whose idTratamiento column cannot be null. Please make another selection for the idTratamiento field.");
                }
            }
            for (RecetaMedicamento recetaMedicamentoListOldRecetaMedicamento : recetaMedicamentoListOld) {
                if (!recetaMedicamentoListNew.contains(recetaMedicamentoListOldRecetaMedicamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecetaMedicamento " + recetaMedicamentoListOldRecetaMedicamento + " since its idReceta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                recetaMedica.setIdCita(idCitaNew);
            }
            if (idTratamientoNew != null) {
                idTratamientoNew = em.getReference(idTratamientoNew.getClass(), idTratamientoNew.getIdTratamiento());
                recetaMedica.setIdTratamiento(idTratamientoNew);
            }
            List<RecetaMedicamento> attachedRecetaMedicamentoListNew = new ArrayList<RecetaMedicamento>();
            for (RecetaMedicamento recetaMedicamentoListNewRecetaMedicamentoToAttach : recetaMedicamentoListNew) {
                recetaMedicamentoListNewRecetaMedicamentoToAttach = em.getReference(recetaMedicamentoListNewRecetaMedicamentoToAttach.getClass(), recetaMedicamentoListNewRecetaMedicamentoToAttach.getIdRecetaMedicamento());
                attachedRecetaMedicamentoListNew.add(recetaMedicamentoListNewRecetaMedicamentoToAttach);
            }
            recetaMedicamentoListNew = attachedRecetaMedicamentoListNew;
            recetaMedica.setRecetaMedicamentoList(recetaMedicamentoListNew);
            recetaMedica = em.merge(recetaMedica);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getRecetaMedicaList().remove(recetaMedica);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getRecetaMedicaList().add(recetaMedica);
                idCitaNew = em.merge(idCitaNew);
            }
            if (idTratamientoOld != null && !idTratamientoOld.equals(idTratamientoNew)) {
                idTratamientoOld.setRecetaMedica(null);
                idTratamientoOld = em.merge(idTratamientoOld);
            }
            if (idTratamientoNew != null && !idTratamientoNew.equals(idTratamientoOld)) {
                idTratamientoNew.setRecetaMedica(recetaMedica);
                idTratamientoNew = em.merge(idTratamientoNew);
            }
            for (RecetaMedicamento recetaMedicamentoListNewRecetaMedicamento : recetaMedicamentoListNew) {
                if (!recetaMedicamentoListOld.contains(recetaMedicamentoListNewRecetaMedicamento)) {
                    RecetaMedica oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento = recetaMedicamentoListNewRecetaMedicamento.getIdReceta();
                    recetaMedicamentoListNewRecetaMedicamento.setIdReceta(recetaMedica);
                    recetaMedicamentoListNewRecetaMedicamento = em.merge(recetaMedicamentoListNewRecetaMedicamento);
                    if (oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento != null && !oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento.equals(recetaMedica)) {
                        oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento.getRecetaMedicamentoList().remove(recetaMedicamentoListNewRecetaMedicamento);
                        oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento = em.merge(oldIdRecetaOfRecetaMedicamentoListNewRecetaMedicamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recetaMedica.getIdReceta();
                if (findRecetaMedica(id) == null) {
                    throw new NonexistentEntityException("The recetaMedica with id " + id + " no longer exists.");
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
            RecetaMedica recetaMedica;
            try {
                recetaMedica = em.getReference(RecetaMedica.class, id);
                recetaMedica.getIdReceta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recetaMedica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RecetaMedicamento> recetaMedicamentoListOrphanCheck = recetaMedica.getRecetaMedicamentoList();
            for (RecetaMedicamento recetaMedicamentoListOrphanCheckRecetaMedicamento : recetaMedicamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecetaMedica (" + recetaMedica + ") cannot be destroyed since the RecetaMedicamento " + recetaMedicamentoListOrphanCheckRecetaMedicamento + " in its recetaMedicamentoList field has a non-nullable idReceta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CitaMedica idCita = recetaMedica.getIdCita();
            if (idCita != null) {
                idCita.getRecetaMedicaList().remove(recetaMedica);
                idCita = em.merge(idCita);
            }
            Tratamiento idTratamiento = recetaMedica.getIdTratamiento();
            if (idTratamiento != null) {
                idTratamiento.setRecetaMedica(null);
                idTratamiento = em.merge(idTratamiento);
            }
            em.remove(recetaMedica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecetaMedica> findRecetaMedicaEntities() {
        return findRecetaMedicaEntities(true, -1, -1);
    }

    public List<RecetaMedica> findRecetaMedicaEntities(int maxResults, int firstResult) {
        return findRecetaMedicaEntities(false, maxResults, firstResult);
    }

    private List<RecetaMedica> findRecetaMedicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecetaMedica.class));
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

    public RecetaMedica findRecetaMedica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecetaMedica.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaMedicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecetaMedica> rt = cq.from(RecetaMedica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
