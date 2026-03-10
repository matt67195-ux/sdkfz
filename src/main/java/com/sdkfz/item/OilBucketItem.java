
package com.sdkfz.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class OilBucketItem extends Item {
	public OilBucketItem() {
		super(new Item.Properties().stacksTo(2).rarity(Rarity.COMMON));
	}
}
