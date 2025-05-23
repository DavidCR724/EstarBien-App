/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import modelo.Usuario;

public class SesionManager {
    private static SesionManager instance;
    private Usuario usuarioLogueado;

    private SesionManager() {}

    public static SesionManager getInstance() {
        if (instance == null) {
            instance = new SesionManager();
        }
        return instance;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean tienePermiso(String rolRequerido) {
        if (usuarioLogueado == null) return false;
        return usuarioLogueado.getRol().equalsIgnoreCase(rolRequerido);
    }

    public boolean esAdministrador() {
        return tienePermiso("administrador");
    }

    public boolean esMedico() {
        return tienePermiso("medico");
    }

    public boolean esPaciente() {
        return tienePermiso("paciente");
    }
}