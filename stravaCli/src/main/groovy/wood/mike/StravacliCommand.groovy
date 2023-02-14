package wood.mike

import io.micronaut.configuration.picocli.PicocliRunner
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import wood.mike.activity.commands.ActivityBridgeCommand

@Command(name = 'stravacli', description = 'CLI onto strava services',
        mixinStandardHelpOptions = true, subcommands = [ ActivityBridgeCommand.class ] )
class StravacliCommand implements Runnable {

    @Option(names = ['-v', '--verbose'], description = '...')
    boolean verbose

    static void main(String[] args) throws Exception {
        PicocliRunner.run(StravacliCommand.class, args)
    }

    void run() {
        // business logic here
        if (verbose) {
            println "Hi!"
        }
    }
}
