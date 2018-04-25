package sonar.logistics.api.wireless;

import java.util.UUID;

import sonar.logistics.api.cabling.INetworkTile;

public interface IWirelessEmitter extends INetworkTile{

	/** can the given player UUID connect to this IDataEmitter */
    EnumConnected canPlayerConnect(UUID uuid);

	/** the emitters name, as chosen by the user */
    String getEmitterName();
	
	WirelessSecurity getSecurity();
}