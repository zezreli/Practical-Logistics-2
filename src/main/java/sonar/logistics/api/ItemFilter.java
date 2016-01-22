package sonar.logistics.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.utils.IRegistryObject;
import sonar.logistics.Logistics;
import sonar.logistics.api.Info;

/** used for providing information on Block/TileEntity for the Info Reader to read, the Provider must be registered in the PractialLogisticsAPI to be used */
public abstract class ItemFilter<T extends ItemFilter> implements IRegistryObject {

	public byte getID(){
		return Logistics.tileProviders.getObjectID(getName());		
	}
	
	/** the name the info helper will be registered too */
	public abstract String getName();

	/**
	 * only called if canProvideInfo is true
	 * 
	 * @param infoList current list of information for the block from this Helper, providers only add to this and don't remove.
	 * @param world The World
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param z Z Coordinate
	 */
	public abstract boolean matchesFilter(ItemStack stack);
	
	public abstract boolean equalFilter(ItemFilter stack);
	
	public abstract void writeToNBT(NBTTagCompound tag);
	
	public abstract void readFromNBT(NBTTagCompound tag);
	
	public abstract T instance();

	/** used when the provider is loaded normally used to check if relevant mods are loaded for APIs to work */
	public boolean isLoadable() {
		return true;
	}
	
}