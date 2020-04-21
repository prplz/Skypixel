package io.prplz.skypixel.asm.hook;

import com.google.common.collect.Collections2;
import io.prplz.skypixel.Skypixel;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Collection;

public class GuiPlayerTabOverlayHook {

    private static final Skypixel skypixel = Skypixel.get();

    /**
     * Wraps calls to {@link NetHandlerPlayClient#getPlayerInfoMap()} in
     * {@link GuiPlayerTabOverlay#renderPlayerlist(int, Scoreboard, ScoreObjective)}
     */
    public static Collection<NetworkPlayerInfo> getPlayerInfoMap(Collection<NetworkPlayerInfo> playerInfoMap) {
        if (skypixel.isInSkyblock() && skypixel.getSettings().hideNpcsOnTab.get()) {
            // Skyblock NPCs have version 2 UUIDs
            return Collections2.filter(playerInfoMap, player -> player.getGameProfile().getId().version() != 2);
        } else {
            return playerInfoMap;
        }
    }
}
