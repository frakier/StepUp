package mod.nottoomanyitems.stepup;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Files;
import java.nio.file.Paths;

import mod.nottoomanyitems.stepup.config.StepUpConfig;
import mod.nottoomanyitems.stepup.worker.StepChanger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@Mod(StepUp.MODID)
public class StepUp {
    public static final String MODID = "stepup";
	public static final String MOD_VERSION = "1.1.3";
	public static final String MOD_NAME = "StepUp";
	public static final String clinetConfigFile = MODID+"-common.toml";
	public static boolean firstRun;
	public static String MC_VERSION;
	
	private static Minecraft mc = Minecraft.getInstance();

    public StepUp() {
    	if (Files.notExists(Paths.get("config", clinetConfigFile))){
        	firstRun = true;
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StepUpConfig.COMMONSPEC);
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientEventHandler {

        @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
        public static void clientTickEvent(final PlayerTickEvent event) {
        	if (event.player != null) {
        		StepChanger.TickEvent(event);
        	}
        }
        
        @SubscribeEvent
        public static void onKeyInput(KeyInputEvent event) {
        	StepChanger.onKeyInput(event);
        }
    }
}
