// Todo el código de sdkfz2entity:


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

public class Sdkfz2Entity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(Sdkfz2Entity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean swinging;
    private boolean lastloop;
    private long lastSwing;
    public String animationprocedure = "empty";

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
        if (source.is(DamageTypes.IN_FIRE))
            return false;
        if (source.getDirectEntity() instanceof AbstractArrow)
            return false;
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.is(DamageTypes.TRIDENT))
            return false;
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
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
        public int getSlotLimit(int slot) {
            return 64;
        }
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
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
            }
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
        if (inventoryCustom instanceof CompoundTag inventoryTag)
            inventory.deserializeNBT(inventoryTag);
        if (compound.contains("Texture"))
            this.setTexture(compound.getString("Texture"));
    }



    //aqui iba mob interact, que remplazamos por interact at

    @Override
    public InteractionResult interactAt(Player sourceentity, Vec3 vec, InteractionHand hand) {
        // 1. REPARACIÓN VITAL: Ejecutar la lógica de gasolina (Oil Bucket)
        // Esto permite que el FuelBucketOilUseProcedure detecte el ítem en tu mano
        FuelBucketOilUseProcedure.execute(this.level(), this, sourceentity);

        // 2. Mantener la funcionalidad del inventario (Shift + Clic)
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

        if (!this.level().isClientSide()) {
            if (!sourceentity.isPassenger()) {
                // VERIFICACIÓN DE OCUPACIÓN
                // Si hay al menos un pasajero, asumimos que el conductor está ahí
                if (!this.getPassengers().isEmpty()) {
                    // Forzamos que te subas como pasajero adicional (index 1 o 2)
                    sourceentity.startRiding(this);
                } else {
                    // Si la moto está vacía, puedes ser el conductor (index 0)
                    sourceentity.startRiding(this);
                }
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide());
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
    @Nullable
    public LivingEntity getControllingPassenger() {
        // Solo el primer pasajero (el conductor en el asiento 0) tiene el control.
        // Esto es vital para que el "salto" (Espacio) del sistema de cambios funcione siempre.
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        Entity driver = this.getPassengers().get(0);
        return driver instanceof LivingEntity ? (LivingEntity) driver : null;
    }

    @Override
    public void travel(Vec3 dir) {
        Entity driver = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        if (this.isVehicle() && driver instanceof LivingEntity passenger) {
            // 1. GIRAR LA MOTO CON EL RATÓN
            // La moto copia la rotación horizontal del pasajero
            this.setYRot(passenger.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(passenger.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());

            // 2. BLOQUEAR CÁMARA (El jugador mira siempre al frente de la moto)
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            passenger.yBodyRot = this.getYRot();
            passenger.yHeadRot = this.getYRot();

            // 3. RECUPERAR VELOCIDAD (Físicas de vehículo, no de mob)
            this.setMaxUpStep(1.0f);
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));

            // En lugar de usar super.travel (que frena la moto),
            // dejamos que el motor de Minecraft use el movimiento del tick anterior
            // sumado al impulso que le den tus procedimientos.
            if (this.isControlledByLocalInstance()) {
                // Calculamos el movimiento basado en hacia dónde mira la moto
                // 'dir.z' es el valor que viene de tus procedimientos de cambios
                super.travel(new Vec3(0, 0, dir.z));
            }

            // Animación visual
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (float) (this.getX() - this.xo) * 0.4F);
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

    public static void init() {
    }

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
            if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && this.onGround() && !this.isSprinting()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            }
            if (this.isInWaterOrBubble()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            }
            if (this.isSprinting()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.move"));
            }
            if (!this.onGround()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.sdkfz2.climb"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }
        return PlayState.STOP;
    }

    String prevAnim = "empty";

    private PlayState procedurePredicate(AnimationState event) {
        if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
            if (!this.animationprocedure.equals(prevAnim))
                event.getController().forceAnimationReset();
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

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
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


    // (aquí terminan tus otros métodos)

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 3;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.5; // Valor negativo para que el jugador se siente
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunc) {
        if (!this.hasPassenger(passenger)) return;

        int index = this.getPassengers().indexOf(passenger);

        double x = 0;
        double y = 0.9375; // Altura base 15/16
        double z = 0;

        if (index == 0) {
            // CONDUCTOR: Altura normal

            y = 0.5;
            z = 0.35;
        } else if (index == 1) {
            // PASAJERO 1 (Seat 2): ¡Lo ponemos a flotar a 3 bloques!
            x = -0.625;
            y = 0.5;
            z = -0.85;
        } else if (index == 2) {
            // PASAJERO 2 (Seat 3): ¡Lo ponemos a flotar a 5 bloques!
            x = 0.625;
            y = 0.5;
            z = -0.85;
        }

        Vec3 rotated = (new Vec3(x, y, z)).yRot((float) Math.toRadians(-this.getYRot()));
        moveFunc.accept(passenger, this.getX() + rotated.x, this.getY() + rotated.y, this.getZ() + rotated.z);


    }


}
