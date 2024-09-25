package ru.bogdsvn.ses.worker.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.bogdsvn.rabbitmq.EnableSesRabbitMq;
import ru.bogdsvn.ses.reddis.EnableSesReddis;
import ru.bogdsvn.store.EnableSesStore;

@Import({
        EnableSesStore.class,
        EnableSesRabbitMq.class,
        EnableSesReddis.class
})
@EnableScheduling
@Configuration
public class ImportConfig {
}
