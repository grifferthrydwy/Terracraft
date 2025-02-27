package io.github.simplycmd.terracraft.items.accessories;

import java.util.UUID;

import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import dev.emi.trinkets.api.TrinketItem;
import io.github.simplycmd.terracraft.util.AccessoryUtil;
import lombok.Getter;

public class AccessoryItem extends TrinketItem {
    @Getter
    private final UUID accessoryUuid;

    public AccessoryItem(FabricItemSettings settings) {
        super(settings.maxCount(1));
        this.accessoryUuid = UUID.randomUUID();
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return !AccessoryUtil.isItemEquipped(entity, this);
	}
}
