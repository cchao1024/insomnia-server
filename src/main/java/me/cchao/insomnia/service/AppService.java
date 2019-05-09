package me.cchao.insomnia.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.cchao.insomnia.dao.FeedBack;
import me.cchao.insomnia.repository.FeedBackRepository;
import me.cchao.insomnia.security.SecurityHelper;

@Service
public class AppService {

    @Autowired
    FeedBackRepository mRepository;

    public void feedBack(String content, String email) {
        if (StringUtils.isEmpty(email)) {
            email= SecurityHelper.getUser().getEmail();
        }
        FeedBack feedBack=new FeedBack();
        feedBack.setContent(content)
            .setEmail(email)
            .setUserId(SecurityHelper.getUserId());

        mRepository.save(feedBack);
    }
}
