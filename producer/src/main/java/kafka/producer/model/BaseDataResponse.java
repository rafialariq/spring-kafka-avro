package kafka.producer.model;

import lombok.Builder;

@Builder
public record BaseDataResponse<T>(T data) {
}
