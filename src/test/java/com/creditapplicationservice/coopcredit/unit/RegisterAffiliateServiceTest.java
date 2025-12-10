package com.creditapplicationservice.coopcredit.unit;

import com.creditapplicationservice.coopcredit.application.RegisterAffiliateService;
import com.creditapplicationservice.coopcredit.domain.exception.DomainException;
import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.domain.port.outbound.AffiliateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterAffiliateServiceTest {

    private AffiliateRepositoryPort affiliateRepository;
    private RegisterAffiliateService service;

    @BeforeEach
    void setup() {
        affiliateRepository = Mockito.mock(AffiliateRepositoryPort.class);
        service = new RegisterAffiliateService(affiliateRepository);
    }

    @Test
    void register_validAffiliate_success() {
        Affiliate a = new Affiliate();
        a.setDocument("123");
        a.setName("John");
        a.setSalary(1000);

        when(affiliateRepository.findByDocument("123")).thenReturn(Optional.empty());
        when(affiliateRepository.save(any(Affiliate.class))).thenAnswer(inv -> inv.getArgument(0));

        Affiliate saved = service.register(a);
        assertNotNull(saved);
        assertEquals("123", saved.getDocument());
        verify(affiliateRepository).save(any(Affiliate.class));
    }

    @Test
    void register_duplicateDocument_throws() {
        Affiliate a = new Affiliate();
        a.setDocument("123");
        when(affiliateRepository.findByDocument("123")).thenReturn(Optional.of(new Affiliate()));
        DomainException ex = assertThrows(DomainException.class, () -> service.register(a));
        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    void register_invalidSalary_throws() {
        Affiliate a = new Affiliate();
        a.setDocument("321");
        a.setSalary(0);
        when(affiliateRepository.findByDocument("321")).thenReturn(Optional.empty());
        DomainException ex = assertThrows(DomainException.class, () -> service.register(a));
        assertTrue(ex.getMessage().contains("salario"));
    }
}

