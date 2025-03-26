package com.example.sale.Service;

import com.example.sale.model.Campaign;
import com.example.sale.model.CampaignDiscount;
import com.example.sale.model.History;
import com.example.sale.model.Product;
import com.example.sale.repository.CampaignRepo;
import com.example.sale.repository.HistoryRepo;
import com.example.sale.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Configuration
@EnableScheduling
@Service

public class CampaignService {

    @Autowired
    CampaignRepo campaignRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    HistoryRepo historyRepo;


    public Campaign save(Campaign campaign) {
        for (CampaignDiscount discount : campaign.getCampaignDiscounts()) {
            if (discount.getProduct() != null) {
                Product product = productRepo.findById(discount.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                product.setCurrentPrice(product.getMrp());
                discount.setProduct(product);
            }
            discount.setCampaign(campaign);
        }
        return campaignRepo.save(campaign);
    }


    @Transactional
    @Scheduled(cron = "0 40 18 * * ?")
    public void applyDiscount() {    // badha active campaign ne lese and update karse
        LocalDate currentDate = LocalDate.now();  // aj ni date ne store karse
        List<Campaign> activeCampaigns = campaignRepo.findActiveCampaigns(currentDate);   // badha databasec mathi aj ni date na campaign hase ane fatch karses

        if (activeCampaigns.isEmpty()) {
            System.out.println("No active campaigns found for date: " + currentDate);
            return;

            // camapign nai male to msg print kari des
        }

        for (Campaign campaign : activeCampaigns) {
            System.out.println("Processing Campaign: " + campaign.getTitle());

            for (CampaignDiscount discount : campaign.getCampaignDiscounts()) {
                Product product = discount.getProduct();
                double originalPrice = product.getCurrentPrice();
                double discountAmount = originalPrice * (discount.getDiscount() / 100.0);
                double newPrice = originalPrice - discountAmount;
                // discount lagadi ne new price set karse

                System.out.println("Applying discount on product: " + product.getId());
                System.out.println("Original Price: " + originalPrice + ", Discount: " + discount.getDiscount() + "%, New Price: " + newPrice);

                // Update Product Price
                product.setCurrentPrice(newPrice);
                productRepo.save(product);    // database ma set karse

                // Save Price History for tracking old price
                History history = new History();
                history.setPrice(newPrice);
                history.setDate(currentDate);
                history.setProduct(product);
                historyRepo.save(history);
            }
        }
    }



    @Transactional
    @Scheduled(cron = "0 26 18 * * ?")
    public void endDate() {
        LocalDate currentDate = LocalDate.now();
        List<Campaign> campaignsEndingToday = campaignRepo.findCampaignsEndingToday(currentDate);
           // aj ni date na campaign lavse repo mathi

        if (campaignsEndingToday.isEmpty()) {
            System.out.println("No campaigns ending on: " + currentDate);
            return;    // camapign nai male to return kari deses and methode stop thay jase
        }

        for (Campaign campaign : campaignsEndingToday) {
            for (CampaignDiscount discount : campaign.getCampaignDiscounts()) {
                Product product = discount.getProduct();

                // badha campaign mate tena campaigndiscount levi che and product fetch kariye che

                List<History> historyList = historyRepo.findByProductIdOrderByDate(product.getId());
                // history mathi price hostory laveye che

                if (!historyList.isEmpty()) {
                    double lastPrice = historyList.get(0).getPrice();
                    System.out.println("Restoring original price for product: " + product.getId());
                    System.out.println("Restored Price: " + lastPrice);

                    // history ma price hoi to old product farithi set karse

                    // Restore original price
                    product.setCurrentPrice(lastPrice);
                    productRepo.save(product);

                    // Save Price History for tracking restored price
                    History history = new History();
                    history.setProduct(product);
                    history.setPrice(product.getCurrentPrice());
                    history.setDate(currentDate);
                    historyRepo.save(history);
                }
            }
        }
    }


    public List<History> getPriceForDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return historyRepo.findByDate(localDate);
    }



}
