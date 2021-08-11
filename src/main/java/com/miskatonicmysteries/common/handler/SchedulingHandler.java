package com.miskatonicmysteries.common.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SchedulingHandler {
	private static List<Consumer<MinecraftServer>> scheduledTasks = new ArrayList<>();

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(SchedulingHandler::tick);
		ServerLifecycleEvents.SERVER_STOPPING.register(SchedulingHandler::stop);
	}

	private static void tick(MinecraftServer server) {
		for (Consumer<MinecraftServer> scheduledTask : scheduledTasks) {
			scheduledTask.accept(server);
		}
		scheduledTasks.clear();
	}

	private static void stop(MinecraftServer server) {
		scheduledTasks.clear();
	}

	public static void addTask(Consumer<MinecraftServer> task) {
		scheduledTasks.add(task);
	}
}
