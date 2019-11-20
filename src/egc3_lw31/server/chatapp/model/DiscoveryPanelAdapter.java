package egc3_lw31.server.chatapp.model;

import java.util.function.Consumer;

import provided.discovery.impl.view.IDiscoveryPanelAdapter;

/**
 * Adapter for discovery panel
 *
 */
@SuppressWarnings("rawtypes")
public class DiscoveryPanelAdapter implements IDiscoveryPanelAdapter {

	@Override
	public void connectToDiscoveryServer(String category, boolean watchOnly, Consumer endPtsUpdateFn) {
	}

	@Override
	public void connectToEndPoint(Object selectedValue) {		
	}

}
