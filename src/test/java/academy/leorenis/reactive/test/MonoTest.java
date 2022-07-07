package academy.leorenis.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Locale;

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


    @Test
    public void monoSubscriberConsumerComplete() {
        var nome = "John Doe";
        var mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(
                s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Finished")
        );

        log.info("---------------------");

        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();
    }


    @Test
    public void monoSubscriberConsumerSubscription() {
        var nome = "John Doe";
        var mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(
                s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Finished"),
                Subscription::cancel
        );

        log.info("---------------------");

        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoDoOnMethods() {
        var nome = "John Doe";
        var mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(s -> log.info("Subscribed {}", s))
                .doOnRequest(longNumber -> log.info("Request received, starting doing smb"))
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}", s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}", s))
                .doOnSuccess(s -> log.info("doOnSuccess Executed {}", s));

        mono.subscribe(
                s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Finished")
        );

        log.info("---------------------");

        /*StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();*/
    }


    @Test
    public void monoDoOnError() {
        var error = Mono.error(new IllegalArgumentException("Ops, Illegal argument"))
                .doOnError(e -> log.error("Error message: {}", e.getMessage()))
                .doOnNext(s -> log.info("Executing this doOnNext"))
                .log();

         StepVerifier.create(error)
                .expectError(IllegalArgumentException.class)
                .verify();

    }

    @Test
    public void monoDoOnErrorResume() {
        var error = Mono.error(new IllegalArgumentException("Ops, Illegal argument"))
                .doOnError(e -> log.error("Error message: {}", e.getMessage()))
                .onErrorResume(s -> {
                    log.info("Inside on error resume");
                    return Mono.just("John Mcfee");
                })
                .log();

        StepVerifier.create(error)
                .expectNext(IllegalArgumentException.class)
                .verifyComplete();

    }
}
