/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Medico;
import modelo.CitaMedica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.HorarioMedico;

/**
 *
 * @author carlo
 */
public class HorarioMedicoJpaController implements Serializable {

    public HorarioMedicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HorarioMedico horarioMedico) {
        if (horarioMedico.getCitaMedicaList() == null) {
            horarioMedico.setCitaMedicaList(new ArrayList<CitaMedica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medico idMedico = horarioMedico.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                horarioMedico.setIdMedico(idMedico);
            }
            List<CitaMedica> attachedCitaMedicaList = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListCitaMedicaToAttach : horarioMedico.getCitaMedicaList()) {
                citaMedicaListCitaMedicaToAttach = em.getReference(citaMedicaListCitaMedicaToAttach.getClass(), citaMedicaListCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaList.add(citaMedicaListCitaMedicaToAttach);
            }
            horarioMedico.setCitaMedicaList(attachedCitaMedicaList);
            em.persist(horarioMedico);
            if (idMedico != null) {
                idMedico.getHorarioMedicoList().add(horarioMedico);
                idMedico = em.merge(idMedico);
            }
            for (CitaMedica citaMedicaListCitaMedica : horarioMedico.getCitaMedicaList()) {
                HorarioMedico oldIdHorarioOfCitaMedicaListCitaMedica = citaMedicaListCitaMedica.getIdHorario();
                citaMedicaListCitaMedica.setIdHorario(horarioMedico);
                citaMedicaListCitaMedica = em.merge(citaMedicaListCitaMedica);
                if (oldIdHorarioOfCitaMedicaListCitaMedica != null) {
                    oldIdHorarioOfCitaMedicaListCitaMedica.getCitaMedicaList().remove(citaMedicaListCitaMedica);
                    oldIdHorarioOfCitaMedicaListCitaMedica = em.merge(oldIdHorarioOfCitaMedicaListCitaMedica);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HorarioMedico horarioMedico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HorarioMedico persistentHorarioMedico = em.find(HorarioMedico.class, horarioMedico.getIdHorario());
            Medico idMedicoOld = persistentHorarioMedico.getIdMedico();
            Medico idMedicoNew = horarioMedico.getIdMedico();
            List<CitaMedica> citaMedicaListOld = persistentHorarioMedico.getCitaMedicaList();
            List<CitaMedica> citaMedicaListNew = horarioMedico.getCitaMedicaList();
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                horarioMedico.setIdMedico(idMedicoNew);
            }
            List<CitaMedica> attachedCitaMedicaListNew = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListNewCitaMedicaToAttach : citaMedicaListNew) {
                citaMedicaListNewCitaMedicaToAttach = em.getReference(citaMedicaListNewCitaMedicaToAttach.getClass(), citaMedicaListNewCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaListNew.add(citaMedicaListNewCitaMedicaToAttach);
            }
            citaMedicaListNew = attachedCitaMedicaListNew;
            horarioMedico.setCitaMedicaList(citaMedicaListNew);
            horarioMedico = em.merge(horarioMedico);
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getHorarioMedicoList().remove(horarioMedico);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getHorarioMedicoList().add(horarioMedico);
                idMedicoNew = em.merge(idMedicoNew);
            }
            for (CitaMedica citaMedicaListOldCitaMedica : citaMedicaListOld) {
                if (!citaMedicaListNew.contains(citaMedicaListOldCitaMedica)) {
                    citaMedicaListOldCitaMedica.setIdHorario(null);
                    citaMedicaListOldCitaMedica = em.merge(citaMedicaListOldCitaMedica);
                }
            }
            for (CitaMedica citaMedicaListNewCitaMedica : citaMedicaListNew) {
                if (!citaMedicaListOld.contains(citaMedicaListNewCitaMedica)) {
                    HorarioMedico oldIdHorarioOfCitaMedicaListNewCitaMedica = citaMedicaListNewCitaMedica.getIdHorario();
                    citaMedicaListNewCitaMedica.setIdHorario(horarioMedico);
                    citaMedicaListNewCitaMedica = em.merge(citaMedicaListNewCitaMedica);
                    if (oldIdHorarioOfCitaMedicaListNewCitaMedica != null && !oldIdHorarioOfCitaMedicaListNewCitaMedica.equals(horarioMedico)) {
                        oldIdHorarioOfCitaMedicaListNewCitaMedica.getCitaMedicaList().remove(citaMedicaListNewCitaMedica);
                        oldIdHorarioOfCitaMedicaListNewCitaMedica = em.merge(oldIdHorarioOfCitaMedicaListNewCitaMedica);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = horarioMedico.getIdHorario();
                if (findHorarioMedico(id) == null) {
                    throw new NonexistentEntityException("The horarioMedico with id " + id + " no longer exists.");
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
            HorarioMedico horarioMedico;
            try {
                horarioMedico = em.getReference(HorarioMedico.class, id);
                horarioMedico.getIdHorario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horarioMedico with id " + id + " no longer exists.", enfe);
            }
            Medico idMedico = horarioMedico.getIdMedico();
            if (idMedico != null) {
                idMedico.getHorarioMedicoList().remove(horarioMedico);
                idMedico = em.merge(idMedico);
            }
            List<CitaMedica> citaMedicaList = horarioMedico.getCitaMedicaList();
            for (CitaMedica citaMedicaListCitaMedica : citaMedicaList) {
                citaMedicaListCitaMedica.setIdHorario(null);
                citaMedicaListCitaMedica = em.merge(citaMedicaListCitaMedica);
            }
            em.remove(horarioMedico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HorarioMedico> findHorarioMedicoEntities() {
        return findHorarioMedicoEntities(true, -1, -1);
    }

    public List<HorarioMedico> findHorarioMedicoEntities(int maxResults, int firstResult) {
        return findHorarioMedicoEntities(false, maxResults, firstResult);
    }

    private List<HorarioMedico> findHorarioMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HorarioMedico.class));
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

    public HorarioMedico findHorarioMedico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HorarioMedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HorarioMedico> rt = cq.from(HorarioMedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
