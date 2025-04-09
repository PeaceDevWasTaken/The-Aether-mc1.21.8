package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

public class ValkyrieShovelItem extends ShovelItem implements ValkyrieTool {
    public ValkyrieShovelItem(Properties properties) {
        this(AetherItemTiers.VALKYRIE, 1.5F, -3.3F, properties);
    }

    public ValkyrieShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties.attributes(ValkyrieTool.createAttributes(material, attackDamage, attackSpeed)));
    }
}
