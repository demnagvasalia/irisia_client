package irisia.altmanager.gui.subguis;

import irisia.altmanager.Alt;
import irisia.Irisia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AccountImport extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	public JButton openButton;

	private JFileChooser fc;

	public AccountImport() {
		this.fc = new JFileChooser();
		this.fc.setFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
		this.openButton = new JButton("Open a File...");
		this.openButton.addActionListener(this);
		add(this.openButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.openButton) {
			int returnVal = this.fc.showOpenDialog(this);
			if (returnVal == 0)
				try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fc.getSelectedFile()))) {
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						String[] arguments = line.split(":");
						for (int i = 0; i < 2; i++)
							arguments[i].replace(" ", "");
						Irisia.getInstance.instanceCollection.getAltManager().getAccounts().add(new Alt(arguments[0], arguments[1], ""));
					}
					Irisia.getInstance.instanceCollection.getAltManager().save();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "An error happened.", "ERROR", 1);
				}
		}
	}

}