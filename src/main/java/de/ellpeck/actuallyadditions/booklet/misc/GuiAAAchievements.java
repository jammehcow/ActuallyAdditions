//TODO Achievement GUI?
/*
package de.ellpeck.actuallyadditions.booklet.misc;

import de.ellpeck.actuallyadditions.common.achievement.InitAchievements;
import de.ellpeck.actuallyadditions.common.util.ModUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.stats.StatisticsManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * (Partially excerpted from Botania by Vazkii with permission, thanks!)
 *
@SideOnly(Side.CLIENT)
public class GuiAAAchievements extends GuiAchievements{

    public GuiAAAchievements(GuiScreen screen, StatisticsManager statistics){
        super(screen, statistics);
        try{
            ReflectionHelper.setPrivateValue(GuiAchievements.class, this, InitAchievements.pageNumber, 20);
        }
        catch(Exception e){
            ModUtil.LOGGER.error("Something went wrong trying to open the Achievements GUI!", e);
        }
    }

    @Override
    public void initGui(){
        super.initGui();

        try{
            this.buttonList.remove(1);
        }
        catch(Exception e){
            ModUtil.LOGGER.error("Something went wrong trying to initialize the Achievements GUI!", e);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int key) throws IOException{
        if(key == Keyboard.KEY_ESCAPE || key == this.mc.gameSettings.keyBindInventory.getKeyCode()){
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else{
            super.keyTyped(typedChar, key);
        }
    }
}
 */