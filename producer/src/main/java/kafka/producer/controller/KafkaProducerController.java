package kafka.producer.controller;

import kafka.producer.model.BaseDataResponse;
import kafka.producer.model.PaymentRequest;
import kafka.producer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class KafkaProducerController {

    private final KafkaProducerService producerService;

    @PostMapping("/producer/send")
    public ResponseEntity<Object> sendPaymentRequest(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(BaseDataResponse.builder()
                .data(producerService.sendMessage(request))
                .build());
    }

}
