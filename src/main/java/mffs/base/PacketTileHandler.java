package mffs.base;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileHandler implements IMessageHandler<PacketTile, IMessage> {
    @Override
    public IMessage onMessage(PacketTile arg0, MessageContext arg1) {
        World w = arg1.getServerHandler().playerEntity.worldObj;

        TileEntity te = arg0.pos.getTileEntity(w);

        if (!(te instanceof TileEntityBase))
            return null;

        ((TileEntityBase) te).onReceivePacket(arg0.type, arg0.data);

        return null;
    }
}
