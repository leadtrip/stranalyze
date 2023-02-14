package wood.mike.activity.commands

import picocli.CommandLine

@CommandLine.Command(
        name = "activity",
        description = "Strava activity related.",
        subcommands = [
            ActivityCountCommand.class,
            LatestActivityCommand.class
        ])
class ActivityBridgeCommand {
}
