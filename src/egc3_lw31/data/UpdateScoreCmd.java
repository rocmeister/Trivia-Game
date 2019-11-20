package egc3_lw31.data;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import egc3_lw31.game.model.GameAdapter;
import provided.datapacket.IDataPacketID;

/**
 * Command for updateScore data type.
 */
public class UpdateScoreCmd extends AMessageCmd<IUpdateScoreData>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2178800004717740805L;

	/**
	 * Constructor
	 */	
	public UpdateScoreCmd() {}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IUpdateScoreData> host, Void... params) {
		System.out.print("UpdateScoreCmd apply!!!");
		GameAdapter gameAdpt = this._cmd2ModelAdpt.getMDDData(host.getData().getKey());
		gameAdpt.updatePoints(host.getData().getScore());
		
		return null;
	}
}
