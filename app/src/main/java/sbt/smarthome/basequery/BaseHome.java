package sbt.smarthome.basequery;

import java.util.Map;

/**
 * Created by godi22 on 31/01/17.
 */

public class BaseHome {

    public String nombre;
    public Map<String, Boolean> usuarios;

    public BaseHome(){

    }

    public BaseHome(String nombre, Map<String, Boolean> usuarios) {
        this.nombre = nombre;
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public Map<String, Boolean> getUsuarios() {
        return usuarios;
    }
}


