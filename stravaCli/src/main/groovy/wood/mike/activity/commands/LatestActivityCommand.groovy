package wood.mike.activity.commands

import jakarta.inject.Inject
import picocli.CommandLine
import reactor.core.publisher.Mono
import wood.mike.activity.clients.StoreClient

import java.time.Duration

@CommandLine.Command(name = 'latest')
class LatestActivityCommand implements Runnable {

    @Inject
    StoreClient client

    @Override
    void run() {
        client.latestActivity()
                .doOnSuccess(System.out::println)
                .doOnError( ex -> println "Error: ${ex.message}" )
                .onErrorResume(ex-> Mono.empty())
                .block(Duration.ofSeconds(3))
    }
}
