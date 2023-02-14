package wood.mike.activity.commands

import jakarta.inject.Inject
import picocli.CommandLine
import reactor.core.publisher.Mono
import wood.mike.activity.clients.StoreClient
import wood.mike.common.AbstractCliCommand

import java.time.Duration

@CommandLine.Command(name = 'latest')
class LatestActivityCommand extends AbstractCliCommand implements Runnable {

    @Inject
    StoreClient client

    @Override
    void run() {
        client.latestActivity()
                .doOnSuccess( resp -> prettyPrintJsonResponse(resp))
                .doOnError( ex -> println "Error: ${ex.message}" )
                .onErrorResume(ex-> Mono.empty())
                .block(Duration.ofSeconds(3))
    }
}
