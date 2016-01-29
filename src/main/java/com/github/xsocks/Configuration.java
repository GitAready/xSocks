package com.github.xsocks;

import com.github.xsocks.acceptor.Acceptor;
import com.github.xsocks.connector.Connector;
import com.github.xsocks.connector.InVMConnector;
import com.github.xsocks.connector.ConnectorManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private Properties props;
    private List<Connector> connector;
    private ConnectorManager resolver;
    private List<Acceptor> acceptor;

    public Configuration(Properties props) {
        this.props = props;

        this.connector = new ArrayList<>();
        this.connector.add(new InVMConnector(this));
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

    public ConnectorManager getDNSResolver() {
        return resolver;
    }

    public List<Acceptor> getAcceptor() {
        return acceptor;
    }
}
