/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author carlo
 */
public class ModListCitaPago extends DefaultListModel<String> {
    private List<CitaMedica> cita; // Inicializa con lista vacía

    @Override
    public int getSize() {
        return cita.size(); 
    }

     public ModListCitaPago (List<CitaMedica> cita) {
        this.cita = cita != null ? cita : new ArrayList<>();
        cargarModelo();
    }

    private void cargarModelo() {
        clear(); // Limpia el modelo
        for (CitaMedica p : cita) {
            addElement(String.valueOf(p.getIdCita())+"     "+ p.getFecha());
        }
    }

    @Override
    public String getElementAt(int index) {
        CitaMedica c = cita.get(index);
        return "Cita #" + c.getIdCita() + "     " + c.getFecha(); // Formato consistente
    }
    
    public CitaMedica getCitaAt(int index) {
        return cita.get(index);
    }
    
    public void actualizar(List<CitaMedica> nuevasCitas) {
        this.cita = nuevasCitas != null ? nuevasCitas : new ArrayList<>();
        fireContentsChanged(this, 0, getSize()); // Notificar cambios
    }
}


