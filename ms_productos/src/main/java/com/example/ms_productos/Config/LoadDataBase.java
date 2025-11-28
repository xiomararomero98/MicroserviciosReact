package com.example.ms_productos.Config;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class LoadDataBase {

    // ==========================================================
    // 1) Cargar Productos Iniciales
    // ==========================================================
    @Bean
    @Order(1)
    CommandLineRunner cargarProductos(ProductoRepository productoRepository) {
        return args -> {

            if (productoRepository.count() == 0) {
                System.out.println("==> Cargando Productos...");

                Producto p1 = new Producto();
                p1.setNombre("Pack Cervezas 12u");
                p1.setDescripcion("Pack de 12 cervezas premium, ideal para fiestas.");
                p1.setPrecio(8990.0);
                p1.setStock(30);
                p1.setImagen("/img/cervezas.png");

                Producto p2 = new Producto();
                p2.setNombre("Whisky Premium 750ml");
                p2.setDescripcion("Whisky importado de alta calidad.");
                p2.setPrecio(24900.0);
                p2.setStock(10);
                p2.setImagen("/img/whiskys.png");

                Producto p3 = new Producto();
                p3.setNombre("Entrada General");
                p3.setDescripcion("Entrada general para acceso al evento nocturno.");
                p3.setPrecio(5000.0);
                p3.setStock(100);
                p3.setImagen("/img/entradas.png");

                productoRepository.save(p1);
                productoRepository.save(p2);
                productoRepository.save(p3);

                System.out.println("Productos precargados correctamente.");

            } else {
                System.out.println("Los productos ya existen.");
            }
        };
    }
}
