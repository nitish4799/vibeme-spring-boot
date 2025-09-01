package net.engineeringdigest.journalApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.engineeringdigest.journalApp.repository.UsersRepository;

@SpringBootTest
public class UserServiceTest  {

    @Autowired
    private UsersRepository usersRepository;

    @Disabled
    @Test
    public void testFindByPhoneNumber(){
        assertNotNull(usersRepository.findByPhoneNumber("7737389719"));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2", "4, 2, 5"
    })
    public void testAdd(int a, int b, int expected) {
        assertEquals(expected, a+b);
    }

}
