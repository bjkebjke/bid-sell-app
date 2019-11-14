package com.buysell.demo.job;

import com.buysell.demo.model.Item;
import com.buysell.demo.model.Message;
import com.buysell.demo.repository.ItemRepository;
import com.buysell.demo.repository.MessageRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ExpirationJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationJob.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        Long itemId = jobDataMap.getLong("itemId");

        Item finalizedItem = itemRepository.getOne(itemId);

        // sending message to user

        Message message = new Message();

        if(finalizedItem.getTopBid() == null) {
            message.setReceiver(finalizedItem.getUser());
            message.setText("Your item ," + finalizedItem.getItemName() +", has expired. There was no bid on your item!");
        } else {
            message.setReceiver(finalizedItem.getTopBid().getUser());
            message.setText("You have won bidding for " + finalizedItem.getItemName() + ".");
        }

        Message saved = messageRepository.save(message);
    }
}
