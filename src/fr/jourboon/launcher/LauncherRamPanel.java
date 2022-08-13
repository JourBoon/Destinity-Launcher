package fr.jourboon.launcher;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;

@SuppressWarnings("serial")
public class LauncherRamPanel extends JPanel implements SwingerEventListener{
	
	private Image background = Swinger.getResource("background.png");
	
	public LauncherRamPanel(LauncherFrame launcherFrame) {
		
	}
	

	@Override
	public void onEvent(SwingerEvent arg0) {

		
	}
	
	@Override
	public void paintComponent(Graphics graph) {
		super.paintComponent(graph);
	    graph.drawImage(this.background, 0, 0, getWidth(), getHeight(), this);
	    
	    this.setBackground(Swinger.TRANSPARENT);
	}

}
