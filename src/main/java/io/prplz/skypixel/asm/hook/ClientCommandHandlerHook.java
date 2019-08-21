package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;

public class ClientCommandHandlerHook {

    /**
     * @return true to cancel command processing and send the message to the server, false to proceed as normal
     */
    public static boolean executeCommand(String message) {
        return Skypixel.get().getSettings().forgeClientCommandFix.get() && !message.trim().startsWith("/");
    }
}
