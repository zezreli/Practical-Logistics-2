package sonar.logistics.networking.displays;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.slot.EnumFaceSlot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sonar.core.integration.multipart.SonarMultipartHelper;
import sonar.logistics.api.networks.ILogisticsNetwork;
import sonar.logistics.api.tiles.displays.ConnectedDisplay;
import sonar.logistics.api.tiles.displays.IDisplay;
import sonar.logistics.api.tiles.displays.ILargeDisplay;
import sonar.logistics.api.tiles.readers.IInfoProvider;
import sonar.logistics.api.viewers.ILogicListenable;

public class DisplayHelper {

	/** in the form of [facing, rotation] */
	public static EnumFacing[] getScreenOrientation(EntityLivingBase placer, EnumFacing side) {
		EnumFacing facing = side;
		EnumFacing rotation = EnumFacing.NORTH;
		if (placer.rotationPitch > 75 || placer.rotationPitch < -75) {
			rotation = placer.getHorizontalFacing().getOpposite();
		} else {
			facing = placer.getHorizontalFacing().getOpposite();
		}
		return new EnumFacing[] { facing, rotation };
	}

	public static List<ILogicListenable> getLocalProviders(IDisplay part, IBlockAccess world, BlockPos pos) {
		List<ILogicListenable> providers = new ArrayList<>();
		if (part instanceof ILargeDisplay) {
			ConnectedDisplay display = ((ILargeDisplay) part).getConnectedDisplay();
			providers = display != null ? display.getLocalProviders(providers) : getLocalProvidersFromDisplay(providers, world, pos, part);
		} else if(part instanceof ConnectedDisplay){
			ConnectedDisplay display = (ConnectedDisplay) part;
			providers = display.getLocalProviders(providers);			
		}else{
			providers = getLocalProvidersFromDisplay(providers, world, pos, part);
		}
		return providers;
	}

	public static List<ILogicListenable> getLocalProvidersFromDisplay(List<ILogicListenable> viewables, IBlockAccess world, BlockPos pos, IDisplay part) {
		ILogisticsNetwork networkCache = part.getNetwork();
		IBlockAccess actualWorld = SonarMultipartHelper.unwrapBlockAccess(world);
		Optional<IMultipartTile> connectedPart = SonarMultipartHelper.getMultipartTile(actualWorld, pos, EnumFaceSlot.fromFace(part.getCableFace()), tile -> true);
		if (connectedPart.isPresent() && connectedPart.get() instanceof IInfoProvider) {
			if (!viewables.contains(connectedPart.get())) {
				viewables.add((IInfoProvider) connectedPart.get());
			}
		} else {
			for (IInfoProvider monitor : networkCache.getGlobalInfoProviders()) {
				if (!viewables.contains(monitor)) {
					viewables.add(monitor);
				}
			}
		}
		return viewables;
	}

}