package ec.edu.espe.gpr.model.file;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

    private MultipartFile file;
    private String nameFile;
    
    public FileRequest(MultipartFile file, String nameFile) {
        this.file = file;
        this.nameFile = nameFile;
    }
    public FileRequest() {
    }
    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    public String getNameFile() {
        return nameFile;
    }
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}