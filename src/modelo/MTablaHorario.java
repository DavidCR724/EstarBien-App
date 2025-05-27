/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlo
 */
// ESTE MODELO SE VA A CAMBIAR PARA MOSTRAR LAS CITAS !!!
public class MTablaHorario extends AbstractTableModel {

    private List<HorarioMedico> horario;
    private String encabezados[] = {"Día", "Hora de inicio", "Hora Fin", "no. Horario"};

    public MTablaHorario(List<HorarioMedico> horario) {
        this.horario = horario;
    }

    @Override
    public int getRowCount() {
        if (horario != null) {
            return horario.size();
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
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        switch (columnIndex) {
            case 0:
                return horario.get(rowIndex).getDiaSemana();
            case 1:
                Date horaInicio = horario.get(rowIndex).getHoraInicio();
                return horaInicio != null ? timeFormat.format(horaInicio) : "";
            case 2:
                Date horaFin = horario.get(rowIndex).getHoraFin();
                return horaFin != null ? timeFormat.format(horaFin) : "";
            default:
                return horario.get(rowIndex).getIdHorario();
        }
    }
}
