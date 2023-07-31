package ec.edu.espe.gpr.model.file;

public class FileRequest {

    private String fileBase64;
    private String nameFile;
    private String previousNameFile;

    public FileRequest(String fileBase64, String nameFile) {
        this.fileBase64 = fileBase64;
        this.nameFile = nameFile;
    }

    public FileRequest() {
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getPreviousNameFile() {
        return previousNameFile;
    }

    public void setPreviousNameFile(String previousNameFile) {
        this.previousNameFile = previousNameFile;
    }
}