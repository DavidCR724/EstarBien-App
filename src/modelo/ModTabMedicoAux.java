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
public class ModTabMedicoAux extends AbstractTableModel{
    private List<Medico> medicos;
    private String encabezados[] = {"ID medico", "Nombre"};
    
    public ModTabMedicoAux(List<Medico> medicos){
        this.medicos = medicos;
    }

    @Override
    public int getRowCount() {
        if(medicos != null)   
            return medicos.size();
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
        switch (columnIndex) {
            case 0:
                return medicos.get(rowIndex).getIdMedico();
            default:
                return medicos.get(rowIndex).getNombre() + " " + medicos.get(rowIndex).getApellidoPaterno()+ " " + medicos.get(rowIndex).getApellidoMaterno();
        }
        
    }
    public void actualizar(List<Medico> medicos) {
        this.medicos = medicos;
        fireTableDataChanged(); // Notifica a la tabla que debe redibujarse
    }
}
