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
public class ModComboMedico extends DefaultComboBoxModel<Medico>{

    public ModComboMedico(List<Medico> medicos) {
        super();
        if (medicos != null) {
            for (Medico m : medicos) {
                addElement(m);
            }
        }
    }

    @Override
    public Medico getSelectedItem() {
        return (Medico) super.getSelectedItem();
    }
}
