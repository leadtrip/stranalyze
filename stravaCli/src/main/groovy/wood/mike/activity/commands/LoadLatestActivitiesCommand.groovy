package wood.mike.activity.commands

import jakarta.inject.Inject
import picocli.CommandLine
import reactor.core.publisher.Mono
import wood.mike.activity.clients.StoreClient

@CommandLine.Command(name = 'load')
final class LoadLatestActivitiesCommand implements Runnable {

    @Inject
    StoreClient client

    @Override
    void run() {
        client.loadLatestActivites()
                .doOnSuccess(System.out::println)
                .doOnError( ex -> println "Error: ${ex.message}" )
                .onErrorResume(ex-> Mono.empty())
                .block()
    }
}
