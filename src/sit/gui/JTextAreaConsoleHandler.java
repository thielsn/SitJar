/**
 * JTextAreaConsoleHandler.java
 *
 * 11-May-2010
 *
 * @author Simon Thiel <simon.thiel@gmx.de>
 *
 * @version $Revision: $ 
 *
 */



package sit.gui;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;

/**
 * class JTextAreaConsoleHandler
 *
 */
public class JTextAreaConsoleHandler extends StreamHandler {

    private JTextArea textArea = null;

    public JTextAreaConsoleHandler(JTextArea textArea) {
        super();
        this.textArea = textArea;
    }

    @Override
    public synchronized void flush() {
        textArea.revalidate();
        
    }

    @Override
    public synchronized void publish(LogRecord record) {
        textArea.append(getFormatter().format(record));
    
    }



}
