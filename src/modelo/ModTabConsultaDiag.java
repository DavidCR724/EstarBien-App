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

public class ModTabConsultaDiag extends AbstractTableModel {
    private List<ConsultaDiagnostico> diagnosticos;
    private String[] encabezados = {"ID", "Código CIE", "Diagnóstico", "Tipo"};
    
    public ModTabConsultaDiag(List<ConsultaDiagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    @Override
    public int getRowCount() {
        return diagnosticos != null ? diagnosticos.size() : 0;
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
        switch(columnIndex){
            case 0: return diagnosticos.get(rowIndex).getIdConsultaDiagnostico();
            case 1: return diagnosticos.get(rowIndex).getCodigoCie();
            case 2: return diagnosticos.get(rowIndex).getDiagnostico();
            default: return diagnosticos.get(rowIndex).getTipo();
            
        }

    }
    
    public void actualizar(List<ConsultaDiagnostico> nuevosDiag) {
        this.diagnosticos = nuevosDiag;
        fireTableDataChanged();
    }
    
    public ConsultaDiagnostico getDiagnosticoAt(int row) {
        return diagnosticos.get(row);
    }
}