package com.buysell.demo.controller;

import com.buysell.demo.job.ExpirationJob;
import com.buysell.demo.model.Item;
import com.buysell.demo.payload.*;
import com.buysell.demo.security.CurrentUser;
import com.buysell.demo.security.UserPrincipal;
import com.buysell.demo.service.ImageService;
import com.buysell.demo.service.ItemService;
import com.buysell.demo.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping(value = "/api/items")
public class ItemController {

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Scheduler scheduler;

    @GetMapping
    @ResponseBody
    public PagedResponse<ItemResponse> getItems(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return itemService.getAllItems(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postItem(@CurrentUser UserPrincipal currentUser,
                                      @RequestParam(value = "files") MultipartFile[] files,
                                      @RequestParam(value = "itemRequest") String jsonRequest) throws IOException {

        ItemRequest itemRequest = objectMapper.readValue(jsonRequest, ItemRequest.class);

        itemRequest.setUserId(currentUser.getId());
        Item item = itemService.postItem(itemRequest);

        // storing images
        for(MultipartFile image: files) {
            imageService.storeImage(image, item);
        }

        //scheduling Job upon item expiration time
        try {
            JobDetail jobDetail = buildJobDetail(item.getId());
            Trigger trigger = buildJobTrigger(jobDetail, item.getExpirationDateTime());
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling expiration", ex);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(item.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Item Uploaded Successfully"));
    }

    @GetMapping("/{itemId}")
    @ResponseBody
    public ItemResponse getItemById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long itemId) {
        return itemService.getItemById(itemId, currentUser);
    }

    @PostMapping("/{itemId}/bids")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public ItemResponse makeBid(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody BidRequest bidRequest) {
        return itemService.makeBidAndGetUpdatedItem(itemId, bidRequest, currentUser);
    }

    private JobDetail buildJobDetail(Long itemId) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("itemId", itemId);

        return JobBuilder.newJob(ExpirationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "expiration-jobs")
                .withDescription("Notify item bid winner")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Instant expirationTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "expiration-triggers")
                .withDescription("Notify item bid winner Trigger")
                .startAt(Date.from(expirationTime))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
