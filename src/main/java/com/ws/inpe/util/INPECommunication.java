package com.ws.inpe.util;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.ws.inpe.model.INPEObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Classe responsável pela conexão e busca de dados nas apis do INPE
 */
public class INPECommunication {

    protected static final Logger logger = LoggerFactory.getLogger(INPECommunication.class);

    private static final int TIMEOUT = 5 * 1000; // 5 seconds
    private static final String METHOD = "GET";

    /**
     * Metodo principal da classe para acesso à clima nos proximos dias
     *
     * @param inpeObject
     * @return Document retornado pelo INPE
     */
    public Document getWeatherByCity(INPEObject inpeObject) {

        if (inpeObject.getCidade() == null) {
            return null;
        }

        if (StringUtils.isEmpty(inpeObject.getCidade()) || inpeObject.getCidade().contains("#")) {
            return null;
        }

        inpeObject = preencheCodCidade(inpeObject);

        if (inpeObject == null) {
            return null;
        }

        Document doc = null;

        String url = inpeObject.getUrlPrevisao().replaceAll("#", String.valueOf(inpeObject.getCodCidade()));

        try {
            doc = retrieveXML(url);
        }catch(Exception e) {
            logger.error("Erro durante a recuperação de objeto externo!", e);
        }

        return doc;
    }

    /**
     * Transforma o documento retornado pelo INPE em um objeto XML manipulável
     *
     * @param url
     * @return Document em formato XML
     */
    private Document retrieveXML(String url) {

        Document xmlDoc = null;

        try {

            BufferedReader clientReader = accessExternalEndpoint(url);

            if (clientReader == null) {
                return null;
            }

            String clientMessage = clientReader.readLine();
            InputSource is = new InputSource(new StringReader(clientMessage));
            xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            logger.error("Erro durante conversões do recurso externo acessado!", ex);
        }
        return xmlDoc;
    }

    /**
     * Acessa o servidor INPE para busca de dados
     *
     * @param url
     * @return buffer com os dados retornados
     */
    private BufferedReader accessExternalEndpoint(String url) {

        BufferedReader in = null;

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(METHOD);
            con.setConnectTimeout(TIMEOUT);

            if (con.getResponseCode() !=  HttpURLConnection.HTTP_OK) {
                return null;
            }

            in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        }catch( IOException e){
            logger.error("Erro acessando recurso externo: INPE!", e);
        }

        return in;
    }

    /**
     * Busca o código da cidade com base no nome da cidade (Araraquara =  586)
     * A consulta de clima do INPE exige o codigo da cidade ao invés do nome dela
     *
     * @param inpeObject com a cidade preenchida
     * @return inpeObject com o codigo da cidade preenchido
     */
    public INPEObject preencheCodCidade(INPEObject inpeObject) {

        String url = inpeObject.getUrlCidade();
        url = url.replaceAll("#", inpeObject.getCidade());

        Document doc = retrieveXML(url);

        if (doc == null) {
            return null;
        }

        XML xml = new XMLDocument(doc);
        List<XML> list = xml.nodes("//cidade");

        StringBuffer buffer = new StringBuffer();

        String id = list.get(0).xpath("//id/text()").get(0);

        if (id != null) {
            inpeObject.setCodCidade(Integer.parseInt(id));
        }

        return inpeObject;
    }

    /**
     * Verifica se a conexão com o INPE está OK
     * Como o INPE não possui uma api para verificação de status, usamos uma busca simples de cidade
     *
     * @param inpeObject
     * @return true se conexão esta ok
     */
    public boolean checkINPECommunicationOK(INPEObject inpeObject) {
        return accessExternalEndpoint(inpeObject.getUrlCheck()) != null;
    }
}
