/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lessl
 */
public class ModTabPacienteCita extends AbstractTableModel {
    private List<Paciente> pacientes;
    private final String[] encabezados = {"Nombre del paciente"};
            
    public ModTabPacienteCita(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    @Override
    public int getRowCount() {
        if (pacientes != null) {
            return pacientes.size();
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
                return pacientes.get(rowIndex).getNombre()+ " " + pacientes.get(rowIndex).getApellidoPaterno();
            default:
                return null;
        }

    }

    public void actualizar(List<Paciente> nuevosPacientes) {
        this.pacientes = nuevosPacientes;
        fireTableDataChanged(); // Notifica a la tabla que debe redibujarse
    }

}
