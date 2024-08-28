package kafka.producer.service;

import kafka.producer.config.KafkaProducerConfig;
import kafka.producer.model.PaymentRequest;
import kafka.producer.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import value.SOURCE.EXAMPLE.PAYMENT;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    @Value("${payment.source.topic:PAYMENT_DEMO}")
    private String topic;

    private final KafkaProducerConfig kafkaConfig;

    private final KafkaTemplate<String, PAYMENT> kafkaTemplate;

    public PaymentResponse sendMessage(PaymentRequest request) {

        String paymentId = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();
        String key = "PAY-" + paymentId;

        PAYMENT paymentRecord = PAYMENT.newBuilder()
                .setPAYMENTID(paymentId)
                .setORDERID(request.orderId())
                .setAMOUNT(request.amount())
                .setPAYMENTDATE(date)
                .setPAYMENTMETHOD(request.paymentMethod())
                .build();

        CompletableFuture<SendResult<String, PAYMENT>> future = kafkaTemplate.send(topic, key, paymentRecord);

        try {
            SendResult<String, PAYMENT> result = future.get();
            log.info(String.format("Produced event to topic %s: key = %-10s value = %s", topic, key, paymentRecord));
            return PaymentResponse.builder()
                    .status("Successfull send payment request")
                    .key(result.getProducerRecord().key())
                    .value(result.getProducerRecord().value().toString())
                    .partitions(result.getRecordMetadata().partition())
                    .timestamp(result.getRecordMetadata().timestamp())
                    .build();
        } catch (InterruptedException | ExecutionException | CancellationException ex) {
            log.error("Error sending message to Kafka", ex);
            throw new RuntimeException("Failed to send message to Kafka", ex);
        }
    }
}
