package common.message;

import provided.datapacket.DataPacketAlgo;
import provided.datapacket.IDataPacketData;

/**
 * The visitor that processes IGroup data packets. All visitors should be of this type. The class helps with type narrowing
 * to allow for easier development and debugging.
 * 
 * @author Joel Webb - jtw5
 * @author Andres Salgado - as100
 */
public class GroupDataPacketAlgo extends DataPacketAlgo<Void, Void> {

	/**
	 * The unique UID of the class.
	 */
	private static final long serialVersionUID = -3883663800071826258L;

	/**
	 * The constructor for the visitor.
	 * 
	 * @param defaultCmd The default command that will be called when a given data type is not recognized.
	 */
	public GroupDataPacketAlgo(AMessageCmd<? extends IDataPacketData> defaultCmd) {
		super(defaultCmd);
	}
	
}
