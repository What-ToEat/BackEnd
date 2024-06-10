package capstone.restaurant.service;

import capstone.restaurant.entity.Vote;
import capstone.restaurant.entity.VoteOption;
import capstone.restaurant.entity.Voter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        msg.setText(generateEmailBody(vote));
        msg.setFrom(senderEmail);
        try{
            mailSender.send(msg);
            log.info(vote.getVoteHash() + "의 결과가 " + vote.getEmail() + "로 보내졌습니다.");
        } catch(Exception e) {
            log.error(vote.getVoteHash() + "투표의 결과가 정상적으로 보내지 않았습니다.");
            log.error(e.getMessage());
        }
    }

    private String generateEmailBody(Vote vote) {
        StringBuilder body = new StringBuilder();
        body.append("Title: ").append(vote.getTitle()).append("\n");
        body.append("Expire At: ").append(vote.getExpireAt()).append("\n\n");
        body.append("Vote Results:\n");

        List<VoteOption> voteOptions = vote.getVoteOptions();
        for (VoteOption option : voteOptions) {
            body.append(option.getRestaurant().getName()).append(": ")
                    .append(option.getVoteResults().size()).append(" votes\n");
        }

        body.append("\nVoters:\n");
        List<Voter> voters = vote.getVoters();
        for (Voter voter : voters) {
            body.append(voter.getNickname()).append("\n");
        }

        return body.toString();
    }
}
