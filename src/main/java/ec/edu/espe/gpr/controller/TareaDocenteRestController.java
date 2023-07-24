package ec.edu.espe.gpr.controller;

import java.util.List;

import ec.edu.espe.gpr.model.*;
import ec.edu.espe.gpr.model.dashboard.DashboardProyectoInvestigacion;
import ec.edu.espe.gpr.model.file.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ec.edu.espe.gpr.services.TareaDocenteService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "https://yellow-river-0ca1d1510.3.azurestaticapps.net", "http://localhost:4200" })
@RestController
@RequestMapping(path = "/tareaDocente")
@RequiredArgsConstructor
public class TareaDocenteRestController {

    @Autowired
    private TareaDocenteService tareaDocenteService;

    @GetMapping(path = "/listarTareasDocentePorCodigoTarea/{codigoTarea}")
    public ResponseEntity<List<TareaDocente>> listarTareasDocentePorCodigoTarea(@PathVariable Integer codigoTarea) {
        try {
            List<TareaDocente> tareas = this.tareaDocenteService.listarTareasDocentePorCodigoTarea(codigoTarea);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/obtenerArchivoTarea/{idTarea}")
    public ResponseEntity<FileModel> obtenerUrlArchivo(@PathVariable Integer idTarea) {
        try {

            FileModel fileModel = this.tareaDocenteService.obtenerUrlArchivo(idTarea);
            return ResponseEntity.ok(fileModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/obtenerArchivoTareaDocente/{codigoTareaDocente}")
    public ResponseEntity<FileModel> obtenerArchivoTareaDocente(@PathVariable Integer codigoTareaDocente) {
        try {

            FileModel fileModel = this.tareaDocenteService.obtenerArchivoTareaDocente(codigoTareaDocente);
            return ResponseEntity.ok(fileModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping(path = "/listarTareas/{idDocente}")
    public ResponseEntity<List<TareaDocenteProyecto>> listarTareas(@PathVariable String idDocente) {
        try {
            List<TareaDocenteProyecto> tareas = this.tareaDocenteService.listarTareasDocentes(idDocente);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTareasPorProyecto/{idDocente}/{idProyecto}")
    public ResponseEntity<List<TareaDocenteProyecto>> listarTareas(@PathVariable String idDocente,@PathVariable Integer idProyecto) {
        try {
            List<TareaDocenteProyecto> tareas = this.tareaDocenteService.listarTareasDocentesPorProyecto(idDocente,idProyecto);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTodasTareasPorProyecto/{idProyecto}")
    public ResponseEntity<List<TareaDocenteProyecto>> listarTodasTareasPorProyecto(@PathVariable Integer idProyecto) {
        try {
            List<TareaDocenteProyecto> tareas = this.tareaDocenteService.listarTodasTareasPorProyecto(idProyecto);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarDocentes")
    public ResponseEntity<List<Docente>> listarDocentes() {
        try {
            List<Docente> docentes = this.tareaDocenteService.listarDocentes();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/obtenerDatosProyectoDashboardInvestigacion/{idTipoProceso}")
    public ResponseEntity<List<DashboardProyectoInvestigacion>> obtenerDatosProyectoDashboardInvestigacion(@PathVariable Integer idTipoProceso) {
        try {
            List<DashboardProyectoInvestigacion> docentes = this.tareaDocenteService.obtenerDatosProyectoDashboardInvestigacion(idTipoProceso);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarDocentesTareasAsignadas")
    public ResponseEntity<List<Docente>> listarDocentesTareasAsignadas() {
        try {
            List<Docente> docentes = this.tareaDocenteService.listarDocentesTareasAsignadas();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarDocentesPorCargo/{codigoCargo}/{codigoDocente}")
    public ResponseEntity<List<Docente>> listarDocentesPorCargo(@PathVariable String codigoCargo,@PathVariable Integer codigoDocente) {
        try {
            List<Docente> docentes = this.tareaDocenteService.obtenerDocentesPorCargo(codigoCargo,codigoDocente);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTodosDocentesPorCargo/{codigoCargo}")
    public ResponseEntity<List<Docente>> listarTodosDocentesPorCargo(@PathVariable String codigoCargo) {
        try {
            List<Docente> docentes = this.tareaDocenteService.obtenerTodosDocentesPorCargo(codigoCargo);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTodosDocentesPorNombreCargo/{nombreCargo}")
    public ResponseEntity<List<Docente>> listarTodosDocentesPorNombreCargo(@PathVariable String nombreCargo) {
        try {
            List<Docente> docentes = this.tareaDocenteService.listarTodosDocentesPorNombreCargo(nombreCargo);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarIndicadores")
    public ResponseEntity<List<Indicador>> listarIndicadores() {
        try {
            List<Indicador> indicadores = this.tareaDocenteService.listarIndicadores();
            return ResponseEntity.ok(indicadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarDocentesTareaAsignada")
    public ResponseEntity<List<Docente>> listarDocentesTareaAsignada(@RequestBody Tarea codigoTarea) {
        try {
            List<Docente> docentes = this.tareaDocenteService.listarDocentesTareaAsignada(codigoTarea);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTareaAsignadaPorDocente/{codigoDocente}")
    public ResponseEntity<List<TareaDocente>> listarTareaAsignadaPorDocente(@PathVariable Integer codigoDocente) {
        try {
            List<TareaDocente> tareaDocente = this.tareaDocenteService.listarTareaAsignadaPorDocente(codigoDocente);
            return ResponseEntity.ok(tareaDocente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarIndicadoresPorTarea/{codigoTareaDocente}")
    public ResponseEntity<List<TareaIndicador>> listarIndicadoresPorTarea(@PathVariable Integer codigoTareaDocente) {
        try {
            List<TareaIndicador> tareaIndicador = this.tareaDocenteService
                    .listarIndicadoresPorTarea(codigoTareaDocente);
            return ResponseEntity.ok(tareaIndicador);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTareasEntregadas/{idDocente}")
    public ResponseEntity<List<TareaDocente>> listarTareasEntregadas(@PathVariable String idDocente) {
        try {
            List<TareaDocente> tareaDocentes = this.tareaDocenteService.listarTareasEntregadas(idDocente);
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTareasAceptadas")
    public ResponseEntity<List<TareaDocente>> listarTareasAceptadas() {
        try {
            List<TareaDocente> tareaDocentes = this.tareaDocenteService.listarTareasAceptadas();
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarDocentesTareasAsignadas/{codigoDocente}")
    public ResponseEntity<List<TareaDocente>> listarDocentesTareasAsignadas(@PathVariable Integer codigoDocente) {
        try {
            List<TareaDocente> tareaDocentes = this.tareaDocenteService.listarDocentesTareasAsignadas(codigoDocente);
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarCargoDocentePorCodDocente/{codigoDocente}")
    public ResponseEntity<List<CargoDocente>> listarCargoDocentePorCodDocente(@PathVariable Integer codigoDocente) {
        try {
            List<CargoDocente> tareaDocentes = this.tareaDocenteService.listarCargoDocentePorCodDocente(codigoDocente);
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTodasTareasRevisar")
    public ResponseEntity<List<TareaDocente>> listarTodasTareasRevisar() {
        try {
            List<TareaDocente> tareaDocentes = this.tareaDocenteService.listarTodasTareasRevisar();
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/listarTodasTareasRevisadas")
    public ResponseEntity<List<TareasRealizadas>> listarTodasTareasRevisadas() {
        try {
            List<TareasRealizadas> tareaDocentes = this.tareaDocenteService.listarTodasTareasRevisadas();
            return ResponseEntity.ok(tareaDocentes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/crearTareaConArchivo")
    public ResponseEntity<Tarea> crear(@RequestParam("tareaDocenteProyecto") String strTareaDocenteProyecto,
            @RequestParam("file") MultipartFile file ) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
            TareaDocenteProyecto tareaDocenteProyecto = gson.fromJson(strTareaDocenteProyecto, TareaDocenteProyecto.class);
            Tarea tarea = this.tareaDocenteService.crear(tareaDocenteProyecto,file);
            return ResponseEntity.ok(tarea);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/crearTarea")
    public ResponseEntity<String> crearTarea(@RequestBody TareaDocenteProyecto  tareaDocenteProyecto) {
        try {
            MultipartFile file = null;
            this.tareaDocenteService.crear(tareaDocenteProyecto,file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/crearTareaConArchivoProgramada")
    public ResponseEntity<List<Tarea>> crearTareaConArchivoProgramada(@RequestParam("tareaDocenteProyecto") String strTareaDocenteProyecto,
            @RequestParam("file") MultipartFile file ) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
            TareaDocenteProyecto tareaDocenteProyecto = gson.fromJson(strTareaDocenteProyecto, TareaDocenteProyecto.class);
            List<Tarea> tareas = this.tareaDocenteService.crearTareaProgramada(tareaDocenteProyecto,file);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/crearTareaProgramada")
    public ResponseEntity<String> crearTareaProgramada(@RequestBody TareaDocenteProyecto  tareaDocenteProyecto) {
        try {
            MultipartFile file = null;
            this.tareaDocenteService.crearTareaProgramada(tareaDocenteProyecto,file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/crearTareasFromProyecto/{idProyectoCopiar}")
    public ResponseEntity<String> crearTareasFromProyecto(@PathVariable Integer idProyectoCopiar,@RequestBody Proyecto proyecto) {
        try {
            this.tareaDocenteService.crearTareasFromProyecto(proyecto,idProyectoCopiar);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<TareaDocente> modificar(@RequestBody TareaDocenteProyecto tareaDocenteProyecto) {
        try {
            TareaDocente tareaDocente = this.tareaDocenteService.modificarDatos(tareaDocenteProyecto,null);
            return ResponseEntity.ok(tareaDocente);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/editarTareaConArchivo")
    public ResponseEntity<String> editarTareaConArchivo(@RequestParam("tareaDocenteProyecto") String strTareaDocenteProyecto,
            @RequestParam("file") MultipartFile file ) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
            TareaDocenteProyecto tareaDocenteProyecto = gson.fromJson(strTareaDocenteProyecto, TareaDocenteProyecto.class);
            this.tareaDocenteService.crear(tareaDocenteProyecto,file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/guardarTareaAsignadaAlProfesor")
    public ResponseEntity<String> guardarTareaAsignadaAlProfesor(
        @RequestParam("tareaIndicadors") String strTareaIndicadors, @RequestParam("codigoTareaDocente") String codigoTareaDocente) {
        try {
            MultipartFile file = null;
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
            List<TareaIndicador> tareaIndicadors = gson.fromJson(strTareaIndicadors, new TypeToken<List<TareaIndicador>>(){}.getType());
            this.tareaDocenteService.guardarTareaAsignadaAlProfesor(tareaIndicadors,file,Integer.parseInt(codigoTareaDocente));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/guardarArchivoTareaAsignadaAlProfesor")
    public ResponseEntity<TareaDocente> guardarArchivoTareaAsignadaAlProfesor(@RequestParam("file") MultipartFile file,
            @RequestParam("tareaIndicadors") String strTareaIndicadors, @RequestParam("codigoTareaDocente") String codigoTareaDocente) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
            //List<TareaIndicador> tareaIndicadors = gson.fromJson(strTareaIndicadors, TareaIndicador[].class);
            List<TareaIndicador> tareaIndicadors = gson.fromJson(strTareaIndicadors, new TypeToken<List<TareaIndicador>>(){}.getType());
            TareaDocente tareaDocente = this.tareaDocenteService.guardarTareaAsignadaAlProfesor(tareaIndicadors,file,Integer.parseInt(codigoTareaDocente));
            return ResponseEntity.ok(tareaDocente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/aprobarTareaDocente")
    public ResponseEntity<String> aprobarTareaDocente(@RequestBody TareaDocente tareaDocente) {
        try {
            this.tareaDocenteService.aprobarTareaDocente(tareaDocente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/denegarTareaDocente")
    public ResponseEntity<String> denegarTareaDocente(@RequestBody TareaDocente tareaDocente) {
        try {
            this.tareaDocenteService.denegarTareaDocente(tareaDocente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/eliminarTarea/{codigoTarea}")
    public ResponseEntity<String> eliminarTarea(@PathVariable Integer codigoTarea) {
        try {
            this.tareaDocenteService.eliminarTarea(codigoTarea);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage() );
            return ResponseEntity.badRequest().build();
        }
    }

}
