package com.sivvg.tradingservices.repository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.dao.DataIntegrityViolationException;

import com.sivvg.tradingservices.model.Status;

@DataJpaTest
public class StatusRepositoryTest {

	 @Autowired
	    private StatusRepository statusRepository;

	    private Status activeStatus;
	    private Status disabledStatus;

	    @BeforeEach
	    public   void setUp() {
	        // Remove deleteAll() to avoid FK conflict
	        activeStatus = statusRepository.findByName("ACTIVE")
	                .orElseGet(() -> statusRepository.save(new Status("ACTIVE")));

	        disabledStatus = statusRepository.findByName("DISABLED")
	                .orElseGet(() -> statusRepository.save(new Status("DISABLED")));
	    }

	    @Test
	    public   void findByName_shouldReturnStatus_whenStatusExists() {
	        Optional<Status> found = statusRepository.findByName("ACTIVE");
	        assertThat(found).isPresent();
	        assertThat(found.get().getName()).isEqualTo("ACTIVE");
	    }

//	    @Test
//	    void findByName_shouldReturnEmpty_whenStatusDoesNotExist() {
//	        Optional<Status> found = statusRepository.findByName("BLOCKED");
//	        assertThat(found).isEmpty();
//	    }
	    
	    @Test
	    public   void findByName_shouldReturnEmpty_whenStatusDoesNotExist() {
	        String randomName = "NON_EXISTENT_" + System.currentTimeMillis();
	        Optional<Status> found = statusRepository.findByName(randomName);
	        assertThat(found).isEmpty();
	    }
	    

	    @Test
	    public  void save_shouldPersistStatus() {
	        String uniqueName = "BLOCKED_" + System.currentTimeMillis();
	        Status blockedStatus = new Status(uniqueName);
	        Status saved = statusRepository.saveAndFlush(blockedStatus);

	        assertThat(saved.getId()).isNotNull();
	        assertThat(saved.getName()).isEqualTo(uniqueName);

	        Optional<Status> found = statusRepository.findByName(uniqueName);
	        assertThat(found).isPresent();
	    }


	    @Test
	    public   void save_shouldThrowException_whenDuplicateName() {
	        Status duplicate = new Status("ACTIVE");

	        assertThatThrownBy(() -> statusRepository.saveAndFlush(duplicate))
	                .isInstanceOf(DataIntegrityViolationException.class);
	    }
	
	
}
