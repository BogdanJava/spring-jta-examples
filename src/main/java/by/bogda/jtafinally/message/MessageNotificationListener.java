package by.bogda.jtafinally.message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import static by.bogda.jtafinally.JtaFinallyApplication.DESTINATION;

/**
 * @author Bogdan Shishkin
 * project: jta-finally
 * date/time: 17/06/2018 / 15:12
 * email: bogdanshishkin1998@gmail.com
 */

@Service
public class MessageNotificationListener {

    @JmsListener(destination = DESTINATION)
    public void onMessage(String id) {
        System.out.println("Message id: " + id);
    }
}
