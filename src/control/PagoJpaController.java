/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author carlo
 */
import modelo.Pago;
import modelo.Paciente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class PagoJpaController {
    
    /* En esta clase vamos a poner los controladores de la ventana de pagos.
     * */
     
    private final EntityManagerFactory emf;

    public  PagoJpaController() {
        emf = Persistence.createEntityManagerFactory("EstarBienPU"); 
    }
    
    
    //Método para cargar la tabla de historial de la tabla
    public  List<Object[]> obtenerDatosTabla() {
        
        /* En este método se recuperan los datos de los modelos de pago, y rellenará la tabla con los 
        * arrays, en este caso nos interesa
        */
        List<Object[]> datos = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Pago> query = em.createQuery("SELECT p FROM Pago p", Pago.class);
            List<Pago> pagos = query.getResultList();

            for (Pago pago : pagos) {
                Paciente paciente = pago.getIdPaciente();
                String nombreCompleto = paciente.getNombre() + " " + paciente.getApellidoPaterno() + " " + paciente.getApellidoMaterno();

                Object[] fila = {
                        pago.getIdPago(),
                        nombreCompleto,
                        pago.getEstatus(),
                        pago.getFecha(),
                        pago.getMonto()
                };
                datos.add(fila);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los datos de pagos: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            em.close();
        }
        //System.out.println("Datos recuperados: " + datos.size()); fue usado para verificar que el método si recupera datos
        return datos;
    }
}
