package net.iampaddy.socks;

import net.iampaddy.socks.acceptor.Acceptor;
import net.iampaddy.socks.connector.Connector;
import net.iampaddy.socks.dns.DNSResolver;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Properties;

public class Configuration {

    private Properties props;
    private List<Connector> connector;
    private DNSResolver resolver;
    private List<Acceptor> acceptor;

    public Configuration(Properties props) {
        this.props = props;
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = props.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, Boolean.FALSE);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = props.getProperty(key);
        if (StringUtils.equalsIgnoreCase("TRUE", value) || StringUtils.equalsIgnoreCase("T", value)
                || StringUtils.equalsIgnoreCase("YES", value) || StringUtils.equalsIgnoreCase("Y", value)) {
            return Boolean.TRUE;
        } else if (StringUtils.equalsIgnoreCase("FALSE", value) || StringUtils.equalsIgnoreCase("F", value)
                || StringUtils.equalsIgnoreCase("NO", value) || StringUtils.equalsIgnoreCase("N", value)) {
            return Boolean.FALSE;
        }
        throw new RuntimeException("Invalid value for key " + key + ":" + value);
    }

    public String getMode() {
        return "";
    }

    public List<Connector> getConnector() {
        return connector;
    }

    public DNSResolver getDNSResolver() {
        return resolver;
    }

    public List<Acceptor> getAcceptor() {
        return acceptor;
    }
}
