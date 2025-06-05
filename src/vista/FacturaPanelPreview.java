/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author carlo
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class FacturaPanelPreview extends JPanel implements Printable {
    private String razonSocial;
    private String direccion;
    private String concepto;
    private String folioFactura;
    private String fecha;
    private String folioPago;
    private String subtotal;
    private String iva;
    private String total;
    private String rfc;
    
    public void setDatosFactura(String razonSocial, String rfc, String direccion, String concepto, 
                           String folioFactura, String fecha, String folioPago,
                           String subtotal, String iva, String total) {
        this.razonSocial = razonSocial;
        this.rfc = rfc;
        this.direccion = direccion;
        this.concepto = concepto;
        this.folioFactura = folioFactura;
        this.fecha = fecha;
        this.folioPago = folioPago;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        repaint(); // Actualizar la visualización
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tamaño carta (612x792 puntos a 72 DPI)
        int width = 612;
        int height = 792;
        setSize(width, height);
        setPreferredSize(getSize());

        // Fondo blanco
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // Bordes
        g2d.setColor(Color.BLACK);
        g2d.drawRect(30, 30, width - 60, height - 60);
        
        // Cabecera
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("FACTURA", width/2 - 40, 70);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Razón Social:", 50, 100);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(razonSocial != null ? razonSocial : "", 150, 100);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("RFC:", 50, 120);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(rfc != null ? rfc : "", 150, 120);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Dirección:", 50, 140);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        
        String[] direccionPartes = splitStringByLength(direccion != null ? direccion : "", 60);
        int yPos1 = 125;
        for (String parte : direccionPartes) {
        yPos1 += 15;
        g2d.drawString(parte, 150, yPos1);
        }
        
        // Detalles de factura 
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Detalles de Factura", 50, 170); 
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Folio Factura: " + (folioFactura != null ? folioFactura : ""), 50, 190);
        g2d.drawString("Fecha: " + (fecha != null ? fecha : ""), 50, 210);
        g2d.drawString("Folio Pago: " + (folioPago != null ? folioPago : ""), 50, 230);
        
        // Concepto
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Concepto:", 50, 260);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(concepto != null ? concepto : "", 150, 260);
        
        // Totales
        int yPos = 300;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Subtotal:", 400, yPos);
        g2d.drawString("IVA:", 400, yPos + 30);
        g2d.drawString("TOTAL:", 400, yPos + 60);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(subtotal != null ? subtotal : "", 480, yPos);
        g2d.drawString(iva != null ? iva : "", 480, yPos + 30);
        g2d.drawString(total != null ? total : "", 480, yPos + 60);
        
        // Pie de página
        g2d.setFont(new Font("Arial", Font.ITALIC, 10));
        g2d.drawString("Este documento es una representación electrónica de una factura", 50, height - 40);
        g2d.dispose();
    }

    private String[] splitStringByLength(String text, int maxLength) {
        List<String> parts = new ArrayList<>();
        int length = text.length();

        for (int i = 0; i < length; i += maxLength) {
            parts.add(text.substring(i, Math.min(length, i + maxLength)));
        }

        return parts.toArray(new String[0]);
        
    }    
        
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        paint(g2d);
        
        return PAGE_EXISTS;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(612, 792); // Tamaño carta estándar
    }
}
