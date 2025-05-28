/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import control.AdmDatos;
import control.MedicamentoJpaController;
import java.awt.Dimension;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Medicamento;
import modelo.ModTabMedicamentos;

/**
 *
 * @author carlo
 */
public class PanelMedicamentos extends javax.swing.JPanel {
    //Variables modificables
    //Variables de control de datos
    private AdmDatos admDatos = new AdmDatos();
    private MedicamentoJpaController medicamentoController;
    private List<Medicamento> medicamento;
    private ModTabMedicamentos modelo;
    //Variables usadas para modificar medicamentos
    private boolean modoEdicion = false;
    private Medicamento medicamentoSeleccionado;
    //Variables usadas para el panel deslizante
    private Timer timerDeslizamiento;
    private boolean desplegado = false;
    private int anchoObjetivo = 490; // Ancho deseado del formulario
    private int velocidadDeslizamiento = 20; // Aumentado para animación más fluida
    private int anchoActual = 0; // Nuevo campo para rastrear el ancho actual
    private DefaultTableModel modeloTabla; //Inicializamos el modelo de la tabla para cargarla

    public PanelMedicamentos() {
        initComponents();
        botonModificar.setEnabled(false);
        medicamentoController = new MedicamentoJpaController(admDatos.getEmf());
        cargarTablaMedicamentos();
        configurarPanelDeslizante();
        configurarListeners();
        buscador.setJTable(tablaMedicamentos);
    }
    
    private void cargarTablaMedicamentos() {
        medicamento = medicamentoController.findMedicamentoEntities();
        medicamento.sort(Comparator.comparing(Medicamento::getIdMedicamento));
        modelo = new ModTabMedicamentos(medicamento);
        tablaMedicamentos.setModel(modelo);
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        modelo.fireTableDataChanged(); // Notificar cambios al modelo
        tablaMedicamentos.repaint();
        tablaMedicamentos.revalidate();
    }
    
    //Métodos para que el panel del formulario se deslice
    private void configurarPanelDeslizante() {
        Dimension preferredSize = new Dimension(0, panelFormularioDeslizante.getHeight());
        panelFormularioDeslizante.setPreferredSize(preferredSize);
        panelFormularioDeslizante.setMinimumSize(preferredSize);
        panelFormularioDeslizante.setMaximumSize(preferredSize);
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();
    }
    //Listeners y configuración de ellos
    private void configurarListeners() {

        timerDeslizamiento = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animarDeslizamiento();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retraerFormulario();
                limpiarCampos();
            }
        });
        
        tablaMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = tablaMedicamentos.getSelectedRow() != -1;
            botonModificar.setEnabled(filaSeleccionada);
        });  
        
        // Botón Agregar
        botonAgregar.addActionListener(e -> {
            modoEdicion = false; // Asegurar modo creación
            desplegarFormulario();
        });

        // Botón Modificar (solo 1 listener)
        botonModificar.addActionListener(e -> {
            int filaSeleccionada = tablaMedicamentos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un medicamento", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Obtener ID de la columna correcta (ej. columna 0)
                int id = (int) tablaMedicamentos.getValueAt(filaSeleccionada, 0);
                medicamentoSeleccionado = medicamentoController.findMedicamento(id);

                if (medicamentoSeleccionado == null) {
                    JOptionPane.showMessageDialog(null, "Error: Medicamento no existe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cargar datos en los campos
                nombreField.setText(medicamentoSeleccionado.getNombre());
                jTextArea1.setText(medicamentoSeleccionado.getDescripcion() != null ? medicamentoSeleccionado.getDescripcion() : "");
                presentacionField.setText(medicamentoSeleccionado.getPresentacion() != null ? medicamentoSeleccionado.getPresentacion() : "");
                dosisField.setText(medicamentoSeleccionado.getDosisRecomendada() != null ? medicamentoSeleccionado.getDosisRecomendada() : "");
                jTextArea2.setText(medicamentoSeleccionado.getContraindicaciones() != null ? medicamentoSeleccionado.getContraindicaciones() : "");

                modoEdicion = true;
                desplegarFormulario(); // Expandir el panel

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
    botonGuardar.addActionListener(e -> {
        try {
            if (modoEdicion && medicamentoSeleccionado != null) { 
                // Actualizar medicamento existente
                actualizarMedicamentoDesdeFormulario();
                medicamentoController.edit(medicamentoSeleccionado);
                JOptionPane.showMessageDialog(null, "¡Medicamento actualizado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else { 
                // Crear nuevo medicamento
                Medicamento nuevoMedicamento = crearMedicamentoDesdeFormulario();
                medicamentoController.create(nuevoMedicamento);
                JOptionPane.showMessageDialog(null, "¡Medicamento creado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            actualizarTablaYFormulario();

        } catch (Exception ex) {
            manejarError(ex);
        }
    });
    
    }
    //Métodos auxiliares
    
    private void actualizarMedicamentoDesdeFormulario() {
        medicamentoSeleccionado.setNombre(nombreField.getText().trim());
        medicamentoSeleccionado.setDescripcion(jTextArea1.getText().trim());
        medicamentoSeleccionado.setPresentacion(presentacionField.getText().trim());
        medicamentoSeleccionado.setDosisRecomendada(dosisField.getText().trim());
        medicamentoSeleccionado.setContraindicaciones(jTextArea2.getText().trim());
        medicamentoSeleccionado.setUpdatedAt(new Date());
    }

    private Medicamento crearMedicamentoDesdeFormulario() {
        Medicamento nuevo = new Medicamento();
        nuevo.setNombre(nombreField.getText().trim());
        nuevo.setDescripcion(jTextArea1.getText().trim());
        nuevo.setPresentacion(presentacionField.getText().trim());
        nuevo.setDosisRecomendada(dosisField.getText().trim());
        nuevo.setContraindicaciones(jTextArea2.getText().trim());
        nuevo.setCreatedAt(new Date());
        nuevo.setUpdatedAt(new Date());
        return nuevo;
    }

    private void actualizarTablaYFormulario() {
        cargarTablaMedicamentos();
        retraerFormulario();
        limpiarCampos();
        modoEdicion = false;
        medicamentoSeleccionado = null;
    }

    private void manejarError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Error: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void desplegarFormulario() {
        
        desplegado = true;
        timerDeslizamiento.start();
        panelFormularioDeslizante.setVisible(true); 
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();

    }

    private void retraerFormulario() {
        desplegado = false;
        timerDeslizamiento.start();
    }

    private void limpiarCampos() {
        medicamentoSeleccionado = null;
        nombreField.setText("");
        jTextArea1.setText("");
        presentacionField.setText("");
        dosisField.setText("");
        jTextArea2.setText("");
    }

    private void animarDeslizamiento() {
        int anchoActual = panelFormularioDeslizante.getPreferredSize().width;
        int nuevoAncho;
        
        if (desplegado) {
            nuevoAncho = Math.min(anchoActual + velocidadDeslizamiento, anchoObjetivo);
        } else {
            nuevoAncho = Math.max(anchoActual - velocidadDeslizamiento, 0);
        }
        
        // Actualizar el tamaño del panel
        panelFormularioDeslizante.setPreferredSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        panelFormularioDeslizante.setMinimumSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        panelFormularioDeslizante.setMaximumSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        
        // Refrescar la visualización
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();
        revalidate();
        repaint();
        
        // Detener el timer cuando se alcanza el objetivo
        if ((desplegado && nuevoAncho == anchoObjetivo) || (!desplegado && nuevoAncho == 0)) {
            timerDeslizamiento.stop();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelFormularioDeslizante = new javax.swing.JPanel();
        nombreLabel = new javax.swing.JLabel();
        nombreField = new javax.swing.JTextField();
        descripcionLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        presentacionLabel = new javax.swing.JLabel();
        presentacionField = new javax.swing.JTextField();
        dosisLabel = new javax.swing.JLabel();
        dosisField = new javax.swing.JTextField();
        dosisLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        botonGuardar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        descripcionLabel1 = new javax.swing.JLabel();
        dosis = new javax.swing.JLabel();
        descripcionLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMedicamentos = new javax.swing.JTable();
        buscador = new miscomponentes.JBuscador();
        botonAgregar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        botonModificar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        panelFormularioDeslizante.setLayout(new java.awt.FlowLayout()); // O BorderLayout, o null (para layout absoluto)
        panelFormularioDeslizante.setBackground(new java.awt.Color(255, 255, 255));

        nombreLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        nombreLabel.setText("Nombre:");

        descripcionLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        descripcionLabel.setText("Descripción:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        presentacionLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        presentacionLabel.setText("Presentación:");

        dosisLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        dosisLabel.setText("Dosis recomendada:");

        dosisLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        dosisLabel2.setText("Contraindicaciones:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        botonGuardar.setText("Guardar");

        botonCancelar.setBackground(new java.awt.Color(153, 0, 0));
        botonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Agrega un medicamento");

        descripcionLabel1.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        descripcionLabel1.setText("Nota: Describa si el medicamento es analgésico, antihistamínico, etc.");

        dosis.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        dosis.setText("ej: 2 tabletas cada 8 horas, especificar la dosis usual");

        descripcionLabel2.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        descripcionLabel2.setText("La presentación que trae y el gramaje, Ej: Tabletas 150mg");

        javax.swing.GroupLayout panelFormularioDeslizanteLayout = new javax.swing.GroupLayout(panelFormularioDeslizante);
        panelFormularioDeslizante.setLayout(panelFormularioDeslizanteLayout);
        panelFormularioDeslizanteLayout.setHorizontalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(128, 128, 128))
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(nombreLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreField))
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(descripcionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addComponent(descripcionLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(dosisLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dosisField))
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addComponent(dosisLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addGap(136, 136, 136)
                                .addComponent(dosis)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addComponent(presentacionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(descripcionLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(presentacionField))
                .addContainerGap())
        );
        panelFormularioDeslizanteLayout.setVerticalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombreLabel))
                .addGap(38, 38, 38)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descripcionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descripcionLabel1)
                .addGap(32, 32, 32)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(presentacionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(presentacionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcionLabel2)
                .addGap(38, 38, 38)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosisLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dosis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(dosisLabel2)
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -14;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(panelFormularioDeslizante, gridBagConstraints);

        tablaMedicamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "ID Medicamento", "Nombre del medicamento", "Descripción", "Presentación", "Dosis recomendada"
            }
        ));
        jScrollPane1.setViewportView(tablaMedicamentos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 729;
        gridBagConstraints.ipady = 468;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 0);
        add(jScrollPane1, gridBagConstraints);

        buscador.setJTable(tablaMedicamentos);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 252;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        add(buscador, gridBagConstraints);

        botonAgregar.setText("Agregar Medicamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 0);
        add(botonAgregar, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Registro de Medicamentos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 188, 0, 0);
        add(jLabel2, gridBagConstraints);

        botonModificar.setText("Modificar Medicamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        add(botonModificar, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Eliminar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 51;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 0);
        add(jButton1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAgregar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton botonModificar;
    private miscomponentes.JBuscador buscador;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JLabel descripcionLabel1;
    private javax.swing.JLabel descripcionLabel2;
    private javax.swing.JLabel dosis;
    private javax.swing.JTextField dosisField;
    private javax.swing.JLabel dosisLabel;
    private javax.swing.JLabel dosisLabel2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField nombreField;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JPanel panelFormularioDeslizante;
    private javax.swing.JTextField presentacionField;
    private javax.swing.JLabel presentacionLabel;
    private javax.swing.JTable tablaMedicamentos;
    // End of variables declaration//GEN-END:variables
    

}
