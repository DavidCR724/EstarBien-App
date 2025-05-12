/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author carlo
 */
public class ModComboPaciente extends DefaultComboBoxModel<Paciente> {

    public ModComboPaciente(List<Paciente> pacientes) {
        super();
        if (pacientes != null) {
            for (Paciente p : pacientes) {
                addElement(p);
            }
        }
    }

    @Override
    public Paciente getSelectedItem() {
        return (Paciente) super.getSelectedItem();
    }
}
