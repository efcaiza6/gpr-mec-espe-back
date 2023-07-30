package ec.edu.espe.gpr.controller;

import ec.edu.espe.gpr.model.Periodo;
import ec.edu.espe.gpr.services.PeriodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"https://yellow-river-0ca1d1510.3.azurestaticapps.net","http://localhost:4200"})
@RestController
@RequestMapping(path = "/periodo")
@RequiredArgsConstructor
public class PeriodoRestController {
    @Autowired
    private PeriodoService periodoService;

    @GetMapping(path = "/listarTodosLosPeriodos")
    public ResponseEntity<List<Periodo>> listarTodosLosPeriodos() {
        try {
            List<Periodo> periodos = this.periodoService.listarTodosLosPeriodos();
            return ResponseEntity.ok(periodos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarPeriodosActivos")
    public ResponseEntity<List<Periodo>> listarPeriodosActivos() {
        try {
            List<Periodo> periodos = this.periodoService.listarPeriodosActivos();
            return ResponseEntity.ok(periodos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody Periodo periodo) {
        try {
            this.periodoService.crear(periodo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Periodo> modificar(@RequestBody Periodo periodo) {
        try {
            periodo = this.periodoService.modificarDatos(periodo);
            return ResponseEntity.ok(periodo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/cambiarEstado/{codigoPeriodo}")
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer codigoPeriodo) {
        try {
            this.periodoService.cambiarEstadoPeriodo(codigoPeriodo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
