
package ec.edu.espe.gpr.model.dashboard;

import ec.edu.espe.gpr.model.Proyecto;
import lombok.Data;

import java.util.List;

@Data
public class DashboardProyectoInvestigacion {
    private String name;
    private Double value;
    private Integer valueTotal;
    private Proyecto proyecto;
    private List<DashboardTareaInvestigacion> dasboardTareaInvestigacionList;
}