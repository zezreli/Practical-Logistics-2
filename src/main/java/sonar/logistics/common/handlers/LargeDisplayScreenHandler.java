package sonar.logistics.common.handlers;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.SonarCore;
import sonar.core.integration.fmp.FMPHelper;
import sonar.core.integration.fmp.handlers.TileHandler;
import sonar.core.network.sync.SyncBoolean;
import sonar.core.network.utils.IByteBufTile;
import sonar.core.utils.BlockCoords;
import sonar.core.utils.helpers.NBTHelper.SyncType;
import sonar.logistics.api.StandardInfo;
import sonar.logistics.api.connecting.IDataCable;
import sonar.logistics.api.connecting.ILargeDisplay;
import sonar.logistics.api.render.LargeScreenSizing;
import sonar.logistics.helpers.CableHelper;
import sonar.logistics.helpers.DisplayHelper;
import sonar.logistics.helpers.InfoHelper;

public class LargeDisplayScreenHandler extends DisplayScreenHandler implements ILargeDisplay, IByteBufTile {

	public SyncBoolean isHandler = new SyncBoolean(0);
	public List<BlockCoords> displays = new ArrayList();
	public BlockCoords handler = null;
	public LargeScreenSizing sizing;
	public boolean resetSizing = true;
	public BlockCoords connectedTile = null;

	public LargeDisplayScreenHandler(boolean isMultipart) {
		super(isMultipart);
	}

	@Override
	public void update(TileEntity te) {
		if (!te.getWorldObj().isRemote) {
			if (!isHandler.getBoolean()) {
				return;
			}
			if (resetSizing) {
				sizing = DisplayHelper.getScreenSizing(te);
				SonarCore.sendPacketAround(te, 64, 2);
				resetSizing = false;
			}
			connectedTile = getConnectedTile(te, ForgeDirection.getOrientation(FMPHelper.getMeta(te)));
			if (connectedTile == null || connectedTile.getTileEntity(te.getWorldObj()) == null) {
				this.updateData(te, te, ForgeDirection.getOrientation(FMPHelper.getMeta(te)));
			} else {				
				this.updateData(connectedTile.getTileEntity(te.getWorldObj()), te, ForgeDirection.getOrientation(FMPHelper.getMeta(te)));
			}
		}

	}

	public BlockCoords getConnectedTile(TileEntity te, ForgeDirection dir) {
		if (displays != null) {
			for (BlockCoords coords : displays) {
				TileEntity target = coords.getTileEntity(te.getWorldObj());

				Object connectedTile = CableHelper.getConnectedTile(target, dir.getOpposite());
				connectedTile = FMPHelper.checkObject(connectedTile);

				if (connectedTile != null) {
					return coords;
				}
			}
		}
		return null;

	}

	@Override
	public void writePacket(ByteBuf buf, int id) {
		super.writePacket(buf, id);
		if (id == 2) {
			if (sizing != null) {
				buf.writeBoolean(true);
				sizing.writeToBuf(buf);
			} else {
				buf.writeBoolean(false);
			}
		}
	}

	@Override
	public void readPacket(ByteBuf buf, int id) {
		super.readPacket(buf, id);
		if (id == 2) {
			if (buf.readBoolean()) {
				sizing = LargeScreenSizing.readFromBuf(buf);
			} else {
				sizing = null;
			}
		}
	}

	@Override
	public boolean isHandler() {
		return isHandler.getBoolean();
	}

	@Override
	public List<BlockCoords> getConnectedScreens() {
		if (displays == null) {
			return Collections.EMPTY_LIST;
		}
		return displays;
	}

	@Override
	public BlockCoords getHandlerCoords() {
		return handler;
	}

	@Override
	public void setHandlerCoords(BlockCoords coords) {
		this.handler = coords;
	}

	@Override
	public void setHandler(boolean isHandler) {
		this.isHandler.setBoolean(isHandler);
	}

	@Override
	public void addDisplay(BlockCoords coords) {
		if (displays == null) {
			displays = new ArrayList();
		}
		displays.add(coords);
		resetSizing();
	}

	@Override
	public void removeDisplay(BlockCoords coords) {
		for (BlockCoords display : displays) {
			if (BlockCoords.equalCoords(display, coords)) {
				displays.remove(display);
				resetSizing();
				return;
			}
		}
	}

	@Override
	public void setConnectedScreens(List<BlockCoords> list) {
		if (list == null) {
			this.displays = new ArrayList();
		} else
			this.displays = list;

	}

	@Override
	public void resetSizing() {
		resetSizing = true;
	}

	public void readData(NBTTagCompound nbt, SyncType type) {
		super.readData(nbt, type);
		if (type == SyncType.SAVE || type == SyncType.SYNC) {
			isHandler.readFromNBT(nbt, type);
			if (nbt.hasKey("hCoords")) {
				handler = BlockCoords.readFromNBT(nbt.getCompoundTag("hCoords"));
			}
			if (nbt.hasKey("cCoords")) {
				connectedTile = BlockCoords.readFromNBT(nbt.getCompoundTag("cCoords"));
			}
			if (nbt.hasKey("mxY")) {
				sizing = LargeScreenSizing.readFromNBT(nbt);
			}

			if (displays == null) {
				displays = new ArrayList();
			}
			NBTTagList list = nbt.getTagList("BlockCoords", 10);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound compound = list.getCompoundTagAt(i);
				displays.add(BlockCoords.readFromNBT(compound));

			}

		}
	}

	public void writeData(NBTTagCompound nbt, SyncType type) {
		super.writeData(nbt, type);
		if (type == SyncType.SAVE || type == SyncType.SYNC) {
			isHandler.writeToNBT(nbt, type);
			if (handler != null) {
				NBTTagCompound infoTag = new NBTTagCompound();
				BlockCoords.writeToNBT(infoTag, handler);
				nbt.setTag("hCoords", infoTag);
			}
			if (connectedTile != null) {
				NBTTagCompound infoTag = new NBTTagCompound();
				BlockCoords.writeToNBT(infoTag, connectedTile);
				nbt.setTag("cCoords", infoTag);
			}
			if (sizing != null) {
				sizing.writeToNBT(nbt);
			}

			NBTTagList list = new NBTTagList();
			if (displays != null) {
				for (int i = 0; i < displays.size(); i++) {
					if (displays.get(i) != null) {
						NBTTagCompound compound = new NBTTagCompound();
						BlockCoords.writeToNBT(compound, displays.get(i));
						list.appendTag(compound);
					}
				}
			}
			nbt.setTag("BlockCoords", list);
		}
	}
}
