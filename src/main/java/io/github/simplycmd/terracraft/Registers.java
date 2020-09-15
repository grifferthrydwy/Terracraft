package io.github.simplycmd.terracraft;

import java.util.HashMap;
import java.util.List;

import io.github.simplycmd.terracraft.armor.ModArmor;
import io.github.simplycmd.terracraft.items.ItemTooltips;
import io.github.simplycmd.terracraft.items.ModItem;
import io.github.simplycmd.terracraft.tools.ModAxe;
import io.github.simplycmd.terracraft.tools.ModHoe;
import io.github.simplycmd.terracraft.tools.ModPickaxe;
import io.github.simplycmd.terracraft.tools.ModShovel;
import io.github.simplycmd.terracraft.tools.ModSword;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Registers {
    static HashMap<Item, List<Text>> tooltip_list = new HashMap<Item, List<Text>>();

    public Item registerNew(Float type, String name, String tooltip) {
        // If you are registering an item
        return registerNew(type, name, tooltip, null, null, null, null, null, null);
    }

    public Item registerNew(Float type, String name, String tooltip, Material tool_material) {
        // If you are registering a block
        return registerNew(type, name, tooltip, null, null, null, null, null, tool_material);
    }

    public Item registerNew(Float type, String name, String tooltip, ToolMaterial tool_material, Integer attack_damage,
            Float attack_speed) {
        // If you are registering a tool
        return registerNew(type, name, tooltip, tool_material, null, attack_damage, attack_speed, null, null);
    }

    public Item registerNew(Float type, String name, String tooltip, ArmorMaterial armor_material, String set_bonus) {
        // If you are registering a piece of armor
        return registerNew(type, name, tooltip, null, armor_material, null, null, set_bonus, null);
    }

    public Item registerNew(Float type, String name, String tooltip, ToolMaterial tool_material,
            ArmorMaterial armor_material, Integer attack_damage, Float attack_speed, String set_bonus, Material block_material) {

        ItemGroup group = ItemGroup.MISC;
        Integer stack = 64;
        EquipmentSlot slot = EquipmentSlot.CHEST;
        if (attack_speed != null) {
            attack_speed = attack_speed - 4.0F; // Translate actual attack speed to weird code attack speed
        }

        // Determine stack size
        /*
         * if ((type <= Types.WEAPON_WHIP) || (type >= Types.ARMOR_HELMET && type <=
         * Types.DYE && type != Types.COIN && type != Types.GEM) || (type >= 12.0F &&
         * type < 15.0F)) { stack = 1; }
         */

        // Determine item group and slot (if armor)
        if (type <= Types.TOOL_SHOVEL) {
            group = ItemGroup.TOOLS;
        }
        if (type >= Types.WEAPON_SWORD && type <= Types.ARMOR_BOOTS) {
            group = ItemGroup.COMBAT;
        }
        if ((type >= Types.FURNITURE && type <= Types.CRAFTING_STATION)) {
            group = ItemGroup.DECORATIONS;
        }
        if ((type >= Types.COIN) || (type >= Types.ACCESSORY) || (type >= Types.PAINT && type <= Types.DYE) || (type >= Types.PET)) {
            group = ItemGroup.MISC;
        }
        if (type >= Types.POTION_RECOVERY && type <= Types.POTION_OTHER) {
            group = ItemGroup.BREWING;
        }
        if (type == Types.ORE || type == Types.BAR || type == Types.BLOCK || type == Types.WALL) {
            group = ItemGroup.BUILDING_BLOCKS;
        }
        if (type == Types.ARMOR_HELMET) {
            slot = EquipmentSlot.HEAD;
        } else if (type == Types.ARMOR_CHESTPLATE) {
            slot = EquipmentSlot.CHEST;
        } else if (type == Types.ARMOR_LEGGINGS) {
            slot = EquipmentSlot.LEGS;
        } else if (type == Types.ARMOR_BOOTS) {
            slot = EquipmentSlot.FEET;
        }

        // Determine type and create item
        if (type == Types.TOOL_AXE) {
            final ModAxe item = new ModAxe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        } else if (type == Types.TOOL_HOE) {
            final ModHoe item = new ModHoe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        } else if (type == Types.TOOL_PICKAXE) {
            final ModPickaxe item = new ModPickaxe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        } else if (type == Types.TOOL_SHOVEL) {
            final ModShovel item = new ModShovel(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        } else if (type == Types.WEAPON_SWORD) {
            final ModSword item = new ModSword(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        } else if (type >= Types.ARMOR_HELMET && type <= Types.ARMOR_BOOTS) {
            final ModArmor item = new ModArmor(armor_material, slot, (new Item.Settings()).group(group));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip, set_bonus);
            return item;
        } else if (type == Types.ORE || type == Types.BAR || type == Types.BLOCK) {
            final Block block = new Block(FabricBlockSettings.of(Material.METAL));
            final BlockItem item = new BlockItem(block, new Item.Settings().group(group).maxCount(stack));
            Registry.register(Registry.BLOCK, new Identifier(Terracraft.MOD_ID, name), block);
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip, set_bonus);
            return item;
        } else {
            final ModItem item = new ModItem(new Item.Settings().group(group).maxCount(stack));
            Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            ItemTooltips.Tooltipper(type, GetTooltip(item), tooltip);
            return item;
        }
    }

    public static void PutTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip_list.put(stack.getItem(), tooltip);
    }

    public List<Text> GetTooltip(Item item) {
        return tooltip_list.get(item);
    }
}