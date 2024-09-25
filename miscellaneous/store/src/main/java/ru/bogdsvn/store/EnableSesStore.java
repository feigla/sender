package ru.bogdsvn.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("ru.bogdsvn.store.daos")
@EntityScan("ru.bogdsvn.store.entities")
@EnableJpaRepositories("ru.bogdsvn.store.repositories")
public class EnableSesStore {
}
