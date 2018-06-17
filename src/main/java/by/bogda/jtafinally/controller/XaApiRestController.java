package by.bogda.jtafinally.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static by.bogda.jtafinally.JtaFinallyApplication.DESTINATION;

/**
 * @author Bogdan Shishkin
 * project: jta-finally
 * date/time: 17/06/2018 / 14:49
 * email: bogdanshishkin1998@gmail.com
 */

@RestController
public class XaApiRestController {

    private final JmsTemplate jmsTemplate;
    private final JdbcTemplate jdbcTemplate;

    public XaApiRestController(JmsTemplate jmsTemplate, JdbcTemplate jdbcTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @PostMapping
    public void write(@RequestBody Map<String, String> body,
                      @RequestParam Optional<Boolean> rollback) {
        String id = UUID.randomUUID().toString();
        String name = body.get("name");
        String message = "Hello, " + name;
        jdbcTemplate.update("insert into MESSAGE(ID, MESSAGE) VALUES(?, ?)", id, message);
        jmsTemplate.convertAndSend(DESTINATION, message);
        if (rollback.orElse(false)) {
            throw new RuntimeException("Couldn't write the message");
        }
    }

    @GetMapping
    public Collection<Map<String, String>> read() {
        return jdbcTemplate.query("select * from MESSAGE", (resultSet, i) -> {
            Map<String, String> message = new HashMap<>();
            message.put("id", resultSet.getString("ID"));
            message.put("message", resultSet.getString("MESSAGE"));
            return message;
        });
    }
}
