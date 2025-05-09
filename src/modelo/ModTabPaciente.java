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
public class ModTabPaciente extends AbstractTableModel{
    private List<Paciente> pacientes;
    private String encabezados[] = {"ID paciente", "ID usuario", "Nombre", "Apellidos", "Fecha Nac", "Genero", "Alergias"};
    public ModTabPaciente(List<Paciente> pacientes){
        this.pacientes = pacientes;
    }

    @Override
    public int getRowCount() {
        if(pacientes != null)   
            return pacientes.size();
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
                return pacientes.get(rowIndex).getIdPaciente();
            case 1:
                return pacientes.get(rowIndex).getIdUsuario().getIdUsuario();
            case 2:
                return pacientes.get(rowIndex).getNombre();
            case 3:
                return pacientes.get(rowIndex).getApellidoPaterno() + " " + pacientes.get(rowIndex).getApellidoMaterno();
            case 4:
                return pacientes.get(rowIndex).getFechaNacimiento();
            case 5:
                return pacientes.get(rowIndex).getGenero();
            default:
                return pacientes.get(rowIndex).getAlergias();
        }
        
    }
    
}
