/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author carlo
 */
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModTabInventario extends AbstractTableModel{
    private List<Inventario> inventario;
    private final String[] encabezados = {"ID Medicamento", "Nombre", " Cantidad", "Lote", "Caducidad", "Ubicación"};

    public ModTabInventario(List<Inventario> inventario) {
        this.inventario = inventario;
    }

    @Override
    public int getRowCount() {
        return inventario != null ? inventario.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return encabezados.length;
    }

        public Inventario getInventarioAt(int rowIndex) {
        return inventario.get(rowIndex);
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return encabezados[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Inventario inventarios = inventario.get(rowIndex);
        Inventario inv = inventario.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return inventarios.getIdMedicamento().getIdMedicamento();
            case 1:
                return  inventarios.getIdMedicamento().getNombre();
            case 2:
                return inventarios.getCantidad();
            case 3:
                return inventarios.getLote(); 
            case 4:
                { // Columna de fecha de caducidad
            if (inv.getFechaCaducidad() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(inv.getFechaCaducidad());
            }
            return "N/A";
        }
            case 5:   
                return inventarios.getUbicacion();
            default:
                return null;
        }
    }

    public void actualizar(List<Inventario> nuevoInventario) {
        this.inventario = nuevoInventario;
        fireTableDataChanged();
    }
}
