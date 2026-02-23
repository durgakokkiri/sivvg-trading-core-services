package com.sivvg.tradingservices.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.model.ContactUSRequest;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.AdminReplyDTO;
import com.sivvg.tradingservices.playload.ContactResponseDTO;
import com.sivvg.tradingservices.playload.ContactUSRequestDto;
import com.sivvg.tradingservices.repository.ContactUSRequestRepository;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.EmailService;

@ExtendWith(MockitoExtension.class)
public class ContactUSServiceImplTest {

    @Mock
    private ContactUSRequestRepository contactRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactUSServiceImpl contactUSService;

    // =====================================================
    // ✅ SUCCESS CASE
    // =====================================================
    @Test
    public void createContactRequest_shouldSaveRequestAndSendEmail() {

        // ---------- Arrange ----------
        ContactUSRequestDto dto =
                new ContactUSRequestDto("Need help", "EMAIL");

        User user = new User();
        user.setUserId("USR001");
        user.setEmail("test@gmail.com");

        when(userRepository.findByUserId("USR001"))
                .thenReturn(Optional.of(user));

        when(contactRequestRepository.save(any(ContactUSRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ---------- Act ----------
        contactUSService.createContactRequest(dto, "USR001");

        // ---------- Assert ----------
        verify(userRepository, times(1))
                .findByUserId("USR001");

        verify(contactRequestRepository, times(2))
                .save(any(ContactUSRequest.class));

        verify(emailService, times(1))
                .sendSimpleMessage(
                        eq("test@gmail.com"),
                        any(String.class),
                        any(String.class)
                );
    }
    
    
    @Test
    void testGetAllContacts() {

        ContactUSRequest contact1 = new ContactUSRequest();
        contact1.setId(1L);
        contact1.setMessage("Need help");
        contact1.setStatus("NEW");
        contact1.setAdminReply(null);

        ContactUSRequest contact2 = new ContactUSRequest();
        contact2.setId(2L);
        contact2.setMessage("Callback required");
        contact2.setStatus("IN_PROGRESS");
        contact2.setAdminReply("Working on it");

        when(contactRequestRepository.findAll())
                .thenReturn(Arrays.asList(contact1, contact2));

        List<ContactResponseDTO> result = contactUSService.getAllContacts();

        assertEquals(2, result.size());
        assertEquals("Need help", result.get(0).getMessage());
        assertEquals("IN_PROGRESS", result.get(1).getStatus());

        verify(contactRequestRepository, times(1)).findAll();
    }
    
    @Test
    void testGetContactById_Success() {

        ContactUSRequest contact = new ContactUSRequest();
        contact.setId(1L);
        contact.setMessage("Test message");
        contact.setStatus("NEW");
        contact.setAdminReply(null);

        when(contactRequestRepository.findById(1L))
                .thenReturn(Optional.of(contact));

        ContactResponseDTO result = contactUSService.getContactById(1L);

        assertNotNull(result);
        assertEquals("Test message", result.getMessage());
        assertEquals("NEW", result.getStatus());

        verify(contactRequestRepository, times(1)).findById(1L);
    }
    @Test
    void testGetContactById_NotFound() {

        when(contactRequestRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactUSService.getContactById(1L);
        });

        assertEquals("Contact request not found", exception.getMessage());
    }

    @Test
    void testReplyToContact() {

        ContactUSRequest contact = new ContactUSRequest();
        contact.setId(1L);
        contact.setStatus("NEW");

        AdminReplyDTO replyDTO = new AdminReplyDTO();
        replyDTO.setStatus("RESOLVED");
        replyDTO.setReplyMessage("Issue resolved");

        when(contactRequestRepository.findById(1L))
                .thenReturn(Optional.of(contact));

        contactUSService.replyToContact(1L, replyDTO);

        assertEquals("RESOLVED", contact.getStatus());
        assertEquals("Issue resolved", contact.getAdminReply());

        verify(contactRequestRepository, times(1)).save(contact);
    }

}
