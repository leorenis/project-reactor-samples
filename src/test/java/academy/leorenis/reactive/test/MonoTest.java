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

        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();

        log.info("Mono {}", mono);
        log.info("Setup");
    }
}
