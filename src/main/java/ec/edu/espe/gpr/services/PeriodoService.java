package ec.edu.espe.gpr.services;

import ec.edu.espe.gpr.dao.IPeriodoDao;
import ec.edu.espe.gpr.dao.IProyectoDao;
import ec.edu.espe.gpr.dao.ITipoProceso;
import ec.edu.espe.gpr.enums.EstadoPeriodoEnum;
import ec.edu.espe.gpr.enums.EstadoProyectoEnum;
import ec.edu.espe.gpr.model.Periodo;
import ec.edu.espe.gpr.model.Proyecto;
import ec.edu.espe.gpr.model.TipoProceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoService {

    @Autowired
	private IPeriodoDao periodoDao;
	
	public Periodo obtenerPorCodigoPeriodo(Integer codPeriodo) {
		Optional<Periodo> periodoOpt = this.periodoDao.findById(codPeriodo);
		if (periodoOpt.isPresent())
			return periodoOpt.get();
		else 
			return null;
	}

    public List<Periodo> listarTodosLosPeriodos() {
        return this.periodoDao.findAll();
    }

	public List<Periodo> listarPeriodosActivos() {
        return this.periodoDao.findByEstadoPeriodo(EstadoPeriodoEnum.ACTIVE.getValue());
    }
	
    public void crear(Periodo periodo) {
        periodo.setNombrePeriodo(periodo.getNombrePeriodo().toUpperCase());
        periodo.setDescripcionPeriodo(periodo.getDescripcionPeriodo().toUpperCase());
        periodo.setEstadoPeriodo(EstadoPeriodoEnum.ACTIVE.getValue());
        periodo.setFechaCreacionPeriodo(new Date());
        periodo.setFechaModificacionPeriodo(new Date());
        this.periodoDao.save(periodo);
    }

    public Periodo modificarDatos(Periodo periodo) {
        periodo.setNombrePeriodo(periodo.getNombrePeriodo().toUpperCase());
        periodo.setDescripcionPeriodo(periodo.getDescripcionPeriodo().toUpperCase());
        periodo.setFechaModificacionPeriodo(new Date());
        periodo= this.periodoDao.save(periodo);
        return periodo;
    }
	
    public Periodo cambiarEstadoPeriodo(Integer codPeriodo) {
        Periodo periodoDB = this.obtenerPorCodigoPeriodo(codPeriodo);
        if(periodoDB.getEstadoPeriodo().equals(EstadoPeriodoEnum.ACTIVE.getValue()))
            periodoDB.setEstadoPeriodo(EstadoPeriodoEnum.INACTIVE.getValue());
        else
            periodoDB.setEstadoPeriodo(EstadoPeriodoEnum.ACTIVE.getValue());
        periodoDB.setFechaModificacionPeriodo(new Date());
        periodoDB= this.periodoDao.save(periodoDB);
        return periodoDB;
    }
}
