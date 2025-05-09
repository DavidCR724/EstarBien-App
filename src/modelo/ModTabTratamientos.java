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
public class ModTabTratamientos extends AbstractTableModel {

    private List<Tratamiento> tratamientos;
    private String encabezados[] = {"ID tratamiento", "ID cita", "ID medico", "ID paciente", "Diagnostico", "Indicaciones", "Duracion"};

    public ModTabTratamientos(List<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    @Override
    public int getRowCount() {
        if (tratamientos != null) {
            return tratamientos.size();
        }
        return 0;

    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    }

    @Override
    public String getColumnName(int i) {
        return encabezados[i];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return tratamientos.get(rowIndex).getIdTratamiento();
            case 1:
                return tratamientos.get(rowIndex).getIdCita().getIdCita();
            case 2:
                return tratamientos.get(rowIndex).getIdMedico().getIdMedico();
            case 3:
                return tratamientos.get(rowIndex).getIdPaciente().getIdPaciente();
            case 4:
                return tratamientos.get(rowIndex).getDiagnostico();
            case 5:
                return tratamientos.get(rowIndex).getIndicaciones();
            default:
                return tratamientos.get(rowIndex).getDuracion();
        }

    }
    

}
