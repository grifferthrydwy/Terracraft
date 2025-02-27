package io.github.simplycmd.terracraft.util;

import java.util.Optional;
import java.util.function.Function;

import dev.emi.trinkets.api.TrinketsApi;
import io.github.simplycmd.terracraft.items.accessories.AccessoryItem;
import io.github.simplycmd.terracraft.items.accessories.CombinationAccessoryItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import io.github.simplycmd.terracraft.items.accessories.DoubleJumpAccessoryItem;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class AccessoryUtil {
    /**
     * Checks if the given AccessoryItem is equipped on the given LivingEntity.
     * @param entity The LivingEntity to check.
     * @param accessory The AccessoryItem to check.
     * @return The outcome of the check.
     */
    public static boolean isItemEquipped(LivingEntity entity, AccessoryItem accessory) {
        return ifEquipped(entity, "accessory", (item) -> 
            Optional.of(item instanceof AccessoryItem && item == accessory)
        ).map(bool -> (Boolean) bool).orElse(false); // Trust me, I'm just as confused as you are.
    }

    /**
     * Checks if the given AccessoryItem/ is equipped on the given LivingEntity.
     * @param entity The LivingEntity to check.
     * @param power The Class to check against.
     * @return The outcome of the check.
     */
    public static boolean isPowerEquipped(LivingEntity entity, Class<? extends AccessoryItem> power) {
        return ifEquipped(entity, "accessory", (item) -> 
            Optional.of(item instanceof AccessoryItem && testHasPowers((AccessoryItem) item, power))
        ).map(bool -> (Boolean) bool).orElse(false);
    }

    /**
     * Executes the given Function on each item in the given slot type group.
     * @param entity The LivingEntity to check.
     * @param slotType String name of the SlotType.
     * @param consumer The Function to execute on each Item.
     * @return The first Function returned if there was an Item, otherwise Optional.empty().
     */
    public static Optional<?> ifEquipped(LivingEntity entity, String slotType, Function<Item, Optional<?>> function) {
        var component = TrinketsApi.getTrinketComponent((LivingEntity) entity);
        if (component.isPresent()) {
            for (var equipped : component.get().getAllEquipped()) {
                if (equipped.getLeft().inventory().getSlotType().getName().equals(slotType)) {
                    return function.apply(equipped.getRight().getItem());
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Tests if the given accessory isof/has the given class.
     * @param accessoryItem The AccessoryItem to test.
     * @param testAgainstClass The Class to check against.
     * @return The outcome of the test.
     */
    public static boolean testHasPowers(AccessoryItem accessoryItem, Class<? extends AccessoryItem> testAgainstClass) {
        // Check if it's a CombinationAccessoryItem
        if (testCombinationContains(accessoryItem, testAgainstClass)) {
            return true;
        }
        // Check if it's an instance of the given AccessoryItem
        if (accessoryItem.getClass() == testAgainstClass) {
            return true;
        }
        return false;
    }

    private static boolean testCombinationContains(AccessoryItem combinationItem, Class<? extends AccessoryItem> testAgainstClass) {
        if (combinationItem instanceof CombinationAccessoryItem) {
            for (var ingredient : ((CombinationAccessoryItem) combinationItem).getIngredients()) {
                if (ingredient.getClass() == testAgainstClass) {
                    return true;
                }
            }
        }
        return false;
    }

    public static DoubleJumpAccessoryItem getDJ(LivingEntity entity) {
        var l = -1;
        var component = TrinketsApi.getTrinketComponent((LivingEntity) entity);
        if (component.isPresent()) {
            for (var equipped : component.get().getAllEquipped()) {
                if (equipped.getLeft().inventory().getSlotType().getName().equals("accessory")
                        && equipped.getRight().getItem() instanceof DoubleJumpAccessoryItem o) {
                    l = Math.max(DoubleJumpAccessoryItem.power().get(o), l);
                }
            }
        }
        if (!DoubleJumpAccessoryItem.power().containsValue(l)) return null;
        return DoubleJumpAccessoryItem.getFromPower(l);
    }

    public static DoubleJumpAccessoryItem[] getDJList(LivingEntity entity) {
        var l = -1;
        var d = new ArrayList<DoubleJumpAccessoryItem>();
        var component = TrinketsApi.getTrinketComponent((LivingEntity) entity);
        if (component.isPresent()) {
            for (var equipped : component.get().getAllEquipped()) {
                if (equipped.getLeft().inventory().getSlotType().getName().equals("accessory")
                        && equipped.getRight().getItem() instanceof DoubleJumpAccessoryItem o) {
                    d.add(o);
                }
            }
        }
        return d.toArray(new DoubleJumpAccessoryItem[0]);
    }


    public static DoubleJumpAccessoryItem getBestActiveDJ(LivingEntity entity) {
        @Nonnull
        var a = getDJList(entity);
        var d = new ArrayList<DoubleJumpAccessoryItem>();
        for (DoubleJumpAccessoryItem doubleJumpAccessoryItem : a) {
            if (((LivingEntityExtension)entity).terracraft$getJumpCounter().get(doubleJumpAccessoryItem) > 0) {
                d.add(doubleJumpAccessoryItem);
            }
        }

        DoubleJumpAccessoryItem l = null;
        for (DoubleJumpAccessoryItem doubleJumpAccessoryItem : d) {
            if (l == null || doubleJumpAccessoryItem.getPower() > l.getPower()) {
                l = doubleJumpAccessoryItem;
            }
        }
        return l;
    }
}
