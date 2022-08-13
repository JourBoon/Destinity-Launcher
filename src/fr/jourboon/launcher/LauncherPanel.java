package fr.jourboon.launcher;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedProgressBar;


@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener{
	
	private Image background = Swinger.getResource("background.png");
	private Saver saver = new Saver(new File(Launcher.D_DIR, "launcher.properties"));
	private JTextField usernameField = new JTextField(saver.get("username"));
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("playButton.png"));
	private STexturedButton closeButton = new STexturedButton(Swinger.getResource("close.png"));
	private STexturedButton webButton = new STexturedButton(Swinger.getResource("web.png"));
	private STexturedButton optionButton = new STexturedButton(Swinger.getResource("option.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("reduce.png"));
	
	private STexturedProgressBar progressBar = new STexturedProgressBar(Swinger.getResource("sword.png"), Swinger.getResource("swordf.png"));
	
	public LauncherFrame launcherFrame;

	
	public LauncherPanel(LauncherFrame launcherFrame) {
		this.launcherFrame = launcherFrame;

		this.setLayout(null);
		
		InputStream is = null;
        Font customFont = null;
        GraphicsEnvironment ge = null;
        is = LauncherPanel.class.getResourceAsStream("resources/Minecraft.ttf"); 
        try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
        ge.registerFont(customFont);
        usernameField.setFont(customFont);
		
		usernameField.setForeground(Color.BLACK);
		usernameField.setFont(usernameField.getFont().deriveFont(22F));
		usernameField.setBounds(475, 485, 310, 45);
		usernameField.setBorder(null);
		usernameField.setOpaque(false);
		this.add(usernameField);
		
		playButton.setBounds(1238, 880);
		playButton.addEventListener(this);
		this.add(playButton);
		
		closeButton.setBounds(1535, 305);
		closeButton.addEventListener(this);
		this.add(closeButton);
		
		hideButton.setBounds(1490, 305);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		
		webButton.setBounds(335, 882);
		webButton.addEventListener(this);
		this.add(webButton);
		
		optionButton.setBounds(515, 882);
		optionButton.addEventListener(this);
		this.add(optionButton);
		
		progressBar.setBounds(640, 244, 634, 162);
		this.add(progressBar);
		
		
		
	}


	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || usernameField.getText().replaceAll(" ", "").length() == 1 || usernameField.getText().replaceAll(" ", "").length() == 2 || usernameField.getText().replaceAll(" ", "").length() == 3) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo valide. Ou un pseudo de plus de 3 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), "");
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter");
						setFieldsEnabled(true);
						return;
				}
					
					
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						LauncherFrame.getCrashReporter().catchError(e, "Impossible de mettre a jour Destinity :/");
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de mettre le jeu a jour ");
						setFieldsEnabled(true);
						return;
				}
					
					try 
					{
						Launcher.launch();
					} catch (LaunchException e) {
						LauncherFrame.getCrashReporter().catchError(e, "Impossible de lancer Destinity :/");
				}
				
				System.out.println("No problem detected.");
				}
			};
		t.start();
		}else if(e.getSource() == closeButton)
            System.exit(0);
		
		else if(e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		
		if (e.getSource() == optionButton) {
			this.launcherFrame.setPanelRam();
            this.launcherFrame.invalidate();
            this.launcherFrame.validate();
            this.launcherFrame.repaint();
        }
		
		
        else if (e.getSource() == this.webButton){
            Desktop d = Desktop.getDesktop();
            try { 
                    d.browse(new URI("https://www.destinity.fr/wiki"));

            } catch (IOException|URISyntaxException localIOException) {}
        }
	}
	
	
	@Override
	public void paintComponent(Graphics graph) {
		super.paintComponent(graph);
	    graph.drawImage(this.background, 0, 0, getWidth(), getHeight(), this);
	    
	    this.setBackground(Swinger.TRANSPARENT);
	}
	
	public void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		playButton.setEnabled(enabled);
		closeButton.setEnabled(enabled);
		optionButton.setEnabled(enabled);
	}
	
	public STexturedProgressBar getProgressBar() {
		return this.progressBar;
	}
	


}
