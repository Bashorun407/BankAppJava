package com.akinovapp.bankapp.email.emailService;

import com.akinovapp.bankapp.email.emailDto.EmailDetails;

public interface EmailService {


    String sendSimpleEmail(EmailDetails emailDetails);
    String sendEmailWithAttachment(EmailDetails emailDetails);
}
