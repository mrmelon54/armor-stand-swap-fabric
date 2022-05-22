package xyz.mrmelon54.armorstandswap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ArmorToolSet {
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack mainHand;
    private final ItemStack offHand;

    public ArmorToolSet(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
                        ItemStack mainHand, ItemStack offHand) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    private boolean helmetBind;
    private boolean chestBind;
    private boolean leggingsBind;
    private boolean bootBind;

    public static void checkArmorBinding(ArmorToolSet a, ArmorToolSet b) {
        a.helmetBind = doBinding(a.helmet, b.helmet);
        b.helmetBind = a.helmetBind;
        a.chestBind = doBinding(a.chestplate, b.chestplate);
        b.chestBind = a.chestBind;
        a.leggingsBind = doBinding(a.leggings, b.leggings);
        b.leggingsBind = a.leggingsBind;
        a.bootBind = doBinding(a.boots, b.boots);
        b.bootBind = a.bootBind;
    }

    private static boolean doBinding(ItemStack a, ItemStack b) {
        return EnchantmentHelper.hasBindingCurse(a) || EnchantmentHelper.hasBindingCurse(b);
    }

    public void transfer(Entity entity) {
        if (!helmetBind) entity.equipStack(EquipmentSlot.HEAD, helmet);
        if (!chestBind) entity.equipStack(EquipmentSlot.CHEST, chestplate);
        if (!leggingsBind) entity.equipStack(EquipmentSlot.LEGS, leggings);
        if (!bootBind) entity.equipStack(EquipmentSlot.FEET, boots);
        entity.equipStack(EquipmentSlot.MAINHAND, mainHand);
        entity.equipStack(EquipmentSlot.OFFHAND, offHand);
    }
}
