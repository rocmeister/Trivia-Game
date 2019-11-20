package egc3_lw31.data;

import egc3_lw31.game.model.GameAdapter;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 *
 */
public interface IUpdateScoreData extends IDataPacketData {
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IUpdateScoreData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}
	
	/**
	 * @return the score to be updated to teammates' view
	 */
	public int getScore();
	
	/**
	 * @return the key associated with the game adapter stored in each game instance's MDD
	 */
	public MixedDataKey<GameAdapter> getKey();
}
