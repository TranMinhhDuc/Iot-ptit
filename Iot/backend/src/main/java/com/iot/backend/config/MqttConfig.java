package com.iot.backend.config;


import com.iot.backend.DTO.MeasureDTO;
import com.iot.backend.Service.DashboardService;
import com.iot.backend.Service.MeasureService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Autowired
    MeasureService measureService;
    @Autowired
    DashboardService dashboardService;

    private String broker = "tcp://192.168.1.4:1885";
    private String clientId = "web";
    public static IMqttClient client;

    private static final String subTopic = "sensor/esp32";
    private static final String pubTopic = "control/esp32";

    public MqttConfig() {
        try {
            client = new MqttClient(broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName("client");
            options.setPassword("00000".toCharArray());

            // Connect to the broker
            client.connect(options);

        } catch (MqttException e) {
            System.out.println("Error during MQTT connection or subscription");
            e.printStackTrace();
        }
    }

    @Bean
    public IMqttClient subscribeToTopic() throws MqttException {

        MeasureDTO newMeasurementHistory = new MeasureDTO();
        client.subscribe(subTopic, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String[] payload = new String(message.getPayload()).split(",");
                if (payload.length > 1) {
                    newMeasurementHistory.setTemperature(Float.parseFloat(payload[0].trim()));
                    newMeasurementHistory.setHumidity(Float.parseFloat(payload[1].trim()));
                    newMeasurementHistory.setBright(Float.parseFloat(payload[2].trim()));
                    newMeasurementHistory.setMeasureDate();
                    newMeasurementHistory.setMeasureTime();
                    dashboardService.createMeasurementHistory(newMeasurementHistory);
                } else {
                    dashboardService.createDeviceHistory(payload[0].trim());
                }
            }
        });
        return client;
    }

    public static void publishMessage(String payload) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());

            client.publish(pubTopic, message);

            System.out.println("Message published to topic: " + pubTopic);
        } catch (MqttException e) {
            System.out.println("Error during message publish");
            e.printStackTrace();
        }
    }

}