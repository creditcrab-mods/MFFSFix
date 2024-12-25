package mffs.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.vector.Vector3;

public class PacketFxs implements IMessage {
    Vector3 pos;
    NBTTagCompound data;

    public PacketFxs() {
        this(null, null);
    }

    public PacketFxs(Vector3 pos, NBTTagCompound data) {
        this.pos = pos;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound nbt = null;
        try {
            nbt = CompressedStreamTools.read(
                new DataInputStream(new ByteBufInputStream(buf))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.pos = Vector3.readFromNBT(nbt);
        this.data = nbt.getCompoundTag("data");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound nbt = new NBTTagCompound();

        this.pos.writeToNBT(nbt);
        nbt.setTag("data", this.data);

        try {
            CompressedStreamTools.write(
                nbt, new DataOutputStream(new ByteBufOutputStream(buf))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
