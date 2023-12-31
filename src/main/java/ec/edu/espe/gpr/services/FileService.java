package ec.edu.espe.gpr.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ec.edu.espe.gpr.config.BaseURLValues;
import ec.edu.espe.gpr.dao.ITareaDao;
import ec.edu.espe.gpr.dao.ITareaDocenteDao;
import ec.edu.espe.gpr.model.Tarea;
import ec.edu.espe.gpr.model.TareaDocente;
import ec.edu.espe.gpr.model.file.FileModel;

@Service
public class FileService {

    private final Path root = Paths.get("uploads");
    private final Path rootFileGuia = Paths.get("archivo_guia");

    @Autowired
    private ITareaDocenteDao tareaDocenteDao;
    @Autowired
    private ITareaDao tareaDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseURLValues baseURLs;

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("No se puede inicializar la carpeta uploads");
        }
    }

    public List<FileModel> getFiles() {
        ResponseEntity<FileModel[]> response = this.restTemplate.getForEntity(baseURLs.getGprStorageURL() + "/getFiles",
                FileModel[].class);
        FileModel[] objectArray = response.getBody();
        List<FileModel> fileInfos = Arrays.asList(objectArray);
        return fileInfos;
    }

    // public Resource getFile(){

    // return objectArray;
    // }

    public FileModel getFileDocenteTarea(Integer codigoTareaDocente) {
        TareaDocente tareaDocente = this.getTareaDocente(codigoTareaDocente);
        FileModel fileModel = null;
        if (tareaDocente.getNombreArchivoTareaDocente() != ""
                || tareaDocente.getNombreArchivoTareaDocente() != null) {
            String filename = tareaDocente.getNombreArchivoTareaDocente();
            String fileSaveName = tareaDocente.getArchivoTareaDocente();
            ResponseEntity<FileModel> response = this.restTemplate.getForEntity(
                    baseURLs.getGprStorageURL() + "/getFileDocenteTarea/" + filename + "/" + fileSaveName,
                    FileModel.class);
            fileModel = response.getBody();
            // String url = MvcUriComponentsBuilder.fromMethodName(FileController.class,
            // "getFile",
            // tareaDocente.getArchivoTareaDocente()).build().toString();
            // fileModel = new FileModel(filename, url);
        }
        return fileModel;
    }

    public FileModel getFileTarea(Integer codigoTarea) {
        Tarea tarea = this.getTarea(codigoTarea);
        FileModel fileModel = null;
        if (tarea.getNombreArchivoTarea() != "" || tarea.getNombreArchivoTarea() != null) {
            String filename = tarea.getNombreArchivoTarea();
            // String[] fileProperties = filename.split("\\.");
            // Tarea tarea = this.fileService.getTarea(Integer.parseInt(fileProperties[0]));
            ResponseEntity<FileModel> response = this.restTemplate.getForEntity(
                    baseURLs.getGprStorageURL() + "/getFileTarea/" + filename + "/" + tarea.getArchivoTarea(),
                    FileModel.class);
            fileModel = response.getBody();
            // String url = MvcUriComponentsBuilder.fromMethodName(FileController.class,
            // "getFileTareaGuia",
            // tarea.getArchivoTarea()).build().toString();
            // fileModel = new FileModel(filename, url);
        }
        return fileModel;
    }

    public void initFileGuia() {
        try {
            Files.createDirectory(rootFileGuia);
        } catch (IOException e) {
            throw new RuntimeException("No se puede inicializar la carpeta uploads");
        }
    }

    public void deleteAllFileGuia() {
        FileSystemUtils.deleteRecursively(rootFileGuia.toFile());
    }

    private TareaDocente obtenerTareaDocentePorCodigoTareaDocente(Integer codigoTareaDocente) {
        Optional<TareaDocente> tareaOpt = this.tareaDocenteDao.findById(codigoTareaDocente);
        if (tareaOpt.isPresent())
            return tareaOpt.get();
        else
            return null;
    }

    public TareaDocente getTareaDocente(Integer codigoTareaDocente) {
        return this.obtenerTareaDocentePorCodigoTareaDocente(codigoTareaDocente);
    }

    private Tarea obtenerTareaPorCodigoTarea(Integer codigoTarea) {
        Optional<Tarea> tareaOpt = this.tareaDao.findById(codigoTarea);
        if (tareaOpt.isPresent())
            return tareaOpt.get();
        else
            return null;
    }

    public Tarea getTarea(Integer codigoTarea) {
        return this.obtenerTareaPorCodigoTarea(codigoTarea);
    }

    public void save(MultipartFile file) {
        try {
            // copy (que queremos copiar, a donde queremos copiar)
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("No se puede guardar el archivo. Error " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se puede leer el archivo ");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Resource loadFileTarea(String filename) {
        try {
            Path file = rootFileGuia.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se puede leer el archivo ");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        // Files.walk recorre nuestras carpetas (uploads) buscando los archivos
        // el 1 es la profundidad o nivel que queremos recorrer
        // :: Referencias a metodos
        // Relativize sirve para crear una ruta relativa entre la ruta dada y esta ruta
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException("No se pueden cargar los archivos ");
        }
    }

    /*
     * public String deleteFile(String filename){
     * try {
     * Boolean delete = Files.deleteIfExists(this.root.resolve(filename));
     * return "Borrado";
     * }catch (IOException e){
     * e.printStackTrace();
     * return "Error Borrando ";
     * }
     * }
     */

}