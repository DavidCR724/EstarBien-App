/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import control.AdmDatos;
import control.FacturaJpaController;
import control.PacienteJpaController;
import control.PagoJpaController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.ModListPacientesPago;
import modelo.ModListPagos;
import modelo.Paciente;
import modelo.Pago;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import modelo.Factura;




/**
 *
 * @author carlo
 */
public class PanelFacturas extends javax.swing.JPanel {
    private final AdmDatos admDatos = new AdmDatos();
    private List <Paciente> pacientes;
    private List<Pago> pagos = new ArrayList();
    private PacienteJpaController pacienteController;
    private FacturaJpaController facturaController;
    private PagoJpaController pagoController;
    private ModListPacientesPago listaPacientes;
    private ModListPagos listaPagos;
    private Paciente pacienteSeleccionado;
    private Pago pagoSeleccionado;
    private static final BigDecimal IVA_PORCENTAJE = new BigDecimal("0.16");
    private static final BigDecimal IVA_TOTAL = new BigDecimal("1.16");
    private FacturaPanelPreview facturaPanelPreview;
    
    
    public PanelFacturas() {
        initComponents();
        cargarControladores();
        configurarListeners();
        inicializarModelos();
        cargarListaPacientes();
        buscadorListaPacientes.setListaPrincipal(jListPacientes);
        buscadorListaPagos.setListaPrincipal(JListPago);
        resetearSelecciones();
        configurarPlaceHolders();
        configurarListenersCamposTexto();
        
        facturaPanelPreview = new FacturaPanelPreview();

        // Cambiar a GridBagLayout para centrar
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(220, 220, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Centrar vertical y horizontalmente
        wrapperPanel.add(facturaPanelPreview, gbc);

        scrollPaneFactura.setViewportView(wrapperPanel);
        scrollPaneFactura.getViewport().setBackground(Color.WHITE);
    }
    
    /*Cargar los controladores para poder encontrar los pacientes, y pagos en las listas
    y el controlador de facturas para poder agregarlo a la base de datos
    */
    public void cargarControladores(){
        pacienteController = new PacienteJpaController(admDatos.getEmf());
        facturaController = new FacturaJpaController(admDatos.getEmf());
        pagoController = new PagoJpaController(admDatos.getEmf());
    }
    //Inicializa los modelos del JList para que sean compatibles con los ModList de pago y paciente
    private void inicializarModelos() {
        jListPacientes.setModel(new ModListPacientesPago(new ArrayList<>()));
        JListPago.setModel(new ModListPagos(new ArrayList<>()));
    }
    //Carga el JList de pacientes.
    private void cargarListaPacientes() {
        pacientes = pacienteController.findPacienteEntities(); //Retorna una lista con pacientes
        //Retorna una lista tipo ModList que solo contiene los pacientes activos.
        listaPacientes = new ModListPacientesPago(pacientes); 
        jListPacientes.setModel(listaPacientes); //Pasa los elementos del modelo a la Jlist.
    }
    
    private void cargarPagosPorPaciente(Paciente paciente) {
        // Limpiar selección actual y datos
        JListPago.clearSelection();
        limpiarDatosFactura();

        // Cargar nuevos pagos
        pagos = pagoController.findPagosByPaciente(paciente);
        listaPagos = new ModListPagos(pagos);

        // Actualizar modelo y forzar repintado
        JListPago.setModel(listaPagos);
        listaPagos.actualizar(pagos);

        razonSocialField.setText(paciente.getNombre() + " " + paciente.getApellidoPaterno() + " " + paciente.getApellidoMaterno());
        
        // Obtener dirección del paciente si existe
        if (paciente.getDireccion() != null) {
            String[] direccionParts = paciente.getDireccion().split(",");
            if (direccionParts.length > 0) calleField.setText(direccionParts[0].trim());
            if (direccionParts.length > 1) coloniaField.setText(direccionParts[1].trim());
            if (direccionParts.length > 2) cpField.setText(direccionParts[2].trim());
            if (direccionParts.length > 3) localidadField.setText(direccionParts[3].trim());
            if (direccionParts.length > 4) entidadFederativaField.setText(direccionParts[4].trim());
        }
        
        // Limpiar selección explícitamente
        JListPago.clearSelection();
        pagoSeleccionado = null;
    }
    
    private void configurarListeners() {
        jListPacientes.addListSelectionListener(e -> { 
            //Para que cuando seleccione un paciente, salgan los pagos asociados a ese paciente.
            if (!e.getValueIsAdjusting() && !jListPacientes.isSelectionEmpty()) {
                // Limpiar selección de pagos antes de cambiar
                JListPago.clearSelection();
                limpiarDatosFactura();

                pacienteSeleccionado = pacientes.get(jListPacientes.getSelectedIndex());
                cargarPagosPorPaciente(pacienteSeleccionado);
                

                // Forzar actualización de la interfaz
                jListPacientes.repaint();
            }
        });

        // Listener para selección de pagos
        JListPago.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !JListPago.isSelectionEmpty()) {
                // Verificar que pagos no sea nulo
                if (pagos != null && JListPago.getSelectedIndex() < pagos.size()) {
                    pagoSeleccionado = pagos.get(JListPago.getSelectedIndex());
                    actualizarDatosFactura(pagoSeleccionado);
                    actualizarCamposDeTextoConPago(pagoSeleccionado);
                    
                    // Forzar actualización
                    JListPago.repaint();
                }
            }
        });
        
        
        buttonDescargarFactura.addActionListener(e -> generarPDF());
        buttonGuardarFactura.addActionListener(e -> guardarFactura());
        buttonCancelar.addActionListener(e -> cancelar());

    }
    
    private void configurarListenersCamposTexto() {
        FocusAdapter actualizarListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarPrevisualizacion();
            }
        };
        razonSocialField.addFocusListener(actualizarListener);
        conceptoField.addFocusListener(actualizarListener);
        cpField.addFocusListener(actualizarListener);
        calleField.addFocusListener(actualizarListener);
        coloniaField.addFocusListener(actualizarListener);
        numeroInteriorField.addFocusListener(actualizarListener);
        numeroExteriorField.addFocusListener(actualizarListener);
        localidadField.addFocusListener(actualizarListener);
        entidadFederativaField.addFocusListener(actualizarListener);
         rfcField.addFocusListener(actualizarListener);
    }
    
    private void actualizarDatosFactura(Pago pago) {
        
        if (pago == null || pago.getMonto() == null) {
            limpiarDatosFactura();
            return;
        }

        BigDecimal monto = pago.getMonto();
        BigDecimal iva = monto.multiply(IVA_PORCENTAJE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = monto.add(iva).setScale(2, RoundingMode.HALF_UP);

        // Formatear fecha a dd/MM/yyyy
        String fechaFormateada = "N/A";
        if (pago.getFecha() != null) {
            java.util.Date fechaUtil;
            if (pago.getFecha() instanceof java.sql.Date) {
                fechaUtil = new java.util.Date(pago.getFecha().getTime());
            } else {
                fechaUtil = pago.getFecha();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormateada = sdf.format(fechaUtil);
        }

        // Generar folio de factura (real o provisional)
        String folioFactura;
        if (pago.getFactura() != null) {
            folioFactura = String.valueOf(pago.getFactura().getIdFactura());
        } else {
            // Calcular el próximo ID disponible
            int nextId = facturaController.getFacturaCount() + 1;
            folioFactura = "PROV-" + nextId;
        }

        rfcField.setText(pago.getFactura().getRfc());
        conceptoField.setText(" " + pago.getFactura().getConcepto());
        folioFacturaLabel.setText("Folio de factura: " + folioFactura);
        fechaLabel.setText("Fecha: " + fechaFormateada);
        folioPagoLabel.setText("Folio de pago: " + pago.getIdPago());
        subtotalLabel.setText(String.format("Subtotal: $%.2f", monto));
        ivaLabel.setText(String.format("IVA: $%.2f", iva));
        totalLabel.setText(String.format("Total: $%.2f", total));

        // Actualizar previsualización
        actualizarPrevisualizacion();
    }

    private void limpiarDatosFactura() {
        folioFacturaLabel.setText("Folio de factura:");
        fechaLabel.setText("Fecha:");
        folioPagoLabel.setText("Folio de pago:");
        subtotalLabel.setText("Subtotal:");
        ivaLabel.setText("IVA:");
        totalLabel.setText("Total:");
    }
    
    private void resetearSelecciones() {
        jListPacientes.clearSelection();
        JListPago.clearSelection();
        pacienteSeleccionado = null;
        pagoSeleccionado = null;
        limpiarDatosFactura();
    }
    
    private void configurarPlaceHolders(){
        razonSocialField.setText("");
        conceptoField.setText("");
        cpField.setText("");
        calleField.setText("");
        coloniaField.setText("");
        numeroInteriorField.setText("");
        numeroExteriorField.setText("");
        localidadField.setText("");
        entidadFederativaField.setText("");
        rfcField.setText("");
        
        setPlaceholder(razonSocialField, "Razón Social...");
        setPlaceholder(conceptoField, "Concepto...");
        setPlaceholder(cpField, "Código Postal...");
        setPlaceholder(calleField, "Ingrese la calle");
        setPlaceholder(coloniaField, "Colonia");
        setPlaceholder(numeroInteriorField, "#N. Interior");
        setPlaceholder(numeroExteriorField, "#N. Exterior");
        setPlaceholder(localidadField, "Localidad");
        setPlaceholder(entidadFederativaField, "Estado/Entidad federativa");
        setPlaceholder(rfcField, "RFC");
        
    }
        
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        
            if (placeholder.equals("RFC...")) {
        rfcField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetterOrDigit(c) || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
    }
    }
    
    private void actualizarPrevisualizacion() {
    // Obtener datos básicos de los campos
        String razonSocial = razonSocialField.getText().equals("Razón Social...") ? "" : razonSocialField.getText();
        String rfc = rfcField.getText().equals("RFC...") ? "" : rfcField.getText();
        String concepto = conceptoField.getText().equals("Concepto...") ? "" : conceptoField.getText();

        // Construir dirección completa con formato profesional
        StringBuilder direccionBuilder = new StringBuilder();

        // Calle y números
        if (!calleField.getText().isEmpty() && !calleField.getText().equals("Ingrese la calle")) {
            direccionBuilder.append(calleField.getText());

            if (!numeroExteriorField.getText().isEmpty() && !numeroExteriorField.getText().equals("#N. Exterior")) {
                direccionBuilder.append(" #").append(numeroExteriorField.getText());
            }

            if (!numeroInteriorField.getText().isEmpty() && !numeroInteriorField.getText().equals("#N. Interior")) {
                direccionBuilder.append(" Int. ").append(numeroInteriorField.getText());
            }

            direccionBuilder.append(", ");
        }

        // Colonia
        if (!coloniaField.getText().isEmpty() && !coloniaField.getText().equals("Colonia")) {
            direccionBuilder.append(coloniaField.getText()).append(", ");
        }

        // Código Postal
        if (!cpField.getText().isEmpty() && !cpField.getText().equals("Código Postal...")) {
            direccionBuilder.append("C.P. ").append(cpField.getText()).append(", ");
        }

        // Localidad y Entidad Federativa
        if (!localidadField.getText().isEmpty() && !localidadField.getText().equals("Localidad")) {
            direccionBuilder.append(localidadField.getText()).append(", ");
        }

        if (!entidadFederativaField.getText().isEmpty() && !entidadFederativaField.getText().equals("Estado/Entidad federativa")) {
            direccionBuilder.append(entidadFederativaField.getText());
        }

        // Limpiar coma final si existe
        String direccion = direccionBuilder.toString();
        if (direccion.endsWith(", ")) {
            direccion = direccion.substring(0, direccion.length() - 2);
        }

        // Obtener datos de las etiquetas
        String folioFactura = folioFacturaLabel.getText().replace("Folio de factura: ", "");
        String fecha = fechaLabel.getText().replace("Fecha: ", "");
        String folioPago = folioPagoLabel.getText().replace("Folio de pago: ", "");
        String subtotal = subtotalLabel.getText().replace("Subtotal: ", "");
        String iva = ivaLabel.getText().replace("IVA: ", "");
        String total = totalLabel.getText().replace("Total: ", "");

        // Actualizar panel de previsualización con TODOS los datos
        facturaPanelPreview.setDatosFactura(
            razonSocial,
            rfc,
            direccion,
            concepto,
            folioFactura,
            fecha,
            folioPago,
            subtotal,
            iva,
            total
        );
    }
    
    private void actualizarCamposDeTextoConPago(Pago p) {
        // Definir los placeholders para cada campo
        final String RAZON_SOCIAL_PLACEHOLDER = "Razón Social...";
        final String CONCEPTO_PLACEHOLDER = "Concepto...";
        final String RFC_PLACEHOLDER = "RFC";
        final String CALLE_PLACEHOLDER = "Ingrese la calle";
        final String COLONIA_PLACEHOLDER = "Colonia";
        final String CP_PLACEHOLDER = "Código Postal...";
        final String LOCALIDAD_PLACEHOLDER = "Localidad";
        final String ENTIDAD_PLACEHOLDER = "Estado/Entidad federativa";

        // Función auxiliar para verificar y actualizar el color del campo
        Consumer<JTextField> updateFieldColor = field -> {
            String placeholder = "";
            if (field == razonSocialField) placeholder = RAZON_SOCIAL_PLACEHOLDER;
            else if (field == conceptoField) placeholder = CONCEPTO_PLACEHOLDER;
            else if (field == rfcField) placeholder = RFC_PLACEHOLDER;
            else if (field == calleField) placeholder = CALLE_PLACEHOLDER;
            else if (field == coloniaField) placeholder = COLONIA_PLACEHOLDER;
            else if (field == cpField) placeholder = CP_PLACEHOLDER;
            else if (field == localidadField) placeholder = LOCALIDAD_PLACEHOLDER;
            else if (field == entidadFederativaField) placeholder = ENTIDAD_PLACEHOLDER;

            if (!field.getText().equals(placeholder) && !field.getText().isEmpty()) {
                field.setForeground(Color.BLACK);
            }
        };

        // Cargar datos
        razonSocialField.setText(p.getIdPaciente().getNombre() + " " + 
                                p.getIdPaciente().getApellidoPaterno() + " " + 
                                p.getIdPaciente().getApellidoMaterno());

        if (p.getIdPaciente().getDireccion() != null) {
            String[] direccionParts = p.getIdPaciente().getDireccion().split(",");
            if (direccionParts.length > 0) calleField.setText(direccionParts[0].trim());
            if (direccionParts.length > 1) coloniaField.setText(direccionParts[1].trim());
            if (direccionParts.length > 2) cpField.setText(direccionParts[2].trim());
            if (direccionParts.length > 3) localidadField.setText(direccionParts[3].trim());
            if (direccionParts.length > 4) entidadFederativaField.setText(direccionParts[4].trim());
        }

        // Actualizar colores de todos los campos
        updateFieldColor.accept(razonSocialField);
        updateFieldColor.accept(conceptoField);
        updateFieldColor.accept(rfcField);
        updateFieldColor.accept(calleField);
        updateFieldColor.accept(coloniaField);
        updateFieldColor.accept(cpField);
        updateFieldColor.accept(localidadField);
        updateFieldColor.accept(entidadFederativaField);

        // Campos que siempre deben mantener su placeholder si están vacíos
        if (numeroInteriorField.getText().isEmpty() || 
            numeroInteriorField.getText().equals("#N. Interior")) {
            numeroInteriorField.setForeground(Color.GRAY);
        }

        if (numeroExteriorField.getText().isEmpty() || 
            numeroExteriorField.getText().equals("#N. Exterior")) {
            numeroExteriorField.setForeground(Color.GRAY);
        }
    }
    
    private void generarPDF() {
        // Obtener los datos actuales
        String razonSocial = razonSocialField.getText().equals("Razón Social...") ? "" : razonSocialField.getText();
        String rfc = rfcField.getText().equals("RFC") ? "" : rfcField.getText();
        String concepto = conceptoField.getText().equals("Concepto...") ? "" : conceptoField.getText();
        String folioFactura = folioFacturaLabel.getText().replace("Folio de factura: ", "");
        String fecha = fechaLabel.getText().replace("Fecha: ", "");
        String folioPago = folioPagoLabel.getText().replace("Folio de pago: ", "");
        String subtotal = subtotalLabel.getText().replace("Subtotal: ", "");
        String iva = ivaLabel.getText().replace("IVA: ", "");
        String total = totalLabel.getText().replace("Total: ", "");

        // Construir dirección  (similar a la previsualización)
        StringBuilder direccion = new StringBuilder();
        if (!calleField.getText().equals("Ingrese la calle")) {
            direccion.append(calleField.getText());
        }
        if (!numeroExteriorField.getText().equals("#N. Exterior")) {
            direccion.append(" #").append(numeroExteriorField.getText());
        }
        if (!numeroInteriorField.getText().equals("#N. Interior")) {
            direccion.append(" Int. ").append(numeroInteriorField.getText());
        }
        if (!coloniaField.getText().equals("Colonia")) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(coloniaField.getText());
        }
        if (!cpField.getText().equals("Código Postal...")) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append("C.P. ").append(cpField.getText());
        }
        if (!localidadField.getText().equals("Localidad")) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(localidadField.getText());
        }
        if (!entidadFederativaField.getText().equals("Estado/Entidad federativa")) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(entidadFederativaField.getText());
        }

        // Crear diálogo para guardar archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Factura PDF");
        fileChooser.setSelectedFile(new File("Factura_" + folioFactura + ".pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }

            try {
                // Crear documento
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.LETTER);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                PdfContentByte cb = writer.getDirectContent();

                // Configuración inicial
                cb.saveState();
                cb.setColorFill(BaseColor.WHITE);
                cb.rectangle(0, 0, PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight());
                cb.fill();
                cb.setColorStroke(BaseColor.BLACK);
                cb.setColorFill(BaseColor.BLACK);
                cb.restoreState();

                // Dibujar borde
                cb.rectangle(30, 30, 
                    PageSize.LETTER.getWidth() - 60, 
                    PageSize.LETTER.getHeight() - 60);
                cb.stroke();

                // Configurar fuentes
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                BaseFont bfNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

                // Iniciar texto
                cb.beginText();

                // Título (centrado)
                cb.setFontAndSize(bf, 24);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "FACTURA", 
                    PageSize.LETTER.getWidth() / 2, 
                    PageSize.LETTER.getHeight() - 70, 0);

                // Razón Social
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Razón Social:", 50, PageSize.LETTER.getHeight() - 100, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, razonSocial, 150, PageSize.LETTER.getHeight() - 100, 0);

                // RFC
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 50, PageSize.LETTER.getHeight() - 120, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, rfc, 150, PageSize.LETTER.getHeight() - 120, 0);

                // Dirección
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dirección:", 50, PageSize.LETTER.getHeight() - 140, 0);
                cb.setFontAndSize(bfNormal, 12);

                // Calcular posición para dirección (manejar múltiples líneas)
                float yPos = PageSize.LETTER.getHeight() - 125;
                String[] direccionPartes = splitStringByLength(direccion.toString(), 60);
                for (String parte : direccionPartes) {
                    yPos -= 15; // Espacio entre líneas
                    cb.showTextAligned(PdfContentByte.ALIGN_LEFT, parte, 150, yPos, 0);
                }

                // Título "Detalles de Factura"
                yPos -= 30; // Espacio después de dirección
                cb.setFontAndSize(bf, 14);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Detalles de Factura", 50, yPos, 0);
                yPos -= 20;

                // Folio Factura
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Folio Factura:", 50, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, folioFactura, 150, yPos, 0);
                yPos -= 20;

                // Fecha
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha:", 50, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, fecha, 150, yPos, 0);
                yPos -= 20;

                // Folio Pago
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Folio Pago:", 50, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, folioPago, 150, yPos, 0);
                yPos -= 30;

                // Concepto
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Concepto:", 50, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, concepto, 150, yPos, 0);
                yPos -= 40;

                // Totales (alineados a la derecha)
                float rightMargin = PageSize.LETTER.getWidth() - 50;

                // Subtotal
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Subtotal:", 400, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, subtotal, rightMargin, yPos, 0);
                yPos -= 30;

                // IVA
                cb.setFontAndSize(bf, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "IVA:", 400, yPos, 0);
                cb.setFontAndSize(bfNormal, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, iva, rightMargin, yPos, 0);
                yPos -= 30;

                // Total
                cb.setFontAndSize(bf, 12);
                cb.setFontAndSize(bf, 14); // Más grande para total
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "TOTAL:", 400, yPos, 0);
                cb.setFontAndSize(bfNormal, 14);
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, total, rightMargin, yPos, 0);

                cb.endText();
                document.close();

                JOptionPane.showMessageDialog(this, 
                    "Factura generada exitosamente:\n" + file.getAbsolutePath(),
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error al generar PDF: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void guardarFactura() {
        if (pagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un pago primero", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si ya existe una factura para este pago
        boolean facturaExistente = (pagoSeleccionado.getFactura() != null);

        if (facturaExistente) {
            int opcion = JOptionPane.showConfirmDialog(this,
                "Ya existe una factura para este pago. ¿Desea editarla?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            // Obtener datos de la interfaz
            String razonSocial = razonSocialField.getText().equals("Razón Social...") ? "" : razonSocialField.getText();
            String rfc = rfcField.getText().equals("RFC") ? "" : rfcField.getText();
            String concepto = conceptoField.getText().equals("Concepto...") ? "" : conceptoField.getText();

            // Construir dirección fiscal
            StringBuilder direccionFiscal = new StringBuilder();
            if (!calleField.getText().equals("Ingrese la calle")) 
                direccionFiscal.append(calleField.getText());
            if (!numeroExteriorField.getText().equals("#N. Exterior"))
                direccionFiscal.append(" #").append(numeroExteriorField.getText());
            if (!numeroInteriorField.getText().equals("#N. Interior"))
                direccionFiscal.append(" Int. ").append(numeroInteriorField.getText());
            if (!coloniaField.getText().equals("Colonia"))
                direccionFiscal.append(", ").append(coloniaField.getText());
            if (!cpField.getText().equals("Código Postal..."))
                direccionFiscal.append(", C.P. ").append(cpField.getText());
            if (!localidadField.getText().equals("Localidad"))
                direccionFiscal.append(", ").append(localidadField.getText());
            if (!entidadFederativaField.getText().equals("Estado/Entidad federativa"))
                direccionFiscal.append(", ").append(entidadFederativaField.getText());

            // Obtener montos
            BigDecimal subtotal = new BigDecimal(subtotalLabel.getText()
                .replace("Subtotal: $", "").replace(",", ""));
            BigDecimal iva = new BigDecimal(ivaLabel.getText()
                .replace("IVA: $", "").replace(",", ""));
            BigDecimal total = new BigDecimal(totalLabel.getText()
                .replace("Total: $", "").replace(",", ""));

            // Crear o actualizar la factura
            Factura factura;
            if (facturaExistente) {
                factura = pagoSeleccionado.getFactura();
            } else {
                factura = new Factura();
                factura.setFolio("FAC-" + (facturaController.getFacturaCount() + 1));
            }

            // Actualizar datos de la factura
            factura.setRazonSocial(razonSocial);
            factura.setRfc(rfc);
            factura.setConcepto(concepto);
            factura.setDireccionFiscal(direccionFiscal.toString());
            factura.setSubtotal(subtotal);
            factura.setIva(iva);
            factura.setTotal(total);
            factura.setFechaEmision(new Date());
            factura.setIdPago(pagoSeleccionado);

            // Guardar en la base de datos
            if (facturaExistente) {
                facturaController.edit(factura);
            } else {
                facturaController.create(factura);
            }

            // Actualizar la relación
            pagoSeleccionado.setFactura(factura);
            pagoController.edit(pagoSeleccionado);

            // Actualizar interfaz
            actualizarDatosFactura(pagoSeleccionado);
            JOptionPane.showMessageDialog(this,
                "Factura " + (facturaExistente ? "actualizada" : "creada") + " exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al guardar factura: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelar() {
        resetearSelecciones();
        configurarPlaceHolders();
        cargarListaPacientes();

        // Limpiar previsualización
        facturaPanelPreview.setDatosFactura(
            "", "", "", "", "", "", "", "", "", ""
        );
        facturaPanelPreview.repaint();

        // Limpiar campos de texto
        razonSocialField.setText("");
        conceptoField.setText("");
        rfcField.setText("");
        calleField.setText("");
        coloniaField.setText("");
        cpField.setText("");
        numeroInteriorField.setText("");
        numeroExteriorField.setText("");
        localidadField.setText("");
        entidadFederativaField.setText("");

        configurarPlaceHolders();
    }

    
    private String[] splitStringByLength(String text, int maxLength) {
        List<String> parts = new ArrayList<>();
        int length = text.length();

        for (int i = 0; i < length; i += maxLength) {
            parts.add(text.substring(i, Math.min(length, i + maxLength)));
        }

        return parts.toArray(new String[0]);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDatos = new javax.swing.JPanel();
        tituloLabel = new javax.swing.JLabel();
        folioFacturaLabel = new javax.swing.JLabel();
        fechaLabel = new javax.swing.JLabel();
        subtotalLabel = new javax.swing.JLabel();
        ivaLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        folioPagoLabel = new javax.swing.JLabel();
        pacienteLabel = new javax.swing.JLabel();
        buscadorListaPacientes = new miscomponentes.JBuscadorLista();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListPacientes = new javax.swing.JList<>();
        buscadorListaPagos = new miscomponentes.JBuscadorLista();
        jScrollPane3 = new javax.swing.JScrollPane();
        JListPago = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        scrollPaneFactura = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        razonSocialField = new javax.swing.JTextField();
        conceptoField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cpField = new javax.swing.JTextField();
        calleField = new javax.swing.JTextField();
        numeroInteriorField = new javax.swing.JTextField();
        numeroExteriorField = new javax.swing.JTextField();
        localidadField = new javax.swing.JTextField();
        entidadFederativaField = new javax.swing.JTextField();
        coloniaField = new javax.swing.JTextField();
        buttonGuardarFactura = new javax.swing.JButton();
        buttonCancelar = new javax.swing.JButton();
        rfcField = new javax.swing.JTextField();
        buttonDescargarFactura = new javax.swing.JButton();

        tituloLabel.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        tituloLabel.setText("Datos de Factura");

        folioFacturaLabel.setText("Folio de factura:");

        fechaLabel.setText("Fecha:");

        subtotalLabel.setText("Subtotal:");

        ivaLabel.setText("IVA:");

        totalLabel.setText("Total:");

        folioPagoLabel.setText("Folio de pago:");

        pacienteLabel.setText("Buscar paciente:");

        jScrollPane4.setViewportView(jListPacientes);

        jScrollPane3.setViewportView(JListPago);

        jLabel2.setText("Buscar Pago");

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloLabel)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(folioFacturaLabel)
                            .addComponent(fechaLabel)
                            .addComponent(folioPagoLabel))
                        .addGap(113, 113, 113)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(subtotalLabel)
                            .addComponent(ivaLabel)
                            .addComponent(totalLabel))))
                .addGap(92, 92, 92)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(buscadorListaPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pacienteLabel))
                .addGap(18, 18, 18)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscadorListaPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloLabel)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pacienteLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(folioFacturaLabel)
                            .addComponent(subtotalLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ivaLabel)
                            .addComponent(fechaLabel))
                        .addGap(19, 19, 19)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalLabel)
                            .addComponent(folioPagoLabel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buscadorListaPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscadorListaPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))))
        );

        scrollPaneFactura.setForeground(new java.awt.Color(204, 204, 204));

        razonSocialField.setText("Razón social");

        conceptoField.setText("Concepto");

        jLabel1.setText("Dirección fiscal:");

        cpField.setText("Codigo postal");

        calleField.setText("Calle");

        numeroInteriorField.setText("Número interior");

        numeroExteriorField.setText("Número exterior");

        localidadField.setText("Nombre de la localidad o municipio");

        entidadFederativaField.setText("Nombre de la entidad federativa");

        coloniaField.setText("Colonia");

        buttonGuardarFactura.setBackground(new java.awt.Color(51, 102, 255));
        buttonGuardarFactura.setForeground(new java.awt.Color(255, 255, 255));
        buttonGuardarFactura.setText("Guardar Datos");

        buttonCancelar.setText("Cancelar");

        rfcField.setText("RFC");

        buttonDescargarFactura.setBackground(new java.awt.Color(0, 153, 51));
        buttonDescargarFactura.setForeground(new java.awt.Color(255, 255, 255));
        buttonDescargarFactura.setText("Descargar Factura");
        buttonDescargarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDescargarFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(numeroInteriorField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numeroExteriorField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel1)
                        .addComponent(razonSocialField)
                        .addComponent(conceptoField)
                        .addComponent(calleField)
                        .addComponent(localidadField, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(entidadFederativaField, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(coloniaField)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(cpField, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(rfcField)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(buttonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(172, 172, 172))
                        .addComponent(buttonGuardarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonDescargarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(razonSocialField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(conceptoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(rfcField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(calleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numeroInteriorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numeroExteriorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(localidadField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(entidadFederativaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(coloniaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonGuardarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonDescargarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollPaneFactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneFactura)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonDescargarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDescargarFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonDescargarFacturaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JListPago;
    private miscomponentes.JBuscadorLista buscadorListaPacientes;
    private miscomponentes.JBuscadorLista buscadorListaPagos;
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonDescargarFactura;
    private javax.swing.JButton buttonGuardarFactura;
    private javax.swing.JTextField calleField;
    private javax.swing.JTextField coloniaField;
    private javax.swing.JTextField conceptoField;
    private javax.swing.JTextField cpField;
    private javax.swing.JTextField entidadFederativaField;
    private javax.swing.JLabel fechaLabel;
    private javax.swing.JLabel folioFacturaLabel;
    private javax.swing.JLabel folioPagoLabel;
    private javax.swing.JLabel ivaLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jListPacientes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField localidadField;
    private javax.swing.JTextField numeroExteriorField;
    private javax.swing.JTextField numeroInteriorField;
    private javax.swing.JLabel pacienteLabel;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JTextField razonSocialField;
    private javax.swing.JTextField rfcField;
    private javax.swing.JScrollPane scrollPaneFactura;
    private javax.swing.JLabel subtotalLabel;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
