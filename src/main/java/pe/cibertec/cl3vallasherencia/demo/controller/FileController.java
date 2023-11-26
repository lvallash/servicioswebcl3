package pe.cibertec.cl3vallasherencia.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.cibertec.cl3vallasherencia.demo.model.response.ResponseFile;
import pe.cibertec.cl3vallasherencia.demo.service.FileService;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/file")
public class FileController {

    @Value("${app.allowed-image-extensions}")
    private String allowedImageExtensions;

    @Value("${app.allowed-excel-extensions}")
    private String allowedExcelExtensions;

    private final FileService fileService;

    @Value("${app.image-directory}")
    private String imageUploadDirectory;

    @Value("${app.excel-directory}")
    private String excelUploadDirectory;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @PostMapping("/upload")
    public ResponseEntity<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : null;

        if (isImageFile(fileExtension) && allowedImageExtensions.contains(fileExtension)) {
            // Procesar archivo de imagen
            try {
                fileService.guardar(file, imageUploadDirectory);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseFile.builder().message("Imagen subida con éxito").build());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseFile.builder().message("Error al subir la imagen").build());
            }
        } else if (isExcelFile(fileExtension) && allowedExcelExtensions.contains(fileExtension)) {
            // Procesar archivo de Excel
            try {
                fileService.guardar(file, excelUploadDirectory);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseFile.builder().message("Archivo Excel subido con éxito").build());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseFile.builder().message("Error al subir el archivo Excel").build());
            }
        } else {
            return ResponseEntity.badRequest().body(ResponseFile.builder().message("La extensión del archivo no es válida.").build());
        }
    }

    private boolean isImageFile(String extension) {
        // Lógica para determinar si la extensión corresponde a un archivo de imagen
        return extension != null && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"));
    }

    private boolean isExcelFile(String extension) {
        // Lógica para determinar si la extensión corresponde a un archivo Excel
        return extension != null && (extension.equals("xlsx") || extension.equals("xls"));
    }
}
