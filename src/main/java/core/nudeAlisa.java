package core;

import core.services.Commands;
import core.services.Properties;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import org.apache.log4j.PropertyConfigurator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class nudeAlisa {
    private static char prefix='+';
    public static GatewayDiscordClient client;
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        if (args.length==0) System.err.print("No argc passed(no token)");

        final GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build()
                .login()
                .block();
        Properties properties=new Properties(client);
        nudeAlisa.client=client;

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(Commands.commands.entrySet())
                                .filter(entry -> content.startsWith(prefix + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(Commands.altCommands.entrySet())
                                .filter(entry -> content.startsWith(entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();
        client.getEventDispatcher().on(ReactionAddEvent.class)
                .flatMap((Function<ReactionAddEvent, Publisher<?>>) ReactionListener::proceedReaction).next()
                .subscribe();


        client.onDisconnect().block();
    }
}
