package sonar.logistics.client.gui.textedit;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import sonar.logistics.api.displays.DisplayGSI;
import sonar.logistics.api.displays.elements.IInfoRequirement;
import sonar.logistics.api.info.InfoUUID;

public class InfoUUIDRequest implements IInfoRequirement {

	public GuiScreen screen;
	public List<InfoUUID> info;
	public int requestSize;
	
	public InfoUUIDRequest(GuiScreen screen, int requestSize){
		this.screen = screen;
		this.info = Lists.newArrayList();
		this.requestSize = requestSize;
	}

	@Override
	public int getRequired() {
		return requestSize;
	}

	@Override
	public List<InfoUUID> getSelectedInfo() {
		return info;
	}

	@Override
	public void onGuiClosed(List<InfoUUID> selected) {
		if(screen instanceof IInfoUUIDRequirementGui){
			((IInfoUUIDRequirementGui) screen).onRequirementCompleted(selected);
		}
	}

	@Override
	public void doInfoRequirementPacket(DisplayGSI gsi, EntityPlayer player, List<InfoUUID> require, int requirementRef) {}
	
}
