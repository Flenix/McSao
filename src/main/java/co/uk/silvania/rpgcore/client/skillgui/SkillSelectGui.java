package co.uk.silvania.rpgcore.client.skillgui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import co.uk.silvania.rpgcore.RPGCore;
import co.uk.silvania.rpgcore.SkillsContainer;
import co.uk.silvania.rpgcore.network.EquipNewSkillPacket;
import co.uk.silvania.rpgcore.skills.EquippedSkills;
import co.uk.silvania.rpgcore.skills.GlobalLevel;
import co.uk.silvania.rpgcore.skills.SkillLevelBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class SkillSelectGui extends GuiContainer {
	
	public static final ResourceLocation skillEquip  = new ResourceLocation(RPGCore.MODID, "textures/gui/skillequip.png");
	public static final ResourceLocation skillEquip2 = new ResourceLocation(RPGCore.MODID, "textures/gui/skillequip2.png");
	public static final ResourceLocation skillIcons  = new ResourceLocation(RPGCore.MODID, "textures/gui/skillicons.png");
	
	private IInventory playerInv;
	
	int xSize;
	int ySize;
	
	public static int slotClicked = -1;
	
	public MultiLineButton buttonConfig;
	public GuiButton buttonParty;
	public GuiButton buttonGuild;
	
	public SkillSelectGui(SkillsContainer containerSkills) {
		super(containerSkills);
		
		playerInv = containerSkills.inventory;
		
		xSize = 256;
		ySize = 256;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int left = (this.width - this.xSize) / 2;
		int top  = (this.height - this.ySize) / 2;

		buttonConfig = new MultiLineButton(1, left+257, top+206, 72, 22, "Configure#Player");
		
		buttonList.add(buttonConfig);
	}
	
	public void mouseClicked(int mouseX, int mouseZ, int buttonId) {
        try {
			super.mouseClicked(mouseX, mouseZ, buttonId);
		} catch (IOException e) {
			e.printStackTrace();
		}
        int skillSlot = getSkillSlotHover(mouseX, mouseZ);
        boolean openGui = true;
        
        if (skillSlot >= 0) {
        	if ((skillSlot == 4 && mc.thePlayer.inventory.armorItemInSlot(0) != null) || 
        		(skillSlot == 5 && mc.thePlayer.inventory.armorItemInSlot(1) != null) || 
        		(skillSlot == 7 && mc.thePlayer.inventory.armorItemInSlot(2) != null) || 
        		(skillSlot == 8 && mc.thePlayer.inventory.armorItemInSlot(3) != null)) {
        		//Equipment slot used for armour. Do not equip skill.
        		//TODO allow removal of armour here.
        		if (skillSlot == 4) { System.out.println("Armour in slot: " + mc.thePlayer.inventory.armorItemInSlot(0).getDisplayName()); }
        		if (skillSlot == 5) { System.out.println("Armour in slot: " + mc.thePlayer.inventory.armorItemInSlot(1).getDisplayName()); }
        		if (skillSlot == 7) { System.out.println("Armour in slot: " + mc.thePlayer.inventory.armorItemInSlot(2).getDisplayName()); }
        		if (skillSlot == 8) { System.out.println("Armour in slot: " + mc.thePlayer.inventory.armorItemInSlot(3).getDisplayName()); }
        		
        		openGui = false;
        		System.out.println("No skills while wearing armour!");
        	}
        	System.out.println("Clicked Skill Slot " + skillSlot);
        	slotClicked = skillSlot;
			if (openGui) { openGui(2); }
        }
	}
	
	public void openGui(int id) {
		mc.thePlayer.openGui(RPGCore.instance, id, mc.thePlayer.worldObj, (int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseZ, float partialTick) {
		super.drawScreen(mouseX, mouseZ, partialTick);
		
		int skillSlot = getSkillSlotHover(mouseX, mouseZ);
		
		EquippedSkills equippedSkills = (EquippedSkills) EquippedSkills.get((EntityPlayer) Minecraft.getMinecraft().thePlayer);
        SkillLevelBase skill = SkillLevelBase.getSkillByID(equippedSkills.getSkillInSlot(skillSlot), mc.thePlayer);
        
        ItemStack equippedItem = null;
        
        if (skillSlot == 4) { equippedItem = mc.thePlayer.inventory.armorItemInSlot(0); }
        if (skillSlot == 5) { equippedItem = mc.thePlayer.inventory.armorItemInSlot(1); }
        if (skillSlot == 7) { equippedItem = mc.thePlayer.inventory.armorItemInSlot(2); }
        if (skillSlot == 8) { equippedItem = mc.thePlayer.inventory.armorItemInSlot(3); }
        
		if (skill != null) {
			String[] text = {skill.nameFormat() + "\u00A7l" + skill.skillName(), "Lvl: " + skill.getLevel(), "XP: " + (int) skill.getXP() + "/" + (int) skill.getXpForLevel(skill.getLevel()), "XP to level up: " + (int) skill.xpToNextLevel()};
			List temp = Arrays.asList(text);
			drawHoveringText(temp, mouseX, mouseZ, fontRendererObj);
		} else if (equippedItem != null) {
			List list = equippedItem.getTooltip(mc.thePlayer, false);
			drawHoveringText(list, mouseX, mouseZ, fontRendererObj);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		int left = -40;
		int top  = -45;
		GlobalLevel glevel = (GlobalLevel) GlobalLevel.get(mc.thePlayer);
		
		
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		mc.fontRendererObj.drawString("\u00A7l" + mc.thePlayer.getName(), left - 100, top + 17, 4210752);
		mc.fontRendererObj.drawString("Global Level: " + glevel.getLevel(), left - 100, top + 30, 4210752);
		mc.fontRendererObj.drawString("XP: " + glevel.getXPForPrint(), left - 100, top + 40, 4210752);
		mc.fontRendererObj.drawString("Guild: ", left - 100, top + 55, 4210752);
		mc.fontRendererObj.drawString("Faction: ", left - 100, top + 70, 4210752);
		mc.fontRendererObj.drawString("\u00A7lParty", left - 100, top + 82, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player1", left - 98, top + 96,  4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player2", left - 98, top + 110, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player3", left - 98, top + 124, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player4", left - 98, top + 138, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player5", left - 98, top + 152, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player6", left - 98, top + 166, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player7", left - 98, top + 180, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player8", left - 98, top + 194, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player9", left - 98, top + 208, 4210752);
		mc.fontRendererObj.drawString("\u00A7l" + "Player10", left - 98, top + 222, 4210752);
		
		RenderItem renderItem = mc.getRenderItem();
		EquippedSkills equippedSkills = (EquippedSkills) EquippedSkills.get((EntityPlayer) Minecraft.getMinecraft().thePlayer);

		GL11.glScalef(1.5F, 1.5F, 1.5F);
		if (mc.thePlayer.inventory.armorItemInSlot(0) != null) {
			renderItem.renderItemAndEffectIntoGUI(mc.thePlayer.inventory.armorItemInSlot(0), 106, 78);	//22 25		
			if (equippedSkills.getSkillInSlot(4).length() >= 3) {
				RPGCore.network.sendToServer(new EquipNewSkillPacket(4, ""));
			}
		}
		
		if (mc.thePlayer.inventory.armorItemInSlot(1) != null) {
			renderItem.renderItemAndEffectIntoGUI(mc.thePlayer.inventory.armorItemInSlot(1), 83, 101);
			if (equippedSkills.getSkillInSlot(5).length() >= 3) {
				RPGCore.network.sendToServer(new EquipNewSkillPacket(5, ""));
			}
		}
		
		if (mc.thePlayer.inventory.armorItemInSlot(2) != null) {
			renderItem.renderItemAndEffectIntoGUI(mc.thePlayer.inventory.armorItemInSlot(2), 19, 101);
			if (equippedSkills.getSkillInSlot(7).length() >= 3) {
				RPGCore.network.sendToServer(new EquipNewSkillPacket(7, ""));
			}
		}
		
		if (mc.thePlayer.inventory.armorItemInSlot(3) != null) {
			renderItem.renderItemAndEffectIntoGUI(mc.thePlayer.inventory.armorItemInSlot(3), -7, 78);
			if (equippedSkills.getSkillInSlot(8).length() >= 3) {
				RPGCore.network.sendToServer(new EquipNewSkillPacket(8, ""));
			}
		}
		GL11.glScalef(0.75F, 0.75F, 0.75F);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseZ) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		this.mc.getTextureManager().bindTexture(skillEquip); //Main skill pane
		int left = (this.width - this.xSize) / 2;
		int top  = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
		
		this.mc.getTextureManager().bindTexture(skillEquip2); //Item inventory & party/guild panel
		this.drawTexturedModalRect(left+this.xSize, top+11, 0, 0, 81, 226);
		this.drawTexturedModalRect(left-106, top+11, 150, 0, 106, 226);
		
		int skillSlot = getSkillSlotHover(mouseX, mouseZ);
		int slotX = 0;
		int slotZ = 0;
		
		EquippedSkills equippedSkills = (EquippedSkills) EquippedSkills.get((EntityPlayer) Minecraft.getMinecraft().thePlayer);
		
		if (equippedSkills != null) {
			
			for (int i = 0; i < equippedSkills.skillSlots; i++) {
				if (!equippedSkills.getSkillInSlot(i).isEmpty()) {
					SkillLevelBase skill = SkillLevelBase.getSkillByID(equippedSkills.getSkillInSlot(i), mc.thePlayer);
					ResourceLocation icon;
					int iconPosX = 0;
					int iconPosZ = 0;
					
					GL11.glDisable(GL11.GL_LIGHTING);
		            GL11.glDisable(GL11.GL_DEPTH_TEST);
		            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		    		GL11.glEnable(GL11.GL_BLEND);
					
					if (skill != null) {
						if (skill.skillIcon() != null) {
							mc.getTextureManager().bindTexture(skill.skillIcon());
							iconPosX = skill.iconX();
							iconPosZ = skill.iconZ();
						} else {
							mc.getTextureManager().bindTexture(skillIcons);
							iconPosZ = 88;
						}
					} else {
						mc.getTextureManager().bindTexture(skillIcons);
						iconPosX = 226;
						iconPosZ = 226;
					}
					
					int xPos = 0;
					int zPos = 0;
					
					if (i == 0)  { xPos = 113;  zPos = 17;  }
					if (i == 1)  { xPos = 161;  zPos = 30;  }
					if (i == 2)  { xPos = 196;  zPos = 65;  }
					if (i == 3)  { xPos = 209;  zPos = 113; }
					if (i == 4)  { xPos = 196;  zPos = 161; }
					if (i == 5)  { xPos = 161;  zPos = 196; }
					if (i == 6)  { xPos = 113;  zPos = 209; }
					if (i == 7)  { xPos = 65;   zPos = 196; }
					if (i == 8)  { xPos = 30;   zPos = 161; }
					if (i == 9)  { xPos = 17;   zPos = 113; }
					if (i == 10) { xPos = 30;   zPos = 64; }
					if (i == 11) { xPos = 65;   zPos = 29; }
					
					drawTexturedModalRect(((this.width - xSize) / 2) + xPos, ((this.height - ySize) / 2) + zPos, iconPosX, iconPosZ, 30, 30);
				}
			}
		}

		
		if (skillSlot == 0)  { slotX = 113;  slotZ = 17;  }
		if (skillSlot == 1)  { slotX = 161;  slotZ = 30;  }
		if (skillSlot == 2)  { slotX = 196;  slotZ = 65;  }
		if (skillSlot == 3)  { slotX = 209;  slotZ = 113; }
		if (skillSlot == 4)  { slotX = 196;  slotZ = 161; }
		if (skillSlot == 5)  { slotX = 161;  slotZ = 196; }
		if (skillSlot == 6)  { slotX = 113;  slotZ = 209; }
		if (skillSlot == 7)  { slotX = 65;   slotZ = 196; }
		if (skillSlot == 8)  { slotX = 30;   slotZ = 161; }
		if (skillSlot == 9)  { slotX = 17;   slotZ = 113; }
		if (skillSlot == 10) { slotX = 30;   slotZ = 64; }
		if (skillSlot == 11) { slotX = 65;   slotZ = 29; }

		if (skillSlot >= 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glColorMask(true, true, true, false);
            
            for (int i = 0; i < 30; i++) {
            	int j = 0; //Adjust the width. Gives the render "roundness" for the icon slots.
            	if (i == 0 || i == 29) { j = 11; }
            	if (i == 1 || i == 28) { j = 8; }
            	if (i == 2 || i == 27) { j = 6; }
            	if (i == 3 || i == 26) { j = 5; }
            	if (i == 4 || i == 25) { j = 4; }
            	if (i == 5 || i == 24) { j = 3; }
            	
            	if (i == 6 || i == 7 || i == 22 || i == 23)      { j = 2; }
            	if ((i >= 8 && i <= 10) || (i >= 19 && i <= 21)) { j = 1; }
            	
            	this.drawGradientRect(left+slotX+j, top+slotZ+i, left+slotX+30-j, top+slotZ+i+1, -2130706433, -2130706433);
            }
		}
	}
	
	public int getSkillSlotHover(int mouseX, int mouseZ) {
		int left = (this.width - this.xSize) / 2;
		int top  = (this.height - this.ySize) / 2;
		
		if (isMouseWithinCircle(mouseX, mouseZ, left + 113, top + 17))  { return 0; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 161, top + 30))  { return 1; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 196, top + 65))  { return 2; }		
		if (isMouseWithinCircle(mouseX, mouseZ, left + 209, top + 113)) { return 3; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 196, top + 161)) { return 4; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 161, top + 196)) { return 5; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 113, top + 209)) { return 6; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 65,  top + 196)) { return 7; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 30,  top + 161)) { return 8; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 17,  top + 113)) { return 9; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 30,  top + 64))  { return 10; }
		if (isMouseWithinCircle(mouseX, mouseZ, left + 65,  top + 29))  { return 11; }
		
		return -1;
	}
	
	//Assumes your circle is 30x30. Can't generalize it unfortunately.
	public boolean isMouseWithinCircle(int mouseX, int mouseZ, int circleLeft, int circleTop) {
		for (int i = 0; i < 30; i++) {
        	int j = 0;
        	if (i == 0 || i == 29) { j = 11; }
        	if (i == 1 || i == 28) { j = 8; }
        	if (i == 2 || i == 27) { j = 6; }
        	if (i == 3 || i == 26) { j = 5; }
        	if (i == 4 || i == 25) { j = 4; }
        	if (i == 5 || i == 24) { j = 3; }
        	
        	if (i == 6 || i == 7 || i == 22 || i == 23)      { j = 2; }
        	if ((i >= 8 && i <= 10) || (i >= 19 && i <= 21)) { j = 1; }
        	
        	if (mouseX >= circleLeft+j && mouseZ >= circleTop+i && mouseX <= circleLeft+30-j && mouseZ <= circleTop+i+1) {
        		return true;
        	}
        }
		return false;
	}
	
    @Override
    public void actionPerformed(GuiButton button) {
    	switch(button.id) {
    	case 1:
    		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    		player.openGui(RPGCore.instance, 1, Minecraft.getMinecraft().theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
    		break;
    	}
    }
}
