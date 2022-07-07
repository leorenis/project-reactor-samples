package academy.leorenis.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {

    /**
     * Mono represents a streams of one or void
     * */
    @Test
    public void monoSubscriber() {
        var nome = "John Doe";
        var mono = Mono.just(nome).log();

        mono.subscribe();

        log.info("---------------------");

        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();

        log.info("Mono {}", mono);
    }


    @Test
    public void monoSubscriberConsumer() {
        var nome = "John Doe";
        var mono = Mono.just(nome).log();

        mono.subscribe(s -> log.info("Value {}", s));

        log.info("---------------------");

        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError() {
        var nome = "John Doe";
        var mono = Mono.just(nome).map(s -> { throw new RuntimeException("Testing mono with error"); });

        mono.subscribe(s -> log.info("Nome {}", s), s -> log.error("Something bad happened"));
        mono.subscribe(s -> log.info("Nome {}", s), Throwable::printStackTrace);

        log.info("---------------------");

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
