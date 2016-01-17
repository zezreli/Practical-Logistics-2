package sonar.logistics.integration.multipart;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.integration.fmp.SonarHandlerPart;
import sonar.core.integration.fmp.handlers.TileHandler;
import sonar.core.network.utils.ITextField;
import sonar.logistics.Logistics;
import sonar.logistics.api.Info;
import sonar.logistics.api.connecting.IDataConnection;
import sonar.logistics.api.render.ICableRenderer;
import sonar.logistics.client.renderers.RenderHandlers;
import sonar.logistics.common.handlers.DataModifierHandler;
import sonar.logistics.network.LogisticsGui;
import sonar.logistics.registries.BlockRegistry;
import codechicken.lib.vec.Cuboid6;

public class DataModifierPart extends SonarHandlerPart implements IDataConnection, ICableRenderer, ITextField {

	public DataModifierHandler handler = new DataModifierHandler(true);

	public DataModifierPart() {
		super();
	}

	public DataModifierPart(int meta) {
		super(meta);
	}

	@Override
	public TileHandler getTileHandler() {
		return handler;
	}

	@Override
	public boolean canConnect(ForgeDirection dir) {
		return handler.canConnect(dir);
	}

	@Override
	public Info currentInfo() {
		return handler.currentInfo();
	}

	@Override
	public void textTyped(String string, int id) {
		handler.textTyped(string, id);
		
	}

	@Override
	public boolean canRenderConnection(ForgeDirection dir) {
		return handler.canRenderConnection(tile(), dir);
	}
	
	public boolean activate(EntityPlayer player, MovingObjectPosition pos, ItemStack stack) {
		if (player != null) {
			player.openGui(Logistics.instance, LogisticsGui.dataModifier, tile().getWorldObj(), x(), y(), z());
			return true;

		}
		return false;
	}
	
	@Override
	public Cuboid6 getBounds() {
		return new Cuboid6(4 * 0.0625, 4 * 0.0625, 4 * 0.0625, 1 - 4 * 0.0625, 1 - 4 * 0.0625, 1 - 4 * 0.0625);
	}

	@Override
	public Block getBlock() {
		return BlockRegistry.dataModifier;
	}

	@Override
	public String getType() {
		return "Data Modifier";
	}

	@Override
	public Object getSpecialRenderer() {
		return new RenderHandlers.DataModifier();
	}


}
