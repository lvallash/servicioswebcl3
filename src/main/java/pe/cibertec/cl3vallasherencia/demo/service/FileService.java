package pe.cibertec.cl3vallasherencia.demo.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {
    private final Path rootFolder = Paths.get("archivos");

    public void guardar(MultipartFile archivo, String directorio) throws IOException {
        Path directorioPath = Paths.get(directorio);
        try {
            Files.copy(archivo.getInputStream(), directorioPath.resolve(archivo.getOriginalFilename()));
        } finally {
            if (archivo.getInputStream() != null) {
                archivo.getInputStream().close();
            }
        }
    }

    public void guardarArchivos(List<MultipartFile> archivos, String directorio) throws IOException {
        for (MultipartFile archivo : archivos) {
            this.guardar(archivo, directorio);
        }
    }

    }
