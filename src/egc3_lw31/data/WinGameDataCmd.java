package egc3_lw31.data;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import provided.datapacket.IDataPacketID;

/**
 * Command for Text Data
 *
 */
public class WinGameDataCmd extends AMessageCmd<IWinGameData>{
	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = -80676366088335491L;

	/**
	 * Constructor 
	 */
	public WinGameDataCmd() {
	}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IWinGameData> host, Void... params) {
		//image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(filename));
		
		this._cmd2ModelAdpt.addComponentFactory(new Supplier<Component> () {
			@Override
			public Component get() {
				JPanel txtPnl = new JPanel();
				JTextArea textArea = new JTextArea(5, 20);
				txtPnl.add(textArea, BorderLayout.CENTER);
				textArea.setText("                " + host.getData().getWinner());
				textArea.setFont(new Font("Verdana",1,20));
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				textArea.setWrapStyleWord(true);
				return txtPnl;
			}
		});
		return null;
	}	
}
