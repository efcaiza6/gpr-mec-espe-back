package ec.edu.espe.gpr.dao;

import ec.edu.espe.gpr.model.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPeriodoDao extends JpaRepository<Periodo, Integer> {
    List<Periodo> findByEstadoPeriodo(String estadoProyecto);
}