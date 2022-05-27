package org.balicki.Ahorcado.model;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utilidad {
    public static UsuarioLoginDTO uLDTO;

    public static void creaUsuario() {
        uLDTO = new UsuarioLoginDTO("root@gmail.com",
                "Root_1234");
    }

    public static String direccionMAC() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return String.valueOf(sb);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
