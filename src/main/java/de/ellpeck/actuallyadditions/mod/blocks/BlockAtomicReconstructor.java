/*
 * This file ("BlockAtomicReconstructor.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.blocks;

import de.ellpeck.actuallyadditions.api.lens.ILensItem;
import de.ellpeck.actuallyadditions.mod.blocks.base.BlockContainerBase;
import de.ellpeck.actuallyadditions.mod.blocks.base.ItemBlockBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityAtomicReconstructor;
import de.ellpeck.actuallyadditions.mod.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAtomicReconstructor extends BlockContainerBase implements IHudDisplay{

    private static final PropertyInteger META = PropertyInteger.create("meta", 0, 5);

    public static final int NAME_FLAVOR_AMOUNTS_1 = 12;
    public static final int NAME_FLAVOR_AMOUNTS_2 = 14;

    public BlockAtomicReconstructor(String name){
        super(Material.rock, name);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(10F);
        this.setResistance(80F);
        this.setStepSound(soundTypeStone);
    }

    @Override
    protected PropertyInteger getMetaProperty(){
        return META;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.EPIC;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack){
        int rotation = BlockPistonBase.getFacingFromEntity(world, pos, player).ordinal();
        PosUtil.setMetadata(pos, world, rotation, 2);

        super.onBlockPlacedBy(world, pos, state, player, stack);
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing par6, float par7, float par8, float par9){
        if(this.tryToggleRedstone(world, pos, player)){
            return true;
        }
        if(!world.isRemote){
            TileEntityAtomicReconstructor reconstructor = (TileEntityAtomicReconstructor)world.getTileEntity(pos);
            if(reconstructor != null){
                ItemStack heldItem = player.getCurrentEquippedItem();
                if(heldItem != null){
                    if(heldItem.getItem() instanceof ILensItem && reconstructor.getStackInSlot(0) == null){
                        ItemStack toPut = heldItem.copy();
                        toPut.stackSize = 1;
                        reconstructor.setInventorySlotContents(0, toPut);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }
                }
                else{
                    if(reconstructor.getStackInSlot(0) != null){
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, reconstructor.getStackInSlot(0).copy());
                        reconstructor.setInventorySlotContents(0, null);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i){
        return new TileEntityAtomicReconstructor();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        this.dropInventory(world, pos);
        super.breakBlock(world, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayHud(Minecraft minecraft, EntityPlayer player, ItemStack stack, MovingObjectPosition posHit, Profiler profiler, ScaledResolution resolution){
        TileEntity tile = minecraft.theWorld.getTileEntity(posHit.getBlockPos());
        if(tile instanceof TileEntityAtomicReconstructor){
            ItemStack slot = ((TileEntityAtomicReconstructor)tile).getStackInSlot(0);
            String strg;
            if(slot == null){
                strg = StringUtil.localize("info."+ModUtil.MOD_ID_LOWER+".noLens");
            }
            else{
                strg = slot.getItem().getItemStackDisplayName(slot);

                AssetUtil.renderStackToGui(slot, resolution.getScaledWidth()/2+15, resolution.getScaledHeight()/2-29, 1F);
            }
            minecraft.fontRendererObj.drawStringWithShadow(EnumChatFormatting.YELLOW+""+EnumChatFormatting.ITALIC+strg, resolution.getScaledWidth()/2+35, resolution.getScaledHeight()/2-25, StringUtil.DECIMAL_COLOR_WHITE);
        }
    }

    @Override
    protected Class<? extends ItemBlockBase> getItemBlock(){
        return TheItemBlock.class;
    }

    public static class TheItemBlock extends ItemBlockBase{

        private long lastSysTime;
        private int toPick1;
        private int toPick2;

        public TheItemBlock(Block block){
            super(block);
            this.setHasSubtypes(false);
            this.setMaxDamage(0);
        }

        @Override
        public String getUnlocalizedName(ItemStack stack){
            return this.getUnlocalizedName();
        }

        @Override
        public int getMetadata(int damage){
            return damage;
        }

        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool){
            long sysTime = System.currentTimeMillis();

            if(this.lastSysTime+3000 < sysTime){
                this.lastSysTime = sysTime;
                this.toPick1 = Util.RANDOM.nextInt(NAME_FLAVOR_AMOUNTS_1)+1;
                this.toPick2 = Util.RANDOM.nextInt(NAME_FLAVOR_AMOUNTS_2)+1;
            }

            String base = "tile."+ModUtil.MOD_ID_LOWER+"."+((BlockAtomicReconstructor)this.block).getBaseName()+".info.";
            list.add(StringUtil.localize(base+"1."+this.toPick1)+" "+StringUtil.localize(base+"2."+this.toPick2));
        }
    }
}