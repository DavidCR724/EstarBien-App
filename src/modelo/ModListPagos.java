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
public class ModListPagos extends DefaultListModel<String> {
    private List<Pago> pagos; // Inicializa con lista vacía

    @Override
    public int getSize() {
        return pagos.size(); 
    }

     public ModListPagos(List<Pago> pagos) {
        this.pagos = pagos != null ? pagos : new ArrayList<>();
        cargarModelo();
    }

    private void cargarModelo() {
        clear(); // Limpia el modelo
        for (Pago p : pagos) {
            addElement(String.valueOf(p.getIdPago())+ " Cita: #"+p.getIdCita());
        }
    }

    @Override
    public String getElementAt(int index) {
        Pago c = pagos.get(index);
        return "Folio: #" + c.getIdPago() + "   " + c.getIdCita(); // Formato consistente
    }
    
    public void actualizar(List<Pago> nuevosPagos) {
        this.pagos = nuevosPagos != null ? nuevosPagos : new ArrayList<>();
        cargarModelo();
        fireContentsChanged(this, 0, getSize());
    }
}
