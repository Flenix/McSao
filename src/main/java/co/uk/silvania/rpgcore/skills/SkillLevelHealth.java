package co.uk.silvania.rpgcore.skills;

import co.uk.silvania.rpgcore.RPGCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkillLevelHealth extends SkillLevelBase implements IExtendedEntityProperties {
	
	public static String staticSkillId; 
	
	public SkillLevelHealth(EntityPlayer player, String skillID) {
		super(skillID);
		staticSkillId = skillID;
		this.xp = 0;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat(skillId + "xp", xp);
		compound.setTag(skillId, nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound nbt = (NBTTagCompound) compound.getTag(skillId);
		xp = nbt.getFloat(skillId + "xp");
	}

	@Override public void init(Entity entity, World world) {}
	
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(SkillLevelHealth.staticSkillId, new SkillLevelHealth(player, staticSkillId));
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(skillId, new SkillLevelHealth((EntityPlayer)event.entity, skillId));
		}
	}

	@Override
	public boolean hasGui() {
		return false;
	}

	@Override
	public String skillName() {
		return "Health";
	}

	@Override public void openGui() {}

	@Override
	public void addRequirements() {
		requiredSkills.add("skillAgility"); 
		requiredSkills.add("skillStrength");
		requiredSkills.add("skillSwords");
		requiredSkills.add("skillJump"); 
	}

	@Override
	public void addDescription() {
		description.add(nameFormat() + "\u00A7l" + skillName());
		description.add("\u00A7oRequirements are just to show how requirements work.");
		description.add("\u00A7oRequirements can never be met.");
		description.add("Each level gives +1 HP");
		description.add("Experience earned by taking damage.");
		description.add("Different damage sources give different XP.");		
	}

	@Override
	public double levelMultiplier() {
		return 1.2;
	}

	@Override
	public ResourceLocation skillIcon() {
		return new ResourceLocation(RPGCore.MODID, "textures/gui/skills.png");
	}

	@Override
	public int iconX() {
		return 60;
	}

	@Override
	public int iconZ() {
		return 0;
	}
	
	@Override
	public int unlockedLevel() {
		return 3;
	}

	@Override
	public void activateSkill(EntityPlayer player, World world) {
		// TODO Auto-generated method stub
		
	}
}
