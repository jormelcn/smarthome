package sbt.smarthome.basequery;

import java.util.Map;

/**
 * Created by godi22 on 29/01/17.
 */

public class BaseUsers {

    public String nombre;
    public Boolean admin;
    public String home_admin;
    public Map<String, Boolean> casas;


    public BaseUsers() {

    }

    public BaseUsers(String nombre, Boolean admin, String home_admin, Map<String, Boolean> casas) {
        this.nombre = nombre;
        this.admin = admin;
        this.home_admin = home_admin;
        this.casas = casas;
    }

    public String getNombre() {
        return nombre;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public String getHome_admin() {
        return home_admin;
    }

    public Map<String, Boolean> getCasas() {
        return casas;
    }
}
