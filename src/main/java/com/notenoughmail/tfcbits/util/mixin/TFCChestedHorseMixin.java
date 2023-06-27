package com.notenoughmail.tfcbits.util.mixin;

import com.notenoughmail.tfcbits.BitsHelper;
import com.notenoughmail.tfcbits.util.config.BitsServerConfig;
import net.dries007.tfc.common.entities.livestock.horse.TFCChestedHorse;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TFCChestedHorse.class, remap = false)
public abstract class TFCChestedHorseMixin extends AbstractChestedHorse {

    @Shadow(remap = false)
    public abstract ItemStack getChestItem();

    private boolean hasChestOrBarrel() {
        return !getChestItem().isEmpty();
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        if (BitsServerConfig.largeHorsesCanHoldMultiplePlayers.get() && !getPassengers().isEmpty()) { // Only modify logic if enabled and there is at least one passenger
                return (BitsServerConfig.largeChestedHorsesCanHoldMultipleWithChest.get() || !hasChestOrBarrel()) && ((TFCChestedHorse) (Object) this).getGeneticSize() >= BitsServerConfig.largeChestedHorseMultipleRidersSize.get() && getPassengers().size() < 2;
            }
        return super.canAddPassenger(passenger);
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            float offset = 0.0F;
            float standAnim0 = getStandAnim(0.0F); // Very dumb way to get standAnim0; Scales 0 -> 1
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    offset += Mth.lerp(standAnim0, 0.2F, 0.15F);
                } else {
                    offset -= Mth.lerp(standAnim0, 0.6F, 0.5F);
                }
            }
            Vec3 vec = (new Vec3(offset, 0.0D, 0.0D)).yRot(-getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F)); // Taken from the boat's code, WTF
            passenger.setPos(this.getX() + vec.x, this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset() + vec.y, this.getZ() + vec.z);
            if (standAnim0 > 0.0F) { // This is what vanilla does
                float f3 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
                float f4 = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
                float f5 = 0.7F * standAnim0;
                float f6 = 0.15F * standAnim0;
                passenger.setPos(this.getX() + (f5 * f3) + vec.x, this.getY() + BitsHelper.getOffsetPassengerRidingOffset(passenger, this, standAnim0) + passenger.getMyRidingOffset() + f6 + vec.y, this.getZ() - (f5 * f4) + vec.z);
                if (passenger instanceof LivingEntity living) {
                    living.yBodyRot = this.yBodyRot;
                }
            }
            BitsHelper.clampRiderRotation(passenger, this);
        }
    }

    @Redirect(method = {"mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"}, at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/entities/livestock/horse/TFCChestedHorse;isVehicle()Z"), remap = false)
    public boolean canRide(TFCChestedHorse instance) {
        if (BitsServerConfig.largeHorsesCanHoldMultiplePlayers.get() && !instance.getPassengers().isEmpty()) {
            return instance.getPassengers().size() >= 2 || instance.getGeneticSize() < BitsServerConfig.largeChestedHorseMultipleRidersSize.get(); // Return true only if it is too small or already has 2 players
        }
        return instance.isVehicle(); // Return default if this feature is disabled
    }

    protected TFCChestedHorseMixin(EntityType<? extends AbstractChestedHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
