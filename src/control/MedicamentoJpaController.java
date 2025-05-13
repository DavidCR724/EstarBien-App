/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author carlo
 */
import modelo.Medicamento;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
public class MedicamentoJpaController {
    
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstarBienPU"); // Reemplaza con tu Unidad de Persistencia

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Medicamento> findMedicamentoEntities() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Medicamento.findAll");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void create(Medicamento medicamento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medicamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el medicamento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
