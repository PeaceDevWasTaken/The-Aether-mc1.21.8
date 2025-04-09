package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ToolMaterial;

public class ValkyrieAxeItem extends AxeItem implements ValkyrieTool {
    public ValkyrieAxeItem(Properties properties) {
        this(AetherItemTiers.VALKYRIE, 5.0F, -3.3F, properties);
    }

    public ValkyrieAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties.attributes(ValkyrieTool.createAttributes(material, attackDamage, attackSpeed)));
    }
}
