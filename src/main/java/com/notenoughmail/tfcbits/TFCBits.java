package com.notenoughmail.tfcbits;

import com.mojang.logging.LogUtils;
import com.notenoughmail.tfcbits.block.BitsBlocks;
import com.notenoughmail.tfcbits.block.entity.BitsBlockEntities;
import com.notenoughmail.tfcbits.util.config.BitsClientConfig;
import com.notenoughmail.tfcbits.client.BitsClientEvents;
import com.notenoughmail.tfcbits.util.config.BitsServerConfig;
import com.notenoughmail.tfcbits.item.BitsItems;
import com.notenoughmail.tfcbits.world.WaterTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(TFCBits.ID)
public class TFCBits {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "tfcbits";

    public TFCBits() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BitsClientConfig.register();
        BitsServerConfig.register();

        BitsItems.ITEMS.register(bus);
        BitsBlocks.BLOCKS.register(bus);
        BitsBlockEntities.BLOCK_ENTITIES.register(bus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BitsClientEvents.init();
        }

        WaterTable.init();
    }
}
