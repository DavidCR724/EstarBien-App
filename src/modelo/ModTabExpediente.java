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
public class ModTabExpediente extends AbstractTableModel{
    
    private List<ExpedienteMedico> expedientes;
    private String encabezados[] = {"ID paciente", "Nombre", "Apellidos", "ID expediente", "Tipo Sangre", "Estatura", "Peso", "Antecedentes", "Alergias"};
    
    public ModTabExpediente(List<ExpedienteMedico> expedientes){
        this.expedientes = expedientes;
    }

    @Override
    public int getRowCount() {
        if(expedientes != null)   
            return expedientes.size();
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
                return expedientes.get(rowIndex).getIdPaciente().getIdPaciente();
            case 1:
                return expedientes.get(rowIndex).getIdPaciente().getNombre();
            case 2:
                return expedientes.get(rowIndex).getIdPaciente().getApellidoPaterno() + " " + expedientes.get(rowIndex).getIdPaciente().getApellidoMaterno();
            case 3:
                return expedientes.get(rowIndex).getIdExpediente();
            case 4:
                return expedientes.get(rowIndex).getTipoSangre();
            case 5:
                return expedientes.get(rowIndex).getEstatura();
            case 6:
                return expedientes.get(rowIndex).getPeso();
            case 7:
                return expedientes.get(rowIndex).getAntecedentes();
            default:
                return expedientes.get(rowIndex).getIdPaciente().getAlergias();
        }
        
    }
    public void actualizar(List<ExpedienteMedico> nuevosExpedienteMedicos) {
        this.expedientes = nuevosExpedienteMedicos;
        fireTableDataChanged(); // Notifica a la tabla que debe redibujarse
    }
    
}
