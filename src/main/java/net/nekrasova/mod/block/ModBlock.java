package net.nekrasova.mod.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekrasova.mod.Mod;
import net.nekrasova.mod.item.ModItems;

import java.util.function.Supplier;

public class ModBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Mod.MOD_ID);


    public static final RegistryObject<Block> EASTER_CAKE_BLOCK = registryBlock("easter_cake_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.SPONGE).speedFactor(5)),
            CreativeModeTab.TAB_MISC
    );

    public static final RegistryObject<Block> PUDDING_BLOCK = registryBlock("pudding_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK).jumpFactor(4)),
            CreativeModeTab.TAB_MISC
    );

    public static final RegistryObject<Block> GINGERBREAD_BLOCK = registryBlock("gingerbread_block",
        () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()),
            CreativeModeTab.TAB_MISC
    );

    public static final RegistryObject<Block> COTTONCANDY_BLOCK = registryBlock("cottoncandy_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).requiresCorrectToolForDrops()),
            CreativeModeTab.TAB_MISC
    );

    public static <T extends Block>RegistryObject<T> registryBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturns = BLOCKS.register(name, block);
        registryBlockItem(name, toReturns, tab);
        return toReturns;
    }

    public static <T extends Block>RegistryObject<Item> registryBlockItem(String name, RegistryObject<T> block,
                                                                          CreativeModeTab tab){
        return ModItems.ITEM.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
