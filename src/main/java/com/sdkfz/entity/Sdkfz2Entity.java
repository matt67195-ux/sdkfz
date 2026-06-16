package com.sdkfz.entity;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoEntity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import io.netty.buffer.Unpooled;

import com.sdkfz.world.inventory.InventoryGuiMenu;
import com.sdkfz.procedures.ShiftEntityTickUpdateProcedure;
import com.sdkfz.procedures.SdkfzdeathProcedure;
import com.sdkfz.procedures.Sdkfz2SolidBoundingBoxConditionProcedure;
import com.sdkfz.procedures.FuelGenerationProcedure;
import com.sdkfz.procedures.FuelBucketOilUseProcedure;
import com.sdkfz.init.SdkfzModEntities;

import java.util.List;

public class Sdkfz2Entity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.STRING);
    private static final boolean MOB_DRIVER_ALLOWED = false;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean swinging;
    private boolean lastloop;
    private long lastSwing;
    public String animationprocedure = "empty";
    public float currentSpeed = 0;

    public Sdkfz2Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(SdkfzModEntities.SDKFZ_2.get(), world);
    }

    public Sdkfz2Entity(EntityType<Sdkfz2Entity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setMaxUpStep(1f);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(TEXTURE, "sdkfz2");
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(TEXTURE);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        Entity entity = this;
        Level world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        return Sdkfz2SolidBoundingBoxConditionProcedure.execute(world, x, y, z);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.2;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("sdkfz:engine")), 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE)) return false;
        if (source.getDirectEntity() instanceof AbstractArrow) return false;
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud) return false;
        if (source.is(DamageTypes.CACTUS)) return false;
        if (source.is(DamageTypes.DROWN)) return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.TRIDENT)) return false;
        if (source.is(DamageTypes.FALLING_ANVIL)) return false;
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        SdkfzdeathProcedure.execute(this);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        FuelGenerationProcedure.execute(this);
        return retval;
    }

    private final ItemStackHandler inventory = new ItemStackHandler(18) {
        @Override
        public int getSlotLimit(int slot) { return 64; }
    };
    private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER && side == null)
            return LazyOptional.of(() -> combined).cast();
        return super.getCapability(capability, side);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack))
                this.spawnAtLocation(itemstack);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("InventoryCustom", inventory.serializeNBT());
        compound.putString("Texture", this.getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Tag inventoryCustom = compound.get("InventoryCustom");
        if (inventoryCustom instanceof CompoundTag inventoryTag) inventory.deserializeNBT(inventoryTag);
        if (compound.contains("Texture")) this.setTexture(compound.getString("Texture"));
    }

    @Override
    public InteractionResult interactAt(Player sourceentity, Vec3 vec, InteractionHand hand) {
        FuelBucketOilUseProcedure.execute(this.level(), this, sourceentity);
        if (sourceentity.isSecondaryUseActive()) {
            if (sourceentity instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                    @Override
                    public Component getDisplayName() { return Component.literal("Sdkfz 2"); }
                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                        packetBuffer.writeBlockPos(sourceentity.blockPosition());
                        packetBuffer.writeByte(0);
                        packetBuffer.writeVarInt(Sdkfz2Entity.this.getId());
                        return new InventoryGuiMenu(id, inventory, packetBuffer);
                    }
                }, buf -> {
                    buf.writeBlockPos(sourceentity.blockPosition());
                    buf.writeByte(0);
                    buf.writeVarInt(this.getId());
                });
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        if (!this.level().isClientSide() && !sourceentity.isPassenger()) {
            List<Entity> passengers = this.getPassengers();
            if (!passengers.isEmpty()) {
                Entity currentDriver = passengers.get(0);
                if (MOB_DRIVER_ALLOWED) {
                    if (!(currentDriver instanceof Player)) {
                        currentDriver.stopRiding();
                        sourceentity.startRiding(this, true);
                        if (this.getPassengers().size() < 3) currentDriver.startRiding(this);
                        return InteractionResult.SUCCESS;
                    } else {
                        if (passengers.size() < 3) { sourceentity.startRiding(this); return InteractionResult.SUCCESS; }
                        sourceentity.displayClientMessage(Component.literal("Vehiculo lleno"), true);
                        return InteractionResult.PASS;
                    }
                } else {
                    if (currentDriver instanceof Player) {
                        if (passengers.size() < 3) { sourceentity.startRiding(this); return InteractionResult.SUCCESS; }
                        sourceentity.displayClientMessage(Component.literal("Vehiculo lleno"), true);
                        return InteractionResult.PASS;
                    } else {
                        currentDriver.stopRiding();
                        sourceentity.startRiding(this, true);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                sourceentity.startRiding(this, true);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunc) {
        if (!this.hasPassenger(passenger)) return;
        int index = this.getPassengers().indexOf(passenger);
        double x = 0;
        double y = 0.3; //altura en la que se sientan todos en el kettenkrad
        double z = 0;
        if (index == 0) { //conductor
            z = 0.25;
        } else if (index == 1) { //asiento derecho
            x = -0.325;
            z = -1.10;
        } else if (index == 2) { //asiento izquierdo
            x = 0.325;
            z = -1.10;
        }
        Vec3 rotated = (new Vec3(x, y, z)).yRot((float) Math.toRadians(-this.getYRot()));
        moveFunc.accept(passenger, this.getX() + rotated.x, this.getY() + rotated.y, this.getZ() + rotated.z);
    }

    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        List<Entity> remaining = this.getPassengers();
        if (remaining.isEmpty() || !(remaining.get(0) instanceof Player)) {
            this.getPersistentData().putDouble("VehicleGear", 0);
            this.setDeltaMovement(Vec3.ZERO);
            this.currentSpeed = 0;
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        ShiftEntityTickUpdateProcedure.execute(this.level(), this);
        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.8);
    }

    @Override
    public void travel(Vec3 dir) {
        if (this.isVehicle() && !this.getPassengers().isEmpty()) {
            Entity driver = this.getPassengers().get(0);
            this.setYRot(driver.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(driver.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = driver.getYRot();
            this.yHeadRot = driver.getYRot();
            // Eliminamos o comentamos la sincronización extra del conductor para evitar problemas de cámara
            // if (driver instanceof LivingEntity livingDriver) {
            //     livingDriver.yBodyRot = this.getYRot();
            //     livingDriver.yHeadRot = this.getYRot();
            // }
            super.travel(new Vec3(0, dir.y, 0));
            double dx = this.getX() - this.xo;
            double dz = this.getZ() - this.zo;
            float speed = (float) Math.sqrt(dx * dx + dz * dz) * 4;
            if (speed > 1.0F) speed = 1.0F;
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (speed - this.walkAnimation.speed()) * 0.4F);
            this.walkAnimation.position(this.walkAnimation.position() + this.walkAnimation.speed());
            this.calculateEntityAnimation(true);
            return;
        }
        super.travel(dir);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    public static void init() {}

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 40);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && this.onGround() && !this.isSprinting())
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            if (this.isInWaterOrBubble())
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            if (this.isSprinting())
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            if (!this.onGround())
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.climb"));
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }
        return PlayState.STOP;
    }

    String prevAnim = "empty";

    private PlayState procedurePredicate(AnimationState event) {
        if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
            if (!this.animationprocedure.equals(prevAnim)) event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (animationprocedure.equals("empty")) {
            prevAnim = "empty";
            return PlayState.STOP;
        }
        prevAnim = this.animationprocedure;
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(Sdkfz2Entity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    public String getSyncedAnimation() { return this.entityData.get(ANIMATION); }
    public void setAnimation(String animation) { this.entityData.set(ANIMATION, animation); }

    public void repositionPassenger(Entity passenger) {
        if (!this.hasPassenger(passenger)) return;
        int index = this.getPassengers().indexOf(passenger);
        double x = 0, y = 0.4, z = 0; //altura
        if (index == 0) z = 0.25;  //conductor
        else if (index == 1) { x = -0.325; z = -0.65; } //asiento derecho
        else if (index == 2) { x = 0.325; z = -0.65; } //asiento izquierdo
        Vec3 rotated = (new Vec3(x, y, z)).yRot((float) Math.toRadians(-this.getYRot()));
        passenger.setPos(this.getX() + rotated.x, this.getY() + rotated.y, this.getZ() + rotated.z);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}