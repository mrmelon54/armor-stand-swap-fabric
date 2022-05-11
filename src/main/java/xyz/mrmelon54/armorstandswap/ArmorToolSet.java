package xyz.mrmelon54.armorstandswap;

import net.minecraft.item.ItemStack;

public record ArmorToolSet(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
                           ItemStack mainHand, ItemStack offHand) {
}
