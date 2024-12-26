package com.yoger.chat_service.common.config;

import com.yoger.chat_service.event.ChatUserNotPresentEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Configuration
    @RequiredArgsConstructor
    public static class KafkaAdminConfig {

        private final KafkaAdminConfigValue configValue;

        @ConfigurationProperties(prefix = "kafka.admin")
        public record KafkaAdminConfigValue(@NotBlank String bootstrapServers) {
        }

        @Bean
        public KafkaAdmin kafkaAdmin() {
            HashMap<String, Object> config = new HashMap<>();
            config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configValue.bootstrapServers);

            return new KafkaAdmin(config);
        }

    }

    @Configuration
    @RequiredArgsConstructor
    public static class KafkaProducerConfig {

        private final KafkaProducerConfigValue configValue;

        @ConfigurationProperties(prefix = "kafka.producer.chat")
        public record KafkaProducerConfigValue(@NotBlank String bootstrapServers,
                                               @NotBlank String keySerializer, @NotBlank String valueSerializer,
                                               @NotNull Integer retries, @NotBlank String acks,
                                               @NotNull Integer bufferMemory, @NotNull Integer lingerMs,
                                               @NotNull Integer batchSize, @NotNull Boolean enableIdempotence,
                                               @NotNull Integer requestTimeoutMs, @NotNull Integer deliveryTimeoutMs) {
        }

        @Bean(name = "chatProducerFactory")
        public DefaultKafkaProducerFactory<String, ChatUserNotPresentEvent> producerFactory() {
            HashMap<String, Object> config = new HashMap<>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configValue.bootstrapServers);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, configValue.keySerializer);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            config.put(ProducerConfig.RETRIES_CONFIG, configValue.retries);
            config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, configValue.bufferMemory);
            config.put(ProducerConfig.LINGER_MS_CONFIG, configValue.lingerMs);
            config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, configValue.enableIdempotence);
            config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, configValue.requestTimeoutMs);
            config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, configValue.deliveryTimeoutMs);

            return new DefaultKafkaProducerFactory<>(config);
        }

        @Bean
        public KafkaTemplate<String, ChatUserNotPresentEvent> chatUserNotPresentKafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }

    }

    @Configuration
    @RequiredArgsConstructor
    public static class KafkaConsumerConfig {

        private final KafkaConsumerConfigValue configValue;

        @ConfigurationProperties(prefix = "kafka.consumer.notification")
        public record KafkaConsumerConfigValue(@NotBlank String bootstrapServers,
                                               @NotBlank String keyDeserializer,
                                               @NotBlank String valueDeserializer,
                                               @NotBlank String autoOffsetReset, @NotNull Boolean enableAutoCommit,
                                               @NotBlank String groupId, @NotBlank String isolationLevel,
                                               @NotNull Integer maxPollRecords, @NotNull Integer fetchMinSize,
                                               @NotNull Integer fetchMaxWait, @NotNull Integer heartbeatInterval,
                                               @NotNull Integer maxPollInterval) {
        }

        @Bean
        public ConsumerFactory<String, ChatUserNotPresentEvent> chatUserNotPresentEventConsumerFactory() {
            HashMap<String, Object> config = new HashMap<>();
            config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configValue.bootstrapServers);
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configValue.autoOffsetReset);
            config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, configValue.enableAutoCommit);
            config.put(ConsumerConfig.GROUP_ID_CONFIG, configValue.groupId);
            config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, configValue.maxPollRecords);
            config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, configValue.fetchMinSize);
            config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, configValue.fetchMaxWait);
            config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, configValue.heartbeatInterval);

            return new DefaultKafkaConsumerFactory<>(
                    config,
                    new StringDeserializer(),
                    new JsonDeserializer<>(ChatUserNotPresentEvent.class,false)
            );
        }

        @Bean(name = "chatUserNotPresentEventContainerFactory")
        public ConcurrentKafkaListenerContainerFactory<String, ChatUserNotPresentEvent> chatUserNotPresentEventContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, ChatUserNotPresentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(chatUserNotPresentEventConsumerFactory());
            return factory;
        }

    }


}
