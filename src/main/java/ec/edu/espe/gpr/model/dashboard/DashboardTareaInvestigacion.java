package ec.edu.espe.gpr.model.dashboard;

import java.util.List;

import ec.edu.espe.gpr.dto.TareaDocenteDto;
import ec.edu.espe.gpr.model.Tarea;
import ec.edu.espe.gpr.model.TareaDocente;
import lombok.Data;

@Data
public class DashboardTareaInvestigacion {
    private String name;
    private Double value;
    private Integer valueTotal;
    private Tarea tarea;
    private List<Series> series;
    private List<TareaDocenteDto> tareaDocentes;
}
