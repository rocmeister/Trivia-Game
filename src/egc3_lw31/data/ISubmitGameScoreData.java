package egc3_lw31.data;

import common.IGroup;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Interface for start game data
 */
public interface ISubmitGameScoreData extends IDataPacketData {
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ISubmitGameScoreData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}

	/**
	 * Gets the team score being submitted.
	 * @return Score being submitted.
	 */
	int getScore();
	
	/**
	 * Gets the group with only players on your team.
	 * @return group with only players on your team.
	 */
	IGroup getTeamGroup();

	/** 
	 * Gets MDD Key
	 * @return MDD Key that is randomly generated UUID
	 */
	@SuppressWarnings("rawtypes")
	MixedDataKey getMDDKey();
}
