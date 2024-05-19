package capstone.restaurant.service;

import capstone.restaurant.entity.Vote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final MailSender mailSender;

    public void sendEmail(Vote vote) {
        SimpleMailMessage msg = new SimpleMailMessage();
        String address = "http://www.test.com/vote/";
        msg.setTo(vote.getEmail());
        msg.setSubject(vote.getTitle() + "가 만료되었습니다.");
        msg.setText(address + vote.getVoteHash());
        msg.setFrom(senderEmail);
        try{
            mailSender.send(msg);
            log.info(vote.getVoteHash() + "의 결과가 " + vote.getEmail() + "로 보내졌습니다.");
        } catch(Error e) {
            log.error(vote.getVoteHash() + "투표의 결과가 정상적으로 보내지 않았습니다.");
            throw e;
        }
    }
}
