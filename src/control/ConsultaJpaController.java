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
import modelo.Medico;
import modelo.Paciente;
import modelo.ConsultaDiagnostico;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Consulta;

/**
 *
 * @author carlo
 */
public class ConsultaJpaController implements Serializable {

    public ConsultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consulta consulta) throws IllegalOrphanException {
        if (consulta.getConsultaDiagnosticoList() == null) {
            consulta.setConsultaDiagnosticoList(new ArrayList<ConsultaDiagnostico>());
        }
        List<String> illegalOrphanMessages = null;
        CitaMedica idCitaOrphanCheck = consulta.getIdCita();
        if (idCitaOrphanCheck != null) {
            Consulta oldConsultaOfIdCita = idCitaOrphanCheck.getConsulta();
            if (oldConsultaOfIdCita != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The CitaMedica " + idCitaOrphanCheck + " already has an item of type Consulta whose idCita column cannot be null. Please make another selection for the idCita field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CitaMedica idCita = consulta.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                consulta.setIdCita(idCita);
            }
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                consulta.setIdMedico(idMedico);
            }
            Paciente idPaciente = consulta.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                consulta.setIdPaciente(idPaciente);
            }
            List<ConsultaDiagnostico> attachedConsultaDiagnosticoList = new ArrayList<ConsultaDiagnostico>();
            for (ConsultaDiagnostico consultaDiagnosticoListConsultaDiagnosticoToAttach : consulta.getConsultaDiagnosticoList()) {
                consultaDiagnosticoListConsultaDiagnosticoToAttach = em.getReference(consultaDiagnosticoListConsultaDiagnosticoToAttach.getClass(), consultaDiagnosticoListConsultaDiagnosticoToAttach.getIdConsultaDiagnostico());
                attachedConsultaDiagnosticoList.add(consultaDiagnosticoListConsultaDiagnosticoToAttach);
            }
            consulta.setConsultaDiagnosticoList(attachedConsultaDiagnosticoList);
            em.persist(consulta);
            if (idCita != null) {
                idCita.setConsulta(consulta);
                idCita = em.merge(idCita);
            }
            if (idMedico != null) {
                idMedico.getConsultaList().add(consulta);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getConsultaList().add(consulta);
                idPaciente = em.merge(idPaciente);
            }
            for (ConsultaDiagnostico consultaDiagnosticoListConsultaDiagnostico : consulta.getConsultaDiagnosticoList()) {
                Consulta oldIdConsultaOfConsultaDiagnosticoListConsultaDiagnostico = consultaDiagnosticoListConsultaDiagnostico.getIdConsulta();
                consultaDiagnosticoListConsultaDiagnostico.setIdConsulta(consulta);
                consultaDiagnosticoListConsultaDiagnostico = em.merge(consultaDiagnosticoListConsultaDiagnostico);
                if (oldIdConsultaOfConsultaDiagnosticoListConsultaDiagnostico != null) {
                    oldIdConsultaOfConsultaDiagnosticoListConsultaDiagnostico.getConsultaDiagnosticoList().remove(consultaDiagnosticoListConsultaDiagnostico);
                    oldIdConsultaOfConsultaDiagnosticoListConsultaDiagnostico = em.merge(oldIdConsultaOfConsultaDiagnosticoListConsultaDiagnostico);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consulta consulta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consulta persistentConsulta = em.find(Consulta.class, consulta.getIdConsulta());
            CitaMedica idCitaOld = persistentConsulta.getIdCita();
            CitaMedica idCitaNew = consulta.getIdCita();
            Medico idMedicoOld = persistentConsulta.getIdMedico();
            Medico idMedicoNew = consulta.getIdMedico();
            Paciente idPacienteOld = persistentConsulta.getIdPaciente();
            Paciente idPacienteNew = consulta.getIdPaciente();
            List<ConsultaDiagnostico> consultaDiagnosticoListOld = persistentConsulta.getConsultaDiagnosticoList();
            List<ConsultaDiagnostico> consultaDiagnosticoListNew = consulta.getConsultaDiagnosticoList();
            List<String> illegalOrphanMessages = null;
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                Consulta oldConsultaOfIdCita = idCitaNew.getConsulta();
                if (oldConsultaOfIdCita != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The CitaMedica " + idCitaNew + " already has an item of type Consulta whose idCita column cannot be null. Please make another selection for the idCita field.");
                }
            }
            for (ConsultaDiagnostico consultaDiagnosticoListOldConsultaDiagnostico : consultaDiagnosticoListOld) {
                if (!consultaDiagnosticoListNew.contains(consultaDiagnosticoListOldConsultaDiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConsultaDiagnostico " + consultaDiagnosticoListOldConsultaDiagnostico + " since its idConsulta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                consulta.setIdCita(idCitaNew);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                consulta.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                consulta.setIdPaciente(idPacienteNew);
            }
            List<ConsultaDiagnostico> attachedConsultaDiagnosticoListNew = new ArrayList<ConsultaDiagnostico>();
            for (ConsultaDiagnostico consultaDiagnosticoListNewConsultaDiagnosticoToAttach : consultaDiagnosticoListNew) {
                consultaDiagnosticoListNewConsultaDiagnosticoToAttach = em.getReference(consultaDiagnosticoListNewConsultaDiagnosticoToAttach.getClass(), consultaDiagnosticoListNewConsultaDiagnosticoToAttach.getIdConsultaDiagnostico());
                attachedConsultaDiagnosticoListNew.add(consultaDiagnosticoListNewConsultaDiagnosticoToAttach);
            }
            consultaDiagnosticoListNew = attachedConsultaDiagnosticoListNew;
            consulta.setConsultaDiagnosticoList(consultaDiagnosticoListNew);
            consulta = em.merge(consulta);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.setConsulta(null);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.setConsulta(consulta);
                idCitaNew = em.merge(idCitaNew);
            }
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getConsultaList().remove(consulta);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getConsultaList().add(consulta);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getConsultaList().remove(consulta);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getConsultaList().add(consulta);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (ConsultaDiagnostico consultaDiagnosticoListNewConsultaDiagnostico : consultaDiagnosticoListNew) {
                if (!consultaDiagnosticoListOld.contains(consultaDiagnosticoListNewConsultaDiagnostico)) {
                    Consulta oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico = consultaDiagnosticoListNewConsultaDiagnostico.getIdConsulta();
                    consultaDiagnosticoListNewConsultaDiagnostico.setIdConsulta(consulta);
                    consultaDiagnosticoListNewConsultaDiagnostico = em.merge(consultaDiagnosticoListNewConsultaDiagnostico);
                    if (oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico != null && !oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico.equals(consulta)) {
                        oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico.getConsultaDiagnosticoList().remove(consultaDiagnosticoListNewConsultaDiagnostico);
                        oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico = em.merge(oldIdConsultaOfConsultaDiagnosticoListNewConsultaDiagnostico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consulta.getIdConsulta();
                if (findConsulta(id) == null) {
                    throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.");
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
            Consulta consulta;
            try {
                consulta = em.getReference(Consulta.class, id);
                consulta.getIdConsulta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConsultaDiagnostico> consultaDiagnosticoListOrphanCheck = consulta.getConsultaDiagnosticoList();
            for (ConsultaDiagnostico consultaDiagnosticoListOrphanCheckConsultaDiagnostico : consultaDiagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the ConsultaDiagnostico " + consultaDiagnosticoListOrphanCheckConsultaDiagnostico + " in its consultaDiagnosticoList field has a non-nullable idConsulta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CitaMedica idCita = consulta.getIdCita();
            if (idCita != null) {
                idCita.setConsulta(null);
                idCita = em.merge(idCita);
            }
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico.getConsultaList().remove(consulta);
                idMedico = em.merge(idMedico);
            }
            Paciente idPaciente = consulta.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getConsultaList().remove(consulta);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(consulta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consulta> findConsultaEntities() {
        return findConsultaEntities(true, -1, -1);
    }

    public List<Consulta> findConsultaEntities(int maxResults, int firstResult) {
        return findConsultaEntities(false, maxResults, firstResult);
    }

    private List<Consulta> findConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consulta.class));
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

    public Consulta findConsulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consulta> rt = cq.from(Consulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
