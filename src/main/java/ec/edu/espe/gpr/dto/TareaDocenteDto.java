package ec.edu.espe.gpr.dto;

import ec.edu.espe.gpr.model.Docente;
import ec.edu.espe.gpr.model.Tarea;
import ec.edu.espe.gpr.model.TareaIndicador;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TareaDocenteDto {
    private Integer codigoTareaDocente;
    private String archivoTareaDocente;
    private String descripcionTareadocente;
    private String estadoTareaDocente;
    private String nombreArchivoTareaDocente;
    private Date fechaEntregadaTareaDocente;
    private String cedulaDocenteRevisor;
    private Docente codigoDocente;
    private Tarea codigoTarea;
    private List<TareaIndicador> tareaIndicadorList;
}
