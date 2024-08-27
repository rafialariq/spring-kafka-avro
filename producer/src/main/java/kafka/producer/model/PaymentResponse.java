package kafka.producer.model;

import lombok.Builder;

@Builder
public record PaymentResponse(
   String status,
   String key,
   String value,
   Integer partitions,
   Long timestamp
) {}
