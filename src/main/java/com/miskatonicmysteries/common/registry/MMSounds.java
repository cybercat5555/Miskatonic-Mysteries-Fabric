package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMSounds {
	//todo more entity sounds
	public static final SoundEvent AMBIENT_SCARY = new SoundEvent(new Identifier(Constants.MOD_ID, "ambient.scary"));
	public static final SoundEvent BLOCK_RESONATOR_AMBIENT = new SoundEvent(new Identifier(Constants.MOD_ID, "block.resonator.ambient"));
	public static final SoundEvent ENTITY_BYAKHEE_SADDLE = SoundEvents.ENTITY_HORSE_SADDLE;
	public static final SoundEvent ENTITY_HARROW_AMBIENT = new SoundEvent(new Identifier(Constants.MOD_ID, "entity.harrow.ambient"));
	public static final SoundEvent ENTITY_HARROW_DEATH = new SoundEvent(new Identifier(Constants.MOD_ID, "entity.harrow.death"));
	public static final SoundEvent ENTITY_HARROW_HURT = new SoundEvent(new Identifier(Constants.MOD_ID, "entity.harrow.hurt"));
	public static final SoundEvent ENTITY_HARROW_CHARGE = new SoundEvent(new Identifier(Constants.MOD_ID, "entity.harrow.charge"));
	public static final SoundEvent ITEM_GUN_GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "item.gun.gun_shot"));
	public static final SoundEvent ITEM_INCANTATION_YOG_INCANTATION_BOUND = new SoundEvent(new Identifier(Constants.MOD_ID, "item.incantation_yog.incantation_bound"));
	public static final SoundEvent ITEM_INFESTED_WHEAT_USE = new SoundEvent(new Identifier(Constants.MOD_ID, "item.infested_wheat.use"));
	public static final SoundEvent RANDOM_PLING = new SoundEvent(new Identifier(Constants.MOD_ID, "random.pling"));
	public static final SoundEvent RITE_RITE_TRIGGERED = new SoundEvent(new Identifier(Constants.MOD_ID, "rite.rite_triggered"));
	public static final SoundEvent RITE_SPOTLIGHT = new SoundEvent(new Identifier(Constants.MOD_ID, "rite.spotlight"));
	public static final SoundEvent RITE_TELEPORT = new SoundEvent(new Identifier(Constants.MOD_ID, "rite.teleport"));
	public static final SoundEvent RITE_VEIL_SPAWN = new SoundEvent(new Identifier(Constants.MOD_ID, "rite.veil_spawn"));
	public static final SoundEvent SPELL_SPELL_CAST = new SoundEvent(new Identifier(Constants.MOD_ID, "spell.spell_cast"));

	public static void init() {
		register(AMBIENT_SCARY);
		register(BLOCK_RESONATOR_AMBIENT);
		//register(ENTITY_BYAKHEE_SADDLE);
		register(ENTITY_HARROW_AMBIENT);
		register(ENTITY_HARROW_DEATH);
		register(ENTITY_HARROW_HURT);
		register(ENTITY_HARROW_CHARGE);
		register(ITEM_GUN_GUN_SHOT);
		register(ITEM_INCANTATION_YOG_INCANTATION_BOUND);
		register(ITEM_INFESTED_WHEAT_USE);
		register(RANDOM_PLING);
		register(RITE_RITE_TRIGGERED);
		register(RITE_SPOTLIGHT);
		register(RITE_TELEPORT);
		register(RITE_VEIL_SPAWN);
		register(SPELL_SPELL_CAST);
	}

	private static void register(SoundEvent event) {
		RegistryUtil.register(Registry.SOUND_EVENT, event.getId().getPath(), event);
	}
}
