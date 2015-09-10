package net.iampaddy.socks.message;

/**
 * Created by Paddy on 3/10/2015.
 */
public class SocksMessage {

    public static class AuthRequest {
        private byte version;
        private byte methodNum;
        private byte[] methods;

        public AuthRequest(byte version, byte methodNum, byte[] methods) {
            this.version = version;
            this.methodNum = methodNum;
            this.methods = methods;
        }

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public byte getMethodNum() {
            return methodNum;
        }

        public void setMethodNum(byte methodNum) {
            this.methodNum = methodNum;
        }

        public byte[] getMethods() {
            return methods;
        }

        public void setMethods(byte[] methods) {
            this.methods = methods;
        }
    }

    public static class AuthResponse {
        private byte version;
        private byte method;

        public AuthResponse(byte version, byte method) {
            this.version = version;
            this.method = method;
        }

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public byte getMethod() {
            return method;
        }

        public void setMethod(byte method) {
            this.method = method;
        }
    }

    public static abstract class CommandMessage {
        protected byte version;
        protected byte reserved = 0x00;
        protected byte addressType;
        protected byte[] addressBytes;
        protected String address;
        protected byte[] portBytes; // two byte
        protected int port;

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public byte getReserved() {
            return reserved;
        }

        public byte getAddressType() {
            return addressType;
        }

        public void setAddressType(byte addressType) {
            this.addressType = addressType;
        }

        public byte[] getAddressBytes() {
            return addressBytes;
        }

        public void setAddressBytes(byte[] addressBytes) {
            this.addressBytes = addressBytes;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public byte[] getPortBytes() {
            return portBytes;
        }

        public void setPortBytes(byte[] portBytes) {
            this.portBytes = portBytes;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class CommandRequest extends CommandMessage {
        private byte command;

        public CommandRequest(byte version, byte command, byte addressType, byte[] addressBytes, byte[] portBytes) {
            this.version = version;
            this.command = command;
            this.addressType = addressType;
            this.addressBytes = addressBytes;
            this.portBytes = portBytes;
            if (addressType == (byte) 1 || addressType == (byte) 3 || addressType == (byte) 4) {
                this.address = new String(addressBytes);
                this.port = ((portBytes[0]&0xff)<<8)+(portBytes[1]&0xff);
            }
        }


        public byte getCommand() {
            return command;
        }

        public void setCommand(byte command) {
            this.command = command;
        }
    }

    public static class CommandResponse extends CommandMessage {

        public CommandResponse(byte version, byte reply, byte addressType, byte[] addressBytes, byte[] portBytes) {
            this.version = version;
            this.reply = reply;
            this.addressType = addressType;
            this.addressBytes = addressBytes;
            this.portBytes = portBytes;
        }

        private byte reply;

        public byte getReply() {
            return reply;
        }

        public void setReply(byte reply) {
            this.reply = reply;
        }
    }


}
