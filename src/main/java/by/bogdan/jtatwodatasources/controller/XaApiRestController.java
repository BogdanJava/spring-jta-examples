package by.bogdan.jtatwodatasources.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Bogdan Shishkin
 * project: jta-two-datasources
 * date/time: 17/06/2018 / 15:56
 * email: bogdanshishkin1998@gmail.com
 */

@RestController
public class XaApiRestController {

    private final JdbcTemplate first, second;

    public XaApiRestController(@Qualifier("dataSourceFirst") DataSource first,
                               @Qualifier("dataSourceSecond") DataSource second) {
        this.first = new JdbcTemplate(first);
        this.second = new JdbcTemplate(second);
    }

    @GetMapping("/pets")
    public Collection<Map<String, String>> getPets() {
        return first.query("select * from PET", (resultSet, i) -> {
            Map<String, String> pet = new HashMap<>();
            pet.put("id", resultSet.getString("ID"));
            pet.put("name", resultSet.getString("NAME"));
            return pet;
        });
    }

    @GetMapping("/messages")
    public Collection<Map<String, String>> getMessages() {
        return second.query("select * from MESSAGE", (resultSet, i) -> {
            Map<String, String> pet = new HashMap<>();
            pet.put("id", resultSet.getString("ID"));
            pet.put("message", resultSet.getString("MESSAGE"));
            return pet;
        });
    }

    @PostMapping
    @Transactional
    public void write(@RequestBody Map<String, String> payload,
                      @RequestParam Optional<Boolean> rollback) {
        String name = payload.get("name");
        String message = "Hello, " + name;
        first.update("insert into PET(ID, NAME) VALUES (?, ?)",
                UUID.randomUUID().toString(), name);
        second.update("insert into MESSAGE(ID, MESSAGE) values (?, ?)",
                UUID.randomUUID().toString(), message);

        if (rollback.orElse(false)) {
            throw new RuntimeException("Couldn't write to database.");
        }
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        first.update("delete from PET");
        second.update("delete from MESSAGE");
        return ResponseEntity.ok().build();
    }
}
