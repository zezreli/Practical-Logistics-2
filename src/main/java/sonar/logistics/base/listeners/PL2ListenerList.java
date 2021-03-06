package sonar.logistics.base.listeners;

import net.minecraft.entity.player.EntityPlayerMP;
import sonar.core.listener.ISonarListenable;
import sonar.core.listener.ListenableList;
import sonar.core.listener.ListenerList;
import sonar.core.listener.PlayerListener;
import sonar.logistics.core.tiles.displays.DisplayViewerHandler;
import sonar.logistics.core.tiles.displays.gsi.DisplayGSI;

import java.util.ArrayList;
import java.util.List;

/**used on Readers for caching the base connected to their info*/
public class PL2ListenerList extends ListenableList<PlayerListener> {
	
	public ListenerList<DisplayGSI> displayListeners = new ListenerList<>(1);

	public PL2ListenerList(ISonarListenable<PlayerListener> listen, int maxTypes) {
		super(listen, maxTypes);
	}
	
	public List<PlayerListener> getAllListeners(ListenerType... enums) {
		List<PlayerListener> listeners = super.getListeners(enums);
		for (ListenerType type : enums) {
			if (type == ListenerType.OLD_DISPLAY_LISTENER) {
				List<DisplayGSI> displays = displayListeners.getListeners(0);
				List<EntityPlayerMP> players = DisplayViewerHandler.instance().getWatchingPlayers(displays);
				players.forEach(player -> {
					PlayerListener listener = new PlayerListener(player);
					if (!listeners.contains(listener)) {
						listeners.add(listener);
					}
				});
			}
		}
		return listeners;
	}
	
	public List<PlayerListener> getDisplayPlayerListeners(){
		List<PlayerListener> listeners = new ArrayList<>();
		List<DisplayGSI> displays = displayListeners.getListeners(0);
		List<EntityPlayerMP> players = DisplayViewerHandler.instance().getWatchingPlayers(displays);
		players.forEach(player -> {
			PlayerListener listener = new PlayerListener(player);
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		});
		return listeners;
	}
	

	public boolean hasListeners() {
		boolean hasListeners = super.hasListeners();
		if (!hasListeners) {
			List<DisplayGSI> displays = displayListeners.getListeners(0);
			for (DisplayGSI display : displays) {
				List<EntityPlayerMP> players = DisplayViewerHandler.instance().getWatchingPlayers(display);
				if (!players.isEmpty()) {
					return true;
				}
			}
		}
		return hasListeners;
	}
	
	public ListenerList<DisplayGSI> getDisplayListeners() {
		return displayListeners;
	}
	public void invalidateList() {
		super.invalidateList();
		displayListeners.invalidateList();
	}
}
