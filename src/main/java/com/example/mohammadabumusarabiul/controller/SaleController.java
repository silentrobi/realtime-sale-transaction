package com.example.mohammadabumusarabiul.controller;


import com.example.mohammadabumusarabiul.datatransferobject.SaleDTO;
import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.service.DefaultSaleService;
import com.example.mohammadabumusarabiul.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Sale related operations will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(final SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping(path = "/sales", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void addSale(@Valid SaleDTO saleDTO) {
        saleService.addSale(saleDTO.getSalesAmount());
    }

    @GetMapping("/statistics")
    public SaleStatisticDTO getSalesStatistics() {
        return saleService.getSalesStatistics();
    }
}
