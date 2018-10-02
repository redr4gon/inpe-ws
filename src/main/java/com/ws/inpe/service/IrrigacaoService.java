package com.ws.inpe.service;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.ws.inpe.model.*;
import com.ws.inpe.util.FileHelper;
import com.ws.inpe.util.INPECommunication;
import com.ws.inpe.util.MQTTCommunication;
import com.ws.inpe.util.Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável pelos serviços de irrigação
 */
@Service
public class IrrigacaoService {

    @Autowired
    MQTTObject mqttObject;

    @Autowired
    INPEObject inpeObject;

    public String newLine = System.getProperty("line.separator");

    /**
     * Checa os status das comunicações com INPE e MQTT na data e hora atual
     *
     * @return Status MQTT, status INPE, Data hora atual
     */
    public Health healthCheck() {

        Health health = new Health();
        health.setConexao_INPE(new INPECommunication().checkINPECommunicationOK(inpeObject) ? "CONECTADO" : "DESCONECTADO");
        health.setConexao_MQTT(new MQTTCommunication().initializeClient(mqttObject) ? "CONECTADO" : "DESCONECTADO");

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss") .withZone(ZoneId.systemDefault());

        health.setDataHora(DATE_TIME_FORMATTER.format(Instant.now()));

        return health;
    }

    /**
     * Ativa ou desativa a irrigação conforme status
     * Esse metodo cria um objeto STATUS a ser enviado no MQTT
     * Para manter este status registrado no webservice, esse status também é salvo em arquivo
     *
     * @param status
     * @return status atual
     */
    public JSONObject controlaIrrigacao(Status status) {

        Status newStatus = new FileHelper().alterarStatus(status);

        if (newStatus == null) {
            return null;
        }

        JSONObject jsonObject = Util.formatStatus(newStatus);

        mqttObject.setContent(jsonObject.toString());

        if (!new MQTTCommunication().sendMessage(mqttObject)) {
            new FileHelper().reverterStatus();
            return null;
        }

        return jsonObject;
    }

    /**
     * Busca e exibe o status atual da irrigação
     *
     * @return Json com o status
     */
    public JSONObject findStatus() {
        return Util.formatStatus(new FileHelper().findStatus());
    }

    /**
     * Busca o clima nos proximos 4 dias
     *
     * @return Json com o clima dos proximos dias
     */
    public String buscaTempo(boolean jsonFormat) {
        Document doc = new INPECommunication().getWeatherByCity(inpeObject);
        return tempoFormatado(doc, jsonFormat);
    }

    /**
     * Formata o documento sobre clima retornado pelo INPE
     *
     * @param document
     * @return JSon com o clima dos proximos dias
     */
    public String tempoFormatado(Document document, boolean jsonFormat) {

        if (document == null) {
            return null;
        }

        XML xml = new XMLDocument(document);
        List<XML> list = xml.nodes("//previsao");

        StringBuffer buffer = new StringBuffer();

        buffer.append("Previsão do tempo nos próximos 4 dias:");
        buffer.append(newLine);

        String data = "";
        String tempo = "";

        JSONObject jsonObject = new JSONObject();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for ( XML node : list ) {

            data = node.xpath("//dia/text()").get(0);

            try {
                data = new SimpleDateFormat("dd/MM/yyyy").format(format.parse(data));
            }catch(ParseException e) {
                data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            }

            tempo = node.xpath("//tempo/text()").get(0);

            if (jsonFormat) {
                jsonObject.put(data, Tempo.valueOf(tempo).getDescricao());
                continue;
            }

            buffer.append(data + " - " + Tempo.valueOf(tempo).getDescricao());
            buffer.append(newLine);
        }

        return jsonFormat ? jsonObject.toString() : buffer.toString();
    }

}
