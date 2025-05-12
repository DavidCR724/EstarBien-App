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
public class ModComboCita extends DefaultComboBoxModel<CitaMedica>{
    public ModComboCita(List<CitaMedica> citas) {
        super();
        if (citas != null) {
            for (CitaMedica m : citas) {
                addElement(m);
            }
        }
    }

    @Override
    public CitaMedica getSelectedItem() {
        return (CitaMedica) super.getSelectedItem();
    }
}
