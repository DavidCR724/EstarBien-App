/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import control.AdmDatos;
import control.MedicamentoJpaController;
import control.RecetaMedicaJpaController;
import control.RecetaMedicamentoJpaController;
import control.TratamientoJpaController;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import modelo.CitaMedica;
import modelo.Medicamento;
import modelo.Medico;
import modelo.ModTabReceta;
import modelo.ModTabRecetaMed;
import modelo.ModTabTratamientoAux;
import modelo.Paciente;
import modelo.RecetaMedica;
import modelo.RecetaMedicamento;
import modelo.Tratamiento;

/**
 *
 * @author carlo
 */
public class PanelReceta extends javax.swing.JPanel {

    private AdmDatos admDatos = new AdmDatos();

    private RecetaMedicamentoJpaController cRecetaMedicamento;
    private TratamientoJpaController cTratamientos;
    private RecetaMedicaJpaController cRecetaMedica;
    private MedicamentoJpaController cMedicamento;

    private List<RecetaMedicamento> recetasMedicamentos;
    private List<Tratamiento> tratamientos;
    private List<RecetaMedica> recetasMedicas;
    private List<Medicamento> medicamentos;
    private HashMap<String, Medicamento> medicamentosMap = new HashMap<>();
    private HashMap<String, CitaMedica> citasMap = new HashMap<>();
    private HashMap<Integer, Tratamiento> tratamientosMap = new HashMap<>();
    private List<Tratamiento> tratamientosSinReceta = new ArrayList<>();

    private ModTabRecetaMed modelo;
    private ModTabTratamientoAux modTabTratamiento;
    private ModTabReceta modeloReceta;

    private CitaMedica citaN;
    private Tratamiento tratN;
    private RecetaMedica newRM;
    private RecetaMedicamento newRMed;

    /**
     * Creates new form PanelReceta
     */
    public PanelReceta() {
        initComponents();

        cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());
        cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());

        recetasMedicamentos = cRecetaMedicamento.findRecetaMedicamentoEntities();
        recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();

        modeloReceta = new ModTabReceta(recetasMedicas);
        modelo = new ModTabRecetaMed(recetasMedicamentos);

        tablaRecetas.setModel(modeloReceta);
        activarMedicamentos(false);
        cargar();

    }

    private void cargar() {
        cargarTratamientos();

        cMedicamento = new MedicamentoJpaController(admDatos.getEmf());
        medicamentos = cMedicamento.findMedicamentoEntities();
        medicamentosMap = new HashMap<>();

        for (Medicamento med : medicamentos) {
            String strMed = med.getIdMedicamento() + " > " + med.getNombre();
            comboMedi.addItem(strMed);
            medicamentosMap.put(strMed, med);
        }
        infoTabla.setModel(modTabTratamiento);

    }

    private void cargarTratamientos() {
        citasMap.clear();
        tratamientosMap.clear();
        tratamientosSinReceta.clear();
        cTratamientos = new TratamientoJpaController(admDatos.getEmf());
        tratamientos = cTratamientos.findTratamientoEntities();

        // Obtener IDs de tratamientos con receta
        Set<Integer> tratamientosConReceta = recetasMedicas.stream()
                .filter(rm -> rm.getIdTratamiento() != null)
                .map(rm -> rm.getIdTratamiento().getIdTratamiento())
                .collect(Collectors.toSet());

        // Filtrar tratamientos sin receta
        for (Tratamiento t : tratamientos) {
            if (!tratamientosConReceta.contains(t.getIdTratamiento())) {
                tratamientosSinReceta.add(t);
                String strNomP = t.getIdPaciente().getNombre() + " " + t.getIdPaciente().getApellidoPaterno() + " " + t.getIdPaciente().getApellidoMaterno();
                citasMap.put(strNomP, t.getIdCita());
                tratamientosMap.put(t.getIdTratamiento(), t);
            }
        }
        modTabTratamiento = new ModTabTratamientoAux(tratamientosSinReceta);
    }

    private void actualizarTablas() {
        cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());
        cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());

        recetasMedicamentos = cRecetaMedicamento.findRecetaMedicamentoEntities();
        recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();
        List<RecetaMedica> recetasFiltrada = new ArrayList<>();

        modeloReceta.actualizar(recetasMedicas);
        int filaSeleccionada = tablaRecetas.getSelectedRow();
        if (filaSeleccionada != -1) {
            int noReceta = (int) tablaRecetas.getValueAt(filaSeleccionada, 3);
            cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());
            recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();
            for (RecetaMedica rm : recetasMedicas) {
                if (rm.getIdReceta() == noReceta) {
                    recetasFiltrada.add(rm);
                }
            }
            modeloReceta = new ModTabReceta(recetasFiltrada);
            tabRMedica.setModel(modeloReceta);

        }
        cargar();
        detalleTrat.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        accionesGrupo = new javax.swing.ButtonGroup();
        activarMedicamento = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRecetas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        instruccTA = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        pacientefield = new javax.swing.JTextField();
        citafield = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        infoTabla = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        cargarBot = new javax.swing.JButton();
        addBot = new javax.swing.JButton();
        editBot = new javax.swing.JButton();
        deleteBot = new javax.swing.JButton();
        eliminarCB = new javax.swing.JCheckBox();
        editarCB = new javax.swing.JCheckBox();
        addCB = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        detalleTrat = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabRMedica = new javax.swing.JTable();
        updateTablesButt = new javax.swing.JButton();
        noCB = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        siCB = new javax.swing.JCheckBox();
        comboMedi = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dosisField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        frecuenciaField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        duracionField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cantidadSpinner = new javax.swing.JSpinner();
        buscBot = new javax.swing.JButton();
        buscadorField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1397, 882));
        setMinimumSize(new java.awt.Dimension(1397, 882));
        setPreferredSize(new java.awt.Dimension(1397, 882));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Panel Recetas");

        tablaRecetas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaRecetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRecetasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRecetas);

        instruccTA.setColumns(20);
        instruccTA.setRows(5);
        jScrollPane2.setViewportView(instruccTA);

        jLabel2.setText("Instrucciones");

        pacientefield.setEditable(false);

        citafield.setEditable(false);

        jLabel4.setText("Paciente");

        jLabel5.setText("Cita");

        infoTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        infoTabla.setMaximumSize(new java.awt.Dimension(1397, 882));
        infoTabla.setMinimumSize(new java.awt.Dimension(1397, 882));
        infoTabla.setPreferredSize(new java.awt.Dimension(1397, 882));
        infoTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoTablaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(infoTabla);

        jLabel6.setText(" Tratamientos sin receta");

        cargarBot.setText("Cargar Receta");
        cargarBot.setEnabled(false);
        cargarBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarBotActionPerformed(evt);
            }
        });

        addBot.setText("Agregar");
        addBot.setEnabled(false);
        addBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBotActionPerformed(evt);
            }
        });

        editBot.setText("Editar");
        editBot.setEnabled(false);
        editBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBotActionPerformed(evt);
            }
        });

        deleteBot.setText("Eliminar");
        deleteBot.setEnabled(false);
        deleteBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBotActionPerformed(evt);
            }
        });

        accionesGrupo.add(eliminarCB);
        eliminarCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarCBActionPerformed(evt);
            }
        });

        accionesGrupo.add(editarCB);
        editarCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCBActionPerformed(evt);
            }
        });

        accionesGrupo.add(addCB);
        addCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCBActionPerformed(evt);
            }
        });

        detalleTrat.setColumns(20);
        detalleTrat.setRows(5);
        jScrollPane4.setViewportView(detalleTrat);

        tabRMedica.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tabRMedica);

        updateTablesButt.setText("Actualizar Tablas");
        updateTablesButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTablesButtActionPerformed(evt);
            }
        });

        activarMedicamento.add(noCB);
        noCB.setSelected(true);
        noCB.setText("No");
        noCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noCBActionPerformed(evt);
            }
        });

        jLabel3.setText("¿Tiene Medicamento?");

        activarMedicamento.add(siCB);
        siCB.setText("Si");
        siCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siCBActionPerformed(evt);
            }
        });

        comboMedi.setEditable(true);

        jLabel7.setText("Medicamento");

        jLabel8.setText("Dosis");

        jLabel9.setText("Frecuencia");

        jLabel10.setText("Duración");

        jLabel11.setText("Cantidad");

        cantidadSpinner.setModel(new javax.swing.SpinnerNumberModel(1, null, 100, 1));

        buscBot.setText("Mis Recetas Preescritas");
        buscBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscBotActionPerformed(evt);
            }
        });

        jLabel12.setText("Ingresa tu ID o nombre:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscadorField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscBot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateTablesButt))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane2)
                                    .addComponent(citafield)
                                    .addComponent(pacientefield)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(siCB)
                                        .addGap(18, 18, 18)
                                        .addComponent(noCB))
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel7)
                                        .addComponent(comboMedi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8)
                                        .addComponent(dosisField)
                                        .addComponent(frecuenciaField)
                                        .addComponent(jLabel10)
                                        .addComponent(duracionField)
                                        .addComponent(jLabel11)
                                        .addComponent(cantidadSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cargarBot)
                                        .addGap(38, 38, 38)
                                        .addComponent(addBot)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addCB)
                                        .addGap(107, 107, 107)
                                        .addComponent(editBot)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editarCB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(deleteBot)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eliminarCB))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateTablesButt)
                        .addComponent(buscBot)
                        .addComponent(buscadorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cargarBot)
                        .addComponent(addBot)
                        .addComponent(editBot)
                        .addComponent(deleteBot))
                    .addComponent(eliminarCB)
                    .addComponent(editarCB)
                    .addComponent(addCB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(noCB)
                                    .addComponent(siCB))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dosisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(5, 5, 5)
                                                .addComponent(comboMedi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27))
                                            .addComponent(jLabel8))
                                        .addGap(33, 33, 33)))
                                .addGap(19, 19, 19)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frecuenciaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pacientefield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(citafield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(duracionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidadSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCBActionPerformed
        addBot.setEnabled(true);
        editBot.setEnabled(false);
        deleteBot.setEnabled(false);

        cargarBot.setEnabled(false);

    }//GEN-LAST:event_addCBActionPerformed

    private void editarCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarCBActionPerformed
        addBot.setEnabled(false);
        editBot.setEnabled(true);
        deleteBot.setEnabled(false);

        cargarBot.setEnabled(false);
    }//GEN-LAST:event_editarCBActionPerformed

    private void eliminarCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarCBActionPerformed
        addBot.setEnabled(false);
        editBot.setEnabled(false);
        deleteBot.setEnabled(true);
        cargarBot.setEnabled(false);
    }//GEN-LAST:event_eliminarCBActionPerformed

    private void tablaRecetasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRecetasMouseClicked
        cargarBot.setEnabled(true);
        addBot.setEnabled(false);
        editBot.setEnabled(false);
        deleteBot.setEnabled(false);
        limpiarCampos();
        activarMedicamentos(true);
        List<RecetaMedica> recetasFiltrada = new ArrayList<>();
        int filaSeleccionada = tablaRecetas.getSelectedRow();
        if (filaSeleccionada != -1) {
            int noReceta = (int) tablaRecetas.getValueAt(filaSeleccionada, 3);
            cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());
            recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();

            for (RecetaMedica rm : recetasMedicas) {
                if (rm.getIdReceta() == noReceta) {
                    recetasFiltrada.add(rm);
                }
            }
            modeloReceta = new ModTabReceta(recetasFiltrada);
            tabRMedica.setModel(modeloReceta);

        }


    }//GEN-LAST:event_tablaRecetasMouseClicked

    private void infoTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoTablaMouseClicked
        int filaSeleccionada = infoTabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            detalleTrat.setText(
                    "Paciente: " + infoTabla.getValueAt(filaSeleccionada, 0) + "\n"
                    + "Medico: " + infoTabla.getValueAt(filaSeleccionada, 1) + "\n"
                    + "Fecha: " + infoTabla.getValueAt(filaSeleccionada, 2) + "\n"
                    + "Diagnostico: " + infoTabla.getValueAt(filaSeleccionada, 3)
            );
            pacientefield.setText((String) infoTabla.getValueAt(filaSeleccionada, 0));
            citafield.setText((String) infoTabla.getValueAt(filaSeleccionada, 2));
        }


    }//GEN-LAST:event_infoTablaMouseClicked

    private void updateTablesButtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTablesButtActionPerformed
        actualizarTablas();
        limpiarCampos();
    }//GEN-LAST:event_updateTablesButtActionPerformed

    private void cargarBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarBotActionPerformed
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        boolean tieneMedicamentos = false;
        cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());
        recetasMedicamentos = cRecetaMedicamento.findRecetaMedicamentoEntities();

        int filaSelec = tablaRecetas.getSelectedRow();
        if (filaSelec != -1) {
            int noreceta = (int) tablaRecetas.getValueAt(filaSelec, 3);

            RecetaMedica rm = new RecetaMedica();
            RecetaMedicamento rmed = new RecetaMedicamento();

            //BUSCA RECETA MEDICA
            for (int i = 0; i < recetasMedicas.size(); i++) {
                if (recetasMedicas.get(i).getIdReceta() == noreceta) {
                    rm = recetasMedicas.get(i);
                }
            }
            //BUSCA MEDICAMENTO DE LA RECETA
            for (RecetaMedicamento med : recetasMedicamentos) {
                if (med.getIdReceta().getIdReceta() == rm.getIdReceta()) {
                    rmed = med;
                    tieneMedicamentos = true;
                    break;
                }
            }

            Date dateFromCita = rm.getIdCita().getHora();
            LocalTime hora = dateFromCita.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            pacientefield.setText(rm.getIdCita().getIdPaciente().getNombre() + " " + rm.getIdCita().getIdPaciente().getApellidoPaterno() + " " + rm.getIdCita().getIdPaciente().getApellidoMaterno());
            citafield.setText("ID> " + rm.getIdCita().getIdCita() + " Fecha " + formato.format(rm.getIdCita().getFecha()) + " Hora " + hora);
            instruccTA.setText(rm.getInstrucciones());

            if (tieneMedicamentos == true) {
                int indice = -1;
                for (int i = 0; i < medicamentos.size(); i++) {
                    if (medicamentos.get(i).getIdMedicamento() == rmed.getIdMedicamento().getIdMedicamento()) {
                        indice = i;
                        break;
                    }
                }
                siCB.setSelected(true);
                comboMedi.setSelectedIndex(indice);
                dosisField.setText(rmed.getDosis());
                frecuenciaField.setText(rmed.getFrecuencia());
                duracionField.setText(rmed.getDuracion());
                cantidadSpinner.setValue(rmed.getCantidad());
            } else {
                noCB.setSelected(true);
                JOptionPane.showMessageDialog(this, "Esta receta medica no tiene medicamentos");
            }
        }
    }//GEN-LAST:event_cargarBotActionPerformed

    private void addBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBotActionPerformed
        int filaSelect = (int) infoTabla.getSelectedRow();
        int idTrata = -1;
        if (filaSelect != -1) {
            idTrata = (int) infoTabla.getValueAt(filaSelect, 4);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un tratamiento", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (instruccTA.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las instrucciones son necesarias", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (siCB.isSelected()) {
            if (dosisField.getText().trim().isEmpty()
                    || frecuenciaField.getText().trim().isEmpty()
                    || duracionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor llena todo los campos de medicamento", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        try {
            cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());
            cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());
            cTratamientos = new TratamientoJpaController(admDatos.getEmf());
            String keyPac = (String) infoTabla.getValueAt(filaSelect, 0);
            int keyTrat = (int) infoTabla.getValueAt(filaSelect, 4);;

            citaN = (CitaMedica) citasMap.get(keyPac);
            tratN = (Tratamiento) tratamientosMap.get(keyTrat);
            newRM = new RecetaMedica();
            newRMed = new RecetaMedicamento();

            if (noCB.isSelected()) {
                newRM.setCreatedAt(new Date());
                newRM.setIdCita(citaN);
                newRM.setIdTratamiento(tratN);
                newRM.setFechaEmision(new Date());
                newRM.setInstrucciones(instruccTA.getText());

                cRecetaMedica.create(newRM);
                recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();
                modeloReceta.actualizar(recetasMedicas);
                tratamientos = cTratamientos.findTratamientoEntities();
                actualizarTablas();
                JOptionPane.showMessageDialog(this, "Receta agregado correctamente", "Informacion importante", JOptionPane.INFORMATION_MESSAGE);
            } else if (siCB.isSelected()) {
                String keyMed = (String) comboMedi.getSelectedItem();
                Medicamento medN = (Medicamento) medicamentosMap.get(keyMed);

                newRM.setCreatedAt(new Date());
                newRM.setIdCita(citaN);
                newRM.setIdTratamiento(tratN);
                newRM.setFechaEmision(new Date());
                newRM.setInstrucciones(instruccTA.getText());
                cRecetaMedica.create(newRM);

                newRMed.setIdReceta(newRM);
                newRMed.setIdMedicamento(medN);
                newRMed.setDosis(dosisField.getText());
                newRMed.setFrecuencia(frecuenciaField.getText());
                newRMed.setDuracion(duracionField.getText());
                newRMed.setCantidad((int) cantidadSpinner.getValue());
                newRMed.setCreatedAt(new Date());
                cRecetaMedicamento.create(newRMed);

                recetasMedicamentos = cRecetaMedicamento.findRecetaMedicamentoEntities();

                recetasMedicas = cRecetaMedica.findRecetaMedicaEntities();
                modeloReceta.actualizar(recetasMedicas);
                tratamientos.clear();
                tratamientos = cTratamientos.findTratamientoEntities();
                actualizarTablas();
                JOptionPane.showMessageDialog(this, "Receta y Medicamento agregado correctamente", "Informacion importante", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al intentar agregar, verifique de nuevo ó consulte con un experto", "Informacion importante", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addBotActionPerformed

    private void editBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBotActionPerformed
        int filaSelect = tablaRecetas.getSelectedRow();
        if (filaSelect == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione y cargue los datos para editar", "Información importante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReceta = (int) tablaRecetas.getValueAt(filaSelect, 3);
        try {
            cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());
            cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());

            RecetaMedica rm = cRecetaMedica.findRecetaMedica(idReceta);

            if (rm == null) {
                JOptionPane.showMessageDialog(this, "Receta no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            rm.setInstrucciones(instruccTA.getText());
            cRecetaMedica.edit(rm);

            // Buscar y actualizar medicamento si corresponde
            boolean tieneMed = siCB.isSelected();
            List<RecetaMedicamento> lista = cRecetaMedicamento.findRecetaMedicamentoEntities();
            RecetaMedicamento recMed = null;

            for (RecetaMedicamento r : lista) {
                if (r.getIdReceta().getIdReceta() == idReceta) {
                    recMed = r;
                    break;
                }
            }

            if (tieneMed) {
                String keyMed = (String) comboMedi.getSelectedItem();
                Medicamento medN = (Medicamento) medicamentosMap.get(keyMed);

                if (recMed != null) {
                    recMed.setIdMedicamento(medN);
                    recMed.setDosis(dosisField.getText());
                    recMed.setFrecuencia(frecuenciaField.getText());
                    recMed.setDuracion(duracionField.getText());
                    recMed.setCantidad((int) cantidadSpinner.getValue());
                    cRecetaMedicamento.edit(recMed);
                } else {
                    // No existía antes, se crea nuevo
                    RecetaMedicamento nuevo = new RecetaMedicamento();
                    nuevo.setIdReceta(rm);
                    nuevo.setIdMedicamento(medN);
                    nuevo.setDosis(dosisField.getText());
                    nuevo.setFrecuencia(frecuenciaField.getText());
                    nuevo.setDuracion(duracionField.getText());
                    nuevo.setCantidad((int) cantidadSpinner.getValue());
                    nuevo.setCreatedAt(new Date());
                    cRecetaMedicamento.create(nuevo);
                }

            } else {
                // Si no debe tener medicamento pero sí existe, se elimina
                if (recMed != null) {
                    cRecetaMedicamento.destroy(recMed.getIdRecetaMedicamento());
                }
            }

            JOptionPane.showMessageDialog(this, "Receta actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablas();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar la receta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_editBotActionPerformed

    private void deleteBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBotActionPerformed
        int filaSelect = tablaRecetas.getSelectedRow();
        if (filaSelect == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una receta para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReceta = (int) tablaRecetas.getValueAt(filaSelect, 3);
        try {
            cRecetaMedica = new RecetaMedicaJpaController(admDatos.getEmf());
            cRecetaMedicamento = new RecetaMedicamentoJpaController(admDatos.getEmf());

            // Primero, eliminar medicamento (si existe)
            List<RecetaMedicamento> lista = cRecetaMedicamento.findRecetaMedicamentoEntities();
            for (RecetaMedicamento r : lista) {
                if (r.getIdReceta().getIdReceta() == idReceta) {
                    cRecetaMedicamento.destroy(r.getIdRecetaMedicamento());
                    break;
                }
            }

            // Luego, eliminar receta médica
            cRecetaMedica.destroy(idReceta);

            JOptionPane.showMessageDialog(this, "Receta eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablas();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la receta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBotActionPerformed

    private void siCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siCBActionPerformed
        activarMedicamentos(true);
    }//GEN-LAST:event_siCBActionPerformed

    private void noCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noCBActionPerformed
        activarMedicamentos(false);
    }//GEN-LAST:event_noCBActionPerformed

    private void buscBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscBotActionPerformed
        String criterio = buscadorField.getText().trim();
        if (!criterio.isEmpty()) {
            buscarPorMedico(criterio);
        } else {
            // Si está vacío, mostramos todas las recetas
            modeloReceta = new ModTabReceta(recetasMedicas);
            tablaRecetas.setModel(modeloReceta);
        }
    }//GEN-LAST:event_buscBotActionPerformed

    private void limpiarCampos() {
        instruccTA.setText("");
        dosisField.setText("");
        frecuenciaField.setText("");
        duracionField.setText("");
        cantidadSpinner.setValue(1);
        pacientefield.setText("");
        citafield.setText("");
        comboMedi.setSelectedIndex(0); // si tiene un valor por defecto
        siCB.setSelected(false);
    }

    private void activarMedicamentos(boolean act) {
        siCB.setSelected(act);
        comboMedi.setEnabled(act);
        dosisField.setEnabled(act);
        frecuenciaField.setEnabled(act);
        duracionField.setEnabled(act);
        cantidadSpinner.setEnabled(act);

    }

    private void buscarPorMedico(String criterio) {
        List<RecetaMedica> filtradas = new ArrayList<>();
        for (RecetaMedica receta : recetasMedicas) {
            CitaMedica cita = receta.getIdCita();
            Medico medico = cita.getIdMedico(); // Asegúrate que la cita tenga un médico asociado

            if (medico != null) {
                String nombreCompleto = medico.getNombre() + " " + medico.getApellidoPaterno() + " " + medico.getApellidoMaterno();
                String idMedico = String.valueOf(medico.getIdMedico());

                if (nombreCompleto.toLowerCase().contains(criterio.toLowerCase()) || idMedico.equals(criterio)) {
                    filtradas.add(receta);
                }
            }
        }

        modeloReceta = new ModTabReceta(filtradas);
        tablaRecetas.setModel(modeloReceta);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup accionesGrupo;
    private javax.swing.ButtonGroup activarMedicamento;
    private javax.swing.JButton addBot;
    private javax.swing.JCheckBox addCB;
    private javax.swing.JButton buscBot;
    private javax.swing.JTextField buscadorField;
    private javax.swing.JSpinner cantidadSpinner;
    private javax.swing.JButton cargarBot;
    private javax.swing.JTextField citafield;
    private javax.swing.JComboBox<String> comboMedi;
    private javax.swing.JButton deleteBot;
    private javax.swing.JTextArea detalleTrat;
    private javax.swing.JTextField dosisField;
    private javax.swing.JTextField duracionField;
    private javax.swing.JButton editBot;
    private javax.swing.JCheckBox editarCB;
    private javax.swing.JCheckBox eliminarCB;
    private javax.swing.JTextField frecuenciaField;
    private javax.swing.JTable infoTabla;
    private javax.swing.JTextArea instruccTA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JCheckBox noCB;
    private javax.swing.JTextField pacientefield;
    private javax.swing.JCheckBox siCB;
    private javax.swing.JTable tabRMedica;
    private javax.swing.JTable tablaRecetas;
    private javax.swing.JButton updateTablesButt;
    // End of variables declaration//GEN-END:variables
}
