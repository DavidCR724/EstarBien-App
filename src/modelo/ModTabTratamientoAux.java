/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import vista.PanelMedicos;

/**
 *
 * @author carlo
 */
public class ModTabTratamientoAux extends AbstractTableModel{
    private List<Tratamiento> tratamientos;
    private String encabezados [] = {"Paciente","Medico","Fecha Cita","Diagnostico","No."};
    public ModTabTratamientoAux(List<Tratamiento>tratamientos){
        this.tratamientos = tratamientos;
    }
    @Override
    public int getRowCount() {
       if(tratamientos != null)
           return tratamientos.size();
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
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        switch(columnIndex){
            case 0:
                return tratamientos.get(rowIndex).getIdPaciente().getNombre() + " " + tratamientos.get(rowIndex).getIdPaciente().getApellidoPaterno() +" "+ tratamientos.get(rowIndex).getIdPaciente().getApellidoMaterno();
            case 1:
                return tratamientos.get(rowIndex).getIdMedico().getNombre() + " " + tratamientos.get(rowIndex).getIdMedico().getApellidoPaterno();
            case 2:
                return formato.format(tratamientos.get(rowIndex).getIdCita().getFecha());
            case 3:
                return tratamientos.get(rowIndex).getDiagnostico();
            default:
                return tratamientos.get(rowIndex).getIdTratamiento();
                
        }
    }
    public void actualizar(List<Tratamiento> tratamientos){
        this.tratamientos = tratamientos;
        fireTableDataChanged();
    }
    
}
