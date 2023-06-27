package com.notenoughmail.tfcbits;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BitsHelper {

    public static double getOffsetPassengerRidingOffset(Entity passenger, Entity vehicle, float scale) {
        double offset = 0.0D;
        if (vehicle.getPassengers().size() > 1) {
            int i = vehicle.getPassengers().indexOf(passenger);
            if (i == 0) {
                offset += 0.2D * scale;
            } else {
                offset -= 0.6D * scale;
            }
        }
        return vehicle.getPassengersRidingOffset() + offset;
    }

    // Lifted from boats
    public static void clampRiderRotation(Entity entityToUpdate, Entity horse) {
        entityToUpdate.setYBodyRot(horse.getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - horse.getYRot());
        float f1 = Mth.clamp(f, -85.0F, 85.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(TFCBits.ID, path);
    }

    public static boolean entityCanSeeBlock(Level level, Entity entity, BlockPos pos, double xCenterOffset, double yCenterOffset, double zCenterOffset) {
        Vec3 eyePos = entity.getEyePosition();
        Vec3 blockPos = Vec3.atCenterOf(pos).add(xCenterOffset, yCenterOffset, zCenterOffset);
        return level.clip(new ClipContext(eyePos, blockPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).getType() != HitResult.Type.BLOCK;
    }
}