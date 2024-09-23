package net.wavemc.core.storage;

import lombok.Data;
import net.wavemc.core.bukkit.WaveBukkit;

import java.sql.SQLException;

@Data
public abstract class Storage {

      private final String name;
      
      public boolean equals(Object o) {
        if (o == this)
          return true; 
        if (!(o instanceof Storage))
          return false; 
        Storage other = (Storage)o;
        if (!other.canEqual(this))
          return false; 
        Object this$name = getName(), other$name = other.getName();
        return !((this$name == null) ? (other$name != null) : !this$name.equals(other$name));
      }
      
      protected boolean canEqual(Object other) {
        return other instanceof Storage;
      }
      
      public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $name = getName();
        return result * 59 + (($name == null) ? 43 : $name.hashCode());
      }
      
      public String toString() {
        return "Storage(name=" + getName() + ")";
      }
      
      public String getName() {
        return this.name;
      }
      
      public Storage(String name) {
        this.name = name;
      }
      
      public final void createTables() {
        try (StorageConnection storageConnection = newConnection()) {
          if (WaveBukkit.getInstance().getConfig().getBoolean("mysql.enable")) {
            storageConnection.execute("create table if not exists wave_pvp (ID varchar(36) not null, name varchar(30), kills int, deaths int, killstreak int, coins int , xp int , killsfps int , winssumo int , losessumo int , kssumo int , wins1v1 int , deaths1v1 int , ks1v1 int ,  deathsfps int, passouchallenge int,  primary key (ID))");
          } else {
            storageConnection.execute("create table if not exists wave_pvp (ID varchar(36) not null, name varchar(30) not null, kills int not null, deaths int not null, killstreak int not null, coins int not null, xp int not null, killsfps int not null, winssumo int not null, losessumo int not null, kssumo int not null, wins1v1 int not null, deaths1v1 int not null, ks1v1 int not null,  deathsfps int not null,  primary key (ID))");
            storageConnection.execute("create table if not exists wave_stats (" +
                    "ID varchar(36) not null, " +
                    "name varchar(30), " +
                    "tag varchar(30), " +
                    "tell boolean, " +
                    "primary key (ID))");
          } 
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      }
      
      public abstract StorageConnection newConnection() throws SQLException;
    }



