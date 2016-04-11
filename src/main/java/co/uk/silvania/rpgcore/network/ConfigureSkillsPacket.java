package co.uk.silvania.rpgcore.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigureSkillsPacket implements IMessage {
	
	int slotId;
	
	int showXP;
	int xpTextType;
	int xpBarPosition;
	int xpXOffset;
	int xpZOffset;
	
	public ConfigureSkillsPacket() {}
	
	public ConfigureSkillsPacket(int slotId, int showXP, int xpTextType, int xpBarPosition, int xpXOffset, int xpZOffset) {
		this.slotId = slotId;
		this.showXP = showXP;
		this.xpTextType = xpTextType;
		this.xpBarPosition = xpBarPosition;
		this.xpXOffset = xpXOffset;
		this.xpZOffset = xpZOffset;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slotId = buf.readInt();
		showXP = buf.readInt();
		xpTextType = buf.readInt();
		xpBarPosition = buf.readInt();
		xpXOffset = buf.readInt();
		xpZOffset = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slotId);
		buf.writeInt(showXP);
		buf.writeInt(xpTextType);
		buf.writeInt(xpBarPosition);
		buf.writeInt(xpXOffset);
		buf.writeInt(xpZOffset);
	}
	
	public static class Handler implements IMessageHandler<ConfigureSkillsPacket, IMessage> {

		@Override
		public IMessage onMessage(ConfigureSkillsPacket message, MessageContext ctx) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
