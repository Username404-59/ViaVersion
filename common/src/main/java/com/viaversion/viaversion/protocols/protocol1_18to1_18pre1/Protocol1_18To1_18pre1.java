package com.viaversion.viaversion.protocols.protocol1_18to1_18pre1;

import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_18pre1to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.rewriter.TagRewriter;

public class Protocol1_18To1_18pre1 extends AbstractProtocol<ClientboundPackets1_18, ClientboundPackets1_18, ServerboundPackets1_17, ServerboundPackets1_17> {
	public Protocol1_18To1_18pre1() {
		super(ClientboundPackets1_18.class, ClientboundPackets1_18.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
	}

	@Override
	protected void registerPackets() {
		final TagRewriter tagRewriter = new TagRewriter(this);
		tagRewriter.registerGeneric(ClientboundPackets1_18.TAGS);
		tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:lava_pool_stone_cannot_replace", "minecraft:big_dripleaf_placeable",
				"minecraft:wolves_spawnable_on", "minecraft:rabbits_spawnable_on", "minecraft:polar_bears_spawnable_on_in_frozen_ocean", "minecraft:parrots_spawnable_on",
				"minecraft:mooshrooms_spawnable_on", "minecraft:goats_spawnable_on", "minecraft:foxes_spawnable_on", "minecraft:axolotls_spawnable_on", "minecraft:animals_spawnable_on",
				"minecraft:azalea_grows_on", "minecraft:azalea_root_replaceable", "minecraft:replaceable_plants", "minecraft:terracotta");
		tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:dirt", "minecraft:terracotta");
	}
}
