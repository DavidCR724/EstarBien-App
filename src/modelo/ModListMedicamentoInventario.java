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
 * @author B550
 */
public class ModListMedicamentoInventario extends DefaultListModel<String>{
     private List<Medicamento> medicamento; // Inicializa con lista vacía

    @Override
    public int getSize() {
        return medicamento.size(); 
    }

     public ModListMedicamentoInventario (List<Medicamento> medicamento) {
        this.medicamento = medicamento != null ? medicamento : new ArrayList<>();
        cargarModelo();
    }

    private void cargarModelo() {
        clear(); // Limpia el modelo
        for (Medicamento m : medicamento) {
            addElement(String.valueOf(m.getNombre())+"     "+ m.getPresentacion());
        }
    }

    @Override
    public String getElementAt(int index) {
        Medicamento m = medicamento.get(index);
        return m.getNombre() + "     " + m.getPresentacion(); // Formato consistente
    }
    
    public Medicamento getMedicamentoAt(int index) {
        return medicamento.get(index);
    }
    
    public void actualizar(List<Medicamento> nuevoMedicamento) {
        this.medicamento = nuevoMedicamento != null ? nuevoMedicamento : new ArrayList<>();
        fireContentsChanged(this, 0, getSize()); // Notificar cambios
    }
}
