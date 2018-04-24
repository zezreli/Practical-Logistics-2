package sonar.logistics.api.networks;

import java.util.Map;

import sonar.core.utils.Pair;
import sonar.logistics.api.info.IInfo;
import sonar.logistics.api.info.InfoUUID;
import sonar.logistics.api.lists.types.AbstractChangeableList;
import sonar.logistics.api.tiles.nodes.NodeConnection;
import sonar.logistics.api.tiles.readers.IListReader;

public interface INetworkListHandler<I extends IInfo, L extends AbstractChangeableList> extends INetworkHandler {

	//public String id();

	int updateRate();

	Class<? extends INetworkListChannels> getChannelsType();
	
	L newChangeableList();

	L updateConnection(INetworkListChannels channels, L list, NodeConnection c);
	
	Map<NodeConnection, L> getAllChannels(Map<NodeConnection, L> list, ILogisticsNetwork network);

	Pair<InfoUUID, AbstractChangeableList<I>> updateAndSendList(ILogisticsNetwork network, IListReader<I> reader, Map<NodeConnection, AbstractChangeableList<I>> channelLists, boolean send);

}
