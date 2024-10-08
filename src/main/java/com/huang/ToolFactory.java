package com.huang;

import dev.coolrequest.tool.CoolToolPanel;
import dev.coolrequest.tool.ToolPanelFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author lei.huang
 * @Description TODO
 **/
public class ToolFactory implements ToolPanelFactory {

	@Override
	public CoolToolPanel createToolPanel() {
		return new BreakerCoolToolPanel();
	}

}
