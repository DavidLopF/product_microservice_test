package co.corp.linktic.productservice.service.s3;


import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    /**
     * Sube un archivo a AWS S3 y retorna la URL pública del archivo subido.
     *
     * @param file el archivo a subir
     * @return la URL pública del archivo
     */
    String uploadFile(MultipartFile file);
}
