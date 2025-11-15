package com.endxel.pvPPartyRemastered;

public class LoggerService {
    public static void warn(String msg){
        try {
            PluginRegistry.getPlugin().getLogger().warning(msg);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public static void info(String msg){
        try {
            PluginRegistry.getPlugin().getLogger().info(msg);
        } catch(Exception e){
            System.out.println(e);
        }
    }

//    public static void error(String msg){
//        try {
//            PluginRegistry.getPlugin().getLogger().(msg);
//        } catch(Exception e){
//            System.out.println(e);
//        }
//    }
}
