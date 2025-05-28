/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author carlo
 */
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModTabConsulta extends AbstractTableModel {
    private List<Consulta> consultas;
    private String[] encabezados = {"ID", "Paciente", "Médico", "Síntomas"};
    
    public ModTabConsulta(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    @Override
    public int getRowCount() {
        return consultas != null ? consultas.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    }

    @Override
    public String getColumnName(int column) {
        return encabezados[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return consultas.get(rowIndex).getIdConsulta();
            case 1: return consultas.get(rowIndex).getIdPaciente().getNombre() + " " + consultas.get(rowIndex).getIdPaciente().getApellidoPaterno()+" "+consultas.get(rowIndex).getIdPaciente().getApellidoMaterno();
            case 2: return consultas.get(rowIndex).getIdMedico().getNombre() + " "+ consultas.get(rowIndex).getIdMedico().getApellidoPaterno()+ " "+ consultas.get(rowIndex).getIdMedico().getApellidoMaterno();
            default: return consultas.get(rowIndex).getSintomas();
        }
    }
    
    public void actualizar(List<Consulta> nuevasConsultas) {
        this.consultas = nuevasConsultas;
        fireTableDataChanged();
    }
    
    public Consulta getConsultaAt(int row) {
        return consultas.get(row);
    }
}