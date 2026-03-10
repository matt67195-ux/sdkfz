package com.sdkfz.entity.model;

import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.constant.DataTickets;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;

import com.sdkfz.entity.Sdkfz2Entity;

public class Sdkfz2Model extends GeoModel<Sdkfz2Entity> {
	@Override
	public ResourceLocation getAnimationResource(Sdkfz2Entity entity) {
		return new ResourceLocation("sdkfz", "animations/sdkfz2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Sdkfz2Entity entity) {
		return new ResourceLocation("sdkfz", "geo/sdkfz2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Sdkfz2Entity entity) {
		return new ResourceLocation("sdkfz", "textures/entities/" + entity.getTexture() + ".png");
	}

	@Override
	public void setCustomAnimations(Sdkfz2Entity animatable, long instanceId, AnimationState animationState) {
		CoreGeoBone head = getAnimationProcessor().getBone("head");
		if (head != null) {
			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}

	}
}
