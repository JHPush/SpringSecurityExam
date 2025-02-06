package com.example.securityapp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.securityapp.domain.Item;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
@Slf4j
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository ir;

    @Test
    @Rollback(value = false)
    public void saveTest(){
        for(int i=0; i<10; i++){
            Item item = new Item();
            item.setName("Item" + i);
            ir.save(item);
        }
    }
    @Test
    public void testFindAll(){
        
    }

}
