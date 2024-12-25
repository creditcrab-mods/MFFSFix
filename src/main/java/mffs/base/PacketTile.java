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

public class PacketTile implements IMessage {
    Type type;
    Vector3 pos;
    NBTTagCompound data;

    public PacketTile(Type type, Vector3 pos, NBTTagCompound data) {
        this.type = type;
        this.pos = pos;
        this.data = data;
    }

    public PacketTile() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            NBTTagCompound nbt = CompressedStreamTools.read(
                new DataInputStream(new ByteBufInputStream(buf))
            );

            this.type = Type.values()[nbt.getInteger("type")];
            this.pos = Vector3.readFromNBT(nbt);
            this.data = nbt.getCompoundTag("data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setInteger("type", this.type.ordinal());
            this.pos.writeToNBT(nbt);
            nbt.setTag("data", this.data);

            CompressedStreamTools.write(
                nbt, new DataOutputStream(new ByteBufOutputStream(buf))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Type {
        NONE,
        FREQUENCY,
        TOGGLE_ACTIVATION,
        TOGGLE_MODE,
        INVENTORY,
        STRING,
    }
}
