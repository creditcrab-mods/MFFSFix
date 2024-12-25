package mffs;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import universalelectricity.core.vector.Vector3;

public class ManipulatorHelper {
    public static void setBlockSneaky(
        final World world,
        final Vector3 position,
        final Block id,
        final int metadata,
        final TileEntity tileEntity
    ) {
        final Chunk chunk
            = world.getChunkFromChunkCoords(position.intX() >> 4, position.intZ() >> 4);
        final Vector3 chunkPosition = new Vector3(
            position.intX() & 0xF, position.intY() & 0xF, position.intZ() & 0xF
        );
        final int heightMapIndex = chunkPosition.intZ() << 4 | chunkPosition.intX();
        if (position.intY() >= chunk.precipitationHeightMap[heightMapIndex] - 1) {
            chunk.precipitationHeightMap[heightMapIndex] = -999;
        }
        final int heightMapValue = chunk.heightMap[heightMapIndex];
        world.removeTileEntity(position.intX(), position.intY(), position.intZ());
        ExtendedBlockStorage extendedBlockStorage
            = chunk.getBlockStorageArray()[position.intY() >> 4];
        if (extendedBlockStorage == null) {
            extendedBlockStorage = new ExtendedBlockStorage(
                position.intY() >> 4 << 4, !world.provider.hasNoSky
            );
            chunk.getBlockStorageArray()[position.intY() >> 4] = extendedBlockStorage;
        }
        extendedBlockStorage.func_150818_a(
            chunkPosition.intX(), chunkPosition.intY(), chunkPosition.intZ(), id
        );
        extendedBlockStorage.setExtBlockMetadata(
            chunkPosition.intX(), chunkPosition.intY(), chunkPosition.intZ(), metadata
        );
        if (position.intY() >= heightMapValue) {
            chunk.generateSkylightMap();
        } else {
            if (chunk.func_150808_b(
                    chunkPosition.intX(), position.intY(), chunkPosition.intZ()
                )
                > 0) {
                if (position.intY() >= heightMapValue) {
                    relightBlock(
                        chunk, Vector3.add(chunkPosition, new Vector3(0.0, 1.0, 0.0))
                    );
                }
            } else if (position.intY() == heightMapValue - 1) {
                relightBlock(chunk, chunkPosition);
            }
            propagateSkylightOcclusion(chunk, chunkPosition);
        }
        chunk.isModified = true;
        world.func_147451_t(position.intX(), position.intY(), position.intZ());
        if (tileEntity != null) {
            world.setTileEntity(
                position.intX(), position.intY(), position.intZ(), tileEntity
            );
        }
        world.markBlockForUpdate(position.intX(), position.intY(), position.intZ());
    }

    public static void relightBlock(final Chunk chunk, final Vector3 position) {
        chunk.relightBlock(position.intX(), position.intY(), position.intZ());
    }

    public static void
    propagateSkylightOcclusion(final Chunk chunk, final Vector3 position) {
        chunk.propagateSkylightOcclusion(position.intX(), position.intZ());
    }
}
