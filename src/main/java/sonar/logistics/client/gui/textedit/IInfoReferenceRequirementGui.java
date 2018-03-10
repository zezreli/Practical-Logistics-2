package sonar.logistics.client.gui.textedit;

import java.util.List;

import sonar.logistics.api.displays.references.InfoReference;
import sonar.logistics.api.info.InfoUUID;

public interface IInfoReferenceRequirementGui {

	public void onRequirementCompleted(List<InfoReference> selected);
	
}
