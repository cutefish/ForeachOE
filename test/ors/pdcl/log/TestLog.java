package ors.pdcl.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestLog {
    private static Log LOG = LogFactory.getLog(TestLog.class);

    public static class Bar {
        private static Log LOG = LogFactory.getLog(Bar.class);

        public void doIt() {
            LOG.fatal("A fatal message");
            LOG.error("An error message");
            LOG.warn("A warning message");
            LOG.info("An info message");
            LOG.debug("A debug message");
            LOG.trace("A trace message");
        }
    }

    public static void main(String args[]) throws Exception {
        LOG.fatal("A fatal message");
        LOG.error("An error message");
        LOG.warn("A warning message");
        LOG.info("An info message");
        LOG.debug("A debug message");
        LOG.trace("A trace message");
        Bar bar = new Bar();
        bar.doIt();
    }
}
