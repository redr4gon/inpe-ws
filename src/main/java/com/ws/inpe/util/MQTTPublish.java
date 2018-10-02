package com.ws.inpe.util;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe responsável para a publicação do MQTT
 */
public class MQTTPublish {

    protected static final Logger logger = LoggerFactory.getLogger(MQTTPublish.class);

    /**
     * Publica a mensagem (pubMsg) no topico do MQTT (topic)
     *
     * @param topic
     * @param pubMsg
     * @throws Exception
     */
    public void publish(MqttTopic topic, byte[] pubMsg) throws Exception {

        int pubQoS = 0;
        MqttMessage message = new MqttMessage(pubMsg);

        message.setQos(pubQoS);
        message.setRetained(false);

        logger.info("Publicando no tópico: " + topic + " com level(qos) 0 - envio e exclusão!");

        MqttDeliveryToken token = null;

        token = topic.publish(message);
        token.waitForCompletion();
        Thread.sleep(100);
    }
}
