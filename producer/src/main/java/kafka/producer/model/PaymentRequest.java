package kafka.producer.model;

public record PaymentRequest(
        String orderId,
        Double amount,
        String paymentMethod
) {
}
