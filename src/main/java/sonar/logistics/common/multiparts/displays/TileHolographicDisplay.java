package sonar.logistics.common.multiparts.displays;

import sonar.logistics.api.tiles.displays.DisplayType;

public class TileHolographicDisplay extends TileDisplayScreen {


	@Override
	public DisplayType getDisplayType() {
		return DisplayType.HOLOGRAPHIC;
	}
}