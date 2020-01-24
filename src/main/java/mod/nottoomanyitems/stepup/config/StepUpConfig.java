package mod.nottoomanyitems.stepup.config;

import mod.nottoomanyitems.stepup.StepUp;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = StepUp.MODID, bus = Bus.MOD)
public class StepUpConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMONSPEC;
    
    private static final Logger LOGGER = LogManager.getLogger();

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMONSPEC = specPair.getRight();
    }

    public static class Common {

    	public final IntValue autoJumpState;

        Common(final Builder builder) {
            builder.push("client");

            this.autoJumpState = builder.comment("The Current State of SteupUp,"
            		+ " 0 All Disabled,"
            		+ "1 StepUp Enabled,"
            		+ "2 Minecraft Jump Enabled")
                .defineInRange("autoJumpState", 1, 0, 2);


            builder.pop();
        }
    }
    
    @SubscribeEvent
	public static void onLoad(final ModConfig.Loading configevent) {
		
	}

}
