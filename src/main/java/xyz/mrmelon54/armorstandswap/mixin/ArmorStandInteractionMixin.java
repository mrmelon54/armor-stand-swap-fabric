package xyz.mrmelon54.armorstandswap.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;
import xyz.mrmelon54.armorstandswap.ArmorToolSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandInteractionMixin extends Entity {
    public ArmorStandInteractionMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(final EquipmentSlot equipmentSlot);

    @Shadow
    protected abstract void setShowArms(boolean showArms);

    @Shadow
    public abstract boolean shouldShowArms();

    @Shadow
    protected abstract void setHideBasePlate(boolean hideBasePlate);

    @Shadow
    public abstract boolean shouldHideBasePlate();

    @Shadow
    public abstract boolean shouldRender(double distance);

    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    private void interactAt(final PlayerEntity player, final Vec3d vec3d, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        ItemStack heldStack = player.getMainHandStack();
        Item heldStackItem = heldStack.getItem();

        // Ignore invisible armor stands
        if (this.isInvisible()) return;
        if (player.isSneaking() && hand == Hand.OFF_HAND) {
            info.setReturnValue(ActionResult.FAIL);
            return;
        }

        // toggling arms and base
        if (player.isSneaking() && hand == Hand.MAIN_HAND && heldStackItem == Items.STICK || heldStackItem == Items.STONE_SLAB) {
            if (heldStackItem == Items.STICK) this.setShowArms(!this.shouldShowArms());
            else this.setHideBasePlate(!this.shouldHideBasePlate());

            info.setReturnValue(ActionResult.FAIL);
            return;
        }

        // swap inventory
        if (player.isSneaking() && hand == Hand.MAIN_HAND) {
            ArmorToolSet armorStandSet = new ArmorToolSet(
                    getEquippedStack(EquipmentSlot.HEAD),
                    getEquippedStack(EquipmentSlot.CHEST),
                    getEquippedStack(EquipmentSlot.LEGS),
                    getEquippedStack(EquipmentSlot.FEET),
                    getEquippedStack(EquipmentSlot.MAINHAND),
                    getEquippedStack(EquipmentSlot.OFFHAND)
            );

            ArmorToolSet playerSet = new ArmorToolSet(
                    player.getEquippedStack(EquipmentSlot.HEAD),
                    player.getEquippedStack(EquipmentSlot.CHEST),
                    player.getEquippedStack(EquipmentSlot.LEGS),
                    player.getEquippedStack(EquipmentSlot.FEET),
                    player.getEquippedStack(EquipmentSlot.MAINHAND),
                    player.getEquippedStack(EquipmentSlot.OFFHAND)
            );

            ArmorToolSet.checkArmorBinding(armorStandSet, playerSet);
            armorStandSet.transfer(player);
            playerSet.transfer((ArmorStandEntity) (Object) this);

            info.setReturnValue(ActionResult.FAIL);
        }
    }
}