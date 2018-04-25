package sonar.logistics.api.wireless;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import sonar.core.listener.ISonarListenable;
import sonar.core.listener.PlayerListener;

public interface IWirelessManager<E extends IWirelessEmitter, R extends IWirelessReceiver> extends ISonarListenable<PlayerListener> {

	WirelessConnectionType type();
	
	E getEmitter(int identity);

	R getReceiver(int identity);

	void addViewer(EntityPlayer player);

	void removeViewer(EntityPlayer player);

	void onEmitterSecurityChanged(E emitter, WirelessSecurity oldSetting);

	List<ClientWirelessEmitter> getClientEmitters(EntityPlayer player);
}