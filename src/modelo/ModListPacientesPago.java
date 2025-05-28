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
public class ModListPacientesPago extends DefaultListModel<String> {
    private List<Paciente> pacientes; // Inicializa con lista vacía

    @Override
    public int getSize() {
        return pacientes.size(); 
    }

     public ModListPacientesPago(List<Paciente> pacientes) {
        this.pacientes = pacientes != null ? pacientes : new ArrayList<>();
        cargarModelo();
    }

    private void cargarModelo() {
        clear(); // Limpia el modelo
        for (Paciente p : pacientes) {
            addElement(p.getNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno());
        }
    }

    public void actualizar(List<Paciente> nuevosPacientes) {
        this.pacientes = nuevosPacientes != null ? nuevosPacientes : new ArrayList<>();
        cargarModelo();
        fireContentsChanged(this, 0, getSize());
    }

}
