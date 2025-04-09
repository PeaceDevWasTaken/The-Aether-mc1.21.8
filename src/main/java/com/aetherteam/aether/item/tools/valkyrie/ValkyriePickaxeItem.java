package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ToolMaterial;

public class ValkyriePickaxeItem extends PickaxeItem implements ValkyrieTool {
    public ValkyriePickaxeItem(Properties properties) {
        this(AetherItemTiers.VALKYRIE, 1.0F, -3.1F, properties);
    }

    public ValkyriePickaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties.attributes(ValkyrieTool.createAttributes(material, attackDamage, attackSpeed)));
    }
}
