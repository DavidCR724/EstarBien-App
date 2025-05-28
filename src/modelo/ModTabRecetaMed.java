/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlo
 */
public class ModTabRecetaMed extends AbstractTableModel{
    private List<RecetaMedicamento> rmedicamentos;
    private String encabezados[] = {"Medicamento", "Dosis", "Frecuencia", "Duración", "Cantidad"};
    
    public ModTabRecetaMed(List<RecetaMedicamento> recetaMedicamentos){
        this.rmedicamentos = recetaMedicamentos;
    }
    @Override
    public int getRowCount() {
        if (rmedicamentos != null) {
            return rmedicamentos.size();
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
                return rmedicamentos.get(rowIndex).getIdMedicamento().getNombre();
            case 1:
                return rmedicamentos.get(rowIndex).getDosis();
            case 2:
               return rmedicamentos.get(rowIndex).getFrecuencia();
            case 3:
                return rmedicamentos.get(rowIndex).getDuracion();
            default:
                return rmedicamentos.get(rowIndex).getCantidad();
                
        }

    }
    public void actualizar(List<RecetaMedicamento> recetrMedicamentos){
        this.rmedicamentos = recetrMedicamentos;
        fireTableDataChanged();
    }
    
}
