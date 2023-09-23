/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculodebarras;

/**
 *
 * @author ItielSanz<ItielSanzAXO>
 */
public class Registro {
    private int noOrden;
    private String nomTecnico;
    private String seResolvio;
    private String hrInicio;
    private String hrFinal;
    private String escalonadoA;

    // Constructor
    public Registro(int noOrden, String nomTecnico, String seResolvio, String hrInicio, String hrFinal, String escalonadoA) {
        this.noOrden = noOrden;
        this.nomTecnico = nomTecnico;
        this.seResolvio = seResolvio;
        this.hrInicio = hrInicio;
        this.hrFinal = hrFinal;
        this.escalonadoA = escalonadoA;
    }

    // Getters y setters (métodos de acceso)

    public int getNoOrden() {
        return noOrden;
    }

    public void setNoOrden(int noOrden) {
        this.noOrden = noOrden;
    }

    public String getNomTecnico() {
        return nomTecnico;
    }

    public void setNomTecnico(String nomTecnico) {
        this.nomTecnico = nomTecnico;
    }

    public String getSeResolvio() {
        return seResolvio;
    }

    public void setSeResolvio(String seResolvio) {
        this.seResolvio = seResolvio;
    }

    public String getHrInicio() {
        return hrInicio;
    }

    public void setHrInicio(String hrInicio) {
        this.hrInicio = hrInicio;
    }

    public String getHrFinal() {
        return hrFinal;
    }

    public void setHrFinal(String hrFinal) {
        this.hrFinal = hrFinal;
    }

    public String getEscalonadoA() {
        return escalonadoA;
    }

    public void setEscalonadoA(String escalonadoA) {
        this.escalonadoA = escalonadoA;
    }
}
