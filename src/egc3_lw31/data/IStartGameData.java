package egc3_lw31.data;

import common.IGroup;
import common.IMember;
import egc3_lw31.game.model.GameAdapter;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Interface for start game data
 */
public interface IStartGameData extends IDataPacketData {
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IStartGameData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}

	/**
	 * Gets the group with all players.
	 * @return group with all players
	 */
	IGroup getFullGroup();
	
	/**
	 * Gets the group with only players on your team.
	 * @return group with only players on your team.
	 */
	IGroup getTeamGroup();

	/**
	 * Gets the provided key for mdd
	 * @return the key for mdd
	 */
	MixedDataKey<GameAdapter> getMDDKey();

	/**
	 * Gets the server stub
	 * @return The server stub
	 */
	IMember getServerStub();
	
	/**
	 * @return the player's index
	 */
	int getPlayerIndex();
}
