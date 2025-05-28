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

public class ModTabPago extends AbstractTableModel{
    private List<Pago> pagos;
    private final String[] encabezados = {"N. de cita","Folio", "Paciente", "Estado del pago", "Fecha y hora", "Monto"};

    public ModTabPago(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public int getRowCount() {
        return pagos != null ? pagos.size() : 0;
    }

    public Pago getPagoAt(int rowIndex) {
        return pagos.get(rowIndex);
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
        Pago pago = pagos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return pago.getIdCita();
            case 1:
                return pago.getIdPago();
            case 2:
                return pago.getIdPaciente().getNombre() + " " +
                       pago.getIdPaciente().getApellidoPaterno() + " " +
                       pago.getIdPaciente().getApellidoMaterno();
            case 3:
                return pago.getEstatus();
            case 4:
                return pago.getFecha(); 
            case 5:
                return pago.getMonto();
            default:
                return null;
        }
    }

    public void actualizar(List<Pago> nuevosPagos) {
        this.pagos = nuevosPagos;
        fireTableDataChanged(); // Notificar a la tabla que los datos cambiaron
    }
}
