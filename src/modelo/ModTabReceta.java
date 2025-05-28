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
public class ModTabReceta extends AbstractTableModel{
    private List<RecetaMedica> recetas;
    private String encabezados[] = {"Fecha Cita","Instrucciones","Fech. Emision","No. Receta", "Tratamiento"};
    
    
    public ModTabReceta(List<RecetaMedica> recetas){
        this.recetas = recetas;
    }

    @Override
    public int getRowCount() {
        if(recetas != null)
            return recetas.size();
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
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "Es"));
        switch(columnIndex){
            case 0:
                return formato.format(recetas.get(rowIndex).getIdCita().getFecha());
            case 1:
                return recetas.get(rowIndex).getInstrucciones();
            case 2:
                return formato.format(recetas.get(rowIndex).getFechaEmision());
            case 3:
                return recetas.get(rowIndex).getIdReceta();
            default:
                return recetas.get(rowIndex).getIdTratamiento();
        }
    }
    public void actualizar(List<RecetaMedica> recetas){
        this.recetas = recetas;
        fireTableDataChanged();
    }
}
