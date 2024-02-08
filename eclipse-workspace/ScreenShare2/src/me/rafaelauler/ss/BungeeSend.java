package me.rafaelauler.ss;


import java.io.ByteArrayOutputStream;
import java.util.logging.Level;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeSend {
	
	public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
	    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	 try {
	      out.writeUTF("BungeeTeleport");
	      out.writeUTF(from.getName());
	      out.writeUTF(to.getName());
	      from.getServer().getInfo()
	        .sendData(Main.channel, byteArrayOut.toByteArray());
	      Main.getInstance().getLogger().log(Level.INFO, "Canais do bungee receberam informa��es!");
	    } catch (Exception e) {
	      e.printStackTrace();
	    
	    }}
}

