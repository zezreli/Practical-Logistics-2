package sonar.logistics.core.tiles.displays.gsi.modes;

import sonar.core.api.utils.BlockInteractionType;
import sonar.logistics.core.tiles.displays.gsi.DisplayGSI;

import java.util.ArrayList;
import java.util.List;

public class GSISelectionMode {

	public DisplayGSI gsi;

	/// element selection mode
	public GSIElementSelection selectionType;
	public List<Integer> selected_identities = new ArrayList<>();
	
	public GSISelectionMode(DisplayGSI gsi) {
		this.gsi = gsi;
	}

	//// ELEMENT SELECTION MODE \\\\

	public void startElementSelectionMode(GSIElementSelection type) {
		selectionType = type;
		selected_identities = new ArrayList<>();
		gsi.isElementSelectionMode = true;
	}

	public void onElementSelected(int containerID, BlockInteractionType type) {
		if (type == BlockInteractionType.RIGHT) {
			if (selected_identities.contains(containerID)) {
				selected_identities.remove(Integer.valueOf(containerID));
			} else {
				selected_identities.add(containerID);
			}
		}
		if (type == BlockInteractionType.SHIFT_LEFT) {
			finishElementSelectionMode(false);
		}

		if (type == BlockInteractionType.SHIFT_RIGHT) {
			if (!selected_identities.isEmpty()) {
				finishElementSelectionMode(true);
			}
		}
	}

	public void finishElementSelectionMode(boolean sendPacket) {
		if (sendPacket)
			selectionType.finishSelection(gsi, selected_identities);
		selectionType = null;
		selected_identities = new ArrayList<>();
		gsi.isElementSelectionMode = false;
	}
}