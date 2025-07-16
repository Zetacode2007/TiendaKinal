package org.josezeta.model;


public class Cliente {
    private int codigoCliente;
    private String nombreCliente;
    private String correo;
    private String telefono;
    private String DPI;

    public Cliente(int codigoCliente, String nombreCliente, String correo, String telefono, String DPI) {
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.correo = correo;
        this.telefono = telefono;
        this.DPI = DPI;
    }

    public Cliente() {
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDPI() {
        return DPI;
    }

    public void setDPI(String DPI) {
        this.DPI = DPI;
    }

    @Override
    public String toString() {
        return "Cliente{" + "codigoCliente=" + codigoCliente + ", nombreCliente=" + nombreCliente + ", correo=" + correo + ", telefono=" + telefono + ", DPI=" + DPI + '}';
    }
    
    
}
