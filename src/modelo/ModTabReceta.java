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
public class ModTabReceta extends AbstractTableModel{
    private List<RecetaMedicamento> rmedicamentos;
    private String encabezados[] = {"ID receta", "ID cita", "Fecha Presc.", "Instrucciones", "Firma", "Dosis", "Frecuencia", "Duración", "Cantidad"};
    
    public ModTabReceta(List<RecetaMedicamento> recetaMedicamentos){
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
                return rmedicamentos.get(rowIndex).getIdReceta().getIdReceta();
            case 1:
                return rmedicamentos.get(rowIndex).getIdReceta().getIdCita().getIdCita();
            case 2:
                return rmedicamentos.get(rowIndex).getIdReceta().getFechaEmision();
            case 3:
                return rmedicamentos.get(rowIndex).getIdReceta().getInstrucciones();
            case 4:
                return rmedicamentos.get(rowIndex).getIdReceta().getFirmaDigital();
            case 5:
                return rmedicamentos.get(rowIndex).getDosis();
            case 6:
                return rmedicamentos.get(rowIndex).getFrecuencia();
            case 7:
                return rmedicamentos.get(rowIndex).getDuracion();
            default:
                return rmedicamentos.get(rowIndex).getCantidad();
        }

    }
    
}
