/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2022 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;

import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<Protocol1_19To1_18_2> {

    public InventoryPackets(Protocol1_19To1_18_2 protocol) {
        super(protocol);
    }

    @Override
    public void registerPackets() {
        registerSetCooldown(ClientboundPackets1_18.COOLDOWN);
        registerWindowItems1_17_1(ClientboundPackets1_18.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, Type.FLAT_VAR_INT_ITEM);
        registerTradeList(ClientboundPackets1_18.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        registerSetSlot1_17_1(ClientboundPackets1_18.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        registerAdvancements(ClientboundPackets1_18.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        registerEntityEquipmentArray(ClientboundPackets1_18.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);

        protocol.registerClientbound(ClientboundPackets1_18.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.UNSIGNED_BYTE); // Window Id
                map(Type.SHORT); // Property
                map(Type.SHORT); // Value
                handler(wrapper -> {
                    short property = wrapper.get(Type.SHORT, 0);
                    if (property >= 4 && property <= 6) { // Enchantment id
                        short enchantmentId = wrapper.get(Type.SHORT, 1);
                        if (enchantmentId >= 12) { // soul_speed added with id 11
                            wrapper.set(Type.SHORT, 1, ++enchantmentId);
                        }
                    }
                });
            }
        });

        protocol.registerClientbound(ClientboundPackets1_18.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // Particle id
                map(Type.BOOLEAN); // Override limiter
                map(Type.DOUBLE); // X
                map(Type.DOUBLE); // Y
                map(Type.DOUBLE); // Z
                map(Type.FLOAT); // Offset X
                map(Type.FLOAT); // Offset Y
                map(Type.FLOAT); // Offset Z
                map(Type.FLOAT); // Max speed
                map(Type.INT); // Particle Count
                handler(wrapper -> {
                    final int id = wrapper.get(Type.INT, 0);
                    final ParticleMappings mappings = protocol.getMappingData().getParticleMappings();
                    if (id == mappings.id("vibration")) {
                        wrapper.read(Type.POSITION1_14);

                        String resourceLocation = wrapper.passthrough(Type.STRING);
                        if (resourceLocation.startsWith("minecraft:")) {
                            resourceLocation = resourceLocation.substring(10);
                        }
                        if (resourceLocation.equals("entity")) {
                            wrapper.passthrough(Type.VAR_INT); // Entity id
                            wrapper.write(Type.FLOAT, 0F); // Y offset
                        }
                    }
                });
                handler(getSpawnParticleHandler(Type.FLAT_VAR_INT_ITEM));
            }
        });

        registerClickWindow1_17_1(ServerboundPackets1_17.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        registerCreativeInvAction(ServerboundPackets1_17.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);

        new RecipeRewriter1_16(protocol).registerDefaultHandler(ClientboundPackets1_18.DECLARE_RECIPES);
    }
}
