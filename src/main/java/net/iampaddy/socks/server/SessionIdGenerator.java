package net.iampaddy.socks.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by pxie on 1/28/16.
 */
public class SessionIdGenerator {

    public static String generate() {

        StringBuilder sessionId = new StringBuilder();

        Random random = new Random();
        random.setSeed(random.hashCode() ^ Runtime.getRuntime().freeMemory() ^ random.nextLong());

        byte b;
        for (int i = 0; i < 5; i++) {
            b = (byte) random.nextInt(256);
            sessionId.append(Character.forDigit(b >> 4 & 0x0F, 16));
            sessionId.append(Character.forDigit(b & 0x0F, 16));
        }

        return new String(sessionId);
    }

}
