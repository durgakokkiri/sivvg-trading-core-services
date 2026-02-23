package com.sivvg.tradingservices.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.sivvg.tradingservices.playload.AdminReplyDTO;
import com.sivvg.tradingservices.playload.ContactResponseDTO;
import com.sivvg.tradingservices.playload.ContactUSRequestDto;

@Service
public interface ContactUSService {


    void createContactRequest(ContactUSRequestDto dto, String userId);

    List<ContactResponseDTO> getAllContacts();

    ContactResponseDTO getContactById(Long id);

    void replyToContact(Long id, AdminReplyDTO dto);
}
