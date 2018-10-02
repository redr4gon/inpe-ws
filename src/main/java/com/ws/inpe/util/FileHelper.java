package com.ws.inpe.util;

import com.ws.inpe.model.Status;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    private final String FILENAME = "device_status.txt";

    /**
     * Reverte o status para o anterior
     *
     * @return status atual
     */
    public Status reverterStatus() {
        return alterarStatus(null);
    }

    /**
     * Altera o status para ON/OFF
     *
     * @param status (opcional - caso não seja fornecido, o status somente será revertido ON->OFF, OFF->ON)
     * @return status atual
     */
    public Status alterarStatus(Status status) {

        File file = new File(FILENAME);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        Status newStatus = null;

        try {

            Path path = Paths.get(file.getName());

            String read = "";

            if (Files.readAllLines(path).size() > 0) {
                read = Files.readAllLines(path).get(0);
            }

            if (!StringUtils.isEmpty(read)) {

                //coloca o status atual ou reverte
                if (status != null) {
                    newStatus = status;
                }else if (read.equalsIgnoreCase(Status.ON.name())) {
                    newStatus = Status.OFF;
                }else{
                    newStatus = Status.ON;
                }
            }

            byte[] strToBytes = newStatus.name().getBytes();
            Files.write(path, strToBytes);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return newStatus;
    }

    /**
     * Busca o status atual
     *
     * @return status atual
     */
    public Status findStatus() {

        File file = new File(FILENAME);

        if (!file.exists()) {
            return Status.OFF;
        }

        String read = "";
        Status status = null;

        try {

            Path path = Paths.get(file.getName());

            if (Files.readAllLines(path).size() > 0) {
                read = Files.readAllLines(path).get(0);
            }

            status = Status.valueOf(read);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}
