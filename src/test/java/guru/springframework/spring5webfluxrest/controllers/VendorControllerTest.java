package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {

    VendorRepository vendorRepository;
    VendorController vendorController;
    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Gabriel").lastName("Rabelo").build(),
                        Vendor.builder().firstName("Rabelo").lastName("Gabriel").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void findById() {
        given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().lastName("Rabelo").firstName("Gabriel").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class);
    }

    @Test
    public void testCreateVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToCreateMono = Mono.just(Vendor.builder().firstName("Gabriel").lastName("Rabelo").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToCreateMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testVendorUpdate() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Gabriel").lastName("Rabelo").build());

        webTestClient.put()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void testPatchWithChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().firstName("Gabriel").lastName("Rabelo").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToPatchMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    public void testPatchWithNoChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToPatchMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, never()).save(any());
    }
}