package ec.edu.espe.gpr.model;

import java.util.List;

public class TareaDocenteProyecto {
    private Tarea tarea;
    private List<Docente> docentes;
    private List<Indicador> indicadors;
    private String claseCirculoPintar;
        
    public TareaDocenteProyecto() {
    }

    public TareaDocenteProyecto(Tarea tarea, List<Docente> docentes, List<Indicador> indicadors) {
        this.tarea = tarea;
        this.docentes = docentes;
        this.indicadors = indicadors;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }

    public List<Indicador> getIndicadors() {
        return indicadors;
    }

    public void setIndicadors(List<Indicador> indicadors) {
        this.indicadors = indicadors;
    }

    public String getClaseCirculoPintar() {
        return claseCirculoPintar;
    }

    public void setClaseCirculoPintar(String claseCirculoPintar) {
        this.claseCirculoPintar = claseCirculoPintar;
    }
}
