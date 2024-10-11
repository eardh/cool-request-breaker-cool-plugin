package com.huang;

import com.huang.net.MessageServer;
import com.sun.tools.attach.*;
import dev.coolrequest.tool.CoolToolPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author lei.huang
 * @Description TODO
 **/
public class BreakerCoolToolPanel implements CoolToolPanel {

	public static String agentJar = "cool-request-agent.jar";

	@Override
	public JPanel createPanel() {
		AtomicBoolean cracked = BreakContext.newBreakContext().getCracked();

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		JButton button = new JButton();
		button.addActionListener(e -> {
			if (!cracked.get()) {
				breakCoolRequest();
			}
		});

		MessageServer.getServer().setCallback(r -> {
			if (Integer.valueOf(1).equals(r)) {
				button.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icons/logo-s2-b.png"))));
				button.setEnabled(false);
				cracked.set(true);
				return true;
			}
			return false;
		});

		button.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icons/logo-s2.png"))));
		button.setText("悟空");
		button.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		button.setFont(new Font(null, Font.BOLD, 16));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setToolTipText("点击破解 cool-request VIP");
		if (cracked.get()) {
			button.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icons/logo-s2-b.png"))));
			button.setEnabled(false);
		}

		panel.add(button, gbc);
		return panel;
	}

	@Override
	public void showTool() {
		if (BreakContext.newBreakContext().getCracked().get()) {
			return;
		}
		copyFile(getClass().getResource("/breakLib/" + agentJar));
		MessageServer.getServer().start();
	}

	@Override
	public void closeTool() {
		MessageServer.getServer().stop();
		File tempLibFile = getTempLibFile(agentJar);
		if (!tempLibFile.exists()) {
			return;
		}
		try {tempLibFile.deleteOnExit();} catch (Throwable ignored) {}
	}

	private void breakCoolRequest() {
		File file = getTempLibFile(agentJar);

		List<VirtualMachineDescriptor> list = VirtualMachine.list();
		for (VirtualMachineDescriptor descriptor : list) {
			if (!"com.intellij.idea.Main".equals(descriptor.displayName())) {
				continue;
			}
			int port = MessageServer.getPort();
			try {
				VirtualMachine virtualMachine = VirtualMachine.attach(descriptor);
				virtualMachine.loadAgent(file.getAbsolutePath(), "port=" + port);
				virtualMachine.detach();
			} catch (AttachNotSupportedException | IOException | AgentLoadException |
			         AgentInitializationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private File getTempLibFile(String fileName) {
		return new File(System.getProperty("java.io.tmpdir"), fileName);
	}

	private File copyFile(URL url) {
		File copyFile = getTempLibFile(agentJar);
		if (copyFile.exists() && !copyFile.delete()) {
			return copyFile;
		}
		try (InputStream inputStream = url.openStream();
		     FileOutputStream outputStream = new FileOutputStream(copyFile)) {
			byte[] buffer = new byte[10240];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return copyFile;
	}

}
