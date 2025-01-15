package com.forero.backend.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
@Profile("dev")
public class MockAwsService {

    public String guardarArchivo(File archivo) throws Exception {
        File destino = new File("temp/" + archivo.getName());
        Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return "file://" + destino.getAbsolutePath();
    }

    public void enviarEmail(String destinatario, String asunto, String contenido, String urlArchivo) {
        System.out.println("Email simulado enviado a: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Contenido: " + contenido);
        System.out.println("URL del archivo: " + urlArchivo);
    }
}