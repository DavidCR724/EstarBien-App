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
import modelo.ExpedienteMedico;
import modelo.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Tratamiento;
import modelo.CitaMedica;
import modelo.Consulta;
import modelo.Paciente;

/**
 *
 * @author carlo
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) {
        if (paciente.getPagoList() == null) {
            paciente.setPagoList(new ArrayList<Pago>());
        }
        if (paciente.getTratamientoList() == null) {
            paciente.setTratamientoList(new ArrayList<Tratamiento>());
        }
        if (paciente.getCitaMedicaList() == null) {
            paciente.setCitaMedicaList(new ArrayList<CitaMedica>());
        }
        if (paciente.getConsultaList() == null) {
            paciente.setConsultaList(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = paciente.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                paciente.setIdUsuario(idUsuario);
            }
            ExpedienteMedico expedienteMedico = paciente.getExpedienteMedico();
            if (expedienteMedico != null) {
                expedienteMedico = em.getReference(expedienteMedico.getClass(), expedienteMedico.getIdExpediente());
                paciente.setExpedienteMedico(expedienteMedico);
            }
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : paciente.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getIdPago());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            paciente.setPagoList(attachedPagoList);
            List<Tratamiento> attachedTratamientoList = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListTratamientoToAttach : paciente.getTratamientoList()) {
                tratamientoListTratamientoToAttach = em.getReference(tratamientoListTratamientoToAttach.getClass(), tratamientoListTratamientoToAttach.getIdTratamiento());
                attachedTratamientoList.add(tratamientoListTratamientoToAttach);
            }
            paciente.setTratamientoList(attachedTratamientoList);
            List<CitaMedica> attachedCitaMedicaList = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListCitaMedicaToAttach : paciente.getCitaMedicaList()) {
                citaMedicaListCitaMedicaToAttach = em.getReference(citaMedicaListCitaMedicaToAttach.getClass(), citaMedicaListCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaList.add(citaMedicaListCitaMedicaToAttach);
            }
            paciente.setCitaMedicaList(attachedCitaMedicaList);
            List<Consulta> attachedConsultaList = new ArrayList<Consulta>();
            for (Consulta consultaListConsultaToAttach : paciente.getConsultaList()) {
                consultaListConsultaToAttach = em.getReference(consultaListConsultaToAttach.getClass(), consultaListConsultaToAttach.getIdConsulta());
                attachedConsultaList.add(consultaListConsultaToAttach);
            }
            paciente.setConsultaList(attachedConsultaList);
            em.persist(paciente);
            if (idUsuario != null) {
                Paciente oldPacienteOfIdUsuario = idUsuario.getPaciente();
                if (oldPacienteOfIdUsuario != null) {
                    oldPacienteOfIdUsuario.setIdUsuario(null);
                    oldPacienteOfIdUsuario = em.merge(oldPacienteOfIdUsuario);
                }
                idUsuario.setPaciente(paciente);
                idUsuario = em.merge(idUsuario);
            }
            if (expedienteMedico != null) {
                Paciente oldIdPacienteOfExpedienteMedico = expedienteMedico.getIdPaciente();
                if (oldIdPacienteOfExpedienteMedico != null) {
                    oldIdPacienteOfExpedienteMedico.setExpedienteMedico(null);
                    oldIdPacienteOfExpedienteMedico = em.merge(oldIdPacienteOfExpedienteMedico);
                }
                expedienteMedico.setIdPaciente(paciente);
                expedienteMedico = em.merge(expedienteMedico);
            }
            for (Pago pagoListPago : paciente.getPagoList()) {
                Paciente oldIdPacienteOfPagoListPago = pagoListPago.getIdPaciente();
                pagoListPago.setIdPaciente(paciente);
                pagoListPago = em.merge(pagoListPago);
                if (oldIdPacienteOfPagoListPago != null) {
                    oldIdPacienteOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldIdPacienteOfPagoListPago = em.merge(oldIdPacienteOfPagoListPago);
                }
            }
            for (Tratamiento tratamientoListTratamiento : paciente.getTratamientoList()) {
                Paciente oldIdPacienteOfTratamientoListTratamiento = tratamientoListTratamiento.getIdPaciente();
                tratamientoListTratamiento.setIdPaciente(paciente);
                tratamientoListTratamiento = em.merge(tratamientoListTratamiento);
                if (oldIdPacienteOfTratamientoListTratamiento != null) {
                    oldIdPacienteOfTratamientoListTratamiento.getTratamientoList().remove(tratamientoListTratamiento);
                    oldIdPacienteOfTratamientoListTratamiento = em.merge(oldIdPacienteOfTratamientoListTratamiento);
                }
            }
            for (CitaMedica citaMedicaListCitaMedica : paciente.getCitaMedicaList()) {
                Paciente oldIdPacienteOfCitaMedicaListCitaMedica = citaMedicaListCitaMedica.getIdPaciente();
                citaMedicaListCitaMedica.setIdPaciente(paciente);
                citaMedicaListCitaMedica = em.merge(citaMedicaListCitaMedica);
                if (oldIdPacienteOfCitaMedicaListCitaMedica != null) {
                    oldIdPacienteOfCitaMedicaListCitaMedica.getCitaMedicaList().remove(citaMedicaListCitaMedica);
                    oldIdPacienteOfCitaMedicaListCitaMedica = em.merge(oldIdPacienteOfCitaMedicaListCitaMedica);
                }
            }
            for (Consulta consultaListConsulta : paciente.getConsultaList()) {
                Paciente oldIdPacienteOfConsultaListConsulta = consultaListConsulta.getIdPaciente();
                consultaListConsulta.setIdPaciente(paciente);
                consultaListConsulta = em.merge(consultaListConsulta);
                if (oldIdPacienteOfConsultaListConsulta != null) {
                    oldIdPacienteOfConsultaListConsulta.getConsultaList().remove(consultaListConsulta);
                    oldIdPacienteOfConsultaListConsulta = em.merge(oldIdPacienteOfConsultaListConsulta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getIdPaciente());
            Usuario idUsuarioOld = persistentPaciente.getIdUsuario();
            Usuario idUsuarioNew = paciente.getIdUsuario();
            ExpedienteMedico expedienteMedicoOld = persistentPaciente.getExpedienteMedico();
            ExpedienteMedico expedienteMedicoNew = paciente.getExpedienteMedico();
            List<Pago> pagoListOld = persistentPaciente.getPagoList();
            List<Pago> pagoListNew = paciente.getPagoList();
            List<Tratamiento> tratamientoListOld = persistentPaciente.getTratamientoList();
            List<Tratamiento> tratamientoListNew = paciente.getTratamientoList();
            List<CitaMedica> citaMedicaListOld = persistentPaciente.getCitaMedicaList();
            List<CitaMedica> citaMedicaListNew = paciente.getCitaMedicaList();
            List<Consulta> consultaListOld = persistentPaciente.getConsultaList();
            List<Consulta> consultaListNew = paciente.getConsultaList();
            List<String> illegalOrphanMessages = null;
            if (expedienteMedicoOld != null && !expedienteMedicoOld.equals(expedienteMedicoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain ExpedienteMedico " + expedienteMedicoOld + " since its idPaciente field is not nullable.");
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoListOldPago + " since its idPaciente field is not nullable.");
                }
            }
            for (Tratamiento tratamientoListOldTratamiento : tratamientoListOld) {
                if (!tratamientoListNew.contains(tratamientoListOldTratamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tratamiento " + tratamientoListOldTratamiento + " since its idPaciente field is not nullable.");
                }
            }
            for (CitaMedica citaMedicaListOldCitaMedica : citaMedicaListOld) {
                if (!citaMedicaListNew.contains(citaMedicaListOldCitaMedica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CitaMedica " + citaMedicaListOldCitaMedica + " since its idPaciente field is not nullable.");
                }
            }
            for (Consulta consultaListOldConsulta : consultaListOld) {
                if (!consultaListNew.contains(consultaListOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaListOldConsulta + " since its idPaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                paciente.setIdUsuario(idUsuarioNew);
            }
            if (expedienteMedicoNew != null) {
                expedienteMedicoNew = em.getReference(expedienteMedicoNew.getClass(), expedienteMedicoNew.getIdExpediente());
                paciente.setExpedienteMedico(expedienteMedicoNew);
            }
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getIdPago());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            paciente.setPagoList(pagoListNew);
            List<Tratamiento> attachedTratamientoListNew = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListNewTratamientoToAttach : tratamientoListNew) {
                tratamientoListNewTratamientoToAttach = em.getReference(tratamientoListNewTratamientoToAttach.getClass(), tratamientoListNewTratamientoToAttach.getIdTratamiento());
                attachedTratamientoListNew.add(tratamientoListNewTratamientoToAttach);
            }
            tratamientoListNew = attachedTratamientoListNew;
            paciente.setTratamientoList(tratamientoListNew);
            List<CitaMedica> attachedCitaMedicaListNew = new ArrayList<CitaMedica>();
            for (CitaMedica citaMedicaListNewCitaMedicaToAttach : citaMedicaListNew) {
                citaMedicaListNewCitaMedicaToAttach = em.getReference(citaMedicaListNewCitaMedicaToAttach.getClass(), citaMedicaListNewCitaMedicaToAttach.getIdCita());
                attachedCitaMedicaListNew.add(citaMedicaListNewCitaMedicaToAttach);
            }
            citaMedicaListNew = attachedCitaMedicaListNew;
            paciente.setCitaMedicaList(citaMedicaListNew);
            List<Consulta> attachedConsultaListNew = new ArrayList<Consulta>();
            for (Consulta consultaListNewConsultaToAttach : consultaListNew) {
                consultaListNewConsultaToAttach = em.getReference(consultaListNewConsultaToAttach.getClass(), consultaListNewConsultaToAttach.getIdConsulta());
                attachedConsultaListNew.add(consultaListNewConsultaToAttach);
            }
            consultaListNew = attachedConsultaListNew;
            paciente.setConsultaList(consultaListNew);
            paciente = em.merge(paciente);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.setPaciente(null);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                Paciente oldPacienteOfIdUsuario = idUsuarioNew.getPaciente();
                if (oldPacienteOfIdUsuario != null) {
                    oldPacienteOfIdUsuario.setIdUsuario(null);
                    oldPacienteOfIdUsuario = em.merge(oldPacienteOfIdUsuario);
                }
                idUsuarioNew.setPaciente(paciente);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (expedienteMedicoNew != null && !expedienteMedicoNew.equals(expedienteMedicoOld)) {
                Paciente oldIdPacienteOfExpedienteMedico = expedienteMedicoNew.getIdPaciente();
                if (oldIdPacienteOfExpedienteMedico != null) {
                    oldIdPacienteOfExpedienteMedico.setExpedienteMedico(null);
                    oldIdPacienteOfExpedienteMedico = em.merge(oldIdPacienteOfExpedienteMedico);
                }
                expedienteMedicoNew.setIdPaciente(paciente);
                expedienteMedicoNew = em.merge(expedienteMedicoNew);
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Paciente oldIdPacienteOfPagoListNewPago = pagoListNewPago.getIdPaciente();
                    pagoListNewPago.setIdPaciente(paciente);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldIdPacienteOfPagoListNewPago != null && !oldIdPacienteOfPagoListNewPago.equals(paciente)) {
                        oldIdPacienteOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldIdPacienteOfPagoListNewPago = em.merge(oldIdPacienteOfPagoListNewPago);
                    }
                }
            }
            for (Tratamiento tratamientoListNewTratamiento : tratamientoListNew) {
                if (!tratamientoListOld.contains(tratamientoListNewTratamiento)) {
                    Paciente oldIdPacienteOfTratamientoListNewTratamiento = tratamientoListNewTratamiento.getIdPaciente();
                    tratamientoListNewTratamiento.setIdPaciente(paciente);
                    tratamientoListNewTratamiento = em.merge(tratamientoListNewTratamiento);
                    if (oldIdPacienteOfTratamientoListNewTratamiento != null && !oldIdPacienteOfTratamientoListNewTratamiento.equals(paciente)) {
                        oldIdPacienteOfTratamientoListNewTratamiento.getTratamientoList().remove(tratamientoListNewTratamiento);
                        oldIdPacienteOfTratamientoListNewTratamiento = em.merge(oldIdPacienteOfTratamientoListNewTratamiento);
                    }
                }
            }
            for (CitaMedica citaMedicaListNewCitaMedica : citaMedicaListNew) {
                if (!citaMedicaListOld.contains(citaMedicaListNewCitaMedica)) {
                    Paciente oldIdPacienteOfCitaMedicaListNewCitaMedica = citaMedicaListNewCitaMedica.getIdPaciente();
                    citaMedicaListNewCitaMedica.setIdPaciente(paciente);
                    citaMedicaListNewCitaMedica = em.merge(citaMedicaListNewCitaMedica);
                    if (oldIdPacienteOfCitaMedicaListNewCitaMedica != null && !oldIdPacienteOfCitaMedicaListNewCitaMedica.equals(paciente)) {
                        oldIdPacienteOfCitaMedicaListNewCitaMedica.getCitaMedicaList().remove(citaMedicaListNewCitaMedica);
                        oldIdPacienteOfCitaMedicaListNewCitaMedica = em.merge(oldIdPacienteOfCitaMedicaListNewCitaMedica);
                    }
                }
            }
            for (Consulta consultaListNewConsulta : consultaListNew) {
                if (!consultaListOld.contains(consultaListNewConsulta)) {
                    Paciente oldIdPacienteOfConsultaListNewConsulta = consultaListNewConsulta.getIdPaciente();
                    consultaListNewConsulta.setIdPaciente(paciente);
                    consultaListNewConsulta = em.merge(consultaListNewConsulta);
                    if (oldIdPacienteOfConsultaListNewConsulta != null && !oldIdPacienteOfConsultaListNewConsulta.equals(paciente)) {
                        oldIdPacienteOfConsultaListNewConsulta.getConsultaList().remove(consultaListNewConsulta);
                        oldIdPacienteOfConsultaListNewConsulta = em.merge(oldIdPacienteOfConsultaListNewConsulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paciente.getIdPaciente();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
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
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getIdPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            ExpedienteMedico expedienteMedicoOrphanCheck = paciente.getExpedienteMedico();
            if (expedienteMedicoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the ExpedienteMedico " + expedienteMedicoOrphanCheck + " in its expedienteMedico field has a non-nullable idPaciente field.");
            }
            List<Pago> pagoListOrphanCheck = paciente.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Pago " + pagoListOrphanCheckPago + " in its pagoList field has a non-nullable idPaciente field.");
            }
            List<Tratamiento> tratamientoListOrphanCheck = paciente.getTratamientoList();
            for (Tratamiento tratamientoListOrphanCheckTratamiento : tratamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Tratamiento " + tratamientoListOrphanCheckTratamiento + " in its tratamientoList field has a non-nullable idPaciente field.");
            }
            List<CitaMedica> citaMedicaListOrphanCheck = paciente.getCitaMedicaList();
            for (CitaMedica citaMedicaListOrphanCheckCitaMedica : citaMedicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the CitaMedica " + citaMedicaListOrphanCheckCitaMedica + " in its citaMedicaList field has a non-nullable idPaciente field.");
            }
            List<Consulta> consultaListOrphanCheck = paciente.getConsultaList();
            for (Consulta consultaListOrphanCheckConsulta : consultaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Consulta " + consultaListOrphanCheckConsulta + " in its consultaList field has a non-nullable idPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = paciente.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.setPaciente(null);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(paciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
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

    public Paciente findPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
