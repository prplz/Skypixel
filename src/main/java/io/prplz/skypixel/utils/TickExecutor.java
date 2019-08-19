package io.prplz.skypixel.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class TickExecutor implements Executor {

    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    @Override
    public void execute(Runnable task) {
        tasks.add(task);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Runnable task;
            while ((task = tasks.poll()) != null) {
                task.run();
            }
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
