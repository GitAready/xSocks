package net.iampaddy.socks.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class MongoAppender extends AppenderBase<ILoggingEvent> {

    private String host;
    private int port;
    private String database;
    private String collection;

    private boolean clear;

    private volatile MongoClient client;
    private MongoDatabase db;

    @Override
    public void stop() {
        super.stop();

        client.close();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {

        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new MongoClient(host, port);
                    db = client.getDatabase(database);

                    if (clear) {
                        db.getCollection(collection).drop();
                    }
                }
            }
        }

        MongoCollection<Document> collection = db.getCollection(this.collection);
        collection.insertOne(new Document()
                .append("Time", new Date(eventObject.getTimeStamp()))
                .append("Thread", eventObject.getThreadName())
                .append("Level", eventObject.getLevel().toString())
                .append("Message", eventObject.getFormattedMessage()));
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }
}
