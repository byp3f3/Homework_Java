package net.nekrasova.mod.item;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekrasova.mod.Mod;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, Mod.MOD_ID);

    public static void register(IEventBus eventBus){
        ITEM.register(eventBus);
    }

    public static final RegistryObject<Item> PUDDING = ITEM.register("pudding",
    () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(20)
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 1000), 0.23f).build())
            .tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> MACARON = ITEM.register("macaron",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5)
                    .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 10000), 0.001f)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 700), 0.49f).build())
                    .tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CROISSANT = ITEM.register("croissant",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100), 0.89f).build())
                    .tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BUN = ITEM.register("bun",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(13).build())
                    .tab(CreativeModeTab.TAB_MISC)));
}
