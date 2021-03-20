package core.services.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.common.util.Snowflake;
import discord4j.voice.VoiceConnection;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public final class GuildAudioManager {

    private static final Map<Snowflake, GuildAudioManager> MANAGERS = new ConcurrentHashMap<>();
    private static final Map<Snowflake, VoiceConnection> connectionMap = new ConcurrentHashMap<>();

    public static GuildAudioManager of(final Snowflake id) {
        return MANAGERS.computeIfAbsent(id, ignored -> new GuildAudioManager());
    }
    private GuildAudioManager() {
        player = LavaAudioProvider.getPlayer();
        scheduler = new TrackScheduler(player);
        provider = new LavaAudioProvider(player);
        player.addListener(scheduler);

    }

    public static void saveConnection(VoiceConnection connection) {
        connectionMap.putIfAbsent(connection.getGuildId(), connection);
    }

    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final LavaAudioProvider provider;

    public static VoiceConnection getConnection(Snowflake id) {
        return connectionMap.get(id);
    }

    public Mono<Void> disconnect(Snowflake id) {
        VoiceConnection connection = connectionMap.get(id);
        if (connection == null || !connection.isConnected().block()) return Mono.justOrEmpty("none").then();
        scheduler.getQueue().clear();
        player.stopTrack();
        scheduler.playlist.clear();
        scheduler.skip();
        scheduler.setloop(false);
        connectionMap.remove(id);
        return connection.disconnect();
    }


    // getters
}