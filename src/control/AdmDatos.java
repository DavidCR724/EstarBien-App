/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carlo
 */
public class AdmDatos {
    protected static EntityManagerFactory emf;
    public EntityManagerFactory getEmf(){
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("EstarBienPU");
            return emf;
    }
}
