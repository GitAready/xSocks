package net.iampaddy.socks.socket;

/**
 * Created by xpjsk on 2015/9/14.
 */
public class DestKey {

    private String address;

    private int port;

    public DestKey(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DestKey destKey = (DestKey) o;

        if (port != destKey.port) return false;
        return address.equals(destKey.address);

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }
}
