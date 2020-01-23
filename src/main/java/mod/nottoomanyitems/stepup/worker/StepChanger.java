package mod.nottoomanyitems.stepup.worker;

import org.apache.logging.log4j.Logger;

import mod.nottoomanyitems.stepup.StepUp;
import mod.nottoomanyitems.stepup.config.StepUpConfig;
import mod.nottoomanyitems.stepup.init.KeyBindings;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

public class StepChanger {
	
	private static PlayerEntity player;
    public KeyBinding myKey;

    private static Minecraft mc = Minecraft.getInstance();

    private static final Logger LOGGER = LogManager.getLogger();
    
    public StepChanger() {}

    public static void TickEvent(PlayerTickEvent event) {
        player = event.player;
        int autoJumpState = StepUpConfig.COMMON.autoJumpState.get();
        
		if (player.isSneaking()) {
            player.stepHeight = .6f;
        } else if (autoJumpState == 0 && player.stepHeight >= 1.0f) { //All Disabled
        	player.stepHeight = .6f;
        } else if (autoJumpState == 1 && player.stepHeight < 1.0f) { //StepUp Enabled
            player.stepHeight = 1.25f;
        } else if (autoJumpState == 2 && player.stepHeight >= 1.0f) { //Minecraft Enabled
            player.stepHeight = .6f;
        }
        autoJump();

        if (StepUp.firstRun && autoJumpState != -1) {
        	StepUp.MC_VERSION = mc.getVersion();
            //if (!StepUp.versionChecker.isLatestVersion()) {
            //    updateMessage();
            //}
            message();
            StepUp.firstRun = false;
        }
    }

    //@SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
    	int autoJumpState = StepUpConfig.COMMON.autoJumpState.get();

        if (KeyBindings.KEYBINDINGS[0].isPressed()) {//(event.getKey() == 36) {
            if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            	StepUpConfig.COMMON.autoJumpState.set(AutoJumpState.DISABLED.getLevelCode());; //0 StepUp and Minecraft Disabled
            } else if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            	StepUpConfig.COMMON.autoJumpState.set(AutoJumpState.ENABLED.getLevelCode());; //1 StepUp Enabled
            } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            	StepUpConfig.COMMON.autoJumpState.set(AutoJumpState.MINECRAFT.getLevelCode()); //2 Minecraft Enabled
            }
            //StepUpConfig.save();
            autoJump();
            message();
        }
    }

    private static void autoJump() {
    	int autoJumpState = StepUpConfig.COMMON.autoJumpState.get();
    	
        boolean b = mc.gameSettings.autoJump;
        if (autoJumpState < AutoJumpState.MINECRAFT.getLevelCode() && b == true) {
            mc.gameSettings.autoJump = false;
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && b == false) {
            mc.gameSettings.autoJump = true;
        }
    }

    private static void message() {
    	int autoJumpState = StepUpConfig.COMMON.autoJumpState.get();
    	
        String m = (Object) TextFormatting.DARK_AQUA + "[" + (Object) TextFormatting.YELLOW + StepUp.MOD_NAME + (Object) TextFormatting.DARK_AQUA + "]" + " ";
        if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            m = m + (Object) TextFormatting.RED + I18n.format(AutoJumpState.DISABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            m = m + (Object) TextFormatting.GREEN + I18n.format(AutoJumpState.ENABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            m = m + (Object) TextFormatting.GREEN + I18n.format(AutoJumpState.MINECRAFT.getDesc());
        }

        player.sendMessage((ITextComponent) new StringTextComponent(m));
    }

    private void updateMessage() {
        String m2 = (Object) TextFormatting.GOLD + I18n.format("mod.stepup.updateAvailable") + ": " + (Object) TextFormatting.DARK_AQUA + "[" + (Object) TextFormatting.YELLOW + "StepUp" + (Object) TextFormatting.WHITE + " v" + "" + (Object) TextFormatting.DARK_AQUA + "]";
        String url = "https://nottoomanyitems.wixsite.com/mods/step-up";
        ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        HoverEvent versionCheckChatHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(I18n.format("mod.stepup.updateTooltip") + "!"));
        TextComponent component = new StringTextComponent(m2);
        Style s = component.getStyle();
        s.setClickEvent(versionCheckChatClickEvent);
        s.setHoverEvent(versionCheckChatHoverEvent);
        component.setStyle(s);
        player.sendMessage((ITextComponent) component);
    }
    
    public enum AutoJumpState
    {
        DISABLED (0,"msg.stepup.enabled"), //StepUp Enabled
        ENABLED (1,"msg.stepup.disabled"), //"All Disabled" 
        MINECRAFT (2,"msg.stepup.minecraft"); //"Minecraft Jump Enabled" 
        
        private final int levelCode;
        private final String desc;

        AutoJumpState(int levelCode, String desc) {
            this.levelCode = levelCode;
            this.desc = desc;
        }
        
        public int getLevelCode() {
            return this.levelCode;
        }

		public String getDesc() {
			return this.desc;
		}
    }

}
