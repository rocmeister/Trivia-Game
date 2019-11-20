package egc3_lw31.data;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import provided.datapacket.IDataPacketID;

/**
 * Command for submitting game score
 */
public class SubmitGameScoreCmd extends AMessageCmd<ISubmitGameScoreData>{
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 5510019526198937640L;

	/**
	 * Constructor
	 */	
	public SubmitGameScoreCmd() {}

	@SuppressWarnings("unchecked")
	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<ISubmitGameScoreData> host, Void... params) {
		this._cmd2ModelAdpt.putMDDData(host.getData().getMDDKey(), host.getData().getTeamGroup().getName() + 
				":" + String.valueOf(host.getData().getScore()));
		return null;
	}
}
