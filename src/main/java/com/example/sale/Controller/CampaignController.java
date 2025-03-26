package com.example.sale.Controller;

import com.example.sale.Service.CampaignService;
import com.example.sale.model.Campaign;
import com.example.sale.model.History;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("campaign")

public class CampaignController {

    @Autowired
    CampaignService campaignService;




    @PostMapping("create")
    public ResponseEntity<?> addCampaign(@RequestBody Campaign campaign) {
        try {
            Campaign savedCampaign = campaignService.save(campaign);
            return new ResponseEntity<>(savedCampaign, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add campaign", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("applydiscount")
    public ResponseEntity<String> applyDiscountManually() {
        campaignService.applyDiscount();
        return ResponseEntity.ok("Discount Applied Successfully");
    }


    @GetMapping("restoreprice")
    public ResponseEntity<String> restorePrices() {
        campaignService.endDate();
        return ResponseEntity.ok("price restored");
    }



    @GetMapping
    public List<History> getPriceForDate(@RequestParam String date) {
        return campaignService.getPriceForDate(date);
    }

}
