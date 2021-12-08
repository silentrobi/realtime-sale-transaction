package com.example.mohammadabumusarabiul.integration;

import com.example.mohammadabumusarabiul.dataaccessobject.SaleRepository;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SaleControllerRestIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SaleRepository saleRepository;

    @Before
    public void clearStorage(){
        saleRepository.deleteAll();
    }

    @Test
    public void whenValidSalesAmount_thenAccept_newSale() throws Exception {
        mvc.perform(post("/sales").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("sales_amount", "100.0"))
                .andExpect(status().isAccepted());

    }

    @Test
    public void whenInValidSalesAmount_thenShowError_forNewSale() throws Exception {
        mvc.perform(post("/sales").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("sales_amount", ""))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/sales").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("sales_amount", "-1.0"))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/sales").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("sales_amount", "asdad"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void whenRequested_returnSaleStatistics() throws Exception {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 210.0));

        for(SaleDO saleDO : sales){
            saleRepository.upsert(saleDO.getId(),saleDO);
        }

        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_sales_amount", is("410.00")))
                .andExpect(jsonPath("$.average_amount_per_order", is("102.50")));
    }
}
