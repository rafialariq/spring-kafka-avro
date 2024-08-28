package kafka.producer.config;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import value.SOURCE.EXAMPLE.PAYMENT;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Value("${payment.source.registry.url}")
    private String registryUrl;

    @Bean
    public ProducerFactory<String, PAYMENT> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        config.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getProducer().getClientId());
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG , registryUrl);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, PAYMENT> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
