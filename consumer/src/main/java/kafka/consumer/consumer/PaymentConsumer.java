package kafka.consumer.consumer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import value.SOURCE.EXAMPLE.PAYMENT;

@Slf4j
@Component
@Observed
@RequiredArgsConstructor
public class PaymentConsumer {

    @KafkaListener(
            topics = {"${payment.source.topic:PAYMENT_DEMO}"},
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePayment(
        PAYMENT record,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info(String.format("Consumed event from topic %s: key = %-10s value = %s", topic, key, record));
    }

}
