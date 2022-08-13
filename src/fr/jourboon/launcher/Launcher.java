package fr.jourboon.launcher;

import java.io.File;
import java.util.Arrays;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;

public class Launcher {

	public static final GameVersion D_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos D_INFOS = new GameInfos("Destinity", D_VERSION, new GameTweak[]{GameTweak.FORGE});
	public static final File D_DIR = D_INFOS.getGameDir();
	public static final File D_CRASHES_DIR = new File(D_DIR, "crashes");
	
	private static AuthInfos authInfos;
	
	private static Thread updateThread;
	
	public static void auth(String username, String password) throws AuthenticationException{
		authInfos = new AuthInfos(username, "sry", "nope");
	}
	
	public static void update() throws Exception {
		SUpdate su = new SUpdate("http://destinity.fr/supdate/", D_DIR);
		su.addApplication(new FileDeleter());
		
		updateThread = new Thread() {
			
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload() == 0) {
						continue;
						
					}
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);

					
				
					
				}
			}
		};
		updateThread.start();
		
		su.start();
		updateThread.interrupt();
	}
	
	public static void launch() throws LaunchException {
		
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(D_INFOS, GameFolder.BASIC, authInfos);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
        ExternalLauncher launcher = new ExternalLauncher(profile);
        
        Process p = launcher.launch();
        ProcessLogManager manager = new ProcessLogManager(p.getInputStream(), new File(D_DIR, "logs.txt"));
        manager.start();
        try {
        	Thread.sleep(5000L);
        	LauncherFrame.getInstance().setVisible(false);
			p.waitFor();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
        System.exit(0);
	}
	
	public static void interruptThread() {
		updateThread.interrupt();
	}
	
	
}
