package guru.springframework.spring5webfluxrest.boostratp;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0) {
            //load data
            System.out.println("##### LOAD DATA ON BOOTSTRAP #####");

            categoryRepository.save(Category.builder()
                    .description("Fruits").build());

            categoryRepository.save(Category.builder()
                    .description("Nuts").build());

            categoryRepository.save(Category.builder()
                    .description("Breads").build());

            categoryRepository.save(Category.builder()
                    .description("Meats").build());

            categoryRepository.save(Category.builder()
                    .description("Eggs").build());

            System.out.println("Loaded Categories: " + categoryRepository.count().block());

            vendorRepository.save(Vendor.builder()
                    .firstName("Joe")
                    .lastName("Buck")
                    .build());

            vendorRepository.save(Vendor.builder()
                    .firstName("Michael")
                    .lastName("Weston")
                    .build());

            vendorRepository.save(Vendor.builder()
                    .firstName("Jessie")
                    .lastName("Waters")
                    .build());

            vendorRepository.save(Vendor.builder()
                    .firstName("Bill")
                    .lastName("Nershi")
                    .build());

            vendorRepository.save(Vendor.builder()
                    .firstName("Jimmy")
                    .lastName("Buffet")
                    .build());

            System.out.println("Loaded Vendors: " + vendorRepository.count().block());
        }
    }
}
