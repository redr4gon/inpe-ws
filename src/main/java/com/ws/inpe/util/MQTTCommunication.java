package com.ws.inpe.util;

import com.ws.inpe.model.MQTTObject;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Classe responsável pela conexao e publicações de mensagens MQTT
 */
public class MQTTCommunication implements MqttCallback {

    protected static final Logger logger = LoggerFactory.getLogger(INPECommunication.class);

    private MqttClient myClient;
    private MqttConnectOptions connOpt;
    private String myTopic;

    /**
     * Método disparado quando a conexão é perdida
     *
     * @param t
     */
    @Override
    public void connectionLost(Throwable t) {
        logger.error("Conexão perdida com o MQTT!", t);
    }

    /**
     * Método disparado quando uma mensagem é recebida (nao é o foco - não temos receptor)
     *
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        logger.info("Mensagem recebida!");
        return;
    }

    /**
     * Método disparado quando uma mensagem é entregue à fila
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            String mensagem = new String(iMqttDeliveryToken.getMessage().getPayload());
            logger.info("Mensagem enviada: " + mensagem);
        }catch (Exception e){
            logger.error("Ocorreu um erro durante o envio da mensagem! Check os parametros informados de conexão!", e);
        }
    }

    /**
     * Envia a mensagem para a queue do MQTT
     *
     * @param mqttObject
     * @return true se enviado com sucesso
     */
    public boolean sendMessage(MQTTObject mqttObject) {

        boolean success = false;

        initializeClient(mqttObject);

        MqttTopic topic = myClient.getTopic(myTopic);

        try {
            MQTTPublish mqttPublish = new MQTTPublish();
            mqttPublish.publish(topic, mqttObject.getContent().getBytes());

            success = true;

            myClient.disconnect();
        } catch (Exception e) {
            logger.error("Ocorreu um erro durante o envio da mensagem! Check os parametros informados de conexão!", e);
        }

        return success;
    }

    /**
     * Inicializa o cliente responsável pela entrega das publicações
     *
     * @param mqttObject
     * @return true if a conexão deu certo
     */
    public boolean initializeClient(MQTTObject mqttObject) {

        boolean success = false;

        connOpt = new MqttConnectOptions();
        myTopic = mqttObject.getTopic();

        connOpt.setCleanSession(false);
        connOpt.setKeepAliveInterval(mqttObject.getLifeTime());

        if (!StringUtils.isEmpty(mqttObject.getUser()) && !StringUtils.isEmpty(mqttObject.getPassword())) {
            connOpt.setUserName(mqttObject.getUser());
            connOpt.setPassword(mqttObject.getPassword().toCharArray());
        }

        String uniqueID = UUID.randomUUID().toString();

        String url = mqttObject.getUrl() + ":" + mqttObject.getPort();

        try {
            myClient = new MqttClient(url, uniqueID);
            myClient.setCallback(this);

            connOpt.setWill(myClient.getTopic(myTopic), uniqueID.getBytes(), 2, true);

            myClient.connect(connOpt);

            success = true;
        } catch (MqttException e) {
            logger.error("Ocorreu um erro durante a criação do cliente! Check os parametros informados de conexão!", e);
        }

        return success;
    }
}
