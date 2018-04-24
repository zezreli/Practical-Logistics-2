package sonar.logistics.api.filters;

import sonar.core.helpers.FluidHelper.ITankFilter;
import sonar.logistics.api.tiles.nodes.BlockConnection;
import sonar.logistics.api.tiles.nodes.INode;
import sonar.logistics.api.tiles.nodes.NodeTransferMode;
import sonar.logistics.api.tiles.nodes.TransferType;

public interface ITransferFilteredTile extends IFilteredTile, INode, ITankFilter {

	NodeTransferMode getTransferMode();
	
	/**if this can transfer the given transfer type. this doesn't disable other nodes transferring to it though, Unless connection is disabled is on.*/
    boolean isTransferEnabled(TransferType type);
	
	void setTransferType(TransferType type, boolean enable);
	
	BlockConnection getConnected();
	
	boolean canConnectToNodeConnection();
	
	void incrementTransferMode();
}
