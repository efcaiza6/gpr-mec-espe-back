package ec.edu.espe.gpr.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ec.edu.espe.gpr.dto.TareaDocenteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ec.edu.espe.gpr.config.BaseURLValues;
import ec.edu.espe.gpr.dao.CargoDocenteDao;
import ec.edu.espe.gpr.dao.ICargoDao;
import ec.edu.espe.gpr.dao.IDocenteDao;
import ec.edu.espe.gpr.dao.IProyectoDao;
import ec.edu.espe.gpr.dao.ITareaDao;
import ec.edu.espe.gpr.dao.ITareaDocenteDao;
import ec.edu.espe.gpr.dao.ITipoProceso;
import ec.edu.espe.gpr.dao.IndicadorDao;
import ec.edu.espe.gpr.dao.TareaIndicadorDao;
import ec.edu.espe.gpr.enums.EstadoProyectoEnum;
import ec.edu.espe.gpr.enums.EstadoTareaDocenteEnum;
import ec.edu.espe.gpr.enums.EstadoTareaEnum;
import ec.edu.espe.gpr.enums.ModulosEnum;
import ec.edu.espe.gpr.model.Cargo;
import ec.edu.espe.gpr.model.CargoDocente;
import ec.edu.espe.gpr.model.Docente;
import ec.edu.espe.gpr.model.Indicador;
import ec.edu.espe.gpr.model.Proyecto;
import ec.edu.espe.gpr.model.Tarea;
import ec.edu.espe.gpr.model.TareaDocente;
import ec.edu.espe.gpr.model.TareaDocenteProyecto;
import ec.edu.espe.gpr.model.TareaIndicador;
import ec.edu.espe.gpr.model.TareasRealizadas;
import ec.edu.espe.gpr.model.TipoProceso;
import ec.edu.espe.gpr.model.dashboard.DashboardProyectoInvestigacion;
import ec.edu.espe.gpr.model.dashboard.DashboardTareaInvestigacion;
import ec.edu.espe.gpr.model.dashboard.Series;
import ec.edu.espe.gpr.model.file.FileModel;
import ec.edu.espe.gpr.model.file.FileRequest;

@Service
public class TareaDocenteService {

    @Autowired
    private ITareaDao tareaDao;
    @Autowired
    private ITareaDocenteDao tareaDocenteDao;
    @Autowired
    private IDocenteDao docenteDao;
    @Autowired
    private IndicadorDao indicadorDao;
    @Autowired
    private TareaIndicadorDao tareaIndicadorDao;
    @Autowired
    private ICargoDao cargoDao;
    @Autowired
    private CargoDocenteDao cargoDocenteDao;
    @Autowired
    private IProyectoDao proyectoDao;
    @Autowired
    private ITipoProceso tipoProcesoDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseURLValues baseURLs;
    /*
     * @Autowired
     * private IPerfilDao perfilDao;
     * 
     * @Autowired
     * private IUsuarioPerfilDao usuarioperfilDao;
     */
    @Autowired
    private IEmailService emservice;

    // private final Path root = Paths.get("uploads");
    // private final Path rootFileGuia = Paths.get("archivo_guia");

    public Tarea obtenerTareaPorCodigoTarea(Integer codTarea) {
        Optional<Tarea> tareaOpt = this.tareaDao.findById(codTarea);
        if (tareaOpt.isPresent())
            return tareaOpt.get();
        else
            return null;
    }

    public Cargo obtenerCargoPorCodigoCargo(String codigoCargo) {
        Optional<Cargo> cargoOpt = this.cargoDao.findById(codigoCargo);
        if (cargoOpt.isPresent())
            return cargoOpt.get();
        else
            return null;
    }

    /*
     * private Docente obtenerDocentePorCodigoUsuario(Usuario usuario) {
     * Optional<Docente> docenteOpt = this.docenteDao.findByCodigoUsuario(usuario);
     * if (docenteOpt.isPresent())
     * return docenteOpt.get();
     * else
     * return null;
     * }
     * 
     * private List<Docente> obtenerDocentesPorPerfil(Perfil perfil){
     * List<Usuper> usupers= this.usuarioperfilDao.findByCodigoPerfil(perfil);
     * List<Docente> docentes = new ArrayList<>();
     * for (Usuper usuper : usupers) {
     * Docente docente =
     * this.obtenerDocentePorCodigoUsuario(usuper.getCodigoUsuario());
     * docentes.add(docente);
     * }
     * return docentes;
     * }
     * 
     * private Perfil obtenerPerfilPorCodigoPerfil(String codPerfil) {
     * Optional<Perfil> perfilOpt = this.perfilDao.findById(codPerfil);
     * if (perfilOpt.isPresent())
     * return perfilOpt.get();
     * else
     * return null;
     * }
     * 
     */
    /*
     * private Perfil obtenerPerfilPorCodigoPerfilPadre(Perfil codPerfil) {
     * Optional<Perfil> perfilOpt =
     * this.perfilDao.findByCodigoPerfilPadre(codPerfil);
     * if (perfilOpt.isPresent())
     * return perfilOpt.get();
     * else
     * return null;
     * }
     */

    public FileModel obtenerUrlArchivo(Integer idTarea){
        Tarea tarea = this.obtenerTareaPorCodigoTarea(idTarea);
        FileModel fileModel=null;
        if (tarea.getNombreArchivoTarea() != ""
                || tarea.getNombreArchivoTarea() != null) {
            ResponseEntity<FileModel> response = this.restTemplate.getForEntity(
                    baseURLs.getGprStorageURL() + "/getFileTarea/"
                            + ModulosEnum.INVESTIGACION.getValue()+ "/"
                            + tarea.getNombreArchivoTarea() + "/"
                            + tarea.getArchivoTarea(),
                    FileModel.class);
            fileModel = response.getBody();
        }
        return fileModel;
    }

    public FileModel obtenerArchivoTareaDocente(Integer idTareaDocente){
        TareaDocente tareaDocente = this.tareaDocenteDao.findById(idTareaDocente).get();
        FileModel fileModel=null;
        if (tareaDocente.getNombreArchivoTareaDocente() != ""
                || tareaDocente.getNombreArchivoTareaDocente() != null) {
            ResponseEntity<FileModel> response = this.restTemplate.getForEntity(
                    baseURLs.getGprStorageURL() + "/getFileDocenteTarea/"
                            + ModulosEnum.INVESTIGACION.getValue()+ "/"
                            + tareaDocente.getNombreArchivoTareaDocente() + "/"
                            + tareaDocente.getArchivoTareaDocente(),
                    FileModel.class);
            fileModel = response.getBody();
        }
        return fileModel;
    }

    public List<TareaDocente> listarTareasDocentePorCodigoTarea(Integer codigoTarea) {
        Tarea tarea = this.obtenerTareaPorCodigoTarea(codigoTarea);
        return this.tareaDocenteDao.findByCodigoTarea(tarea);
    }

    public List<Docente> obtenerDocentesPorCargo(String codigoCargo, Integer codigoDocente) {
        Cargo cargo = this.obtenerCargoPorCodigoCargo(codigoCargo);
        // Docente docente = this.obtenerDocentePorCodigoDocente(codigoDocente);
        List<CargoDocente> cargoDocentes = this.cargoDocenteDao.findByCodCargo(cargo);
        // Cargo cargo = obtenerCargoPorCodigoCargo(codigoCargo);
        /*
         * Perfil perfil =
         * obtenerPerfilPorCodigoPerfilPadre(obtenerPerfilPorCodigoPerfil(codigoPerfil))
         * ;
         * List<Docente> docentes = this.obtenerDocentesPorPerfil(perfil);
         * List<Docente> docentesPerfil = new ArrayList<>();
         */
        List<Docente> docentes = new ArrayList<>();
        for (CargoDocente cargoDocente : cargoDocentes) {
            if (cargoDocente.getCodigoDocente().getCodigoDocente() != codigoDocente)
                docentes.add(cargoDocente.getCodigoDocente());
        }
        return docentes.stream().sorted(Comparator.comparing(Docente::getApellidoDocente)).collect(Collectors.toList());// this.docenteDao.findByCodCargo(cargo);
        /*
         * for (Docente docente : docentes) {
         * if(docente.getCodCargo().equals(cargo))
         * docentesPerfil.add(docente);
         * }
         */
        /*
         * Docente docente = docenteDao.findByNombreDocente("Admin");
         * int indice = docentes.indexOf(docente);
         * if(indice != -1)
         * docentes.remove(indice);
         */

        // return docentesPerfil;
    }

    public List<Docente> obtenerTodosDocentesPorCargo(String codigoCargo) {
        Cargo cargo = this.obtenerCargoPorCodigoCargo(codigoCargo);
        List<CargoDocente> cargoDocentes = this.cargoDocenteDao.findByCodCargo(cargo);
        List<Docente> docentes = new ArrayList<>();
        for (CargoDocente cargoDocente : cargoDocentes) {
            docentes.add(cargoDocente.getCodigoDocente());
        }
        return docentes.stream().sorted(Comparator.comparing(Docente::getApellidoDocente)).collect(Collectors.toList());// this.docenteDao.findByCodCargo(cargo);
    }

    public Docente obtenerDocentePorCodigoDocente(Integer codigoDocente) {
        Optional<Docente> docenteOpt = this.docenteDao.findByCodigoDocente(codigoDocente);
        if (docenteOpt.isPresent())
            return docenteOpt.get();
        else
            return null;
    }

    public TareaDocente obtenerIndicadorPorCodigoTareaDocente(Integer codigoTareaDocente) {
        Optional<TareaDocente> tareaDocenteOpt = this.tareaDocenteDao.findById(codigoTareaDocente);
        if (tareaDocenteOpt.isPresent())
            return tareaDocenteOpt.get();
        else
            return null;
    }

    public List<TareaDocenteProyecto> listarTareasDocentes(String idDocente) {
        List<TareaDocenteProyecto> tListDocenteProyecto = new ArrayList<>();
        List<Tarea> tarea = this.tareaDao.findByIdDocenteRevisor(idDocente);
        for (Tarea t : tarea) {
            TareaDocenteProyecto tDocenteProyecto = new TareaDocenteProyecto();
            tDocenteProyecto.setTarea(t);
            Boolean check = true;
            List<Docente> docentes = new ArrayList<>();
            for (TareaDocente tDocente : t.getTareaDocenteList()) {
                if (check) {
                    List<Indicador> tareaIndicadors = new ArrayList<>();

                    for (TareaIndicador tareaIndicador : tDocente.getTareaIndicadorList()) {
                        Indicador indicador = new Indicador(
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getCodigoIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getNombreIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getEstadoIndicador(),
                                tareaIndicador.getDescripcionTareaIndicador());
                        tareaIndicadors.add(indicador);
                    }
                    tDocenteProyecto.setIndicadors(tareaIndicadors);
                    check = false;
                }
                docentes.add(tDocente.getCodigoDocente());
            }
            tDocenteProyecto.setDocentes(docentes);
            tListDocenteProyecto.add(tDocenteProyecto);
        }
        return tListDocenteProyecto;
    }

    public List<TareaDocenteProyecto> listarTareasDocentesPorProyecto(String idDocente, Integer idProyecto) {
        List<TareaDocenteProyecto> tListDocenteProyecto = new ArrayList<>();
        Proyecto proyecto = this.proyectoDao.findById(idProyecto).get();
        List<Tarea> tarea = this.tareaDao.findByIdDocenteRevisorAndCodigoProyecto(idDocente, proyecto);
        for (Tarea t : tarea) {
            TareaDocenteProyecto tDocenteProyecto = new TareaDocenteProyecto();
            tDocenteProyecto.setTarea(t);
            Boolean check = true;
            List<Docente> docentes = new ArrayList<>();
            Integer count = 0;
            Boolean checkEstadotarea = true;
            for (TareaDocente tDocente : t.getTareaDocenteList()) {
                if (checkEstadotarea) {
                    if (tDocente.getEstadoTareaDocente().equals("ACEPTADO")) {
                        count++;
                    }
                }
                if (tDocente.getEstadoTareaDocente().equals("EN REVISIÓN")
                        || tDocente.getEstadoTareaDocente().equals("DENEGADO")
                        || tDocente.getEstadoTareaDocente().equals("ASIGNADA")) {
                    checkEstadotarea = false;
                }
                if (check) {
                    List<Indicador> tareaIndicadors = new ArrayList<>();

                    for (TareaIndicador tareaIndicador : tDocente.getTareaIndicadorList()) {
                        Indicador indicador = new Indicador(
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getCodigoIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getNombreIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getEstadoIndicador(),
                                tareaIndicador.getDescripcionTareaIndicador());
                        tareaIndicadors.add(indicador);
                    }
                    tDocenteProyecto.setIndicadors(tareaIndicadors);
                    check = false;
                }
                docentes.add(tDocente.getCodigoDocente());
            }
            if (checkEstadotarea == false)
                tDocenteProyecto.setClaseCirculoPintar("amarillo");
            else if (count == 0) {
                tDocenteProyecto.setClaseCirculoPintar("rojo");
            } else
                tDocenteProyecto.setClaseCirculoPintar("verde");
            tDocenteProyecto.setDocentes(docentes);
            tListDocenteProyecto.add(tDocenteProyecto);
        }
        return tListDocenteProyecto;
    }

    public List<TareaDocenteProyecto> listarTodasTareasPorProyecto(Integer idProyecto) {
        List<TareaDocenteProyecto> tListDocenteProyecto = new ArrayList<>();
        Proyecto proyecto = this.proyectoDao.findById(idProyecto).get();
        List<Tarea> tarea = this.tareaDao.findByCodigoProyecto(proyecto);
        for (Tarea t : tarea) {
            TareaDocenteProyecto tDocenteProyecto = new TareaDocenteProyecto();
            tDocenteProyecto.setTarea(t);
            Boolean check = true;
            List<Docente> docentes = new ArrayList<>();
            Integer count = 0;
            Boolean checkEstadotarea = true;
            for (TareaDocente tDocente : t.getTareaDocenteList()) {
                if (checkEstadotarea) {
                    if (tDocente.getEstadoTareaDocente().equals("ACEPTADO")) {
                        count++;
                    }
                }
                if (tDocente.getEstadoTareaDocente().equals("EN REVISIÓN")
                        || tDocente.getEstadoTareaDocente().equals("DENEGADO")
                        || tDocente.getEstadoTareaDocente().equals("ASIGNADA")) {
                    checkEstadotarea = false;
                }
                if (check) {
                    List<Indicador> tareaIndicadors = new ArrayList<>();

                    for (TareaIndicador tareaIndicador : tDocente.getTareaIndicadorList()) {
                        Indicador indicador = new Indicador(
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getCodigoIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getNombreIndicador(),
                                tareaIndicador.getIndicadorCODIGOINDICADOR().getEstadoIndicador(),
                                tareaIndicador.getDescripcionTareaIndicador());
                        tareaIndicadors.add(indicador);
                    }
                    tDocenteProyecto.setIndicadors(tareaIndicadors);
                    check = false;
                }
                docentes.add(tDocente.getCodigoDocente());
            }
            if (checkEstadotarea == false)
                tDocenteProyecto.setClaseCirculoPintar("amarillo");
            else if (count == 0) {
                tDocenteProyecto.setClaseCirculoPintar("rojo");
            } else
                tDocenteProyecto.setClaseCirculoPintar("verde");
            tDocenteProyecto.setDocentes(docentes);
            tListDocenteProyecto.add(tDocenteProyecto);
        }
        return tListDocenteProyecto;
    }

    public List<DashboardProyectoInvestigacion> obtenerDatosProyectoDashboardInvestigacion(Integer idProceso) {
        TipoProceso tipoProceso = this.tipoProcesoDao.findById(idProceso).get();
        List<Proyecto> proyectos = this.proyectoDao.findByTipoProceso(tipoProceso);
        List<DashboardProyectoInvestigacion> listDataDashboard = new ArrayList<>();
        Double contProyecto;
        Double contadorTotalProyecto;
        Double contTotaltareas;
        Double contTareas;

        List<DashboardTareaInvestigacion> dashboardTareaInvestigacionList = null;
        ;
        for (Proyecto proyecto : proyectos) {
            contadorTotalProyecto = 0.0;
            contProyecto = 0.0;
            DashboardProyectoInvestigacion dashboardProyectoInvestigacion = new DashboardProyectoInvestigacion();
            dashboardProyectoInvestigacion.setProyecto(proyecto);
            dashboardTareaInvestigacionList = new ArrayList<>();
            Integer contTareasPorProyecto = 0;
            for (Tarea tarea : proyecto.getTareaList()) {

                contTotaltareas = 0.0;
                contTareas = 0.0;
                contTareasPorProyecto++;

                Integer contTareaAsignada = 0;
                Integer contTareaRevision = 0;
                Integer contTareaDenegado = 0;

                DashboardTareaInvestigacion dashboardTareaInvestigacion = new DashboardTareaInvestigacion();
                List<TareaDocente> tareaDocentes = new ArrayList<>();
                for (TareaDocente tareaDocente : tarea.getTareaDocenteList()) {
                    if (tareaDocente.getEstadoTareaDocente().equals("ACEPTADO")) {
                        contProyecto++;
                        contTareas++;
                    }
                    if (tareaDocente.getEstadoTareaDocente().equals("ASIGNADA"))
                        contTareaAsignada++;
                    if (tareaDocente.getEstadoTareaDocente().equals("EN REVISIÓN"))
                        contTareaRevision++;
                    if (tareaDocente.getEstadoTareaDocente().equals("DENEGADO"))
                        contTareaDenegado++;

                    contadorTotalProyecto++;
                    contTotaltareas++;
                    tareaDocentes.add(tareaDocente);
                }
                List<TareaDocenteDto> tareaDocenteDtos = new ArrayList<>();
                for (TareaDocente tareaDocente:tareaDocentes) {
                    TareaDocenteDto tareaDocenteDto = new TareaDocenteDto();
                    tareaDocenteDto.setCodigoTareaDocente(tareaDocente.getCodigoTareaDocente());
                    tareaDocenteDto.setArchivoTareaDocente(tareaDocente.getArchivoTareaDocente());
                    tareaDocenteDto.setDescripcionTareadocente(tareaDocente.getDescripcionTareadocente());
                    tareaDocenteDto.setEstadoTareaDocente(tareaDocente.getEstadoTareaDocente());
                    tareaDocenteDto.setNombreArchivoTareaDocente(tareaDocente.getNombreArchivoTareaDocente());
                    tareaDocenteDto.setFechaEntregadaTareaDocente(tareaDocente.getFechaEntregadaTareaDocente());
                    tareaDocenteDto.setCedulaDocenteRevisor(tareaDocente.getCedulaDocenteRevisor());
                    tareaDocenteDto.setCodigoDocente(tareaDocente.getCodigoDocente());
                    tareaDocenteDto.setCodigoTarea(tareaDocente.getCodigoTarea());
                    tareaDocenteDto.setTareaIndicadorList(tareaDocente.getTareaIndicadorList());
                    tareaDocenteDtos.add(tareaDocenteDto);
                }
                dashboardTareaInvestigacion.setTareaDocentes(tareaDocenteDtos);
                List<Series> series = new ArrayList<>();

                Series seriesModel = new Series();
                seriesModel.setName("ASIGNADA");
                seriesModel.setValue(contTareaAsignada);

                series.add(seriesModel);

                seriesModel = new Series();
                seriesModel.setName("EN REVISIÓN\"");
                seriesModel.setValue(contTareaRevision);

                series.add(seriesModel);

                seriesModel = new Series();
                seriesModel.setName("DENEGADO");
                seriesModel.setValue(contTareaDenegado);

                series.add(seriesModel);

                seriesModel = new Series();
                seriesModel.setName("ACEPTADO");
                seriesModel.setValue(contTareas.intValue());

                series.add(seriesModel);

                dashboardTareaInvestigacion.setName(contTareasPorProyecto + "." + tarea.getNombreTarea());
                dashboardTareaInvestigacion.setValue((contTareas / contTotaltareas) * 100);
                dashboardTareaInvestigacion.setValueTotal(contTotaltareas.intValue());
                dashboardTareaInvestigacion.setTarea(tarea);
                dashboardTareaInvestigacion.setSeries(series);
                dashboardTareaInvestigacionList.add(dashboardTareaInvestigacion);
            }
            dashboardProyectoInvestigacion.setName(proyecto.getNombreProyecto());
            dashboardProyectoInvestigacion.setValue((contProyecto / contadorTotalProyecto) * 100);
            dashboardProyectoInvestigacion.setValueTotal(contadorTotalProyecto.intValue());
            dashboardProyectoInvestigacion.setDasboardTareaInvestigacionList(dashboardTareaInvestigacionList);
            listDataDashboard.add(dashboardProyectoInvestigacion);
        }
        return listDataDashboard;
    }

    public List<Docente> listarDocentes() {
        List<Docente> docentes = this.docenteDao.findAll();
        Docente docente = docenteDao.findByNombreDocente("Admin");
        int indice = docentes.indexOf(docente);
        if (indice != -1)
            docentes.remove(indice);
        return docentes;
    }

    public List<Docente> listarDocentesTareasAsignadas() {
        List<Docente> docentes = this.docenteDao.findAll();
        List<Docente> docentesAsignados = new ArrayList<>();

        for (Docente docente : docentes) {
            for (TareaDocente tareaDocente : docente.getTareaDocenteList()) {
                if (!tareaDocente.getEstadoTareaDocente().equals(EstadoTareaDocenteEnum.ACEPTADO.getValue())) {
                    docentesAsignados.add(docente);
                    break;
                }
            }
        }
        return docentesAsignados;
    }

    public List<Indicador> listarIndicadores() {
        return this.indicadorDao.findAll();
    }

    public List<TareaDocente> listarTareasEntregadas(String cedulaDocenteRevisor) {
        return this.tareaDocenteDao.findByEstadoTareaDocenteAndCedulaDocenteRevisor(
                EstadoTareaDocenteEnum.EN_REVISION.getText(), cedulaDocenteRevisor);
    }

    public List<TareaDocente> listarTareasAceptadas() {
        return this.tareaDocenteDao.findByEstadoTareaDocente(EstadoTareaDocenteEnum.ACEPTADO.getText());
    }

    public List<Docente> listarDocentesTareaAsignada(Tarea codigoTarea) {
        List<TareaDocente> tareas = this.tareaDocenteDao.findByCodigoTarea(codigoTarea);
        List<Docente> docentes = new ArrayList<>();
        for (TareaDocente tarea : tareas) {
            docentes.add(tarea.getCodigoDocente());
        }
        return docentes;
    }

    public List<TareaDocente> listarTareaAsignadaPorDocente(Integer codigoDocente) {
        Docente docente = this.obtenerDocentePorCodigoDocente(codigoDocente);
        List<TareaDocente> tareas = this.tareaDocenteDao.findByCodigoDocente(docente);
        return tareas;
    }

    public List<TareaIndicador> listarIndicadoresPorTarea(Integer codigoTareaDocente) {
        TareaDocente tareaDocente = this.obtenerIndicadorPorCodigoTareaDocente(codigoTareaDocente);
        return tareaDocente.getTareaIndicadorList();
    }

    public List<TareaDocente> listarDocentesTareasAsignadas(Integer codigoDocente) {
        Docente docente = this.obtenerDocentePorCodigoDocente(codigoDocente);
        return this.tareaDocenteDao.findByCodigoDocenteAndEstadoTareaDocenteNot(docente,
                EstadoTareaDocenteEnum.ACEPTADO.getValue());
    }

    public List<CargoDocente> listarCargoDocentePorCodDocente(Integer codigoDocente) {
        Docente docente = this.obtenerDocentePorCodigoDocente(codigoDocente);
        return this.cargoDocenteDao.findByCodigoDocente(docente);
    }

    public List<TareaDocente> listarTodasTareasRevisar() {
        return this.tareaDocenteDao.findByEstadoTareaDocente(EstadoTareaDocenteEnum.EN_REVISION.getValue());
    }

    public List<TareasRealizadas> listarTodasTareasRevisadas() {
        List<TareaDocente> tareaDocentes = this.tareaDocenteDao
                .findByEstadoTareaDocente(EstadoTareaDocenteEnum.ACEPTADO.getValue());
        List<TareasRealizadas> tRealizadas = new ArrayList<>();
        for (TareaDocente tareaDocente : tareaDocentes) {
            TareasRealizadas tRealizada = new TareasRealizadas();
            tRealizada.setNombreDocenteRevisor(tareaDocente.getCodigoTarea().getNombreDocenteRevisor());
            tRealizada.setTipoTarea(tareaDocente.getCodigoTarea().getTipoTarea());
            tRealizada.setTipoProceso(
                    tareaDocente.getCodigoTarea().getCodigoProyecto().getTipoProceso().getNombreTipoProceso());
            tRealizada.setNombreProyecto(tareaDocente.getCodigoTarea().getCodigoProyecto().getNombreProyecto());
            tRealizada.setNombreTarea(tareaDocente.getCodigoTarea().getNombreTarea());
            tRealizada.setPrioridadTarea(tareaDocente.getCodigoTarea().getPrioridadTarea());
            tRealizada.setPesoTarea(tareaDocente.getCodigoTarea().getValorPesoTarea() + " "
                    + tareaDocente.getCodigoTarea().getPesoTarea());
            tRealizada.setFechaCreaciontarea(tareaDocente.getCodigoTarea().getFechaCreaciontarea());
            tRealizada.setFechaEntregaTarea(tareaDocente.getCodigoTarea().getFechaEntregaTarea());
            tRealizada.setResponsable(tareaDocente.getCodigoDocente().getNombreDocente() + " "
                    + tareaDocente.getCodigoDocente().getApellidoDocente());
            tRealizada.setTareaIndicadors(this.tareaIndicadorDao.findByTareadocenteCODIGOTAREADOCENTE(tareaDocente));
            if (tareaDocente.getNombreArchivoTareaDocente() != ""
                    || tareaDocente.getNombreArchivoTareaDocente() != null) {
                tRealizada.setNombreArchivo(tareaDocente.getNombreArchivoTareaDocente());
                // String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                //         tareaDocente.getArchivoTareaDocente()).build().toString();
                ResponseEntity<FileModel> response = this.restTemplate.getForEntity(
                    baseURLs.getGprStorageURL() + "/getFileDocenteTarea/" +ModulosEnum.INVESTIGACION.getValue()+"/"+ tareaDocente.getNombreArchivoTareaDocente() + "/" + tareaDocente.getArchivoTareaDocente(),
                    FileModel.class);
                FileModel fileModel = response.getBody();

                tRealizada.setUrlArchivo(fileModel.getUrl());
            }
            tRealizadas.add(tRealizada);
        }
        return tRealizadas;
    }

    public Tarea crear(TareaDocenteProyecto tareaDocenteProyecto, MultipartFile file) {
        tareaDocenteProyecto.getTarea().setFechaCreaciontarea(new Date());
        tareaDocenteProyecto.getTarea().setEstadoTarea(EstadoTareaEnum.ACTIVE.getValue().charAt(0));

        Tarea tarea = this.tareaDao.save(tareaDocenteProyecto.getTarea());

        if (file != null) {
            String extensionArchivo = "";
            int i = file.getOriginalFilename().toString().lastIndexOf('.');

            if (i > 0)
                extensionArchivo = file.getOriginalFilename().toString().substring(i + 1);

            tarea.setArchivoTarea(tarea.getCodigoTarea().toString() + "." + extensionArchivo);
            tarea.setNombreArchivoTarea(file.getOriginalFilename());
            tarea = this.tareaDao.save(tarea);
            /*FileRequest fileRequest = new FileRequest(file, tarea.getArchivoTarea());
            this.restTemplate.postForObject(
                    baseURLs.getGprStorageURL() + "/saveFileGuia/" + ModulosEnum.INVESTIGACION.getValue(), fileRequest,
                    FileRequest.class);*/
        }

        for (Docente docente : tareaDocenteProyecto.getDocentes()) {
            TareaDocente t = new TareaDocente();
            t.setEstadoTareaDocente(EstadoTareaDocenteEnum.ASIGNADA.getValue());
            t.setCodigoDocente(docente);
            t.setCodigoTarea(tarea);
            t.setCedulaDocenteRevisor(tarea.getIdDocenteRevisor());
            emservice.enviarCorreo(docente.getCorreoDocente(), "GPR - Nueva Tarea: " + tarea.getNombreTarea(),
                    "Se ha asignado una nueva tarea de prioridad " + tarea.getPrioridadTarea() +
                            ", y debe ser realizada hasta la fecha de:" + tarea.getFechaEntregaTarea());

            TareaDocente tDocenteBD = this.tareaDocenteDao.save(t);
            for (Indicador indicador : tareaDocenteProyecto.getIndicadors()) {
                TareaIndicador indicadorBD = new TareaIndicador();
                indicadorBD.setFechaCreacionIndicador(new Date());
                indicadorBD.setIndicadorCODIGOINDICADOR(indicador);
                indicadorBD.setDescripcionTareaIndicador(indicador.getDescripcionIndicador());
                indicadorBD.setTareadocenteCODIGOTAREADOCENTE(tDocenteBD);
                this.tareaIndicadorDao.save(indicadorBD);
            }
        }
        return tarea;
    }

    public List<Tarea> crearTareaProgramada(TareaDocenteProyecto tareaDocenteProyecto, MultipartFile file) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        tareaDocenteProyecto.getTarea().setFechaCreaciontarea(new Date());
        tareaDocenteProyecto.getTarea().setEstadoTarea(EstadoTareaEnum.ACTIVE.getValue().charAt(0));
        List<Tarea> tareas = new ArrayList<>();
        for (int k = 0; k < tareaDocenteProyecto.getTarea().getCantidadRepeticiones(); k++) {
            Tarea tareaNueva = new Tarea();
            tareaNueva.setNombreTarea(tareaDocenteProyecto.getTarea().getNombreTarea());
            tareaNueva.setTipoTarea(tareaDocenteProyecto.getTarea().getTipoTarea());
            tareaNueva.setFechaCreaciontarea(tareaDocenteProyecto.getTarea().getFechaCreaciontarea());
            tareaNueva.setPrioridadTarea(tareaDocenteProyecto.getTarea().getPrioridadTarea());
            tareaNueva.setObservacionTarea(tareaDocenteProyecto.getTarea().getObservacionTarea());
            tareaNueva.setEstadoTarea(tareaDocenteProyecto.getTarea().getEstadoTarea());
            tareaNueva.setArchivoTarea(tareaDocenteProyecto.getTarea().getArchivoTarea());
            tareaNueva.setNombreArchivoTarea(tareaDocenteProyecto.getTarea().getNombreArchivoTarea());
            tareaNueva.setPesoTarea(tareaDocenteProyecto.getTarea().getPesoTarea());
            tareaNueva.setValorPesoTarea(tareaDocenteProyecto.getTarea().getValorPesoTarea());
            tareaNueva.setIdDocenteRevisor(tareaDocenteProyecto.getTarea().getIdDocenteRevisor());
            tareaNueva.setNombreDocenteRevisor(tareaDocenteProyecto.getTarea().getNombreDocenteRevisor());
            tareaNueva.setPeriodo(tareaDocenteProyecto.getTarea().getPeriodo());
            tareaNueva.setFechaInicioTarea(tareaDocenteProyecto.getTarea().getFechaInicioTarea());

            tareaNueva.setCantidadRepeticiones(tareaDocenteProyecto.getTarea().getCantidadRepeticiones());
            tareaNueva.setTipoActividad(tareaDocenteProyecto.getTarea().getTipoActividad());
            tareaNueva.setCodigoProyecto(tareaDocenteProyecto.getTarea().getCodigoProyecto());

            LocalDate fechaFinTarea = LocalDate.parse(sdf.format(tareaNueva.getFechaInicioTarea()), formateador);

            // LocalDate.parse(sdf.format(tareaNueva.getFechaInicioTarea().toString()));

            if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Diaria")) {
                fechaFinTarea = fechaFinTarea.plusDays(1);
                // LocalDate fechaInicioTarea =
                // LocalDate.parse(sdf.format(tareaDocenteProyecto.getTarea().getFechaFinTarea()),
                // formateador);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Semanal")) {
                fechaFinTarea = fechaFinTarea.plusWeeks(1);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Mensual")) {
                fechaFinTarea = fechaFinTarea.plusMonths(1);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Bimestral")) {
                fechaFinTarea = fechaFinTarea.plusMonths(6);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Trimestral")) {
                fechaFinTarea = fechaFinTarea.plusMonths(3);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Cuatrimestral")) {
                fechaFinTarea = fechaFinTarea.plusMonths(4);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Semestral")) {
                fechaFinTarea = fechaFinTarea.plusMonths(4);
            } else if (tareaDocenteProyecto.getTarea().getPeriodo().equals("Anual")) {
                fechaFinTarea = fechaFinTarea.plusYears(1);
            }
            try {
                tareaNueva.setFechaFinTarea(sdf.parse(fechaFinTarea.toString()));
                tareaNueva.setFechaEntregaTarea(sdf.parse(fechaFinTarea.toString()));
            } catch (ParseException e) {
                throw new RuntimeException("error al transformar la fecha " + e.getMessage());
            }

            Tarea tarea = this.tareaDao.save(tareaNueva);

            if (file != null) {
                String extensionArchivo = "";
                int i = file.getOriginalFilename().toString().lastIndexOf('.');

                if (i > 0)
                    extensionArchivo = file.getOriginalFilename().toString().substring(i + 1);

                tarea.setArchivoTarea(tarea.getCodigoTarea().toString() + "." + extensionArchivo);
                tarea.setNombreArchivoTarea(file.getOriginalFilename());
                tarea = this.tareaDao.save(tarea);

               /* FileRequest fileRequest = new FileRequest(file, tarea.getArchivoTarea());
                this.restTemplate.postForObject(
                        baseURLs.getGprStorageURL() + "/saveFileGuia/" + ModulosEnum.INVESTIGACION.getValue(),
                        fileRequest, FileRequest.class);*/
                // this.saveFileGuia(file, tarea.getArchivoTarea());
            }
            tareas.add(tarea);

            for (Docente docente : tareaDocenteProyecto.getDocentes()) {
                TareaDocente t = new TareaDocente();
                t.setEstadoTareaDocente(EstadoTareaDocenteEnum.ASIGNADA.getValue());
                t.setCodigoDocente(docente);
                t.setCodigoTarea(tarea);
                t.setCedulaDocenteRevisor(tarea.getIdDocenteRevisor());
                emservice.enviarCorreo(docente.getCorreoDocente(), "GPR - Nueva Tarea: " +
                tarea.getNombreTarea(),
                "Se ha asignado una nueva tarea de prioridad " + tarea.getPrioridadTarea() +
                        ", y debe ser realizada hasta la fecha de:" + tarea.getFechaEntregaTarea());

                TareaDocente tDocenteBD = this.tareaDocenteDao.save(t);
                for (Indicador indicador : tareaDocenteProyecto.getIndicadors()) {
                    TareaIndicador indicadorBD = new TareaIndicador();
                    indicadorBD.setFechaCreacionIndicador(new Date());
                    indicadorBD.setIndicadorCODIGOINDICADOR(indicador);
                    indicadorBD.setDescripcionTareaIndicador(indicador.getDescripcionIndicador());
                    indicadorBD.setTareadocenteCODIGOTAREADOCENTE(tDocenteBD);
                    this.tareaIndicadorDao.save(indicadorBD);
                }
            }
            // tareaNueva.setFechaInicioTarea(tareaNueva.getFechaFinTarea());
            tareaDocenteProyecto.getTarea().setFechaInicioTarea(tareaNueva.getFechaFinTarea());
        }
        return tareas;
    }

    public void crearTareasFromProyecto(Proyecto proyecto, Integer idProyectoCopiar) {
        Long codProy = this.proyectoDao.count() + 1;
        proyecto.setCodigoProyecto(codProy.intValue());
        proyecto.setNombreProyecto(proyecto.getNombreProyecto().toUpperCase());
        proyecto.setFechaCreacionproyecto(new Date());
        proyecto.setEstadoProyecto(EstadoProyectoEnum.ACTIVE.getValue());
        proyecto = this.proyectoDao.save(proyecto);

        Proyecto proyectoModel = this.proyectoDao.findById(idProyectoCopiar).get();
        for (Tarea tarea : proyectoModel.getTareaList()) {
            Tarea tareaNueva = new Tarea();
            tareaNueva.setNombreTarea(tarea.getNombreTarea());
            tareaNueva.setTipoTarea(tarea.getTipoTarea());
            tareaNueva.setFechaCreaciontarea(tarea.getFechaCreaciontarea());
            tareaNueva.setPrioridadTarea(tarea.getPrioridadTarea());
            tareaNueva.setObservacionTarea(tarea.getObservacionTarea());
            tareaNueva.setEstadoTarea(tarea.getEstadoTarea());
            tareaNueva.setFechaEntregaTarea(tarea.getFechaEntregaTarea());
            tareaNueva.setArchivoTarea(tarea.getArchivoTarea());
            tareaNueva.setNombreArchivoTarea(tarea.getNombreArchivoTarea());
            tareaNueva.setPesoTarea(tarea.getPesoTarea());
            tareaNueva.setValorPesoTarea(tarea.getValorPesoTarea());
            tareaNueva.setIdDocenteRevisor(tarea.getIdDocenteRevisor());
            tareaNueva.setNombreDocenteRevisor(tarea.getNombreDocenteRevisor());
            tareaNueva.setPeriodo(tarea.getPeriodo());
            tareaNueva.setFechaInicioTarea(tarea.getFechaInicioTarea());
            tareaNueva.setFechaFinTarea(tarea.getFechaFinTarea());
            tareaNueva.setCantidadRepeticiones(tarea.getCantidadRepeticiones());
            tareaNueva.setTipoActividad(tarea.getTipoActividad());
            tareaNueva.setCodigoProyecto(proyecto);
            tareaNueva = this.tareaDao.save(tareaNueva);
            for (TareaDocente tareaDocente : tarea.getTareaDocenteList()) {
                TareaDocente tareaDocenteNuevo = new TareaDocente();
                tareaDocenteNuevo.setArchivoTareaDocente(tareaDocente.getArchivoTareaDocente());
                tareaDocenteNuevo.setEstadoTareaDocente(tareaDocente.getEstadoTareaDocente());
                tareaDocenteNuevo.setNombreArchivoTareaDocente(tareaDocente.getNombreArchivoTareaDocente());
                tareaDocenteNuevo.setFechaEntregadaTareaDocente(tareaDocente.getFechaEntregadaTareaDocente());
                tareaDocenteNuevo.setCedulaDocenteRevisor(tareaDocente.getCedulaDocenteRevisor());
                tareaDocenteNuevo.setCodigoDocente(tareaDocente.getCodigoDocente());
                tareaDocenteNuevo.setCodigoTarea(tareaNueva);
                tareaDocenteNuevo = this.tareaDocenteDao.save(tareaDocenteNuevo);
                for (TareaIndicador tareaIndicador : tareaDocente.getTareaIndicadorList()) {
                    TareaIndicador tareaIndicadorNuevo = new TareaIndicador();
                    tareaIndicadorNuevo.setFechaCreacionIndicador(tareaIndicador.getFechaCreacionIndicador());
                    tareaIndicadorNuevo.setValorIndicador(tareaIndicador.getValorIndicador());
                    tareaIndicadorNuevo.setDescripcionTareaIndicador(tareaIndicador.getDescripcionTareaIndicador());
                    tareaIndicadorNuevo.setIndicadorCODIGOINDICADOR(tareaIndicador.getIndicadorCODIGOINDICADOR());
                    tareaIndicadorNuevo.setTareadocenteCODIGOTAREADOCENTE(tareaDocenteNuevo);
                    this.tareaIndicadorDao.save(tareaIndicadorNuevo);
                }
            }
        }
    }

    public TareaDocente modificarDatos(TareaDocenteProyecto tareaDocenteProyecto, MultipartFile file) {
        Tarea tarea = this.tareaDao.save(tareaDocenteProyecto.getTarea());
        if (file != null) {
            // try {
            // Files.deleteIfExists(this.rootFileGuia.resolve(tarea.getArchivoTarea()));
            // } catch (IOException e) {
            // throw new RuntimeException("No se puede guardar el archivo. Error " +
            // e.getMessage());
            // }
            String extensionArchivo = "";
            int i = file.getOriginalFilename().toString().lastIndexOf('.');

            if (i > 0)
                extensionArchivo = file.getOriginalFilename().toString().substring(i + 1);

            tarea.setArchivoTarea(tarea.getCodigoTarea().toString() + "." + extensionArchivo);
            tarea.setNombreArchivoTarea(file.getOriginalFilename());
            tarea = this.tareaDao.save(tarea);
            FileRequest fileRequest = new FileRequest(file, tarea.getArchivoTarea());
            this.restTemplate.postForObject(
                    baseURLs.getGprStorageURL() + "/saveFileGuia/" + ModulosEnum.INVESTIGACION.getValue(), fileRequest,
                    FileRequest.class);
            // this.saveFileGuia(file, tarea.getArchivoTarea());
        }
        List<TareaDocente> tareaDocentes = this.tareaDocenteDao.findByCodigoTarea(tareaDocenteProyecto.getTarea());
        int indice;
        // Boolean check = true;
        // TareaDocente tareaD = new TareaDocente();
        for (TareaDocente tareaDocente : tareaDocentes) {
            /*
             * if(check){
             * tareaD = tareaDocente;
             * check = false;
             * }
             */
            indice = tareaDocenteProyecto.getDocentes().indexOf(tareaDocente.getCodigoDocente());
            if (indice == -1) {

                for (TareaIndicador tIndicador : tareaDocente.getTareaIndicadorList())
                    this.tareaIndicadorDao.delete(tIndicador);

                this.tareaDocenteDao.delete(tareaDocente);
            } else
                tareaDocenteProyecto.getDocentes().remove(indice);
        }

        if (tareaDocenteProyecto.getDocentes().size() > 0) {
            for (Docente docente : tareaDocenteProyecto.getDocentes()) {
                TareaDocente t = new TareaDocente();
                t.setEstadoTareaDocente(EstadoTareaDocenteEnum.ASIGNADA.getValue());
                t.setCodigoDocente(docente);
                t.setCodigoTarea(tareaDocenteProyecto.getTarea());
                t.setCedulaDocenteRevisor(tarea.getIdDocenteRevisor());
                emservice.enviarCorreo(docente.getCorreoDocente(),
                        "GPR - Nueva Tarea: " + tareaDocenteProyecto.getTarea().getNombreTarea(),
                        "Se ha asignado una nueva tarea de prioridad "
                                + tareaDocenteProyecto.getTarea().getPrioridadTarea() +
                                ", y debe ser realizada hasta la fecha de:"
                                + tareaDocenteProyecto.getTarea().getFechaEntregaTarea());
                TareaDocente tDocenteBD = this.tareaDocenteDao.save(t);
                for (Indicador indicador : tareaDocenteProyecto.getIndicadors()) {
                    TareaIndicador indicadorBD = new TareaIndicador();
                    indicadorBD.setFechaCreacionIndicador(new Date());
                    indicadorBD.setIndicadorCODIGOINDICADOR(indicador);
                    indicadorBD.setDescripcionTareaIndicador(indicador.getDescripcionIndicador());
                    indicadorBD.setTareadocenteCODIGOTAREADOCENTE(tDocenteBD);
                    this.tareaIndicadorDao.save(indicadorBD);
                }
            }
        }

        /*
         * 
         * List<TareaIndicador> tareaIndicadors =
         * this.tareaIndicadorDao.findByTareadocenteCODIGOTAREADOCENTE(tareaD);
         * for(TareaIndicador tareaIndicador : tareaIndicadors){
         * indice = tareaDocenteProyecto.getIndicadors().indexOf(tareaIndicador.
         * getIndicadorCODIGOINDICADOR());
         * if(indice == -1)
         * this.tareaIndicadorDao.delete(tareaIndicador);
         * else
         * tareaDocenteProyecto.getIndicadors().remove(indice);//eliminar los
         * indicadores de esta tarea
         * }
         */

        /*
         * if(tareaDocenteProyecto.getIndicadors().size() > 0){
         * for(Indicador indicador : tareaDocenteProyecto.getIndicadors()){
         * TareaIndicador indicadorBD = new TareaIndicador();
         * indicadorBD.setFechaCreacionIndicador(new Date());
         * indicadorBD.setIndicadorCODIGOINDICADOR(indicador);
         * indicadorBD.setTareadocenteCODIGOTAREADOCENTE(tDocenteBD);
         * this.tareaIndicadorDao.save(indicadorBD);
         * this.tareaIndicadorDao.save(tIndicador);
         * }
         * }
         */
        return new TareaDocente();
    }

    public TareaDocente guardarTareaAsignadaAlProfesor(List<TareaIndicador> tareaIndicadors, MultipartFile file,
            Integer codigoTareaDocente) {
        TareaDocente tareaDocente = this.obtenerIndicadorPorCodigoTareaDocente(codigoTareaDocente);
        if (file != null) {
            /*
             * String extensionArchivo = "";
             * int i = file.getOriginalFilename().toString().lastIndexOf('.');
             * 
             * if (i > 0)
             * extensionArchivo = file.getOriginalFilename().toString().substring(i+1);
             */
            tareaDocente.setArchivoTareaDocente(tareaDocente.getCodigoTareaDocente().toString() + ".pdf");// Revisar
            tareaDocente.setNombreArchivoTareaDocente(file.getOriginalFilename());

            /*FileRequest fileRequest = new FileRequest(file, tareaDocente.getArchivoTareaDocente());
            this.restTemplate.postForObject(
                    baseURLs.getGprStorageURL() + "/saveFile/" + ModulosEnum.INVESTIGACION.getValue(), fileRequest,
                    FileRequest.class);*/
            // this.saveFile(file, tareaDocente.getArchivoTareaDocente());
        }

        for (TareaIndicador tIndicador : tareaIndicadors)
            this.tareaIndicadorDao.save(tIndicador);

        tareaDocente.setEstadoTareaDocente(EstadoTareaDocenteEnum.EN_REVISION.getValue());
        this.tareaDocenteDao.save(tareaDocente);
        Tarea tarea = tareaDocente.getCodigoTarea();
        tarea.setEstadoTarea(EstadoTareaEnum.INACTIVE.getValue().charAt(0));
        this.tareaDao.save(tarea);
        Docente docenteRevisor = this.docenteDao.findByCedulaDocente(tarea.getIdDocenteRevisor());
        emservice.enviarCorreo(docenteRevisor.getCorreoDocente(),
                "GPR - Actividad: " + tareaDocente.getCodigoTarea().getNombreTarea(),
                "La Actividad perteneciente a: " + tareaDocente.getCodigoDocente().getNombreDocente() + " " +
                        tareaDocente.getCodigoDocente().getApellidoDocente() + " ha sido enviada y debe ser revisada ");

        return tareaDocente;
    }

    // public void init() {
    // try {
    // Files.createDirectory(root);
    // } catch (IOException e) {
    // throw new RuntimeException("No se puede inicializar la carpeta uploads");
    // }
    // }

    // private void saveFile(MultipartFile file, String nameFile) {
    // try {
    // Files.deleteIfExists(this.root.resolve(nameFile));
    // Files.copy(file.getInputStream(), this.root.resolve(nameFile));
    // } catch (IOException e) {
    // throw new RuntimeException("No se puede guardar el archivo. Error " +
    // e.getMessage());
    // }
    // }

    public void guardarArchivoTareaAsignadaAlProfesor(MultipartFile file, Integer codigoTareaDocente) {
        /*
         * TareaDocente tareaDocente =
         * this.obtenerIndicadorPorCodigoTareaDocente(codigoTareaDocente);
         * this.saveFile(file,tareaDocente.getCodigoTareaDocente().toString()+".pdf");
         * tareaDocente.setArchivoTareaDocente(tareaDocente.getCodigoTareaDocente().
         * toString()+".pdf");//Revisar
         * tareaDocente.setNombreArchivoTareaDocente(file.getOriginalFilename());
         * tareaDocente.setEstadoTareaDocente(EstadoTareaDocenteEnum.EN_REVISION.
         * getValue());
         * this.tareaDocenteDao.save(tareaDocente);
         */
    }

    public void aprobarTareaDocente(TareaDocente tareaDocente) {
        /*
         * DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss z");
         * String date = dateFormat.format(new Date());
         */
        tareaDocente.setEstadoTareaDocente(EstadoTareaDocenteEnum.ACEPTADO.getValue());
        emservice.enviarCorreo(tareaDocente.getCodigoDocente().getCorreoDocente(),
                "GPR - Actividad: " + tareaDocente.getCodigoTarea().getNombreTarea(),
                "Su Actividad ha sido aprobada: ");
        this.tareaDocenteDao.save(tareaDocente);
    }

    public void denegarTareaDocente(TareaDocente tareaDocente) {
        tareaDocente.setEstadoTareaDocente(EstadoTareaDocenteEnum.DENEGADO.getValue());
        emservice.enviarCorreo(tareaDocente.getCodigoDocente().getCorreoDocente(),
                "GPR - Actividad: " + tareaDocente.getCodigoTarea().getNombreTarea(),
                "Su Actividad ha sido Denegada: ");
        this.tareaDocenteDao.save(tareaDocente);
    }

    public void eliminarTarea(Integer codigoTarea) {
        Tarea t = this.obtenerTareaPorCodigoTarea(codigoTarea);
        /*
         * for (TareaDocente tareaDocente : t.getTareaDocenteList()) {
         * for (TareaIndicador tareaIndicador : tareaDocente.getTareaIndicadorList()) {
         * System.out.println("TareaIndicadorD"+tareaIndicador.getCodigoTareaIndicador()
         * );
         * this.tareaIndicadorDao.deleteById(tareaIndicador.getCodigoTareaIndicador());
         * }
         * this.tareaDocenteDao.deleteById(tareaDocente.getCodigoTareaDocente());
         * }
         */
        List<TareaDocente> tareaDocentes = this.tareaDocenteDao.findByCodigoTarea(t);
        for (TareaDocente tareaDocente : tareaDocentes) {
            for (TareaIndicador tIndicador : tareaDocente.getTareaIndicadorList())
                this.tareaIndicadorDao.delete(tIndicador);
            this.tareaDocenteDao.delete(tareaDocente);
        }
        this.tareaDao.delete(t);
    }

}