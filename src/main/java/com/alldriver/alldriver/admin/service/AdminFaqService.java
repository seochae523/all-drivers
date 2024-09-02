package com.alldriver.alldriver.admin.service;

import com.alldriver.alldriver.faq.dto.request.FaqSaveRequestDto;
import com.alldriver.alldriver.faq.dto.request.FaqUpdateRequestDto;
import com.alldriver.alldriver.faq.dto.response.FaqFindResponseDto;

import java.util.Date;
import java.util.List;

public interface AdminFaqService {
    String saveFaq(FaqSaveRequestDto faqSaveRequestDto);
    String updateFaq(FaqUpdateRequestDto faqUpdateRequestDto);
    String deleteFaq(String id, Date createdAt);
}
