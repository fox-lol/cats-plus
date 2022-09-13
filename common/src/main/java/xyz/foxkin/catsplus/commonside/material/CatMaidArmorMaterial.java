package xyz.foxkin.catsplus.commonside.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import xyz.foxkin.catsplus.commonside.init.ModItems;


public class CatMaidArmorMaterial implements ArmorMaterial {

    private static final int[] BASE_DURABILITY = {8, 10, 12, 8};
    private static final int[] PROTECTION_AMOUNT = {2, 5, 6, 2};

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * 40;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return PROTECTION_AMOUNT[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.CAT_MAID_SCRAP.get());
    }

    @Override
    public String getName() {
        return "maid_outfit";
    }

    @Override
    public float getToughness() {
        return 3;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.1F;
    }
}
