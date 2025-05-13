/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConexion {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        try {
            // "EstarBien" debe coincidir con el nombre de tu persistence-unit en persistence.xml
            emf = Persistence.createEntityManagerFactory("EstarBien");
            System.out.println("¡Conexión a la base de datos exitosa!");
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        } finally {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}
