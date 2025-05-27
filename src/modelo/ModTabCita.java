/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lessl
 */
public class ModTabCita extends AbstractTableModel {
    private List<CitaMedica> citas;
    private final String[] columnNames = {"N° Cita", "Paciente", "Hora", "Motivo", "Estatus"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public ModTabCita(List<CitaMedica> citas) {
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return citas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        CitaMedica cita = citas.get(rowIndex);
        switch (columnIndex) {
            case 0: return cita.getIdCita();
            case 1: return cita.getIdPaciente().getNombre() + " " +cita.getIdPaciente().getApellidoPaterno(); // Asume que Paciente tiene este método
            case 2: return timeFormat.format(cita.getHora());
            case 3: return cita.getMotivo();
            case 4: return cita.getEstatus();
            default: return null;
        }
    }

    public void actualizar(List<CitaMedica> citas) {
        this.citas = citas;
        fireTableDataChanged();
    }
}


