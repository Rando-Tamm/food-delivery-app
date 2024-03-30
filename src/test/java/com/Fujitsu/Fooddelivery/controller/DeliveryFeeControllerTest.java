package com.Fujitsu.Fooddelivery.controller;

import com.Fujitsu.Fooddelivery.controller.DeliveryFeeController;
import com.Fujitsu.Fooddelivery.model.DeliveryFeeResponse;
import com.Fujitsu.Fooddelivery.service.DeliveryFeeCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryFeeControllerTest {

    @Mock
    private DeliveryFeeCalculatorService deliveryFeeCalculatorService;

    @InjectMocks
    private DeliveryFeeController deliveryFeeController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(deliveryFeeController).build();
    }

    @Test
    public void testGetDeliveryFee() throws Exception {
        // Mocking the service response
         Double mockResponse = 5.0; // Mock delivery fee of 5.0


        when(deliveryFeeCalculatorService.calculateDeliveryFee(any(String.class), any(String.class))).thenReturn(mockResponse);

        // Performing the request and verifying the response
        mockMvc.perform(MockMvcRequestBuilders.get("/delivery-fee")
                .param("city", "Tallinn")
                .param("vehicleType", "Car")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryFee").value(5.0));
    }
}

