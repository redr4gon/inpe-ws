package com.ws.inpe.configuration;

import com.ws.inpe.model.MQTTObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mqtt.properties")
public class MQTTConfiguration {

    @Value( "${mqtt.url}" )
    private String url;

    @Value( "${mqtt.port}" )
    private String port;

    @Value( "${mqtt.topic}" )
    private String topic;

    @Value( "${mqtt.life-time}" )
    private String lifeTime;

    @Value("mqtt.user")
    private String user;

    @Value("mqtt.password")
    private String password;

    /**
     * Cria um objeto MQTT a ser usado nas conexões com queue
     *
     * @return MQTTObject
     */
    @Bean
    public MQTTObject mqttObject() {

        MQTTObject mqttObject = new MQTTObject();
        mqttObject.setLifeTime(Integer.parseInt(lifeTime));
        mqttObject.setPort(port);
        mqttObject.setTopic(topic);
        mqttObject.setUrl(url);
        mqttObject.setUser(user);
        mqttObject.setPassword(password);

        return mqttObject;
    }

}
