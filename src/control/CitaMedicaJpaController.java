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
import modelo.Tratamiento;
import modelo.HorarioMedico;
import modelo.Medico;
import modelo.Paciente;
import modelo.Consulta;
import modelo.RecetaMedica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.CitaMedica;

/**
 *
 * @author carlo
 */
public class CitaMedicaJpaController implements Serializable {

    public CitaMedicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CitaMedica citaMedica) {
        if (citaMedica.getRecetaMedicaList() == null) {
            citaMedica.setRecetaMedicaList(new ArrayList<RecetaMedica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago = citaMedica.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getIdPago());
                citaMedica.setPago(pago);
            }
            Tratamiento tratamiento = citaMedica.getTratamiento();
            if (tratamiento != null) {
                tratamiento = em.getReference(tratamiento.getClass(), tratamiento.getIdTratamiento());
                citaMedica.setTratamiento(tratamiento);
            }
            HorarioMedico idHorario = citaMedica.getIdHorario();
            if (idHorario != null) {
                idHorario = em.getReference(idHorario.getClass(), idHorario.getIdHorario());
                citaMedica.setIdHorario(idHorario);
            }
            Medico idMedico = citaMedica.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                citaMedica.setIdMedico(idMedico);
            }
            Paciente idPaciente = citaMedica.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                citaMedica.setIdPaciente(idPaciente);
            }
            Consulta consulta = citaMedica.getConsulta();
            if (consulta != null) {
                consulta = em.getReference(consulta.getClass(), consulta.getIdConsulta());
                citaMedica.setConsulta(consulta);
            }
            List<RecetaMedica> attachedRecetaMedicaList = new ArrayList<RecetaMedica>();
            for (RecetaMedica recetaMedicaListRecetaMedicaToAttach : citaMedica.getRecetaMedicaList()) {
                recetaMedicaListRecetaMedicaToAttach = em.getReference(recetaMedicaListRecetaMedicaToAttach.getClass(), recetaMedicaListRecetaMedicaToAttach.getIdReceta());
                attachedRecetaMedicaList.add(recetaMedicaListRecetaMedicaToAttach);
            }
            citaMedica.setRecetaMedicaList(attachedRecetaMedicaList);
            em.persist(citaMedica);
            if (pago != null) {
                CitaMedica oldIdCitaOfPago = pago.getIdCita();
                if (oldIdCitaOfPago != null) {
                    oldIdCitaOfPago.setPago(null);
                    oldIdCitaOfPago = em.merge(oldIdCitaOfPago);
                }
                pago.setIdCita(citaMedica);
                pago = em.merge(pago);
            }
            if (tratamiento != null) {
                CitaMedica oldIdCitaOfTratamiento = tratamiento.getIdCita();
                if (oldIdCitaOfTratamiento != null) {
                    oldIdCitaOfTratamiento.setTratamiento(null);
                    oldIdCitaOfTratamiento = em.merge(oldIdCitaOfTratamiento);
                }
                tratamiento.setIdCita(citaMedica);
                tratamiento = em.merge(tratamiento);
            }
            if (idHorario != null) {
                idHorario.getCitaMedicaList().add(citaMedica);
                idHorario = em.merge(idHorario);
            }
            if (idMedico != null) {
                idMedico.getCitaMedicaList().add(citaMedica);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getCitaMedicaList().add(citaMedica);
                idPaciente = em.merge(idPaciente);
            }
            if (consulta != null) {
                CitaMedica oldIdCitaOfConsulta = consulta.getIdCita();
                if (oldIdCitaOfConsulta != null) {
                    oldIdCitaOfConsulta.setConsulta(null);
                    oldIdCitaOfConsulta = em.merge(oldIdCitaOfConsulta);
                }
                consulta.setIdCita(citaMedica);
                consulta = em.merge(consulta);
            }
            for (RecetaMedica recetaMedicaListRecetaMedica : citaMedica.getRecetaMedicaList()) {
                CitaMedica oldIdCitaOfRecetaMedicaListRecetaMedica = recetaMedicaListRecetaMedica.getIdCita();
                recetaMedicaListRecetaMedica.setIdCita(citaMedica);
                recetaMedicaListRecetaMedica = em.merge(recetaMedicaListRecetaMedica);
                if (oldIdCitaOfRecetaMedicaListRecetaMedica != null) {
                    oldIdCitaOfRecetaMedicaListRecetaMedica.getRecetaMedicaList().remove(recetaMedicaListRecetaMedica);
                    oldIdCitaOfRecetaMedicaListRecetaMedica = em.merge(oldIdCitaOfRecetaMedicaListRecetaMedica);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CitaMedica citaMedica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CitaMedica persistentCitaMedica = em.find(CitaMedica.class, citaMedica.getIdCita());
            Pago pagoOld = persistentCitaMedica.getPago();
            Pago pagoNew = citaMedica.getPago();
            Tratamiento tratamientoOld = persistentCitaMedica.getTratamiento();
            Tratamiento tratamientoNew = citaMedica.getTratamiento();
            HorarioMedico idHorarioOld = persistentCitaMedica.getIdHorario();
            HorarioMedico idHorarioNew = citaMedica.getIdHorario();
            Medico idMedicoOld = persistentCitaMedica.getIdMedico();
            Medico idMedicoNew = citaMedica.getIdMedico();
            Paciente idPacienteOld = persistentCitaMedica.getIdPaciente();
            Paciente idPacienteNew = citaMedica.getIdPaciente();
            Consulta consultaOld = persistentCitaMedica.getConsulta();
            Consulta consultaNew = citaMedica.getConsulta();
            List<RecetaMedica> recetaMedicaListOld = persistentCitaMedica.getRecetaMedicaList();
            List<RecetaMedica> recetaMedicaListNew = citaMedica.getRecetaMedicaList();
            List<String> illegalOrphanMessages = null;
            if (tratamientoOld != null && !tratamientoOld.equals(tratamientoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tratamiento " + tratamientoOld + " since its idCita field is not nullable.");
            }
            if (consultaOld != null && !consultaOld.equals(consultaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Consulta " + consultaOld + " since its idCita field is not nullable.");
            }
            for (RecetaMedica recetaMedicaListOldRecetaMedica : recetaMedicaListOld) {
                if (!recetaMedicaListNew.contains(recetaMedicaListOldRecetaMedica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecetaMedica " + recetaMedicaListOldRecetaMedica + " since its idCita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getIdPago());
                citaMedica.setPago(pagoNew);
            }
            if (tratamientoNew != null) {
                tratamientoNew = em.getReference(tratamientoNew.getClass(), tratamientoNew.getIdTratamiento());
                citaMedica.setTratamiento(tratamientoNew);
            }
            if (idHorarioNew != null) {
                idHorarioNew = em.getReference(idHorarioNew.getClass(), idHorarioNew.getIdHorario());
                citaMedica.setIdHorario(idHorarioNew);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                citaMedica.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                citaMedica.setIdPaciente(idPacienteNew);
            }
            if (consultaNew != null) {
                consultaNew = em.getReference(consultaNew.getClass(), consultaNew.getIdConsulta());
                citaMedica.setConsulta(consultaNew);
            }
            List<RecetaMedica> attachedRecetaMedicaListNew = new ArrayList<RecetaMedica>();
            for (RecetaMedica recetaMedicaListNewRecetaMedicaToAttach : recetaMedicaListNew) {
                recetaMedicaListNewRecetaMedicaToAttach = em.getReference(recetaMedicaListNewRecetaMedicaToAttach.getClass(), recetaMedicaListNewRecetaMedicaToAttach.getIdReceta());
                attachedRecetaMedicaListNew.add(recetaMedicaListNewRecetaMedicaToAttach);
            }
            recetaMedicaListNew = attachedRecetaMedicaListNew;
            citaMedica.setRecetaMedicaList(recetaMedicaListNew);
            citaMedica = em.merge(citaMedica);
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.setIdCita(null);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                CitaMedica oldIdCitaOfPago = pagoNew.getIdCita();
                if (oldIdCitaOfPago != null) {
                    oldIdCitaOfPago.setPago(null);
                    oldIdCitaOfPago = em.merge(oldIdCitaOfPago);
                }
                pagoNew.setIdCita(citaMedica);
                pagoNew = em.merge(pagoNew);
            }
            if (tratamientoNew != null && !tratamientoNew.equals(tratamientoOld)) {
                CitaMedica oldIdCitaOfTratamiento = tratamientoNew.getIdCita();
                if (oldIdCitaOfTratamiento != null) {
                    oldIdCitaOfTratamiento.setTratamiento(null);
                    oldIdCitaOfTratamiento = em.merge(oldIdCitaOfTratamiento);
                }
                tratamientoNew.setIdCita(citaMedica);
                tratamientoNew = em.merge(tratamientoNew);
            }
            if (idHorarioOld != null && !idHorarioOld.equals(idHorarioNew)) {
                idHorarioOld.getCitaMedicaList().remove(citaMedica);
                idHorarioOld = em.merge(idHorarioOld);
            }
            if (idHorarioNew != null && !idHorarioNew.equals(idHorarioOld)) {
                idHorarioNew.getCitaMedicaList().add(citaMedica);
                idHorarioNew = em.merge(idHorarioNew);
            }
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getCitaMedicaList().remove(citaMedica);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getCitaMedicaList().add(citaMedica);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getCitaMedicaList().remove(citaMedica);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getCitaMedicaList().add(citaMedica);
                idPacienteNew = em.merge(idPacienteNew);
            }
            if (consultaNew != null && !consultaNew.equals(consultaOld)) {
                CitaMedica oldIdCitaOfConsulta = consultaNew.getIdCita();
                if (oldIdCitaOfConsulta != null) {
                    oldIdCitaOfConsulta.setConsulta(null);
                    oldIdCitaOfConsulta = em.merge(oldIdCitaOfConsulta);
                }
                consultaNew.setIdCita(citaMedica);
                consultaNew = em.merge(consultaNew);
            }
            for (RecetaMedica recetaMedicaListNewRecetaMedica : recetaMedicaListNew) {
                if (!recetaMedicaListOld.contains(recetaMedicaListNewRecetaMedica)) {
                    CitaMedica oldIdCitaOfRecetaMedicaListNewRecetaMedica = recetaMedicaListNewRecetaMedica.getIdCita();
                    recetaMedicaListNewRecetaMedica.setIdCita(citaMedica);
                    recetaMedicaListNewRecetaMedica = em.merge(recetaMedicaListNewRecetaMedica);
                    if (oldIdCitaOfRecetaMedicaListNewRecetaMedica != null && !oldIdCitaOfRecetaMedicaListNewRecetaMedica.equals(citaMedica)) {
                        oldIdCitaOfRecetaMedicaListNewRecetaMedica.getRecetaMedicaList().remove(recetaMedicaListNewRecetaMedica);
                        oldIdCitaOfRecetaMedicaListNewRecetaMedica = em.merge(oldIdCitaOfRecetaMedicaListNewRecetaMedica);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citaMedica.getIdCita();
                if (findCitaMedica(id) == null) {
                    throw new NonexistentEntityException("The citaMedica with id " + id + " no longer exists.");
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
            CitaMedica citaMedica;
            try {
                citaMedica = em.getReference(CitaMedica.class, id);
                citaMedica.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citaMedica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Tratamiento tratamientoOrphanCheck = citaMedica.getTratamiento();
            if (tratamientoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CitaMedica (" + citaMedica + ") cannot be destroyed since the Tratamiento " + tratamientoOrphanCheck + " in its tratamiento field has a non-nullable idCita field.");
            }
            Consulta consultaOrphanCheck = citaMedica.getConsulta();
            if (consultaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CitaMedica (" + citaMedica + ") cannot be destroyed since the Consulta " + consultaOrphanCheck + " in its consulta field has a non-nullable idCita field.");
            }
            List<RecetaMedica> recetaMedicaListOrphanCheck = citaMedica.getRecetaMedicaList();
            for (RecetaMedica recetaMedicaListOrphanCheckRecetaMedica : recetaMedicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CitaMedica (" + citaMedica + ") cannot be destroyed since the RecetaMedica " + recetaMedicaListOrphanCheckRecetaMedica + " in its recetaMedicaList field has a non-nullable idCita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pago pago = citaMedica.getPago();
            if (pago != null) {
                pago.setIdCita(null);
                pago = em.merge(pago);
            }
            HorarioMedico idHorario = citaMedica.getIdHorario();
            if (idHorario != null) {
                idHorario.getCitaMedicaList().remove(citaMedica);
                idHorario = em.merge(idHorario);
            }
            Medico idMedico = citaMedica.getIdMedico();
            if (idMedico != null) {
                idMedico.getCitaMedicaList().remove(citaMedica);
                idMedico = em.merge(idMedico);
            }
            Paciente idPaciente = citaMedica.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getCitaMedicaList().remove(citaMedica);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(citaMedica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CitaMedica> findCitaMedicaEntities() {
        return findCitaMedicaEntities(true, -1, -1);
    }

    public List<CitaMedica> findCitaMedicaEntities(int maxResults, int firstResult) {
        return findCitaMedicaEntities(false, maxResults, firstResult);
    }

    private List<CitaMedica> findCitaMedicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CitaMedica.class));
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

    public CitaMedica findCitaMedica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CitaMedica.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaMedicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CitaMedica> rt = cq.from(CitaMedica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
