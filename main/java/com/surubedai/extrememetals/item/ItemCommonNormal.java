package com.surubedai.extrememetals.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

// extends Item を追加する
public class ItemCommonNormal extends Item {

    // コンストラクタの例（1.20以降などのPropertiesを渡す形式）
    public ItemCommonNormal(Properties properties) {
        super(properties.rarity(Rarity.COMMON));
    }
}
