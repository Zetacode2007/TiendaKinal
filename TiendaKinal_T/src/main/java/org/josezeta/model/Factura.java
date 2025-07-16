package org.josezeta.model;

import java.time.LocalDate;

/**
 *
 * @author RV
 */
public class Factura {
    private int codigoFactura;
    private LocalDate fechaEmision;
    private double total;
    private String metodoPago;
    private int codigoCliente;
    private int codigoEmpleado;

    public Factura(int codigoFactura, LocalDate fechaEmision, double total, String metodoPago, int codigoCliente, int codigoEmpleado) {
        this.codigoFactura = codigoFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.metodoPago = metodoPago;
        this.codigoCliente = codigoCliente;
        this.codigoEmpleado = codigoEmpleado;
    }

    public Factura() {
    }

    public int getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    @Override
    public String toString() {
        return "Factura{" + "codigoFactura=" + codigoFactura + ", fechaEmision=" + fechaEmision + ", total=" + total + ", metodoPago=" + metodoPago + ", codigoCliente=" + codigoCliente + ", codigoEmpleado=" + codigoEmpleado + '}';
    }

    
}
