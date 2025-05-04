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
import modelo.Usuario;
import modelo.HorarioMedico;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Tratamiento;
import modelo.CitaMedica;
import modelo.Consulta;
import modelo.Medico;

/**
 *
 * @author carlo
 */
public class MedicoJpaController implements Serializable {

    public MedicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medico medico) {
        if (medico.getHorarioMedicoList() == null) {
            medico.setHorarioMedicoList(new ArrayList<HorarioMedico>());
        }
        if (medico.getTratamientoList() == null) {
            medico.setTratamientoList(new ArrayList<Tratamiento>());
        }
        if (medico.getCitaMedicaList() == null) {
            medico.setCitaMedicaList(new ArrayList<CitaMedica>());
        }
        if (medico.getConsultaList() == null) {
            medico.setConsultaList(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = medico.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                medico.setIdUsuario(idUsuario);
            }
            List<HorarioMedico> attachedHorarioMedicoList = new ArrayList<HorarioMedico>();
            for (HorarioMedico horarioMedicoListHorarioMedicoToAttach : medico.getHorarioMedicoList()) {
                horarioMedicoListHorarioMedicoToAttach = em.getReference(horarioMedicoListHorarioMedicoToAttach.getClass(), horarioMedicoListHorarioMedicoToAttach.getIdHorario());
                attachedHorarioMedicoList.add(horarioMedicoListHorarioMedicoToAttach);
            }
            medico.setHorarioMedicoList(attachedHorarioMedicoList);
            List<Tratamiento> attachedTratamientoList = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListTratamientoToAttach : medico.getTratamientoList()) {
                tratamientoListTratamientoToAttach = em.getReference(tratamientoListTratamientoToAttach.getClass(), tratamientoListTratamientoToAttach.getIdTratamiento());
                attachedTratamientoList.add(tratamientoListTratamientoToAttach);
            }
            medico.setTratamientoList(attachedTratamientoList);
            List<CitaMedica> attachedCitaMedicaList = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListCitaMedicaToAttach : medico.getCitaMedicaList()) {
                citaMedicaListCitaMedicaToAttach = em.getReference(citaMedicaListCitaMedicaToAttach.getClass(), citaMedicaListCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaList.add(citaMedicaListCitaMedicaToAttach);
            }
            medico.setCitaMedicaList(attachedCitaMedicaList);
            List<Consulta> attachedConsultaList = new ArrayList<Consulta>();
            for (Consulta consultaListConsultaToAttach : medico.getConsultaList()) {
                consultaListConsultaToAttach = em.getReference(consultaListConsultaToAttach.getClass(), consultaListConsultaToAttach.getIdConsulta());
                attachedConsultaList.add(consultaListConsultaToAttach);
            }
            medico.setConsultaList(attachedConsultaList);
            em.persist(medico);
            if (idUsuario != null) {
                Medico oldMedicoOfIdUsuario = idUsuario.getMedico();
                if (oldMedicoOfIdUsuario != null) {
                    oldMedicoOfIdUsuario.setIdUsuario(null);
                    oldMedicoOfIdUsuario = em.merge(oldMedicoOfIdUsuario);
                }
                idUsuario.setMedico(medico);
                idUsuario = em.merge(idUsuario);
            }
            for (HorarioMedico horarioMedicoListHorarioMedico : medico.getHorarioMedicoList()) {
                Medico oldIdMedicoOfHorarioMedicoListHorarioMedico = horarioMedicoListHorarioMedico.getIdMedico();
                horarioMedicoListHorarioMedico.setIdMedico(medico);
                horarioMedicoListHorarioMedico = em.merge(horarioMedicoListHorarioMedico);
                if (oldIdMedicoOfHorarioMedicoListHorarioMedico != null) {
                    oldIdMedicoOfHorarioMedicoListHorarioMedico.getHorarioMedicoList().remove(horarioMedicoListHorarioMedico);
                    oldIdMedicoOfHorarioMedicoListHorarioMedico = em.merge(oldIdMedicoOfHorarioMedicoListHorarioMedico);
                }
            }
            for (Tratamiento tratamientoListTratamiento : medico.getTratamientoList()) {
                Medico oldIdMedicoOfTratamientoListTratamiento = tratamientoListTratamiento.getIdMedico();
                tratamientoListTratamiento.setIdMedico(medico);
                tratamientoListTratamiento = em.merge(tratamientoListTratamiento);
                if (oldIdMedicoOfTratamientoListTratamiento != null) {
                    oldIdMedicoOfTratamientoListTratamiento.getTratamientoList().remove(tratamientoListTratamiento);
                    oldIdMedicoOfTratamientoListTratamiento = em.merge(oldIdMedicoOfTratamientoListTratamiento);
                }
            }
            for (CitaMedica citaMedicaListCitaMedica : medico.getCitaMedicaList()) {
                Medico oldIdMedicoOfCitaMedicaListCitaMedica = citaMedicaListCitaMedica.getIdMedico();
                citaMedicaListCitaMedica.setIdMedico(medico);
                citaMedicaListCitaMedica = em.merge(citaMedicaListCitaMedica);
                if (oldIdMedicoOfCitaMedicaListCitaMedica != null) {
                    oldIdMedicoOfCitaMedicaListCitaMedica.getCitaMedicaList().remove(citaMedicaListCitaMedica);
                    oldIdMedicoOfCitaMedicaListCitaMedica = em.merge(oldIdMedicoOfCitaMedicaListCitaMedica);
                }
            }
            for (Consulta consultaListConsulta : medico.getConsultaList()) {
                Medico oldIdMedicoOfConsultaListConsulta = consultaListConsulta.getIdMedico();
                consultaListConsulta.setIdMedico(medico);
                consultaListConsulta = em.merge(consultaListConsulta);
                if (oldIdMedicoOfConsultaListConsulta != null) {
                    oldIdMedicoOfConsultaListConsulta.getConsultaList().remove(consultaListConsulta);
                    oldIdMedicoOfConsultaListConsulta = em.merge(oldIdMedicoOfConsultaListConsulta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medico medico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medico persistentMedico = em.find(Medico.class, medico.getIdMedico());
            Usuario idUsuarioOld = persistentMedico.getIdUsuario();
            Usuario idUsuarioNew = medico.getIdUsuario();
            List<HorarioMedico> horarioMedicoListOld = persistentMedico.getHorarioMedicoList();
            List<HorarioMedico> horarioMedicoListNew = medico.getHorarioMedicoList();
            List<Tratamiento> tratamientoListOld = persistentMedico.getTratamientoList();
            List<Tratamiento> tratamientoListNew = medico.getTratamientoList();
            List<CitaMedica> citaMedicaListOld = persistentMedico.getCitaMedicaList();
            List<CitaMedica> citaMedicaListNew = medico.getCitaMedicaList();
            List<Consulta> consultaListOld = persistentMedico.getConsultaList();
            List<Consulta> consultaListNew = medico.getConsultaList();
            List<String> illegalOrphanMessages = null;
            for (HorarioMedico horarioMedicoListOldHorarioMedico : horarioMedicoListOld) {
                if (!horarioMedicoListNew.contains(horarioMedicoListOldHorarioMedico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HorarioMedico " + horarioMedicoListOldHorarioMedico + " since its idMedico field is not nullable.");
                }
            }
            for (Tratamiento tratamientoListOldTratamiento : tratamientoListOld) {
                if (!tratamientoListNew.contains(tratamientoListOldTratamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tratamiento " + tratamientoListOldTratamiento + " since its idMedico field is not nullable.");
                }
            }
            for (CitaMedica citaMedicaListOldCitaMedica : citaMedicaListOld) {
                if (!citaMedicaListNew.contains(citaMedicaListOldCitaMedica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CitaMedica " + citaMedicaListOldCitaMedica + " since its idMedico field is not nullable.");
                }
            }
            for (Consulta consultaListOldConsulta : consultaListOld) {
                if (!consultaListNew.contains(consultaListOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaListOldConsulta + " since its idMedico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                medico.setIdUsuario(idUsuarioNew);
            }
            List<HorarioMedico> attachedHorarioMedicoListNew = new ArrayList<HorarioMedico>();
            for (HorarioMedico horarioMedicoListNewHorarioMedicoToAttach : horarioMedicoListNew) {
                horarioMedicoListNewHorarioMedicoToAttach = em.getReference(horarioMedicoListNewHorarioMedicoToAttach.getClass(), horarioMedicoListNewHorarioMedicoToAttach.getIdHorario());
                attachedHorarioMedicoListNew.add(horarioMedicoListNewHorarioMedicoToAttach);
            }
            horarioMedicoListNew = attachedHorarioMedicoListNew;
            medico.setHorarioMedicoList(horarioMedicoListNew);
            List<Tratamiento> attachedTratamientoListNew = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListNewTratamientoToAttach : tratamientoListNew) {
                tratamientoListNewTratamientoToAttach = em.getReference(tratamientoListNewTratamientoToAttach.getClass(), tratamientoListNewTratamientoToAttach.getIdTratamiento());
                attachedTratamientoListNew.add(tratamientoListNewTratamientoToAttach);
            }
            tratamientoListNew = attachedTratamientoListNew;
            medico.setTratamientoList(tratamientoListNew);
            List<CitaMedica> attachedCitaMedicaListNew = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListNewCitaMedicaToAttach : citaMedicaListNew) {
                citaMedicaListNewCitaMedicaToAttach = em.getReference(citaMedicaListNewCitaMedicaToAttach.getClass(), citaMedicaListNewCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaListNew.add(citaMedicaListNewCitaMedicaToAttach);
            }
            citaMedicaListNew = attachedCitaMedicaListNew;
            medico.setCitaMedicaList(citaMedicaListNew);
            List<Consulta> attachedConsultaListNew = new ArrayList<Consulta>();
            for (Consulta consultaListNewConsultaToAttach : consultaListNew) {
                consultaListNewConsultaToAttach = em.getReference(consultaListNewConsultaToAttach.getClass(), consultaListNewConsultaToAttach.getIdConsulta());
                attachedConsultaListNew.add(consultaListNewConsultaToAttach);
            }
            consultaListNew = attachedConsultaListNew;
            medico.setConsultaList(consultaListNew);
            medico = em.merge(medico);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.setMedico(null);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                Medico oldMedicoOfIdUsuario = idUsuarioNew.getMedico();
                if (oldMedicoOfIdUsuario != null) {
                    oldMedicoOfIdUsuario.setIdUsuario(null);
                    oldMedicoOfIdUsuario = em.merge(oldMedicoOfIdUsuario);
                }
                idUsuarioNew.setMedico(medico);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (HorarioMedico horarioMedicoListNewHorarioMedico : horarioMedicoListNew) {
                if (!horarioMedicoListOld.contains(horarioMedicoListNewHorarioMedico)) {
                    Medico oldIdMedicoOfHorarioMedicoListNewHorarioMedico = horarioMedicoListNewHorarioMedico.getIdMedico();
                    horarioMedicoListNewHorarioMedico.setIdMedico(medico);
                    horarioMedicoListNewHorarioMedico = em.merge(horarioMedicoListNewHorarioMedico);
                    if (oldIdMedicoOfHorarioMedicoListNewHorarioMedico != null && !oldIdMedicoOfHorarioMedicoListNewHorarioMedico.equals(medico)) {
                        oldIdMedicoOfHorarioMedicoListNewHorarioMedico.getHorarioMedicoList().remove(horarioMedicoListNewHorarioMedico);
                        oldIdMedicoOfHorarioMedicoListNewHorarioMedico = em.merge(oldIdMedicoOfHorarioMedicoListNewHorarioMedico);
                    }
                }
            }
            for (Tratamiento tratamientoListNewTratamiento : tratamientoListNew) {
                if (!tratamientoListOld.contains(tratamientoListNewTratamiento)) {
                    Medico oldIdMedicoOfTratamientoListNewTratamiento = tratamientoListNewTratamiento.getIdMedico();
                    tratamientoListNewTratamiento.setIdMedico(medico);
                    tratamientoListNewTratamiento = em.merge(tratamientoListNewTratamiento);
                    if (oldIdMedicoOfTratamientoListNewTratamiento != null && !oldIdMedicoOfTratamientoListNewTratamiento.equals(medico)) {
                        oldIdMedicoOfTratamientoListNewTratamiento.getTratamientoList().remove(tratamientoListNewTratamiento);
                        oldIdMedicoOfTratamientoListNewTratamiento = em.merge(oldIdMedicoOfTratamientoListNewTratamiento);
                    }
                }
            }
            for (CitaMedica citaMedicaListNewCitaMedica : citaMedicaListNew) {
                if (!citaMedicaListOld.contains(citaMedicaListNewCitaMedica)) {
                    Medico oldIdMedicoOfCitaMedicaListNewCitaMedica = citaMedicaListNewCitaMedica.getIdMedico();
                    citaMedicaListNewCitaMedica.setIdMedico(medico);
                    citaMedicaListNewCitaMedica = em.merge(citaMedicaListNewCitaMedica);
                    if (oldIdMedicoOfCitaMedicaListNewCitaMedica != null && !oldIdMedicoOfCitaMedicaListNewCitaMedica.equals(medico)) {
                        oldIdMedicoOfCitaMedicaListNewCitaMedica.getCitaMedicaList().remove(citaMedicaListNewCitaMedica);
                        oldIdMedicoOfCitaMedicaListNewCitaMedica = em.merge(oldIdMedicoOfCitaMedicaListNewCitaMedica);
                    }
                }
            }
            for (Consulta consultaListNewConsulta : consultaListNew) {
                if (!consultaListOld.contains(consultaListNewConsulta)) {
                    Medico oldIdMedicoOfConsultaListNewConsulta = consultaListNewConsulta.getIdMedico();
                    consultaListNewConsulta.setIdMedico(medico);
                    consultaListNewConsulta = em.merge(consultaListNewConsulta);
                    if (oldIdMedicoOfConsultaListNewConsulta != null && !oldIdMedicoOfConsultaListNewConsulta.equals(medico)) {
                        oldIdMedicoOfConsultaListNewConsulta.getConsultaList().remove(consultaListNewConsulta);
                        oldIdMedicoOfConsultaListNewConsulta = em.merge(oldIdMedicoOfConsultaListNewConsulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medico.getIdMedico();
                if (findMedico(id) == null) {
                    throw new NonexistentEntityException("The medico with id " + id + " no longer exists.");
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
            Medico medico;
            try {
                medico = em.getReference(Medico.class, id);
                medico.getIdMedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<HorarioMedico> horarioMedicoListOrphanCheck = medico.getHorarioMedicoList();
            for (HorarioMedico horarioMedicoListOrphanCheckHorarioMedico : horarioMedicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the HorarioMedico " + horarioMedicoListOrphanCheckHorarioMedico + " in its horarioMedicoList field has a non-nullable idMedico field.");
            }
            List<Tratamiento> tratamientoListOrphanCheck = medico.getTratamientoList();
            for (Tratamiento tratamientoListOrphanCheckTratamiento : tratamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the Tratamiento " + tratamientoListOrphanCheckTratamiento + " in its tratamientoList field has a non-nullable idMedico field.");
            }
            List<CitaMedica> citaMedicaListOrphanCheck = medico.getCitaMedicaList();
            for (CitaMedica citaMedicaListOrphanCheckCitaMedica : citaMedicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the CitaMedica " + citaMedicaListOrphanCheckCitaMedica + " in its citaMedicaList field has a non-nullable idMedico field.");
            }
            List<Consulta> consultaListOrphanCheck = medico.getConsultaList();
            for (Consulta consultaListOrphanCheckConsulta : consultaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the Consulta " + consultaListOrphanCheckConsulta + " in its consultaList field has a non-nullable idMedico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = medico.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.setMedico(null);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(medico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medico> findMedicoEntities() {
        return findMedicoEntities(true, -1, -1);
    }

    public List<Medico> findMedicoEntities(int maxResults, int firstResult) {
        return findMedicoEntities(false, maxResults, firstResult);
    }

    private List<Medico> findMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medico.class));
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

    public Medico findMedico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medico.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medico> rt = cq.from(Medico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
