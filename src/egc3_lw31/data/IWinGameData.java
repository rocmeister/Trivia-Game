package egc3_lw31.data;

import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;

/**
 * Interface for start game data
 */
public interface IWinGameData extends IDataPacketData {
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IWinGameData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}

	/**
	 * Gets the winning teams.
	 * @return Winning teams.
	 */
	String getWinner();
}
