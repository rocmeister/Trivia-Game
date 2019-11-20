package egc3_lw31.data;

import java.awt.Component;
import java.util.function.Supplier;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import map.MapPanel;
import provided.datapacket.IDataPacketID;

/**
 * Command for the client to install to 
 * @author Rocmeister
 *
 */
public class MapCmd extends AMessageCmd<IMapData> {
	/**
	 * Generated ID 
	 */
	private static final long serialVersionUID = 4388015296993331031L;
	
	//private ICmd2ModelAdapter _c2mAdpt;
	
	/**
	 * Constructor
	 */	
	public MapCmd() {}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IMapData> host, Void... params) {
		//image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(filename));
		
		this._cmd2ModelAdpt.addComponentFactory(new Supplier<Component> () {
			@Override
			public Component get() {
				MapPanel _mapPanel = new MapPanel();
				_mapPanel.setPreferredSize(new java.awt.Dimension(600, 400));
				_mapPanel.start();
				return _mapPanel;
			}
		});
		return null;
	}

}
