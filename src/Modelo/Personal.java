/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author richard
 */
public class Personal {
    public String pk_personal;
    public String titulo;
    public String cargo;
    public String salario;

    public Personal(String pk_personal, String titulo, String cargo, String salario) {
        this.pk_personal = pk_personal;
        this.titulo = titulo;
        this.cargo = cargo;
        this.salario = salario;
    }

    public String getPk_personal() {
        return pk_personal;
    }

    public void setPk_personal(String pk_personal) {
        this.pk_personal = pk_personal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }
    
    
}
