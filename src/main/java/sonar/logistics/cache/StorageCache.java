package sonar.logistics.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.SonarCore;
import sonar.core.api.BlockCoords;
import sonar.core.api.FluidHandler;
import sonar.core.api.InventoryHandler.StorageSize;
import sonar.core.api.SonarAPI;
import sonar.core.api.StoredFluidStack;
import sonar.core.api.StoredItemStack;
import sonar.logistics.api.LogisticsAPI;
import sonar.logistics.api.cache.CacheTypes;
import sonar.logistics.api.cache.IStorageCache;
import sonar.logistics.api.connecting.IEntityNode;
import sonar.logistics.api.wrappers.FluidWrapper.StorageFluids;
import sonar.logistics.api.wrappers.ItemWrapper.StorageItems;

public abstract class StorageCache implements IStorageCache{
	
	public StorageItems getCachedItems() {
		ArrayList<StoredItemStack> storedStacks = new ArrayList();
		StorageSize storage = new StorageSize(0, 0);

		Map<BlockCoords, ForgeDirection> blocks = getExternalBlocks(true);
		if (!blocks.isEmpty()) {
			storage = LogisticsAPI.getItemHelper().getTileInventory(storedStacks, storage, blocks);
		} else {
			TileEntity tile = getFirstTileEntity(CacheTypes.ENTITY_NODES);
			if (tile != null && tile instanceof IEntityNode) {
				storage = LogisticsAPI.getItemHelper().getEntityInventory(storedStacks, storage, ((IEntityNode) tile).getEntities());
			}
		}
		return new StorageItems(storedStacks, storage);
	}

	public StorageFluids getCachedFluids() {
		ArrayList<StoredFluidStack> fluidList = new ArrayList();
		StorageSize storage = new StorageSize(0, 0);
		List<FluidHandler> providers = SonarCore.fluidProviders.getObjects();
		LinkedHashMap<BlockCoords, ForgeDirection> blocks = getExternalBlocks(true);
		for (FluidHandler provider : providers) {
			for (Map.Entry<BlockCoords, ForgeDirection> entry : blocks.entrySet()) {
				TileEntity fluidTile = entry.getKey().getTileEntity();
				if (fluidTile != null && provider.canHandleFluids(fluidTile, entry.getValue())) {
					List<StoredFluidStack> info = new ArrayList();
					StorageSize size = provider.getFluids(info, fluidTile, entry.getValue());
					storage.addItems(size.getStoredFluids());
					storage.addStorage(size.getMaxFluids());
					for (StoredFluidStack fluid : info) {
						SonarAPI.getFluidHelper().addFluidToList(fluidList, fluid);
					}
				}
			}
		}
		return new StorageFluids(fluidList, storage);
	}
}
