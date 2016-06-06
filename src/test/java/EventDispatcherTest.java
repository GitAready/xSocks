import com.github.xsocks.core.EventDispatcher;
import com.github.xsocks.event.Event;
import com.github.xsocks.event.EventProcessor;
import org.junit.Test;

/**
 * Created by pxie on 2/6/16.
 */
public class EventDispatcherTest {

    @Test
    public void testDispatch() {
        EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.register(TestEvent.class, new TestEventProcessor());
        dispatcher.start();

        dispatcher.dispatch(new TestEvent());
    }


    class TestEvent implements Event<Object> {

        @Override
        public Object getSource() {
            return this;
        }
    }

    class TestEventProcessor implements EventProcessor<TestEvent> {

        @Override
        public void process(TestEvent event) {

        }

        @Override
        public void failure(TestEvent event, Throwable t) {

        }
    }

    class Event2 implements  Event<Object> {

        @Override
        public Object getSource() {
            return this;
        }
    }

    class TestEventProcessor2 implements EventProcessor<Event2> {

        @Override
        public void process(Event2 event) {

        }

        @Override
        public void failure(Event2 event, Throwable t) {

        }
    }
}
