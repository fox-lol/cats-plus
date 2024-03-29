package xyz.foxkin.catsplus.mixin.commonloader.commonside.armor;

import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.foxkin.catsplus.commonside.init.ModArmorMaterials;

import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(ArmorItem.class)
abstract class ArmorItemMixin {

    @Shadow
    @Final
    private static UUID[] MODIFIERS;

    @Shadow
    @Final
    protected float knockbackResistance;

    /**
     * Adds additional knockback resistance to armor whose material is {@link ModArmorMaterials#CAT_MAID_ARMOR}.
     */
    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap;builder()Lcom/google/common/collect/ImmutableMultimap$Builder;", remap = false))
    private ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> catsPlus$addCatMaidArmorModifiers(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder, ArmorMaterial material, EquipmentSlot slot) {
        if (material.equals(ModArmorMaterials.CAT_MAID_ARMOR)) {
            UUID uuid = MODIFIERS[slot.getEntitySlotId()];
            attributeBuilder.put(
                    EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                    new EntityAttributeModifier(uuid,
                            "Armor knockback resistance",
                            knockbackResistance,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );
        }
        return attributeBuilder;
    }
}
