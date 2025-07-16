package org.josezeta.model;

/**
 *
 * @author RV
 */
public class Empleado {

    private int codigoEmpleado;
    private String nombreEmpleado;
    private String puesto;
    private String correo;

    public Empleado(int codigoEmpleado, String nombreEmpleado, String puesto, String correo) {
        this.codigoEmpleado = codigoEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.puesto = puesto;
        this.correo = correo;
    }

    public Empleado() {
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Empleado{" + "codigoEmpleado=" + codigoEmpleado + ", nombreEmpleado=" + nombreEmpleado + ", puesto=" + puesto + ", correo=" + correo + '}';
    }

}
