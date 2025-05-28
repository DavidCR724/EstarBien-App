/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author carlo
 */
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModTabMedicamentos extends AbstractTableModel {
    private List<Medicamento> medicamentos;
    private final String[] encabezados = {"ID Medicamento", "Nombre", "Descripción", "Presentación", "Dosis Recomendada"};

    public ModTabMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    @Override
    public int getRowCount() {
        return medicamentos != null ? medicamentos.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return encabezados[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Medicamento medicamento = medicamentos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return medicamento.getIdMedicamento();
            case 1:
                return medicamento.getNombre();
            case 2:
                return medicamento.getDescripcion();
            case 3:
                return medicamento.getPresentacion(); 
            case 4:
                return medicamento.getDosisRecomendada();
            default:
                return null;
        }
    }

    public void actualizar(List<Medicamento> nuevosMedicamentos) {
        this.medicamentos = nuevosMedicamentos;
        fireTableDataChanged();
    }
}
