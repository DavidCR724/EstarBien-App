/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlo
 */
public class ModTabCitaAux extends AbstractTableModel{
    private List<CitaMedica> citas;
    private String encabezados[] = {"No. Paciente","Paciente","No. Doctor","Doctor"};
    
    
    public ModTabCitaAux(List<CitaMedica> citas){
        this.citas = citas;
    }

    @Override
    public int getRowCount() {
        if(citas != null)
            return citas.size();
        return 0;
    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    
    }
    @Override
    public String getColumnName(int i){
        return encabezados[i];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       switch(columnIndex){
            case 0:
                return citas.get(rowIndex).getIdPaciente().getIdPaciente();
            case 1:
                return citas.get(rowIndex).getIdPaciente().getNombre() + " " + citas.get(rowIndex).getIdPaciente().getApellidoPaterno() +" "+ citas.get(rowIndex).getIdPaciente().getApellidoMaterno();
            case 2:
                return citas.get(rowIndex).getIdMedico().getIdMedico();
            default:
                return citas.get(rowIndex).getIdMedico().getNombre() + " " + citas.get(rowIndex).getIdMedico().getApellidoPaterno() +" "+ citas.get(rowIndex).getIdMedico().getApellidoMaterno();
                
        }
    }
    public void actualizar(List<CitaMedica> citas){
        this.citas = citas;
        fireTableDataChanged();
    }
}
